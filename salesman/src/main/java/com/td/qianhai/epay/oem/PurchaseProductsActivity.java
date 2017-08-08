package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.MoneyListBean;
import com.td.qianhai.epay.oem.interfaces.onMyaddTextListener;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.MyEditDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PurchaseProductsActivity extends BaseActivity {

	private EditText et_balance;
	private TextView btn_confirm,balance_tv,tv_pro;
	private String orderid, ordertyp, phone,balace,savedamt,dayinnum,dayoutnum,inday,outday;
	private String tag = "",teg = "";
	private OneButtonDialogWarn warnDialog;
	private MyEditDialog doubleWarnDialog;
	private ArrayList<HashMap<String, Object>> mList;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.purchase_product_activity);
		AppContext.getInstance().addActivity(this);
		phone = MyCacheUtil.getshared(this).getString("Mobile", "");

		Intent it = getIntent();
		teg = it.getStringExtra("teg");
		if(teg!=null&&teg.equals("4")){
			tag = "0";
			GetWalletInfo  walletinfo = new GetWalletInfo();
				
			walletinfo.execute(HttpUrls.RICH_TREASURE_INFO + "",
						phone);
			
		}else{
			tag = it.getStringExtra("tag");
			orderid = it.getStringExtra("orderid");
			ordertyp = it.getStringExtra("ordertyp");
			balace = it.getStringExtra("balace");
			savedamt = it.getStringExtra("savedamt");
			inday = it.getStringExtra("inday");
			outday = it.getStringExtra("outday");
			dayinnum = it.getStringExtra("dayinnum");
			dayoutnum = it.getStringExtra("dayoutnum");
			initview();
		}
		
	}

	private void initview() {
		balance_tv = (TextView) findViewById(R.id.balance_tv);
		tv_pro = (TextView) findViewById(R.id.tv_pro);
		
		// TODO Auto-generated method stub
		if (tag.equals("0")) {
			((TextView) findViewById(R.id.tv_title_contre)).setText("转入");
			balance_tv.setText(balace+"元");
			tv_pro.setText("日最大转入次数"+dayinnum+"次,"+"日最大转入金额"+inday+"元。");
		} else {
			((TextView) findViewById(R.id.tv_title_contre)).setText("转出");
			balance_tv.setText(savedamt+"元");
			tv_pro.setText("日最大转出次数"+dayoutnum+"次,"+"日最大转出金额"+outday+"元。");
		}

		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});

		et_balance = (EditText) findViewById(R.id.et_balance);
		btn_confirm = (TextView) findViewById(R.id.btn_confirm);
		
	
		btn_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 String t = et_balance.getText().toString();
				 final String blance = (int)(Double.parseDouble(t)*100)+"";
				double b = Double.parseDouble(t);
				if(b<=0){
					Toast.makeText(getApplicationContext(), "输入金额有误", Toast.LENGTH_SHORT).show();
					return;
				}
				if(b>Double.parseDouble(inday)){
					Toast.makeText(getApplicationContext(), "超出最大金额", Toast.LENGTH_SHORT).show();
					return;
				}
//				if(b>Double.parseDouble(balace)){
//					
//					Toast.makeText(PurchaseProductsActivity.this, "余额不足", Toast.LENGTH_SHORT).show();
//					return;
//				}
				
				doubleWarnDialog = new MyEditDialog(PurchaseProductsActivity.this,
						R.style.MyEditDialog, "", "请输入支付密码", "确认", "取消", blance,
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
//										ToastCustom.showMessage(
//												WithdrawalActivity.this,
//												"请输入支付密码！");
										return;
									}
									if (paypwd.length() < 6 || paypwd.length() > 15) {
//										ToastCustom.showMessage(
//												WithdrawalActivity.this,
//												"输入的密码长度有误,请输入6～15个数字、字母或特殊符号！");
										return;
									}
									break;
								default:
									break;
								}
							}

						},
				new onMyaddTextListener() {
					
					@Override
					public void refreshActivity(String paypwd) {
						
						if (paypwd == null || paypwd.equals("")) {
							Toast.makeText(getApplicationContext(),"请输入支付密码",
									Toast.LENGTH_SHORT).show();
//							ToastCustom.showMessage(
//									WithdrawalActivity.this,
//									"请输入支付密码！");
							return;
						}
						if (paypwd.length() < 6 || paypwd.length() > 15) {
//							ToastCustom.showMessage(
//									WithdrawalActivity.this,
//									"输入的密码长度有误,请输入6个数字、字母或特殊符号！");
							return;
						}
						if (tag.equals("0")) {
							changeinto(blance,paypwd);
						} else {
							turnout(blance,paypwd);
						}
						
					}
				});
				doubleWarnDialog.setCancelable(false);
				doubleWarnDialog.setCanceledOnTouchOutside(false);
				doubleWarnDialog.show();

			}

		});
		
		

