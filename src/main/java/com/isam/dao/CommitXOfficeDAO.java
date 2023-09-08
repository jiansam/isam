package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.isam.bean.CommitXOffice;
import com.isam.helper.SQL;

public class CommitXOfficeDAO {
	
	public List<CommitXOffice> select(String IDNO){
		List<CommitXOffice> result = new ArrayList<CommitXOffice>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommitXOffice WHERE IDNO = ? ";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				CommitXOffice bean= new CommitXOffice();
				bean.setIDNO(rs.getString(1));
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
	public int insert(CommitXOffice bean) {
		int result = 0;
		String forpstmt = "insert into CommitXOffice values(?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getIDNO());
			pstmt.setString(2, bean.getType());
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
	public void insertBatch(String[] restrainTypes,String IDNO,java.sql.Timestamp ctime,String cuser) {
		String forpstmt = "INSERT INTO CommitXOffice VALUES(?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<restrainTypes.length;i++){
				pstmt.setString(1, IDNO);
				pstmt.setString(2, restrainTypes[i]);
				pstmt.setTimestamp(3, ctime);
				pstmt.setString(4, cuser);
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
	public void delete(String IDNO){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM CommitXOffice WHERE IDNO = ?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, IDNO);
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
