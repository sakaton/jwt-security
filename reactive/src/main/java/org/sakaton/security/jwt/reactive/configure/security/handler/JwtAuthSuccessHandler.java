package org.sakaton.security.jwt.reactive.configure.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

/**
 * @author sakaton
 * @version created on 2020/4/9.
 */
public class JwtAuthSuccessHandler implements ServerAuthenticationSuccessHandler {


	@Override
	public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {

		return webFilterExchange.getChain().filter(webFilterExchange.getExchange());
	}
}
