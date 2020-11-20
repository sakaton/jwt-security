package org.sakaton.security.jwt.configure.security.access;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import java.util.Collection;
import java.util.Objects;

/**
 *   {@link ExceptionTranslationFilter#handleSpringSecurityException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain, java.lang.RuntimeException)}
 * @author sakaton
 * @version created on 2019/6/27.
 */
@SuppressWarnings("JavadocReference")
public class CustomAccessDecisionManager implements AccessDecisionManager {


	/**
	 * 认证的
	 *
	 * @param authentication   认证
	 * @param object           请求信息
	 * @param configAttributes 本次请求所需的url
	 * @throws AccessDeniedException               没有权限
	 * @throws InsufficientAuthenticationException 缺乏
	 */
	@Override
	public void decide(Authentication authentication,
			Object object,
			Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {

		System.out.println("configAttributes"+configAttributes);
		if (Objects.nonNull(object)){
			throw new AccessDeniedException("");
		}
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}
