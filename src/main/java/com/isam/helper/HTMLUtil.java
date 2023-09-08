package com.isam.helper;

public class HTMLUtil {
	public static String clean(String inString){
		inString = inString.replaceAll("\"微軟正黑體\"", "微軟正黑體")
					.replaceAll("\"sans-serif\"", "sans-serif")
					.replaceAll("<span lang=\"EN-US\">(.*?)</span>", "$1");
		
		return inString;
	}
}
