package com.td.qianhai.epay.oem.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 订单支付bean
 * 
 * @author liangge
 * 
 */
public class OrderPayBean implements Serializable {
	private String startDate;// 开始时间
	private String endDate;// 结束时间
	private String mobile;// 手机号
	private String pagNumber;// 当前页
	private String numberPage;// 每页记录数
	private String tolcnt;// 总记录数
	private String orderNo; // 订单号
	private String ordAtm;// 订单金额
	private String orderTim;// 支付时间
	private String transts;// 支付状态
	private String prdordType;// 订单类型
	private String prdordNo;// 商品订单号
	private String tn;// 交易流水
	private String qn;// 查询流水
	private String rspcod;
	private String rspmsg;
	private String clsdat;//清算时间
	private String feerat;//清算费率
	private String clsamt;//清算金额
	private String frramt;//手续费
	private String clssts;//清算状态
	
	public String getClsdat() {
		return clsdat;
	}

	public void setClsdat(String clsdat) {
		this.clsdat = clsdat;
	}

	public String getFeerat() {
		return feerat;
	}

	public void setFeerat(String feerat) {
		this.feerat = feerat;
	}

	public String getClsamt() {
		return clsamt;
	}

	public void setClsamt(String clsamt) {
		this.clsamt = clsamt;
	}

	public String getFrramt() {
		return frramt;
	}

	public void setFrramt(String frramt) {
		this.frramt = frramt;
	}

	public String getClssts() {
		return clssts;
	}

	public void setClssts(String clssts) {
		this.clssts = clssts;
	}

	public ArrayList<HashMap<String, Object>> getList() {
		return list;
	}

	public void setList(ArrayList<HashMap<String, Object>> list) {
		this.list = list;
	}

	public OrderPayBean() {
		list = new ArrayList<HashMap<String,Object>>();
		
	}
	
	public ArrayList<HashMap<String, Object>> list;

	public String getRspcod() {
		return rspcod;
	}

	public String getRspmsg() {
		return rspmsg;
	}

	public void setRspcod(String rspcod) {
		this.rspcod = rspcod;
	}

	public void setRspmsg(String rspmsg) {
		this.rspmsg = rspmsg;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getMobile() {
		return mobile;
	}

	public String getPagNumber() {
		return pagNumber;
	}

	public String getNumberPage() {
		return numberPage;
	}

	public String getTolcnt() {
		return tolcnt;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public String getOrdAtm() {
		return ordAtm;
	}

	public String getOrderTim() {
		return orderTim;
	}

	public String getTransts() {
		return transts;
	}

	public String getPrdordType() {
		return prdordType;
	}

	public String getPrdordNo() {
		return prdordNo;
	}

	public String getTn() {
		return tn;
	}

	public String getQn() {
		return qn;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setPagNumber(String pagNumber) {
		this.pagNumber = pagNumber;
	}

	public void setNumberPage(String numberPage) {
		this.numberPage = numberPage;
	}

	public void setTolcnt(String tolcnt) {
		this.tolcnt = tolcnt;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public void setOrdAtm(String ordAtm) {
		this.ordAtm = ordAtm;
	}

	public void setOrderTim(String orderTim) {
		this.orderTim = orderTim;
	}

	public void setTransts(String transts) {
		this.transts = transts;
	}

	public void setPrdordType(String prdordType) {
		this.prdordType = prdordType;
	}

	public void setPrdordNo(String prdordNo) {
		this.prdordNo = prdordNo;
	}

	public void setTn(String tn) {
		this.tn = tn;
	}

	public void setQn(String qn) {
		this.qn = qn;
	}

	@Override
	public String toString() {
		return "OrderPayBean [startDate=" + startDate + ", endDate=" + endDate
				+ ", mobile=" + mobile + ", pagNumber=" + pagNumber
				+ ", numberPage=" + numberPage + ", tolcnt=" + tolcnt
				+ ", orderNo=" + orderNo + ", ordAtm=" + ordAtm + ", orderTim="
				+ orderTim + ", transts=" + transts + ", prdordType="
				+ prdordType + ", prdordNo=" + prdordNo + ", tn=" + tn
				+ ", qn=" + qn + ", rspcod=" + rspcod + ", rspmsg=" + rspmsg
				+ ", list=" + list + "]";
	}
	
	
}
