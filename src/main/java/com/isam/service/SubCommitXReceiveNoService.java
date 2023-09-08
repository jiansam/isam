package com.isam.service;

import java.util.List;

import com.isam.bean.CommitXReceiveNo;
import com.isam.dao.SubCommitXReceiveNoDAO;

public class SubCommitXReceiveNoService {
	SubCommitXReceiveNoDAO dao = null;
	public SubCommitXReceiveNoService(){
		dao = new SubCommitXReceiveNoDAO();
	}
	
	public List<CommitXReceiveNo> select(String subserno){
		return dao.select(subserno);
	}
	public String selectStr(String subserno){
		List<CommitXReceiveNo> list = this.select(subserno);
		String result="";
		StringBuffer sb =new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if(i!=0){
				sb.append(",");
			}
			sb.append(list.get(i).getReceiveNo());
		}
		result=sb.toString();
		sb.setLength(0);
		return result;
	}
	public void insertInto(String subserno,String serno,String subReceive) {
		dao.insertInto(subserno,serno,subReceive);
	}
	public void delete(String subserno){
		dao.delete(subserno);
	}
}
