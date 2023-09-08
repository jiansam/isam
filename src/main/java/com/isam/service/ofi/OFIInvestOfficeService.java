package com.isam.service.ofi;

import java.util.List;
import java.util.Map;

import com.isam.bean.OFIInvestList;
import com.isam.bean.OFIInvestOffice;
import com.isam.dao.ofi.OFIInvestListDAO;
import com.isam.dao.ofi.OFIInvestOfficeDAO;

public class OFIInvestOfficeService{
	OFIInvestOfficeDAO dao = null;
	public OFIInvestOfficeService(){
		dao = new OFIInvestOfficeDAO();
	}

	public OFIInvestOffice select(String investNo){
		return dao.select(investNo);
	}
	public List<Map<String,String>> select(Map<String,String> terms){
		return dao.select(terms);
	}
	public int update(OFIInvestOffice bean) {
		return dao.update(bean);
	}
} 
