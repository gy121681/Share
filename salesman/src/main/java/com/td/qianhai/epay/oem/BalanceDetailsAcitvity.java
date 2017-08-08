package com.td.qianhai.epay.oem;

import java.util.HashMap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.RichTreasureBean;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.AnimationUtil;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class BalanceDetailsAcitvity extends BaseActivity {

	private String money;

	private int state;

	private TextView me_result_money, me_result, bt_title_left,
			tv_title_contre,me_profit_money,user_state_img,lin_profit;

	private RelativeLayout  btn_accounts;

	private LinearLayout me_data;
	
	private RelativeLayout btn_recharge,btn_mention;

	private Intent it;

	private ImageView result_img;
	
	private String phone;
	
	private RichTreasureBean treasureBean;
	
	private OneButtonDialogWarn warnDialog;
	
	private boolean tag = false;
	
	private String aa;
	
	private String attStr,sts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.balance_info_activity);
		AppContext.getInstance().addActivity(this);
		
		final ScaleAnimation anima = new ScaleAnimation(1f, 2f, 1f, 2f, 
				Animation.RELATIVE_TO_SELF, 3f, Animation.RELATIVE_TO_SELF, 0f); 
				anima.setDuration(500);
				anima.setFillAfter(true);
				final ScaleAnimation anima2 = new ScaleAnimation(2f, 1f, 2f, 1f, 
						Animation.RELATIVE_TO_SELF, 3f, Animation.RELATIVE_TO_SELF, 0f); 
						anima2.setDuration(500);
				user_state_img = (TextView) findViewById(R.id.user_state_img);
				
				user_state_img.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View arg0, MotionEvent event) {
						// TODO Auto-generated method stub
						
						if(event.getAction() == MotionEvent.ACTION_DOWN){
					        user_state_img.startAnimation(anima);
					        if(aa!=null){
					        	user_state_img.setTextSize(8);
					        	user_state_img.setText(treasureBean.getMerNam()+"\n"+"状态:"+aa);
					        	
					        }
						}
						if(event.getAction() == MotionEvent.ACTION_UP){
							user_state_img.startAnimation(anima2);
							user_state_img.setTextSize(13);
							user_state_img.setText(treasureBean.getMerNam());
							
						}
						
						return true;
						
						
						
					}
				});	
			

				}

	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		phone = ((AppContext)getApplication()).getMobile();
		phone = MyCacheUtil.getshared(this).getString("Mobile", "");
		
		attStr =  MyCacheUtil.getshared(this).getString("MERSTS", "");
		sts = MyCacheUtil.getshared(this).getString("STS", "");
		
			GetWalletInfo  walletinfo = new GetWalletInfo();
			
			walletinfo.execute(HttpUrls.RICH_TREASURE_INFO + "",
					phone);
			
			initview();
	}
	
	
	private void initview() {
		btn_recharge = (RelativeLayout) findViewById(R.id.btn_recharge);
		me_profit_money =(TextView) findViewById(R.id.me_profit_money);
		btn_accounts = (RelativeLayout) findViewById(R.id.btn_accounts);
		me_result_money = (TextView) findViewById(R.id.me_result_money);
		tv_title_contre = (TextView) findViewById(R.id.tv_title_contre);
		tv_title_contre.setText("钱包");
		result_img = (ImageView) findViewById(R.id.result_img);
		btn_mention = (RelativeLayout) findViewById(R.id.btn_mentionss);
		me_result = (TextView) findViewById(R.id.me_results);
		lin_profit = (TextView) findViewById(R.id.lin_profit);
		bt_title_left = (TextView) findViewById(R.id.bt_title_left);
		AnimationUtil.ScaleAnimations(me_profit_money);
		AnimationUtil.ScaleAnimations(me_result_money);
		
				
		bt_title_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		lin_profit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent it = new Intent(BalanceDetailsAcitvity.this,CurrentAccountInfoActivity.class);
				startActivity(it);
			}
		});
		
		btn_recharge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent it = new Intent(BalanceDetailsAcitvity.this,OrderPayActivity.class);
				startActivity(it);
			}
		});
