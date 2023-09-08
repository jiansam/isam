package com.isam.ofi.reject.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.helper.DataUtil;
import com.isam.ofi.reject.bean.OFIRejectCompany;
import com.isam.ofi.reject.service.OFIRejectCompanyService;
import com.isam.ofi.reject.service.OFIRejectService;
import com.isam.ofi.reject.service.OFIRejectXApplicantService;
import com.isam.ofi.reject.service.OFIRejectXTWSICService;
import com.isam.service.ofi.OFIInvestNoXTWSICService;
import com.isam.service.ofi.OFIInvestOptionService;

public class OFIShowRejectFormServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIRejectService reSer;
	private OFIRejectXTWSICService rsicSer;
	private OFIRejectCompanyService recSer;
	private OFIRejectXApplicantService appSer;
	private OFIInvestOptionService optSer;
	private OFIInvestNoXTWSICService sicSer;
	private Map<String,Map<String,String>> opt;
	Map<String,String> twsic;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		reSer=new OFIRejectService();
		recSer = new OFIRejectCompanyService();
		optSer = new OFIInvestOptionService();
		sicSer=new OFIInvestNoXTWSICService();
		rsicSer=new OFIRejectXTWSICService();
		appSer=new OFIRejectXApplicantService();
		opt =optSer.select();
		twsic=new TreeMap<String, String>();
		twsic.putAll(sicSer.getTWSICMap());
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
		session.removeAttribute("rejectObj");
		session.removeAttribute("rejbean");
		session.removeAttribute("opt");
		session.removeAttribute("twsic");
		session.removeAttribute("infoMap");
		session.removeAttribute("rSic");
		session.removeAttribute("appls");
		
		Map<String,String> info=new HashMap<String, String>();
		String serno=DataUtil.nulltoempty(request.getParameter("serno"));
		String cNo=DataUtil.nulltoempty(request.getParameter("cNo"));
		String isExists=DataUtil.nulltoempty(request.getParameter("isExists"));
		String editType=DataUtil.nulltoempty(request.getParameter("editType"));
		info.put("editType", editType);
		info.put("isExists", isExists);
		info.put("serno", serno);
		
		OFIRejectCompany bean=recSer.select(cNo);
		if(bean!=null){
			session.setAttribute("rejbean", bean);
		}
		if(!serno.isEmpty()){
			session.setAttribute("rejectObj", reSer.select(serno));
			session.setAttribute("rSic", rsicSer.select(serno));
			session.setAttribute("appls", appSer.getRejectApplicant(serno));
		}
		session.setAttribute("opt", opt);
		session.setAttribute("twsic", twsic);
		session.setAttribute("infoMap", info);
		
		String path = request.getContextPath();
		if(request.getServletPath().startsWith("/console")){
			response.sendRedirect(path + "/console/reject/rejectform.jsp");		
		}else{
			response.sendRedirect(path + "/reject/rejectform.jsp");		
		}
	}
}
