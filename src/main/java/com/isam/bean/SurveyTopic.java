package com.isam.bean;

import java.io.Serializable;

public class SurveyTopic implements Serializable{
	private static final long serialVersionUID = 1L;
	private String qType;
	private String topic;
	private String title;
	private String parent;
	
	public String getqType() {
		return qType;
	}
	public void setqType(String qType) {
		this.qType = qType;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
}
