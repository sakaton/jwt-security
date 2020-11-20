package org.sakaton.security.jwt.configure.security.access;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;

/**
 * {@link DefaultFilterInvocationSecurityMetadataSource}
 *
 * @author sakaton
 * @version created on 2019/6/27.
 */
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private AntPathMatcher antPathMatcher = new AntPathMatcher();


	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

		if (object instanceof FilterInvocation) {
			FilterInvocation invocation = FilterInvocation.class.cast(object);
			String uri = invocation.getRequestUrl();
			String method = invocation.getHttpRequest().getMethod();

		}
		return SecurityConfig.createList("ROLE_USER");
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}
}
