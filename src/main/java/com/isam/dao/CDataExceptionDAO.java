package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.isam.helper.SQL;

public class CDataExceptionDAO {
	public static ArrayList<String> listBySerno(int serno) {
		SQL sqltool = new SQL();
		ArrayList<String> result = new ArrayList<String>();
		
		try {
			PreparedStatement statement = sqltool.prepare("SELECT investNo FROM CDataException WHERE investNo IN ("
				+ "SELECT investNo FROM CommitXInvestNo WHERE serno = ? ) ");
			statement.setInt(1, serno);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				result.add(rs.getString("INVESTOR_CHTNAME"));
			}
			rs.close();
			statement.close();
		}catch (Exception e) {
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
	
	public static boolean insert(String investNo, String name, String IDNO, int serno) {
		SQL sqltool = new SQL();
		
		try {
			String note_name = IDNO;
			PreparedStatement statement = sqltool.prepare("SELECT INVESTOR_CHTNAME FROM CDataInvestor WHERE IDNO = ? ");
			statement.setString(1, IDNO);
			ResultSet rs = statement.executeQuery();
			if(rs.next()) {
				note_name = rs.getString("INVESTOR_CHTNAME");
			}
			rs.close();
			statement.close();
			
			statement = sqltool.prepare("INSERT INTO CDataException (investNo, name, note) VALUES (?, ?, ?) ");
			statement.setString(1, investNo);
			statement.setString(2, name);
			statement.setString(3, new SimpleDateFormat("yyyyMMdd").format(new Date()) + "," + note_name);
			statement.executeUpdate();
			statement.close();
			
			statement = sqltool.prepare("INSERT INTO CommitXInvestNo (serno, investNo) VALUES (?, ?) ");
			statement.setInt(1, serno);
			statement.setString(2, investNo);
			statement.executeUpdate();
			statement.close();
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally{
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	public static boolean delete(String investNo, int serno) {
		SQL sqltool = new SQL();
		int result = 0;
		
		try {
			PreparedStatement statement = sqltool.prepare("DELETE FROM CommitXInvestNo WHERE serno = ? AND investNo = ? ");
			statement.setInt(1, serno);
			statement.setString(2, investNo);
			statement.executeUpdate();
			statement.close();
			
			statement = sqltool.prepare("DELETE FROM CDataException WHERE investNo NOT IN ("
					+ "SELECT investNo FROM CommitXInvestNo) ");
			result = statement.executeUpdate();
			statement.close();
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result > 0;
	}
}
