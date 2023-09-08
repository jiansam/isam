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
import org.json.simple.JSONObject;

import com.isam.service.ProjectReportService;
import com.isam.service.ProjectService;

public class GetProjectReportPivotServlet extends HttpServlet {
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
		String temp = request.getParameter("serno");
		String investNo = request.getParameter("investNo");
		String idNO = request.getParameter("idNO");
		ProjectService pRer = new ProjectService();
		int serno;
//		System.out.println(idNO+":"+investNo);
		if(investNo==null&&idNO==null){
			if(temp==null||temp.isEmpty()){
				return;
			}else{
				serno=Integer.valueOf(temp);
			}
		}else{
			serno = pRer.getSerno(investNo, idNO);
//			System.out.println(serno);
		}
		ProjectReportService ser = new ProjectReportService();
		ProjectService pser = new ProjectService();
		investNo = pser.getProjectBySerno(String.valueOf(serno)).getInvestNo();
		
		List<List<String>> lists =ser.getReportPivot(serno);
		JSONObject obj1 = new JSONObject();
		obj1.put("noNeed", ser.getNoNeedMap(investNo));
		JSONArray ary = new JSONArray();
		ary.addAll(lists);
		JSONArray jarray = new JSONArray();
		jarray.add(obj1);
		jarray.add(ary);
//		System.out.println(jarray.toString());
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(jarray.toJSONString());
		out.close();
		
/*		List<List<String>> lists =ser.getReportPivot(serno);
		
		JSONArray jarray = new JSONArray();
		jarray.addAll(lists);
//		System.out.println(jarray.toString());
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(jarray.toJSONString());
		out.close();
*/	
		}

}
