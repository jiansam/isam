package com.isam.bean;

import java.io.Serializable;

public class InterviewContent  implements Serializable{
	private static final long serialVersionUID = 1L;

	private int identifier;
	private int interviewIdentifier;
	private String outlineCode;
	private String text;

	public int getIdentifier() {
		return identifier;
	}
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
	public int getInterviewIdentifier() {
		return interviewIdentifier;
	}
	public void setInterviewIdentifier(int interviewIdentifier) {
		this.interviewIdentifier = interviewIdentifier;
	}
	public String getOutlineCode() {
		return outlineCode;
	}
	public void setOutlineCode(String outlineCode) {
		this.outlineCode = outlineCode;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
