package org.sakaton.security.jwt.configure.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhengshijun
 * @version created on 2020/9/5.
 */
public class JwtCoreFilter extends AbstractAuthenticationProcessingFilter {

	public JwtCoreFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	public JwtCoreFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

		Authentication authResult =getAuthenticationManager().authenticate(new JwtAuthToken(1));

		return authResult;
	}
}
