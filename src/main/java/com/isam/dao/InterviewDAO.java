package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import com.isam.bean.Interview;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class InterviewDAO {
	public static Vector<Interview> select(String year[], String investmentType[], String industry[], String region[]){
		Vector<Interview> result = new Vector<Interview>();

		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder forStmt = new StringBuilder();

		try{
			forStmt.append("SELECT *, year(interviewDate) as year FROM Interview WHERE enable = 1 ");

			//1. filter year
			if(year != null && year.length > 0){
				forStmt.append("AND year(interviewDate) IN (");

				for(int i = 0;i < year.length;i++){
					forStmt.append(i == 0 ? "" : ", ").append(year[i]);
				}

				forStmt.append(") ");
			}

			//2. filter investmentType
			if(investmentType != null && investmentType.length > 0){
				forStmt.append("AND identifier IN (")
					.append("SELECT InterviewIdentifier FROM InterviewXInvestment WHERE InvestmentType IN(");

				for(int i = 0;i < investmentType.length;i++){
					forStmt.append(i == 0 ? "" : ", ").append("'").append(investmentType[i]).append("'");
				}

				forStmt.append(")) ");
			}

			//3. filter industry
			if(industry != null && industry.length > 0){
				forStmt.append("AND identifier IN (")
					.append("SELECT InterviewIdentifier FROM InterviewXIndustry WHERE IndustryCode IN(");

				for(int i = 0;i < industry.length;i++){
					forStmt.append(i == 0 ? "" : ", ").append("'").append(industry[i]).append("'");
				}

				forStmt.append(")) ");
			}

			//4.filter region
			if(region != null && region.length > 0){
				forStmt.append("AND identifier IN (")
					.append("SELECT InterviewIdentifier FROM InterviewXRegion WHERE RegionCode IN(");

				for(int i = 0;i < region.length;i++){
					forStmt.append(i == 0 ? "" : ", ").append("'").append(region[i]).append("'");
				}

				forStmt.append(")) ");
			}

			//System.out.println(forStmt.toString());
			stmt = sqltool.prepare(forStmt.toString());
			rs = stmt.executeQuery();

			while(rs.next()){
				Interview interview = new Interview();
				interview.setIdentifier(rs.getInt("identifier"));
				interview.setYear(rs.getInt("year"));
				interview.setCompany(rs.getString("company"));
				interview.setUpdatetime(DataUtil.trim(rs.getString("updatetime")));

				result.add(interview);
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

	public static Vector<Interview> select(String identifier[]){
		Vector<Interview> result = new Vector<Interview>();

		if(identifier == null || identifier.length == 0){
			return result;
		}

		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder forStmt = new StringBuilder();

		try{
			forStmt.append("SELECT * FROM Interview WHERE enable = 1 ");

			//1. filter identifier
			if(identifier != null && identifier.length > 0){
				forStmt.append("AND identifier IN (");

				for(int i = 0;i < identifier.length;i++){
					forStmt.append(i == 0 ? "" : ", ").append(identifier[i]);
				}

				forStmt.append(") ");
			}else{
				sqltool.close();
				return result;
			}

			//System.out.println(forStmt.toString());
			stmt = sqltool.prepare(forStmt.toString());
			rs = stmt.executeQuery();

			while(rs.next()){
				Interview interview = new Interview();
				interview.setIdentifier(rs.getInt("identifier"));
				interview.setCompany(rs.getString("company"));
				interview.setCompanyEnglish(rs.getString("companyEnglish"));
				interview.setParentCompany(rs.getString("parentCompany"));
				interview.setInterviewDate(rs.getDate("interviewDate"));
				interview.setInterviewee(rs.getString("interviewee"));
				interview.setInterviewer(rs.getString("interviewer"));
				interview.setInterviewPlace(rs.getString("interviewPlace"));
				interview.setNote(rs.getString("note"));
				interview.setPublicity(rs.getBoolean("publicity"));

				result.add(interview);
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

	public static int insert(Interview interview){
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		StringBuilder forStmt = new StringBuilder();

		int identifier = -1;

		try{
			forStmt.append("INSERT INTO Interview (interviewDate, company, companyEnglish, parentCompany, ")
						   .append("interviewee, interviewer, publicity, interviewPlace, note, enable, createtime, ")
						   .append("updatetime ")
						   .append(") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 1, GETDATE(), GETDATE())");

			stmt = sqltool.prepare(forStmt.toString());
			stmt.setDate(1, new java.sql.Date(interview.getInterviewDate().getTime()));
			stmt.setNString(2, interview.getCompany());
			stmt.setNString(3, interview.getCompanyEnglish());
			stmt.setNString(4, interview.getParentCompany());
			stmt.setNString(5, interview.getInterviewee());
			stmt.setNString(6, interview.getInterviewer());
			stmt.setBoolean(7, interview.isPublicity());
			stmt.setNString(8, interview.getInterviewPlace());
			stmt.setNString(9, interview.getNote());

			stmt.executeUpdate();

			ResultSet rs = sqltool.query("SELECT MAX(identifier) from Interview");

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

	public static void update(Interview interview){
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		StringBuilder forStmt = new StringBuilder();

		try{
			forStmt.append("UPDATE Interview SET ")
						   .append("interviewDate = ?, ")
						   .append("company = ?, ")
						   .append("companyEnglish = ?, ")
						   .append("parentCompany = ?, ")
						   .append("interviewee = ?, ")
						   .append("interviewer = ?, ")
						   .append("publicity = ?, ")
						   .append("interviewPlace = ?, ")
						   .append("note = ?, ")
						   .append("updatetime = GETDATE() ")
						   .append("WHERE identifier = ? ");

			stmt = sqltool.prepare(forStmt.toString());
			stmt.setDate(1, new java.sql.Date(interview.getInterviewDate().getTime()));
			stmt.setNString(2, interview.getCompany());
			stmt.setNString(3, interview.getCompanyEnglish());
			stmt.setNString(4, interview.getParentCompany());
			stmt.setNString(5, interview.getInterviewee());
			stmt.setNString(6, interview.getInterviewer());
			stmt.setBoolean(7, interview.isPublicity());
			stmt.setNString(8, interview.getInterviewPlace());
			stmt.setNString(9, interview.getNote());
			stmt.setInt(10, interview.getIdentifier());

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

			stmt = sqltool.prepare("DELETE FROM Interview WHERE identifier = ?");
			stmt.setInt(1, identifier);
			stmt.executeUpdate();
			stmt.close();

			stmt = sqltool.prepare("DELETE FROM InterviewContent WHERE interviewIdentifier = ?");
			stmt.setInt(1, identifier);
			stmt.executeUpdate();
			stmt.close();

			stmt = sqltool.prepare("DELETE FROM InterviewFile WHERE interviewIdentifier = ? AND Purpose = 'cier'");
			stmt.setInt(1, identifier);
			stmt.executeUpdate();
			stmt.close();

			stmt = sqltool.prepare("DELETE FROM InterviewXRegion WHERE InterviewIdentifier = ?");
			stmt.setInt(1, identifier);
			stmt.executeUpdate();
			stmt.close();

			stmt = sqltool.prepare("DELETE FROM InterviewXIndustry WHERE InterviewIdentifier = ?");
			stmt.setInt(1, identifier);
			stmt.executeUpdate();
			stmt.close();

			stmt = sqltool.prepare("DELETE FROM InterviewXInvestment WHERE InterviewIdentifier = ?");
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
