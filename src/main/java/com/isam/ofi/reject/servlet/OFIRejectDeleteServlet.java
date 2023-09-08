package com.isam.ofi.reject.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.ofi.reject.service.OFIRejectService;
import com.isam.ofi.reject.service.OFIRejectXAgentService;
import com.isam.ofi.reject.service.OFIRejectXApplicantService;
import com.isam.ofi.reject.service.OFIRejectXTWSICService;

public class OFIRejectDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String serno = DataUtil.nulltoempty(request.getParameter("serno"));
		if(serno.isEmpty()){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('系統出現無法預期的問題，請通知系統工程師！');window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/reject/showlist.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		
		OFIRejectService rSer=new OFIRejectService();
		rSer.delete(serno);
		
		OFIRejectXTWSICService sicSer=new OFIRejectXTWSICService();
		sicSer.delete(serno);
		
		OFIRejectXApplicantService aplSer=new OFIRejectXApplicantService();
		aplSer.deleteBySerno(serno);
		
		OFIRejectXAgentService agSer=new OFIRejectXAgentService();
		agSer.deleteBySerno(serno);
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>alert('申請資料已刪除！');window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/reject/showlist.jsp';</script>");
		out.flush();
		out.close();
		return;
		
	}

}