//		if (money.length() == 1) {
//			me_result_money.setText("0.0" + money);
//		} else if (money.length() == 2) {
//			me_result_money.setText("0." + money);
//		} else {
//			me_result_money.setText(money.substring(0, money.length() - 2)
//					+ "." + money.substring(money.length() - 2));
//		}

		btn_mention.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
//				if(((AppContext)getApplication()).getTxnsts().equals("0")){
//					if(attStr.equals("0")&&sts.equals("0")){
				
				if (sts.equals("3")) {
				warnDialog = new OneButtonDialogWarn(BalanceDetailsAcitvity.this,
						R.style.CustomDialog, "提示", "银行卡变更申请正在审核中",
						"确定", new OnMyDialogClickListener() {
							@Override
							public void onClick(View v) {
								warnDialog.dismiss();

							}
						});
				warnDialog.show();
			} else{

				Intent it = new Intent(BalanceDetailsAcitvity.this,
						WithdrawalActivity.class);
				it.putExtra("tag", "2");
				startActivity(it);
			}
//						Intent it = new Intent(BalanceDetailsAcitvity.this,
//								WithdrawalActivity.class);
//						startActivity(it);
//						}else if(sts.equals("1")){
//							Toast.makeText(getApplicationContext(), "用户信息正在审核中,暂无法提现",
//									Toast.LENGTH_SHORT).show();
//							
//						}else if(sts.equals("3")){
//							Toast.makeText(getApplicationContext(), "您的银行卡信息修改正在审核中,暂无法提现",
//									Toast.LENGTH_SHORT).show();
//							
//						}else if(sts.equals("4")){
//							Toast.makeText(getApplicationContext(), "您的手机号修改正在审核中,暂无法提现",
//									Toast.LENGTH_SHORT).show();
//						}else{
//							warnDialog = new OneButtonDialogWarn(BalanceDetailsAcitvity.this,
//									R.style.CustomDialog, "提示", "为了您账户安全！请补全资料进行实名认证再进行操作", "确定",
//									new OnMyDialogClickListener() {
//										@Override
//										public void onClick(View v) {
//											Intent it = new Intent(BalanceDetailsAcitvity.this,NewRealNameAuthenticationActivity.class);
//											startActivity(it);
//											warnDialog.dismiss();
//										}
//									});
//							warnDialog.show();
//						}
//				}else{
//					warnDialog = new OneButtonDialogWarn(BalanceDetailsAcitvity.this,
//							R.style.CustomDialog, "提示",
//							"尊敬的用户,请先充值再操作", "确定",
//							new OnMyDialogClickListener() {
//								@Override
//								public void onClick(View v) {
//									Intent it = new Intent(
//											BalanceDetailsAcitvity.this,
//											OrderPayActivity.class);
//									startActivity(it);
//									warnDialog.dismiss();
//								}
//							});
//					warnDialog.show();
//				}


//						if(attStr.equals("0")&&!sts.equals("0")){
//
//						}else if(!attStr.equals("0")&&sts.equals("1")){
//							ToastCustom.showMessage(BalanceDetailsAcitvity.this,
//									"用户信息正在审核中,暂无法提现");
//						}else{
//
//						}
//					}
				
			}
		});

		btn_accounts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

//				if(((AppContext)getApplication()).getTxnsts().equals("0")){
//					if(attStr.equals("0")&&sts.equals("0")){
						Intent it = new Intent(BalanceDetailsAcitvity.this,
								TransferAccountsActivity.class);
						startActivity(it);
