package com.isam.bean;

import java.io.Serializable;

public class OFIInvestorList implements Serializable{
private static final long serialVersionUID = 1L;
	
	private String investorSeq;
	private String nation;
	private String cnCode;
	private String note;
	private String isFilled;
	private String enable;
	private java.sql.Timestamp updatetime;
	private String updateuser;
	private java.sql.Timestamp createtime;
	private String createuser;
	private String cname;
	private String ename;
	private String inrole;
	public String getInvestorSeq() {
		return investorSeq;
	}
	public void setInvestorSeq(String investorSeq) {
		this.investorSeq = investorSeq;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getCnCode() {
		return cnCode;
	}
	public void setCnCode(String cnCode) {
		this.cnCode = cnCode;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getIsFilled() {
		return isFilled;
	}
	public void setIsFilled(String isFilled) {
		this.isFilled = isFilled;
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
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getInrole() {
		return inrole;
	}
	public void setInrole(String inrole) {
		this.inrole = inrole;
	}
}
