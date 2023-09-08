package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.isam.bean.Project;
import com.isam.helper.SQL;

public class ProjectDAO {
	
	public int getSerno(String investNo,String IDNO){
		int result=-1;
		SQL sqltool = new SQL();
		String forStmt = "SELECT Serno FROM Project WHERE investNo=? and IDNO=?";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, IDNO);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result=rs.getInt(1);
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
	public List<Project> select(String serno,String state){
		List<Project> result = new ArrayList<Project>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM Project WHERE serno = isnull(?,serno) and state = isnull(?,state) ORDER BY serno";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			stmt.setString(2, state);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Project bean= new Project();
				bean.setSerno(rs.getInt(1));
				bean.setInvestNo(rs.getString(2).trim());
				bean.setIDNO(rs.getString(3).trim());
				bean.setState(rs.getString(4));
				bean.setNote(rs.getString(5));
				bean.setUpdatetime(rs.getTimestamp(6));
				bean.setUpdateuser(rs.getString(7));
				bean.setCreatetime(rs.getTimestamp(8));
				bean.setCreateuser(rs.getString(9));
				bean.setNeedAlert(rs.getString(10));
				bean.setLastReceiveNo(rs.getString(11)==null?"":rs.getString(11));
				bean.setPorjDate(rs.getString(12)==null?"":rs.getString(12));
				bean.setIsSysDate(rs.getString(13));
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
	public List<Project> selectBySernoStr(String SernoStr){
		List<Project> result = new ArrayList<Project>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM Project WHERE serno in ("+SernoStr+")";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Project bean= new Project();
				bean.setSerno(rs.getInt(1));
				bean.setInvestNo(rs.getString(2).trim());
				bean.setIDNO(rs.getString(3).trim());
				bean.setState(rs.getString(4));
				bean.setNote(rs.getString(5));
				bean.setUpdatetime(rs.getTimestamp(6));
				bean.setUpdateuser(rs.getString(7));
				bean.setCreatetime(rs.getTimestamp(8));
				bean.setCreateuser(rs.getString(9));
				bean.setNeedAlert(rs.getString(10));
				bean.setLastReceiveNo(rs.getString(11)==null?"":rs.getString(11));
				bean.setPorjDate(rs.getString(12)==null?"":rs.getString(12));
				bean.setIsSysDate(rs.getString(13));
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
	public List<String> getIDNOByInvestNo(String investNo){
		SQL sqltool = new SQL();
		List<String> result=new ArrayList<String>();
		String forStmt = "SELECT IDNO FROM Project WHERE investNo = (select investNo FROM CDataInvestment where INVESTMENT_NO=?)";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString(1).trim());
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
	public List<Project> getSearchResult(String investor,String IDNO,String investNo,String cnName,String state,String alert){
		List<Project> result = new ArrayList<Project>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT p.* FROM Project p where p.IDNO in (");
		sb.append("SELECT IDNO FROM CDataInvestor where INVESTOR_CHTNAME like ? and (oIDNO like ? or IDNO like ?) ) ");
		sb.append("and p.investNo in ( ");
		sb.append("SELECT investNo FROM CDataInvestment where INVESTMENT_NO like ? and COMP_CHTNAME like ?) ");
		sb.append("and state in ("+state+") ");
		sb.append("and needAlert=isnull(?,needAlert)");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investor);
			stmt.setString(2, IDNO);
			stmt.setString(3, IDNO);
			stmt.setString(4, investNo);
			stmt.setString(5, cnName);
			stmt.setString(6, alert);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Project bean= new Project();
				bean.setSerno(rs.getInt(1));
				bean.setInvestNo(rs.getString(2).trim());
				bean.setIDNO(rs.getString(3).trim());
				bean.setState(rs.getString(4));
				bean.setNote(rs.getString(5));
				bean.setUpdatetime(rs.getTimestamp(6));
				bean.setUpdateuser(rs.getString(7));
				bean.setCreatetime(rs.getTimestamp(8));
				bean.setCreateuser(rs.getString(9));
				bean.setNeedAlert(rs.getString(10));
				bean.setLastReceiveNo(rs.getString(11)==null?"":rs.getString(11));
				bean.setPorjDate(rs.getString(12)==null?"":rs.getString(12));
				bean.setIsSysDate(rs.getString(13));
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
	public List<Project> getSearchResult(String investor,String IDNO,String investNo,String cnName,String state,String from,String to,String alert){
		List<Project> result = new ArrayList<Project>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT p.* FROM Project p inner join (SELECT investNo.investNo,investor.IDNO ");
		sb.append("FROM [ISAM].[dbo].[CDataICMFN405] data,CDataInvestment investNo,CDataInvestor investor ");
		sb.append("where investNo.INVESTMENT_NO=data.INVEST_NO and investor.oIDNO=data.ID_NO and LEN(ID_NO)=8 ");
		sb.append("and (RESP_DATE >=ISNULL(?,RESP_DATE) and RESP_DATE <=ISNULL(?,RESP_DATE) ");
		if(from==null||to==null){
			sb.append(" or RESP_DATE is null)");
		}else{
			sb.append(")");
		}
		sb.append("and investNo.investNo in (SELECT investNo FROM CDataInvestment where INVESTMENT_NO like ? and COMP_CHTNAME like ?) ");
		sb.append("and investor.IDNO in( SELECT IDNO FROM CDataInvestor where INVESTOR_CHTNAME like ? and (oIDNO like ? or IDNO like ?)) ");
		sb.append("group by investNo.investNo,investor.IDNO) src on src.IDNO=p.IDNO and src.investNo=p.investNo ");
		sb.append("where needAlert=isnull(?,needAlert) and state in(").append(state).append(")");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, from);
			stmt.setString(2, to);
			stmt.setString(3, investNo);
			stmt.setString(4, cnName);
			stmt.setString(5, investor);
			stmt.setString(6, IDNO);
			stmt.setString(7, IDNO);
			stmt.setString(8, alert);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Project bean= new Project();
				bean.setSerno(rs.getInt(1));
				bean.setInvestNo(rs.getString(2).trim());
				bean.setIDNO(rs.getString(3).trim());
				bean.setState(rs.getString(4));
				bean.setNote(rs.getString(5));
				bean.setUpdatetime(rs.getTimestamp(6));
				bean.setUpdateuser(rs.getString(7));
				bean.setCreatetime(rs.getTimestamp(8));
				bean.setCreateuser(rs.getString(9));
				bean.setNeedAlert(rs.getString(10));
				bean.setLastReceiveNo(rs.getString(11)==null?"":rs.getString(11));
				bean.setPorjDate(rs.getString(12)==null?"":rs.getString(12));
				bean.setIsSysDate(rs.getString(13));
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
	public int update(Project bean) {
		int result = 0;
//		String forpstmt = "UPDATE Project SET state=?,note=?,updatetime=?,updateuser=? WHERE investNo in (select investNo FROM Project where serno=?)";
		String forpstmt = "UPDATE Project SET state=?,note=?,updatetime=?,updateuser=?,porjDate=?,IsSysDate=? where serno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getState());
			pstmt.setString(2, bean.getNote());
			pstmt.setTimestamp(3, bean.getUpdatetime());
			pstmt.setString(4, bean.getUpdateuser());
			pstmt.setString(5, bean.getPorjDate());
			pstmt.setString(6, bean.getIsSysDate());
			pstmt.setInt(7, bean.getSerno());
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
	public int updateState(String state,String updateuser,String serno) {
		int result = 0;
//		String forpstmt = "update Project set state=? ,updatetime =GETDATE() , updateuser = ? where investNo in (select investNo FROM Project where serno in ("+serno+"))";
		String forpstmt = "update Project set state=? ,updatetime =GETDATE() , updateuser = ? where serno in ("+serno+")";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, state);
			pstmt.setString(2, updateuser);
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
	public int updateNeedAlert(int serno,String needAlert) {
		int result = 0;
		String forpstmt = "update Project set needAlert=? where serno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, needAlert);
			pstmt.setInt(2, serno);
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
}
