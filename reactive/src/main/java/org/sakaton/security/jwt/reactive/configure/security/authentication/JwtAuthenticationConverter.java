package org.sakaton.security.jwt.reactive.configure.security.authentication;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * @author sakaton
 * @version created on 2020/11/14.
 */
public class JwtAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {
	@Override
	public Mono<Authentication> apply(ServerWebExchange serverWebExchange) {
		HttpRequest request = serverWebExchange.getRequest();

		String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if(authorization == null) {
			return Mono.empty();
		}
		return Mono.just(new JwtAuthenticationToken(authorization));
	}
}
