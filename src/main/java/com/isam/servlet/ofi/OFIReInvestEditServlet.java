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

import com.isam.bean.OFIReInvestList;
import com.isam.bean.OFIReInvestXTWSIC;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.ofi.OFIReInvestListService;
import com.isam.service.ofi.OFIReInvestXTWSICService;

public class OFIReInvestEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIReInvestListService reSer;
	private OFIReInvestXTWSICService reSicSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		reSer = new OFIReInvestListService();
		reSicSer= new OFIReInvestXTWSICService();
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
		String reInvestNo=DataUtil.nulltoempty(request.getParameter("reInvestNo"));
		String idno=DataUtil.nulltoempty(request.getParameter("idno"));
		String orgType=DataUtil.nulltoempty(request.getParameter("orgType"));
		String city=DataUtil.nulltoempty(request.getParameter("city"));
		String town=DataUtil.nulltoempty(request.getParameter("town"));
		String addr=DataUtil.nulltoempty(request.getParameter("addr"));
		String isNew=DataUtil.nulltoempty(request.getParameter("isNew"));
		String setupdate=DataUtil.paramToTWDate(request.getParameter("setupdate"));
		String setupnote=DataUtil.nulltoempty(request.getParameter("setupnote"));
		String stockNum=DataUtil.paramToStringD(request.getParameter("stockNum"));
		String faceValue=DataUtil.paramToStringD(request.getParameter("faceValue"));
		String regicapital=DataUtil.paramToStringD(request.getParameter("regicapital"));
		String paidcapital=DataUtil.paramToStringD(request.getParameter("paidcapital"));
		String isOperated=DataUtil.nulltoempty(request.getParameter("isOperated"));
		String sdate=DataUtil.paramToTWDate(request.getParameter("sdate"));
		String edate=DataUtil.paramToTWDate(request.getParameter("edate"));
		String note=DataUtil.nulltoempty(request.getParameter("note"));
		String isFilled=DataUtil.nulltoempty(request.getParameter("isFilled"));
		String mainSic=DataUtil.nulltoempty(request.getParameter("mainSic"));
		String[] secondary=request.getParameterValues("secondary");
		String[] spSic=request.getParameterValues("spSic");
		
		OFIReInvestList bean=reSer.selectbean(investNo,reInvestNo);
		if(bean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			//連結要記得改
			out.print("<script language='javascript'>alert('轉投資已不存在!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
			out.flush();
			out.close();
			return;
		}
		Timestamp time=DataUtil.getNowTimestamp();
		bean.setOrgType(orgType);
		bean.setIdno(idno);
		bean.setAddr(addr);
		if(!addr.isEmpty()){
			bean.setCity(city);
			bean.setTown(town);
		}
		bean.setIsNew(isNew);
		bean.setSetupdate(setupdate);
		bean.setSetupnote(setupnote);
		bean.setStockNum(stockNum);
		bean.setFaceValue(faceValue);
		bean.setRegiCapital(regicapital);
		bean.setPaidCapital(paidcapital);
		bean.setIsOperated(isOperated);
		bean.setSdate(sdate);
		bean.setEdate(edate);
		bean.setNote(note);
		bean.setIsFilled(isFilled);
		bean.setUpdateuser(updateuser);
		bean.setUpdatetime(time);
		
		reSer.update(bean);
		
		reSicSer.delete(reInvestNo);
		List<OFIReInvestXTWSIC> beans=new ArrayList<OFIReInvestXTWSIC>();
		if(!mainSic.isEmpty()){
			OFIReInvestXTWSIC sic= new OFIReInvestXTWSIC();
			sic.setReInvestNo(reInvestNo);
			sic.setItem(mainSic);
			sic.setType("1");
			sic.setSeq(1);
			sic.setUpdatetime(time);
			sic.setUpdateuser(updateuser);
			beans.add(sic);
		}
		if(secondary!=null){
			for (int i = 0; i < secondary.length; i++) {
				OFIReInvestXTWSIC sic= new OFIReInvestXTWSIC();
				sic.setReInvestNo(reInvestNo);
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
				OFIReInvestXTWSIC sic= new OFIReInvestXTWSIC();
				sic.setReInvestNo(reInvestNo);
				sic.setItem(spSic[i]);
				sic.setType("0");
				sic.setSeq(i+1);
				sic.setUpdatetime(time);
				sic.setUpdateuser(updateuser);
				beans.add(sic);
			}
		}
		if(!beans.isEmpty()){
			reSicSer.insert(beans);
		}
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>alert('轉投資資料已更新!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
		out.flush();
		out.close();
		return;
	}
}
