package com.isam.service.ofi;

import java.util.List;

import com.isam.bean.OFIInvestorXRelated;
import com.isam.dao.ofi.OFIInvestorXRelatedDAO;

public class OFIInvestorXRelatedService{
	OFIInvestorXRelatedDAO dao = null;
	public OFIInvestorXRelatedService(){
		dao = new OFIInvestorXRelatedDAO();
		
	}
	public List<OFIInvestorXRelated> select(String investorSeq){
		return dao.select(investorSeq);
	}
	public OFIInvestorXRelated selectBySerno(String serno){
		return dao.selectBySerno(serno);
	}
	public void delete(String serno){
		dao.delete(serno);
	}
	public void insert(OFIInvestorXRelated bean) {
		dao.insert(bean);
	}
	public void update(OFIInvestorXRelated bean) {
		dao.update(bean);
	}
} 
