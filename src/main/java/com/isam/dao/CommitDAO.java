package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.Commit;
import com.isam.helper.SQL;

public class CommitDAO {
	
	public Map<String,String> getInvestNOList(String IDNO){
		Map<String,String> result= new HashMap<String,String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT b.investNo,c.title FROM [Commit] a,CommitXInvestNo b,CommitRestrainType c ");
		sb.append("where a.enable='1' and a.IDNO=? and a.serno=b.serno and c.restrainType=a.type ");
		sb.append("and c.enable='1' and c.level='1' group by investNo,title");
		String forStmt = sb.toString();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				String investNo=rs.getString(1);
				String title=rs.getString(2);
				if(result.containsKey(investNo)){
					title=result.get(investNo)+"、"+title;
				}
				result.put(investNo, title);
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
	public Map<String,List<String>> getReceviceNOList(String IDNO){
		Map<String,List<String>> result= new HashMap<String,List<String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT b.respDate,b.receiveNo,b.note,c.title FROM [Commit] a,CommitXReceiveNo b,CommitRestrainType c ");
		sb.append("where a.enable='1' and a.IDNO=? and a.serno=b.serno and c.restrainType=a.type ");
		sb.append("and c.enable='1' and c.level='1' group by b.respDate,b.receiveNo,b.note,c.title");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				String ReceviceNO=rs.getString(2);
				String note=rs.getString(3);
				String respDate=rs.getString(1);
				String title=rs.getString(4);
				List<String> list;
				if(result.containsKey(ReceviceNO)){
					list = result.get(ReceviceNO);
					title=list.get(3)+"、"+title;
					list.remove(3);
					list.add(title);
				}else{
					list = new ArrayList<String>();
					list.add(respDate);
					list.add(ReceviceNO);
					list.add(note);
					list.add(title);
				}
				result.put(ReceviceNO, list);
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
	public int checkYearRange(String IDNO,String type,String startYear,String endYear){
		int result=0;
		SQL sqltool = new SQL();
		String forStmt = "SELECT count(Serno) FROM [Commit] WHERE IDNO=? and type=? and startYear<=? and endYear>=? and enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			stmt.setString(2, type);
			stmt.setString(3, endYear);
			stmt.setString(4, startYear);
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
	public int checkYearRange(String IDNO,String type,String startYear,String endYear,int serno){
		int result=0;
		SQL sqltool = new SQL();
		String forStmt = "SELECT count(Serno) FROM [Commit] WHERE IDNO=? and type=? and startYear<=? and endYear>=? and serno!=? and enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			stmt.setString(2, type);
			stmt.setString(3, endYear);
			stmt.setString(4, startYear);
			stmt.setInt(5, serno);
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
	public int getSerno(String IDNO,String type,String startYear,String endYear){
		int result=-1;
		SQL sqltool = new SQL();
		String forStmt = "SELECT Serno FROM [Commit] WHERE IDNO=? and type=? and startYear=? and endYear=? and enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			stmt.setString(2, type);
			stmt.setString(3, startYear);
			stmt.setString(4, endYear);
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
	public List<Commit> selectByIDNO(String IDNO){
		List<Commit> result = new ArrayList<Commit>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM [Commit] WHERE IDNO = ? and enable='1' ORDER BY type";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Commit bean= new Commit();
				bean.setSerno(rs.getInt(1));
				bean.setIDNO(rs.getString(2));
				bean.setType(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setRepType(rs.getString(5));
				bean.setNote(rs.getString(6));
				bean.setStartYear(rs.getString(7));
				bean.setEndYear(rs.getString(8));
				bean.setUpdatetime(rs.getTimestamp(9));
				bean.setUpdateuser(rs.getString(10));
				bean.setCreatetime(rs.getTimestamp(11));
				bean.setCreateuser(rs.getString(12));
				bean.setEnable(rs.getString(13));
				bean.setNeedAlert(rs.getString(14));
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
	public String getMaxMinYear(String IDNO){
		String result = "";
		SQL sqltool = new SQL();
		String forStmt = "SELECT min(startYear),max(endYear) FROM [ISAM].[dbo].[Commit] where type='01' and IDNO=?";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				if(rs.getString(1)!=null){
					result=rs.getString(1)+" ~ "+rs.getString(2);
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
	public List<Commit> select(String serno){
		List<Commit> result = new ArrayList<Commit>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM [Commit] WHERE serno = isnull(?,serno) and enable='1' ORDER BY serno";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Commit bean= new Commit();
				bean.setSerno(rs.getInt(1));
				bean.setIDNO(rs.getString(2));
				bean.setType(rs.getString(3));
				bean.setState(rs.getString(4));
				bean.setRepType(rs.getString(5));
				bean.setNote(rs.getString(6));
				bean.setStartYear(rs.getString(7));
				bean.setEndYear(rs.getString(8));
				bean.setUpdatetime(rs.getTimestamp(9));
				bean.setUpdateuser(rs.getString(10));
				bean.setCreatetime(rs.getTimestamp(11));
				bean.setCreateuser(rs.getString(12));
				bean.setEnable(rs.getString(13));
				bean.setNeedAlert(rs.getString(14));
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
	public int insert(Commit bean) {
		int result = 0;
		String forpstmt = "insert into [Commit] values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getIDNO());
			pstmt.setString(2, bean.getType());
			pstmt.setString(3, bean.getState());
			pstmt.setString(4, bean.getRepType());
			pstmt.setString(5, bean.getNote());
			pstmt.setString(6, bean.getStartYear());
			pstmt.setString(7, bean.getEndYear());
			pstmt.setTimestamp(8, bean.getUpdatetime());
			pstmt.setString(9, bean.getUpdateuser());
			pstmt.setTimestamp(10, bean.getCreatetime());
			pstmt.setString(11, bean.getCreateuser());
			pstmt.setString(12, bean.getEnable());
			pstmt.setString(13, bean.getNeedAlert());
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
	public int update(Commit bean) {
		int result = 0;
		String forpstmt = "UPDATE [Commit] SET state=?,repType=?,note=?,startYear=?,endYear=?,updatetime=?,updateuser=? WHERE serno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getState());
			pstmt.setString(2, bean.getRepType());
			pstmt.setString(3, bean.getNote());
			pstmt.setString(4, bean.getStartYear());
			pstmt.setString(5, bean.getEndYear());
			pstmt.setTimestamp(6, bean.getUpdatetime());
			pstmt.setString(7, bean.getUpdateuser());
			pstmt.setInt(8, bean.getSerno());
			result=pstmt.executeUpdate();
//			System.out.println(result);
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
	public int unable(String serno) {
		int result = 0;
		String forpstmt = "UPDATE [Commit] SET enable='0' WHERE serno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, serno);
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
	public int updateNeedAlert(String serno,String needAlert) {
		int result = 0;
		String forpstmt = "update [Commit] set needAlert=? where serno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, needAlert);
			pstmt.setString(2, serno);
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
