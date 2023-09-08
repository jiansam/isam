package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.isam.bean.OFIInvestorXRelated;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class OFIInvestorXRelatedDAO {
	
	public List<OFIInvestorXRelated> select(String investorSeq){
		List<OFIInvestorXRelated> list= new ArrayList<OFIInvestorXRelated>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_InvestorXRelated where investorSeq=?";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investorSeq);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				OFIInvestorXRelated bean= new OFIInvestorXRelated();
				bean.setSerno(rs.getString("serno"));
				bean.setInvestorSeq(rs.getString("investorSeq"));
				bean.setRelatedname(rs.getString("relatedname"));
				bean.setNation(rs.getString("nation"));
				bean.setCnCode(DataUtil.nulltoempty(rs.getString("cnCode")));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
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
	public OFIInvestorXRelated selectBySerno(String serno){
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_InvestorXRelated where serno=?";
		OFIInvestorXRelated bean= new OFIInvestorXRelated();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean.setSerno(rs.getString("serno"));
				bean.setInvestorSeq(rs.getString("investorSeq"));
				bean.setRelatedname(rs.getString("relatedname"));
				bean.setNation(rs.getString("nation"));
				bean.setCnCode(DataUtil.nulltoempty(rs.getString("cnCode")));
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
	public void delete(String serno){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM OFI_InvestorXRelated WHERE serno = ?";
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
	public void update(OFIInvestorXRelated bean) {
		String forpstmt = "update OFI_InvestorXRelated set relatedname=?,nation=?,cnCode=?,updatetime=?,updateuser=? where serno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getRelatedname());
			pstmt.setString(2, bean.getNation());
			pstmt.setString(3, bean.getCnCode());
			pstmt.setTimestamp(4, bean.getUpdatetime());
			pstmt.setString(5, bean.getUpdateuser());
			pstmt.setString(6, bean.getSerno());
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
	public void insert(OFIInvestorXRelated bean) {
		String forpstmt = "insert into OFI_InvestorXRelated values(?,?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getInvestorSeq());
			pstmt.setString(2, bean.getRelatedname());
			pstmt.setString(3, bean.getNation());
			pstmt.setString(4, bean.getCnCode());
			pstmt.setTimestamp(5, bean.getUpdatetime());
			pstmt.setString(6, bean.getUpdateuser());
			pstmt.setTimestamp(7, bean.getCreatetime());
			pstmt.setString(8, bean.getCreateuser());
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
