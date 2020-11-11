package org.sakaton.security.jwt.reactive.configure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author zhengshijun
 * @version created on 2020/11/11.
 */
@Configuration
public class SecurityConfiguration {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
		http.authorizeExchange();

		return http.build();
	}
}
