package org.sakaton.security.jwt.configure.security;

import org.sakaton.security.jwt.configure.security.access.CustomAccessDecisionManager;
import org.sakaton.security.jwt.configure.security.access.CustomSecurityMetadataSource;
import org.sakaton.security.jwt.configure.security.authentication.JwtAuthenticationFilter;
import org.sakaton.security.jwt.configure.security.authentication.JwtAuthenticationProvider;
import org.sakaton.security.jwt.configure.security.authentication.MutateRequestFilter;
import org.sakaton.security.jwt.configure.security.handler.JwtAccessDeniedHandler;
import org.sakaton.security.jwt.configure.security.handler.JwtAuthFailureHandler;
import org.sakaton.security.jwt.configure.security.handler.JwtAuthSuccessHandler;
import org.sakaton.security.jwt.configure.security.handler.JwtLogoutHandler;
import org.sakaton.security.jwt.configure.security.handler.JwtLogoutSuccessHandler;
import org.sakaton.security.jwt.configure.security.handler.SessionAuthenticationHandler;
import org.sakaton.security.jwt.configure.security.handler.TokenManageHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sakaton
 * @version created on 2019/6/27.
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 101)
@Configuration
@EnableWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityAccessConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.requestMatcher(new AntPathRequestMatcher("/api/**"));

		http = http.httpBasic().disable()
				.formLogin().disable()
				.csrf().disable()
				.logout().disable()
				.sessionManagement().disable();


		http.authorizeRequests()
				.antMatchers("/api/de/**").permitAll()
				.anyRequest().authenticated().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
			@Override
			public <O extends FilterSecurityInterceptor> O postProcess(O object) {
				object.setAccessDecisionManager(new CustomAccessDecisionManager());
				object.setSecurityMetadataSource(new CustomSecurityMetadataSource());
				return object;
			}
		});

		// 未登录
		http.exceptionHandling().authenticationEntryPoint(new JwtAuthFailureHandler()::onAuthenticationFailure)
				// 没有权限
				.accessDeniedHandler(new JwtAccessDeniedHandler());

	}


}
