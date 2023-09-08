package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.Commit;
import com.isam.bean.CommitInvestor;
import com.isam.dao.CommitRestrainTypeDAO;
import com.isam.helper.ApplicationAttributeHelper;
import com.isam.helper.DataUtil;
import com.isam.service.CommitDetailService;
import com.isam.service.CommitInvestorService;
import com.isam.service.CommitInvestorXContactsService;
import com.isam.service.CommitReportDetailService;
import com.isam.service.CommitReportService;
import com.isam.service.CommitService;
import com.isam.service.CommitXOfficeService;
import com.isam.service.CommitXRestrainOfficeService;
import com.isam.service.ProjectKeyHelp;
import com.isam.service.SubCommitReportDetailService;
import com.isam.service.SubCommitReportService;

public class CommitShowDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Map<String, String> IDNOToName;
	Map<String, String> userName;
	Map<String, String> CRType;
	Map<String, String> repTypeMap;
	private CommitReportDetailService cpdSer;
	private CommitInvestorService ser;
	private CommitXOfficeService office;
	private CommitService commit;
	private CommitReportService crSer;
	private CommitDetailService cdSer;
	private SubCommitReportService subcrSer;
	private SubCommitReportDetailService subcpdSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ser= new CommitInvestorService();
		office = new CommitXOfficeService();
		commit = new CommitService();
		crSer = new CommitReportService();
		cdSer = new CommitDetailService();
		subcrSer = new SubCommitReportService();
		subcpdSer =new SubCommitReportDetailService();
		ProjectKeyHelp help = new ProjectKeyHelp();
		IDNOToName = help.getIDNOToName();
		userName=help.getUserToName();
		repTypeMap=help.getRepTypeMap();
		CRType = CommitRestrainTypeDAO.getTypeMap();
		cpdSer = new CommitReportDetailService();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		
		HttpSession session = request.getSession();
		session.removeAttribute("cdbean");
		session.removeAttribute("cdInfo");
		session.removeAttribute("crlist");
		session.removeAttribute("investList");
		session.removeAttribute("creNOList");
		session.removeAttribute("povCR");
		session.removeAttribute("summary");
		session.removeAttribute("mapExDetail");
		session.removeAttribute("repTypeMap");
		session.removeAttribute("newadd");
		session.removeAttribute("CRType");
		session.removeAttribute("subcrSer");
		session.removeAttribute("subcpd");
		session.removeAttribute("cContacts");
		session.removeAttribute("cXr");
		session.removeAttribute("editContact");
		
		String idno=request.getParameter("serno")==null?"":request.getParameter("serno").trim();
		String updateOK=request.getParameter("updateOK")==null?"":request.getParameter("updateOK").trim();
		if(request.getParameter("add")!=null){
			session.setAttribute("newadd","Y");
		}
		Map<String,String> map = new HashMap<String, String>();
		CommitInvestor cdbean=ser.select(idno);
		if(idno.isEmpty()){
			request.setCharacterEncoding("UTF-8");
	    	response.setContentType("text/html;charset=UTF-8");
	    	PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('統編輸入錯誤，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommit.jsp';</script>");
			out.flush();
			out.close();
			return;
		}else if(cdbean==null){
			request.setCharacterEncoding("UTF-8");
	    	response.setContentType("text/html;charset=UTF-8");
	    	PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您要修改的企業資料不存在!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommit.jsp';</script>");
			out.flush();
			out.close();
			return;
		}else{
			String officstr = office.getOfficeStr(idno);
			if(!officstr.isEmpty()){
				map.put("officstr",officstr);
			}
			map.put("updateOK",updateOK);
			List<Commit> comList=commit.selectByIDNO(idno);
			List<Commit> commitList=new ArrayList<Commit>();
			Map<String,String> typemap=new HashMap<String, String>();
			Map<String,List<List<String>>> mapExDetail = new HashMap<String, List<List<String>>>();
			/*管制項目狀態(commitList)及分年資料處理(mapExDetail)*/
			for(Commit c :comList){
				String typetmp=c.getType();
				if(!typetmp.equals("04")){
					typemap.put(typetmp, c.getIDNO());
				}
				if(typetmp.equals("02")){
					mapExDetail.put(typetmp, cdSer.select02ByIdno(idno));
				}
				if(typetmp.equals("03")){
					mapExDetail.put(typetmp, cpdSer.getSummary03Report(idno));
				}
				String state = c.getState().equals("Y")?"管制":"解除管制";
				String title = CRType.get(typetmp);
				c.setState(state);
				c.setType(title);
				commitList.add(c);
			}
			if(!mapExDetail.isEmpty()){
				session.setAttribute("mapExDetail", mapExDetail);
			}
			if(!commit.getMaxMinYear(idno).isEmpty()){
				map.put("minmaxyear",commit.getMaxMinYear(idno));
			}
			List<List<String>> investList=new ArrayList<List<String>>();
			Map<String,String> mapInvestNO= commit.getInvestNOList(idno);
			for (Entry<String, String> m:mapInvestNO.entrySet()) {
				List<String> tmp = new ArrayList<String>();
				tmp.add(m.getKey());
				tmp.add(InvestNoToName.get(m.getKey()));
				tmp.add(m.getValue());
				investList.add(tmp);
			}
			List<List<String>> receviceNOList=new ArrayList<List<String>>();
			Map<String,List<String>> mapReceviceNO = commit.getReceviceNOList(idno);
			for (Entry<String, List<String>> m:mapReceviceNO.entrySet()) {
				receviceNOList.add(m.getValue());
			}
			/*取得執行情形填報狀況*/
			List<List<String>> povCR=crSer.getReportPivot(idno);
			Map<String,List<List<String>>> summary=new TreeMap<String, List<List<String>>>();
			for (Entry<String, String> m:typemap.entrySet()) {
				summary.put(m.getKey(), cpdSer.getSummaryReport( m.getValue(),m.getKey()));
			}
			if(summary.size()>0){
				session.setAttribute("summary", summary);
			}
			
			CommitXRestrainOfficeService roSer= new CommitXRestrainOfficeService();
			map.put("restrainOffice", roSer.selectStrNameByIDNO(idno));
			map.put("investor", IDNOToName.get(cdbean.getIDNO()));
			map.put("lastUpdate", DataUtil.toTWDateStr(cdbean.getUpdatetime()));
			map.put("lastEditor",userName.get(cdbean.getUpdateuser())==null?"admin":userName.get(cdbean.getUpdateuser()));
			
			CommitInvestorXContactsService ser= new CommitInvestorXContactsService();
			session.setAttribute("cContacts", ser.select(idno));
			session.setAttribute("cXr", ser.getReceiveNoStr(idno));
			
			session.setAttribute("repTypeMap", repTypeMap);
			session.setAttribute("CRType", CRType);
			session.setAttribute("povCR", povCR);
			session.setAttribute("subcrSer", subcrSer.getReportPivot(idno));
			session.setAttribute("creNOList", receviceNOList);
			session.setAttribute("investList", investList);
			session.setAttribute("crlist", commitList);
			session.setAttribute("cdbean", cdbean);
			session.setAttribute("cdInfo", map);
			session.setAttribute("subcpd", subcpdSer.getSummaryReport(idno));
			session.setAttribute("sub03cpd", subcpdSer.getSummary03Report(idno));
		}
		
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/commit/commitdetail.jsp");
	}
}
