package com.isam.service.ofi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.OFIAuditOption;
import com.isam.dao.ofi.OFIAuditOptionDAO;

public class OFIAuditOptionService{
	OFIAuditOptionDAO dao = null;
	public OFIAuditOptionService(){
		dao = new OFIAuditOptionDAO();
	}
	public List<OFIAuditOption> getAuditOption(){
		return dao.getAuditOption();
	}
	public Map<String, String> getAuditOptionMap() {
		Map<String, String> map =new HashMap<String,String>();
		List<OFIAuditOption> list=this.getAuditOption();
		for (int i = 0; i < list.size(); i++) {
			map.put(list.get(i).getAuditCode(), list.get(i).getDescription());
		}
		return map;
	}
} 
