package com.td.qianhai.epay.oem.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class ReturnActcodeBean {
	
	private String rspcod;
	
	private String rspmsg;
	
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


	public ArrayList<HashMap<String, Object>> list;
	
	
	public ReturnActcodeBean() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<HashMap<String,Object>>();
	}

}
