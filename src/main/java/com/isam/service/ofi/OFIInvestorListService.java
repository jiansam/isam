package com.isam.service.ofi;

import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestorList;
import com.isam.dao.ofi.OFIInvestorListDAO;

public class OFIInvestorListService{
	OFIInvestorListDAO dao = null;
	public OFIInvestorListService(){
		dao = new OFIInvestorListDAO();
	}
	public List<Map<String,String>> select(Map<String,String> terms){
		return dao.select(terms);
	}
	public OFIInvestorList select(String investorSeq){
		return dao.select(investorSeq);
	}
	public void update(OFIInvestorList bean) {
		dao.update(bean);
	}
} 
