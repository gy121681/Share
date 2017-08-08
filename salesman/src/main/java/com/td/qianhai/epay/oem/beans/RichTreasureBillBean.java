package com.td.qianhai.epay.oem.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 致富宝账单实体
 * 
 * @author Administrator
 * 
 */
public class RichTreasureBillBean implements Serializable {

	/**
	 * 致富宝账单实体UID
	 */
	private static final long serialVersionUID = 1235704358126017811L;
	private String alogno;
	private String operbtyp;
	private String operstyp;
	private String sumamt;
	private String txnamt;
	private String feeamt;
	private String opertim;
	private String operid;
	private String opersts;
	private String souractid;
	private String targactid;
	private String targcardno;
	private String targatcnam;
	private String remark;
	private String sdat;
	private String edat;
	private String rspcod;
	private String rspmsg;
	private String numperpage;
	private String income;
	private String expenditure;
	private String tolcnt;

	public RichTreasureBillBean() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<HashMap<String,Object>>();
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(String expenditure) {
		this.expenditure = expenditure;
	}

	public String getTolcnt() {
		return tolcnt;
	}

	public void setTolcnt(String tolcnt) {
		this.tolcnt = tolcnt;
	}

	public String getNumperpage() {
		return numperpage;
	}

	public void setNumperpage(String numperpage) {
		this.numperpage = numperpage;
	}

	/**
	 * 致富宝未知节点存储list
	 */
	public ArrayList<HashMap<String, Object>> list;

	/**
	 * 获取流水号
	 * 
	 * @return 流水号 alogno
	 */
	public String getAlogno() {
		return alogno;
	}

	/**
	 * 设置流水号
	 * 
	 * @param alogno
	 */
	public void setAlogno(String alogno) {
		this.alogno = alogno;
	}

	/**
	 * 获取操作大类 0转入 2转出 4管理
	 * 
	 * @return 操作大类 0转入 2转出 4管理
	 */
	public String getOperbtyp() {
		return operbtyp;
	}

	/**
	 * 设置操作大类 0转入 2转出 4管理
	 * 
	 * @param operbtyp
	 */
	public void setOperbtyp(String operbtyp) {
		this.operbtyp = operbtyp;
	}

	/**
	 * 获取操作小类 0开头的为转入 01消费转入 02虚拟账户转入 03现金充值 04利息收益转入 2开头的为转出 21 提现到绑定卡 22转出其它账户
	 * 23转出他行卡 24手机充值 4开头其它 41冻结 42解冻 43活转定 44定转活 45转存
	 * 
	 * @return 操作小类 0开头的为转入 01消费转入 02虚拟账户转入 03现金充值 04利息收益转入 2开头的为转出 21 提现到绑定卡
	 *         22转出其它账户 23转出他行卡 24手机充值 4开头其它 41冻结 42解冻 43活转定 44定转活 45转存
	 */
	public String getOperstyp() {
		return operstyp;
	}

	/**
	 * 设置操作小类 0开头的为转入 01消费转入 02虚拟账户转入 03现金充值 04利息收益转入 2开头的为转出 21 提现到绑定卡 22转出其它账户
	 * 23转出他行卡 24手机充值 4开头其它 41冻结 42解冻 43活转定 44定转活 45转存
	 * 
	 * @param operstyp
	 */
	public void setOperstyp(String operstyp) {
		this.operstyp = operstyp;
	}

	/**
	 * 获取总金额
	 * 
	 * @return 总金额
	 */
	public String getSumamt() {
		return sumamt;
	}

	/**
	 * 设置总金额
	 * 
	 * @param sumamt
	 */
	public void setSumamt(String sumamt) {
		this.sumamt = sumamt;
	}

	/**
	 * 获取交易金额
	 * 
	 * @return 交易金额
	 */
	public String getTxnamt() {
		return txnamt;
	}

	/**
	 * 设置交易金额
	 * 
	 * @param txnamt
	 */
	public void setTxnamt(String txnamt) {
		this.txnamt = txnamt;
	}

	/**
	 * 获取佣金 操作大类为 4管理 的时候没有佣金
	 * 
	 * @return
	 */
	public String getFeeamt() {
		return feeamt;
	}

	/**
	 * 设置佣金 操作大类为 4管理 的时候没有佣金
	 * 
	 * @param feeamt
	 */
	public void setFeeamt(String feeamt) {
		this.feeamt = feeamt;
	}

	/**
	 * 获取操作时间
	 * 
	 * @return 操作时间
	 */
	public String getOpertim() {
		return opertim;
	}

	/**
	 * 设置操作人
	 * 
	 * @param opertim
	 */
	public void setOpertim(String opertim) {
		this.opertim = opertim;
	}

	/**
	 * 获取状态 1成功 0失败
	 * 
	 * @return 状态 1成功 0失败
	 */
	public String getOperid() {
		return operid;
	}

	/**
	 * 设置状态 1成功 0失败
	 * 
	 * @param operid
	 */
	public void setOperid(String operid) {
		this.operid = operid;
	}

	/**
	 * 获取转出账户
	 * 
	 * @return
	 */
	public String getOpersts() {
		return opersts;
	}

	/**
	 * 设置转出账户
	 * 
	 * @param opersts
	 */
	public void setOpersts(String opersts) {
		this.opersts = opersts;
	}

	/**
	 * 获取转入账户
	 * 
	 * @return 转入账户
	 */
	public String getSouractid() {
		return souractid;
	}

	/**
	 * 设置转入账户
	 * 
	 * @param souractid
	 */
	public void setSouractid(String souractid) {
		this.souractid = souractid;
	}

	/**
	 * 获取转入账户
	 * 
	 * @return 转入账户
	 */
	public String getTargactid() {
		return targactid;
	}

	/**
	 * 设置转入账户
	 * 
	 * @param targactid
	 */
	public void setTargactid(String targactid) {
		this.targactid = targactid;
	}

	/**
	 * 获取转出卡号
	 * 
	 * @return 转出卡号
	 */
	public String getTargcardno() {
		return targcardno;
	}

	/**
	 * 设置转出卡号
	 * 
	 * @param targcardno
	 */
	public void setTargcardno(String targcardno) {
		this.targcardno = targcardno;
	}

	/**
	 * 获取转卡卡户名
	 * 
	 * @return 转卡卡户名
	 */
	public String getTargatcnam() {
		return targatcnam;
	}

	/**
	 * 设置转卡卡户名
	 * 
	 * @param targatcnam
	 */
	public void setTargatcnam(String targatcnam) {
		this.targatcnam = targatcnam;
	}

	/**
	 * 获取备注
	 * 
	 * @return 备注
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置备注
	 * 
	 * @param remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取开始日期
	 * 
	 * @return 开始日期
	 */
	public String getSdat() {
		return sdat;
	}

	/**
	 * 设置开始日期
	 * 
	 * @param sdat
	 */
	public void setSdat(String sdat) {
		this.sdat = sdat;
	}

	/**
	 * 获取结束时间
	 * 
	 * @return 结束时间
	 */
	public String getEdat() {
		return edat;
	}

	/**
	 * 设置结束时间
	 * 
	 * @param sdat
	 */
	public void setEdat(String edat) {
		this.edat = edat;
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
