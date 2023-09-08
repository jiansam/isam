package com.isam.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.Interviewone;
import com.isam.bean.InterviewoneContent;
import com.isam.bean.InterviewoneManage;
import com.isam.helper.DataUtil;
import com.isam.service.COMTBDataService;
import com.isam.service.InterviewoneContentService;
import com.isam.service.InterviewoneFileService;
import com.isam.service.InterviewoneHelp;
import com.isam.service.InterviewoneManageService;
import com.isam.service.InterviewoneService;
import com.isam.service.MoeaicDataService;
import com.isam.service.ProjectKeyHelp;
import com.isam.service.ofi.OFIInvestNoXAuditService;

public class InterviewoneShowFollowingByQNoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MoeaicDataService MSer;
	private COMTBDataService CSer;
	private InterviewoneService ioSer;
	private InterviewoneFileService iofSer;
	private InterviewoneContentService iocSer;
	private InterviewoneManageService imSer;
	private InterviewoneHelp iohelper;
	private Map<String,Map<String,String>> optionValName;
	private Map<String, String> levelone;
	private Map<String, String> leveltwo;
	private Map<String, String> userName;
	private OFIInvestNoXAuditService audSer;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		MSer = new MoeaicDataService();
		ioSer = new InterviewoneService();
		iofSer= new InterviewoneFileService();
		iocSer = new InterviewoneContentService();
		imSer=new InterviewoneManageService();
		CSer = new COMTBDataService();
		Map<Integer, Map<String, String>> mapTW=CSer.getTWADDRCode();
		levelone = mapTW.get(1);
		leveltwo = mapTW.get(2);
		iohelper = new InterviewoneHelp();
		optionValName=iohelper.getOptionValName();
		ProjectKeyHelp help = new ProjectKeyHelp();
		userName=help.getUserToName();
		audSer = new OFIInvestNoXAuditService();
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
		session.removeAttribute("IOBaseInfo");
		session.removeAttribute("IObean");
		session.removeAttribute("IOFilelist");
		session.removeAttribute("IMap");
		session.removeAttribute("SMap");
		session.removeAttribute("IOLV1");
		session.removeAttribute("IOLV2");
		session.removeAttribute("optionValName");
		session.removeAttribute("userName");
		session.removeAttribute("LastMap");
		session.removeAttribute("spNeed");
		session.removeAttribute("followinglist");
		session.removeAttribute("sflist");
		
		String qNo=request.getParameter("qNo")==null?"":request.getParameter("qNo").trim();
		List<Interviewone> list=ioSer.select(null, qNo);
		Interviewone bean=null;
		if(!list.isEmpty()){
			bean=list.get(0);
		}
		String investNo=bean.getInvestNo();
		Map<String,String> baseinfo;
		if(bean.getReInvestNo().equals("0")){
			baseinfo=MSer.getCNSysBaseInfo(null, bean.getInvestNo());
			session.setAttribute("spNeed", audSer.getSPNeed(bean.getInvestNo()));
		}else{
			baseinfo=ioSer.getReInvestNoBaseInfo("", bean.getReInvestNo());
		}
		int qnotmp=Integer.valueOf(qNo);
		Map<String,String> LastMap= iocSer.getLastYearFinancial(qnotmp, DataUtil.addZeroForNum(String.valueOf(Integer.valueOf(bean.getYear())-1), 3));
		Map<String,String> IMap= iocSer.select(qnotmp, "I");
		Map<String,String> SMap= iocSer.select(qnotmp, "S");
		
		//2017-07-18 財務異常加入異常原因
		String isFError = iocSer.isFinancialError(qNo)?"異常":"正常"; 
		StringBuilder isFError_reason = new StringBuilder(); 
		if("異常".equals(isFError)){
			int count = 1;
			for(InterviewoneContent error : iocSer.getFinancialError(qNo)){
				
				if(isFError_reason.length() > 0){
					isFError_reason.append("<br>");
				}
				isFError_reason.append("("+count+")"+error.getOptionName()+"："+error.getValue());
				++count;
			}
		}
		baseinfo.put("isFError", isFError);
		baseinfo.put("isFError_reason", isFError_reason.toString());
		
		//2017-07-18 訪視異常加入細項
		String isIError = iocSer.isInterviewError(qNo)?"異常":"正常";
		StringBuilder isIError_reason = new StringBuilder(); 
		if("異常".equals(isIError)){
			int count = 1;
			for(InterviewoneContent error : iocSer.getInterviewError(qNo)){
				
				if(isIError_reason.length() > 0){
					isIError_reason.append("<br>");
				}
				isIError_reason.append("("+count+")"+error.getOptionName()+"："+error.getValue());
				++count;
			}
		}
		baseinfo.put("isIError", isIError);
		baseinfo.put("isIError_reason", isIError_reason.toString());
		baseinfo.put("lastUpdate", DataUtil.toTWDateStr(bean.getUpdatetime()));
		
		List<InterviewoneManage> fl=imSer.selectByQNo(qNo);
		Map<String,List<InterviewoneManage>> fmap=imSer.selectByQNoFlag(fl);
		String canadd="";
		if(fl!=null&&!fl.isEmpty()){
			canadd=fl.get(0).getFollowing();
		}
		baseinfo.put("canadd", canadd);
		session.setAttribute("followinglist", fmap.get("0"));
		session.setAttribute("sflist", fmap.get("1"));
		
		session.setAttribute("spNeed", audSer.getSPNeed(investNo));
		session.setAttribute("LastMap", LastMap);
		session.setAttribute("IMap", IMap);
		session.setAttribute("SMap", SMap);
		session.setAttribute("IOBaseInfo", baseinfo);
		session.setAttribute("IObean", bean);
		session.setAttribute("IOFilelist", iofSer.select(investNo,bean.getReInvestNo(),bean.getYear()));
		session.setAttribute("IOLV1", levelone);
		session.setAttribute("IOLV2", leveltwo);
		session.setAttribute("optionValName", optionValName);
		session.setAttribute("userName", userName);

		
		String path = request.getContextPath();
		response.sendRedirect(path +"/console/interviewone/showFollowing.jsp");		
	}
}
