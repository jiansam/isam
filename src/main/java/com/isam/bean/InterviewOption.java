package com.isam.bean;

import java.io.Serializable;

public class InterviewOption implements Serializable {
	private static final long serialVersionUID = 1L;
	private int Identifier;
	private String Description;
	private String CDescription;
	private String SelectName;
	private String OptionValue;
	private int Seq;
	private boolean Enable;

	public int getIdentifier() {
		return Identifier;
	}
	public void setIdentifier(int identifier) {
		Identifier = identifier;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getCDescription() {
		return CDescription;
	}
	public void setCDescription(String cDescription) {
		CDescription = cDescription;
	}
	public String getSelectName() {
		return SelectName;
	}
	public void setSelectName(String selectName) {
		SelectName = selectName;
	}
	public String getOptionValue() {
		return OptionValue;
	}
	public void setOptionValue(String optionValue) {
		OptionValue = optionValue;
	}
	public int getSeq() {
		return Seq;
	}
	public void setSeq(int seq) {
		Seq = seq;
	}
	public boolean isEnable() {
		return Enable;
	}
	public void setEnable(boolean enable) {
		Enable = enable;
	}

}
