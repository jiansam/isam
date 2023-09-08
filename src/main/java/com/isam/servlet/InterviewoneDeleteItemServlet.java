package com.isam.servlet;


import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.UserMember;
import com.isam.service.InterviewoneFileService;
import com.isam.service.InterviewoneService;


public class InterviewoneDeleteItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private InterviewoneService ioSer;
	private InterviewoneFileService iofSer;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ioSer = new InterviewoneService();
		iofSer = new InterviewoneFileService();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserMember user = (UserMember) session.getAttribute("userInfo");
		String updateuser = user.getIdMember();

		String year=request.getParameter("year");
		String qNo=request.getParameter("serno");

		ioSer.unable(qNo,updateuser);
		iofSer.deleteByQNo(qNo);
		
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/interviewone/showiolist.jsp?action=delete&year="+year);
	}
	
}