//		transfer_confirm.setEnabled(false);
		//进行交易
		

		
		
		et_balance.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					btn_confirm.setEnabled(true);
				}else{
					btn_confirm.setEnabled(false);
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
	}

	private void changeinto(String t, String paypwd) {
		// TODO Auto-generated method stub
		ChangeIntoTask walletinfo = new ChangeIntoTask();

		walletinfo.execute(HttpUrls.TOCHANGEINTO + "", phone,orderid,ordertyp,t,paypwd);
	}

	private void turnout(String t, String paypwd) {
		// TODO Auto-generated method stub
		TurnOutTask walletinfo = new TurnOutTask();

		walletinfo.execute(HttpUrls.TURNOUT + "", phone,t,paypwd);
	}

	class ChangeIntoTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在加载...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {

			String[] values = { params[0], params[1] , params[2], params[3], params[4], params[5]};
			return NetCommunicate
					.getMidatc(HttpUrls.TOCHANGEINTO, values);

		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD) != null
						&& result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
				
					warnDialog = new OneButtonDialogWarn(
							PurchaseProductsActivity.this,
							R.style.CustomDialog, "提示", result.get(
									Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									finish();
									warnDialog.dismiss();
								}
							});
					warnDialog.show();
				} else {

					if (result.get(Entity.RSPMSG) != null) {
						// loadingDialogWhole.dismiss();

						warnDialog = new OneButtonDialogWarn(
								PurchaseProductsActivity.this,
								R.style.CustomDialog, "提示", result.get(
										Entity.RSPMSG).toString(), "确定",
								new OnMyDialogClickListener() {
									@Override
									public void onClick(View v) {
										warnDialog.dismiss();
									}
								});
						warnDialog.show();
					} else {
						// loadingDialogWhole.dismiss();

						warnDialog = new OneButtonDialogWarn(
								PurchaseProductsActivity.this,
								R.style.CustomDialog, "提示", "网络异常请重试", "确定",
								new OnMyDialogClickListener() {
									@Override
									public void onClick(View v) {
										warnDialog.dismiss();
									}
								});
						warnDialog.show();
					}

				}
			} else {
				Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络连接",
						Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);

		}

	}

	class TurnOutTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在加载...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {

			String[] values = { params[0], params[1], params[2],params[3]};
			return NetCommunicate
					.getMidatc(HttpUrls.TURNOUT, values);

		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD) != null
						&& result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					
					warnDialog = new OneButtonDialogWarn(
							PurchaseProductsActivity.this,
							R.style.CustomDialog, "提示", result.get(
									Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									finish();
									warnDialog.dismiss();
								}
							});
					warnDialog.show();
				} else {

					if (result.get(Entity.RSPMSG) != null) {
						// loadingDialogWhole.dismiss();

						warnDialog = new OneButtonDialogWarn(
								PurchaseProductsActivity.this,
								R.style.CustomDialog, "提示", result.get(
										Entity.RSPMSG).toString(), "确定",
								new OnMyDialogClickListener() {
									@Override
									public void onClick(View v) {
										warnDialog.dismiss();
									}
								});
						warnDialog.show();
					} else {
						// loadingDialogWhole.dismiss();

						warnDialog = new OneButtonDialogWarn(
								PurchaseProductsActivity.this,
								R.style.CustomDialog, "提示", "网络异常请重试", "确定",
								new OnMyDialogClickListener() {
									@Override
									public void onClick(View v) {
										warnDialog.dismiss();
									}
								});
						warnDialog.show();
					}

				}
			} else {
				Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络连接",
						Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
	}
	
	
class GetWalletInfo extends AsyncTask<String , Integer, HashMap<String, Object>>{
		
