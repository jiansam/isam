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
import java.util.Map.Entry;
import java.util.TreeMap;

import com.isam.bean.OFIPDFItem;
import com.isam.helper.DataUtil;
import com.isam.helper.SQL;

public class OFIAllDataDAO {
	
	public Map<String,Map<String,String>> getAuditCodeList(String investNo){
		SQL sqltool = new SQL();
		Map<String,Map<String,String>> result= new TreeMap<String,Map<String,String>>();
		StringBuilder sb = new StringBuilder();
		sb.append("select left(s1,2) type,x.auditCode,Description from (select auditCode,s1,s2 from(");
		sb.append("SELECT auditCode,case when CHARINDEX('_',auditCode)=0 and left(auditcode,2)<=02 ");
		sb.append("then 1 when CHARINDEX('_',auditCode)=0 then 100 ");
		sb.append("when CHARINDEX('_',auditCode)>0 and SUBSTRING(auditCode,CHARINDEX('_',auditCode)+1,2)=07 then 100+(0.1*convert(float,SUBSTRING(auditCode,0,CHARINDEX('_',auditCode)))) ");
		sb.append("else 1+(0.1*convert(float,SUBSTRING(auditCode,0,CHARINDEX('_',auditCode)))) end s2 ");
		sb.append(",SUBSTRING(auditCode,CHARINDEX('_',auditCode)+1,LEN(auditCode) ");
		sb.append(") s1 FROM dbo.OFI_GETALL_Audit() group by auditCode )x )x,OFI_AuditOption a where a.auditCode=x.s1 order by s2,s1");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			Map<String,String> sub;
			while(rs.next()){
				String type=rs.getString("type");
				if(result.containsKey(type)){
					sub=result.get(type);
				}else{
					sub= new LinkedHashMap<String,String>();
				}
				sub.put(rs.getString("auditCode"), rs.getString("Description"));
				result.put(type,sub);
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
	private List<String> getAuditCodeList(){
		SQL sqltool = new SQL();
		List<String> result= new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		sb.append("select auditCode from(SELECT auditCode,case when CHARINDEX('_',auditCode)=0 and left(auditcode,2)<=02 ");
		sb.append("then 1 when CHARINDEX('_',auditCode)=0 then 100 ");
		sb.append("when CHARINDEX('_',auditCode)>0 and SUBSTRING(auditCode,CHARINDEX('_',auditCode)+1,2)=07 then 100+(0.1*convert(float,SUBSTRING(auditCode,0,CHARINDEX('_',auditCode)))) ");
		sb.append("else 1+(0.1*convert(float,SUBSTRING(auditCode,0,CHARINDEX('_',auditCode)))) end s2 ");
		sb.append(",SUBSTRING(auditCode,CHARINDEX('_',auditCode)+1,LEN(auditCode) ");
		sb.append(") s1 FROM dbo.OFI_GETALL_Audit() group by auditCode )x order by s2,s1");
		String forStmt = sb.toString();
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
	private String getAllInvestmentDataSql(Map<String, String> auditOpt){
		List<String> audits=getAuditCodeList();
		StringBuilder sb = new StringBuilder();
		String aOptPvt=DataUtil.fmtPvtItem(audits);
		
		for (int i = 0; i < audits.size(); i++) {
			String str=audits.get(i);
			String[] strAry=str.split("_");			
			if(i!=0){
				sb.append(",");
			}
			if(str.length()==2){
				sb.append("isnull(a.[").append(str).append("],'未確認') '");
			}else{
				sb.append("a.[").append(str).append("] '");
			}
			if(strAry.length!=1){
				sb.append(strAry[0]).append("_").append(auditOpt.get(strAry[1]));
			}else{
				sb.append(auditOpt.get(strAry[0]));
			}
			sb.append("'");
		}
		String titleStr=sb.toString();
		sb.setLength(0);
		sb.append("SELECT b.investNo '陸資案號',ic.COMP_CHTNAME '國內事業名稱',ic.BAN_NO '統一編號',");
		sb.append("ic.OrgTypeName '組織型態',ic.issueTypeName '發行方式',");
		sb.append("(SELECT dbo.OFI_WebOptionName('isFilled',isFilled)) '資料狀態',");
		sb.append("(SELECT dbo.OFI_WebOptionName('active',active)) '執行情形',");
		sb.append("(SELECT dbo.OFI_WebOptionName('isnew',isnew)) '設立情形',");
		sb.append("setupdate '設立日期',setupnote '設立日期備註',approvaldate '初次審定或備查日期',");
		sb.append("(SELECT dbo.OFI_WebOptionName('isOperated',isOperated)) '經營狀況',");
		sb.append("sdate '停業起',edate '停業迄',inSrc.src '經營身分',ic.REGI_CAPITAL '登記資本額',");
		sb.append("case when ic.OrgTypeName='分公司' then '同登記資本額' else (SELECT [dbo].[FormatNumber] (ic.PAID_CAPITAL)) end '實收資本額',");
		sb.append("case when CharIndex('股份有限',ic.OrgTypeName)=0 then '無須填寫' ");
//		sb.append("ic.PAID_CAPITAL '實收資本額',case when CharIndex('股份有限',ic.OrgTypeName)=0 then '無須填寫' ");
		sb.append("when ic.FACE_VALUE is null then '尚無資料' else CONVERT(nvarchar(20),ic.FACE_VALUE) end'面額',");
		sb.append("c.contact '國內事業連絡人',c.tel '聯絡電話',m.item '主要營業類別',m.itemname '主要營業類別名稱',");
		sb.append("s.item '次要營業類別',s.itemname '次要營業類別名稱',sp.item '營業項目涉及特許及特殊項目',sp.itemname '營業項目涉及特許及特殊項目名稱',");
		sb.append("b.firmXNTBTSic '國稅局登記營業項目',"); //107-08-14 追加 國稅局登記營業項目 輸出欄位
		sb.append("ms.year '最新管理密度年度',ms.score '強度分數',ms.classify '管理等級',b.note '國內事業備註',f.reportyear '最新申報年度',");
		sb.append("f.reportdate '填報日期',f.CA '流動資產',f.SE '股東權益',f.asset '總資產',f.NI '營業收入淨額',");
		sb.append("f.cost '營業成本',f.expense '營業費用',f.NP '本期稅後損益',f.firm '事務所',f.accountant '會計師',");
		sb.append("f.fnote '財簽保留意見',f.note '備註',f.emptw '國內事業員工(台灣)',f.empforeign '國內事業員工(外國)',f.empcn '國內事業員工(大陸)',");
		sb.append("case when re.c>0 then '有' else '無' end '國內轉投資事業',");
		sb.append(titleStr).append(" FROM OFI_InvestList b left join (SELECT * FROM OFI_GetALL_LatestFinancial())f ");
		sb.append("on f.investNo=b.investNo left join (SELECT * FROM OFI_GETALL_ContactStrs()) c on b.investNo=c.investNo ");
		sb.append("left join (SELECT * FROM OFI_GETALL_IND('0'))sp on b.investNo=sp.investNo ");
		sb.append("left join (SELECT * FROM OFI_GETALL_IND('1'))m on b.investNo=m.investNo ");
		sb.append("left join (SELECT * FROM OFI_GETALL_IND('2'))s on b.investNo=s.investNo ");
		sb.append("left join (select * from (SELECT * FROM OFI_GETALL_Audit())a pivot(MAX(a.value) ");
		sb.append("for a.auditCode in (").append(aOptPvt).append("))p)a on a.investNo=b.investNo ");
		sb.append("left join (select * FROM [moeaic].[dbo].[OFI_BASEDATA](null))ic on ic.INVESTMENT_NO=b.investno ");
		sb.append("left join (SELECT investNo,count(reInvestNo) c FROM OFI_ReInvestXInvestNo where enable='1' ");
		sb.append("group by investNo) re on re.investNo=b.investNo left join (SELECT * FROM OFI_GETALL_INSRC()) inSrc ");
		sb.append("on inSrc.investNo=b.investNo left join (SELECT * FROM OFI_GETALL_MScore()) ms ");
		sb.append("on ms.investNo=b.investNo where b.enable='1'");
		String forStmt = sb.toString();
		sb.setLength(0);
		return forStmt;
	}
	public List<List<String>> getAllInvestmentData(Map<String, String> auditOpt){
		List<List<String>> result = new ArrayList<List<String>>();
		SQL sqltool = new SQL();
		String forStmt = getAllInvestmentDataSql(auditOpt);
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
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
	private String getAllInvestorDataSql(){
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
		sb.append("SELECT iic.investNo '陸資案號',ic.COMP_CHTNAME '國內事業名稱',ic.BAN_NO '統一編號',");
		sb.append("ic.OrgTypeName '組織型態',ic.issueTypeName '發行方式',(SELECT dbo.FormatNumber(ic.REGI_CAPITAL)) '登記資本額',");
		sb.append("(SELECT dbo.FormatNumber(ic.PAID_CAPITAL)) '實收資本額',case when CharIndex('股份有限',ic.OrgTypeName)=0 then '無須填寫 ' ");
		sb.append("when ic.FACE_VALUE is null then '尚無資料' else CONVERT(nvarchar(20),ic.FACE_VALUE) end '面額',");
		sb.append("iic.INVESTOR_CHTNAME '投資人',(SELECT dbo.OFI_WebOptionName('isFilled',isFilled)) '資料狀態'");
		sb.append(",(SELECT dbo.OFI_WebOptionName('nation',nation)) '投資人國別'");
		sb.append(",(SELECT dbo.OFI_WebOptionName('cnCode',cnCode)) '投資人省分',iic.inSrc '資金類型'");
		sb.append(",iic.investvalue '投資金額',iic.investedcapital '持有股權(出資額)',iic.sp '股權比例',");
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
		sb.append("on ic.INVESTMENT_NO=iic.investno where i.enable='1'");
		String forStmt = sb.toString();
		sb.setLength(0);
		return forStmt;
	}
	public List<List<String>> getAllInvestorData(){
		List<List<String>> result = new ArrayList<List<String>>();
		SQL sqltool = new SQL();
		String forStmt = getAllInvestorDataSql();
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
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
	private String getAllReinvestmentDataSql(){
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT b.investNo '陸資案號',ic.COMP_CHTNAME '國內事業名稱',ic.BAN_NO '統一編號'");
		sb.append(",ic.OrgTypeName '組織型態',ic.issueTypeName '發行方式',reinvestment '轉投資事業名稱',idno '統一編號'");
		sb.append(",(SELECT dbo.OFI_WebOptionName('isFilled',isFilled)) '資料狀態'");
		sb.append(",(SELECT dbo.OFI_WebOptionName('orgType',orgType)) '組織型態'");
		sb.append(",c.COUNTY_NAME '縣市',t.TOWN_NAME '區',addr '地址'");
		sb.append(",(SELECT dbo.OFI_WebOptionName('isNew',isNew)) '設立情形'");
		sb.append(",setupdate '設立日期',setupnote '設立日期備註'");
		sb.append(",(SELECT dbo.OFI_WebOptionName('isOperated',isOperated)) '經營狀況',sdate '停業起'");
		sb.append(",edate '停業迄',a.regi_capital '登記資本額',stockNum '股數',faceValue '面額',a.paid_capital '實收資本額'");
		sb.append(",shareholding '持有股數',shareholdingValue '持有股權/出資額',reinvestMoney '轉投資金額'");
		sb.append(",convert(nvarchar(20),convert(numeric(8,2),round(shareholdingRatio,2)))+'%' '持有股權比例'");
		sb.append(",m.item  '主要營業類別',m.itemname  '主要營業類別名稱',s.item  '次要營業類別',s.itemname  '次要營業類別名稱'");
		sb.append(",sp.item  '營業項目涉及特許及特殊項目',sp.itemname  '營業項目涉及特許及特殊項目名稱'");
		sb.append(",b.note '備註',f.reportyear '最新申報年度',f.reportdate '填報日期',f.CA '流動資產',f.SE '股東權益',f.asset '總資產',f.NI '營業收入淨額',");
		sb.append("f.cost '營業成本',f.expense '營業費用',f.NP '本期稅後損益',f.firm '事務所',f.accountant '會計師',");
		sb.append("f.fnote '財簽保留意見',f.note '備註',f.emptw '國內事業員工(台灣)',f.empforeign '國內事業員工(外國)',f.empcn '國內事業員工(大陸)' ");
		sb.append("FROM OFI_ReInvestXInvestNo b left join (select * FROM [moeaic].[dbo].[OFI_BASEDATA](null))ic ");
		sb.append("on ic.INVESTMENT_NO=b.investno join OFI_ReInvestmentList a on a.reInvestNo=b.reInvestNo ");
		sb.append("left join (SELECT * FROM OFI_GetALL_LatestReFinancial())f on a.reInvestNo =f.reinvestNo ");
		sb.append("left join [moeaic].[dbo].[COMTB932] c on c.COUNTY_NO=a.city left join ");
		sb.append("[moeaic].[dbo].[COMTB933] t on t.TOWN_NO=a.town left join ");
		sb.append("(SELECT * FROM OFI_GETALL_REIND('1'))m on a.reInvestNo=m.reInvestNo ");
		sb.append("left join (SELECT * FROM OFI_GETALL_REIND('2'))s on a.reInvestNo=s.reInvestNo ");
		sb.append("left join (SELECT * FROM OFI_GETALL_REIND('0'))sp on a.reInvestNo=sp.reInvestNo ");
		sb.append("where a.enable='1' and b.enable='1' ");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		sb.setLength(0);
		return forStmt;
	}
	public List<List<String>> getAllReinvestmentData(){
		List<List<String>> result = new ArrayList<List<String>>();
		SQL sqltool = new SQL();
		String forStmt = getAllReinvestmentDataSql();
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
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
	private Map<String,String> getInterviewoneItemMap(){
		SQL sqltool = new SQL();
		Map<String,String> result= new LinkedHashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		/*58是optionid=26的排序序號，如果修改序號僅跟著調整optionid=26的排序58*/
		sb.append("select optionId,cName from (select tmp.optionid,seq,optId,case when optId>=26 and optId<= 29 then seq+cName else cName end cName,");
		sb.append("case "
				+ "when optId>=26 and optId<= 29 then 58+0.01*convert(int,seq) "
				+ "when optId>=93 and optId<= 104 and optId!=103 then 37+0.001*convert(int,optId) " //--因為要擠在18跟80中間（nsort37跟38中間)，所以強制編號37.xxx
				+ "when optId=106 then 58+0.001*convert(int,optId) " //-- 107-07-03 新增關注項目
				+ "when optId=109 then 75+0.001*convert(int,optId) " //-- 107-07-05 新增營所稅營業收入業別
				+ "when optId>=107 and optId<=108 then 82+0.001*convert(int,optId) " //-- 107-07-05 新增投資經營管理甲類、乙類
				);
		sb.append("else nseq end nsort ");
		sb.append("from (select item.optionid,SUBSTRING(optionid,0,CHARINDEX('_',optionid)) seq");
		sb.append(",SUBSTRING(optionid,CHARINDEX('_',optionid)+1,LEN(optionid)) optId ");
		sb.append("from (SELECT distinct optionId FROM OFI_GETALL_ISDetail()) item ");
		sb.append(")tmp left join (SELECT optionId,cName,nseq FROM InterviewoneItem where ");
		sb.append("(qType='S' or qType='I') and enable='1') x on x.optionId=tmp.optId)y ");
		sb.append("order by y.nsort,y.optId");
		String forStmt = sb.toString();
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				result.put(rs.getString("optionId"),rs.getString("cName"));
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
	private String getInterviewoneDataSql(){
		Map<String,String> tmap=getInterviewoneItemMap();
		List<String> list=new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		int flag=0;
		for (Entry<String,String> m:tmap.entrySet()) {
			list.add(m.getKey());
			if(flag!=0){
				sb.append(",");
			}
			if(m.getValue().equals("財務概況來源")){
				sb.append("case when surveyStatus=1 then convert(nvarchar(3),convert(int,i.year)-1) else '' end '財務概況年度',");
			}
			sb.append("d.[").append(m.getKey()).append("] '").append(m.getValue()).append("'");
			flag=1;
		}
		String OptPvt=DataUtil.fmtPvtItem(list);
		String titleStr=sb.toString();
		sb.setLength(0);
		
		sb.append("SELECT i.investNo '陸資案號',case when i.reInvestNo=0 then ic.COMP_CHTNAME else r.COMP_CHTNAME end '國內事業名稱'");
		sb.append(",case when i.reInvestNo=0 then '' else Replace(cname,'(轉投資)','') end '轉投資事業名稱'");
		sb.append(",case when i.reInvestNo=0 then ic.BAN_NO else r.idno end '統一編號'");
		sb.append(",case when i.reInvestNo=0 then ic.OrgTypeName else r.orgType end'組織型態'");
		sb.append(",case when i.reInvestNo=0 then ic.issueTypeName else '' end'發行方式'");
		sb.append(",i.year '訪視年度',err.iError '訪視異常',err.fError '財務異常',");
		sb.append("(SELECT dbo.InterviewoneOptionName('interviewStatus',interviewStatus)) '訪視狀態',");
		sb.append("(SELECT dbo.InterviewoneOptionName('surveyStatus',surveyStatus)) '問卷狀態',");
		sb.append("(SELECT dbo.InterviewoneOptionName('following',isnull(f.following,''))) '最新追蹤情形',");
		sb.append("(SELECT dbo.InterviewoneOptionName('progress',isnull(f.progress,''))) '最新處理狀態',");
		sb.append(titleStr).append(" FROM Interviewone i left join (SELECT * FROM OFI_GETALL_ErrorByYear())err ");
		sb.append("on i.qNo=err.qNo left join ");
		sb.append("(select * FROM [moeaic].[dbo].[OFI_BASEDATA](null))ic on i.investNo=ic.INVESTMENT_NO ");
		sb.append("left join (select * FROM [isam].[dbo].ReInvestNoXBaseInfo(''))r on i.reInvestNo=r.reInvestNo ");
		sb.append("left join (select * from(SELECT * FROM OFI_GETALL_ISDetail())a PIVOT (max(value) for optionId in (");
		sb.append(OptPvt).append("))as p)d on d.qNo=i.qNo left join (select qNo,progress,following from (");
		sb.append("SELECT qNo,optionValue progress,following,ROW_NUMBER() over (partition by qNo order by receiveDate desc) seq ");
		sb.append("FROM InterviewoneManage where enable='1') x where x.seq='1') f ");
		sb.append("on f.qNo=i.qNo where i.enable='1'");
		String forStmt = sb.toString();
		sb.setLength(0);
		return forStmt;
	}
	public List<List<String>> getAllInterviewoneData(){
		List<List<String>> result = new ArrayList<List<String>>();
		SQL sqltool = new SQL();
		String forStmt = getInterviewoneDataSql();
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
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
	public List<List<String>> getAllAgentXReceiveNoData(){
		List<List<String>> result = new ArrayList<List<String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT IN_CHTNAME '投資人',IN_NATION_NAME '投資人國別',INVESTMENT_NO '陸資案號',COMP_CHTNAME '國內事業'");
		sb.append(",RESP_DATE '核准日期',RECEIVE_NO '收文文號',APPLICATION_NAME '案由',IN_AGENT '代理人' ");
		sb.append("FROM STAFN100 where (INVESTMENT_NO like '4%' or INVESTMENT_NO like '5%' ) and IN_AGENT!='' ");
		sb.append("order by IN_CHTNAME,INVESTMENT_NO");
		String forStmt = sb.toString();
		sb.setLength(0);
//		System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
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
	public List<List<String>> getAllOfficeData(){
		List<List<String>> result = new ArrayList<List<String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT COMP_CHTNAME '公司名稱',Ban_No '統一編號',Status '公司狀況',Location '辦事處所在地'");
		sb.append(",Setupdate '核准登記日期', sdate '停業日期(起)', edate '停業日期(迄)', Agent '在中華民國境內代表人' ");
		sb.append("FROM OFI_InvestOffice ");
		String forStmt = sb.toString();
		sb.setLength(0);
	//	System.out.println(forStmt);
		try {
			PreparedStatement stmt = sqltool.prepare(forStmt);
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
					sub.add(DataUtil.nulltoempty(rs.getString(i)).trim().replace("0000000", ""));
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
	/*ToPDF*/
	public Map<String,List<OFIPDFItem>> getPDFItemMap(){
		Map<String,List<OFIPDFItem>> result = new HashMap<String,List<OFIPDFItem>>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_InvestPDFItem order by classify,seq";
		PreparedStatement stmt =null;
		ResultSet rs=null;
		List<OFIPDFItem> sub;
		try {
			stmt = sqltool.prepare(forStmt);
			rs = stmt.executeQuery();
			while(rs.next()){
				String key=rs.getString("classify");
				OFIPDFItem bean=new OFIPDFItem();
				bean.setiNo(rs.getString("iNo"));
				bean.setClassify(key);
				bean.setItem(rs.getString("item"));
				bean.setName(rs.getString("name"));
				bean.setColspan(rs.getInt("colspan"));
				if(result.containsKey(key)){
					sub=result.get(key);
				}else{
					sub=new ArrayList<OFIPDFItem>();
				}
				sub.add(bean);
				result.put(key, sub);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	public Map<String,List<OFIPDFItem>> getPDFSubItemMap(){
		Map<String,List<OFIPDFItem>> result = new HashMap<String,List<OFIPDFItem>>();
		SQL sqltool = new SQL();
		String forStmt = "SELECT * FROM OFI_InvestPDFItemXSub order by iNo,seq";
		PreparedStatement stmt =null;
		ResultSet rs=null;
		List<OFIPDFItem> sub;
		try {
			stmt = sqltool.prepare(forStmt);
			rs = stmt.executeQuery();
			while(rs.next()){
				String key=rs.getString("iNo");
				OFIPDFItem bean=new OFIPDFItem();
				bean.setiNo(key);
				bean.setClassify(rs.getString("classify"));
				bean.setItem(rs.getString("item"));
				bean.setName(rs.getString("name"));
				bean.setColspan(rs.getInt("colspan"));
				if(result.containsKey(key)){
					sub=result.get(key);
				}else{
					sub=new ArrayList<OFIPDFItem>();
				}
				sub.add(bean);
				result.put(key, sub);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/*ToPDF Investment start*/
	public Map<String,String> getPDFBaseData(String investNo){
		Map<String,String> result = new HashMap<String,String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT a.investNo,COMP_CHTNAME cname,BAN_NO idno,OrgTypeName,issueTypeName,(SELECT dbo.OFI_WebOptionName('active',active)) active");
		sb.append(",(SELECT dbo.OFI_WebOptionName('isNew',isNew)) isNew,case when len(setupdate)=0 then '未填' else (select dbo.AddDateSlash(setupdate)) end setupdate");
		sb.append(",setupnote,case when len(approvaldate)=0 then '未填' else (select dbo.AddDateSlash(approvaldate)) end approvaldate");
		sb.append(",case when b.REGI_CAPITAL is null then '尚無資料' else (convert(nvarchar(30),cast(REGI_CAPITAL as money), 1)) end regiCapital");
		sb.append(",case when OrgTypeName='分公司' then '同登記資本額' when b.PAID_CAPITAL is null then '尚無資料' ");
		sb.append("else (convert(nvarchar(30),cast(PAID_CAPITAL as money), 1)) end paidCapital");
		sb.append(",case when CharIndex('股份有限',OrgTypeName)=0 then '無須填寫' when FACE_VALUE is null then '尚無資料' ");
		sb.append("else convert(nvarchar(10),cast(b.Face_value as money), 1) end faceValue");
		sb.append(",case when isOperated=2 then(SELECT dbo.OFI_WebOptionName('isOperated',isOperated))+'('+(select dbo.AddDateSlash(sdate)) +'~'+(select dbo.AddDateSlash(edate))+')' ");
		sb.append("else (SELECT dbo.OFI_WebOptionName('isOperated',isOperated)) end isOperated ");
		sb.append(",note,(select dbo.AddDateSlash(respdate)) respdate,receiveNo,(SELECT dbo.OFI_WebOptionName('isCNFDI',isCNFDI)) isCNFDI");
		sb.append(",CHARGE_PERSON owner,TEL_NO sysTel,isnull(county_name,'')+isnull(town_name,'')+isnull(address,'') sysAddr,isnull(src,'尚無資料') src ");
		sb.append("FROM OFI_InvestList a,(select * FROM moeaic.dbo.OFI_BASEDATA(?))b left join (SELECT * FROM OFI_GETALL_INSRC()where investNo=?)c ");
		sb.append("on b.INVESTMENT_NO=c.investNo where a.investNo=b.INVESTMENT_NO and a.enable='1'");
		String forStmt = sb.toString();
		sb.setLength(0);
		PreparedStatement stmt =null;
		ResultSet rs=null;
		try {
			stmt = sqltool.prepare(forStmt);
			stmt.setString(1,investNo);
			stmt.setString(2,investNo);
			rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				for(int i=1;i<=meta.getColumnCount();i++){
					result.put(meta.getColumnName(i), DataUtil.nulltoempty(rs.getString(i)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	public Map<String,String> getPDFContactData(String investNo){
		Map<String,String> result = new HashMap<String,String>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select (stuff(( select '、'+v from (SELECT investNo,name+'('+telNo+')' v ");
		sb.append("FROM OFI_Contacts where investNo=?)t2 where t2.investNo=t1.investNo for XML PATH('')),1,1,''))v ");
		sb.append("from (SELECT investNo,name+'('+telNo+')' v FROM OFI_Contacts where investNo=?)t1 group by investNo");
		String forStmt = sb.toString();
		sb.setLength(0);
		PreparedStatement stmt =null;
		ResultSet rs=null;
		try {
			stmt = sqltool.prepare(forStmt);
			stmt.setString(1,investNo);
			stmt.setString(2,investNo);
			rs = stmt.executeQuery();
			while(rs.next()){
				result.put("ofiContacts", rs.getString("v"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/*ToPDF Investment end*/
	/*ToPDF Investor start*/
	public Map<String,Map<String,String>> getPDFInvestorData(String investNo){
		Map<String,Map<String,String>> result = new HashMap<String,Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT INVESTOR_SEQ investorSeq,INVESTOR_CHTNAME+'('+(SELECT dbo.OFI_WebOptionName('nation',nation)) ");
		sb.append("+ISNULL((SELECT dbo.OFI_WebOptionName('cnCode',cnCode)),'')+')' investor,(select dbo.AddDateSlash(RESP_DATE)) respDate ");
		sb.append(",case when StockPercent>=0 and StockPercent<=1 then CONVERT(nvarchar(25),convert(numeric(18,2),round([StockPercent]*100,2)))+'%' ");
		sb.append("when StockPercent is null then '異常(尚無資料)' ");
		sb.append("else '異常('+CONVERT(nvarchar(100),convert(numeric(18,2),round([StockPercent]*100,2)))+'%)' end StockPercent ");
		sb.append(",isnull((SELECT dbo.FormatNumber(investvalue)),'尚無資料') MONEY2,");
		sb.append("isnull((SELECT dbo.FormatNumber(investedcapital)),'尚無資料') investvalue");
		sb.append(",note FROM OFI_InvestCase ic,OFI_InvestorList i ");
		sb.append("where INVE_ROLE_CODE=3 and investNo=?  and INVESTOR_SEQ=investorSeq");
		String forStmt = sb.toString();
		sb.setLength(0);
		PreparedStatement stmt =null;
		ResultSet rs=null;
		try {
			stmt = sqltool.prepare(forStmt);
			stmt.setString(1,investNo);
			rs = stmt.executeQuery();
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				String k=rs.getString("investorSeq");
				Map<String,String> sub=new HashMap<String,String>();
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.put(meta.getColumnName(i), DataUtil.nulltoempty(rs.getString(i)));
				}
				result.put(k, sub);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	public Map<String,Map<String,String>> getPDFInvestorXOtherData(String investNo){
		Map<String,Map<String,String>> result = new HashMap<String,Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (SELECT investorSeq,itemname,'BG1' type FROM OFI_GETALL_BG('BG1') ");
		sb.append("union all SELECT investorSeq,itemname,'BG2' type FROM OFI_GETALL_BG('BG2') union all ");
		sb.append("SELECT investorSeq,value,bgType FROM OFI_InvestorXBG where bgType like '%Note' union all ");
		sb.append("SELECT investorSeq,case when count(fNo)>0 then '有' else '無' end ,'framechart' FROM OFI_InvestorXFile ");
		sb.append("where enable='1' group by investorSeq ) bg where investorSeq in (SELECT INVESTOR_SEQ ");
		sb.append("from OFI_InvestCase where INVE_ROLE_CODE=3 and investNo=?)");
		String forStmt = sb.toString();
		sb.setLength(0);
		PreparedStatement stmt =null;
		ResultSet rs=null;
		try {
			stmt = sqltool.prepare(forStmt);
			stmt.setString(1,investNo);
			rs = stmt.executeQuery();
			Map<String,String> sub;
			while(rs.next()){
				String k=rs.getString("investorSeq");
				if(result.containsKey(k)){
					sub=result.get(k);
				}else{
					sub=new HashMap<String,String>();
				}
				sub.put(rs.getString("type"), rs.getString("itemname"));
				result.put(k, sub);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	public Map<String,List<List<String>>> getPDFCNInvestorXRelatedData(String investNo){
		Map<String,List<List<String>>> result = new HashMap<String,List<List<String>>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT investorSeq,relatedname 'aname',(SELECT dbo.OFI_WebOptionName('nation',nation))+");
		sb.append("ISNULL((SELECT dbo.OFI_WebOptionName('cnCode',cnCode)),'') nation ");
		sb.append("FROM OFI_InvestorXRelated where investorSeq in (SELECT INVESTOR_SEQ from OFI_InvestCase where INVE_ROLE_CODE=3 and investNo=?)");
		String forStmt = sb.toString();
		sb.setLength(0);
		PreparedStatement stmt =null;
		ResultSet rs=null;
		try {
			stmt = sqltool.prepare(forStmt);
			stmt.setString(1,investNo);
			rs = stmt.executeQuery();
			List<List<String>> list;
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				String k=rs.getString("investorSeq");
				if(result.containsKey(k)){
					list=result.get(k);
				}else{
					list=new ArrayList<List<String>>();
				}
				List<String> sub=new ArrayList<String>();
				for(int i=2;i<=meta.getColumnCount();i++){
					sub.add(DataUtil.nulltoempty(rs.getString(i)));
				}
				list.add(sub);
				result.put(k, list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	public Map<String,List<List<String>>> getPDFCNInvestorXAgentData(String investNo){
		Map<String,List<List<String>>> result = new HashMap<String,List<List<String>>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT INVESTOR_SEQ investorSeq,(select dbo.AddDateSlash(s.RESP_DATE)) RESP_DATE,RECEIVE_NO,APPLICATION_NAME");
		sb.append(",IN_AGENT,IN_AGENT_TEL_NO FROM STAFN100 s,OFI_InvestCase i where i.INVESTOR_CHTNAME=s.IN_CHTNAME and ");
		sb.append("(INVESTMENT_NO like '4%' or INVESTMENT_NO like '5%') and IN_AGENT!='' and INVESTMENT_NO=? ");
		sb.append("group by INVESTOR_SEQ,s.RESP_DATE,RECEIVE_NO,APPLICATION_NAME,IN_AGENT,IN_AGENT_TEL_NO order by s.RESP_DATE desc");
		String forStmt = sb.toString();
		sb.setLength(0);
		PreparedStatement stmt =null;
		ResultSet rs=null;
		try {
			stmt = sqltool.prepare(forStmt);
			stmt.setString(1,investNo);
			rs = stmt.executeQuery();
			List<List<String>> list;
			ResultSetMetaData meta=rs.getMetaData();
			while(rs.next()){
				List<String> sub=new ArrayList<String>();
				String k=rs.getString("investorSeq");
				if(result.containsKey(k)){
					list=result.get(k);
				}else{
					list=new ArrayList<List<String>>();
				}
				for(int i=2;i<=meta.getColumnCount();i++){
					sub.add(DataUtil.nulltoempty(rs.getString(i)));
				}
				list.add(sub);
				result.put(k, list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/*ToPDF Investor end*/
	/*ToPDF ReInvestment start*/
	public List<Map<String,String>> getReInvestmentBase(String investNo){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		SQL sqltool = new SQL();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT a.reInvestNo,reinvestment,idno,(SELECT dbo.OFI_WebOptionName('orgType',orgType)) OrgTypeName");
		sb.append(",isnull(c.COUNTY_NAME,'')+isnull(t.TOWN_NAME,'')+isnull(addr,'') rAddr,(SELECT dbo.OFI_WebOptionName('isNew',isNew)) isNew");
		sb.append(",case when len(setupdate)=0 then '未填' else (select dbo.AddDateSlash(setupdate)) end setupdate,setupnote");
		sb.append(",case when isOperated=2 then(SELECT dbo.OFI_WebOptionName('isOperated',isOperated))+'('+(select dbo.AddDateSlash(sdate))+'~'+ (select dbo.AddDateSlash(edate))+')' ");
		sb.append("else (SELECT dbo.OFI_WebOptionName('isOperated',isOperated)) end isOperated,isnull((SELECT dbo.FormatNumber(a.regi_capital)),'尚無資料') regiCapital ");
		sb.append(",case when orgType!=1 and orgType!=2 then '無須填寫' when stockNum is null then '尚無資料' else convert(nvarchar(20),cast(stockNum as money), 1) end regiShare ");
		sb.append(",case when orgType!=1 and orgType!=2 then '無須填寫' when faceValue is null then '尚無資料' else convert(nvarchar(10),cast(faceValue as money), 1) end faceValue ");
		sb.append(",case when orgType!=1 and orgType!=2 then '同登記資本額' else isnull((SELECT dbo.FormatNumber(PAID_CAPITAL)),'尚無資料') end paidCapital ");
		sb.append(",isnull((SELECT dbo.FormatNumber(shareholding)),'尚無資料') holdingShare,isnull((SELECT dbo.FormatNumber(shareholdingValue)),'尚無資料') contribution");
		sb.append(",isnull((SELECT dbo.FormatNumber(reinvestMoney)),'尚無資料') reinvestValue,case when (orgType!=1 and orgType!=2) and isnull(regi_capital,0)>0 ");
		sb.append("then (SELECT dbo.FormatNumber(convert(numeric(17,2),convert(float,Round((isnull(reinvestMoney,0)/regi_capital)*100,2)))))+'%' ");
		sb.append("when (orgType=1 or orgType=2) and isnull(PAID_CAPITAL,0)>0 then ");
		sb.append("(SELECT dbo.FormatNumber(convert(numeric(17,2),convert(float,Round(((isnull(shareholding,0)*isNull(faceValue,0))/PAID_CAPITAL)*100,2)))))+'%' ");
		sb.append("else '異常(無資料)' end holdingShareRatio,b.note FROM OFI_ReInvestXInvestNo b join OFI_ReInvestmentList a on a.reInvestNo=b.reInvestNo ");
		sb.append("left join moeaic.dbo.COMTB932 c on c.COUNTY_NO=a.city left join moeaic.dbo.COMTB933 t on t.TOWN_NO=a.town ");
		sb.append("where a.enable='1' and b.enable='1' and investNo=?");
		String forStmt = sb.toString();
		sb.setLength(0);
		PreparedStatement stmt =null;
		ResultSet rs=null;
		try {
			stmt = sqltool.prepare(forStmt);
			stmt.setString(1,investNo);
			rs = stmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();
			while(rs.next()){
				Map<String,String> sub=new HashMap<String,String>(); 
				for(int i=1;i<=meta.getColumnCount();i++){
					sub.put(meta.getColumnName(i), DataUtil.nulltoempty(rs.getString(i)));
				}
				result.add(sub);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try{
				sqltool.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/*ToPDF ReInvestment end*/
}
