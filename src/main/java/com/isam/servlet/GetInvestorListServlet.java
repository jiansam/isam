package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

import com.isam.dao.InvestorDAO;
import com.isam.helper.DataUtil;

public class GetInvestorListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String temp = request.getParameter("investor")==null?"":request.getParameter("investor").trim();
		String IDNO = request.getParameter("IDNO")==null?"%":DataUtil.fmtSearchItem(request.getParameter("IDNO").trim(),"");;

		String investor =DataUtil.fmtSearchItem(  java.net.URLDecoder.decode(temp,"UTF-8") ,"%");
		if(investor=="%"&&IDNO=="%"){
			return;
		}
		
		JSONArray jarray = new JSONArray();
		InvestorDAO dao = new InvestorDAO();
		
		List<List<String>> list = dao.select(IDNO, investor);
		for (List<String> s:list) {
			jarray.add(s);
		}
		
//		System.out.println(jarray.toString());
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(jarray.toJSONString());
		out.close();
	}
}
