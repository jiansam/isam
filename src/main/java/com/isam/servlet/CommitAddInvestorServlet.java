package com.isam.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.CommitInvestor;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.CommitInvestorService;

public class CommitAddInvestorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommitInvestorService ser;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ser= new CommitInvestorService();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String idno=DataUtil.nulltoempty(request.getParameter("serno"));
		UserMember bean = (UserMember) session.getAttribute("userInfo");
		String updateuser = bean.getIdMember();
//		System.out.println("commitAddInvestor:"+idno);
		
		String path = request.getContextPath();
		if(ser.isExists(idno)){
			response.sendRedirect(path + "/console/commit/showcommitdetail.jsp?serno="+idno);
		}else{
			java.sql.Timestamp date= DataUtil.getNowTimestamp();
			CommitInvestor cibean = new CommitInvestor();
			cibean.setIDNO(idno);
			cibean.setNote("");
			cibean.setUpdatetime(date);
			cibean.setUpdateuser(updateuser);
			cibean.setCreatetime(date);
			cibean.setCreateuser(updateuser);
			cibean.setEnable("1");
			cibean.setNeedAlert("0");
			ser.insert(cibean);
			response.sendRedirect(path + "/console/commit/showcommitdetail.jsp?add=new&serno="+idno);
		}
		
	}
}
