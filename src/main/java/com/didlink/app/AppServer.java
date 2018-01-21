package com.didlink.app;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.server.handlers.resource.ResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.util.Headers;
import org.apache.commons.lang3.tuple.Pair;
import org.jboss.resteasy.cdi.CdiInjectorFactory;
import org.jboss.resteasy.plugins.interceptors.CorsFilter;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.weld.environment.servlet.Listener;
import com.didlink.rest.controllers.ControllerFactory;
import com.didlink.security.TokenAuthenticationService;
import com.didlink.security.User;
import com.didlink.security.UserService;
import com.didlink.servlets.EchoServlet;
import com.didlink.servlets.HelloServlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppServer {
    private static Thread hook;

	private static TokenAuthenticationService tokenAuthenticationService = null;
    public static TokenAuthenticationService getTokenAuthenticationService() {
        return tokenAuthenticationService;
    }
    //private static H2JdbcDatabase jdbcDatabase = null;
    //private static JmdnsService jmdnsService = null;
   //public static H2JdbcDatabase getJdbcDatabase() {
    //    return jdbcDatabase;
    //}
    //public static JmdnsService getJmdnsService() {
	//    return jmdnsService;
    //}

	public static void main(String[] args) throws Exception {
		final UserService userService = new UserService();
		tokenAuthenticationService = new TokenAuthenticationService("tooManySecrets", userService);
        //jdbcDatabase = new H2JdbcDatabase();
        //jmdnsService = new JmdnsService();

        hook = new ShutdownHook();
        Runtime.getRuntime().addShutdownHook(hook);

        new AppServer("0.0.0.0", 5946)
                .addHandler( "/static",  createStaticResourceHandler() )
                .addHandler( "/api",     new InternalApiHandler() )
                .addHandler( "/serv",    createServletHandler() )
                .addHandler( "/rest",    createRestApiHandler( "/rest", "/api" ) )
                .start();
    }

    private final String host;
    private final int port;
    private final List<Pair<String,HttpHandler>> handlers;
    public AppServer( final String host, final Integer port ) {
        this.host = host;
        this.port = port;
        this.handlers = new ArrayList<>();
    }

    protected AppServer addHandler( final String path, final HttpHandler handler ) {
        this.handlers.add(Pair.of(path, handler));
        return this;
    }

    protected void start() {
        PathHandler pathHandlers = Handlers.path();
        for ( Pair<String,HttpHandler> handler : this.handlers ) {
            pathHandlers.addPrefixPath( handler.getLeft(), handler.getRight() );
        }

        CorsFilter filter = new CorsFilter();
        filter.setAllowedMethods("GET,POST,PUT,DELETE,OPTIONS");
        filter.getAllowedOrigins().add("*");
        filter.setAllowedHeaders("Authorization,content-type");
        ResteasyProviderFactory providerFactory = ResteasyProviderFactory.getInstance();
        providerFactory.register(filter);

        //jmdnsService.register();

        Undertow server = Undertow.builder()
            .addHttpListener(port, host)
            .setHandler(pathHandlers)
            .build();
        server.start();
    }

    private static HttpHandler createStaticResourceHandler() {
        final ResourceManager staticResources = new ClassPathResourceManager(
                AppServer.class.getClassLoader(),"static");
        final ResourceHandler resourceHandler = new ResourceHandler(staticResources);
        resourceHandler.setWelcomeFiles("index.html");
        return resourceHandler;
    }

    private static class InternalApiHandler implements HttpHandler {
        @Override
        public void handleRequest(final HttpServerExchange exchange) throws Exception {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Hello World From the InternalApiHandler");
        }
    }

    private static HttpHandler createServletHandler() throws ServletException {
        DeploymentInfo di = Servlets.deployment()
                .setClassLoader(AppServer.class.getClassLoader())
                .setContextPath("/serv")
                .setDeploymentName("Servlet APIs")
                .addServlets(
                        Servlets.servlet("helloServlet", HelloServlet.class).addMapping("/hello"),
                        Servlets.servlet("echoServlet", EchoServlet.class).addMapping("/echo")
                );
        DeploymentManager manager = Servlets.defaultContainer().addDeployment(di);
        manager.deploy();
        HttpHandler servletHandler = manager.start();
        return servletHandler;
    }

    private static HttpHandler createRestApiHandler( final String contextPath, final String appPath ) throws ServletException {
        final UndertowJaxrsServer server = new UndertowJaxrsServer();

        ResteasyDeployment deployment = new ResteasyDeployment();

        deployment.setInjectorFactoryClass(CdiInjectorFactory.class.getName());
        deployment.setApplicationClass( ControllerFactory.class.getName() );

        DeploymentInfo di = server.undertowDeployment(deployment, appPath )
                .setClassLoader(AppServer.class.getClassLoader())
                .setContextPath( contextPath )
                .setDeploymentName("Rest APIs");
	    {
		    FilterInfo filterInfo = new FilterInfo("filter", AuthFilter.class);
		    di.addFilter(filterInfo);
		    di.addFilterServletNameMapping("filter", "ResteasyServlet", DispatcherType.REQUEST);
	    }
        di.addListeners(Servlets.listener(Listener.class));
        DeploymentManager manager = Servlets.defaultContainer().addDeployment(di);

        manager.deploy();
        HttpHandler servletHandler = manager.start();
        return servletHandler;
    }

	public static class AuthFilter implements Filter
	{
		@SuppressWarnings("unused")
		private FilterConfig config;

		@Override
		public void init(FilterConfig filterConfig) throws ServletException
		{
			config = filterConfig;
		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
		{
			if (request instanceof ServletRequest)
			{
				HttpServletRequest httpRequest = HttpServletRequest.class.cast(request);
				List<String> headers = Collections.list(httpRequest.getHeaderNames());
				for (String hdr: headers) {
					System.out.println( "["+hdr+": "+httpRequest.getHeader(hdr)+"] ");
				}
				if (!"OPTIONS".equals(httpRequest.getMethod())) {
                    User loginUser = null;
                    try {
                        loginUser = AppServer.getTokenAuthenticationService().getAuthentication(httpRequest);
                    }
                    catch(Exception e) {
                        loginUser = null;
                    }

                    if ( loginUser == null ) { // user is not authenticated, we may need to throw exception
                        if ( httpRequest.getRequestURI().indexOf("/rest/api/login")<0 &&
                                httpRequest.getRequestURI().indexOf("/rest/api/register")<0 &&
                                httpRequest.getRequestURI().startsWith("/rest/api/") ) {
                            throw new ServletException("Invalid authentication when accessing "+httpRequest.getRequestURI());
                        }
                    }
                    else {
                        AuthHttpServletRequest authHttpServletRequest = new AuthHttpServletRequest(loginUser, httpRequest);
                        chain.doFilter((ServletRequest)authHttpServletRequest, response);
                        return;
                    }
                }
			}
			chain.doFilter(request, response);
		}

		@Override
		public void destroy()
		{
			config = null;
		}
	}
}
