package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestNoXFContent;
import com.isam.helper.SQL;

public class OFIReInvestNoXFContentDAO {
	public void delete(String serno){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM OFI_ReInvestNoXFContent WHERE serno = ?";
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
	public void insert(List<OFIInvestNoXFContent> beans) {
		String forpstmt = "insert into OFI_ReInvestNoXFContent values(?,?,?)";
		SQL sqltool = new SQL();
		try {
			sqltool.noCommit();
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				OFIInvestNoXFContent bean = beans.get(i);
				pstmt.setInt(1, bean.getSerno());
				pstmt.setInt(2, bean.getOptionId());
				pstmt.setString(3, bean.getValue());
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
	public Map<String,String> selectBySerno(String serno){
		SQL sqltool = new SQL();
		String forStmt = "SELECT optionId,value FROM OFI_ReInvestNoXFContent where serno=?";
		Map<String,String> result= new HashMap<String, String>();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.put(rs.getString("optionId"), rs.getString("value"));
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
}
