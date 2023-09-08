package com.isam.bean;

import java.io.Serializable;

public class InterviewoneFile implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int fNo;
	private int qNo;
//	private String investNo;
	private String year;
	private String qType;
	private String fName;
	private byte[] fContent;
	private java.sql.Timestamp updatetime;
	private String updateuser;
	
	public int getfNo() {
		return fNo;
	}
	public void setfNo(int fNo) {
		this.fNo = fNo;
	}
//	public String getInvestNo() {
//		return investNo;
//	}
//	public void setInvestNo(String investNo) {
//		this.investNo = investNo;
//	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getqType() {
		return qType;
	}
	public int getqNo() {
		return qNo;
	}
	public void setqNo(int qNo) {
		this.qNo = qNo;
	}
	public void setqType(String qType) {
		this.qType = qType;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public byte[] getfContent() {
		return fContent;
	}
	public void setfContent(byte[] fContent) {
		this.fContent = fContent;
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
