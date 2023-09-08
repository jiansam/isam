package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.InterviewoneManage;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.InterviewoneManageService;

public class InterviewoneEditFollowingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private InterviewoneManageService imSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		imSer = new InterviewoneManageService();
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
		
		String sernoStr=DataUtil.nulltoempty(request.getParameter("serno"));
		String qNoStr=DataUtil.nulltoempty(request.getParameter("qNo"));
		String receiveNo=DataUtil.nulltoempty(request.getParameter("receiveNo"));
		String receiveDate=DataUtil.nulltoempty(request.getParameter("receiveDate"));
		String issueNo=DataUtil.nulltoempty(request.getParameter("issueNo"));
		String issueDate=DataUtil.nulltoempty(request.getParameter("issueDate"));
		String following=DataUtil.nulltoempty(request.getParameter("following"));
		String note=DataUtil.nulltoempty(request.getParameter("note"));
		String progress=DataUtil.nulltoempty(request.getParameter("progress"));
		String issueby=DataUtil.nulltoempty(request.getParameter("issueby"));
		String editType=DataUtil.nulltoempty(request.getParameter("type"));
		String interviewer=DataUtil.nulltoempty(request.getParameter("interviewer"));
		String interviewee=DataUtil.nulltoempty(request.getParameter("interviewee"));
		String flag=DataUtil.nulltoempty(request.getParameter("flag"));
		
		StringBuilder sb = new StringBuilder();
		sb.append(request.getContextPath()).append("/console/interviewone/showfollowingbyqno.jsp?qNo=").append(qNoStr);
		String sUrl=sb.toString();
		sb.setLength(0);
		sb.append("http://").append(request.getServerName()).append(":").append(request.getServerPort()).append(sUrl);
		String url=sb.toString();
		sb.setLength(0);
		
		if(!editType.equals("delete")){
			List<String> list=new ArrayList<String>();
			list.add(following);
			list.add(progress);
			if(flag.equals("0")){
				list.add(receiveNo);
				list.add(receiveDate);
				list.add(issueNo);
				list.add(issueDate);
				if(!DataUtil.isMatchPtn(receiveDate, "\\d{3}/\\d{2}/\\d{2}")||!DataUtil.isMatchPtn(issueDate, "\\d{3}/\\d{2}/\\d{2}")){
					request.setCharacterEncoding("UTF-8");
					response.setContentType("text/html;charset=UTF-8");
					PrintWriter out = response.getWriter();
					sb.append("<script language='javascript'>alert('日期格式未正確填寫，請重新檢查格式為(民國年/月/日)，含斜線共9碼。'); window.location.href='");
					sb.append(url).append("';</script>");
					out.print(sb.toString());
					sb.setLength(0);
					out.flush();
					out.close();
					return;
				}
			}else{
				list.add(receiveDate);
				list.add(interviewer);
//				list.add(interviewee);
				if(!DataUtil.isMatchPtn(receiveDate, "\\d{3}/\\d{2}/\\d{2}")){
					request.setCharacterEncoding("UTF-8");
					response.setContentType("text/html;charset=UTF-8");
					PrintWriter out = response.getWriter();
					sb.append("<script language='javascript'>alert('日期格式未正確填寫，請重新檢查格式為(民國年/月/日)，含斜線共9碼。'); window.location.href='");
					sb.append(url).append("';</script>");
					out.print(sb.toString());
					sb.setLength(0);
					out.flush();
					out.close();
					return;
				}
			}
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).length()==0){
					request.setCharacterEncoding("UTF-8");
					response.setContentType("text/html;charset=UTF-8");
					PrintWriter out = response.getWriter();
					sb.append("<script language='javascript'>alert('必填欄位未正確填寫，請重新檢查。'); window.location.href='");
					sb.append(url).append("';</script>");
					out.print(sb.toString());
					sb.setLength(0);
					out.flush();
					out.close();
					return;
				}
			}
		}
		java.sql.Timestamp date = DataUtil.getNowTimestamp();
		int qNo = Integer.valueOf(qNoStr);
		InterviewoneManage bean;
		if(editType.equals("add")){
			bean=new InterviewoneManage();
			bean.setqNo(qNo);
			bean.setReceiveNo(receiveNo);
			bean.setReceiveDate(receiveDate.replaceAll("/", ""));
			bean.setIssueNo(issueNo);
			bean.setIssueDate(issueDate.replaceAll("/", ""));
			bean.setIssueby(issueby);
			bean.setOptionValue(progress);
			bean.setFollowing(following);
			bean.setInterviewer(interviewer);
			bean.setInterviewee(interviewee);
			bean.setNote(note);
			bean.setEnable("1");
			bean.setFlag(flag);
			bean.setUpdatetime(date);
			bean.setUpdateuser(updateuser);
			bean.setCreatetime(date);
			bean.setCreateuser(updateuser);
			imSer.insert(bean);
		}else if(editType.equals("edit")){
			int serno = Integer.valueOf(sernoStr);
			bean=imSer.select(sernoStr);
			bean.setSerno(serno);
			bean.setReceiveNo(receiveNo);
			bean.setReceiveDate(receiveDate.replaceAll("/", ""));
			bean.setIssueNo(issueNo);
			bean.setIssueDate(issueDate.replaceAll("/", ""));
			bean.setIssueby(issueby);
			bean.setOptionValue(progress);
			bean.setFollowing(following);
			bean.setInterviewer(interviewer);
			bean.setInterviewee(interviewee);
			bean.setNote(note);
			bean.setEnable("1");
			bean.setUpdatetime(date);
			bean.setUpdateuser(updateuser);
			imSer.update(bean);
		}else if(editType.equals("delete")){
			imSer.unable(sernoStr, updateuser);
		}
		response.sendRedirect(sUrl);
	}
}
