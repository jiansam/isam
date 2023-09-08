package com.isam.servlet.ofi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.OFIInvestNoXFContent;
import com.isam.bean.OFIInvestNoXFinancial;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.InterviewoneHelp;
import com.isam.service.ofi.OFIInvestNoXFContentService;
import com.isam.service.ofi.OFIInvestNoXFinancialService;

public class OFIInvestXFinancialEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIInvestNoXFinancialService fSer;
	private OFIInvestNoXFContentService fcSer;
	private InterviewoneHelp iohelp;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		fSer = new OFIInvestNoXFinancialService();
		iohelp=new InterviewoneHelp();
		fcSer=new OFIInvestNoXFContentService();
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
		
		String serno=DataUtil.nulltoempty(request.getParameter("serno"));
		String investNo=DataUtil.nulltoempty(request.getParameter("investNo"));
		String reportyear=request.getParameter("reportyear")==null?"":DataUtil.addZeroForNum(request.getParameter("reportyear"),3);
		String reportdate=DataUtil.paramToTWDate(request.getParameter("reportdate"));
		String note=DataUtil.paramToTWDate(request.getParameter("note"));
		String type=DataUtil.nulltoempty(request.getParameter("type"));
		/*seq預留dsFinancial*/
		String seq="0";
		Map<String, Integer> pMap=iohelp.getoptidbyparam(iohelp.getqTypeF());
		pMap.remove("reportdate");
		pMap.remove("note");
		pMap.remove("dsFinancial");
		
		Timestamp time=DataUtil.getNowTimestamp();
		if(type.equals("delete")){
			if(serno.isEmpty()){
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("<script language='javascript'>alert('資料已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
				out.flush();
				out.close();
				return;
			}
			fSer.delete(serno);
		}else if(type.equals("edit")||type.equals("add")){
			if(reportdate.isEmpty()){
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("<script language='javascript'>alert('填報日期為必填!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
				out.flush();
				out.close();
				return;
			}
			
			List<OFIInvestNoXFinancial> list=fSer.select(investNo, reportyear,seq);
			if(type.equals("edit")){
				OFIInvestNoXFinancial bean=fSer.selectBySerno(serno);
				if(bean==null){
					request.setCharacterEncoding("UTF-8");
					response.setContentType("text/html;charset=UTF-8");
					PrintWriter out = response.getWriter();
					out.print("<script language='javascript'>alert('查無此財報資料，請重新確認!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
					out.flush();
					out.close();
					return;
				}else{
					bean.setReportyear(reportyear);
					bean.setReportdate(reportdate);
					bean.setNote(note);
					bean.setUpdatetime(time);
					bean.setUpdateuser(updateuser);
					fSer.update(bean);
				}
			}else if(type.equals("add")){
				if(!list.isEmpty()){
					request.setCharacterEncoding("UTF-8");
					response.setContentType("text/html;charset=UTF-8");
					PrintWriter out = response.getWriter();
					out.print("<script language='javascript'>alert('不可重複申報同一年度財報，請重新確認!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
					out.flush();
					out.close();
					return;
				}
				OFIInvestNoXFinancial bean = new OFIInvestNoXFinancial();
				bean.setInvestNo(investNo);
				bean.setReportyear(reportyear);
				bean.setReportdate(reportdate);
				bean.setNote(note);
				bean.setUpdatetime(time);
				bean.setUpdateuser(updateuser);
				bean.setCreatetime(time);
				bean.setCreateuser(updateuser);
				bean.setSeq(seq);
				serno=String.valueOf(fSer.insert(bean));
			}
			if(!serno.equals("0")){
				int iserno=Integer.valueOf(serno);
				List<OFIInvestNoXFContent> beans = new ArrayList<OFIInvestNoXFContent>();
				Enumeration<?> pNames=request.getParameterNames();
				while(pNames.hasMoreElements()){  
					String name=(String)pNames.nextElement();
					String strs=DataUtil.nulltoempty(request.getParameter(name));
					if(!strs.isEmpty()&&pMap.containsKey(name)){
						OFIInvestNoXFContent bean=new OFIInvestNoXFContent();
						bean.setOptionId(pMap.get(name));
						bean.setSerno(iserno);
						bean.setValue(strs);
						beans.add(bean);
					}
				}
				fcSer.delete(serno);
				fcSer.insert(beans);
			}
		}
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>alert('資料已更新!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
		out.flush();
		out.close();
		return;

	}
}
