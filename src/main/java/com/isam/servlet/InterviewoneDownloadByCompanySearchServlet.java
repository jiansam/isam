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

import com.google.gson.Gson;
import com.isam.bean.CommonItemList;
import com.isam.bean.Interviewone;
import com.isam.bean.InterviewoneCompany;
import com.isam.helper.DataUtil;
import com.isam.service.CommonItemListService;
import com.isam.service.InterviewoneFileService;
import com.isam.service.InterviewoneHelp;
import com.isam.service.InterviewoneService;
import com.isam.service.MoeaicDataService;

public class InterviewoneDownloadByCompanySearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private InterviewoneService ioSer;

	private MoeaicDataService mdSer;
	private InterviewoneHelp iohelper;
	private Map<String,Map<String,String>> cninfo;
	private Map<String,Map<String,String>> optionValName;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ioSer = new InterviewoneService();

		mdSer = new MoeaicDataService();
		cninfo=mdSer.getCNSysBaseInfo();
		iohelper = new InterviewoneHelp();
		optionValName=iohelper.getOptionValName();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("yearlist");
		session.removeAttribute("Interviewone");
		session.removeAttribute("dyear");
		session.removeAttribute("action");
		session.removeAttribute("cninfo");
		session.removeAttribute("iStatus");
		session.removeAttribute("sStatus");
		session.removeAttribute("terms");
		session.removeAttribute("iCount");
		session.removeAttribute("sCount");
		session.removeAttribute("cMap");
		session.removeAttribute("reInvestList");
		session.removeAttribute("reInvestBase");
		
	
		Map<String,String> map=new HashMap<String, String>();
	
		String action=request.getParameter("action")==null?"":request.getParameter("action");
		String investNo=DataUtil.fmtSearchItem(request.getParameter("investNo"), "");
		String IDNO=DataUtil.fmtSearchItem(request.getParameter("IDNO"), "");
		String investName=DataUtil.fmtSearchItem(request.getParameter("investName"),"%");
		String status=request.getParameter("status");
		String interviewStatus=request.getParameter("interviewstatus");
		String type=request.getParameter("type");
	
		//107-08-01 新增異常狀況條件查詢(來源來自訪視問卷裡的 異常狀況彙總 & 訪視備註)
		String errMsgXnote=request.getParameter("errMsgXnote")==null?"":request.getParameter("errMsgXnote");
		Map<String,String> maxYM=ioSer.getMaxInterviewDateYM();
	
	
		map.put("investNo", investNo);
		map.put("IDNO", IDNO);
		map.put("investName", investName);
		map.put("status", status);
		map.put("type", type);
		map.put("errMsgXnote", errMsgXnote);
	
	
		List<InterviewoneCompany> beans;

		beans = ioSer.selectByCompany(map);
		
		map.put("investNo", investNo.replace("%", ""));
		map.put("IDNO", IDNO.replace("%", ""));
		map.put("investName", investName.replace("%", ""));
		map.put("status", request.getParameter("status")==null?"-1":status);
		map.put("type", request.getParameter("type")==null?"-1":type);
		map.put("interviewstatus", request.getParameter("interviewstatus")==null?"-1":interviewStatus);
		Map<String,Integer> cMap=new HashMap<String, Integer>();
		for (int i = 0; i < beans.size(); i++) {
			InterviewoneCompany b=beans.get(i);
			String ikey="I_"+b.getInterviewStatus();
			String skey="S_"+b.getSurveyStatus();
			int iVal=1;
			if(cMap.containsKey(ikey)){
				iVal=cMap.get(ikey)+1;
			}
			cMap.put(ikey, iVal);
			iVal=1;
			if(cMap.containsKey(skey)){
				iVal=cMap.get(skey)+1;
			}
			cMap.put(skey, iVal);
		}
		for (int i = 0; i < 2; i++) {
			if(!cMap.containsKey("I_"+i)){
				cMap.put("I_"+i, 0);
			}
			if(!cMap.containsKey("S_"+i)){
				cMap.put("S_"+i, 0);
			}
		}
		if(!cMap.containsKey("I_9")){
			cMap.put("I_9", 0);
		}

		session.setAttribute("cMap", cMap);

		session.setAttribute("Interviewone", beans);

		session.setAttribute("cninfo", cninfo);
		
		//107-08-01 新增異常狀況條件查詢(來源來自訪視問卷裡的 異常狀況彙總 & 訪視備註)
		Gson gson = new Gson();
		CommonItemListService dao = new CommonItemListService();
//		ArrayList<String> errMsgXnoteS = dao.getCommonItemString();
//		session.setAttribute("errMsgXnote", gson.toJson(errMsgXnoteS));
		//107-08-22
		ArrayList<CommonItemList> errMsgXnoteS = dao.get();
		session.setAttribute("errMsgXnote", gson.toJson(errMsgXnoteS));
		
		String servletPath= request.getServletPath();
		
		String url="/interviewone/downloadbycompany.jsp";
	
		
			session.setAttribute("terms", map);
			session.setAttribute("iStatus", optionValName.get("interviewStatus"));
			session.setAttribute("sStatus", optionValName.get("surveyStatus"));
	
		if(servletPath.indexOf("console")==-1){
			url=url.replace("/console", "");
		}
		String path = request.getContextPath();
		response.sendRedirect(path + url);
	}
}
