package com.td.qianhai.epay.oem.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class CityEntity extends Entity{
	private String rspcod;
	private String rspmsg;
	private String numPerpage;
	private String tolcnt;
	public ArrayList<HashMap<String, Object>> list;
	
	public CityEntity() {
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
}
