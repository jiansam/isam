package com.isam.servlet.ofi;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.OFIAuditOption;
import com.isam.bean.OFIInvestOffice;
import com.isam.bean.OFIInvestorList;
import com.isam.helper.DataUtil;
import com.isam.service.COMTBDataService;
import com.isam.service.ofi.OFIAuditOptionService;
import com.isam.service.ofi.OFIContactsService;
import com.isam.service.ofi.OFIDepartmentService;
import com.isam.service.ofi.OFIInvestCaseService;
import com.isam.service.ofi.OFIInvestNoXAuditService;
import com.isam.service.ofi.OFIInvestOfficeService;
import com.isam.service.ofi.OFIInvestOptionService;
import com.isam.service.ofi.OFIInvestorListService;
import com.isam.service.ofi.OFIInvestorXBGService;
import com.isam.service.ofi.OFIInvestorXFileService;
import com.isam.service.ofi.OFIInvestorXRelatedService;

public class OFIShowInvestOfficeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIInvestOfficeService listSer;
	private OFIInvestorXBGService bgSer;
	private OFIInvestorXRelatedService reSer;
	private OFIInvestCaseService icSer;
	private OFIInvestOptionService optSer;
	private OFIContactsService contact;
	private OFIInvestNoXAuditService auditSer;
	private OFIAuditOptionService aoptSer;
	private OFIDepartmentService deptSer;
	private OFIInvestorXFileService fSer;
	private COMTBDataService CSer;
	private Map<String, String> levelone;
	private Map<String, String> leveltwo;
	private Map<String,Map<String,String>> optmap;
	private List<OFIAuditOption> auditOpt;
	private Map<String,String> deptOpt;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		listSer = new OFIInvestOfficeService();
	
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
		session.removeAttribute("ofiiobean");
	
		
		String banno=DataUtil.nulltoempty(request.getParameter("banno"));
		OFIInvestOffice bean=listSer.select(banno);
		if(banno.isEmpty()||bean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您選取的資料已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/cnfdi/officelist.jsp';</script>");
			out.flush();
			out.close();
			return;
		}

		
		session.setAttribute("ofiiobean", bean);
	
		
		String path = request.getContextPath();
		response.sendRedirect(path + "/cnfdi/office.jsp");		
	}
}
