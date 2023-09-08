package com.isam.service;

import java.util.List;

import com.isam.bean.CommitXRestrainOffice;
import com.isam.dao.SubCommitXRestrainOfficeDAO;

public class SubCommitXRestrainOfficeService {
	SubCommitXRestrainOfficeDAO dao = null;
	public SubCommitXRestrainOfficeService(){
		dao = new SubCommitXRestrainOfficeDAO();
	}
	
	public List<CommitXRestrainOffice> select(String subserno){
		return dao.select(subserno);
	}
	public String selectStr(String subserno){
		List<CommitXRestrainOffice> list = this.select(subserno);
		String result="";
		StringBuffer sb =new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if(i!=0){
				sb.append(",");
			}
			sb.append(list.get(i).getType());
		}
		result=sb.toString();
		sb.setLength(0);
		return result;
	}
	public void insert(List<CommitXRestrainOffice> beans) {
		dao.insert(beans);
	}
	public void delete(String subserno){
		dao.delete(subserno);
	}
}
