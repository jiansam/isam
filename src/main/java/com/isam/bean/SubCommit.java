package com.isam.bean;

import java.io.Serializable;

public class SubCommit implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String subserno;
	private String serno;
	private String investNo;
	private String type;
	private String state;
	private String repType;
	private String note;
	private String startYear;
	private String endYear;
	private java.sql.Timestamp updatetime;
	private String updateuser;
	private java.sql.Timestamp createtime;
	private String createuser;
	private String enable;
	private String needAlert;
	
	public String getSubserno() {
		return subserno;
	}
	public void setSubserno(String subserno) {
		this.subserno = subserno;
	}
	public String getSerno() {
		return serno;
	}
	public void setSerno(String serno) {
		this.serno = serno;
	}
	public String getInvestNo() {
		return investNo;
	}
	public void setInvestNo(String investNo) {
		this.investNo = investNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRepType() {
		return repType;
	}
	public void setRepType(String repType) {
		this.repType = repType;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getStartYear() {
		return startYear;
	}
	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}
	public String getEndYear() {
		return endYear;
	}
	public void setEndYear(String endYear) {
		this.endYear = endYear;
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
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getNeedAlert() {
		return needAlert;
	}
	public void setNeedAlert(String needAlert) {
		this.needAlert = needAlert;
	}
}
