package com.isam.ofi.reject.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.isam.helper.DataUtil;
import com.isam.helper.SQL;
import com.isam.ofi.reject.bean.OFIReject;

public class OFIRejectDAO {
	
	public Map<String,String> getMAXMINDay(){
		SQL sqltool = new SQL();
		String forStmt = "select MAX(receiveDate) MaxR,MIN(receiveDate) MinR,MAX(issueDate) MaxI,MIN(issueDate) MinI from OFI_Reject where enable='1'";
		Map<String,String> map=new HashMap<String, String>();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			if(rs.next()){
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					map.put(meta.getColumnName(i), rs.getString(i));
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
		return map;
	}
	public List<Map<String,String>> getRejectsList(Map<String,String> terms){
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		String cName=terms.get("cName");
		String cApplicant=terms.get("cApplicant");
		String rejectType=terms.get("rejectType");
		String decision=terms.get("decision");
		String MaxR=terms.get("MaxR");
		String MinR=terms.get("MinR");
		String MaxI=terms.get("MaxI");
		String MinI=terms.get("MinI");
		
		sb.append("SELECT a.serno,a.cNo,a.receiveDate,a.issueDate,b.cName,rt.Description ");
		sb.append("FROM OFI_Reject a,OFI_RejectCompany b,");
		sb.append("(select Description,OptionValue from OFI_WebOption where SelectName='rejectType') rt ");
		sb.append("where a.cNo=b.cNo and a.enable='1' and b.enable='1' and rt.OptionValue=a.rejectType ");
		if(!rejectType.isEmpty()){
			sb.append("and rejectType in (").append(rejectType).append(") ");
		}
		sb.append("and decision=ISNULL(?,decision) and b.cName like'");
		sb.append(cName).append("' and a.serno in (SELECT serno FROM OFI_RejectXApplicant ");
		sb.append("where enable='1' and (cApplicant like '").append(cApplicant);
		sb.append("' or eApplicant like '").append(cApplicant).append("')) ");
		sb.append("and a.receiveDate>=").append(MinR).append(" and a.receiveDate<=").append(MaxR);
		sb.append(" and a.issueDate>=").append(MinI).append(" and a.issueDate<=").append(MaxI);
		
		
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, decision.isEmpty()?null:decision);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				Map<String,String> map=new HashMap<String, String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					map.put(meta.getColumnName(i), rs.getString(i));
				}
				list.add(map);
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
	
