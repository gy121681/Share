package com.td.qianhai.epay.oem.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class ProvinceEntity extends Entity{
	private String rspcod;
	private String rspmsg;
	private String issno;
    private String issnam;
    private String provid;
    private String pronam;
    private String cityid;
    private String citynam;
    private String bkno;
    private String benelx;
    private String dcflag;
    public String getDcflag() {
		return dcflag;
	}
	public void setDcflag(String dcflag) {
		this.dcflag = dcflag;
	}
	public String getProvid() {
		return provid;
	}
	public void setProvid(String provid) {
		this.provid = provid;
	}
	public String getPronam() {
		return pronam;
	}
	public void setPronam(String pronam) {
		this.pronam = pronam;
	}
	public String getCityid() {
		return cityid;
	}
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	public String getCitynam() {
		return citynam;
	}
	public void setCitynam(String citynam) {
		this.citynam = citynam;
	}
	public String getBkno() {
		return bkno;
	}
	public void setBkno(String bkno) {
		this.bkno = bkno;
	}
	public String getBenelx() {
		return benelx;
	}
	public void setBenelx(String benelx) {
		this.benelx = benelx;
	}
	public ArrayList<HashMap<String, Object>> list;
	public ProvinceEntity() {
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
	public String getIssno() {
		return issno;
	}
	public void setIssno(String issno) {
		this.issno = issno;
	}
	public String getIssnam() {
		return issnam;
	}
	public void setIssnam(String issnam) {
		this.issnam = issnam;
	}
    
    
}