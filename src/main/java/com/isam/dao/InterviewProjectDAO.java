package com.isam.dao;

import java.sql.*;
import java.util.*;

import com.isam.bean.*;
import com.isam.helper.SQL;

public class InterviewProjectDAO {
	public static Vector<InterviewProject> select(){
		SQL sqltool = null;
		PreparedStatement stmt = null;

		Vector<InterviewProject> result = new Vector<InterviewProject>();

		try{
			sqltool = new SQL("jdbc/sqlChinaDB");
			stmt = sqltool.prepare("SELECT * FROM TB_Project ORDER BY year DESC ");
			ResultSet rs = stmt.executeQuery();

			while(rs.next()){
				InterviewProject interviewproject = new InterviewProject();
				interviewproject.setIdentifier(rs.getInt("Identifier"));
				interviewproject.setYear(rs.getInt("year"));
				interviewproject.setName(rs.getString("Name"));
				interviewproject.setSubName(rs.getString("SubName"));

				result.add(interviewproject);
			}

			rs.close();
			stmt.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(sqltool != null){
					sqltool.close();
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}

		return result;
	}
}
