package org.sakaton.security.jwt.configure.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author sakaton
 * @version created on 2020/4/9.
 */
public class JwtAuthToken extends AbstractAuthenticationToken {

	private UserDetails principal;

	private String credentials;

	private String token;

	public JwtAuthToken(String token) {
		super(Collections.emptyList());
		this.token = token;
	}

	public JwtAuthToken(UserDetails principal, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;

	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}
}
