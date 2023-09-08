package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.SubCommit;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class SubCommitDAO {
	
	public List<Map<String,String>> getInvestNOXSubList(String serno,String type){
		 List<Map<String,String>> result= new ArrayList<Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select c.serno,c.investNo,c.type,ISNULL(sc.state,c.state) state,c.repType ");
		sb.append(",ISNULL(sc.startYear,c.startYear) startYear,ISNULL(sc.endYear,c.endYear) endYear,subserno from (");
		sb.append("SELECT c.serno,ci.investNo,type,state,repType,startYear,endYear ");
		sb.append("FROM [Commit] c,CommitXInvestNo ci where c.serno=ci.serno and c.enable='1' and type=? and c.serno=?");
		sb.append(") c left join (select * from SubCommit where enable='1')sc on c.serno=sc.serno and sc.investNo=c.investNo");
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, type);
			stmt.setString(2, serno);
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
	public int checkYearRange(String serno,String investNo,String type,String startYear,String endYear){
		int result=0;
		SQL sqltool = new SQL();
		String forStmt = "SELECT count(subserno) FROM [SubCommit] WHERE serno=? and investNo=? and type=? and startYear<=? and endYear>=? and enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			stmt.setString(2, investNo);
			stmt.setString(3, type);
			stmt.setString(4, endYear);
			stmt.setString(5, startYear);
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
	public int checkYearRange(String subserno,String serno,String investNo,String type,String startYear,String endYear){
		int result=0;
		SQL sqltool = new SQL();
		String forStmt = "SELECT count(subserno) FROM [SubCommit] WHERE serno=? and investNo=? and type=? and startYear<=? and endYear>=? and subserno!=? and enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			stmt.setString(2, investNo);
			stmt.setString(3, type);
			stmt.setString(4, endYear);
			stmt.setString(5, startYear);
			stmt.setString(6, subserno);
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
	public SubCommit select(String subserno){
		SubCommit bean= new SubCommit();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM SubCommit where enable='1' and subserno=?";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, subserno);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean.setSubserno(rs.getString("subserno"));
				bean.setSerno(rs.getString("serno"));
				bean.setInvestNo(rs.getString("investNo"));
				bean.setType(rs.getString("type"));
				bean.setState(rs.getString("state"));
				bean.setRepType(rs.getString("repType"));
				bean.setNote(rs.getString("note"));
				bean.setStartYear(rs.getString("startYear"));
				bean.setEndYear(rs.getString("endYear"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setEnable(DataUtil.nulltoempty(rs.getString("enable")));
				bean.setNeedAlert(DataUtil.nulltoempty(rs.getString("needAlert")));
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
	public SubCommit select(String serno,String investNo,String subserno){
		SubCommit bean= new SubCommit();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select sc.subserno,c.serno,c.investNo,c.type,ISNULL(sc.state,c.state) state,c.repType,sc.note ");
		sb.append(",ISNULL(sc.startYear,c.startYear) startYear,ISNULL(sc.endYear,c.endYear) endYear ");
		sb.append(",sc.updatetime,sc.updateuser,sc.createtime,sc.createuser,sc.enable,sc.needAlert from (");
		sb.append("SELECT c.serno,ci.investNo,type,state,repType,startYear,endYear FROM [Commit] c,CommitXInvestNo ci ");
		sb.append("where c.serno=ci.serno and c.enable='1' and c.serno=? and ci.investNo=?)c left join (");
//		sb.append("where c.serno=ci.serno and c.enable='1' and type='01' and c.serno=? and ci.investNo=?)c left join (");
		sb.append("select * from SubCommit where enable='1' and subserno=isnull(?,subserno))sc on c.serno=sc.serno");
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			stmt.setString(2, investNo);
			stmt.setString(3, subserno);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean.setSubserno(DataUtil.nulltoempty(rs.getString("subserno")));
				bean.setSerno(DataUtil.nulltoempty(rs.getString("serno")));
				bean.setInvestNo(rs.getString("investNo"));
				bean.setType(rs.getString("type"));
				bean.setState(rs.getString("state"));
				bean.setRepType(rs.getString("repType"));
				bean.setNote(DataUtil.nulltoempty(rs.getString("note")));
				bean.setStartYear(rs.getString("startYear"));
				bean.setEndYear(rs.getString("endYear"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setEnable(DataUtil.nulltoempty(rs.getString("enable")));
				bean.setNeedAlert(DataUtil.nulltoempty(rs.getString("needAlert")));
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
	public int insert(SubCommit bean) {
		int result = -1;
		String forpstmt = "insert into [SubCommit] output INSERTED.subserno values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getSerno());
			pstmt.setString(2, bean.getInvestNo());
			pstmt.setString(3, bean.getType());
			pstmt.setString(4, bean.getState());
			pstmt.setString(5, bean.getRepType());
			pstmt.setString(6, bean.getNote());
			pstmt.setString(7, bean.getStartYear());
			pstmt.setString(8, bean.getEndYear());
			pstmt.setTimestamp(9, bean.getUpdatetime());
			pstmt.setString(10, bean.getUpdateuser());
			pstmt.setTimestamp(11, bean.getCreatetime());
			pstmt.setString(12, bean.getCreateuser());
			pstmt.setString(13, bean.getEnable());
			pstmt.setString(14, bean.getNeedAlert());
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
	public int update(SubCommit bean) {
		int result = 0;
		String forpstmt = "UPDATE [SubCommit] SET State=?,repType=?,note=?,startYear=?,endYear=?,updatetime=?,updateuser=? WHERE subserno=?";
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
			pstmt.setString(8, bean.getSubserno());
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
	public int unable(String subserno) {
		int result = 0;
		String forpstmt = "UPDATE [SubCommit] SET enable='0' WHERE subserno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, subserno);
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
	public int unableAllSub(String serno) {
		int result = 0;
		String forpstmt = "UPDATE [SubCommit] SET enable='0' WHERE serno=?";
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
	public void deleteAllSub(String serno) {
		String forpstmt = "delete [SubCommit] WHERE serno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, serno);
			pstmt.executeUpdate();
			forpstmt = "delete [SubCommitDetail] WHERE subserno in (SELECT [subserno] FROM [SubCommit] where serno=? and enable='1')";
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
	public int updateNeedAlert(String subserno,String needAlert) {
		int result = 0;
		String forpstmt = "update [SubCommit] set needAlert=? where subserno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, needAlert);
			pstmt.setString(2, subserno);
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
