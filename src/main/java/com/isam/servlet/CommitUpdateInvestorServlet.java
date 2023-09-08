package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;

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
import com.isam.service.CommitXOfficeService;

public class CommitUpdateInvestorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommitInvestorService ser;
	private CommitXOfficeService office;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ser= new CommitInvestorService();
		office = new CommitXOfficeService();
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
		String idno=DataUtil.nulltoempty(request.getParameter("serno"));
		String note=DataUtil.nulltoempty(request.getParameter("notes"));

		String[] restrainType=request.getParameterValues("restrainType");
		if(office.isExists(idno)){
			office.delete(idno);
		}
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
			cibean.setNote(note);
			cibean.setUpdatetime(date);
			cibean.setUpdateuser(updateuser);
			ser.update(cibean);
			if(restrainType!=null){
				/*應該要驗證restrainType有沒有在資料庫內，待補*/
				office.insertBatch(restrainType, idno,date,updateuser);
			}
			/*String contact=DataUtil.nulltoempty(request.getParameter("contact"));
			String tel=DataUtil.nulltoempty(request.getParameter("tel"));
			CommitInvestorXContactsService ser= new CommitInvestorXContactsService();
			CommitInvestorXContacts cbean=new CommitInvestorXContacts();
			cbean.setIDNO(idno);
			cbean.setContact(contact);
			cbean.setTel(tel);
			cbean.setUpdatetime(date);
			cbean.setUpdateuser(updateuser);
			ser.delete(idno);
			ser.insert(cbean);*/
			
			StringBuilder sb = new StringBuilder();
			sb.append(path).append("/console/commit/showcommitdetail.jsp?serno=").append(idno).append("&updateOK=0");
			response.sendRedirect(sb.toString());
		}
		
	}
}
