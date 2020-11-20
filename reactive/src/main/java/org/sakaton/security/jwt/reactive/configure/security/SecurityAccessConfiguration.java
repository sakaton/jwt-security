package org.sakaton.security.jwt.reactive.configure.security;

import org.sakaton.security.jwt.reactive.configure.security.access.CustomAuthorizationManager;
import org.sakaton.security.jwt.reactive.configure.security.handler.JwtAccessDeniedHandler;
import org.sakaton.security.jwt.reactive.configure.security.handler.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author zhengshijun
 * @version created on 2020/11/20.
 */
@Configuration
//@EnableWebFluxSecurity
public class SecurityAccessConfiguration {
	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

		http = http.httpBasic().disable()
				.formLogin().disable()
				.csrf().disable()
				.logout().disable();


		ServerHttpSecurity.AuthorizeExchangeSpec spec = http.authorizeExchange();
		spec.pathMatchers("/api/de/**").permitAll();

		spec.and().headers();

		spec.pathMatchers("/api/app/order/**").access(new CustomAuthorizationManager()).anyExchange().authenticated();

		http.exceptionHandling()
				// 未登录
				.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
				// 没有权限
				.accessDeniedHandler(new JwtAccessDeniedHandler());
		return http.build();
	}
}
