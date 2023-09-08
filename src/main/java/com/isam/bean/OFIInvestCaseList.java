package com.isam.bean;

import java.io.Serializable;

public class OFIInvestCaseList implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int caseNo;
	private String investNo;
	private String investorSeq;
	private String enable;
	private String isFilled;
	private String note;
	private java.sql.Timestamp updatetime;
	private String updateuser;
	private java.sql.Timestamp createtime;
	private String createuser;
	public int getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(int caseNo) {
		this.caseNo = caseNo;
	}
	public String getInvestNo() {
		return investNo;
	}
	public void setInvestNo(String investNo) {
		this.investNo = investNo;
	}
	public String getInvestorSeq() {
		return investorSeq;
	}
	public void setInvestorSeq(String investorSeq) {
		this.investorSeq = investorSeq;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getIsFilled() {
		return isFilled;
	}
	public void setIsFilled(String isFilled) {
		this.isFilled = isFilled;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
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
}
