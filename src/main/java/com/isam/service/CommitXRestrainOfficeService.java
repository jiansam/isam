package com.isam.service;

import java.util.List;

import com.isam.bean.CommitXRestrainOffice;
import com.isam.dao.CommitXRestrainOfficeDAO;

public class CommitXRestrainOfficeService {
	CommitXRestrainOfficeDAO dao = null;
	public CommitXRestrainOfficeService(){
		dao = new CommitXRestrainOfficeDAO();
	}
	
	public List<CommitXRestrainOffice> select(String serno){
		return dao.select(serno);
	}
	public String selectStr(String serno){
		return dao.selectStr(serno);
	}
	public String selectStrName(String serno){
		return dao.selectStrName(serno);
	}
	public String selectStrNameByIDNO(String idno){
		return dao.selectStrNameByIDNO(idno);
	}
	public int insert(CommitXRestrainOffice bean) {
		return dao.insert(bean);
	}
	public void insert(List<CommitXRestrainOffice> beans) {
		dao.insert(beans);
	}
	public void delete(int serno){
		dao.delete(serno);
	}
}
