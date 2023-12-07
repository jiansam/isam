package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.isam.bean.*;
import com.isam.helper.SQL;

public class InterviewCierDAO {
	public static ArrayList<InterviewCier> select() {
		return select(null);
	}

	public static ArrayList<InterviewFile> select(int interviewIdentifier, String purpose){
		SQL sqltool = null;
		PreparedStatement stmt = null;

		ArrayList<InterviewFile> result = new ArrayList<InterviewFile>();

		try{
			sqltool = new SQL();
			stmt = sqltool.prepare("SELECT * FROM InterviewFile WHERE interviewIdentifier = ? AND Purpose = ? ORDER BY UploadDate ASC");
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
	
	public static ArrayList<InterviewCier> select(Integer identifier) {
		ArrayList<InterviewCier> result = new ArrayList<InterviewCier>();

		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder forStmt = new StringBuilder();

		try {
			forStmt.append("SELECT * FROM InterviewCier ");
			if(identifier != null){
				forStmt.append("WHERE identifier = ? ");
			}else {
				forStmt.append("ORDER BY year DESC ");
			}
			stmt = sqltool.prepare(forStmt.toString());

			if(identifier != null){
				stmt.setInt(1, identifier);
			}

			rs = stmt.executeQuery();

			while(rs.next()){
				InterviewCier interviewCier = new InterviewCier();
				interviewCier.setIdentifier(rs.getInt("identifier"));
				interviewCier.setYear(rs.getInt("year"));
				interviewCier.setCompany(rs.getString("company"));
				interviewCier.setDescription(rs.getString("description"));
				interviewCier.setUpdateuser(rs.getString("updateuser"));
				interviewCier.setUpdatetime(rs.getDate("updatetime"));
				interviewCier.setType1(rs.getString("type1"));
				interviewCier.setType2(rs.getString("type2"));
				interviewCier.setType3(rs.getString("type3"));
				
				result.add(interviewCier);
			}

			rs.close();
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			try{
				stmt.close();
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public static int insert(InterviewCier interviewCier){
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		StringBuilder forStmt = new StringBuilder();

		int identifier = -1;

		try{

			forStmt.append("INSERT INTO InterviewCier (year, company, description, createuser, createtime, updateuser, updatetime ,type1,type2,type3 ")
			   .append(") VALUES (?, ?, ?, ?, GETDATE(), ?, GETDATE(),?,?,?)");


			stmt = sqltool.prepare(forStmt.toString());
			stmt.setInt(1, interviewCier.getYear());
			stmt.setNString(2, interviewCier.getCompany());
			stmt.setNString(3, interviewCier.getDescription());
			stmt.setNString(4, interviewCier.getUpdateuser());
			stmt.setNString(5, interviewCier.getUpdateuser());
			stmt.setNString(6, interviewCier.getType1());
			stmt.setNString(7, interviewCier.getType2());
			stmt.setNString(8, interviewCier.getType3());
			
			stmt.executeUpdate();

			ResultSet rs = sqltool.query("SELECT MAX(identifier) from InterviewCier ");

			if(rs.next()){
				identifier = rs.getInt(1);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				stmt.close();
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}

		return identifier;
	}

	public static void update(InterviewCier interviewCier){
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		StringBuilder forStmt = new StringBuilder();

		try{
			forStmt.append("UPDATE InterviewCier SET ")
						   .append("year = ?, ")
						   .append("company = ?, ")
						   .append("description = ?, ")
						   .append("updateuser = ?, ")
						   .append("type1 = ?, ")
						   .append("type2 = ?, ")
						   .append("type3 = ?, ")
						   
						   .append("updatetime = GETDATE() ")
						   .append("WHERE identifier = ? ");

			stmt = sqltool.prepare(forStmt.toString());
			stmt.setInt(1, interviewCier.getYear());
			stmt.setNString(2, interviewCier.getCompany());
			stmt.setNString(3, interviewCier.getDescription());
			stmt.setNString(4, interviewCier.getUpdateuser());
			
			stmt.setNString(5, interviewCier.getType1());
			stmt.setNString(6, interviewCier.getType2());
			stmt.setNString(7, interviewCier.getType3());
			
			
			stmt.setInt(8, interviewCier.getIdentifier());

			stmt.executeUpdate();
		}catch (Exception e) {
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

	public static void delete(int identifier){
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;

		try{
			sqltool.noCommit();

			stmt = sqltool.prepare("DELETE FROM InterviewCier WHERE identifier = ?");
			stmt.setInt(1, identifier);
			stmt.executeUpdate();
			stmt.close();

			stmt = sqltool.prepare("DELETE FROM InterviewFile WHERE interviewIdentifier = ? AND Purpose = 'moea'");
			stmt.setInt(1, identifier);
			stmt.executeUpdate();
			stmt.close();

			sqltool.commit();
		}catch (Exception e) {
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
}
