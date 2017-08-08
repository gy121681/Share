package com.shareshenghuo.app.shop.network.response;

import java.util.List;

import com.shareshenghuo.app.shop.network.bean.MyBankCardBean;
import com.shareshenghuo.app.shop.network.request.MyBankCardRequest;

public class BankCardListResponse extends BaseResponse{
	
	public List<MyBankCardRequest> data;

}
