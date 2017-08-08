package com.shareshenghuo.app.user.network.request;

public class getUserOrShopRequest extends BaseRequest{
	
	public String account;
	public String userType;
	
	public String investmentUserType;
	public String investmentUserId;
	public String investmentAccount;
	public String filialPietyNum;
	public String investmentType;
	public String userOrShopId;
	public String investmentProjectId;
	public String payPassword;
	
	//--临时加入
	public String channelType;//产业链,公益
	
	@Override
	public String toString() {
		return "getUserOrShopRequest [account=" + account + ", userType="
				+ userType + ", investmentUserType=" + investmentUserType
				+ ", investmentUserId=" + investmentUserId
				+ ", investmentAccount=" + investmentAccount
				+ ", filialPietyNum=" + filialPietyNum + ", investmentType="
				+ investmentType + ", userOrShopId=" + userOrShopId
				+ ", investmentProjectId=" + investmentProjectId + "]";
	}
}
