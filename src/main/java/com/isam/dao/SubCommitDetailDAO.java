package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.CommitDetail;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class SubCommitDetailDAO {
	
	public List<CommitDetail> select(String subserno){
		List<CommitDetail> result = new ArrayList<CommitDetail>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM SubCommitDetail WHERE subserno = ?  and enable='1' and (isFinancial='1' or total!=0) order by type";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, subserno);
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
	public Map<String,List<CommitDetail>> getDetail(String subserno){
		Map<String,List<CommitDetail>> result = new HashMap<String, List<CommitDetail>>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT subserno,type,year,value,total,enable,case when (isFinancial='1' or total!=0) then 'T' else 'S' end as classify FROM SubCommitDetail WHERE subserno = ?  and enable='1' order by year,type";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, subserno);
			ResultSet rs = stmt.executeQuery();
			List<CommitDetail> list;
			while(rs.next()){
				String k1=rs.getString("classify");
				if(result.containsKey(k1)){
					list=result.get(k1);
				}else{
					list= new ArrayList<CommitDetail>();
				}
				CommitDetail bean=new CommitDetail();
				bean.setSerno(rs.getInt(1));
				bean.setType(rs.getString(2));
				bean.setYear(rs.getString(3));
				bean.setValue(rs.getDouble(4));
				bean.setTotal(rs.getString(5));
				bean.setIsFinancial(rs.getString(6));
				bean.setEnable(rs.getString(7));
				list.add(bean);
				result.put(k1, list);
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
			forStmt = "select * from ( SELECT [type] type,[year] year,[value] value FROM SubCommitDetail where isFinancial='' and year!='' and Subserno=?) A pivot (MAX(A.value) for A.type in ([0101],[0102],[0103],[0104])) as pvt";
		}else if(type.equals("02")){
			forStmt = "select * from ( SELECT [type] type,[year] year,[value] value FROM SubCommitDetail where isFinancial='' and year!='' and Subserno=?) A pivot (MAX(A.value) for A.type in ([0201],[0202],[0203])) as pvt";
		}else if(type.equals("03")){
			forStmt = "select * from ( SELECT [type] type,[year] year,[value] value FROM SubCommitDetail where isFinancial='' and year!='' and Subserno=?) A pivot (MAX(A.value) for A.type in ([0301],[0302])) as pvt";
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
	public Map<String,Map<String,Double>> getDetailSummary(String serno,String type){
		Map<String,Map<String,Double>> result = new HashMap<String, Map<String,Double>>();
		SQL sqltool = new SQL();
		StringBuilder sb=new StringBuilder();
		sb.append("select * from ( SELECT type,year,value FROM SubCommitDetail where isFinancial='' and year!='' ");
		sb.append("and Subserno in (SELECT subserno FROM SubCommit where enable='1' and serno=?)) A pivot (");
		if(type.equals("01")){
			sb.append(" sum(A.value) for A.type in ([0101],[0102],[0103],[0104])) as pvt");
		}else if(type.equals("03")){
			sb.append(" sum(A.value) for A.type in ([0301],[0302])) as pvt");
		}
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta =rs.getMetaData();
			while(rs.next()){
				Map<String,Double> subExMap = new HashMap<String, Double>();
				for(int i=2;i<=meta.getColumnCount();i++){
					subExMap.put(meta.getColumnName(i),rs.getDouble(i));
				}
				result.put(rs.getString(1),subExMap);
			}
			if(rs != null){
				try{
					rs.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
				rs = null;
			}
			if(stmt != null){
				try{
						stmt.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
					stmt = null;
			}
			sb.append("SELECT sub.type,startYear,endYear,case when repType='01' then value/(sub.total/2) else value/total end v ");
			sb.append("FROM SubCommit m ,(SELECT subserno,type,value,total FROM SubCommitDetail where enable='1' and year='' ");
			sb.append("and isFinancial='' and subserno in(SELECT subserno FROM SubCommit where enable='1' and serno=? ) and ");
			sb.append("subserno not in(SELECT subserno  FROM SubCommitDetail where isFinancial='' and year!='' and Subserno in( ");
			sb.append("SELECT subserno FROM SubCommit where enable='1' and serno=? )group by subserno)) sub  where m.subserno=sub.subserno and serno=?");
			forStmt = sb.toString();
			sb.setLength(0);
			stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			stmt.setString(2, serno);
			stmt.setString(3, serno);
			rs = stmt.executeQuery();
			
			while(rs.next()){
				int sY=Integer.valueOf(rs.getString("startYear"));
				int eY=Integer.valueOf(rs.getString("endYear"));
				String subkey=rs.getString("type");
				double val=rs.getDouble("v");
				Map<String,Double> subExMap;
				if(type.equals("01")){
					for (int i = sY; i <=eY ; i++) {
						String k=DataUtil.addZeroForNum(String.valueOf(i), 3);
						if(result.containsKey(k)){
							subExMap=result.get(k);
						}else{
							subExMap=new HashMap<String, Double>();
						}
						double newVal=0;
						if(subExMap.containsKey(subkey)){
							newVal=subExMap.get(subkey)+val;
						}else{
							newVal=val;
						}
						subExMap.put(subkey, newVal);
						result.put(k, subExMap);
					}
				}else if(type.equals("03")){
					subkey="0301";
					for (int i = sY; i <=eY ; i++) {
						String k=DataUtil.addZeroForNum(String.valueOf(i), 3);
						String lastk=DataUtil.addZeroForNum(String.valueOf(i-1), 3);
						if(result.containsKey(k)){
							subExMap=result.get(k);
						}else{
							subExMap=new HashMap<String, Double>();
						}
						double newVal=0;
						if(subExMap.containsKey(subkey)){
							newVal=subExMap.get(subkey)+val;
						}else{
							newVal=val;
						}
						subExMap.put(subkey, newVal);
						double cumVal=0;
						if(result.containsKey(lastk)){
							if(result.get(lastk).containsKey("0302")){
								cumVal=result.get(lastk).get("0302")+newVal;
							}else{
								cumVal=newVal;
							}
							subExMap.put("0302", cumVal);
						}else{
							subExMap.put("0302", newVal);
						}
						result.put(k, subExMap);
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
	public void insert(List<CommitDetail> beans) {
		String forpstmt = "insert into SubCommitDetail values(?,?,?,?,?,?,?)";
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
	public void delete(String subserno){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM SubCommitDetail WHERE subserno = ?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, subserno);
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
		String forpstmt = "UPDATE SubCommitDetail SET enable='0' WHERE subserno=?";
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
