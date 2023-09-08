package com.isam.servlet;

import java.io.*;
import java.net.URLEncoder;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.isam.dao.InterviewFileDAO;
import com.isam.helper.*;

public class DownloadInterviewFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ACTION_DELETE = "delete";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		SQL sqltool = null;
		ServletOutputStream out = null;
		PreparedStatement stmt = null;

		try{
			int identifier = -1;

			try{
				identifier = Integer.parseInt(ThreeDes.getDecryptString(request.getParameter("identifier")));
			}catch(Exception e){
				System.out.println("Error identifier: " + identifier);
				e.printStackTrace();
			}

			if(identifier < 0){
				return;
			}

			if(ACTION_DELETE.equalsIgnoreCase(request.getParameter("action"))){
				InterviewFileDAO.delete(identifier);
			}

			if(request.getProtocol().equalsIgnoreCase("HTTP/1.0")){
				response.setHeader("Pragma", "no-cache");
			}else if(request.getProtocol().equalsIgnoreCase("HTTP/1.1")){
				response.setHeader("Cache-Control", "no-cache");
			}

			out = response.getOutputStream();
			sqltool = new SQL();
			stmt = sqltool.prepare("SELECT * FROM InterviewFile WHERE Identifier = ?");
			stmt.setInt(1, identifier);
			ResultSet rs = stmt.executeQuery();

			if(rs.next()){
				String filename = rs.getString("Filename");
				int filesize = rs.getInt("FileSize");
				response.setContentType("application/x-download");
				response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(filename, "utf-8") + "\"");
				response.setContentLength(filesize);

				int length = 128*1024;
				byte[] buffer = new byte[length];
				InputStream is = rs.getBinaryStream("Content");
				while((length = is.read(buffer)) != -1){
					out.write(buffer, 0, length);
				}

				is.close();
			}

			rs.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(sqltool != null){
					sqltool.close();
				}
				if(out != null){
					out.close();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
}
