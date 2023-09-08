package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

/**** 2020/11/22 新增欄位SQL
 * 
 * 
 * @author antfire
 *

BEGIN TRANSACTION
SET QUOTED_IDENTIFIER ON
SET ARITHABORT ON
SET NUMERIC_ROUNDABORT OFF
SET CONCAT_NULL_YIELDS_NULL ON
SET ANSI_NULLS ON
SET ANSI_PADDING ON
SET ANSI_WARNINGS ON
COMMIT
BEGIN TRANSACTION
GO
ALTER TABLE dbo.OFI_InvestCase ADD
	IL_MONEY1 decimal(20, 0) NULL,
	IL_MONEY2 decimal(20, 0) NULL,
	IL_STOCK_IMP decimal(20, 0) NULL
GO
ALTER TABLE dbo.OFI_InvestCase SET (LOCK_ESCALATION = TABLE)
GO
COMMIT

 ****************************/

public class OFIInvestCaseDAO {
	public List<Map<String,String>> getInvestcase(String investorSeq,String caseNo){
		List<Map<String,String>> result = new ArrayList<Map<String, String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT a.caseNo,a.investNo,a.investorSeq,a.isFilled,a.note,b.COMP_CHTNAME cname,b.BAN_NO idno,b.RESP_DATE respdate");
		sb.append(",b.investvalue,b.investedcapital,b.FACE_VALUE faceval,convert(decimal(18,2),(b.StockPercent*100)) sp,");
		sb.append("case when SUBSTRING(a.investNo, 1, 1)=4 then '99' else b.ORG_TYPE_CODE end orgType ");
		sb.append(", b.IL_MONEY1 ilmoney1,  b.IL_MONEY2 ilmoney2,  b.IL_STOCK_IMP ilstockimp ");
		sb.append(", b.MONEY1 money1,  b.MONEY2 money2,  b.STOCK_IMP stockimp ");
		sb.append("FROM OFI_InvestCaseList a,OFI_InvestCase b where a.investNo=b.investNo and a.investorSeq=b.INVESTOR_SEQ ");
		sb.append("and a.enable='1' and a.investorSeq=? and a.caseNo=isnull(?,caseNo)");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investorSeq);
			stmt.setString(2, caseNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				Map<String,String> sub = new HashMap<String, String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.put(meta.getColumnName(i),DataUtil.nulltoempty(rs.getString(i)));
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
	public Map<String,String> getRemarkCount(String investNo){
		Map<String,String> result = new LinkedHashMap<String, String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select (SELECT COUNT(investNo) FROM OFI_InvestCase where isVisible='1' and INVESTOR_SEQ in (");
		sb.append("SELECT investorSeq FROM OFI_InvestorXBG where value!='0' and len(bgType)<4) and investNo=?) bg,");
		sb.append("(select (SELECT count(investNo) FROM OFI_InvestNoXTWSIC where type='0' and investNo=?)+");
		sb.append("(SELECT count(*) FROM [ISAM].[dbo].[OFI_ReInvestXTWSIC] where type='0' and reInvestNo ");
		sb.append("in (select reInvestNo from OFI_ReInvestXInvestNo where investNO=?)))sp,");
		sb.append("(SELECT COUNT(*) FROM OFI_InvestNoXAudit where auditCode ='02' and value!='0' and investNo=?) a02,");
		sb.append("(SELECT COUNT(*) FROM OFI_InvestNoXAudit where auditCode in('01','03') and value!='0' and investNo=?) a0103");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, investNo);
			stmt.setString(3, investNo);
			stmt.setString(4, investNo);
			stmt.setString(5, investNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				for(int i=1;i<=meta.getColumnCount();i++){
					result.put(meta.getColumnName(i),DataUtil.nulltoempty(rs.getString(i)));
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
	public Map<String,Map<String,String>> getInvestorNameSp(String investNo,String rolecode){
		Map<String,Map<String,String>> result = new HashMap<String, Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT a.investorSeq,b.INVESTOR_CHTNAME iscname,convert(decimal(18,2),(b.StockPercent*100)) sp ");
		sb.append("FROM OFI_InvestCaseList a,OFI_InvestCase b where a.investNo=b.investNo ");
		sb.append("and a.investorSeq=b.INVESTOR_SEQ and b.isVisible='1' and a.enable='1' and a.investNo=? and INVE_ROLE_CODE=?");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, rolecode);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				Map<String,String> sub = new HashMap<String, String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.put(meta.getColumnName(i),DataUtil.nulltoempty(rs.getString(i)));
				}
				result.put(rs.getString("investorSeq"),sub);
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
	public String getInvestName(String investNo){
		SQL sqltool = new SQL();
		String result="";
		String forStmt = "select COMP_CHTNAME cname from OFI_InvestCase where investNo=? group by COMP_CHTNAME";
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result=rs.getString("cname");
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
	public double getNowInvestvalue(String investNo){
		SQL sqltool = new SQL();
		double result=0;
		String forStmt = "SELECT sum([investvalue]) FROM OFI_InvestCase where investNo=? group by [investNo]";
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result=rs.getDouble(1);
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
	public List<Map<String,String>> select(String investNo){
		List<Map<String,String>> result = new ArrayList<Map<String, String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT INVESTOR_SEQ,INVESTOR_CHTNAME,INVE_ROLE_CODE,RESP_DATE,investvalue MONEY2,investedcapital investvalue,convert(decimal(18,2),[StockPercent]*100) pt ");
		sb.append("FROM OFI_InvestCase where investNo=? ");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				Map<String,String> sub = new HashMap<String, String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.put(meta.getColumnName(i),DataUtil.nulltoempty(rs.getString(i)));
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
	public List<Map<String,String>> select(Map<String,String> terms){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		SQL sqltool = new SQL();
		String investNo=terms.get("investNo");
		String IDNO=terms.get("IDNO");
		String companytype=terms.get("companytype");
		String company=terms.get("company");
		String investor=terms.get("investor");
		String AndOr=terms.get("AndOr");
		List<String> aduit =null;
		if(terms.get("aduit").trim().length()>0){
			aduit = DataUtil.StrArytoList(terms.get("aduit").split(","));
		}
		String aduitStr="";
		String aduit06="";
		String aduit04="";
		StringBuilder sb = new StringBuilder();
		if(aduit!=null){
			if(AndOr.isEmpty()){
				if(aduit.indexOf("06")!=-1){
					sb.append(" (a.investNo in (SELECT investNo FROM dbo.OFI_ErrorList('') where reinvestNo=0 ");
					sb.append("union SELECT r.investNo FROM dbo.OFI_ErrorList('') e,(SELECT reInvestNo,investNo FROM ");
					sb.append("OFI_ReInvestXInvestNo where enable='1')r where e.reinvestNo!=0 and e.reinvestNo=r.reInvestNo)) ");
					aduit06=sb.toString();
					sb.setLength(0);
					aduit.remove("06");
				}
				if(aduit.indexOf("04")!=-1){
					aduit04=" a.investNo in (select investNo from (SELECT investNo,sum([investvalue]) val FROM OFI_InvestCase group by investNo) a where a.val>=500000000) ";
					aduit.remove("04");
				}
				if(aduit.size()>0){
					sb.append("and (a.investNo in (select investNo from OFI_InvestNoXAudit where value='1' and auditCode in (");
					sb.append(DataUtil.fmtStrAryItem(aduit)).append(")) ");
					if(!aduit06.isEmpty()){
						sb.append(" or ").append(aduit06);
					}
					if(!aduit04.isEmpty()){
						sb.append(" or ").append(aduit04);
					}
					sb.append(") ");
				}else{
					if(!aduit06.isEmpty()&&!aduit04.isEmpty()){
						sb.append(" and( ").append(aduit06).append(" or ").append(aduit04).append(") ");
					}else if(!aduit06.isEmpty()&&aduit04.isEmpty()){
						sb.append(" and ").append(aduit06);
					}else if(aduit06.isEmpty()&&!aduit04.isEmpty()){
						sb.append(" and ").append(aduit04);
					}
				}
			}else if(AndOr.equals("1")){
				if(aduit.indexOf("06")!=-1){
					sb.append(" investNo in (SELECT investNo FROM dbo.OFI_ErrorList('') where reinvestNo=0 ");
					sb.append("union SELECT r.investNo FROM dbo.OFI_ErrorList('') e,(SELECT reInvestNo,investNo FROM ");
					sb.append("OFI_ReInvestXInvestNo where enable='1')r where e.reinvestNo!=0 and e.reinvestNo=r.reInvestNo) ");
					aduit06=sb.toString();
					sb.setLength(0);
					aduit.remove("06");
				}
				if(aduit.indexOf("04")!=-1){
					aduit04=" investNo in ( select investNo from (SELECT investNo,sum([investvalue]) val FROM OFI_InvestCase group by investNo) a where a.val>=500000000) ";
					aduit.remove("04");
				}
				int aSize=aduit.size();
				
				sb.append("and a.investNo in ( select distinct investNo from OFI_InvestNoXAudit where ");
				if(aSize>0){
					sb.append("investNo in (select investNo from ( SELECT investNo,COUNT(investNo) c FROM OFI_InvestNoXAudit ");
					sb.append("where seq='1' and value='1' and (").append(DataUtil.termOrToParam("auditCode",aduit));
					sb.append(") group by investNo)x where x.c=").append(aSize).append(" ) ");
					if(!aduit06.isEmpty()){
						sb.append(" and ").append(aduit06);
					}
					if(!aduit04.isEmpty()){
						sb.append(" and ").append(aduit04);
					}
				}else{
					if(!aduit06.isEmpty()&&!aduit04.isEmpty()){
						sb.append(aduit06).append(" And ").append(aduit04);
					}else if(!aduit06.isEmpty()&&aduit04.isEmpty()){
						sb.append(aduit06);
					}else if(aduit06.isEmpty()&&!aduit04.isEmpty()){
						sb.append(aduit04);
					}
				}
				sb.append(") ");
			}
			aduitStr=sb.toString();
			sb.setLength(0);
		}
		sb.append("SELECT distinct a.caseNo,a.investNo,a.investorSeq,b.COMP_CHTNAME,b.INVESTOR_CHTNAME FROM OFI_InvestCaseList a,");
		sb.append("( select investNo,INVESTOR_SEQ,COMP_CHTNAME,INVESTOR_CHTNAME from OFI_InvestCase where ");
		sb.append("isnull(BAN_NO,'') like '").append(IDNO).append("' and investNo like '").append(investNo);
		sb.append("' and COMP_CHTNAME like '").append(company).append("' and INVESTOR_CHTNAME like '");
		sb.append(investor).append("' union all select investNo,INVESTOR_SEQ,COMP_CHTNAME,INVESTOR_CHTNAME ");
		sb.append("from OFI_InvestCase where investNo in(SELECT investNo FROM OFI_ReInvestXInvestNo where  enable='1' and reInvestNo ");
		sb.append("in (select reInvestNo from OFI_ReInvestmentList where reinvestment like '").append(company).append("' and idno like '");
		sb.append(IDNO).append("')) ").append("and investNo like '").append(investNo).append("'");
		sb.append(")b where a.enable='1' and a.investNo=b.investNo and a.investorSeq=b.INVESTOR_SEQ ");
		sb.append(" and INVESTOR_CHTNAME like '").append(investor).append("' ");
		
		//107-06-25 因為原筆數有上千筆，有填進這兩個table的只有幾百筆，為避免無法取出全部，在這三欄有填資料時，才加入這串SQL
		String investorXRelated = terms.get("investorXRelated");
		String relatedNation = terms.get("relatedNation");
		String relatedCnCode = terms.get("relatedCnCode");

		if(!(investorXRelated.replaceAll("%", "")).isEmpty() || !relatedNation.isEmpty() || !relatedCnCode.isEmpty()) {
			if(!(investorXRelated.replaceAll("%", "")).isEmpty()) {
				//關鍵字不空白，兩個表格都要找
				sb.append("and ("
						+ "a.investorSeq in ("
									+ "select investorSeq from OFI_InvestorXRelated "
									+ "where relatedname like '" + investorXRelated +"' "
									+ "and nation = isnull( convert( nvarchar(3), NULLIF('"+ relatedNation +"', '') ) , nation) "
									+ "and cnCode = isnull( convert( nvarchar(5), NULLIF('"+ relatedCnCode +"', '') ) , cnCode) "
									+ ") "
						+ "OR "
						+ "a.investorSeq in ("
									+ "SELECT investorSeq FROM OFI_InvestorXBG where bgType='BG1Note' AND value like '" + investorXRelated + "' "
									+ "UNION all "
									+ "SELECT investorSeq FROM OFI_InvestorXBG where bgType='BG2Note' AND value like '" + investorXRelated + "' "
									+ ") "
						+ ") " );
			}else if(!relatedNation.isEmpty() || !relatedCnCode.isEmpty()) {
				//選擇國家省份時，則只搜尋母公司表格
				sb.append("and ("
						+ "a.investorSeq in ("
									+ "select investorSeq from OFI_InvestorXRelated "
									+ "where relatedname like '" + investorXRelated +"' "
									+ "and nation = isnull( convert( nvarchar(3), NULLIF('"+ relatedNation +"', '') ) , nation) "
									+ "and cnCode = isnull( convert( nvarchar(5), NULLIF('"+ relatedCnCode +"', '') ) , cnCode) "
									+ ") "
						+ ") " );

			}
		}
		sb.append(aduitStr);
		
		
		/* 新增辦事處查詢 start*/
		if(companytype.isEmpty() || companytype.equals("2") ) {
			if(companytype.equals("2")) {
				sb.append(" and a.investNo  = '' ");
			}
			sb.append(" union all select  Ban_NO  as caseNo, '' as investNo, '' as investorSeq , COMP_CHTNAME as COMP_CHTNAME , COMP_CHTNAME as 'INVESTOR_CHTNAME' from OFI_InvestOffice where 1=1 ");
			if(!company.isEmpty() ) {
				sb.append(" and COMP_CHTNAME like '").append(company).append("'");
			}
			if(!investor.isEmpty() ) {
				sb.append(" and COMP_CHTNAME like '").append(investor).append("'");
			}
			
			if(!investorXRelated.isEmpty()) {
				sb.append(" and COMP_CHTNAME like '").append(investorXRelated).append("'");
		}
			if(!IDNO.isEmpty() ) {
				sb.append(" and Ban_NO like '").append(IDNO).append("'");
			}
		}
		/* 新增辦事處查詢 end */
		
		
		String forStmt = sb.toString();
		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				Map<String,String> sub = new HashMap<String, String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.put(meta.getColumnName(i),rs.getString(i));
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
	public List<Map<String,String>> summary(String investNo){
		List<Map<String,String>> result = new ArrayList<Map<String, String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select b.Description gname,SUM(investvalue) money2,SUM(investedcapital) ival,SUM(pt) sp from ( ");
		sb.append("SELECT INVESTOR_SEQ,INVESTOR_CHTNAME,INVE_ROLE_CODE,investvalue,investedcapital,convert(decimal(18,2),[StockPercent]*100) pt ");
		sb.append("FROM OFI_InvestCase where investNo=? )a right join ");
		sb.append("(select OptionValue,Description,seq from OFI_WebOption where SelectName='inSrc')b ");
		sb.append("on a.INVE_ROLE_CODE=b.OptionValue group by Description,seq order by b.seq");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				Map<String,String> sub = new HashMap<String, String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.put(meta.getColumnName(i),DataUtil.nulltoempty(rs.getString(i)));
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
	public String investorSrc(String investNo){
		String result = "";
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT distinct a.investNo,b.Description FROM OFI_InvestCase a,OFI_WebOption b ");
		sb.append("where a.isVisible='1' and a.INVE_ROLE_CODE=b.OptionValue and b.SelectName='inSrc' and investNo=? ");
		sb.append("union all select investNo, '本國' from ( ");
		sb.append("SELECT investNo,case when convert(decimal(18,2),sum([StockPercent]*100))<100 then '0' else '' end v ");
		sb.append("FROM OFI_InvestCase where isVisible='1' and investNo=? group by investNo)a where a.v='0' ");
		sb.append("order by investNo desc");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				if(sb.length()>0){
					sb.append("、");
				}
				sb.append(rs.getString("Description"));
			}
			 result = sb.toString();
			 sb.setLength(0);
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
	public List<Map<String,String>> getAgents(String investNo){
		List<Map<String,String>> result = new ArrayList<Map<String, String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT IN_AGENT,TEL_NO,POSITION_NAME FROM OFI_Agent where investment_no=? and resp_date = (");
		sb.append("select min(resp_date) FROM OFI_Agent where investment_no=? ");
		sb.append(") group by in_agent,tel_no,position_name");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, investNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				Map<String,String> sub = new HashMap<String, String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.put(meta.getColumnName(i),DataUtil.nulltoempty(rs.getString(i)));
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
	public Map<String,Map<String,String>> getAgent(String investorSeq){
		Map<String,Map<String,String>> result = new HashMap<String,Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (SELECT caseNo,IN_AGENT,TEL_NO,POSITION_NAME,ROW_NUMBER() over(Partition By caseNo Order By RESP_DATE asc,RECEIVE_NO asc) sort ");
		sb.append("FROM OFI_Agent a,OFI_InvestCaseList b where a.INVESTOR_SEQ=b.investorSeq and a.INVESTMENT_NO=b.investNo ");
		sb.append(")x where sort='1' and caseNo in (select caseNo from OFI_InvestCaseList where enable='1' and investorSeq=?)");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investorSeq);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			Map<String,String> sub;
			while(rs.next()){
				String caseNo=rs.getString("caseNo");
				if(result.containsKey(caseNo)){
					sub= result.get(caseNo);
				}else{
					sub= new HashMap<String, String>();
				}
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.put(meta.getColumnName(i),DataUtil.nulltoempty(rs.getString(i)));
				}
				result.put(caseNo, sub);
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
	public List<List<String>> getAgentInfos(String investorSeq){
		List<List<String>> result = new ArrayList<List<String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT distinct INVESTMENT_NO,COMP_CHTNAME,RESP_DATE,RECEIVE_NO,APPLICATION_NAME,IN_AGENT ");
		sb.append("FROM OFI_InvestorXAgentInfos ");
		sb.append("where IN_CHTNAME in (SELECT INVESTOR_CHTNAME FROM OFI_InvestCase where INVESTOR_SEQ=?)");
/*		sb.append("SELECT distinct INVESTMENT_NO,COMP_CHTNAME,RESP_DATE,RECEIVE_NO,APPLICATION_NAME,IN_AGENT ");
		sb.append("FROM STAFN100 where (INVESTMENT_NO like '4%' or INVESTMENT_NO like '5%') and IN_AGENT!='' ");
		sb.append("and IN_CHTNAME in (SELECT INVESTOR_CHTNAME FROM OFI_InvestCase where INVESTOR_SEQ=?)");
*/		String forStmt = sb.toString();
//		System.out.println(forStmt);
//		System.out.println(investorSeq);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investorSeq);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				List<String> list=new ArrayList<String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					list.add(DataUtil.nulltoempty(rs.getString(i)));
				}
				result.add(list);
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
//	public Map<String,List<Map<String,String>>> getContacts(String investorSeq){
//		Map<String,List<Map<String,String>>> result = new HashMap<String,List<Map<String,String>>>();
//		SQL sqltool = new SQL();
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT caseNo,name,telNo FROM OFI_InvestCaseXContacts ");
//		sb.append("where caseNo in (select caseNo from OFI_InvestCaseList where enable='1' and investorSeq=?) ");
//		sb.append("order by seq ");
//		String forStmt = sb.toString();
////		System.out.println(forStmt);
////		System.out.println(investorSeq);
//		sb.setLength(0);
//		try {
//			PreparedStatement stmt = sqltool.prepare(forStmt);
//			stmt.setString(1, investorSeq);
//			ResultSet rs = stmt.executeQuery();
//			ResultSetMetaData meta = rs.getMetaData();
//			List<Map<String,String>> list;
//			while(rs.next()){
//				String caseNo=rs.getString("caseNo");
//				if(result.containsKey(caseNo)){
//					list= result.get(caseNo);
//				}else{
//					list=new ArrayList<Map<String,String>>();
//				}
//				Map<String,String> sub= new HashMap<String, String>();
//				for(int i=1;i<=meta.getColumnCount();i++){
//					sub.put(meta.getColumnName(i),DataUtil.nulltoempty(rs.getString(i)));
//				}
//				list.add(sub);
//				result.put(caseNo, list);
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
//	public Map<String,List<Map<String,String>>> getAudits(String investorSeq){
//		Map<String,List<Map<String,String>>> result = new HashMap<String,List<Map<String,String>>>();
//		SQL sqltool = new SQL();
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT caseNo,name,telNo FROM OFI_InvestCaseXContacts ");
//		sb.append("where caseNo in (select caseNo from OFI_InvestCaseList where enable='1' and investorSeq=?) ");
//		sb.append("order by seq ");
//		String forStmt = sb.toString();
////		System.out.println(forStmt);
//		sb.setLength(0);
//		try {
//			PreparedStatement stmt = sqltool.prepare(forStmt);
//			stmt.setString(1, investorSeq);
//			ResultSet rs = stmt.executeQuery();
//			ResultSetMetaData meta = rs.getMetaData();
//			List<Map<String,String>> list;
//			while(rs.next()){
//				String caseNo=rs.getString("caseNo");
//				if(result.containsKey(caseNo)){
//					list= result.get(caseNo);
//				}else{
//					list=new ArrayList<Map<String,String>>();
//				}
//				Map<String,String> sub= new HashMap<String, String>();
//				for(int i=1;i<=meta.getColumnCount();i++){
//					sub.put(meta.getColumnName(i),DataUtil.nulltoempty(rs.getString(i)));
//				}
//				list.add(sub);
//				result.put(caseNo, list);
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
	public void updateIsFilled(String investorSeq,String investNo,String username) {
		StringBuilder sb = new StringBuilder();
		sb.append("update OFI_InvestCaseList set isFilled='1',updatetime=GETDATE(),updateuser=? ");
		sb.append("where caseNo in (SELECT a.caseNo FROM OFI_InvestCaseList a,ofi_investList b,");
		sb.append("OFI_InvestorList c where a.investNo=b.investNo and a.investorSeq=c.investorSeq ");
		sb.append(" and b.isFilled='1' and c.isFilled='1' and a.isFilled!='1' ");
		sb.append("and c.investorSeq=ISNULL(?,c.investorSeq) and b.investNo=ISNULL(?,b.investNo))");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, username);
			pstmt.setString(2, investorSeq);
			pstmt.setString(3, investNo);
			pstmt.executeUpdate();	
			sb.append("update OFI_InvestCaseList set isFilled='0',updatetime=GETDATE(),updateuser=? ");
			sb.append("where caseNo in (SELECT a.caseNo FROM OFI_InvestCaseList a,ofi_investList b,");
			sb.append("OFI_InvestorList c where a.investNo=b.investNo and a.investorSeq=c.investorSeq ");
			sb.append("and (b.isFilled='0' or c.isFilled='0') and a.isFilled='1' ");
			sb.append("and c.investorSeq=ISNULL(?,c.investorSeq) and b.investNo=ISNULL(?,b.investNo))");
			forStmt = sb.toString();
			sb.setLength(0);
			pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, username);
			pstmt.setString(2, investorSeq);
			pstmt.setString(3, investNo);
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
	
	public void updateInvestInfo(String investNo,String money1,String money2,String stockImp){
		StringBuilder sb = new StringBuilder();
		sb.append("update OFI_InvestCase set  IL_MONEY1=?, IL_MONEY2=?, IL_STOCK_IMP=? ");
		sb.append("where investNo = ISNULL(?, investNo)");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, money1);
			pstmt.setString(2, money2);
			pstmt.setString(3, stockImp);
			pstmt.setString(4, investNo);
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
	
}
