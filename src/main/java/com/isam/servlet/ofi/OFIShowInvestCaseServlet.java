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
import com.isam.service.ofi.OFIInvestListService;
import com.isam.service.ofi.OFIInvestNoXAuditService;
import com.isam.service.ofi.OFIInvestOfficeService;
import com.isam.service.ofi.OFIInvestOptionService;
import com.isam.service.ofi.OFIInvestorListService;
import com.isam.service.ofi.OFIInvestorXBGService;
import com.isam.service.ofi.OFIInvestorXRelatedService;

public class OFIShowInvestCaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
//	private InterviewoneContentService iocSer;
	private OFIInvestorListService listSer;
	private OFIInvestorXBGService bgSer;
	private OFIInvestorXRelatedService reSer;
	private OFIInvestCaseService icSer;
	private OFIInvestOfficeService officeSer;
	private OFIContactsService contact;
	private OFIInvestOptionService optSer;
	private OFIInvestListService iSer;
	private OFIInvestNoXAuditService auditSer;
	private OFIAuditOptionService aoptSer;
	private OFIDepartmentService deptSer;
	private COMTBDataService CSer;
	private Map<String, String> levelone;
	private Map<String, String> leveltwo;
	private Map<String,Map<String,String>> optmap;
	private List<OFIAuditOption> auditOpt;
	private Map<String,String> deptOpt;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
//		iocSer= new InterviewoneContentService();
		listSer = new OFIInvestorListService();
		bgSer = new OFIInvestorXBGService();
		reSer = new OFIInvestorXRelatedService();
		icSer = new OFIInvestCaseService();
		auditSer= new OFIInvestNoXAuditService();
		aoptSer=new OFIAuditOptionService();
		deptSer= new OFIDepartmentService();
		optSer = new OFIInvestOptionService();
		iSer=new OFIInvestListService();
		contact=new OFIContactsService();
		officeSer = new OFIInvestOfficeService();
		optmap=optSer.select();
		CSer = new COMTBDataService();
		Map<Integer, Map<String, String>> mapTW=CSer.getTWADDRCode();
		levelone = mapTW.get(1);
		leveltwo = mapTW.get(2);
		auditOpt=aoptSer.getAuditOption();
		deptOpt=deptSer.getCodeNameMap();
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
		session.removeAttribute("ofiiooffice");
		session.removeAttribute("bgs");
		session.removeAttribute("relateds");
		session.removeAttribute("optmap");
		session.removeAttribute("icase");
		session.removeAttribute("agent"); 
		session.removeAttribute("contacts");
		session.removeAttribute("audits");
		session.removeAttribute("audit02");
		session.removeAttribute("audit07");
		String caseNo=DataUtil.nulltoempty(request.getParameter("caseNo"));
		String investorSeq=DataUtil.nulltoempty(request.getParameter("investorSeq"));
		String banno = DataUtil.nulltoempty(request.getParameter("banno"));
		
		System.out.println(banno+" 1:"+caseNo+":"+investorSeq);
		
		if(!banno.isEmpty()) {
			OFIInvestOffice office =  officeSer.select(banno);
			System.out.println(office.getBanNo());
			
			session.setAttribute("ofiiooffice", office);
		}else {
		OFIInvestorList bean=listSer.select(investorSeq);
		if(investorSeq.isEmpty()||bean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您選取的資料已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/cnfdi/listinvestcase.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
			List<Map<String,String>> icase=icSer.getInvestcase(investorSeq,caseNo);
			String investNo=icase.get(0).get("investNo");
			session.setAttribute("ibean", iSer.select(investNo));
			session.setAttribute("ofiiobean", bean);
			session.setAttribute("icase",icase.get(0));
			session.setAttribute("audits",auditSer.getAduitsByInvestorSeq(investorSeq, icase));
			session.setAttribute("audit02", auditSer.getAudit02Details(investNo));
			session.setAttribute("audit07", auditSer.getAudit07Details(investNo));
		}
		

		
		session.setAttribute("bgs", bgSer.getBGMap(investorSeq));
		session.setAttribute("relateds", reSer.select(investorSeq));
	
		session.setAttribute("agent", icSer.getAgent(investorSeq));
		session.setAttribute("contacts",contact.getContacts(investorSeq));
		
		session.setAttribute("auditOpt",auditOpt);
		session.setAttribute("deptOpt",deptOpt);
		session.setAttribute("optmap", optmap);
		session.setAttribute("levelone", levelone);
		session.setAttribute("leveltwo", leveltwo);
		
		String path = request.getContextPath();
		response.sendRedirect(path + "/cnfdi/investcase.jsp");		
	}
}
