package com.isam.service;

import java.util.List;

import com.isam.dao.ProjectOnlineSrcDAO;

public class ProjectOnlineSrcService{
	ProjectOnlineSrcDAO dao = null;
	public ProjectOnlineSrcService(){
		dao = new ProjectOnlineSrcDAO();
	}
	/*取得無法對應到系統現存專案的資料*/
	public List<List<String>> getNotMatch(){
		return dao.getNotMatch();
	}
	public void insert(String no) {
		dao.insert(no);
	}
} 
