package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.isam.helper.ApplicationAttributeHelper;

public class GetInvestNoNameServlet extends HttpServlet {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String temp = request.getParameter("investNo");
//		System.out.println("temp:"+temp);
		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		
		JSONArray jarray = new JSONArray();
		if(InvestNoToName.containsKey(temp)){
			String investNo = temp;
			if(InvestNoToName.containsKey(investNo)){
				String cnName=InvestNoToName.get(investNo);
				JSONObject investment = new JSONObject();
				investment.put("investNo", investNo);
				investment.put("cnName", cnName);
				jarray.add(investment);
			}
			
			System.out.println(investNo + ":"  + InvestNoToName.containsKey(investNo));
		}
//		System.out.println("temp2:"+temp);
		
//		System.out.println(jarray.toString());
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(jarray.toJSONString());
		out.close();

	}

}
