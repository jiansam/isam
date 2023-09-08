package com.isam.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.service.MoeaicDataService;

public class ApprovalShowDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String idno = request.getParameter("idno");
		String investNo = request.getParameter("investNo");
		String investor = request.getParameter("investor");
		String cnName = request.getParameter("cnName");
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("idno", idno);
		map.put("investNo", investNo);
		map.put("investor", investor);
		map.put("cnName", cnName);
		
		HttpSession session = request.getSession();
		session.removeAttribute("approvalDetail");
		session.removeAttribute("apInfo");
		
		MoeaicDataService ser = new MoeaicDataService();
		List<List<String>> approvalDetail = ser.selectWebSRC(idno, investNo);
		
		session.setAttribute("approvalDetail", approvalDetail);
		session.setAttribute("apInfo", map);
		String path = request.getContextPath();
		response.sendRedirect(path + "/approval/approvaldetail.jsp");
	}
	
}
