package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.isam.bean.CommitXReceiveNo;
import com.isam.helper.SQL;

public class CommitXReceiveNoDAO {
	
	public JSONArray getJsonFmt(String serno){
		JSONArray ary = new JSONArray();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommitXReceiveNo WHERE serno = ?";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta =rs.getMetaData();
			while(rs.next()){
				JSONObject obj =new JSONObject();
				for(int i=1;i<=meta.getColumnCount();i++){
					obj.put(meta.getColumnName(i), rs.getString(i).replaceAll("[\\r\\n\\f]", ""));
				}
				ary.add(obj);
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
		return ary;
	}
	public List<CommitXReceiveNo> select(String serno){
		List<CommitXReceiveNo> result = new ArrayList<CommitXReceiveNo>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommitXReceiveNo WHERE serno = ? ";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
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
	public List<CommitXReceiveNo> selectByIDNO(String idno){
		List<CommitXReceiveNo> result = new ArrayList<CommitXReceiveNo>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommitXReceiveNo WHERE serno in (select serno from [Commit] where enable='1' and idno=?) ";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, idno);
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
	public int insert(CommitXReceiveNo bean) {
		int result = 0;
		String forpstmt = "insert into CommitXReceiveNo values(?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setInt(1, bean.getSerno());
			pstmt.setString(2, bean.getReceiveNo());
			pstmt.setString(3, bean.getRespDate());
			pstmt.setString(4, bean.getNote());
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
	public void insert(List<CommitXReceiveNo> beans) {
		String forpstmt = "insert into CommitXReceiveNo values(?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				CommitXReceiveNo bean = beans.get(i);
				pstmt.setInt(1, bean.getSerno());
				pstmt.setString(2, bean.getReceiveNo());
				pstmt.setString(3, bean.getRespDate());
				pstmt.setString(4, bean.getNote());
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
	public void delete(int serno){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM CommitXReceiveNo WHERE serno = ?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setInt(1, serno);
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
