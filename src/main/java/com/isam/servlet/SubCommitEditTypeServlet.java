package com.isam.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
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
import com.isam.bean.CommitDetail;
import com.isam.bean.CommitInvestor;
import com.isam.bean.CommitXRestrainOffice;
import com.isam.bean.SubCommit;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.CommitDetailService;
import com.isam.service.CommitInvestorService;
import com.isam.service.CommitService;
import com.isam.service.ProjectKeyHelp;
import com.isam.service.SubCommitDetailService;
import com.isam.service.SubCommitReportService;
import com.isam.service.SubCommitService;
import com.isam.service.SubCommitXReceiveNoService;
import com.isam.service.SubCommitXRestrainOfficeService;

public class SubCommitEditTypeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommitService commitSer;
	private CommitInvestorService investorSer;
	private SubCommitXRestrainOfficeService officeSer;
	private SubCommitXReceiveNoService receNoSer;
	private SubCommitDetailService detailSer;
	private CommitDetailService cdetailSer;
	private SubCommitService subSer;
	private SubCommitReportService crSer;
	private Map<String, String> IDNOToName;
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		commitSer=new CommitService();
		investorSer=new CommitInvestorService();
		officeSer = new SubCommitXRestrainOfficeService();
		receNoSer = new SubCommitXReceiveNoService();
		detailSer = new SubCommitDetailService();
		subSer= new SubCommitService();
		cdetailSer=new CommitDetailService();
		crSer=new SubCommitReportService();
		ProjectKeyHelp help = new ProjectKeyHelp();
		IDNOToName=help.getIDNOToName();
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
		UserMember ubean = (UserMember) session.getAttribute("userInfo");
		String updateuser = ubean.getIdMember();
		
		String subserno=request.getParameter("subserno");
		String sernoStr=DataUtil.nulltoempty(request.getParameter("sernoStr"));
		String investNo=DataUtil.nulltoempty(request.getParameter("investNo"));
		String idno=DataUtil.nulltoempty(request.getParameter("IDNO"));
		String from=request.getParameter("from")==null?"0":DataUtil.addZeroForNum(request.getParameter("from").trim(), 3);
		String to=request.getParameter("to")==null?"0":DataUtil.addZeroForNum(request.getParameter("to").trim(), 3);
		String state=DataUtil.nulltoempty(request.getParameter("state"));
		String parentType=DataUtil.nulltoempty(request.getParameter("restrainType"));
		String typeYear=request.getParameter("typeYear")==null?"":DataUtil.addZeroForNum(request.getParameter("typeYear").trim(), 3);
		String note=request.getParameter("notes")==null?"":request.getParameter("notes").trim();
		String editType=request.getParameter("editType")==null?"":request.getParameter("editType").trim();
		StringBuilder sb = new StringBuilder();
		
		if(editType.equals("delete")){
			subSer.unable(subserno);
			detailSer.unable(subserno);
			crSer.unableBySerno(subserno);
			updateCommit(sernoStr, parentType, idno, updateuser);
			sb.append("<script language='javascript'>alert('資料已經刪除!'); window.location.href='http://");
			sb.append(request.getServerName()+":"+request.getServerPort()+request.getContextPath());
			sb.append("/console/commit/commitviewo.jsp?serno="+sernoStr+"&editType=show&idno="+idno);
			sb.append("&investor="+IDNOToName.get(idno)+"&tabNo=2';</script>");
			String urls=sb.toString();
			sb.setLength(0);
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print(urls);
			out.flush();
			out.close();
			return;
		}
		
		int totaltemp=Integer.valueOf(to)-Integer.valueOf(from)+1;

		SubCommit bean=subSer.select(sernoStr, investNo, subserno);
		if(bean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('該企業已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommit.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		
		if(subserno==null&&!editType.equals("add")){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('該明細已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommit.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		String repType=bean.getRepType();
		if(parentType.equals("01")&&repType.equals("01")){
			totaltemp = totaltemp*2;
		}
		String total = String.valueOf(totaltemp);
		
		String[] institution=request.getParameterValues("institution");
		String subReceive=DataUtil.fmtStrAryItem(request.getParameterValues("subReceive"));
		
		/*取得不訂長度和未知的RsType name*/
		Enumeration<?> pNames=request.getParameterNames();
		/*分年資料*/
		Map<String,String> amap= new HashMap<String, String>();
		/*國內相對投資總金額另外處理*/
		Map<String,String> tRsTypeMap= new HashMap<String, String>();
		/*其他總金額或百分比*/
		Map<String,String> rsTypeMap = new HashMap<String,String>();
		while(pNames.hasMoreElements()){  
			String name=(String)pNames.nextElement();
			String value="";
			if(name.startsWith("atype")||name.startsWith("aRsType")){
				String[] temp=request.getParameterValues(name);
				value=DataUtil.fmtStrAryItemWithNA(temp);
				amap.put(name, value);
			}else if(name.startsWith("RsType")){
				String key = name.trim().replace("RsType", "");
				value=request.getParameter(name)==null?"":request.getParameter(name).trim().replace(",", "");
				if(!value.isEmpty()){
					rsTypeMap.put(key, value);
				}
			}else if(name.startsWith("TRsType")){
				String key = name.trim().replace("TRsType", "");
				value=request.getParameter(name)==null?"":request.getParameter(name).trim().replace(",", "");
				if(!value.isEmpty()){
					tRsTypeMap.put(key, value);
				}
			}
		} 
		java.sql.Timestamp time = DataUtil.getNowTimestamp();
		if(!investorSer.isExists(idno)){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('該企業已不存在，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommit.jsp';</script>");
			out.flush();
			out.close();
			return;
		}else{
			CommitInvestor investor=investorSer.select(idno);
			investor.setUpdatetime(time);
			investor.setUpdateuser(updateuser);
			investorSer.update(investor);
			
//			System.out.println(commitSer.checkYearRange(idno, parentType, from, to)+":xxx:"+parentType);
//			SubCommit bean = null;
			if(editType.equals("add")){
				bean = new SubCommit();
				bean.setSerno(sernoStr);
				bean.setInvestNo(investNo);
				bean.setType(parentType);
				bean.setState(state);
				bean.setRepType(repType);
				bean.setNote(note);
				bean.setStartYear(from);
				bean.setEndYear(to);
				bean.setUpdatetime(time);
				bean.setUpdateuser(updateuser);
				bean.setCreatetime(time);
				bean.setCreateuser(updateuser);
				bean.setEnable("1");
				bean.setNeedAlert("0");
				subserno =String.valueOf(subSer.insert(bean));
			}else if(editType.equals("edit")){
				bean.setState(state);
				bean.setRepType(repType);
				bean.setNote(note);
				bean.setStartYear(from);
				bean.setEndYear(to);
				bean.setUpdatetime(time);
				bean.setUpdateuser(updateuser);
				bean.setSubserno(subserno);
				subSer.update(bean);
			}
//			System.out.println("bean insert done");
			int subSerno=Integer.valueOf(subserno);
			if(subserno!=null&&subSerno!=-1){
				detailSer.delete(subserno);
				List<CommitDetail> details= new ArrayList<CommitDetail>();
				String atypeYear="";
				if(amap.containsKey("atypeYear")){
					atypeYear = amap.get("atypeYear");
					amap.remove("atypeYear");
				}
				for(Entry<String, String> m:rsTypeMap.entrySet()){
					CommitDetail cd=new CommitDetail();
					cd.setSerno(subSerno);
					cd.setType(m.getKey());
					cd.setYear(typeYear);
					cd.setValue(Double.valueOf(m.getValue()));
					cd.setTotal(total);
					if(parentType.equals("02")){
						cd.setIsFinancial("1");
					}else{
						cd.setIsFinancial("");
					}
					cd.setEnable("1");
					details.add(cd);
				}
				for(Entry<String, String> m:tRsTypeMap.entrySet()){
					CommitDetail cd=new CommitDetail();
					cd.setSerno(subSerno);
					cd.setType(m.getKey());
					cd.setYear(typeYear);
					cd.setValue(Double.valueOf(m.getValue()));
					cd.setTotal(total);
					cd.setIsFinancial("");
					cd.setEnable("1");
					details.add(cd);
				}
//				System.out.println("atypeYear:"+atypeYear);
				String[] aryTypeYear=atypeYear.split(",");
				if(aryTypeYear.length!=0){
					for(Entry<String, String> m:amap.entrySet()){
						String key = m.getKey().replace("aRsType", "");
						String[] valueAry = m.getValue().split(",");
						for(int i=0;i<aryTypeYear.length;i++){
							String val = valueAry[i];
							String year= aryTypeYear[i];
							if(val.trim().length()>0&&!val.equals("NA")){
								CommitDetail cd=new CommitDetail();
								cd.setSerno(subSerno);
								cd.setType(key);
								cd.setYear(year);
								cd.setValue(Double.valueOf(val));
								cd.setTotal("");
								cd.setIsFinancial("");
								cd.setEnable("1");
								details.add(cd);
							}
						}
					}
				}
				detailSer.insert(details);
				
				if(parentType.equals("01")||parentType.equals("03")){
					updateCommit(sernoStr, parentType, idno, updateuser);
				}
				/*修改國內相對投資機關*/
				officeSer.delete(subserno);
				List<CommitXRestrainOffice> institutions=new ArrayList<CommitXRestrainOffice>();
				if(institution!=null){
					for (String s: institution) {
						CommitXRestrainOffice inst=new CommitXRestrainOffice();
						inst.setSerno(subSerno);
						inst.setType(s);
						institutions.add(inst);
					}
					officeSer.insert(institutions);
				}
				/*修改涉及文號*/
				receNoSer.delete(subserno);
				if(!subReceive.isEmpty()){
					receNoSer.insertInto(subserno, sernoStr, subReceive);
				}
			}//end of serno!=-1
		}
		
		String path = request.getContextPath();
		sb.append(path).append("/console/commit/commitviewo.jsp?serno=").append(sernoStr).append("&editType=show&idno=");
		sb.append(idno).append("&investor=").append(IDNOToName.get(idno)).append("&tabNo=2");
		response.sendRedirect(sb.toString());
	}
	private void updateCommit(String serno,String type,String idno,String updateuser){
		   SubCommitDetailService scdSer = new SubCommitDetailService();
		   Map<String,Map<String,Double>> map=scdSer.getDetailSummary(serno,type);
		   Map<String, Double> t=new HashMap<String, Double>();
		   for(Entry<String, Map<String, Double>> m:map.entrySet()){
			   for(Entry<String, Double> sub:m.getValue().entrySet()){
				   String k=sub.getKey();
				   double v=t.containsKey(k)?sub.getValue()+t.get(k):sub.getValue();
				   t.put(k, v);
			   }
		   }
		   java.sql.Timestamp time = DataUtil.getNowTimestamp();
			Commit ibean=commitSer.select(serno);
			ibean.setUpdatetime(time);
			ibean.setUpdateuser(updateuser);
			commitSer.update(ibean);
			
			int tt =ibean.getRepType().equals("01")?2:1;
			int total =(Integer.valueOf(ibean.getEndYear())-Integer.valueOf(ibean.getStartYear())+1)*tt;

			
			int sernoint=Integer.valueOf(serno);
			Map<String,CommitDetail> ocd=cdetailSer.selectOriginalTT(serno);

			cdetailSer.delete(sernoint);
			List<CommitDetail> details= new ArrayList<CommitDetail>();
			DecimalFormat df =new DecimalFormat("0.00");
			if(type.equals("01")){
				for (Entry<String, Double> m:t.entrySet()) {
					CommitDetail b=new CommitDetail();
					b.setSerno(sernoint);
					b.setType(m.getKey());
					b.setYear("");
					b.setValue(Double.valueOf(df.format(m.getValue())));
					b.setTotal(String.valueOf(total));
					b.setIsFinancial("");
					b.setEnable("1");
					details.add(b);
					if(ocd.containsKey(m.getKey())){
//						System.out.println("remove:"+m.getKey());
						ocd.remove(m.getKey());
					}
				}
			}else if(type.equals("03")){
				double tmp=t.get("0301");
				CommitDetail b=new CommitDetail();
				b.setSerno(sernoint);
				b.setType("03");
				b.setYear("");
				b.setValue(Double.valueOf(df.format(tmp)));
				b.setTotal(String.valueOf(total));
				b.setIsFinancial("");
				b.setEnable("1");
				details.add(b);
				if(ocd.containsKey("03")){
					ocd.remove("03");
				}
			}
			for (Entry<String, CommitDetail> m:ocd.entrySet()) {
//				System.out.println(m.getKey());
				details.add(m.getValue());
			}
			for(Entry<String, Map<String, Double>> m:map.entrySet()){
				String year=m.getKey();
				for(Entry<String, Double> sub:m.getValue().entrySet()){
					CommitDetail cd=new CommitDetail();
					cd.setSerno(sernoint);
					cd.setType(sub.getKey());
					cd.setYear(year);
					cd.setValue(Double.valueOf(df.format(sub.getValue())));
					cd.setTotal("");
					cd.setIsFinancial("");
					cd.setEnable("1");
					details.add(cd);
				}
			}
			cdetailSer.insert(details);
			if(type.equals("01")){
				if(cdetailSer.checkAccPt(serno)){
					commitSer.updateNeedAlert(serno, "1");
					investorSer.updateNeedAlert(idno, "1");
				}else{
					commitSer.updateNeedAlert(serno, "0");
					investorSer.updateNeedAlert(idno, "0");
				}
			}
	}
}