//						}else if(sts.equals("1")){
//							ToastCustom.showMessage(BalanceDetailsAcitvity.this,
//									"用户信息正在审核中,暂无法转账");
//						}else if(sts.equals("4")){
//							ToastCustom.showMessage(BalanceDetailsAcitvity.this,
//									"您的手机号修改正在审核中,暂无法转账");
//						}else{
//							
//							warnDialog = new OneButtonDialogWarn(BalanceDetailsAcitvity.this,
//									R.style.CustomDialog, "提示", "为了您账户安全！请补全资料进行实名认证再进行操作", "确定",
//									new OnMyDialogClickListener() {
//										@Override
//										public void onClick(View v) {
//											Intent it = new Intent(BalanceDetailsAcitvity.this,NewRealNameAuthenticationActivity.class);
//											startActivity(it);
//											warnDialog.dismiss();
//										}
//									});
//							warnDialog.show();
//						}
//				}else{
//					warnDialog = new OneButtonDialogWarn(BalanceDetailsAcitvity.this,
//							R.style.CustomDialog, "提示",
//							"尊敬的用户,请先充值再操作", "确定",
//							new OnMyDialogClickListener() {
//								@Override
//								public void onClick(View v) {
//									Intent it = new Intent(
//											BalanceDetailsAcitvity.this,
//											OrderPayActivity.class);
//									startActivity(it);
//									warnDialog.dismiss();
//								}
//							});
//					warnDialog.show();
//				}
//
//
//				// ToastCustom.showMessage(BalanceDetailsAcitvity.this, "即将开通");
			}
		});
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
					Log.e("", "result.gettoString() = = "+result.get("LOGSTS").toString());
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
//					treasureBean.setCustid(result.get("CARDID").toString());
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
					
					
					
					if(treasureBean.getActsts().equals("0")){
						aa = "不可用";
					}else{
						aa = "可用";
						}
					
					user_state_img.setText(treasureBean.getMerNam());
					
//					tv_name.setText(treasureBean.getMerNam());
//					if (treasureBean.getActsts().equals("0")) {
//						tv_account_state.setText("不可用");
//					} else {
//						tv_account_state.setText("可用");
//					}
					if (treasureBean.getTotamt().length() == 1) {
						me_result_money.setText("0.0" + treasureBean.getTotamt());
					} else if (treasureBean.getTotamt().length() == 2) {
						me_result_money.setText("0." + treasureBean.getTotamt());
					} else {
						me_result_money.setText(treasureBean.getTotamt().substring(0,
								treasureBean.getTotamt().length() - 2)
								+ "."
								+ treasureBean.getTotamt().substring(
										treasureBean.getTotamt().length() - 2));
					}
					
					
					if (treasureBean.getCumulative().length() == 1) {
						me_profit_money.setText("0.0"
								+ treasureBean.getCumulative());
					} else if (treasureBean.getCumulative().length() == 2) {
						me_profit_money
								.setText("0." + treasureBean.getCumulative());
					} else {
						me_profit_money.setText(treasureBean.getCumulative()
								.substring(0, treasureBean.getCumulative().length() - 2)
								+ "."
								+ treasureBean.getCumulative().substring(
										treasureBean.getCumulative().length() - 2));
					}
					
					if (treasureBean.getLogsts().equals("1")) {
						
					} else if (treasureBean.getLogsts().equals("0")) {
						loadingDialogWhole.dismiss();
						Toast.makeText(getApplicationContext(), "账户暂未开通该功能",
								Toast.LENGTH_SHORT).show();
					} else {
						loadingDialogWhole.dismiss();
						Toast.makeText(getApplicationContext(), "账户暂未开通该功能",
								Toast.LENGTH_SHORT).show();
//						ToastCustom.showMessage(BalanceDetailsAcitvity.this, "账户暂未开通该功能!"
//								+ treasureBean.getLogsts());
					}
				} else {
					loadingDialogWhole.dismiss();
					
					warnDialog = new OneButtonDialogWarn(BalanceDetailsAcitvity.this,
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
				Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络连接",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(BalanceDetailsAcitvity.this, "数据获取失败,请检查网络连接");
				loadingDialogWhole.dismiss();
			}
			super.onPostExecute(result);
			
				
		}
		
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(loadingDialogWhole!=null){
		loadingDialogWhole.dismiss();
		}
	}
}
