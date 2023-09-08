package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.isam.service.ProjectOnlineSrcService;

public class ProjectReportConfirmNoMatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProjectOnlineSrcService poSer;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		poSer=new ProjectOnlineSrcService();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		String temp=request.getParameter("no");
		
		if(temp==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('至少需選擇一項!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/project/listrpconfirm.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		poSer.insert(temp);
		
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/project/listrpconfirm.jsp?type=3");
	}
}
