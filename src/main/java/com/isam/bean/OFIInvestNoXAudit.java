package com.isam.bean;

import java.io.Serializable;

public class OFIInvestNoXAudit implements Serializable{
	private static final long serialVersionUID = 1L;
	private String investNo;
	private String auditCode;
	private String value;
	private int seq;
	private java.sql.Timestamp createtime;
	private String createuser;
	
	
	
	@Override
	public String toString()
	{
		return "OFIInvestNoXAudit [investNo=" + investNo + ", auditCode=" + auditCode + ", value=" + value + ", seq="
				+ seq + ", createtime=" + createtime + ", createuser=" + createuser + "]";
	}
	public String getInvestNo() {
		return investNo;
	}
	public void setInvestNo(String investNo) {
		this.investNo = investNo;
	}
	public String getAuditCode() {
		return auditCode;
	}
	public void setAuditCode(String auditCode) {
		this.auditCode = auditCode;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
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
