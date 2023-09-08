package com.isam.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.Project;
import com.isam.helper.ApplicationAttributeHelper;
import com.isam.helper.DataUtil;
import com.isam.service.ProjectKeyHelp;
import com.isam.service.ProjectService;

public class ProjectShowDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<String, String> IDNOToName;
	private Map<String, String> userName;
	private Map<String,String> projState;
	private ProjectService ser;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ser= new ProjectService();
		ProjectKeyHelp help = new ProjectKeyHelp();
		IDNOToName = help.getIDNOToName();
		userName=help.getUserToName();
		projState=help.getProjectState();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, String> InvestNoToName = ApplicationAttributeHelper.getInvestNoToName(request.getServletContext());
		
		HttpSession session = request.getSession();
		session.removeAttribute("pjbean");
		session.removeAttribute("projInfo");
		session.removeAttribute("nnMap");
		
		String serno=request.getParameter("serno")==null?"":request.getParameter("serno").trim();
		String updateMark=request.getParameter("updateMark")==null?"":request.getParameter("updateMark").trim();
//			System.out.println(serno);
		List<Project> beans = ser.select(serno, null);
		if(beans!=null&&beans.size()==1){
			Project bean =  beans.get(0);
			Map<String,String> map = new HashMap<String, String>();
			session.setAttribute("pjbean", bean);
			map.put("cnName", InvestNoToName.get(bean.getInvestNo()));
			map.put("investor", IDNOToName.get(bean.getIDNO()));
			map.put("lastUpdate", DataUtil.toTWDateStr(bean.getUpdatetime()));
			map.put("lastEditor",userName.get(bean.getUpdateuser())==null?"admin":userName.get(bean.getUpdateuser()));
			if(updateMark.equals("ok")){
				map.put("updateOk","資料已更新成功");
				map.put("done","0");
			}else if(updateMark.equals("pjok")){
				map.put("done","2");
				StringBuilder sb=new StringBuilder();
				sb.append(request.getParameter("year")).append("年/第").append(request.getParameter("season")).append("季");
				sb.append("企業申報報表已經更新完成！");
				map.put("updateOk",sb.toString());
				sb.setLength(0);
			}else if(updateMark.equals("pjdel")){
				map.put("done","2");
				StringBuilder sb=new StringBuilder();
				sb.append("已經刪除");
				sb.append(request.getParameter("year")).append("年/第").append(request.getParameter("season")).append("季");
				sb.append("的企業申報報表！");
				map.put("updateOk",sb.toString());
				sb.setLength(0);
			}
			session.setAttribute("projInfo", map);
			session.setAttribute("projState", projState);
		}
		String path = request.getContextPath();
		response.sendRedirect(path + "/console/project/projectdetail.jsp");
	}
}
