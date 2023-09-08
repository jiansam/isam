package com.isam.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.Interviewone;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.InterviewoneService;

public class InterviewoneXReinvestmentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private InterviewoneService ioSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ioSer = new InterviewoneService();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		UserMember user = (UserMember) session.getAttribute("userInfo");
		String updateuser = user.getIdMember();
		
		String year=DataUtil.addZeroForNum(request.getParameter("year"), 3);
		String[] reinvestNos=request.getParameterValues("reinvestNo");
		Map<String,String> investMap=ioSer.getInvestNoByReInvestNo(year);
		List<Interviewone> list=new ArrayList<Interviewone>();
		Timestamp time=DataUtil.getNowTimestamp();
		for (int i = 0; i < reinvestNos.length; i++) {
//			System.out.println(reinvestNos[i]);
			Interviewone bean=new Interviewone();
			bean.setInvestNo(investMap.get(reinvestNos[i]));
			bean.setReInvestNo(reinvestNos[i]);
			bean.setYear(year);
			bean.setInterviewStatus("0");
			bean.setSurveyStatus("0");
			bean.setEnable("1");
			bean.setUpdatetime(time);
			bean.setUpdateuser(updateuser);
			bean.setCreatetime(time);
			bean.setCreateuser(updateuser);
			bean.setMsg("");
			list.add(bean);
		}
		
		ioSer.insert(list);
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/interviewone/showiolist.jsp?action=manage&year="+year);
	}
}
