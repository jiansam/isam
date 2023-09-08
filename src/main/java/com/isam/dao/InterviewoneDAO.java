package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.dasin.tools.dTools;

import com.isam.bean.Interviewone;
import com.isam.bean.InterviewoneCI;
import com.isam.bean.InterviewoneCompany;
import com.isam.bean.InterviewoneYear;
import com.isam.bean.OFIInvestNoXTWSIC;
import com.isam.bean.OFIInvestorXBG;
import com.isam.bean.OFIInvestorXRelated;
import com.isam.bean.OFIInvestNoXAudit;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

import Lara.Utility.ToolsUtil;

public class InterviewoneDAO {
	
	public Map<String,String> getMaxInterviewDateYM(){
		Map<String,String> result= new HashMap<String, String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select left(MAX(value),3) year,SUBSTRING(MAX(value),4,2) month from (");
		sb.append("SELECT value FROM InterviewoneContent where optionId='1' ");
		sb.append("and qNo in (SELECT qNo FROM Interviewone where (interviewStatus='1' or interviewStatus='9') and enable='1')");
		sb.append("union all SELECT value FROM InterviewoneContent where optionId='32' ");
		sb.append("and qNo in (SELECT qNo FROM Interviewone where surveyStatus='1' and enable='1'))a");
		String forStmt = sb.toString();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.put("year", DataUtil.nulltoempty(rs.getString("year")));
				result.put("month", DataUtil.nulltoempty(rs.getString("month")));
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
	public List<String> getYearList(){
		List<String> result= new ArrayList<String>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT year FROM Interviewone where enable='1' group by year order by year desc";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString(1));
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
	public List<String> getYearList(String investNo,String reInvestNo){
		List<String> result= new ArrayList<String>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT year FROM Interviewone where enable='1' and investNo=isNull(?,investNo) and reInvestNo=isNull(?,reInvestNo) group by year order by year desc";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, reInvestNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString(1));
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
	public Map<String,String> getMaxYearEMPCount(String investNo){
		Map<String,String> result= new HashMap<String, String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select *,convert(float,replace([45], ',', ''))+convert(float,replace([46], ',', ''))+convert(float,replace([47], ',', '')) tt ");
		sb.append("from (SELECT ic.qNo,year,optionId,value FROM InterviewoneContent ic,");
		sb.append("(SELECT qNo,year FROM Interviewone where investNo=? and enable='1' and reInvestNo='0' and surveyStatus='1' ");
		sb.append("and year =(select MAX(year) from Interviewone where investNo=? and reInvestNo='0'and enable='1' ");
		sb.append("and surveyStatus='1'))i where optionId in ('45','46','47')  and ic.qNo=i.qNo)a pivot ");
		sb.append("(MAX(value) for a.optionid in ([45],[46],[47]))pvt");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, investNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				for (int i = 1; i <= meta.getColumnCount(); i++) {
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
	public Map<String,String> getMaxYearEMPCountRx(String reInvestNo){
		Map<String,String> result= new HashMap<String, String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select *,convert(float,replace([45], ',', ''))+convert(float,replace([46], ',', ''))+convert(float,replace([47], ',', '')) tt ");
		sb.append("from (SELECT ic.qNo,year,optionId,value FROM InterviewoneContent ic,");
		sb.append("(SELECT qNo,year FROM Interviewone where reInvestNo=? and enable='1' and surveyStatus='1' ");
		sb.append("and year =(select MAX(year) from Interviewone where reInvestNo=? and enable='1' ");
		sb.append("and surveyStatus='1'))i where optionId in ('45','46','47')  and ic.qNo=i.qNo)a pivot ");
		sb.append("(MAX(value) for a.optionid in ([45],[46],[47]))pvt");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, reInvestNo);
			stmt.setString(2, reInvestNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				for (int i = 1; i <= meta.getColumnCount(); i++) {
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
	public Map<String,String> getContacts(String investNo){
		Map<String,String> result= new HashMap<String, String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT b.pname,value FROM InterviewoneContent a ");
		sb.append(",(SELECT [optionId],[qType]+'_'+[paramName] pname FROM InterviewoneItem ");
		sb.append("where optionId in ('3','4','5','6','7','36','37','38','39','40'))b ");
		sb.append("where a.optionId=b.optionId and qNo in (select qNo from Interviewone ");
		sb.append("where enable='1' and reInvestNo = '0' and investNo=? and year=(select MAX(year) ");
		sb.append("from Interviewone where enable='1' and reInvestNo = '0' and investNo=?)) ");
		String forStmt = sb.toString();
		//System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.put(rs.getString(1),rs.getString(2));
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
	public int getQNoByYear(String year,String investNo,String reInvestNo){
		int result= 0;
		SQL sqltool = new SQL();
		String forStmt = "SELECT qNo FROM Interviewone where year=? and investNo=isnull(?,investNo) and reInvestNo=isnull(?,reInvestNo) and enable='1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1,year);
			stmt.setString(2,investNo);
			stmt.setString(3,reInvestNo);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result=rs.getInt("qNo");
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
	public int countByYear(String year,String survey,String interview){
		int result =0;
		SQL sqltool = new SQL();
		String forStmt = "SELECT count(qNo) FROM Interviewone where enable='1' and year=? and surveyStatus=isnull(?,surveyStatus) and interviewStatus =isnull(?,interviewStatus)";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, year);
			stmt.setString(2, survey);
			stmt.setString(3, interview);
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
	
	public List<Interviewone> select(Map<String,String> terms){
		List<Interviewone> result = new ArrayList<Interviewone>();
		String investNo=terms.get("investNo");
		String investName=terms.get("investName");
		String IDNO=terms.get("IDNO");
		String year=terms.get("year").isEmpty()?terms.get("maxY"):terms.get("year");
		String survey=terms.get("survey");
		String interview=terms.get("interview");
		if(survey!=null&&survey.equals("-1")){
			survey=null;
		}
		if(interview!=null&&interview.equals("-1")){
			interview=null;
		}
		String AndOr=terms.get("AndOr");
		String start=terms.get("start");
		String end=terms.get("end");
		List<String> abnormal=terms.get("abnormal")!=null&&!terms.get("abnormal").isEmpty()?DataUtil.StrArytoList(terms.get("abnormal").split(",")):null;
		String errMsgXnote=terms.get("errMsgXnote"); //107-08-01 新增異常條件狀況
		
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM Interviewone WHERE year=? and enable='1' and surveyStatus=isnull(?,surveyStatus) ");
		sb.append("and interviewStatus =isnull(?,interviewStatus) and (investNo in (select investNo from ( ");
		sb.append("select * from (SELECT MAX(OFITB102.INVESTMENT_NO) AS investNo,OFITB201.COMP_CHTNAME cname,isnull(OFITB202.BAN_NO,'') idno ");
		sb.append("FROM moeaic.dbo.OFITB102,  moeaic.dbo.OFITB201 left join moeaic.dbo.OFITB202 ");
		sb.append("on OFITB201.COMP_CHTNAME=OFITB202.COMP_CHTNAME where (OFITB102.INVESTMENT_NO like '4%' or OFITB102.INVESTMENT_NO like '5%') ");
		sb.append("and OFITB102.INVESTMENT_NO=OFITB201.INVESTMENT_NO group by OFITB102.[INVESTMENT_NO],OFITB201.COMP_CHTNAME,OFITB202.BAN_NO ");
		sb.append(")a)x where x.investNo like '").append(investNo).append("' and x.cname like '");
		sb.append(investName).append("' and x.idno like '").append(IDNO).append("' ) ");
		sb.append("or reInvestNo in(SELECT reInvestNo FROM dbo.ReInvestNoXBaseInfo (?) where investNo like '").append(investNo).append("' and cname like '");
		sb.append(investName).append("' and idno like '").append(IDNO).append("' )) ");
//		sb.append("and  qNo in (SELECT qNo FROM InterviewoneContent where optionId='1' and value>? and value<?) ");
		sb.append("and ( qNo in (SELECT qNo FROM InterviewoneContent where optionId='1' and value>? and value<?) ");
		sb.append("or  qNo in (SELECT qNo FROM InterviewoneContent where optionId='32' and value>? and value<?)) ");
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
					sb.append("qNo in (SELECT * FROM OFI_InterviewErrorList('").append(year).append("')) ");
				}else if(x.equals("2")){
					sb.append("qNo in (SELECT * FROM OFI_FinancialErrorList('").append(year).append("')) ");
				}else if(x.equals("3")){
					sb.append("(investNo in (select investNo from OFI_InvestNoXAudit where auditCode ='0603' and value<>'') and reInvestNo=0)");
				}
			}
			sb.append(")");
		}
		
		//107-08-01 新增
		if(errMsgXnote!=null&&!errMsgXnote.isEmpty()){
			
			//107-08-22 改成多選，檢查有沒有逗號隔開的多選項
			StringBuilder str = new StringBuilder();
			for(String val : ToolsUtil.getValueToList(errMsgXnote, ",")) {
				if(str.length() > 0) {
					str.append("and ");
				}
				str.append("value like '%"+val+"%' ");
			}
			
			sb.append("and qNo in (SELECT qNo FROM VW_InterviewoneAbnormal where "+str.toString()+" )");
		}
		
		String forStmt = sb.toString();
//		for (Entry<String, String> m:terms.entrySet()) {
//			System.out.println(m.getKey()+"="+m.getValue());
//		}
	//	System.out.println(forStmt);
		sb.setLength(0);
		
		try {
			HashMap<Integer, String> code_map = new HashMap<Integer, String>(); 
			PreparedStatement stmt = sqltool.prepare("SELECT * FROM InterviewoneContent WHERE optionId = 109 ");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				code_map.put(rs.getInt("qNo"), rs.getString("value"));
			}
			rs.close();
			stmt.close();
			
			stmt = sqltool.prepare(forStmt);
			stmt.setString(1, year);
			stmt.setString(2, survey);
			stmt.setString(3, interview);
			stmt.setString(4, year);
			if(!start.isEmpty()){
				stmt.setString(5, start);
				stmt.setString(6, end);
				stmt.setString(7, start);
				stmt.setString(8, end);
			}
			
			rs = stmt.executeQuery();
			while(rs.next()){
				Interviewone bean= new Interviewone();
				bean.setqNo(rs.getInt("qNo"));
				bean.setInvestNo(rs.getString("investNo"));
				bean.setReInvestNo(rs.getString("reInvestNo"));
				bean.setYear(rs.getString("year"));
				bean.setInterviewStatus(rs.getString("interviewStatus"));
				bean.setSurveyStatus(rs.getString("surveyStatus"));
				bean.setEnable(rs.getString("enable"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setMsg(rs.getString("msg"));
				
				bean.setBusinessIncomeTaxCode(dTools.trim(code_map.get(bean.getqNo())));
				
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
	
	private List<String> getRelatedList(){
		SQL sqltool = new SQL();
		List<String> result= new ArrayList<String>();
		String forStmt = "SELECT colname FROM OFI_GETALL_InvestorXRelated() group by colname order by colname";
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString(1));
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
	

	private String getCIInvestorDataSql(){
		Map<String,String> tmap=new HashMap<String, String>();
		tmap.put("aname", "母公司（或關連企業）及受益人名稱");
		tmap.put("nation", "母公司（或關連企業）及受益人國別");
		List<String> related=getRelatedList();
		StringBuilder sb = new StringBuilder();
		String OptPvt=DataUtil.fmtPvtItem(related);
		for (int i = 0; i < related.size(); i++) {
			String str=related.get(i);
			String[] strAry=str.split("_");			
			if(i!=0){
				sb.append(",");
			}
			sb.append("ir.[").append(str).append("] '").append(strAry[0]).append("_");
			sb.append(tmap.get(strAry[1])).append("'");
		}
		String titleStr=sb.toString();
		sb.setLength(0);
		sb.append("SELECT  * from ( ");
		sb.append("SELECT iic.investNo '陸資案號',ic.COMP_CHTNAME '國內事業名稱',ic.BAN_NO '統一編號',");
	
		sb.append("iic.INVESTOR_CHTNAME '投資人',(SELECT dbo.OFI_WebOptionName('isFilled',isFilled)) '資料狀態'");
		sb.append(",(SELECT dbo.OFI_WebOptionName('nation',nation)) '投資人國別'");
		sb.append(",(SELECT dbo.OFI_WebOptionName('cnCode',cnCode)) '投資人省分',iic.inSrc '資金類型',");

		sb.append(" iic.MONEY1 '核准金額',iic.MONEY2 '審定金額',iic.STOCK_IMP '審定股數',");
		sb.append(titleStr).append(",bg1.itemname '背景1-黨政軍案件',BG1Note '背景1-備註'");
		sb.append(",bg2.itemname '背景2-央企政府出資案件',BG2Note '背景2-備註'");
		sb.append(",case when f.c>0 then '有' else '無' end '架構圖',i.note '備註' FROM OFI_InvestorList i ");
		sb.append("left join (SELECT investNo,INVESTOR_SEQ,INVESTOR_CHTNAME,(SELECT dbo.OFI_WebOptionName('inSrc',INVE_ROLE_CODE)) inSrc");
		sb.append(",case when [StockPercent]>=0 and StockPercent<=1 then CONVERT(nvarchar(25),convert(numeric(18,2),round([StockPercent]*100,2)))+'%' ");
		sb.append("else '異常('+CONVERT(nvarchar(100),convert(numeric(18,2),round(StockPercent*100,2)))+'%)' end sp");
		sb.append(",(SELECT dbo.FormatNumber(investvalue)) investvalue");
		sb.append(",(SELECT dbo.FormatNumber(IL_MONEY1)) MONEY1");
		sb.append(",(SELECT dbo.FormatNumber(IL_MONEY2)) MONEY2");
		sb.append(",(SELECT dbo.FormatNumber(IL_STOCK_IMP)) STOCK_IMP");
		sb.append(",(SELECT dbo.FormatNumber(investedcapital)) investedcapital FROM OFI_InvestCase) iic ");
		sb.append("on iic.INVESTOR_SEQ=i.investorSeq left join(SELECT * FROM (SELECT * FROM OFI_GETALL_InvestorXRelated()) S ");
		sb.append("PIVOT (max(value) FOR colname IN (").append(OptPvt).append("))p)ir on ir.investorSeq=iic.INVESTOR_SEQ ");
		sb.append("left join (select * from (SELECT [investorSeq],[value],bgType FROM OFI_InvestorXBG where bgType like '%Note') A ");
		sb.append("pivot (max(value) for a.bgType in ([BG1Note],[BG2Note])) as pvt ) bgnote ");
		sb.append("on bgnote.investorSeq=i.investorSeq left join (");
		sb.append("SELECT * FROM OFI_GETALL_BG('BG1'))bg1 on i.investorSeq=bg1.investorSeq ");
		sb.append("left join (SELECT * FROM OFI_GETALL_BG('BG2'))bg2 on i.investorSeq=bg2.investorSeq ");
		sb.append("left join (SELECT count(fNo) c,investorSeq FROM OFI_InvestorXFile where enable='1' group by investorSeq) f ");
		sb.append("on i.investorSeq=f.investorSeq left join (select * FROM [moeaic].[dbo].[OFI_BASEDATA](null))ic ");
		sb.append("on ic.INVESTMENT_NO=iic.investno where i.enable='1' and iic.inSrc = '陸資' ");
		sb.append(" ) as [t0] ");
		String forStmt = sb.toString();
		sb.setLength(0);
		return forStmt;
	}
	private List<String> getAuditList(){
		SQL sqltool = new SQL();
		List<String> result= new ArrayList<String>();
		String forStmt = "SELECT value FROM OFI_GETALL_InvestCaseXAudit() where auditcode like '02%' and len(auditcode) = 4 group by value order by value";
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString("value"));
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
	private List<String> getInterviewOneList(){
		SQL sqltool = new SQL();
		List<String> result= new ArrayList<String>();
		String forStmt = "SELECT year FROM OFI_GETALL_InterviewOne() where year > 102  group by year order by year";
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString(1));
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
	private String getTIInvestorDataSql() {
		Map<String,String> tmap=new HashMap<String, String>();
		tmap.put("0201", "發文日期");
		tmap.put("0202", "文號");
		tmap.put("0203", "說明項次");
		tmap.put("0204", "附款內容");
		tmap.put("0205", "需繳交資料");
	
		List<String> auditList=getAuditList();
		
		List<String> yearList=getInterviewOneList();
		StringBuilder sb = new StringBuilder();
		String auditOptPvt=DataUtil.fmtPvtItem(auditList);

		String interviewOneOptPvt = DataUtil.fmtPvtItem(yearList);
		
		for (int i = 0; i < auditList.size(); i++) {
			String str=auditList.get(i);
			String[] strAry=str.split("_");		
		
			if(strAry.length!=2) {
				continue;
			}
			if(i!=0){
				sb.append(",");
			}
			sb.append("[").append(str).append("] '"+ strAry[0]+"_"+tmap.get(strAry[1])+"'");
		}
		String auditTitleStr=sb.toString();
		sb.setLength(0);
		for (int i = 0; i < yearList.size(); i++) {
			String str=yearList.get(i);
			String[] strAry=str.split("_");			
			
			if(i!=0){
				sb.append(",");
			}
			sb.append("[").append(str).append("] ");
		}
		String yearTitleStr=sb.toString();
		
		
		sb.setLength(0);
		sb.append("SELECT  * from ( ");
		sb.append("select a.[陸資案號], a.[國內事業名稱], a.[統一編號],a.[資料狀態],a.[經營狀況], (SELECT dbo.FormatNumber(a.[核准金額])) '核准金額', (SELECT dbo.FormatNumber(a.[審定金額])) '審定金額', (SELECT dbo.FormatNumber(a.[審定股數])) '審定股數',a.[初次審定或備查日期],a.[設立日期],a.[I3],a.[type] '涉及特許/特殊業別', a.[公文附加附款案件],");
		sb.append(auditTitleStr).append(",a.[協力機關要求標註關切案件],a.[協力機關],a.[備註],a.[委員會核准之重大投資案], a.[黨政軍背景], a.[央企政府出資背景],");
		sb.append(yearTitleStr).append(" from ( ");
		sb.append(" ");
		sb.append(" SELECT iic.investNo '陸資案號',iic.COMP_CHTNAME '國內事業名稱',iic.BAN_NO '統一編號',").append(auditOptPvt).append(",iic.協力機關要求標註關切案件 '協力機關要求標註關切案件', iic.協力機關,iic.[委員會核准之重大投資案], iic.備註,  ");
		sb.append(interviewOneOptPvt);
		sb.append(",(SELECT dbo.OFI_WebOptionName('isFilled',case when (exists (select 1 from OFI_InvestList a where a.investNo = iic.investno and a.isFilled='1' )) then '1' else '0' end)) '資料狀態',(SELECT dbo.OFI_WebOptionName('isOperated',isOperated)) '經營狀況', (iic.MONEY1 ) '核准金額',(iic.MONEY2) '審定金額',(iic.STOCK_IMP ) '審定股數', iic.approvaldate '初次審定或備查日期', iic.setupdate '設立日期', iic.I3 'I3', iic.[type] 'type', iic.公文附加附款案件 '公文附加附款案件' ,bg1itemname '黨政軍背景',bg2itemname '央企政府出資背景' ");
		sb.append("	FROM  ");
		sb.append("	 ");
		sb.append("	( ");
		sb.append("	 ");
		sb.append("	SELECT (select top 1 [isOperated] from  [OFI_InvestList] a where a.investNo = t0.investNo ) [isOperated],(select top 1 [approvaldate] from  [OFI_InvestList] a where a.investNo = t0.investNo ) [approvaldate] , ");
		sb.append("	(select top 1 [setupdate] from  [OFI_InvestList] a where a.investNo = t0.investNo ) [setupdate] ,   ");
		sb.append("	case when exists(select 1 from OFI_InvestNoXAudit [t3] where [t0].[investNo] = [t3].[investNo] and [t3].[auditCode] = '02' and [t3].[value] = '1') then '是' else '否' end '公文附加附款案件',  ");
		sb.append("	case when exists(select 1 from OFI_InvestNoXAudit [t3] where [t0].[investNo] = [t3].[investNo] and [t3].[auditCode] = '03' and [t3].[value] = '1') then '是' else '否' end '協力機關要求標註關切案件',  ");
		sb.append("	  STUFF( (SELECT '、'+cast((select name from (	SELECT  top 1000 a.code,case when a.[level]=2 then b.[description]+a.[description] else a.[description] end name FROM OFI_Department a left join OFI_Department b on a.parent=b.code order by code ) dp  where dp.code = [a].[value]) as nvarchar(50)) from OFI_InvestNoXAudit as [a] where [a].[InvestNo] = [t0].investNo and [a].[auditCode] = '0301' FOR XML PATH('')),1,1,'') '協力機關',");
		sb.append("	(select top 1 [value] from OFI_InvestNoXAudit [t3] where [t0].[investNo] = [t3].[investNo] and [t3].[auditCode] = '0399') '備註',  ");
		
		sb.append("	case when exists(select 1 from OFI_InvestNoXAudit [t3] where [t0].[investNo] = [t3].[investNo] and [t3].[auditCode] = '0703' ) then STUFF((SELECT '、'+cast(  [a].[value] as nvarchar(100)) from OFI_InvestNoXAudit as [a] where [a].[InvestNo] = [t0].[InvestNo] and [a].[auditCode] = '0703' order by [seq]  FOR XML PATH('')),1,1,'') else '否' end '委員會核准之重大投資案',  ");

		sb.append("	case when exists(select 1 from OFI_InvestNoXTWSIC [t3] where [t0].[investNo] = [t3].[investNo] and [t3].[item] like 'I3%' ) then 'O' end 'I3', ");
		sb.append("	case when exists(select 1 from OFI_InvestNoXTWSIC [t2] where [t0].[investNo] = [t2].[investNo] and [t2].[type] = '0' )  then 'O' else '' end 'type' , ");
		sb.append("	[t0].[investNo],COMP_CHTNAME,BAN_NO, sum(IL_MONEY1) 'MONEY1', sum(IL_MONEY2) 'MONEY2', sum(IL_STOCK_IMP) 'STOCK_IMP' ,");
		sb.append("	(	select case when  exists(SELECT itemname FROM OFI_GETALL_BG('BG1') where itemname<>'否' and investorSeq in (select INVESTOR_SEQ from OFI_InvestCase a where a.investNo=[t0].investNo ))\r\n"
				+ "	then (SELECT top 1 itemname FROM OFI_GETALL_BG('BG1') where itemname<>'否' and investorSeq in (select INVESTOR_SEQ from OFI_InvestCase a where a.investNo=[t0].investNo ))\r\n"
				+ "	else '否'\r\n"
				+ "	end) 'bg1itemname',\r\n"
				+ "(select case when  exists(SELECT itemname FROM OFI_GETALL_BG('BG2') where itemname<>'否' and investorSeq in (select INVESTOR_SEQ from OFI_InvestCase a where a.investNo=[t0].investNo ))\r\n"
				+ "	then (SELECT  top 1 itemname FROM OFI_GETALL_BG('BG2') where itemname<>'否' and investorSeq in (select INVESTOR_SEQ from OFI_InvestCase a where a.investNo=[t0].investNo ))\r\n"
				+ "	else '否'\r\n"
				+ "	end) 'bg2itemname' ");
		sb.append("	FROM OFI_InvestCase  as [t0] where dbo.OFI_WebOptionName('inSrc', [t0].INVE_ROLE_CODE) ='陸資' group by investno,BAN_NO,COMP_CHTNAME	) iic  ");
		sb.append(" ");
		sb.append(" ");
		sb.append("		left join(select * from  (SELECT * FROM OFI_GETALL_Audit() ) S PIVOT (max(value) for auditcode in (").append(auditOptPvt).append("))p)ir on ir.investNo=iic.investNo ");
		sb.append("		left join(	select * from  (SELECT * FROM OFI_GETALL_InterviewOne() where year > 102 ) S PIVOT (max(status) for year in (").append(interviewOneOptPvt).append("))p)iy on iy.investNo=iic.investNo ");
		
		sb.append("	) a ");
	//	sb.append("	group by a.[陸資案號], a.[國內事業名稱], a.[統一編號],a.[資料狀態],a.[經營狀況],a.[資金審定日期],a.[設立日期],a.[I3],a.[type], a.[公文附加附款案件], a.[黨政軍背景], a.[央企政府出資背景],"+auditOptPvt+",a.[協力機關要求標註關切案件],a.[協力機關],a.[備註],a.[委員會核准之重大投資案], "+interviewOneOptPvt);
	
		sb.append(" ) as [t0] ");
		String forStmt = sb.toString();
		sb.setLength(0);
		return forStmt;
	}
	public List<List<String>> getCIInvestorData(Map<String,String> terms){
		List<List<String>> result = new ArrayList<List<String>>();
		
		String investNo=terms.get("investNo");
		String investName=terms.get("investName");
		String IDNO=terms.get("IDNO");
		
		List<String> abnormal=terms.get("abnormal")!=null&&!terms.get("abnormal").isEmpty()?DataUtil.StrArytoList(terms.get("abnormal").split(",")):new ArrayList<String>();;
		
		SQL sqltool = new SQL();
		String forStmt = getCIInvestorDataSql();
		
		
		Set<String> condition = new HashSet<String>();
		if(investNo!=null && !investNo.replace("%","").isEmpty()) {
			condition.add(" [t0].[陸資案號] like ? ");
		}
		
		if(IDNO!=null && !IDNO.replace("%","").isEmpty()) {
			condition.add(" [t0].[統一編號] like ? ");
		}
		
		if(investName!=null && !investName.replace("%","").isEmpty()) {
			condition.add(" [t0].[國內事業名稱] like ? ");
		}
		

	
		
		for(String normal :abnormal) {
			switch(normal) {
			case "1":
				condition.add("  exists (SELECT top 1 * from OFI_InvestNoXTWSIC b WHERE b.investNo = [t0].[陸資案號] and  [b].[item] like 'I3%' ) ");
				break;
			case "2":
				condition.add("  exists (SELECT top 1 * from OFI_InvestNoXTWSIC b WHERE b.investNo =[t0].[陸資案號] and [b].[type] ='0'  ) ");
				break;
			case "3":
				condition.add("  exists (SELECT top 1 * from OFI_InvestNoXAudit b WHERE b.investNo = [t0].[陸資案號] and [b].[auditCode] ='02' and [b].[value] = '1')");
				break;
			case "4":
				condition.add("  exists (SELECT top 1 * from OFI_InvestNoXAudit b WHERE b.investNo =[t0].[陸資案號] and [b].[auditCode] ='07' and [b].[value] = '1')");
				break;
			case "5":
				condition.add(" [t0].[背景1-黨政軍案件] <>  '否' ");
				break;
			case "6":
				condition.add(" [t0].[背景2-央企政府出資案件] <>  '否' ");
				break;
			}
		}
		
		if(condition.size()>0) {
		
			forStmt = forStmt + (" where  "+String.join(" and ", condition));
		}
	
		forStmt = forStmt +" order by [陸資案號]";
		
		
		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			
			int currentArg = 1;
			if(investNo!=null && !investNo.replace("%","").isEmpty()) {
				stmt.setString(currentArg++, investNo);
			}
			
			if(IDNO!=null && !IDNO.replace("%","").isEmpty()) {
				stmt.setString(currentArg++, IDNO);
			}
			
			if(investName!=null && !investName.replace("%","").isEmpty()) {
				stmt.setString(currentArg++, investName);
			}
			
			ResultSet rs = stmt.executeQuery();
			
			
			
			ResultSetMetaData meta = rs.getMetaData();
			List<String> title = new ArrayList<String>();
			int flag=0;
			while(rs.next()){
				List<String> sub = new ArrayList<String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					if(flag==0){
						title.add(meta.getColumnName(i));
					}
					sub.add(DataUtil.nulltoempty(rs.getString(i)));
				}
				result.add(sub);
				flag=1;
			}
			result.add(0,title);
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
	
	public List<List<String>> getTIInvestorData(Map<String,String> terms){
		List<List<String>> result = new ArrayList<List<String>>();
		
		String investNo=terms.get("investNo");
		String investName=terms.get("investName");
		String IDNO=terms.get("IDNO");
		
		List<String> abnormal=!terms.get("abnormal").isEmpty()?DataUtil.StrArytoList(terms.get("abnormal").split(",")):new ArrayList<String>();
		
		SQL sqltool = new SQL();
		String forStmt = getTIInvestorDataSql();
		
		
	//	System.out.println(forStmt);
		Set<String> condition = new HashSet<String>();
		if(investNo!=null && !investNo.replace("%","").isEmpty()) {
			condition.add(" [t0].[陸資案號] like ? ");
		}
		
		if(IDNO!=null && !IDNO.replace("%","").isEmpty()) {
			condition.add(" [t0].[統一編號] like ? ");
		}
		
		if(investName!=null && !investName.replace("%","").isEmpty()) {
			condition.add(" [t0].[國內事業名稱] like ? ");
		}
		
		
		if(abnormal.contains("1")) {
			condition.add(" [t0].[I3] = 'O' ");
		}
		if(abnormal.contains("2")) {
			condition.add(" [t0].[涉及特許/特殊業別] = 'O' ");
		}
		if(abnormal.contains("3")) {
			condition.add(" [t0].[公文附加附款案件] = '是' ");
		}
		if(abnormal.contains("4")) {
			condition.add(" [t0].[委員會核准之重大投資案] <> '否'  ");
		}
		if(abnormal.contains("5")) {
			condition.add(" [t0].[黨政軍背景] <>  '否' ");
		}
		if(abnormal.contains("6")) {
			condition.add(" [t0].[央企政府出資背景] <>  '否' ");
		}
		

		if(condition.size()>0) {
		
			forStmt = forStmt + (" where  "+String.join(" and ", condition));
		}
		forStmt = forStmt +" order by [陸資案號]";
		
		
		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			
			int currentArg = 1;
			if(investNo!=null && !investNo.replace("%","").isEmpty()) {
				stmt.setString(currentArg++, investNo);
			}
			
			if(IDNO!=null && !IDNO.replace("%","").isEmpty()) {
				stmt.setString(currentArg++, IDNO);
			}
			
			if(investName!=null && !investName.replace("%","").isEmpty()) {
				stmt.setString(currentArg++, investName);
			}
			
			ResultSet rs = stmt.executeQuery();
			
			
			
			ResultSetMetaData meta = rs.getMetaData();
			List<String> title = new ArrayList<String>();
			int flag=0;
			while(rs.next()){
				List<String> sub = new ArrayList<String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					if(flag==0){
						title.add(meta.getColumnName(i));
					}
					sub.add(DataUtil.nulltoempty(rs.getString(i)));
				}
				result.add(sub);
				flag=1;
			}
			result.add(0,title);
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
	
	
	
	public List<InterviewoneCompany> selectByCompany(Map<String,String> terms){
		List<InterviewoneCompany> result = new ArrayList<InterviewoneCompany>();
		String investNo=terms.getOrDefault("investNo","");
		String investName=terms.getOrDefault("investName","");
		String IDNO=terms.getOrDefault("IDNO","");
		String survey=terms.getOrDefault("status","");
		String interview=terms.getOrDefault("type","");
		
		String startYear = terms.getOrDefault("startyear", "");
		String endYear = terms.getOrDefault("endyear", "");
		List<String> abnormal=terms.get("abnormal")!=null&&!terms.get("abnormal").isEmpty()?DataUtil.StrArytoList(terms.get("abnormal").split(",")):new ArrayList<String>();
		

	//	String errMsgXnote=terms.get("errMsgXnote"); //107-08-01 新增異常條件狀況
		
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * from ( ");
		sb.append("SELECT [t4].[year] AS [Year], [t3].[investNo] AS [InvestNo], [t3].[COMP_CHTNAME] AS [COMP_CHTNAME], [t3].[BAN_NO] AS [BAN_NO], [t13].[value2] AS [I], [t13].[value3] AS [S] , [t13].[value5] as [IfNo], [t13].[value4] as [SfNo] ");
		sb.append("FROM ( ");
		sb.append("    SELECT [t0].[investNo] ");
		sb.append("    FROM [OFI_InvestCase] AS [t0] ");
		
		
		Set<String> condition = new HashSet<String>();
		if(investNo!=null && !investNo.replace("%","").isEmpty()) {
			condition.add(" [t0].[InvestNo] like ? ");
		}
		
		if(IDNO!=null && !IDNO.replace("%","").isEmpty()) {
			condition.add(" [t0].[BAN_NO] like ? ");
		}
		
		if(investName!=null && !investName.replace("%","").isEmpty()) {
			condition.add(" [t0].[COMP_CHTNAME] like ? ");
		}
		
		
		if(condition.size()>0) {
		
		sb.append(" where  "+String.join(" and ", condition));
		}
		
		condition.clear();
		
		//sb.append("    WHERE ([t0].[investNo] = ?) AND ([t0].[BAN_NO] = ?) AND ([t0].[COMP_CHTNAME] = ?) ");
		sb.append("    GROUP BY [t0].[investNo] ");
		sb.append("    ) AS [t1] ");
		sb.append("OUTER APPLY ( ");
		sb.append("    SELECT TOP (1) [t2].[investNo], [t2].[BAN_NO], [t2].[COMP_CHTNAME] ");
		sb.append("    FROM [OFI_InvestCase] AS [t2] ");
		sb.append("    WHERE ((([t1].[investNo] IS NULL) AND ([t2].[investNo] IS NULL)) OR (([t1].[investNo] IS NOT NULL) AND ([t2].[investNo] IS NOT NULL) AND ([t1].[investNo] = [t2].[investNo])))");
		if(investNo!=null && !investNo.replace("%","").isEmpty()) {
			condition.add(" [t2].[InvestNo] like ? ");
		}
		
		if(IDNO!=null && !IDNO.replace("%","").isEmpty()) {
			condition.add(" [t2].[BAN_NO] like ? ");
		}
		
		if(investName!=null && !investName.replace("%","").isEmpty()) {
			condition.add(" [t2].[COMP_CHTNAME] like ? ");
		}
		
		
		if(condition.size()>0) {
			
			sb.append(" AND  "+String.join(" and ", condition));
			}
		
	//	sb.append("  AND ([t2].[investNo] = ?) AND ([t2].[BAN_NO] = ?) AND ([t2].[COMP_CHTNAME] = ?) ");
		sb.append("    ) AS [t3] ");
		sb.append("INNER JOIN ([Interviewone] AS [t4] ");
		sb.append("    INNER JOIN ( ");
		sb.append("        SELECT ( ");
		sb.append("            SELECT [t8].[qNo] \r\n");
		sb.append("            FROM ( \r\n");
		sb.append("                SELECT TOP (1) [t7].[qNo] \r\n");
		sb.append("                FROM [InterviewoneFile] AS [t7] \r\n");
		sb.append("                WHERE [t6].[qNo] = [t7].[qNo] \r\n");
		sb.append("                ) AS [t8] \r\n");
		sb.append("            ) AS [value],STUFF((  SELECT  ','+cast( [t10].[fName]     as nvarchar(200))             FROM (                  SELECT  [t9].[fName]                    FROM [InterviewoneFile] AS [t9]                   WHERE ([t9].[qType] ='I') AND ([t6].[qNo] = [t9].[qNo])                   ) AS [t10]                  FOR XML PATH('')) ,1,1,'' ) AS [value2],  ");
		sb.append("            STUFF(( SELECT ','+cast([t12].[fNo]     as varchar(200))              FROM (                   SELECT  [t11].[fNo]                  FROM [InterviewoneFile] AS [t11]                   WHERE ([t11].[qType] = 'I') AND ([t6].[qNo] = [t11].[qNo])                   ) AS [t12]                FOR XML PATH('')) ,1,1,'' )  AS [value5] , ");
		sb.append("			  ");
		sb.append("							STUFF((              SELECT ','+cast([t12].[fName]     as nvarchar(200))           FROM (                   SELECT  [t11].[fName] , [t11].[fNo]                  FROM [InterviewoneFile] AS [t11]                   WHERE ([t11].[qType] = 'S') AND ([t6].[qNo] = [t11].[qNo])                   ) AS [t12]                 FOR XML PATH('')) ,1,1,'' )  AS [value3] , ");
		sb.append("           	STUFF((               SELECT ','+cast([t12].[fNo]     as varchar(200))           FROM (                   SELECT   [t11].[fNo]                  FROM [InterviewoneFile] AS [t11]                   WHERE ([t11].[qType] = 'S') AND ([t6].[qNo] = [t11].[qNo])                   ) AS [t12]              FOR XML PATH('')) ,1,1,'' ) AS [value4]  ");
		sb.append("        FROM ( ");
		sb.append("            SELECT [t5].[qNo] ");
		sb.append("            FROM [InterviewoneFile] AS [t5] ");
		sb.append("            GROUP BY [t5].[qNo] ");
		sb.append("            ) AS [t6] ");
		sb.append("        ) AS [t13] ON [t4].[qNo] = [t13].[value]) ON [t3].[investNo] = [t4].[investNo] ");
		sb.append("WHERE ([t4].[enable] = '1')  AND  ");
		
		if(survey != null && survey.equals("3")) {
			sb.append("(([t4].[interviewStatus] = 1) OR ([t4].[interviewStatus] = 9))");
		}
		else if (survey != null && survey.equals("2")) {
			sb.append(" ([t4].[interviewStatus] = 9)");
		}
		else {
			sb.append(" ([t4].[interviewStatus] = 1)");
		}
		
		if(!startYear.isEmpty()&& !endYear.isEmpty()) {
			sb.append("AND ([t4].[year] between ? and ?)");
		}
		
	for(String normal :abnormal) {
		switch(normal) {
		case "1":
			sb.append("   and exists (SELECT 1 from OFI_InvestNoXTWSIC b WHERE b.investNo = [t4].[investno] and  [b].[item] like 'I3%' ) ");
			break;
		case "2":
			sb.append(" and exists (SELECT 1 from OFI_InvestNoXTWSIC b WHERE b.investNo = [t4].[investno] and [b].[type] ='0'  ) ");
			break;
		case "3":
			sb.append(" and exists (SELECT 1 from OFI_InvestNoXAudit b WHERE b.investNo = [t4].[investno] and [b].[auditCode] ='02' and [b].[value] = '1')");
			break;
		case "4":
			sb.append(" and exists (SELECT 1 from OFI_InvestNoXAudit b WHERE b.investNo = [t4].[investno] and [b].[auditCode] ='07' and [b].[value] = '1')");
			break;
		case "5":
			sb.append(" 	and exists (SELECT 1 from OFI_InvestorXBG b where b.investorSeq in (select [INVESTOR_SEQ] from OFI_INVESTCASE c where c.investNo = t4.investNo) and [b].[bgType]= 'BG1' and [b].[value] in ('1','2','3')) ");
		break;
		case "6":
			sb.append(" and exists (SELECT 1 from OFI_InvestorXBG b where b.investorSeq in (select [INVESTOR_SEQ] from OFI_INVESTCASE c where c.investNo = t4.investNo) and [b].[bgType]= 'BG2' and [b].[value] in ('1','2') ) ");
			break;
		}
	}
		
		sb.append(")  a ");
			
	    if(interview!=null && !interview.equals("3")) {
	    	sb.append(" where  ");
	    	if(interview!=null && interview.equals("2")) {
	    		sb.append("   [a].[s] is not null ");
	    	}
	    	else {
	    		sb.append("   [a].[i] is not null ");
	    	}
	    }
		
		
		String forStmt = sb.toString();
//		for (Entry<String, String> m:terms.entrySet()) {
//			System.out.println(m.getKey()+"="+m.getValue());
//		}
	System.out.println(forStmt);
		sb.setLength(0);
		
		try {
			HashMap<Integer, String> code_map = new HashMap<Integer, String>(); 
			PreparedStatement stmt = sqltool.prepare("SELECT * FROM InterviewoneContent WHERE optionId = 109 ");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				code_map.put(rs.getInt("qNo"), rs.getString("value"));
			}
			rs.close();
			stmt.close();
			
			stmt = sqltool.prepare(forStmt);
			int currentArgs = 1;
			if(investNo!=null && !investNo.replace("%","").isEmpty()) {
				stmt.setString(currentArgs++, investNo);
			}
			
			if(IDNO!=null && !IDNO.replace("%","").isEmpty()) {
				stmt.setString(currentArgs++, IDNO);
			}
			
			if(investName!=null && !investName.replace("%","").isEmpty()) {
				stmt.setString(currentArgs++, investName);
			}
			
			
			if(investNo!=null && !investNo.replace("%","").isEmpty()) {
				stmt.setString(currentArgs++, investNo);
			}
			
			if(IDNO!=null && !IDNO.replace("%","").isEmpty()) {
				stmt.setString(currentArgs++, IDNO);
			}
			
			if(investName!=null && !investName.replace("%","").isEmpty()) {
				stmt.setString(currentArgs++, investName);
			}
			
		
			if(!startYear.isEmpty()&& !endYear.isEmpty()) {
				stmt.setString(currentArgs++, startYear);
				stmt.setString(currentArgs++, endYear);
			}
		
		
		
			
			rs = stmt.executeQuery();
			while(rs.next()){
				InterviewoneCompany bean= new InterviewoneCompany();
			//	bean.setqNo(rs.getInt("qNo"));
				bean.setInvestNo(rs.getString("investNo"));
			
				bean.setYear(rs.getString("year"));
				bean.setCompanyName(rs.getString("COMP_CHTNAME"));
				bean.setBanNo(rs.getString("BAN_NO"));
				bean.setInterviewTable(rs.getString("I"));
				bean.setSurvey(rs.getString("S"));
				bean.setInterviewFNo(rs.getString("IfNo"));
				bean.setSurveyFNo(rs.getString("SfNo"));
				
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
	
//	
//	public List<InterviewoneYear> selectByYear(Map<String,String> terms){
//		List<InterviewoneYear> result = new ArrayList<InterviewoneYear>();
//
//		
//		String investNo=terms.get("investNo");
//		String investName=terms.get("investName");
//		String IDNO=terms.get("IDNO");
//	
//		Map<String,String> isFilledOption = new HashMap<String, String>();
//		Map<String,String> nationOption = new HashMap<String, String>();
//		Map<String,String> isOperatedOption = new HashMap<String, String>();
//		Map<String,String> cnCodeOption = new HashMap<String, String>();
//		
//		List<String> abnormal=terms.get("abnormal")!=null&&!terms.get("abnormal").isEmpty()?DataUtil.StrArytoList(terms.get("abnormal").split(",")):null;
//		String errMsgXnote=terms.get("errMsgXnote"); //107-08-01 新增異常條件狀況
//		
//		if(abnormal!=null) {
//			System.out.println("abnormal:"+String.join(",", abnormal));
//		}
//		SQL sqltool = new SQL();
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT  ");
//		sb.append("[t0].*, ");
//		sb.append("STUFF((SELECT ','+cast([a].[INVESTOR_SEQ] as varchar(10)) from OFI_INVESTCASE as [a] where [a].[InvestNo] = [t0].[InvestNo] FOR XML PATH('')),1,1,'') as [INVESTOR_SEQ] ");
//		sb.append(" ");
//		sb.append("from ( ");
//		sb.append(" ");
//		sb.append("select  [t0].[investNo] AS [InvestNo], isnull( [t0].[BAN_NO],'') as [BAN_NO], [t0].[COMP_CHTNAME],sum( isnull([t0].[MONEY1], 0 )) as [MONEY1], sum(isnull([t0].[MONEY2],0)) as [MONEY2], sum(isnull([t0].[STOCK_IMP],0)) as [STOCK_IMP] , sum( isnull([t0].[IL_MONEY1],0) )as [IL_MONEY1],sum( isnull( [t0].[IL_MONEY2],0) ) as [IL_MONEY2], sum(isnull([t0].[IL_STOCK_IMP],0)) as [IL_STOCK_IMP], ");
//		sb.append("	[t1].[enable],[t1].[isfilled], [t1].[isOperated], [t1].[Setupdate], [t1].[Approvaldate] ");
//		sb.append("	, [t2].[nation], [t2].[cncode] ");
//		sb.append("	from  ");
//		sb.append("	( ");
//		sb.append("	SELECT  [t0].[INVESTOR_SEQ] as [INVESTOR_SEQ], [t0].[investNo] AS [InvestNo], isnull( [t0].[BAN_NO],'') as [BAN_NO], [t0].[COMP_CHTNAME], isnull([t0].[MONEY1], 0 ) as [MONEY1], isnull([t0].[MONEY2],0) as [MONEY2], isnull([t0].[STOCK_IMP],0) as [STOCK_IMP] ,  isnull([t0].[IL_MONEY1],0) as [IL_MONEY1], isnull( [t0].[IL_MONEY2],0) as [IL_MONEY2], isnull([t0].[IL_STOCK_IMP],0) as [IL_STOCK_IMP], ");
//		sb.append("	[t1].[enable], [t1].[isfilled] ");
//		sb.append("	FROM  (SELECT * FROM  [OFI_InvestCase] WHERE investno in (\r\n"
//				+ "				SELECT  [t0].[investNo]\r\n"
//				+ "FROM [Interviewone] AS [t0]\r\n"
//				+ "WHERE ([t0].[enable] = '1') AND ([t0].[interviewStatus] = '1')\r\n"
//				+ ") ) AS [t0] 	 ");
//		sb.append("	inner join [OFI_InvestCaseList] AS [t1] on [t0].[InvestNo] = [t1].[investNo] and [t0].[INVESTOR_SEQ] = [t1].[investorSeq] ) as  [t0] ");
//		sb.append("inner join [OFI_InvestList] as [t1] on [t0].[InvestNo] = [t1].[InvestNo] ");
//		sb.append("inner join [OFI_InvestorList] as [t2] on [t0].[INVESTOR_SEQ] = [t2].[investorSeq] ");
//		sb.append(" ");
//		sb.append("group by [t0].[investNo], [t0].[BAN_NO],[t0].[COMP_CHTNAME],[t1].[enable], [t1].[isfilled], [t1].[isOperated], [t1].[Setupdate], [t1].[Approvaldate] ,[t2].[nation], [t2].[cncode] ");
//		sb.append(" ");
//		sb.append(") as [t0] ");
//		
//		Set<String> condition = new HashSet<String>();
//		if(investNo!=null && !investNo.replace("%","").isEmpty()) {
//			condition.add(" [t0].[InvestNo] like ? ");
//		}
//		
//		if(IDNO!=null && !IDNO.replace("%","").isEmpty()) {
//			condition.add(" [t0].[BAN_NO] like ? ");
//		}
//		
//		if(investName!=null && !investName.replace("%","").isEmpty()) {
//			condition.add(" [t0].[COMP_CHTNAME] like ? ");
//		}
//		
//		
//		if(condition.size()>0) {
//		
//		sb.append(" where  "+String.join(" and ", condition));
//		}
//		System.out.println(sb.toString() +" : "+investNo+" : "+IDNO+" : "+investName);
//		//107-08-01 新增
//		if(errMsgXnote!=null&&!errMsgXnote.isEmpty()){
//			
//			//107-08-22 改成多選，檢查有沒有逗號隔開的多選項
//			StringBuilder str = new StringBuilder();
//			for(String val : ToolsUtil.getValueToList(errMsgXnote, ",")) {
//				if(str.length() > 0) {
//					str.append("and ");
//				}
//				str.append("value like '%"+val+"%' ");
//			}
//			
//			sb.append("and qNo in (SELECT qNo FROM VW_InterviewoneAbnormal where "+str.toString()+" )");
//		}
//		
//		String forStmt = sb.toString();
////		for (Entry<String, String> m:terms.entrySet()) {
////			System.out.println(m.getKey()+"="+m.getValue());
////		}
////		System.out.println(forStmt);
//		sb.setLength(0);
//		
//		try {
//			//HashMap<Integer, String> code_map = new HashMap<Integer, String>(); 
//			PreparedStatement stmt = sqltool.prepare(forStmt);
//			ResultSet rs ;// stmt.executeQuery();
//			
//			stmt = sqltool.prepare("SELECT [t0].[Identifier], [t0].[Description], [t0].[SelectName], [t0].[OptionValue], [t0].[seq] AS [Seq], [t0].[enable] AS [Enable], [t0].[updatetime] AS [Updatetime], [t0].[updateuser] AS [Updateuser], [t0].[createtime] AS [Createtime], [t0].[createuser] AS [Createuser]\r\n"
//					+ "FROM [OFI_WebOption] AS [t0]\r\n"
//					+ "WHERE [t0].[SelectName]  = ?");
//			
//			stmt.setString(1, "isFilled");
//			rs = stmt.executeQuery();
//			while(rs.next()){
//				isFilledOption.put(rs.getString("OptionValue"),rs.getString("Description"));
//				
//			}
//			rs.close();
//			stmt.close();
//			
//			stmt = sqltool.prepare("SELECT [t0].[Identifier], [t0].[Description], [t0].[SelectName], [t0].[OptionValue], [t0].[seq] AS [Seq], [t0].[enable] AS [Enable], [t0].[updatetime] AS [Updatetime], [t0].[updateuser] AS [Updateuser], [t0].[createtime] AS [Createtime], [t0].[createuser] AS [Createuser]\r\n"
//					+ "FROM [OFI_WebOption] AS [t0]\r\n"
//					+ "WHERE [t0].[SelectName]  = ?");
//			
//			stmt.setString(1, "isOperated");
//			rs = stmt.executeQuery();
//			while(rs.next()){
//				isOperatedOption.put(rs.getString("OptionValue"),rs.getString("Description"));
//				
//			}
//			rs.close();
//			stmt.close();
//			
//			stmt = sqltool.prepare("SELECT [t0].[Identifier], [t0].[Description], [t0].[SelectName], [t0].[OptionValue], [t0].[seq] AS [Seq], [t0].[enable] AS [Enable], [t0].[updatetime] AS [Updatetime], [t0].[updateuser] AS [Updateuser], [t0].[createtime] AS [Createtime], [t0].[createuser] AS [Createuser]\r\n"
//					+ "FROM [OFI_WebOption] AS [t0]\r\n"
//					+ "WHERE [t0].[SelectName]  = ?");
//			
//			stmt.setString(1, "nation");
//			rs = stmt.executeQuery();
//			while(rs.next()){
//				nationOption.put(rs.getString("OptionValue"),rs.getString("Description"));
//				
//			}
//			rs.close();
//			stmt.close();
//			
//			stmt = sqltool.prepare("SELECT [t0].[Identifier], [t0].[Description], [t0].[SelectName], [t0].[OptionValue], [t0].[seq] AS [Seq], [t0].[enable] AS [Enable], [t0].[updatetime] AS [Updatetime], [t0].[updateuser] AS [Updateuser], [t0].[createtime] AS [Createtime], [t0].[createuser] AS [Createuser]\r\n"
//					+ "FROM [OFI_WebOption] AS [t0]\r\n"
//					+ "WHERE [t0].[SelectName]  = ?");
//			
//			stmt.setString(1, "cnCode");
//			rs = stmt.executeQuery();
//			while(rs.next()){
//				cnCodeOption.put(rs.getString("OptionValue"),rs.getString("Description"));
//				
//			}
//			rs.close();
//			stmt.close();
//		//	stmt.close();
//			
//			stmt = sqltool.prepare(forStmt);
//			int currentArg = 1;
//			if(investNo!=null && !investNo.replace("%","").isEmpty()) {
//				stmt.setString(currentArg++, investNo);
//			}
//			
//			if(IDNO!=null && !IDNO.replace("%","").isEmpty()) {
//				stmt.setString(currentArg++, IDNO);
//			}
//			
//			if(investName!=null && !investName.replace("%","").isEmpty()) {
//				stmt.setString(currentArg++, investName);
//			}
//			
//		/*	stmt.setString(1, year);
//			stmt.setString(2, survey);
//			stmt.setString(3, interview);
//			stmt.setString(4, year);
//			if(!start.isEmpty()){
//				stmt.setString(5, start);
//				stmt.setString(6, end);
//				stmt.setString(7, start);
//				stmt.setString(8, end);
//			}
//			*/
//			
//			
//			rs = stmt.executeQuery();
//			while(rs.next()){
//				InterviewoneYear bean= new InterviewoneYear();
//			
//				bean.setInvestNo(rs.getString("InvestNo"));
//				bean.setCompanyName(rs.getString("COMP_CHTNAME"));
//				bean.setBanNo(rs.getString("Ban_no"));
//				bean.setMoney1(rs.getInt("IL_money1"));
//				bean.setMoney2(rs.getInt("IL_money2"));
//				bean.setStockimp(rs.getInt("IL_stock_imp"));
//				bean.setEnable(rs.getString("enable"));
//				bean.setIsFilled(isFilledOption.get(rs.getString("isfilled")));
//				bean.setIsOperated(isOperatedOption.get(rs.getString("isoperated")));
//				bean.setNation(nationOption.get(rs.getString("nation")));
//				bean.setCnCode(cnCodeOption.get(rs.getString("cncode")));
//				bean.setApprovalDate(rs.getString("approvaldate"));
//				bean.setSetupDate(rs.getString("setupdate"));
//				bean.setInvestorSeq(rs.getString("INVESTOR_SEQ"));
//		
//			
//				
//				result.add(bean);
//			}
//			rs.close();
//			stmt.close();
//			
//			System.out.println("處理 OFI_InvestNoXTWSIC");
//			
//			for(InterviewoneYear ea : result) {
//				stmt = sqltool.prepare("SELECT  [t0].[investNo] AS [InvestNo], [t0].[item] AS [Item], [t0].[type] AS [Type], [t0].[seq] AS [Seq], [t0].[updatetime] AS [Updatetime], [t0].[updateuser] AS [Updateuser] "
//						+ "FROM [OFI_InvestNoXTWSIC] AS [t0] WHERE [t0].[investNo] = ?");
//				
//				stmt.setString(1, ea.getInvestNo());
//				rs = stmt.executeQuery();
//				while(rs.next()){
//					OFIInvestNoXTWSIC bean= new OFIInvestNoXTWSIC();
//				
//					bean.setInvestNo(rs.getString("InvestNo"));
//					bean.setItem(rs.getString("item"));
//					bean.setType(rs.getString("type"));
//				
//					ea.getTwsic().add(bean);
//				}
//				rs.close();
//				stmt.close();
//			}
//		
//			System.out.println("處理 OFI_InvestNoXAudit");
//			for(InterviewoneYear ea : result) {
//				stmt = sqltool.prepare("SELECT  [t0].[investNo] AS [InvestNo], [t0].[auditCode] AS [AuditCode], [t0].[value] AS [Value], [t0].[seq] AS [Seq], [t0].[createtime] AS [Createtime], [t0].[createuser] AS [Createuser] "
//						+ " FROM [OFI_InvestNoXAudit] AS [t0] "
//						+ " WHERE [t0].[investNo] = ?");
//				
//				stmt.setString(1, ea.getInvestNo());
//				rs = stmt.executeQuery();
//				while(rs.next()){
//					OFIInvestNoXAudit bean= new OFIInvestNoXAudit();
//				
//					bean.setInvestNo(rs.getString("InvestNo"));
//					
//					bean.setAuditCode(rs.getString("AuditCode"));
//					bean.setValue(rs.getString("Value"));
//					bean.setSeq(rs.getInt("Seq"));
//					ea.getAudit().add(bean);
//				}
//				rs.close();
//				stmt.close();
//			}
//			
//			System.out.println("處理 Interviewone");
//			for(InterviewoneYear ea : result) {
//				stmt = sqltool.prepare("SELECT  [t0].[qNo] AS [QNo], [t0].[investNo] AS [InvestNo], [t0].[reInvestNo] AS [ReInvestNo], [t0].[year] AS [Year], [t0].[interviewStatus] AS [InterviewStatus], [t0].[surveyStatus] AS [SurveyStatus], [t0].[enable] AS [Enable], [t0].[updatetime] AS [Updatetime], [t0].[updateuser] AS [Updateuser], [t0].[createtime] AS [Createtime], [t0].[createuser] AS [Createuser], [t0].[msg] AS [Msg]\r\n"
//						+ " FROM [Interviewone] AS [t0] \r\n"
//					
//						+ " WHERE [t0].[enable]='1' and [t0].[interviewStatus] = '1' and [t0].[investNo] = ?");
//				
//				stmt.setString(1, ea.getInvestNo());
//				rs = stmt.executeQuery();
//				while(rs.next()){
//					Interviewone bean= new Interviewone();
//				
//					bean.setInvestNo(rs.getString("InvestNo"));
//					bean.setReInvestNo(rs.getString("ReInvestNo"));
//					bean.setYear(rs.getString("Year"));
//					bean.setInterviewStatus(rs.getString("interviewStatus"));
//					bean.setSurveyStatus(rs.getString("surveyStatus"));
//					bean.setEnable(rs.getString("enable"));
//					ea.getInterviewone().add(bean);
//				}
//				rs.close();
//				stmt.close();
//			}
//			
//			System.out.println("處理 OFI_InvestorXBG");
//			for(InterviewoneYear ea : result) {
//				if(ea.getInvestorSeq().isEmpty()) {
//					continue;
//				}
//				stmt = sqltool.prepare("SELECT  [t0].[investorSeq] AS [InvestorSeq], [t0].[bgType] AS [BgType], [t0].[value] AS [Value], [t0].[seq] AS [Seq], [t0].[createtime] AS [Createtime], [t0].[createuser] AS [Createuser]\r\n"
//						+ "FROM [OFI_InvestorXBG] AS [t0] "
//						+ " WHERE [t0].[InvestorSeq] in ( "+ea.getInvestorSeq()+")");
//	
//				rs = stmt.executeQuery();
//				while(rs.next()){
//					OFIInvestorXBG bean= new OFIInvestorXBG();
//				
//					bean.setBgType(rs.getString("BgType"));
//					
//					bean.setSeq(rs.getInt("seq"));
//					bean.setValue(rs.getString("Value"));
//					bean.setInvestorSeq(rs.getString("InvestorSeq"));
//					ea.getXBG().add(bean);
//				}
//				rs.close();
//				stmt.close();
//			}
//			System.out.println("處理 OFI_InvestorXRelated");
//			for(InterviewoneYear ea : result) {
//				if(ea.getInvestorSeq().isEmpty()) {
//					continue;
//				}
//				stmt = sqltool.prepare("SELECT [t0].[serno] AS [Serno], [t0].[investorSeq] AS [InvestorSeq], [t0].[relatedname] AS [Relatedname], [t0].[nation] AS [Nation], [t0].[cnCode] AS [CnCode], [t0].[updatetime] AS [Updatetime], [t0].[updateuser] AS [Updateuser], [t0].[createtime] AS [Createtime], [t0].[createuser] AS [Createuser]\r\n"
//						+ "FROM [OFI_InvestorXRelated] AS [t0] "
//						+ " WHERE [t0].[InvestorSeq] in ( "+ea.getInvestorSeq()+")");
//				
//			
//				rs = stmt.executeQuery();
//				while(rs.next()){
//					OFIInvestorXRelated bean= new OFIInvestorXRelated();
//				
//					bean.setSerno(rs.getString("Serno"));
//					
//					bean.setInvestorSeq(rs.getString("InvestorSeq"));
//					bean.setRelatedname(rs.getString("Relatedname"));
//					bean.setNation(rs.getString("Nation"));
//					bean.setCnCode(rs.getString("CnCode"));
//					ea.getRelated().add(bean);
//				}
//				rs.close();
//				stmt.close();
//			}
//			
//			System.out.println("執行結束");
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			try{
//				sqltool.close();
//			}catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//		return result;
//	}
//	
	
	public List<Interviewone> select(String year,String qNo){
		List<Interviewone> result = new ArrayList<Interviewone>();
		SQL sqltool = new SQL();
		
		try {
			HashMap<Integer, String> code_map = new HashMap<Integer, String>(); 
			PreparedStatement stmt = sqltool.prepare("SELECT * FROM InterviewoneContent WHERE optionId = 109 ");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				code_map.put(rs.getInt("qNo"), rs.getString("value"));
			}
			rs.close();
			stmt.close();

			stmt = sqltool.prepare(
				"SELECT * FROM Interviewone WHERE qNo = isnull(?, qNo) AND year = isnull(?, year) AND enable='1' ORDER BY qNo "
			);
			stmt.setString(1, qNo);
			stmt.setString(2, year);
			
			rs = stmt.executeQuery();
			while(rs.next()){
				Interviewone bean= new Interviewone();
				bean.setqNo(rs.getInt("qNo"));
				bean.setInvestNo(rs.getString("investNo"));
				bean.setReInvestNo(rs.getString("reInvestNo"));
				bean.setYear(rs.getString("year"));
				bean.setInterviewStatus(rs.getString("interviewStatus"));
				bean.setSurveyStatus(rs.getString("surveyStatus"));
				bean.setEnable(rs.getString("enable"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setMsg(rs.getString("msg"));
				
				bean.setBusinessIncomeTaxCode(dTools.trim(code_map.get(bean.getqNo())));
				
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
	
	/*public List<Interviewone> selectByInvestNo(String investNo){
		List<Interviewone> result = new ArrayList<Interviewone>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM Interviewone WHERE investNo = ? and enable='1' ORDER BY year desc";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Interviewone bean= new Interviewone();
				bean.setqNo(rs.getInt("qNo"));
				bean.setInvestNo(rs.getString("investNo"));
				bean.setReInvestNo(rs.getString("reInvestNo"));
				bean.setYear(rs.getString("year"));
				bean.setInterviewStatus(rs.getString("interviewStatus"));
				bean.setSurveyStatus(rs.getString("surveyStatus"));
				bean.setEnable(rs.getString("enable"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setMsg(rs.getString("msg"));
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
	}*/
	public Map<String,List<Interviewone>> selectByInvestNo(String investNo){
		Map<String,List<Interviewone>> result = new TreeMap<String,List<Interviewone>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from Interviewone where enable='1' and investNo=? and reInvestNo=0 union all ");
		sb.append("select * from Interviewone where enable='1' and reInvestNo in (");
		sb.append("select reInvestNo from OFI_ReInvestXInvestNo where enable='1' and investNo=?)");
		sb.append("order by reInvestNo, year desc");
		
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				String key=rs.getString("reInvestNo");
				Interviewone bean= new Interviewone();
				bean.setqNo(rs.getInt("qNo"));
				bean.setInvestNo(rs.getString("investNo"));
				bean.setReInvestNo(rs.getString("reInvestNo"));
				bean.setYear(rs.getString("year"));
				bean.setInterviewStatus(rs.getString("interviewStatus"));
				bean.setSurveyStatus(rs.getString("surveyStatus"));
				bean.setEnable(rs.getString("enable"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setMsg(rs.getString("msg"));
				List<Interviewone> tmp;
				if(result.containsKey(key)){
					tmp=result.get(key);
				}else{
					tmp=new ArrayList<Interviewone>();
				}
				tmp.add(bean);
				result.put(key,tmp);
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
	/* 106-11-29新增下載檔案數量欄位 */
	public Map<String,List<Interviewone>> selectByInvestNo2(String investNo){
		Map<String,List<Interviewone>> result = new TreeMap<String,List<Interviewone>>();
		SQL sqltool = new SQL();
		try {
			PreparedStatement stmt = sqltool.prepare(
					"SELECT A.*, B.fileCount FROM "
				  + "	( "
				  + "	SELECT * from Interviewone "
				  + "	where enable='1' and investNo = ? and reInvestNo = 0 "
				  + "	union all "
				  + "	SELECT * from Interviewone "
				  + "	where enable='1' and reInvestNo in ("
				  + "										SELECT reInvestNo from OFI_ReInvestXInvestNo "
				  + "										where enable='1' and investNo = ? ) "
				  + "	) A "
				  + "LEFT OUTER JOIN "
				  + "	( "
				  + "	select * from VW_InterviewoneXFile "
				  + "	where investNo = isnull(? , investNo) "
				  + "	) B "
				  + "ON A.investNo=B.investNo and A.reInvestNo=B.reInvestNo and A.year=B.year "
				  + "order by A.reInvestNo, A.year desc"
				  );
			stmt.setString(1, investNo);
			stmt.setString(2, investNo);
			stmt.setString(3, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				String key=rs.getString("reInvestNo");
				Interviewone bean= new Interviewone();
				bean.setqNo(rs.getInt("qNo"));
				bean.setInvestNo(rs.getString("investNo"));
				bean.setReInvestNo(rs.getString("reInvestNo"));
				bean.setYear(rs.getString("year"));
				bean.setInterviewStatus(rs.getString("interviewStatus"));
				bean.setSurveyStatus(rs.getString("surveyStatus"));
				bean.setEnable(rs.getString("enable"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setMsg(rs.getString("msg"));
				bean.setFileCount(rs.getInt("fileCount"));
				List<Interviewone> tmp;
				if(result.containsKey(key)){
					tmp=result.get(key);
				}else{
					tmp=new ArrayList<Interviewone>();
				}
				tmp.add(bean);
				result.put(key,tmp);
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
	public List<Interviewone> selectByQNo(String qNo,String reInvestNo){
		List<Interviewone> result = new ArrayList<Interviewone>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from Interviewone where enable=1 and ");
		if(reInvestNo.equals("0")){
			sb.append("(investNo in (select investNo from Interviewone where qNo=?) and reInvestNo=0)");
		}else{
			sb.append("(reInvestNo in (select reInvestNo from Interviewone where qNo=?) and reInvestNo!=0)");
		}
		sb.append(" ORDER BY year desc");
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, qNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Interviewone bean= new Interviewone();
				bean.setqNo(rs.getInt("qNo"));
				bean.setInvestNo(rs.getString("investNo"));
				bean.setReInvestNo(rs.getString("reInvestNo"));
				bean.setYear(rs.getString("year"));
				bean.setInterviewStatus(rs.getString("interviewStatus"));
				bean.setSurveyStatus(rs.getString("surveyStatus"));
				bean.setEnable(rs.getString("enable"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setMsg(rs.getString("msg"));
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
	public Interviewone selectByQNo(String qNo){
		Interviewone bean= new Interviewone();
		SQL sqltool = new SQL();
		String forStmt = "select * from Interviewone where enable=1 and qNo=? ";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, qNo);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean.setqNo(rs.getInt("qNo"));
				bean.setInvestNo(rs.getString("investNo"));
				bean.setReInvestNo(rs.getString("reInvestNo"));
				bean.setYear(rs.getString("year"));
				bean.setInterviewStatus(rs.getString("interviewStatus"));
				bean.setSurveyStatus(rs.getString("surveyStatus"));
				bean.setEnable(rs.getString("enable"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setMsg(rs.getString("msg"));
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
	public int insert(Interviewone bean) {
		int result = 0;
		String forpstmt = "insert into (investNo,year,interviewStatus,surveyStatus,enable,updatetime,updateuser,createtime,createuser) Interviewone Output Inserted.qNo values(?,?,?,?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getInvestNo());
			pstmt.setString(2, bean.getYear());
			pstmt.setString(3, bean.getInterviewStatus());
			pstmt.setString(4, bean.getSurveyStatus());
			pstmt.setString(5, bean.getEnable());
			pstmt.setTimestamp(6, bean.getUpdatetime());
			pstmt.setString(7, bean.getUpdateuser());
			pstmt.setTimestamp(8, bean.getCreatetime());
			pstmt.setString(9, bean.getCreateuser());
			pstmt.setString(10, bean.getMsg());
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
	public void insert(List<Interviewone> beans) {
		String forpstmt = "insert into Interviewone values(?,?,?,?,?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			sqltool.noCommit();
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				int count=1;
				Interviewone bean = beans.get(i);
				pstmt.setString(count++, bean.getInvestNo());
				pstmt.setString(count++, bean.getReInvestNo());
				pstmt.setString(count++, bean.getYear());
				pstmt.setString(count++, bean.getInterviewStatus());
				pstmt.setString(count++, bean.getSurveyStatus());
				pstmt.setString(count++, bean.getEnable());
				pstmt.setTimestamp(count++, bean.getUpdatetime());
				pstmt.setString(count++, bean.getUpdateuser());
				pstmt.setTimestamp(count++, bean.getCreatetime());
				pstmt.setString(count++, bean.getCreateuser());
				pstmt.setString(count++, bean.getMsg());
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
	public int updateMsg(String msg,String updateuser,String qNo) {
		int result = 0;
		String forpstmt = "UPDATE Interviewone SET msg=?,updatetime=getDate(),updateuser=? WHERE qNo=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, msg);
			pstmt.setString(2,updateuser);
			pstmt.setString(3, qNo);
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
	public int update(Interviewone bean) {
		int result = 0;
		String forpstmt = "UPDATE [Interviewone] SET InterviewStatus=?,SurveyStatus=?,updatetime=?,updateuser=? WHERE qNo=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getInterviewStatus());
			pstmt.setString(2, bean.getSurveyStatus());
			pstmt.setTimestamp(3, bean.getUpdatetime());
			pstmt.setString(4, bean.getUpdateuser());
			pstmt.setInt(5, bean.getqNo());
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
	public int unable(String qNo,String updateuser) {
		int result = 0;
		String forpstmt = "UPDATE [Interviewone] SET enable='0',updatetime=getdate(),updateuser=? WHERE qNo=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, updateuser);
			pstmt.setString(2, qNo);
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
	/*轉投資事業*/
	public List<String> getReInvestNoByYear(String year){
		List<String> result = new ArrayList<String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT reInvestNo FROM dbo.ReInvestNoXBaseInfo (?) ");
		sb.append("where reInvestNo not in (select reInvestNo from Interviewone where enable='1' and year=?) ");
		sb.append("and reInvestNo in (SELECT reInvestNo FROM OFI_ReInvestXInvestNo where enable='1' and investNo in (");
		sb.append("SELECT investNo FROM Interviewone where enable='1' and year=?))");
/*		sb.append("SELECT a.reInvestNo FROM OFI_ReInvestmentList a,");
		sb.append("(SELECT * FROM dbo.ReInvestNoXInvestNos(?)) b where enable='1' and a.reInvestNo=b.reInvestNo ");
		sb.append("and a.reInvestNo not in (select reInvestNo from Interviewone where enable='1' and year=?) ");
		sb.append("and a.reInvestNo in (SELECT reInvestNo FROM OFI_ReInvestXInvestNo where enable='1' and investNo in (");
		sb.append("SELECT investNo FROM Interviewone where enable='1' and year=?))");
*/		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, year);
			stmt.setString(2, year);
			stmt.setString(3, year);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString("reInvestNo"));
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
	public Map<String,Map<String,String>> getReInvestNoBaseInfo(String year){
		Map<String,Map<String,String>> result = new HashMap<String,Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		String forStmt = "SELECT * FROM dbo.ReInvestNoXBaseInfo (?)";
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, year);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				Map<String,String> bean= new HashMap<String,String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					bean.put(meta.getColumnName(i), rs.getString(i));
				}
				result.put(rs.getString("reInvestNo"), bean);
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
	public Map<String,String> getReInvestNoByYearInvestNo(String year,String investNo){
		Map<String,String> result = new HashMap<String,String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT reInvestNo,cname FROM dbo.ReInvestNoXBaseInfo (?) ");
		sb.append("where reInvestNo in (select reInvestNo from OFI_ReInvestXInvestNo where enable='1' and investNo=?) ");
		sb.append("and reInvestNo in (SELECT distinct reInvestNo from Interviewone where year=? and enable='1')");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		 sb.setLength(0);
		 try {
			 PreparedStatement stmt = sqltool.prepare(forStmt);
			 stmt.setString(1, year);
			 stmt.setString(2, investNo);
			 stmt.setString(3, year);
			 ResultSet rs = stmt.executeQuery();
			
			 while(rs.next()){
				
				 result.put(rs.getString("reInvestNo"),rs.getString("cname").replace("(轉投資)", ""));
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
	public Map<String,String> getReInvestNoBaseInfo(String year,String reInvestNo){
		Map<String,String> result = new HashMap<String,String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		String forStmt = "SELECT * FROM [dbo].[ReInvestNoXBaseInfo] (?) where reInvestNo=?";
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, year);
			stmt.setString(2, reInvestNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					 result.put(meta.getColumnName(i), rs.getString(i));
				}
//				result.put(rs.getString("reInvestNo"),rs.getString("cname"));
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

//	public List<Interviewone> select(String syear,String eyear,String survey,String interview,String investName,String investNo,String IDNO){
//	List<Interviewone> result = new ArrayList<Interviewone>();
//	SQL sqltool = new SQL();
//	StringBuilder sb = new StringBuilder();
//	sb.append("SELECT * FROM Interviewone WHERE year>=? and year<=? and enable='1' ");
//	sb.append("and surveyStatus=isnull(?,surveyStatus) and interviewStatus =isnull(?,interviewStatus) ");
//	sb.append("and investNo in ( SELECT MAX(OFITB102.INVESTMENT_NO) AS investNo ");
//	sb.append("FROM moeaic.dbo.OFITB102,  moeaic.dbo.OFITB201 left join  moeaic.dbo.OFITB202 ");
//	sb.append("on OFITB201.COMP_CHTNAME=OFITB202.COMP_CHTNAME ");
//	sb.append("where (OFITB102.INVESTMENT_NO like '4%' or OFITB102.INVESTMENT_NO like '5%') ");
//	sb.append("and OFITB102.INVESTMENT_NO=OFITB201.INVESTMENT_NO ");
//	if(!investName.equals("%")){
//		sb.append("and OFITB201.COMP_CHTNAME like '").append(investName).append("' ");
//	}
//	if(!investNo.equals("%")){
//		sb.append("and  OFITB102.INVESTMENT_NO like '").append(investNo).append("' ");
//	}
//	if(!IDNO.equals("%")){
//		sb.append("and OFITB202.BAN_NO like '").append(IDNO).append("' ");
//	}
//	sb.append("group by OFITB102.[INVESTMENT_NO],OFITB201.COMP_CHTNAME,OFITB202.BAN_NO )");
//	String forStmt = sb.toString();
//	sb.setLength(0);
//	try {
//		PreparedStatement stmt = sqltool.prepare(forStmt);
//		stmt.setString(1, syear);
//		stmt.setString(2, eyear);
//		stmt.setString(3, survey);
//		stmt.setString(4, interview);
//		ResultSet rs = stmt.executeQuery();
//		while(rs.next()){
//			Interviewone bean= new Interviewone();
//			bean.setqNo(rs.getInt("qNo"));
//			bean.setInvestNo(DataUtil.nulltoempty(rs.getString("investNo")));
//			bean.setReInvestNo(rs.getString("reInvestNo"));
//			bean.setYear(rs.getString("year"));
//			bean.setInterviewStatus(rs.getString("interviewStatus"));
//			bean.setSurveyStatus(rs.getString("surveyStatus"));
//			bean.setEnable(rs.getString("enable"));
//			bean.setUpdatetime(rs.getTimestamp("updatetime"));
//			bean.setUpdateuser(rs.getString("updateuser"));
//			bean.setCreatetime(rs.getTimestamp("createtime"));
//			bean.setCreateuser(rs.getString("createuser"));
//			bean.setMsg(rs.getString("msg"));
//			result.add(bean);
//		}
//	} catch (Exception e) {
//		e.printStackTrace();
//	}finally{
//		try{
//			sqltool.close();
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	return result;
//}
//	public List<Interviewone> select(String year,String survey,String interview,String investName,String investNo,String IDNO){
//		List<Interviewone> result = new ArrayList<Interviewone>();
//		SQL sqltool = new SQL();
//		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT * FROM Interviewone WHERE year=? and enable='1' and surveyStatus=isnull(?,surveyStatus) ");
//		sb.append("and interviewStatus =isnull(?,interviewStatus) and investNo in (select investNo from ( ");
//		sb.append("select * from (SELECT MAX(OFITB102.INVESTMENT_NO) AS investNo,OFITB201.COMP_CHTNAME cname,isnull(OFITB202.BAN_NO,'') idno ");
//		sb.append("FROM moeaic.dbo.OFITB102,  moeaic.dbo.OFITB201 left join  moeaic.dbo.OFITB202 ");
//		sb.append("on OFITB201.COMP_CHTNAME=OFITB202.COMP_CHTNAME where (OFITB102.INVESTMENT_NO like '4%' or OFITB102.INVESTMENT_NO like '5%') ");
//		sb.append("and OFITB102.INVESTMENT_NO=OFITB201.INVESTMENT_NO group by OFITB102.[INVESTMENT_NO],OFITB201.COMP_CHTNAME,OFITB202.BAN_NO ");
//		sb.append(")a)x where x.investNo like '").append(investNo).append("' and x.cname like '");
//		sb.append(investName).append("' and x.idno like '").append(IDNO).append("' )");
//		String forStmt = sb.toString();
//		sb.setLength(0);
//		try {
//			PreparedStatement stmt = sqltool.prepare(forStmt);
//			stmt.setString(1, year);
//			stmt.setString(2, survey);
//			stmt.setString(3, interview);
//			ResultSet rs = stmt.executeQuery();
//			while(rs.next()){
//				Interviewone bean= new Interviewone();
//				bean.setqNo(rs.getInt("qNo"));
//				bean.setInvestNo(rs.getString("investNo"));
//				bean.setReInvestNo(rs.getString("reInvestNo"));
//				bean.setYear(rs.getString("year"));
//				bean.setInterviewStatus(rs.getString("interviewStatus"));
//				bean.setSurveyStatus(rs.getString("surveyStatus"));
//				bean.setEnable(rs.getString("enable"));
//				bean.setUpdatetime(rs.getTimestamp("updatetime"));
//				bean.setUpdateuser(rs.getString("updateuser"));
//				bean.setCreatetime(rs.getTimestamp("createtime"));
//				bean.setCreateuser(rs.getString("createuser"));
//				bean.setMsg(rs.getString("msg"));
//				result.add(bean);
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
}
