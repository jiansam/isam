package com.isam.ofi.reject.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.ofi.reject.bean.OFIReject;
import com.isam.ofi.reject.bean.OFIRejectCompany;
import com.isam.ofi.reject.bean.OFIRejectXTWSIC;
import com.isam.ofi.reject.service.OFIRejectCompanyService;
import com.isam.ofi.reject.service.OFIRejectService;
import com.isam.ofi.reject.service.OFIRejectXTWSICService;

public class OFIRejectEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserMember bean = (UserMember) session.getAttribute("userInfo");
		String updateuser = bean.getIdMember();
		Timestamp t=DataUtil.getNowTimestamp();
		
		String cNoUpdate = DataUtil.nulltoempty(request.getParameter("cNoUpdate"));
		String cNo = DataUtil.nulltoempty(request.getParameter("cNo"));
		String serno = DataUtil.nulltoempty(request.getParameter("serno"));
		String investName = DataUtil.nulltoempty(request.getParameter("investName"));
		String IDNO = DataUtil.nulltoempty(request.getParameter("IDNO"));
		String isNew = DataUtil.nulltoempty(request.getParameter("isNew"));
		String setupdate = DataUtil.fmtDateItem(request.getParameter("setupdate"));
		String orgType = DataUtil.nulltoempty(request.getParameter("orgType"));
		
		if(cNo.isEmpty()||serno.isEmpty()){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('系統出現無法預期的問題，請通知系統工程師！');window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/reject/showlist.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		
		OFIRejectCompany company=new OFIRejectCompany();
		company.setcNo(Integer.valueOf(cNo));
		company.setCname(investName);
		company.setIdno(IDNO);
		company.setIsNew(isNew);
		company.setSetupdate(setupdate);
		company.setOrgType(orgType);
		company.setEnable("1");
		company.setUpdatetime(t);
		company.setUpdateuser(updateuser);
		OFIRejectCompanyService cSer=new OFIRejectCompanyService();
		OFIRejectService rSer=new OFIRejectService();
		cSer.update(company);
		
		OFIReject reject=new OFIReject();
		reject.setSerno(Integer.valueOf(serno));
		reject.setReceiveDate(DataUtil.fmtDateItem(request.getParameter("receiveDate")));
		reject.setReceiveNo(DataUtil.nulltoempty(request.getParameter("receiveNo")));
		reject.setCurrency(DataUtil.nulltoempty(request.getParameter("currency")));
		reject.setMoney(DataUtil.paramToStringD(request.getParameter("money")));
		reject.setShareholding(DataUtil.paramToStringD(request.getParameter("shareholding")));
		reject.setOtherSic(DataUtil.nulltoempty(request.getParameter("otherSic")));
		reject.setIssueDate(DataUtil.fmtDateItem(request.getParameter("issueDate")));
		reject.setIssueNo(DataUtil.nulltoempty(request.getParameter("issueNo")));
		reject.setRejectType(DataUtil.nulltoempty(request.getParameter("rejectType")));
		reject.setDecision(DataUtil.nulltoempty(request.getParameter("decision")));
		reject.setExplanation(DataUtil.nulltoempty(request.getParameter("explanation")));
		reject.setReason(DataUtil.nulltoempty(request.getParameter("reason")));
		reject.setNote(DataUtil.nulltoempty(request.getParameter("note")));
		reject.setMoneyother(DataUtil.nulltoempty(request.getParameter("moneyother")));
		reject.setEnable("1");
		reject.setUpdatetime(t);
		reject.setUpdateuser(updateuser);
		rSer.update(reject);
		
		if(cNoUpdate.equals("1")){
			rSer.mergeCNo(reject, investName,cNo);
			cSer.mergeCNo(company);
		}
		
		String[] sicAry=request.getParameterValues("sic");
		OFIRejectXTWSICService sicSer=new OFIRejectXTWSICService();
		sicSer.delete(serno);
		if(sicAry!=null){
			List<OFIRejectXTWSIC> bs=new ArrayList<OFIRejectXTWSIC>();
			for (int i = 0; i < sicAry.length; i++) {
				OFIRejectXTWSIC sub=new OFIRejectXTWSIC();
				sub.setSerno(serno);
				sub.setItem(sicAry[i]);
				sub.setSeq(i+1);
				sub.setUpdatetime(t);
				sub.setUpdateuser(updateuser);
				bs.add(sub);
			}
			if(!bs.isEmpty()){
				sicSer.insert(bs);
			}
		}
//		OFIRejectXApplicantService aplSer=new OFIRejectXApplicantService();
//		aplSer.acvtiveAdd(serno, updateuser, t);
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>alert('資料已更新!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/reject/showlist.jsp';</script>");
		out.flush();
		out.close();
		return;
		
	}

}
