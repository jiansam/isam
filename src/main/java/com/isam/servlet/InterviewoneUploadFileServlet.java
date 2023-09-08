package com.isam.servlet;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.isam.bean.InterviewoneFile;
import com.isam.bean.UserMember;
import com.isam.helper.DataUtil;
import com.isam.service.InterviewoneFileService;
import com.isam.service.InterviewoneService;


public class InterviewoneUploadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserMember user = (UserMember) session.getAttribute("userInfo");
		String updateuser = user.getIdMember();
		String year="";
		Map<String,String> map=new HashMap<String, String>();
		InterviewoneFileService ser = new InterviewoneFileService();
		Timestamp time=DataUtil.getNowTimestamp();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		List<InterviewoneFile> list = new ArrayList<InterviewoneFile>();
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
						 if(name.equals("year")){
							 year=DataUtil.addZeroForNum(value, 3);
						 }
						 map.put(name, value);
//						 System.out.println(name+"="+value);
					}else{
						String filename = item.getName();
						InterviewoneFile bean= new InterviewoneFile();
						bean.setfName(filename);
						int len = 0;
						byte[] b = new byte[1024];
						output = new ByteArrayOutputStream();
						while ((len = stream.read(b, 0, b.length)) != -1) {
							output.write(b, 0, len);
						}
						byte[] buffer = output.toByteArray();
						bean.setfContent(buffer);
						bean.setUpdatetime(time);
						bean.setUpdateuser(updateuser);
						list.add(bean);
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
//			System.out.println("list.size():"+list.size());
			
			/* 107-07-16 
			 * qNo代表某一家公司,某一年度的代號，這代號只代表一個年度，但是上傳視窗中年度是可選的，這個固定的qNo導致選任何年都沒用，
			 * 因為當 設定選取 某一年的列表頁時，每一家公司的qNo都已經一起取出了，所以若列表頁107年，每家公司的qNo都是107年的ID號。
			 * 
			 * 改善方法是一起傳入  investNo、reInvestNo，搭配原有的year，使用以下方法取出qNo
			 * InterviewoneService.getQNoByYear(String year,String investNo,String reInvestNo)
			*/
			String investNo = map.get("investNo");
			String reInvestNo = map.get("reInvestNo");
			InterviewoneService itvOneSer = new InterviewoneService();
			int qNo = itvOneSer.getQNoByYear(year, investNo, reInvestNo);
//			System.out.println("正確的qNo="+qNo);
			
			for (int i = 0; i < list.size(); i++) {
				InterviewoneFile bean= list.get(i);
				bean.setqNo(qNo);
				bean.setYear(year);
				bean.setqType(map.get("qType"));
				ser.insert(bean);
			}
		}
		
	}
	
}
