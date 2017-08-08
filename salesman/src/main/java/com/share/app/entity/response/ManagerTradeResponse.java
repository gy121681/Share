package com.share.app.entity.response;

/**
 * Created by kezhong.
 * QQ:396926020@qq.com
 * on 2017/7/24
 */

public class ManagerTradeResponse {


	/**
	 * areaType : 区域类型,1:省;2:市;3:区
	 * areaId : 省市区id
	 * areaCode : 省市区编码
	 * tradeDate : 交易日期
	 * tradeAmountType1 : 25%交易金额(以分为单位)
	 * tradeAmountType2 : 50%交易金额(以分为单位)
	 * tradeAmountType3 : 100%交易金额(以分为单位)
	 * totalTradeAmount : 总交易金额
	 */

	private String areaType;
	private String areaId;
	private String areaCode;
	private String tradeDate;
	private String tradeAmountType1;
	private String tradeAmountType2;
	private String tradeAmountType3;
	private String totalTradeAmount;

	public String getAreaType() {
		return areaType;
	}

	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getTradeAmountType1() {
		return tradeAmountType1;
	}

	public void setTradeAmountType1(String tradeAmountType1) {
		this.tradeAmountType1 = tradeAmountType1;
	}

	public String getTradeAmountType2() {
		return tradeAmountType2;
	}

	public void setTradeAmountType2(String tradeAmountType2) {
		this.tradeAmountType2 = tradeAmountType2;
	}

	public String getTradeAmountType3() {
		return tradeAmountType3;
	}

	public void setTradeAmountType3(String tradeAmountType3) {
		this.tradeAmountType3 = tradeAmountType3;
	}

	public String getTotalTradeAmount() {
		return totalTradeAmount;
	}

	public void setTotalTradeAmount(String totalTradeAmount) {
		this.totalTradeAmount = totalTradeAmount;
	}
}


