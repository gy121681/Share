package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.UserInfo;
import com.shareshenghuo.app.shop.network.request.WithdrawRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.Arith;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;

public class WithdrawActivity extends BaseTopActivity {
	
	private EditText edAccount;
	private EditText edMoney;
	
	private int source;	//1支付宝 2微信 3银行卡
	
	private UserInfo user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw);
		init();
	}
	
	public void init() {
//		source = getIntent().getIntExtra("source", 1);
		user = UserInfoManager.getUserInfo(this);
		source = user.source;
		
		initTopBar("提现");
		edAccount = getView(R.id.edWithdrawAccount);
		edMoney = getView(R.id.edWithdrawMoney);
		
		edAccount.setText(user.alipay_account);
		
		setText(R.id.tvWithdrawMoney, user.money+"");
		if(source == 1)
			setText(R.id.tvWithdrawSource, "支付宝账号：");
		else if(source == 2)
			setText(R.id.tvWithdrawSource, "微信账号：");
		else if(source == 3)
			setText(R.id.tvWithdrawSource, "银行卡号：");
		
		getView(R.id.btnWithdraw).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				if(ViewUtil.checkEditEmpty(edAccount, "请填写账号"))
//					return;
				if(ViewUtil.checkEditEmpty(edMoney, "请输入金额"))
					return;
				
				double money = Double.parseDouble(edMoney.getText().toString());
				if(money > user.money) {
					T.showShort(WithdrawActivity.this, "余额不足");
					return;
				}
				
				withdraw(edAccount.getText().toString(), money);
			}
		});
	}
	
	public void withdraw(final String account, final double money) {
		Intent it = new Intent(WithdrawActivity.this, VCodeActivity.class);
		it.putExtra("source", source);
		it.putExtra("account", account);
		it.putExtra("money", money);
		startActivityForResult(it, 1);
	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent arg2) {
		if(reqCode==1 && resCode==RESULT_OK)
			finish();
	}
}
