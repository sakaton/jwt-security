package org.sakaton.security.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * @author sakaton
 * @version created on 2020/9/5.
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class JwtSecurityBootstrap {

	public static void main(String[] args) {
		SpringApplication.run(JwtSecurityBootstrap.class, args);
	}
}
