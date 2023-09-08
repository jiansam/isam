package Lara.Utility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateUtil
{
	//取出今天是星期幾
	public static String getDayNameOfWeek(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
		String dayname = "";
		if(Calendar.SUNDAY == weekDay){
			dayname = "星期日";
		}else if(Calendar.MONDAY == weekDay){
			dayname = "星期一";
		}else if(Calendar.TUESDAY == weekDay){
			dayname = "星期二";
		}else if(Calendar.WEDNESDAY == weekDay){
			dayname = "星期三";
		}else if(Calendar.THURSDAY == weekDay){
			dayname = "星期四";
		}else if(Calendar.FRIDAY == weekDay){
			dayname = "星期五";
		}else if(Calendar.SATURDAY == weekDay){
			dayname = "星期六";
		}
		return dayname;
	}

	//獲取當前日期，與本週一相差的天數，再取出星期一的Date
	public static Date getMondayOfthisweek(Date date)
	{
		/*取出跟星期一的range*/
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int todayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int range = 0;
		if(todayOfWeek == 1){
			range = -6; //-6(上週一)  or 1(本週一)
		}else{
			range = 2 - todayOfWeek;			
		}

		/* GregorianCalendar是Calendar的子類別，可以處理閏年*/
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.setTime(date);
		currentDate.add(GregorianCalendar.DATE, range);
		Date monday = currentDate.getTime();
		return monday;
	}
	
	//取出當天加上range後的那天 GregorianCalendar有處理各地的時間制
	public static Date getNextRangeDay(int Calendar_field, int range, Date when)
	{
		Calendar currentDate = new GregorianCalendar();
		currentDate.setTime(when);
		currentDate.add(Calendar_field, range); //Gregorian_field.DATE, 8
		return currentDate.getTime();
	}
	
	//取出起始日到結束日的所有工作天
	public static ArrayList<Date> getWorkDay(Date startDate, Date endDate)
	{
		/* 比較 第一天 到 最後一天 共有幾個週數 */
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		int start = calendar.get(Calendar.WEEK_OF_YEAR);
		calendar.setTime(endDate);
		int end = calendar.get(Calendar.WEEK_OF_YEAR);
		int weesNum = end - start + 1;
		
		
		/* 取出第一個星期一
		 * GregorianCalendar是Calendar的子類別，可以處理閏年*/
		Date monday = getMondayOfthisweek(startDate);
		
		/* 先放入第一個星期一，然後跑 週數迴圈，確定有最後一天在裡面後結束在該週星期五
		 * 使用 GregorianCalendar.DATE，加上跟星期一差距 
		 * 使用 GregorianCalendar.DAY_OF_WEEK，也是加上跟星期一差距 */
		ArrayList<Date> list = new ArrayList<>();
		for(int x=0; x<weesNum; x++){
			
			for(int r=0; r<5; r++){
				Date workday = DateUtil.getNextRangeDay(GregorianCalendar.DATE, r, monday);
				list.add(workday);
			}
			//取出下一個星期一
			monday = DateUtil.getNextRangeDay(GregorianCalendar.DATE, 7, monday);
		}
		
		return list;
	}

	
	/* 時間區間判斷
	 * 
	 * @param 起始年月、結尾年月
	 * @return Map<String,String> 兩個時間字串
	 */
	public static Map<String,String> getYearRange(int year1, int year2, 
													String monthLeft1, String monthLeft2 ){
		
		String start_time = null;
		String end_time = null;
		/*
		 * 預防時間軸顛倒，起始比結尾 晚 
		 */
//		if(year1!=0 && year2!=0 && year1 > year2){
//			int temp = year1;
//			year1 = year2;
//			year2 = temp;
//		}
//		if(year1==0 && year2==0){
//			if(!"0".equals(monthLeft1) && !"0".equals(monthLeft2)){
//				int m1 = Integer.valueOf(monthLeft1);
//				int m2 = Integer.valueOf(monthLeft2);
//				if(m1 > m2){
//					String temp = monthLeft1;
//					monthLeft1 = monthLeft2;
//					monthLeft2 = temp;
//				}
//			}
//		}
//		System.out.println(monthLeft1+", "+monthLeft2);
		/* 如果年度沒選，卻有選月區間，就以該年度作搜尋，因為時間是區間搜尋
		 */
		
		/* 起始時間：
		 * 沒有年度，沒有月，給null，全域搜尋
		 * 沒有年度，沒有月，但如果[結尾年度]不是空的，就用[結尾年度]的年
		 * 沒有年度，有月，用今年搜尋，日期設定該月第一天
		 * 沒有年度，有月，但如果[結尾年度]不是空的，就用[結尾年度]的年
		 */
		if(year1==0){
			int thisYear = DateUtil.getThisCHYear() + 1911 ; //西元年
			if(("0").equals(monthLeft1)){
				start_time = null;
				if(year2!=0){
					thisYear = year2 + 1911;
					start_time = String.valueOf(thisYear) +"-01-01"; //yyyy-mm-dd
				}
			}else{
				if(year2!=0){
					thisYear = year2 + 1911;
				}
				monthLeft1 = monthLeft1.length()==1 ? "0"+monthLeft1 : monthLeft1;
				start_time = String.valueOf(thisYear) +"-"+ monthLeft1 +"-01"; //yyyy-mm-dd
			}
		}
		
		/* 起始時間：
		 * 有年度，沒有月，從1月1日起搜尋
		 * 有年度，有月，日期設定該月第一天
		 */
		else if(year1!=0){
			int thisYear = year1 + 1911; 
			if(("0").equals(monthLeft1)){
				start_time = String.valueOf(thisYear) +"-01-01";
			}else{
				monthLeft1 = monthLeft1.length()==1 ? "0"+monthLeft1 : monthLeft1;
				start_time = String.valueOf(thisYear) +"-"+ monthLeft1 +"-01";
			}
		}
		
		
		/* 結束時間：
		 * 沒有年度，沒有月，給null，全域搜尋 
		 * 沒有年度，沒有月，但如果[起始年度]不是空的，就用[起始年度]的年，搜尋到該年度最後一天 
		 * 沒有年度，有月，用今年搜尋，日期設定該月最後一天。若沒有起始時間，就把起始時間設定成與結尾時間同一月份第一天
		 * 沒有年度，有月，但如果[起始年度]不是空的，就用[起始年度]的年
		 */
		if(year2==0){
			int thisYear = DateUtil.getThisCHYear() + 1911 ; //西元年			
			if("0".equals(monthLeft2)){
				end_time = null;
				if(year1!=0){
					thisYear = year1 + 1911;
					end_time = String.valueOf(thisYear) +"-12-31 23:59:59";	
				}
			}else{
				if(year1!=0){
					thisYear = year1 + 1911;
				}
				String day = DateUtil.getTheDayOfMonth(thisYear, Integer.valueOf(monthLeft2));
				monthLeft2 = monthLeft2.length()==1 ? "0"+monthLeft2 : monthLeft2;
				end_time = String.valueOf(thisYear) +"-"+ monthLeft2 +"-"+day+" 23:59:59";					
				
				// 有搜尋的結尾時間，卻沒有開頭時間，開頭時間就用同一月份搜尋
				if(start_time == null){
					start_time = String.valueOf(thisYear) +"-"+ monthLeft2 +"-01";	
				}
			}
		}

		/* 結束時間：
		 * 有年度，沒有月，就設定12月31日
		 * 有年度，有月，日期設定該月最後一天。若沒有起始時間，就把起始時間設定成與結尾時間同一月份第一天
		 */
		else if(year2!=0){
			int thisYear = year2 + 1911; 
			if("0".equals(monthLeft2)){
				end_time = String.valueOf(thisYear) +"-12-31 23:59:59";
			}else{
				String day = DateUtil.getTheDayOfMonth(thisYear, Integer.valueOf(monthLeft2));
				monthLeft2 = monthLeft2.length()==1 ? "0"+monthLeft2 : monthLeft2;
				end_time = String.valueOf(thisYear) +"-"+ monthLeft2 +"-"+day+" 23:59:59";

				// 有搜尋的結尾時間，卻沒有開頭時間，開頭時間就用同一月份搜尋
				if(start_time == null){
					start_time = String.valueOf(thisYear) +"-"+ monthLeft2 +"-01";	
				}
			}
		}	
//		System.out.println(start_time);
//		System.out.println(end_time);
//		System.out.println("----------------");
		
		/* 預防時間軸顛倒，起始比結尾 晚 
		 */
		if(start_time!=null && end_time!=null){
			Date start = DateUtil.dateToChange(start_time, "yyyy-MM-dd", "EN");
			Date end = DateUtil.dateToChange(end_time, "yyyy-MM-dd HH:mm:ss", "EN");
			if(start.getTime() > end.getTime()){
				String temp = end_time;
				end_time = start_time + temp.substring(10);
				start_time = temp.substring(0, 10);
			}
		}
		
		Map<String,String> result = new HashMap<>();
		result.put("start_time", start_time);
		result.put("end_time", end_time);
		return result;
	}
	
	
	
	
	/* 把Date 轉成 Timestamp */
	public static Timestamp dateToTimestamp(Date date){
		Timestamp result = null;
		if(date!=null){
			result = new Timestamp(date.getTime());
		}
		return result;
	}
	
	/* 把 字串日期 改成Date日期 */
	public static Date dateToChange(String dateStr , String pattern, String CHorEN){
		Date result = null;
		if(dateStr!=null && dateStr.trim().length()!=0){
			
			SimpleDateFormat sdf = null;
			if("CH".equalsIgnoreCase(CHorEN)){
				sdf = new SimpleDateFormat(pattern, Locale.CHINESE);
			}else{
				sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
			}
			
			try {
				result = sdf.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/* 把 Date日期 改成 字串日期 */
	public static String dateToChange(Date date , String pattern, String CHorEN) {
		String dateStr = "";
		if(date!=null){
			SimpleDateFormat sdf = null;
			if("CH".equalsIgnoreCase(CHorEN)){
				sdf = new SimpleDateFormat(pattern, Locale.CHINESE);
			}else{
				sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
			}
			
			try {
				dateStr = sdf.format(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dateStr;
	}
	
	/* 把 Date日期 改成 字串日期，而且是民國年 */
	public static String dateToChangeROC(Date date , String pattern, String CHorEN){
		StringBuilder dateStr = new StringBuilder();
		if(date!=null){
			
			SimpleDateFormat sdf = null;
			if("CH".equalsIgnoreCase(CHorEN)){
				sdf = new SimpleDateFormat(pattern, Locale.CHINESE);
			}else{
				sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
			}
			
			String temp = null;
			try {
				temp = sdf.format(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			int year = ToolsUtil.parseInt(temp.substring(0,4))-1911;
			dateStr.append(year + temp.substring(4));
		}
		return dateStr.toString();
	}
		
	/* 把 字串日期 改成Date日期 ，而且是民國年  ex:105-09-09 */
	public static Date dateToChangeROC(String date , String pattern, String CHorEN){
		Date result = null;
		
		if(date!=null){
			
			int year = Integer.valueOf(date.substring(0,3)) + 1911;
			
			SimpleDateFormat sdf = null;
			if("CH".equalsIgnoreCase(CHorEN)){
				sdf = new SimpleDateFormat(pattern, Locale.CHINESE);
			}else{
				sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
			}
			
			try {
				result = sdf.parse(year + date.substring(3));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return result;
	}
	
	
	
	/* 取出某年某月的天數 */
	public static String getTheDayOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month-1 , 1);
		int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return String.valueOf(day);
	}
	
	//判定取得的日期 > 今天
	public static boolean isTodayAfterWhen(Date when)
	{
		//取出現在時間
		Calendar now = Calendar.getInstance(); 
		Date today = now.getTime();
//		Date today = dateToSet(2016, 9, 19);
//		System.out.println("today="+today + ", finalDay="+when + ", today.after(when)  " + today.after(when));
		return today.after(when); //今天 是否晚於 指定日期-->true
	}
	
	//取得上月哪一月 + 最後一天
	public static Date getLastmonthLastday()
	{
		//取出現在時間
		Calendar now = Calendar.getInstance(); 
		now.add(Calendar.MONTH, -1);
		String lastmonth = 
				now.get(Calendar.YEAR)+"/"+(now.get(Calendar.MONTH)+1)+"/"+(now.getActualMaximum(Calendar.DAY_OF_MONTH)) 
																					+ " 23:59:59";
		return dateToChange(lastmonth, "yyyy/MM/dd HH:mm:ss", "EN");
	}
	
	/*自填日期 （month會先-1再填入）*/
	public static Date dateToSet(int year, int month, int date){ 
		Calendar ca = Calendar.getInstance();
		ca.set(year, month-1, date);
		return ca.getTime();
	}	
	
	/*取得今年民國年*/
	public static int getThisCHYear(){
		Calendar rightNow = Calendar.getInstance();
		int year = ( rightNow.get(Calendar.YEAR) ) - 1911;
		return year;
	}
	
	//產生出  年度 - 民國版 
	public static ArrayList<String> getChineseYearRange(int start_year){ 
		Calendar rightNow = Calendar.getInstance();
		int year = ( rightNow.get(Calendar.YEAR) ) - 1911;
		ArrayList<String> years = new ArrayList<>();
		for(int n=year; n>=start_year ; n--){
			years.add(String.valueOf(n));
		}
		return years;
	}
	
	/* 今天日期  加減天數，取得日期（使用Calendar） */
	public static String getDistanceDay(int distance) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_MONTH, distance); //-30天 = 30天前
		return dateToChange(now.getTime(), "yyyy-MM-dd", "EN");
	}
	
	/* 今天日期 加減天數，取得日期  
	 * int範圍（10位數）無法承受 86400000豪秒 乘以 天數，會超過範圍(25開始)，所以一定要改成Long型態，否則超過範圍就變負數了 
	 * style == 0 減天數； 
	 * style == 1 加天數 
	 */
	public static String dateToOneDate(Date when, String pattern, int range, int style){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		if(style == 0){
			return sdf.format( new Date( when.getTime() - (86400000 * Long.valueOf(range)) ));
		}else{
			return sdf.format( new Date( when.getTime() + (86400000 * Long.valueOf(range)) ));
		}
	}
	
	
	
	/* 今天日期 加減天數，取得日期  
	 * int範圍（10位數）無法承受 86400000豪秒 乘以 天數，會超過範圍(25開始)，所以一定要改成Long型態，否則超過範圍就變負數了 
	 * style == 0 減天數； 
	 * style == 1 加天數 
	 */
	public static Date dateToOneDate(Date when, int range, int style){
		if(style == 0){
			return new Date( when.getTime() - (86400000 * Long.valueOf(range)) );
		}else{
			return new Date( when.getTime() + (86400000 * Long.valueOf(range)) );
		}
		
	}
}
