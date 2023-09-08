package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.isam.bean.CommitXRestrainOffice;
import com.isam.helper.SQL;

public class CommitXRestrainOfficeDAO {
	
	public String selectStr(String serno){
		String result = "";
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommitXRestrainOffice WHERE serno = ? ";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				if(result.length()>0){
					result+=",";
				}
				result+=rs.getString(2);
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
	public String selectStrName(String serno){
		String result = "";
		SQL sqltool = new SQL();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from (SELECT [title] FROM CommitXRestrainOffice cro left join ( SELECT [restrainType],[title] FROM CommitRestrainType ");
		sb.append("where enable='1')crt on cro.type=crt.restrainType where serno=? group by [title])x");
		String forStmt = sb.toString();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				if(result.length()>0){
					result+=",";
				}
				result+=rs.getString(1);
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
	public String selectStrNameByIDNO(String idno){
		String result = "";
		SQL sqltool = new SQL();
		StringBuffer sb = new StringBuffer();
		sb.append("select * from (SELECT [title] FROM CommitXRestrainOffice cro left join ( SELECT [restrainType],[title] FROM CommitRestrainType ");
		sb.append("where enable='1')crt on cro.type=crt.restrainType where serno in (");
		sb.append("SELECT [serno] FROM [Commit] where IDNO=? and enable='1' and TYPE='01' ) group by [title])x");
		String forStmt = sb.toString();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, idno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				if(result.length()>0){
					result+=",";
				}
				result+=rs.getString(1);
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
	public List<CommitXRestrainOffice> select(String serno){
		List<CommitXRestrainOffice> result = new ArrayList<CommitXRestrainOffice>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommitXRestrainOffice WHERE serno = ? ";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
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
	public int insert(CommitXRestrainOffice bean) {
		int result = 0;
		String forpstmt = "insert into CommitXRestrainOffice values(?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setInt(1, bean.getSerno());
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
	public void insert(List<CommitXRestrainOffice> beans) {
		String forpstmt = "insert into CommitXRestrainOffice values(?,?)";
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
	public void delete(int serno){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM CommitXRestrainOffice WHERE serno = ?";
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
