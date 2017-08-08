package com.shareshenghuo.app.shop.network.bean;

public class BannerBean {
	
	public String sumTotalConsumption;//：全联盟总消费额
	public String countShop;//：商户数
	public String countUser;//：用户数
	public String payDate;//：日期
	public String userFilialPric;//：用户秀心单价
	public String shopFilialPric;//：商家秀心单价
	public String oneSunTotalFee;//：25%积分 总消费额
	public String twoSunTotalFee;//：50%积分 总消费额
	public String threeSunTotalFee;//：100%积分 总消费
	public String openDate;
	public String isStop;
	
	public String userFilialPricRate;
	public String shopFilialPricRate;
	
	public String sumTotalConsumptionNew;//：全联盟总消费额
//	public String countShopNew;//：商户数
//	public String countUserNew;//：用户数
	public String payDateNew;//：日期
	public String userFilialPricNew;//：用户秀心单价
	public String shopFilialPricNew;//：商家秀心单价
	public String oneSunTotalFeeNew;//：25%积分 总消费额
	public String twoSunTotalFeeNew;//：50%积分 总消费额
	public String threeSunTotalFeeNew;//：100%积分 总消费
	
	public String userFilialPricRateNew;
	public String shopFilialPricRateNew;
	
	
	
	@Override
	public String toString() {
		return "BannerBean [sumTotalConsumption=" + sumTotalConsumption
				+ ", countShop=" + countShop + ", countUser=" + countUser
				+ ", payDate=" + payDate + ", userFilialPric=" + userFilialPric
				+ ", shopFilialPric=" + shopFilialPric + ", oneSunTotalFee="
				+ oneSunTotalFee + ", twoSunTotalFee=" + twoSunTotalFee
				+ ", threeSunTotalFee=" + threeSunTotalFee + "]";
	}

}
