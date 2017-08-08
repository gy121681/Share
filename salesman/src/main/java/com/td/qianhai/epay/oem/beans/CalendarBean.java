package com.td.qianhai.epay.oem.beans;

import java.util.Date;

public class CalendarBean {

	public static final String tagSingle = "\n出发";
	public static final String tagRound = "\n返回";
	private static int tomorrowTag; // 是否默认标记明天为出行日
	private static int singleI,roundI; // 行
	private static int singleY,roundY; // 列
	private static boolean flag,roundFlag; // 蓝色标记出行日
	private static Date calendarday,roundCalendarday; //记录的标记的当前日历出行、返回日历
	private static int tagSingleYear,tagRoundYear; //出行年、返回年
	private static int tagSingleMonth,tagRoundMonth; //出现月、返回月
	
	public static int getRoundI() {
		return roundI;
	}

	public static void setRoundI(int roundI) {
		CalendarBean.roundI = roundI;
	}

	public static int getRoundY() {
		return roundY;
	}

	public static void setRoundY(int roundY) {
		CalendarBean.roundY = roundY;
	}

	public static boolean isRoundFlag() {
		return roundFlag;
	}

	public static void setRoundFlag(boolean roundFlag) {
		CalendarBean.roundFlag = roundFlag;
	}

	public static Date getRoundCalendarday() {
		return roundCalendarday;
	}

	public static void setRoundCalendarday(Date roundCalendarday) {
		CalendarBean.roundCalendarday = roundCalendarday;
	}

	public static int getTagRoundYear() {
		return tagRoundYear;
	}

	public static void setTagRoundYear(int tagRoundYear) {
		CalendarBean.tagRoundYear = tagRoundYear;
	}

	public static int getTagRoundMonth() {
		return tagRoundMonth;
	}

	public static void setTagRoundMonth(int tagRoundMonth) {
		CalendarBean.tagRoundMonth = tagRoundMonth;
	}

	public static int getTagSingleYear() {
		return tagSingleYear;
	}

	public static void setTagSingleYear(int tagSingleYear) {
		CalendarBean.tagSingleYear = tagSingleYear;
	}

	public static int getTagSingleMonth() {
		return tagSingleMonth;
	}

	public static void setTagSingleMonth(int tagSingleMonth) {
		CalendarBean.tagSingleMonth = tagSingleMonth;
	}

	public static Date getCalendarday() {
		return calendarday;
	}

	public static void setCalendarday(Date calendarday) {
		CalendarBean.calendarday = calendarday;
	}

	public static int getTomorrowTag() {
		return tomorrowTag;
	}

	public static void setTomorrowTag(int tomorrowTag) {
		CalendarBean.tomorrowTag = tomorrowTag;
	}

	public static int getSingleI() {
		return singleI;
	}

	public static void setSingleI(int singleI) {
		CalendarBean.singleI = singleI;
	}

	public static int getSingleY() {
		return singleY;
	}

	public static void setSingleY(int singleY) {
		CalendarBean.singleY = singleY;
	}

	public static boolean isFlag() {
		return flag;
	}

	public static void setFlag(boolean flag) {
		CalendarBean.flag = flag;
	}
}
