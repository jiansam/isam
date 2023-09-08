package com.isam.servlet.ofi;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.isam.helper.SQL;

public class OFIInvestZIPServlet_bak extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos, Charset.forName("big5")); 
		SQL sqltool = new SQL();
		byte bytes[] = new byte[16*1024];
		
		/* 加入架構圖 */
		String[] investorFile = request.getParameterValues("investorFile") == null ? new String[0]
			: request.getParameterValues("investorFile");

		try {
			PreparedStatement statement = sqltool.prepare("SELECT fName, fContent FROM OFI_InvestorXFile WHERE investorSeq = ? ");
			
			for(String investor : investorFile) {
				String directory = "";
				ResultSet rs = sqltool.query("SELECT INVESTOR_CHTNAME FROM OFI_InvestCase WHERE INVESTOR_SEQ = "
						+ Integer.parseInt(investor));
				
				if(rs.next()) {
					directory = "架構圖/" + rs.getString("INVESTOR_CHTNAME") + "/";
				}
				rs.close();
				
				statement.setInt(1, Integer.parseInt(investor));
				rs = statement.executeQuery();
				while(rs.next()) {
					int bytesRead;
					zos.putNextEntry(new ZipEntry(directory + rs.getString("fName")));
					
					//rs取出binaryStream
					InputStream is = rs.getBinaryStream("fContent");
					while ((bytesRead = is.read(bytes)) != -1) {
		                zos.write(bytes, 0, bytesRead);
		            }
					
					zos.closeEntry();
		            //is.close();
				}
				
				rs.close();
			}
			
			statement.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		zos.flush();
        baos.flush();
        zos.close();
        baos.close();
        
        try {
			sqltool.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        /* Write attachment to JSP. */
        ServletOutputStream sos = response.getOutputStream();
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"example.zip\"");
        
        sos.write(baos.toByteArray());
        sos.flush();
	}
}
