package com.isam.dao;

import java.sql.*;
import java.util.*;

import com.isam.bean.*;
import com.isam.helper.SQL;

public class ApprovalOptionDAO {
	
	public static List<ApprovalOption> select(String type,String column){
		List<ApprovalOption> result = new ArrayList<ApprovalOption>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM ApprovalOption WHERE Enable = 1 and type=isnull(?,type) and [column]=isnull(?,[column]) ORDER BY Seq";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, type);
			stmt.setString(2, column);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				ApprovalOption bean= new ApprovalOption();
				bean.setType(rs.getString(1));
				bean.setCode(rs.getString(2));
				bean.setTitle(rs.getString(3));
				bean.setColumn(rs.getString(4));
				bean.setRestrainType(rs.getString(5));
				bean.setSeq(rs.getInt(6));
				bean.setEnable(rs.getInt(7));
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
	public static Map<String,String> getOptionMapByType(String type){
		List<ApprovalOption> list = select(null,null);
		Map<String,String> map = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			ApprovalOption bean=list.get(i);
			if(bean.getType().equalsIgnoreCase(type)){
				map.put(bean.getCode(), bean.getTitle());
			}
		}
		return map;
	}
}
