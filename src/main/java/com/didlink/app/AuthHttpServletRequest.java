package com.didlink.app;

import com.didlink.security.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.security.Principal;

public class AuthHttpServletRequest extends HttpServletRequestWrapper {
	private User loginUser;

	public AuthHttpServletRequest(User loginUser, HttpServletRequest request) {
		super(request);
		this.loginUser = loginUser;
	}

	@Override
	public Principal getUserPrincipal() {
		if (this.loginUser == null) {
			return super.getUserPrincipal();
		}
		return this.loginUser;
	}
}