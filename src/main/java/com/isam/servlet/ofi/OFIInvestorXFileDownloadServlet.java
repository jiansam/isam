package com.isam.servlet.ofi;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.isam.bean.OFIInvestorXFile;
import com.isam.service.ofi.OFIInvestorXFileService;

public class OFIInvestorXFileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String temp = request.getParameter("serno");
		int fNo =-1;
		if(temp!=null&&temp.trim().length()!=0){
			fNo = Integer.valueOf(temp);
		}
//		System.out.println(fNo);
		OFIInvestorXFileService ser= new OFIInvestorXFileService();
		
		ServletOutputStream out = null;
		
		if(fNo>0){
			OFIInvestorXFile bean = ser.select(fNo);
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
}

