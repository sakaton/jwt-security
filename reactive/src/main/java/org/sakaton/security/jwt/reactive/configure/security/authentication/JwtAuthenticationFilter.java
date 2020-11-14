package org.sakaton.security.jwt.reactive.configure.security.authentication;

import org.sakaton.security.jwt.reactive.configure.security.handler.JwtAuthFailureHandler;
import org.sakaton.security.jwt.reactive.configure.security.handler.JwtAuthSuccessHandler;
import org.sakaton.security.jwt.reactive.configure.security.handler.JwtAuthenticationEntryPoint;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * {@link AuthenticationWebFilter}
 *
 * @author sakaton
 * @version created on 2020/11/14.
 */
public class JwtAuthenticationFilter implements WebFilter {

	private final ServerWebExchangeMatcher requiresAuthenticationMatcher;

	private final ReactiveAuthenticationManager authenticationManager;

	private final Function<ServerWebExchange, Mono<Authentication>> authenticationConverter = new JwtAuthenticationConverter();

	private final ServerAuthenticationFailureHandler authenticationFailureHandler = new JwtAuthFailureHandler(new JwtAuthenticationEntryPoint());

	private final ServerAuthenticationSuccessHandler authenticationSuccessHandler = new JwtAuthSuccessHandler();

	public JwtAuthenticationFilter(ServerWebExchangeMatcher requiresAuthenticationMatcher, ReactiveAuthenticationManager authenticationManager) {
		this.requiresAuthenticationMatcher = requiresAuthenticationMatcher;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

		return this.requiresAuthenticationMatcher.matches(exchange)
				.filter(ServerWebExchangeMatcher.MatchResult::isMatch)
				.flatMap(matchResult -> this.authenticationConverter.apply(exchange))
				.switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
				.flatMap(token -> authenticate(exchange, chain, token)).then(chain.filter(exchange));
	}

	private Mono<Void> authenticate(ServerWebExchange exchange,
			WebFilterChain chain, Authentication token) {
		WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, chain);
		return this.authenticationManager.authenticate(token)
				.flatMap(authentication -> onAuthenticationSuccess(authentication, webFilterExchange))
				.onErrorResume(AuthenticationException.class, e -> this.authenticationFailureHandler
						.onAuthenticationFailure(webFilterExchange, e));
	}

	private Mono<Void> onAuthenticationSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {

		return authenticationSuccessHandler
				.onAuthenticationSuccess(webFilterExchange, authentication)
				.subscriberContext(ReactiveSecurityContextHolder.withAuthentication(authentication));
	}

}
