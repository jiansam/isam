package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.CommitReport;
import com.isam.helper.SQL;

public class CommitReportDAO {
	
	public int isExists(String serno,String year,String restrainType){
		int result=0;
		SQL sqltool = new SQL();
		String forStmt = "SELECT count(repserno) FROM CommitReport WHERE serno=? and year=? and repType=? and enable='1'";
//		String forStmt = "SELECT count(repserno) FROM CommitReport WHERE serno=? and year=? and left(repType,2)=? and enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			stmt.setString(2, year);
			stmt.setString(3, restrainType);
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
	}
	public int getRepSerno(String serno,String year,String restrainType){
		int result=-1;
		SQL sqltool = new SQL();
		String forStmt = "SELECT repserno FROM CommitReport WHERE serno=? and year=? and repType=? and enable='1'";
//		String forStmt = "SELECT repserno FROM CommitReport WHERE serno=? and year=? and left(repType,2)=? and enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			stmt.setString(2, year);
			stmt.setString(3, restrainType);
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
	}
	public CommitReport select(String repserno){
		CommitReport bean= new CommitReport();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM CommitReport WHERE repserno=? and enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, repserno);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean.setRepserno(rs.getInt(1));
				bean.setSerno(rs.getInt(2));
				bean.setYear(rs.getString(3));
				bean.setRepType(rs.getString(4));
				bean.setUpdatetime(rs.getTimestamp(5));
				bean.setUpdateuser(rs.getString(6));
				bean.setCreatetime(rs.getTimestamp(7));
				bean.setCreateuser(rs.getString(8));
				bean.setEnable(rs.getString(9));
				bean.setIsOnline(rs.getString(10));
				bean.setKeyinNo(rs.getString(11));
				bean.setIsConversion(rs.getString(12));
				bean.setNote(rs.getString(13));
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
	public List<List<String>> getReportPivot(String IDNO){
		SQL sqltool = new SQL();
		List<List<String>> result=new ArrayList<List<String>>();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (	SELECT [repserno] as value,[Year] as year,[repType] as type FROM CommitReport where enable='1' ");
		sb.append("and serno in (select serno from [Commit] where IDNO=? and enable='1'))a pivot(");
		sb.append("Max(value) for a.type in([0101],[0102],[0103],[02],[03]))as pvt");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			ResultSet rs = stmt.executeQuery();
			int count =rs.getMetaData().getColumnCount();
			while(rs.next()){
				List<String> row=new ArrayList<String>();
				for (int i = 1; i <= count; i++) {
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
	public int insert(CommitReport bean) {
		int result = 0;
		String forStmt = "insert into CommitReport values(?,?,?,?,?,?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setInt(1, bean.getSerno());
			pstmt.setString(2, bean.getYear());
			pstmt.setString(3, bean.getRepType());
			pstmt.setTimestamp(4, bean.getUpdatetime());
			pstmt.setString(5, bean.getUpdateuser());
			pstmt.setTimestamp(6, bean.getCreatetime());
			pstmt.setString(7, bean.getCreateuser());
			pstmt.setString(8, bean.getEnable());
			pstmt.setString(9, bean.getIsOnline());
			pstmt.setString(10, bean.getKeyinNo());
			pstmt.setString(11, bean.getIsConversion());
			pstmt.setString(12, bean.getNote());
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
	public int update(CommitReport bean) {
		int result = 0;
		String forpstmt = "UPDATE CommitReport SET updatetime=?,updateuser=?,keyinNo=?,isConversion=?,note=? WHERE repserno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setTimestamp(1, bean.getUpdatetime());
			pstmt.setString(2, bean.getUpdateuser());
			pstmt.setString(3, bean.getKeyinNo());
			pstmt.setString(4, bean.getIsConversion());
			pstmt.setString(5, bean.getNote());
			pstmt.setInt(6, bean.getRepserno());
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
	public int unable(String repserno) {
		int result = 0;
//		String forpstmt = "UPDATE CommitReport SET enable='0' WHERE repserno=?";
		String forpstmt = "delete CommitReport WHERE repserno=?";
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
	public void unableBySerno(String serno) {
		String forpstmt = "delete CommitReportDetail where repserno in (select repserno from CommitReport where serno=?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, serno);
			pstmt.executeUpdate();
//			pstmt = sqltool.prepare("UPDATE CommitReportDetail SET enable='0' where repserno in (select repserno from CommitReport where serno=? and enable='0')");
			pstmt = sqltool.prepare("delete CommitReport where serno=? ");
			pstmt.setString(1, serno);
			pstmt.executeUpdate();
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
//	public void unableBySerno(String serno) {
//		String forpstmt = "UPDATE CommitReport SET enable='0' WHERE serno=?";
//		SQL sqltool = new SQL();
//		try {
//			PreparedStatement pstmt = sqltool.prepare(forpstmt);
//			pstmt.setString(1, serno);
//			pstmt.executeUpdate();
////			pstmt = sqltool.prepare("UPDATE CommitReportDetail SET enable='0' where repserno in (select repserno from CommitReport where serno=? and enable='0')");
//			pstmt = sqltool.prepare("delete CommitReportDetail where repserno in (select repserno from CommitReport where serno=? and enable='0')");
//			pstmt.setString(1, serno);
//			pstmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				sqltool.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	public Map<String,Map<String,String>> getMaxMinYearQuarter(){
		Map<String,Map<String,String>> map = new HashMap<String, Map<String,String>>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT max([Year]) maxyear,MIN([Year]) minyear,LEFT([repType],2) repType FROM [CommitReport] where enable='1' group by LEFT([repType],2)";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			int count=meta.getColumnCount();
			while(rs.next()){
				Map<String,String> sub = new HashMap<String, String>();
				for(int i=1;i<=count;i++){
					sub.put(meta.getColumnName(i),rs.getString(i));
				}
				map.put(rs.getString(3), sub);
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
		return map;
	}
	
	public List<Map<String,String>> getReportList(String year,String type,String repType,String repState){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ( select [IDNO],[type],[state],[startYear],[endYear],");
		sb.append("case when isnull(cr.serno,'')=0 then '0' else '1' end repState from (");
		sb.append("SELECT [serno],[IDNO],[type],[state],[repType],[startYear],[endYear] FROM [Commit] ");
		sb.append("where enable='1' and type=? and startYear<=? and endYear>=? and repType=isnull(?,repType)) c ");
		sb.append("left join (select serno from [CommitReport] where enable='1' and Year=? and repType=isnull(?,repType))cr ");
		sb.append("on c.serno= cr.serno)rep where rep.repState=ISNULL(?,rep.repState)");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		
		SQL sqltool = new SQL();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, type);
			stmt.setString(2, year);
			stmt.setString(3, year);
			if(!type.equals("01")){
				repType=null;
			}
			if(repType!=null&&(repType.equals("0101")||repType.equals("0102"))){
				stmt.setString(4, type);
			}else if(repType!=null&&(repType.equals("0103"))){
				stmt.setString(4, repType);
			}else{
				stmt.setString(4, null);
			}
			stmt.setString(5, year);
			stmt.setString(6, repType);
			stmt.setString(7, repState);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Map<String,String> map = new HashMap<String, String>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					map.put(rs.getMetaData().getColumnName(i), rs.getString(i)==null?"":rs.getString(i));
				}
				list.add(map);
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
		return list;
	}
	
	public List<Map<String,String>> getReportList(String syear,String eyear,String type,String repType,String repState,String idno,String investment){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (SELECT b.year,type,c.IDNO,state,case when isnull(cr.serno,'')=0 then '0' else '1' end repState,");
		sb.append("isnull(cic.reNos,cir.reNo) reNos,cic.contact FROM (SELECT * FROM getTWYearList(?,?))b left join ");
		sb.append("[Commit] c on b.year>=c.startYear and  b.year<=c.endYear left join (select serno,year,repType from ");
		sb.append("CommitReport where enable='1' and Year>=? and year<=? and repType=isnull(?,repType))cr ");
		sb.append("on cr.serno=c.serno and cr.year=b.year left join(SELECT * FROM CommitXContactXReceiveNo())cic ");
		sb.append("on cic.IDNO=c.IDNO left join (SELECT max(receiveNo) reNo,serno FROM CommitXReceiveNo group by serno)cir ");
		sb.append("on c.serno=cir.serno where enable='1' and type=? and c.repType=isnull(?,c.repType) and c.idno=isnull(?,c.idno) ");
		sb.append("and c.idno in (SELECT idno FROM CDataInvestor where INVESTOR_CHTNAME like ?))x where repState=isnull(?,repState) order by year desc,idno");
		
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		
		SQL sqltool = new SQL();
		int count=1;
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(count++, syear);
			stmt.setString(count++, eyear);
			stmt.setString(count++, syear);
			stmt.setString(count++, eyear);
			if(!type.equals("01")){
				repType=null;
			}
			stmt.setString(count++, repType);
			stmt.setString(count++, type);
			if(repType!=null&&(repType.equals("0101")||repType.equals("0102"))){
				stmt.setString(count++, type);
			}else if(repType!=null&&(repType.equals("0103"))){
				stmt.setString(count++, repType);
			}else{
				stmt.setString(count++, null);
			}
			stmt.setString(count++, idno.isEmpty()?null:idno);
			stmt.setString(count++, "%"+investment+"%");
			stmt.setString(count++, repState);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Map<String,String> map = new HashMap<String, String>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					map.put(rs.getMetaData().getColumnName(i), rs.getString(i)==null?"":rs.getString(i));
				}
				list.add(map);
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
		return list;
	}
	
	public void updateCommitReportBySub(String serno,String updateuser){
		StringBuilder sb = new StringBuilder();
		sb.append("merge into CommitReport using(SELECT '").append(serno);
		sb.append("' serno,Year,repType,GETDATE() updatetime,'").append(updateuser);
		sb.append("' updateuser,GETDATE() createtime,'").append(updateuser);
		sb.append("' createuser,'1' enable,'N' isOnline,'' keyinNo,'N' isConversion,'' note ");
		sb.append("FROM SubCommitReport where enable='1' and  Subserno in(SELECT subserno ");
		sb.append("FROM SubCommit where serno=? and enable='1') group by Year,repType,enable) tmp ");
		sb.append("on CommitReport.serno=tmp.serno and CommitReport.year=tmp.year and CommitReport.repType=tmp.repType and CommitReport.enable='1' ");
		sb.append("when matched then update set CommitReport.updatetime=tmp.updatetime,CommitReport.updateuser=tmp.updateuser ");
		sb.append("when not matched then insert values(serno,year,reptype,updatetime,updateuser,createtime,createuser,enable,isonline,keyinNo,isconversion,note);");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		SQL sqltool = new SQL();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			stmt.executeUpdate();
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
	public void checkCommitReportHasSubData(String serno,String updateuser){
		StringBuilder sb = new StringBuilder();
		sb.append("update CommitReport set enable='0' ,updatetime=GETDATE(),updateuser=? ");
		sb.append("where serno=? and enable='1' and repserno not in (SELECT repserno FROM ");
		sb.append("CommitReportDetail where repserno in (SELECT repserno FROM CommitReport ");
		sb.append("where serno=? and enable='1'))");
		String forStmt = sb.toString();
		sb.setLength(0);
		SQL sqltool = new SQL();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, updateuser);
			stmt.setString(2, serno);
			stmt.setString(3, serno);
			stmt.executeUpdate();
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
