package com.isam.ofi.reject.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

import com.isam.helper.DataUtil;
import com.isam.ofi.reject.service.OFIRejectCompanyService;

public class GetCNoByCNameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIRejectCompanyService recSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		recSer = new OFIRejectCompanyService();
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
		String temp1 = DataUtil.nulltoempty(request.getParameter("cname"));
		String cNo = request.getParameter("cNo");
		String type = DataUtil.nulltoempty(request.getParameter("type"));
//		System.out.println("temp:"+temp1+";temp:"+temp2);
		JSONArray jarray = new JSONArray();
		if(type.isEmpty()){
			List<String> list=recSer.getCNoListByCName(temp1,cNo);
			jarray.addAll(list);
		}else{
			jarray.add(recSer.getCNoByCName(temp1));
			jarray.add(cNo);
		}
//		System.out.println(jarray.toString());
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(jarray.toJSONString());
		out.close();

	}

}
