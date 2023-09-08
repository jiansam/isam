package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.helper.SQL;

public class WebFolderAuthority {
	private List<String> urlRestrict= new ArrayList<String>();
	
	public WebFolderAuthority(){
		urlRestrict = this.getUrlList();
	}
	
	public List<String> checkList(List<String> passList) {
		List<String> resultlist = new ArrayList<String>();
		List<String> list = new ArrayList<String>();
		list.addAll(urlRestrict);
		for (int i=0;i<list.size();i++) {
			String str =list.get(i);
			if(!passList.contains(str)){
				resultlist.add(str);
//				System.out.println("remove:"+list.get(i));
			}
		}
		return resultlist;
	}
	public List<String> getUrlList(){
		List<String> list = new ArrayList<String>();
		SQL sqltool = new SQL();
		String forpstmt = "SELECT url FROM WebFolderUrl";
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString(1));
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
	public List<String> getCodeList(){
		List<String> list = new ArrayList<String>();
		SQL sqltool = new SQL();
		String forpstmt = "SELECT code FROM WebFolderUrl";
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString(1));
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
	public List<String> getURLByIdMember(String idMember){
		List<String> list= new ArrayList<String>();
		SQL sqltool = new SQL();
		String forpstmt = "SELECT distinct url FROM WebFolderUrl where code in (select code from WebFolderAuthority where idMember=?)";
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, idMember);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString(1));
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
	public Map<String,String> getURLCodeMap(){
		Map<String,String> map= new HashMap<String,String>();
		SQL sqltool = new SQL();
		String forpstmt = "SELECT code, url FROM WebFolderUrl";
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString(2),rs.getString(1));
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
		return map;
	}
	public Map<String,String> getCodeURLByIdMember(String idMember){
		Map<String,String> map= new HashMap<String,String>();
		SQL sqltool = new SQL();
		String forpstmt = "SELECT code, url FROM WebFolderUrl where code in (select code from WebFolderAuthority where idMember=?)";
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, idMember);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString(1),rs.getString(2));
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
		return map;
	}
	public List<String> getMenuItemByIdMember(String idMember){
		 List<String> list = new ArrayList<String>();
		SQL sqltool = new SQL();
		String forpstmt = "SELECT code FROM WebFolderUrl where code in (select code from WebFolderAuthority where idMember=?)";
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, idMember);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString(1));
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
	public boolean checkExists(String idMember){
		boolean result=false;
		SQL sqltool = new SQL();
		String forpstmt = "SELECT count(*) FROM WebFolderAuthority where idMember=?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, idMember);
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				int count= rs.getInt(1);
				if(count>0){
					result=true;
				}
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
	public int insert(String idMember,String code) {
		int result = 0;
		String forpstmt = "insert into WebFolderAuthority(idMember,code) values (?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, idMember);
			pstmt.setString(2, code);
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
	public int delete(String idMember) {
		int result = 0;
		if(checkExists(idMember)){
			String forpstmt = "delete from WebFolderAuthority where idMember=?";
			SQL sqltool = new SQL();
			try {
				PreparedStatement pstmt = sqltool.prepare(forpstmt);
				pstmt.setString(1, idMember);
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
		}
		return result;
	}
}
