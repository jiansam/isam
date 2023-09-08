package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.isam.bean.InterviewoneItem;
import com.isam.bean.InterviewoneOption;
import com.isam.helper.SQL;

public class InterviewoneHelperDAO {
	
	public List<InterviewoneItem> getItems(){
		List<InterviewoneItem> result= new ArrayList<InterviewoneItem>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT optionId,qType,cName,paramName,isText,enable FROM InterviewoneItem where enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				InterviewoneItem bean= new InterviewoneItem();
				bean.setOptionId(rs.getInt("optionId"));
				bean.setqType(rs.getString("qType"));
				bean.setcName(rs.getString("cName"));
				bean.setParamName(rs.getString("paramName"));
				bean.setIsText(rs.getString("isText"));
				bean.setEnable(rs.getString("enable"));
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
	public List<InterviewoneOption> getOptions(){
		List<InterviewoneOption> result= new ArrayList<InterviewoneOption>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT optionNo,cname,optionValue,paramName,enable,seq FROM InterviewoneOption where enable='1' order by paramName,seq";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				InterviewoneOption bean= new InterviewoneOption();
				bean.setOptionNo(rs.getInt("optionNo"));
				bean.setCname(rs.getString("cName"));
				bean.setOptionValue(rs.getString("optionValue"));
				bean.setParamName(rs.getString("paramName"));
				bean.setEnable(rs.getString("enable"));
				bean.setSeq(rs.getInt("seq"));
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
