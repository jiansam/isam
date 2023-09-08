package com.isam.ofi.reject.bean;

import java.io.Serializable;


public class OFIRejectXApplicant implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int serno;
	private int applyNo;
	private String cApplicant;
	private String eApplicant;
	private String nation;
	private String cnCode;
	private String note;
	private String enable;
	private java.sql.Timestamp updatetime;
	private String updateuser;
	private java.sql.Timestamp createtime;
	private String createuser;
	
	public int getSerno() {
		return serno;
	}
	public void setSerno(int serno) {
		this.serno = serno;
	}
	public int getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(int applyNo) {
		this.applyNo = applyNo;
	}
	public String getcApplicant() {
		return cApplicant;
	}
	public void setcApplicant(String cApplicant) {
		this.cApplicant = cApplicant;
	}
	public String geteApplicant() {
		return eApplicant;
	}
	public void seteApplicant(String eApplicant) {
		this.eApplicant = eApplicant;
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
}
