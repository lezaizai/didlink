package com.didlink.security;

import java.security.Principal;

public class User implements Principal {
	private String username;

	public User() {
	}
	public User(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getName() {
		return username;
	}
}
