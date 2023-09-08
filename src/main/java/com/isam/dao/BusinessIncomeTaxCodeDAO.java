package com.isam.dao;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.isam.bean.BusinessIncomeTaxCode;
import com.isam.helper.SQL;

public class BusinessIncomeTaxCodeDAO {
	public static ArrayList<BusinessIncomeTaxCode> list(){
		ArrayList<BusinessIncomeTaxCode> result = new ArrayList<BusinessIncomeTaxCode>();
		SQL sqltool = new SQL();
		
		try{
			ResultSet rs = sqltool.query("SELECT * FROM BusinessIncomeTaxCode ");
			while(rs.next()) {
				BusinessIncomeTaxCode code = new BusinessIncomeTaxCode();
				code.setCode(rs.getString("code"));
				code.setName(rs.getString("name"));
				result.add(code);
			}
			
			rs.close();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
}
