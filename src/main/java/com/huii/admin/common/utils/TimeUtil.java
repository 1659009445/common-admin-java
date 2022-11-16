package com.huii.admin.common.utils;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component
public class TimeUtil {

	public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	public static String EEE_MMM_DD_YYYY_HH_MM_SS_Z = "EEE MMM dd yyyy HH:mm:ss z";

	/**
	 * 转换GMT时间
	 * 如果有获取的时间有加号需要改为("GMT"," ")
	 * 若果是element-ui time 处理格式
	 * "Thu Nov 17 2022 00:00:00 GMT 0800 (中国标准时间)";
	 * @param gmtTime GMT form time
	 * @return yyyy-MM-dd HH:mm:ss time
	 */
	public String castGMTTime(String gmtTime){
		gmtTime = gmtTime.replace("GMT ", "+").replaceAll("\\(.*\\)", "");
		gmtTime = gmtTime.substring(0,gmtTime.length()-1);
		SimpleDateFormat format = new SimpleDateFormat(EEE_MMM_DD_YYYY_HH_MM_SS_Z, Locale.ENGLISH);
		Date date = null;
		try {
			date = format.parse(gmtTime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}
