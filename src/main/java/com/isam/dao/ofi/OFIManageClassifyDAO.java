package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class OFIManageClassifyDAO {
	
	public  Map<String,Map<String,String>> select(String investNo){
		 Map<String,Map<String,String>> result = new LinkedHashMap<String, Map<String,String>>();
		SQL sqltool = new SQL();
		String forStmt ="SELECT * FROM OFI_ManageClassify where investNo=? order by year desc";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			Map<String,String> sub;
			while(rs.next()){
				String k1=rs.getString("year");
				if(result.containsKey(k1)){
					sub =result.get(k1);
				}else{
					sub = new LinkedHashMap<String, String>();
				}
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.put(meta.getColumnName(i),DataUtil.nulltoempty(rs.getString(i)));
				}
				result.put(k1, sub);
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
	public int update(String year,String investNo,String nclassify,String updateuser) {
		String forpstmt = "update OFI_ManageClassify set nclassify=?,updateuser=?,updatetime=getdate() where year=? and investNo=?";
		SQL sqltool = new SQL();
		int result=-1;
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1,nclassify);
			pstmt.setString(2,updateuser);
			pstmt.setString(3,year);
			pstmt.setString(4,investNo);
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
	public List<List<String>> getMSReport(){
		List<List<String>> result = new ArrayList<List<String>>();
		SQL sqltool = new SQL();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT m.[investNo] '陸資案號',b.COMP_CHTNAME '事業名稱',m.[year] '年度',b.isFilled '資料狀態',b.active '執行情形' ");
		sb.append(",isOperated '經營狀況',sdate '停業起日',edate '停業迄日',m.[score] '管理密度分數',isnull([nclassify],[oclassify]) '管理密度等級' ");
		sb.append(",isnull(interviewStatus,'') '繳交訪視紀錄表' ,isnull(surveyStatus,'') '繳交問卷資料'");
		sb.append(",isNull(I+rI,'') '稽核6-訪視異常',isNull(S+rS,'') '稽核6-財務異常',sp.value '稽核6-特殊需要' ");
		sb.append(",b.OrgTypeName '組織型態',b.BAN_NO '統一編號',b.POST_NO '地址區碼',b.addr '事業地址',b.TEL_NO '電話' ");
//		sb.append(",msg '異常企業評語',b.OrgTypeName '組織型態',b.BAN_NO '統一編號',b.POST_NO '地址區碼',b.addr '事業地址',b.TEL_NO '電話' ");
		sb.append(",agent.contact '聯絡人',agent.agent '代理人',agent.tel '代理人電話',left(b.setupdate,3) '設立年',case when b.setupdate<>'' ");
		sb.append("then SUBSTRING(b.setupdate,4, 2)+'/'+right(b.setupdate,2) else '' end '設立月日' FROM OFI_ManageClassify m left join (");
		sb.append("select i.investNo,i.year,i.interviewStatus,i.surveyStatus,case when ei.qNo is null then '國內事業：否；' else '國內事業：是；' end I ");
		sb.append(",case when fi.qNo is null then '國內事業：否；' else '國內事業：是；' end S");
		sb.append(",case when r.rEqNo=0 then '轉投資：否；' when r.rEqNo is null then '' else '轉投資：是；' end rI");
		sb.append(",case when r.rFqNo=0 then '轉投資：否；' when r.rFqNo is null then '' else '轉投資：是；' end rS from Interviewone i ");
		sb.append("left join(SELECT * FROM OFI_InterviewErrorList('')) ei on ei.qNo=i.qNo ");
		sb.append("left join(SELECT * FROM OFI_FinancialErrorList('')) fi on fi.qNo=i.qNo ");
		sb.append("left join(select r.investNo,i.year,isnull(max(ei.qNo),0) rEqNo,isnull(max(fi.qNo),0) rFqNo from Interviewone i left join ");
		sb.append("(select reInvestNo,investNo from isam.dbo.OFI_ReInvestXInvestNo where enable='1')r on i.reInvestNo=r.reInvestNo ");
		sb.append("left join(SELECT * FROM OFI_InterviewErrorList('')) ei on ei.qNo=i.qNo left join(SELECT * FROM OFI_FinancialErrorList('')) fi ");
		sb.append("on fi.qNo=i.qNo where i.enable='1' and i.reInvestNo!=0 group by r.investNo,i.year)r on r.year=i.year and r.investNo=i.investNo ");
		sb.append("where i.enable='1' and i.reInvestNo=0");
		sb.append(") i on m.investNo=i.investNo and m.year=i.year ");
		sb.append("left join (SELECT a.[investNo],a.setupdate,b.COMP_CHTNAME,b.BAN_NO,b.POST_NO,b.OrgTypeName,b.COUNTY_NAME+b.TOWN_NAME+b.ADDRESS addr ");
		sb.append(",b.CHARGE_PERSON,b.TEL_NO,a.[active],a.isFilled,a.isOperated,sdate,edate FROM OFI_InvestList a,(SELECT * FROM [moeaic].[dbo].[OFI_BASEDATA] (null)) b ");
		sb.append("where a.investNo=b.INVESTMENT_NO and a.enable='1') b on m.[investNo]=b.investNo left join (SELECT * FROM [ISAM].[dbo].[OFI_getAgentContactStrs]())agent ");
		sb.append("on m.investNo=agent.investNo left join (SELECT value,investNo FROM OFI_InvestNoXAudit where auditCode='0603')sp on m.investNo=sp.investNo order by m.investNo");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			List<String> title = new ArrayList<String>();
			int count=0;
			while(rs.next()){
				List<String> sub = new ArrayList<String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					if(count==0){
						title.add(meta.getColumnName(i));
					}
					sub.add(DataUtil.nulltoempty(rs.getString(i)));
				}
				if(count==0){
					result.add(title);
				}
				result.add(sub);
				count++;
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
	/*public List<List<String>> getMSReport(){
		List<List<String>> result = new ArrayList<List<String>>();
		SQL sqltool = new SQL();
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT m.[investNo] '陸資案號',b.COMP_CHTNAME '事業名稱',m.[year] '年度',b.isFilled '資料狀態'");
		sb.append(",b.active '執行情形',isnull(interviewStatus,0) '訪視',isnull(surveyStatus,0) '問卷',m.[score] '管理密度分數'");
		sb.append(",isnull([nclassify],[oclassify]) '管理密度等級' FROM OFI_ManageClassify m ");
		sb.append("left join (select investNo,interviewStatus,surveyStatus,year from Interviewone where enable='1') i ");
		sb.append("on m.[investNo]=i.investNo and m.[year]=i.year left join (SELECT a.[investNo], b.COMP_CHTNAME,a.[active],a.isFilled ");
		sb.append("FROM OFI_InvestList a,(SELECT [investNo],[COMP_CHTNAME] FROM OFI_InvestCase group by  [investNo],[COMP_CHTNAME]) b ");
		sb.append("where a.investNo=b.investNo and a.enable='1') b on m.[investNo]=b.investNo");
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			List<String> title = new ArrayList<String>();
			int count=0;
			while(rs.next()){
				List<String> sub = new ArrayList<String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					if(count==0){
						title.add(meta.getColumnName(i));
					}
					sub.add(DataUtil.nulltoempty(rs.getString(i)));
				}
				if(count==0){
					result.add(title);
				}
				result.add(sub);
				count++;
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
	
}
