package com.isam.service;

import java.util.List;
import java.util.Map;

import com.isam.bean.SubCommit;
import com.isam.dao.SubCommitDAO;

public class SubCommitService {
	SubCommitDAO dao = null;
	public SubCommitService(){
		dao = new SubCommitDAO();
	}
	public List<Map<String,String>> getInvestNOXSubList(String serno,String type){
		return dao.getInvestNOXSubList(serno,type);
	}
	public SubCommit select(String serno,String investNo,String subserno){
		return dao.select(serno, investNo, subserno);
	}
	public SubCommit select(String subserno){
		return dao.select(subserno);
	}
	public int checkYearRange(String subserno,String serno,String investNo,String type,String startYear,String endYear){
		return dao.checkYearRange(subserno, serno, investNo, type, startYear, endYear);
	}
	public int checkYearRange(String serno,String investNo,String type,String startYear,String endYear){
		return dao.checkYearRange(serno, investNo, type, startYear, endYear);
	}
	public int insert(SubCommit bean) {
		return dao.insert(bean);
	}
	public int update(SubCommit bean) {
		return dao.update(bean);
	}
	public int unable(String subserno) {
		return dao.unable(subserno);
	}
	public int unableAllSub(String serno) {
		return dao.unableAllSub(serno);
	}
	public void deleteAllSub(String serno) {
		dao.deleteAllSub(serno);
	}
	public int updateNeedAlert(String subserno,String needAlert) {
		return dao.updateNeedAlert(subserno, needAlert);
	}
}
