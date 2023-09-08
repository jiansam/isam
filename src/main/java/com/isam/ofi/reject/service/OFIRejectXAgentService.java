package com.isam.ofi.reject.service;

import java.util.List;

import com.isam.ofi.reject.bean.OFIRejectXAgent;
import com.isam.ofi.reject.dao.OFIRejectXAgentDAO;

public class OFIRejectXAgentService {
	OFIRejectXAgentDAO dao = null;
	public OFIRejectXAgentService(){
		dao = new OFIRejectXAgentDAO();
	}
	
	public List<OFIRejectXAgent>  select(String applyNo) {
		return dao.select(applyNo);
	}
	public void insert(List<OFIRejectXAgent> beans) {
		 dao.insert(beans);
	}
	public void delete(String applyNo) {
		dao.delete(applyNo);
	}
	public void deleteBySerno(String serno) {
		dao.deleteBySerno(serno);
	}
}
