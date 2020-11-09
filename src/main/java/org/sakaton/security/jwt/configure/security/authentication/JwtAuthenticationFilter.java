package org.sakaton.security.jwt.configure.security.authentication;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sakaton
 * @version created on 2020/9/5.
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	/**
	 * {@link NegatedRequestMatcher} 取反
	 * <p>
	 * {@link OrRequestMatcher}
	 * <p>
	 * {@link AndRequestMatcher}
	 * <p>
	 * {@link AntPathRequestMatcher}
	 */
	private RequestMatcher ignoreRequestMatcher;

	public JwtAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	public JwtAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

		Authentication authResult = getAuthenticationManager().authenticate(new JwtAuthenticationToken(authorization));

		return authResult;
	}
}
