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
import com.isam.helper.DataUtil;
import com.isam.service.CommonItemListService;
import com.isam.service.InterviewoneFileService;
import com.isam.service.InterviewoneHelp;
import com.isam.service.InterviewoneService;
import com.isam.service.MoeaicDataService;

public class InterviewoneShowListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private InterviewoneService ioSer;
	private InterviewoneFileService iofSer;
	private MoeaicDataService mdSer;
	private InterviewoneHelp iohelper;
	private Map<String,Map<String,String>> cninfo;
	private Map<String,Map<String,String>> optionValName;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ioSer = new InterviewoneService();
		iofSer = new InterviewoneFileService();
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
		
		List<String> yearlist=ioSer.getYearList();
		
		Map<String,String> map=new HashMap<String, String>();
		String year=request.getParameter("year")==null?"":DataUtil.addZeroForNum(request.getParameter("year").trim(), 3);
		String smonth=request.getParameter("smonth")==null?"":DataUtil.addZeroForNum(request.getParameter("smonth").trim(), 2);
		String emonth=request.getParameter("emonth")==null?"":DataUtil.addZeroForNum(request.getParameter("emonth").trim(), 2);
		String action=request.getParameter("action")==null?"":request.getParameter("action");
		String investNo=DataUtil.fmtSearchItem(request.getParameter("investNo"), "");
		String IDNO=DataUtil.fmtSearchItem(request.getParameter("IDNO"), "");
		String investName=DataUtil.fmtSearchItem(request.getParameter("investName"),"%");
		String survey=request.getParameter("survey");
		String interview=request.getParameter("interview");
		String AndOr=request.getParameter("AndOr")==null?"":request.getParameter("AndOr");
		String abnormal=request.getParameterValues("abnormal")==null?"":DataUtil.addTokenToItem(request.getParameterValues("abnormal"),",");
		//107-08-01 新增異常狀況條件查詢(來源來自訪視問卷裡的 異常狀況彙總 & 訪視備註)
		String errMsgXnote=request.getParameter("errMsgXnote")==null?"":request.getParameter("errMsgXnote");
		Map<String,String> maxYM=ioSer.getMaxInterviewDateYM();
		map.put("year", year);
		map.put("smonth", smonth);
		map.put("emonth", emonth);
		map.put("maxM", maxYM.get("month"));
		map.put("maxY", maxYM.get("year"));
		map.put("investNo", investNo);
		map.put("IDNO", IDNO);
		map.put("investName", investName);
		map.put("survey", survey);
		map.put("interview", interview);
		map.put("AndOr", AndOr);
		map.put("abnormal", abnormal);
		map.put("errMsgXnote", errMsgXnote);
		String start=map.get("maxY")+"0100";
//		String start=map.get("maxY")+map.get("maxM")+"00";
		String end=map.get("maxY")+map.get("maxM")+"99";
		if(!year.isEmpty()){
			start=year+smonth+"00";
			end=year+emonth+"99";
		}
		map.put("start",start);
		map.put("end",end);
		List<Interviewone> beans;System.out.println(year);
		if(!action.isEmpty()){
			beans = ioSer.select(year.isEmpty() ? map.get("maxY") : year);
		}else{
			beans = ioSer.select(map);
		}
		int tc=ioSer.getTCountByYear(year.isEmpty()?map.get("maxY"):year);
		int noCount=ioSer.getNotFilledCountByYear(year.isEmpty()?map.get("maxY"):year);
		map.put("totalCount", String.valueOf(tc));
		map.put("noCount", String.valueOf(noCount));
		
		map.put("investNo", investNo.replace("%", ""));
		map.put("IDNO", IDNO.replace("%", ""));
		map.put("investName", investName.replace("%", ""));
		map.put("survey", request.getParameter("survey")==null?"-1":survey);
		map.put("interview", request.getParameter("interview")==null?"-1":interview);
		
		Map<String,Integer> cMap=new HashMap<String, Integer>();
		for (int i = 0; i < beans.size(); i++) {
			Interviewone b=beans.get(i);
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
		session.setAttribute("yearlist", yearlist);
		session.setAttribute("Interviewone", beans);
		session.setAttribute("dyear", year.isEmpty() ? map.get("maxY") : year);
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
		
		String url="/console/interviewone/listInterview.jsp";
		session.setAttribute("reInvestList", ioSer.getReInvestNoByYear(year.isEmpty() ? map.get("maxY") : year));
		session.setAttribute("reInvestBase", ioSer.getReInvestNoBaseInfo(year.isEmpty() ? map.get("maxY") : year));
		if(!action.isEmpty()){
			session.setAttribute("action", action);
			List<List<Integer>> list=iofSer.getISFileCount(year.isEmpty() ? map.get("maxY") : year);
			Map<Integer,Integer> iCount = new HashMap<Integer, Integer>();
			Map<Integer,Integer> sCount = new HashMap<Integer, Integer>();
			for (int i = 0; i < list.size(); i++) {
				List<Integer> counts=list.get(i);
				iCount.put(counts.get(0), counts.get(1));
				sCount.put(counts.get(0), counts.get(2));
			}
			session.setAttribute("iCount", iCount);
			session.setAttribute("sCount", sCount);
			url="/console/interviewone/editcompanylist.jsp";
		}else{
			session.setAttribute("terms", map);
			session.setAttribute("iStatus", optionValName.get("interviewStatus"));
			session.setAttribute("sStatus", optionValName.get("surveyStatus"));
		}
		if(servletPath.indexOf("console")==-1){
			url=url.replace("/console", "");
		}
		String path = request.getContextPath();
		response.sendRedirect(path + url);
	}
}
