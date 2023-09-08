package com.isam.bean;

import java.io.Serializable;

public class LoginRecord implements Serializable{
	private static final long serialVersionUID = 1L;
	private int serno;
	private String idMember;
	private java.sql.Timestamp loginTime;
	private String loginResult;
	private String loginIP;
	
	public int getSerno() {
		return serno;
	}
	public void setSerno(int serno) {
		this.serno = serno;
	}
	public String getIdMember() {
		return idMember;
	}
	public void setIdMember(String idMember) {
		this.idMember = idMember;
	}
	public java.sql.Timestamp getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(java.sql.Timestamp loginTime) {
		this.loginTime = loginTime;
	}
	public String getLoginResult() {
		return loginResult;
	}
	public void setLoginResult(String loginResult) {
		this.loginResult = loginResult;
	}
	public String getLoginIP() {
		return loginIP;
	}
	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}
}
