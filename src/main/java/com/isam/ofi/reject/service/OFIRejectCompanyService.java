package com.isam.ofi.reject.service;

import java.util.List;

import com.isam.ofi.reject.bean.OFIRejectCompany;
import com.isam.ofi.reject.dao.OFIRejectCompanyDAO;

public class OFIRejectCompanyService {
	OFIRejectCompanyDAO dao = null;
	public OFIRejectCompanyService(){
		dao = new OFIRejectCompanyDAO();
	}
	public List<String> getCNoListByCName(String cName,String cNo){
		return dao.getCNoListByCName(cName,cNo);
	}
	public String getCNoByCName(String cName){
		return dao.getCNoByCName(cName);
	}
	public OFIRejectCompany select(String cNo){
		return dao.select(cNo);
	}
	public List<OFIRejectCompany> getCNameList(String cName,String idno){
		return dao.getCNameList(cName, idno);
	}
	public String insert(OFIRejectCompany bean) {
		return dao.insert(bean);
	}
	public void update(OFIRejectCompany bean) {
		dao.update(bean);
	}
	public void mergeCNo(OFIRejectCompany bean) {
		dao.unable(bean);
	}
}
