package com.td.qianhai.epay.oem;

import java.math.BigDecimal;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.interfaces.onMyaddTextListener;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.MyEditDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class CreditToDetileActivity1 extends BaseActivity {


	private TextView tv_pro,tv_charge,btn_credit_confirm,card_tvs,name_tvs,balance_tv,card_names,tv_pro1,rate_remind,capital_tvs;
	
	private EditText et_credit_balance;

	private MyEditDialog doubleWarnDialog1;

	private String money, mercnum;
	
	String avaamt ="0";
	
	private OneButtonDialogWarn warnDialog;
	
	private String  ids,car,nam,carname;
	
	private String mobile;
	
	private ImageView card_img;
	
	private double cpaymentminsingamt = 0.0 ,cpaymentminsingamt1 = 0.0;
	
	private String tag = "1",actualamount = "";
	
	private RadioGroup radioGroup1;
	
	private RadioButton radioGroupButton0;
	
	private LinearLayout capital_layout;
	
	private String creet;
	
	private boolean istrue = false;
	

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cretdit_detile1);
		AppContext.getInstance().addActivity(this);
//		mercnum = ((AppContext) getApplication()).getMercNum();
//		mobile = ((AppContext) getApplication()).getMobile();
		
		mercnum = MyCacheUtil.getshared(this).getString("MercNum", "");
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		
		Intent it = getIntent();
		
		nam = it.getStringExtra("nam");
		
		car = it.getStringExtra("car");
		
		ids = it.getStringExtra("ids");
		
		carname = it.getStringExtra("carname");
		
		initview();
		
		GetWalletInfo  walletinfo = new GetWalletInfo();
		
		walletinfo.execute(HttpUrls.PAYMENTCOUPON + "",
				mobile);
		
		
		
		

	}

	private void initview() {
		tv_charge = (TextView) findViewById(R.id.tv_charge);
		tv_charge.setText(Html.fromHtml("<u>还款说明></u>"));
		((TextView) findViewById(R.id.tv_title_contre)).setText("信用卡还款");
		((TextView) findViewById(R.id.bt_title_right1)).setVisibility(View.VISIBLE);
		((TextView) findViewById(R.id.bt_title_right1)).setText("解绑");
		((TextView) findViewById(R.id.bt_title_right)).setVisibility(View.GONE);
		tv_pro = (TextView) findViewById(R.id.tv_pro);
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		findViewById(R.id.bt_title_right1).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						SpannableString msp = new SpannableString("您要删除该信用卡?");
						showDoubleWarnDialog(msp);
					}
				});
		
		tv_charge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(CreditToDetileActivity1.this,ChargingrRulesActivity.class);
				it.putExtra("creet", creet);
				startActivity(it);
			}
		});
		rate_remind = (TextView) findViewById(R.id.rate_remind);
		capital_tvs = (TextView) findViewById(R.id.capital_tvs);
		capital_layout = (LinearLayout) findViewById(R.id.capital_layouts);
		tv_pro1 = (TextView) findViewById(R.id.tv_pro1);
		radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		radioGroupButton0 = (RadioButton) findViewById(R.id.radioGroupButton0);
		card_img = (ImageView) findViewById(R.id.card_img);
		card_names = (TextView) findViewById(R.id.card_names);
		btn_credit_confirm = (TextView) findViewById(R.id.btn_credit_confirm);
		et_credit_balance = (EditText) findViewById(R.id.et_credit_balance);
		card_tvs = (TextView) findViewById(R.id.card_tvs);
		name_tvs = (TextView) findViewById(R.id.name_tvs);
		balance_tv = (TextView) findViewById(R.id.balance_tv);
		if(car!=null&&!car.equals("")){
			card_tvs.setText("尾号"+car.substring(car.length()-4));
		}
		if(nam!=null&&!nam.equals("")){
			name_tvs.setText(nam);
		}
		if(carname!=null&&!carname.equals("")){
			card_names.setText(carname);
		}
		
		initcard();
		btn_credit_confirm.setEnabled(false);
		
		radioGroup1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub
				
				if(checkedId==radioGroupButton0.getId()){
					tag = "1";
					capital_layout.setVisibility(View.VISIBLE);
					Log.e("", "0");
					istrue = false;
				}else{
					capital_layout.setVisibility(View.GONE);
					tag = "0";
					istrue = true;
					Log.e("", "1");
				}
			}
		});
		
		et_credit_balance.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			
				if(s.length()>0){
					if(istrue == false){
						
					capital_layout.setVisibility(View.VISIBLE);
					}
						btn_credit_confirm.setEnabled(true);
						try {
						 actualamount= (Double.parseDouble(et_credit_balance.getText().toString())+(Double.parseDouble(et_credit_balance.getText().toString())*((Double.parseDouble(creet))/100)))+"";
							
//							String str = String.valueOf(a);
//							String[] nstr = a.split("\\.");
//							String b = nstr[1].substring(0,2);
							String c = actualamount.substring(0, actualamount.length()-1);
							
							BigDecimal mData = new BigDecimal(actualamount).setScale(2, BigDecimal.ROUND_HALF_UP);
							rate_remind.setText("信用卡还款 : "+et_credit_balance.getText()+"元 , ");
							capital_tvs.setText("钱包实际扣款  : "+mData+"元");
//							if (Double.parseDouble(s.toString()) > 20000) {
//								etBalanceError.setVisibility(View.VISIBLE);
//								etBalanceError.setText("收款金额不能大于两万整");
//								etBalanceError.setError("收款金额不能大于两万整");
//								etBalance.setVisibility(View.GONE);
//								etBalance.setText("");
//								return;
//							}
						} catch (Exception e) {
							capital_tvs.setText("输入有误。。。");
						}
					
				}else{
					capital_layout.setVisibility(View.GONE);
					btn_credit_confirm.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		btn_credit_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String money = et_credit_balance.getText().toString();
				
				int a = (int) (Double.parseDouble(money) * 100);
				
				if(Double.parseDouble(money)>(Double.parseDouble(avaamt)/100)){
					Toast.makeText(getApplicationContext(), "余额不足", Toast.LENGTH_LONG).show();
					return;
				}
				if((Double.parseDouble(money))<cpaymentminsingamt){
					Toast.makeText(getApplicationContext(), "还款金额不能小于"+cpaymentminsingamt+"元", Toast.LENGTH_LONG).show();
					return;
				}
				if((Double.parseDouble(money))>cpaymentminsingamt1){
					Toast.makeText(getApplicationContext(), "还款金额不能大于"+cpaymentminsingamt1+"元", Toast.LENGTH_LONG).show();
					return;
				}
				
				final String moneys = String.valueOf(a);
				
				doubleWarnDialog1 = new MyEditDialog(
						CreditToDetileActivity1.this, R.style.MyEditDialog,
						"还款", "请输入支付密码", "确认", "取消", "",
						new OnMyDialogClickListener() {

							@Override
							public void onClick(View v) {

								switch (v.getId()) {
								case R.id.btn_right:
									doubleWarnDialog1.dismiss();
									InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
									m.toggleSoftInput(0,
											InputMethodManager.HIDE_NOT_ALWAYS);
									break;
								case R.id.btn_left:
									String paypwd = doubleWarnDialog1
											.getpaypwd();

									if (paypwd == null || paypwd.equals("")) {
										Toast.makeText(getApplicationContext(),"请输入支付密码",
												Toast.LENGTH_SHORT).show();
//										ToastCustom.showMessage(
//												CreditToDetileActivity1.this,
//												"请输入支付密码！");
										return;
									}
									if (paypwd.length() < 6
											|| paypwd.length() > 15) {
										Toast.makeText(getApplicationContext(),"输入的密码长度有误,请输入6～15个数字、字母或特殊符号",
												Toast.LENGTH_SHORT).show();
//										ToastCustom
//												.showMessage(
//														CreditToDetileActivity1.this,
//														"输入的密码长度有误,请输入6～15个数字、字母或特殊符号！");
										return;
									}

									break;
								default:
									break;
								}
							}
						}, new onMyaddTextListener() {

							@Override
							public void refreshActivity(String paypwd) {

								if (paypwd == null || paypwd.equals("")) {
									Toast.makeText(getApplicationContext(),"请输入支付密码",
											Toast.LENGTH_SHORT).show();
//									ToastCustom.showMessage(
//											CreditToDetileActivity1.this,
//											"请输入支付密码！");
									return;
								}
								if (paypwd.length() < 6 || paypwd.length() > 15) {
									Toast.makeText(getApplicationContext(),"输入的密码长度有误,请输入6个数字、字母或特殊符号",
											Toast.LENGTH_SHORT).show();
//									ToastCustom.showMessage(
//											CreditToDetileActivity1.this,
//											"输入的密码长度有误,请输入6个数字、字母或特殊符号！");
									return;
								}
								String actualamount1 = "";
								
								if(!istrue){
									actualamount1 = String.valueOf((int) (Double.parseDouble((Double.parseDouble(et_credit_balance.getText().toString())+(Double.parseDouble(et_credit_balance.getText().toString())*((Double.parseDouble(creet))/100)))+"") * 100));
								}else{
									actualamount1 = moneys;
								}
								CreditToPay payTask = new CreditToPay();
								payTask.execute(HttpUrls.CREDITPAY+ "", mobile,actualamount1,car,tag,paypwd,moneys);

							}
						});
				doubleWarnDialog1.setCancelable(false);
				doubleWarnDialog1.setCanceledOnTouchOutside(false);
				doubleWarnDialog1.show();

			}
		});

	}

	private void initcard() {
		// TODO Auto-generated method stub
		
		if (ids.equals("CMBCHINACREDIT")) {
			card_img.setImageResource(R.drawable.ps_cmb);
		}else if(ids.equals("ABCCREDIT")) {
			card_img.setImageResource(R.drawable.ps_abc);
		}else if(ids.equals("BCCBCREDIT")){
			card_img.setImageResource(R.drawable.ps_bjb);
		}else if(ids.equals("BOCCREDIT")){
			card_img.setImageResource(R.drawable.ps_boc);
		}else if(ids.equals("CCBCREDIT")){
			card_img.setImageResource(R.drawable.ps_ccb);
		}else if(ids.equals("EVERBRIGHTCREDIT")){
			card_img.setImageResource(R.drawable.ps_cebb);
		}else if(ids.equals("CIBCREDIT")){
			card_img.setImageResource(R.drawable.ps_cib);
		}else if(ids.equals("ECITICCREDIT")){
			card_img.setImageResource(R.drawable.ps_citic);
		}else if(ids.equals("CMBCCREDIT")){
			card_img.setImageResource(R.drawable.ps_cmbc);
		}else if(ids.equals("BOCOCREDIT")){
			card_img.setImageResource(R.drawable.ps_comm);
		}else if(ids.equals("HXBCREDIT")){
			card_img.setImageResource(R.drawable.ps_hxb);
		}else if(ids.equals("GDBCREDIT")){
			card_img.setImageResource(R.drawable.ps_gdb);
		}else if(ids.equals("PSBCCREDIT")){
			card_img.setImageResource(R.drawable.ps_psbc);
		}else if(ids.equals("ICBCCREDIT")){
			card_img.setImageResource(R.drawable.ps_icbc);
		}else if(ids.equals("PINGANCREDIT")){
			card_img.setImageResource(R.drawable.ps_spa);
		}else if(ids.equals("SPDBCREDIT")){
			card_img.setImageResource(R.drawable.ps_spdb);
		}else if(ids.equals("SDBCREDIT")){
			card_img.setImageResource(R.drawable.ps_spdb);
		}else if(ids.equals("BSBCREDIT")){
			card_img.setImageResource(R.drawable.ps_bsb);
		}else if(ids.equals("BOSHCREDIT")){
			card_img.setImageResource(R.drawable.ps_sh);
		}else{
			card_img.setImageResource(R.drawable.ps_unionpay);
		}
		}
		
