package com.td.qianhai.epay.oem.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 定期模版
 * 
 * @author Administrator
 * 
 */
public class BasisModel implements Serializable {
	private String dptmid;
	private String dptmnam;
	private String dptrate;
	private String dptcycle;
	private String dptmin;
	private String svakind;
	private String currency;
	private String brkmodnam;
	private String rspcod;
	private String rspmsg;
	private HashMap<String, Object> positionList;
	public ArrayList<HashMap<String, Object>> list;

	public BasisModel() {
		super();
		// TODO Auto-generated constructor stub
		list = new ArrayList<HashMap<String, Object>>();
	}

	public HashMap<String, Object> getPositionList() {
		return positionList;
	}

	public void setPositionList(HashMap<String, Object> positionList) {
		this.positionList = positionList;
	}

	public String getDptmid() {
		return dptmid;
	}

	public void setDptmid(String dptmid) {
		this.dptmid = dptmid;
	}

	public String getDptmnam() {
		return dptmnam;
	}

	public void setDptmnam(String dptmnam) {
		this.dptmnam = dptmnam;
	}

	public String getDptrate() {
		return dptrate;
	}

	public void setDptrate(String dptrate) {
		this.dptrate = dptrate;
	}

	public String getDptcycle() {
		return dptcycle;
	}

	public void setDptcycle(String dptcycle) {
		this.dptcycle = dptcycle;
	}

	public String getDptmin() {
		return dptmin;
	}

	public void setDptmin(String dptmin) {
		this.dptmin = dptmin;
	}

	public String getSvakind() {
		return svakind;
	}

	public void setSvakind(String svakind) {
		this.svakind = svakind;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBrkmodnam() {
		return brkmodnam;
	}

	public void setBrkmodnam(String brkmodnam) {
		this.brkmodnam = brkmodnam;
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
