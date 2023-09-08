package com.isam.servlet.ofi;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.ofi.OFIManageClassifyService;

public class OFIInvestXManageScoreEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIManageClassifyService mcSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		mcSer = new OFIManageClassifyService();
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
		
		String nscore=DataUtil.nulltoempty(request.getParameter("nscore"));
		String investNo=DataUtil.nulltoempty(request.getParameter("investNo"));
		String year=DataUtil.nulltoempty(request.getParameter("year"));
		
		if(nscore.isEmpty()&&!DataUtil.isMatchPtn(nscore,"[0-9]*")){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('修改後管理等級須為正整數或空值，請重新確認!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
			out.flush();
			out.close();
			return;
		}
		if(!year.isEmpty()){
			year=DataUtil.addZeroForNum(year, 3);
		}
		if(nscore.isEmpty()){
			nscore=null;
		}
		int count=mcSer.update(year, investNo, nscore, updateuser);
		if(count!=-1){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('資料已更新!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
			out.flush();
			out.close();
			return;
		}

	}
}
