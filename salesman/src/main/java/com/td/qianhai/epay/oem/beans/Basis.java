package com.td.qianhai.epay.oem.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Basis implements Serializable {

	private String actid;
	private String sumprincipal;
	private String pagenum;
	private String numperpage;
	private String tolcnt;
	private String dptlogno;
	private String dptmnam;
	private String principal;
	private String dptcycle;
	private String dptrate;
	private String expinterest;
	private String expsumamt;
	private String opendat;
	private String enddat;
	private String svakind;
	private String currency;
	private String modsts;
	private String rspcod;
	private String rspmsg;
	private HashMap<String, Object> positionList;
	public ArrayList<HashMap<String, Object>> list;

	public Basis() {
		super();
		list = new ArrayList<HashMap<String, Object>>();
	}
	
	public HashMap<String, Object> getPositionList() {
		return positionList;
	}



	public void setPositionList(HashMap<String, Object> positionList) {
		this.positionList = positionList;
	}



	public String getActid() {
		return actid;
	}

	public void setActid(String actid) {
		this.actid = actid;
	}

	public String getSumprincipal() {
		return sumprincipal;
	}

	public void setSumprincipal(String sumprincipal) {
		this.sumprincipal = sumprincipal;
	}

	public String getPagenum() {
		return pagenum;
	}

	public void setPagenum(String pagenum) {
		this.pagenum = pagenum;
	}

	public String getNumperpage() {
		return numperpage;
	}

	public void setNumperpage(String numperpage) {
		this.numperpage = numperpage;
	}

	public String getTolcnt() {
		return tolcnt;
	}

	public void setTolcnt(String tolcnt) {
		this.tolcnt = tolcnt;
	}

	public String getDptlogno() {
		return dptlogno;
	}

	public void setDptlogno(String dptlogno) {
		this.dptlogno = dptlogno;
	}

	public String getDptmnam() {
		return dptmnam;
	}

	public void setDptmnam(String dptmnam) {
		this.dptmnam = dptmnam;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getDptcycle() {
		return dptcycle;
	}

	public void setDptcycle(String dptcycle) {
		this.dptcycle = dptcycle;
	}

	public String getDptrate() {
		return dptrate;
	}

	public void setDptrate(String dptrate) {
		this.dptrate = dptrate;
	}

	public String getExpinterest() {
		return expinterest;
	}

	public void setExpinterest(String expinterest) {
		this.expinterest = expinterest;
	}

	public String getExpsumamt() {
		return expsumamt;
	}

	public void setExpsumamt(String expsumamt) {
		this.expsumamt = expsumamt;
	}

	public String getOpendat() {
		return opendat;
	}

	public void setOpendat(String opendat) {
		this.opendat = opendat;
	}

	public String getEnddat() {
		return enddat;
	}

	public void setEnddat(String enddat) {
		this.enddat = enddat;
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

	public String getModsts() {
		return modsts;
	}

	public void setModsts(String modsts) {
		this.modsts = modsts;
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
