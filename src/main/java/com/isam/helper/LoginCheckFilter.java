package com.isam.helper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.UserMember;

public class LoginCheckFilter implements Filter {
	private Collection<String> url = new ArrayList<String>();

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession();
		UserMember bean = (UserMember) session.getAttribute("userInfo");
		String servletPath= request.getServletPath();
		if(mustLogin(servletPath)){
//			System.out.println(servletPath);
			if(bean==null){
//				System.out.println("1:"+servletPath);
//				Map<String,String> map = new HashMap<String, String>();
//				Enumeration<?> pNames=request.getParameterNames();
//				while(pNames.hasMoreElements()){ 
//					String name=(String)pNames.nextElement();
//					String value=request.getParameter(name)==null?"":request.getParameter(name);
//					map.put(name, value);
//				}
//				session.setAttribute("target", servletPath);
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("<script language='javascript'>alert('您已被系統登出，請重新登入!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/logout.jsp';</script>");
				out.flush();
				out.close();
				return;
//				request.getRequestDispatcher("/isam.jsp").forward(request, response);
			}else{
//				List<String> memberUrls=(List<String>) session.getAttribute("memberUrls");
				List<String> checkList= (List<String>) session.getAttribute("checkUrls");
				if(checkList.size()>0){
					if(cannotPass(servletPath,checkList)){
//						System.out.println("2:"+servletPath);
						request.setCharacterEncoding("UTF-8");
						response.setContentType("text/html;charset=UTF-8");
						PrintWriter out = response.getWriter();
						out.print("<script language='javascript'>alert('您沒有查閱的權限，如須取得使用權限請洽網站管理者!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/logout.jsp';</script>");
						out.flush();
						out.close();
						return;
					}else{
//						request.setAttribute("navigation", "in test");
						chain.doFilter(request, response);
					}
				}else{
//					request.setAttribute("navigation", "in test");
					chain.doFilter(request, response);
				}
			}
		}else{
//			System.out.println("3:"+servletPath);
			chain.doFilter(request, response);
		}
	}
	@SuppressWarnings("unused")
	private FilterConfig config = null;
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
		// 準備在init()內讀入起始參數,讀入的起始參數會存放在url內
		Enumeration<String> e = config.getInitParameterNames();
		while (e.hasMoreElements()) {
			String name = e.nextElement();
			url.add(config.getInitParameter(name));
		}
	}
	public void destroy() {
		this.config = null;
	}
	private boolean mustLogin(String servletPath) {
		boolean login = true;
		if(servletPath==null||!servletPath.endsWith(".jsp")){
			login = false;
		}
		for (String sURL : url) {
			if (servletPath.equals(sURL)) {
				login = false;
				break;
			}
		}
		return login;
	}
	private boolean cannotPass(String servletPath,List<String> url) {
		boolean result = false;
		for (String sURL : url) {
			if (sURL.endsWith("*")) {
				sURL = sURL.substring(0, sURL.length() - 1);
				if (servletPath.startsWith(sURL)) {
					result = true;
					break;
				}
			} else {
				if (servletPath.equals(sURL)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}
	/*private List<String> getNavigation(String servletPath,List<String> memberUrls) {
		List<String> list = new ArrayList<String>();
		if(servletPath.equals("/useredit.jsp")||servletPath.startsWith("/console/")){
			list.add("資料維護");
			if(servletPath.equals("/useredit.jsp")){
				list.add("修改個人密碼");
			}else if(servletPath.startsWith("/console/admin/")){
				list.add("網站使用者管理");				
			}else{
				list.add("後台管理");				
			}
		}else if(servletPath.startsWith("/console/")){
			list.add("");
		}
		return list;
	}*/
}
