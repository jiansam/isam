package com.isam.bean;

import java.io.Serializable;

public class OFIInvestorXRelated implements Serializable{
private static final long serialVersionUID = 1L;
	
	private String serno;
	private String investorSeq;
	private String relatedname;
	private String nation;
	private String cnCode;
	private java.sql.Timestamp updatetime;
	private String updateuser;
	private java.sql.Timestamp createtime;
	private String createuser;
	
	public String getSerno() {
		return serno;
	}
	public void setSerno(String serno) {
		this.serno = serno;
	}
	public String getInvestorSeq() {
		return investorSeq;
	}
	public void setInvestorSeq(String investorSeq) {
		this.investorSeq = investorSeq;
	}
	public String getRelatedname() {
		return relatedname;
	}
	public void setRelatedname(String relatedname) {
		this.relatedname = relatedname;
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
