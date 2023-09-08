package com.isam.ofi.reject.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.helper.DataUtil;
import com.isam.helper.SQL;
import com.isam.ofi.reject.bean.OFIRejectXApplicant;

public class OFIRejectXApplicantDAO {
	
	public List<Map<String,String>> getRejectApplicant(String serno,String enable){
		SQL sqltool = new SQL();
		String forStmt = "select * from dbo.OFI_RejectApplicantXAgents(?,?)";
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			stmt.setString(2, enable);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				Map<String,String> map=new HashMap<String, String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					map.put(meta.getColumnName(i), DataUtil.nulltoempty(rs.getString(i)));
				}
				list.add(map);
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
		return list;
	}
	public OFIRejectXApplicant select(String applyNo) {
		SQL sqltool = new SQL();
		String forpstmt = "select * from OFI_RejectXApplicant where applyNo=?";
		OFIRejectXApplicant bean=new OFIRejectXApplicant();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, applyNo);
			ResultSet rs= pstmt.executeQuery();
			if(rs.next()){
				bean.setApplyNo(rs.getInt("applyNo"));
				bean.setSerno(rs.getInt("serno"));
				bean.setcApplicant(rs.getString("cApplicant"));
				bean.seteApplicant(rs.getString("eApplicant"));
				bean.setNation(rs.getString("nation"));
				bean.setCnCode(rs.getString("cnCode"));
				bean.setNote(rs.getString("note"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				sqltool.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return bean;
	}
	public String insert(OFIRejectXApplicant bean) {
		SQL sqltool = new SQL();
		String forpstmt = "insert into OFI_RejectXApplicant output inserted.applyNo values(?,?,?,?,?,?,?,?,?,?,?)";
		String str="";
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setInt(1, bean.getSerno());
			pstmt.setString(2, bean.getcApplicant());
			pstmt.setString(3, bean.geteApplicant());
			pstmt.setString(4, bean.getNation());
			pstmt.setString(5, bean.getCnCode());
			pstmt.setString(6, bean.getNote());
			pstmt.setString(7, bean.getEnable());
			pstmt.setTimestamp(8, bean.getUpdatetime());
			pstmt.setString(9, bean.getUpdateuser());
			pstmt.setTimestamp(10, bean.getCreatetime());
			pstmt.setString(11, bean.getCreateuser());
			ResultSet rs= pstmt.executeQuery();
			if(rs.next()){
				str=rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				sqltool.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return str;
	}
	public void update(OFIRejectXApplicant bean) {
		SQL sqltool = new SQL();
		String forpstmt = "update OFI_RejectXApplicant set cApplicant=?,eApplicant=?,nation=?,cnCode=?,note=?,updatetime=?,updateuser=? where applyNo=?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getcApplicant());
			pstmt.setString(2, bean.geteApplicant());
			pstmt.setString(3, bean.getNation());
			pstmt.setString(4, bean.getCnCode());
			pstmt.setString(5, bean.getNote());
			pstmt.setTimestamp(6, bean.getUpdatetime());
			pstmt.setString(7, bean.getUpdateuser());
			pstmt.setInt(8, bean.getApplyNo());
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
	public void acvtiveAdd(String serno,String updateuser,Timestamp updatetime) {
		SQL sqltool = new SQL();
		String forpstmt = "update OFI_RejectXApplicant set enable='1',updateuser=?,updatetime=? where serno=? and enable='0'";
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1,updateuser);
			pstmt.setTimestamp(2,updatetime);
			pstmt.setString(3,serno);
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
	public void deleteBySerno(String serno) {
		String forpstmt = "delete OFI_RejectXApplicant WHERE Serno=? ";
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
	public void delete(String applyNo) {
		String forpstmt = "delete OFI_RejectXApplicant WHERE applyNo=? ";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, applyNo);
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
