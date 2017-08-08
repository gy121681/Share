package com.td.qianhai.epay.oem.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class ShareDetailsBean {
	
	private String logno;
	
	private String tottxnamt;
	
	private String agtshramt;
	
	private String bjstrate;
	
	private String ratediff;
	
	private String txndate;
	
	private String txntime;
	
	private String shrsts;
	
	private String shrtype;
	
	private String rspcod;
	
	private String rspmsg;
	
	private String tolcnt;
	
	public ArrayList<HashMap<String, Object>> list;
	
	public ShareDetailsBean() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<HashMap<String,Object>>();
	}

	
	public String getTolcnt() {
		return tolcnt;
	}

	public void setTolcnt(String tolcnt) {
		this.tolcnt = tolcnt;
	}

	public String getLogno() {
		return logno;
	}

	public void setLogno(String logno) {
		this.logno = logno;
	}

	public String getTottxnamt() {
		return tottxnamt;
	}

	public void setTottxnamt(String tottxnamt) {
		this.tottxnamt = tottxnamt;
	}

	public String getAgtshramt() {
		return agtshramt;
	}

	public void setAgtshramt(String agtshramt) {
		this.agtshramt = agtshramt;
	}

	public String getBjstrate() {
		return bjstrate;
	}

	public void setBjstrate(String bjstrate) {
		this.bjstrate = bjstrate;
	}

	public String getRatediff() {
		return ratediff;
	}

	public void setRatediff(String ratediff) {
		this.ratediff = ratediff;
	}

	public String getTxndate() {
		return txndate;
	}

	public void setTxndate(String txndate) {
		this.txndate = txndate;
	}

	public String getTxntime() {
		return txntime;
	}

	public void setTxntime(String txntime) {
		this.txntime = txntime;
	}

	public String getShrsts() {
		return shrsts;
	}

	public void setShrsts(String shrsts) {
		this.shrsts = shrsts;
	}

	public String getShrtype() {
		return shrtype;
	}

	public void setShrtype(String shrtype) {
		this.shrtype = shrtype;
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

	@Override
	public String toString() {
		return "ShareDetailsBean [logno=" + logno + ", tottxnamt=" + tottxnamt
				+ ", agtshramt=" + agtshramt + ", bjstrate=" + bjstrate
				+ ", ratediff=" + ratediff + ", txndate=" + txndate
				+ ", txntime=" + txntime + ", shrsts=" + shrsts + ", shrtype="
				+ shrtype + ", rspcod=" + rspcod + ", rspmsg=" + rspmsg + "]";
	}

}
