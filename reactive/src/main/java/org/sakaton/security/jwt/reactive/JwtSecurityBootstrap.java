package org.sakaton.security.jwt.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author sakaton
 * @version created on 2020/11/11.
 */
@SpringBootApplication
public class JwtSecurityBootstrap {
	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(JwtSecurityBootstrap.class,args);

		System.out.println(applicationContext);
	}
}
