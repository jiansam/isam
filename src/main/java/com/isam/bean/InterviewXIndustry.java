package com.isam.bean;

import java.io.Serializable;

public class InterviewXIndustry implements Serializable{
	private static final long serialVersionUID = 1L;
	private int InterviewIdentifier;
	private String IndustryCode;
	
	public int getInterviewIdentifier() {
		return InterviewIdentifier;
	}
	public void setInterviewIdentifier(int interviewIdentifier) {
		InterviewIdentifier = interviewIdentifier;
	}
	public String getIndustryCode() {
		return IndustryCode;
	}
	public void setIndustryCode(String industryCode) {
		IndustryCode = industryCode;
	}
}
