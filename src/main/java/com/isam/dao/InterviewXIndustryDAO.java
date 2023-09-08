package com.isam.dao;

import java.sql.*;
import java.util.*;

import com.isam.helper.SQL;

public class InterviewXIndustryDAO {
	public static HashMap<Integer, ArrayList<String>> createTable(){
		HashMap<Integer, ArrayList<String>> result = new HashMap<Integer, ArrayList<String>>();
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String forStmt = "SELECT * FROM InterviewXIndustry";

		try {
			stmt = sqltool.prepare(forStmt);
			rs = stmt.executeQuery();

			while(rs.next()){
				int InterviewIdentifer = rs.getInt("InterviewIdentifier");
				String IndustryCode = rs.getString("IndustryCode");

				ArrayList<String> list;
				if((list = result.get(InterviewIdentifer)) == null){
					list = new ArrayList<String>();
					result.put(InterviewIdentifer, list);
				}

				list.add(IndustryCode);
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

	public static void update(int InterviewIdentifier, String[] IndustryCode){
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;

		try{
			sqltool.noCommit();

			stmt = sqltool.prepare("DELETE FROM InterviewXIndustry WHERE InterviewIdentifier = ?");
			stmt.setInt(1, InterviewIdentifier);
			stmt.executeUpdate();
			stmt.close();

			stmt = sqltool.prepare("INSERT INTO InterviewXIndustry (InterviewIdentifier, IndustryCode) VALUES (?, ?)");
			stmt.setInt(1, InterviewIdentifier);
			for(String code : IndustryCode){
				stmt.setString(2, code);
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
