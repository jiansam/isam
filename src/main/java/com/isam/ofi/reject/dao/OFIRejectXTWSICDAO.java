package com.isam.ofi.reject.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.isam.helper.SQL;
import com.isam.ofi.reject.bean.OFIRejectXTWSIC;

public class OFIRejectXTWSICDAO {
	public String select(String serno){
		SQL sqltool = new SQL();
		String forStmt ="SELECT item FROM OFI_RejectXTWSIC where serno=?";
		StringBuilder sb = new StringBuilder();
		String result="";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				if(sb.length()>0){
					sb.append(",");
				}
				sb.append(rs.getString(1));
			}
			result=sb.toString();
			sb.setLength(0);
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
	public void delete(String serno){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM OFI_RejectXTWSIC WHERE serno = ?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, serno);
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
	public void insert(List<OFIRejectXTWSIC> beans) {
		String forpstmt = "insert into OFI_RejectXTWSIC values(?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			sqltool.noCommit();
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				OFIRejectXTWSIC bean= beans.get(i);
				pstmt.setString(1, bean.getSerno());
				pstmt.setString(2, bean.getItem());
				pstmt.setInt(3, bean.getSeq());
				pstmt.setTimestamp(4, bean.getUpdatetime());
				pstmt.setString(5, bean.getUpdateuser());
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
