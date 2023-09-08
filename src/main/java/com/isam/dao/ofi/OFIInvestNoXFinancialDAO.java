package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestNoXFinancial;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class OFIInvestNoXFinancialDAO {
//	public List<List<String>> getFinancialReport(String year,String yearp1){
//		List<List<String>> result = new ArrayList<List<String>>();
//		SQL sqltool = new SQL();
//		String forStmt = "SELECT * FROM [ISAM].[dbo].[OFI_GetFinancialReport] (?,?) order by investNo";
//		try {
//			PreparedStatement stmt = sqltool.prepare(forStmt);
//			stmt.setString(1, year);
//			stmt.setString(2, yearp1);
//			ResultSet rs = stmt.executeQuery();
//			ResultSetMetaData meta = rs.getMetaData();
//			while(rs.next()){
//				List<String> sub = new ArrayList<String>();
//				for(int i=1;i<=meta.getColumnCount();i++){
//					sub.add(DataUtil.nulltoempty(rs.getString(i)));
//				}
//				result.add(sub);
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
	public List<List<String>> getFinancialReport(String year,String YN){
		List<List<String>> result = new ArrayList<List<String>>();
		SQL sqltool = new SQL();
		List<String> alist=getAudit02Items();
		String aitem=DataUtil.fmtPvtItem(alist);
		List<String> title= getFinancialReportTitle(year, alist);
		StringBuilder sb = new StringBuilder();
		sb.append("select x.investNo,b.COMP_CHTNAME,b.COUNTY_NAME+b.TOWN_NAME+b.ADDRESS addr,b.CHARGE_PERSON");
		sb.append(",b.TEL_NO,c.contact,x.Description,x.sdate,x.edate,x.pCapital,x.sp,");
		sb.append("case when x.issp=1 then 'Y' else '' end issp,case when x.value=1 then 'Y' else '' end value,");
		sb.append(aitem).append(",x.iyear,x.fyear,x.reportdate,CA,SE,asset,NI,cost,expense,NP,");
		sb.append("firm,accountant,fnote,note,emptt,emptw,empforeign,empcn from (SELECT * FROM OFI_GetFinancialReport(?))x ");
		sb.append("left join (SELECT * FROM [moeaic].[dbo].[OFI_BASEDATA](null))b on x.investNo=b.INVESTMENT_NO left join ");
		sb.append("(SELECT * FROM OFI_getAgentContactStrs()) c on x.investNo=c.investNo left join (select * from (SELECT investNo,value,");
		sb.append("auditCode+'_'+convert(nvarchar(50),ROW_NUMBER() over (PARTITION by investNo,auditCode order by seq asc)) sort FROM OFI_InvestNoXAudit ");
		sb.append("where auditCode like '02%' and investNo in (select investNo FROM OFI_InvestNoXAudit where auditCode='0205' and value='1'))a ");
		sb.append("pivot(MAX(a.value) for a.sort in (").append(aitem).append(")) p)e on x.investNo=e.investNo ");
		sb.append("where (x.value is not null or x.issp is not null or x.reportdate is not null) and year=?");
		if(!YN.isEmpty()){
			if(YN.equals("1")){
				sb.append(" and x.reportdate is not null");
			}else if(YN.equals("0")){
				sb.append(" and x.reportdate is null");
			}
		}
		String forStmt = sb.toString();
//		System.out.println(forStmt);
//		System.out.println("year="+year);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, year);
			stmt.setString(2, year);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				List<String> sub = new ArrayList<String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.add(DataUtil.nulltoempty(rs.getString(i)));
				}
				result.add(sub);
			}
			result.add(0, title);
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
	public List<String> getFinancialReportTitle(String year,List<String> alist){
		List<String> list = new ArrayList<String>();
		list.add("陸資案號");
		list.add("公司名稱");
		list.add("事業地址");
		list.add("負責人");
		list.add("電話");
		list.add("聯絡人");
		list.add("經營狀況");
		list.add("停業起日");
		list.add("停業迄日");
		list.add("實收資本額");
		list.add("陸資投資事業(%)");
		list.add("符合實收資本額及陸資條件");
		list.add("符合稽核條件");
		if(alist.size()>4){
			for (int i = 1; i <= Integer.valueOf(alist.size()/4); i++) {
				list.add("發文日期_"+i);
				list.add("文號_"+i);
				list.add("說明項次_"+i);
				list.add("附款內容_"+i);
			}
		}else{
			list.add("發文日期");
			list.add("文號");
			list.add("說明項次");
			list.add("附款內容");
		}
		list.add("訪視年度");
		list.add("財報年度");
		list.add("填報日期");
		list.add("流動資產");
		list.add("股東權益");
		list.add("總資產");
		list.add("營業收入淨額");
		list.add("營業成本");
		list.add("營業費用");
		list.add("本期稅後損益");
		list.add("事務所");
		list.add("會計師");
		list.add("財簽保留意見");
		list.add("備註");
		list.add("員工總人數");
		list.add("台灣籍員工人數");
		list.add("外國籍員工人數");
		list.add("大陸籍員工人數");
		return list;
	}
	public List<String> getAudit02Items(){
		List<String> result = new ArrayList<String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT distinct auditCode+'_'+convert(nvarchar(50),ROW_NUMBER() over (PARTITION by [investNo], ");
		sb.append("[auditCode] order by seq asc)) sort,seq,auditCode FROM OFI_InvestNoXAudit where auditCode in('0201','0202','0203','0204') ");
		sb.append("and investNo in (select investNo  FROM OFI_InvestNoXAudit where auditCode='0205' and value='1') order by seq,auditCode");
		String forStmt = sb.toString();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.add(DataUtil.nulltoempty(rs.getString(1)));
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
	public List<OFIInvestNoXFinancial> select(String investNo,String reportyear,String seq){
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_InvestNoXFinancial where investNo=? and reportyear=isnull(?,reportyear) and seq=isnull(?,seq) "
					   + "order by reportyear desc"; //107-08-10追加排序
		List<OFIInvestNoXFinancial> list= new ArrayList<OFIInvestNoXFinancial>();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, reportyear);
			stmt.setString(3, seq);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				OFIInvestNoXFinancial bean= new OFIInvestNoXFinancial();
				bean.setSerno(rs.getString("serno"));
				bean.setInvestNo(rs.getString("investNo"));
				bean.setReportyear(rs.getString("reportyear"));
				bean.setReportdate(rs.getString("reportdate"));
				bean.setNote(rs.getString("note"));
				bean.setSeq(rs.getString("seq"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				list.add(bean);
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
	public OFIInvestNoXFinancial selectbean(String investNo,String reportyear,String seq){
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_InvestNoXFinancial where investNo=?  and reportyear=? and seq=isnull(?,seq)";
		OFIInvestNoXFinancial bean= new OFIInvestNoXFinancial();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investNo);
			stmt.setString(2, reportyear);
			stmt.setString(3, seq);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean.setSerno(rs.getString("serno"));
				bean.setInvestNo(rs.getString("investNo"));
				bean.setReportyear(rs.getString("reportyear"));
				bean.setReportdate(rs.getString("reportdate"));
				bean.setNote(rs.getString("note"));
				bean.setSeq(rs.getString("seq"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
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
	public OFIInvestNoXFinancial selectBySerno(String serno){
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_InvestNoXFinancial where serno=?";
		OFIInvestNoXFinancial bean= new OFIInvestNoXFinancial();
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, serno);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()){
				bean.setSerno(rs.getString("serno"));
				bean.setInvestNo(rs.getString("investNo"));
				bean.setReportyear(rs.getString("reportyear"));
				bean.setReportdate(rs.getString("reportdate"));
				bean.setNote(rs.getString("note"));
				bean.setSeq(rs.getString("seq"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
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
	
	public ArrayList<Map<String, String>> selectBySernoS(ArrayList<String> sernoS){
		ArrayList<Map<String, String>> result = null;
		SQL sqltool = new SQL();
		String forStmt = "SELECT A.serno, A.investNo, A.reportyear, B.isNew, B.setupdate "
						+ "FROM OFI_InvestNoXFinancial A "
						+ "LEFT OUTER JOIN OFI_InvestList B ON A.investNo = B.investNo "
						+ "where serno=? ";
		try {
			result = new ArrayList<>();
			PreparedStatement stmt = sqltool.prepare(forStmt);
			for(String serno : sernoS){
				
				stmt.setString(1, serno);
				ResultSet rs = stmt.executeQuery();
				if(rs.next()){
					Map<String, String> data = new HashMap<>();
					data.put("serno", rs.getString("serno"));
					data.put("investNo", rs.getString("investNo"));
					data.put("reportyear", rs.getString("reportyear"));
					data.put("isNew", rs.getString("isNew"));
					data.put("setupdate", rs.getString("setupdate"));
					result.add(data);
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
	public void delete(String serno){
		SQL sqltool = new SQL();
		String forStmt = "DELETE FROM OFI_InvestNoXFinancial WHERE serno = ?";
		try {
			PreparedStatement pstmt = sqltool.prepare(forStmt);
			pstmt.setString(1, serno);
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
	public void update(OFIInvestNoXFinancial bean) {
		String forpstmt = "update OFI_InvestNoXFinancial set reportyear=?,reportdate=?,note=?,updatetime=?,updateuser=? where serno=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getReportyear());
			pstmt.setString(2, bean.getReportdate());
			pstmt.setString(3, bean.getNote());
			pstmt.setTimestamp(4, bean.getUpdatetime());
			pstmt.setString(5, bean.getUpdateuser());
			pstmt.setString(6, bean.getSerno());
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
	public int insert(OFIInvestNoXFinancial bean) {
		int result= 0;
		String forpstmt = "insert into OFI_InvestNoXFinancial OUTPUT INSERTED.serno values(?,?,?,?,?,?,?,?,?)";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getInvestNo());
			pstmt.setString(2, bean.getReportyear());
			pstmt.setString(3, bean.getReportdate());
			pstmt.setString(4, bean.getNote());
			pstmt.setString(5, bean.getSeq());
			pstmt.setTimestamp(6, bean.getUpdatetime());
			pstmt.setString(7, bean.getUpdateuser());
			pstmt.setTimestamp(8, bean.getCreatetime());
			pstmt.setString(9, bean.getCreateuser());
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				result=rs.getInt("serno");
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
}
