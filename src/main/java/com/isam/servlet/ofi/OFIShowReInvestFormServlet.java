package com.isam.servlet.ofi;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.OFIInvestList;
import com.isam.bean.OFIReInvestList;
import com.isam.helper.DataUtil;
import com.isam.service.COMTBDataService;
import com.isam.service.ofi.OFIInvestListService;
import com.isam.service.ofi.OFIInvestNoXTWSICService;
import com.isam.service.ofi.OFIInvestOptionService;
import com.isam.service.ofi.OFIMoeaicDataService;
import com.isam.service.ofi.OFIReInvestListService;
import com.isam.service.ofi.OFIReInvestXTWSICService;

public class OFIShowReInvestFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIInvestListService listSer;
	private OFIInvestOptionService optSer;
	private OFIReInvestXTWSICService reSicSer;
	private OFIMoeaicDataService omdSer;
	private OFIReInvestListService reSer;
	private OFIInvestNoXTWSICService twsic;
	private Map<String,Map<String,String>> optmap;
	private COMTBDataService CSer;
	private Map<String, String> levelone;
	private Map<String, String> leveltwo;
	
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		listSer = new OFIInvestListService();
		omdSer= new OFIMoeaicDataService();
		optSer = new OFIInvestOptionService();
		reSer=new OFIReInvestListService();
		reSicSer = new OFIReInvestXTWSICService();
		CSer = new COMTBDataService();
		optmap=optSer.select();
		Map<Integer, Map<String, String>> mapTW=CSer.getTWADDRCode();
		levelone = mapTW.get(1);
		leveltwo = mapTW.get(2);
		twsic=new OFIInvestNoXTWSICService(); 
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
		session.removeAttribute("reBean");
		session.removeAttribute("sysinfo");
		session.removeAttribute("reInvestItems");
		session.removeAttribute("optmap");
		session.removeAttribute("IOLV1");
		session.removeAttribute("IOLV2");
		session.removeAttribute("sicOption");
		
		String reInvestNo=DataUtil.nulltoempty(request.getParameter("reinvest"));
		String investNo=request.getParameter("investNo").isEmpty()?null:request.getParameter("investNo");
		OFIReInvestList reBean=reSer.selectbean(investNo,reInvestNo);
		investNo=reBean.getInvestNo();
		OFIInvestList bean=listSer.select(investNo);
		if(bean==null||investNo==null||investNo.isEmpty()){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您選取的資料已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/listnotfilled.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		Map<String,String> sysinfo=omdSer.getSysBaseInfo(investNo);
		String prefix=investNo.substring(0, 1).equals("4")?"（陸分）":"（陸）";
		sysinfo.put("prefix", prefix);
		
		session.setAttribute("reBean", reBean);
		session.setAttribute("sysinfo", sysinfo);
		session.setAttribute("sic", reSicSer.getTWSICSelected(reInvestNo));
		session.setAttribute("optmap", optmap);
		session.setAttribute("IOLV1", levelone);
		session.setAttribute("IOLV2", leveltwo);
		session.setAttribute("sicOption", reSicSer.getTWSICList());
		session.setAttribute("sicMap", twsic.getTWSICMap());
		
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/cnfdi/reinvestment.jsp");		
	}
}
