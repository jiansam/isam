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

import org.json.simple.JSONArray;

import com.isam.bean.Commit;
import com.isam.service.CommitDetailService;
import com.isam.service.CommitInvestorXContactsService;
import com.isam.service.CommitService;
import com.isam.service.CommitXInvestNoService;
import com.isam.service.CommitXReceiveNoService;
import com.isam.service.CommitXRestrainOfficeService;
import com.isam.service.ProjectKeyHelp;

public class CommitShowEditOServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommitXRestrainOfficeService officeSer;
	private CommitXReceiveNoService receNoSer;
	private CommitXInvestNoService investNoSer;
	private CommitDetailService detailSer;
	private CommitService commitSer;
	private Map<String, String> UserName;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		officeSer = new CommitXRestrainOfficeService();
		receNoSer = new CommitXReceiveNoService();
		detailSer = new CommitDetailService();
		commitSer = new CommitService();
		investNoSer = new CommitXInvestNoService();
		ProjectKeyHelp help = new ProjectKeyHelp();
		UserName=help.getUserToName();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.removeAttribute("info");
		session.removeAttribute("bean");
		session.removeAttribute("detailBean");
		session.removeAttribute("detailEx");
		session.removeAttribute("office");
		session.removeAttribute("receiveNo");
		session.removeAttribute("investNo");
		
		String serno=request.getParameter("serno")==null?"":request.getParameter("serno").trim();
		String editType=request.getParameter("editType")==null?"":request.getParameter("editType").trim();
		String idno=request.getParameter("idno")==null?"":request.getParameter("idno").trim();
		String investor=request.getParameter("investor")==null?"":request.getParameter("investor").trim();
		String typetmp=request.getParameter("type")==null?"":request.getParameter("type").trim();
		
		Map<String,String> info=new HashMap<String, String>();
		info.put("serno", serno);
		info.put("editType", editType);
		info.put("idno", idno);
		info.put("investor", investor);
		
	/*	request.setAttribute("serno", serno);
		request.setAttribute("editType", editType);
		request.setAttribute("idno", idno);
		request.setAttribute("investor", investor);*/
		int type=0;
		
		Commit bean= commitSer.select(serno);
		if(bean==null&&!editType.equals("add")){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您選取的資料已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommitdetail.jsp?serno="+idno+"';</script>");
			out.flush();
			out.close();
			return;
		}
//		request.setAttribute("bean", bean);
//		request.setAttribute("editor", UserName.get(bean.getUpdateuser()));
		if(editType.equals("edit")){
			session.setAttribute("bean", bean);
			info.put("editor", UserName.get(bean.getUpdateuser()));
			
			JSONArray detailBean = detailSer.getJsonFmt(serno);
//		request.setAttribute("detailBean", detailBean);
			session.setAttribute("detailBean", detailBean);
			List<List<String>> detailEx =  detailSer.select(serno,bean.getType());
			
			if(detailEx!=null&&!detailEx.isEmpty()){
//			request.setAttribute("detailEx", detailEx);
				session.setAttribute("detailEx", detailEx);
			}
			String office = officeSer.selectStr(serno);
			if(office!=null&&!office.isEmpty()){
//			request.setAttribute("office", office);
				session.setAttribute("office", office);
			}		
			JSONArray receiveNo =receNoSer.getJsonFmt(serno);
//		request.setAttribute("receiveNo", receiveNo);
			session.setAttribute("receiveNo", receiveNo);
			JSONArray investNo = investNoSer.getJsonFmt(serno);
//		request.setAttribute("investNo", investNo);
			session.setAttribute("investNo", investNo);
			String typetemp=bean.getType();
			type=Integer.valueOf(typetemp);
		}else if(editType.equals("add")){
			type=Integer.valueOf(typetmp);
		}
		
		//106-12-29 追加判斷，如果下面的聯絡人不是空的，在編輯頁面-變動  管制或解除管制選項時，就問要不要轉到聯絡人頁面繼續編輯
		CommitInvestorXContactsService ser= new CommitInvestorXContactsService();
		session.setAttribute("cContacts", ser.select(idno).size());
		
		session.setAttribute("info", info);
		switch (type) {
		case 1:
			request.getRequestDispatcher("/console/commit/commitrestrain.jsp").forward(request, response);
			break;
		case 2:
			request.getRequestDispatcher("/console/commit/commitfinancial.jsp").forward(request, response);
			break;
		case 3:
			request.getRequestDispatcher("/console/commit/commitcapital.jsp").forward(request, response);
			break;
		case 4:
			request.getRequestDispatcher("/console/commit/commitfinancialservice.jsp").forward(request, response);
			break;
		default:
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您要修改的資料已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommitdetail.jsp?serno="+idno+"';</script>");
			out.flush();
			out.close();
			return;
		} 
//		String path = request.getContextPath();
//		response.sendRedirect(path + "/console/commit/commitrestrain.jsp?idno="+idno+"&editType="+editType+"&investor="+investor+"serno="+sernoTmp);
	}
}
