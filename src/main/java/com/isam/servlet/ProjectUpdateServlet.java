package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.Project;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.ProjectKeyHelp;
import com.isam.service.ProjectService;

public class ProjectUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Map<String, String> IDNOToName;
	Map<String, String> ProjectState;
	Map<String, String> userName;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ProjectKeyHelp help = new ProjectKeyHelp();
		IDNOToName = help.getIDNOToName();
		ProjectState = help.getProjectState();
		userName = help.getUserToName();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		HttpSession session = request.getSession();
		
		UserMember bean = (UserMember) session.getAttribute("userInfo");
		String updateuser = bean.getIdMember();
		String serno=request.getParameter("serno")==null?"":request.getParameter("serno").trim();
		String state=request.getParameter("state")==null?"":request.getParameter("state").trim();
		String notes=request.getParameter("notes")==null?"":request.getParameter("notes").trim();
		String projDate=request.getParameter("projDate")==null?"":request.getParameter("projDate").trim().replaceAll("\\D", "");

		if(serno.isEmpty()||state.isEmpty()){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('查無符合條件資料，請確認代號!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/project/showproject.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		
		ProjectService ser= new ProjectService();
		List<Project> projList = ser.select(serno, null);
		if(projList.size()==1){
			Project proj = projList.get(0);
			proj.setState(state);
			proj.setNote(notes);
			proj.setUpdateuser(updateuser);
			proj.setUpdatetime(DataUtil.getNowTimestamp());
			proj.setPorjDate(projDate);
			proj.setIsSysDate("0");
			ser.update(proj);
		}
		
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/project/showprojectdetail.jsp?serno="+serno+"&updateMark=ok");
	}
}
