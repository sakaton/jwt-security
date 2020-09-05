package org.sakaton.security.jwt.configure;

import org.sakaton.security.jwt.configure.authentication.JwtAuthFailureHandler;
import org.sakaton.security.jwt.configure.authentication.JwtAuthProvider;
import org.sakaton.security.jwt.configure.authentication.JwtAuthSuccessHandler;
import org.sakaton.security.jwt.configure.authentication.JwtCoreFilter;
import org.sakaton.security.jwt.configure.authentication.SessionAuthStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

import java.util.Arrays;

/**
 * @author sakaton
 * @version created on 2019/6/27.
 */
@Order(Ordered.HIGHEST_PRECEDENCE+100)
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http = http.httpBasic().disable()
				.formLogin().disable()
				.csrf().disable()
				.logout().disable()
				.sessionManagement().disable();

		AndRequestMatcher andRequestMatcher = new AndRequestMatcher(new AntPathRequestMatcher(
				"/api/app/order/**"),new RequestHeaderRequestMatcher(HttpHeaders.AUTHORIZATION));

		JwtCoreFilter coreFilter = new JwtCoreFilter(andRequestMatcher);
		coreFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
		coreFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		coreFilter.setAuthenticationManager(authenticationManager());
		coreFilter.setContinueChainBeforeSuccessfulAuthentication(true);
		coreFilter.setSessionAuthenticationStrategy(new SessionAuthStrategy());

		http.authorizeRequests()
				.antMatchers("/api/de/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
				new Header("Access-control-Allow-Origin","*"),
				new Header("Access-Control-Expose-Headers","Authorization"))))
				.and()
				.addFilterBefore(coreFilter, LogoutFilter.class)
				//.authenticationProvider(jwtAuthProvider())
				.exceptionHandling().authenticationEntryPoint(authenticationFailureHandler::onAuthenticationFailure);

	}




	@Bean
	public AuthenticationSuccessHandler authSuccessHandler(){
		return new JwtAuthSuccessHandler();
	}


	@Bean
	public AuthenticationProvider jwtAuthProvider() {
		return new JwtAuthProvider();
	}


	@Bean
	public AuthenticationFailureHandler authFailureHandler(){
		return new JwtAuthFailureHandler();
	}
}
