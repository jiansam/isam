package com.isam.servlet.ofi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.OFIContacts;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.ofi.OFIContactsService;

public class OFIInvestXContactEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIContactsService cSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		cSer = new OFIContactsService();
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
		
		String investNo=DataUtil.nulltoempty(request.getParameter("investNo"));
		String serno=DataUtil.nulltoempty(request.getParameter("serno"));
		String type=DataUtil.nulltoempty(request.getParameter("type"));
		String name=DataUtil.nulltoempty(request.getParameter("name"));
		String telNo=DataUtil.nulltoempty(request.getParameter("telNo"));
		
		Timestamp time=DataUtil.getNowTimestamp();
		if((type.equals("edit")||type.equals("add"))&&name.isEmpty()){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('聯絡人名稱不可為空白，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
			out.flush();
			out.close();
			return;
		}
		if(type.equals("delete")){
			if(serno.isEmpty()){
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("<script language='javascript'>alert('資料已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
				out.flush();
				out.close();
				return;
			}
			cSer.delete(serno);
		}else if(type.equals("edit")){
			if(investNo.isEmpty()){
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("<script language='javascript'>alert('陸資案號錯誤，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
				out.flush();
				out.close();
				return;
			}
			OFIContacts bean =cSer.selectbean(serno);
			bean.setCreatetime(time);
			bean.setCreateuser(updateuser);
			bean.setName(name);
			bean.setTelNo(telNo);
			cSer.update(bean);
		}else if(type.equals("add")){
			if(investNo.isEmpty()){
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("<script language='javascript'>alert('陸資案號錯誤，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
				out.flush();
				out.close();
				return;
			}
			OFIContacts bean =new OFIContacts();
			bean.setCreatetime(time);
			bean.setCreateuser(updateuser);
			bean.setName(name);
			bean.setTelNo(telNo);
			bean.setInvestNo(investNo);
			cSer.insert(bean);
		}
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>alert('資料已更新!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
		out.flush();
		out.close();
		return;
	}
}
