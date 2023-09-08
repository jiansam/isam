package com.isam.servlet.ofi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.OFIInvestList;
import com.isam.bean.OFIInvestNoXTWSIC;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.ofi.OFIInvestCaseService;
import com.isam.service.ofi.OFIInvestListService;
import com.isam.service.ofi.OFIInvestNoXTWSICService;

public class OFIInvestEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIInvestListService listSer;
	private OFIInvestNoXTWSICService sicSer;
	private OFIInvestCaseService icSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		listSer = new OFIInvestListService();
		sicSer = new OFIInvestNoXTWSICService();
		icSer=new OFIInvestCaseService();
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
		String active=DataUtil.nulltoempty(request.getParameter("active"));
		String isNew=DataUtil.nulltoempty(request.getParameter("isNew"));
		String setupdate=DataUtil.paramToTWDate(request.getParameter("setupdate"));
		String approvaldate=DataUtil.paramToTWDate(request.getParameter("approvaldate"));
		String setupnote=DataUtil.nulltoempty(request.getParameter("setupnote"));
		String isOperated=DataUtil.nulltoempty(request.getParameter("isOperated"));
		String sdate=DataUtil.paramToTWDate(request.getParameter("sdate"));
		String edate=DataUtil.paramToTWDate(request.getParameter("edate"));
		String note=DataUtil.nulltoempty(request.getParameter("note"));
		String isFilled=DataUtil.nulltoempty(request.getParameter("isFilled"));
		String mainSic=DataUtil.nulltoempty(request.getParameter("mainSic"));
		String[] secondary=request.getParameterValues("secondary");
		String[] spSic=request.getParameterValues("spSic");
		String firmXNTBTSic=DataUtil.nulltoempty(request.getParameter("firmXNTBTSic")); //107-07-13 新增國稅局或財報登記營業項目
		
		OFIInvestList bean=listSer.select(investNo);
		if(bean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			//連結要記得改
			out.print("<script language='javascript'>alert('此資料無法編輯，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/project/showproject.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		Timestamp time=DataUtil.getNowTimestamp();
		bean.setActive(active);
		bean.setIsNew(isNew);
		bean.setSetupdate(setupdate);
		bean.setApprovaldate(approvaldate);
		bean.setSetupnote(setupnote);
		bean.setIsOperated(isOperated);
		bean.setSdate(sdate);
		bean.setEdate(edate);
		bean.setNote(note);
		bean.setIsFilled(isFilled);
		bean.setUpdateuser(updateuser);
		bean.setUpdatetime(time);
		bean.setFirmXNTBTSic(firmXNTBTSic);
		listSer.update(bean);
		
		/*update investcase isFilled*/
		icSer.updateIsFilled(null, investNo, updateuser);
		
		sicSer.delete(investNo);
		List<OFIInvestNoXTWSIC> beans=new ArrayList<OFIInvestNoXTWSIC>();
		if(!mainSic.isEmpty()){
			OFIInvestNoXTWSIC sic= new OFIInvestNoXTWSIC();
			sic.setInvestNo(investNo);
			sic.setItem(mainSic);
			sic.setType("1");
			sic.setSeq(1);
			sic.setUpdatetime(time);
			sic.setUpdateuser(updateuser);
			beans.add(sic);
		}
		if(secondary!=null){
			for (int i = 0; i < secondary.length; i++) {
				OFIInvestNoXTWSIC sic= new OFIInvestNoXTWSIC();
				sic.setInvestNo(investNo);
				sic.setItem(secondary[i]);
				sic.setType("2");
				sic.setSeq(i+1);
				sic.setUpdatetime(time);
				sic.setUpdateuser(updateuser);
				beans.add(sic);
			}
		}
		if(spSic!=null){
			for (int i = 0; i < spSic.length; i++) {
				OFIInvestNoXTWSIC sic= new OFIInvestNoXTWSIC();
				sic.setInvestNo(investNo);
				sic.setItem(spSic[i]);
				sic.setType("0");
				sic.setSeq(i+1);
				sic.setUpdatetime(time);
				sic.setUpdateuser(updateuser);
				beans.add(sic);
			}
		}
		if(!beans.isEmpty()){
			sicSer.insert(beans);
		}
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>alert('基本資料已更新!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
		out.flush();
		out.close();
		return;
	}
}
