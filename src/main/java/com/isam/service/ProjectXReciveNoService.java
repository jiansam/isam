package com.isam.service;

import java.util.List;

import com.isam.dao.ProjectXReciveNoDAO;

public class ProjectXReciveNoService{
	ProjectXReciveNoDAO dao = null;
	public ProjectXReciveNoService(){
		dao = new ProjectXReciveNoDAO();
	}

	public boolean isExists(int repSerno){
		return dao.isExists(repSerno);
	}
	public List<String> select(int repSerno){
		return dao.select(repSerno);
	}
	public void delete(int repSerno){
		if(dao.isExists(repSerno)){
			dao.delete(repSerno);
		}
	}
	public void insertBatch(String[] receviceNoStr,int repSerno) {
		dao.insertBatch(receviceNoStr, repSerno);
	}
} 
