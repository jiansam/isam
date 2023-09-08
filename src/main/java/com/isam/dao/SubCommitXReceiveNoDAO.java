package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.isam.bean.CommitXReceiveNo;
import com.isam.helper.SQL;

public class SubCommitXReceiveNoDAO {
	
	public List<CommitXReceiveNo> select(String subserno){
		List<CommitXReceiveNo> result = new ArrayList<CommitXReceiveNo>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM SubCommitXReceiveNo WHERE subserno = ? ";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, subserno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				CommitXReceiveNo bean= new CommitXReceiveNo();
				bean.setSerno(rs.getInt(1));
				bean.setReceiveNo(rs.getString(2));
				bean.setRespDate(rs.getString(3));
				bean.setNote(rs.getString(4).replaceAll("[\\r\\n\\f]", ""));
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
	public void insertInto(String subserno,String serno,String subReceive) {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into SubCommitXReceiveNo SELECT '").append(subserno).append("' subserno,receiveNo,respDate,note ");
		sb.append("FROM CommitXReceiveNo where receiveNo in (").append(subReceive).append(") and serno=?");
		String forpstmt =sb.toString();
		sb.setLength(0);
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, serno);
			pstmt.executeUpdate();
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
		String forStmt = "DELETE FROM SubCommitXReceiveNo WHERE subserno = ?";
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
