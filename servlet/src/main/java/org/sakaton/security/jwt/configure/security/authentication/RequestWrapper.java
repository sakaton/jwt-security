package org.sakaton.security.jwt.configure.security.authentication;

import com.google.common.collect.Lists;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServletServerHttpRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * @author sakaton
 * @version created on 2020/9/9.
 */
public class RequestWrapper extends HttpServletRequestWrapper {

	private final HttpHeaders httpHeaders;

	public RequestWrapper(HttpServletRequest request) {
		super(request);
		httpHeaders = new ServletServerHttpRequest(request).getHeaders();
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return Collections.enumeration(httpHeaders.keySet());
	}

	@Override
	public String getHeader(String name) {

		return httpHeaders.getFirst(name);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {

		List<String> list = httpHeaders.get(name);
		if (Objects.isNull(list)){
			return Collections.emptyEnumeration();
		}
		return Collections.enumeration(list);
	}

	public RequestWrapper addHeader(String name, String value) {
		List<String> values = httpHeaders.get(name);
		if (Objects.isNull(values)) {
			values = new ArrayList<>();
		}
		values.add(value);
		httpHeaders.put(name, values);
		return this;
	}


	public RequestWrapper replaceHeader(String name, Object value) {
		if (Objects.nonNull(value)) {
			httpHeaders.put(name, Lists.newArrayList());
		}
		return this;
	}

	public RequestWrapper removeHeader(String name) {
		httpHeaders.remove(name);
		return this;
	}


}
