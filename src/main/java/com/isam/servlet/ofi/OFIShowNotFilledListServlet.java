package com.isam.servlet.ofi;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.helper.DataUtil;
import com.isam.service.ofi.OFIReceiveNoListService;

public class OFIShowNotFilledListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIReceiveNoListService listSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		listSer = new OFIReceiveNoListService();
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
		session.removeAttribute("filledlist");
		session.removeAttribute("filledterms");
		session.removeAttribute("filledpnames");
		
		Map<String,String> terms=new HashMap<String, String>();		
		String pname=DataUtil.fmtSearchItem(request.getParameter("pname"), "%");
		String receiveNo=DataUtil.nulltoempty(request.getParameter("receiveNo"));
		terms.put("pname",pname);
		terms.put("receiveNo", receiveNo);
		
		List<Map<String,String>> filledlist=listSer.getIsFilledList(pname,receiveNo.isEmpty()?null:receiveNo);
		List<Map<String,String>> list=listSer.getIsFilledList("%",null);
		Set<String> set=new TreeSet<String>();
		for(int i=0;i<list.size();i++){
			Map<String,String> sub=list.get(i);
			set.add(sub.get("pname"));
		}
		for (Entry<String, String> m:terms.entrySet()) {
			terms.put(m.getKey(), m.getValue().replaceAll("%", ""));
		}
		
		session.setAttribute("filledlist", filledlist);
		session.setAttribute("filledterms", terms);
		session.setAttribute("filledpnames", set);
		
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/cnfdi/filledlist.jsp");		
	}
}
