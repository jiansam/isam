package com.isam.bean;

import java.io.Serializable;

public class OFIContacts implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String serno;
	private String investNo;
	private String name;
	private String telNo;
	private java.sql.Timestamp createtime;
	private String createuser;
	
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelNo() {
		return telNo;
	}
	public void setTelNo(String telNo) {
		this.telNo = telNo;
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
}
