package com.td.qianhai.epay.oem.beans;

import java.io.Serializable;

/**
 * 航班信息对象
 * 
 * @author liangge
 * 
 */
public class FlightBean implements Serializable {

	/**
	 * 航空名字
	 */
	private String aviationName;
	/**
	 * 航班号
	 */
	private String flightNumber;
	/**
	 * 飞机名
	 */
	private String aircraftName;
	/**
	 * 起飞时间
	 */
	private String takeOffDate;
	/**
	 * 到达时间
	 */
	private String arriveDate;
	/**
	 * 起飞地点
	 */
	private String takeOfSite;
	/**
	 * 到达地点
	 */
	private String arriveSite;
	/**
	 * 价格
	 */
	private String price;
	/**
	 * 舱位类型
	 */
	private String spaceType;
	/**
	 * 折扣
	 */
	private String discount;
	
	public String getAviationName() {
		return aviationName;
	}
	public String getFlightNumber() {
		return flightNumber;
	}
	public String getAircraftName() {
		return aircraftName;
	}
	public String getTakeOffDate() {
		return takeOffDate;
	}
	public String getArriveDate() {
		return arriveDate;
	}
	public String getTakeOfSite() {
		return takeOfSite;
	}
	public String getArriveSite() {
		return arriveSite;
	}
	public String getPrice() {
		return price;
	}
	public String getSpaceType() {
		return spaceType;
	}
	public String getDiscount() {
		return discount;
	}
	public void setAviationName(String aviationName) {
		this.aviationName = aviationName;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	public void setAircraftName(String aircraftName) {
		this.aircraftName = aircraftName;
	}
	public void setTakeOffDate(String takeOffDate) {
		this.takeOffDate = takeOffDate;
	}
	public void setArriveDate(String arriveDate) {
		this.arriveDate = arriveDate;
	}
	public void setTakeOfSite(String takeOfSite) {
		this.takeOfSite = takeOfSite;
	}
	public void setArriveSite(String arriveSite) {
		this.arriveSite = arriveSite;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public void setSpaceType(String spaceType) {
		this.spaceType = spaceType;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
}
