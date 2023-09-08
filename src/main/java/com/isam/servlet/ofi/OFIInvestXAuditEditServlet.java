package com.isam.servlet.ofi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.OFIInvestList;
import com.isam.bean.OFIInvestNoXAudit;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.ofi.OFIInvestListService;
import com.isam.service.ofi.OFIInvestNoXAuditService;

public class OFIInvestXAuditEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIInvestListService listSer;
	private OFIInvestNoXAuditService aSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		listSer = new OFIInvestListService();
		aSer = new OFIInvestNoXAuditService();
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
		
		OFIInvestList bean=listSer.select(investNo);
		if(bean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('此資料無法編輯，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
			out.flush();
			out.close();
			return;
		}
		Timestamp time=DataUtil.getNowTimestamp();
		/*取得不訂長度和未知的RsType name*/
		aSer.delete(investNo);
		Enumeration<?> pNames=request.getParameterNames();
		List<OFIInvestNoXAudit> beans = new ArrayList<OFIInvestNoXAudit>();
		while(pNames.hasMoreElements()){  
			String name=(String)pNames.nextElement();
			if(name.startsWith("audit")){
				String key = name.trim().replace("audit", "");
				String[] strs=request.getParameterValues(name);
				if(strs!=null){
					for (int i = 0; i < strs.length; i++) {
						OFIInvestNoXAudit sub= new OFIInvestNoXAudit();
						sub.setInvestNo(investNo);
						sub.setAuditCode(key);
						sub.setValue(strs[i]);
						sub.setSeq(i+1);
						sub.setCreatetime(time);
						sub.setCreateuser(updateuser);
						beans.add(sub);
						/*if(strs[i].length()>0){
							beans.add(sub);
						}*/
					}
				}
			}
		} 
		if(beans.size()>0){
			aSer.insert(beans);
		}
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>alert('稽核資料已更新!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
		out.flush();
		out.close();
		return;
	}
}
