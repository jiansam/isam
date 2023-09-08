package com.isam.bean;

import java.io.Serializable;

public class CommitReport implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int repserno;
	private int serno;
	private String Year;
	private String repType;
	private java.sql.Timestamp updatetime;
	private String updateuser;
	private java.sql.Timestamp createtime;
	private String createuser;
	private String enable;
	private String isOnline;
	private String keyinNo;
	private String isConversion;
	private String note;
	
	public int getRepserno() {
		return repserno;
	}
	public void setRepserno(int repserno) {
		this.repserno = repserno;
	}
	public int getSerno() {
		return serno;
	}
	public void setSerno(int serno) {
		this.serno = serno;
	}
	public String getYear() {
		return Year;
	}
	public void setYear(String year) {
		Year = year;
	}
	public String getRepType() {
		return repType;
	}
	public void setRepType(String repType) {
		this.repType = repType;
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
	public String getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getKeyinNo() {
		return keyinNo;
	}
	public void setKeyinNo(String keyinNo) {
		this.keyinNo = keyinNo;
	}
	public String getIsConversion() {
		return isConversion;
	}
	public void setIsConversion(String isConversion) {
		this.isConversion = isConversion;
	}
}
