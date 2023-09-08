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
import com.isam.bean.CommitDetail;
import com.isam.bean.CommitInvestor;
import com.isam.bean.CommitXInvestNo;
import com.isam.bean.CommitXReceiveNo;
import com.isam.bean.CommitXRestrainOffice;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.CommitDetailService;
import com.isam.service.CommitInvestorService;
import com.isam.service.CommitService;
import com.isam.service.CommitXInvestNoService;
import com.isam.service.CommitXReceiveNoService;
import com.isam.service.CommitXRestrainOfficeService;

public class CommitEditTypeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommitInvestorService investorSer;
	private CommitXRestrainOfficeService officeSer;
	private CommitXReceiveNoService receNoSer;
	private CommitXInvestNoService investNoSer;
	private CommitDetailService detailSer;
	private CommitService commitSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		investorSer= new CommitInvestorService();
		officeSer = new CommitXRestrainOfficeService();
		receNoSer = new CommitXReceiveNoService();
		detailSer = new CommitDetailService();
		commitSer = new CommitService();
		investNoSer = new CommitXInvestNoService();
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
		
		String sernoStr=request.getParameter("sernoStr")==null?"":request.getParameter("sernoStr").trim();
		String idno=request.getParameter("IDNO")==null?"":request.getParameter("IDNO").trim();
		String from=request.getParameter("from")==null?"":DataUtil.addZeroForNum(request.getParameter("from").trim(), 3);
		String to=request.getParameter("to")==null?"":DataUtil.addZeroForNum(request.getParameter("to").trim(), 3);
		String repType=request.getParameter("repType")==null?"":request.getParameter("repType").trim();
		String state=request.getParameter("state")==null?"":request.getParameter("state").trim();
		String parentType=request.getParameter("restrainType")==null?"":request.getParameter("restrainType").trim();
		String typeYear=request.getParameter("typeYear")==null?"":DataUtil.addZeroForNum(request.getParameter("typeYear").trim(), 3);
		String note=request.getParameter("notes")==null?"":request.getParameter("notes").trim();
		String editType=request.getParameter("editType")==null?"":request.getParameter("editType").trim();
		String DelreNo=request.getParameter("DelreNo")==null?"":request.getParameter("DelreNo").trim();
		int totaltemp=Integer.valueOf(to)-Integer.valueOf(from)+1;
		if(parentType.equals("01")&&repType.equals("01")){
			totaltemp = totaltemp*2;
		}
		
		String total = String.valueOf(totaltemp);
//		System.out.println("CommitUpdateInvestor:"+idno);
		String[] institution=request.getParameterValues("institution");
		String[] investNo=request.getParameterValues("investNo");
		
