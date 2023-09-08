package com.isam.bean;

import java.io.Serializable;
import java.lang.String;

import com.isam.helper.DataUtil;

public class InterviewRegion implements Serializable {
	private static final long serialVersionUID = 1L;
	private String code;
	private String name;
	private int level;
	private String parent;
	private int seq;
	private boolean enable;

	public InterviewRegion() {
		super();
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setLevel(String level) {
		try{
			this.level = Integer.parseInt(level);
		}catch(Exception e){
			this.level = 1;	//Set default level = 1
		}
	}

	public String getParent() {
		return DataUtil.trim(this.parent);
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public int getSeq() {
		return this.seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	public void setSeq(String seq) {
		try{
			this.seq = Integer.parseInt(seq);
		}catch(Exception e){
			this.seq = 1;	//Set default seq = 1
		}
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	public void setEnable(String enable) {
		this.enable = "1".equalsIgnoreCase(enable);
	}
}
