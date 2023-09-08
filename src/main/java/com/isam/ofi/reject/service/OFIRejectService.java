package com.isam.ofi.reject.service;

import java.util.List;
import java.util.Map;

import com.isam.ofi.reject.bean.OFIReject;
import com.isam.ofi.reject.dao.OFIRejectDAO;

public class OFIRejectService {
	OFIRejectDAO dao = null;
	public OFIRejectService(){
		dao = new OFIRejectDAO();
	}
	public Map<String,String> getMAXMINDay(){
		return dao.getMAXMINDay();
	}
	public List<Map<String,String>> getRejectsList(Map<String,String> terms){
		return dao.getRejectsList(terms);
	}
	public List<Map<String,String>> getRejectsCounts(Map<String,String> terms){
		return dao.getRejectsCounts(terms);
	}
	public Map<String,List<List<String>>> getRejectContext(Map<String,String> terms){
		return dao.getRejectContext(terms);
	}
	public OFIReject select(String serno){
		return dao.select(serno);
	}
	public String insert(OFIReject bean) {
		return dao.insert(bean);
	}
	public void update(OFIReject bean) {
		dao.update(bean);
	}
	public void delete(String serno) {
		dao.delete(serno);
	}
	public void mergeCNo(OFIReject bean,String cname,String cNo) {
		dao.mergeCNo(bean, cname,cNo);
	}
}
