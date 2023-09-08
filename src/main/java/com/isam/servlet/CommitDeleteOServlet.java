package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.isam.service.CommitDetailService;
import com.isam.service.CommitInvestorService;
import com.isam.service.CommitReportService;
import com.isam.service.CommitService;
import com.isam.service.SubCommitDetailService;
import com.isam.service.SubCommitService;

public class CommitDeleteOServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommitService cSer;
	private CommitDetailService cdSer;
	private CommitReportService crSer;
	private CommitInvestorService ciSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		cSer = new CommitService();
		cdSer = new CommitDetailService();
		crSer = new CommitReportService();
		ciSer=new CommitInvestorService();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String serno=request.getParameter("serno")==null?"":request.getParameter("serno").trim();
		String idno=request.getParameter("idno")==null?"":request.getParameter("idno").trim();
		
		if(cSer.select(serno).getType().equals("01")){
			if(cdSer.checkAccPt(serno)){
				cSer.updateNeedAlert(serno, "1");
				ciSer.updateNeedAlert(idno, "1");
			}else{
				cSer.updateNeedAlert(serno, "0");
				ciSer.updateNeedAlert(idno, "0");
			}
		}
		cSer.unable(serno);
		cdSer.unable(serno);
		crSer.unableBySerno(serno);
		SubCommitService scs=new SubCommitService();
		scs.deleteAllSub(serno);
		SubCommitDetailService scds=new SubCommitDetailService();
		scds.delete(serno);
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>alert('資料已刪除，即將返回企業編輯列表!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommitdetail.jsp?serno="+idno+"';</script>");
		out.flush();
		out.close();
		return;
	}
}
