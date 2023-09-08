package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.isam.bean.Investor;
import com.isam.helper.SQL;

public class InvestorDAO {
	public List<Investor> select(){
		List<Investor> result = new ArrayList<Investor>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CDataInvestor";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Investor bean= new Investor();
				bean.setIDNO(rs.getString(1).trim());
				bean.setoIDNO(rs.getString(2).trim());
				bean.setInvestor(rs.getString(3));
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
	public List<List<String>> select(String IDNO,String investor){
		List<List<String>> result = new ArrayList<List<String>>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT c.IDNO,c.INVESTOR_CHTNAME,isnull(i.IDNO,'N') FROM CDataInvestor c left join CommitInvestor i on c.IDNO=i.IDNO where (c.oIDNO like ? or c.IDNO like ?) and c.INVESTOR_CHTNAME like ? and len(c.IDNO)=8";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			stmt.setString(2, IDNO);
			stmt.setString(3, investor);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list= new ArrayList<String>();
				list.add(rs.getString(1).trim());
				list.add(rs.getString(2).trim());
				list.add(rs.getString(3).trim());
				result.add(list);
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
}
