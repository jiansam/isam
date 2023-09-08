package com.isam.bean;

import java.io.Serializable;

public class Interviewone implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int qNo;
	private String investNo;
	private String reInvestNo;
	private String year;
	private String interviewStatus;
	private String surveyStatus;
	private String enable;
	private java.sql.Timestamp updatetime;
	private String updateuser;
	private java.sql.Timestamp createtime;
	private String createuser;
	private String msg;
	private int fileCount; //106-11-29新增 紀錄[下載檔案的數量]
	private String businessIncomeTaxCode; //2018.7.13.dasin : 新增行業別顯示
	
	public String getBusinessIncomeTaxCode() {
		return businessIncomeTaxCode;
	}
	public void setBusinessIncomeTaxCode(String businessIncomeTaxCode) {
		this.businessIncomeTaxCode = businessIncomeTaxCode;
	}
	public int getqNo() {
		return qNo;
	}
	public void setqNo(int qNo) {
		this.qNo = qNo;
	}
	public String getInvestNo() {
		return investNo;
	}
	public void setInvestNo(String investNo) {
		this.investNo = investNo;
	}
	public String getReInvestNo() {
		return reInvestNo;
	}
	public void setReInvestNo(String reInvestNo) {
		this.reInvestNo = reInvestNo;
	}
	public String getYear() {
		return year;
	}
	
	public int getYearInt() {
		try {
			return Integer.parseInt(year);
		}
		catch (NumberFormatException e) {
	        return 103;
	    }

		
		
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getInterviewStatus() {
		return interviewStatus;
	}
	public void setInterviewStatus(String interviewStatus) {
		this.interviewStatus = interviewStatus;
	}
	public String getSurveyStatus() {
		return surveyStatus;
	}
	public void setSurveyStatus(String surveyStatus) {
		this.surveyStatus = surveyStatus;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public java.sql.Timestamp getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(java.sql.Timestamp updatetime) {
		this.updatetime = updatetime;
	}
	public String getUpdateuser() {
		return updateuser;
	}
	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}
	public java.sql.Timestamp getCreatetime() {
		return createtime;
	}
	public void setCreatetime(java.sql.Timestamp createtime) {
		this.createtime = createtime;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getFileCount()
	{
		return fileCount;
	}
	public void setFileCount(int fileCount)
	{
		this.fileCount = fileCount;
	}
	
}
