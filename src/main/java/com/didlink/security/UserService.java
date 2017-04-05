package com.didlink.security;


import com.didlink.db.UserDao;
import com.didlink.rest.bean.LoginAuth;

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
		UserDao userDao = new UserDao();
		LoginAuth user = null;

		try {
			user = userDao.findUser(username);
		} catch (Exception e) {
			//ignore
		}

		if (user != null) {
			if (password.equals(user.getPassword())) {
				return true;
			} else {
				return false;
			}
		}

		// usually we would compare this with a database table, or call an external service
		// here just simple hard-coded compare:
		if ( Objects.equals(username,"guest") && Objects.equals(password,"guest") ) {
			return true;
		}
		return false;
	}
}
