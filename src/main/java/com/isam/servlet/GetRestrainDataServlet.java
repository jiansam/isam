package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.isam.bean.MoeaicData;
import com.isam.helper.ApplicationAttributeHelper;
import com.isam.service.MoeaicDataService;

public class GetRestrainDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*	2019.6.6.dasin : changed for consistency.		 */
		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		
		String IDNO = request.getParameter("serno");
//		System.out.println("temp:"+IDNO);
		if(IDNO==null){
			return;
		}
		
		JSONArray jarray = new JSONArray();
//		JSONObject investNos = new JSONObject();
		MoeaicDataService ser = new MoeaicDataService();
		List<String> getCNNameStrs = ser.getCNNameStrs(IDNO);
		Set<String> set = new HashSet<String>();
		for(String s:getCNNameStrs){
			if(InvestNoToName.containsKey(s)){
				set.add(s);
			}
		}
		
		JSONObject obj = new JSONObject();
		JSONArray investNos = new JSONArray();
		List<List<String>> tmplist = new  ArrayList<List<String>>();
		for(String s:set){
			List<String> tmp = new  ArrayList<String>();
			tmp.add(s);
			tmp.add(InvestNoToName.get(s));
			tmp.add("1");
			tmplist.add(tmp);
		}
		investNos.addAll(tmplist);
		obj.put("cnnames", investNos);
		
//		List<List<String>> relist = ser.selectByInvestNoIDNONew("",IDNO);
//		obj.put("receive", relist);
		List<MoeaicData> relist = ser.selectByInvestNoIDNO("",IDNO);
		JSONArray recevice = new JSONArray();
		for(MoeaicData s:relist){
			List<String> tmp = new  ArrayList<String>();
			tmp.add(s.getRespDate());
			tmp.add(s.getReceiveNo());
			tmp.add(s.getAppName());
			tmp.add("1");
			recevice.add(tmp);
		}
		obj.put("receive", recevice);
		jarray.add(obj);
		
//		System.out.println(jarray.toString());
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(jarray.toJSONString());
//		System.out.println("before out.close");
		out.close();
	}
}
