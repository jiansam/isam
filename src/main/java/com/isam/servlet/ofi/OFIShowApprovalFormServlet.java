package com.isam.servlet.ofi;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.OFIAuditOption;
import com.isam.bean.OFIInvestList;
import com.isam.bean.OFIReInvestList;
import com.isam.helper.DataUtil;
import com.isam.service.COMTBDataService;
import com.isam.service.InterviewoneContentService;
import com.isam.service.InterviewoneFileService;
import com.isam.service.InterviewoneHelp;
import com.isam.service.InterviewoneManageService;
import com.isam.service.InterviewoneService;
import com.isam.service.ofi.OFIAuditOptionService;
import com.isam.service.ofi.OFIContactsService;
import com.isam.service.ofi.OFIDepartmentService;
import com.isam.service.ofi.OFIInvestCaseService;
import com.isam.service.ofi.OFIInvestListService;
import com.isam.service.ofi.OFIInvestNoXAuditService;
import com.isam.service.ofi.OFIInvestNoXFinancialService;
import com.isam.service.ofi.OFIInvestNoXTWSICService;
import com.isam.service.ofi.OFIInvestOptionService;
import com.isam.service.ofi.OFIManageClassifyService;
import com.isam.service.ofi.OFIManageScoreService;
import com.isam.service.ofi.OFIMoeaicDataService;
import com.isam.service.ofi.OFINTBTDatasService;
import com.isam.service.ofi.OFIReInvestListService;
import com.isam.service.ofi.OFIReInvestNoXFinancialService;
import com.isam.service.ofi.OFIReInvestXTWSICService;
import com.isam.service.ofi.OFIReceiveNoListService;