//	}else{
//		card_img.setImageResource(R.drawable.ps_unionpay);
//	}

	/**
	 * 还款
	 * 
	 * 
	 */
	class CreditToPay extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在操作中...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3],params[4],params[5],params[6]};
			return NetCommunicate.getMidatc(HttpUrls.CREDITPAY,
					values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			doubleWarnDialog1.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
//					Intent it = new Intent(CreditToDetileActivity1.this,CreditCardResultActivity.class);
//					it.putExtra("tag", "2");
//					it.putExtra("result", result.get("RSPMSG").toString());
//					startActivity(it);
//					finish();
					
					warnDialog = new OneButtonDialogWarn(CreditToDetileActivity1.this,
							R.style.CustomDialog, "提示", result.get(Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									finish();
									warnDialog.dismiss();
								}
							});
						warnDialog.show();
					
				} else {
//					Intent it = new Intent(CreditToDetileActivity1.this,CreditCardResultActivity.class);
//					it.putExtra("tag", "1");
//					it.putExtra("result", result.get("RSPMSG").toString());
//					startActivity(it);
					warnDialog = new OneButtonDialogWarn(CreditToDetileActivity1.this,
							R.style.CustomDialog, "提示", result.get(Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
//									finish();
									warnDialog.dismiss();
								}
							});
						warnDialog.show();

				}
			} else {
				Toast.makeText(getApplicationContext(),"fail",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(CreditToDetileActivity1.this, "fail");
			}
			super.onPostExecute(result);
		}
	}
	
