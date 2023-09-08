package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.isam.bean.Commit;
import com.isam.bean.CommitReport;
import com.isam.bean.CommitReportDetail;
import com.isam.bean.SubCommit;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.CommitDetailService;
import com.isam.service.CommitInvestorService;
import com.isam.service.CommitReportDetailService;
import com.isam.service.CommitReportService;
import com.isam.service.CommitService;
import com.isam.service.SubCommitReportDetailService;
import com.isam.service.SubCommitReportService;
import com.isam.service.SubCommitService;

public class SubCommitReportEditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommitService Ser;
	private SubCommitService cSer;
	private SubCommitReportDetailService crdSer;
	private SubCommitReportService crSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		cSer=new SubCommitService();
		Ser=new CommitService();
		crdSer = new SubCommitReportDetailService();
		crSer = new SubCommitReportService();
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

		String repSernoTemp=request.getParameter("repSerno")==null?"":request.getParameter("repSerno").trim();
		String idno=request.getParameter("idno")==null?"":request.getParameter("idno").trim();
//		String investor=request.getParameter("investor")==null?"":request.getParameter("investor").trim();
//		String serno=request.getParameter("sernoStr")==null?"":request.getParameter("sernoStr").trim();
		
		String subserno=DataUtil.nulltoempty(request.getParameter("subserno"));
		String repType=request.getParameter("repType")==null?"":request.getParameter("repType").trim();
		String year=request.getParameter("year")==null?"":DataUtil.addZeroForNum(request.getParameter("year").trim(), 3);
		String parentType=request.getParameter("restrainType")==null?"":request.getParameter("restrainType").trim();
		String editType=request.getParameter("editType")==null?"":request.getParameter("editType").trim();
		String count=request.getParameter("count")==null?"0":request.getParameter("count").trim();
		String note=request.getParameter("note")==null?"":request.getParameter("note").trim();
		String keyinNo=request.getParameter("keyinNo")==null?"":request.getParameter("keyinNo").trim();
		String isConversion=request.getParameter("isConversion")==null?"N":request.getParameter("isConversion").trim();
		
		SubCommit subbean= cSer.select(subserno);
		if(subbean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('您要新增的填報執行情形所屬的管制項目已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommit.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
//		System.out.println("subbean.getSerno()="+subbean.getSerno());
		Commit cbean=Ser.select(subbean.getSerno());
		idno=cbean.getIDNO();
//		System.out.println("idno="+idno);
		if(editType.equals("delete")){
			crSer.unable(repSernoTemp);
			crdSer.unable(repSernoTemp);
			if(subbean.getType().equals("01")||subbean.getType().equals("03")){
				updateCommit(subbean.getSerno(),subbean.getType(), idno, updateuser);
			}
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommitdetail.jsp?serno="+idno+"&updateOK=2';</script>");
			out.flush();
			out.close();
			return;
		}
		
		/*取得不訂長度和未知的RsType name*/
		Enumeration<?> pNames=request.getParameterNames();
		Map<String,String> oRsTypeMap= new HashMap<String, String>();
		Map<String,String> rsTypeMap = new HashMap<String,String>();
		while(pNames.hasMoreElements()){  
			String name=(String)pNames.nextElement();
			String value="";
			if(name.startsWith("ORsType")){
				String key = name.trim().replace("ORsType", "");
				value=request.getParameter(name)==null?"0":request.getParameter(name).trim().replace(",", "");
				oRsTypeMap.put(key, value);
			}else if(name.startsWith("RsType")){
				String key = name.trim().replace("RsType", "");
				value=request.getParameter(name)==null?"0":request.getParameter(name).trim().replace(",", "");
				rsTypeMap.put(key, value);
			}
		} 
		java.sql.Timestamp time = DataUtil.getNowTimestamp();

		int repSerno=-1;
			CommitReport crBean;
			if(editType.equals("add")){
				crBean=new CommitReport();
				crBean.setSerno(Integer.valueOf(subserno));
				crBean.setYear(year);
				if(repType.isEmpty()){
					crBean.setRepType(parentType);
					repType=parentType;
				}else{
					crBean.setRepType(repType);
				}
				if(crSer.isExists(subserno, year,repType)!=0){
					repSerno=crSer.getRepSerno(subserno, year,repType);
					request.setCharacterEncoding("UTF-8");
					response.setContentType("text/html;charset=UTF-8");
					PrintWriter out = response.getWriter();
					out.print("<script language='javascript'>alert('本筆資料已被新增過，即將轉往修改畫面!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showsubcommitreport.jsp?repserno="+repSerno+"&editType=edit';</script>");
					out.flush();
					out.close();
					return;
				}
				crBean.setUpdatetime(time);
				crBean.setUpdateuser(updateuser);
				crBean.setCreatetime(time);
				crBean.setCreateuser(updateuser);
				crBean.setNote(note);
				crBean.setIsConversion(isConversion);
				crBean.setKeyinNo(keyinNo);
				crBean.setEnable("1");
				crBean.setIsOnline("N");
				crSer.insert(crBean);
				repSerno=crSer.getRepSerno(subserno, year,repType);
			}else if(editType.equals("edit")){
				crBean = crSer.select(repSernoTemp);
				crBean.setUpdatetime(time);
				crBean.setUpdateuser(updateuser);
				crBean.setNote(note);
				crBean.setIsConversion(isConversion);
				crBean.setKeyinNo(keyinNo);
				crSer.update(crBean);
				repSerno=crBean.getRepserno();
			}
			if(!parentType.equals("01")){
				count= String.valueOf(Integer.valueOf(subbean.getEndYear())-Integer.valueOf(subbean.getStartYear())+1);
			}
			if(repSerno!=-1){
				crdSer.delete(repSerno);
				List<CommitReportDetail> crdBeans=new ArrayList<CommitReportDetail>();
				for(Entry<String, String> m:rsTypeMap.entrySet()){
					String type = m.getKey();
					String repvalue = m.getValue().isEmpty()?"0": m.getValue();
					String corpvalue = "0";
					if(oRsTypeMap.containsKey(type)){
						corpvalue = oRsTypeMap.get(type).isEmpty()?"0":oRsTypeMap.get(type);
					}
//					if(repvalue.equals("0")&&corpvalue.equals("0")){
//						continue;
//					}else{
						CommitReportDetail crdBean = new CommitReportDetail();
						crdBean.setRepserno(repSerno);
						crdBean.setType(type);
						crdBean.setCorpvalue(Double.valueOf(corpvalue));
						crdBean.setRepvalue(Double.valueOf(repvalue));
						crdBean.setYear(year);
						crdBean.setCount(Integer.valueOf(count));
						crdBean.setEnable("1");	
						crdBeans.add(crdBean);
//					}
				}
				crdSer.insert(crdBeans);
			}
			if(parentType.equals("01")||parentType.equals("03")){
				updateCommit(subbean.getSerno(),subbean.getType(), idno, updateuser);
			}
		String path = request.getContextPath();
		StringBuilder sb = new StringBuilder();
		sb.append(path).append("/console/commit/showcommitdetail.jsp?serno=").append(idno).append("&updateOK=2");
		response.sendRedirect(sb.toString());
		
	}
	private void updateCommit(String serno,String type,String idno,String updateuser){
		CommitDetailService cdSer=new CommitDetailService();
		CommitInvestorService ciSer=new CommitInvestorService();
		CommitReportDetailService rcdSer=new CommitReportDetailService();
		CommitReportService rcSer=new CommitReportService();
		
		rcSer.updateCommitReportBySub(serno, updateuser);
		rcdSer.updateCommitReportDetailsBySub(serno,type);
		rcSer.checkCommitReportHasSubData(serno, updateuser);
		
		if(type.equals("01")){
			if(cdSer.checkAccPt(serno)){
				Ser.updateNeedAlert(serno, "1");
				ciSer.updateNeedAlert(idno, "1");
			}else{
				Ser.updateNeedAlert(serno, "0");
				ciSer.updateNeedAlert(idno, "0");
			}
		}
	}
}
