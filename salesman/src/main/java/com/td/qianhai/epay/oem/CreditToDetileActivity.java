package com.td.qianhai.epay.oem;

import java.util.HashMap;

import net.tsz.afinal.FinalBitmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.interfaces.onMyaddTextListener;
import com.td.qianhai.epay.oem.mail.utils.GetImageUtil;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.MyTextWatcher;
import com.td.qianhai.epay.oem.views.dialog.MyEditDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class CreditToDetileActivity extends BaseActivity {

	private ImageView bank_imgs,im_bank;

	private TextView btn_credit_confirm,balance_tv,et_credit_name,et_credit_balance;
	
	private EditText et_credit_card;

	private MyEditDialog doubleWarnDialog;

	private String money, mercnum,banknum = "";
	
	private ProgressBar pro;
	
	private String  ids;
	
	private String mobile;
	
	private OneButtonDialogWarn warnDialog;
	
	private String[] c;
	
	private int[] d;
	
	private String[] b;
	

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cretdit_detile);
		AppContext.getInstance().addActivity(this);
//		mercnum = ((AppContext) getApplication()).getMercNum();
//		
//		mobile = ((AppContext) getApplication()).getMobile();
		mercnum = MyCacheUtil.getshared(this).getString("MercNum", "");
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		Intent it = getIntent();
		
		ids = it.getStringExtra("ids");
		
		initview();
		
		GetWalletInfo  walletinfo = new GetWalletInfo();
		
		walletinfo.execute(HttpUrls.RICH_TREASURE_INFO + "",
				mobile);

	}

	private void initview() {
		
		b = new String[]{"农业银行","北京银行","中国银行","建设银行","光大银行","兴业银行","中信银行","招商银行","民生银行","广东发展银行","华夏银行",
				"工商银行","邮政储蓄银行","平安银行","浦东发展银行","包商银行","上海银行"};
		c = new String[]{"ABCCREDIT","BCCBCREDIT","BOCCREDIT","CCBCREDIT","EVERBRIGHTCREDIT","CIBCREDIT","ECITICCREDIT",
				"CMBCHINACREDIT","CMBCCREDIT","GDBCREDIT","HXBCREDIT","ICBCCREDIT","PSBCCREDIT",
				"PINGANCREDIT","SPDBCREDIT","BSBCREDIT","BOSHCREDIT"};
		d = new int[]{R.drawable.ps_abc,R.drawable.ps_bjb,R.drawable.ps_boc,R.drawable.ps_ccb,R.drawable.ps_cebb,
				R.drawable.ps_cib,R.drawable.ps_citic,R.drawable.ps_cmb,R.drawable.ps_cmbc,
				R.drawable.ps_gdb,R.drawable.ps_hxb,R.drawable.ps_icbc,R.drawable.ps_psbc,
				R.drawable.ps_spa,R.drawable.ps_spdb,R.drawable.ps_bsb,R.drawable.ps_sh};
		((TextView) findViewById(R.id.tv_title_contre)).setText("添加信用卡");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});

		bank_imgs = (ImageView) findViewById(R.id.bank_imgs);
		im_bank = (ImageView) findViewById(R.id.im_bank);
		pro = (ProgressBar) findViewById(R.id.getbanck_pro);
		btn_credit_confirm = (TextView) findViewById(R.id.btn_credit_confirm);
		et_credit_balance = (TextView) findViewById(R.id.et_credit_balance);
		btn_credit_confirm.setEnabled(false);
		balance_tv = (TextView) findViewById(R.id.balance_tv);
		et_credit_name = (TextView) findViewById(R.id.et_credit_name);
		et_credit_card = (EditText) findViewById(R.id.et_credit_card);
		et_credit_card.addTextChangedListener(new MyTextWatcher(et_credit_card));
		
		bank_imgs.setAlpha(200);
		
