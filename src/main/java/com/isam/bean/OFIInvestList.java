package com.isam.bean;

import java.io.Serializable;

public class OFIInvestList implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String investNo;
	private String active;
	private String isNew;
	private String setupdate;
	private String approvaldate;
	private String setupnote;
	private String respdate;
	private String receiveNo;
	private String isOperated;
	private String sdate;
	private String edate;
	private String note;
	private String enable;
	private String isFilled;
	private String isCNFDI;
	private java.sql.Timestamp updatetime;
	private String updateuser;
	private java.sql.Timestamp createtime;
	private String createuser;
	private String firmXNTBTSic; //107-07-13 新增國稅局或財報登記營業項目
	
	
	public String getInvestNo() {
		return investNo;
	}
	public void setInvestNo(String investNo) {
		this.investNo = investNo;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	public String getSetupdate() {
		return setupdate;
	}
	public void setSetupdate(String setupdate) {
		this.setupdate = setupdate;
	}
	public String getApprovaldate() {
		return approvaldate;
	}
	public void setApprovaldate(String approvaldate) {
		this.approvaldate = approvaldate;
	}
	public String getSetupnote() {
		return setupnote;
	}
	public void setSetupnote(String setupnote) {
		this.setupnote = setupnote;
	}
	public String getRespdate() {
		return respdate;
	}
	public void setRespdate(String respdate) {
		this.respdate = respdate;
	}
	public String getReceiveNo() {
		return receiveNo;
	}
	public void setReceiveNo(String receiveNo) {
		this.receiveNo = receiveNo;
	}
	public String getIsOperated() {
		return isOperated;
	}
	public void setIsOperated(String isOperated) {
		this.isOperated = isOperated;
	}
	public String getSdate() {
		return sdate;
	}
	public void setSdate(String sdate) {
		this.sdate = sdate;
	}
	public String getEdate() {
		return edate;
	}
	public void setEdate(String edate) {
		this.edate = edate;
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
	public String getIsFilled() {
		return isFilled;
	}
	public void setIsFilled(String isFilled) {
		this.isFilled = isFilled;
	}
	public String getIsCNFDI() {
		return isCNFDI;
	}
	public void setIsCNFDI(String isCNFDI) {
		this.isCNFDI = isCNFDI;
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
	public String getFirmXNTBTSic()
	{
		return firmXNTBTSic;
	}
	public void setFirmXNTBTSic(String firmXNTBTSic)
	{
		this.firmXNTBTSic = firmXNTBTSic;
	}
	
}
