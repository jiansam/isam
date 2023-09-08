package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.isam.helper.SQL;

public class ProjectOnlineSrcDAO {
	
	public List<List<String>> getNotMatch(){
		SQL sqltool = new SQL();
		StringBuilder sb=new StringBuilder();
		sb.append("SELECT A.no, A.investor_id, A.investor_chtname, A.investment_no, A.comp_chtname, A.apply_period, B.error_reason "
				+ "FROM ProjectOnlineSrc A "
				+ "LEFT OUTER JOIN VW_ProjectOnlineSrcError B ON A.no = B.no "
				+ "WHERE A.serno IS NULL ");
		sb.append("AND NOT EXISTS (SELECT * FROM ProjectOnlineSrcEX C WHERE A.investor_id = C.investor_id ");
		sb.append("AND A.investor_chtname = C.investor_chtname ");
		sb.append("AND A.investment_no = C.investment_no ");
		sb.append("AND A.comp_chtname = C.comp_chtname ");
		sb.append("AND A.apply_period = C.apply_period) ");
		
		String forStmt = sb.toString();
		sb.setLength(0);
		List<List<String>>  result=new ArrayList<List<String>>();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				List<String> sub = new ArrayList<String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					sub.add(rs.getString(meta.getColumnName(i)));
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
	public void insert(String no) {
		StringBuilder sb=new StringBuilder();
		sb.append("insert into ProjectOnlineSrcEX SELECT investor_id,investor_chtname,investment_no");
		sb.append(",comp_chtname,apply_period FROM ProjectOnlineSrc where no in (");
		String[] temp =no.split(",");
		for (int i = 0; i < temp.length; i++) {
			if(i!=0){
				sb.append(",");
			}
			sb.append("?");
		}
		sb.append(")");
		String forpstmt = sb.toString();
		sb.setLength(0);
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for (int i = 0; i < temp.length; i++) {
				pstmt.setString(i+1,temp[i]);
			}
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
}
