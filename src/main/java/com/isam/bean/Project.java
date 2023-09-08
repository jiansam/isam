package com.isam.bean;

public class Project {
	private int serno;
	private String investNo;
	private String IDNO;
	private String state;
	private String note;
	private java.sql.Timestamp updatetime;
	private String updateuser;
	private java.sql.Timestamp createtime;
	private String createuser;
	private String needAlert;
	private String lastReceiveNo;
	private String porjDate;
	private String isSysDate;
	
	public int getSerno() {
		return serno;
	}
	public void setSerno(int serno) {
		this.serno = serno;
	}
	public String getInvestNo() {
		return investNo;
	}
	public void setInvestNo(String investNo) {
		this.investNo = investNo;
	}
	public String getIDNO() {
		return IDNO;
	}
	public void setIDNO(String iDNO) {
		IDNO = iDNO;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
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
	public String getNeedAlert() {
		return needAlert;
	}
	public void setNeedAlert(String needAlert) {
		this.needAlert = needAlert;
	}
	public String getLastReceiveNo() {
		return lastReceiveNo;
	}
	public void setLastReceiveNo(String lastReceiveNo) {
		this.lastReceiveNo = lastReceiveNo;
	}
	public String getPorjDate() {
		return porjDate;
	}
	public void setPorjDate(String porjDate) {
		this.porjDate = porjDate;
	}
	public String getIsSysDate() {
		return isSysDate;
	}
	public void setIsSysDate(String isSysDate) {
		this.isSysDate = isSysDate;
	}
}
