package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.isam.bean.OFINTBTDatas;
import com.isam.helper.SQL;

import Lara.Utility.DateUtil;

public class OFINTBTDatasDAO
{
	public ArrayList<OFINTBTDatas> list(String investNo){
		ArrayList<OFINTBTDatas> result = null;
		SQL sqltools = new SQL();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = sqltools.prepare("SELECT id, investNo, title, fName, note, createtime "
								   + "FROM OFI_NTBTDatas "
								   + "WHERE investNo = ? "
								   + "AND enable='1' ");
								   //+ "ORDER BY createtime desc");
			pstmt.setString(1, investNo);
			rs = pstmt.executeQuery();
			result = new ArrayList<>();
			while (rs.next()){
				
				OFINTBTDatas bean = new OFINTBTDatas();
				bean.setId(rs.getInt("id"));
				bean.setInvestNo(rs.getString("investNo"));
				bean.setTitle(rs.getString("title"));
				bean.setfName(rs.getString("fName"));
				bean.setNote(rs.getString("note"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreatetime_ROC(rs.getTimestamp("createtime"));
				result.add(bean);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				sqltools.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		return result;
	}
	
	public ArrayList<OFINTBTDatas> uploadFile(String investNo){
		ArrayList<OFINTBTDatas> result = null;
		SQL sqltools = new SQL();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = sqltools.prepare("SELECT fName, fContent "
								   + "FROM OFI_NTBTDatas "
								   + "WHERE investNo = ? "
								   + "AND enable='1' "
								   + "ORDER BY createtime desc");
			pstmt.setString(1, investNo);
			rs = pstmt.executeQuery();
			result = new ArrayList<>();
			while (rs.next()){
				
				OFINTBTDatas bean = new OFINTBTDatas();
				bean.setfName(rs.getString("fName"));
				bean.setfContent(rs.getBytes("fContent"));
				result.add(bean);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				sqltools.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		return result;
	}
	public OFINTBTDatas get(int id){
		OFINTBTDatas result = null;
		SQL sqltools = new SQL();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = sqltools.prepare("SELECT * FROM OFI_NTBTDatas WHERE id = ? ");
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()){
				
				result = new OFINTBTDatas();
				result.setId(rs.getInt("id"));
				result.setInvestNo(rs.getString("investNo"));
				result.setTitle(rs.getString("title"));
				result.setfName(rs.getString("fName"));
				result.setfContent(rs.getBytes("fContent"));
				result.setNote(rs.getString("note"));
				result.setUpdatetime(rs.getTimestamp("updatetime"));
				result.setUpdatetime_ROC(rs.getTimestamp("updatetime"));
				result.setUpdateuser(rs.getString("updateuser"));
				result.setCreatetime(rs.getTimestamp("createtime"));
				result.setCreatetime_ROC(rs.getTimestamp("createtime"));
				result.setCreateuser(rs.getString("createuser"));
				result.setEnable(rs.getBoolean("enable"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				sqltools.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		return result;
	}
	
	public int insert(OFINTBTDatas bean){
		
		int result = 0;
		SQL sqltools = new SQL();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = sqltools.prepare(
					"INSERT INTO OFI_NTBTDatas "
			      + "(investNo, title, fName, fContent, note, updatetime, updateuser, createtime, createuser, enable) "
			      + "VALUES (?,?,?,?,?,?,?,?,?,?) " , new String[]{"id"});
			
			int column = 1;
			pstmt.setString(column++, bean.getInvestNo());
			pstmt.setString(column++, bean.getTitle());
			pstmt.setString(column++, bean.getfName());
			pstmt.setBytes(column++, bean.getfContent());
			pstmt.setString(column++, bean.getNote());
			pstmt.setTimestamp(column++, DateUtil.dateToTimestamp(bean.getUpdatetime()));
			pstmt.setString(column++, bean.getUpdateuser());
			pstmt.setTimestamp(column++, DateUtil.dateToTimestamp(bean.getCreatetime()));
			pstmt.setString(column++, bean.getCreateuser());
			pstmt.setBoolean(column++, true);
			pstmt.executeUpdate();
			rs = pstmt.getGeneratedKeys();
			if(rs.next()){
				result = rs.getInt(1);
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				sqltools.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public void update(OFINTBTDatas bean){
		
		SQL sqltools = new SQL();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = sqltools.prepare(
					"UPDATE OFI_NTBTDatas SET "
			      + "title = ?,"
			      + "fName = ?, "
			      + "fContent = ?, "
			      + "note = ?, "
			      + "updatetime = ?, "
			      + "updateuser = ? "
			      + "WHERE id = ? ");
			
			int column = 1;
			pstmt.setString(column++, bean.getTitle());
			pstmt.setString(column++, bean.getfName());
			pstmt.setBytes(column++, bean.getfContent());
			pstmt.setString(column++, bean.getNote());
			pstmt.setTimestamp(column++, DateUtil.dateToTimestamp(bean.getUpdatetime()));
			pstmt.setString(column++, bean.getUpdateuser());
			pstmt.setInt(column++, bean.getId());
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				sqltools.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public int unable(int id, String updateuser) {
		int result = 0;
		SQL sqltools = new SQL();
		PreparedStatement pstmt = null;
		try {
			pstmt = sqltools.prepare(
					"UPDATE OFI_NTBTDatas SET "
				  + "enable = ?, "
			      + "updatetime = getDate(), "
			      + "updateuser = ? "
			      + "WHERE id = ? ");
			int column = 1;
			pstmt.setBoolean(column++, false);
			pstmt.setString(column++, updateuser);
			pstmt.setInt(column++, id);
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				sqltools.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}	
	
}
