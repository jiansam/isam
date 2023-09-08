package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.ProjectReportService;

public class ProjectReportConfirmEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProjectReportService ser;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ser= new ProjectReportService();
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
		UserMember user = (UserMember) session.getAttribute("userInfo");
		String updateuser = user.getIdMember();
		java.sql.Timestamp time=DataUtil.getNowTimestamp();
		
		String type=DataUtil.nulltoempty(request.getParameter("type"));
		String temp=DataUtil.nulltoempty(request.getParameter("repSerno"));
		String repSerno=DataUtil.fmtStrAryItem(temp.split(","));
		
		if((!type.equals("0")&&!type.equals("1")) || repSerno.isEmpty()){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('參數錯誤，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/project/listrpconfirm.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		
		if(type.equals("0")){
			ser.forDBReference(repSerno, updateuser, time);
		}else if(type.equals("1")){
			ser.overwrite(repSerno, updateuser, time);
		}
		
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/project/listrpconfirm.jsp?type="+type);
	}
}
