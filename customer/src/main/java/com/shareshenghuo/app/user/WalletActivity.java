//package com.shareshenghuo.app.user;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.EditText;
//
//import com.shareshenghuo.app.user.R;
//import com.shareshenghuo.app.user.fragment.WalletRechargeLogFragment;
//import com.shareshenghuo.app.user.fragment.WithdrawLogFragment;
//import UserInfoManager;
//import com.shareshenghuo.app.user.network.bean.UserInfo;
//import MyTabView;
//
//public class WalletActivity extends BaseTopActivity implements OnClickListener {
//	
//	private EditText edFee;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_wallet);
//		initView();
//	}
//	
//	public void initView() {
//		edFee = getView(R.id.edRechargeFee);
//		
//		getView(R.id.ivBack).setOnClickListener(this);
//		getView(R.id.tvWalletDetail).setOnClickListener(this);
//		findViewById(R.id.llWalletRecharge).setOnClickListener(this);
//		findViewById(R.id.llWalletWithdraw).setOnClickListener(this);
//	}
//
//	@Override
//	protected void onStart() {
//		super.onStart();
//		UserInfo userInfo = UserInfoManager.getUserInfo(this);
//		setText(R.id.tvWalletMoney, "¥"+userInfo.money);
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch(v.getId()) {
//		case R.id.ivBack:
//			finish();
//			break;
//			
//		case R.id.tvWalletDetail:
//			startActivity(new Intent(this, WalletDetailActivity.class));
//			break;
//			
//		case R.id.llWalletRecharge:
//			Intent it = new Intent(this, RechargeActivity.class);
//			it.putExtra("fee", edFee.getText().toString());
//			startActivity(it);
//			break;
//			
//		case R.id.llWalletWithdraw:
//			withdraw();
//			break;
//		}
//	}
//	
//	private int index = 0;
//	public void withdraw() {
//		String[] items = {"提现到支付宝", "提现到微信钱包"};
//		new AlertDialog.Builder(this).setTitle("提现")
//		.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int which) {
//				index = which;
//			}
//		})
//		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				Intent it = new Intent(WalletActivity.this, WithdrawActivity.class);
//				it.putExtra("source", index+1);
//				startActivity(it);
//			}
//		})
//		.setNegativeButton("取消", null)
//		.show();
//	}
//}
