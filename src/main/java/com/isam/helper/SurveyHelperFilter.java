package com.isam.helper;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.service.SurveyHelp;

public class SurveyHelperFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession();
		String flag = (String) session.getAttribute("SurveyHelper");
		if(flag==null||flag.isEmpty()){
			SurveyHelp ser = new SurveyHelp();
			session.setAttribute("indLevel1", ser.getIndLevel1());
			session.setAttribute("indLevel2", ser.getIndLevel2());
			session.setAttribute("regionLev1", ser.getRegionLev1());
			session.setAttribute("regionLev2", ser.getRegionLev2());
			session.setAttribute("regionLev3", ser.getRegionLev3());
			session.setAttribute("qTypeName", ser.getqType());
			session.setAttribute("qTypeYear", ser.getqTypeXYear());
			session.setAttribute("topicLev1", ser.getTopicLev1());
			session.setAttribute("topicLev2", ser.getTopicLev2());
			session.setAttribute("topicMap", ser.getTopicMap());
			session.setAttribute("SurveyHelper","get");
		}
		chain.doFilter(request, response);
	}
	@SuppressWarnings("unused")
	private FilterConfig config = null;
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}
	public void destroy() {
		this.config = null;
	}
}
