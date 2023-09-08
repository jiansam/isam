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

import com.isam.bean.CommitInvestor;
import com.isam.helper.DataUtil;
import com.isam.service.CommitInvestorService;
import com.isam.service.ProjectKeyHelp;

public class CommitShowListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Map<String, String> IDNOToName;
	Map<String, String> userName;
	private CommitInvestorService ser;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ser= new CommitInvestorService();
		ProjectKeyHelp help = new ProjectKeyHelp();
		IDNOToName = help.getIDNOToName();
		userName = help.getUserToName();
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
		session.removeAttribute("commitTerms");
		session.removeAttribute("commitList");
		String IDNO=request.getParameter("IDNO")==null?"%":DataUtil.fmtSearchItem(request.getParameter("IDNO").trim(),"");
		String investor=request.getParameter("investName")==null?"%":DataUtil.fmtSearchItem(request.getParameter("investName").trim(),"%");
		String from=request.getParameter("from")==null?null:DataUtil.fmtDateItem(request.getParameter("from").trim());
		String to=request.getParameter("to")==null?null:DataUtil.fmtDateItem(request.getParameter("to").trim());
		String temp=request.getParameter("alert")==null?"":request.getParameter("alert");
		String[] stateAry=request.getParameterValues("state");
		String state="";
		if(stateAry!=null){
			state = DataUtil.fmtStrAryItem(stateAry);
		}
		String alert=null;
		if(temp.equals("Y")){
			alert="1";
			state="01";
		}
		
		Map<String,String> terms = new HashMap<String, String>();
		terms.put("IDNO", IDNO.replace("%", ""));
		terms.put("investor", investor.replace("%", ""));
		terms.put("from", request.getParameter("from")==null?"":request.getParameter("from"));
		terms.put("to", request.getParameter("to")==null?"":request.getParameter("to"));
		terms.put("state", state.replace("'", ""));
		
//		List<CommitInvestor> beans = ser.select();
		List<CommitInvestor> beans = ser.getSearchResult(investor,IDNO,state,from,to,alert);
		
		List<List<String>> result = new ArrayList<List<String>>();
		if(beans!=null){
			for(int i=0;i<beans.size();i++){
				CommitInvestor bean= beans.get(i);
				List<String> list= new ArrayList<String>();
				list.add(bean.getIDNO());
				list.add(IDNOToName.get(bean.getIDNO()));
				list.add(userName.get(bean.getUpdateuser())==null?bean.getUpdateuser():userName.get(bean.getUpdateuser()));
				list.add(DataUtil.toTWDateStr(bean.getUpdatetime()));
				result.add(list);
			}
		}
		
		session.setAttribute("commitTerms", terms);
		session.setAttribute("commitList", result);
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/commit/commitlist.jsp");
	}
}
