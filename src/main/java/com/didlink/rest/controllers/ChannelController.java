package com.didlink.rest.controllers;

import com.didlink.db.ChannelDao;
import com.didlink.rest.bean.AnyBean;
import com.didlink.rest.bean.Channel;
import com.didlink.rest.bean.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import java.util.Arrays;
import java.util.List;

@Path("/channel")
public class ChannelController implements Controller {

    Logger log = LoggerFactory.getLogger( ChannelController.class );

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/add")
    public Channel add(@Context SecurityContext securityContext, Channel channel){
        ChannelDao channelDao = new ChannelDao();
        System.out.println( "!!!"+securityContext.getUserPrincipal().getName() );

        try {
            Channel channel1 = channelDao.add(channel);
            System.out.println(channel1.toString());
            return channel1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get")
    public List<Channel> get(@Context SecurityContext securityContext) {
	    System.out.println( "!!!"+securityContext.getUserPrincipal().getName() );

	    ChannelDao channelDao = new ChannelDao();

	    try {
            return channelDao.findByName("a");
        } catch (Exception ex) {
	        ex.printStackTrace();
	        return null;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{chid}")
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
