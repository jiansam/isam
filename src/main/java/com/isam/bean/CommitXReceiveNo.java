package com.isam.bean;

import java.io.Serializable;

public class CommitXReceiveNo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int serno;
	private String receiveNo;
	private String respDate;
	private String note;

	public int getSerno() {
		return serno;
	}
	public void setSerno(int serno) {
		this.serno = serno;
	}
	public String getReceiveNo() {
		return receiveNo;
	}
	public void setReceiveNo(String receiveNo) {
		this.receiveNo = receiveNo;
	}
	public String getRespDate() {
		return respDate;
	}
	public void setRespDate(String respDate) {
		this.respDate = respDate;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
