package org.sakaton.security.jwt.configure.security.authentication;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 *    可变的请求参数处理过滤器
 *
 * @author sakaton
 * @version created on 2020/9/22.
 */
public class MutateRequestFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		RequestWrapper requestWrapper = new RequestWrapper(request);

		filterChain.doFilter(requestWrapper, response);
	}

	@Override
	protected String getFilterName() {
		return MutateRequestFilter.class.getName();
	}
}
