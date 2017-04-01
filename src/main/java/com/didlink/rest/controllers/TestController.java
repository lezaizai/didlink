package com.didlink.rest.controllers;

import com.didlink.rest.bean.AnyBean;
import com.didlink.rest.bean.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.List;

@Path("/mt")
public class TestController implements Controller {

    Logger log = LoggerFactory.getLogger( TestController.class );

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test(@Context SecurityContext securityContext){
	    System.out.println( "!!!"+securityContext.getUserPrincipal().getName() );
        return "Hello Any Test";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/json")
    public AnyBean json(@Context SecurityContext securityContext) {
	    System.out.println( "!!!"+securityContext.getUserPrincipal().getName() );
	    return new AnyBean();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/comment")
    public List<Comment> comments(){
        return Arrays.asList(
            new Comment( "A", "a" ),
            new Comment( "B", "b" ),
            new Comment( "C", "c" )
        );
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/comment")
    public List<Comment> comments( Comment c ) {
        log.warn( "c={}", c );
        return Arrays.asList(
                new Comment( "A", "a" ),
                new Comment( "B", "b" ),
                new Comment( "C", "c" ),
                c
        );
    }
}
