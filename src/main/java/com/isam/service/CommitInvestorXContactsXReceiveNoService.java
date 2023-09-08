package com.isam.service;

import java.util.List;

import com.isam.dao.CommitInvestorXContactsXReceiveNoDAO;

public class CommitInvestorXContactsXReceiveNoService {
	
	CommitInvestorXContactsXReceiveNoDAO dao = null;
	public CommitInvestorXContactsXReceiveNoService(){
		dao=new CommitInvestorXContactsXReceiveNoDAO();
	}
	public List<String> select(String cid,String idno){
		return dao.select(cid, idno);
	}
	public void insert(String cid,List<String> list) {
		delete(cid);
		dao.insert(cid,list);
	}
	public int delete(String cid) {
		return dao.delete(cid);
	}
}
