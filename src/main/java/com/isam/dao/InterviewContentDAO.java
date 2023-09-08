package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.isam.helper.DataUtil;
import com.isam.helper.PairHashtable;
import com.isam.helper.SQL;

public class InterviewContentDAO {
	public static PairHashtable<Integer, String, String> select(String interviewIdentifier[], String outlineCode[]){
		PairHashtable<Integer, String, String> result = new PairHashtable<Integer, String, String>();

		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder forStmt = new StringBuilder();

		try{
			forStmt.append("SELECT * FROM InterviewContent WHERE text IS NOT NULL ");

			//1. filter interview
			if(interviewIdentifier != null && interviewIdentifier.length > 0){
				forStmt.append("AND interviewIdentifier IN (");

				for(int i = 0;i < interviewIdentifier.length;i++){
					forStmt.append(i == 0 ? "" : ", ").append(interviewIdentifier[i]);
				}

				forStmt.append(") ");
			}

			//2. filter outline
			if(outlineCode != null && outlineCode.length > 0){
				forStmt.append("AND outlineCode IN (");

				for(int i = 0;i < outlineCode.length;i++){
					forStmt.append(i == 0 ? "" : ", ").append("'").append(outlineCode[i]).append("'");
				}

				forStmt.append(") ");
			}

			//System.out.println(forStmt.toString());
			stmt = sqltool.prepare(forStmt.toString());
			rs = stmt.executeQuery();

			while(rs.next()){
				result.put(rs.getInt("interviewIdentifier"), rs.getString("outlineCode"), rs.getString("text"));
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

	public static void update(PairHashtable<Integer, String, String> contentTable){
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		StringBuilder forStmt = new StringBuilder();

		try{
			sqltool.noCommit();

			for(int interviewIdentifier : contentTable.firstKeySet()){
				forStmt.append("DELETE FROM InterviewContent WHERE interviewIdentifier = ?");
				stmt = sqltool.prepare(forStmt.toString());
				stmt.setInt(1, interviewIdentifier);

				stmt.executeUpdate();
				stmt.close();

				forStmt.delete(0, forStmt.length());
				forStmt.append("INSERT INTO InterviewContent (interviewIdentifier, outlineCode, text)  VALUES (?, ?, ?)");
				stmt = sqltool.prepare(forStmt.toString());
				for(String outlineCode : contentTable.secondKeySet()){
					if(DataUtil.isEmpty(contentTable.get(interviewIdentifier, outlineCode))){
						continue;
					}

					stmt.setInt(1, interviewIdentifier);
					stmt.setString(2, outlineCode);
					stmt.setString(3, contentTable.get(interviewIdentifier, outlineCode));

					stmt.executeUpdate();
				}
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
