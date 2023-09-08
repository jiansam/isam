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

import com.isam.bean.OFIInvestNoXAudit;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class OFIInvestNoXAuditDAO {
	
	public Map<String,Map<String,String>> select(String investorSeq){
		Map<String,Map<String,String>>  result= new HashMap<String,Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (SELECT investNo,auditCode,(STUFF((SELECT ',' + value ");
		sb.append("FROM OFI_InvestNoXAudit T2 WHERE  T2.investNo = T1.investNo and T2.auditCode=T1.auditCode ");
		sb.append("FOR XML PATH('')), 1, 1, '')) AS value FROM OFI_InvestNoXAudit T1 GROUP BY  investNo,auditCode) a ");
		sb.append("where investNo in (select investNo from OFI_InvestCaseList where enable='1' and investorSeq=?) ");
		sb.append("order by investNo,auditCode");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investorSeq);
			ResultSet rs = stmt.executeQuery();
			Map<String,String> sub;
			while(rs.next()){
				String key1=rs.getString("investNo");
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
	public List<Map<String,String>> selectByInvestNo(String investNo){
		List<Map<String,String>>  result= new ArrayList<Map<String,String>>();
		SQL sqltool = new SQL();
		String forStmt = "select auditCode,value,seq from OFI_InvestNoXAudit where investNo=? order by seq,auditCode";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				Map<String,String> sub=new HashMap<String, String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					sub.put(meta.getColumnName(i), rs.getString(i));
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
	
	public Map<String,String> getAduitByInvestNo(String investNo){
		Map<String,String>  result= new HashMap<String,String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select auditCode,value from (SELECT investNo,auditCode,(STUFF((SELECT ',' + value ");
		sb.append("FROM OFI_InvestNoXAudit T2 WHERE  T2.investNo = T1.investNo and T2.auditCode=T1.auditCode ");
		sb.append("FOR XML PATH('')), 1, 1, '')) AS value FROM OFI_InvestNoXAudit T1 GROUP BY investNo,auditCode) a ");
		sb.append("where investNo=? ");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				String key=rs.getString("auditCode");
				String value=DataUtil.nulltoempty(rs.getString("value"));
				result.put(key,value);
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
	public Map<String,String> getAudit06(String investNo){
		Map<String,String> result = new HashMap<String, String>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT auditcode,value FROM OFI_GETALL_Audit() where investNo=? and auditcode in ('06','0601','0602','0603')";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.put(rs.getString("auditcode"), rs.getString("value"));
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
	public Map<String,String> getAuditAll(String investNo){
		Map<String,String> result = new HashMap<String, String>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT auditcode,value FROM OFI_GETALL_Audit() where investNo=?";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.put(rs.getString("auditcode"), rs.getString("value"));
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
	public String isEditable(String investNo){
		String result="0";
		SQL sqltool = new SQL();
		String forStmt = "select distinct createuser from OFI_InvestNoXAudit where investNo=?";
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				String user=DataUtil.nulltoempty(rs.getString("createuser"));
				if(!user.isEmpty()&&!user.equals("admin")){
					result="1";
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
	public String getSPNeed(String investNo){
		String result="";
		SQL sqltool = new SQL();
		String forStmt = "SELECT value FROM OFI_InvestNoXAudit where auditCode='0603' and investNo=?";
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				result=DataUtil.nulltoempty(rs.getString("value"));
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
	public void delete(String investNo){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM OFI_InvestNoXAudit WHERE investNo = ? and auditCode not like '02%' and auditCode not like '07%' ";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, investNo);
			pstmt.executeUpdate();
			forStmt = "DELETE FROM OFI_InvestNoXAudit WHERE investNo = ? and (auditCode ='02' OR auditCode ='07') ";
			pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, investNo);
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
	
/*	public void delete(String investNo){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM OFI_InvestNoXAudit WHERE investNo = ?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, investNo);
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
	}*/
	public void insert(List<OFIInvestNoXAudit> beans) {
		String forpstmt = "insert into OFI_InvestNoXAudit values(?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			sqltool.noCommit();
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				OFIInvestNoXAudit bean= beans.get(i);
				pstmt.setString(1, bean.getInvestNo());
				pstmt.setString(2, bean.getAuditCode());
				pstmt.setString(3, bean.getValue());
				pstmt.setInt(4, bean.getSeq());
				pstmt.setTimestamp(5, bean.getCreatetime());
				pstmt.setString(6, bean.getCreateuser());
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
	public Map<String,Map<String,String>> getAudit02Details(String investNo){
		Map<String,Map<String,String>> result= new LinkedHashMap<String, Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from(select auditCode,seq,value from OFI_InvestNoXAudit where investNo=? ");
		sb.append("and auditCode like '02%' and LEN(auditCode)>2  and value!='')a pivot (max(value) for auditcode in ");
		sb.append("([0201],[0202],[0203],[0204],[0205]))p order by seq");
		String forStmt = sb.toString();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				Map<String,String> sub=new LinkedHashMap<String, String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					sub.put(meta.getColumnName(i), DataUtil.nulltoempty(rs.getString(i)));
//					System.out.println(meta.getColumnName(i)+"="+DataUtil.nulltoempty(rs.getString(i)));
				}
				result.put(rs.getString("seq"),sub);
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
	public void deleteAudit02(String investNo,String seq){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM OFI_InvestNoXAudit WHERE investNo = ? and seq=? and auditCode like '02%' and LEN(auditCode)>2";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, investNo);
			pstmt.setString(2, seq);
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
	public void insertAudit02(List<OFIInvestNoXAudit> beans,String seqStr) {
		String forpstmt = "select MAX(seq) FROM OFI_InvestNoXAudit WHERE investNo=? and auditCode like '02%' and LEN(auditCode)>2 ";
		SQL sqltool = new SQL();
		try {
			sqltool.noCommit();
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			String investNo=beans.get(0).getInvestNo();
			int seq = 1;
			if(!seqStr.isEmpty()){
				seq=Integer.valueOf(seqStr);
				deleteAudit02(investNo, seqStr);
			}else{
				pstmt.setString(1, investNo);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()){
					seq=rs.getInt(1)+1;
				}
			}
			
			forpstmt = "insert into OFI_InvestNoXAudit values(?,?,?,?,?,?)";
			pstmt =sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				OFIInvestNoXAudit bean= beans.get(i);
				pstmt.setString(1, bean.getInvestNo());
				pstmt.setString(2, bean.getAuditCode());
				pstmt.setString(3, bean.getValue());
				pstmt.setInt(4, seq);
				pstmt.setTimestamp(5, bean.getCreatetime());
				pstmt.setString(6, bean.getCreateuser());
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
	public Map<String,Map<String,Map<String,String>>> getAudit02ByInvestorSeq(String investorSeq){
		Map<String,Map<String,Map<String,String>>> result=new HashMap<String, Map<String,Map<String,String>>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from(select auditCode,seq,value,investNo from OFI_InvestNoXAudit where ");
		sb.append("investNo in (select investNo from OFI_InvestCaseList where enable='1' and investorSeq=?) ");
		sb.append("and auditCode like '02%' and LEN(auditCode)>2  and value!='')a pivot (max(value) for auditcode in ");
		sb.append("([0201],[0202],[0203],[0204],[0205]))p order by investNo,seq ");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investorSeq);
			ResultSet rs = stmt.executeQuery();
			Map<String,Map<String,String>> sub;
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				String key1=rs.getString("investNo");
				String key2=rs.getString("seq");
				if(result.containsKey(key1)){
					sub= result.get(key1);
				}else{
					sub= new LinkedHashMap<String, Map<String,String>>();
				}
				Map<String,String> tmp=new HashMap<String, String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					tmp.put(meta.getColumnName(i), DataUtil.nulltoempty(rs.getString(i)));
//					System.out.println(meta.getColumnName(i)+"="+DataUtil.nulltoempty(rs.getString(i)));
				}
				sub.put(key2,tmp);
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
	
	/*public void insertAudit02(List<OFIInvestNoXAudit> beans) {
		String forpstmt = "select MAX(seq) FROM OFI_InvestNoXAudit WHERE investNo=? and auditCode like '02%' and LEN(auditCode)>2 ";
		SQL sqltool = new SQL();
		try {
			sqltool.noCommit();
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			String investNo=beans.get(0).getInvestNo();
			int seq = 1;
			pstmt.setString(1, investNo);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				seq=rs.getInt(1)+1;
			}
			
			forpstmt = "insert into OFI_InvestNoXAudit values(?,?,?,?,?,?)";
			sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				OFIInvestNoXAudit bean= beans.get(i);
				pstmt.setString(1, bean.getInvestNo());
				pstmt.setString(2, bean.getAuditCode());
				pstmt.setString(3, bean.getValue());
				pstmt.setInt(4, seq);
				pstmt.setTimestamp(5, bean.getCreatetime());
				pstmt.setString(6, bean.getCreateuser());
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
	}*/
	
	public Map<String,Map<String,String>> getAudit07Details(String investNo){
		Map<String,Map<String,String>> result= new LinkedHashMap<String, Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from(select auditCode,seq,value from OFI_InvestNoXAudit where investNo=? ");
		sb.append("and auditCode like '07%' and LEN(auditCode)>2 and value!='')a pivot (max(value) for auditcode in ");
		sb.append("([0701],[0702],[0703]))p order by seq");
		String forStmt = sb.toString();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				Map<String,String> sub=new LinkedHashMap<String, String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					sub.put(meta.getColumnName(i), DataUtil.nulltoempty(rs.getString(i)));
//					System.out.println(meta.getColumnName(i)+"="+DataUtil.nulltoempty(rs.getString(i)));
				}
				result.put(rs.getString("seq"),sub);
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
	
	public void insertAudit07(List<OFIInvestNoXAudit> beans,String seqStr) {
		String forpstmt = "select MAX(seq) FROM OFI_InvestNoXAudit WHERE investNo=? and auditCode like '07%' and LEN(auditCode)>2 ";
		SQL sqltool = new SQL();
		try {
			sqltool.noCommit();
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			String investNo=beans.get(0).getInvestNo();
			int seq = 1;
			if(!seqStr.isEmpty()){
				seq=Integer.valueOf(seqStr);
				deleteAudit07(investNo, seqStr);
			}else{
				pstmt.setString(1, investNo);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()){
					seq=rs.getInt(1)+1;
				}
			}
			
			forpstmt = "insert into OFI_InvestNoXAudit values(?,?,?,?,?,?)";
			pstmt =sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				OFIInvestNoXAudit bean= beans.get(i);
				pstmt.setString(1, bean.getInvestNo());
				pstmt.setString(2, bean.getAuditCode());
				pstmt.setString(3, bean.getValue());
				pstmt.setInt(4, seq);
				pstmt.setTimestamp(5, bean.getCreatetime());
				pstmt.setString(6, bean.getCreateuser());
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
	
	public void deleteAudit07(String investNo,String seq){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM OFI_InvestNoXAudit WHERE investNo = ? and seq=? and auditCode like '07%' and LEN(auditCode)>2";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, investNo);
			pstmt.setString(2, seq);
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
	
	public Map<String,Map<String,Map<String,String>>> getAudit07ByInvestorSeq(String investorSeq){
		Map<String,Map<String,Map<String,String>>> result=new HashMap<String, Map<String,Map<String,String>>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from(select auditCode,seq,value,investNo from OFI_InvestNoXAudit where ");
		sb.append("investNo in (select investNo from OFI_InvestCaseList where enable='1' and investorSeq=?) ");
		sb.append("and auditCode like '07%' and LEN(auditCode)>2  and value!='')a pivot (max(value) for auditcode in ");
		sb.append("([0701],[0702],[0703]))p order by investNo,seq ");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investorSeq);
			ResultSet rs = stmt.executeQuery();
			Map<String,Map<String,String>> sub;
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				String key1=rs.getString("investNo");
				String key2=rs.getString("seq");
				if(result.containsKey(key1)){
					sub= result.get(key1);
				}else{
					sub= new LinkedHashMap<String, Map<String,String>>();
				}
				Map<String,String> tmp=new HashMap<String, String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					tmp.put(meta.getColumnName(i), DataUtil.nulltoempty(rs.getString(i)));
//					System.out.println(meta.getColumnName(i)+"="+DataUtil.nulltoempty(rs.getString(i)));
				}
				sub.put(key2,tmp);
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
}


