package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class OFIInvestCaseXAuditDAO_bak {
	
	public Map<String,Map<String,String>> select(String investorSeq){
		Map<String,Map<String,String>>  result= new HashMap<String,Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (SELECT [caseNo],[auditCode],(STUFF((SELECT ',' + value ");
		sb.append("FROM OFI_InvestCaseXAudit T2 WHERE  T2.[caseNo] = T1.[caseNo] and T2.auditCode=T1.auditCode ");
		sb.append("FOR XML PATH('')), 1, 1, '')) AS value FROM OFI_InvestCaseXAudit T1 GROUP BY  [caseNo],[auditCode]) a ");
		sb.append("where caseNo in (select caseNo from OFI_InvestCaseList where enable='1' and investorSeq=?) ");
		sb.append("order by caseNo,auditCode");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investorSeq);
			ResultSet rs = stmt.executeQuery();
			Map<String,String> sub;
			while(rs.next()){
				String key1=rs.getString("caseNo");
				String key2=rs.getString("auditCode");
				String value=DataUtil.nulltoempty(rs.getString("value"));
				if(result.containsKey(key1)){
					sub= result.get(key1);
				}else{
					sub=new HashMap<String,String>();
				}
				sub.put(key2,value);
				result.put(key1, sub);
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
	public String getSPNeed(String investNo){
		String result="";
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT value FROM OFI_InvestCaseXAudit where auditCode='0603' and caseNo in (");
		sb.append("select caseNo from OFI_InvestCaseList where enable='1' and investNo=?)");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				if(sb.length()>0){
					sb.append("；");
				}
				sb.append(DataUtil.nulltoempty(rs.getString("value")));
			}
			result=sb.toString();
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
}
