package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.isam.bean.OFIInvestorXFile;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class OFIInvestorXFileDAO {
	
	public int insert(OFIInvestorXFile bean) {
		int result = 0;
		String forpstmt = "insert into OFI_InvestorXFile Output Inserted.fNo values(?,?,?,?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getInvestorSeq());
			pstmt.setString(2,bean.getTitle() );
			pstmt.setString(3,bean.getfName() );
			pstmt.setBytes(4, bean.getfContent());
			pstmt.setString(5,bean.getNote() );
			pstmt.setTimestamp(6, bean.getUpdatetime());
			pstmt.setString(7,bean.getUpdateuser() );
			pstmt.setTimestamp(8, bean.getCreatetime());
			pstmt.setString(9,bean.getCreateuser() );
			pstmt.setString(10,bean.getEnable());
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				result=rs.getInt(1);
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
	public int unable(int fNo,String updateuser) {
		int result = 0;
		String forpstmt = "update OFI_InvestorXFile set enable='0',updatetime=getDate(),updateuser=? WHERE fNo=? ";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, updateuser);
			pstmt.setInt(2, fNo);
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
	public void update(OFIInvestorXFile bean) {
		String forpstmt = "update OFI_InvestorXFile set title=?,fName=?,fContent=?,note=?,updatetime=?,updateuser=? WHERE fNo=? ";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getTitle());
			pstmt.setString(2, bean.getfName());
			pstmt.setBytes(3, bean.getfContent());
			pstmt.setString(4, bean.getNote());
			pstmt.setTimestamp(5, bean.getUpdatetime());
			pstmt.setString(6, bean.getUpdateuser());
			pstmt.setInt(7, bean.getfNo());
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
	public List<OFIInvestorXFile> select(String investorSeq){
		List<OFIInvestorXFile> result = new ArrayList<OFIInvestorXFile>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT fNo,investorSeq,title,fName,note,createtime FROM OFI_InvestorXFile where investorSeq=? and enable='1' order by createtime desc";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setInt(1, Integer.parseInt(investorSeq));
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				OFIInvestorXFile bean= new OFIInvestorXFile();
				bean.setfNo(rs.getInt("fNo"));
				bean.setInvestorSeq(rs.getString("investorSeq"));
				bean.setTitle(rs.getString("title"));
				bean.setfName(rs.getString("fName"));
				bean.setNote(rs.getString("note"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreatetime_str(rs.getTimestamp("createtime")); //107-07-04 供投資人、投資案 列表頁 出 架構圖用
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
	
	public List<Map<String,String>> getInvestorHasfile(String investNo){
		List<Map<String,String>> result = new ArrayList<Map<String, String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT DISTINCT A.INVESTOR_SEQ, A.INVESTOR_CHTNAME FROM OFI_InvestCase A ");
		sb.append("LEFT OUTER JOIN OFI_InvestorXFile B ON A.INVESTOR_SEQ = B.investorSeq ");
		sb.append("WHERE A.investNo = ? AND B.enable='1' ");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				Map<String,String> sub = new HashMap<String, String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.put(meta.getColumnName(i),DataUtil.nulltoempty(rs.getString(i)));
				}
				result.add(sub);
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
	public List<OFIInvestorXFile> uploadFile(ArrayList<String> investorSeqS){
		List<OFIInvestorXFile> result = new ArrayList<OFIInvestorXFile>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT B.fName, B.fContent, A.INVESTOR_CHTNAME "
					   + "FROM OFI_InvestorXFile B "
					   + "LEFT OUTER JOIN OFI_InvestCase A ON A.INVESTOR_SEQ = B.investorSeq "
					   + "where investorSeq = ? "
					   + "and enable='1' "
					   + "order by createtime desc ";
		try {
			sqltool.noCommit();
			PreparedStatement stmt = sqltool.prepare(forStmt);
			for(String investorSeq : investorSeqS){

				stmt.setString(1, investorSeq);
				ResultSet rs = stmt.executeQuery();
				while(rs.next()){
					OFIInvestorXFile bean= new OFIInvestorXFile();
					bean.setfName(rs.getString("fName"));
					bean.setfContent(rs.getBytes("fContent"));
					bean.setINVESTOR_CHTNAME(rs.getString("iNVESTOR_CHTNAME"));
					result.add(bean);
				}
			}
			sqltool.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				sqltool.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally{
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	public ArrayList<Map<String, String>> selectBySernoS(ArrayList<String> sernoS){
		ArrayList<Map<String, String>> result = null;
		SQL sqltool = new SQL();
		String forStmt = "SELECT A.serno, A.investNo, A.reportyear, B.isNew, B.setupdate "
						+ "FROM OFI_InvestNoXFinancial A "
						+ "LEFT OUTER JOIN OFI_InvestList B ON A.investNo = B.investNo "
						+ "where serno=? ";
		try {
			result = new ArrayList<>();
			PreparedStatement stmt = sqltool.prepare(forStmt);
			for(String serno : sernoS){
				
				stmt.setString(1, serno);
				ResultSet rs = stmt.executeQuery();
				if(rs.next()){
					Map<String, String> data = new HashMap<>();
					data.put("serno", rs.getString("serno"));
					data.put("investNo", rs.getString("investNo"));
					data.put("reportyear", rs.getString("reportyear"));
					data.put("isNew", rs.getString("isNew"));
					data.put("setupdate", rs.getString("setupdate"));
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
	public OFIInvestorXFile select(int fNo){
		OFIInvestorXFile bean= new OFIInvestorXFile();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_InvestorXFile where fNo=? ";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setInt(1, fNo);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean.setfNo(rs.getInt("fNo"));
				bean.setInvestorSeq(rs.getString("investorSeq"));
				bean.setTitle(rs.getString("title"));
				bean.setfName(rs.getString("fName"));
				bean.setfContent(rs.getBytes("fContent"));
				bean.setNote(rs.getString("note"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setEnable(rs.getString("enable"));
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
	
	//取出所有 有架構圖的 InvestorSeq
	public ArrayList<String> selectInvestorSeqS_hasFile(){
		ArrayList<String> result = null;
		SQL sqltool = new SQL();
		String forStmt = "SELECT distinct investorSeq FROM OFI_InvestorXFile";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			result = new ArrayList<>();
			while(rs.next()){
				result.add(rs.getString("investorSeq"));
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
