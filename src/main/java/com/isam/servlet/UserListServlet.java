package com.isam.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.LoginRecord;
import com.isam.bean.UserMember;
import com.isam.service.UserHelp;

public class UserListServlet extends HttpServlet{
	Map<String,String> companyMap;
	private static final long serialVersionUID = 1L;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		companyMap = new HashMap<String, String>();
		companyMap.put("ibtech", "鐵橋數位科技");
		companyMap.put("moea", "投資審議委員會");
		companyMap.put("cier", "中華經濟研究院");
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("listuser");
		session.removeAttribute("listrecord");
		session.removeAttribute("companyMap");
		
		UserHelp user = new UserHelp();
		List<UserMember> listuser = user.select(null, null);
		List<LoginRecord> listrecord = user.getloginRecord(null);
		session.setAttribute("companyMap", companyMap);
		session.setAttribute("listuser", listuser);
		session.setAttribute("listrecord", listrecord);
		
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/admin/userlist.jsp");
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doGet(req, resp);
	}
 
}
