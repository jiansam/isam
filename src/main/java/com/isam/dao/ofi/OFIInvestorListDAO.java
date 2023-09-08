package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestorList;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class OFIInvestorListDAO {
	
	public List<Map<String,String>> select(Map<String,String> terms){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		SQL sqltool = new SQL();
		String investNo=terms.get("investNo");
		String IDNO=terms.get("IDNO");
		String company=terms.get("company");
		String companytype=terms.get("companytype");
		String investor=terms.get("investor");
		String nation=terms.get("nation").isEmpty()?null:terms.get("nation");
		String cnCode=terms.get("cnCode").isEmpty()?null:terms.get("cnCode");
		String BG1Str=fmtBGterms(terms.get("BG1"),"BG1",terms.get("AndOr1"));
		String BG2Str=fmtBGterms(terms.get("BG2"),"BG2",terms.get("AndOr2"));
		String investorOnly =  terms.get("investorOnly")==null?null:terms.get("investorOnly");
		
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (SELECT distinct i.investNoStrs,a.[investorSeq],e.INVESTOR_CHTNAME ");
		sb.append(",(SELECT [dbo].[OFI_WebOptionName] ('nation',a.nation)) country,(SELECT [dbo].[OFI_WebOptionName] ('cnCode',a.cnCode)) cn");
		sb.append(",e.INVE_ROLE_CODE AS inrole ,a.note"); //106-09-27 追加[投資人資金類型][投資人備註]兩欄，EXCEL用
		sb.append(",a.isFilled FROM moeaic.dbo.OFITB205 e, OFI_InvestorList a left join (");
		sb.append("SELECT T1.investorSeq,(STUFF (( select '、'+investNo from (SELECT investNo,investorSeq FROM OFI_InvestCaseList where enable='1') t2 ");
		sb.append("where t2.investorSeq=t1.investorSeq order by investorSeq,investNo for XML Path ('')) ,1,1,'')) as investNoStrs ");
		sb.append("from (SELECT investNo,investorSeq FROM OFI_InvestCaseList where enable='1') t1 group by investorSeq) i ");
		sb.append("on a.investorSeq=i.investorSeq where a.investorSeq=e.INVESTOR_SEQ and a.nation=ISNULL(?,a.nation) and a.cnCode=ISNULL(?,a.cnCode) ");
		sb.append(")tmp where ");

		sb.append("tmp.investorSeq in (SELECT INVESTOR_SEQ FROM OFI_InvestCase where isnull(BAN_NO,'') like '");
		sb.append(IDNO).append("' and investNo like '").append(investNo).append("' and COMP_CHTNAME like '").append(company);
		sb.append("' and INVESTOR_CHTNAME like '").append(investor).append("' union all select INVESTOR_SEQ from OFI_InvestCase ");
		sb.append("where investNo in(SELECT investNo FROM OFI_ReInvestXInvestNo where enable='1' and reInvestNo ");
		sb.append("in (select reInvestNo from OFI_ReInvestmentList where reinvestment like '");
		sb.append(company).append("' and idno like '").append(IDNO).append("') and investNo like '").append(investNo);
		sb.append("')  and INVESTOR_CHTNAME like '").append(investor).append("') ");
		
		
		//107-06-25 因為原筆數有上千筆，有填進這兩個table的只有幾百筆，為避免無法取出全部，在這三欄有填資料時，才加入這串SQL
		String investorXRelated = terms.get("investorXRelated");
		String relatedNation = terms.get("relatedNation");
		String relatedCnCode = terms.get("relatedCnCode");
		if(!(investorXRelated.replaceAll("%", "")).isEmpty() || !relatedNation.isEmpty() || !relatedCnCode.isEmpty()) {

			if(!(investorXRelated.replaceAll("%", "")).isEmpty()) {
				//關鍵字不空白，兩個表格都要找
				sb.append("and ("
						+ "tmp.investorSeq in ("
									+ "select investorSeq from OFI_InvestorXRelated "
									+ "where relatedname like '" + investorXRelated +"' "
									+ "and nation = isnull( convert( nvarchar(3), NULLIF('"+ relatedNation +"', '') ) , nation) "
									+ "and cnCode = isnull( convert( nvarchar(5), NULLIF('"+ relatedCnCode +"', '') ) , cnCode) "
									+ ") "
						+ "OR "
						+ "tmp.investorSeq in ("
									+ "SELECT investorSeq FROM OFI_InvestorXBG where bgType='BG1Note' AND value like '" + investorXRelated + "' "
									+ "UNION all "
									+ "SELECT investorSeq FROM OFI_InvestorXBG where bgType='BG2Note' AND value like '" + investorXRelated + "' "
									+ ") "
						+ ") " );
			}else if(!relatedNation.isEmpty() || !relatedCnCode.isEmpty()) {
				//選擇省份時，則只搜尋母公司表格
				sb.append("and ("
						+ "tmp.investorSeq in ("
									+ "select investorSeq from OFI_InvestorXRelated "
									+ "where relatedname like '" + investorXRelated +"' "
									+ "and nation = isnull( convert( nvarchar(3), NULLIF('"+ relatedNation +"', '') ) , nation) "
									+ "and cnCode = isnull( convert( nvarchar(5), NULLIF('"+ relatedCnCode +"', '') ) , cnCode) "
									+ ") "
						+ ") " );

			}
		}
		
		if(!BG1Str.isEmpty()){
			sb.append("and ").append(BG1Str);
		}
		if(!BG2Str.isEmpty()){
			sb.append("and ").append(BG2Str);
		}
		System.out.println(companytype);
		
		
		/* 新增辦事處查詢 start*/
		if( (  investorOnly == null ) &&(  companytype==null|| companytype.isEmpty() || companytype.equals("2"))) {
			if(companytype!=null && companytype.equals("2")) {
				sb.append(" and investNoStrs = '' ");
			}
			
			sb.append(" union all select '' as investNoStrs, '' as investorSeq , COMP_CHTNAME as INVESTOR_CHTNAME , '' as country , '' as cn, '3' as inrole, Ban_NO as note, '1' as isFilled from OFI_InvestOffice where 1=1 ");
			
			//if(!company.replace("%", "").isEmpty() || !investor.replace("%", "").isEmpty()  || !investNo.replace("%", "").isEmpty() || !IDNO.replace("%", "").isEmpty() ) {
			//}
			if(!company.replace("%", "").isEmpty() ) {
				sb.append(" and COMP_CHTNAME like '").append(company).append("'");
			}
			if(!investor.replace("%", "").isEmpty() ) {
				sb.append(" and COMP_CHTNAME like '").append(investor).append("'");
			}
			if(!(investorXRelated.replaceAll("%", "")).isEmpty()) {
				sb.append(" and COMP_CHTNAME like '").append(investorXRelated).append("'");
				
			}
			if(!investNo.replace("%", "").isEmpty()) {
				sb.append(" and COMP_CHTNAME like '").append(investNo).append("'");
			}
			if(!IDNO.replace("%", "").isEmpty()) {
				sb.append(" and ban_no like '").append(IDNO).append("%'");
			}
		}
		/* 新增辦事處查詢 end */
		
		String forStmt = sb.toString();
		System.out.println(forStmt);
		sb.setLength(0);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, nation);
			stmt.setString(2, cnCode);
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
//	public List<Map<String,String>> select(Map<String,String> terms){
//		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
//		SQL sqltool = new SQL();
//		String investNo=terms.get("investNo");
//		String IDNO=terms.get("IDNO");
//		String company=terms.get("company");
//		String investor=terms.get("investor");
//		String nation=terms.get("nation").isEmpty()?null:terms.get("nation");
//		String cnCode=terms.get("cnCode").isEmpty()?null:terms.get("cnCode");
//		String BG1Str=fmtBGterms(terms.get("BG1"),"BG1",terms.get("AndOr1"));
//		String BG2Str=fmtBGterms(terms.get("BG2"),"BG2",terms.get("AndOr2"));
//		
//		StringBuilder sb = new StringBuilder();
//		sb.append("select * from ( SELECT distinct a.[investorSeq],e.INVESTOR_CHTNAME,a.nation,a.cnCode,b.Description country,c.Description cn,d.IN_AGENT,d.TEL_NO,a.isFilled,a.enable ");
//		sb.append("FROM (select * from OFI_WebOption where SelectName='nation' and enable='1' and OptionValue=ISNULL(?,OptionValue))b,moeaic.dbo.OFITB205 e, ");
//		sb.append("OFI_InvestorList a left join (select * from OFI_WebOption where SelectName='cnCode' and enable='1')c ");
//		sb.append("on a.cnCode=c.OptionValue left join (select * from (SELECT INVESTOR_SEQ,[IN_AGENT],[TEL_NO],[POSITION_NAME] ");
//		sb.append(",ROW_NUMBER() Over (Partition By INVESTOR_SEQ Order By resp_date Desc) As Sort FROM OFI_Agent)a where sort='1')d ");
//		sb.append("on a.investorSeq=d.INVESTOR_SEQ where a.nation=b.OptionValue and a.investorSeq=e.INVESTOR_SEQ)tmp where cnCode=ISNULL(?,cnCode) ");
//		if(!BG1Str.isEmpty()){
//			sb.append("and ").append(BG1Str);
//		}
//		if(!BG2Str.isEmpty()){
//			sb.append("and ").append(BG2Str);
//		}
//		sb.append("and tmp.investorSeq in (SELECT INVESTOR_SEQ FROM OFI_InvestCase where isnull(BAN_NO,'') like '");
//		sb.append(IDNO).append("' and investNo like '").append(investNo).append("' and COMP_CHTNAME like '").append(company);
//		sb.append("' and INVESTOR_CHTNAME like '").append(investor).append("' union all select INVESTOR_SEQ from OFI_InvestCase ");
//		sb.append("where investNo in(SELECT investNo FROM OFI_ReInvestXInvestNo where enable='1' and reInvestNo ");
//		sb.append("in (select reInvestNo from OFI_ReInvestmentList where reinvestment like '");
//		sb.append(company).append("' and idno like '").append(IDNO).append("') and investNo like '").append(investNo);
//		sb.append("')  and INVESTOR_CHTNAME like '").append(investor).append("')");
//		
//		String forStmt = sb.toString();
//		System.out.println(forStmt);
//		sb.setLength(0);
//		try {
//			PreparedStatement stmt = sqltool.prepare(forStmt);
//			stmt.setString(1, nation);
//			stmt.setString(2, cnCode);
//			ResultSet rs = stmt.executeQuery();
//			ResultSetMetaData meta = rs.getMetaData();
//			while(rs.next()){
//				Map<String,String> sub = new HashMap<String, String>();
//				for(int i=1;i<=meta.getColumnCount();i++){
//					sub.put(meta.getColumnName(i),rs.getString(i));
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
	public OFIInvestorList select(String investorSeq){
		SQL sqltool = new SQL();
		String forStmt = "SELECT a.*,OFITB205.INVESTOR_CHTNAME cname,OFITB205.INVESTOR_ENGNAME ename,OFITB205.INVE_ROLE_CODE inrole FROM OFI_InvestorList a,moeaic.dbo.OFITB205 OFITB205 where a.investorSeq=OFITB205.INVESTOR_SEQ and a.investorSeq=?";
		OFIInvestorList bean= null;
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			stmt.setString(1, investorSeq);
			System.out.println( forStmt);
			System.out.println(investorSeq);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				bean= new OFIInvestorList();
				bean.setInvestorSeq(rs.getString("investorSeq"));
				bean.setNation(rs.getString("nation"));
				bean.setCnCode(rs.getString("cnCode"));
				bean.setNote(rs.getString("note"));
				bean.setIsFilled(rs.getString("isFilled"));
				bean.setEnable(rs.getString("enable"));
				bean.setUpdatetime(rs.getTimestamp("updatetime"));
				bean.setUpdateuser(rs.getString("updateuser"));
				bean.setCreatetime(rs.getTimestamp("createtime"));
				bean.setCreateuser(rs.getString("createuser"));
				bean.setCname(rs.getString("cname"));
				bean.setEname(rs.getString("ename"));
				bean.setInrole(rs.getString("inrole"));
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
	public void update(OFIInvestorList bean) {
		String forpstmt = "update OFI_InvestorList set cnCode=?,note=?,isFilled=?,updatetime=?,updateuser=? where investorSeq=?";
		SQL sqltool = new SQL();
		try {
			PreparedStatement pstmt = sqltool.prepare(forpstmt);
			pstmt.setString(1, bean.getCnCode());
			pstmt.setString(2, bean.getNote());
			pstmt.setString(3, bean.getIsFilled());
			pstmt.setTimestamp(4, bean.getUpdatetime());
			pstmt.setString(5, bean.getUpdateuser());
			pstmt.setString(6, bean.getInvestorSeq());
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
	/*99被拿掉了 改用否*/
	private String fmtBGterms(String term,String bgType,String AndOr){
		List<String> bglist=null;
		StringBuilder sb = new StringBuilder();
		String bg="";
		String andor=AndOr.isEmpty()?" or ":" and ";
		if(!term.isEmpty()){
			bglist=DataUtil.StrArytoList(term.split(","));
			sb.append(" (");
			if(bglist.indexOf("99")!=-1&&!bglist.isEmpty()){
				bglist.remove(bglist.indexOf("99"));
				sb.append("tmp.investorSeq not in (SELECT [investorSeq] FROM OFI_InvestorXBG where bgType='");
				sb.append(bgType).append("') ");
				if(bglist.size()>0){
					sb.append(andor);
				}
			}
			if(bglist.size()>0){
				if(AndOr.isEmpty()){
					sb.append(" tmp.investorSeq  in (SELECT investorSeq FROM OFI_InvestorXBG where ");
					sb.append(" bgType='").append(bgType).append("' and value in (").append(DataUtil.fmtStrAryItem(bglist)).append("))");
				}else{
					String num="";
					for (int i = 0; i < bglist.size(); i++) {
						num+=bglist.get(i);
					}
					sb.append(" tmp.investorSeq  in (select investorSeq from (SELECT investorSeq,(SELECT value+'' FROM OFI_InvestorXBG t2 ");
					sb.append("where t1.bgType=t2.bgType and t1.investorSeq=t2.investorSeq order by t2.investorSeq,t2.seq ");
					sb.append("FOR XML PATH('')) as bg FROM OFI_InvestorXBG t1 where bgType='").append(bgType).append("')a where a.bg='");
					sb.append(num).append("')");
				}
			}
			sb.append(") ");
			bg=sb.toString();
		}
		sb.setLength(0);
		return bg;
	}
/*	private String fmtBGterms(String term,String bgType,String AndOr){
		List<String> bglist=null;
		StringBuilder sb = new StringBuilder();
		String bg="";
		String andor=AndOr.isEmpty()?" or ":" and ";
		if(!term.isEmpty()){
			bglist=DataUtil.StrArytoList(term.split(","));
			sb.append(" (");
			if(bglist.indexOf("99")!=-1&&!bglist.isEmpty()){
				bglist.remove(bglist.indexOf("99"));
				sb.append("tmp.investorSeq not in (SELECT [investorSeq] FROM OFI_InvestorXBG where bgType='");
				sb.append(bgType).append("') ");
				if(bglist.size()>0){
					sb.append("or ");
				}
			}
			if(bglist.size()>0){
				sb.append(" tmp.investorSeq  in (SELECT investorSeq FROM OFI_InvestorXBG where ");
				sb.append(" bgType='").append(bgType).append("' and value in (").append(DataUtil.fmtStrAryItem(bglist)).append("))");
			}
			sb.append(") ");
			bg=sb.toString();
		}
		sb.setLength(0);
		return bg;
	}
*/}
