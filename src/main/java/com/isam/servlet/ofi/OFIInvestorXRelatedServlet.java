package com.isam.servlet.ofi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.OFIInvestorXRelated;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.ofi.OFIInvestorXRelatedService;

public class OFIInvestorXRelatedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIInvestorXRelatedService rSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		rSer = new OFIInvestorXRelatedService();
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
		
		String investorSeq=DataUtil.nulltoempty(request.getParameter("investorSeq"));
		String relatedname=DataUtil.nulltoempty(request.getParameter("relatedname"));
		
		String type=DataUtil.nulltoempty(request.getParameter("type"));
		String serno=DataUtil.nulltoempty(request.getParameter("serno"));
		String nation=DataUtil.nulltoempty(request.getParameter("nation"));
		String cnCode=DataUtil.nulltoempty(request.getParameter("cnCode"));
		
		if(!type.equals("delete") && (relatedname.isEmpty()||nation.isEmpty())){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('輸入格式錯誤，請重新輸入!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvestform.jsp?investorSeq="+investorSeq+"';</script>");
			out.flush();
			out.close();
			return;
		}else if(investorSeq.isEmpty()){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			/*連結要記得改*/
			out.print("<script language='javascript'>alert('投資人已不存在，請重新輸入!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvestform.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		Timestamp time=DataUtil.getNowTimestamp();
		if(type.equals("delete")){
			if(serno.isEmpty()){
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("<script language='javascript'>alert('輸入格式錯誤，請選要刪除的對象!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvestform.jsp?investorSeq="+investorSeq+"';</script>");
				out.flush();
				out.close();
				return;
			}
			rSer.delete(serno);
		}else if(type.equals("edit")){
			OFIInvestorXRelated bean=rSer.selectBySerno(serno);
			if(bean==null){
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("<script language='javascript'>alert('本筆資料已不存在，請重選要編輯的對象!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvestform.jsp?investorSeq="+investorSeq+"';</script>");
				out.flush();
				out.close();
				return;
			}
			bean.setSerno(serno);
			bean.setInvestorSeq(investorSeq);
			bean.setRelatedname(relatedname);
			bean.setNation(nation);
			bean.setCnCode(cnCode);
			bean.setUpdatetime(time);
			bean.setUpdateuser(updateuser);
			rSer.update(bean);
		}else if(type.equals("add")){
			OFIInvestorXRelated bean=new OFIInvestorXRelated();
			bean.setInvestorSeq(investorSeq);
			bean.setRelatedname(relatedname);
			bean.setNation(nation);
			bean.setCnCode(cnCode);
			bean.setUpdatetime(time);
			bean.setUpdateuser(updateuser);
			bean.setCreatetime(time);
			bean.setCreateuser(updateuser);
			rSer.insert(bean);
		}
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>alert('資料已更新!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvestform.jsp?investorSeq="+investorSeq+"';</script>");
		out.flush();
		out.close();
		return;
	}
}
