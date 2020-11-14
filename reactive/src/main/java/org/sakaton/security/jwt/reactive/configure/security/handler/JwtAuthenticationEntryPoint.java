package org.sakaton.security.jwt.reactive.configure.security.handler;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author sakaton
 * @version created on 2020/11/14.
 */
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
	@Override
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		DataBuffer dataBuffer = response.bufferFactory().wrap(httpStatus.getReasonPhrase().getBytes());
		return response.writeWith(Mono.just(dataBuffer));
	}
}
