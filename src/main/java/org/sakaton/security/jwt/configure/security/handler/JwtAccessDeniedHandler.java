package org.sakaton.security.jwt.configure.security.handler;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zhengshijun
 * @version created on 2020/11/9.
 */
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		HttpStatus httpStatus = HttpStatus.FORBIDDEN;
		response.setStatus(httpStatus.value());
		response.getWriter().write(httpStatus.getReasonPhrase());
	}
}
