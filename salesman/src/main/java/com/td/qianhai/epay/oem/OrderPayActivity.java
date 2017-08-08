package com.td.qianhai.epay.oem;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Random;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.mail.utils.SetPricePoint;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.TwoButtonDialogStylePrompty;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.epay.utils.DateUtil;
import com.td.qianhai.epay.utils.InputMethodUtils;

public class OrderPayActivity extends BaseActivity implements OnClickListener {
	/** 金额、付款卡号、付款卡号开户姓名 */
	private EditText etBalance,edits;
	/** 金额错误提示框,结算方式 */
	private TextView etBalanceError, tvClearing, tvContent, tvRight,rate_remind;
	/** 提交按钮 */
	private TextView btnConfirm, tvbycard,capital_tvs;
	private String clearing;
	private String custId;
	// private RadioDialog dialog;
	private String paytype = "0302";
	private String urltype = "1", merordername = "MAC", prdshortname = "",
			merorderdesc = "", merType = "02";
	private Spinner sps;

	private OneButtonDialogWarn warnDialog;
	
	private LinearLayout capital_layout;
	
	private String raten;
	
	private SharedPreferences share;
	
	private TwoButtonDialogStylePrompty doubleWarnDialog;
	
	private void initView() {

		tvRight = (TextView) findViewById(R.id.bt_title_right);
		capital_layout = (LinearLayout) findViewById(R.id.capital_layouts);
		capital_tvs  = (TextView) findViewById(R.id.capital_tvs);
		tvbycard = (TextView) findViewById(R.id.btn_order_receive_bycard);
		etBalance = (EditText) findViewById(R.id.et_order_receive_balance);
		etBalance.setFocusable(true);
		etBalance.setFocusableInTouchMode(true);
		etBalance.requestFocus();
		btnConfirm = (TextView) findViewById(R.id.btn_order_receive_confirm);
		etBalanceError = (TextView) findViewById(R.id.et_order_receive_balance_error);
		tvContent = (TextView) findViewById(R.id.tv_title_contre);
//		custId = ((AppContext) getApplication()).getCustId();
		custId = share.getString("Mobile", "");
		tvContent.setText("充值");
		tvClearing = (TextView) findViewById(R.id.et_receive_clearing);
		tvbycard.setOnClickListener(this);
		tvClearing.setOnClickListener(this);
		etBalanceError.setOnClickListener(this);
		btnConfirm.setOnClickListener(this);
		tvRight.setOnClickListener(this);
		edits = (EditText) findViewById(R.id.edits);
		edits.setText(getRandomString(18));
		findViewById(R.id.bt_title_left).setOnClickListener(this);

		InputMethodUtils.show(OrderPayActivity.this, etBalance);

		etBalance.setSelection(etBalance.length());
		
		
		etBalance.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					capital_layout.setVisibility(View.VISIBLE);
					try {
						String a = (Double.parseDouble(etBalance.getText().toString())-(Double.parseDouble(etBalance.getText().toString())*((Double.parseDouble(raten))/100)))+"";
						
//						String str = String.valueOf(a);
//						String[] nstr = a.split("\\.");
//						String b = nstr[1].substring(0,2);
						String c = a.substring(0, a.length()-1);
						
						BigDecimal mData = new BigDecimal(a).setScale(2, BigDecimal.ROUND_HALF_UP);
						
						capital_tvs.setText("钱包实际入账   : "+mData+"元");
						if (Double.parseDouble(s.toString()) > 20000) {
							etBalanceError.setVisibility(View.VISIBLE);
							etBalanceError.setText("充值金额不能大于两万整");
							etBalanceError.setError("充值金额不能大于两万整");
							etBalance.setVisibility(View.GONE);
							etBalance.setText("");
							return;
						}
					} catch (Exception e) {
						capital_tvs.setText("输入有误。。。");
					}
				} else {
					capital_layout.setVisibility(View.GONE);
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

		etBalance.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					balanceFlag();
				}
			}
		});
		SetPricePoint.setPricePoint(etBalance);

	}
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_pay_layout);
		AppContext.getInstance().addActivity(this);
		share = MyCacheUtil.getshared(OrderPayActivity.this);
		raten = share.getString("nocardfeerate","");
		initView();
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		Rect rect = new Rect();
		View view = getWindow().getDecorView();
		view.getWindowVisibleDisplayFrame(rect);
		
