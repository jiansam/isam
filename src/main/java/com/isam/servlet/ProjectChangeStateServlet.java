package com.isam.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.isam.helper.ApplicationAttributeHelper;
import com.isam.helper.DataUtil;
import com.isam.service.ProjectKeyHelp;
import com.isam.service.ProjectService;

public class ProjectChangeStateServlet extends HttpServlet {
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
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		
		HttpSession session = request.getSession();
		UserMember bean = (UserMember) session.getAttribute("userInfo");
		String updateuser = bean.getIdMember();
		
		String[] serno=request.getParameterValues("no");
		String state=request.getParameter("changeTo");
		
		Map<String, String> errors=new HashMap<String, String>();
		String sernoStr="";
		if(serno==null){
			errors.put("serno", "請至少選擇一筆專案");
		}else{
			sernoStr = DataUtil.fmtStrAryItem(serno);
		}
//		System.out.println(sernoStr);
		Map<String,String> terms = new HashMap<String, String>();
		terms.put("state", state);
		terms.put("changeDone", "下列資料已修改狀態為"+ProjectState.get(state));
		ProjectService ser= new ProjectService();
		ser.updateState(state, updateuser, sernoStr);
		List<Project> beans = ser.selectBySernoStr(sernoStr);
		
		List<List<String>> result = new ArrayList<List<String>>();
		for(int i=0;i<beans.size();i++){
			Project proj= beans.get(i);
			List<String> list= new ArrayList<String>();
			list.add(String.valueOf(proj.getSerno()));
			list.add(IDNOToName.get(proj.getIDNO()));
			list.add(proj.getInvestNo());
			list.add(InvestNoToName.get(proj.getInvestNo()));
			list.add(ProjectState.get(proj.getState()));
			list.add(userName.get(proj.getUpdateuser())==null?proj.getUpdateuser():userName.get(proj.getUpdateuser()));
			list.add(DataUtil.toTWDateStr(proj.getUpdatetime()));
			result.add(list);
		}		
		session.removeAttribute("projTerms");
		session.removeAttribute("projList");
		session.setAttribute("projTerms", terms);
		session.setAttribute("projList", result);
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/project/projectlist.jsp");
	}
}
