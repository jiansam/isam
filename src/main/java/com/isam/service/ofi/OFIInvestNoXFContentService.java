package com.isam.service.ofi;

import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestNoXFContent;
import com.isam.dao.ofi.OFIInvestNoXFContentDAO;

public class OFIInvestNoXFContentService{
	OFIInvestNoXFContentDAO dao = null;
	public OFIInvestNoXFContentService(){
		dao = new OFIInvestNoXFContentDAO();
	}
	public void delete(String serno){
		dao.delete(serno);
	}
	public void insert(List<OFIInvestNoXFContent> beans) {
		dao.insert(beans);
	}
	public Map<String,String> selectBySerno(String serno){
		return dao.selectBySerno(serno);
	}
} 
