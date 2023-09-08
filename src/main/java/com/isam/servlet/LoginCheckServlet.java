package com.isam.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class LoginCheckServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public void init() throws ServletException {
		super.init();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		Map<String, String> error=new HashMap<String, String>();
		session.removeAttribute("userInfo");
		session.removeAttribute("memberUrls");
		session.removeAttribute("checkUrls");
	    String idMember=request.getParameter("account");	
		String pWD=request.getParameter("pWD");
		UserHelp user = new UserHelp();
		UserMember bean = null;
		if(DataUtil.isEmpty(idMember)||DataUtil.isEmpty(pWD)||!DataUtil.isMatchPtn(pWD,"[A-Za-z0-9\\p{Punct}]{6,}")){
			error.put("inputER", "帳號或密碼輸入錯誤");
		}else{
			List<UserMember> list = user.select(idMember, "1");
			if(list.size()==0){
				error.put("noER", "查無此帳號");
			}else{
				bean = user.select(idMember, "1").get(0);
				if(bean==null){
					error.put("noER", "查無此帳號");
				}else{
					if(user.checkLogin(bean.getUserPwd(), pWD)){
						session.setAttribute("userInfo", bean);
					}else{
						error.put("inputER", "帳號或密碼輸入錯誤");
					}
				}
			}
		}
		LoginRecord rd= new LoginRecord();
		String ip=getIpAddr(request);
		if(!DataUtil.isEmpty(idMember)){
			rd.setIdMember(idMember);
		}
		rd.setLoginIP(ip);
		rd.setLoginTime(DataUtil.getNowTimestamp());
		if(error.size()!=0){
			rd.setLoginResult("N");
			user.loginRecord(rd);
			request.setAttribute("errors", error);
			request.getRequestDispatcher("/isam.jsp").forward(request, response);
		}else{
			rd.setLoginResult("Y");
			user.loginRecord(rd);
			session.setAttribute("loginRecord", user.getloginRecord(idMember));
			
			WebFolderAuthority webFolder=new WebFolderAuthority();
			List<String> passList = webFolder.getURLByIdMember(idMember);
			List<String> checkUrls = webFolder.checkList(passList);
			session.setAttribute("checkUrls", checkUrls);
			List<String> memberUrls=webFolder.getMenuItemByIdMember(idMember);
			session.setAttribute("memberUrls", memberUrls);
//			System.out.println("memberUrls:"+memberUrls.size());
			
//			String target= (String) session.getAttribute("target");
			String path = request.getContextPath();
//			if(target!=null){
//				session.removeAttribute("target");
//				response.sendRedirect(path + target);
//			}else{
				response.sendRedirect(path + "/useredit.jsp");
//			}
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
	private String getIpAddr(HttpServletRequest request) {
	      String ip = request.getHeader("x-forwarded-for");
	      if(ip ==null || ip.length() ==0 ||  "unknown".equalsIgnoreCase(ip)) {      
	          ip = request.getHeader("Proxy-Client-IP");      
	      }      
	      if(ip ==null || ip.length() ==0 ||  "unknown".equalsIgnoreCase(ip)) {      
	          ip = request.getHeader("WL-Proxy-Client-IP");      
	       }      
	     if(ip ==null || ip.length() ==0 ||  "unknown".equalsIgnoreCase(ip)) {      
	           ip = request.getRemoteAddr();      
	      }      
	     if(ip!=null&&ip.indexOf(",")!=-1){
	    	 ip=ip.split(",")[0];
	     }
	     return ip;      
	}
}
