package com.didlink.rest.controllers;

import com.didlink.app.AppServer;
import com.didlink.db.UserDao;
import com.didlink.rest.bean.LoginAuth;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/register")
public class RegisterController implements Controller {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public LoginAuth login(LoginAuth loginAuth ) {
		System.out.println(loginAuth.getUsername());
		System.out.println(loginAuth.getPassword());
		UserDao userDao = new UserDao();

		LoginAuth user = null;

		try {
			user = userDao.findUser(loginAuth.getUsername());
		} catch (Exception e) {
			//ignore
		}

		if (user != null) {
			user.setStatus(false);
			return user;
		}

		try {
			user = userDao.saveUser(loginAuth.getUsername(), loginAuth.getPassword());
		} catch (Exception e) {
			//ignore
		}

		if (user == null) {
			user = new LoginAuth();
			user.setStatus(false);
		} else {
			final String token = AppServer.getTokenAuthenticationService().authenticateByUsernameAndPassword(loginAuth.getUsername(), loginAuth.getPassword());
			if (token == null) {
				user.setStatus(false);
			} else {
				user.setStatus(true);
				user.setToken(token);
			}
		}

		return user;
	}
}
