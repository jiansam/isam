package com.isam.bean;

public class ApprovalOption {
	private String type;
	private String code;
	private String title;
	private String column;
	private String restrainType;
	private int seq;
	private int enable;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public String getRestrainType() {
		return restrainType;
	}
	public void setRestrainType(String restrainType) {
		this.restrainType = restrainType;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
}
