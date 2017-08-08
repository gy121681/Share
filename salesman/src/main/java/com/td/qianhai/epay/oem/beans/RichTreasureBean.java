package com.td.qianhai.epay.oem.beans;

import java.io.Serializable;

public class RichTreasureBean implements Serializable {

	private String logsts;// 登录状态
	private String merNam;// 商户姓名
	private String actsts;// 账户状态
	private String totamt;// 总金额
	private String fixamt;// 定存金额
	private String checkamt;// 活期金额
	private String avaamt; // 可用资金(元)
	private String frzamt; // 冻结资金(元)
	private String dptrate; // 挂牌利率(%)
	private String cumulative; // 累计收益
	private String milincom; // 万份收益(元)
	private String weekincom; // 近一周收益(元)
	private String monthincom; // 近一月收益(元)
	private String yesterincom; // 昨日收益(元)
	private String actcard; // 卡号
	private String banknam; // 银行卡名称
	private String crdflg; // 卡标识
	private String isActpwout; //提示密码输错是否超限(0/1)

	public String getIsActpwout() {
		return isActpwout;
	}

	public void setIsActpwout(String isActpwout) {
		this.isActpwout = isActpwout;
	}

	public String getLogsts() {
		return logsts;
	}

	public void setLogsts(String logsts) {
		this.logsts = logsts;
	}

	public String getActcard() {
		return actcard;
	}

	public void setActcard(String actcard) {
		this.actcard = actcard;
	}

	public String getBanknam() {
		return banknam;
	}

	public void setBanknam(String banknam) {
		this.banknam = banknam;
	}

	public String getCrdflg() {
		return crdflg;
	}

	public void setCrdflg(String crdflg) {
		this.crdflg = crdflg;
	}

	public String getYesterincom() {
		return yesterincom;
	}

	public void setYesterincom(String yesterincom) {
		this.yesterincom = yesterincom;
	}

	public String getMerNam() {
		return merNam;
	}

	public void setMerNam(String merNam) {
		this.merNam = merNam;
	}

	public String getActsts() {
		return actsts;
	}

	public void setActsts(String actsts) {
		this.actsts = actsts;
	}

	public String getTotamt() {
		return totamt;
	}

	public void setTotamt(String totamt) {
		this.totamt = totamt;
	}

	public String getFixamt() {
		return fixamt;
	}

	public void setFixamt(String fixamt) {
		this.fixamt = fixamt;
	}

	public String getCheckamt() {
		return checkamt;
	}

	public void setCheckamt(String checkamt) {
		this.checkamt = checkamt;
	}

	public String getAvaamt() {
		return avaamt;
	}

	public void setAvaamt(String avaamt) {
		this.avaamt = avaamt;
	}

	public String getFrzamt() {
		return frzamt;
	}

	public void setFrzamt(String frzamt) {
		this.frzamt = frzamt;
	}

	public String getDptrate() {
		return dptrate;
	}

	public void setDptrate(String dptrate) {
		this.dptrate = dptrate;
	}

	public String getCumulative() {
		return cumulative;
	}

	public void setCumulative(String cumulative) {
		this.cumulative = cumulative;
	}

	public String getMilincom() {
		return milincom;
	}

	public void setMilincom(String milincom) {
		this.milincom = milincom;
	}

	public String getWeekincom() {
		return weekincom;
	}

	public void setWeekincom(String weekincom) {
		this.weekincom = weekincom;
	}

	public String getMonthincom() {
		return monthincom;
	}

	public void setMonthincom(String monthincom) {
		this.monthincom = monthincom;
	}
}
