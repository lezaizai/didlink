package com.didlink.security;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserService {
	private final Map<String, User> userMap = new HashMap<String, User>();;
	public UserService() {}

	public User loadUserByUsername(final String username) throws Exception {
		final User user = userMap.get(username);
		if (user == null) {
			throw new Exception("user not found");
		}
		return user;
	}

	public void addUser(User user) {
		userMap.put(user.getUsername(), user);
	}

	protected static boolean isValidUsernameAndPassword(final String username, final String password) {
		// usually we would compare this with a database table, or call an external service
		// here just simple hard-coded compare:
		if ( Objects.equals(username,"guest") && Objects.equals(password,"welcome") ) {
			return true;
		}
		if ( Objects.equals(username,"hello") && Objects.equals(password,"world") ) {
			return true;
		}
		if ( Objects.equals(username,"world") && Objects.equals(password,"peace") ) {
			return true;
		}
		return false;
	}
}
