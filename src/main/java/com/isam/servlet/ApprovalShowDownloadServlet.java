package com.isam.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.isam.dao.ApprovalOptionDAO;
import com.isam.dao.CommitRestrainTypeDAO;
import com.isam.helper.DataUtil;
import com.isam.service.CommitReportService;
import com.isam.service.ProjectReportService;

public class ApprovalShowDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommitReportService crSer;
	private ProjectReportService prSer;
	private Map<String, String> CRType;
	private Map<String,String> AoCode;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		crSer = new CommitReportService();
		prSer = new ProjectReportService();
		CRType = CommitRestrainTypeDAO.getTypeMap();
		AoCode = ApprovalOptionDAO.getOptionMapByType("Commit");
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String approval = DataUtil.nulltoempty(request.getParameter("approval"));
		String year = DataUtil.nulltoempty(request.getParameter("year"));
		String quarter = DataUtil.nulltoempty(request.getParameter("quarter"));
		String type = DataUtil.nulltoempty(request.getParameter("type"));
		String repType = DataUtil.nulltoempty(request.getParameter("repType"));
		String declare = DataUtil.nulltoempty(request.getParameter("declare"));
		
		
		Map<String,String> terms=new HashMap<String, String>();
		terms.put("approval", approval);
		terms.put("year",year );
		terms.put("quarter", quarter);
		terms.put("type",type );
		terms.put("repType", repType);
		terms.put("declare", declare);
		request.setAttribute("terms", terms);
		
		Map<String,Map<String,String>> cMax=crSer.getMaxMinYearQuarter();
		Map<String,String> pMax=prSer.getMaxMinYearQuarter();
		
		request.setAttribute("cMax", cMax);
		request.setAttribute("pMax", pMax);
		request.setAttribute("CRType", CRType);
		request.setAttribute("AoCode", AoCode);
		
		request.getRequestDispatcher("/approval/approvaldownload.jsp").forward(request, response);
	}
}
