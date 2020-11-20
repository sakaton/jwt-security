package org.sakaton.security.jwt.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.handler.FilteringWebHandler;

/**
 *  {@link FilteringWebHandler}
 *  -- {@link DispatcherHandler}
 *  -- -- {@code org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping}
 *  -- -- -- {@link FilteringWebHandler}
 *
 * @author sakaton
 * @version created on 2020/11/11.
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class JwtSecurityBootstrap {
	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext =
				SpringApplication.run(JwtSecurityBootstrap.class,args);

		System.out.println(applicationContext);
	}
}