//		String[] receiveNo=request.getParameterValues("receiveNo");
		String[] receiveNoAdd=request.getParameterValues("receiveNoAdd");
		
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
//				if(value.isEmpty()){
//					value="NA";
//				}
//				System.out.println(name+"="+value);
				amap.put(name, value);
			}else if(name.startsWith("RsType")){
				String key = name.trim().replace("RsType", "");
				value=request.getParameter(name)==null?"":request.getParameter(name).trim().replace(",", "");
//				System.out.println("RsType:"+key+"="+value);
				if(!value.isEmpty()){
					rsTypeMap.put(key, value);
				}
			}else if(name.startsWith("TRsType")){
				String key = name.trim().replace("TRsType", "");
				value=request.getParameter(name)==null?"":request.getParameter(name).trim().replace(",", "");
//				System.out.println("RsType:"+key+"="+value);
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
			
			int serno =-1;
//			System.out.println(commitSer.checkYearRange(idno, parentType, from, to)+":xxx:"+parentType);
				Commit commit = null;
			if(editType.equals("add")){
				if(commitSer.checkYearRange(idno, parentType, from, to)==0){
					commit = new Commit();
					commit.setIDNO(idno);
					commit.setType(parentType);
					commit.setState(state);
					commit.setRepType(repType);
					commit.setNote(note);
					commit.setStartYear(from);
					commit.setEndYear(to);
					commit.setUpdatetime(time);
					commit.setUpdateuser(updateuser);
					commit.setCreatetime(time);
					commit.setCreateuser(updateuser);
					commit.setEnable("1");
					commit.setNeedAlert("0");
					commitSer.insert(commit);
					serno = commitSer.getSerno(idno, parentType, from, to);
				}else{
					request.setCharacterEncoding("UTF-8");
					response.setContentType("text/html;charset=UTF-8");
					PrintWriter out = response.getWriter();
					out.print("<script language='javascript'>alert('起始年度不可與其他重疊，請重新輸入!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommit.jsp';</script>");
					out.flush();
					out.close();
					return;
				}
			}else if(editType.equals("edit")){
				commit = commitSer.select(sernoStr);
				serno = commit.getSerno();
				/*限制除了自己以外的*/
				if(commitSer.checkYearRange(idno, parentType, from, to,serno)!=0){
					request.setCharacterEncoding("UTF-8");
					response.setContentType("text/html;charset=UTF-8");
					PrintWriter out = response.getWriter();
					out.print("<script language='javascript'>alert('起始年度不可與其他重疊，請重新輸入!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/commit/showcommit.jsp';</script>");
					out.flush();
					out.close();
					return;
				}
				commit.setNote(note);
				commit.setState(state);
				commit.setRepType(repType);
				commit.setStartYear(from);
				commit.setEndYear(to);
				commit.setUpdatetime(time);
				commit.setUpdateuser(updateuser);
				commitSer.update(commit);
			}
//			System.out.println("commit insert done");
			if(serno!=-1){
				detailSer.delete(serno);
				List<CommitDetail> details= new ArrayList<CommitDetail>();
				String atypeYear="";
				if(amap.containsKey("atypeYear")){
					atypeYear = amap.get("atypeYear");
					amap.remove("atypeYear");
				}
				for(Entry<String, String> m:rsTypeMap.entrySet()){
					CommitDetail cd=new CommitDetail();
					cd.setSerno(serno);
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
					cd.setSerno(serno);
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
								cd.setSerno(serno);
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
				if(parentType.equals("01")){
					if(detailSer.checkAccPt(String.valueOf(serno))){
						commitSer.updateNeedAlert(String.valueOf(serno), "1");
						investorSer.updateNeedAlert(idno, "1");
					}else{
						commitSer.updateNeedAlert(String.valueOf(serno), "0");
						investorSer.updateNeedAlert(idno, "0");
					}
				}
				/*修改國內相對投資機關*/
				officeSer.delete(serno);
				List<CommitXRestrainOffice> institutions=new ArrayList<CommitXRestrainOffice>();
				if(institution!=null){
					for (String s: institution) {
						CommitXRestrainOffice inst=new CommitXRestrainOffice();
						inst.setSerno(serno);
						inst.setType(s);
						institutions.add(inst);
					}
					officeSer.insert(institutions);
				}
				if(!DelreNo.equals("0")){
					/*修改涉及案號*/
					investNoSer.delete(serno);
					List<CommitXInvestNo> investNos=new ArrayList<CommitXInvestNo>();
					if(investNo!=null){
						for (String s: investNo) {
							CommitXInvestNo inst=new CommitXInvestNo();
							inst.setSerno(serno);
							inst.setInvestNo(s);
							investNos.add(inst);
						}
						investNoSer.insert(investNos);
					}
					/*修改涉及文號*/
					receNoSer.delete(serno);
					List<CommitXReceiveNo> receiveNos=new ArrayList<CommitXReceiveNo>();
					if(receiveNoAdd!=null){
						for (String s: receiveNoAdd) {
							String[] strary=s.split("&&&");
							CommitXReceiveNo inst=new CommitXReceiveNo();
							inst.setSerno(serno);
							inst.setReceiveNo(strary[0].trim());
							inst.setRespDate(strary[1].trim());
							inst.setNote(strary[2].trim());
							receiveNos.add(inst);
						}
						receNoSer.insert(receiveNos);
					}
				}
			}//end of serno!=-1
		}
		
		
		String path = request.getContextPath();
		StringBuilder sb = new StringBuilder();
		sb.append(path).append("/console/commit/showcommitdetail.jsp?serno=").append(idno);
		//107-01-04 如果要調整完管制或解除管制，要繼續編輯聯絡人，就跳轉到聯絡人tab。利用updateOK的數字跳轉tab
		if( "Y".equals(request.getParameter("isEditContact")) ) {
			sb.append("&updateOK=6");
		}else {
			sb.append("&updateOK=1");
		}
		response.sendRedirect(sb.toString());
		
	}

}
