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
import com.isam.helper.SQL;

public class CommitReportDetailDAO {
	
	public JSONArray getJsonFmt(String repSerno){
		JSONArray ary = new JSONArray();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommitReportDetail WHERE repSerno = ?  and enable='1'";
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
		String forStmt = "SELECT * FROM CommitReportDetail WHERE repSerno = ? and enable='1' order by type";
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
	public List<List<String>> getSummaryReport(String IDNO,String type){
		SQL sqltool = new SQL();
		List<List<String>> result=new ArrayList<List<String>>();
		String temp =type.equals("01")?"4":"2";
		StringBuilder sb = new StringBuilder();
		sb.append("select cbean.type,cbean.repType,cbean.startYear,cbean.endYear,cbean.countR,cbean.total,cbean.value,pbean.RValue, ");
		sb.append("case when cbean.value=0 then 0 else (pbean.RValue/cbean.value)*100 end pt ");
		sb.append("from (select k.serno,k.repType,k.startYear,k.endYear,k.type,k.value,k.total,cont.countR from ( select c.serno,c.repType,c.startYear,c.endYear,isnull(d.type,c.type) type,d.value,d.total from(");
		sb.append("SELECT serno,type,repType,startYear,endYear FROM [Commit] where [IDNO]=? and type=? and enable='1')c ");
		sb.append("left join (SELECT [serno],[type],[value],[total] FROM [CommitDetail] where year='' and enable='1' and isFinancial='')d ");
		sb.append("on c.serno = d.serno)k left join ( SELECT serno,count(distinct repserno) countR,left(repType,2) type FROM [ISAM].[dbo].[CommitReport] where enable='1' and serno in (select serno from ISAM.dbo.[Commit] where IDNO=? and type=?) group by serno,left(repType,2))cont on cont.serno=k.serno ) cbean left join (select a.serno,LEFT(type,").append(temp).append(") as type,SUM(repvalue) as RValue from ");
		sb.append("(SELECT [serno],[repserno] FROM CommitReport where enable='1' and serno in ( ");
		sb.append("SELECT [serno] FROM [ISAM].[dbo].[Commit] where IDNO=? and type=? and enable='1'))a left join ");
		sb.append("( SELECT [repserno],[type],[repvalue] FROM [CommitReportDetail] where enable='1')b on a.repserno=b.repserno ");
		sb.append("group by serno,LEFT(type,").append(temp).append("))pbean on cbean.serno=pbean.serno and cbean.type=pbean.type order by type,startYear");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			stmt.setString(2, type);
			stmt.setString(3, IDNO);
			stmt.setString(4, type);
			stmt.setString(5, IDNO);
			stmt.setString(6, type);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				List<String> row=new ArrayList<String>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					row.add(rs.getString(i)==null?"":rs.getString(i));
				}
				result.add(row);
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
	public List<List<String>> getSummary02Report(String IDNO){
		SQL sqltool = new SQL();
		List<List<String>> result=new ArrayList<List<String>>();
		StringBuilder sb = new StringBuilder();
		sb.append("select isnull(cb.year,pb.year),cb.[0201],cb.[0202],cb.[0203],pb.[0201],pb.[0202],pb.[0203] from ");
		sb.append("(select * from ( SELECT serno,[type] type,[year] year,[value] value FROM CommitDetail where isFinancial='' and year!='' ");
		sb.append("and serno in (SELECT [serno] FROM [Commit] where [IDNO]=? and type='02' and enable='1') ");
		sb.append(") A pivot (MAX(A.value) for A.type in ([0201],[0202],[0203])) as pvt )cb ");
		sb.append("full join ( select * from ( select a.serno,a.Year,b.type,b.repvalue as value from (SELECT [repserno] ");
		sb.append(",[serno],[Year] FROM [CommitReport] where serno in (SELECT [serno] FROM [Commit] where [IDNO]=? and type='02' and enable='1') and enable='1')a ");
		sb.append("left join(SELECT [repserno] ,[type],[repvalue] FROM [CommitReportDetail])b on a.repserno=b.repserno ");
		sb.append(") A pivot (MAX(A.value) for A.type in ([0201],[0202],[0203])) as pvt)pb on cb.serno=pb.serno and cb.year=pb.Year");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			stmt.setString(2, IDNO);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				List<String> row=new ArrayList<String>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					row.add(rs.getString(i)==null?"":rs.getString(i));
				}
				result.add(row);
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
		sb.append("select isnull(cb.year,pb.year),cb.[0301],cb.[0302],pb.[0301] from ");
		sb.append("(select * from ( SELECT serno,[type] type,[year] year,[value] value FROM CommitDetail where isFinancial='' and year!='' ");
		sb.append("and serno in (SELECT [serno] FROM [Commit] where [IDNO]=? and type='03' and enable='1') ");
		sb.append(") A pivot (MAX(A.value) for A.type in ([0301],[0302])) as pvt )cb ");
		sb.append("full join ( select * from ( select a.serno,a.Year,b.type,b.repvalue as value from (SELECT [repserno] ");
		sb.append(",[serno],[Year] FROM [CommitReport] where serno in (SELECT [serno] FROM [Commit] where [IDNO]=? and type='03' and enable='1') and enable='1')a ");
		sb.append("left join(SELECT [repserno] ,[type],[repvalue] FROM [CommitReportDetail])b on a.repserno=b.repserno ");
		sb.append(") A pivot (MAX(A.value) for A.type in ([0301])) as pvt)pb on cb.serno=pb.serno and cb.year=pb.Year");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			stmt.setString(2, IDNO);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				List<String> row=new ArrayList<String>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					row.add(rs.getString(i)==null?"":rs.getString(i));
				}
				result.add(row);
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
	public void insert(List<CommitReportDetail> beans) {
		String forpstmt = "insert into CommitReportDetail values(?,?,?,?,?,?,?)";
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
		String forStmt = "DELETE FROM CommitReportDetail WHERE repSerno = ?";
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
		String forpstmt = "UPDATE CommitReportDetail SET enable='0' WHERE repserno=?";
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
	public void updateCommitReportDetailsBySub(String serno){
		StringBuilder sb = new StringBuilder();
		sb.append("delete CommitReportDetail where repserno in( ");
		sb.append("SELECT repserno FROM CommitReport where enable='1' and serno=?)");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		SQL sqltool = new SQL();
		try {
			sqltool.noCommit();
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			stmt.executeUpdate();
			
			sb.append("insert into CommitReportDetail SELECT repserno,cr.Year,type,corpvalue,repvalue,t,enable ");
			sb.append("FROM CommitReport cr,(select b.Year,b.reptype,b.type,b.corpvalue,b.repvalue,");
			sb.append("case when b.repType='0103' then (convert(int,Year)-convert(int,startYear))+1 ");
			sb.append("else case when b.repType='0101' then (convert(int,Year)-convert(int,startYear)+1)*2-1 ");
			sb.append("else (convert(int,Year)-convert(int,startYear)+1)*2 end end t from (SELECT serno,repType");
			sb.append(",startYear FROM [Commit] where serno=? and enable='1' and type='01')a ,(");
			sb.append("SELECT m.Year,s.type,m.repType,'").append(serno).append("' serno,sum([corpvalue]) [corpvalue]");
			sb.append(",sum([repvalue]) [repvalue] FROM SubCommitReport m left join SubCommitReportDetail s on m.repserno=s.repserno ");
			sb.append("where m.enable='1' and s.enable='1' and m.Subserno in (SELECT subserno FROM SubCommit where serno=? and enable='1'");
			sb.append(")group by  m.Year,s.type,m.repType)b where a.serno=b.serno)crd ");
			sb.append("where serno=? and enable='1' and cr.Year=crd.Year and cr.repType=crd.repType");
			forStmt = sb.toString();
			sb.setLength(0);
			stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			stmt.setString(2, serno);
			stmt.setString(3, serno);
			stmt.executeUpdate();
			sqltool.commit();
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
//	public void updateCommitReportDetailsBySub(String serno){
//		StringBuilder sb = new StringBuilder();
//		sb.append("delete CommitReportDetail where repserno in( ");
//		sb.append("SELECT repserno FROM CommitReport where enable='1' and serno=?)");
//		String forStmt = sb.toString();
////		System.out.println(forStmt);
//		sb.setLength(0);
//		SQL sqltool = new SQL();
//		try {
//			sqltool.noCommit();
//			PreparedStatement stmt = sqltool.prepare(forStmt);
//			stmt.setString(1, serno);
//			stmt.executeUpdate();
//			
//			sb.append("insert into CommitReportDetail SELECT repserno,cr.Year,type,corpvalue,repvalue,t,enable ");
//			sb.append("FROM CommitReport cr,(select b.Year,b.reptype,b.type,b.corpvalue,b.repvalue,");
//			sb.append("case when b.repType='0103' then (convert(int,Year)-convert(int,startYear))+1 ");
//			sb.append("else case when b.repType='0101' then (convert(int,Year)-convert(int,startYear)+1)*2-1 ");
//			sb.append("else (convert(int,Year)-convert(int,startYear)+1)*2 end end t from (SELECT serno,repType");
//			sb.append(",startYear FROM [Commit] where serno=? and enable='1' and type='01')a ,(");
//			sb.append("SELECT d.Year,d.type,t.repType,'").append(serno).append("' serno,sum([corpvalue]) [corpvalue]");
//			sb.append(",sum([repvalue])  [repvalue] FROM SubCommitReportDetail d,(SELECT repserno,Year,repType ");
//			sb.append("FROM SubCommitReport where enable='1' and  Subserno in(SELECT subserno FROM SubCommit ");
//			sb.append("where serno=? and enable='1'))t where enable='1' and d.repserno=t.repserno group by d.Year,d.type,t.repType ");
//			sb.append(")b where a.serno=b.serno)crd where serno=? and enable='1' and cr.Year=crd.Year and cr.repType=crd.repType");
//			forStmt = sb.toString();
//			sb.setLength(0);
//			stmt = sqltool.prepare(forStmt);
//			stmt.setString(1, serno);
//			stmt.setString(2, serno);
//			stmt.setString(3, serno);
//			stmt.executeUpdate();
//			sqltool.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			try{
//				sqltool.close();
//			}catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
	public void updateCommitReportDetailsBySub03(String serno){
		StringBuilder sb = new StringBuilder();
		sb.append("delete CommitReportDetail where repserno in( ");
		sb.append("SELECT repserno FROM CommitReport where enable='1' and serno=?)");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		SQL sqltool = new SQL();
		try {
			sqltool.noCommit();
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			stmt.executeUpdate();
			
			sb.append("insert into CommitReportDetail SELECT repserno,cr.Year,type,corpvalue,repvalue,t,enable ");
			sb.append("FROM CommitReport cr,(select b.Year,b.reptype,b.type,b.corpvalue,b.repvalue,");
			sb.append("(convert(int,Year)-convert(int,startYear))+1 t from (SELECT serno,repType");
			sb.append(",startYear FROM [Commit] where serno=? and enable='1' and type='03')a ,(");
			sb.append("SELECT d.Year,d.type,t.repType,'").append(serno).append("' serno,sum([corpvalue]) [corpvalue]");
			sb.append(",sum([repvalue])  [repvalue] FROM SubCommitReportDetail d,(SELECT repserno,Year,repType ");
			sb.append("FROM SubCommitReport where enable='1' and  Subserno in(SELECT subserno FROM SubCommit ");
			sb.append("where serno=? and enable='1'))t where enable='1' and d.repserno=t.repserno group by d.Year,d.type,t.repType ");
			sb.append(")b where a.serno=b.serno)crd where serno=? and enable='1' and cr.Year=crd.Year and cr.repType=crd.repType");
			forStmt = sb.toString();
			sb.setLength(0);
			
			stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			stmt.setString(2, serno);
			stmt.setString(3, serno);
			stmt.executeUpdate();
			sqltool.commit();
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
}
