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

import com.isam.helper.ApplicationAttributeHelper;
import com.isam.helper.DataUtil;
import com.isam.service.ApprovalService;
import com.isam.service.ProjectKeyHelp;

public class ApprovalShowListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Map<String, String> IDNOToName;
	private ApprovalService ser;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ser= new ApprovalService();
		ProjectKeyHelp help = new ProjectKeyHelp();
		IDNOToName = help.getIDNOToName();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		
		HttpSession session = request.getSession();
		session.removeAttribute("ApprovalTerms");
		session.removeAttribute("ApprovalList");
		String investNo=request.getParameter("investNo")==null?"%":DataUtil.fmtSearchItem(request.getParameter("investNo").trim(),"");
		String cnName=request.getParameter("cnName")==null?"%":DataUtil.fmtSearchItem(request.getParameter("cnName").trim(),"%");
		String IDNO=request.getParameter("IDNO")==null?"%":DataUtil.fmtSearchItem(request.getParameter("IDNO").trim(),"");
		String investor=request.getParameter("investName")==null?"%":DataUtil.fmtSearchItem(request.getParameter("investName").trim(),"%");
		String com=request.getParameter("com")==null?"0":request.getParameter("com");
		
		String[] stateAry=request.getParameterValues("state");
		String state="";
		if(stateAry!=null){
			state = DataUtil.fmtStrAryItem(stateAry);
		}
		if(state.isEmpty()&&!com.equals("2")){
			state="'01','02'";
		}
		
		Map<String,String> terms = new HashMap<String, String>();
		terms.put("investNo", investNo.replace("%", ""));
		terms.put("cnName", cnName.replace("%", ""));
		terms.put("IDNO", IDNO.replace("%", ""));
		terms.put("investor", investor.replace("%", ""));
		terms.put("state", state.replace("'", ""));
		terms.put("com",com);
//		System.out.println(IDNO+":"+investor+":"+investNo+":"+cnName+":"+state+":"+com);
		List<Map<String, String>> mapList = ser.getApprovalMapList(IDNO, investor, investNo, cnName, state, com);
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		
		if(mapList!=null){
			for(int i=0;i<mapList.size();i++){
				Map<String, String> map = mapList.get(i);
				map.put("investor", IDNOToName.get(map.get("idno")));
				map.put("cnName", InvestNoToName.get(map.get("investNo")));
				String stateTemp =  map.get("state");
				if(com.equals("2")&&stateTemp.isEmpty()){
					continue;
				}else{
					map.put("stateName", stateTemp==""?"":(stateTemp.equals("01")?"列管":"解除季報"));
				}
				result.add(map);
			}
		}
		session.setAttribute("ApprovalTerms", terms);
		session.setAttribute("ApprovalList", result);
		String path = request.getContextPath();
		response.sendRedirect(path + "/approval/approval.jsp");
	}
}
