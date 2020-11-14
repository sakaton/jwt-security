package org.sakaton.security.jwt.reactive.configure.security.authentication;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * {@link DaoAuthenticationProvider}
 *
 * @author sakaton
 * @version created on 2020/4/9.
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {


	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
		String token = jwtAuthenticationToken.getToken();
		if (StringUtils.isNotBlank(token)) {

		} else {
			throw new BadCredentialsException(StringUtils.EMPTY);
		}
		jwtAuthenticationToken.setAuthenticated(Boolean.TRUE);
		return jwtAuthenticationToken;

	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(JwtAuthenticationToken.class);
	}
}
