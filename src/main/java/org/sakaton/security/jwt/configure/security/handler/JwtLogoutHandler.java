package org.sakaton.security.jwt.configure.security.handler;


import org.sakaton.security.jwt.configure.security.authentication.JwtAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理登出操作
 *
 * @author sakaton
 * @version created on 2020/11/24.
 */
public class JwtLogoutHandler extends SecurityContextLogoutHandler {

	private final static Logger log = LoggerFactory.getLogger(JwtLogoutHandler.class);

	@Autowired
	private TokenManageHandler tokenManageHandler;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		if (authentication instanceof JwtAuthenticationToken) {
			String token = JwtAuthenticationToken.class.cast(authentication).getToken();
			boolean flag = tokenManageHandler.removeToken(token);
			log.debug("token remove token:{}, result :{}", token, flag);
		}
		super.logout(request, response, authentication);
	}
}
