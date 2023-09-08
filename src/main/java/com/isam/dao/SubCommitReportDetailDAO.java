package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.isam.bean.CommitReportDetail;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class SubCommitReportDetailDAO {
	
	public JSONArray getJsonFmt(String repSerno){
		JSONArray ary = new JSONArray();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM SubCommitReportDetail WHERE repSerno = ?  and enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, repSerno);
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
	public List<CommitReportDetail> select(String repSerno){
		List<CommitReportDetail> result = new ArrayList<CommitReportDetail>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM SubCommitReportDetail WHERE repSerno = ? and enable='1' order by type";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, repSerno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				CommitReportDetail bean= new CommitReportDetail();
				bean.setRepserno(rs.getInt(1));
				bean.setYear(rs.getString(2));
				bean.setType(rs.getString(3));
				bean.setCorpvalue(rs.getDouble(4));
				bean.setRepvalue(rs.getDouble(5));
				bean.setCount(rs.getInt(6));
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
	public List<List<String>> getSummaryReport(String IDNO){
		SQL sqltool = new SQL();
		List<List<String>> result=new ArrayList<List<String>>();
		StringBuilder sb = new StringBuilder();
		sb.append("select cbean.type,cbean.startYear,cbean.endYear,cbean.value,pbean.val,case when cbean.value=0 then 0 else (pbean.val/cbean.value)*100 end pt from (");
		sb.append("select c.serno,c.startYear, c.endYear,isnull(d.type,sc.type) type,SUM(d.value) value from ");
		sb.append("(SELECT serno,startYear, endYear FROM [Commit] where [IDNO]=? and type='01' and enable='1') c left join (");
		sb.append("SELECT [subserno],[serno],[investNo],[type] FROM SubCommit where enable='1')sc on c.serno=sc.serno ");
		sb.append("left join (SELECT [subserno],[type],[value],[total] FROM [SubCommitDetail] where year='' and enable='1' and isFinancial='')d ");
		sb.append("on sc.subserno=d.subserno group by c.serno,c.startYear, c.endYear,d.type,sc.type) cbean left join (  select c.serno,b.type,sum(b.repvalue) val from (");
		sb.append("(SELECT serno FROM [Commit] where [IDNO]=? and type='01' and enable='1') c left join ");
		sb.append("(SELECT [subserno],[serno] FROM [SubCommit] where enable='1')sc on c.serno=sc.serno left join ");
		sb.append("(SELECT repserno,Subserno FROM SubCommitReport where enable='1' )sr on sr.Subserno=sc.subserno ");
		sb.append("left join (SELECT [repserno],LEFT(type,4) type,[repvalue] FROM [SubCommitReportDetail] where enable='1')b ");
		sb.append("on sr.repserno=b.repserno) group by c.serno,b.type) pbean on cbean.serno=pbean.serno and cbean.type=pbean.type ");
		sb.append("where cbean.type IS NOT NULL");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			stmt.setString(2, IDNO);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				ResultSetMetaData meta=rs.getMetaData();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					String str=DataUtil.nulltoempty(rs.getString(i));
					sb.append(str);
					list.add(DataUtil.nulltoempty(rs.getString(i)));
				}
				if(sb.length()!=0){
					result.add(list);
					sb.setLength(0);
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
	public List<List<String>> getSummary03Report(String IDNO){
		SQL sqltool = new SQL();
		List<List<String>> result=new ArrayList<List<String>>();
		StringBuilder sb = new StringBuilder();
		sb.append("select cbean.type,cbean.startYear, cbean.endYear,cbean.value,pbean.val,case when cbean.value=0 then 0 else (pbean.val/cbean.value)*100 end pt from (");
		sb.append("select c.serno,c.startYear,c.endYear,isnull(d.type,sc.type) type,SUM(d.value) value from ");
		sb.append("(SELECT serno, startYear, endYear FROM [Commit] where [IDNO]=? and type='03' and enable='1') c left join (");
		sb.append("SELECT [subserno],[serno],[investNo],[type] FROM SubCommit where enable='1')sc on c.serno=sc.serno ");
		sb.append("left join (SELECT [subserno],[type],[value],[total] FROM [SubCommitDetail] where year='' and enable='1' and isFinancial='')d ");
		sb.append("on sc.subserno=d.subserno group by c.serno,c.startYear,c.endYear,d.type,sc.type) cbean left join (  select c.serno,b.type,sum(b.repvalue) val from (");
		sb.append("(SELECT serno FROM [Commit] where [IDNO]=? and type='03' and enable='1') c left join ");
		sb.append("(SELECT [subserno],[serno] FROM [SubCommit] where enable='1')sc on c.serno=sc.serno left join ");
		sb.append("(SELECT repserno,Subserno FROM SubCommitReport where enable='1' )sr on sr.Subserno=sc.subserno ");
		sb.append("left join (SELECT [repserno],LEFT(type,2) type,[repvalue] FROM [SubCommitReportDetail] where enable='1')b ");
		sb.append("on sr.repserno=b.repserno) group by c.serno,b.type) pbean on cbean.serno=pbean.serno and cbean.type=pbean.type ");
		sb.append("where cbean.type IS NOT NULL ");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			stmt.setString(2, IDNO);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				ResultSetMetaData meta=rs.getMetaData();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					String str=DataUtil.nulltoempty(rs.getString(i));
					sb.append(str);
					list.add(DataUtil.nulltoempty(rs.getString(i)));
				}
				if(sb.length()!=0){
					result.add(list);
					sb.setLength(0);
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
//	public List<List<String>> getSummary02Report(String IDNO){
//		SQL sqltool = new SQL();
//		List<List<String>> result=new ArrayList<List<String>>();
//		StringBuilder sb = new StringBuilder();
//		sb.append("select isnull(cb.year,pb.year),cb.[0201],cb.[0202],cb.[0203],pb.[0201],pb.[0202],pb.[0203] from ");
//		sb.append("(select * from ( SELECT serno,[type] type,[year] year,[value] value FROM CommitDetail where isFinancial='' and year!='' ");
//		sb.append("and serno in (SELECT [serno] FROM [Commit] where [IDNO]=? and type='02' and enable='1') ");
//		sb.append(") A pivot (MAX(A.value) for A.type in ([0201],[0202],[0203])) as pvt )cb ");
//		sb.append("full join ( select * from ( select a.serno,a.Year,b.type,b.repvalue as value from (SELECT [repserno] ");
//		sb.append(",[serno],[Year] FROM [CommitReport] where serno in (SELECT [serno] FROM [Commit] where [IDNO]=? and type='02' and enable='1') and enable='1')a ");
//		sb.append("left join(SELECT [repserno] ,[type],[repvalue] FROM [CommitReportDetail])b on a.repserno=b.repserno ");
//		sb.append(") A pivot (MAX(A.value) for A.type in ([0201],[0202],[0203])) as pvt)pb on cb.serno=pb.serno and cb.year=pb.Year");
//		String forStmt = sb.toString();
////		System.out.println(forStmt);
//		sb.setLength(0);
//		try {
//			PreparedStatement stmt = sqltool.prepare(forStmt);
//			stmt.setString(1, IDNO);
//			stmt.setString(2, IDNO);
//			ResultSet rs = stmt.executeQuery();
//			while(rs.next()){
//				List<String> row=new ArrayList<String>();
//				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
//					row.add(rs.getString(i)==null?"":rs.getString(i));
//				}
//				result.add(row);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			try{
//				sqltool.close();
//			}catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return result;
//	}
//	public List<List<String>> getSummary03Report(String IDNO){
//		SQL sqltool = new SQL();
//		List<List<String>> result=new ArrayList<List<String>>();
//		StringBuilder sb = new StringBuilder();
//		sb.append("select isnull(cb.year,pb.year),cb.[0301],cb.[0302],pb.[0301] from ");
//		sb.append("(select * from ( SELECT serno,[type] type,[year] year,[value] value FROM CommitDetail where isFinancial='' and year!='' ");
//		sb.append("and serno in (SELECT [serno] FROM [Commit] where [IDNO]=? and type='03' and enable='1') ");
//		sb.append(") A pivot (MAX(A.value) for A.type in ([0301],[0302])) as pvt )cb ");
//		sb.append("full join ( select * from ( select a.serno,a.Year,b.type,b.repvalue as value from (SELECT [repserno] ");
//		sb.append(",[serno],[Year] FROM [CommitReport] where serno in (SELECT [serno] FROM [Commit] where [IDNO]=? and type='03' and enable='1') and enable='1')a ");
//		sb.append("left join(SELECT [repserno] ,[type],[repvalue] FROM [CommitReportDetail])b on a.repserno=b.repserno ");
//		sb.append(") A pivot (MAX(A.value) for A.type in ([0301])) as pvt)pb on cb.serno=pb.serno and cb.year=pb.Year");
//		String forStmt = sb.toString();
////		System.out.println(forStmt);
//		sb.setLength(0);
//		try {
//			PreparedStatement stmt = sqltool.prepare(forStmt);
//			stmt.setString(1, IDNO);
//			stmt.setString(2, IDNO);
//			ResultSet rs = stmt.executeQuery();
//			while(rs.next()){
//				List<String> row=new ArrayList<String>();
//				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
//					row.add(rs.getString(i)==null?"":rs.getString(i));
//				}
//				result.add(row);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			try{
//				sqltool.close();
//			}catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return result;
//	}
	public void insert(List<CommitReportDetail> beans) {
		String forpstmt = "insert into SubCommitReportDetail values(?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				CommitReportDetail bean = beans.get(i);
				pstmt.setInt(1, bean.getRepserno());
				pstmt.setString(2, bean.getYear());
				pstmt.setString(3, bean.getType());
				pstmt.setDouble(4, bean.getCorpvalue());
				pstmt.setDouble(5, bean.getRepvalue());
				pstmt.setInt(6, bean.getCount());
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
	
	public void delete(int repSerno){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM SubCommitReportDetail WHERE repSerno = ?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setInt(1, repSerno);
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
	public int unable(String repserno) {
		int result = 0;
		String forpstmt = "UPDATE SubCommitReportDetail SET enable='0' WHERE repserno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, repserno);
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
