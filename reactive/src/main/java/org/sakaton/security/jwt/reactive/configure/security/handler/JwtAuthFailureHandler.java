package org.sakaton.security.jwt.reactive.configure.security.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import reactor.core.publisher.Mono;

/**
 * @author sakaton
 * @version created on 2020/11/14.
 */
public class JwtAuthFailureHandler implements ServerAuthenticationFailureHandler {

	private final ServerAuthenticationEntryPoint authenticationEntryPoint;

	public JwtAuthFailureHandler(ServerAuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	@Override
	public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {

		return authenticationEntryPoint.commence(webFilterExchange.getExchange(), exception);
	}
}
