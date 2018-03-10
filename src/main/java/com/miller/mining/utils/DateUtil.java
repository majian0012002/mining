package com.miller.mining.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.miller.mining.exception.VerifyException;

public class DateUtil {

	public static String getDateStrWithFormat(String format,Date date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static Date getDateFromDateStr(String format,String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(dateStr);
	}
	
	public static long getDistanceOfDate(Date date1,Date date2) throws VerifyException {
		if(null == date1 || null == date2) {
			throw new VerifyException("开始时间或结束时间不能为空");
		}
		long distance = Math.abs(date2.getTime() - date1.getTime()) / (1000 * 60 * 60);
		
		return distance;
	}
	
	public static long getDistanceOfDate(String datestr1,String dateste2,String dateFormat) throws ParseException, VerifyException {
		Date date1 = getDateFromDateStr(dateFormat,datestr1);
		Date date2 = getDateFromDateStr(dateFormat,dateste2);
		
		return getDistanceOfDate(date1,date2);
	}
	

	/**
	public static void main(String[] args) {
		try {
			String dateStr = getDateStrWithFormat("yyyy-MM-ddHH:mm:ss", new Date());
			System.out.println(dateStr);
			Date date = getDateFromDateStr("yyyy-MM-ddHH:mm:ss",dateStr);
			System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}**/

}
