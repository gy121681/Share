package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.views.AnimationUtil;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class RateMianActivity extends BaseActivity {
	
	private TextView rate_num,btn_rate_update,bt_title_left,tv_title_contre;
	private OneButtonDialogWarn warnDialog;
	private String phone;
	private ArrayList<HashMap<String, Object>> list;
	private String ratenoein,oemfeet;
	private String attStr,sts;
	private SharedPreferences share;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rate_userinfo_activity);
		AppContext.getInstance().addActivity(this);
		
		attStr =  MyCacheUtil.getshared(this).getString("MERSTS", "");
		sts = MyCacheUtil.getshared(this).getString("STS", "");
//		initview();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		share = MyCacheUtil.getshared(RateMianActivity.this);
		ratenoein = share.getString("nocardfeerate","");
		oemfeet = share.getString("oemfeerate","");
		rate_num = (TextView) findViewById(R.id.rate_num);
		
		AnimationUtil.ScaleAnimations(rate_num);
		initview();
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		rate_num.setText(ratenoein+"%");
	}

	private void initview() {
//		phone = ((AppContext)getApplication()).getMobile();
		phone =share.getString("Mobile", "");
		bt_title_left = (TextView) findViewById(R.id.bt_title_left);
		tv_title_contre = (TextView) findViewById(R.id.tv_title_contre);
		tv_title_contre.setText("费率");
		btn_rate_update = (TextView) findViewById(R.id.btn_rate_update);
		
//		if(ratenoein.equals("0.49")){
//			btn_rate_update.setEnabled(false);
//			btn_rate_update.setText("不可");
//		}
		
		
		btn_rate_update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				Float	ratenoeins = Float.parseFloat(ratenoein);
				Float	oemfeets = Float.parseFloat(oemfeet);
				if(ratenoeins<=oemfeets){
					warnDialog = new OneButtonDialogWarn(RateMianActivity.this,
							R.style.CustomDialog, "提示", "当前费率已不可升级", "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									btn_rate_update.setEnabled(false);
									warnDialog.dismiss();
								}
							});

					warnDialog.show();
				}else{
//					if(((AppContext)getApplication()).getTxnsts().equals("0")){
//						if(attStr.equals("0")&&sts.equals("0")){
							Intent intent = new Intent(RateMianActivity.this,
									PremiumUpgradeActivity.class);
//							RateListActivity
							startActivity(intent);
//							}else if(sts.equals("1")){
//								ToastCustom.showMessage(RateMianActivity.this,
//										"用户信息正在审核中,暂无法购买费率");
//							}else if(sts.equals("4")){
//								ToastCustom.showMessage(RateMianActivity.this,
//										"您的手机号修改正在审核中,暂无法购买费率");
//							}else{
//								warnDialog = new OneButtonDialogWarn(RateMianActivity.this,
//										R.style.CustomDialog, "提示", "用户权限！请补全资料待审核通过后重试", "确定",
//										new OnMyDialogClickListener() {
//											@Override
//											public void onClick(View v) {
//												Intent it = new Intent(RateMianActivity.this,NewRealNameAuthenticationActivity.class);
//												startActivity(it);
//												finish();
//												warnDialog.dismiss();
//											}
//										});
//								warnDialog.show();
//							}
//					}else{
//						warnDialog = new OneButtonDialogWarn(RateMianActivity.this,
//								R.style.CustomDialog, "提示",
//								"尊敬的用户,请先充值再操作", "确定",
//								new OnMyDialogClickListener() {
//									@Override
//									public void onClick(View v) {
//										Intent it = new Intent(
//												RateMianActivity.this,
//												OrderPayActivity.class);
//										startActivity(it);
//										warnDialog.dismiss();
//									}
//								});
//						warnDialog.show();
//					}
//
				}
			}
		});
		bt_title_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				finish();
				
			}
		});

	}

}
