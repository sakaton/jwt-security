package org.sakaton.security.jwt.reactive.configure.security.handler;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author sakaton
 * @version created on 2020/11/14.
 */
public class JwtAccessDeniedHandler implements ServerAccessDeniedHandler {
	@Override
	public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
		HttpStatus httpStatus = HttpStatus.FORBIDDEN;
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		DataBuffer dataBuffer = response.bufferFactory().wrap(httpStatus.getReasonPhrase().getBytes());
		return response.writeWith(Mono.just(dataBuffer));
	}
}
