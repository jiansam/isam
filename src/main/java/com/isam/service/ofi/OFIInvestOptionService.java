package com.isam.service.ofi;

import java.util.Map;

import com.isam.dao.ofi.OFIInvestOptionDAO;

public class OFIInvestOptionService{
	OFIInvestOptionDAO dao = null;
	public OFIInvestOptionService(){
		dao = new OFIInvestOptionDAO();
	}
	public Map<String,Map<String,String>> select(){
		return dao.select();
	}
	
} 
