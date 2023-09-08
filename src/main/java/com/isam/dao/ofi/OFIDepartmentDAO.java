package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

import com.isam.helper.SQL;

public class OFIDepartmentDAO {
	
	public Map<String,String> getCodeNameMap(){
		SQL sqltool = new SQL();
		Map<String,String> result= new LinkedHashMap<String, String>();
		String forStmt = "SELECT a.code,case when a.[level]=2 then b.[description]+a.[description] else a.[description] end name FROM OFI_Department a left join OFI_Department b on a.parent=b.code order by code";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.put(rs.getString("code"), rs.getString("name"));
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
