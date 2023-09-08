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
import com.isam.helper.ApplicationAttributeHelper;
import com.isam.service.MoeaicDataService;
import com.isam.service.ProjectKeyHelp;
import com.isam.service.ProjectService;

public class GetMoeaicByInvestNo extends HttpServlet {
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
		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		
		String temp = request.getParameter("investNo");
//		System.out.println("temp:"+temp);
		if(temp==null||temp.trim().length()!=6){
			return;
		}
		String investNo = temp;
		JSONArray jarray = new JSONArray();
		JSONObject investment = new JSONObject();
//		investment.put("investNo", investNo);
		investment.put("cnName", InvestNoToName.get(investNo));
		jarray.add(investment);
//		System.out.println("temp2:"+temp);
		
		ProjectService proj = new ProjectService();
		MoeaicDataService ser = new MoeaicDataService();
		
		/*如果要抓全部的話，要用MoeaicDataService的，下面這行*/
//		List<String> list = ser.selectIDNOByInvestNo(investNo);
		List<String> list= proj.getIDNOByInvestNo(investNo);
		JSONArray investorAry = new JSONArray();
		for(String s:list){
			JSONObject investor = new JSONObject();
			investor.put("idno", IDNOToMain.get(s));
			investor.put("investor", IDNOToName.get(s));
			investorAry.add(investor);
		}
		jarray.add(investorAry);
//		System.out.println("temp3:"+temp);
		JSONArray reAry = new JSONArray();
//		MoeaicDataService ser = new MoeaicDataService();
		if(list.size()>0){
			List<MoeaicData> relist = ser.selectByInvestNoIDNO(temp,list.get(0));
//			System.out.println("list.get(0):"+list.get(0));
			for(MoeaicData s:relist){
				JSONObject receive = new JSONObject();
				receive.put("respDate",s.getRespDate());
				receive.put("receiveNo", s.getReceiveNo());
				receive.put("appName", s.getAppName());
				reAry.add(receive);
			}
		}
		jarray.add(reAry);
//		System.out.println(jarray.toString());
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(jarray.toJSONString());
		out.close();

	}

}