//		OrderTask1 task1 = new OrderTask1();
//		task1.execute(HttpUrls.YIBAO + "", "10", urltype,
//				merordername, prdshortname, merorderdesc, merType,"IDCARD","432503199212010059","彭杰","6228481388554376077","1","15773876350");
	}
	
	public String getRandomString(int length) { //length表示生成字符串的长度  
	    String base = "0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 } 
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		rate_remind = (TextView) findViewById(R.id.rate_remind);
		rate_remind.setText("费率: "+raten+"%");
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		rate_remind.setText("费率: "+raten+"%");
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

	private void balanceFlag() {
		String balance = etBalance.getText().toString();

		if (balance != null && !balance.equals("")) {
			float intBalance = Float.parseFloat(balance);
			if (intBalance < 2) {

//				ToastCustom.showMessage(this, "充值金额不能小于1");

				return;
			}
		}

		if (balance == null || (balance != null && balance.equals(""))) {
			etBalanceError.setVisibility(View.VISIBLE);
			etBalanceError.setText("请输入充值金额");
			etBalanceError.setError("请输入充值金额");
			etBalance.setVisibility(View.GONE);
			return;
		}
		if (!isNumber(balance)) {
			etBalanceError.setVisibility(View.VISIBLE);
			etBalanceError.setText("格式错误");
			etBalanceError.setError("格式错误");
			etBalance.setVisibility(View.GONE);
			return;
		} else {
			if (balance.substring(balance.length() - 1, balance.length())
					.equals(".")) {
				etBalanceError.setVisibility(View.VISIBLE);
				etBalanceError.setText("格式错误");
				etBalanceError.setError("格式错误");
				etBalance.setVisibility(View.GONE);
				return;
			}
		}
		if (balance.length() >= 3) {
			if ((balance.indexOf(String.valueOf('.')) == -1)) {
				String balanceSub = balance.substring(balance.length() - 3,
						balance.length());
				Log.v("balanceSub3", "" + balanceSub);
				char[] balances = balanceSub.toCharArray();

				String balanceOne = balance.substring(balance.length() - 1,
						balance.length());
				String balanceTwo = balance.substring(balance.length() - 2,
						balance.length() - 1);
				String balanceThree = balance.substring(balance.length() - 3,
						balance.length() - 2);

				System.out.println(balanceOne + "\n" + balanceTwo + "\n"
						+ balanceThree);

				// if ((balances[0] == balances[1] && balances[0] ==
				// balances[2])
				// || balanceOne.equals(balanceTwo)
				// && balanceOne.equals(balanceThree)) {
				// etBalanceError.setVisibility(View.VISIBLE);
				// etBalanceError.setText("后三位不能是相同的数");
				// etBalanceError.setError("后三位不能是相同的数");
				// etBalance.setVisibility(View.GONE);
				// return;
				// }

			} else {
				// 字符串中存在.，在第balance.indexOf(String.valueOf('.')) 个位置
				String balanceSub = balance.substring(0,
						balance.lastIndexOf("."));
				String balanceSub2 = balance.substring(
						balance.lastIndexOf(".") + 1, balance.length());
				Log.v("balanceSub1", "" + balanceSub);
				if (balanceSub.length() >= 3) {
					balanceSub = balanceSub.substring(balanceSub.length() - 3,
							balanceSub.length());
					char[] balances = balanceSub.toCharArray();
					char[] balances2 = balanceSub2.toCharArray();
					boolean flag = true;
					for (int i = 0; i < balances2.length; i++) {
						if (i != 0 && balances2[0] == balances2[i]) {
							System.out.println("判断失败");
							flag = false;
							break;
						}
					}
					// if (balances[0] == balances[1]
					// && balances[0] == balances[2] && flag == false) {
					// ToastCustom.showMessage(this, "后三位不能是相同的数",
					// Toast.LENGTH_SHORT);
					// return;
					// }
				}
			}
		}
	}

	private void receive() {
		etBalance.clearFocus();
		// etCardName.clearFocus();
		String balance = etBalance.getText().toString();

		if (balance != null && !balance.equals("")) {
			float intBalance = Float.parseFloat(balance);
			if (intBalance < 2) {
				Toast.makeText(getApplicationContext(),"充值金额不能小于2.00元",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(this, "充值金额不能小于2.00元");
				return;
				
			} else if (intBalance > 100000) {
				etBalance.setText("");
				Toast.makeText(getApplicationContext(),"充值金额不能大于100000元,请重新输入",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(OrderPayActivity.this,
//						"充值金额不能大于100000元,请重新输入");
				etBalance.setFocusable(true);
				etBalance.setFocusableInTouchMode(true);
				return;
			}
			
		}

		if (balance == null || (balance != null && balance.equals(""))) {
			etBalanceError.setVisibility(View.VISIBLE);
			etBalanceError.setText("请输入充值金额");
			etBalanceError.setError("请输入充值金额");
			etBalance.setVisibility(View.GONE);
			return;
		}
		if (!isNumber(balance)) {
			etBalanceError.setVisibility(View.VISIBLE);
			etBalanceError.setText("格式错误");
			etBalanceError.setError("格式错误");
			etBalance.setVisibility(View.GONE);
			return;
		} else {
			if (balance.substring(balance.length() - 1, balance.length())
					.equals(".")) {
				etBalanceError.setVisibility(View.VISIBLE);
				etBalanceError.setText("格式错误");
				etBalanceError.setError("格式错误");
				etBalance.setVisibility(View.GONE);
				return;
			}
		}
		if (balance.length() >= 3) {
			if ((balance.indexOf(String.valueOf('.')) == -1)) {
				String balanceSub = balance.substring(balance.length() - 3,
						balance.length());
				Log.v("balanceSub3", "" + balanceSub);
				char[] balances = balanceSub.toCharArray();

				String balanceOne = balance.substring(balance.length() - 1,
						balance.length());
				String balanceTwo = balance.substring(balance.length() - 2,
						balance.length() - 1);
				String balanceThree = balance.substring(balance.length() - 3,
						balance.length() - 2);

				System.out.println(balanceOne + "\n" + balanceTwo + "\n"
						+ balanceThree);

				// if ((balances[0] == balances[1] && balances[0] ==
				// balances[2])
				// || balanceOne.equals(balanceTwo)
				// && balanceOne.equals(balanceThree)) {
				// etBalanceError.setVisibility(View.VISIBLE);
				// etBalanceError.setText("后三位不能是相同的数");
				// etBalanceError.setError("后三位不能是相同的数");
				// etBalance.setVisibility(View.GONE);
				// return;
				// }

			} else {
				// 字符串中存在.，在第balance.indexOf(String.valueOf('.')) 个位置
				String balanceSub = balance.substring(0,
						balance.lastIndexOf("."));
				String balanceSub2 = balance.substring(
						balance.lastIndexOf(".") + 1, balance.length());
				Log.v("balanceSub1", "" + balanceSub);
				if (balanceSub.length() >= 3) {
					balanceSub = balanceSub.substring(balanceSub.length() - 3,
							balanceSub.length());
					char[] balances = balanceSub.toCharArray();
					char[] balances2 = balanceSub2.toCharArray();
					boolean flag = true;
					for (int i = 0; i < balances2.length; i++) {
						if (i != 0 && balances2[0] == balances2[i]) {
							System.out.println("判断失败");
							flag = false;
							break;
						}
					}
					// if (balances[0] == balances[1]
					// && balances[0] == balances[2] && flag == false) {
					// ToastCustom.showMessage(this, "后三位不能是相同的数",
					// Toast.LENGTH_SHORT);
					// return;
					// }
				}
			}
		}

		//
		// clearing = tvClearing.getText().toString();
		//
		// if (clearing == null || (clearing != null && clearing.equals(""))) {
		// ToastCustom.showMessage(this, "请选择结算方式", Toast.LENGTH_SHORT);
		// return;
		// }
		
//		if(MyCacheUtil.getshared(OrderPayActivity.this).getString("Txnsts", "").equals("0")){
			String balances = etBalance.getText().toString();
//			int a = (int) (Double.parseDouble(balances) * 100);
//			balances = String.valueOf(a);
			Intent it = new Intent(OrderPayActivity.this,PayMainActivity.class);
			
			it.putExtra("balance", balances);
			finish();
			startActivity(it);
//		}else{
//			SpannableString msps = new SpannableString("");
//			showDoubleWarnDialog(msps);
//		}

		
//			OrderTask task = new OrderTask();
//			task.execute(HttpUrls.ORDER_CREATE + "", custId, balance, urltype,
//					merordername, prdshortname, merorderdesc, merType);

	}
	
	
	/***
	 * 双按钮提示dialog
	 * 
	 * @param msg
	 */
	protected void showDoubleWarnDialog(SpannableString msg) {
		 doubleWarnDialog = new TwoButtonDialogStylePrompty(this,
				R.style.CustomDialog, "提示", msg, "同意", "取消",
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doubleWarnOnClick(v);
					}
				});
		doubleWarnDialog.setCancelable(false);
//		doubleWarnDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode,
//					KeyEvent event) {
//				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
//					return true;
//				} else {
//					return true; // 默认返回 false
//				}
//			}
//		});
		doubleWarnDialog.setCanceledOnTouchOutside(false);
		doubleWarnDialog.show();
	}
	
	
	protected void doubleWarnOnClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
			String balance = etBalance.getText().toString();
			int a = (int) (Double.parseDouble(balance) * 100);
			balance = String.valueOf(a);
			Intent it = new Intent(OrderPayActivity.this,PayMainActivity.class);
			
			it.putExtra("balance", a/100+"");
			startActivity(it);
			finish();
			doubleWarnDialog.dismiss();
		break;
		case R.id.btn_right:
			doubleWarnDialog.dismiss();
			break;
		default:
			break;
		}
	}

	class OrderTask extends AsyncTask<String, Integer, HashMap<String, Object>> {
		private AlertDialog dialog;

		@Override
		protected void onPreExecute() {
			AlertDialog.Builder builder = new Builder(OrderPayActivity.this);
			builder.setCancelable(false);
			dialog = builder.create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			Window mWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = mWindow.getAttributes();
			lp.dimAmount = 0f;
			dialog.setContentView(R.layout.load);
			super.onPreExecute();
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3],
					params[4], params[5], params[6], params[7] };
			return NetCommunicate.getPay(HttpUrls.ORDER_CREATE, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			dialog.dismiss();
			if (result != null) {
				if (Entity.STATE_OK.equals(result.get(Entity.RSPCOD))) {
					Intent intent = new Intent(OrderPayActivity.this,
							OnlineWeb.class);
					intent.putExtra("urlStr", result.get("REURL").toString());
					intent.putExtra("titleStr", "充值");
					startActivity(intent);
					finish();
				} else {
					ToastCustom.showMessage(OrderPayActivity.this,
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT);
				}
			}
			super.onPostExecute(result);
		}
	}
	
	

	// class OrderPayTask extends
	// AsyncTask<String, Integer, HashMap<String, Object>> {
	// private AlertDialog dialog;
	//
	// @Override
	// protected void onPreExecute() {
	// AlertDialog.Builder builder = new Builder(OrderPayActivity.this);
	// builder.setCancelable(false);
	// dialog = builder.create();
	// dialog.setCanceledOnTouchOutside(false);
	// dialog.show();
	// Window mWindow = dialog.getWindow();
	// WindowManager.LayoutParams lp = mWindow.getAttributes();
	// lp.dimAmount = 0f;
	// dialog.setContentView(R.layout.load);
	// super.onPreExecute();
	// }
	//
	// @Override
	// protected HashMap<String, Object> doInBackground(String... params) {
	// String[] values = { params[0], params[1], params[2], params[3] };
	// return NetCommunicate.getPay(HttpUrls.ORDER_PAY, values);
	// }
	//
	// @Override
	// protected void onPostExecute(HashMap<String, Object> result) {
	// dialog.dismiss();
	// if (result != null) {
	// if (Entity.STATE_OK.equals(result.get(Entity.RSPCOD))) {
	// Log.e("", result.get("REURL").toString());
	// Intent intent = new Intent();
	// intent.setAction("android.intent.action.VIEW");
	// Uri content_url1 = Uri
	// .parse(result.get("REURL").toString());
	// intent.setData(content_url1);
	// startActivity(intent);
	// } else {
	// ToastCustom.showMessage(OrderPayActivity.this,
	// result.get(Entity.RSPMSG).toString(),
	// Toast.LENGTH_SHORT);
	// }
	// }
	// super.onPostExecute(result);
	// }
	// }

	public void onClick(View v) {
		if (DateUtil.isFastDoubleClick()) {
			return;
		} else {
			switch (v.getId()) {
			case R.id.bt_title_left:
				finish();
				break;
			case R.id.btn_order_receive_confirm:
				receive();
				break;
			case R.id.btn_order_receive_bycard:
				ToastCustom.showMessage(this, "此功能即将开通", Toast.LENGTH_SHORT);
				break;
			case R.id.et_order_receive_balance_error:
				etBalance.setVisibility(View.VISIBLE);
				etBalanceError.setVisibility(View.GONE);
				etBalance.requestFocus();
				InputMethodUtils.show(OrderPayActivity.this, etBalance);
				break;

			case R.id.bt_title_right:

				Intent it = new Intent(OrderPayActivity.this,
						DealRecordsActivity.class);
//				 DealRecordsActivity
				
				startActivity(it);
//				Intent it = new Intent(OrderPayActivity.this,MentionNowAcitvity.class);
//				
//				it.putExtra("State", 3);
//				it.putExtra("res", "sss");
//				startActivity(it);
//				finish();

				break;
			case R.id.et_receive_clearing:
				// dialog = new RadioDialog(OrderPayActivity.this,
				// R.style.CustomDialog, new OnBackDialogClickListener() {
				//
				// @Override
				// public void OnBackClick(View v, String str,
				// int position) {
				// // TODO Auto-generated method stub
				// tvClearing.setText(str);
				// if (position == 0) {
				// clearingId = "0";
				// } else if (position == 1) {
				// clearingId = "1";
				// } else if (position == 2) {
				// clearingId = "5";
				// }
				// dialog.dismiss();
				// }
				// });
				// dialog.setCancelable(false);
				// dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				// @Override
				// public boolean onKey(DialogInterface dialog, int keyCode,
				// KeyEvent event) {
				// if (keyCode == KeyEvent.KEYCODE_SEARCH) {
				// return true;
				// } else {
				// return true; // 默认返回 false
				// }
				// }
				// });
				// dialog.setCanceledOnTouchOutside(false);
				// dialog.show();
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
