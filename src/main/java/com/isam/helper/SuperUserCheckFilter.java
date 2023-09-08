package com.isam.helper;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.UserMember;

public class SuperUserCheckFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession();
		UserMember bean = (UserMember) session.getAttribute("userInfo");
		if(!bean.getGroupId().equals("super")){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您沒有查閱網站使用者的權限，請確認帳號後，重新登入!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/logout.jsp';</script>");
			out.flush();
			out.close();
			return;
		}else{
			chain.doFilter(request, response);
		}
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
