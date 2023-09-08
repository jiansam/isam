package com.isam.service.ofi;

import java.util.List;
import java.util.Map;

import com.isam.dao.ofi.OFIManageClassifyDAO;

public class OFIManageClassifyService {
	OFIManageClassifyDAO dao = null;
	public OFIManageClassifyService(){
		dao = new OFIManageClassifyDAO();
	}
	public  Map<String,Map<String,String>> select(String investNo){
		return dao.select(investNo);
	}
	public int update(String year,String investNo,String nclassify,String updateuser) {
		return dao.update(year, investNo, nclassify, updateuser);
	}
	public List<List<String>> getMSReport(){
		return dao.getMSReport();
	}
}
