package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.isam.helper.SQL;

public class COMTBDataDAO {
	public Map<Integer,Map<String,String>> getTWADDRCode(){
		Map<Integer,Map<String,String>> result= new HashMap<Integer, Map<String,String>>();
		SQL sqltool = new SQL("jdbc/sqlMoeaic");
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COUNTY_NO,COUNTY_NAME,TOWN_NO,TOWN_NAME FROM ");
		sb.append("COMTB932 a,COMTB933 b where a.COUNTY_NO=LEFT(b.Town_no,5) ");
		sb.append("and COUNTY_NAME not like '%(舊)' ");
		String tmp=sb.toString();
		sb.append("and COUNTY_NO !='09007' order by POST_NO");
		String forStmt = sb.toString();
		sb.setLength(0);
		Map<String,String> level1=new LinkedHashMap<String, String>();
		Map<String,String> level2=new LinkedHashMap<String, String>();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				level1.put(rs.getString("COUNTY_NO").trim(), rs.getString("COUNTY_NAME").trim());
				level2.put(rs.getString("TOWN_NO").trim(), rs.getString("TOWN_NAME").trim());
			}
			sb.append(tmp).append("and COUNTY_NO ='09007' order by POST_NO");
			forStmt = sb.toString();
			sb.setLength(0);
			stmt = sqltool.prepare(forStmt);
			rs = stmt.executeQuery();
			while(rs.next()){
				level1.put(rs.getString("COUNTY_NO").trim(), rs.getString("COUNTY_NAME").trim());
				level2.put(rs.getString("TOWN_NO").trim(), rs.getString("TOWN_NAME").trim());
			}
			result.put(1, level1);
			result.put(2, level2);
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
}
