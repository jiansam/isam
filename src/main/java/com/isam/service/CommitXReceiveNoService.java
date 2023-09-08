package com.isam.service;

import java.util.List;

import org.json.simple.JSONArray;

import com.isam.bean.CommitXReceiveNo;
import com.isam.dao.CommitXReceiveNoDAO;

public class CommitXReceiveNoService {
	CommitXReceiveNoDAO dao = null;
	public CommitXReceiveNoService(){
		dao = new CommitXReceiveNoDAO();
	}
	public JSONArray getJsonFmt(String serno){
		return dao.getJsonFmt(serno);
	}
	public List<CommitXReceiveNo> select(String serno){
		return dao.select(serno);
	}
	public List<CommitXReceiveNo> selectByIDNO(String idno){
		return dao.selectByIDNO(idno);
	}
	public int insert(CommitXReceiveNo bean) {
		return dao.insert(bean);
	}
	public void insert(List<CommitXReceiveNo> beans) {
		dao.insert(beans);
	}
	public void delete(int serno){
		dao.delete(serno);
	}
}
