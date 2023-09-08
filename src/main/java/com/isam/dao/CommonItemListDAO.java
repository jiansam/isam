package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.CommonItemList;
import com.isam.helper.SQL;

public class CommonItemListDAO {
	
	public int select(String idno){
		int result= 0;
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommonItemList where idno=? and enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, idno);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result=rs.getInt("idno");
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
	
	public ArrayList<CommonItemList> get()
	{
		ArrayList<CommonItemList> result = null;
		SQL sqltools = new SQL();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = sqltools.prepare("SELECT * FROM CommonItemList where enable='1' ORDER BY type, idno");
			rs = pstmt.executeQuery();
			result = new ArrayList<>();
			while (rs.next()) {
				CommonItemList bean = new CommonItemList();
				bean.setIdno(rs.getString("idno"));
				bean.setCname(rs.getString("cname"));
				bean.setType(rs.getString("type"));
				bean.setEnable(rs.getString("enable"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				result.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				sqltools.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public Map<String,List<CommonItemList>> getCommonItemMap(){
		Map<String,List<CommonItemList>> result= new HashMap<String, List<CommonItemList>>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommonItemList where enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				List<CommonItemList> temp;
				String type=rs.getString("type");
				if(result.containsKey(type)){
					temp=result.get(type);
				}else{
					temp= new ArrayList<CommonItemList>();
				}
				CommonItemList bean=new CommonItemList();
				bean.setIdno(rs.getString("idno"));
				bean.setCname(rs.getString("cname"));
				bean.setType(rs.getString("type"));
				bean.setEnable(rs.getString("enable"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				temp.add(bean);
				result.put(type, temp);
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
	
	public int insert(CommonItemList bean) {
		int result = 0;
		String forStmt = "insert into CommonItemList Output Inserted.idno values(?,?,1,getdate(),?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, bean.getCname());
			pstmt.setString(2, bean.getType());
			pstmt.setString(3, bean.getUpdateuser());
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
	public int update(CommonItemList bean) {
		int result = 0;
		String forpstmt = "UPDATE CommonItemList SET cname=?,updatetime=getdate(),updateuser=? WHERE idno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getCname());
			pstmt.setString(2, bean.getUpdateuser());
			pstmt.setString(3, bean.getIdno());
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
	public int unable(String idno) {
		int result = 0;
		String forpstmt = "UPDATE CommonItemList SET enable='0' WHERE idno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, idno);
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
