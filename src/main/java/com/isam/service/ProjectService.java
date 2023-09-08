package com.isam.service;

import java.util.List;

import com.isam.bean.Project;
import com.isam.dao.ProjectDAO;

public class ProjectService{
	ProjectDAO dao = null;
	public ProjectService(){
		dao = new ProjectDAO();
	}
	public Project getProjectBySerno(String serno){
		return dao.select(serno, null).isEmpty()?null:dao.select(serno, null).get(0);
	}
	public int getSerno(String investNo,String IDNO){
		return dao.getSerno(investNo, IDNO);
	}
	public List<Project> select(String serno,String state){
		return dao.select(serno, state);
	}
	public int update(Project bean) {
		return dao.update(bean);
	}
	public List<Project> getSearchResult(String investor,String IDNO,String investNo,String cnName,String state,String alert){
		return dao.getSearchResult(investor, IDNO, investNo, cnName, state,alert);
	}
	public List<Project> getSearchResult(String investor,String IDNO,String investNo,String cnName,String state,String from,String to,String alert){
		return dao.getSearchResult(investor, IDNO, investNo, cnName, state,from,to,alert);
	}
	public int updateState(String state,String updateuser,String serno) {
		return dao.updateState(state, updateuser, serno);
	}
	public List<Project> selectBySernoStr(String SernoStr){
		return dao.selectBySernoStr(SernoStr);
	}
	public List<String> getIDNOByInvestNo(String investNo){
		return dao.getIDNOByInvestNo(investNo);
	}
	
	/*當累計已實行投資比例>=100%時，修改為1*/
	public int updateNeedAlert(int serno,String needAlert) {
		return dao.updateNeedAlert(serno, needAlert);
	}
} 
