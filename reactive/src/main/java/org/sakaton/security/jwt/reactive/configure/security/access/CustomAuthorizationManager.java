package org.sakaton.security.jwt.reactive.configure.security.access;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

/**
 * @author zhengshijun
 * @version created on 2020/11/20.
 */
public class CustomAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
	@Override
	public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext object) {


		return Mono.just(new AuthorizationDecision(true));
	}
}
