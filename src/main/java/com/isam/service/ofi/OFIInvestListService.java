package com.isam.service.ofi;

import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestList;
import com.isam.dao.ofi.OFIInvestListDAO;

public class OFIInvestListService{
	OFIInvestListDAO dao = null;
	public OFIInvestListService(){
		dao = new OFIInvestListDAO();
	}
	public Map<String, String> getYearRange(){
		return dao.getYearRange();
	}
	public OFIInvestList select(String investNo){
		return dao.select(investNo);
	}
	public List<Map<String,String>> select(Map<String,String> terms){
		return dao.select(terms);
	}
	public int update(OFIInvestList bean) {
		return dao.update(bean);
	}
} 
