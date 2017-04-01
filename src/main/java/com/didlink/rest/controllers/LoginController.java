package com.didlink.rest.controllers;

import com.didlink.app.AppServer;
import com.didlink.rest.bean.LoginAuth;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;


@Path("/login")
public class LoginController implements Controller {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public LoginAuth login(LoginAuth loginAuth ) {
		System.out.println(loginAuth.getUsername());
		System.out.println(loginAuth.getPassword());

		final String token = AppServer.getTokenAuthenticationService().authenticateByUsernameAndPassword(loginAuth.getUsername(), loginAuth.getPassword());

		if (token == null) {
			loginAuth.setStatus(false);
		} else {
			loginAuth.setStatus(true);
		}

		loginAuth.setPassword("");
		loginAuth.setToken(token);

		System.out.println(loginAuth.toString());

		return loginAuth;
	}
}
