package com.td.qianhai.epay.oem.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class MoneyListBean {
	
	
	private String Midoemid;// 产品ID	
	private String oemid; //OEMID
	private String instatus;// 转入状态 
	private String outstatus; //转出状态 0 不可转 1可转	
	private String dptrate; // 日利息（%）0 不可转 1可转	
	private String mininamt;//最小转入金额
	private String maxinamt;// 最大转入金额
	private String updatim; // 更新时间 
	private String daysumamt;//日最大转入金额	
	private String dayinnum;//日转入次数
	private String dayoutnum;//日转出次数
	private String minoutintegral; //最低兑换积分 
	private String maxoutintegral;//最高兑换积分
	private String dayoutintegral;//日最高兑换积分	        
	private String status;//状态 0不可用 1可用
	private String  Dayintegralnum; //日最大兑换次数 
	private String minoutamt;//最小转出金额
	private String maxoutamt;//最大转出金额
	private String dayoutsumamt; //日转出总金额
	private String rspcod;
	private String rspmsg;
	/**
	 * 节点存储list
	 */
	public ArrayList<HashMap<String, Object>> list;

	
	public MoneyListBean() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<HashMap<String,Object>>();
	}


	public String getMidoemid() {
		return Midoemid;
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


	public void setMidoemid(String midoemid) {
		Midoemid = midoemid;
	}


	public String getOemid() {
		return oemid;
	}


	public void setOemid(String oemid) {
		this.oemid = oemid;
	}


	public String getInstatus() {
		return instatus;
	}


	public void setInstatus(String instatus) {
		this.instatus = instatus;
	}


	public String getOutstatus() {
		return outstatus;
	}


	public void setOutstatus(String outstatus) {
		this.outstatus = outstatus;
	}


	public String getDptrate() {
		return dptrate;
	}


	public void setDptrate(String dptrate) {
		this.dptrate = dptrate;
	}


	public String getMininamt() {
		return mininamt;
	}


	public void setMininamt(String mininamt) {
		this.mininamt = mininamt;
	}


	public String getMaxinamt() {
		return maxinamt;
	}


	public void setMaxinamt(String maxinamt) {
		this.maxinamt = maxinamt;
	}


	public String getUpdatim() {
		return updatim;
	}


	public void setUpdatim(String updatim) {
		this.updatim = updatim;
	}


	public String getDaysumamt() {
		return daysumamt;
	}


	public void setDaysumamt(String daysumamt) {
		this.daysumamt = daysumamt;
	}


	public String getDayinnum() {
		return dayinnum;
	}


	public void setDayinnum(String dayinnum) {
		this.dayinnum = dayinnum;
	}


	public String getDayoutnum() {
		return dayoutnum;
	}


	public void setDayoutnum(String dayoutnum) {
		this.dayoutnum = dayoutnum;
	}


	public String getMinoutintegral() {
		return minoutintegral;
	}


	public void setMinoutintegral(String minoutintegral) {
		this.minoutintegral = minoutintegral;
	}


	public String getMaxoutintegral() {
		return maxoutintegral;
	}


	public void setMaxoutintegral(String maxoutintegral) {
		this.maxoutintegral = maxoutintegral;
	}


	public String getDayoutintegral() {
		return dayoutintegral;
	}


	public void setDayoutintegral(String dayoutintegral) {
		this.dayoutintegral = dayoutintegral;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getDayintegralnum() {
		return Dayintegralnum;
	}


	public void setDayintegralnum(String dayintegralnum) {
		Dayintegralnum = dayintegralnum;
	}


	public String getMinoutamt() {
		return minoutamt;
	}


	public void setMinoutamt(String minoutamt) {
		this.minoutamt = minoutamt;
	}


	public String getMaxoutamt() {
		return maxoutamt;
	}


	public void setMaxoutamt(String maxoutamt) {
		this.maxoutamt = maxoutamt;
	}


	public String getDayoutsumamt() {
		return dayoutsumamt;
	}


	public void setDayoutsumamt(String dayoutsumamt) {
		this.dayoutsumamt = dayoutsumamt;
	}

}
