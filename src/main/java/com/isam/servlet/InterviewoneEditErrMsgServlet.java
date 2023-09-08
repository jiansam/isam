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

import com.isam.bean.Interviewone;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.InterviewoneService;

public class InterviewoneEditErrMsgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private InterviewoneService ioSer;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ioSer = new InterviewoneService();
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
		UserMember user = (UserMember) session.getAttribute("userInfo");
		String updateuser = user.getIdMember();
		
		String qNo=DataUtil.nulltoempty(request.getParameter("qNo"));
		String msg=DataUtil.nulltoempty(request.getParameter("msg"));
		List<Interviewone> list=ioSer.select(null, qNo);
		Interviewone bean=null;
		if(!list.isEmpty()){
			bean=list.get(0);
		}
		/*取得基本資料，並驗證資料國內事業是否存在*/
		if(bean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('尚無詳細資料，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/interviewone/showiolist.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		String investNo=bean.getInvestNo();
		ioSer.updateMsg(msg, updateuser, qNo);
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>alert('評語已更新!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/interviewone/listbyqno.jsp?investNo="+investNo+"';</script>");
		out.flush();
		out.close();
		return;
	}
}
