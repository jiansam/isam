package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.isam.helper.SQL;

public class OFIInvestOptionDAO {
	
	public Map<String,Map<String,String>> select(){
		SQL sqltool = new SQL();
		Map<String,Map<String,String>> result= new HashMap<String, Map<String,String>>();
		String forStmt = "SELECT Description,SelectName,OptionValue FROM OFI_WebOption where enable='1' order by SelectName,seq";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			Map<String,String> sub; 
			while(rs.next()){
				String key1=rs.getString("SelectName");
				String key2=rs.getString("OptionValue");
				String value=rs.getString("Description");
				if(result.containsKey(key1)){
					sub=result.get(key1);
				}else{
					sub= new LinkedHashMap<String, String>();
				}
				sub.put(key2, value);
				result.put(key1, sub);
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
