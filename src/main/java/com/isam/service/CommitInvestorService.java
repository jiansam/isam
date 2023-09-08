package com.isam.service;

import java.util.List;

import com.isam.bean.CommitInvestor;
import com.isam.dao.CommitInvestorDAO;

public class CommitInvestorService {
	
	CommitInvestorDAO dao = null;
	public CommitInvestorService(){
		dao = new CommitInvestorDAO();
	}
	public List<CommitInvestor> getSearchResult(String investor,String IDNO,String type,String from,String to,String needAlert){
		return dao.getSearchResult(investor, IDNO, type, from, to, needAlert);
	}
	public List<CommitInvestor> select(){
		return dao.select(null, null);
	}
	public CommitInvestor select(String IDNO){
		List<CommitInvestor> list=dao.select(IDNO, null);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}
	public boolean isExists(String IDNO){
		boolean result=false;
		if(dao.select(IDNO, null).size()>0){
			result=true;
		}
		return result;
	}
	public int insert(CommitInvestor bean) {
		return dao.insert(bean);
	}
	public int update(CommitInvestor bean) {
		return dao.update(bean);
	}
	
	public int updateNeedAlert(String idno,String needAlert) {
		return dao.updateNeedAlert(idno, needAlert);
	}
}
