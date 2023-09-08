package com.isam.ofi.reject.service;

import java.util.List;

import com.isam.ofi.reject.bean.OFIRejectXTWSIC;
import com.isam.ofi.reject.dao.OFIRejectXTWSICDAO;

public class OFIRejectXTWSICService {
	OFIRejectXTWSICDAO dao = null;
	public OFIRejectXTWSICService(){
		dao = new OFIRejectXTWSICDAO();
	}
	public String select(String serno){
		return dao.select(serno);
	}
	public void delete(String serno){
		dao.delete(serno);
	}
	public void insert(List<OFIRejectXTWSIC> beans) {
		dao.insert(beans);
	}
}
