package org.sakaton.security.jwt.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhengshijun
 * @version created on 2020/11/11.
 */
@SpringBootApplication
public class JwtSecurityBootstrap {
	public static void main(String[] args) {
		SpringApplication.run(JwtSecurityBootstrap.class,args);
	}
}
