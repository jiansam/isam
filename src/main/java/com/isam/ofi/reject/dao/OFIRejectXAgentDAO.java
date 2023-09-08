package com.isam.ofi.reject.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.isam.helper.SQL;
import com.isam.ofi.reject.bean.OFIRejectXAgent;

public class OFIRejectXAgentDAO {
	
	public List<OFIRejectXAgent>  select(String applyNo) {
		SQL sqltool = new SQL();
		String forpstmt = "select applyNo,agent,postion from OFI_RejectXAgent where applyNo=?";
		List<OFIRejectXAgent> beans=new ArrayList<OFIRejectXAgent>();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, applyNo);
			ResultSet rs= pstmt.executeQuery();
			while(rs.next()){
				OFIRejectXAgent bean=new OFIRejectXAgent();
				bean.setApplyNo(rs.getInt("applyNo"));
				bean.setAgent(rs.getString("agent"));
				bean.setPostion(rs.getString("postion"));
				beans.add(bean);
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
		return beans;
	}
	public void insert(List<OFIRejectXAgent> beans) {
		String forpstmt = "insert into OFI_RejectXAgent values(?,?,?)";
		SQL sqltool = new SQL();
		try {
			sqltool.noCommit();
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				OFIRejectXAgent bean= beans.get(i);
				pstmt.setInt(1, bean.getApplyNo());
				pstmt.setString(2, bean.getAgent());
				pstmt.setString(3, bean.getPostion());
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
	public void delete(String applyNo) {
		String forpstmt = "delete OFI_RejectXAgent WHERE applyNo=? ";
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
	public void deleteBySerno(String serno) {
		String forpstmt = "delete OFI_RejectXAgent WHERE applyNo in ( select applyNo from ISAM.dbo.OFI_RejectXApplicant where serno=?)";
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
}
