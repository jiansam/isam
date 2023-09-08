package com.isam.service.ofi;

import java.util.Map;

import com.isam.dao.ofi.OFIDepartmentDAO;

public class OFIDepartmentService{
	OFIDepartmentDAO dao = null;
	public OFIDepartmentService(){
		dao = new OFIDepartmentDAO();
	}
	public Map<String,String> getCodeNameMap(){
		return dao.getCodeNameMap();
	}
	
} 
