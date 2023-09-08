package com.isam.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

public class DataUtil {
	public static String trim(String inString){
		if(inString == null){
			inString = "";
		}else{
			inString = inString.trim();
		}

		return inString;
	}
	public static String shorten(String inString, int maxStringLength){
		inString = trim(inString);
		if(inString.length() > maxStringLength){
			inString = inString.substring(0, maxStringLength) + "...";
		}
		return inString;
	}
	public static String shortenTooltip(String inString, int maxStringLength,String classname){
		inString = trim(inString);
		if(inString.length() > maxStringLength){
			inString = "<a href='#' class='"+classname+"' title='"+inString+"'>"+inString.substring(0, maxStringLength) + "...</a>";
		}
		return inString;
	}
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static java.sql.Timestamp convertDate(String date) {
		String data = date.replace("/", "-");
		java.sql.Timestamp result = null;
		Date dat = new Date();

		if (data != null) {
			try {
				dat = (java.util.Date) sdf.parse(data);
				result = new java.sql.Timestamp(dat.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
				result = new java.sql.Timestamp(new java.util.Date().getTime());
			}
		}
		return result;
	}
	
	public static String getStrUDate(){
		return sdf.format(new java.util.Date());
	}
	
	public static String timestamp2Str(java.sql.Timestamp date) {
		return sdf.format(date);
	}
	public static String toTWDateStr(java.sql.Timestamp date) {
		String result="";
		if(date!=null){
			String tmp=sdf.format(date);
			String[] d =tmp.split("-");
			result=(Integer.valueOf(d[0])-1911)+"/"+d[1]+"/"+d[2];
		}
		return result;
	}
	public static java.sql.Timestamp getNowTimestamp() {
		return new java.sql.Timestamp(new java.util.Date().getTime());
	}
	public static boolean isEmpty(String inString){
		boolean result=false;
		if(inString==null||inString.trim().length()==0){
			result=true;
		}
		return result;
	}
	public static boolean isMatchPtn(String str,String pattren){ 
	    Pattern pattern = Pattern.compile(pattren); 
	    return pattern.matcher(str).matches();   
	}
	/*public static boolean isMatchIdMember(String str){ 
		boolean result=false;
		if(isMatchPtn(str, "[A-Za-z0-9]{6,12}")&&isMatchPtn(str, ".*[A-Za-z]+.*")&&isMatchPtn(str, ".*[0-9]+.*")){
			result=true;
		}
		return result;   
	}*/
	public static boolean isMatchEmail(String str){ 
		return isMatchPtn(str,"^[_A-Za-z0-9-]+([.][_A-Za-z0-9-]+)*@[A-Za-z0-9-]+([.][A-Za-z0-9-]+)*$");   
	}
	public static boolean isMatchTWWord(String str){ 
		return isMatchPtn(str,"^[\u4E00-\u9FA5]+");   
	}
	public static boolean isMatchFmtNumber(String str){ 
		return isMatchPtn(str.replaceAll(",", ""),"-{0,1}[0-9]*\\.{0,1}[0-9]*");   
	}
	public static String fmtSearchItem(String item,String start){
		item=DataUtil.nulltoempty(item);
		StringBuilder sb = new StringBuilder();
		String rs="";
		if(!item.isEmpty()){
			item=item.trim();
			item.replace("%", "").replace("(", "（").replace(")", "）");
			sb.append(start).append(item).append("%");
		}else{
			sb.append("%");
		}
		rs = sb.toString();
		sb.setLength(0);
		return rs;
	}
	public static String fmtDateItem(String item){
		StringBuilder sb = new StringBuilder();
		String rs=null;
		if(!item.isEmpty()){
			sb.append(item.replace("/", ""));
			rs=sb.toString();
		}
		sb.setLength(0);
		return rs;
	}
	public static String fmtStrAryItem(String[] items){
		StringBuilder sb = new StringBuilder();
		if(items!=null){
			for(String s:items){
				s=s.replace(",", "");
				if(sb.length()!=0){
					sb.append(",");
				}
				sb.append("'").append(s).append("'");
			}
		}
		return sb.toString();
	}
	public static String fmtStrAryItem(List<String> items){
		StringBuilder sb = new StringBuilder();
		for(String s:items){
			s=s.replace(",", "");
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append("'").append(s).append("'");
		}
		return sb.toString();
	}
	public static String fmtPvtItem(List<String> items){
		StringBuilder sb = new StringBuilder();
		for(String s:items){
			s=s.replace(",", "");
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append("[").append(s).append("]");
		}
		return sb.toString();
	}
	public static String addTokenToItem(String[] items,String token){
		StringBuilder sb = new StringBuilder();
		for(String s:items){
			s=s.replace(token, "");
			if(sb.length()!=0){
				sb.append(",");
			}
			sb.append(s);
		}
		return sb.toString();
	}
	public static String fmtStrAryItemWithNA(String[] items){
		StringBuilder sb = new StringBuilder();
		for(String s:items){
			s=s.replace(",", "");
			if(sb.length()!=0){
				sb.append(",");
			}
			if(s.isEmpty()){
				s="NA";
			}
			sb.append(s);
		}
		return sb.toString();
	}
	public static String addZeroForNum(String str, int strLength) {
		int strLen = str.length();
		if (strLen < strLength) {
			while (strLen < strLength) {
				StringBuffer sb = new StringBuffer();
				sb.append("0").append(str);// 左補0
				// sb.append(str).append("0");//右補0
				str = sb.toString();
				strLen = str.length();
			}
		}
		return str;
	}
	public static String addRigthZeroForNum(String str, int strLength) {
		int strLen = str.length();
		if (strLen < strLength) {
			while (strLen < strLength) {
				StringBuffer sb = new StringBuffer();
				sb.append(str).append("0");//右補0
				str = sb.toString();
				strLen = str.length();
			}
		}
		return str;
	}
	public static String addRigthNineForNum(String str, int strLength) {
		int strLen = str.length();
		if (strLen < strLength) {
			while (strLen < strLength) {
				StringBuffer sb = new StringBuffer();
				sb.append(str).append("9");//右補0
				str = sb.toString();
				strLen = str.length();
			}
		}
		return str;
	}
	public static double toDouble(String item) {
		double result=0;
		if(item!=null&&item.length()>0&&DataUtil.isMatchFmtNumber(item)){
			result=Double.valueOf(item.replaceAll(",",""));
		}
		return result;
	}
	public static String formatIntString(double str) {
		NumberFormat fmt = new DecimalFormat("#,##0");
		String result="";
		result = fmt.format(str);
		return result;
	}
	public static String formatString(String str) {
		NumberFormat fmt = new DecimalFormat("#,##0.00");
		String result="";
		if(str.isEmpty()){
			result = "-";
		}else{
			result = fmt.format(Double.valueOf(str));
		}
		return result;
	}
	
	public static double SToD(String str) {
		double result=0;
		if(str!=null&&!str.isEmpty()&&DataUtil.isMatchFmtNumber(str)){
			try {
				result=Double.valueOf(str.replaceAll(",", ""));
			} catch (NumberFormatException e) {
				e.printStackTrace();
				result=0;
			}
		}
		return result;
	}
	
	public static int SToI(String str) {
		int result = 0;
		str = str == null ? "0" : str.replaceAll(",", "");
		
		try {
			result = Double.valueOf(str).intValue();
		} catch (NumberFormatException e) {
			//e.printStackTrace();
		}

		return result;
	}
	
	public static String paramToStringD(String str) {
		String result=null;
		if(str!=null){
			if(!str.isEmpty()&&DataUtil.isMatchFmtNumber(str)){
				try {
					result=str.replaceAll(",", "");
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	public static String termOrToParam(String paramname,List<String> list) {
		String result="";
		StringBuffer sb=new StringBuffer();
		for(int i = 0;i<list.size();i++){
			if(i>0){
				sb.append(" or ");
			}
			sb.append(paramname).append("='").append(list.get(i)).append("'");
		}
		result=sb.toString();
		sb.setLength(0);
		return result;
	}
	public static String paramToTWDate(String str) {
		String result=DataUtil.nulltoempty(str);
		if(!result.isEmpty()){
			if(DataUtil.isMatchPtn(result,"\\d{2,3}/\\d{2}/\\d{2}")){
				result=DataUtil.addZeroForNum(result.replaceAll("\\D", ""), 7);
			}else if(DataUtil.isMatchPtn(result,"\\d{2,3}/\\d{1,2}/\\d{1,2}")){
				StringBuffer sb = new StringBuffer();
				String[] s=result.split("\\");
				sb.append(DataUtil.addZeroForNum(s[0], 3));
				sb.append(DataUtil.addZeroForNum(s[1], 2));
				sb.append(DataUtil.addZeroForNum(s[2], 2));
				result=sb.toString();
				sb.setLength(0);
			}
		}
		return result;
	}
	public static String paramToTWYM(String str) {
		String result=DataUtil.nulltoempty(str);
		if(!result.isEmpty()){
			if(DataUtil.isMatchPtn(result,"\\d{2,3}/\\d{2}")){
				result=DataUtil.addZeroForNum(result.replaceAll("\\D", ""), 5);
			}else if(DataUtil.isMatchPtn(result,"\\d{2,3}/\\d{1,2}")){
				StringBuffer sb = new StringBuffer();
				String[] s=result.split("/");
				sb.append(DataUtil.addZeroForNum(s[0], 3));
				sb.append(DataUtil.addZeroForNum(s[1], 2));
				result=sb.toString();
				sb.setLength(0);
			}
		}
		return result;
	}
	public static String addSlashToTWDate(String str) {
		String result="";
		str = str.trim();
		StringBuffer sb = new StringBuffer();
		if(!str.isEmpty()&&DataUtil.isMatchFmtNumber(str)){
			sb.append(DataUtil.addZeroForNum(str, 7));
			sb.insert(5, "/");
			sb.insert(3, "/");
			result=sb.toString();
			sb.setLength(0);
		}
		return result;
	}
	public static String addSlashToTWYM(String str) {
		String result="";
		str = str.trim();
		StringBuffer sb = new StringBuffer();
		if(!str.isEmpty()&&DataUtil.isMatchFmtNumber(str)){
			sb.append(DataUtil.addZeroForNum(str, 5));
			sb.insert(3, "/");
			result=sb.toString();
			sb.setLength(0);
		}
		return result;
	}
	public static String toParamStr(Map<String,String> map) {
		StringBuffer sb= new StringBuffer();
		for(Entry<String, String> m:map.entrySet()){
			sb.append(m.getKey()).append("=").append(m.getValue()).append("&");
		}
		String result = sb.substring(0, sb.length()-1).toString();
		sb.setLength(0);
		return result;
	}
	public static String nulltoempty(String inString){
		String result="";
		if(inString!=null&&inString.trim().length()!=0){
			result=inString.trim();
		}
		return result;
	}
	public static List<String> StrArytoList(String[] inString){
		List<String> list = new ArrayList<String>();
		if(inString!=null){
			for (String s:inString) {
				list.add(s);
			}
		}
		return list;
	}
	public static List<String> StrArytoListNoEmpty(String[] inString){
		return removeEmpty(StrArytoList(inString));
	}
	private static List<String> removeEmpty(List<String> list){
		List<String> result = new ArrayList<String>();
		for (String s:list) {
			if(!s.isEmpty()){
				result.add(s);
			}
		}
		return result;
	}
	public static String getGrowthRate(String up,String down){
		String result="";
		double upD=SToD(up);
		double downD=SToD(down);
		if(downD!=0){
			result=String.valueOf((upD-downD)/Math.abs(downD)*100);
		}
		return result;
	}
	public static String getPercent(String up,String down){
		String result="";
		double upD=SToD(up);
		double downD=SToD(down);
		if(downD!=0){
			result=String.valueOf((upD/downD)*100);
		}
		return result;
	}
	public static String encodeFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
		  String userAgent = request.getHeader("user-agent");
//		  System.out.println(userAgent);
		  String encodedFileName;
		  // IE
		  if (userAgent.indexOf("Trident")!=-1||userAgent.indexOf("MSIE")!=-1) {
		    encodedFileName = URLEncoder.encode(fileName, "utf-8");
		  }
		  // Firefox
		  else if (userAgent.indexOf("Firefox")!=-1) {
		    StringBuilder sb = new StringBuilder();
		    for (int i = 0; i < fileName.length(); i++) {
		      char c = fileName.charAt(i);
		      // Firefox 會自空白處切斷，所以將空白轉為+
		      if (((int) c) < 128) {
		        sb.append(URLEncoder.encode(String.valueOf(c), "ISO8859-1"));
		      }
		      else {
		        sb.append(new String(String.valueOf(c).getBytes(), "ISO8859-1"));
		      }
		    }
		    encodedFileName = sb.toString();
		  }
		  // Chrome
		  else if(userAgent.indexOf("Chrome")!=-1){
		    encodedFileName = MimeUtility.encodeWord(fileName, "utf-8", "Q");
		  }else{
			  encodedFileName = URLEncoder.encode(fileName, "utf-8");
		  }
		  return encodedFileName;
		}
/*	private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm a",Locale.ENGLISH);
	public static java.sql.Timestamp convertDateTmp(String date) {
		String data = date.replace("/", "-");
		java.sql.Timestamp result = null;
		Date dat = new Date();

		if (data != null) {
			try {
				dat = (java.util.Date) sdf1.parse(data);
				System.out.println(sdf1.format(dat));
				result = new java.sql.Timestamp(dat.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
				result = new java.sql.Timestamp(new java.util.Date().getTime());
			}
		}
		return result;
	}*/
	public static void main(String[] args){
//		System.out.println(DataUtil.paramToTWYM("104/6"));
	}
}
