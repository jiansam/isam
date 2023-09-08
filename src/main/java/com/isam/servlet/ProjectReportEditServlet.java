package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.ProjectReport;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.ProjectKeyHelp;
import com.isam.service.ProjectReportService;
import com.isam.service.ProjectService;
import com.isam.service.ProjectXReciveNoService;

public class ProjectReportEditServlet  extends HttpServlet{
	private static final long serialVersionUID = 1L;
	Map<String, String> IDNOToMain;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ProjectKeyHelp help = new ProjectKeyHelp();
		IDNOToMain = help.getIDNoToMain();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserMember bean = (UserMember) session.getAttribute("userInfo");
		String updateuser = bean.getIdMember();

		String url= request.getParameter("url")==null?"/isam/console/project/showproject.jsp":request.getParameter("url");
		String edittype=request.getParameter("edittype")==null?"":request.getParameter("edittype");
		String isOnline=request.getParameter("isOnline");
		String repType=request.getParameter("repType");
		String investNo=request.getParameter("investNo");
		String IDNO=request.getParameter("investor");
		String keyinNo=request.getParameter("pNo");
		String pYear=request.getParameter("pYear");
		String pSeason=request.getParameter("pSeason");
		String financial=request.getParameter("financial")==null?"":request.getParameter("financial");
		String outwardMoney=request.getParameter("outwardMoney")==null?"":request.getParameter("outwardMoney").replace(",", "");
		String approvalMoney=request.getParameter("approvalMoney")==null?"":request.getParameter("approvalMoney").replace(",", "");
		String approvedMoney=request.getParameter("approvedMoney")==null?"":request.getParameter("approvedMoney").replace(",", "");
		String investMoney=request.getParameter("investMoney")==null?"":request.getParameter("investMoney").replace(",", "");
		String isConversion=request.getParameter("isConversion")==null?"N":request.getParameter("isConversion").trim();
		String outwardNote=DataUtil.nulltoempty(request.getParameter("outwardNote"));
		String approvalNote=DataUtil.nulltoempty(request.getParameter("approvalNote"));
		String approvedNote=DataUtil.nulltoempty(request.getParameter("approvedNote"));
		String note=request.getParameter("note")==null?"":request.getParameter("note").trim();
		String[] receviceNoStr=request.getParameterValues("receviceNo");
		String repSernoTemp=request.getParameter("repserno");
		String noNeed=request.getParameter("noNeed");
		String noNeedNote=DataUtil.nulltoempty(request.getParameter("noNeedNote"));
		
		if(outwardMoney.isEmpty()){
			outwardMoney=null;
		}
		if(approvalMoney.isEmpty()){
			approvalMoney=null;
		}
		if(approvedMoney.isEmpty()){
			approvedMoney=null;
		}
		if(investMoney.isEmpty()){
			investMoney=null;
		}
		
		String completeMoney = null;
		if(approvedMoney!=null&&approvalMoney!=null){
			if(Double.valueOf(approvedMoney)!=0&&Double.valueOf(approvalMoney)!=0){
				completeMoney = String.valueOf((Double.valueOf(approvedMoney)/Double.valueOf(approvalMoney)));
			}
		}

		ProjectService ser= new ProjectService();
		ProjectReportService prSer= new ProjectReportService();
		ProjectXReciveNoService pXRNo=new ProjectXReciveNoService();
		
