package com.isam.bean;

import java.io.Serializable;

public class CommitXRestrainOffice implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private int serno;
	private String type;
	
	public int getSerno() {
		return serno;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setSerno(int serno) {
		this.serno = serno;
	}
}
