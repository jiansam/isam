package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.Commit;
import com.isam.bean.CommitDetail;
import com.isam.bean.CommitXReceiveNo;
import com.isam.bean.CommitXRestrainOffice;
import com.isam.bean.SubCommit;
import com.isam.dao.ApprovalOptionDAO;
import com.isam.dao.CommitRestrainTypeDAO;
import com.isam.helper.ApplicationAttributeHelper;
import com.isam.helper.DataUtil;
import com.isam.service.CommitDetailService;
import com.isam.service.CommitService;
import com.isam.service.CommitXReceiveNoService;
import com.isam.service.CommitXRestrainOfficeService;
import com.isam.service.ProjectKeyHelp;
import com.isam.service.SubCommitDetailService;
import com.isam.service.SubCommitService;
import com.isam.service.SubCommitXReceiveNoService;
import com.isam.service.SubCommitXRestrainOfficeService;

public class SubCommitShowEditOServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommitXRestrainOfficeService officeSer;
	private CommitXReceiveNoService receNoSer;
	private CommitService commitSer;
	private CommitDetailService cdSer;
	private SubCommitService subSer;
	private SubCommitXRestrainOfficeService subOSer;
	private SubCommitXReceiveNoService subRnSer;
	private SubCommitDetailService detailSer;
	private Map<String, String> UserName;
	private Map<String,String> AoCode;
	private Map<String, String> IDNOToName;
	private Map<String, String> CRType;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		officeSer = new CommitXRestrainOfficeService();
		receNoSer = new CommitXReceiveNoService();
		commitSer = new CommitService();
		subSer = new SubCommitService();
		cdSer = new CommitDetailService();
		detailSer=new SubCommitDetailService();
		subOSer=new SubCommitXRestrainOfficeService();
		subRnSer = new SubCommitXReceiveNoService();
		ProjectKeyHelp help = new ProjectKeyHelp();
		UserName=help.getUserToName();
		IDNOToName=help.getIDNOToName();
		AoCode = ApprovalOptionDAO.getOptionMapByType("Commit");
		CRType = CommitRestrainTypeDAO.getTypeMap();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		
		HttpSession session = request.getSession();
		session.removeAttribute("subinfo");
		session.removeAttribute("subReceiveNo");
		session.removeAttribute("suboffice");
		session.removeAttribute("AoCode");
		session.removeAttribute("cbean");
		session.removeAttribute("CRType");
		session.removeAttribute("subbean");
		session.removeAttribute("TDetail");
		session.removeAttribute("SDetail");
		session.removeAttribute("receiveNo");
		
		String serno=DataUtil.nulltoempty(request.getParameter("serno"));
		String subserno=request.getParameter("subserno");
		String investNo=DataUtil.nulltoempty(request.getParameter("investNo"));
		String editType=DataUtil.nulltoempty(request.getParameter("editType"));
		
		Map<String,String> info=new HashMap<String, String>();
		info.put("serno", serno);
		info.put("subserno", DataUtil.nulltoempty(subserno));
		info.put("investNo", investNo);
		info.put("investName", InvestNoToName.get(investNo));
		info.put("editType", editType);
		
		Commit cbean= commitSer.select(serno);
		SubCommit bean=subSer.select(serno, investNo, subserno);
		if(cbean==null||bean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您選取的資料已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommit.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		String typetemp=cbean.getType();
		int type=Integer.valueOf(typetemp);
		String investor=IDNOToName.get(cbean.getIDNO());
		info.put("investor", investor);
		if(editType.equals("edit")||editType.equals("show")){
			info.put("editor", UserName.get(bean.getUpdateuser()));
			Map<String,List<CommitDetail>> map=detailSer.getDetail(subserno);
			if(map.containsKey("T")){
				List<CommitDetail> list = map.get("T");
				Map<String,Double> tmap=new HashMap<String,Double>();
				for (int i = 0; i < list.size(); i++) {
					tmap.put(list.get(i).getType(), list.get(i).getValue());
				}
				session.setAttribute("TDetail", tmap);
			}
			if(map.containsKey("S")){
				List<CommitDetail> list = map.get("S");
				Map<String,Map<String,Double>> smap=new HashMap<String, Map<String,Double>>();
				Map<String,Double> sub;
				for (int i = 0; i < list.size(); i++) {
					CommitDetail tmp= list.get(i);
					String k1=tmp.getYear();
					if(smap.containsKey(k1)){
						sub=smap.get(k1);
					}else{
						sub=new HashMap<String, Double>();
					}
					sub.put(tmp.getType(), tmp.getValue());
					smap.put(k1, sub);
				}
				session.setAttribute("SDetail", smap);
			}
			info.put("office", subOSer.selectStr(subserno));
			info.put("receive", subRnSer.selectStr(subserno));
			if(editType.equals("show")){
				session.setAttribute("receiveNo", subRnSer.select(subserno));
			}
		}
		
		/*取得主項目限制子項目資料*/
		session.setAttribute("cbean", cbean);
		session.setAttribute("subbean", bean);
		List<CommitXReceiveNo> receiveNo=receNoSer.select(serno);
		session.setAttribute("subReceiveNo", receiveNo);
		List<CommitXRestrainOffice>  office = officeSer.select(serno);
		session.setAttribute("suboffice", office);
		
//		System.out.println(serno);
		session.setAttribute("cRsType", cdSer.select(serno));
		session.setAttribute("subinfo", info);
		session.setAttribute("AoCode", AoCode);
		session.setAttribute("CRType", CRType);
		
		String path = request.getContextPath();
		switch (type) {
		case 1:
			response.sendRedirect(path + "/console/commit/subcommitrestrain.jsp");
			break;
//		case 2:
//			request.getRequestDispatcher("/console/commit/commitfinancial.jsp").forward(request, response);
//			break;
		case 3:
			response.sendRedirect(path + "/console/commit/subcommitcapital.jsp");
			break;
//		case 4:
//			request.getRequestDispatcher("/console/commit/commitfinancialservice.jsp").forward(request, response);
//			break;
//		default:
//			request.setCharacterEncoding("UTF-8");
//			response.setContentType("text/html;charset=UTF-8");
//			PrintWriter out = response.getWriter();
//			out.print("<script language='javascript'>alert('您要修改的資料已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommitdetail.jsp?serno="+idno+"';</script>");
//			out.flush();
//			out.close();
//			return;
		} 
		
	}
}
