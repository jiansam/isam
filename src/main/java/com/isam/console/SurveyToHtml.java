package com.isam.console;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.helper.SQL;

public class SurveyToHtml {
	
	public List<String> getqNoByQTypeAndYear(String qType,String year){
		SQL sqltool = new SQL();
		List<String> list = new ArrayList<String>(); 
		String forStmt ="SELECT distinct qNo FROM Survey where qType=? and year=?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, qType);
			pstmt.setString(2, year);
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
	public List<List<String>> getQTypeAndYear(){
		SQL sqltool = new SQL();
		List<List<String>> list = new ArrayList<List<String>>();
		String forStmt ="SELECT distinct qType,year FROM Survey";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				List<String> subList = new ArrayList<String>();
				subList.add(rs.getString(1));
				subList.add(rs.getString(2));
				list.add(subList);
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
	public List<SurveyTopicXHtml> getSurveyTopicXHtml(String qType,String year){
		SQL sqltool = new SQL();
		List<SurveyTopicXHtml> list = new ArrayList<SurveyTopicXHtml>();
		String forStmt ="SELECT [topic],[filename],[qType],[year],[html] FROM SurveyTopicXHtml where qType=? and year=?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, qType);
			pstmt.setString(2, year);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				SurveyTopicXHtml bean = new SurveyTopicXHtml();
				bean.setTopic(rs.getString(1));
				bean.setFilename(rs.getString(2));
				bean.setqType(rs.getString(3));
				bean.setYear(rs.getString(4));
				bean.setHtml(rs.getString(5));
				list.add(bean);
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
	public List<Survey> getSurveyValue(String qType,String year,String qNo,String topic){
		SQL sqltool = new SQL();
		List<Survey> list = new ArrayList<Survey>();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT a.[qType],a.[year],a.[qNo],a.[topic],a.[item],a.[value],b.type ");
		sb.append("FROM Survey a,SurveyTopicXItem b ");
		sb.append("where a.qType=b.qType and a.item=b.item and a.qType=? and a.year=? and qNo=? and a.topic=?");
		String forStmt =sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, qType);
			pstmt.setString(2, year);
			pstmt.setString(3, qNo);
			pstmt.setString(4, topic);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				Survey bean = new Survey();
				bean.setqType(rs.getString(1));
				bean.setYear(rs.getString(2));
				bean.setqNo(rs.getString(3));
				bean.setTopic(rs.getString(4));
				bean.setItem(rs.getString(5));
				bean.setValue(rs.getString(6));
				bean.setType(rs.getString(7));
				list.add(bean);
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
	public Map<String,String> getSurveyRegion(){
		SQL sqltool = new SQL();
		 Map<String,String> map = new HashMap<String, String>();
		String forStmt ="select code,name from SurveyRegion where ISNUMERIC(code)=1";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
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
	public Map<String,String> getSurveyIndustry(){
		SQL sqltool = new SQL();
		Map<String,String> map = new HashMap<String, String>();
		String forStmt ="select code,name from Industry where ISNUMERIC(code)=1";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
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
	public Map<String,Map<String,String>> getAbroadValue(){
		SQL sqltool = new SQL();
		Map<String,Map<String,String>> map = new HashMap<String, Map<String,String>>();
		String forStmt ="SELECT * FROM SurveyTypeDetail where type in ('difficulty','motive','state')";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				Map<String,String> submap;
				if(!map.containsKey(rs.getString(1))){
					submap = new HashMap<String, String>();
				}else{
					submap = map.get(rs.getString(1));
				}
				submap.put(rs.getString(2),rs.getString(3));
				map.put(rs.getString(1), submap);
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
	public int insert(SurveyXHtml bean) {
		int result = 0;
		String forpstmt = "insert into SurveyXHtml(qNo,topic,qType,year,html) values (?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getqNo());
			pstmt.setString(2, bean.getTopic());
			pstmt.setString(3, bean.getqType());
			pstmt.setString(4, bean.getYear());
			pstmt.setString(5, bean.getHtml());
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
	public void insertBatch(List<SurveyXHtml> list){
		SQL sqltool = new SQL();
		String forpstmt = "insert into SurveyXHtml(qNo,topic,qType,year,html) values (?,?,?,?,?)";
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<list.size();i++){
				SurveyXHtml bean = list.get(i);
				pstmt.setString(1, bean.getqNo());
				pstmt.setString(2, bean.getTopic());
				pstmt.setString(3, bean.getqType());
				pstmt.setString(4, bean.getYear());
				pstmt.setString(5, bean.getHtml());
				pstmt.addBatch();
			}
			pstmt.executeBatch();
			sqltool.commit();
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
