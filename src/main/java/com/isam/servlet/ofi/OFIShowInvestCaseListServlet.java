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

import com.isam.helper.DataUtil;
import com.isam.service.ofi.OFIInvestCaseService;
import com.isam.service.ofi.OFIInvestOptionService;
import com.isam.service.ofi.OFIInvestorXFileService;

public class OFIShowInvestCaseListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIInvestCaseService listSer;
	private OFIInvestorXFileService fSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		listSer = new OFIInvestCaseService();
		fSer = new OFIInvestorXFileService();
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
		session.removeAttribute("ofiCase");
		session.removeAttribute("ofiCaseterms");
		
		Map<String,String> terms=new HashMap<String, String>();		
		String investNo=DataUtil.fmtSearchItem(request.getParameter("investNo"), "");
		String IDNO=DataUtil.fmtSearchItem(request.getParameter("IDNO"), "");
		String companytype=request.getParameter("companytype")==null?"":request.getParameter("companytype");
		String company=DataUtil.fmtSearchItem(request.getParameter("company"), "%");
		String investor=DataUtil.fmtSearchItem(request.getParameter("investor"), "%");
		String investorXRelated=DataUtil.fmtSearchItem(request.getParameter("investorXRelated"), "%");
		String relatedNation=DataUtil.nulltoempty(request.getParameter("nation2"));
		String relatedCnCode=DataUtil.nulltoempty(request.getParameter("cnCode2"));
		String AndOr=DataUtil.nulltoempty(request.getParameter("AndOr"));
		String aduit=request.getParameterValues("aduit")==null?"":DataUtil.addTokenToItem(request.getParameterValues("aduit"),",");
		
		
		// 進行「陸資案號、母公司或關連企業及受益人國別、稽核」查詢時，投資型態自動選擇分公司或子公司」。
		if(!investNo.replace("%","").isEmpty() || !relatedNation.replace("%","").isEmpty() || !aduit.replace("%","").isEmpty()) {
			companytype = "1";
		}
		
		
		terms.put("investNo", investNo);
		terms.put("IDNO", IDNO);
		terms.put("company",company);
		terms.put("companytype",companytype);
		terms.put("investor",investor );
		terms.put("investorXRelated", investorXRelated);
		terms.put("relatedNation", relatedNation);
		terms.put("relatedCnCode", relatedCnCode);		
		terms.put("aduit", aduit);
		terms.put("AndOr", AndOr);
		
		List<Map<String,String>> list=listSer.select(terms);
		for (Entry<String, String> m:terms.entrySet()) {
			terms.put(m.getKey(), m.getValue().replaceAll("%", ""));
		}
		
		//107-07-03 比對有無架構圖
		List<Map<String,String>> newlist = new ArrayList<>();
		ArrayList<String> file_investorSeq = fSer.selectInvestorSeqS_hasFile();
		for(Map<String,String> map : list) {
			String investorSeq = map.get("investorSeq");
			if(file_investorSeq.contains(investorSeq)) {
				map.put("file", "有");
			}else{
				map.put("file", "無");
			}
			newlist.add(map);
		}
		
		session.setAttribute("ofiCase", newlist);
		session.setAttribute("ofiCaseterms", terms);
		
		String path = request.getContextPath();
		response.sendRedirect(path + "/cnfdi/investcaselist.jsp");		
	}
}
