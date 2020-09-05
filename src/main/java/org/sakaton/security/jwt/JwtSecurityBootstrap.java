package org.sakaton.security.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sakaton
 * @version created on 2020/9/5.
 */
@SpringBootApplication
public class JwtSecurityBootstrap {

	public static void main(String[] args) {
		SpringApplication.run(JwtSecurityBootstrap.class,args);
	}
}
