package com.isam.service;

import java.util.List;
import java.util.Map;

import com.isam.bean.CommitInvestorXContacts;
import com.isam.dao.CommitInvestorXContactsDAO;

public class CommitInvestorXContactsService {
	
	CommitInvestorXContactsDAO dao = null;
	public CommitInvestorXContactsService(){
		dao = new CommitInvestorXContactsDAO();
	}
	public List<CommitInvestorXContacts> select(String IDNO){
		return dao.select(IDNO);
	}
	public CommitInvestorXContacts selectByCID(String cid){
		return dao.selectByCID(cid);
	}
	public Map<String,String> getReceiveNoStr(String IDNO){
		return dao.getReceiveNoStr(IDNO);
	}
	public String insert(CommitInvestorXContacts bean) {
		return dao.insert(bean);
	}
	public int update(CommitInvestorXContacts bean) {
		return dao.update(bean);
	}
	public int delete(String cid) {
		return dao.delete(cid);
	}
}
