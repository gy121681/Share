package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;

public class ActCodeBean {
	
	private String rspcod;
	
	private String rspmsg;
	
	private String tolcnt;
	
	public String getTolcnt() {
		return tolcnt;
	}

	public void setTolcnt(String tolcnt) {
		this.tolcnt = tolcnt;
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


	public ArrayList<HashMap<String, Object>> list;
	
	
	public ActCodeBean() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<HashMap<String,Object>>();
	}

}
