package com.didlink.servlets;


import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloServlet implements Servlet {

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
    }

    public ServletConfig getServletConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        PrintWriter writer = res.getWriter();
        writer.write("Hello World");
        writer.flush();
    }

    public String getServletInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public void destroy() {
        // TODO Auto-generated method stub
    }
}
