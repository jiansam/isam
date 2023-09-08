package com.isam.dao;

import java.sql.*;
import java.util.*;
import com.isam.helper.*;

public class InterviewXInvestmentDAO {
	public static HashMap<Integer, ArrayList<String>> createTable(){
		return createTable(null);
	}
	public static HashMap<Integer, ArrayList<String>> createTable(Integer InterviewIdentifier){
		HashMap<Integer, ArrayList<String>> result = new HashMap<Integer, ArrayList<String>>();
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sb = new StringBuilder("SELECT * FROM InterviewXInvestment");

		try {
			if(InterviewIdentifier != null){
				sb.append(" WHERE InterviewIdentifier = ?");
			}

			stmt = sqltool.prepare(sb.toString());
			if(InterviewIdentifier != null){
				stmt.setInt(1, InterviewIdentifier);
			}

			rs = stmt.executeQuery();

			while(rs.next()){
				int InterviewIdentifer = rs.getInt("InterviewIdentifier");

				if(result.get(InterviewIdentifer) == null){
					result.put(InterviewIdentifer, new ArrayList<String>());
				}

				result.get(InterviewIdentifer).add(rs.getString("InvestmentType"));
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

	public static void update(int InterviewIdentifier, String[] InvestmentType){
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;

		try{
			sqltool.noCommit();

			stmt = sqltool.prepare("DELETE FROM InterviewXInvestment WHERE InterviewIdentifier = ?");
			stmt.setInt(1, InterviewIdentifier);
			stmt.executeUpdate();
			stmt.close();

			stmt = sqltool.prepare("INSERT INTO InterviewXInvestment (InterviewIdentifier, InvestmentType) VALUES (?, ?)");
			stmt.setInt(1, InterviewIdentifier);
			for(String value : InvestmentType){
				stmt.setString(2, value);
				stmt.executeUpdate();
			}

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
