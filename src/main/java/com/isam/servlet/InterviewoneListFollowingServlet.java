package com.isam.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.helper.DataUtil;
import com.isam.service.InterviewoneHelp;
import com.isam.service.InterviewoneManageService;
import com.isam.service.InterviewoneService;
import com.isam.service.ofi.OFIInvestOptionService;

public class InterviewoneListFollowingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private InterviewoneManageService imSer;
	private InterviewoneService ioSer;
	private OFIInvestOptionService ofiopt;
	private Map<String,String> opMap;
	
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		imSer = new InterviewoneManageService();
		ioSer = new InterviewoneService();
		ofiopt=new OFIInvestOptionService();
		opMap=ofiopt.select().get("isOperated");
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
		session.removeAttribute("imList");
		session.removeAttribute("dyear");
		session.removeAttribute("terms");
		session.removeAttribute("fOpt");
		session.removeAttribute("opMap");
		
		session.setAttribute("opMap", opMap);
		List<String> yearlist=ioSer.getYearList();
		
		Map<String,String> map=new HashMap<String, String>();
		String gap=DataUtil.nulltoempty(request.getParameter("gap"));
		String priority=DataUtil.nulltoempty(request.getParameter("priority"));
		String year=request.getParameter("year")==null?"":DataUtil.addZeroForNum(request.getParameter("year").trim(), 3);
		String smonth=request.getParameter("smonth")==null?"":DataUtil.addZeroForNum(request.getParameter("smonth").trim(), 2);
		String emonth=request.getParameter("emonth")==null?"":DataUtil.addZeroForNum(request.getParameter("emonth").trim(), 2);
		String investNo=DataUtil.fmtSearchItem(request.getParameter("investNo"), "%");
		String IDNO=DataUtil.fmtSearchItem(request.getParameter("IDNO"), "");
		String investName=DataUtil.fmtSearchItem(request.getParameter("investName"),"%");
		String AndOr=request.getParameter("AndOr")==null?"":request.getParameter("AndOr");
		String abnormal=request.getParameterValues("abnormal")==null?"":DataUtil.addTokenToItem(request.getParameterValues("abnormal"),",");
		String[] progressAry=request.getParameterValues("progress");
		String[] followingAry=request.getParameterValues("following");
		String progress="";
		String following="";
		if(followingAry!=null){
			following=DataUtil.fmtStrAryItem(followingAry);
		}
		if(progressAry!=null){
			progress=DataUtil.fmtStrAryItem(progressAry);
		}
		Map<String,String> maxYM=ioSer.getMaxInterviewDateYM();
		map.put("gap", gap);
		map.put("year", year);
		map.put("smonth", smonth);
		map.put("emonth", emonth);
		map.put("maxM", maxYM.get("month"));
		map.put("maxY", maxYM.get("year"));
		map.put("investNo", investNo);
		map.put("IDNO", IDNO);
		map.put("investName", investName);
		map.put("AndOr", AndOr);
		map.put("abnormal", abnormal);
		map.put("progress", progress);
		map.put("following", following);
		
//		String start=map.get("maxY")+map.get("maxM")+"00";
		String start=map.get("maxY")+"0100";
		String end=map.get("maxY")+map.get("maxM")+"99";
		if(!year.isEmpty()){
			start=year+smonth+"00";
			end=year+emonth+"99";
		}
		if(priority.equals("1")){
			start=map.get("maxY")+"0100";
			end=map.get("maxY")+map.get("maxM")+"99";
			map.put("year", map.get("maxY"));
			map.put("smonth", "1");
			map.put("emonth", map.get("maxM"));
		}
		map.put("start",start);
		map.put("end",end);
		List<Map<String,String>> beans=imSer.getIMList(map);
//		System.out.println(beans.size());
		map.put("investNo", investNo.replace("%", ""));
		map.put("IDNO", IDNO.replace("%", ""));
		map.put("investName", investName.replace("%", ""));
		map.put("progress", progress.replaceAll("\'", ""));
		map.put("following", following.replaceAll("\'", ""));
		
		session.setAttribute("yearlist", yearlist);
		session.setAttribute("imList", beans);
		session.setAttribute("dyear", year);
		
		
		InterviewoneHelp opt = new InterviewoneHelp();
		session.setAttribute("fOpt", opt.getOptionValName());
		
		String servletPath= request.getServletPath();
		
		String url="/console/interviewone/listfollowing.jsp";
		session.setAttribute("terms", map);
		
		if(servletPath.indexOf("console")==-1){
			url=url.replace("/console", "");
		}
		String path = request.getContextPath();
		response.sendRedirect(path + url);		
	}
}
