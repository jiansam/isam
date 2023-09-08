package com.isam.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.helper.DataUtil;
import com.isam.service.ProjectOnlineSrcService;
import com.isam.service.ProjectReportService;

public class ProjectReportConfirmListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProjectReportService ser;
	private ProjectOnlineSrcService poSer;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ser= new ProjectReportService();
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
		HttpSession session = request.getSession();
		session.removeAttribute("prclist");
		session.removeAttribute("notMatchlist");
		session.removeAttribute("type");
		session.removeAttribute("searchStr");
		
		session.setAttribute("searchStr", DataUtil.nulltoempty(request.getParameter("investNo")));
		session.setAttribute("type", DataUtil.nulltoempty(request.getParameter("type")));
		session.setAttribute("prclist", ser.select());
		session.setAttribute("notMatchlist", poSer.getNotMatch());
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/project/prconfirmlist.jsp");
	}
}
