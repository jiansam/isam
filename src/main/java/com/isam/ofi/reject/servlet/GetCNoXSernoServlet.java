package com.isam.ofi.reject.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.ofi.reject.bean.OFIReject;
import com.isam.ofi.reject.bean.OFIRejectCompany;
import com.isam.ofi.reject.service.OFIRejectCompanyService;
import com.isam.ofi.reject.service.OFIRejectService;

public class GetCNoXSernoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserMember bean = (UserMember) session.getAttribute("userInfo");
		String updateuser = bean.getIdMember();
		Timestamp t=DataUtil.getNowTimestamp();
		
		String cname = DataUtil.nulltoempty(request.getParameter("investName"));
		String cNo = DataUtil.nulltoempty(request.getParameter("cNo"));
		String serno = DataUtil.nulltoempty(request.getParameter("serno"));
		
		if(cNo.isEmpty()&&cname.isEmpty()){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('國內事業名稱不可為空白，請重新選取！');window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/reject/showlist.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		if(cNo.isEmpty()){
			OFIRejectCompany cb=new OFIRejectCompany();
			cb.setCname(cname);
			cb.setIdno("");
			cb.setIsNew("");
			cb.setSetupdate("");
			cb.setOrgType("");
			cb.setEnable("0");
			cb.setUpdatetime(t);
			cb.setUpdateuser(updateuser);
			cb.setCreatetime(t);
			cb.setCreateuser(updateuser);
			OFIRejectCompanyService cSer=new OFIRejectCompanyService();
			cNo=cSer.insert(cb);
		}
		if(serno.isEmpty()&&!cNo.isEmpty()){
			OFIReject b=new OFIReject();
			b.setcNo(cNo);
			b.setReceiveDate("");
			b.setReceiveNo("");
			b.setRejectType("");
			b.setDecision("");
			b.setEnable("0");
			b.setUpdatetime(t);
			b.setUpdateuser(updateuser);
			b.setCreatetime(t);
			b.setCreateuser(updateuser);
			OFIRejectService rSer=new OFIRejectService();
			serno=rSer.insert(b);
		}
		if(cNo.isEmpty()&&serno.isEmpty()){
			request.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<script language='javascript'>alert('系統出現無法預期的問題，請通知系統工程師！');window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/reject/showlist.jsp';</script>");
			out.flush();
			out.close();
			return;
		}
		JSONObject obj=new JSONObject();
		obj.put("cNo", cNo);
		obj.put("serno", serno);
		
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(obj.toJSONString());
		out.close();

	}

}
