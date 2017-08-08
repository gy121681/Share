package com.td.qianhai.epay.oem.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class RateListBean {
	
	
	private String feeratno; //费率id
	
	private String feerate; //费率
	
	private String feeratedesc;//描述
	
	private String price;// 价钱

	private String rspcod;
	
	private String rspmsg;
	
	private String nocr;
	
	
	
	public String getNocr() {
		return nocr;
	}

	public void setNocr(String nocr) {
		this.nocr = nocr;
	}

	public RateListBean() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<HashMap<String,Object>>();
	}
	
	
	/**
	 * 节点存储list
	 */
	public ArrayList<HashMap<String, Object>> list;
	
	
	public String getRspcod() {
		return rspcod;
	}

	public void setRspcod(String rspcod) {
		this.rspcod = rspcod;
	}

	public String getRspmsg() {
		return rspmsg;
	}

	public void setRspmsg(String rspmsg) {
		this.rspmsg = rspmsg;
	}

	public String getFeeratno() {
		return feeratno;
	}

	public void setFeeratno(String feeratno) {
		this.feeratno = feeratno;
	}

	public String getFeerate() {
		return feerate;
	}

	public void setFeerate(String feerate) {
		this.feerate = feerate;
	}

	public String getFeeratedesc() {
		return feeratedesc;
	}

	public void setFeeratedesc(String feeratedesc) {
		this.feeratedesc = feeratedesc;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "RateListBean [feeratno=" + feeratno + ", feerate=" + feerate
				+ ", feeratedesc=" + feeratedesc + ", price=" + price
				+ ", rspcod=" + rspcod + ", rspmsg=" + rspmsg + "]";
	}

}