		@Override
		protected void onPreExecute() {
			showLoadingDialog("正在加载中...");
			super.onPreExecute();
		}
		
		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			
			String[] values = { params[0], params[1] };
			return NetCommunicate
					.getMidatc(HttpUrls.RICH_TREASURE_INFO, values);
			
		}
		String avaamt ="0";
		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			if (result != null) {
				if (result.get(Entity.RSPCOD)!=null&&result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					double d = Double.parseDouble(result.get("AVAAMT").toString());
					balace = String .format("%.2f",d/100);
					
					loadMore();
					
				}
			}else{
				warnDialog = new OneButtonDialogWarn(PurchaseProductsActivity.this,
						R.style.CustomDialog, "提示", result.get(Entity.RSPMSG).toString(), "确定",
						new OnMyDialogClickListener() {
							@Override
							public void onClick(View v) {
								finish();
								warnDialog.dismiss();
							}
						});
			}
		}
	}


	class ChangeIntoInfoTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {

			String[] values = { params[0], params[1], params[2], params[3],
					params[4], params[5], params[6] };
			return NetCommunicate.getMidatc(HttpUrls.EXCHANGEINFO, values);

		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD) != null
						&& result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {

					// instatus = result.get("INSTATUS").toString(); // 转入
					// outstatus = result.get("OUTSTATUS").toString(); // 转出
					//
					// mininamt = String.format("%.2f",
					// Double.parseDouble(result
					// .get("MININAMT").toString()) / 100); // 转入限额
					// maxinamt = String.format("%.2f",
					// Double.parseDouble(result
					// .get("MAXINAMT").toString()) / 100);
					//
					// minoutamt = String.format("%.2f",
					// Double.parseDouble(result
					// .get("MINOUTAMT").toString()) / 100); // 转出限额
					// maxoutamt = String.format("%.2f",
					// Double.parseDouble(result
					// .get("MAXOUTAMT").toString()) / 100);
					//
					// tot = String.format("%.2f",
					// Double.parseDouble(result.get(
					// "TOTPOINTS").toString()) / 100);
					// pop = String.format("%.2f",
					// Double.parseDouble(result.get(
					// "OPOINTS").toString()) / 100);

					inday = String.format("%.2f", Double.parseDouble(result
							.get("DAYSUMAMT").toString()) / 100);
					outday = String.format("%.2f", Double.parseDouble(result
							.get("DAYOUTSUMAMT").toString()) / 100);

					dayinnum = result.get("DAYINNUM").toString(); // 转入次数
					dayoutnum = result.get("DAYOUTNUM").toString(); // 转出次数

					savedamt = String.format("%.2f", Double.parseDouble(result
							.get("SAVEDAMT").toString()) / 100);

					// if (instatus.equals("0")) {
					// tv_3.setTextColor(FinancialProductsActivity.this.getResources().getColor(R.color.gray));
					// tv_3.setText("不可转");
					// btn_recharge.setEnabled(false);
					// }
					// if (outstatus.equals("0")) {
					// tv_4.setTextColor(FinancialProductsActivity.this.getResources().getColor(R.color.gray));
					// tv_4.setText("不可转");
					// btn_accounts.setEnabled(false);
					// }

					// if(ordertyp.equals("1")){
					// if(result.get("DPTCYCLETYP").toString().equals("1")){
					// capital_pro.setText("定存期限"+result.get("DPTCYCLE").toString()+"天,未到期不可转出");
					// }else
					// if(result.get("DPTCYCLETYP").toString().equals("2")){
					// capital_pro.setText("定存期限"+result.get("DPTCYCLE").toString()+"个月,未到期不可转出");
					// }
					// }

					// if(ordertyp.equals("1")){
					// me_profit_money.setText(tot);
					// }else{
					// me_profit_money.setText(pop);
					// }
					//
					// me_tot_money.setText(tot);
					//
					// me_result_money.setText(savedamt);
					//
					// if(!ordertyp.equals("0")){
					// loadMore();
					// }
					initview();
				} else {

					if (result.get(Entity.RSPMSG) != null) {
						// loadingDialogWhole.dismiss();

						warnDialog = new OneButtonDialogWarn(
								PurchaseProductsActivity.this,
								R.style.CustomDialog, "提示", result.get(
										Entity.RSPMSG).toString(), "确定",
								new OnMyDialogClickListener() {
									@Override
									public void onClick(View v) {
										warnDialog.dismiss();
									}
								});
						warnDialog.show();
					} else {
						// loadingDialogWhole.dismiss();

						warnDialog = new OneButtonDialogWarn(
								PurchaseProductsActivity.this,
								R.style.CustomDialog, "提示", "网络异常请重试", "确定",
								new OnMyDialogClickListener() {
									@Override
									public void onClick(View v) {
										warnDialog.dismiss();
									}
								});
						warnDialog.show();
					}
				}
			} else {
				Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络连接",
						Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
	}
	
	private void loadMore() {
//		if (page != 1 && page > allPageNum) {
////			ToastCustom.showMessage(this, "没有更多记录了");
//			return;
//		}
		mList = new ArrayList<HashMap<String, Object>>();
			new Thread(run).start();

	}
	
	Runnable run = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			String[] values = { String.valueOf(HttpUrls.FINANCIALPRODUCTS),phone,"1","10"};
			MoneyListBean entitys = NetCommunicate.getMoneyListBean(
					HttpUrls.FINANCIALPRODUCTS, values);

			Message msg = new Message();
			if (entitys != null) {
				list = entitys.list;
				mList.addAll(list);
					if(mList.size()<=0||mList==null){
						msg.what = 2;
					}else{
						msg.what = 1;
					}

			} else {
				msg.what = 3;
			}
			handler.sendMessage(msg);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				orderid = mList.get(0).get("MIDOEMID").toString(); // 订单id
				ordertyp = mList.get(0).get("MIDOEMTYP").toString(); //订单typ
				ChangeIntoInfoTask walletinfo = new ChangeIntoInfoTask();
				
				walletinfo.execute(HttpUrls.EXCHANGEINFO + "", phone,orderid,"","","","");
				break;
			case 2:

				Toast.makeText(getApplicationContext(),"网络异常，请检查网络设置",
						Toast.LENGTH_SHORT).show();

//				ToastCustom.showMessage(RateListActivity.this,
//						"没有获取到您的订单信息");
				break;
			case 3:
				Toast.makeText(getApplicationContext(),"网络异常，请检查网络设置",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"订单信息获取失败");
				break;
			default:
				break;
			}
		};
	};
}
