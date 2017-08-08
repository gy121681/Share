package com.td.qianhai.epay.oem.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class PhoneChargeBean {

	private String prdtype;//类型
	
	private String prdnum;//数量
	
	private String prdname;//名称
	
	private String prdid; //id
	
	private String prdamt;//金额
	
	private String prdparvalue; //面值
	
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
	
	
	public PhoneChargeBean(){
		
		list = new ArrayList<HashMap<String,Object>>();
	}
	
	public String getPrdtype() {
		return prdtype;
	}

	public void setPrdtype(String prdtype) {
		this.prdtype = prdtype;
	}

	public String getPrdnum() {
		return prdnum;
	}

	public void setPrdnum(String prdnum) {
		this.prdnum = prdnum;
	}

	public String getPrdname() {
		return prdname;
	}

	public void setPrdname(String prdname) {
		this.prdname = prdname;
	}

	public String getPrdid() {
		return prdid;
	}

	public void setPrdid(String prdid) {
		this.prdid = prdid;
	}

	public String getPrdamt() {
		return prdamt;
	}

	public void setPrdamt(String prdamt) {
		this.prdamt = prdamt;
	}

	public String getPrdparvalue() {
		return prdparvalue;
	}

	public void setPrdparvalue(String prdparvalue) {
		this.prdparvalue = prdparvalue;
	}

	@Override
	public String toString() {
		return "PhoneChargeBean [prdtype=" + prdtype + ", prdnum=" + prdnum
				+ ", prdname=" + prdname + ", prdid=" + prdid + ", prdamt="
				+ prdamt + ", prdparvalue=" + prdparvalue + "]";
	}
	
}
