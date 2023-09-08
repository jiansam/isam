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

import com.isam.bean.OFIInvestorList;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.ofi.OFIInvestCaseService;
import com.isam.service.ofi.OFIInvestorListService;

public class OFIInvestorEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIInvestorListService listSer;
	private OFIInvestCaseService icSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		listSer = new OFIInvestorListService();
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
		
		String investorSeq=DataUtil.nulltoempty(request.getParameter("investorSeq"));
		String isFilled=DataUtil.nulltoempty(request.getParameter("isFilled"));
		
		String cnCode=DataUtil.nulltoempty(request.getParameter("cnCode"));
		String note=DataUtil.nulltoempty(request.getParameter("note"));
		
		/* 2020/11/22 Added by antfire start */
		String ids[] = request.getParameterValues("ids"); 
		String money1[]= request.getParameterValues("ilmoney1"); 
		
		String money2[]= request.getParameterValues("ilmoney2"); 
		String ilstockimp[]= request.getParameterValues("ilstockimp"); 
		
		//System.out.print("ids:"+ids.length);
		/* 2020/11/22 Added by antfire end */
		OFIInvestorList bean=listSer.select(investorSeq);
		
		if(bean==null || (ids.length!=money1.length || money2.length != ids.length || ilstockimp.length!= ids.length)){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('此資料無法編輯，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvestform.jsp?investorSeq="+investorSeq+"';</script>");
			out.flush();
			out.close();
			return;
		}
		Timestamp time=DataUtil.getNowTimestamp();
		bean.setCnCode(cnCode);
		bean.setNote(note);
		bean.setIsFilled(isFilled);
		bean.setUpdateuser(updateuser);
		bean.setUpdatetime(time);
		listSer.update(bean);
		
		/*update investcase isFilled*/
		icSer.updateIsFilled(investorSeq, null, updateuser);
	
		
		for(int i = 0; i < ids.length; i++)
		{
			icSer.updateInvestInfo(ids[i], money1[i], money2[i], ilstockimp[i]);
		}
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>alert('基本資料已更新!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvestform.jsp?investorSeq="+investorSeq+"';</script>");
		out.flush();
		out.close();
		return;
	}
}
