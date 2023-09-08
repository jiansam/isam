package com.isam.service.ofi;

import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestorList;
import com.isam.dao.ofi.OFIInvestCaseDAO;

public class OFIInvestCaseService{
	OFIInvestCaseDAO dao = null;
	public OFIInvestCaseService(){
		dao = new OFIInvestCaseDAO();
	}
	/*OFI_InvestCase*/
	public List<Map<String,String>> getInvestcase(String investorSeq){
		return dao.getInvestcase(investorSeq,null);
	}
	public List<Map<String,String>> getInvestcase(String investorSeq,String caseNo){
		return dao.getInvestcase(investorSeq,caseNo);
	}
	public List<Map<String,String>> select(String investNo){
		return dao.select(investNo);
	}
	public List<Map<String,String>> summary(String investNo){
		return dao.summary(investNo);
	}
	public void updateIsFilled(String investorSeq,String investNo,String username) {
		dao.updateIsFilled(investorSeq, investNo, username);
	}
	public void updateInvestInfo(String investNo, String money1, String money2, String stockImp) {
		dao.updateInvestInfo(investNo, money1, money2, stockImp);
	}
	public List<List<String>> getAgentInfos(String investorSeq){
		return dao.getAgentInfos(investorSeq);
	}
	/*計算事後管理注意事項表-有無*/
	public Map<String,String> getRemarkCount(String investNo){
		return dao.getRemarkCount(investNo);
	}
//	public Map<String,Map<String,String>> getInvestorNameSp(String investNo,String rolecode){
//		return dao.getInvestorNameSp(investNo,rolecode);
//	}
	public Map<String,Map<String,String>> getCNInvestorNameSp(String investNo){
		return dao.getInvestorNameSp(investNo,"3");
	}
	public String getInvestName(String investNo){
		return dao.getInvestName(investNo);
	}
	public String investorSrc(String investNo){
		return dao.investorSrc(investNo);
	}
	public double getNowInvestvalue(String investNo){
		return dao.getNowInvestvalue(investNo);
	}
	public String isSignificantInvest(String investNo){
		String result="0";
		if(this.getNowInvestvalue(investNo)>=500000000){
			result= "1";
		}
		return result;
	}
	/*OFI_InvestCaseList*/
	public List<Map<String,String>> select(Map<String,String> terms){
		return dao.select(terms);
	}
	/*OFI_Agent*/
	public List<Map<String,String>> getAgents(String investNo){
		return dao.getAgents(investNo);
	}
	public Map<String,Map<String,String>> getAgent(String investorSeq){
		return dao.getAgent(investorSeq);
	}
	/*OFI_OFI_InvestCaseXContacts*/
//	public Map<String,List<Map<String,String>>> getContacts(String investorSeq){
//		return dao.getContacts(investorSeq);
//	}
} 
