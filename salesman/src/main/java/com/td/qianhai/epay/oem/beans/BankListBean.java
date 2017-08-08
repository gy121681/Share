package com.td.qianhai.epay.oem.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class BankListBean {
	
	private String credtype;
	
	private String credcode;
	
	private String issuer;
	
	private String frpid;
	
	private String cardtel;
	
	private String cardname;
	
	private String cardcode;
	
	private String expireyear;
	
	private String expiremonth;
	
	private String rechargenumber;
	
	private String cvv;
	
	private String rspcod;
	
	private String rspmsg;
	
	public ArrayList<HashMap<String, Object>> list;
	
	public BankListBean(){
		
		list = new ArrayList<HashMap<String,Object>>();
	}

	public String getCredtype() {
		return credtype;
	}

	public void setCredtype(String credtype) {
		this.credtype = credtype;
	}

	public String getCredcode() {
		return credcode;
	}

	public void setCredcode(String credcode) {
		this.credcode = credcode;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getFrpid() {
		return frpid;
	}

	public void setFrpid(String frpid) {
		this.frpid = frpid;
	}

	public String getCardtel() {
		return cardtel;
	}

	public void setCardtel(String cardtel) {
		this.cardtel = cardtel;
	}

	public String getCardname() {
		return cardname;
	}

	public void setCardname(String cardname) {
		this.cardname = cardname;
	}

	public String getCardcode() {
		return cardcode;
	}

	public void setCardcode(String cardcode) {
		this.cardcode = cardcode;
	}

	public String getExpireyear() {
		return expireyear;
	}

	public void setExpireyear(String expireyear) {
		this.expireyear = expireyear;
	}

	public String getExpiremonth() {
		return expiremonth;
	}

	public void setExpiremonth(String expiremonth) {
		this.expiremonth = expiremonth;
	}

	public String getRechargenumber() {
		return rechargenumber;
	}

	public void setRechargenumber(String rechargenumber) {
		this.rechargenumber = rechargenumber;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

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

	@Override
	public String toString() {
		return "BankListBean [credtype=" + credtype + ", credcode=" + credcode
				+ ", issuer=" + issuer + ", frpid=" + frpid + ", cardtel="
				+ cardtel + ", cardname=" + cardname + ", cardcode=" + cardcode
				+ ", expireyear=" + expireyear + ", expiremonth=" + expiremonth
				+ ", rechargenumber=" + rechargenumber + ", cvv=" + cvv
				+ ", rspcod=" + rspcod + ", rspmsg=" + rspmsg + "]";
	}

}
