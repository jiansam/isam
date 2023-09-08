package com.isam.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.Industry;
import com.isam.bean.SurveyRegion;
import com.isam.bean.SurveyTopic;
import com.isam.helper.SQL;

public class SurveyDAO {
	/*
	 * 取得問卷種類和對應的年度
	 * key：問卷種類
	 * value：年度list
	*/
	public Map<String,List<String>> getQTypeXYear(){
		SQL sqltool = new SQL();
		Map<String,List<String>> qTypeYear = new HashMap<String, List<String>>(); 
		String forStmt ="SELECT distinct qType,year FROM Survey order by qType,year desc";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				if(!qTypeYear.containsKey(rs.getString(1))){
					List<String> tmp = new ArrayList<String>();
					tmp.add(rs.getString(2));
					qTypeYear.put(rs.getString(1), tmp);
				}else{
					List<String> tmp =qTypeYear.get(rs.getString(1));
					tmp.add(rs.getString(2));
					qTypeYear.put(rs.getString(1), tmp);
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
		return qTypeYear;
	}
	/* 取得產業分類BEANS */
	public List<Industry> getIndustry(){
		SQL sqltool = new SQL();
		List<Industry> list = new ArrayList<Industry>(); 
		String forStmt = "select code,name,level,parent from Industry order by seq";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				Industry bean = new Industry();
				bean.setCode(rs.getString(1));
				bean.setName(rs.getString(2));
				bean.setLevel(rs.getString(3));
				bean.setParent(rs.getString(4));
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
	/* 取得問卷地區分類BEANS */
	public List<SurveyRegion> getSurveyRegion(){
		SQL sqltool = new SQL();
		StringBuffer sb = new StringBuffer();
		List<SurveyRegion> list = new ArrayList<SurveyRegion>(); 
		sb.append("select region.code,region.name,region.level,region.parent,region.note from (");
		sb.append("SELECT a.[code] code,a.[name] name,a.level level,a.note note,a.seq seq,a.parent parent ");
		sb.append("FROM SurveyRegion a,SurveyRegion b ");
		sb.append("where a.parent=b.code union all ");
		sb.append("SELECT [code] code,[name] name,[level] level,[note] note,seq seq,'' parent ");
		sb.append("FROM SurveyRegion where level='1') region ");
		sb.append("order by region.level,region.seq");
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				SurveyRegion bean = new SurveyRegion();
				bean.setCode(rs.getString(1));
				bean.setName(rs.getString(2));
				bean.setLevel(rs.getString(3));
				bean.setParent(rs.getString(4));
				bean.setNote(rs.getString(5));
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
	/* 取得問卷題目BEANS */
	public List<SurveyTopic> getSurveyTopic(){
		SQL sqltool = new SQL();
		List<SurveyTopic> list = new ArrayList<SurveyTopic>(); 
		String forStmt = "select qType,topic,title,parent from SurveyTopic order by topic,seq";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				SurveyTopic bean = new SurveyTopic();
				bean.setqType(rs.getString(1));
				bean.setTopic(rs.getString(2));
				bean.setTitle(rs.getString(3));
				bean.setParent(rs.getString(4));
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
	/* 
	 * 以問卷種類取得問卷細項題目代碼及中文名稱
	 * key：問卷細項題目(item)
	 * value：問卷細項中文名稱	 
	 */
	public Map<String,String> getSurveyItemName(String qType){
		SQL sqltool = new SQL();
		Map<String,String> map = new HashMap<String, String>();
		String forStmt = "select item,name from SurveyTopicXItem where qType=?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, qType);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString(1), rs.getString(2));
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
	/* 
	 * 以問卷種類取得問卷題目代碼及中文名稱
	 * key：問卷題目(topic)
	 * value：問卷題目中文名稱	 
	 */
	public Map<String,String> getSurveyTopicName(String qType){
		SQL sqltool = new SQL();
		Map<String,String> map = new HashMap<String, String>();
		String forStmt = "select topic,title from SurveyTopic where parent<>'' and qType=? order by seq";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, qType);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString(1), rs.getString(2));
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
	/*For SurveyService*/
	/*
	 *呼叫預存程序getItemsByTopic
	 *回傳值主要是提供EXCEL下載用，預存程序會動態將所有內容代號改為其代號所代表的文字內容
	 *其中，meta.getColumnName取回的是item代號，所以取回後要把他改為文字
	*/
	public List<List< String>> getSurveyResult(String qType,String year,String topic,String ind,String region){
		SQL sqltool = new SQL();
		List<List<String>> list= new  ArrayList<List< String>>();
		int c=0;
		try {
//			System.out.println(qType+"="+year+"="+topic+"="+ind+"="+region);
			CallableStatement cs = sqltool.prepareCall("{call getItemsByTopic(?,?,?,?,?)}");
			cs.setString(1, qType);
			cs.setString(2, year);
			cs.setString(3, topic);
			cs.setString(4, ind);
			cs.setString(5, region);
			ResultSet rs=cs.executeQuery();
			ResultSetMetaData meta =rs.getMetaData();
			int count = meta.getColumnCount();
			List<String> columnNames = new ArrayList<String>();
			while(rs.next()){
				if(c==0){
					Map<String,String> topicMap=getSurveyItemName(qType);
					for(int i=1;i<=count;i++){
						columnNames.add(topicMap.get(meta.getColumnName(i)));
					}			
					list.add(columnNames);
					c++;
				}
				List<String> conts = new ArrayList<String>();
				for(int i=1;i<=count;i++){
					conts.add(rs.getString(i)==null?"":rs.getString(i));
				}
				list.add(conts);
			}
		} catch (SQLException e) {
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
	/*呼叫預存程序getHtmlsByTopic*/
	public List<List< String>> getSurveyHtmlResult(String qType,String year,String topic,String qNo){
		SQL sqltool = new SQL();
		List<List<String>> list= new  ArrayList<List< String>>();
		try {
			CallableStatement cs = sqltool.prepareCall("{call getHtmlsByTopic(?,?,?,?)}");
			cs.setString(1, qType);
			cs.setString(2, year);
			cs.setString(3, topic);
			cs.setString(4, qNo);
			ResultSet rs=cs.executeQuery();
			ResultSetMetaData meta =rs.getMetaData();
			int count = meta.getColumnCount();
			List<String> columnNames = new ArrayList<String>();
			Map<String,String> topicMap =getSurveyTopicName(qType);
			Map<String,String> qNoNameMap;
			int flag=0;
			while(rs.next()){
				if(flag==0){
					qNoNameMap=getQNoName(qType,year,qNo);
					columnNames.add(meta.getColumnName(1));
					for(int i=2;i<=count;i++){
						columnNames.add(qNoNameMap.get(meta.getColumnName(i)));
					}			
					list.add(columnNames);
					flag=1;
				}
				List<String> conts = new ArrayList<String>();
				conts.add(topicMap.get(rs.getString(1))==null?"":topicMap.get(rs.getString(1)));
				for(int i=2;i<=count;i++){
					conts.add(rs.getString(i)==null?"":rs.getString(i));
				}
				list.add(conts);
			}
		} catch (SQLException e) {
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
	/*取回可以被查詢的地區、產業及問卷代號*/
	public Map<String,List<String>> getCheckMap(){
		SQL sqltool = new SQL();
		Map<String,List<String>> map= new HashMap<String,List<String>>();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT [qType] as type,[topic] as value FROM SurveyTopic where parent!=''");
		sb.append("  union all  SELECT [type] as type,[value] as value FROM SurveyTypeDetail");
		sb.append("  where type='Industry' or type='distraction'");
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			ResultSet rs=pstmt.executeQuery();
			List<String> list;
			while(rs.next()){
				String type= rs.getString(1);
				String value= rs.getString(2);
				if(!map.containsKey(type)){
					list = new ArrayList<String>();
					list.add(value);
				}else{
					list = map.get(type);
					list.add(value);
				}
				map.put(type, list);
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
	/*取回符合條件的問卷廠商資料*/
	public List<Map<String,String>> getQNoResult(String qType,String year,String ind,String region){
		SQL sqltool = new SQL();
		List<Map<String,String>> list= new ArrayList<Map<String,String>>();
		String forStmt = "SELECT qNo,company,IDNO FROM [getQNoXNameXIDNO] (?,?,?,?)";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
//			System.out.println(qType+year+ind+region);
			pstmt.setString(1, qType);
			pstmt.setString(2, year);
			pstmt.setString(3, ind);
			pstmt.setString(4, region);
			ResultSet rs=pstmt.executeQuery();
			Map<String,String> map;
			while(rs.next()){
				map=new HashMap<String, String>();
				map.put("qNo", rs.getString(1));
				map.put("company", rs.getString(2));
				map.put("IDNO", rs.getString(3));
				list.add(map);
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
	/*取回問卷廠商代碼與中文名*/
	public Map<String,String> getQNoName(String qType,String year,String qNoStr){
		SQL sqltool = new SQL();
		Map<String,String> map= new HashMap<String,String>();
		String forStmt = "SELECT qNo,company FROM [getQNoXName] (?,?,?)";
//		System.out.println(qType+":"+year+":"+qNoStr);
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, qType);
			pstmt.setString(2, year);
			pstmt.setString(3, qNoStr);
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()){
				map.put(rs.getString(1), rs.getString(2));
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
}
