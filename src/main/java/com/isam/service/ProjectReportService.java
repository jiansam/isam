package com.isam.service;

import java.util.List;
import java.util.Map;

import com.isam.bean.ProjectReport;
import com.isam.bean.ProjectReportName;
import com.isam.dao.ProjectReportDAO;

public class ProjectReportService{
	ProjectReportDAO dao = null;
	public ProjectReportService(){
		dao = new ProjectReportDAO();
	}
	
	public boolean isMaxYearQuarter(String year,String quarter,int serno){
		return dao.isMaxYearQuarter(year, quarter,serno);
	}
	/*確認資料是否存在*/
	public boolean isExists(int serno,String repType,String year,String quarter){
		return dao.isExists(serno, repType, year, quarter);
	}
	/*取回資料*/
	public ProjectReport select(int serno,String repType,String year,String quarter){
		return dao.select(serno, repType, year, quarter,"1");
	}
	public ProjectReport select(int serno,String repType,String year,String quarter,String enable){
		return dao.select(serno, repType, year, quarter,enable);
	}
	public ProjectReport selectByRepSerno(int repSerno,String enable){
		return dao.selectByRepSerno(repSerno,enable);
	}
	public void insert(ProjectReport bean) {
		dao.insert(bean);
	}
	public int update(ProjectReport bean) {
		return dao.update(bean);
	}
	public void unable(int repSerno,String updateuser,java.sql.Timestamp time) {
		dao.unable(repSerno, updateuser, time);
	}
	public List<List<String>> getReportPivot(int serno){
		return dao.getReportPivot(serno);
	}
	public Map<String,String> getMaxMinYearQuarter(){
		return dao.getMaxMinYearQuarter();
	}
	public String getNeedAlert(int serno){
		return dao.getNeedAlert(serno);
	}
	public Map<String,String> getNoNeedMap(String investNo){
		return dao.getNoNeedMap(investNo);
	}
	/*產出excel用*/
	public List<Map<String,String>> getReportList(String year,String quarter,String repState,String projDate){
		return dao.getReportList(year, quarter, repState,projDate);
	}
	/*檢視資料庫與廠商登載不符資料*/
	public List<Map<String,String>> getDifferList(String year,String quarter,String serno,String respdate){
		return dao.getDifferList(year, quarter, serno,respdate);
	}
	/*線上申報待確認相關*/
	/*取得線上申報待確認列表；enable=2*/
	public List<ProjectReportName> select(){
		//return dao.select("2");
		return dao.getConfirmList();
	}
	/*使用待確認的資料覆蓋原本系統的資料*/
	public void overwrite(String repSernos,String updateuser,java.sql.Timestamp updatetime) {
		dao.overwrite(repSernos, updateuser, updatetime);
	}
	/*將待確認資料做為參考資料*/
	public void forDBReference(String repSernos,String updateuser,java.sql.Timestamp time) {
		dao.forDBReference(repSernos, updateuser, time);
	}
	/*刪除前須檢查是否還有待確認資料*/
	public int checkUnConfirm(String repSerno){
		return dao.checkUnConfirm(repSerno);
	}
} 
