package com.isam.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.Project;
import com.isam.helper.ApplicationAttributeHelper;
import com.isam.helper.DataUtil;
import com.isam.service.ProjectKeyHelp;
import com.isam.service.ProjectService;

public class ProjectShowListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Map<String, String> IDNOToName;
	Map<String, String> ProjectState;
	Map<String, String> userName;
	private ProjectService ser;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ser= new ProjectService();
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
		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		
		HttpSession session = request.getSession();
		session.removeAttribute("projTerms");
		session.removeAttribute("projList");
		String investNo=request.getParameter("investNo")==null?"%":DataUtil.fmtSearchItem(request.getParameter("investNo").trim(),"");
		String cnName=request.getParameter("cnName")==null?"%":DataUtil.fmtSearchItem(request.getParameter("cnName").trim(),"%");
		String IDNO=request.getParameter("IDNO")==null?"%":DataUtil.fmtSearchItem(request.getParameter("IDNO").trim(),"");
		String investor=request.getParameter("investName")==null?"%":DataUtil.fmtSearchItem(request.getParameter("investName").trim(),"%");
		String from=request.getParameter("from")==null?null:DataUtil.fmtDateItem(request.getParameter("from").trim());
		String to=request.getParameter("to")==null?null:DataUtil.fmtDateItem(request.getParameter("to").trim());
		String temp=request.getParameter("alert")==null?"":request.getParameter("alert");
		String[] stateAry=request.getParameterValues("state");
		String state="";
		Map<String, String> errors=new HashMap<String, String>();
		if(stateAry==null){
			errors.put("stateAry", "至少需選擇一項狀態");
			state="'01','02','03','04'";
		}else{
			state = DataUtil.fmtStrAryItem(stateAry);
		}
		String alert="";
		if(temp.equals("Y")){
			alert="1";
			state="01";
		}else{
			alert=null;
		}
		Map<String,String> terms = new HashMap<String, String>();
		terms.put("investNo", investNo.replace("%", ""));
		terms.put("cnName", cnName.replace("%", ""));
		terms.put("IDNO", IDNO.replace("%", ""));
		terms.put("investor", investor.replace("%", ""));
		terms.put("from", request.getParameter("from")==null?"":request.getParameter("from"));
		terms.put("to", request.getParameter("to")==null?"":request.getParameter("to"));
		terms.put("state", state.replace("'", ""));
		
		List<Project> beans=ser.getSearchResult(investor, IDNO, investNo, cnName, state,from,to,alert);
		List<List<String>> result = new ArrayList<List<String>>();
		Set<String> countCN= new HashSet<String>();
		Set<String> countIDNO= new HashSet<String>();
		if(beans!=null){
			for(int i=0;i<beans.size();i++){
				Project proj= beans.get(i);
				countCN.add(proj.getInvestNo());
				countIDNO.add(proj.getIDNO());
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
		}
		terms.put("countCN", String.valueOf(countCN.size()));
		terms.put("countIDNO", String.valueOf(countIDNO.size()));
		
		session.setAttribute("projTerms", terms);
		session.setAttribute("projList", result);
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/project/projectlist.jsp");
	}
}
