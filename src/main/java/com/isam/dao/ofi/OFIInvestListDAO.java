package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestList;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

import Lara.Utility.ToolsUtil;

public class OFIInvestListDAO {
	public Map<String, String> getYearRange(){
		SQL sqltool = new SQL();
		Map<String, String> map= new HashMap<String, String>();
		String forStmt = "select max(SUBSTRING([respdate],1,3)) eyear, min(SUBSTRING([respdate],1,3)) syear FROM OFI_InvestList where [respdate]!='' and enable='1' ";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				map.put("eyear",rs.getString("eyear"));
				map.put("syear",rs.getString("syear"));
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
	
	public OFIInvestList select(String investNo){
		SQL sqltool = new SQL();
		OFIInvestList bean = new OFIInvestList();
		try {
			PreparedStatement stmt = sqltool.prepare("SELECT * FROM OFI_InvestList WHERE investNo = ? AND enable = '1' ");
			stmt.setString(1, investNo);
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				bean.setInvestNo(rs.getString("investNo"));
				bean.setActive(rs.getString("active"));
				bean.setIsNew(rs.getString("isNew"));
				bean.setSetupdate(rs.getString("setupdate"));
				bean.setApprovaldate(rs.getString("approvaldate"));
				bean.setSetupnote(rs.getString("setupnote"));
				bean.setRespdate(rs.getString("respdate"));
				bean.setReceiveNo(rs.getString("receiveNo"));
				bean.setIsOperated(rs.getString("isOperated"));
				bean.setSdate(rs.getString("sdate"));
				bean.setEdate(rs.getString("edate"));
				bean.setNote(rs.getString("note"));
				bean.setEnable(rs.getString("enable"));
				bean.setIsFilled(rs.getString("isFilled"));
				bean.setIsCNFDI(rs.getString("isCNFDI"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setFirmXNTBTSic(rs.getString("firmXNTBTSic"));
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
	
	public List<Map<String,String>> select(Map<String,String> terms){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		SQL sqltool = new SQL();
		String investNo=terms.get("investNo");
		String IDNO=terms.get("IDNO");
		String company=terms.get("company");
		String year=terms.get("year");
		String iyear=terms.get("iyear");
		String investor=terms.get("investor");
		String reInvest=terms.get("reInvest");
		String CNFDI=terms.get("CNFDI");
		String sp=terms.get("sp");
		String AndOr=terms.get("AndOr");
		String twsic=terms.get("twsic");
		List<String> abnormal=terms.get("abnormal")!=null&&!terms.get("abnormal").isEmpty()?DataUtil.StrArytoList(terms.get("abnormal").split(",")):null;
		String issueType=terms.get("issueType");
		String errMsgXnote=terms.get("errMsgXnote");
		String itvOneYear=terms.get("itvOneYear");
		if(CNFDI.isEmpty()||CNFDI.equals("99")){
			CNFDI=null;
		}
		String sdate="0000000";
		String edate="9999999";
		if(!year.isEmpty()&&!year.equals("000")){
			sdate=DataUtil.addRigthZeroForNum(year, 7);
			edate=DataUtil.addRigthNineForNum(year, 7);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct a.* from (SELECT SUBSTRING([respdate],1,3) year,investNo,BAN_NO idno,COMP_CHTNAME company,isFilled,isCNFDI ");
		sb.append("FROM OFI_InvestList a join ( SELECT INVESTMENT_NO,BAN_NO,COMP_CHTNAME FROM [moeaic].[dbo].[OFI_BASEDATA] (null) ");
		sb.append("where BAN_NO like '").append(IDNO).append("' and INVESTMENT_NO like '").append(investNo).append("' and COMP_CHTNAME like '");
		sb.append(company).append("' union all SELECT INVESTMENT_NO,BAN_NO,COMP_CHTNAME FROM [moeaic].[dbo].[OFI_BASEDATA] (null) ");
		sb.append("where INVESTMENT_NO in(SELECT investNo FROM OFI_ReInvestXInvestNo where enable='1' and reInvestNo in (");
		sb.append("SELECT reInvestNo FROM OFI_ReInvestmentList where enable='1' and reinvestment like '");
		sb.append(company).append("' and idno like '").append(IDNO).append("' and investNo like '").append(investNo).append("'))) b ");
		sb.append("on a.investNo=b.INVESTMENT_NO where enable='1' ");
		sb.append("and isCNFDI=isnull(?,isCNFDI) ");
		sb.append("and respdate>='").append(sdate).append("' and respdate<='").append(edate).append("' ");
		if(reInvest.equals("1")){
			sb.append("and COMP_CHTNAME in (SELECT COMP_CHTNAME FROM [moeaic].[dbo].[OFITB204] group by COMP_CHTNAME) ");
		}else if(reInvest.equals("0")){
			sb.append("and COMP_CHTNAME not in (SELECT COMP_CHTNAME FROM [moeaic].[dbo].[OFITB204] group by COMP_CHTNAME) ");
		}
		if(!sp.equals("99")&&!sp.isEmpty()){
			if(sp.equals("1")){
				sb.append("and investNo in (SELECT investNo FROM OFI_InvestNoXTWSIC where TYPE='0' group by investNo) ");
			}else if(sp.equals("0")){
				sb.append("and investNo not in (SELECT investNo FROM OFI_InvestNoXTWSIC where TYPE='0' group by investNo) ");
			}
		}
		if(!twsic.isEmpty()){
			sb.append("and investNo in ( SELECT investNo from (SELECT investNo FROM OFI_InvestNoXTWSIC where item in ( ");
			sb.append(twsic).append(") union all SELECT investNo FROM OFI_ReInvestXInvestNo where reInvestNo In ");
			sb.append("(select reInvestNo FROM OFI_ReInvestXTWSIC  where item in (");
			sb.append(twsic).append(")))t group by investNo) ");
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
					sb.append("investNo in (select investNo from Interviewone where enable='1' and reInvestNo='0' and ");
					sb.append("qNo in (SELECT * FROM OFI_InterviewErrorList('").append(iyear).append("')) union ");
					sb.append("select r.investNo from Interviewone i,OFI_ReInvestXInvestNo r where i.enable='1' ");
					sb.append("and i.reInvestNo!='0' and i.reInvestNo=r.reInvestNo and i.qNo in (SELECT * FROM OFI_InterviewErrorList('").append(iyear).append("'))) ");
//					sb.append("investNo in (SELECT * FROM OFI_InterviewErrorList('").append(iyear).append("')) ");
				}else if(x.equals("2")){
					sb.append("investNo in (select investNo from Interviewone where enable='1' and reInvestNo='0' and ");
					sb.append("qNo in (SELECT * FROM OFI_FinancialErrorList('").append(iyear).append("')) union ");
					sb.append("select r.investNo from Interviewone i,OFI_ReInvestXInvestNo r where i.enable='1' ");
					sb.append("and i.reInvestNo!='0' and i.reInvestNo=r.reInvestNo and i.qNo in (SELECT * FROM OFI_FinancialErrorList('").append(iyear).append("')))");
//					sb.append("investNo in (SELECT * FROM OFI_FinancialErrorList('").append(iyear).append("')) ");
				}else if(x.equals("3")){
					sb.append("investNo in (select investNo from OFI_InvestNoXAudit where auditCode ='0603' and value<>'')");
				}
			}
			sb.append(")");
		}
		if(issueType!=null&&!issueType.isEmpty()){
			sb.append(" and investNo in (SELECT INVESTMENT_NO FROM [moeaic].[dbo].[OFI_BASEDATA] (null) ");
			sb.append("where issueTypeCode in (").append(issueType).append(")) ");
		}
		sb.append(")a ");
		sb.append("join (select investNo from OFI_InvestCase where INVESTOR_CHTNAME like '").append(investor).append("') i ");
		sb.append("on i.investNo=a.investNo ");
		
		if(errMsgXnote!=null && !errMsgXnote.isEmpty()) {
			
			//107-08-22 改成多選，檢查有沒有逗號隔開的多選項
			StringBuilder str = new StringBuilder();
			for(String val : ToolsUtil.getValueToList(errMsgXnote, ",")) {
				if(str.length() > 0) {
					str.append("and ");
				}
				str.append("value like '%"+val+"%' ");
			}
			
			sb.append("WHERE a.investNo in ("
											+ "SELECT investNo FROM Interviewone "
											+ "WHERE year="+itvOneYear+" "
											+ "AND enable = '1' "
											+ "and qNo in ( "
											+ 				"SELECT qNo FROM VW_InterviewoneAbnormal "
											+ 				"where "+ str.toString() +" "
											+ 			 ") "
										+ ") "
					 );
		}
		
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, CNFDI);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				Map<String,String> sub = new HashMap<String, String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.put(meta.getColumnName(i),rs.getString(i));
				}
				result.add(sub);
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
	
	public int update(OFIInvestList bean) {
		int result = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE OFI_InvestList SET active=?,isNew=?,setupdate=?,setupnote=?,");
		sb.append("isOperated=?,sdate=?,edate=?,note=?,isFilled=?,updatetime=?,updateuser=?,approvaldate=?,firmXNTBTSic=? ");
		sb.append("where investNo=?");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, bean.getActive());
			pstmt.setString(2, bean.getIsNew());
			pstmt.setString(3, bean.getSetupdate());
			pstmt.setString(4, bean.getSetupnote());
			pstmt.setString(5, bean.getIsOperated());
			pstmt.setString(6, bean.getSdate());
			pstmt.setString(7, bean.getEdate());
			pstmt.setString(8, bean.getNote());
			pstmt.setString(9, bean.getIsFilled());
			pstmt.setTimestamp(10, bean.getUpdatetime());
			pstmt.setString(11, bean.getUpdateuser());
			pstmt.setString(12, bean.getApprovaldate());
			pstmt.setString(13, bean.getFirmXNTBTSic());
			pstmt.setString(14, bean.getInvestNo());
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
}
