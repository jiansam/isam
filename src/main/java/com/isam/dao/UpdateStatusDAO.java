package com.isam.dao;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.isam.bean.UpdateStatus;
import com.isam.helper.SQL;

public class UpdateStatusDAO {
	public static ArrayList<UpdateStatus> list(){
		ArrayList<UpdateStatus> result = new ArrayList<UpdateStatus>();
		
		SQL sqltool = new SQL();
		
		try {
			ResultSet rs = sqltool.query("SELECT * FROM UpdateStatus ORDER BY update_time DESC ");
			
			while(rs.next()) {
				UpdateStatus us = new UpdateStatus();
				us.setUpdate_date(rs.getString("update_time"));
				us.setStatus(rs.getBoolean("status"));
				
				result.add(us);
			}
			
			rs.close();
		}catch (Exception e) {
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
