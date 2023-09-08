package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.LoginRecord;
import com.isam.bean.UserMember;
import com.isam.dao.WebFolderAuthority;
import com.isam.helper.DataUtil;
import com.isam.service.UserHelp;

public class GetEditUserServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("recordlogin");
    	session.removeAttribute("userbean");
    	session.removeAttribute("webFolder");
    	
	    String idMember=request.getParameter("user");	
	    
	    WebFolderAuthority webFolder=new WebFolderAuthority();
	    UserHelp user = new UserHelp();
	    UserMember bean = null;
	    boolean flag = false;
	    if(DataUtil.isEmpty(idMember)){
	    	flag=true;
	    }else{
	    	bean = user.isIdMemberExist(idMember);
	    	if(bean==null){
	    		flag=true;
	    	}
	    }
	    if(flag){
	    	request.setCharacterEncoding("UTF-8");
	    	response.setContentType("text/html;charset=UTF-8");
	    	PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您要修改的使用者資料不存在!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/admin/userlist.jsp';</script>");
			out.flush();
			out.close();
			return;
	    }else{
	    	List<LoginRecord> loginRecord = user.getloginRecord(idMember);
	    	session.setAttribute("recordlogin", loginRecord);
	    	session.setAttribute("userbean", bean);
	    	List<String> urlCode = webFolder.getMenuItemByIdMember(idMember);
	    	session.setAttribute("webFolder", urlCode);
	    	String path = request.getContextPath();
	    	response.sendRedirect(path + "/console/admin/edituser.jsp");
	    }
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
 
}
