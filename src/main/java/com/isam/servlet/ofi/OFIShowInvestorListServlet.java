package com.isam.servlet.ofi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.isam.bean.OFIInvestorXFile;
import com.isam.helper.DataUtil;
import com.isam.service.ofi.OFIInvestOptionService;
import com.isam.service.ofi.OFIInvestorListService;
import com.isam.service.ofi.OFIInvestorXFileService;

public class OFIShowInvestorListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIInvestorListService listSer;
	private OFIInvestOptionService optSer;
	private OFIInvestorXFileService fSer;
	private Map<String,Map<String,String>> optmap;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		listSer = new OFIInvestorListService();
		optSer = new OFIInvestOptionService();
		fSer = new OFIInvestorXFileService();
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
		
		/* 如果按了下載EXCEL，會在網頁上插入一個doThing的input，所以只要有doThing就轉頁到另一個Excel Servlet */
		String doThing = request.getParameter("doThing");
		if(doThing != null && "loadExcel".equalsIgnoreCase(doThing)){
			request.getRequestDispatcher("/console/cnfdi/OFIInvestorExcel.view").forward(request, response);
			return;
		}
		
		
		HttpSession session = request.getSession();
		session.removeAttribute("ofiInvestors");
		session.removeAttribute("ofiIOterms");
		session.removeAttribute("optmap");

		Map<String,String> terms=new HashMap<String, String>();		
		String investNo=DataUtil.fmtSearchItem(request.getParameter("investNo"), "");
		String IDNO=DataUtil.fmtSearchItem(request.getParameter("IDNO"), "");
		String companytype=request.getParameter("companytype")==null?"":request.getParameter("companytype");
		String company=DataUtil.fmtSearchItem(request.getParameter("company"), "%");
		String investor=DataUtil.fmtSearchItem(request.getParameter("investor"), "%");
		String investorXRelated=DataUtil.fmtSearchItem(request.getParameter("investorXRelated"), "%");
		String relatedNation=DataUtil.nulltoempty(request.getParameter("nation2"));
		String relatedCnCode=DataUtil.nulltoempty(request.getParameter("cnCode2"));
		String nation=DataUtil.nulltoempty(request.getParameter("nation"));
		String cnCode=DataUtil.nulltoempty(request.getParameter("cnCode"));
		String BG1=request.getParameterValues("BG1")==null?"":DataUtil.addTokenToItem(request.getParameterValues("BG1"),",");
		String BG2=request.getParameterValues("BG2")==null?"":DataUtil.addTokenToItem(request.getParameterValues("BG2"),",");
		String AndOr1=DataUtil.nulltoempty(request.getParameter("AndOr1"));
		String AndOr2=DataUtil.nulltoempty(request.getParameter("AndOr2"));
		terms.put("investNo", investNo);
		terms.put("IDNO", IDNO); //統一編號
		terms.put("companytype",companytype);
		terms.put("company",company);
		terms.put("investor",investor );
		terms.put("investorXRelated", investorXRelated);
		terms.put("relatedNation", relatedNation);
		terms.put("relatedCnCode", relatedCnCode);
		terms.put("nation",nation ); 
		terms.put("cnCode",cnCode );
		terms.put("BG1", BG1); //背景1
		terms.put("BG2", BG2); //背景2
		terms.put("AndOr1", AndOr1);
		terms.put("AndOr2", AndOr2);
		
		if( companytype.equals( "2") &&  (! BG1.isEmpty() || !BG2.isEmpty()) || !nation.isEmpty() || !relatedNation.isEmpty() ) {
			terms.put("companytype","1");
		}
		
	//	
		
		String fbtype=DataUtil.nulltoempty(request.getParameter("fbtype"));
		terms.put("fbtype", fbtype);
		String url="/cnfdi/investorlist.jsp";
		if(fbtype.equals("b")){
			terms.put("investorOnly", "true");
			url="/console/cnfdi/investorlist.jsp";
		}
		List<Map<String,String>> list=listSer.select(terms);
		for (Entry<String, String> m:terms.entrySet()) {
//			System.out.println(m.getKey()+"="+m.getValue());
			terms.put(m.getKey(), m.getValue().replaceAll("%", ""));
		}
		
		
		//107-07-03 比對有無架構圖
		List<Map<String,String>> newlist = new ArrayList<>();
		ArrayList<String> file_investorSeq = fSer.selectInvestorSeqS_hasFile();

		for(Map<String,String> map : list) {
			String investorSeq = map.get("investorSeq");
		
			System.out.println(investorSeq);
			if(file_investorSeq.contains(investorSeq)) {
				map.put("file", "有");
			}else{
				map.put("file", "無");
			}
			
			if(investorSeq.equals("0")) {
				//continue;
			}
			
			String cn = map.get("cn");
			if(cn!=null ) {
				String investorname = map.get("INVESTOR_CHTNAME");
				if(  cn.isEmpty()) {
					if(investorname.indexOf("香港商")>=0) {
						map.replace("cn", "香港");
					}
					else if(investorname.indexOf("日商")>=0) {
						map.replace("cn", "日本");
					}
					else if(investorname.indexOf("大陸商")>=0) {
						map.replace("cn", "中國大陸");
					}
					else if(investorname.indexOf("薩摩亞商")>=0) {
						map.replace("cn", "薩摩亞");
					}
					else if(investorname.indexOf("英商")>=0) {
						map.replace("cn", "英國");
					}
					else if(investorname.indexOf("新加坡商")>=0) {
						map.replace("cn", "新加坡");
					}
					else if(investorname.indexOf("英屬維京群島商")>=0) {
						map.replace("cn", "英屬維京群島");
					}
					else if(investorname.indexOf("貝里斯商")>=0) {
						map.replace("cn", "貝里斯");
					}
					else if(investorname.indexOf("=賽席爾商")>=0) {
						map.replace("cn", "賽席爾");
					}
				}
			}
			newlist.add(map);
		}
		
		session.setAttribute("ofiInvestors", newlist);
		session.setAttribute("ofiIOterms", terms);
		session.setAttribute("optmap", optmap);
		
		String path = request.getContextPath();
		response.sendRedirect(path + url);		
	}
}
