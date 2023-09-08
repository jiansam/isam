package com.isam.service;

import java.util.List;
import java.util.Map;

import com.isam.bean.CommitDetail;
import com.isam.dao.SubCommitDetailDAO;

public class SubCommitDetailService {
	SubCommitDetailDAO dao = null;
	public SubCommitDetailService(){
		dao = new SubCommitDetailDAO();
	}
	public List<CommitDetail> select(String subserno){
		return dao.select(subserno);
	}
	public Map<String,List<CommitDetail>> getDetail(String subserno){
		return dao.getDetail(subserno);
	}
	public Map<String,Map<String,String>> selectWithoutTotal(String serno,String type){
		return dao.selectWithoutTotal(serno, type);
	}
	public Map<String,Map<String,Double>> getDetailSummary(String serno,String type){
		return dao.getDetailSummary(serno, type);
	}
	public void insert(List<CommitDetail> beans) {
		dao.insert(beans);
	}
	public void delete(String subserno){
		dao.delete(subserno);
	}
	public int unable(String subserno) {
		return dao.unable(subserno);
	}
}
