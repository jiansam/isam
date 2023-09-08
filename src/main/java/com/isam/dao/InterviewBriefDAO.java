package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.isam.bean.InterviewBrief;
import com.isam.helper.SQL;

public class InterviewBriefDAO {
	public static ArrayList<InterviewBrief> select(){
		return select(null);
	}

	public static ArrayList<InterviewBrief> select(Integer identifier){
		ArrayList<InterviewBrief> result = new ArrayList<InterviewBrief>();

		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder forStmt = new StringBuilder();

		try{
			forStmt.append("SELECT * FROM InterviewBrief ");
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
				InterviewBrief interviewbrief = new InterviewBrief();
				interviewbrief.setIdentifier(rs.getInt("identifier"));
				interviewbrief.setYear(rs.getInt("year"));
				interviewbrief.setCompany(rs.getString("company"));
				interviewbrief.setDescription(rs.getString("description"));
				interviewbrief.setUpdateuser(rs.getString("updateuser"));
				interviewbrief.setUpdatetime(rs.getDate("updatetime"));

				result.add(interviewbrief);
			}

			rs.close();
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

		return result;
	}

	public static int insert(InterviewBrief interviewbrief){
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		StringBuilder forStmt = new StringBuilder();

		int identifier = -1;

		try{
			forStmt.append("INSERT INTO InterviewBrief (year, company, description, createuser, createtime, updateuser, updatetime ,type1,type2,type3 ")
						   .append(") VALUES (?, ?, ?, ?, GETDATE(), ?, GETDATE())");

			stmt = sqltool.prepare(forStmt.toString());
			stmt.setInt(1, interviewbrief.getYear());
			stmt.setNString(2, interviewbrief.getCompany());
			stmt.setNString(3, interviewbrief.getDescription());
			stmt.setNString(4, interviewbrief.getUpdateuser());
			stmt.setNString(5, interviewbrief.getUpdateuser());

			stmt.setNString(6, interviewbrief.getType1());
			stmt.setNString(7, interviewbrief.getType2());
			stmt.setNString(8, interviewbrief.getType3());
			
			stmt.executeUpdate();

			ResultSet rs = sqltool.query("SELECT MAX(identifier) from InterviewBrief ");

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

	public static void update(InterviewBrief interviewbrief){
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		StringBuilder forStmt = new StringBuilder();

		try{
			forStmt.append("UPDATE InterviewBrief SET ")
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
			stmt.setInt(1, interviewbrief.getYear());
			stmt.setNString(2, interviewbrief.getCompany());
			stmt.setNString(3, interviewbrief.getDescription());
			stmt.setNString(4, interviewbrief.getUpdateuser());
			
			stmt.setNString(5, interviewbrief.getType1());
			stmt.setNString(6, interviewbrief.getType2());
			stmt.setNString(7, interviewbrief.getType3());
			
			stmt.setInt(8, interviewbrief.getIdentifier());

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

			stmt = sqltool.prepare("DELETE FROM InterviewBrief WHERE identifier = ?");
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
