package com.td.qianhai.epay.oem;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.RichTreasureBean;
import com.td.qianhai.epay.oem.interfaces.onMyaddTextListener;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.MyEditDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.SuccessHintDialog;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.mpay.utils.XhgUtil;
import com.umeng.socialize.utils.Log;

public class WithdrawalActivity extends BaseActivity {

	private TextView tv_bank_name_info, tv_bank_card_info, tv_withdrawal_money,tv_pro,
			tv_capital,btn_next1,tv_charge;
	private RelativeLayout rl_capital;
	private Button btn_next;
	private EditText et_withdrawl_money;//, et_withdrawl_password;
	private TextView et_withdrawl_money_error,tv_title_contre;
	// private Intent intent;
	// private Bundle bundle;
	private  LinearLayout ordinary_layout;
	private RichTreasureBean treasureBean;
	private String cardInfo, mobile, payPassword;
	private Double money;
	private SuccessHintDialog hintDialog;
	private OneButtonDialogWarn warnDialog;
	
	private ImageView bankimag;
	/** 用户手机号*/
	
	private String tag = "0";
	
	private String tags = "1";
	
	private String type;
	
	private MyEditDialog doubleWarnDialog;
	
	private String withdrawlMoney;
	
	private String etu;
	private double Upperlimit,lowamt;
	

	void initView() {
		
		btn_next1 = (TextView) findViewById(R.id.btn_next1);
		tv_title_contre = (TextView) findViewById(R.id.tv_title_contre);
		tv_title_contre.setText("提现");
		tv_bank_name_info = (TextView) findViewById(R.id.tv_bank_name_info);
		tv_bank_card_info = (TextView) findViewById(R.id.tv_bank_card_info);
		tv_withdrawal_money = (TextView) findViewById(R.id.tv_withdrawal_money);
		tv_pro = (TextView) findViewById(R.id.tv_pro);
//		et_withdrawl_password = (EditText) findViewById(R.id.et_withdrawl_password);
		btn_next = (Button) findViewById(R.id.btn_next);
		et_withdrawl_money = (EditText) findViewById(R.id.et_withdrawl_money);
		et_withdrawl_money_error = (TextView) findViewById(R.id.et_withdrawl_money_error);
		rl_capital = (RelativeLayout) findViewById(R.id.rl_hint_capital);
		tv_capital = (TextView) findViewById(R.id.tv_capital);
		bankimag = (ImageView) findViewById(R.id.back_imgs);
		tv_charge = (TextView) findViewById(R.id.tv_charge);
		tv_charge.setText(Html.fromHtml("<u>收费规则></u>"));
		ordinary_layout = (LinearLayout) findViewById(R.id.ordinary_layout);
		Intent it = getIntent();
		if(it.getStringExtra("tag")!=null&&it.getStringExtra("tag").equals("2")){
			ordinary_layout.setVisibility(View.VISIBLE);
		}
		tv_charge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(WithdrawalActivity.this,OnlineWeb.class);
				it.putExtra("titleStr", "收费规则");
				it.putExtra("urlStr", HttpUrls.CHARGERULE);
				startActivity(it);
//				Intent it = new Intent(WithdrawalActivity.this,ChargingrRulesActivity.class);
//				startActivity(it);
				
			}
		});
		btn_next1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(et_withdrawl_money.getText().toString()!=null&&!et_withdrawl_money.getText().toString().equals("")){
					double money = Double.parseDouble(et_withdrawl_money.getText()
							.toString());
					if(money<lowamt){
						et_withdrawl_money.setVisibility(View.GONE);
						et_withdrawl_money_error
								.setVisibility(View.VISIBLE);
						et_withdrawl_money_error.setError("提现金额不能小于"+lowamt+"元");
						et_withdrawl_money_error.setText("提现金额不能小于"+lowamt+"元");
						return;
					}else if(money>=Upperlimit){
						et_withdrawl_money.setVisibility(View.GONE);
						et_withdrawl_money_error
								.setVisibility(View.VISIBLE);
						et_withdrawl_money_error.setError("提现金额"+Upperlimit+"以下");
						et_withdrawl_money_error.setText("提现金额"+Upperlimit+"以下");
						return;
					}
				}
			
				
				validation(tags);
				
				
