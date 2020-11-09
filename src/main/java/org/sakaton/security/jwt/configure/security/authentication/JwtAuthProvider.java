package org.sakaton.security.jwt.configure.security.authentication;

import org.sakaton.security.jwt.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author sakaton
 * @version created on 2020/4/9.
 */
public class JwtAuthProvider implements AuthenticationProvider {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("");

		if (userDetails != null) {
			//throw new AccountExpiredException("");
		}
		// BadCredentialsException 密码错误
		authentication.setAuthenticated(Boolean.TRUE);
		return authentication;
		//return new JwtAuthToken(userDetails,userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(JwtAuthenticationToken.class);
	}
}
