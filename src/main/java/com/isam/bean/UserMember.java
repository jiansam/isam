package com.isam.bean;

import java.io.Serializable;

public class UserMember implements Serializable{
	private static final long serialVersionUID = 1L;
	private String idMember;
	private String userPwd;
	private String company;
	private String username;
	private String userEmail;
	private String groupId;
	private String idEditor;
	private String idCreatetor;
	private java.sql.Timestamp createtime;
	private java.sql.Timestamp updtime;
	private String enable;
	
	public String getIdMember() {
		return idMember;
	}
	public void setIdMember(String idMember) {
		this.idMember = idMember;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getIdEditor() {
		return idEditor;
	}
	public void setIdEditor(String idEditor) {
		this.idEditor = idEditor;
	}
	public String getIdCreatetor() {
		return idCreatetor;
	}
	public void setIdCreatetor(String idCreatetor) {
		this.idCreatetor = idCreatetor;
	}
	public java.sql.Timestamp getCreatetime() {
		return createtime;
	}
	public void setCreatetime(java.sql.Timestamp createtime) {
		this.createtime = createtime;
	}
	public java.sql.Timestamp getUpdtime() {
		return updtime;
	}
	public void setUpdtime(java.sql.Timestamp updtime) {
		this.updtime = updtime;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
}
