package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.isam.bean.Investment;
import com.isam.helper.SQL;

public class InvestmentDAO {
	public List<Investment> select(){
		List<Investment> result = new ArrayList<Investment>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CDataInvestment union all SELECT investNo,investNo,name FROM CDataException";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Investment bean= new Investment();
				bean.setInvestNo(rs.getString(1).trim());
				bean.setoInvestNo(rs.getString(2).trim());
				bean.setCompName(rs.getString(3));
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
}
