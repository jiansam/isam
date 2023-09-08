package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.isam.helper.SQL;

public class ProjectXReciveNoDAO {
	
	
	public boolean isExists(int repSerno){
		SQL sqltool = new SQL();
		boolean result=false;
		String forStmt = "SELECT count(*) FROM ProjectXReciveNo WHERE repSerno = ?";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setInt(1, repSerno);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				if(rs.getInt(1)>0){
					 result=true;
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
	public List<String> select(int repSerno){
		SQL sqltool = new SQL();
		List<String> result=new ArrayList<String>();
		String forStmt = "SELECT receiveNo FROM ProjectXReciveNo WHERE repSerno = ?";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setInt(1, repSerno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString(1));
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
	public void delete(int repSerno){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM ProjectXReciveNo WHERE repSerno = ?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setInt(1, repSerno);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void insertBatch(String[] receviceNoStr,int repSerno) {
		String forpstmt = "INSERT INTO ProjectXReciveNo VALUES(?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<receviceNoStr.length;i++){
				pstmt.setInt(1, repSerno);
				pstmt.setString(2, receviceNoStr[i]);
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			sqltool.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				sqltool.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
