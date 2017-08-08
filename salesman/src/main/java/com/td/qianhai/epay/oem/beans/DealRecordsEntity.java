package com.td.qianhai.epay.oem.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class DealRecordsEntity extends Entity{
	
	private String rspcod;
	private String rspmsg;
	private String tolcnt;
	private String numPergage;
	
	public DealRecordsEntity(){
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
	
	public String getTolcnt() {
		return tolcnt;
	}
	public void setTolcnt(String tolcnt) {
		this.tolcnt = tolcnt;
	}
	public String getNumPergage() {
		return numPergage;
	}
	public void setNumPergage(String numPergage) {
		this.numPergage = numPergage;
	}
	
	public ArrayList<HashMap<String, Object>> list;
	
}
