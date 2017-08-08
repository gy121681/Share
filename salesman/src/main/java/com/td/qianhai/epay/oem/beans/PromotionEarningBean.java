package com.td.qianhai.epay.oem.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class PromotionEarningBean {

	private String ryearmonth;
	private String rday;
	private String incomamt;
	private String membernum;
	private String typ;
	private String rspcod;
	private String rspmsg;
	private String tolcnt;
	
	public ArrayList<HashMap<String, Object>> list;
	
	public PromotionEarningBean() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<HashMap<String,Object>>();
	}

	public String getRyearmonth() {
		return ryearmonth;
	}

	public void setRyearmonth(String ryearmonth) {
		this.ryearmonth = ryearmonth;
	}

	public String getRday() {
		return rday;
	}

	public void setRday(String rday) {
		this.rday = rday;
	}

	public String getIncomamt() {
		return incomamt;
	}

	public void setIncomamt(String incomamt) {
		this.incomamt = incomamt;
	}

	public String getMembernum() {
		return membernum;
	}

	public void setMembernum(String membernum) {
		this.membernum = membernum;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
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

	@Override
	public String toString() {
		return "PromotionEarningBean [ryearmonth=" + ryearmonth + ", rday="
				+ rday + ", incomamt=" + incomamt + ", membernum=" + membernum
				+ ", typ=" + typ + ", rspcod=" + rspcod + ", rspmsg=" + rspmsg
				+ ", tolcnt=" + tolcnt + ", list=" + list + "]";
	}

}
