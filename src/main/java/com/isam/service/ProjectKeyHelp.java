package com.isam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.isam.bean.ApprovalOption;
import com.isam.bean.Investment;
import com.isam.bean.Investor;
import com.isam.bean.UserMember;
import com.isam.dao.ApprovalOptionDAO;
import com.isam.dao.InvestmentDAO;
import com.isam.dao.InvestorDAO;
import com.isam.dao.UserLoginDAO;


public class ProjectKeyHelp {
	private Map<String,String> InvestNoToMain= null;
	private Map<String,String> InvestNoToName= null;
	private Map<String,String> IDNoToMain= null;
	private Map<String,String> IDNOToName= null;
	private Map<String,String> ProjectState= null;
	private Map<String,String> financialState= null;
	private Map<String,String> userToName= null;
	private Map<String,String> repTypeMap= null;
	
	public ProjectKeyHelp(){
		initial();
		read();
	}
	private void initial(){
		InvestNoToMain=new HashMap<String, String>();
		InvestNoToName=new HashMap<String, String>();
		IDNoToMain=new HashMap<String, String>();
		IDNOToName=new HashMap<String, String>();
		ProjectState=new HashMap<String, String>();
		financialState=new HashMap<String, String>();
		userToName=new HashMap<String, String>();
		repTypeMap=new HashMap<String, String>();
	}
	private void read(){
		getInvsetor();
		getInvsetment();
		getProject();
		getUserName();
	}
	private void getInvsetor(){
		InvestorDAO dao=new InvestorDAO();
		List<Investor> list=dao.select();
		for(Investor bean:list){
			IDNoToMain.put(bean.getoIDNO(), bean.getIDNO());
			IDNOToName.put(bean.getIDNO(), bean.getInvestor());
		}
	}
	private void getProject(){
		List<ApprovalOption> list=ApprovalOptionDAO.select("project","state");
		for(ApprovalOption bean:list){
			ProjectState.put(bean.getCode(), bean.getTitle());
		}
		list=ApprovalOptionDAO.select("ProjectReport","financial");
		for(ApprovalOption bean:list){
			financialState.put(bean.getCode(), bean.getTitle());
		}
		list=ApprovalOptionDAO.select("Commit","repType");
		for(ApprovalOption bean:list){
			repTypeMap.put(bean.getCode(), bean.getTitle());
		}
	}
	private void getInvsetment(){
		InvestmentDAO dao=new InvestmentDAO();
		List<Investment> list=dao.select();
		for(Investment bean:list){
			InvestNoToMain.put(bean.getoInvestNo(), bean.getInvestNo());
			InvestNoToName.put(bean.getInvestNo(), bean.getCompName());
		}
	}
	private void getUserName(){
		UserLoginDAO dao=new UserLoginDAO();
		List<UserMember> list=dao.select(null,null);
		for(UserMember bean:list){
			userToName.put(bean.getIdMember(), bean.getUsername());
		}
	}
	
	public Map<String, String> getInvestNoToMain() {
		return InvestNoToMain;
	}
	
	public Map<String, String> getInvestNoToName() {
		return InvestNoToName;
	}
	
	public Map<String, String> getIDNoToMain() {
		return IDNoToMain;
	}
	public Map<String, String> getIDNOToName() {
		return IDNOToName;
	}
	public Map<String, String> getFinancialState() {
		return financialState;
	}
	public Map<String, String> getProjectState() {
		return ProjectState;
	}
	public Map<String, String> getUserToName() {
		return userToName;
	}
	public Map<String, String> getRepTypeMap() {
		return repTypeMap;
	}
}
