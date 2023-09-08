package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.helper.ThreeDes;
import com.isam.service.UserHelp;

public class ChangePWDServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Map<String, String> errors=new HashMap<String, String>();
	    String idMember=request.getParameter("account");	
		String oldStr=request.getParameter("oldStr");
		String newStr=request.getParameter("newStr");
		String checkStr=request.getParameter("checkStr");
		
		UserHelp user = new UserHelp();
		UserMember bean = (UserMember) session.getAttribute("userInfo");
		
		if(DataUtil.isEmpty(oldStr)||DataUtil.isEmpty(newStr)||DataUtil.isEmpty(checkStr)){
			if(DataUtil.isEmpty(oldStr)){
				errors.put("emptyOld", "請輸入舊的密碼");
			}
			if(DataUtil.isEmpty(newStr)){
				errors.put("emptyNewStr", "請輸入新密碼");
			}else if(DataUtil.isEmpty(checkStr)){
				errors.put("emptyCheckStr", "請再次輸入新密碼");
			}
		}else{
			if(!newStr.equals(checkStr)){
				errors.put("notEqual", "您兩次輸入的新密碼不一致，請重新輸入");
			}else if(!bean.getUserPwd().equals(ThreeDes.toMD5(oldStr))){
				errors.put("oldError", "舊密碼錯誤，請重新輸入");
			}else{
				if(!DataUtil.isMatchPtn(newStr,"[A-Za-z0-9\\p{Punct}]{6,}")){
					errors.put("noMatch", "您輸入的新密碼不符合密碼的格式，請以6個以上英數字或符號組成新密碼");
				}else{
					bean.setUpdtime(DataUtil.getNowTimestamp());
					bean.setUserPwd(ThreeDes.toMD5(newStr));
					bean.setIdEditor(idMember);
				}
			}
		}
		
		if(errors.size()>0){
			request.setAttribute("errors", errors);
			request.getRequestDispatcher("/useredit.jsp").forward(request, response);
		}else{
			boolean result = user.updateUserMember(bean);
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			if(result){
				out.print("<script language='javascript'>alert('密碼已經更新，請重新登入!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/logout.jsp';</script>");
			}else{
				out.print("<script language='javascript'>alert('密碼更新失敗，請洽網站管理員'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/useredit.jsp';</script>");
			}
			out.flush();
			out.close();
			return;
		}

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
 
}
