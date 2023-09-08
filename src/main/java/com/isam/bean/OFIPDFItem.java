package com.isam.bean;

import java.io.Serializable;

public class OFIPDFItem implements Serializable{
private static final long serialVersionUID = 1L;
	
	private String iNo;
	private String item;
	private String name;
	private String classify;
	private int colspan;
	
	public String getiNo() {
		return iNo;
	}
	public void setiNo(String iNo) {
		this.iNo = iNo;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public int getColspan() {
		return colspan;
	}
	public void setColspan(int colspan) {
		this.colspan = colspan;
	}
}
