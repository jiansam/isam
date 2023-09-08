package com.isam.service.ofi;

import java.util.List;
import java.util.Map;

import com.isam.bean.OFIContacts;
import com.isam.dao.ofi.OFIContactsDAO;

public class OFIContactsService{
	OFIContactsDAO dao = null;
	public OFIContactsService(){
		dao = new OFIContactsDAO();
	}
	public Map<String,List<OFIContacts>> getContacts(String investorSeq){
		return dao.getContacts(investorSeq);
	}
	public List<OFIContacts> select(String investNo){
		return dao.select(investNo);
	}
	public OFIContacts selectbean(String serno){
		return dao.selectbean(serno);
	}
	public void insert(OFIContacts bean) {
		dao.insert(bean);
	}
	public void update(OFIContacts bean) {
		dao.update(bean);
	}
	public void delete(String serno){
		dao.delete(serno);
	}
} 
