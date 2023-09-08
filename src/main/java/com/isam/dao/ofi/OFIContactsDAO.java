package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.OFIContacts;
import com.isam.helper.SQL;

public class OFIContactsDAO {
	public Map<String,List<OFIContacts>> getContacts(String investorSeq){
		Map<String,List<OFIContacts>> result= new HashMap<String,List<OFIContacts>>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_Contacts where investNo in (select investNo from OFI_InvestCaseList where enable='1' and investorSeq=?)";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investorSeq);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				String key=rs.getString("investNo");
				List<OFIContacts> sub ;
				if(result.containsKey(key)){
					sub=result.get(key);
				}else{
					sub= new ArrayList<OFIContacts>();
				}
				OFIContacts bean= new OFIContacts();
				bean.setSerno(rs.getString("serno"));
				bean.setInvestNo(key);
				bean.setName(rs.getString("name"));
				bean.setTelNo(rs.getString("telNo"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				sub.add(bean);
				result.put(key, sub);
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
	public List<OFIContacts> select(String investNo){
		List<OFIContacts> result = new ArrayList<OFIContacts>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_Contacts where investNo=?";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				OFIContacts bean= new OFIContacts();
				bean.setSerno(rs.getString("serno"));
				bean.setInvestNo(rs.getString("investNo"));
				bean.setName(rs.getString("name"));
				bean.setTelNo(rs.getString("telNo"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
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
	public OFIContacts selectbean(String serno){
		OFIContacts bean= new OFIContacts();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_Contacts where serno=? ";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				bean.setSerno(rs.getString("serno"));
				bean.setInvestNo(rs.getString("investNo"));
				bean.setName(rs.getString("name"));
				bean.setTelNo(rs.getString("telNo"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
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
		return bean;
	}
	public void delete(String serno){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM OFI_Contacts WHERE serno = ?";
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
	public void update(OFIContacts bean) {
		String forpstmt = "update OFI_Contacts set name=?,telNo=?,Createtime=?,createuser=? where serno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getName());
			pstmt.setString(2, bean.getTelNo());
			pstmt.setTimestamp(3, bean.getCreatetime());
			pstmt.setString(4, bean.getCreateuser());
			pstmt.setString(5, bean.getSerno());
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
	public void insert(OFIContacts bean) {
		String forpstmt = "insert into OFI_Contacts values(?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getInvestNo());
			pstmt.setString(2, bean.getName());
			pstmt.setString(3, bean.getTelNo());
			pstmt.setTimestamp(4, bean.getCreatetime());
			pstmt.setString(5, bean.getCreateuser());
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
}
