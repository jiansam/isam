package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.OFIReInvestXTWSIC;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class OFIReInvestXTWSICDAO {
	
	public Map<String,Map<String,List<String>>> getReInvestFrontTWSICs(String investNo){
		SQL sqltool = new SQL();
		Map<String,Map<String,List<String>>> result= new HashMap<String, Map<String,List<String>>>();
		StringBuilder sb = new StringBuilder();
		sb.append("select a.reInvestNo,a.type,b.code+'-'+b.codename item from ( ");
		sb.append("SELECT distinct reInvestNo,type,case when type<>0 then substring(item,0,5) else item end item ");
		sb.append("FROM OFI_ReInvestXTWSIC where reInvestNo in (select reInvestNo from OFI_ReInvestXInvestNo ");
		sb.append("where investNO=?))a,(select code,case when isnull(NODE,'')='' then codename ");
		sb.append("else codename+'（'+node+'）' end codename from OFI_TWSIC a left join (select OPER_ITEM_CODE,");
		sb.append("node from moeaic.dbo.OFITB920) b on a.code=b.OPER_ITEM_CODE) b where a.item=b.code");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			ResultSet rs = stmt.executeQuery();
			Map<String,List<String>> sub; 
			List<String> sublist;
			while(rs.next()){
				String key1=rs.getString("reInvestNo");
				String key2=rs.getString("type");
				String value=rs.getString("item");
				if(result.containsKey(key1)){
					sub=result.get(key1);
				}else{
					sub= new HashMap<String, List<String>>();
				}
				if(sub.containsKey(key2)){
					sublist=sub.get(key2);
				}else{
					sublist= new ArrayList<String>();
				}
				sublist.add(value);
				sub.put(key2, sublist);
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
	public List<Map<String,String>> getTWSICList(){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT code,codename,isSP FROM (select code,case when isnull(NODE,'')='' then codename ");
		sb.append("else codename+'（'+node+'）' end codename ,isSP from OFI_TWSIC a left join (select ");
		sb.append("OPER_ITEM_CODE,node from moeaic.dbo.OFITB920) b on a.code=b.OPER_ITEM_CODE where ");
		sb.append("LEN(code)>4 and  level='2' and enable='1')x");
		String forStmt = sb.toString();
//		String forStmt = "SELECT code,codename,isSP FROM OFI_TWSIC where level='2' and enable='1' ";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
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
	public Map<String,List<String>> getReInvestXTWSIC(String reInvestNo){
		Map<String,List<String>> result = new HashMap<String,List<String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT a.item,a.type FROM OFI_ReInvestXTWSIC a,OFI_ReInvestmentList b ");
		sb.append("where a.reInvestNo=b.reInvestNo and b.enable='1' and b.reInvestNo=?");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, reInvestNo);
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
	public void delete(String reInvestNo){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM OFI_ReInvestXTWSIC WHERE reInvestNo = ?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, reInvestNo);
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
	public void insert(List<OFIReInvestXTWSIC> beans) {
		String forpstmt = "insert into OFI_ReInvestXTWSIC values(?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			for(int i=0;i<beans.size();i++){
				OFIReInvestXTWSIC bean= beans.get(i);
				pstmt.setString(1, bean.getReInvestNo());
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
