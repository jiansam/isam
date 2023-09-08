package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class OFIManageScoreDAO {
	
	public Map<String,List<Map<String,String>>> select(String investNo){
		Map<String,List<Map<String,String>>> result = new TreeMap<String,List<Map<String,String>>>();
		SQL sqltool = new SQL();
		String forStmt ="SELECT * FROM OFI_ManageScore where investNo=?  order by year desc,item";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			List<Map<String,String>> list;
			while(rs.next()){
				Map<String,String> sub=new HashMap<String, String>();
				String k1=rs.getString("year");
				if(result.containsKey(k1)){
					list =result.get(k1);
				}else{
					list = new ArrayList<Map<String,String>>();
				}
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.put(meta.getColumnName(i),DataUtil.nulltoempty(rs.getString(i)));
				}
				list.add(sub);
				result.put(k1, list);
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
