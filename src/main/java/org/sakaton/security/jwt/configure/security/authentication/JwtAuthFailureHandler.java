package org.sakaton.security.jwt.configure.security.authentication;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author sakaton
 * @version created on 2020/4/9.
 */
public class JwtAuthFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
		response.setStatus(httpStatus.value());
		response.getWriter().write(httpStatus.getReasonPhrase());

	}
}
