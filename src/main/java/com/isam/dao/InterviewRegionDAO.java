package com.isam.dao;

import java.sql.*;
import java.util.*;

import com.isam.bean.InterviewRegion;
import com.isam.helper.SQL;

public class InterviewRegionDAO {
	public static Hashtable<String, Vector<InterviewRegion>> loadRegionTree(){
		Hashtable<String, Vector<InterviewRegion>> result = new Hashtable<String, Vector<InterviewRegion>>();
		SQL sqltool = new SQL();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String forStmt = "SELECT * FROM InterviewRegion WHERE enable = 1 ORDER BY parent, seq, code";
		
		try {
			stmt = sqltool.prepare(forStmt);
			rs = stmt.executeQuery();
			
			Vector<InterviewRegion> vec = new Vector<InterviewRegion>();
			String parent = null;
			
			while(rs.next()){
				InterviewRegion region= new InterviewRegion();
				region.setCode(rs.getString("code"));
				region.setName(rs.getString("name"));
				region.setLevel(rs.getString("level"));
				region.setParent(rs.getString("parent"));
				region.setSeq(rs.getString("seq"));
				region.setEnable(rs.getString("enable"));
				
				//Create a children list for each parent.
				if(!region.getParent().equalsIgnoreCase(parent)){
					if(vec.size() > 0){
						result.put(parent, vec);
					}
					
					vec = new Vector<InterviewRegion>();
					parent = region.getParent();
				}
				
				vec.add(region);
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
