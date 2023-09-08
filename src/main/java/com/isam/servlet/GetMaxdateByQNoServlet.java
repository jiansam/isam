package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.isam.helper.DataUtil;
import com.isam.service.InterviewoneManageService;

public class GetMaxdateByQNoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String qNo = DataUtil.nulltoempty(request.getParameter("qNo"));
		String serno = DataUtil.nulltoempty(request.getParameter("serno"));
//		System.out.println(qNo+":"+serno);
		
		JSONArray jarray = new JSONArray();
		InterviewoneManageService imSer= new InterviewoneManageService();
		JSONObject obj=new JSONObject();
		obj.put("following", imSer.checkFowllowing(qNo, serno));
		obj.put("receiveDate", imSer.checkMaxReceiveDate(qNo, serno));
		jarray.add(obj);
		
		
//		System.out.println(jarray.toString());
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(jarray.toJSONString());
		out.close();

	}

}
