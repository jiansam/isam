package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.isam.bean.CommitXRestrainOffice;
import com.isam.helper.SQL;

public class SubCommitXRestrainOfficeDAO {
	
	public List<CommitXRestrainOffice> select(String subserno){
		List<CommitXRestrainOffice> result = new ArrayList<CommitXRestrainOffice>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM SubCommitXRestrainOffice WHERE subserno = ? ";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, subserno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				CommitXRestrainOffice bean= new CommitXRestrainOffice();
				bean.setSerno(rs.getInt(1));
				bean.setType(rs.getString(2));
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
	
	public void insert(List<CommitXRestrainOffice> beans) {
		String forpstmt = "insert into SubCommitXRestrainOffice values(?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				CommitXRestrainOffice bean= beans.get(i);
				pstmt.setInt(1, bean.getSerno());
				pstmt.setString(2, bean.getType());
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
	public void delete(String subserno){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM SubCommitXRestrainOffice WHERE subserno = ?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, subserno);
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
}
