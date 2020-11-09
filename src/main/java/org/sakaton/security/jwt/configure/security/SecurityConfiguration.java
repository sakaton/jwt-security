package org.sakaton.security.jwt.configure.security;

import org.sakaton.security.jwt.configure.security.authentication.JwtAuthFailureHandler;
import org.sakaton.security.jwt.configure.security.authentication.JwtAuthProvider;
import org.sakaton.security.jwt.configure.security.authentication.JwtAuthSuccessHandler;
import org.sakaton.security.jwt.configure.security.authentication.JwtAuthenticationFilter;
import org.sakaton.security.jwt.configure.security.authentication.MutateRequestFilter;
import org.sakaton.security.jwt.configure.security.authentication.SessionAuthStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

import javax.servlet.Filter;
import java.util.Arrays;

/**
 * @author sakaton
 * @version created on 2019/6/27.
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http = http.httpBasic().disable()
				.formLogin().disable()
				.csrf().disable()
				.logout().disable()
				.sessionManagement().disable();


		http.authorizeRequests()
				.antMatchers("/api/de/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
				new Header("Access-control-Allow-Origin", "*"),
				new Header("Access-Control-Expose-Headers", "Authorization"))))
				.and()
				.addFilterBefore(builderJwtAuthenticationFilter(), LogoutFilter.class)
				.exceptionHandling().authenticationEntryPoint(new JwtAuthFailureHandler()::onAuthenticationFailure);

		http.addFilterAfter(new MutateRequestFilter(), JwtAuthenticationFilter.class);

	}

	@Override
	public void configure(WebSecurity web) throws Exception {

	}

	/**
	 * 构建 核心处理token过滤器
	 *
	 * @return 返回 {@link Filter}
	 * @throws Exception
	 */
	private Filter builderJwtAuthenticationFilter() throws Exception {
		AndRequestMatcher andRequestMatcher = new AndRequestMatcher(new AntPathRequestMatcher(
				"/api/app/order/**"), new RequestHeaderRequestMatcher(HttpHeaders.AUTHORIZATION));

		JwtAuthenticationFilter coreFilter = new JwtAuthenticationFilter(andRequestMatcher);
		// 失败回调
		coreFilter.setAuthenticationFailureHandler(new JwtAuthFailureHandler());
		// 成功回调
		coreFilter.setAuthenticationSuccessHandler(new JwtAuthSuccessHandler());
		coreFilter.setAuthenticationManager(authenticationManager());
		coreFilter.setContinueChainBeforeSuccessfulAuthentication(true);
		coreFilter.setSessionAuthenticationStrategy(new SessionAuthStrategy());
		return coreFilter;
	}


	/**
	 * 默认处理器
	 * {@link ProviderManager}
	 * 处理 {@link Authentication}
	 *
	 * @return 返回处理
	 */
	@Bean
	public AuthenticationProvider jwtAuthProvider() {
		return new JwtAuthProvider();
	}
}
