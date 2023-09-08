package com.isam.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.isam.bean.Commit;
import com.isam.bean.CommitReport;
import com.isam.service.CommitDetailService;
import com.isam.service.CommitInvestorService;
import com.isam.service.CommitReportDetailService;
import com.isam.service.CommitReportService;
import com.isam.service.CommitService;

public class CommitReportDeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommitDetailService cdSer;
	private CommitInvestorService ciSer;
	private CommitService cSer;
	private CommitReportDetailService crdSer;
	private CommitReportService crSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		cdSer=new CommitDetailService();
		cSer=new CommitService();
		ciSer=new CommitInvestorService();
		crdSer = new CommitReportDetailService();
		crSer = new CommitReportService();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
//		HttpSession session = request.getSession();
//		UserMember bean = (UserMember) session.getAttribute("userInfo");
//		String updateuser = bean.getIdMember();

		String repSerno=request.getParameter("repserno")==null?"":request.getParameter("repserno").trim();
		
		CommitReport crBean=crSer.select(repSerno);
		String serno = String.valueOf(crBean.getSerno());
//		java.sql.Timestamp time = DataUtil.getNowTimestamp();
		Commit cbean= cSer.select(serno);
		String idno=cbean.getIDNO();
		crSer.unable(repSerno);
		crdSer.unable(repSerno);
		
		if(cbean.getType().equals("01")){
			if(cdSer.checkAccPt(serno)){
				cSer.updateNeedAlert(serno, "1");
				ciSer.updateNeedAlert(idno, "1");
			}else{
				cSer.updateNeedAlert(serno, "0");
				ciSer.updateNeedAlert(idno, "0");
			}
		}
		
		String path = request.getContextPath();
		StringBuilder sb = new StringBuilder();
		sb.append(path).append("/console/commit/showcommitdetail.jsp?serno=").append(idno).append("&updateOK=2");
		response.sendRedirect(sb.toString());
		
	}

}