//		et_credit_card.addTextChangedListener(new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
//				// TODO Auto-generated method stub
//				
//				if(s.length()>=16){
//					 String card = et_credit_card.getText().toString();
//					
//					GetbankName banktexk = new GetbankName();
//					banktexk.execute(HttpUrls.GETBANKNAME+ "",card);
//				}
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});

		et_credit_balance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(CreditToDetileActivity.this,SelectBankActivity.class);
				it.putExtra("tag", "4");
				startActivityForResult(it, 1);
//				Intent it = new Intent(CreditToDetileActivity.this,ChooseBankActivity.class);
//				it.putExtra("tag", "0");
//				startActivityForResult(it, 1);
			}
		});
//		et_credit_balance.addTextChangedListener(new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
//				// TODO Auto-generated method stub
//				
//				if(s.length()>0){
//					String aa = et_credit_card.getText().toString();
//					String bb = et_credit_name.getText().toString();
//					if(aa.length()>=14&&bb!=null&&!bb.equals("")){
//						btn_credit_confirm.setEnabled(true);
//					}
//					
//				}
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		et_credit_name.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
				if(s.length()>0){
					String aa = et_credit_card.getText().toString().replace(" ","");;
					String bb = et_credit_balance.getText().toString();
					if(aa.length()>=14&&bb!=null&&!bb.equals("")){
						btn_credit_confirm.setEnabled(true);
					}
					
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
		
		et_credit_card.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
				if(s.length()>=14){
					String aa = et_credit_name.getText().toString();
					String bb = et_credit_balance.getText().toString();
					if(aa!=null&&!aa.equals("")&&bb!=null&&!bb.equals("")){
						btn_credit_confirm.setEnabled(true);
					}
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
//				final String money = et_credit_balance.getText().toString();
				
//				final String name = et_credit_name.getText().toString();
				
				final String card = et_credit_card.getText().toString().replace(" ","");;
				
//				int a = (int) (Double.parseDouble(money) * 100);
//				final String moneys = String.valueOf(a);
				
//				doubleWarnDialog = new MyEditDialog(
//						CreditToDetileActivity.this, R.style.MyEditDialog,
//						"还款", "请输入支付密码", "确认", "取消", moneys,
//						new OnMyDialogClickListener() {
//
//							@Override
//							public void onClick(View v) {
//
//								switch (v.getId()) {
//								case R.id.btn_right:
//									doubleWarnDialog.dismiss();
//									InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//									m.toggleSoftInput(0,
//											InputMethodManager.HIDE_NOT_ALWAYS);
//									break;
//								case R.id.btn_left:
//									String paypwd = doubleWarnDialog
//											.getpaypwd();
//
//									if (paypwd == null || paypwd.equals("")) {
//										Toast.makeText(getApplicationContext(),"请输入支付密码",
//												Toast.LENGTH_SHORT).show();
////										ToastCustom.showMessage(
////												CreditToDetileActivity.this,
////												"请输入支付密码！");
//										return;
//									}
//									if (paypwd.length() < 6
//											|| paypwd.length() > 15) {
//										Toast.makeText(getApplicationContext(),"输入的密码长度有误,请输入6～15个数字、字母或特殊符号",
//												Toast.LENGTH_SHORT).show();
////										ToastCustom.showMessage(CreditToDetileActivity.this,
////														"输入的密码长度有误,请输入6～15个数字、字母或特殊符号！");
//										return;
//									}
//
//									break;
//								default:
//									break;
//								}
//							}
//						}, new onMyaddTextListener() {
//
//							@Override
//							public void refreshActivity(String paypwd) {
//
//								if (paypwd == null || paypwd.equals("")) {
//									Toast.makeText(getApplicationContext(),"请输入支付密码",
//											Toast.LENGTH_SHORT).show();
////									ToastCustom.showMessage(
////											CreditToDetileActivity.this,
////											"请输入支付密码！");
//									return;
//								}
//								if (paypwd.length() < 6 || paypwd.length() > 15) {
//									Toast.makeText(getApplicationContext(),"输入的密码长度有误,请输入6个数字、字母或特殊符号",
//											Toast.LENGTH_SHORT).show();
////									ToastCustom.showMessage(
////											CreditToDetileActivity.this,
////											"输入的密码长度有误,请输入6个数字、字母或特殊符号！");
//									return;
//								}
								String name = et_credit_balance.getText().toString();
								CreditToPay payTask = new CreditToPay();
								payTask.execute(HttpUrls.ADDCREDITCARD+ "", mobile,name,banknum,card);

//							}
//						});
//				doubleWarnDialog.setCancelable(false);
//				doubleWarnDialog.setCanceledOnTouchOutside(false);
//				doubleWarnDialog.show();

			}
		});

	}

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
			String[] values = { params[0], params[1], params[2], params[3],params[4]};
			return NetCommunicate.getMidatc(HttpUrls.ADDCREDITCARD,
					values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					warnDialog = new OneButtonDialogWarn(CreditToDetileActivity.this,
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
//					Intent it = new Intent(CreditToDetileActivity.this,CreditCardResultActivity.class);
//					it.putExtra("tag", "0");
//					it.putExtra("card", et_credit_card.getText().toString());
//					it.putExtra("name", et_credit_name.getText().toString());
//					it.putExtra("result", result.get("RSPMSG").toString());
//					it.putExtra("id", result.get("ORDERID").toString());
//					startActivity(it);
//					finish();
					
				} else {
//					doubleWarnDialog.dismiss();
//					Intent it = new Intent(CreditToDetileActivity.this,CreditCardResultActivity.class);
//					it.putExtra("tag", "1");
//					it.putExtra("result", result.get("RSPMSG").toString());
//					startActivity(it);
					warnDialog = new OneButtonDialogWarn(CreditToDetileActivity.this,
							R.style.CustomDialog, "提示", result.get(Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
//									finish();
									warnDialog.dismiss();
								}
							});
					if(warnDialog!=null){
						warnDialog.show();
					}

				}
			} else {
				Toast.makeText(getApplicationContext(),"fail",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(CreditToDetileActivity.this, "fail");
			}
			super.onPostExecute(result);
		}
	}
	
	
	/**
	 * 查询银行
	 * 
	 * 
	 */
	class GetbankName extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pro.setVisibility(View.VISIBLE);
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1]};
			return NetCommunicate.getPay(HttpUrls.GETBANKNAME,
					values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			pro.setVisibility(View.GONE);
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					if( result.get("ISSNAM")!=null){
						String banckname = result.get("ISSNAM").toString();
						if (banckname.substring(0,2).equals("招商")) {
							bank_imgs.setImageResource(R.drawable.ps_cmb);
						}else if(banckname.substring(0,2).equals("农业")) {
							bank_imgs.setImageResource(R.drawable.ps_abc);
						}else if(banckname.substring(0,2).equals("农行")) {
							bank_imgs.setImageResource(R.drawable.ps_abc);
						}else if(banckname.substring(0,2).equals("北京")){
							bank_imgs.setImageResource(R.drawable.ps_bjb);
						}else if(banckname.substring(0,2).equals("中国")){
							if(banckname.substring(0,4).equals("中国建设")){
								bank_imgs.setImageResource(R.drawable.ps_ccb);
							}else{
								bank_imgs.setImageResource(R.drawable.ps_boc);
							}
						}else if(banckname.substring(0,2).equals("建设")){
							bank_imgs.setImageResource(R.drawable.ps_ccb);
						}else if(banckname.substring(0,2).equals("光大")){
							bank_imgs.setImageResource(R.drawable.ps_cebb);
						}else if(banckname.substring(0,2).equals("兴业")){
							bank_imgs.setImageResource(R.drawable.ps_cib);
						}else if(banckname.substring(0,2).equals("中信")){
							bank_imgs.setImageResource(R.drawable.ps_citic);
						}else if(banckname.substring(0,2).equals("民生")){
							bank_imgs.setImageResource(R.drawable.ps_cmbc);
						}else if(banckname.substring(0,2).equals("交通")){
							bank_imgs.setImageResource(R.drawable.ps_comm);
						}else if(banckname.substring(0,2).equals("华夏")){
							bank_imgs.setImageResource(R.drawable.ps_hxb);
						}else if(banckname.substring(0,4).equals("广东发展")){
							bank_imgs.setImageResource(R.drawable.ps_gdb);
						}else if(banckname.substring(0,2).equals("广发")){
							bank_imgs.setImageResource(R.drawable.ps_gdb);
						}else if(banckname.substring(0,2).equals("邮政")){
							bank_imgs.setImageResource(R.drawable.ps_psbc);
						}else if(banckname.substring(0,2).equals("邮储")){
							bank_imgs.setImageResource(R.drawable.ps_psbc);
						}else if(banckname.substring(0,2).equals("工商")){
							bank_imgs.setImageResource(R.drawable.ps_icbc);
						}else if(banckname.substring(0,2).equals("平安")){
							bank_imgs.setImageResource(R.drawable.ps_spa);
						}else if(banckname.substring(0,2).equals("浦东")){
							bank_imgs.setImageResource(R.drawable.ps_spdb);
						}else if(banckname.substring(0,2).equals("工商")){
							bank_imgs.setImageResource(R.drawable.ps_icbc);
						}else{
							bank_imgs.setImageResource(R.drawable.ps_unionpay);
						}
					}
				} else {
				}
			} else {
				Toast.makeText(getApplicationContext(),"fail",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(CreditToDetileActivity.this, "fail");
			}
			super.onPostExecute(result);
		}
	}
	
	
