package org.sakaton.security.jwt.configure.security.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出操作成功 处理器
 *
 * @author sakaton
 * @version created on 2020/11/10.
 */
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		response.addHeader("context-type", MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.OK.value());
		response.getWriter().write(Boolean.TRUE.toString());
	}
}
