package com.isam.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.isam.bean.InterviewoneFile;
import com.isam.helper.DataUtil;
import com.isam.service.InterviewoneFileService;

public class InterviewonwFileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String temp = request.getParameter("fNo");
		System.out.println(temp);
		if(temp.indexOf(",")<0) {
			int fNo =-1;
			if(temp!=null&&temp.trim().length()!=0){
				fNo = Integer.valueOf(temp);
			}
			InterviewoneFileService ser= new InterviewoneFileService();
			
			ServletOutputStream out = null;
			
			if(fNo>0){
				InterviewoneFile bean = ser.select(fNo);
				try {
					out=response.getOutputStream();
					byte[] fileBytes = new byte[0];
					String dfilename = "";
					dfilename =bean.getfName();
					fileBytes = bean.getfContent();
					if(fileBytes.length > 0){
						response.setContentType("application/x-download");
						response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(dfilename, "utf-8") + "\"");
						response.setContentLength(fileBytes.length);
						out.write(fileBytes);
					}
					out.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(out!=null){
						out.close();
					}
				}
			}
		}
		else {

			System.out.println("download zip");
			ServletOutputStream out = null;
			ByteArrayOutputStream baos = null;
			ZipOutputStream zos = null;
			
			try {
				baos = new ByteArrayOutputStream();
				zos = new ZipOutputStream(baos, Charset.forName("big5")); 
				
			for(String strFno : temp.split(",")) {
				int fNo =-1;
				if(temp!=null&&temp.trim().length()!=0){
					fNo = Integer.valueOf(strFno);
				}
				InterviewoneFileService ser= new InterviewoneFileService();
				
				if(fNo>0){
					InterviewoneFile bean = ser.select(fNo);
					
						
						zos.putNextEntry(new ZipEntry(bean.getfName()));
						zos.write(bean.getfContent());
						zos.closeEntry();
					
					
				}
			}
			zos.flush();
			baos.flush();
			zos.close();
			byte[] zipBytes = baos.toByteArray();
			baos.close();
			
			/* Write attachment to JSP. */
			String dfilename = DataUtil.getStrUDate().replace("-", "")  +  ".zip";
	        out = response.getOutputStream();
	        response.setContentType("application/zip");
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(dfilename, "utf-8") + "\"");
	        out.write(zipBytes);
	        out.flush();
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				if(zos != null){
					try {
						zos.flush();
						zos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(baos != null){
					try {
						baos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(out != null){
					try {
						out.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
