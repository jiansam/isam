package com.isam.dao.ofi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestBaseData;
import com.isam.bean.OFIInvestorXBG;
import com.isam.bean.OFIInvestorXRelated;
import com.isam.helper.PairHashtable;
import com.isam.helper.SQL;

public class OFIInvestorExcelDAO
{
	
	public static Map<String, List<OFIInvestorXRelated>> getRelateds(List<String> investors) //取出所有投資人的母公司資訊
	{
		Map<String, List<OFIInvestorXRelated>> result = null;
		SQL sqltools = new SQL();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = sqltools.prepare("SELECT investorSeq, relatedname, nation, cnCode FROM OFI_InvestorXRelated ORDER BY investorSeq, serno");
			rs = pstmt.executeQuery();
			result = new HashMap<>();
			while(rs.next()){

				String investorSeq = rs.getString("investorSeq");
				if(! investors.contains(investorSeq)){
					continue;
				}
				
				if(result.get(investorSeq) == null){
					result.put(investorSeq, new ArrayList<OFIInvestorXRelated>());
				}
				
				OFIInvestorXRelated bean = new OFIInvestorXRelated();
				bean.setInvestorSeq(investorSeq);
				bean.setRelatedname(rs.getString("relatedname"));
				bean.setNation(rs.getString("nation"));
				bean.setCnCode(rs.getString("cnCode"));
				result.get(investorSeq).add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				sqltools.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;	
	}

	public static Map<String, List<OFIInvestorXBG>> getBGs(List<String> investors) //取出所有投資人的背景資訊
	{
		Map<String, List<OFIInvestorXBG>> result = null;
		SQL sqltools = new SQL();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = sqltools.prepare("SELECT investorSeq, bgType, value, seq FROM OFI_InvestorXBG "
								   + "ORDER BY investorSeq, bgType, seq");
			rs = pstmt.executeQuery();
			result = new HashMap<>();
			while(rs.next()){

				String investorSeq = rs.getString("investorSeq");
				if(! investors.contains(investorSeq)){
					continue;
				}
				
				if(result.get(investorSeq) == null){
					result.put(investorSeq, new ArrayList<OFIInvestorXBG>());
				}
				
				OFIInvestorXBG bean = new OFIInvestorXBG();
				bean.setInvestorSeq(investorSeq);
				bean.setBgType(rs.getString("bgType"));
				bean.setValue(rs.getString("value"));
				bean.setSeq(rs.getShort("seq"));
				result.get(investorSeq).add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				sqltools.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;	
	}

	public static Map<String, List<String>> getFiles(List<String> investors) //取出所有投資人的架構圖ID
	{
		Map<String, List<String>> result = null;
		SQL sqltools = new SQL();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = sqltools.prepare("SELECT fNo,investorSeq FROM OFI_InvestorXFile where enable='1' ORDER BY investorSeq");
			rs = pstmt.executeQuery();
			result = new HashMap<>();
			while(rs.next()){

				String investorSeq = rs.getString("investorSeq");
				if(! investors.contains(investorSeq)){
					continue;
				}
				
				if(result.get(investorSeq) == null){
					result.put(investorSeq, new ArrayList<String>());
				}
				
				result.get(investorSeq).add(rs.getString("fNo"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				sqltools.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;	
	}
	
	public static PairHashtable<String, String, OFIInvestBaseData> getInvestDatas(List<String> investors) //取出所有投資人的投資案資料
	{
		PairHashtable<String, String, OFIInvestBaseData> result = null;
		SQL sqltools = new SQL();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = sqltools.prepare(
					  "SELECT a.investNo, b.COMP_CHTNAME, a.investorSeq,  b.INVESTOR_CHTNAME, "
					+ "isnull(b.BAN_NO,'') AS ID_NO, b.REGI_CAPITAL, b.PAID_CAPITAL, b.FACE_VALUE, "
					+ "b.investvalue, b.investedcapital, convert(decimal(18,2),(b.StockPercent*100)) sp "
					+ "FROM OFI_InvestCaseList a, OFI_InvestCase b "
					+ "where a.investNo=b.investNo and a.investorSeq=b.INVESTOR_SEQ and a.enable='1' ");
			rs = pstmt.executeQuery();
			result = new PairHashtable<>();
			while(rs.next()){

				String investorSeq = rs.getString("investorSeq");
				if(! investors.contains(investorSeq)){
					continue;
				}
				
				OFIInvestBaseData bean = new OFIInvestBaseData();
				bean.setInvestNo(rs.getString("investNo"));
				bean.setCOMP_CHTNAME( rs.getString("COMP_CHTNAME"));
				bean.setInvestorSeq(investorSeq);
				bean.setINVESTOR_CHTNAME(rs.getString("iNVESTOR_CHTNAME"));
				bean.setID_NO(rs.getString("iD_NO"));
				bean.setREGI_CAPITAL(rs.getString("REGI_CAPITAL"));
				bean.setPAID_CAPITAL(rs.getString("PAID_CAPITAL"));
				bean.setFACE_VALUE(rs.getString("FACE_VALUE"));
				bean.setInvestvalue(rs.getString("investvalue"));
				bean.setInvestedcapital(rs.getString("investedcapital"));
				bean.setSp(rs.getString("sp"));
				
				result.put(investorSeq, bean.getInvestNo(), bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				sqltools.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;	
	}
	
	
	
	public static Map<String, OFIInvestBaseData> getMoeaicDatas(List<String> investNos) //取出所有陸資案號的組織型態、發行方式
	{
		Map<String, OFIInvestBaseData> result = null;
		SQL sqltools = new SQL("jdbc/sqlMoeaic");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = sqltools.prepare(
					  "SELECT a.INVESTMENT_NO, a.COMP_CHTNAME AS cname, "
					+ "case when SUBSTRING(a.INVESTMENT_NO, 1, 1)=4 "
					+ " then '分公司' "
					+ " else (SELECT [moeaic].[dbo].[OFI_OrgTypeName] (a.ORG_TYPE_CODE,a.ISSUE_TYPE_CODE)) "
					+ " end orgTypeName, "
					+ "OFITB904.ISSUE_TYPE_NAME "
					+ "from (SELECT OFITB102.INVESTMENT_NO, OFITB202.COMP_CHTNAME, OFITB202.BAN_NO, "
					+ "			OFITB202.ORG_TYPE_CODE, OFITB202.ISSUE_TYPE_CODE, OFITB202.REGI_CAPITAL, "
					+ "			OFITB202.PAID_CAPITAL, OFITB202.FACE_VALUE"
					+ "		 FROM OFITB101,OFITB102,OFITB201,OFITB202 "
					+ "		 where OFITB101.SEND_STATUS_CODE='04' and OFITB101.RESP_RESULT='Y' "
					+ "		 and SUBSTRING(OFITB101.RECEIVE_NO, 1, 1) <> '9' "
					+ "		 and (OFITB102.INVESTMENT_NO like '4%' or OFITB102.INVESTMENT_NO like '5%') "
					+ "		 and OFITB101.RECEIVE_NO=OFITB102.RECEIVE_NO "
					+ "		 and OFITB102.INVESTMENT_NO=OFITB201.INVESTMENT_NO "
					+ "		 and OFITB201.COMP_CHTNAME=OFITB202.COMP_CHTNAME "
					+ "		 and OFITB201.COMP_CHTNAME in( "
					+ "				SELECT OFITB202.[COMP_CHTNAME] "
					+ "				FROM [moeaic].[dbo].[OFITB201],[moeaic].[dbo].[OFITB202] "
					+ "				where OFITB201.COMP_CHTNAME=OFITB202.COMP_CHTNAME "
					+ "				and OFITB201.INVESTMENT_NO = isnull(INVESTMENT_NO,OFITB201.INVESTMENT_NO) "
					+ "				)"
					+ "		 group by OFITB202.COMP_CHTNAME, OFITB202.ORG_TYPE_CODE, OFITB202.ISSUE_TYPE_CODE, "
					+ "		 OFITB202.BAN_NO, OFITB202.REGI_CAPITAL, OFITB202.PAID_CAPITAL, OFITB202.FACE_VALUE, "
					+ "		 OFITB202.CHARGE_PERSON, OFITB202.TEL_NO, OFITB202.COUNTY_NO, OFITB202.TOWN_NO, "
					+ "		 OFITB202.ADDRESS, OFITB102.INVESTMENT_NO "
					+ "		) a "
					+ "left join OFITB904 on OFITB904.ISSUE_TYPE_CODE=a.ISSUE_TYPE_CODE ");
			rs = pstmt.executeQuery();
			result = new HashMap<>();
			while(rs.next()){

				String INVESTMENT_NO = rs.getString("INVESTMENT_NO");
				if(! investNos.contains(INVESTMENT_NO)){
					continue;
				}
				
				OFIInvestBaseData bean = new OFIInvestBaseData();
				bean.setInvestNo(INVESTMENT_NO);
				bean.setOrgTypeName(rs.getString("orgTypeName"));
				bean.setISSUE_TYPE_NAME(rs.getString("ISSUE_TYPE_NAME"));
				
				result.put(INVESTMENT_NO, bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt != null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				sqltools.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;	
	}
}
