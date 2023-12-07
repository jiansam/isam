package com.isam.dao;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.isam.bean.InterviewFile;
import com.isam.helper.SQL;

public class InterviewCierFileDAO {
	public static Hashtable<String, String> upload(HttpServletRequest request){
		Hashtable<String, String> parameters = new Hashtable<String, String>();

		if(ServletFileUpload.isMultipartContent(request)){
			SQL sqltool = new SQL();
			PreparedStatement stmt = null;

			try{
				ServletFileUpload sfu = new ServletFileUpload();
				FileItemIterator iter = sfu.getItemIterator(request);
				sqltool.noCommit();

				stmt = sqltool.prepare("INSERT INTO InterviewCierFile (Filename, Content, interviewIdentifier, Purpose, UploadDate"
						  + ") VALUES (?, ?, ?, ?, GETDATE())");

				while(iter.hasNext()){
					FileItemStream fis = iter.next();
					InputStream is = fis.openStream();

					// Note: normal form fields MUST be placed before file input.
					if(fis.isFormField()){
						parameters.put(fis.getFieldName(), Streams.asString(is, "utf-8"));
					}else{
						stmt.setNString(1, fis.getName());
						stmt.setBinaryStream(2, is);
						stmt.setInt(3, Integer.parseInt(parameters.get("identifier")));
						stmt.setString(4, parameters.get("Purpose"));

						stmt.executeUpdate();
					}

					is.close();
				}

				stmt.close();

				stmt = sqltool.prepare("UPDATE InterviewCierFile SET FileSize = DATALENGTH(Content) WHERE interviewIdentifier = ? ");
				stmt.setInt(1, Integer.parseInt(parameters.get("identifier")));
				stmt.executeUpdate();

				sqltool.commit();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try{
					stmt.close();
					sqltool.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return parameters;
	}


	public static ArrayList<InterviewFile> select(int interviewIdentifier, String purpose){
		SQL sqltool = null;
		PreparedStatement stmt = null;

		ArrayList<InterviewFile> result = new ArrayList<InterviewFile>();

		try{
			sqltool = new SQL();
			stmt = sqltool.prepare("SELECT * FROM InterviewCierFile WHERE interviewIdentifier = ? AND Purpose = ? ORDER BY UploadDate ASC");
			stmt.setInt(1, interviewIdentifier);
			stmt.setString(2, purpose);
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				InterviewFile interviewfile = new InterviewFile();
				interviewfile.setIdentifier(rs.getInt("Identifier"));
				interviewfile.setFilename(rs.getString("Filename"));
				interviewfile.setFileSize(rs.getInt("FileSize"));
				interviewfile.setUploadDate(rs.getDate("UploadDate"));
				interviewfile.setInterviewIdentifier(rs.getInt("interviewIdentifier"));

				result.add(interviewfile);
			}

			rs.close();
			stmt.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(sqltool != null){
					sqltool.close();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}

		return result;
	}

	public static void delete(int identifier){
		SQL sqltool = null;
		PreparedStatement stmt = null;


		try{
			sqltool = new SQL();
			stmt = sqltool.prepare("DELETE FROM InterviewCierFile WHERE identifier = ?");
			stmt.setInt(1, identifier);

			stmt.executeUpdate();
			stmt.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(sqltool != null){
					sqltool.close();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
}
