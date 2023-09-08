package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dasin.tools.dPairHashMap;

import com.isam.bean.ProjectContact;
import com.isam.bean.ProjectReport;
import com.isam.bean.ProjectReportName;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class ProjectReportDAO {
	
	public boolean isMaxYearQuarter(String year,String quarter,int serno){
		SQL sqltool = new SQL();
		boolean result=false;
		String forStmt = "SELECT count(repSerno) FROM ProjectReport WHERE year>=? and quarter>? and serno = ? and enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, year);
			stmt.setString(2, quarter);
			stmt.setInt(3, serno);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				if(rs.getInt(1)>0){
					result=true;
				}
			}
			
			rs.close();
			stmt.close();
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
	
	public boolean isExists(int serno,String repType,String year,String quarter){
		SQL sqltool = new SQL();
		boolean result=false;
		String forStmt = "SELECT count(repSerno) FROM ProjectReport WHERE serno = ? and repType = ? and year=? and quarter=? and enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setInt(1, serno);
			stmt.setString(2, repType);
			stmt.setString(3, year);
			stmt.setString(4, quarter);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				if(rs.getInt(1)>0){
					 result=true;
				}
			}
			
			rs.close();
			stmt.close();
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
	
	public ProjectReport select(int serno,String repType,String year,String quarter,String enable){
		SQL sqltool = new SQL();
		ProjectReport bean= new ProjectReport();
		String forStmt = "SELECT * FROM ProjectReport WHERE serno = ? and repType = ? and year=? and quarter=? and enable=isnull(?,enable)";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setInt(1, serno);
			stmt.setString(2, repType);
			stmt.setString(3, year);
			stmt.setString(4, quarter);
			stmt.setString(5, enable);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean.setRepSerno(rs.getInt("repSerno"));
				bean.setSerno(rs.getInt("serno"));
				bean.setKeyinNo(rs.getString("keyinNo"));
				bean.setIsOnline(rs.getString("isOnline"));
				bean.setRepType(rs.getString("repType"));
				bean.setYear(rs.getString("year"));
				bean.setQuarter(rs.getString("quarter"));
				bean.setOutwardMoney(DataUtil.nulltoempty(rs.getString("outwardMoney")));
				bean.setOutwardNote(DataUtil.nulltoempty(rs.getString("outwardNote")));
				bean.setApprovalMoney(DataUtil.nulltoempty(rs.getString("approvalMoney")));
				bean.setApprovalNote(DataUtil.nulltoempty(rs.getString("approvalNote")));
				bean.setApprovedMoney(DataUtil.nulltoempty(rs.getString("approvedMoney")));
				bean.setApprovedNote(DataUtil.nulltoempty(rs.getString("approvedNote")));
				bean.setCompleteMoney(DataUtil.nulltoempty(rs.getString("completeMoney")));
				bean.setInvestMoney(DataUtil.nulltoempty(rs.getString("investMoney")));
				bean.setFinancial(rs.getString("financial"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setEnable(rs.getString("enable"));
				bean.setIsConversion(rs.getString("isConversion"));
				bean.setNote(rs.getString("note"));
				bean.setNoNeed(rs.getString("noNeed"));
				bean.setNoNeedNote(rs.getString("noNeedNote"));
				bean.setPusername(DataUtil.nulltoempty(rs.getString("pusername")));
				bean.setReceiveDate(DataUtil.nulltoempty(rs.getString("receiveDate")));
				bean.setSendDate(DataUtil.nulltoempty(rs.getString("sendDate")));
			}
			
			rs.close();
			stmt.close();
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
	
	/*待改不須填寫的部分*/
	public List<List<String>> getReportPivot(int serno){
		SQL sqltool = new SQL();
		List<List<String>> result=new ArrayList<List<String>>();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ( SELECT CAST([repSerno] as nvarchar(10)) as no,[year] Y,[quarter] Q FROM ProjectReport ");
		sb.append("where serno=? and enable='1' union all SELECT financial as no,[year] Y,'5' Q FROM ProjectReport ");
		sb.append("where serno=? and enable='1' and repType='Y') A  pivot ( max(A.No) for A.Q in ([1],[2],[3],[4],[5])) as pvt order by y desc");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setInt(1, serno);
			stmt.setInt(2, serno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				List<String> row=new ArrayList<String>();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					row.add(DataUtil.nulltoempty(rs.getString(i)));
				}
				result.add(row);
			}
			
			rs.close();
			stmt.close();
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
	
	public ProjectReport selectByRepSerno(int repSerno,String enable){
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM ProjectReport WHERE repSerno=? and enable=isnull(?,enable)";
		ProjectReport bean= null;
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setInt(1, repSerno);
			stmt.setString(2, enable);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean= new ProjectReport();
				bean.setRepSerno(rs.getInt("repSerno"));
				bean.setSerno(rs.getInt("serno"));
				bean.setKeyinNo(rs.getString("keyinNo"));
				bean.setIsOnline(rs.getString("isOnline"));
				bean.setRepType(rs.getString("repType"));
				bean.setYear(rs.getString("year"));
				bean.setQuarter(rs.getString("quarter"));
				bean.setOutwardMoney(DataUtil.nulltoempty(rs.getString("outwardMoney")));
				bean.setOutwardNote(DataUtil.nulltoempty(rs.getString("outwardNote")));
				bean.setApprovalMoney(DataUtil.nulltoempty(rs.getString("approvalMoney")));
				bean.setApprovalNote(DataUtil.nulltoempty(rs.getString("approvalNote")));
				bean.setApprovedMoney(DataUtil.nulltoempty(rs.getString("approvedMoney")));
				bean.setApprovedNote(DataUtil.nulltoempty(rs.getString("approvedNote")));
				bean.setCompleteMoney(DataUtil.nulltoempty(rs.getString("completeMoney")));
				bean.setInvestMoney(DataUtil.nulltoempty(rs.getString("investMoney")));
				bean.setFinancial(rs.getString("financial"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setEnable(rs.getString("enable"));
				bean.setIsConversion(rs.getString("isConversion"));
				bean.setNote(rs.getString("note"));
				bean.setNoNeed(rs.getString("noNeed"));
				bean.setNoNeedNote(rs.getString("noNeedNote"));
				bean.setPusername(DataUtil.nulltoempty(rs.getString("pusername")));
				bean.setReceiveDate(DataUtil.nulltoempty(rs.getString("receiveDate")));
				bean.setSendDate(DataUtil.nulltoempty(rs.getString("sendDate")));
			}
			
			rs.close();
			stmt.close();
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
	
	public void insert(ProjectReport bean) {
		String forpstmt = "INSERT INTO ProjectReport VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setInt(1, bean.getSerno());
			pstmt.setString(2, bean.getKeyinNo());
			pstmt.setString(3, bean.getIsOnline());
			pstmt.setString(4,bean.getRepType());
			pstmt.setString(5,bean.getYear());
			pstmt.setString(6,bean.getQuarter());
			pstmt.setString(7,bean.getOutwardMoney());
			pstmt.setString(8,bean.getOutwardNote());
			pstmt.setString(9,bean.getApprovalMoney());
			pstmt.setString(10,bean.getApprovalNote());
			pstmt.setString(11,bean.getApprovedMoney());
			pstmt.setString(12,bean.getApprovedNote());
			pstmt.setString(13,bean.getCompleteMoney());
			pstmt.setString(14,bean.getInvestMoney());
			pstmt.setString(15,bean.getFinancial());
			pstmt.setTimestamp(16,bean.getUpdatetime());
			pstmt.setString(17,bean.getUpdateuser());
			pstmt.setTimestamp(18,bean.getCreatetime());
			pstmt.setString(19,bean.getCreateuser());
			pstmt.setString(20,bean.getEnable());
			pstmt.setString(21, bean.getIsConversion());
			pstmt.setString(22, bean.getNote());
			pstmt.setString(23, bean.getNoNeed());
			pstmt.setString(24, bean.getNoNeedNote());
			pstmt.setString(25, bean.getPusername());
			pstmt.setString(26, bean.getReceiveDate());
			pstmt.setString(27, bean.getSendDate());
			pstmt.executeUpdate();
			
			pstmt.close();
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
	
	public int update(ProjectReport bean) {
		int result = 0;
		String forpstmt = "UPDATE ProjectReport SET keyinNo = ?,outwardMoney = ?,approvalMoney=?,approvedMoney=?,completeMoney=?,investMoney=?,financial=?,updatetime=?,updateuser=?,isConversion=?,note=?,noNeed=?,noNeedNote=?,outwardNote=?,approvalNote=?,approvedNote=? WHERE repSerno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getKeyinNo());
			pstmt.setString(2,bean.getOutwardMoney());
			pstmt.setString(3,bean.getApprovalMoney());
			pstmt.setString(4,bean.getApprovedMoney());
			pstmt.setString(5,bean.getCompleteMoney());
			pstmt.setString(6,bean.getInvestMoney());
			pstmt.setString(7,bean.getFinancial());
			pstmt.setTimestamp(8,bean.getUpdatetime());
			pstmt.setString(9,bean.getUpdateuser());
			pstmt.setString(10,bean.getIsConversion());
			pstmt.setString(11,bean.getNote());
			pstmt.setString(12, bean.getNoNeed());
			pstmt.setString(13, bean.getNoNeedNote());
			pstmt.setString(14, bean.getOutwardNote());
			pstmt.setString(15, bean.getApprovalNote());
			pstmt.setString(16, bean.getApprovedNote());
			pstmt.setInt(17,bean.getRepSerno());
			result=pstmt.executeUpdate();	
			
			pstmt.close();
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
	
	public void unable(int repSerno,String updateuser,java.sql.Timestamp time) {
		String forpstmt = "UPDATE ProjectReport SET enable ='0',updateuser=?,updatetime=? WHERE repSerno=? ";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, updateuser);
			pstmt.setTimestamp(2, time);
			pstmt.setInt(3, repSerno);
			pstmt.executeUpdate();
			
			pstmt.close();
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
	
	public Map<String,String> getMaxMinYearQuarter(){
		Map<String,String> map = new HashMap<String, String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT a.maxyear,a.maxQ,b.minyear,b.minQ from ");
		sb.append("(select MAX(year) maxyear,max([quarter]) maxQ ");
		sb.append("FROM ProjectReport where enable='1' and year = (select MAX(year) FROM ProjectReport where enable='1'))a, ");
		sb.append("(select MIN(year) minyear,MIN([quarter])minQ ");
		sb.append("FROM ProjectReport where enable='1' and year = (select min(year) FROM ProjectReport where enable='1'))b");
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					map.put(rs.getMetaData().getColumnName(i), DataUtil.nulltoempty(rs.getString(i)));
				}
			}
			
			rs.close();
			stmt.close();
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
	
	public String getNeedAlert(int serno){
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select count(serno) from (select * from (select ROW_NUMBER() over(PARTITION BY ");
		sb.append("serno order by serno,year desc,quarter desc) no,serno,approvalMoney,approvedMoney ");
		sb.append("FROM [ProjectReport] where enable=1)x where x.no=1 and approvalMoney!=0 and approvalMoney ");
		sb.append("is not null and serno=?)k where (k.approvedMoney/nullif(k.approvalMoney,0))>=1");
		String forStmt = sb.toString();
		sb.setLength(0);
		String result="0";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setInt(1, serno);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result=rs.getString(1);
			}
			
			rs.close();
			stmt.close();
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
	
	public Map<String,String> getNoNeedMap(String investNo){
		SQL sqltool = new SQL();
		String forStmt = "SELECT repSerno,noNeed FROM ProjectReport where serno in (select serno from Project where investNo=?) and enable=1";
		Map<String,String> result= new HashMap<String, String>();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.put(rs.getString(1), rs.getString(2));
			}
			rs.close();
			stmt.close();
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
	
	public List<Map<String,String>> getReportList(String year,String quarter,String repState,String projDate){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ( "
				+ "SELECT investNo, IDNO, state, "
				+ "CASE WHEN ISNULL(PR.serno,'') = 0 then '0' ELSE "
				+ "CASE WHEN noNeed = 1 THEN '99' ELSE '1' END "
				+ "END repState, "
				+ "financial, porjDate, noNeedNote, "
				+ "PC.contact_name, PC.contact_tel_no, PC.COUNTY_NAME, PC.TOWN_NAME, PC.contact_ADDRESS "
				+ "FROM (SELECT [serno],[investNo],[IDNO],[state],porjDate FROM [Project] where state!='04' and state!='03') p ");
		sb.append("LEFT OUTER JOIN ( "
				+ "SELECT serno, keyinNo, financial, noNeed, noNeedNote "
				+ "FROM [ProjectReport] WHERE year = ? AND quarter = ? AND enable = '1' "
				+ ") PR ON p.serno = PR.serno "
				+ "LEFT OUTER JOIN ProjectContact PC ON PC.receive_no = PR.keyinNo "
				+ ") rep "
				+ "WHERE rep.repState=ISNULL(?,rep.repState) AND (rep.porjDate='' or rep.porjDate<=?) ");
		
		if(!quarter.equals("4")){
			sb.append("and rep.state!='02'");
		}
		
		SQL sqltool = new SQL();
		try {
			PreparedStatement stmt = sqltool.prepare(sb.toString());
			stmt.setString(1, year);
			stmt.setString(2, quarter);
			stmt.setString(3, repState);
			stmt.setString(4, projDate);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				Map<String,String> map = new HashMap<String, String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					map.put(meta.getColumnName(i), rs.getString(i)==null?"":rs.getString(i));
				}
				list.add(map);
			}
			
			rs.close();
			stmt.close();
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
	
	public List<Map<String,String>> getDifferList(String year,String quarter,String serno,String respdate){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT b.[serno],a.IDNO,a.investNo,b.[year],b.[quarter],b.[approvalMoney] k1,b.[approvedMoney] k2 ");
		sb.append(",a.money1 d1,a.money2 d2,case when CONVERT(float,b.[approvalMoney])-CONVERT(float,a.money1)!=0  ");
		sb.append("or (b.[approvalMoney] is null or a.money1 is null) then '1' else '' end as s1 ");
		sb.append(",case when CONVERT(float,b.approvedMoney)-CONVERT(float,a.money2)!=0  or (b.approvedMoney is null ");
		sb.append("or a.money2 is null) then '1' else '' end as s2 FROM  ProjectReport b ");
		sb.append("left join (select p.serno,p.investNo,p.IDNO,p.state,k.money1,k.money2 from [ISAM].[dbo].[Project] p left join (");
		sb.append("SELECT a.investNo,data.[ID_NO],sum([MONEY1]) money1,sum([MONEY2]) money2 FROM CDataICMFN405 data,CDataInvestment a ");
		sb.append("where [RESP_DATE] <=? and a.INVESTMENT_NO=data.INVEST_NO and LEN(data.[ID_NO])=8 ");
		sb.append("group by [ID_NO],a.investNo) k on k.ID_NO=p.IDNO and k.investNo=p.investNo)a ");
		sb.append("on a.serno=b.serno where b.year=? and b.quarter=? and b.enable='1' and b.serno=isnull(?,b.serno) and b.serno!=-1");
		String forStmt = sb.toString();
		sb.setLength(0);
		SQL sqltool = new SQL();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, respdate);
			stmt.setString(2, year);
			stmt.setString(3, quarter);
			stmt.setString(4, serno);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				Map<String,String> map = new HashMap<String, String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					map.put(meta.getColumnName(i), rs.getString(i)==null?"":rs.getString(i));
				}
				list.add(map);
			}
			
			rs.close();
			stmt.close();
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
	
	public List<ProjectReportName> select(String enable){
		SQL sqltool = new SQL();
		List<ProjectReportName> result=new ArrayList<ProjectReportName>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT distinct pr.*,p.investNo,p.IDNO,c.COMP_CHTNAME investment,d.INVESTOR_CHTNAME investor FROM ProjectReport pr");
		sb.append(",(SELECT serno,investNo,IDNO FROM Project) p,CDataInvestment c,CDataInvestor d ");
		sb.append("WHERE enable=? and pr.serno=p.serno and p.investNo=c.investNo and p.IDNO=d.IDNO");
		String forStmt = sb.toString();
		sb.setLength(0);
		
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, enable);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				ProjectReportName bean= new ProjectReportName();
				bean.setInvestNo(rs.getString("investNo"));
				bean.setIdno(rs.getString("IDNO"));
				bean.setInvestment(rs.getString("investment"));
				bean.setInvestor(rs.getString("investor"));
				bean.setRepSerno(rs.getInt("repSerno"));
				bean.setSerno(rs.getInt("serno"));
				bean.setKeyinNo(rs.getString("keyinNo"));
				bean.setIsOnline(rs.getString("isOnline"));
				bean.setRepType(rs.getString("repType"));
				bean.setYear(rs.getString("year"));
				bean.setQuarter(rs.getString("quarter"));
				bean.setOutwardMoney(DataUtil.nulltoempty(rs.getString("outwardMoney")));
				bean.setOutwardNote(DataUtil.nulltoempty(rs.getString("outwardNote")));
				bean.setApprovalMoney(DataUtil.nulltoempty(rs.getString("approvalMoney")));
				bean.setApprovalNote(DataUtil.nulltoempty(rs.getString("approvalNote")));
				bean.setApprovedMoney(DataUtil.nulltoempty(rs.getString("approvedMoney")));
				bean.setApprovedNote(DataUtil.nulltoempty(rs.getString("approvedNote")));
				bean.setCompleteMoney(DataUtil.nulltoempty(rs.getString("completeMoney")));
				bean.setInvestMoney(DataUtil.nulltoempty(rs.getString("investMoney")));
				bean.setFinancial(rs.getString("financial"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setEnable(rs.getString("enable"));
				bean.setIsConversion(rs.getString("isConversion"));
				bean.setNote(rs.getString("note"));
				bean.setNoNeed(rs.getString("noNeed"));
				bean.setNoNeedNote(rs.getString("noNeedNote"));
				bean.setPusername(DataUtil.nulltoempty(rs.getString("pusername")));
				bean.setReceiveDate(DataUtil.nulltoempty(rs.getString("receiveDate")));
				bean.setSendDate(DataUtil.nulltoempty(rs.getString("sendDate")));
				result.add(bean);
			}
			
			rs.close();
			stmt.close();
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
	
	public List<ProjectReportName> getConfirmList(){
		SQL sqltool = new SQL();
		List<ProjectReportName> result=new ArrayList<ProjectReportName>();
		
		try {
			PreparedStatement stmt = sqltool.prepare(
				"SELECT A.*, B.investNo, B.IDNO, C.COMP_CHTNAME investment, D.INVESTOR_CHTNAME investor, E.* "
				+ "FROM ProjectReport A "
				+ "INNER JOIN Project B ON A.serno = B.serno AND A.enable = 2 "
				+ "INNER JOIN CDataInvestment C ON B.investNo = C.investNo "
				+ "INNER JOIN CDataInvestor D ON B.IDNO = D.IDNO "
				+ "LEFT OUTER JOIN VW_ProjectReportConfirmList E ON A.repSerno = E.repSerno "
			);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				ProjectReportName bean= new ProjectReportName();
				bean.setInvestNo(rs.getString("investNo"));
				bean.setIdno(rs.getString("IDNO"));
				bean.setInvestment(rs.getString("investment"));
				bean.setInvestor(rs.getString("investor"));
				bean.setRepSerno(rs.getInt("repSerno"));
				bean.setSerno(rs.getInt("serno"));
				
				String keyinNoStatus = rs.getString("keyinNoStatus");
				bean.setKeyinNo(keyinNoStatus.isEmpty() ? rs.getString("keyinNo") : 
					rs.getString("keyinNo") + "<br><span style='color:red'>" + keyinNoStatus + "</span>");
				
				bean.setIsOnline(rs.getString("isOnline"));
				bean.setRepType(rs.getString("repType"));
				bean.setYear(rs.getString("year"));
				bean.setQuarter(rs.getString("quarter"));
				
				String outwardMoneyStatus = rs.getString("outwardMoneyStatus");
				bean.setOutwardMoney(outwardMoneyStatus.isEmpty() ? DataUtil.formatString(rs.getString("outwardMoney")) : 
					DataUtil.formatString(rs.getString("outwardMoney")) + "<br><span style='color:red'>" + DataUtil.formatString(outwardMoneyStatus) + "</span>");
				
				String outwardNoteStatus = rs.getString("outwardNoteStatus");
				bean.setOutwardNote(outwardNoteStatus.isEmpty() ? rs.getString("outwardNote") : 
					rs.getString("outwardNote") + "<br><span style='color:red'>" + outwardNoteStatus + "</span>");
				
				String approvalMoneyStatus = rs.getString("approvalMoneyStatus");
				bean.setApprovalMoney(approvalMoneyStatus.isEmpty() ? DataUtil.formatString(rs.getString("approvalMoney")) : 
					DataUtil.formatString(rs.getString("approvalMoney")) + "<br><span style='color:red'>" + DataUtil.formatString(approvalMoneyStatus) + "</span>");

				String approvalNoteStatus = rs.getString("approvalNoteStatus");
				bean.setApprovalNote(approvalNoteStatus.isEmpty() ? rs.getString("approvalNote") : 
					rs.getString("approvalNote") + "<br><span style='color:red'>" + approvalNoteStatus + "</span>");

				String approvedMoneyStatus = rs.getString("approvedMoneyStatus");
				bean.setApprovedMoney(approvalMoneyStatus.isEmpty() ? DataUtil.formatString(rs.getString("approvedMoney")) : 
					DataUtil.formatString(rs.getString("approvedMoney")) + "<br><span style='color:red'>" + DataUtil.formatString(approvedMoneyStatus) + "</span>");
				
				String approvedNoteStatus = rs.getString("approvedNoteStatus");
				bean.setApprovedNote(approvedNoteStatus.isEmpty() ? rs.getString("approvedNote") : 
					rs.getString("approvedNote") + "<br><span style='color:red'>" + approvedNoteStatus + "</span>");
				
				bean.setCompleteMoney(DataUtil.nulltoempty(rs.getString("completeMoney")));
				bean.setInvestMoney(DataUtil.nulltoempty(rs.getString("investMoney")));
				bean.setFinancial(rs.getString("financial"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setEnable(rs.getString("enable"));
				bean.setIsConversion(rs.getString("isConversion"));
				bean.setNote(rs.getString("note"));
				bean.setNoNeed(rs.getString("noNeed"));
				bean.setNoNeedNote(rs.getString("noNeedNote"));
				bean.setPusername(DataUtil.nulltoempty(rs.getString("pusername")));
				
				String receiveDateStatus = rs.getString("receiveDateStatus");
				bean.setReceiveDate(receiveDateStatus.isEmpty() ? rs.getString("receiveDate") : 
					rs.getString("receiveDate") + "<br><span style='color:red'>" + receiveDateStatus + "</span>");
				
				String sendDateStatus = rs.getString("sendDateStatus");
				bean.setSendDate(sendDateStatus.isEmpty() ? rs.getString("sendDate") : 
					rs.getString("sendDate") + "<br><span style='color:red'>" + sendDateStatus + "</span>");
				
				result.add(bean);
			}
			
			rs.close();
			stmt.close();
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
	
	@SuppressWarnings("resource")
	public void overwrite(String repSernos,String updateuser,java.sql.Timestamp updatetime) {
		StringBuilder sb =new StringBuilder();
		/*將文號移到enable=2的下面*/
		sb.append("UPDATE ProjectXReciveNo set repSerno = i.new FROM (SELECT a.repSerno old,b.repSerno new ");
		sb.append("FROM ProjectReport a,(select repSerno,serno,year,quarter from ProjectReport ");
		sb.append("where enable='2' and repSerno in (").append(repSernos).append("))b ") ;
		sb.append("where enable='1' and a.serno=b.serno and a.year=b.year and a.quarter=b.quarter) i ");
		sb.append("WHERE ProjectXReciveNo.repSerno=i.old ");
		String forpstmt = sb.toString();
		sb.setLength(0);
		SQL sqltool = new SQL();
		PreparedStatement pstmt =null;
		try {
			sqltool.noCommit();
			pstmt = sqltool.prepare(forpstmt);
			pstmt.executeUpdate();
			pstmt.close();
			
			/*新增參考資料到已排除，避免匯入資料修改後，造成的二次匯入*/
			sb.append("insert into ProjectReport SELECT serno,keyinNo,isOnline,repType,year,quarter,outwardMoney");
			sb.append(",outwardNote,approvalMoney,approvalNote,approvedMoney,approvedNote,completeMoney");
			sb.append(",investMoney,financial,updatetime,updateuser,createtime,createuser,'3' enable");
			sb.append(",isConversion,note,noNeed,noNeedNote,pusername,receiveDate,sendDate FROM ProjectReport ");
			sb.append("where enable='2' and repSerno in (").append(repSernos).append(")") ;
			forpstmt = sb.toString();
			sb.setLength(0);
			pstmt = sqltool.prepare(forpstmt);
			pstmt.executeUpdate();
			pstmt.close();
			
			/*把原本的enable=1移除*/
			sb.append("update ProjectReport set enable='0',updateuser=?,updatetime=? where repSerno in (");
			sb.append("SELECT a.repSerno old FROM ProjectReport a,(select repSerno,serno,year,quarter from ProjectReport ");
			sb.append("where enable='2' and repSerno in (").append(repSernos).append("))b where enable='1' ") ;
			sb.append("and a.serno=b.serno and a.year=b.year and a.quarter=b.quarter) ");
			forpstmt = sb.toString();
			sb.setLength(0);
			pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, updateuser);
			pstmt.setTimestamp(2, updatetime);
			pstmt.executeUpdate();
			pstmt.close();
			
			/*把原本的enable=2改為enable=1*/
			sb.append("update ProjectReport set enable='1',updateuser=?,updatetime=? where repSerno in (");
			sb.append("select repSerno from ProjectReport where enable='2' and repSerno in (");
			sb.append(repSernos).append("))") ;
			forpstmt = sb.toString();
			sb.setLength(0);
			pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, updateuser);
			pstmt.setTimestamp(2, updatetime);
			pstmt.executeUpdate();
			pstmt.close();
			
			sqltool.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(pstmt != null){
				try{
					pstmt.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
				pstmt = null;
			}
			try {
				sqltool.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void forDBReference(String repSernos,String updateuser,java.sql.Timestamp time) {
		StringBuilder sb =new StringBuilder();
		sb.append("UPDATE ProjectReport SET enable ='3',updateuser=?,updatetime=? ");
		sb.append("WHERE repSerno in (").append(repSernos).append(")") ;
		String forpstmt = sb.toString();
		sb.setLength(0);
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, updateuser);
			pstmt.setTimestamp(2, time);
			pstmt.executeUpdate();	
			pstmt.close();
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
	
	public int checkUnConfirm(String repSerno){
		SQL sqltool = new SQL();
		int result=0;
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT count(repSerno) c FROM ProjectReport b where enable='2' and EXISTS (");
		sb.append("SELECT repSerno,year,quarter,serno FROM ProjectReport a where repSerno=? ");
		sb.append("and a.year=b.year and a.quarter=b.quarter and a.serno=b.serno)");
		String forStmt = sb.toString();
		sb.setLength(0);
		PreparedStatement stmt =null;
		ResultSet rs =null;
		try {
			stmt = sqltool.prepare(forStmt);
			stmt.setString(1, repSerno);
			rs = stmt.executeQuery();
			if(rs.next()){
				result=rs.getInt("c");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
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
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static dPairHashMap<String, String, ProjectContact> mapLatestContact(){
		dPairHashMap<String, String, ProjectContact> result = new dPairHashMap<String, String, ProjectContact>();
		SQL sqltool = new SQL();
		
		try {
			ResultSet rs = sqltool.query(
				"WITH complete_list AS( "
				+ "SELECT A.investNo, A.IDNO, C.*, "
				+ "ROW_NUMBER() OVER(PARTITION BY A.investNo, A.IDNO "
				+ "ORDER BY B.year DESC, B.quarter DESC) AS rk "
				+ "FROM Project A "
				+ "LEFT OUTER JOIN ProjectReport B ON A.serno = B.serno "
				+ "LEFT OUTER JOIN ProjectContact C ON C.receive_no = B.keyinNo "
				+ ") SELECT * FROM complete_list "
				+ "WHERE rk = 1 "
			);
			
			while(rs.next()) {
				if(rs.getString("receive_no") == null) {
					continue;
				}
				
				ProjectContact contact = new ProjectContact();
				contact.setContact_name(rs.getString("contact_name"));
				contact.setContact_tel_no(rs.getString("contact_tel_no"));
				contact.setCOUNTY_NAME(rs.getString("COUNTY_NAME"));
				contact.setTOWN_NAME(rs.getString("TOWN_NAME"));
				contact.setContact_ADDRESS(rs.getString("contact_ADDRESS"));
				
				result.put(rs.getString("investNo"), rs.getString("IDNO"), contact);
			}
			
			rs.close();
		}catch (Exception e) {
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