	public List<Map<String,String>> getRejectsCounts(Map<String,String> terms){
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		String rejectType=terms.get("rejectType");
		String yearPvt=terms.get("yearPvt");
		
		sb.append("select *  from (SELECT OptionValue FROM OFI_WebOption where SelectName='rejectType' and enable='1' ");
		if(!rejectType.isEmpty()){
			sb.append("and OptionValue in(").append(rejectType).append(")");
		}
		sb.append(")x left join (SELECT rejectType,LEFT(issueDate,3) year,COUNT(cNo) c ");
		sb.append("FROM OFI_Reject where enable='1' and rejectType in (SELECT OptionValue ");
		sb.append("FROM OFI_WebOption where SelectName='rejectType' and enable='1') ");
		sb.append(getRejectRange(terms));
		sb.append(" group by rejectType,LEFT(issueDate,3)) a pivot (sum(c) for a.year in (");
		sb.append(yearPvt).append("))as pvt on x.OptionValue=pvt.rejectType order by x.OptionValue");
		
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				Map<String,String> map=new LinkedHashMap<String, String>();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					map.put(meta.getColumnName(i), DataUtil.nulltoempty(rs.getString(i)));
				}
				list.add(map);
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
	/*for generate excel start*/
	private String getRejectRange(Map<String,String> terms){
		StringBuilder sb = new StringBuilder();
		String rejectType=terms.get("rejectType");
		String MaxR=terms.get("MaxR");
		String MinR=terms.get("MinR");
		String MaxI=terms.get("MaxI");
		String MinI=terms.get("MinI");
		sb.append(" and receiveDate>=").append(MinR).append(" and receiveDate<=").append(MaxR);
		sb.append(" and issueDate>=").append(MinI).append(" and issueDate<=").append(MaxI).append(" ");
		if(!rejectType.isEmpty()){
			sb.append(" and rejectType in(").append(rejectType).append(") ");
		}
		String forStmt = sb.toString();
		sb.setLength(0);
		return forStmt;
	}
	private String getMaxApplicantCount(Map<String,String> terms){
		StringBuilder sb = new StringBuilder();
		sb.append("select MAX(sort) max from (SELECT ROW_NUMBER() over(Partition By ");
		sb.append("serno Order By applyNo Desc) sort FROM OFI_RejectXApplicant where ");
		sb.append("enable='1' and serno in(SELECT serno FROM OFI_Reject where enable='1' ");
		sb.append(getRejectRange(terms)).append("))x");
		String forStmt = sb.toString();
		sb.setLength(0);
		return forStmt;
	}
	private List<String> getApplicantPivotColumn(){
		List<String> column=new ArrayList<String>();
		column.add("cApplicant");
		column.add("eApplicant");
		column.add("nation");
		column.add("cnCode");
		column.add("note");
		column.add("agent");
		return column;
	}
	private String getApplicantPivotStr(int count){
		StringBuilder sb = new StringBuilder();
		List<String> column=getApplicantPivotColumn();
		for (int i = 1; i <= count; i++) {
			for (int j = 0; j < column.size(); j++) {
				if(sb.length()>0){
					sb.append(",");
				}
				sb.append("[").append(column.get(j)).append("_").append(i).append("]");
			}
		}
		String str = sb.toString();
		sb.setLength(0);
		return str;
	}
	private String getApplicantOrderStr(){
		StringBuilder sb = new StringBuilder();
		List<String> column=getApplicantPivotColumn();
		for (int j = 0; j < column.size(); j++) {
				if(sb.length()>0){
					sb.append(",");
				}
			sb.append("('").append(column.get(j)).append("_'+convert(nvarchar(50),C.sort),C.").append(column.get(j)).append(")");
		}
		String str = sb.toString();
		sb.setLength(0);
		return str;
	}
	private String getRejectContextForStmt(Map<String,String> terms,int count){
		String pvt=getApplicantPivotStr(count);
		String pvtTitle=pvt.replaceAll("\\[", "appl.[");
		String pvtOrder=getApplicantOrderStr();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT rejectType keycode,(SELECT dbo.OFI_WebOptionName ('rejectType',a.rejectType)) rejectType");
		sb.append(",b.cName,(SELECT dbo.OFI_WebOptionName ('isNew',b.isNew)) isNew,setupdate");
		sb.append(",(SELECT dbo.OFI_WebOptionName ('orgType',b.orgType)) orgType,a.receiveDate,a.receiveNo");
		sb.append(",(SELECT dbo.OFI_WebOptionName ('currency',currency)) currency,a.money,a.money_other as 'moneyother',a.shareholding");
		sb.append(",sic.item,a.otherSic,a.issueDate,issueNo,(SELECT dbo.OFI_WebOptionName('decision',decision)) decision");
		sb.append(",explanation,reason,note,").append(pvtTitle).append(" FROM OFI_RejectCompany b,OFI_Reject a ");
		sb.append("left join (SELECT * FROM dbo.OFI_RejectTWSICXName()) sic on sic.serno=a.serno ");
		sb.append("left join(SELECT * FROM (SELECT serno,X.value,X.col FROM (SELECT serno,a.cApplicant");
		sb.append(",a.eApplicant ,(SELECT dbo.OFI_WebOptionName('nation',nation)) nation ");
		sb.append(",(SELECT dbo.OFI_WebOptionName('cnCode',cnCode)) cnCode,note,agent");
		sb.append(",ROW_NUMBER() over(Partition By serno Order By a.applyNo Desc) sort ");
		sb.append("FROM OFI_RejectXApplicant a left join (SELECT applyNo,agent FROM OFI_RejectAgentStrs())ag ");
		sb.append("on ag.applyNo=a.applyNo where a.enable='1') C CROSS APPLY (VALUES").append(pvtOrder);
		sb.append(")X (col,value)) S PIVOT (max([value]) FOR [col] IN (").append(pvt).append(")) P) appl ");
		sb.append("on appl.serno=a.serno where a.cNo=b.cNo and a.enable='1' and b.enable='1' ");
		sb.append(getRejectRange(terms)).append("order by keycode,issueDate");
		String forStmt = sb.toString();
		////System.out.println(forStmt);
		sb.setLength(0);
		return forStmt;
	}
	public Map<String,List<List<String>>> getRejectContext(Map<String,String> terms){
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.setLength(0);
		Map<String,List<List<String>>> result = new TreeMap<String, List<List<String>>>();
		try {
			PreparedStatement stmt = sqltool.prepare(getMaxApplicantCount(terms));
			ResultSet rs = stmt.executeQuery();
			int count=0;
			if(rs.next()){
				count=rs.getInt(1);
			}
			if(count!=0){
				stmt = sqltool.prepare(getRejectContextForStmt(terms,count));
				rs = stmt.executeQuery();
				ResultSetMetaData meta=rs.getMetaData();
				List<String> title=new ArrayList<String>();
				List<List<String>> list;
				int flag=0;
				while(rs.next()){
					String key=rs.getString("keycode");
					if(result.containsKey(key)){
						list=result.get(key);
					}else{
						list=new ArrayList<List<String>>();
					}
					List<String> sub=new ArrayList<String>();
					for (int i = 2; i <= meta.getColumnCount(); i++) {
						sub.add(DataUtil.nulltoempty(rs.getString(i)));
						if(flag==0){
							title.add(meta.getColumnName(i));
						}
					}
					flag++;
					list.add(sub);
					result.put(key,list);
				}
				for (Entry<String, List<List<String>>> m:result.entrySet()) {
					List<List<String>> l=m.getValue();
					l.add(0,title);
					result.put(m.getKey(), l);
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
	/*for generate excel end*/
	public OFIReject select(String serno){
		OFIReject bean = new OFIReject();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_Reject WHERE serno = ? and enable = '1'";
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean.setSerno(rs.getInt("serno"));
				bean.setcNo(rs.getString("cNo"));
				bean.setReceiveDate(rs.getString("receiveDate"));
				bean.setReceiveNo(rs.getString("receiveNo"));
				bean.setCurrency(rs.getString("currency"));
				bean.setMoney(DataUtil.nulltoempty(rs.getString("money")));
				bean.setShareholding(DataUtil.nulltoempty(rs.getString("shareholding")));
				bean.setOtherSic(DataUtil.nulltoempty(rs.getString("otherSic")));
				bean.setIssueDate(rs.getString("issueDate"));
				bean.setIssueNo(rs.getString("issueNo"));
				bean.setRejectType(rs.getString("rejectType"));
				bean.setDecision(rs.getString("decision"));
				bean.setExplanation(rs.getString("explanation"));
				bean.setReason(rs.getString("reason"));
				bean.setNote(rs.getString("note"));
				bean.setEnable(rs.getString("enable"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setMoneyother(rs.getString("money_other"));
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
	public String insert(OFIReject bean) {
		String forpstmt = "insert into OFI_Reject output inserted.serno values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		String str="";
		int count=1;
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(count++, bean.getcNo());
			pstmt.setString(count++, bean.getReceiveDate());
			pstmt.setString(count++, bean.getReceiveNo());
			pstmt.setString(count++, bean.getCurrency());
			pstmt.setObject(count++, bean.getMoney());
			pstmt.setObject(count++, bean.getShareholding());
			pstmt.setString(count++, bean.getOtherSic());
			pstmt.setString(count++, bean.getIssueDate());
			pstmt.setString(count++, bean.getIssueNo());
			pstmt.setString(count++, bean.getRejectType());
			pstmt.setString(count++, bean.getDecision());
			pstmt.setString(count++, bean.getExplanation());
			pstmt.setString(count++, bean.getReason());
			pstmt.setString(count++, bean.getNote());
			pstmt.setString(count++, bean.getEnable());
			pstmt.setTimestamp(count++, bean.getUpdatetime());
			pstmt.setString(count++, bean.getUpdateuser());
			pstmt.setTimestamp(count++, bean.getCreatetime());
			pstmt.setString(count++, bean.getCreateuser());
			pstmt.setString(count++, bean.getMoneyother());
			ResultSet rs= pstmt.executeQuery();
			if(rs.next()){
				str=rs.getString(1);
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
		return str;
	}
	public void update(OFIReject bean) {
		StringBuilder sb=new StringBuilder();
		sb.append("update OFI_Reject set receiveDate=?,receiveNo=?,currency=?,money=?,shareholding=?,OtherSic=?,");
		sb.append("issueDate=?,issueNo=?,rejectType=?,decision=?,explanation=?,reason=?,note=?,enable=?,");
		sb.append("updatetime=?,updateuser=?, money_other=? where serno=?");
		String forpstmt = sb.toString();
		sb.setLength(0);
		SQL sqltool = new SQL();
		int count=1;
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(count++, bean.getReceiveDate());
			pstmt.setString(count++, bean.getReceiveNo());
			pstmt.setString(count++, bean.getCurrency());
			pstmt.setObject(count++, bean.getMoney());
			pstmt.setObject(count++, bean.getShareholding());
			pstmt.setString(count++, bean.getOtherSic());
			pstmt.setString(count++, bean.getIssueDate());
			pstmt.setString(count++, bean.getIssueNo());
			pstmt.setString(count++, bean.getRejectType());
			pstmt.setString(count++, bean.getDecision());
			pstmt.setString(count++, bean.getExplanation());
			pstmt.setString(count++, bean.getReason());
			pstmt.setString(count++, bean.getNote());
			pstmt.setString(count++, bean.getEnable());
			pstmt.setTimestamp(count++, bean.getUpdatetime());
			pstmt.setString(count++, bean.getUpdateuser());
			pstmt.setString(count++, bean.getMoneyother());
			pstmt.setInt(count++, bean.getSerno());
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
	public void delete(String serno) {
		String forpstmt = "delete OFI_Reject where serno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, serno);
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
	public void mergeCNo(OFIReject bean,String cname,String cNo) {
		String forpstmt = "update OFI_Reject set cNo=?,updatetime=?,updateuser=? where cNo in(SELECT cNo FROM OFI_RejectCompany where cName=? and cNo!=? and enable='1') and enable='1'";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, cNo);
			pstmt.setTimestamp(2, bean.getUpdatetime());
			pstmt.setString(3, bean.getUpdateuser());
			pstmt.setString(4, cname);
			pstmt.setString(5, cNo);
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
