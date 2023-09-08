package com.isam.bean;

import java.util.Date;

public class SurveyFile
{
	int id;
	int year;
	String type;
	String file_title;
	byte[] file_content;
	boolean disable;
	Date updateTime;
	
	@Override
	public String toString()
	{
		return "SurveyFile [id=" + id + ", year=" + year + ", type=" + type + ", file_title=" + file_title
				+ ", disable=" + disable + "]";
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getFile_title()
	{
		return file_title;
	}

	public void setFile_title(String file_title)
	{
		this.file_title = file_title;
	}

	public byte[] getFile_content()
	{
		return file_content;
	}

	public void setFile_content(byte[] file_content)
	{
		this.file_content = file_content;
	}

	public boolean isDisable()
	{
		return disable;
	}

	public void setDisable(boolean disable)
	{
		this.disable = disable;
	}
	
	
}
