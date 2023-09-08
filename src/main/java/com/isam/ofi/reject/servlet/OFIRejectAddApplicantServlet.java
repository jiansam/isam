package com.isam.ofi.reject.servlet;

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

import org.json.simple.JSONObject;

import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.ofi.reject.bean.OFIRejectXAgent;
import com.isam.ofi.reject.bean.OFIRejectXApplicant;
import com.isam.ofi.reject.service.OFIRejectXAgentService;
import com.isam.ofi.reject.service.OFIRejectXApplicantService;

public class OFIRejectAddApplicantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIRejectXApplicantService appSer;
	private OFIRejectXAgentService ageSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		appSer=new OFIRejectXApplicantService();
		ageSer=new OFIRejectXAgentService();
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
		UserMember bean = (UserMember) session.getAttribute("userInfo");
		String updateuser = bean.getIdMember();
		Timestamp t=DataUtil.getNowTimestamp();
		
		String editApplyNo = DataUtil.nulltoempty(request.getParameter("applyNo"));
		
		String serno = DataUtil.nulltoempty(request.getParameter("serno"));
		String cApplicant = DataUtil.nulltoempty(request.getParameter("cApplicant"));
		String eApplicant = DataUtil.nulltoempty(request.getParameter("eApplicant"));
		String nation = DataUtil.nulltoempty(request.getParameter("nation"));
		String cnCode = DataUtil.nulltoempty(request.getParameter("cnCode"));
		String note = DataUtil.nulltoempty(request.getParameter("note"));
		String[] agents = request.getParameterValues("agent");
		String[] postions = request.getParameterValues("pos");
		int applyNo;
		
		
		
		OFIRejectXApplicant applicant=new OFIRejectXApplicant();
		if(!serno.isEmpty()) {
			applicant.setSerno(Integer.valueOf(serno));
		}
		applicant.setcApplicant(cApplicant);
		applicant.seteApplicant(eApplicant);
		applicant.setNation(nation);
		applicant.setCnCode(cnCode);
		applicant.setNote(note);
		applicant.setUpdatetime(t);
		applicant.setUpdateuser(updateuser);
		if(editApplyNo.isEmpty()){
			applicant.setEnable("1");
			applicant.setCreatetime(t);
			applicant.setCreateuser(updateuser);
			applyNo=Integer.valueOf(appSer.insert(applicant));
		}else{
			applyNo=Integer.valueOf(editApplyNo);
			applicant.setApplyNo(applyNo);
			appSer.update(applicant);
			ageSer.delete(editApplyNo);
		}
		
		List<OFIRejectXAgent> list=new ArrayList<OFIRejectXAgent>();
		OFIRejectXAgent sub;
		
		if(agents!=null){
			for (int i = 0; i < agents.length; i++) {
				sub=new OFIRejectXAgent();
				sub.setAgent(agents[i]);
				sub.setApplyNo(applyNo);
				sub.setPostion(postions[i]);
				list.add(sub);
			}
		}
		if(list.size()>0){
			ageSer.insert(list);
		}
		
		JSONObject obj=new JSONObject();
		obj.put("applyNo", applyNo);
		obj.put("serno", serno);
		
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(obj.toJSONString());
		out.close();
	}
}