		int serno = ser.getSerno(investNo, IDNOToMain.get(IDNO));
//		System.out.println(serno+";inv:"+InvestNoToMain.get(investNo)+";IDNO:"+IDNO);
		ProjectReport rep = null;
		java.sql.Timestamp time = DataUtil.getNowTimestamp();
		if(!edittype.equals("delete")){
			if(pYear.length()==2){
				pYear="0"+pYear;
			}
		}
		if(edittype.equals("insert")){
			if(prSer.isExists(serno, repType, pYear, pSeason)){
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("<script language='javascript'>alert('資料已經存在，即將轉往修改頁面!');window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/project/updatepr.jsp?repserno="+prSer.select(serno, repType, pYear, pSeason).getRepSerno()+"';</script>");
				out.flush();
				out.close();
				return;
			}
			rep = new ProjectReport();
			rep.setSerno(serno);
			rep.setKeyinNo(keyinNo);
			rep.setIsOnline(isOnline);
			rep.setRepType(repType);
			rep.setYear(pYear);
			rep.setQuarter(pSeason);
			rep.setOutwardMoney(outwardMoney);
			rep.setOutwardNote(outwardNote);
			rep.setApprovalMoney(approvalMoney);
			rep.setApprovalNote(approvalNote);
			rep.setApprovedMoney(approvedMoney);
			rep.setApprovedNote(approvedNote);
			rep.setCompleteMoney(completeMoney);
			rep.setInvestMoney(investMoney);
			rep.setFinancial(financial);
			rep.setUpdatetime(time);
			rep.setUpdateuser(updateuser);
			rep.setCreatetime(time);
			rep.setCreateuser(updateuser);
			rep.setEnable("1");
			rep.setIsConversion(isConversion);
			rep.setNote(note);
			rep.setNoNeed(noNeed);
			rep.setNoNeedNote(noNeedNote);
			prSer.insert(rep);
			rep = prSer.select(serno, repType, pYear, pSeason);
		}else if(edittype.equals("update")){
			rep = prSer.selectByRepSerno(Integer.valueOf(repSernoTemp),"1");
			rep.setKeyinNo(keyinNo);
			rep.setFinancial(financial);
			rep.setOutwardMoney(outwardMoney);
			rep.setOutwardNote(outwardNote);
			rep.setApprovalMoney(approvalMoney);
			rep.setApprovalNote(approvalNote);
			rep.setApprovedMoney(approvedMoney);
			rep.setApprovedNote(approvedNote);
			rep.setCompleteMoney(completeMoney);
			rep.setInvestMoney(investMoney);
			rep.setUpdatetime(time);
			rep.setUpdateuser(updateuser);
			rep.setIsConversion(isConversion);
			rep.setNote(note);
			rep.setNoNeed(noNeed);
			rep.setNoNeedNote(noNeedNote);
			prSer.update(rep);
		}else if(edittype.equals("delete")){
			if(prSer.checkUnConfirm(repSernoTemp)!=0){
				request.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				StringBuilder sb=new StringBuilder();
				sb.append("<script language='javascript'>alert('於線上申報待確認列表裡有相同填報時點資料，無法進行刪除，即將轉往線上申報待確認頁面!');window.location.href='http://");
				sb.append(request.getServerName()).append(":").append(request.getServerPort()).append(request.getContextPath());
				sb.append("/console/project/listrpconfirm.jsp?investNo=").append(investNo).append("';</script>");
				PrintWriter out = response.getWriter();
				out.print(sb.toString());
				sb.setLength(0);
				out.flush();
				out.close();
				return;
			}
			rep = prSer.selectByRepSerno(Integer.valueOf(repSernoTemp),"1");
			prSer.unable(Integer.valueOf(repSernoTemp),updateuser,time);
		}
		if(!edittype.equals("delete")){
			/*更新備標註的文號*/
			int repSerno = rep.getRepSerno();
			pXRNo.delete(repSerno);
			if(receviceNoStr!=null){
				pXRNo.insertBatch(receviceNoStr, repSerno);
			}
		}

		ser.updateNeedAlert(serno,prSer.getNeedAlert(serno));
//		System.out.println("prSer.getNeedAlert(serno):"+prSer.getNeedAlert(serno));
		String path = request.getContextPath();
		if(edittype.equals("insert")){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('資料新增成功，請繼續新增!');window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+url+"';</script>");
			out.flush();
			out.close();
			return;
		}else if(edittype.equals("update")){
//			System.out.println("update serno:"+serno);
			response.sendRedirect(path + "/console/project/showprojectdetail.jsp?serno="+rep.getSerno()+"&updateMark=pjok&year="+pYear+"&season="+pSeason);
		}else if(edittype.equals("delete")){
//			System.out.println("delete serno:"+serno);
			response.sendRedirect(path + "/console/project/showprojectdetail.jsp?serno="+rep.getSerno()+"&updateMark=pjdel&year="+rep.getYear()+"&season="+rep.getQuarter());
		}
	}
}
