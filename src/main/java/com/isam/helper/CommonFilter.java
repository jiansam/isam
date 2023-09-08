package com.isam.helper;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class CommonFilter implements Filter {
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		request.setCharacterEncoding("UTF-8");
		chain.doFilter(request, resp);
	}

	@SuppressWarnings("unused")
	private FilterConfig config = null;
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
		
		//2019.6.6.dasin : set application attributes
		ApplicationAttributeHelper.getInvestNoToName(config.getServletContext());
	}

	public void destroy() {
		this.config = null;
	}
}
