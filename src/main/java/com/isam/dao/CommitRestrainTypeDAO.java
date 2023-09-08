package com.isam.dao;

import java.sql.*;
import java.util.*;

import com.isam.bean.*;
import com.isam.helper.SQL;

public class CommitRestrainTypeDAO {
	
	public static List<CommitRestrainType> select(){
		List<CommitRestrainType> result = new ArrayList<CommitRestrainType>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommitRestrainType WHERE Enable = '1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				CommitRestrainType bean= new CommitRestrainType();
				bean.setType(rs.getString(1));
				bean.setTitle(rs.getString(2));
				bean.setLevel(rs.getInt(3));
				bean.setSeq(rs.getInt(4));
				bean.setEnable(rs.getInt(5));
				result.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	public static Map<String,String> getTypeMap(){
		List<CommitRestrainType> list = select();
		Map<String,String> map = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			CommitRestrainType bean=list.get(i);
			map.put(bean.getType(), bean.getTitle());
		}
		return map;
	}
}
