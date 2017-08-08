package com.td.qianhai.epay.oem.beans;

import java.util.ArrayList;
import java.util.HashMap;

public class MyCircleBean {
	
	private String mercnam;
	
	private String merphonenumber;
	
	private String applydat;
	
	private String rspcod;
	
	private String rspmsg;
	
	private String tolcnt;
	
	private String totshramt;
	
	private String personpic;
	
	private String isseniormember;
	
	private String isretailers;
	
	private String issaleagt;
	
	private String isgeneralagent;
	
	public ArrayList<HashMap<String, Object>> list;
	
	public MyCircleBean() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<HashMap<String,Object>>();
	}
	
	public String getTotshramt() {
		return totshramt;
	}

	public void setTotshramt(String totshramt) {
		this.totshramt = totshramt;
	}



	public String getTolcnt() {
		return tolcnt;
	}

	public void setTolcnt(String tolcnt) {
		this.tolcnt = tolcnt;
	}


	public String getMercnam() {
		return mercnam;
	}

	public void setMercnam(String mercnam) {
		this.mercnam = mercnam;
	}

	public String getMerphonenumber() {
		return merphonenumber;
	}

	public void setMerphonenumber(String merphonenumber) {
		this.merphonenumber = merphonenumber;
	}

	public String getApplydat() {
		return applydat;
	}

	public void setApplydat(String applydat) {
		this.applydat = applydat;
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
	
	public String getPersonpic() {
		return personpic;
	}

	public void setPersonpic(String personpic) {
		this.personpic = personpic;
	}

	public String getIsseniormember() {
		return isseniormember;
	}

	public void setIsseniormember(String isseniormember) {
		this.isseniormember = isseniormember;
	}

	public String getIsretailers() {
		return isretailers;
	}

	public void setIsretailers(String isretailers) {
		this.isretailers = isretailers;
	}

	public String getIssaleagt() {
		return issaleagt;
	}

	public void setIssaleagt(String issaleagt) {
		this.issaleagt = issaleagt;
	}

	public String getIsgeneralagent() {
		return isgeneralagent;
	}

	public void setIsgeneralagent(String isgeneralagent) {
		this.isgeneralagent = isgeneralagent;
	}

	@Override
	public String toString() {
		return "MyCircleBean [mercnam=" + mercnam + ", merphonenumber="
				+ merphonenumber + ", applydat=" + applydat + ", rspcod="
				+ rspcod + ", rspmsg=" + rspmsg + "]";
	}

}
