package com.isam.service;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;

import com.isam.bean.CommitDetail;
import com.isam.dao.CommitDetailDAO;

public class CommitDetailService {
	CommitDetailDAO dao = null;
	public CommitDetailService(){
		dao = new CommitDetailDAO();
	}
	
	public JSONArray getJsonFmt(String serno){
		return dao.getJsonFmt(serno);
	}
	public List<CommitDetail> select(String serno){
		return dao.select(serno);
	}
	public Map<String,CommitDetail> selectOriginalTT(String serno){
		return dao.selectOriginalTT(serno);
	}
	public List<List<String>> select(String serno,String type){
		return dao.select(serno, type);
	}
	public Map<String,String> selectTotalValue(String serno){
		return dao.selectTotalValue(serno);
	}
	public Map<String,Map<String,String>> selectWithoutTotal(String serno,String type){
		return dao.selectWithoutTotal(serno, type);
	}
	public List<List<String>> select02ByIdno(String idno){
		return dao.select02ByIdno(idno);
	}
	public void insert(List<CommitDetail> beans) {
		dao.insert(beans);
	}
	public void delete(int serno){
		dao.delete(serno);
	}
	public int unable(String serno) {
		return dao.unable(serno);
	}
	public boolean checkAccPt(String serno){
		return dao.checkAccPt(serno);
	}
}
