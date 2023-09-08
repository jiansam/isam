package com.isam.ofi.reject.servlet;

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
import com.isam.service.ofi.OFIInvestOptionService;

public class OFIListRejectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIRejectService rSer;
	private OFIInvestOptionService optSer;
	private Map<String,Map<String,String>> opt;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		rSer = new OFIRejectService();
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
		session.removeAttribute("opt");
		session.removeAttribute("rInfo");
		session.removeAttribute("rmm");
		session.removeAttribute("rejects");
		
		
		Map<String,String> info=new HashMap<String, String>();
		String cName=DataUtil.fmtSearchItem(request.getParameter("cName"), "%");
		String cApplicant=DataUtil.fmtSearchItem(request.getParameter("cApplicant"),"%");
		String decision=DataUtil.nulltoempty(request.getParameter("decision"));
		String MaxR=DataUtil.paramToTWYM(request.getParameter("MaxR"));
		String MinR=DataUtil.paramToTWYM(request.getParameter("MinR"));
		String MaxI=DataUtil.paramToTWYM(request.getParameter("MaxI"));
		String MinI=DataUtil.paramToTWYM(request.getParameter("MinI"));
		String[] rejectTypeTmp=request.getParameterValues("rejectType");
		String rejectType=DataUtil.nulltoempty(request.getParameter("rType"));
		if(rejectTypeTmp!=null){
			rejectType=DataUtil.fmtStrAryItem(rejectTypeTmp);
		}else if(!rejectType.isEmpty()){
			rejectType=DataUtil.fmtStrAryItem(rejectType.split(","));
		}
		
		info.put("cName", cName);
		info.put("cApplicant", cApplicant);
		info.put("MaxR", DataUtil.addRigthNineForNum(MaxR,7));
		info.put("MinR", DataUtil.addRigthZeroForNum(MinR,7));
		info.put("MaxI", DataUtil.addRigthNineForNum(MaxI,7));
		info.put("MinI", DataUtil.addRigthZeroForNum(MinI,7));
		info.put("rejectType", rejectType);
		info.put("decision", decision);
		
		List<Map<String, String>> rejects=rSer.getRejectsList(info);
		Map<String,String> rmm=rSer.getMAXMINDay();
		
		info.put("MaxR", MaxR.isEmpty()?DataUtil.addSlashToTWDate(rmm.get("MaxR")).substring(0, 6):DataUtil.addSlashToTWYM(MaxR));
		info.put("MinR", MinR.isEmpty()?DataUtil.addSlashToTWDate(rmm.get("MinR")).substring(0, 6):DataUtil.addSlashToTWYM(MinR));
		info.put("MaxI", MaxI.isEmpty()?DataUtil.addSlashToTWDate(rmm.get("MaxI")).substring(0, 6):DataUtil.addSlashToTWYM(MaxI));
		info.put("MinI", MinI.isEmpty()?DataUtil.addSlashToTWDate(rmm.get("MinI")).substring(0, 6):DataUtil.addSlashToTWYM(MinI));
		info.put("cApplicant", cApplicant.replaceAll("%", ""));
		info.put("cName", cName.replaceAll("%", ""));
		info.put("rejectType", rejectType.replaceAll("'", ""));
		
		session.setAttribute("rejects", rejects);
		session.setAttribute("rmm", rmm);
		session.setAttribute("opt", opt);
		session.setAttribute("rInfo", info);
		
		String path = request.getContextPath();
		if(request.getServletPath().startsWith("/console")){
			response.sendRedirect(path + "/console/reject/rejectlist.jsp");		
		}else{
			response.sendRedirect(path + "/reject/rejectlist.jsp");		
		}
	}
}
