package com.isam.servlet.ofi;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Timestamp;
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

import com.isam.bean.OFIInvestList;
import com.isam.bean.OFIInvestNoXAudit;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.ofi.OFIInvestListService;
import com.isam.service.ofi.OFIInvestNoXAuditService;

public class OFIInvestXAudit02EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OFIInvestListService listSer;
	private OFIInvestNoXAuditService aSer;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		listSer = new OFIInvestListService();
		aSer = new OFIInvestNoXAuditService();
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
		
		String investNo=DataUtil.nulltoempty(request.getParameter("investNo"));
		String seq=DataUtil.nulltoempty(request.getParameter("seq"));
		String type=DataUtil.nulltoempty(request.getParameter("type"));
		String tabsNum=DataUtil.nulltoempty(request.getParameter("tabsNum"));
		String other=DataUtil.nulltoempty(request.getParameter("other"));
//		System.out.println("other="+other);
		OFIInvestList bean=listSer.select(investNo);
		if(bean==null){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('此資料無法編輯，請重新選取!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"';</script>");
			out.flush();
			out.close();
			return;
		}
		Timestamp time=DataUtil.getNowTimestamp();
		/*取得不訂長度和未知的RsType name*/
		Enumeration<?> pNames=request.getParameterNames();
		List<OFIInvestNoXAudit> beans = new ArrayList<OFIInvestNoXAudit>();
		if(type.equals("delete")){
			aSer.deleteAudit02(investNo, seq);
		}else if(type.equals("add")){
			while(pNames.hasMoreElements()){  
				String name=(String)pNames.nextElement();
				if(DataUtil.isMatchFmtNumber(name)){
					String str=DataUtil.nulltoempty(request.getParameter(name));
					if(name.equals("0201")){
						 str=str.replace("/", "");
					}
					OFIInvestNoXAudit sub= new OFIInvestNoXAudit();
					sub.setInvestNo(investNo);
					sub.setAuditCode(name);
					sub.setValue(str);
					sub.setCreatetime(time);
					sub.setCreateuser(updateuser);
					beans.add(sub);
				}
			} 
			if(beans.size()>0){
				aSer.insertAudit02(beans, "");
			}
		}else if(type.equals("edit")){
			while(pNames.hasMoreElements()){  
				String name=(String)pNames.nextElement();
				if(DataUtil.isMatchFmtNumber(name)){
					String str=DataUtil.nulltoempty(request.getParameter(name));
					if(name.equals("0201")){
						 str=str.replace("/", "");
					}
						OFIInvestNoXAudit sub= new OFIInvestNoXAudit();
						sub.setInvestNo(investNo);
						sub.setAuditCode(name);
						sub.setValue(str);
						sub.setSeq(Integer.valueOf(seq));
						sub.setCreatetime(time);
						sub.setCreateuser(updateuser);
						beans.add(sub);
//					if(!str.isEmpty()){
//					}
				}
			} 
			if(beans.size()>0){
				aSer.insertAudit02(beans, seq);
			}
		}
		if(!other.isEmpty()){
			Map<String,List<String>> map=new HashMap<String,List<String>>();
			String[] ary=other.split("&");
			List<String> sub;
			for (String s:ary) {
				String[] sStr=s.split("=");
				String k=sStr[0];
				if(k.startsWith("audit")){
					k=k.replace("audit", "");
					String v=sStr.length>1?sStr[1]:"";
//					System.out.println(k+"="+v);
					if(!v.isEmpty()){
//						System.out.println("not empty:"+k+"="+v);
						if(map.containsKey(k)){
							sub=map.get(k);
						}else{
							sub=new ArrayList<String>();
						}
						sub.add(URLDecoder.decode(v, "utf-8"));
						map.put(k, sub);
					}
				}
			}
			List<OFIInvestNoXAudit> bs = new ArrayList<OFIInvestNoXAudit>();
			for (Entry<String, List<String>> m:map.entrySet()) {
				String k=m.getKey();
				List<String> v=m.getValue();
				for (int i = 0; i < v.size(); i++) {
					OFIInvestNoXAudit sMap= new OFIInvestNoXAudit();
					sMap.setInvestNo(investNo);
					sMap.setAuditCode(k);
					sMap.setValue(v.get(i));
					sMap.setSeq(i+1);
					sMap.setCreatetime(time);
					sMap.setCreateuser(updateuser);
					bs.add(sMap);
				}
			}
			if(bs.size()>0){
				aSer.delete(investNo);
				aSer.insert(bs);
			}
		}
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>alert('稽核資料已更新!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvest.jsp?investNo="+investNo+"&tabsNum="+tabsNum+"';</script>");
		out.flush();
		out.close();
		return;
	}
}
