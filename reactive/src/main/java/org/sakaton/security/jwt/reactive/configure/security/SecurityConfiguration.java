package org.sakaton.security.jwt.reactive.configure.security;

import org.sakaton.security.jwt.reactive.configure.security.authentication.JwtAuthenticationFilter;
import org.sakaton.security.jwt.reactive.configure.security.authentication.JwtAuthenticationProvider;
import org.sakaton.security.jwt.reactive.configure.security.handler.JwtAccessDeniedHandler;
import org.sakaton.security.jwt.reactive.configure.security.handler.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerAdapter;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.WebFilter;

import java.util.List;

/**
 * {@link WebFilter}
 *
 * @author sakaton
 * @version created on 2020/11/11.
 */
@ConditionalOnProperty(prefix = "spring.security", value = "enabled",
		havingValue = "true", matchIfMissing = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Configuration
public class SecurityConfiguration {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,AuthenticationManager authenticationManager) {

		http = http.httpBasic().disable()
				.formLogin().disable()
				.csrf().disable()
				.logout().disable();


		ServerHttpSecurity.AuthorizeExchangeSpec spec = http.authorizeExchange();
		spec.pathMatchers("/api/de/**").permitAll();
		spec.anyExchange().authenticated();
		spec.and().headers();

		spec.and().addFilterAt(builderJwtAuthenticationFilter(new ReactiveAuthenticationManagerAdapter(authenticationManager)), SecurityWebFiltersOrder.LOGOUT);

		http.exceptionHandling()
				// 未登录
				.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
		        // 没有权限
		        .accessDeniedHandler(new JwtAccessDeniedHandler());
		return http.build();
	}

	public WebFilter builderJwtAuthenticationFilter(ReactiveAuthenticationManager authenticationManager){

		return new JwtAuthenticationFilter(ServerWebExchangeMatchers.pathMatchers("/api/app/order/**"),authenticationManager);
	}




	@Bean
	@Autowired
	public AuthenticationManager providerManager(ObjectProvider<List<AuthenticationProvider>> providers){

		return new ProviderManager(providers.getObject());

	}

	@Bean
	public AuthenticationProvider authenticationProvider(){
		return new JwtAuthenticationProvider();
	}
}