class GetWalletInfo extends AsyncTask<String , Integer, HashMap<String, Object>>{
		
		@Override
		protected void onPreExecute() {
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
					if(result.get("MERNAM")!=null){
						et_credit_name.setText(result.get("MERNAM").toString());
					}
//					if(result.get("AVAAMT")!=null){
//						avaamt = result.get("AVAAMT").toString();
//						
//						}
//					
//					if (avaamt.length() == 1) {
//						balance_tv.setText("0.0" + avaamt);
//					} else if (avaamt.length() == 2) {
//						balance_tv.setText("0." + avaamt);
//					} else {
//						balance_tv.setText(avaamt.substring(0,
//								avaamt.length() - 2)
//								+ "."
//								+ avaamt.substring(
//										avaamt.length() - 2));
//					}
					
				} else {
					
					warnDialog = new OneButtonDialogWarn(CreditToDetileActivity.this,
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
				Toast.makeText(getApplicationContext(),"fail",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(CreditToDetileActivity.this, "数据获取失败,请检查网络连接");
			}
			super.onPostExecute(result);
			
			}
				
		}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == 2) {
//			im_bank.setVisibility(View.VISIBLE);
//			String banks = data.getExtras().getString("result");
//			et_credit_balance.setText(banks);
//			String imgurl = data.getExtras().getString("img");
//			Bitmap bit = null;
//			try {
//				bit = GetImageUtil.iscace(im_bank, HttpUrls.HOST_POSM + imgurl);
//			} catch (Exception e) {
//				// TODO: handle exception\
//				Log.e("", "" + e.toString());
//			}
//			if (bit != null) {
//				im_bank.setImageBitmap(bit);
//			} else {
//			}
//		}
//	}
//	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == 1) {
    		String name = data.getExtras().getString("result");
    		banknum = data.getExtras().getString("num");
    		String url = data.getExtras().getString("img");
//    		int id = data.getExtras().getInt("result");//得到新Activity 关闭后返回的数据
    		
    		
//			banknum = c[(int)id];
//			bakname = b[(int)id];
//			imgid = d[(int)id];
			im_bank.setVisibility(View.VISIBLE);
			
			et_credit_balance.setText(name);
			
			FinalBitmap.create(CreditToDetileActivity.this).display(im_bank,
					HttpUrls.HOST_POSM + url,
					im_bank.getWidth(),
					im_bank.getHeight(), null, null);
			
			String aa = et_credit_card.getText().toString().replace(" ","");;
			String bb = et_credit_name.getText().toString();
			if(aa!=null&&!aa.equals("")&&bb!=null&&!bb.equals("")){
				btn_credit_confirm.setEnabled(true);
			}
			
    	}
    }
}
