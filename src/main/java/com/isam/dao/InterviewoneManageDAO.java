package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.InterviewoneManage;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class InterviewoneManageDAO {
	
	public List<Map<String,String>> getIMList(Map<String,String> terms){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		String investNo=terms.get("investNo");
		String investName=terms.get("investName");
		String IDNO=terms.get("IDNO");
		String year=terms.get("year").isEmpty()?terms.get("maxY"):terms.get("year");
		String AndOr=terms.get("AndOr");
		String start=terms.get("start");
		String end=terms.get("end");
		String progress=terms.get("progress");
		String following=terms.get("following");
		String gap=terms.get("gap");
		
		List<String> abnormal=(terms.get("abnormal")!=null&&!terms.get("abnormal").isEmpty())?DataUtil.StrArytoList(terms.get("abnormal").split(",")):null;
		
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select i.qNo,i.investNo,i.reInvestNo,case when i.reInvestNo=0 then ib.BAN_NO else rb.idno end idno ");
		sb.append(",case when i.reInvestNo=0 then ib.COMP_CHTNAME else rb.cname end cname");
		sb.append(",b.value iDate,c.optionValue progress,isnull(c.following,'') following,c.serno");
		sb.append(",case when i.reInvestNo=0 then ib.isOperated else rb.isOperated end isOperated,c.gap ");
		sb.append("FROM Interviewone i join (select qno,value from InterviewoneContent where optionId='1' and value>=? and value<=?)b ");
		sb.append("on i.qNo=b.qNo left join (select serno,qNo,optionValue,following,datediff(day,convert(varchar(4),CONVERT(int,LEFT(DL,3))+1911)");
		sb.append("+'-'+SUBSTRING(DL,4,2)+'-'+RIGHT(DL,2),GETDATE())gap from (SELECT serno,qNo,optionValue,following");
		sb.append(",ROW_NUMBER() over(partition by qNo order by receiveDate desc,updatetime desc) as rank");
		sb.append(",case when optionValue=5 then receiveDate else case when optionValue=6 then issueDate else null end end as DL ");
		sb.append("FROM InterviewoneManage where enable='1')x where rank='1' )c on i.qno=c.qNo left join ");
		sb.append("(SELECT investNo,BAN_NO,COMP_CHTNAME,isOperated FROM OFI_InvestList a");
		sb.append(",(SELECT INVESTMENT_NO,COMP_CHTNAME,BAN_NO FROM moeaic.dbo.OFI_BASEDATA (null))b ");
		sb.append(" where a.investNo=b.INVESTMENT_NO and enable='1') ib on i.investNo=ib.investNo left join (");
		sb.append("SELECT a.reInvestNo,investNo,idno,'(轉投資)'+reinvestment cname,isOperated ");
		sb.append("FROM (SELECT * FROM ReInvestNoXInvestNos (?)) b,OFI_ReInvestmentList a where enable='1' ");
		sb.append("and a.reInvestNo=b.reInvestNo) rb on i.reInvestNo=rb.reInvestNo where enable='1' ");
		sb.append("and i.year=? and i.qNo in(SELECT qNo FROM OFI_ErrorList(?)) ");
		if(following!=null&&!following.isEmpty()){
			sb.append("and isnull(c.following,'') in (").append(following).append(") ");
		}
		if(progress!=null&&!progress.isEmpty()){
			sb.append("and c.optionValue in (").append(progress).append(") ");
		}
		if(abnormal!=null&&!abnormal.isEmpty()){
			String andor=" or ";
			if(AndOr.equals("1")){
				andor=" and ";
			}
			sb.append("and (");
			for (int i = 0; i < abnormal.size(); i++) {
				String x=abnormal.get(i);
				if(i!=0){
					sb.append(andor);
				}
				if(x.equals("1")){
					sb.append("i.qNo in (SELECT * FROM OFI_InterviewErrorList('").append(year).append("')) ");
				}else if(x.equals("2")){
					sb.append("i.qNo in (SELECT * FROM OFI_FinancialErrorList('").append(year).append("')) ");
				}else if(x.equals("3")){
					sb.append("( i.investNo in (select investNo from OFI_InvestNoXAudit where auditCode ='0603' and value<>'') and i.reInvestNo=0 )");
				}
			}
			sb.append(") ");
		}
		String tmp=sb.toString(); 
		sb.setLength(0);
		sb.append("select * from (").append(tmp).append(")t where idno like ? and investNo like ? and cname like ? ");
		if(gap!=null&&!gap.isEmpty()){
			sb.append("and gap >= 90 and isOperated!='3'");
		}
		
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			int count=1;
			stmt.setString(count++, start);
			stmt.setString(count++, end);
			stmt.setString(count++, year);
			stmt.setString(count++, year);
			stmt.setString(count++, year);
			stmt.setString(count++, IDNO);
			stmt.setString(count++, investNo);
			stmt.setString(count++, investName);

			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				Map<String,String> b=new HashMap<String, String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					b.put(meta.getColumnName(i), DataUtil.nulltoempty(rs.getString(i)));
				}
				result.add(b);
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
/*	public List<Map<String,String>> getIMList(Map<String,String> terms){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		String investNo=terms.get("investNo");
		String investName=terms.get("investName");
		String IDNO=terms.get("IDNO");
		String year=terms.get("year").isEmpty()?terms.get("maxY"):terms.get("year");
		String AndOr=terms.get("AndOr");
		String start=terms.get("start");
		String end=terms.get("end");
		String progress=terms.get("progress");
		String following=terms.get("following");
		String gap=terms.get("gap");
		
		List<String> abnormal=(terms.get("abnormal")!=null&&!terms.get("abnormal").isEmpty())?DataUtil.StrArytoList(terms.get("abnormal").split(",")):null;
		
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT a.qNo,a.investNo,d.idno,d.COMP_CHTNAME cname,b.value iDate,c.optionValue progress");
		sb.append(",isnull(c.following,'') following,c.serno,i.isOperated,c.gap FROM Interviewone a ");
		sb.append("join(select qno,value from InterviewoneContent where optionId='1' and value>=? and value<=?)b ");
		sb.append("on a.qNo=b.qNo join (SELECT investNo,isOperated FROM OFI_InvestList where enable='1')i ");
		sb.append("on i.investNo=a.investNo left join (select serno,qNo,optionValue,following,");
		sb.append("datediff(day,convert(varchar(4),CONVERT(int,LEFT(DL,3))+1911)+'-'+SUBSTRING(DL,4,2)+'-'+RIGHT(DL,2)");
		sb.append(",GETDATE())gap from (SELECT serno,qNo,optionValue,following,ROW_NUMBER() over(partition by qNo order by ");
		sb.append("receiveDate desc,updatetime desc) as rank,case when optionValue=5 then receiveDate else case when ");
		sb.append("optionValue=6 then issueDate else null end end as DL FROM InterviewoneManage where enable='1')x where rank='1' ");
		sb.append(")c on a.qno=c.qNo join (select distinct investNo,COMP_CHTNAME,isnull(BAN_NO,'') idno ");
		sb.append("from OFI_InvestCase where isnull(BAN_NO,'') like '").append(IDNO).append("' and investNo like '");
		sb.append(investNo).append("' and COMP_CHTNAME like '").append(investName);
		sb.append("' ) d on a.investNo=d.investNo where enable='1' and (interviewStatus='1' or interviewStatus='9') ");
		sb.append("and a.year=? and a.qNo in(SELECT qNo FROM [ISAM].[dbo].[OFI_ErrorList] (?)) ");
		if(following!=null&&!following.isEmpty()){
			sb.append("and isnull(c.following,'') in (").append(following).append(") ");
		}
		if(progress!=null&&!progress.isEmpty()){
			sb.append("and c.optionValue in (").append(progress).append(") ");
		}
		if(abnormal!=null&&!abnormal.isEmpty()){
			String andor=" or ";
			if(AndOr.equals("1")){
				andor=" and ";
			}
			sb.append("and (");
			for (int i = 0; i < abnormal.size(); i++) {
				String x=abnormal.get(i);
				if(i!=0){
					sb.append(andor);
				}
				if(x.equals("1")){
					sb.append("a.qNo in (SELECT * FROM OFI_InterviewErrorList('").append(year).append("')) ");
				}else if(x.equals("2")){
					sb.append("a.qNo in (SELECT * FROM OFI_FinancialErrorList('").append(year).append("')) ");
				}else if(x.equals("3")){
					sb.append("( a.investNo in (select investNo from OFI_InvestNoXAudit where auditCode ='0603' and value<>'') and reInvestNo=0 )");
				}
			}
			sb.append(") ");
		}
		if(gap!=null&&!gap.isEmpty()){
			sb.append("and c.gap >= 90 and i.isOperated!='3' ");
		}
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, start);
			stmt.setString(2, end);
			stmt.setString(3, year);
			stmt.setString(4, year);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				Map<String,String> b=new HashMap<String, String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					b.put(meta.getColumnName(i), DataUtil.nulltoempty(rs.getString(i)));
				}
				result.add(b);
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
*/	public String checkMaxReceiveDate(String qNo,String serno){
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		String result="";
		sb.append("SELECT max(receiveDate) receiveDate FROM InterviewoneManage where qNo=? and enable='1'");
		if(!serno.isEmpty()){
			sb.append(" and serno!=?");
		}
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, qNo);
			if(!serno.isEmpty()){
				stmt.setString(2, serno);
			}
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result=DataUtil.nulltoempty(rs.getString("receiveDate"));
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
	public String checkFowllowing(String qNo,String serno){
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		String result="";
		sb.append("SELECT MAX(following) following FROM InterviewoneManage where qNo=? and enable='1'");
		if(!serno.isEmpty()){
			sb.append(" and serno!=?");
		}
		String forStmt = sb.toString();
		sb.setLength(0);
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, qNo);
			if(!serno.isEmpty()){
				stmt.setString(2, serno);
			}
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result=DataUtil.nulltoempty(rs.getString("following"));
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
	public Map<String,Map<String,String>> getFollowingMap(String investNo){
		Map<String,Map<String,String>> result = new HashMap<String, Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select qNo,optionValue,following from(SELECT qNo,optionValue,following,");
		sb.append("ROW_NUMBER() over(partition by qNo order by following desc,receiveDate desc,updatetime desc) rank ");
		sb.append("FROM InterviewoneManage where enable='1' and qNo in (SELECT qNo FROM Interviewone ");
		sb.append("where enable='1' and investNo =?))a where RANK='1'");
		String forStmt = sb.toString();
		sb.setLength(0);
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				Map<String,String> b=new HashMap<String, String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					b.put(meta.getColumnName(i), DataUtil.nulltoempty(rs.getString(i)));
				}
				result.put(rs.getString("qNo"),b);
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
	public Map<String,Map<String,String>> getFollowingMap(String qNo,String reInvestNo){
		Map<String,Map<String,String>> result = new HashMap<String, Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select qNo,optionValue,following from(SELECT qNo,optionValue,following,");
		sb.append("ROW_NUMBER() over(partition by qNo order by following desc,receiveDate desc,updatetime desc) rank ");
		sb.append("FROM InterviewoneManage where enable='1' and qNo in (SELECT qNo FROM Interviewone where enable='1' and ");
		if(reInvestNo.equals("0")){
			sb.append("(investNo in (select investNo from Interviewone where qNo=?) and reInvestNo=0)");
		}else{
			sb.append("(reInvestNo in (select reInvestNo from Interviewone where qNo=?) and reInvestNo!=0)");
		}
		sb.append("))a where RANK='1'");
		String forStmt = sb.toString();
		sb.setLength(0);
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, qNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				Map<String,String> b=new HashMap<String, String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					b.put(meta.getColumnName(i), DataUtil.nulltoempty(rs.getString(i)));
				}
				result.put(rs.getString("qNo"),b);
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
	public List<InterviewoneManage> selectByQNo(String qNo){
		SQL sqltool = new SQL();
		List<InterviewoneManage> list= new ArrayList<InterviewoneManage>();
		String forStmt = "SELECT * FROM InterviewoneManage where qNo=? and enable='1' order by following desc,receiveDate desc,updatetime desc";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, qNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				InterviewoneManage bean = new InterviewoneManage();
				bean.setSerno(rs.getInt("serno"));
				bean.setqNo(rs.getInt("qNo"));
				bean.setReceiveNo(rs.getString("receiveNo"));
				bean.setReceiveDate(rs.getString("receiveDate"));
				bean.setIssueNo(rs.getString("issueNo"));
				bean.setIssueDate(rs.getString("issueDate"));
				bean.setIssueby(rs.getString("issueby"));
				bean.setOptionValue(rs.getString("optionValue"));
				bean.setFollowing(rs.getString("following"));
				bean.setInterviewer(rs.getString("interviewer"));
				bean.setInterviewee(rs.getString("interviewee"));
				bean.setNote(rs.getString("note"));
				bean.setEnable(rs.getString("enable"));
				bean.setFlag(rs.getString("flag"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				list.add(bean);				
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
	public InterviewoneManage select(String serno){
		SQL sqltool = new SQL();
		InterviewoneManage bean = new InterviewoneManage();
		String forStmt = "SELECT * FROM InterviewoneManage where serno=? and enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean.setSerno(rs.getInt("serno"));
				bean.setqNo(rs.getInt("qNo"));
				bean.setReceiveNo(rs.getString("receiveNo"));
				bean.setReceiveDate(rs.getString("receiveDate"));
				bean.setIssueNo(rs.getString("issueNo"));
				bean.setIssueDate(rs.getString("issueDate"));
				bean.setIssueby(rs.getString("issueby"));
				bean.setOptionValue(rs.getString("optionValue"));
				bean.setFollowing(rs.getString("following"));
				bean.setInterviewer(rs.getString("interviewer"));
				bean.setInterviewee(rs.getString("interviewee"));
				bean.setNote(rs.getString("note"));
				bean.setEnable(rs.getString("enable"));
				bean.setFlag(rs.getString("flag"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
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
	public int insert(InterviewoneManage bean) {
		int result = 0;
		String forpstmt = "insert into InterviewoneManage (qNo,receiveNo,receiveDate,issueNo,issueDate,issueby,optionValue,following,note,enable,updatetime,updateuser,createtime,createuser,interviewer,interviewee,flag) Output Inserted.serno values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setInt(1, bean.getqNo());
			pstmt.setString(2, bean.getReceiveNo());
			pstmt.setString(3, bean.getReceiveDate());
			pstmt.setString(4, bean.getIssueNo());
			pstmt.setString(5, bean.getIssueDate());
			pstmt.setString(6, bean.getIssueby());
			pstmt.setString(7, bean.getOptionValue());
			pstmt.setString(8, bean.getFollowing());
			pstmt.setString(9, bean.getNote());
			pstmt.setString(10, bean.getEnable());
			pstmt.setTimestamp(11, bean.getUpdatetime());
			pstmt.setString(12, bean.getUpdateuser());
			pstmt.setTimestamp(13, bean.getCreatetime());
			pstmt.setString(14, bean.getCreateuser());
			pstmt.setString(15, bean.getInterviewer());
			pstmt.setString(16, bean.getInterviewee());
			pstmt.setString(17, bean.getFlag());
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
	public int update(InterviewoneManage bean) {
		int result = 0;
		String forpstmt = "UPDATE InterviewoneManage SET receiveNo=?,receiveDate=?,issueNo=?,issueDate=?,issueby=?,optionValue=?,following=?,note=?,updatetime=?,updateuser=?,interviewer=?,interviewee=? WHERE serno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getReceiveNo());
			pstmt.setString(2, bean.getReceiveDate());
			pstmt.setString(3, bean.getIssueNo());
			pstmt.setString(4, bean.getIssueDate());
			pstmt.setString(5, bean.getIssueby());
			pstmt.setString(6, bean.getOptionValue());
			pstmt.setString(7, bean.getFollowing());
			pstmt.setString(8, bean.getNote());
			pstmt.setTimestamp(9, bean.getUpdatetime());
			pstmt.setString(10, bean.getUpdateuser());
			pstmt.setString(11, bean.getInterviewer());
			pstmt.setString(12, bean.getInterviewee());
			pstmt.setInt(13, bean.getSerno());
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
	public int unable(String serno,String updateuser) {
		int result = 0;
		String forpstmt = "UPDATE InterviewoneManage SET enable='0',updatetime=getdate(),updateuser=? WHERE serno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, updateuser);
			pstmt.setString(2, serno);
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
