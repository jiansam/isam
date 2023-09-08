package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.isam.helper.SQL;

public class CommitInvestorXContactsXReceiveNoDAO {
	
	public List<String> select(String cid,String idno){
		List<String> result = new ArrayList<String>();
		StringBuilder sb=new StringBuilder();
		sb.append("SELECT receiveNo FROM CommitInvestorXContactsXReceiveNo where cid=? and receiveNo in ");
		sb.append("(select receiveNo from CommitXReceiveNo where serno in (select serno from [Commit] ");
		sb.append(" where enable='1' and idno=?))");
		SQL sqltool = new SQL();
		String forStmt =sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, cid);
			stmt.setString(2, idno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString("receiveNo"));
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
	
	public void insert(String cid,List<String> list) {
		String forpstmt = "insert into CommitInvestorXContactsXReceiveNo values(?,?)";
		SQL sqltool = new SQL();
		try {
			sqltool.noCommit();
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<list.size();i++){
				pstmt.setString(1, cid);
				pstmt.setString(2, list.get(i));
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
	public int delete(String cid) {
		int result = 0;
		String forpstmt = "delete CommitInvestorXContactsXReceiveNo WHERE cid=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, cid);
			result=pstmt.executeUpdate();			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				sqltool.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
