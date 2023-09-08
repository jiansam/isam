package org.dasin.tools;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

public class dTools {
	/*
	 * String trim functions.
	 */
	public static String trim(String inString){
		return inString == null ? "" : inString.trim();
	}
	
	public static String trim(String inString, int length){
		String result = trim(inString);

		return result.length() > length ? result.substring(0, length) : result;
	}
	
	public static boolean isEmpty(String inString){
		return trim(inString).isEmpty();
	}
	
	/*
	 * Read .properties files.
	 */
	public static String getParameter(String propertyFile, String propertyName){
		String result = "";
		try{
			File externalFile = new File(System.getProperty("user.dir"), propertyFile + ".properties");
			if(externalFile.exists()){
				Properties prop = new Properties();
				prop.load(new FileInputStream(externalFile));

				result = trim(prop.getProperty(propertyName));

				if(!result.isEmpty()){
					return result;
				}
			}

			result = trim(ResourceBundle.getBundle(propertyFile).getString(propertyName));
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	/*
	 * Numeric string functions.
	 */
	public static int parseInt(String inString){
		int result = 0;
		inString = trim(inString).replace(",", "");
		try{
			if(inString.isEmpty()){
				return result;
			}

			result = Integer.parseInt(inString);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("0 will be returned.");
		}

		return result;
	}
	
	public static long parseLong(String inString){
		long result = 0;
		inString = trim(inString).replace(",", "");
		try{
			if(inString.isEmpty()){
				return result;
			}

			result = Long.parseLong(inString);
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("0 will be returned.");
		}

		return result;
	}
	public static String leadingZero(String inString, int digits){
		inString = trim(inString);
		while(inString.length() < digits){
			inString = "0" + inString;
		}

		return inString;
	}

	public static String leadingZero(int number, int digits){
		return leadingZero(String.valueOf(number), digits);
	}

	/*
	 * Make string valid for SQL commands.
	 */
	public static String dbstring(String inString){
		return trim(inString).replaceAll("'", "''");
	}
	
	/*
	 * Make string valid for XML file.
	 */
	public static String xmlstring(String inString){
		return trim(inString).replaceAll("&", "&amp;");
	}

	/*
	 * Make string valid for filenames.
	 */
	public static String filenamestring(String inString){
		return trim(inString).replaceAll("[/\\\\]", "")
				.replaceAll("[\n\\?]", "");
	}
	
	/*
	 * String to ArrayList.
	 */
	public static ArrayList<String> split(String inString, String delimiter){
		ArrayList<String> result = new ArrayList<String>();
		for(String s : trim(inString).split(delimiter)){
			s = s.trim();
			if(s.length() > 0){
				result.add(s);
			}
		}
		
		return result;
	}
}
