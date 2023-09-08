package com.isam.servlet.ofi;

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
import com.isam.ofi.reject.service.OFIRejectService;
import com.isam.service.ofi.OFIInvestOfficeService;
import com.isam.service.ofi.OFIInvestOptionService;

public class OFIListOfficeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIInvestOfficeService rSer;
	private OFIInvestOptionService optSer;
	private Map<String,Map<String,String>> opt;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		rSer = new OFIInvestOfficeService();
		optSer = new OFIInvestOptionService();
		opt=optSer.select();
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
	
		session.removeAttribute("Info");
		
		session.removeAttribute("offices");
		
		
		Map<String,String> info=new HashMap<String, String>();
		Map<String,String> terms=new HashMap<String, String>();

		terms.put("company",request.getParameter("company"));
		terms.put("banno",request.getParameter("banno"));
		terms.put("status",request.getParameter("status"));
		
		String company=DataUtil.fmtSearchItem(request.getParameter("company"),"%");
		String banno=DataUtil.nulltoempty(request.getParameter("banno"));
	
		String status=DataUtil.nulltoempty(request.getParameter("status"));
		info.put("company",company);
		info.put("banno", banno);
	
		info.put("status", status);
		List<Map<String, String>> offices=rSer.select(info);
		session.setAttribute("offices", offices);

		session.setAttribute("Info", terms);
		String path = request.getContextPath();
		if(request.getServletPath().startsWith("/console")){
			response.sendRedirect(path + "/console/cnfdi/officelist.jsp");		
		}else{
			response.sendRedirect(path + "/cnfdi/officelist.jsp");		
		}
	}
}
