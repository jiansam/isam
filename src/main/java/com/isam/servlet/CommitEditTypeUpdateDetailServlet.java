package com.isam.servlet;

import java.io.IOException;
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
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.CommitDetailService;
import com.isam.service.CommitInvestorService;
import com.isam.service.CommitService;

public class CommitEditTypeUpdateDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommitDetailService detailSer;
	private CommitService commitSer;
	private CommitInvestorService investorSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		detailSer = new CommitDetailService();
		commitSer = new CommitService();
		investorSer= new CommitInvestorService();
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
		
		String sernoStr=DataUtil.nulltoempty(request.getParameter("serno"));
		String idno=DataUtil.nulltoempty(request.getParameter("idno"));
		String investor=DataUtil.nulltoempty(request.getParameter("investor"));
		String parentType=DataUtil.nulltoempty(request.getParameter("parentType"));
		String total=DataUtil.nulltoempty(request.getParameter("total"));
//		System.out.println(sernoStr+";"+idno+";"+parentType+";"+total+";"+investor);
		int serno=Integer.valueOf(sernoStr);
		
		/*取得不訂長度和未知的RsType name*/
		Enumeration<?> pNames=request.getParameterNames();
		/*國內相對投資總金額另外處理*/
		Map<String,String> tRsTypeMap= new HashMap<String, String>();
		/*其他總金額或百分比*/
		Map<String,String> rsTypeMap = new HashMap<String,String>();
		while(pNames.hasMoreElements()){  
			String name=(String)pNames.nextElement();
			String value="";
			if(name.startsWith("sRType")){
				String key = name.trim().replace("sRType", "");
				value=request.getParameter(name)==null?"":request.getParameter(name).trim().replace(",", "");
//				System.out.println("RsType:"+key+"="+value);
				if(!value.isEmpty()){
					rsTypeMap.put(key, value);
				}
			}else if(name.startsWith("tRType")){
				String key = name.trim().replace("tRType", "");
				value=request.getParameter(name)==null?"":request.getParameter(name).trim().replace(",", "");
				if(!value.isEmpty()){
					tRsTypeMap.put(key, value);
				}
			}
		} 
		java.sql.Timestamp time = DataUtil.getNowTimestamp();
		Commit ibean=commitSer.select(sernoStr);
		ibean.setUpdatetime(time);
		ibean.setUpdateuser(updateuser);
		commitSer.update(ibean);
		
		detailSer.delete(serno);
		List<CommitDetail> details= new ArrayList<CommitDetail>();
		for (Entry<String, String> m:tRsTypeMap.entrySet()) {
			CommitDetail b=new CommitDetail();
			b.setSerno(serno);
			b.setType(m.getKey());
			b.setYear("");
			b.setValue(DataUtil.SToD(m.getValue()));
			b.setTotal(total);
			b.setIsFinancial("");
			b.setEnable("1");
			details.add(b);
		}
		for(Entry<String, String> m:rsTypeMap.entrySet()){
			String[] tmp=m.getKey().split("-");
			CommitDetail cd=new CommitDetail();
			cd.setSerno(serno);
			cd.setType(tmp[0]);
			cd.setYear(tmp[1]);
			cd.setValue(DataUtil.SToD(m.getValue()));
			cd.setTotal("");
			cd.setIsFinancial("");
			cd.setEnable("1");
			details.add(cd);
		}
		detailSer.insert(details);
		if(parentType.equals("01")){
			if(detailSer.checkAccPt(sernoStr)){
				commitSer.updateNeedAlert(sernoStr, "1");
				investorSer.updateNeedAlert(idno, "1");
			}else{
				commitSer.updateNeedAlert(sernoStr, "0");
				investorSer.updateNeedAlert(idno, "0");
			}
		}

		request.setAttribute("editType","show");  
		request.setAttribute("serno",serno);  
		request.setAttribute("idno",idno);  
		request.setAttribute("investor",investor);  
		request.getRequestDispatcher("/console/commit/commitviewo.jsp").forward(request,response); 
	}

}
