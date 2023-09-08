package com.isam.dao;

import java.sql.*;
import java.util.*;

import com.isam.bean.*;
import com.isam.helper.SQL;

public class InterviewOutlineDAO {
	public static Hashtable<String, Vector<InterviewOutline>> loadOutlineTree(){
		Hashtable<String, Vector<InterviewOutline>> result = new Hashtable<String, Vector<InterviewOutline>>();
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String forStmt = "SELECT * FROM InterviewOutline WHERE notShow <> '1' ORDER BY parent, seq, code";

		try {
			stmt = sqltool.prepare(forStmt);
			rs = stmt.executeQuery();

			Vector<InterviewOutline> vec = new Vector<InterviewOutline>();
			String parent = null;

			while(rs.next()){
				InterviewOutline outline= new InterviewOutline();
				outline.setCode(rs.getString("code"));
				outline.setName(rs.getString("name"));
				outline.setLevel(rs.getString("level"));
				outline.setParent(rs.getString("parent"));
				outline.setSeq(rs.getString("seq"));
				outline.setNotShow(rs.getBoolean("notShow"));

				//Create a children list for each parent.
				if(!outline.getParent().equalsIgnoreCase(parent)){
					if(vec.size() > 0){
						result.put(parent, vec);
					}

					vec = new Vector<InterviewOutline>();
					parent = outline.getParent();
				}

				vec.add(outline);
			}
			rs.close();

			//Add last list.
			if(vec.size() > 0){
				result.put(parent, vec);
			}
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

	public static Hashtable<String, InterviewOutline> loadOutlineTable(){
		Hashtable<String, InterviewOutline> result = new Hashtable<String, InterviewOutline>();
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String forStmt = "SELECT * FROM InterviewOutline";

		try {
			stmt = sqltool.prepare(forStmt);
			rs = stmt.executeQuery();

			while(rs.next()){
				InterviewOutline outline= new InterviewOutline();
				outline.setCode(rs.getString("code"));
				outline.setName(rs.getString("name"));
				outline.setLevel(rs.getString("level"));
				outline.setParent(rs.getString("parent"));
				outline.setSeq(rs.getString("seq"));
				outline.setNotShow(rs.getBoolean("notShow"));

				result.put(outline.getCode(), outline);
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
