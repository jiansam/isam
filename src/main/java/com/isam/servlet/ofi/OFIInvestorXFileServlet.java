package com.isam.servlet.ofi;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.isam.bean.OFIInvestorXFile;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.ofi.OFIInvestorXFileService;


public class OFIInvestorXFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserMember user = (UserMember) session.getAttribute("userInfo");
		String updateuser = user.getIdMember();
		Map<String,String> map=new HashMap<String, String>();
		OFIInvestorXFileService ser = new OFIInvestorXFileService();
		Timestamp time=DataUtil.getNowTimestamp();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		OFIInvestorXFile bean= new OFIInvestorXFile();
		if (isMultipart) {
			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iter = null;
			FileItemStream item =null;
			InputStream stream=null;
			ByteArrayOutputStream output=null;
			try { 
				iter = upload.getItemIterator(request);
				while (iter.hasNext()) {
					item = iter.next();
					String name = item.getFieldName();
					stream = item.openStream();
					if (item.isFormField()) {
						String value = Streams.asString(stream,"UTF-8");
						 map.put(name, value);
//						 System.out.println(name+"="+value);
					}else{
						String filename = item.getName();
						bean.setfName(filename);
						int len = 0;
						byte[] b = new byte[1024];
						output = new ByteArrayOutputStream();
						while ((len = stream.read(b, 0, b.length)) != -1) {
							output.write(b, 0, len);
						}
						byte[] buffer = output.toByteArray();
						bean.setfContent(buffer);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(stream!=null){
					stream.close();
				}
				if(output!=null){
					output.close();
				}
			}
		}
		OFIInvestorXFile b= new OFIInvestorXFile();
		String type=map.containsKey("type")?map.get("type"):DataUtil.nulltoempty(request.getParameter("type"));
		String title=map.get("title");
		String investorSeq=map.containsKey("investorSeq")?map.get("investorSeq"):"";
		String note=map.containsKey("note")?map.get("note"):"";
		int fNo = 0;
		String msg="";
		if(type.equals("add")){
			bean.setInvestorSeq(investorSeq);
			bean.setTitle(title);
			bean.setNote(note);
			bean.setUpdatetime(time);
			bean.setUpdateuser(updateuser);
			bean.setCreatetime(time);
			bean.setCreateuser(updateuser);
			bean.setEnable("1");
			ser.insert(bean);
			msg="資料已經新增";
		}else if(type.equals("edit")){
			fNo=Integer.valueOf(map.get("fNo"));
			b=ser.select(fNo);
			bean.setfNo(fNo);
			bean.setInvestorSeq(investorSeq);
			bean.setTitle(title);
			if(bean.getfContent().length==0){
				bean.setfContent(b.getfContent());
				bean.setfName(b.getfName());
			}
			bean.setNote(note);
			bean.setUpdatetime(time);
			bean.setUpdateuser(updateuser);
			bean.setCreatetime(b.getCreatetime());
			bean.setCreateuser(b.getCreateuser());
			bean.setEnable(b.getEnable());
			ser.update(bean);
			msg="資料已經修改";
		}else if(type.equals("del")){
			fNo=Integer.valueOf(DataUtil.nulltoempty(request.getParameter("serno")));
			ser.unable(fNo,updateuser);
			investorSeq=DataUtil.nulltoempty(request.getParameter("investorSeq"));
			msg="資料已經刪除";
		}
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>alert('"+msg+"!'); window.location.href='http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/console/showinvestform.jsp?investorSeq="+investorSeq+"';</script>");
		out.flush();
		out.close();
		return;
	}
	
}
