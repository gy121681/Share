package com.td.qianhai.epay.oem;

import java.util.HashMap;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.RichTreasureBean;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.SuccessHintDialog;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.mpay.utils.XhgUtil;

public class IntoBasisActivity extends BaseActivity {

	private RelativeLayout btn_basis_model, rl_capital;
	private Button btn_next;
	private EditText et_into_money;
	private TextView tv_into_money, tv_basis_model, tv_capital,et_into_money_error;
	private Double money;
	private RichTreasureBean treasureBean;
	private HashMap<String, Object> hashMap;
	private String basisModel, mobile;
	private SuccessHintDialog hintDialog;
	private OneButtonDialogWarn warnDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.into_on_basis);
		AppContext.getInstance().addActivity(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		treasureBean = ((AppContext) getApplication()).getTreasureBean();
//		mobile = ((AppContext) getApplication()).getMobile();
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		initView();
	}

	private void initView() {
		btn_basis_model = (RelativeLayout) findViewById(R.id.btn_basis_model);
		btn_next = (Button) findViewById(R.id.btn_next);
		rl_capital = (RelativeLayout) findViewById(R.id.rl_hint_capital);
		tv_capital = (TextView) findViewById(R.id.tv_capital);
		et_into_money_error = (TextView) findViewById(R.id.et_into_money_error);
		et_into_money = (EditText) findViewById(R.id.et_into_money);
		tv_into_money = (TextView) findViewById(R.id.tv_into_money);
		tv_basis_model = (TextView) findViewById(R.id.tv_basis_model);

		if (treasureBean.getAvaamt().length() == 1) {
			tv_into_money.setText("0.0" + treasureBean.getAvaamt());
		} else if (treasureBean.getAvaamt().length() == 2) {
			tv_into_money.setText("0." + treasureBean.getAvaamt());
		} else {
			tv_into_money.setText(treasureBean.getAvaamt().substring(0,
					treasureBean.getAvaamt().length() - 2)
					+ "."
					+ treasureBean.getAvaamt().substring(
							treasureBean.getAvaamt().length() - 2));
		}
		btn_basis_model.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(IntoBasisActivity.this,
						IntoModelListActiviy.class);
				startActivityForResult(intent, 8);
			}
		});
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				validation();
			}
		});

		et_into_money.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					rl_capital.setVisibility(View.VISIBLE);
					try {
						tv_capital.setText(XhgUtil.change(Double
								.parseDouble(et_into_money.getText().toString())));
						money = Double.parseDouble(et_into_money.getText()
								.toString()) * 100;
						if (money > Double.parseDouble(treasureBean.getAvaamt())) {
							et_into_money.setVisibility(View.GONE);
							et_into_money_error.setVisibility(View.VISIBLE);
							et_into_money_error.setText("金额超限");
							et_into_money_error.setError("金额超限");
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

	}

	private void validation() {
		String withdrawlMoney = et_into_money.getText().toString();
		if (hashMap == null) {
//			ToastCustom.showMessage(IntoBasisActivity.this, "请选择定存模式");
			return;
		}
		basisModel = hashMap.get("DPTMID").toString();
		if (basisModel == null) {
//			ToastCustom.showMessage(IntoBasisActivity.this, "请选择定存模式");
			return;
		}
		if (hashMap == null) {
//			ToastCustom.showMessage(IntoBasisActivity.this, "请选择定存模式");
			return;
		}
		if (withdrawlMoney == null || withdrawlMoney.equals("")) {
//			ToastCustom.showMessage(IntoBasisActivity.this, "请输入要入账的金额");
			return;
		}
		money = Double.parseDouble(withdrawlMoney) * 100;
		if (money > Double.parseDouble(treasureBean.getAvaamt())) {
//			ToastCustom.showMessage(IntoBasisActivity.this, "入账金额超限");
			return;
		}
		if (money <= 0) {
//			ToastCustom.showMessage(IntoBasisActivity.this, "入账金额必须大于零元");
			return;
		}
		if (!isNumber(withdrawlMoney)) {
//			ToastCustom.showMessage(IntoBasisActivity.this, "格式错误");
			return;
		} else {
			if (withdrawlMoney.substring(withdrawlMoney.length() - 1,
					withdrawlMoney.length()).equals(".")) {
//				ToastCustom.showMessage(IntoBasisActivity.this, "格式错误");
				return;
			}
		}
		int a = (int) (Double.parseDouble(withdrawlMoney) * 100);

		IntoBasisTask intoBasisTask = new IntoBasisTask();
		intoBasisTask.execute(HttpUrls.BASIS_DEPOSIT + "", mobile, basisModel,
				String.valueOf(a));

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 8) {
			if (data != null) {
				hashMap = (HashMap<String, Object>) data
						.getSerializableExtra("basis");
				tv_basis_model.setText(hashMap.get("DPTMNAM").toString());
			}
		}
	}

	/**
	 * 存入定期
	 * 
	 * @author liangge
	 * 
	 */
	class IntoBasisTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			showLoadingDialog("正在努力加载...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate.getMidatc(HttpUrls.BASIS_DEPOSIT, values);
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
					hintDialog = new SuccessHintDialog(IntoBasisActivity.this,
							R.style.CustomDialog, "提示", result.get(
									Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									switch (v.getId()) {
									case R.id.buttonOk:
										hintDialog.dismiss();
										finish();
										break;
									}
								}
							});
					hintDialog.setCancelable(false);
					hintDialog
							.setOnKeyListener(new DialogInterface.OnKeyListener() {
								@Override
								public boolean onKey(DialogInterface dialog,
										int keyCode, KeyEvent event) {
									if (keyCode == KeyEvent.KEYCODE_SEARCH) {
										return true;
									} else {
										return true; // 默认返回 false
									}
								}
							});
					hintDialog.setCanceledOnTouchOutside(false);
					hintDialog.show();
				} else if (result.get(Entity.RSPCOD).equals("000001")) {
					treasureBean.setIsActpwout("1");
					((AppContext) getApplication())
							.setTreasureBean(treasureBean);
					finish();
//					ToastCustom.showMessage(IntoBasisActivity.this,
//							"您当日密码输错次数已超限,请明日再试。");
				} else {
					warnDialog = new OneButtonDialogWarn(
							IntoBasisActivity.this, R.style.CustomDialog,
							"提示", result.get(Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									warnDialog.dismiss();
								}
							});
					warnDialog.show();
				}
			} else {
//				ToastCustom.showMessage(IntoBasisActivity.this, "fail");
			}
			super.onPostExecute(result);
		}
	}
}
