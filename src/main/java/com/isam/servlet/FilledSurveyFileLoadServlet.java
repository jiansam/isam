package com.isam.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.dasin.tools.dPairHashMap;

import com.isam.bean.SurveyFile;
import com.isam.dao.FilledSurveyFileDAO;
import com.isam.helper.SQL;

import Lara.Utility.DateUtil;
import Lara.Utility.ToolsUtil;

public class FilledSurveyFileLoadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	FilledSurveyFileDAO dao;
	SurveyFile bean;
	HttpSession session;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = "";
		String doThing = "";
		SQL sqltools = new SQL();
		ServletOutputStream out = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String sql = "";
		String filename = "";
		byte[] fileBytes = new byte[0];
		
		try {
			id = ( request.getParameter("id") == null ) ? "" : request.getParameter("id");
			
			if(id.length() > 0){

				stmt = sqltools.prepare("SELECT file_title, file_content FROM [ISAM].[dbo].[FilledSurveyFile] WHERE id = ?");
				stmt.setInt(1, Integer.valueOf(id));
				rs = stmt.executeQuery();
				
				if(rs.next()){
					filename = rs.getString("file_title");
					fileBytes = rs.getBytes("file_content");
				}
				
				if(fileBytes.length > 0){
					if(request.getProtocol().equalsIgnoreCase("HTTP/1.0")){
						response.setHeader("Pragma", "no-cache");
					}else if(request.getProtocol().equalsIgnoreCase("HTTP/1.1")){
						response.setHeader("Cache-Control", "no-cache");
					}
					
					try {
						out = response.getOutputStream();					
						response.setContentType("application/x-download");
						response.setHeader("Content-Disposition", "attachment; filename=\"" + ToolsUtil.encode(filename, "utf-8") + "\"" );
						response.setContentLength(fileBytes.length);
						out.write(fileBytes);
						out.flush();
					}catch(ClientAbortException cae){
					}finally {
						try {
							out.close();
						}catch(ClientAbortException cae){
						}
					}
				}
				
			}
		} catch(NumberFormatException ne) {
			System.out.println("fileId="+id);
		} catch (Exception e) {
			System.out.println("fileId="+id);
			e.printStackTrace();
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			try {
				sqltools.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			fileBytes = null;
		}
	}

}
