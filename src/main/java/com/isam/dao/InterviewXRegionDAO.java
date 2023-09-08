package com.isam.dao;

import java.sql.*;
import java.util.*;

import com.isam.helper.SQL;

public class InterviewXRegionDAO {
	public static HashMap<Integer, ArrayList<String>> createTable(){
		HashMap<Integer, ArrayList<String>> result = new HashMap<Integer, ArrayList<String>>();
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String forStmt = "SELECT * FROM InterviewXRegion";

		try {
			stmt = sqltool.prepare(forStmt);
			rs = stmt.executeQuery();

			while(rs.next()){
				int InterviewIdentifer = rs.getInt("InterviewIdentifier");
				String RegionCode = rs.getString("RegionCode");

				ArrayList<String> list;
				if((list = result.get(InterviewIdentifer)) == null){
					list = new ArrayList<String>();
					result.put(InterviewIdentifer, list);
				}

				list.add(RegionCode);
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

	public static void update(int InterviewIdentifier, String[] RegionCode){
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;

		try{
			sqltool.noCommit();

			stmt = sqltool.prepare("DELETE FROM InterviewXRegion WHERE InterviewIdentifier = ?");
			stmt.setInt(1, InterviewIdentifier);
			stmt.executeUpdate();
			stmt.close();

			stmt = sqltool.prepare("INSERT INTO InterviewXRegion (InterviewIdentifier, RegionCode) VALUES (?, ?)");
			stmt.setInt(1, InterviewIdentifier);
			for(String code : RegionCode){
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