class GetWalletInfo extends AsyncTask<String , Integer, HashMap<String, Object>>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("请稍候...");
		}
		
		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			
			String[] values = { params[0], params[1] };
			return NetCommunicate
					.getMidatc(HttpUrls.PAYMENTCOUPON, values);
			
		}
		
		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD)!=null&&result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					
					if(result.get("AVAAMT").toString()!=null){
						avaamt = result.get("AVAAMT").toString();
						
						}
					
					if (avaamt.length() == 1) {
						balance_tv.setText("0.0" + avaamt);
					} else if (avaamt.length() == 2) {
						balance_tv.setText("0." + avaamt);
					} else {
						balance_tv.setText(avaamt.substring(0,
								avaamt.length() - 2)
								+ "."
								+ avaamt.substring(
										avaamt.length() - 2));
					}
					if(result.get("PAYMENTMINSINGAMT")!=null){
						String c = result.get("PAYMENTMINSINGAMT").toString();
						cpaymentminsingamt = Double.parseDouble(c)/100;
					}
					
					if(result.get("PAYMENTMAXSINGAMT")!=null){
						 String a = result.get("PAYMENTMAXSINGAMT").toString();
						String b = result.get("PAYMENTCOUNT").toString();
						creet = result.get("URGENTFATE").toString();
						cpaymentminsingamt1 = Double.parseDouble(a)/100;
						et_credit_balance.setHint("单笔限额"+Double.parseDouble(a)/100+"元");//,日上限"+b+"笔");
//						最低"+cpaymentminsingamt+"元,
//						tv_pro1.setText("温馨提示: 信用卡还款单笔限额最低"+cpaymentminsingamt+"元,最高"+Double.parseDouble(a)/100+"元,日上限"+b+"笔");//,T+0还款收"+creet+"%手续费");
					}
