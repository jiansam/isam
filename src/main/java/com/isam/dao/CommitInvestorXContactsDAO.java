package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.CommitInvestorXContacts;
import com.isam.helper.SQL;

public class CommitInvestorXContactsDAO {
	
	public List<CommitInvestorXContacts> select(String IDNO){
		List<CommitInvestorXContacts> result = new ArrayList<CommitInvestorXContacts>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommitInvestorXContacts WHERE IDNO = isnull(?,IDNO)";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				CommitInvestorXContacts bean= new CommitInvestorXContacts();
				bean.setCid(rs.getString("cid"));
				bean.setIDNO(rs.getString("IDNO"));
				bean.setContact(rs.getString("contact"));
				bean.setTel(rs.getString("tel"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
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
	public CommitInvestorXContacts selectByCID(String cid){
		CommitInvestorXContacts bean = new CommitInvestorXContacts();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommitInvestorXContacts WHERE cid =? ";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, cid);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean.setCid(rs.getString("cid"));
				bean.setIDNO(rs.getString("IDNO"));
				bean.setContact(rs.getString("contact"));
				bean.setTel(rs.getString("tel"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
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
	public Map<String,String> getReceiveNoStr(String IDNO){
		Map<String,String> result = new HashMap<String,String>();
		StringBuilder sb=new StringBuilder();
		sb.append("SELECT cid,(STUFF((select '、'+receiveNo from CommitInvestorXContactsXReceiveNo t2 where t2.cid=t1.cid ");
		sb.append("for XML PATH('')),1,1,'')) receiveNoStr from CommitInvestorXContactsXReceiveNo t1 ");
		sb.append("where cid in (select cid from CommitInvestorXContacts where idno=? ) and receiveNo in (");
		sb.append("SELECT receiveNo FROM CommitXReceiveNo where serno in (select serno from [Commit] ");
		sb.append("where enable='1' and idno=?)) group by cid");
		SQL sqltool = new SQL();
		String forStmt =sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			stmt.setString(2, IDNO);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.put(rs.getString("cid"),rs.getString("receiveNoStr"));
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
	
	public String insert(CommitInvestorXContacts bean) {
		String result = "";
		String forpstmt = "insert into CommitInvestorXContacts OUTPUT inserted.cid values(?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getIDNO());
			pstmt.setString(2, bean.getContact());
			pstmt.setString(3, bean.getTel());
			pstmt.setTimestamp(4, bean.getUpdatetime());
			pstmt.setString(5, bean.getUpdateuser());
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				result=rs.getString(1);
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
	public int update(CommitInvestorXContacts bean) {
		int result = 0;
		String forpstmt = "UPDATE CommitInvestorXContacts SET contact=?,tel=?,updatetime=?,updateuser=? WHERE cid=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			int c=1;
			pstmt.setString(c++, bean.getContact());
			pstmt.setString(c++, bean.getTel());
			pstmt.setTimestamp(c++, bean.getUpdatetime());
			pstmt.setString(c++, bean.getUpdateuser());
			pstmt.setString(c++, bean.getCid());
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
	public int delete(String cid) {
		int result = 0;
		String forpstmt = "delete CommitInvestorXContacts WHERE cid=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, cid);
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
