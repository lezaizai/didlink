package com.didlink.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.servlet.http.HttpServletRequest;

public class TokenAuthenticationService {
	public static final String AUTH_HEADER_NAME = "Authorization";
	public static final String AUTH_TOKEN_PREFIX = "Bearer ";

	private final String secret;
	private final UserService userService;

	public TokenAuthenticationService(String secret, UserService userService) {
		this.secret = secret;
		this.userService = userService;
	}


	public String authenticateByUsernameAndPassword(final String username, final String password) {
		if ( !UserService.isValidUsernameAndPassword(username, password) ) {
			return null;
		}
		final User loginUser = new User(username);
		final String token = createTokenForUser(loginUser);
		if ( token!=null && !token.isEmpty() ) {
			userService.addUser(loginUser);
		}
		return token;
	}

	public User getAuthentication(final HttpServletRequest request) throws Exception {
		String token = request.getHeader(AUTH_HEADER_NAME);
		if (token != null && !token.isEmpty() && token.startsWith(AUTH_TOKEN_PREFIX)) {
			token = token.substring(AUTH_TOKEN_PREFIX.length());
			if (!token.isEmpty()) {
				return parseUserFromToken(token);
			}
		}
		return null;
	}



	public String createTokenForUser(User user) {
		return Jwts.builder().setSubject(user.getUsername()).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	private User parseUserFromToken(String token) throws Exception {
		String username = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
		return userService.loadUserByUsername(username);
	}
}