public class OFIShowApprovalFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIInvestListService listSer;
	private OFIInvestOptionService optSer;
	private OFIMoeaicDataService omdSer;
	private OFIContactsService cSer;
	private OFIInvestCaseService icSer;
	private InterviewoneService ioSer;
	private OFIInvestNoXTWSICService twsic;
	private OFIReInvestListService reSer;
	private OFIReInvestXTWSICService reSicSer;
	private OFIInvestNoXAuditService audSer;
	private OFIAuditOptionService aoptSer;
	private OFIDepartmentService deptSer;
	private OFIInvestNoXFinancialService finSer;
	private OFIReInvestNoXFinancialService rfinSer;
	private Map<String,Map<String,String>> optmap;
	private COMTBDataService CSer;
	private Map<String, String> levelone;
	private Map<String, String> leveltwo;
	private List<OFIAuditOption> auditOpt;
	private Map<String,String> deptOpt;
	private Map<String, Map<String, String>> ioOpt;
	private InterviewoneContentService iocSer;
	private OFIReceiveNoListService rSer;
	private OFIManageClassifyService mcSer;
	private OFIManageScoreService msSer;
	private InterviewoneManageService imSer;
	private InterviewoneFileService iofSer;
	private OFINTBTDatasService NTBTser;
	
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		listSer = new OFIInvestListService();
		omdSer= new OFIMoeaicDataService();
		twsic = new OFIInvestNoXTWSICService();
		optSer = new OFIInvestOptionService();
		ioSer = new InterviewoneService();
		iocSer = new InterviewoneContentService();
		imSer = new InterviewoneManageService();
		cSer = new OFIContactsService();
		reSer=new OFIReInvestListService();
		CSer = new COMTBDataService();
		audSer=new OFIInvestNoXAuditService();
		aoptSer=new OFIAuditOptionService();
		deptSer= new OFIDepartmentService();
		finSer= new OFIInvestNoXFinancialService();
		rfinSer= new OFIReInvestNoXFinancialService();
		icSer = new OFIInvestCaseService();
		rSer = new OFIReceiveNoListService();
		reSicSer=new OFIReInvestXTWSICService();
		mcSer =new OFIManageClassifyService();
		msSer=new OFIManageScoreService();
		optmap=optSer.select();
		InterviewoneHelp h = new InterviewoneHelp();
		ioOpt=h.getOptionValName();
		Map<Integer, Map<String, String>> mapTW=CSer.getTWADDRCode();
		levelone = mapTW.get(1);
		leveltwo = mapTW.get(2);
		auditOpt=aoptSer.getAuditOption();
		deptOpt=deptSer.getCodeNameMap();
		iofSer=new InterviewoneFileService();
		NTBTser = new OFINTBTDatasService();
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
		session.removeAttribute("manage");
		session.removeAttribute("mscore");
		session.removeAttribute("audit");
		session.removeAttribute("audit02");
		session.removeAttribute("audit07");
		session.removeAttribute("auditOpt");
		session.removeAttribute("deptOpt");
		session.removeAttribute("financial");
		session.removeAttribute("refinancial");
		session.removeAttribute("optmap");
		session.removeAttribute("sysinfo");
		session.removeAttribute("opbean");
		session.removeAttribute("investors");
		session.removeAttribute("opsummary");
		session.removeAttribute("sic");
		session.removeAttribute("sicOption");
	/*	session.removeAttribute("agents");*/
		session.removeAttribute("contacts");
		session.removeAttribute("contactsName");
		session.removeAttribute("reInvests");
		session.removeAttribute("reInvestNoXName");
		session.removeAttribute("reInvestItems");
		session.removeAttribute("IOLV1");
		session.removeAttribute("IOLV2");
		session.removeAttribute("IReceiveNo");
		session.removeAttribute("singlelist");
		session.removeAttribute("errorIlist");
		session.removeAttribute("errorFlist");
		session.removeAttribute("sicMap");
		session.removeAttribute("ioOpt");
		session.removeAttribute("fsMap");
		session.removeAttribute("iofCount");
		session.removeAttribute("NTBTDatas");

		String investNo=DataUtil.nulltoempty(request.getParameter("investNo"));
		String tabsNum=DataUtil.nulltoempty(request.getParameter("tabsNum"));
		
		OFIInvestList bean=listSer.select(investNo);
		if(investNo.isEmpty()||bean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您選取的資料已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/cnfdi/listapproval.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		
		Map<String,String> sysinfo=omdSer.getSysBaseInfo(investNo);
		String prefix=investNo.substring(0, 1).equals("4")?"（陸分）":"（陸）";
		sysinfo.put("tabsNum", tabsNum.isEmpty()?"0":tabsNum);
		sysinfo.put("prefix", prefix);
		sysinfo.put("inSrc", icSer.investorSrc(investNo));
		Map<String,String> sub = audSer.getAduitsByInvestNo(investNo);
		sysinfo.put("isAudit", audSer.isEditable(investNo));
//		sysinfo.put("isAudit", "1");
		
		session.setAttribute("audit", sub);
		session.setAttribute("auditOpt", auditOpt);
		session.setAttribute("audit02", audSer.getAudit02Details(investNo));
		session.setAttribute("audit07", audSer.getAudit07Details(investNo)); //107-10-24 新增
		
		session.setAttribute("manage", mcSer.select(investNo));
		session.setAttribute("mscore", msSer.select(investNo));
		session.setAttribute("deptOpt", deptOpt);
		session.setAttribute("financial", finSer.select(investNo,"0"));
		session.setAttribute("refinancial", rfinSer.select(investNo));
		session.setAttribute("optmap", optmap);
		session.setAttribute("sysinfo", sysinfo);
		session.setAttribute("opbean", bean);
		session.setAttribute("investors", icSer.select(investNo));
		session.setAttribute("opsummary", icSer.summary(investNo));
		session.setAttribute("sic", twsic.getTWSICSelected(investNo));
		session.setAttribute("sicOption", twsic.getTWSICList(investNo));
		session.setAttribute("sicMap", twsic.getTWSICMap());
		/*session.setAttribute("agents", icSer.getAgents(investNo));*/
		session.setAttribute("contacts", ioSer.getContacts(investNo));
		session.setAttribute("contactsName", cSer.select(investNo));
		List<OFIReInvestList> reInvests=reSer.select(investNo);
		Map<String,String> reInvestNoXName=new HashMap<String,String>();
		for (OFIReInvestList b:reInvests) {
			reInvestNoXName.put(b.getReInvestNo(), b.getReinvestment());
		}
		session.setAttribute("reInvests", reInvests);
		session.setAttribute("reInvestNoXName", reInvestNoXName);
		session.setAttribute("reInvestItems", reSicSer.getReInvestFrontTWSICs(investNo));
		session.setAttribute("IOLV1", levelone);
		session.setAttribute("IOLV2", leveltwo);
		session.setAttribute("IReceiveNo", rSer.getinvestReceiveNo(investNo));
		session.setAttribute("singlelist", ioSer.selectByInvestNo2(investNo)); //106-11-29，新增DAO，將下載檔案數量一起加入取出
		session.setAttribute("errorIlist", iocSer.getInterviewErrorList()); 
		session.setAttribute("errorFlist", iocSer.getFinancialErrorList());
		session.setAttribute("fsMap", imSer.getFollowingMap(investNo));
		session.setAttribute("ioOpt", ioOpt);
		session.setAttribute("iofCount", iofSer.countByYear(investNo, "0")); //106-11-29停用，修改singlelist sql直接一起取出附檔數量
		session.setAttribute("NTBTDatas", NTBTser.list(investNo));
		//System.out.println(investNo);
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/cnfdi/approvalfirm.jsp");		
	}
}
