package com.shareshenghuo.app.shop;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.shareshenghuo.app.shop.R;

public class WithdrawResultActivity extends BaseTopActivity {
	
	private int source;
	private String account;
	private double money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw_result);
		init();
	}
	
	public void init() {
		source = getIntent().getIntExtra("source", 1);
		account = getIntent().getStringExtra("account");
		money = getIntent().getDoubleExtra("money", 0);
		
		initTopBar("提现状态");
		if(source == 1)
			setText(R.id.tvWithdrawSource, "支付宝");
		else if(source == 2)
			setText(R.id.tvWithdrawSource, "微信");
		else if(source == 3)
			setText(R.id.tvWithdrawSource, "银行卡");
		setText(R.id.tvWithdrawAccount, account);
		setText(R.id.tvWithdrawMoney, "¥"+money);
		
		getView(R.id.btnWithdrawComplete).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
