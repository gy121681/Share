package com.td.qianhai.epay.oem.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAgtBean {

	private String agentid;

	private String agtnam;

	private String feerate;

	private String actcodnum;
	
	private String actid;
	
	private String rspcod;
	
	private String rspmsg;
	
	private String tolcnt;
	
	private String minrate;
	
	public ArrayList<HashMap<String, Object>> list;

	public MyAgtBean() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<HashMap<String, Object>>();
	}

	
	public String getMinrate() {
		return minrate;
	}

	public void setMinrate(String minrate) {
		this.minrate = minrate;
	}

	public String getTolcnt() {
		return tolcnt;
	}

	public void setTolcnt(String tolcnt) {
		this.tolcnt = tolcnt;
	}

	public String getActid() {
		return actid;
	}

	public void setActid(String actid) {
		this.actid = actid;
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

	public String getAgentid() {
		return agentid;
	}

	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}

	public String getAgtnam() {
		return agtnam;
	}

	public void setAgtnam(String agtnam) {
		this.agtnam = agtnam;
	}

	public String getFeerate() {
		return feerate;
	}

	public void setFeerate(String feerate) {
		this.feerate = feerate;
	}

	public String getActcodnum() {
		return actcodnum;
	}

	public void setActcodnum(String actcodnum) {
		this.actcodnum = actcodnum;
	}

}
