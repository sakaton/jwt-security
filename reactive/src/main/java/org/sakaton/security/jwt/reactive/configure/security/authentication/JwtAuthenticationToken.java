package org.sakaton.security.jwt.reactive.configure.security.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * @author sakaton
 * @version created on 2020/4/9.
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private UserDetails principal;

	private String credentials;

	private String token;

	public JwtAuthenticationToken(String token) {
		super(Collections.emptyList());
		this.token = token;
	}

	public JwtAuthenticationToken(UserDetails principal, Collection<? extends GrantedAuthority> authorities) {
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

	public String getToken() {
		return token;
	}
}
