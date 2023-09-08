package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class OFIMoeaicDataDAO {
	public Map<String,String> getSysBaseInfo(String investNo){
		Map<String,String> result = new HashMap<String, String>();
		SQL sqltool = new SQL("jdbc/sqlMoeaic");
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT b.RECEIVE_NO,b.RESP_DATE,a.* from ");
//		sb.append("(select * FROM [moeaic].[dbo].[OFI_BASEDATA](?)) a ");
//		sb.append("join (SELECT * FROM [moeaic].[dbo].[OFI_RESPDATE](?)) b ");
//		sb.append("on a.investment_no=b.investment_no");
//		String forStmt = sb.toString();
//		System.out.println(forStmt);
//		sb.setLength(0);
		String forStmt = "select * FROM [moeaic].[dbo].[OFI_BASEDATA](?)";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
//			stmt.setString(2, investNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				for(int i=1;i<=meta.getColumnCount();i++){
					result.put(meta.getColumnName(i),DataUtil.nulltoempty(rs.getString(i)));
				}
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