//					tv_pro.setText("温馨提示: 信用卡还款1000元,钱包扣款"+(int)(1000+1000*Double.parseDouble(creet)/100)+"元");

					
				} else {
					
					warnDialog = new OneButtonDialogWarn(CreditToDetileActivity1.this,
							R.style.CustomDialog, "提示", result.get(Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									finish();
									warnDialog.dismiss();
								}
							});
					if(warnDialog!=null){
						warnDialog.show();
					}
				}
			} else {
				Toast.makeText(getApplicationContext(),"数据获取失败,请检查网络连接",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(CreditToDetileActivity1.this, "数据获取失败,请检查网络连接");
			}
			super.onPostExecute(result);
			}
		}

	@Override
	protected void doubleWarnOnClick(View v) {
		// TODO Auto-generated method stub
		super.doubleWarnOnClick(v);
		switch (v.getId()) {
		case R.id.btn_left:
			doubleWarnDialog.dismiss();

			break;
		case R.id.btn_right:
			DeleteCreditToPay1 payTask1 = new DeleteCreditToPay1();
			payTask1.execute(HttpUrls.DELETECREDITCARD + "", mobile, car);
			doubleWarnDialog.dismiss();
			break;

		default:
			break;
		}
	}

/**
 * 解绑
 * 
 * 
 */
class DeleteCreditToPay1 extends
		AsyncTask<String, Integer, HashMap<String, Object>> {

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected HashMap<String, Object> doInBackground(String... params) {
		String[] values = { params[0], params[1],params[2]};
		return NetCommunicate.getMidatc(HttpUrls.DELETECREDITCARD,
				values);
	}

	@Override
	protected void onPostExecute(HashMap<String, Object> result) {
		if (result != null) {
			if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
				finish();
				Toast.makeText(getApplicationContext(),result.get("RSPMSG").toString(),
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(CreditToActivity.this,result.get("RSPMSG").toString());
				
			} else {
				Toast.makeText(getApplicationContext(),result.get("RSPMSG").toString(),
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(CreditToActivity.this,result.get("RSPMSG").toString());

			}
		} else {
			Toast.makeText(getApplicationContext(),"fail",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(CreditToActivity.this, "fail");
		}
		super.onPostExecute(result);
	}
}
}
