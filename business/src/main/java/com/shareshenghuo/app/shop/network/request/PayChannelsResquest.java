package com.shareshenghuo.app.shop.network.request;

public class PayChannelsResquest extends BaseRequest{
	
	public 	String TERMTYPE; 
	public String ACCOUNT;
	public String TERMNO;
	
	public String ORDAMT;
	public String POINTSID;
	public String CUSTERMTEL;
	public String ABSIMGPATH;
	public String LOGNO ;
	@Override
	public String toString() {
		return "PayChannelsResquest [TERMTYPE=" + TERMTYPE + ", ACCOUNT="
				+ ACCOUNT + ", ORDAMT=" + ORDAMT + ", POINTSID=" + POINTSID
				+ ", CUSTERMTEL=" + CUSTERMTEL + "]";
	}

}
