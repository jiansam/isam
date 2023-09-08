package com.isam.bean;

import java.io.Serializable;

public class InterviewXInvestment implements Serializable{
	private static final long serialVersionUID = 1L;
	private int InterviewIdentifier;
	private String InvestmentType;

	public int getInterviewIdentifier() {
		return InterviewIdentifier;
	}
	public void setInterviewIdentifier(int interviewIdentifier) {
		InterviewIdentifier = interviewIdentifier;
	}
	public String getInvestmentType() {
		return InvestmentType;
	}
	public void setInvestmentType(String investmentType) {
		InvestmentType = investmentType;
	}
}
