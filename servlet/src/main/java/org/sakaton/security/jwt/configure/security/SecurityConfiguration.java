package org.sakaton.security.jwt.configure.security;

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
@ConditionalOnProperty(prefix = "spring.security", value = "enabled",
		havingValue = "true", matchIfMissing = true)
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
@Configuration
@EnableWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import({JwtLogoutHandler.class, JwtLogoutSuccessHandler.class})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

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
				.anyRequest().authenticated()
				.and()
				.headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
				new Header("Access-control-Allow-Origin", "*"),
				new Header("Access-Control-Expose-Headers", "Authorization"))))
				.and()
				.addFilterBefore(builderJwtAuthenticationFilter(), LogoutFilter.class);

		// 未登录
		http.exceptionHandling().authenticationEntryPoint(new JwtAuthFailureHandler()::onAuthenticationFailure)
				// 没有权限
				.accessDeniedHandler(new JwtAccessDeniedHandler());


		http.addFilterAfter(new MutateRequestFilter(), JwtAuthenticationFilter.class);

		// 新增
		// http.apply();

	}

	/**
	 * {@link DelegatingFilterProxy} -->> 代理 {@link FilterChainProxy}
	 *
	 * @param web web
	 * @throws Exception 抛出异常
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		ObjectPostProcessor<Object> object = new ObjectPostProcessor<Object>() {
			@Override
			public Object postProcess(Object object) {
				throw new IllegalStateException(
						ObjectPostProcessor.class.getName()
								+ " is a required bean. Ensure you have used @EnableWebSecurity and @Configuration");
			}
		};
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		web.ignoring().mvcMatchers("/api/product/**", "/error");

		AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(object);
		Map<Class<? extends Object>, Object> sharedObjects = new HashMap<>(16);
		HttpSecurity httpSecurity = new HttpSecurity(object, builder, sharedObjects);
		httpSecurity.requestMatcher(new AntPathRequestMatcher("/demo/**"));
		httpSecurity.addFilterBefore(builderJwtAuthenticationFilter(), LogoutFilter.class);
		// 添加 httpSecurity
		web.addSecurityFilterChainBuilder(httpSecurity);
	}

	/**
	 * 构建 核心处理token过滤器
	 *
	 * @return 返回 {@link Filter}
	 * @throws Exception Exception 抛出异常
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
		coreFilter.setSessionAuthenticationStrategy(new SessionAuthenticationHandler());
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
		return new JwtAuthenticationProvider();
	}

	/**
	 * token 管理器
	 *
	 * @return 返回结果
	 */
	@Bean
	@ConditionalOnMissingBean(TokenManageHandler.class)
	public TokenManageHandler tokenManageHandler() {
		return new TokenManageHandler.Default();
	}
}
