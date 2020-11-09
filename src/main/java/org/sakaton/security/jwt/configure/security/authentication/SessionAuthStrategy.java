package org.sakaton.security.jwt.configure.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhengshijun
 * @version created on 2020/4/13.
 */
public class SessionAuthStrategy implements SessionAuthenticationStrategy {
	@Override
	public void onAuthentication(Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws SessionAuthenticationException {

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
