package com.isam.bean;

import java.io.Serializable;

public class InterviewXRegion implements Serializable{
	private static final long serialVersionUID = 1L;
	private int InterviewIdentifier;
	private String RegionCode;
	
	public int getInterviewIdentifier() {
		return InterviewIdentifier;
	}
	public void setInterviewIdentifier(int interviewIdentifier) {
		InterviewIdentifier = interviewIdentifier;
	}
	public String getRegionCode() {
		return RegionCode;
	}
	public void setRegionCode(String regionCode) {
		RegionCode = regionCode;
	}
}
