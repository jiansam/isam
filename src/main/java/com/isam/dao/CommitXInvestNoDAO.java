package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.isam.bean.CommitXInvestNo;
import com.isam.helper.SQL;

public class CommitXInvestNoDAO {
	
	@SuppressWarnings("unchecked")
	public JSONArray getJsonFmt(String serno){
		JSONArray ary = new JSONArray();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommitXInvestNo WHERE serno = ? ";
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
	public List<CommitXInvestNo> select(String serno){
		List<CommitXInvestNo> result = new ArrayList<CommitXInvestNo>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommitXInvestNo WHERE serno = ? ";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				CommitXInvestNo bean= new CommitXInvestNo();
				bean.setSerno(rs.getInt(1));
				bean.setInvestNo(rs.getString(2));
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
	public int insert(CommitXInvestNo bean) {
		int result = 0;
		String forpstmt = "insert into CommitXInvestNo values(?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setInt(1, bean.getSerno());
			pstmt.setString(2, bean.getInvestNo());
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
	public void insert(List<CommitXInvestNo> beans) {
		String forpstmt = "insert into CommitXInvestNo values(?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				CommitXInvestNo bean= beans.get(i);
				pstmt.setInt(1, bean.getSerno());
				pstmt.setString(2, bean.getInvestNo());
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
		String forStmt = "DELETE FROM CommitXInvestNo WHERE serno = ?";
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
