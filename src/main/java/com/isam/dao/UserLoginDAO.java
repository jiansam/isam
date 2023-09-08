package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.isam.bean.LoginRecord;
import com.isam.bean.UserMember;
import com.isam.helper.SQL;

public class UserLoginDAO {
	
	public List<UserMember> select(String idMember,String enable){
		UserMember user= null;
		List<UserMember> list = new ArrayList<UserMember>();
		SQL sqltool = new SQL();
		String forpstmt = "select * from UserMember where idMember=isnull(?,idMember) and enable=isnull(?,enable)";
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, idMember);
			pstmt.setString(2, enable);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				user= new UserMember();
				user.setIdMember(rs.getString("idMember"));
				user.setUserPwd(rs.getString("userPwd"));
				user.setCompany(rs.getString("company"));
				user.setUsername(rs.getString("username"));
				user.setUserEmail(rs.getString("userEmail"));
				user.setGroupId(rs.getString("groupId"));
				user.setIdEditor(rs.getString("idEditor"));
				user.setIdCreatetor(rs.getString("idCreatetor"));
				user.setCreatetime(rs.getTimestamp("createtime"));
				user.setUpdtime(rs.getTimestamp("updtime"));
				user.setEnable(rs.getString("enable"));
				list.add(user);
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
		return list;
	}
	public int insert(UserMember bean) {
		int result = 0;
		String forpstmt = "insert into UserMember(idMember,userPwd,company,username,userEmail,groupId,idEditor,idCreatetor,createtime,updtime,enable) values (?,?,?,?,?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getIdMember());
			pstmt.setString(2, bean.getUserPwd());
			pstmt.setString(3, bean.getCompany());
			pstmt.setString(4, bean.getUsername());
			pstmt.setString(5, bean.getUserEmail());
			pstmt.setString(6, bean.getGroupId());
			pstmt.setString(7, bean.getIdEditor());
			pstmt.setString(8, bean.getIdEditor());
			pstmt.setTimestamp(9, bean.getCreatetime());
			pstmt.setTimestamp(10, bean.getUpdtime());
			pstmt.setString(11, bean.getEnable());
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
	public int update(UserMember bean) {
		int result = 0;
		String forpstmt = "update UserMember set userPwd=?, company=?,username=?,userEmail=?,groupId=?,idEditor=?,idCreatetor=?,updtime=?,enable=? where idMember=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getUserPwd());
			pstmt.setString(2, bean.getCompany());
			pstmt.setString(3, bean.getUsername());
			pstmt.setString(4, bean.getUserEmail());
			pstmt.setString(5, bean.getGroupId());
			pstmt.setString(6, bean.getIdEditor());
			pstmt.setString(7, bean.getIdCreatetor());
			pstmt.setTimestamp(8, bean.getUpdtime());
			pstmt.setString(9, bean.getEnable());
			pstmt.setString(10, bean.getIdMember());
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
	public boolean IsIdMemberExists(String idMember,String groupId,String enable){
		boolean result= false;
		SQL sqltool = new SQL();
		String forpstmt = "select idMember from UserMember where idMember=? and groupId=isnull(?,groupId) and enable=isnull(?,enable)";
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, idMember);
			pstmt.setString(2, groupId);
			pstmt.setString(3, enable);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				 result= true;
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
	public int insert(LoginRecord bean) {
		int result = 0;
		String forpstmt = "insert into LoginRecord(idMember,loginTime,loginResult,loginIP) values (?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getIdMember());
			pstmt.setTimestamp(2, bean.getLoginTime());
			pstmt.setString(3, bean.getLoginResult());
			pstmt.setString(4, bean.getLoginIP());
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
	public List<LoginRecord> selectTopTen (String idMember) {
		List<LoginRecord> result = new ArrayList<LoginRecord>();
		String forpstmt = "SELECT TOP 10 [loginTime],[loginResult],[idMember] FROM [ISAM].[dbo].[LoginRecord] where idMember=isNull(?,idMember) order by loginTime desc";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1,idMember);
			ResultSet rs=pstmt.executeQuery();	
			while(rs.next()){
				LoginRecord bean = new LoginRecord();
				bean.setIdMember(rs.getString(3));
				bean.setLoginTime(rs.getTimestamp(1));
				bean.setLoginResult(rs.getString(2));
				result.add(bean);
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
