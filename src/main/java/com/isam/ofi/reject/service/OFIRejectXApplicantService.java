package com.isam.ofi.reject.service;

import java.util.List;
import java.util.Map;

import com.isam.ofi.reject.bean.OFIRejectXApplicant;
import com.isam.ofi.reject.dao.OFIRejectXApplicantDAO;

public class OFIRejectXApplicantService {
	OFIRejectXApplicantDAO dao = null;
	public OFIRejectXApplicantService(){
		dao = new OFIRejectXApplicantDAO();
	}
	public List<Map<String,String>> getRejectApplicant(String serno){
		return dao.getRejectApplicant(serno,"1");
	}
	public List<Map<String,String>> getRejectApplicant(String serno,String enable){
		return dao.getRejectApplicant(serno,enable);
	}
	public OFIRejectXApplicant select(String applyNo) {
		return dao.select(applyNo);
	}
	public String insert(OFIRejectXApplicant bean) {
		return dao.insert(bean);
	}
	public void update(OFIRejectXApplicant bean) {
		dao.update(bean);
	}
	public void delete(String applyNo) {
		dao.delete(applyNo);
	}
	public void deleteBySerno(String serno) {
		dao.deleteBySerno(serno);
	}
}
