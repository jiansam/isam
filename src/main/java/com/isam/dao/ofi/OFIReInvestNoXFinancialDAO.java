package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestNoXFinancial;
import com.isam.helper.SQL;

public class OFIReInvestNoXFinancialDAO {
	public int isExist(String reInvestNo,String year){
		SQL sqltool = new SQL();
		String forStmt = "SELECT count(*) FROM OFI_ReInvestNoXFinancial where ReInvestNo=? and reportyear=?";
		int count=0;
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, reInvestNo);
			stmt.setString(2, year);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				count=rs.getInt(1);
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
		return count;
	}
	public Map<String,List<OFIInvestNoXFinancial>> select(String investNo){
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_ReInvestNoXFinancial where ReInvestNo in (SELECT reInvestNo from OFI_ReInvestXInvestNo where enable='1' and investNo=?) "
				   	   + "order by reportyear desc"; //107-08-10追加排序
		Map<String,List<OFIInvestNoXFinancial>> map=new HashMap<String,List<OFIInvestNoXFinancial>>();
		List<OFIInvestNoXFinancial> list=null;
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				String key=rs.getString("ReInvestNo");
				if(map.containsKey(key)){
					list=map.get(key);
				}else{
					list=new ArrayList<OFIInvestNoXFinancial>();
				}
				OFIInvestNoXFinancial bean= new OFIInvestNoXFinancial();
				bean.setSerno(rs.getString("serno"));
				bean.setInvestNo(rs.getString("ReInvestNo"));
				bean.setReportyear(rs.getString("reportyear"));
				bean.setReportdate(rs.getString("reportdate"));
				bean.setNote(rs.getString("note"));
				bean.setSeq(rs.getString("seq"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				list.add(bean);
				map.put(key, list);
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
		return map;
	}
	public OFIInvestNoXFinancial selectBySerno(String serno){
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_ReInvestNoXFinancial where serno=?";
		OFIInvestNoXFinancial bean= new OFIInvestNoXFinancial();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean.setSerno(rs.getString("serno"));
				bean.setInvestNo(rs.getString("ReInvestNo"));
				bean.setReportyear(rs.getString("reportyear"));
				bean.setReportdate(rs.getString("reportdate"));
				bean.setNote(rs.getString("note"));
				bean.setSeq(rs.getString("seq"));
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
	public ArrayList<Map<String, String>> selectBySernoS(ArrayList<String> sernoS){
		ArrayList<Map<String, String>> result = null;
		SQL sqltool = new SQL();
		String forStmt = "SELECT A.serno, A.ReInvestNo, A.reportyear, B.isNew, B.setupdate, B.reinvestment "
						+ "FROM OFI_ReInvestNoXFinancial A "
						+ "LEFT OUTER JOIN OFI_ReInvestmentList B ON A.reInvestNo = B.ReInvestNo "
						+ "where A.serno = ?";
		try {
			result = new ArrayList<>();
			PreparedStatement stmt = sqltool.prepare(forStmt);
			for(String serno : sernoS){
				
				stmt.setString(1, serno);
				ResultSet rs = stmt.executeQuery();
				if(rs.next()){
					Map<String, String> data = new HashMap<>();
					data.put("serno", rs.getString("serno"));
					data.put("ReInvestNo", rs.getString("ReInvestNo"));
					data.put("reportyear", rs.getString("reportyear"));
					data.put("isNew", rs.getString("isNew"));
					data.put("setupdate", rs.getString("setupdate"));
					data.put("reinvestment", rs.getString("reinvestment"));
					result.add(data);
				}
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
	public OFIInvestNoXFinancial selectbean(String reInvestNo,String year){
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_ReInvestNoXFinancial where reInvestNo=? and reportyear=?";
		OFIInvestNoXFinancial bean= new OFIInvestNoXFinancial();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, reInvestNo);
			stmt.setString(2, year);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean.setSerno(rs.getString("serno"));
				bean.setInvestNo(rs.getString("ReInvestNo"));
				bean.setReportyear(rs.getString("reportyear"));
				bean.setReportdate(rs.getString("reportdate"));
				bean.setNote(rs.getString("note"));
				bean.setSeq(rs.getString("seq"));
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
		String forStmt = "DELETE FROM OFI_ReInvestNoXFinancial WHERE serno = ?";
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
	public void update(OFIInvestNoXFinancial bean) {
		String forpstmt = "update OFI_ReInvestNoXFinancial set reportyear=?,reportdate=?,note=?,updatetime=?,updateuser=? where serno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getReportyear());
			pstmt.setString(2, bean.getReportdate());
			pstmt.setString(3, bean.getNote());
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
	public int insert(OFIInvestNoXFinancial bean) {
		int result= 0;
		String forpstmt = "insert into OFI_ReInvestNoXFinancial OUTPUT INSERTED.serno values(?,?,?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getInvestNo());
			pstmt.setString(2, bean.getReportyear());
			pstmt.setString(3, bean.getReportdate());
			pstmt.setString(4, bean.getNote());
			pstmt.setString(5, bean.getSeq());
			pstmt.setTimestamp(6, bean.getUpdatetime());
			pstmt.setString(7, bean.getUpdateuser());
			pstmt.setTimestamp(8, bean.getCreatetime());
			pstmt.setString(9, bean.getCreateuser());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				result=rs.getInt("serno");
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
		return result;
	}
}
