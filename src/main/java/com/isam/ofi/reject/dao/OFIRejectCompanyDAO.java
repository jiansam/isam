package com.isam.ofi.reject.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.isam.helper.SQL;
import com.isam.ofi.reject.bean.OFIRejectCompany;

public class OFIRejectCompanyDAO {
	
	public List<String> getCNoListByCName(String cName,String cNo){
		SQL sqltool = new SQL();
		String forStmt = "SELECT cNo FROM OFI_RejectCompany where cName=? and enable='1' and cNo!=isnull(?,cNo)";
		 List<String> str=new ArrayList<String>();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, cName);
			stmt.setString(2, cNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				str.add(rs.getString(1));
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
		return str;
	}
	public String getCNoByCName(String cName){
		SQL sqltool = new SQL();
		String forStmt = "SELECT cNo FROM OFI_RejectCompany where cName=? and enable='1'";
		String str="";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, cName);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				str=rs.getString(1);
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
		return str;
	}
	public OFIRejectCompany select(String cNo){
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_RejectCompany where cNo=? and enable='1'";
		OFIRejectCompany bean=null;
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, cNo);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean = new OFIRejectCompany();
				bean.setcNo(rs.getInt("cNo"));
				bean.setCname(rs.getString("cName"));
				bean.setIdno(rs.getString("idno"));
				bean.setIsNew(rs.getString("isNew"));
				bean.setSetupdate(rs.getString("setupdate"));
				bean.setOrgType(rs.getString("orgType"));
				bean.setEnable(rs.getString("enable"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
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
	public List<OFIRejectCompany> getCNameList(String cName,String idno){
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT cNo,cName,idno,setupdate FROM OFI_RejectCompany where enable='1' and cName like '");
		sb.append(cName).append("' and idno like '").append(idno).append("'");
		String forStmt = sb.toString();
		sb.setLength(0);
		List<OFIRejectCompany> list = new ArrayList<OFIRejectCompany>();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				OFIRejectCompany bean=new OFIRejectCompany();
				bean.setcNo(rs.getInt("cNo"));
				bean.setCname(rs.getString("cName"));
				bean.setIdno(rs.getString("idno"));
				bean.setSetupdate(rs.getString("setupdate"));
				list.add(bean);
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
	public String insert(OFIRejectCompany bean) {
		String forpstmt = "insert into OFI_RejectCompany output inserted.cNo values(?,?,?,?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		String str="";
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getCname());
			pstmt.setString(2, bean.getIdno());
			pstmt.setString(3, bean.getIsNew());
			pstmt.setString(4, bean.getSetupdate());
			pstmt.setString(5, bean.getOrgType());
			pstmt.setString(6, bean.getEnable());
			pstmt.setTimestamp(7, bean.getUpdatetime());
			pstmt.setString(8, bean.getUpdateuser());
			pstmt.setTimestamp(9, bean.getCreatetime());
			pstmt.setString(10, bean.getCreateuser());
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
	public void update(OFIRejectCompany bean) {
		String forpstmt = "update OFI_RejectCompany set cName=?,idno=?,isNew=?,setupdate=?,orgType=?,enable=?,updatetime=?,updateuser=? where cNo=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getCname());
			pstmt.setString(2, bean.getIdno());
			pstmt.setString(3, bean.getIsNew());
			pstmt.setString(4, bean.getSetupdate());
			pstmt.setString(5, bean.getOrgType());
			pstmt.setString(6, bean.getEnable());
			pstmt.setTimestamp(7, bean.getUpdatetime());
			pstmt.setString(8, bean.getUpdateuser());
			pstmt.setInt(9, bean.getcNo());
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
	public void unable(OFIRejectCompany bean) {
		String forpstmt = "update OFI_RejectCompany set enable='0',updatetime=?,updateuser=? where cNo!=? and cname=? and enable='1'";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setTimestamp(1, bean.getUpdatetime());
			pstmt.setString(2, bean.getUpdateuser());
			pstmt.setInt(3, bean.getcNo());
			pstmt.setString(4, bean.getCname());
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
