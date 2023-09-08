package com.isam.bean;

import java.io.Serializable;

public class OFIInvestorXBG implements Serializable{
private static final long serialVersionUID = 1L;
	
	private String investorSeq;
	private String bgType;
	private String value;
	private int seq;
	private java.sql.Timestamp createtime;
	private String createuser;
	
	
	public String getInvestorSeq() {
		return investorSeq;
	}
	public void setInvestorSeq(String investorSeq) {
		this.investorSeq = investorSeq;
	}
	public String getBgType() {
		return bgType;
	}
	public void setBgType(String bgType) {
		this.bgType = bgType;
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
