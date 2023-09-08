package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.InterviewoneFile;
import com.isam.helper.SQL;

public class InterviewoneFileDAO {
	
	public int insert(InterviewoneFile bean) {
		int result = 0;
		String forpstmt = "insert into InterviewoneFile Output Inserted.fNo values(?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			int count=1;
			pstmt.setInt(count++, bean.getqNo());
			pstmt.setString(count++, bean.getqType());
			pstmt.setString(count++,bean.getfName() );
			pstmt.setBytes(count++, bean.getfContent());
			pstmt.setTimestamp(count++, bean.getUpdatetime());
			pstmt.setString(count++,bean.getUpdateuser() );
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				result=rs.getInt(1);
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
		return result;
	}
	public void insert(List<InterviewoneFile> beans) {
		String forpstmt = "insert into InterviewoneFile values(?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			sqltool.noCommit();
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				InterviewoneFile bean = beans.get(i);
				int count=1;
				pstmt.setInt(count++, bean.getqNo());
				pstmt.setString(count++, bean.getqType());
				pstmt.setString(count++,bean.getfName() );
				pstmt.setBytes(count++, bean.getfContent());
				pstmt.setTimestamp(count++, bean.getUpdatetime());
				pstmt.setString(count++,bean.getUpdateuser() );
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
	public int delete(int fNo) {
		int result = 0;
		String forpstmt = "delete from InterviewoneFile WHERE fNo=? ";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setInt(1, fNo);
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
	public int deleteByQNo(String qNo) {
		int result = 0;
		String forpstmt = "delete from InterviewoneFile WHERE qNo=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, qNo);
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
	public List<List<Integer>> getISFileCount(String year){
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT a.qNo,isnull(b.I,0) I,isnull(b.S,0) S FROM Interviewone a left join (");
		sb.append("select * from (SELECT qNo,qType FROM InterviewoneFile where qNo in (");
		sb.append("select qno from Interviewone where year=? and enable='1'))a pivot(");
		sb.append("count(qType) for a.qType in ([I],[S])) as pvt)b on a.qNo=b.qNo where ");
		sb.append("a.year=? and a.enable='1'");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, year);
			stmt.setString(2, year);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				List<Integer> temp = new ArrayList<Integer>();
				temp.add(rs.getInt("qNo"));
				temp.add(rs.getInt("I"));
				temp.add(rs.getInt("S"));
				result.add(temp);
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
/*	public List<List<Integer>> getISFileCount(String year){
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT a.qNo,isnull(b.I,0) I,isnull(b.S,0) S FROM Interviewone a left join (");
		sb.append("select * from (SELECT [investNo] invsetNo,year,[investNo] a2,[qType] FROM InterviewoneFile ");
		sb.append("where year=?)a pivot(count(a2) for a.qType in ([I],[S])) as pvt)b ");
		sb.append("on a.investNo=b.invsetNo and a.year=b.year");
		String forStmt = sb.toString();
		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, year);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				List<Integer> temp = new ArrayList<Integer>();
				temp.add(rs.getInt("qNo"));
				temp.add(rs.getInt("I"));
				temp.add(rs.getInt("S"));
				result.add(temp);
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
	public int countByYear(String investNo,String year){
		int result =0;
		SQL sqltool = new SQL();
		String forStmt = "SELECT count(fNo) FROM InterviewoneFile where investNo=? and year=?";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, year);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result=rs.getInt(1);
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
	}*/
	public List<InterviewoneFile> select(String investNo,String reInvestNo,String year){
		List<InterviewoneFile> result = new ArrayList<InterviewoneFile>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT fNo,year,qType,fname FROM Interviewone a,InterviewoneFile b where a.qNo=b.qNo ");
		sb.append("and enable='1' and investNo=isnull(?,investNo) and reInvestNo=isnull(?,reInvestNo) ");
		sb.append("and year=isnull(?,year) order by year desc");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, reInvestNo);
			stmt.setString(3, year);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				InterviewoneFile bean= new InterviewoneFile();
				bean.setfNo(rs.getInt("fNo"));
				bean.setYear(rs.getString("year"));
				bean.setqType(rs.getString("qType"));
				bean.setfName(rs.getString("fName"));
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
	public Map<String,Integer> countByYear(String investNo,String reInvestNo){
		Map<String,Integer> result = new HashMap<String,Integer>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT year,count(fNo) c FROM Interviewone a,InterviewoneFile b where a.qNo=b.qNo ");
		sb.append("and enable='1' and investNo=isnull(?,investNo) and reInvestNo=isnull(?,reInvestNo) ");
		sb.append("group by year");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, reInvestNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.put(rs.getString("year"),rs.getInt("c"));
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
	public InterviewoneFile select(int fNo){
		InterviewoneFile bean= new InterviewoneFile();
		SQL sqltool = new SQL();
		String forStmt = "SELECT fName,fContent FROM InterviewoneFile where fNo=? ";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setInt(1, fNo);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean.setfName(rs.getString("fName"));
				bean.setfContent(rs.getBytes("fContent"));
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
		return bean;
	}
}
