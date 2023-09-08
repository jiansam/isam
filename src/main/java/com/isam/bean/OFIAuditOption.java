package com.isam.bean;

import java.io.Serializable;

public class OFIAuditOption implements Serializable{
private static final long serialVersionUID = 1L;
	
	private String auditCode;
	private String description;
	private String selectName;
	private String isMultiple;
	private String autoDef;
	
	public String getAuditCode() {
		return auditCode;
	}
	public void setAuditCode(String auditCode) {
		this.auditCode = auditCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSelectName() {
		return selectName;
	}
	public void setSelectName(String selectName) {
		this.selectName = selectName;
	}
	public String getIsMultiple() {
		return isMultiple;
	}
	public void setIsMultiple(String isMultiple) {
		this.isMultiple = isMultiple;
	}
	public String getAutoDef() {
		return autoDef;
	}
	public void setAutoDef(String autoDef) {
		this.autoDef = autoDef;
	}
}
