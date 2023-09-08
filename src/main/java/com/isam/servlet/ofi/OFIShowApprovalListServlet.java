package com.isam.servlet.ofi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.isam.bean.CommonItemList;
import com.isam.helper.DataUtil;
import com.isam.service.CommonItemListService;
import com.isam.service.InterviewoneService;
import com.isam.service.ofi.OFIInvestListService;
import com.isam.service.ofi.OFIInvestOptionService;

public class OFIShowApprovalListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIInvestListService listSer;
	private OFIInvestOptionService optSer;
	private InterviewoneService ioSer;
	private Map<String,Map<String,String>> optmap;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		listSer = new OFIInvestListService();
		ioSer = new InterviewoneService();
		optSer = new OFIInvestOptionService();
		optmap=optSer.select();
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
		session.removeAttribute("ofilist");
		session.removeAttribute("ofiterms");
		session.removeAttribute("optmap");
		session.removeAttribute("iyearlist");
		
		Map<String,String> terms=new HashMap<String, String>();		
		String investNo=DataUtil.fmtSearchItem(request.getParameter("investNo"), "");
		String IDNO=DataUtil.fmtSearchItem(request.getParameter("IDNO"), "");
		String company=DataUtil.fmtSearchItem(request.getParameter("company"), "%");
		String year=request.getParameter("year")==null?"":DataUtil.addZeroForNum(request.getParameter("year").trim(), 3);
		String investor=DataUtil.fmtSearchItem(request.getParameter("investor"), "%");
		String reInvest=DataUtil.nulltoempty(request.getParameter("reInvest"));
		String CNFDI=DataUtil.nulltoempty(request.getParameter("CNFDI"));
		String sp=DataUtil.nulltoempty(request.getParameter("sp"));
		String iyeartmp=DataUtil.nulltoempty(request.getParameter("iyear"));
		String AndOr=DataUtil.nulltoempty(request.getParameter("AndOr"));
		String twsictmp=DataUtil.nulltoempty(request.getParameter("twsic"));
		String twsic=twsictmp;
		if(!twsictmp.isEmpty()){
			twsic=DataUtil.fmtStrAryItem(twsictmp.split(","));
		}
		String abnormal=request.getParameterValues("abnormal")==null?"":DataUtil.addTokenToItem(request.getParameterValues("abnormal"),",");
		String issueType=request.getParameterValues("issueType")==null?"'01','02','03','04','05','99'":DataUtil.fmtStrAryItem(request.getParameterValues("issueType"));
		
		//107-08-01 新增年度 + 異常狀況條件查詢(來源來自訪視問卷裡的 異常狀況彙總 & 訪視備註)
		String errMsgXnote = request.getParameter("errMsgXnote")==null? "":request.getParameter("errMsgXnote");
		
		List<String> yearlist=ioSer.getYearList();
		
		String itvOneYeartmp = DataUtil.nulltoempty(request.getParameter("itvOneYear"));
		String itvOneYear = itvOneYeartmp;
		if(itvOneYeartmp.equals("-1")) {
			itvOneYear = "year"; //DAO 的搜尋條件有year，沒有year就讓它等於year
		}else if(itvOneYeartmp.isEmpty()){
			itvOneYear = yearlist.get(0);
		}
		
		String iyear=iyeartmp;
		if(iyeartmp.equals("-1")){
			iyear="";
		}else if(iyeartmp.isEmpty()){
			iyear=yearlist.get(0);
		}
		
		Map<String, String> map= listSer.getYearRange();
		terms.put("investNo", investNo);
		terms.put("IDNO", IDNO);
		terms.put("company",company);
		terms.put("year",year );
		terms.put("iyear",iyear);
		terms.put("investor",investor );
		terms.put("reInvest",reInvest );
		terms.put("CNFDI",CNFDI );
		terms.put("sp", sp);
		terms.put("AndOr", AndOr);
		terms.put("abnormal", abnormal);
		terms.put("issueType", issueType.replaceAll("'", ""));
		terms.put("syear", map.get("syear"));
		terms.put("eyear", map.get("eyear"));
		terms.put("dyear", year);
		terms.put("twsic", twsic);
		terms.put("errMsgXnote", errMsgXnote);
		terms.put("itvOneYear", itvOneYear);
		
		
		String fbtype=DataUtil.nulltoempty(request.getParameter("fbtype"));
		terms.put("fbtype", fbtype);
		String url="/cnfdi/approvallist.jsp";
		if(fbtype.equals("b")){
			url="/console/cnfdi/approvallist.jsp";
		}
		List<Map<String,String>> list=listSer.select(terms);
		terms.put("iyear",iyear.isEmpty()?"-1":iyear);
		terms.put("twsic", twsictmp);
		for (Entry<String, String> m:terms.entrySet()) {
//			System.out.println(m.getKey()+"="+m.getValue());
			terms.put(m.getKey(), m.getValue().replaceAll("%", ""));
		}
		
		session.setAttribute("iyearlist", yearlist);
		session.setAttribute("ofilist", list);
		session.setAttribute("ofiterms", terms);
		session.setAttribute("optmap", optmap);
		
		//107-08-01 新增異常狀況條件查詢(來源來自訪視問卷裡的 異常狀況彙總 & 訪視備註)
		Gson gson = new Gson();
		CommonItemListService dao = new CommonItemListService();
//		ArrayList<String> errMsgXnoteS = dao.getCommonItemString();
//		session.setAttribute("errMsgXnote", gson.toJson(errMsgXnoteS));
		//107-08-22
		ArrayList<CommonItemList> errMsgXnoteS = dao.get();
		session.setAttribute("errMsgXnote", gson.toJson(errMsgXnoteS));
		
		String path = request.getContextPath();
		response.sendRedirect(path + url);		
	}
}
