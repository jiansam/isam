package com.isam.bean;

public class CommitInvestorXContacts {
	private String cid;
	private String IDNO;
	private String contact;
	private String tel;
	private java.sql.Timestamp updatetime;
	private String updateuser;
	

	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getIDNO() {
		return IDNO;
	}
	public void setIDNO(String iDNO) {
		IDNO = iDNO;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
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
	
}
