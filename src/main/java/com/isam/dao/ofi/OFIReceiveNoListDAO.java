package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.helper.SQL;

public class OFIReceiveNoListDAO {
	
	public List<Map<String,String>> getIsFilledList(String pname,String receiveNo){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM OFI_IsFilledList() where receiveNo = isNull(?,receiveNo) ");
		sb.append("and (INVE_ROLE_CODE ='3' or INVE_ROLE_CODE = '4' or INVE_ROLE_CODE is null) "); //106-08-29新增條件，讓投資人部分只出陸資，不會出現本國或外資未確認
		if(!pname.isEmpty()){
			sb.append("and pname like '").append(pname).append("' ");
		}
		String forStmt = sb.toString();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, receiveNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				Map<String,String> sub = new HashMap<String, String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.put(meta.getColumnName(i),rs.getString(i));
				}
				result.add(sub);
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
	public List<Map<String,String>> select(String type,String serno,String investNo){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_ReceiveNoList where type=isNull(?,type) and serno=isNull(?,serno) and investNo=isNull(?,investNo)";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, type);
			stmt.setString(2, serno);
			stmt.setString(3, investNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				Map<String,String> sub = new HashMap<String, String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.put(meta.getColumnName(i),rs.getString(i));
				}
				result.add(sub);
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
}
