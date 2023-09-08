package com.isam.helper;

import java.util.HashMap;
import java.util.Map;


public class OrderUtil {
	private static final Map<String,String> m1=new HashMap<String,String>();

	static {
		m1.put("1", "一");
		m1.put("2", "二");
		m1.put("3", "三");
		m1.put("4", "四");
		m1.put("5", "五");
		m1.put("6", "六");
		m1.put("7", "七");
		m1.put("8", "八");
		m1.put("9", "九");
		m1.put("0", "十");
	}
	public static String getOrderName(int num){
		String result="";
		StringBuilder sb=new StringBuilder();
		String c=String.valueOf(num);
		char[] chs=c.toCharArray();
		if(num==10){
			sb.append(m1.get("0"));
		}else if(c.length()<3){
			for( char a : chs ){
				sb.append(m1.get(String.valueOf(a)));
			}
		}else{
			try {
				throw new Exception("num can not exceed 99");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		result=sb.toString();
		if(sb.length()==2){
			if(result.startsWith("一")){
				sb.delete(0, 1);
				sb.insert(0, "十");
			}else if(!result.endsWith("十")){
				sb.insert(1, "十");
			}
		}
		result=sb.toString();
		sb.setLength(0);
		return result;
	}
	public static String addSquare(String str){
		StringBuilder sb=new StringBuilder();
		sb.append("（").append(str).append("）");
		String result=sb.toString();
		sb.setLength(0);
		return result;
	}
	public static String addComma(String str){
		StringBuilder sb=new StringBuilder();
		sb.append(str).append("、");
		String result=sb.toString();
		sb.setLength(0);
		return result;
	}
	public static void main(String args[]){
//		for (int i = 1; i <= 99; i++){
//			System.out.println(getOrderName(i));
//		}
	}
}
