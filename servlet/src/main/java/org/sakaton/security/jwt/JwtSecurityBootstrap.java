package org.sakaton.security.jwt;

import org.apache.catalina.core.ApplicationFilterChain;
import org.apache.catalina.core.StandardContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter;
import org.springframework.boot.actuate.web.trace.servlet.HttpTraceFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.boot.web.servlet.filter.OrderedHttpPutFormContentFilter;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.ServletContainerInitializer;

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
 *
 *
 *
 *
 *  spring boot 启动 加载Servlet 容器过程
 *  {@link ServletWebServerApplicationContext#onRefresh()}
 *
 *     {@link ServletWebServerApplicationContext#createWebServer()}
 *        {@link ServletWebServerApplicationContext#getSelfInitializer()} -> 获取到目前该接口的所有实现 {@link ServletContextInitializer}
 *           beans
 *             {@link org.springframework.boot.web.servlet.FilterRegistrationBean}
 *               {@link OrderedCharacterEncodingFilter}
 *               {@link WebMvcMetricsFilter}
 *               {@link OrderedHiddenHttpMethodFilter}
 *               {@link OrderedHttpPutFormContentFilter}
 *               {@link OrderedRequestContextFilter}
 *             {@link org.springframework.boot.web.servlet.DelegatingFilterProxyRegistrationBean}
 *               {@link DelegatingFilterProxy}
 *                 {@link FilterChainProxy} 被代理过滤器
 *               {@link HttpTraceFilter}
 *             {@link org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean}
 *             {@link org.springframework.boot.actuate.endpoint.web.ServletEndpointRegistrar}
 *
 *        {@link TomcatServletWebServerFactory#getWebServer(org.springframework.boot.web.servlet.ServletContextInitializer...)} 创建容器
 *          {@link TomcatServletWebServerFactory#prepareContext(org.apache.catalina.Host, org.springframework.boot.web.servlet.ServletContextInitializer[])}
 *            {@link TomcatServletWebServerFactory#configureContext(org.apache.catalina.Context, org.springframework.boot.web.servlet.ServletContextInitializer[])}
 *              || 创建  {@link org.springframework.boot.web.embedded.tomcat.TomcatStarter} 实现了 -> {@link ServletContainerInitializer}
 *              这是 已经 把准备好的 {@link ServletContainerInitializer} 给了 TomcatStarter 然后 TomcatStarter 设置到了 {@link org.springframework.boot.web.embedded.tomcat.TomcatEmbeddedContext#setStarter(org.springframework.boot.web.embedded.tomcat.TomcatStarter)}
 *              {@link org.springframework.boot.web.embedded.tomcat.TomcatEmbeddedContext}实现了{@link StandardContext}
 *          {@link TomcatServletWebServerFactory#getTomcatWebServer(org.apache.catalina.startup.Tomcat)}
 *             {@link TomcatWebServer#initialize()} 在初始化的时候启动 tomcat
 *               {@link StandardContext#startInternal()} 进入 5225 行遍历 {@link ServletContainerInitializer} 实现
 *                 {@link org.springframework.boot.web.embedded.tomcat.TomcatStarter#onStartup(java.util.Set, javax.servlet.ServletContext)} 此时进入了 TomcatStarter
 *
 *
 *
 * javax.servlet.ServletContainerInitializer
 *      org.springframework.web.SpringServletContainerInitializer
 * 	     org.springframework.web.WebApplicationInitializer
 *
 * 	    org.springframework.boot.web.embedded.tomcat.TomcatStarter
 * 	     org.springframework.boot.web.servlet.ServletContextInitializer
 * 		     org.springframework.boot.web.servlet.FilterRegistrationBean
 * 			 org.springframework.boot.web.servlet.FilterRegistrationBean
 * 			 org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean
 *
 *
 *
 *
 * @author sakaton
 * @version created on 2020/9/5.
 */
@SuppressWarnings("JavadocReference")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class JwtSecurityBootstrap {

	public static void main(String[] args) {
		SpringApplication.run(JwtSecurityBootstrap.class, args);
	}
}
