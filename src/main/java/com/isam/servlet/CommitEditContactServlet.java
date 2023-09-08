package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.CommitInvestor;
import com.isam.bean.CommitInvestorXContacts;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.CommitInvestorService;
import com.isam.service.CommitInvestorXContactsService;
import com.isam.service.CommitInvestorXContactsXReceiveNoService;

public class CommitEditContactServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommitInvestorService ser;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ser= new CommitInvestorService();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		String cID=DataUtil.nulltoempty(request.getParameter("cID"));
		String idno=DataUtil.nulltoempty(request.getParameter("idno"));
		String type=DataUtil.nulltoempty(request.getParameter("type"));
		String contact=DataUtil.nulltoempty(request.getParameter("contact"));
		String tel=DataUtil.nulltoempty(request.getParameter("tel"));
		String reNos=DataUtil.nulltoempty(request.getParameter("reNos"));

		UserMember bean = (UserMember) session.getAttribute("userInfo");
		String updateuser = bean.getIdMember();
//		System.out.println("CommitUpdateInvestor:"+idno);
		
		String path = request.getContextPath();
		if(!ser.isExists(idno)){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('該企業已不存在!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommit.jsp';</script>");
			out.flush();
			out.close();
			return;
		}else{
			java.sql.Timestamp date= DataUtil.getNowTimestamp();
			CommitInvestor cibean = ser.select(idno);
			cibean.setUpdatetime(date);
			cibean.setUpdateuser(updateuser);
			ser.update(cibean);
			CommitInvestorXContactsService cxc= new CommitInvestorXContactsService();
			CommitInvestorXContactsXReceiveNoService crx= new CommitInvestorXContactsXReceiveNoService();
			
			if(type.equals("delete")){
				crx.delete(cID);
				cxc.delete(cID);
			}else if(type.equals("add")||type.equals("edit")){
				CommitInvestorXContacts cbean=new CommitInvestorXContacts();
				if(type.equals("add")){
					cbean.setIDNO(idno);
					cbean.setContact(contact);
					cbean.setTel(tel);
					cbean.setUpdatetime(date);
					cbean.setUpdateuser(updateuser);
					cID=cxc.insert(cbean);
				}else if(type.equals("edit")){
					cbean=cxc.selectByCID(cID);
					cbean.setContact(contact);
					cbean.setTel(tel);
					cbean.setUpdatetime(date);
					cbean.setUpdateuser(updateuser);
					cxc.update(cbean);
				}
				List<String> list=DataUtil.StrArytoListNoEmpty(reNos.split(","));
				crx.insert(cID, list);
			}
			
			
			StringBuilder sb = new StringBuilder();
			sb.append(path).append("/console/commit/showcommitdetail.jsp?serno=").append(idno).append("&updateOK=5");
			response.sendRedirect(sb.toString());
		}
		
	}
}
