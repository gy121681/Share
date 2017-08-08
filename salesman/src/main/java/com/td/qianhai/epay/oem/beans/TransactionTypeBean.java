package com.td.qianhai.epay.oem.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionTypeBean {
	
	private String operstypid;
	
	private String operstypnam;
	
	private String rspcod;
	
	private String rspmsg;
	
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

	public String getOperstypid() {
		return operstypid;
	}

	public void setOperstypid(String operstypid) {
		this.operstypid = operstypid;
	}

	public String getOperstypnam() {
		return operstypnam;
	}

	public void setOperstypnam(String operstypnam) {
		this.operstypnam = operstypnam;
	}
	
	public TransactionTypeBean() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<HashMap<String,Object>>();
	}

}
