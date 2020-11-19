package org.sakaton.security.jwt;

import org.apache.catalina.core.ApplicationFilterChain;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter;
import org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.boot.web.servlet.filter.OrderedHttpPutFormContentFilter;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

/**
 *
 *  {@link OrderedCharacterEncodingFilter}
 *     {@link ApplicationFilterChain}
 *        {@link WebMvcMetricsFilter}
 *           {@link ApplicationFilterChain}
 *              {@link OrderedHiddenHttpMethodFilter}
 *                 {@link ApplicationFilterChain}
 *                    {@link OrderedHttpPutFormContentFilter}
 *                       {@link ApplicationFilterChain}
 *                          {@link OrderedRequestContextFilter}
 *                            {@link ApplicationFilterChain}   分歧点
 *                               {@link DelegatingFilterProxy}
 *                                 {@link org.springframework.security.web.FilterChainProxy}
 *
 *
 *  {@link OrderedCharacterEncodingFilter}
 *     {@link ApplicationFilterChain}
 *        {@link WebMvcMetricsFilter}
 *           {@link ApplicationFilterChain}
 *              {@link OrderedHiddenHttpMethodFilter}
 *                 {@link ApplicationFilterChain}
 *                    {@link OrderedHttpPutFormContentFilter}
 *                       {@link ApplicationFilterChain}
 *                          {@link OrderedRequestContextFilter}
 *                            {@link ApplicationFilterChain}   分歧点
 *                              {@link HttpTraceFilter}
 *                                {@link org.apache.tomcat.websocket.server.WsFilter}
 * @author sakaton
 * @version created on 2020/9/5.
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class JwtSecurityBootstrap {

	public static void main(String[] args) {
		SpringApplication.run(JwtSecurityBootstrap.class, args);
	}
}
