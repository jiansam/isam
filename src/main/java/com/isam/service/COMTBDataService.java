package com.isam.service;

import java.util.Map;

import com.isam.dao.COMTBDataDAO;

public class COMTBDataService extends ProjectKeyHelp{
	COMTBDataDAO dao = null;
	public COMTBDataService(){
		dao = new COMTBDataDAO();
	}
	public Map<Integer,Map<String,String>> getTWADDRCode(){
		return dao.getTWADDRCode();
	}
} 
