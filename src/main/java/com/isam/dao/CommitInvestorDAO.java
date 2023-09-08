package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.isam.bean.CommitInvestor;
import com.isam.helper.SQL;

public class CommitInvestorDAO {
	
	
	public List<CommitInvestor> getSearchResult(String investor,String IDNO,String type,String from,String to,String needAlert){
		List<CommitInvestor> result = new ArrayList<CommitInvestor>();
		SQL sqltool = new SQL();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT c.* FROM CommitInvestor c where c.IDNO in ( SELECT IDNO FROM CDataInvestor where INVESTOR_CHTNAME like ? ");
		sb.append("and (oIDNO like ? or IDNO like ?)) ");
		if(type.length()!=0){
			sb.append("and ( c.IDNO in ( select IDNO from [Commit] where type in (");
			sb.append(type).append(") and enable='1' group by IDNO) ");
			if(type.indexOf("05")!=-1){
				sb.append("or c.IDNO in (SELECT [IDNO] FROM CommitXOffice group by IDNO)");
			}
			sb.append(") ");
		}
		sb.append("and needAlert=isnull(?,needAlert) and enable='1' ");
		if(from!=null||to!=null){
			sb.append("and c.IDNO in( SELECT [IDNO] FROM [Commit] where serno in ( SELECT [serno] FROM [CommitXReceiveNo]");
			sb.append(" where respDate >=ISNULL(?,respDate) and respDate <=ISNULL(?,respDate)))");
		}
		String forStmt = sb.toString();
		sb.setLength(0);
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investor);
			stmt.setString(2, IDNO);
			stmt.setString(3, IDNO);
			stmt.setString(4, needAlert);
			if(from!=null||to!=null){
				stmt.setString(5, from);
				stmt.setString(6, to);
			}
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				CommitInvestor bean= new CommitInvestor();
				bean.setIDNO(rs.getString(1).trim());
				bean.setNote(rs.getString(2));
				bean.setUpdatetime(rs.getTimestamp(3));
				bean.setUpdateuser(rs.getString(4));
				bean.setCreatetime(rs.getTimestamp(5));
				bean.setCreateuser(rs.getString(6));
				bean.setEnable(rs.getString(7));
				bean.setNeedAlert(rs.getString(8));
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
	public List<CommitInvestor> select(String IDNO,String needAlert){
		List<CommitInvestor> result = new ArrayList<CommitInvestor>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommitInvestor WHERE IDNO = isnull(?,IDNO) and needAlert = isnull(?,needAlert) and enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			stmt.setString(2, needAlert);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				CommitInvestor bean= new CommitInvestor();
				bean.setIDNO(rs.getString(1).trim());
				bean.setNote(rs.getString(2));
				bean.setUpdatetime(rs.getTimestamp(3));
				bean.setUpdateuser(rs.getString(4));
				bean.setCreatetime(rs.getTimestamp(5));
				bean.setCreateuser(rs.getString(6));
				bean.setEnable(rs.getString(7));
				bean.setNeedAlert(rs.getString(8));
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
	
	public int insert(CommitInvestor bean) {
		int result = 0;
		String forpstmt = "insert into CommitInvestor values(?,?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getIDNO());
			pstmt.setString(2, bean.getNote());
			pstmt.setTimestamp(3, bean.getUpdatetime());
			pstmt.setString(4, bean.getUpdateuser());
			pstmt.setTimestamp(5, bean.getCreatetime());
			pstmt.setString(6, bean.getCreateuser());
			pstmt.setString(7, bean.getEnable());
			pstmt.setString(8, bean.getNeedAlert());
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
	public int update(CommitInvestor bean) {
		int result = 0;
		String forpstmt = "UPDATE CommitInvestor SET note=?,updatetime=?,updateuser=? WHERE IDNO=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getNote());
			pstmt.setTimestamp(2, bean.getUpdatetime());
			pstmt.setString(3, bean.getUpdateuser());
			pstmt.setString(4, bean.getIDNO());
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
	
	public int updateNeedAlert(String idno,String needAlert) {
		int result = 0;
		String forpstmt = "update CommitInvestor set needAlert=? where idno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, needAlert);
			pstmt.setString(2, idno);
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
