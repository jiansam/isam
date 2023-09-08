package com.isam.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.MoeaicData;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class MoeaicDataDAO {
	public List<String> getCNNameStrs(String IDNO){
		List<String> result = new ArrayList<String>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT INVEST_NO FROM [moeaic].[dbo].[ICMFN401] (?,'') group by INVEST_NO";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.add(rs.getString(1).trim());
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
	
	public List<MoeaicData> selectByInvestNoIDNO(String investNo,String IDNO){
		List<MoeaicData> result = new ArrayList<MoeaicData>();
		SQL sqltool = new SQL();
		try {
			PreparedStatement stmt = sqltool.prepare("SELECT * FROM [moeaic].[dbo].[getReceviceNo] (?,?) ");
			stmt.setString(1, IDNO);
			stmt.setString(2, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				MoeaicData bean = new MoeaicData();
				bean.setReceiveNo(rs.getString(1));
				bean.setRespDate(rs.getString(3));
				bean.setAppName(rs.getString(4).replaceAll("[\\r\\n\\f]", ""));
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
	
//	public List<List<String>> selectByInvestNoIDNONew(String investNo,String IDNO){
//		System.out.println("start:"+DataUtil.getStrUDate());
//		List<List<String>> result = new ArrayList<List<String>>();
//		Map<String,MoeaicData> map = new HashMap<String,MoeaicData>();
//		SQL sqltool = new SQL();
//		String forStmt = "SELECT RECEIVE_NO,RECEIVE_DATE,RESP_DATE,APPLICATION_NAME FROM [moeaic].[dbo].ICMFN405_03 (?,?) where INVEST_NO like '6%' group by RECEIVE_DATE,RESP_DATE,RECEIVE_NO,APPLICATION_NAME";
//		try {
//			PreparedStatement stmt = sqltool.prepare(forStmt);
//			stmt.setString(1, IDNO);
//			stmt.setString(2, investNo);
//			ResultSet rs = stmt.executeQuery();
//			while(rs.next()){
//				MoeaicData bean;
//				String reNO = rs.getString(1);
//				if(!map.containsKey(reNO)){
//					bean= new MoeaicData();
//					bean.setAppName(rs.getString(4).replace("\r\n", ""));
//					bean.setReceiveNo(rs.getString(1));
//					bean.setRespDate(rs.getString(3));
//				}else{
//					bean=map.get(reNO);
//					bean.setAppName(bean.getAppName()+"、"+rs.getString(4).replace("\r\n", ""));
//					if(bean.getRespDate()==null){
//						bean.setRespDate(rs.getString(3));
//					}
//				}
//				map.put(reNO, bean);
//			}
//			if(map.size()>0){
//				for (Entry<String, MoeaicData> m : map.entrySet()) {
//					MoeaicData s = m.getValue();
//					List<String> tmp = new  ArrayList<String>();
//					tmp.add(s.getRespDate()==null?"&nbsp;":s.getRespDate());
//					tmp.add(s.getReceiveNo()==null?"&nbsp;":s.getReceiveNo());
//					tmp.add(s.getAppName()==null?"&nbsp;":s.getAppName());
//					tmp.add("1");
//					result.add(tmp);
//				}
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
//		System.out.println("end:"+DataUtil.getStrUDate());
//		return result;
//	}
	public List<MoeaicData> selectByInvestNoIDNO(String investNo,String IDNO,int repserno){
		List<MoeaicData> result = new ArrayList<MoeaicData>();
		SQL sqltool = new SQL();
		String forStmt = "select * from [moeaic].[dbo].[getReceviceNo] (?,?) where RECEIVE_NO in (SELECT [receiveNo] FROM [ISAM].[dbo].[ProjectXReciveNo] where [repSerno]=?)";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			stmt.setString(2, investNo);
			stmt.setInt(3, repserno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				MoeaicData bean= new MoeaicData();
				bean.setReceiveNo(rs.getString(1));
				bean.setRespDate(rs.getString(3));
				bean.setAppName(rs.getString(4).replaceAll("[\\r\\n\\f]", ""));
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
	public List<MoeaicData> selectByInvestNo(String investNo,String IDNO){
		List<MoeaicData> result = new ArrayList<MoeaicData>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT distinct INVESTMENT_NO,COMP_CHTNAME,ID_NO,INVESTOR_CHTNAME,RECEIVE_NO,RESP_DATE,(");
		sb.append("STUFF((select distinct ','+[APPLICATION_NAME] FROM [moeaic].[dbo].[ICMFN405_03] (?,?) t2 ");
		sb.append("where t1.INVESTMENT_NO=t2.INVESTMENT_NO and t1.COMP_CHTNAME=t2.COMP_CHTNAME and t1.RECEIVE_NO=t2.RECEIVE_NO ");
		sb.append("and t1.ID_NO=t2.ID_NO and t1.RESP_DATE=t2.RESP_DATE	FOR XML PATH('')),1,1,''))AS [APPLICATION_NAME] ");
		sb.append("FROM [moeaic].[dbo].[ICMFN405_03] (?,?) t1 group by INVESTMENT_NO,COMP_CHTNAME,");
		sb.append("ID_NO,INVESTOR_CHTNAME,RECEIVE_NO,RESP_DATE");
		String forStmt = sb.toString();
//		System.out.println("forStmt:"+forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, IDNO);
			stmt.setString(2, investNo);
			stmt.setString(3, IDNO);
			stmt.setString(4, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				MoeaicData bean= new MoeaicData();
//				bean.setInvestNoStrs(rs.getString(1));
//				bean.setInvestNo(investNo);
//				bean.setCnName(rs.getString(2));
//				bean.setIDNO(rs.getString(3));
//				bean.setInvestor(rs.getString(4));
				bean.setReceiveNo(rs.getString(5));
				bean.setRespDate(rs.getString(6));
				bean.setAppName(rs.getString(7).replaceAll("[\\r\\n\\f]", ""));
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
	public List<MoeaicData> selectByInvestNo(String investNo){
		List<MoeaicData> result = new ArrayList<MoeaicData>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
//		sb.append("SELECT distinct INVESTMENT_NO,ID_NO,RECEIVE_NO,RESP_DATE,APPLICATION_NAME ");
		sb.append("SELECT distinct INVESTMENT_NO,ID_NO,RECEIVE_NO,RESP_DATE ");
		sb.append(" FROM CDataICMFN405 where CHARINDEX(?,INVESTMENT_NO)!=0 order by RESP_DATE desc");
		String forStmt = sb.toString();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				MoeaicData bean= new MoeaicData();
//				bean.setInvestNoStrs(rs.getString(1));
//				bean.setInvestNo(investNo);
//				bean.setIDNO(rs.getString(2));
				bean.setReceiveNo(rs.getString(3));
				bean.setRespDate(rs.getString(4));
//				bean.setAppName(rs.getString(5).replace("\r\n", ""));
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
	public List<String> selectIDNOByInvestNo(String investNo){
		List<String> result = new ArrayList<String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT distinct [ID_NO] FROM [ISAM].[dbo].[CDataICMFN405]");
		sb.append("where CHARINDEX(?,INVESTMENT_NO)!=0");
		String forStmt = sb.toString();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
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
	public List<String> selectSumMoney(String idno,String investNo){
		List<String> result = new ArrayList<String>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT SUM(MONEY1) as approvalMoney,SUM(money2) as approvedMoney FROM [moeaic].[dbo].[ICMFN405_03](?,?)";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, idno);
			stmt.setString(2, investNo);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result.add(rs.getString(1)==null?"":rs.getString(1));
				result.add(rs.getString(2)==null?"":rs.getString(2));
				result.add(DataUtil.formatString(String.valueOf((rs.getDouble(2)/rs.getDouble(1))*100))+"%");
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
	public List<List<String>> selectExcelSRC(String idno,String investNo){
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> colname = new ArrayList<String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT INVEST_NO '案號',COMP_CHTNAME '大陸事業名稱',CCOMP_CHTNAME '控股公司',INVESTOR_CHTNAME '投資人名稱'");
		sb.append(",ID_NO '統一編號',receive_no '申請文號',PREV_REC_NO '原核准文號',RECEIVE_DATE '申請日期',RESP_DATE '核准日期'");
		sb.append(",APPLICATION_NAME '案由',外幣,機器,原料,技術,商標,其他,盈餘,轉受讓,MONEY3 '額度',MONEY1 '投資金額',MONEY2 '審定合計'");
		sb.append(",case when UPD_FLAG='1' then '已修正' else '' end '修正註記',TOCORRECT '釐正註記'");
		sb.append("FROM [moeaic].[dbo].[ICMFN405] (?,?)");
		String forStmt = sb.toString();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, idno);
			stmt.setString(2, investNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			int flag=0;
			while(rs.next()){
				List<String> sublist = new ArrayList<String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					if(flag<meta.getColumnCount()){
						colname.add(meta.getColumnName(i));
						flag++;
					}
					sublist.add(rs.getString(i)==null?"":rs.getString(i));
				}
				result.add(sublist);
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
		if(result.size()>0){
			result.add(0,colname);
		}
		return result;
	}
	public List<List<String>> selectWebSRC(String idno,String investNo){
		List<List<String>> result = new ArrayList<List<String>>();
		List<String> colname = new ArrayList<String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT receive_no '申請文號',RECEIVE_DATE '申請日期',RESP_DATE '核准日期',APPLICATION_NAME '案由',PREV_REC_NO '原核准文號'");
		sb.append(",case when UPD_FLAG='1' then '已修正' else '' end '修正註記',TOCORRECT '釐正註記',CCOMP_CHTNAME '控股公司'");
		sb.append(",外幣 '現金',機器,原料,技術,商標,其他,轉受讓,盈餘,MONEY3 '額度',MONEY1 '投資金額',MONEY2 '審定合計'");
		sb.append("FROM [moeaic].[dbo].[ICMFN405] (?,?) order by RESP_DATE,RECEIVE_DATE");
		String forStmt = sb.toString();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, idno);
			stmt.setString(2, investNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			int flag=0;
			while(rs.next()){
				List<String> sublist = new ArrayList<String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					if(flag<meta.getColumnCount()){
						colname.add(meta.getColumnName(i));
						flag++;
					}
					sublist.add(rs.getString(i)==null?"":rs.getString(i));
				}
				result.add(sublist);
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
		if(result.size()>0){
			result.add(0,colname);
		}
		return result;
	}
	public Map<String,String> getCNSysBaseInfo(String idno,String investNo){
		Map<String,String> result = new HashMap<String, String>();
		SQL sqltool = new SQL("jdbc/sqlMoeaic");
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (SELECT(SELECT dbo.OFIFN116(OFITB201.COMP_CHTNAME)) AS investNoStr ");
		sb.append(",MAX(OFITB102.INVESTMENT_NO) AS investNo ");
		sb.append(",OFITB201.COMP_CHTNAME cname,isnull(OFITB202.BAN_NO,'') idno,OFITB202.TEL_NO tel");
		sb.append(",isnull(COMTB932.COUNTY_NAME,'')+isnull(COMTB933.TOWN_NAME,'')+isnull(OFITB202.[ADDRESS],'') addr ");
/*		sb.append(",isnull(COMTB932.COUNTY_NAME+COMTB933.TOWN_NAME+OFITB202.[ADDRESS],OFITB202.[ADDRESS]) addr ");*/
		sb.append("FROM OFITB102, OFITB201 left join OFITB202 on OFITB201.COMP_CHTNAME=OFITB202.COMP_CHTNAME ");
		sb.append("left join COMTB932 on OFITB202.COUNTY_NO=COMTB932.COUNTY_NO ");
		sb.append("left join COMTB933 on COMTB933.TOWN_NO=OFITB202.TOWN_NO ");
		sb.append("where (OFITB102.INVESTMENT_NO like '4%' or OFITB102.INVESTMENT_NO like '5%') ");
		sb.append("and OFITB102.INVESTMENT_NO=OFITB201.INVESTMENT_NO ");
		sb.append("group by OFITB102.[INVESTMENT_NO],OFITB201.COMP_CHTNAME,OFITB202.BAN_NO,OFITB202.TEL_NO, ");
		sb.append("COMTB932.COUNTY_NAME,COMTB933.TOWN_NAME,OFITB202.ADDRESS ");
		sb.append(")a where investNo=isnull(?,investNo) and idno=isnull(?,idno) order by investNo");
		String forStmt = sb.toString();
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, idno);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result.put("investNo", DataUtil.nulltoempty(rs.getString("investNo")));
				result.put("cname", DataUtil.nulltoempty(rs.getString("cname")));
				result.put("idno", DataUtil.nulltoempty(rs.getString("idno")));
				result.put("tel", DataUtil.nulltoempty(rs.getString("tel")));
				result.put("addr", DataUtil.nulltoempty(rs.getString("addr")));
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
	public Map<String,Map<String,String>> getCNSysBaseInfo(){
		Map<String,Map<String,String>> result = new HashMap<String, Map<String,String>>();
		SQL sqltool = new SQL("jdbc/sqlMoeaic");
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (SELECT(SELECT dbo.OFIFN116(OFITB201.COMP_CHTNAME)) AS investNoStr ");
		sb.append(",MAX(OFITB102.INVESTMENT_NO) AS investNo ");
		sb.append(",OFITB201.COMP_CHTNAME cname,isnull(OFITB202.BAN_NO,'') idno,OFITB202.TEL_NO tel");
		sb.append(",isnull(COMTB932.COUNTY_NAME,'')+isnull(COMTB933.TOWN_NAME,'')+isnull(OFITB202.[ADDRESS],'') addr ");
		//sb.append(",isnull(COMTB932.COUNTY_NAME+COMTB933.TOWN_NAME+OFITB202.[ADDRESS],OFITB202.[ADDRESS]) addr ");
		sb.append("FROM OFITB102, OFITB201 left join OFITB202 on OFITB201.COMP_CHTNAME=OFITB202.COMP_CHTNAME ");
		sb.append("left join COMTB932 on OFITB202.COUNTY_NO=COMTB932.COUNTY_NO ");
		sb.append("left join COMTB933 on COMTB933.TOWN_NO=OFITB202.TOWN_NO ");
		sb.append("where (OFITB102.INVESTMENT_NO like '4%' or OFITB102.INVESTMENT_NO like '5%') ");
		sb.append("and OFITB102.INVESTMENT_NO=OFITB201.INVESTMENT_NO ");
		sb.append("group by OFITB102.[INVESTMENT_NO],OFITB201.COMP_CHTNAME,OFITB202.BAN_NO,OFITB202.TEL_NO, ");
		sb.append("COMTB932.COUNTY_NAME,COMTB933.TOWN_NAME,OFITB202.ADDRESS ");
		sb.append(")a order by investNo");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Map<String,String> temp = new HashMap<String, String>();
				String investNo= rs.getString("investNo")==null?"":rs.getString("investNo").trim();
				temp.put("cname", rs.getString("cname")==null?"":rs.getString("cname").trim());
				temp.put("idno", rs.getString("idno")==null?"":rs.getString("idno").trim());
				temp.put("tel", rs.getString("tel")==null?"":rs.getString("tel").trim());
				temp.put("addr", rs.getString("addr")==null?"":rs.getString("addr").trim());
				result.put(investNo,temp);
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
}
