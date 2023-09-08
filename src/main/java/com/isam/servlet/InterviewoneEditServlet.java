package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
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
import com.isam.bean.InterviewoneItem;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.InterviewoneContentService;
import com.isam.service.InterviewoneHelp;
import com.isam.service.InterviewoneService;

public class InterviewoneEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private InterviewoneService ioSer;
	private InterviewoneContentService iocSer;
	private InterviewoneHelp iohelper;
	private Map<String,Map<String,Integer>> optionValNo;
	private Map<String,InterviewoneItem> qTypeS;
	private Map<String,InterviewoneItem> qTypeI;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ioSer = new InterviewoneService();
		iocSer = new InterviewoneContentService();
		iohelper = new InterviewoneHelp();
		optionValNo=iohelper.getOptionValNo();
		qTypeS=iohelper.getqTypeS();
		qTypeI=iohelper.getqTypeI();
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
		
		String qNoTemp=request.getParameter("qNo")==null?"":request.getParameter("qNo").trim();
		String year=request.getParameter("year")==null?"":request.getParameter("year").trim();
		String qType=request.getParameter("qType")==null?"":request.getParameter("qType").trim();
		String editType=request.getParameter("editType")==null?"":request.getParameter("editType").trim();

		int qNo = Integer.valueOf(qNoTemp);
		
		Map<String,InterviewoneItem> items=new HashMap<String, InterviewoneItem>();
		String reurl="";
		if(qType.equals("I")){
			items.putAll(qTypeI);
			reurl="/console/interviewone/getinterviewone.jsp"; //I 訪視記錄表
		}else{
			items.putAll(qTypeS);
			reurl="/console/interviewone/getsurveyone.jsp"; //S 營運問卷
		}
		List<Interviewone> iobeans=ioSer.select(year, qNoTemp);
		if(editType.equals("delete")){
			iobeans=ioSer.select(null,qNoTemp);
		}
		Interviewone iobean=null;
		if(!iobeans.isEmpty()){
			iobean=iobeans.get(0);
		}else{
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('本筆資料未在年度訪視清單內，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/interviewone/getinterviewone.jsp';</script>");
			out.flush();
			return;
		}
		iobean.setUpdatetime(DataUtil.getNowTimestamp());
		iobean.setUpdateuser(updateuser);
		
		if(editType.equals("delete")){
			iocSer.delete(qNo,qType);
			if(qType.equals("I")){
				iobean.setInterviewStatus("0");
			}else if(qType.equals("S")){
				iobean.setSurveyStatus("0");
			}
			ioSer.update(iobean);
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('資料已刪除!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/interviewone/showiolist.jsp';</script>");
			out.flush();
			return;
		}
		List<InterviewoneContent> list=new ArrayList<InterviewoneContent>();
		/*取得不訂長度和未知的RsType name*/
		Enumeration<?> pNames=request.getParameterNames();
		while(pNames.hasMoreElements()){  
			String name=(String)pNames.nextElement();
			String value="";
			String pName="";
			int seq=1;
			if(name.contains("_")){
				int e=name.indexOf("_");
				pName=name.substring(0, e);
				seq=Integer.valueOf(name.substring(e+1));
			}else{
				pName=name;
			}
//			System.out.println(pName+":"+seq);
			if(items.containsKey(pName)){
				if(optionValNo.containsKey(pName)){
					String[] temp = request.getParameterValues(name);
					for (int i = 0; i < temp.length; i++) {
//						seq=i+1; 
						value=temp[i];
						if(pName.equals("interviewStatus")){
							iobean.setInterviewStatus(value);
						}else if(pName.equals("surveyStatus")){
							iobean.setSurveyStatus(value);
						}else{
							if(!value.isEmpty()){
								InterviewoneContent bean = new InterviewoneContent();
								bean.setqNo(qNo);
								bean.setSeq(seq);
								bean.setOptionId(items.get(pName).getOptionId());
								bean.setValue(value);
								list.add(bean);
							}
						}
//						System.out.println(pName+":"+seq+":"+value);
					}
				}else{
					value = request.getParameter(name)==null?"": request.getParameter(name).trim();
					if(name.equals("reportdate")){
						value =value.replace("/", "");
					}
					if(!value.isEmpty()){
						InterviewoneContent bean = new InterviewoneContent();
						bean.setqNo(qNo);
						bean.setSeq(seq);
						bean.setOptionId(items.get(pName).getOptionId());
						bean.setValue(value);
						list.add(bean);
					}
				}
			}
		} 
		if(list.size()>0){
			ioSer.update(iobean);
			iocSer.delete(qNo,qType);
			iocSer.insert(list);
		}
		if(editType.isEmpty()){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('新增完成!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+reurl+"';</script>");
			out.flush();
			return;
		}else{
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('編輯完成!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/interviewone/listbyqno.jsp?qNo="+iobean.getqNo()+"+';</script>");
			out.flush();
			return;
		}
	}
}
