package com.shareshenghuo.app.user;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.shareshenghuo.app.user.R;

/**
 * @author hang
 *	提现结果
 */
public class WithdrawResultActivity extends BaseTopActivity {
	
	private String[] sourceAccount = {"支付宝", "微信"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw_result);
		init();
	}
	
	public void init() {
		initTopBar("提现状态");
		llTopBack.setVisibility(View.GONE);
		
		setText(R.id.tvWithdrawSource, sourceAccount[getIntent().getIntExtra("source", 1)-1]);
		setText(R.id.tvWithdrawAccount, getIntent().getStringExtra("account"));
		setText(R.id.tvWithdrawMoney, "¥"+getIntent().getStringExtra("money"));
		
		findViewById(R.id.btnWithdrawComplete).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
