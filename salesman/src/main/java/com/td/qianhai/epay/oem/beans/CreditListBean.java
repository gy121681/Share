package com.td.qianhai.epay.oem.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class CreditListBean {
	
	private String cardactid;
	
	private String cardno;
	
	private String cardname;
	
	private String issnam;
	
	private String issno;
	
	private String rspcod;
	
	private String rspmsg;
	
	public ArrayList<HashMap<String, Object>> list;
	
	public CreditListBean(){
		
		list = new ArrayList<HashMap<String,Object>>();
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



	public String getCardactid() {
		return cardactid;
	}

	public void setCardactid(String cardactid) {
		this.cardactid = cardactid;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getCardname() {
		return cardname;
	}

	public void setCardname(String cardname) {
		this.cardname = cardname;
	}

	public String getIssnam() {
		return issnam;
	}

	public void setIssnam(String issnam) {
		this.issnam = issnam;
	}

	public String getIssno() {
		return issno;
	}

	public void setIssno(String issno) {
		this.issno = issno;
	}


	@Override
	public String toString() {
		return "CreditListBean [cardactid=" + cardactid + ", cardno=" + cardno
				+ ", cardname=" + cardname + ", issnam=" + issnam + ", issno="
				+ issno + ", rspcod=" + rspcod + ", rspmsg=" + rspmsg + "]";
	}

}
