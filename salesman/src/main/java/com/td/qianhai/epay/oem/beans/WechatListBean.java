package com.td.qianhai.epay.oem.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class WechatListBean {
	
	
	private String payTime;
	private String orderCount;
	private String payDate;
	private String totalFee;
	private String shopCodeMobile;
	/**
	 * 节点存储list
	 */
	public ArrayList<HashMap<String, Object>> list;

	
	public WechatListBean() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<HashMap<String,Object>>();
	}


	public String getPayTime() {
		return payTime;
	}


	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}


	public String getOrderCount() {
		return orderCount;
	}


	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}


	public String getPayDate() {
		return payDate;
	}


	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}


	public String getTotalFee() {
		return totalFee;
	}


	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}


	public String getShopCodeMobile() {
		return shopCodeMobile;
	}


	public void setShopCodeMobile(String shopCodeMobile) {
		this.shopCodeMobile = shopCodeMobile;
	}


	public ArrayList<HashMap<String, Object>> getList() {
		return list;
	}

	public void setList(ArrayList<HashMap<String, Object>> list) {
		this.list = list;
	}
	
	
}
