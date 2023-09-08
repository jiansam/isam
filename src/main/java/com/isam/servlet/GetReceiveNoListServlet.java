package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.isam.bean.MoeaicData;
import com.isam.service.MoeaicDataService;
import com.isam.service.ProjectKeyHelp;

public class GetReceiveNoListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Map<String, String> IDNOToName;
	Map<String, String> IDNOToMain;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ProjectKeyHelp help = new ProjectKeyHelp();
		IDNOToMain = help.getIDNoToMain();
		IDNOToName = help.getIDNOToName();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		String temp1 = request.getParameter("investNo");
		String temp2 = request.getParameter("IDNO");
//		System.out.println("temp:"+temp1+";temp:"+temp2);
		if(temp1==null||temp1.trim().length()!=6||temp2==null){
			return;
		}
		
		String investNo = temp1;
		String IDNO=IDNOToMain.get(temp2);
		
		JSONArray jarray = new JSONArray();
		
		MoeaicDataService ser = new MoeaicDataService();
		if(investNo!=null&&IDNO!=null){
//			List<MoeaicData> relist = ser.selectByInvestNoIDNONew(investNo,IDNO);
			List<MoeaicData> relist = ser.selectByInvestNoIDNO(investNo,IDNO);
			for(MoeaicData s:relist){
				JSONObject receive = new JSONObject();
				receive.put("respDate",s.getRespDate());
				receive.put("receiveNo", s.getReceiveNo());
				receive.put("appName", s.getAppName());
				jarray.add(receive);
			}
		}
		
//		System.out.println(jarray.toString());
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(jarray.toJSONString());
		out.close();

	}

}
