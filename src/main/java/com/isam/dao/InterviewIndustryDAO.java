package com.isam.dao;

import java.sql.*;
import java.util.*;

import com.isam.bean.*;
import com.isam.helper.SQL;

public class InterviewIndustryDAO {
	public static Hashtable<String, Vector<Industry>> loadIndustryTree(){
		Hashtable<String, Vector<Industry>> result = new Hashtable<String, Vector<Industry>>();
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String forStmt = "SELECT * FROM Industry ORDER BY parent, seq, code";
		
		try {
			stmt = sqltool.prepare(forStmt);
			rs = stmt.executeQuery();
			
			Vector<Industry> vec = new Vector<Industry>();
			String parent = null;
			
			while(rs.next()){
				Industry industry= new Industry();
				industry.setCode(rs.getString("code"));
				industry.setName(rs.getString("name"));
				industry.setLevel(rs.getString("level"));
				industry.setParent(rs.getString("parent"));
				industry.setSeq(rs.getString("seq"));
				
				//Create a children list for each parent.
				if(!industry.getParent().equalsIgnoreCase(parent)){
					if(vec.size() > 0){
						result.put(parent, vec);
					}
					
					vec = new Vector<Industry>();
					parent = industry.getParent();
				}
				
				vec.add(industry);
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
}
