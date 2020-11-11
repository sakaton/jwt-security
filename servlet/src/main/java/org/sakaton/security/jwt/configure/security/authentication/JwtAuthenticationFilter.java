package org.sakaton.security.jwt.configure.security.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author sakaton
 * @version created on 2020/9/5.
 */
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final static Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	/**
	 * {@link NegatedRequestMatcher} 取反
	 * <p>
	 * {@link OrRequestMatcher}
	 * <p>
	 * {@link AndRequestMatcher}
	 * <p>
	 * {@link AntPathRequestMatcher}
	 * <p>
	 * {@link AnyRequestMatcher}
	 */
	private final RequestMatcher ignoreRequestMatcher = AnyRequestMatcher.INSTANCE;

	public JwtAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	public JwtAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

		Authentication authResult;
		try {
			authResult = getAuthenticationManager().authenticate(new JwtAuthenticationToken(authorization));
		} catch (AuthenticationException e) {
			log.warn("验证失败", e);
			if (Objects.nonNull(ignoreRequestMatcher) && ignoreRequestMatcher.matches(request)) {
				return new AnonymousAuthenticationToken("key", "anonymous", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
			} else {
				throw e;
			}
		}
		return authResult;
	}
}
