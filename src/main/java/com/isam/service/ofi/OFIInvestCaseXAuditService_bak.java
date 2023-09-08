package com.isam.service.ofi;

import java.util.Map;

import com.isam.dao.ofi.OFIInvestCaseXAuditDAO_bak;

public class OFIInvestCaseXAuditService_bak{
	OFIInvestCaseXAuditDAO_bak dao = null;
	public OFIInvestCaseXAuditService_bak(){
		dao = new OFIInvestCaseXAuditDAO_bak();
	}
	public Map<String,Map<String,String>> select(String investorSeq){
		return dao.select(investorSeq);
	}
	public String getSPNeed(String investNo){
		return dao.getSPNeed(investNo);
	}
} 
