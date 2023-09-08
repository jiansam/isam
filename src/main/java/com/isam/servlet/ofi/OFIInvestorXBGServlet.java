package com.isam.servlet.ofi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.OFIInvestorList;
import com.isam.bean.OFIInvestorXBG;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.ofi.OFIInvestorListService;
import com.isam.service.ofi.OFIInvestorXBGService;

public class OFIInvestorXBGServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIInvestorListService listSer;
	private OFIInvestorXBGService bgSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		listSer = new OFIInvestorListService();
		bgSer = new OFIInvestorXBGService();
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
		
		String investorSeq=DataUtil.nulltoempty(request.getParameter("investorSeq"));

		String BG1Note=DataUtil.nulltoempty(request.getParameter("BG1Note"));
		String BG2Note=DataUtil.nulltoempty(request.getParameter("BG2Note"));
		String[] BG1=request.getParameterValues("BG1");
		String[] BG2=request.getParameterValues("BG2");
		
		OFIInvestorList bean=listSer.select(investorSeq);
		if(bean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('此資料無法編輯，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvestform.jsp?investorSeq="+investorSeq+"';</script>");
			out.flush();
			out.close();
			return;
		}
		Timestamp time=DataUtil.getNowTimestamp();
		
		bgSer.delete(investorSeq);
		List<OFIInvestorXBG> beans=new ArrayList<OFIInvestorXBG>();
		
		if(BG1!=null){
			for (int i = 0; i < BG1.length; i++) {
				OFIInvestorXBG bg= new OFIInvestorXBG();
				bg.setInvestorSeq(investorSeq);
				bg.setBgType("BG1");
				bg.setValue(BG1[i]);
				bg.setSeq(i+1);
				bg.setCreatetime(time);
				bg.setCreateuser(updateuser);
				beans.add(bg);
			}
		}
		if(BG2!=null){
			for (int i = 0; i < BG2.length; i++) {
				OFIInvestorXBG bg= new OFIInvestorXBG();
				bg.setInvestorSeq(investorSeq);
				bg.setBgType("BG2");
				bg.setValue(BG2[i]);
				bg.setSeq(i+1);
				bg.setCreatetime(time);
				bg.setCreateuser(updateuser);
				beans.add(bg);
			}
		}
		if(!BG1Note.isEmpty()){
			OFIInvestorXBG bg= new OFIInvestorXBG();
			bg.setInvestorSeq(investorSeq);
			bg.setBgType("BG1Note");
			bg.setValue(BG1Note);
			bg.setSeq(1);
			bg.setCreatetime(time);
			bg.setCreateuser(updateuser);
			beans.add(bg);
		}
		if(!BG2Note.isEmpty()){
			OFIInvestorXBG bg= new OFIInvestorXBG();
			bg.setInvestorSeq(investorSeq);
			bg.setBgType("BG2Note");
			bg.setValue(BG2Note);
			bg.setSeq(1);
			bg.setCreatetime(time);
			bg.setCreateuser(updateuser);
			beans.add(bg);
		}
		if(!beans.isEmpty()){
			bgSer.insert(beans);
		}
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>alert('基本資料已更新!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvestform.jsp?investorSeq="+investorSeq+"';</script>");
		out.flush();
		out.close();
		return;
	}
}