//				ToastCustom.showMessage(WithdrawalActivity.this, "此功能即将开通");
				
			}
		});
		et_withdrawl_money_error.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				et_withdrawl_money_error.setVisibility(View.GONE);
				et_withdrawl_money.setVisibility(View.VISIBLE);
				et_withdrawl_money.requestFocus();
				et_withdrawl_money.setText("");
			}
		});
		et_withdrawl_money.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					rl_capital.setVisibility(View.VISIBLE);
					try {
						tv_capital.setText(XhgUtil.change(Double
								.parseDouble(et_withdrawl_money.getText()
										.toString())));
						Float money = Float.parseFloat(et_withdrawl_money.getText()
								.toString()) * 100;
						if (money > Float.parseFloat(treasureBean.getAvaamt())) {
							et_withdrawl_money.setVisibility(View.GONE);
							et_withdrawl_money_error
									.setVisibility(View.VISIBLE);
							et_withdrawl_money_error.setError("您的账户余额不足");
							et_withdrawl_money_error.setText("您的账户余额不足");
							
							return;
						}
					} catch (Exception e) {
						tv_capital.setText("输入有误。。。");
					}
				} else {
					rl_capital.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
		
		
		et_withdrawl_money.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					if(!et_withdrawl_money.getText().toString().equals("")){
						double money = Double.parseDouble(et_withdrawl_money.getText()
								.toString());
						 if(money< lowamt){
								et_withdrawl_money.setVisibility(View.GONE);
								et_withdrawl_money_error
										.setVisibility(View.VISIBLE);
								et_withdrawl_money_error.setError("提现金额必须大于"+lowamt+"元");
								et_withdrawl_money_error.setText("提现金额必须大于"+lowamt+"元");
								et_withdrawl_money.setText("");
								return;
							}
					}else{
						et_withdrawl_money.setVisibility(View.GONE);
						et_withdrawl_money_error
								.setVisibility(View.VISIBLE);
						et_withdrawl_money_error.setError("请输入提现金额");
						et_withdrawl_money_error.setText("请输入提现金额");
						et_withdrawl_money.setText("");
					}

				}
			}
		});
		((TextView) findViewById(R.id.bt_title_left))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(et_withdrawl_money.getText().toString()!=null&&!et_withdrawl_money.getText().toString().equals("")){
				double money = Double.parseDouble(et_withdrawl_money.getText()
						.toString());
				if(money<10){
					et_withdrawl_money.setVisibility(View.GONE);
					et_withdrawl_money_error
							.setVisibility(View.VISIBLE);
					et_withdrawl_money_error.setError("提现金额不能小于10元");
					et_withdrawl_money_error.setText("提现金额不能小于10元");
					return;
				}else if(money>=Upperlimit){
					et_withdrawl_money.setVisibility(View.GONE);
					et_withdrawl_money_error
							.setVisibility(View.VISIBLE);
					et_withdrawl_money_error.setError("提现金额"+Upperlimit+"以下");
					et_withdrawl_money_error.setText("提现金额"+Upperlimit+"以下");
					return;
				}
				}
				
				validation(tag);
				
			}
		});
	}

	private void validation(final String tag) {
		
		type = tag;
		withdrawlMoney = et_withdrawl_money.getText().toString();
		if (withdrawlMoney == null || withdrawlMoney.equals("")) {
			Toast.makeText(getApplicationContext(),"请输入要提现的金额",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(WithdrawalActivity.this, "请输入要提现的金额");
			return;
		}
		money = Double.parseDouble(withdrawlMoney) * 100;
		if (money > Double.parseDouble(treasureBean.getAvaamt())) {
			Toast.makeText(getApplicationContext(),"提现金额超限",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(WithdrawalActivity.this, "提现金额超限");
			return;
		}
		if (money <= 0) {
			Toast.makeText(getApplicationContext(),"提现金额必须大于0元",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(WithdrawalActivity.this, "提现金额必须大于0元");
			return;
		}
		if (!isNumber(withdrawlMoney)) {
			Toast.makeText(getApplicationContext(),"提现金额格式错误",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(WithdrawalActivity.this, "提现金额格式错误");
			return;
		} else {
			if (withdrawlMoney.substring(withdrawlMoney.length() - 1,
					withdrawlMoney.length()).equals(".")) {
				Toast.makeText(getApplicationContext(),"提现金额格式错误",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(WithdrawalActivity.this, "提现金额格式错误");
				return;
			}
		}
//		payPassword = et_withdrawl_password.getText().toString();
//		if (payPassword == null || payPassword.equals("")) {
//			ToastCustom.showMessage(WithdrawalActivity.this, "请输入支付密码！");
//			return;
//		}
//		if (payPassword.length() < 6||payPassword.length() > 15) {
//			ToastCustom.showMessage(WithdrawalActivity.this,
//					"输入的密码长度有误,请输入6～15个数字、字母或特殊符号！");
//			return;
//		}
		int a = (int) (Double.parseDouble(withdrawlMoney) * 100);
		withdrawlMoney = String.valueOf(a);
		btn_next1.setEnabled(false);
		btn_next.setEnabled(false);
		
		doubleWarnDialog = new MyEditDialog(WithdrawalActivity.this,
				R.style.MyEditDialog, "提现", "请输入支付密码", "确认", "取消", withdrawlMoney,
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {

						switch (v.getId()) {
						case R.id.btn_right:
							doubleWarnDialog.dismiss();
							InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
							break;
						case R.id.btn_left:
							String paypwd = doubleWarnDialog.getpaypwd();

							if (paypwd == null || paypwd.equals("")) {
								Toast.makeText(getApplicationContext(),"请输入支付密码",
										Toast.LENGTH_SHORT).show();
//								ToastCustom.showMessage(
//										WithdrawalActivity.this,
//										"请输入支付密码！");
								return;
							}
							if (paypwd.length() < 6 || paypwd.length() > 15) {
//								ToastCustom.showMessage(
//										WithdrawalActivity.this,
//										"输入的密码长度有误,请输入6～15个数字、字母或特殊符号！");
								return;
							}
							WithdrawalOnBankCardTask cardTask = new WithdrawalOnBankCardTask();
							cardTask.execute(HttpUrls.WITHDRAWAL_ON_BANK_CARD + "", mobile,
									withdrawlMoney, paypwd,tag);
							break;
						default:
							break;
						}
						btn_next.setEnabled(true);
						btn_next1.setEnabled(true);
					}

				},
		new onMyaddTextListener() {
			
			@Override
			public void refreshActivity(String paypwd) {
				
				if (paypwd == null || paypwd.equals("")) {
					Toast.makeText(getApplicationContext(),"请输入支付密码",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(
//							WithdrawalActivity.this,
//							"请输入支付密码！");
					return;
				}
				if (paypwd.length() < 6 || paypwd.length() > 15) {
//					ToastCustom.showMessage(
//							WithdrawalActivity.this,
//							"输入的密码长度有误,请输入6个数字、字母或特殊符号！");
					return;
				}
				WithdrawalOnBankCardTask cardTask = new WithdrawalOnBankCardTask();
				cardTask.execute(HttpUrls.WITHDRAWAL_ON_BANK_CARD + "", mobile,
						withdrawlMoney, paypwd,tag);
				
			}
		});
		doubleWarnDialog.setCancelable(false);
		doubleWarnDialog.setCanceledOnTouchOutside(false);
		doubleWarnDialog.show();
//		transfer_confirm.setEnabled(false);
		//进行交易
		

	}

	// 验证金额
	public static boolean isNumber(String str) {
		java.util.regex.Pattern pattern = java.util.regex.Pattern
				.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后一位的数字的正则表达式
		java.util.regex.Matcher match = pattern.matcher(str);
		if (match.matches() == false) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.withdrawal_on_bank_card);
		AppContext.getInstance().addActivity(this);
//		phone = ((AppContext)getApplication()).getMobile();
		mobile =  MyCacheUtil.getshared(this).getString("Mobile", "");
//		phone = MyCacheUtil.getshared(this).getString("Mobile", "");
//		treasureBean = ((AppContext) getApplication()).getTreasureBean();
		
		GetWalletInfo  walletinfo1 = new GetWalletInfo();
		
		walletinfo1.execute(HttpUrls.RICH_TREASURE_INFO + "",
				mobile);
		initView();
		// TODO Auto-generated method stub
		// intent = getIntent();
		// bundle = intent.getExtras();
		// treasureBean = (RichTreasureBean) bundle.get("treasureBean");

		// initView();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		

	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		btn_next1.setEnabled(true);
		btn_next.setEnabled(true);
		if(loadingDialogWhole!=null){
			loadingDialogWhole.dismiss();
		}
	}

	/**
	 * 提现
	 * 
	 * @author liangge
	 * 
	 */
	class WithdrawalOnBankCardTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			showLoadingDialog("正在操作中...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3],params[4] };
			return NetCommunicate.getMidatc(HttpUrls.WITHDRAWAL_ON_BANK_CARD,
					values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					treasureBean.setAvaamt(result.get("AVAAMT").toString());
					treasureBean.setCheckamt(result.get("CHECKAMT").toString());
					((AppContext) getApplication())
							.setTreasureBean(treasureBean);
					
					Intent it = new Intent(WithdrawalActivity.this,MentionNowAcitvity.class);
					
					it.putExtra("State", 1);
					it.putExtra("tag", type);
					it.putExtra("Actcard", treasureBean.getActcard());
					it.putExtra("Banknam", treasureBean.getBanknam());
					it.putExtra("Crdflg", treasureBean.getCrdflg());
					it.putExtra("Money", et_withdrawl_money.getText().toString());
					startActivity(it);
					finish();
//					hintDialog = new SuccessHintDialog(WithdrawalActivity.this,
//							R.style.CustomDialog, "提示", result.get(
//									Entity.RSPMSG).toString(), "确定",
//							new OnMyDialogClickListener() {
//
//								@Override
//								public void onClick(View v) {
//									// TODO Auto-generated method stub
//									switch (v.getId()) {
//									case R.id.buttonOk:
//										hintDialog.dismiss();
//										finish();
//										break;
//									}
//								}
//							});
//					hintDialog.setCancelable(false);
//					hintDialog
//							.setOnKeyListener(new DialogInterface.OnKeyListener() {
//								@Override
//								public boolean onKey(DialogInterface dialog,
//										int keyCode, KeyEvent event) {
//									if (keyCode == KeyEvent.KEYCODE_SEARCH) {
//										return true;
//									} else {
//										return true; // 默认返回 false
//									}
//								}
//							});
//					hintDialog.setCanceledOnTouchOutside(false);
//					hintDialog.show();
				} else if (result.get(Entity.RSPCOD).equals("000001")) {
					doubleWarnDialog.dismiss();
					treasureBean.setIsActpwout("1");
					((AppContext) getApplication())
							.setTreasureBean(treasureBean);
					Intent it = new Intent(WithdrawalActivity.this,MentionNowAcitvity.class);
					
					it.putExtra("State", 0);
					it.putExtra("Err", "您当日密码输错次数已超限,请明日再试");
					startActivity(it);
					finish();
//					ToastCustom.showMessage(WithdrawalActivity.this,
//							"您当日密码输错次数已超限,请明日再试。");
				}else if(result.get(Entity.RSPCOD).equals("000088")){
					doubleWarnDialog.dismiss();
				}else if(result.get(Entity.RSPCOD).equals("10029")){
					warnDialog = new OneButtonDialogWarn(WithdrawalActivity.this,
							R.style.CustomDialog, "提示", result.get(
									Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent it = new Intent(WithdrawalActivity.this,RequestCardInfoChangeActivity.class);
									startActivity(it);
									warnDialog.dismiss();
									finish();
								}
							});
					warnDialog.show();
				} else {
//					warnDialog = new OneButtonDialogWarn(
//							WithdrawalActivity.this, R.style.CustomDialog,
//							"提示", result.get(Entity.RSPMSG).toString(), "确定",
//							new OnMyDialogClickListener() {
//
//								@Override
//								public void onClick(View v) {
//									// TODO Auto-generated method stub
//									warnDialog.dismiss();
//								}
//							});
					doubleWarnDialog.dismiss();
					Intent it = new Intent(WithdrawalActivity.this,MentionNowAcitvity.class);
					
					it.putExtra("State", 0);
					it.putExtra("Err", result.get(Entity.RSPMSG).toString());
					startActivity(it);
//					warnDialog.show();
				}
			} else {
				Toast.makeText(getApplicationContext(),"系统错误",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(WithdrawalActivity.this, "fail");
			}
			super.onPostExecute(result);
		}
	}
	
	class GetWalletInfo extends AsyncTask<String , Integer, HashMap<String, Object>>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在加载...");
		}
		
		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			
			String[] values = { params[0], params[1] };
			return NetCommunicate
					.getMidatc(HttpUrls.RICH_TREASURE_INFO, values);
			
		}
		
		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			
			if (result != null) {
				if (result.get(Entity.RSPCOD)!=null&&result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					loadingDialogWhole.dismiss();
					treasureBean = new RichTreasureBean();
					treasureBean.setLogsts(result.get("LOGSTS").toString());
					treasureBean.setActsts(result.get("ACTSTS").toString());
					treasureBean.setAvaamt(result.get("AVAAMT").toString());
					treasureBean.setYesterincom(result.get("YESTERINCOM")
							.toString());
					treasureBean.setTotamt(result.get("TOTAMT").toString());
					treasureBean.setFixamt(result.get("FIXAMT").toString());
					treasureBean.setCheckamt(result.get("CHECKAMT").toString());
					treasureBean.setFrzamt(result.get("FRZAMT").toString());
					treasureBean.setDptrate(result.get("DPTRATE").toString());
					treasureBean.setCumulative(result.get("CUMULATIVE")
							.toString());
					treasureBean.setMilincom(result.get("MILINCOM").toString());
					treasureBean.setWeekincom(result.get("WEEKINCOM")
							.toString());
					treasureBean.setMonthincom(result.get("MONTHINCOM")
							.toString());
					if(result.get("MERNAM")!=null&&!result.get("MERNAM").equals("")){
						treasureBean.setMerNam(result.get("MERNAM").toString());
					}else{
						treasureBean.setMerNam("");
					}
					treasureBean.setBanknam(result.get("BANKNAM").toString());
					treasureBean.setActcard(result.get("ACTCARD").toString());
					treasureBean.setCrdflg(result.get("CRDFLG").toString());
					treasureBean.setIsActpwout(result.get("ISACTPWOUT")
							.toString());
					((AppContext)getApplication())
							.setTreasureBean(treasureBean);
					
					tv_bank_name_info.setText(treasureBean.getBanknam());
					if (treasureBean.getBanknam() != null&&treasureBean.getBanknam().length()>0) {
						if (treasureBean.getBanknam().substring(0,2).equals("招商")) {
							bankimag.setImageResource(R.drawable.ps_cmb);
						}else if(treasureBean.getBanknam().substring(0,2).equals("农业")) {
							bankimag.setImageResource(R.drawable.ps_abc);
						}else if(treasureBean.getBanknam().substring(0,2).equals("农行")){
							bankimag.setImageResource(R.drawable.ps_abc);
						}else if(treasureBean.getBanknam().substring(0,2).equals("北京")){
							bankimag.setImageResource(R.drawable.ps_bjb);
						}else if(treasureBean.getBanknam().substring(0,2).equals("中国")){
							if(treasureBean.getBanknam().substring(0,4).equals("中国")){
								bankimag.setImageResource(R.drawable.ps_ccb);
							}else{
								bankimag.setImageResource(R.drawable.ps_boc);
							}
							
						}else if(treasureBean.getBanknam().substring(0,2).equals("建设")){
							bankimag.setImageResource(R.drawable.ps_ccb);
						}else if(treasureBean.getBanknam().substring(0,2).equals("光大")){
							bankimag.setImageResource(R.drawable.ps_cebb);
						}else if(treasureBean.getBanknam().substring(0,2).equals("兴业")){
							bankimag.setImageResource(R.drawable.ps_cib);
						}else if(treasureBean.getBanknam().substring(0,2).equals("中信")){
							bankimag.setImageResource(R.drawable.ps_citic);
						}else if(treasureBean.getBanknam().substring(0,2).equals("民生")){
							bankimag.setImageResource(R.drawable.ps_cmbc);
						}else if(treasureBean.getBanknam().substring(0,2).equals("交通")){
							bankimag.setImageResource(R.drawable.ps_comm);
						}else if(treasureBean.getBanknam().substring(0,2).equals("华夏")){
							bankimag.setImageResource(R.drawable.ps_hxb);
						}else if(treasureBean.getBanknam().substring(0,4).equals("广东发展")){
							bankimag.setImageResource(R.drawable.ps_gdb);
						}else if(treasureBean.getBanknam().substring(0,2).equals("广发")){
							bankimag.setImageResource(R.drawable.ps_gdb);
						}else if(treasureBean.getBanknam().substring(0,2).equals("邮政")){
							bankimag.setImageResource(R.drawable.ps_psbc);
						}else if(treasureBean.getBanknam().substring(0,2).equals("邮储")){
							bankimag.setImageResource(R.drawable.ps_psbc);
						}else if(treasureBean.getBanknam().substring(0,2).equals("工商")){
							bankimag.setImageResource(R.drawable.ps_icbc);
						}else if(treasureBean.getBanknam().substring(0,2).equals("平安")){
							bankimag.setImageResource(R.drawable.ps_spa);
						}else if(treasureBean.getBanknam().substring(0,2).equals("浦东")){
							bankimag.setImageResource(R.drawable.ps_spdb);
						}else if(treasureBean.getBanknam().substring(0,2).equals("工商")){
							bankimag.setImageResource(R.drawable.ps_icbc);
						}else{
							bankimag.setImageResource(R.drawable.ps_unionpay);
						}
						
					}else{
						bankimag.setImageResource(R.drawable.ps_unionpay);
					}
					((TextView) findViewById(R.id.tv_title_contre)).setText("提现");
					if(treasureBean.getActcard().length()>10){
						cardInfo = treasureBean.getActcard().substring(
								treasureBean.getActcard().length() - 4,
								treasureBean.getActcard().length());
					}
					if (treasureBean.getCrdflg().equals("01")) {
						tv_bank_card_info.setText("尾号" + cardInfo + "　借记卡");
					} else {
						tv_bank_card_info.setText("尾号" + cardInfo + "　贷记卡");
					}
					double d = Double.parseDouble(treasureBean.getAvaamt());
					
					
					String r = String .format("%.2f",d/100);
//					me_result_money.setText(d/100+"");
					tv_withdrawal_money.setText(r);
					
					if(result.get("EPURSWITHDRAWCOUNT")!=null){
						etu = result.get("EPURSWITHDRAWCOUNT").toString();
					}
					if(result.get("EPURSWITHDRAWSUMAMT")!=null){
						String etms = result.get("EPURSWITHDRAWSUMAMT").toString();
						Upperlimit = Double.parseDouble(etms)/100/Double.parseDouble(etu);
						
					}
					
					if(result.get("LOWWDAMT")!=null){
						String etms = result.get("LOWWDAMT").toString();
						lowamt = Double.parseDouble(etms)/100;
						
					}
					tv_pro.setText("提现单笔上限"+Upperlimit+"元，日提现上限"+etu+"笔");
					
//					if (treasureBean.getAvaamt().length() == 1) {
//						transfer_allmoney.setText("0.0" + treasureBean.getAvaamt());
//					} else if (treasureBean.getAvaamt().length() == 2) {
//						transfer_allmoney.setText("0." + treasureBean.getAvaamt());
//					} else {
//						transfer_allmoney.setText(treasureBean.getAvaamt().substring(0,
//								treasureBean.getAvaamt().length() - 2)
//								+ "."
//								+ treasureBean.getAvaamt().substring(
//										treasureBean.getAvaamt().length() - 2));
//					}
					
					if (treasureBean.getLogsts().equals("1")) {
						
					} else if (treasureBean.getLogsts().equals("0")) {
						loadingDialogWhole.dismiss();
						Toast.makeText(getApplicationContext(),"账户暂未开通该功能",
								Toast.LENGTH_SHORT).show();
//						ToastCustom
//								.showMessage(WithdrawalActivity.this, "账户暂未开通该功能!");
					} else {
						loadingDialogWhole.dismiss();
						Toast.makeText(getApplicationContext(),"账户暂未开通该功能",
								Toast.LENGTH_SHORT).show();
//						ToastCustom.showMessage(WithdrawalActivity.this, "账户暂未开通该功能!"
//								+ treasureBean.getLogsts());
					}
				} else {
					loadingDialogWhole.dismiss();
					
					warnDialog = new OneButtonDialogWarn(WithdrawalActivity.this,
							R.style.CustomDialog, "提示", result.get(Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									finish();
									warnDialog.dismiss();
								}
							});
					warnDialog.show();
//					ToastCustom.showMessage(RichTreasureActivity.this,
//							result.get(Entity.RSPMSG).toString());
				}
			} else {
				Toast.makeText(getApplicationContext(),"数据获取失败,请检查网络连接",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(WithdrawalActivity.this, "数据获取失败,请检查网络连接");
				loadingDialogWhole.dismiss();
			}
			super.onPostExecute(result);
			
				
		}
		
	}
}
