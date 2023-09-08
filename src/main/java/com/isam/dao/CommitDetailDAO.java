package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.isam.bean.CommitDetail;
import com.isam.helper.SQL;

public class CommitDetailDAO {
	
	public JSONArray getJsonFmt(String serno){
		JSONArray ary = new JSONArray();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommitDetail WHERE serno = ? and enable='1' and (isFinancial='1' or total!=0)";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta =rs.getMetaData();
			while(rs.next()){
				JSONObject obj =new JSONObject();
				for(int i=1;i<=meta.getColumnCount();i++){
					obj.put(meta.getColumnName(i), rs.getString(i));
				}
				ary.add(obj);
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
		return ary;
	}
	public List<CommitDetail> select(String serno){
		List<CommitDetail> result = new ArrayList<CommitDetail>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommitDetail WHERE serno = ?  and enable='1' and (isFinancial='1' or total!=0) order by type";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				CommitDetail bean= new CommitDetail();
				bean.setSerno(rs.getInt(1));
				bean.setType(rs.getString(2));
				bean.setYear(rs.getString(3));
				bean.setValue(rs.getDouble(4));
				bean.setTotal(rs.getString(5));
				bean.setIsFinancial(rs.getString(6));
				bean.setEnable(rs.getString(7));
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
	public Map<String,CommitDetail> selectOriginalTT(String serno){
		Map<String,CommitDetail> result = new HashMap<String, CommitDetail>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT serno,type,year,0 value,total,isFinancial,enable FROM CommitDetail WHERE serno = ?  and enable='1' and (isFinancial='1' or total!=0)";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				CommitDetail bean= new CommitDetail();
				bean.setSerno(rs.getInt(1));
				bean.setType(rs.getString(2));
				bean.setYear(rs.getString(3));
				bean.setValue(rs.getDouble(4));
				bean.setTotal(rs.getString(5));
				bean.setIsFinancial(rs.getString(6));
				bean.setEnable(rs.getString(7));
				result.put(rs.getString(2), bean);
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
	
	public boolean checkAccPt(String serno){
		boolean result=false;
		SQL sqltool = new SQL();
		int count=0;
		StringBuffer sb = new StringBuffer();
		try {
		sb.append("SELECT count(repserno) FROM CommitReport WHERE serno=? and enable=1 and repType like '01%' ");
		String forStmt = sb.toString();
		sb.setLength(0);
		PreparedStatement stmt = sqltool.prepare(forStmt);
		stmt.setString(1, serno);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()){
			count=rs.getInt(1);
		}
		if(count!=0){
			sb.append("select COUNT(*) from ( SELECT d.[serno],d.[type],d.[value],case when d.[value]=0 then 0 else (a.TRValue/d.[value]) end pt FROM CommitDetail d ");
			sb.append("left join (SELECT Left([type],4) as type,Sum([repvalue]) as TRValue FROM CommitReportDetail ");
			sb.append("where enable='1' and repserno in (select repserno from [CommitReport] where serno=?) ");
			sb.append("group by Left([type],4)) as a on d.type=a.type where d.serno=? and d.enable=1)t where pt<1 or pt is null");
			forStmt = sb.toString();
			sb.setLength(0);
			stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			stmt.setString(2, serno);
			rs = stmt.executeQuery();
			if(rs.next()){
				count=rs.getInt(1);
			}
			if(count==0){
				result= true;
			}
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
	public List<List<String>> select(String serno,String type){
		List<List<String>> result = new ArrayList<List<String>>();
		SQL sqltool = new SQL();
		String forStmt ="";
		if(type.equals("01")){
			forStmt = "select * from ( SELECT [type] type,[year] year,[value] value FROM CommitDetail where isFinancial='' and year!='' and serno=?) A pivot (MAX(A.value) for A.type in ([0101],[0102],[0103],[0104])) as pvt order by year desc";
		}else if(type.equals("02")){
			forStmt = "select * from ( SELECT [type] type,[year] year,[value] value FROM CommitDetail where isFinancial='' and year!='' and serno=?) A pivot (MAX(A.value) for A.type in ([0201],[0202],[0203])) as pvt order by year desc";
		}else if(type.equals("03")){
			forStmt = "select * from ( SELECT [type] type,[year] year,[value] value FROM CommitDetail where isFinancial='' and year!='' and serno=?) A pivot (MAX(A.value) for A.type in ([0301],[0302])) as pvt order by year desc";
		}else{
			return result;
		}
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta =rs.getMetaData();
			while(rs.next()){
				List<String> sub = new ArrayList<String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.add(rs.getString(i));
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
	public Map<String,String> selectTotalValue(String serno){
		Map<String,String> result = new LinkedHashMap<String, String>();
		SQL sqltool = new SQL();
		String forStmt =" select * from ( SELECT year,[type] type,[value] value FROM CommitDetail where (isFinancial='1' or year='') and serno=?) A pivot (MAX(A.value) for A.type in ([0101],[0102],[0103],[0104],[0201],[0202],[0203],[03])) as pvt";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta =rs.getMetaData();
			while(rs.next()){
				for(int i=1;i<=meta.getColumnCount();i++){
					if(rs.getString(i)!=null&&!rs.getString(i).isEmpty()){
						result.put(meta.getColumnName(i),rs.getString(i));
					}
				}
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
	public Map<String,Map<String,String>> selectWithoutTotal(String serno,String type){
		Map<String,Map<String,String>> result = new HashMap<String, Map<String,String>>();
		SQL sqltool = new SQL();
		String forStmt ="";
		if(type.equals("01")){
			forStmt = "select * from ( SELECT [type] type,[year] year,[value] value FROM CommitDetail where isFinancial='' and year!='' and serno=?) A pivot (MAX(A.value) for A.type in ([0101],[0102],[0103],[0104])) as pvt";
		}else if(type.equals("02")){
			forStmt = "select * from ( SELECT [type] type,[year] year,[value] value FROM CommitDetail where isFinancial='' and year!='' and serno=?) A pivot (MAX(A.value) for A.type in ([0201],[0202],[0203])) as pvt";
		}else if(type.equals("03")){
			forStmt = "select * from ( SELECT [type] type,[year] year,[value] value FROM CommitDetail where isFinancial='' and year!='' and serno=?) A pivot (MAX(A.value) for A.type in ([0301],[0302])) as pvt";
		}
//		String forStmt ="select * from ( SELECT [type] type,[year] year,[value] value FROM CommitDetail where isFinancial='' and year!='' and serno=?) A pivot (MAX(A.value) for A.type in ([0101],[0102],[0103],[0104])) as pvt";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta =rs.getMetaData();
			while(rs.next()){
				Map<String,String> subExMap = new HashMap<String, String>();
				for(int i=2;i<=meta.getColumnCount();i++){
					subExMap.put(meta.getColumnName(i),rs.getString(i)==null?"":rs.getString(i));
				}
				result.put(rs.getString(1),subExMap);
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
	public List<List<String>> select02ByIdno(String idno){
		List<List<String>> result = new ArrayList<List<String>>();
		SQL sqltool = new SQL();
		StringBuffer sb = new StringBuffer();
		String forStmt ="";
		sb.append("select com.repType,com.startYear,com.endYear,pvt.year,pvt.[0201],pvt.[0202],pvt.[0203] from ( ");
		sb.append("SELECT serno,[type] type,[year] year,[value] value FROM CommitDetail ");
		sb.append("where isFinancial='1' and year!='' and serno in (select serno from [Commit] where IDNO=? and type='02' and enable='1')) ");
		sb.append("A pivot (MAX(A.value) for A.type in ([0201],[0202],[0203])) as pvt,");
		sb.append("(select repType,startYear,endYear,serno from [Commit] where IDNO=? and enable='1' and type='02') com where pvt.serno=com.serno");
		forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, idno);
			stmt.setString(2, idno);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta =rs.getMetaData();
			while(rs.next()){
				List<String> sub = new ArrayList<String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.add(rs.getString(i));
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
	public void insert(List<CommitDetail> beans) {
		String forpstmt = "insert into CommitDetail values(?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				CommitDetail bean = beans.get(i);
				pstmt.setInt(1, bean.getSerno());
				pstmt.setString(2, bean.getType());
				pstmt.setString(3, bean.getYear());
				pstmt.setDouble(4, bean.getValue());
				pstmt.setString(5, bean.getTotal());
				pstmt.setString(6, bean.getIsFinancial());
				pstmt.setString(7, bean.getEnable());
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
	public void delete(int serno){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM CommitDetail WHERE serno = ?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setInt(1, serno);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public int unable(String serno) {
		int result = 0;
		String forpstmt = "UPDATE CommitDetail SET enable='0' WHERE serno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, serno);
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
	
}
