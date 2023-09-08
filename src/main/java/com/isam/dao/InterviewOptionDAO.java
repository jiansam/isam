package com.isam.dao;

import java.sql.*;
import java.util.*;

import com.isam.bean.*;
import com.isam.helper.SQL;

public class InterviewOptionDAO {
	public static Vector<InterviewOption> select(String SelectName){
		/*  Note:
		 *   The year options come from distinct year value of [Interview] table.
		 * */
		if("year".equalsIgnoreCase(SelectName)){
			return selectInterviewYear();
		}

		Vector<InterviewOption> result = new Vector<InterviewOption>();

		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String forStmt = "SELECT * FROM InterviewOption WHERE Enable = 1 AND SelectName = ? ORDER BY Seq, Identifier";

		try {
			stmt = sqltool.prepare(forStmt);
			stmt.setString(1, SelectName);
			rs = stmt.executeQuery();

			while(rs.next()){
				InterviewOption option= new InterviewOption();
//				option.setIdentifier(rs.getInt("Identifier"));
				option.setCDescription(rs.getString("CDescription"));
				option.setOptionValue(rs.getString("OptionValue"));

				result.add(option);
			}

			rs.close();
		} catch (Exception e) {
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

	public static Vector<InterviewOption> selectInterviewYear(){
		Vector<InterviewOption> result = new Vector<InterviewOption>();

		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String forStmt = "SELECT DISTINCT year(interviewDate) AS year FROM Interview WHERE Enable = 1 ORDER BY year DESC";

		try {
			stmt = sqltool.prepare(forStmt);
			rs = stmt.executeQuery();

			while(rs.next()){
				InterviewOption option= new InterviewOption();
				option.setCDescription(String.valueOf(rs.getInt("year") - 1911));
				option.setOptionValue(String.valueOf(rs.getInt("year")));

				result.add(option);
			}

			rs.close();
		} catch (Exception e) {
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
}
