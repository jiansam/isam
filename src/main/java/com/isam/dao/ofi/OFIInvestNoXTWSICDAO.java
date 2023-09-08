package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestNoXTWSIC;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class OFIInvestNoXTWSICDAO {
	public Map<String,List<String>> select(String investNo){
		Map<String,List<String>> result = new HashMap<String,List<String>>();
		SQL sqltool = new SQL();
		String forStmt ="SELECT type,item FROM OFI_InvestNoXTWSIC where investNo=? order by type,item";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				List<String> sub;
				String key =rs.getString("type");
				if(result.containsKey(key)){
					sub=result.get(key);
				}else{
					sub= new ArrayList<String>();
				}
				sub.add(rs.getString("item"));
				result.put(key,sub);
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
	public Map<String,List<String>> selectFrontFmt(String investNo){
		Map<String,List<String>> result = new HashMap<String,List<String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select a.type,b.code+'-'+b.codename item from (SELECT distinct type,");
		sb.append("case when type<>0 then substring(item,0,5) else item end item FROM OFI_InvestNoXTWSIC where investNo=? )a,");
		sb.append("(select code,case when isnull(NODE,'')='' then codename else codename+'（'+node+'）' end codename from OFI_TWSIC a ");
		sb.append("left join (select OPER_ITEM_CODE,node from moeaic.dbo.OFITB920) b on a.code=b.OPER_ITEM_CODE )b ");
		sb.append("where a.item=b.code");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				List<String> sub;
				String key =rs.getString("type");
				if(result.containsKey(key)){
					sub=result.get(key);
				}else{
					sub= new ArrayList<String>();
				}
				sub.add(rs.getString("item"));
				result.put(key,sub);
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
	public Map<String,String> getTWSICMap(){
		SQL sqltool = new SQL();
		/*StringBuilder sb = new StringBuilder();
		sb.append("SELECT code,codename FROM (select code,case when isnull(NODE,'')='' then codename ");
		sb.append("else codename+'（'+node+'）' end codename  from OFI_TWSIC a left join (select ");
		sb.append("OPER_ITEM_CODE,node from moeaic.dbo.OFITB920) b on a.code=b.OPER_ITEM_CODE where ");
		sb.append("LEN(code)>4 and  level='2' and enable='1')x");*/
		String forStmt = "select * from dbo.OFI_TWSICXName()";
		Map<String,String> result = new HashMap<String, String>();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.put(rs.getString("code"),rs.getString("codename"));
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
	public List<Map<String,String>> getTWSICList(String investNo){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select code,case when isnull(NODE,'')='' then codename else codename+'（'+node+'）' end codename,isSP,'1' isSysData ");
		sb.append("from OFI_TWSIC a left join (select OPER_ITEM_CODE,node from moeaic.dbo.OFITB920) b on a.code=b.OPER_ITEM_CODE ");
		sb.append("where a.level='2' and enable='1' and code in (SELECT OPER_ITEM_CODE FROM [moeaic].[dbo].[OFITB203] ");
		sb.append("where OPER_ITEM_CODE!='' and COMP_CHTNAME in(SELECT distinct [COMP_CHTNAME] FROM OFI_InvestCase where investNo=?)) ");
		sb.append("union all select code,case when isnull(NODE,'')='' then codename else codename+'（'+node+'）' end codename,isSP");
		sb.append(",'0' isSysData from OFI_TWSIC a left join (select OPER_ITEM_CODE,node from moeaic.dbo.OFITB920) b ");
		sb.append("on a.code=b.OPER_ITEM_CODE where a.level='2' and enable='1' and  code not in (");
		sb.append("SELECT OPER_ITEM_CODE FROM [moeaic].[dbo].[OFITB203] where OPER_ITEM_CODE!='' ");
		sb.append("and COMP_CHTNAME in(SELECT distinct COMP_CHTNAME FROM OFI_InvestCase where investNo=?)) ");
		sb.append("order by isSysData desc");
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
					sub.put(meta.getColumnName(i),DataUtil.trim(rs.getString(i)));
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
	public void delete(String investNo){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM OFI_InvestNoXTWSIC WHERE investNo = ?";
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
	}
	public void insert(List<OFIInvestNoXTWSIC> beans) {
		String forpstmt = "insert into OFI_InvestNoXTWSIC values(?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				OFIInvestNoXTWSIC bean= beans.get(i);
				pstmt.setString(1, bean.getInvestNo());
				pstmt.setString(2, bean.getItem());
				pstmt.setString(3, bean.getType());
				pstmt.setInt(4, bean.getSeq());
				pstmt.setTimestamp(5, bean.getUpdatetime());
				pstmt.setString(6, bean.getUpdateuser());
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
}
