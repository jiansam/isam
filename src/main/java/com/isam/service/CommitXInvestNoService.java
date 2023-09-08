package com.isam.service;

import java.util.List;

import org.json.simple.JSONArray;

import com.isam.bean.CommitXInvestNo;
import com.isam.dao.CommitXInvestNoDAO;

public class CommitXInvestNoService {
	CommitXInvestNoDAO dao = null;
	public CommitXInvestNoService(){
		dao = new CommitXInvestNoDAO();
	}
	public JSONArray getJsonFmt(String serno){
		return dao.getJsonFmt(serno);
	}
	public List<CommitXInvestNo> select(String serno){
		return dao.select(serno);
	}
	public int insert(CommitXInvestNo bean) {
		return dao.insert(bean);
	}
	public void insert(List<CommitXInvestNo> beans) {
		dao.insert(beans);
	}
	public void delete(int serno){
		dao.delete(serno);
	}
}
