package com.isam.service.ofi;

import java.util.Map;

import com.isam.dao.ofi.OFIMoeaicDataDAO;

public class OFIMoeaicDataService{
	OFIMoeaicDataDAO dao = null;
	public OFIMoeaicDataService(){
		dao = new OFIMoeaicDataDAO();
	}
	public Map<String,String> getSysBaseInfo(String investNo){
		return dao.getSysBaseInfo(investNo);
	}
} 
