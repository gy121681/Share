package com.td.qianhai.epay.oem;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.CityEntity;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.ProvinceEntity;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.utils.AppManager;
import com.td.qianhai.epay.utils.CardUtil;
import com.td.qianhai.epay.utils.Checkingroutine;
import com.td.qianhai.epay.utils.ChineseUtil;
import com.td.qianhai.epay.utils.DateUtil;

/**
 * 开户信息修改(未使用)
 * 
 * @author liangge
 * 
 */
public class ReviseBankInfoActivity extends BaseActivity implements
		OnClickListener {
	/** 商户名、商户身份证号码、银行卡号 */
	private EditText dealerName, dealerCardNo, bankCardNo;
	/** 银行名称、银行开户省、银行开户城市、银行开户支行 、返回 */
	private TextView tvBankName, tvBankProvince, tvBankCity, tvBankBranch,et_update_bank,
			bt_back, tv_contre;
	/** 修改按钮 */
	private LinearLayout btnRevise;
	// /** 刷卡器 */
	// private XssSwiper swiper;
	boolean isOpen;
	/** 二磁道、三磁道 */
	private String track2, track3;
	/** 商户Id、终端号、 */
	private String custId, psamId, issno, bankProvinceid, cardNo, bankname,
			bankProvincename, bankCityid, bankCityname, bankBranchName,
			bankBranchid, tvName, tvCardNo, cardData, dcflag, cardType;
	private ArrayList<HashMap<String, Object>> list;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 6:
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				isOpen = imm.isActive();
				if (isOpen) {
					imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
				bankCardNo.clearFocus();
				list = null;
				cardData = (String) msg.obj;
				// etCardNo.setText(getCardNo(cardData.split("d")[0]));
				bankCardNo.setText(cardData.split("d")[0]);

				bankCardNo.setEnabled(true);
				// char[] charBin = cardData.toCharArray();
				// String bankName = BankInfo.getNameOfBank(charBin, 0);

				cardData = cardData
						+ "d49121202369991430fffffffffff996222024000079840084d1561560000000000001"
						+ "003236999010000049120d000000000000d000000000000d00000000fffffffffffffff";

				track2 = cardData.substring(0, 48).replace("f", "")
						.replace("d", "D");
				track3 = cardData.substring(48, cardData.length())
						.replace("f", "").replace("d", "D");
				System.out.println("cardData:" + cardData);
				System.out.println("track2:" + track2);
				System.out.println("track3:" + track3);
				if ((String) msg.obj != null) {
					BankProvinceTask task2 = new BankProvinceTask();
					task2.execute(HttpUrls.QUERY_BANK_NAME + "", psamId,
							track2, track3);
				}
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.revise_bank_info_new);
		AppContext.getInstance().addActivity(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		tvBankName = (TextView) findViewById(R.id.tv_revise_bank_info_bank_name);
		et_update_bank = (TextView) findViewById(R.id.et_update_bank);
		tvBankProvince = (TextView) findViewById(R.id.tv_revise_info_bank_province);
		bankCardNo = (EditText) findViewById(R.id.et_deale_card_no);
		tvBankProvince.setOnClickListener(this);
		tvBankCity = (TextView) findViewById(R.id.tv_revise_info_bank_city);
		bt_back = (TextView) findViewById(R.id.bt_title_left);
		bt_back.setOnClickListener(this);
		tv_contre = (TextView) findViewById(R.id.tv_title_contre);
		tv_contre.setText("开户信息修改");
		tvBankCity.setOnClickListener(this);
		tvBankBranch = (TextView) findViewById(R.id.tv_revise_info_bank_branch);
		tvBankBranch.setOnClickListener(this);
//		custId = ((AppContext) this.getApplication()).getCustId();
		custId = MyCacheUtil.getshared(this).getString("Mobile", "");
		psamId = ((AppContext) this.getApplication()).getPsamId();

		initView();

		BussinessInfoTask task = new BussinessInfoTask();
		task.execute(HttpUrls.BUSSINESS_INFO + "", custId, psamId, "4");
		GetBankProvinceTask task2 = new GetBankProvinceTask();
		task2.execute(HttpUrls.QUERY_BANK_INFO + "", custId);

	}

	private void initView() {

		dealerName = (EditText) findViewById(R.id.et_deale_name);
		dealerCardNo = (EditText) findViewById(R.id.et_deale_cert_no);

		btnRevise = (LinearLayout) findViewById(R.id.btn_revise_bank_info_revise);
		btnRevise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DateUtil.isFastDoubleClick()) {
					return;
				} else {
					reviseBankInfo();
				}
			}
		});

		/**
		 * 点击银行卡把其他的信息先清除掉
		 */
		bankCardNo.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub

				if (hasFocus == false) {
					String cardNo = bankCardNo.getText().toString();
					if (cardNo != null && !cardNo.trim().equals("")) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						isOpen = imm.isActive();
						if (isOpen) {
							imm.toggleSoftInput(
									InputMethodManager.SHOW_IMPLICIT,
									InputMethodManager.HIDE_NOT_ALWAYS);
						}
						Message message = Message.obtain(mHandler);
						message.what = 6;
						message.obj = bankCardNo.getText().toString();
						message.sendToTarget();
					}
				} else {
					bankBranchName = "";
					tvBankProvince.setText("选择省份");
					tvBankCity.setText("选择城市");
					tvBankBranch.setText("选择支行");
					list = null;
					bankProvinceid = null;
					bankCityid = null;
					bankBranchid = null;
					tvBankName.setText("");
				}
			}
		});

		tvBankName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bankCardNo.clearFocus();
			}
		});
	}

	private void reviseBankInfo() {

		String name = dealerName.getText().toString();
		if (name == null || (name != null && name.equals(""))) {
			// dealerName.setError("请输入用户姓名");
			ToastCustom.showMessage(ReviseBankInfoActivity.this, "请输入用户姓名",
					Toast.LENGTH_SHORT);
			dealerName.requestFocus();
			return;
		} else if (!ChineseUtil.checkNameChese(name)) {
			// dealerName.setError("用户姓名必须全为中文");
			ToastCustom.showMessage(ReviseBankInfoActivity.this, "用户姓名必须全为中文",
					Toast.LENGTH_SHORT);
			dealerName.requestFocus();
			return;
		}

		String certNo = dealerCardNo.getText().toString();
		if (certNo == null || (certNo != null && certNo.equals(""))) {
			// dealerCardNo.setError("请输入身份证号");
			ToastCustom.showMessage(ReviseBankInfoActivity.this, "请输入身份证号",
					Toast.LENGTH_SHORT);
			dealerCardNo.requestFocus();
			return;
		}
		String identifyID = null;
		try {
			identifyID = Checkingroutine.IDCard.IDCardValidate(certNo);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		if (!"".equals(identifyID)) {
			ToastCustom.showMessage(ReviseBankInfoActivity.this, identifyID,
					Toast.LENGTH_SHORT);
			// dealerCardNo.setError(identifyID);
			dealerCardNo.requestFocus();
			return;
		}

		

		String bankCard = bankCardNo.getText().toString();

		if (bankCard == null || (bankCard != null && bankCard.equals(""))) {
			ToastCustom.showMessage(this, "请输入银行卡号", Toast.LENGTH_SHORT);
			return;
		} else if (!CardUtil.checkBankCard(bankCard)) {
			ToastCustom.showMessage(this, "银行卡号不正确", Toast.LENGTH_SHORT);
			return;
		}

		String bankCode = tvBankName.getText().toString();
		bankCode = bankname;
		if (bankCode == null || (bankCode != null && bankCode.equals(""))) {
			ToastCustom.showMessage(this, "银行信息不正确", Toast.LENGTH_SHORT);
			return;
		}

		// if (bankBranchid == null
		// || (bankBranchid != null && bankBranchid.equals(""))) {
		// ToastCustom.showMessage(ReviseBankInfoActivity.this, "银行信息未选择完整",
		// Toast.LENGTH_SHORT);
		// return;
		// }

		String tvBankProvincename = tvBankProvince.getText().toString();
		String tvBankCityname = tvBankCity.getText().toString();
		String tvBankBranchName = tvBankBranch.getText().toString();

		if (bankBranchid == null
				|| (bankBranchid != null && bankBranchid.equals(""))) {
			ToastCustom.showMessage(this, "请选择开户银行支行", Toast.LENGTH_SHORT);
			return;
		}

		if (tvName.equals(name) && certNo.equals(tvCardNo)
				&& tvBankProvincename.equals(bankProvincename)
				&& tvBankCityname.equals(bankCityname)
				&& tvBankBranchName.equals(bankBranchName)) {
			ToastCustom.showMessage(ReviseBankInfoActivity.this,
					"修改信息一致，请核对后再修改", Toast.LENGTH_SHORT);
			return;
		}

		ReviseBankInfoTask task = new ReviseBankInfoTask();
		Log.v("test", custId + "\n" + psamId + "\n" + bankCode + "\n"
				+ bankProvinceid + "\n" + bankCityid + "\n" + bankBranchid
				+ "\n" + name + "\n" + certNo);

		task.execute(HttpUrls.REVISE_BANK_INFO + "", custId, psamId, "4",
				bankCode, bankProvinceid, bankCityid, bankBranchid, name,
				certNo, bankCard);

	}

	class ReviseBankInfoTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			showLoadingDialog("正在修改中...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3],
					params[4], params[5], params[6], params[7], params[8],
					params[9], params[10] };
			return NetCommunicate.get(HttpUrls.REVISE_BANK_INFO, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					ToastCustom.showMessage(ReviseBankInfoActivity.this, result
							.get(Entity.RSPMSG).toString(), Toast.LENGTH_SHORT);
					Intent it = new Intent(ReviseBankInfoActivity.this,
							AccountManageActivity.class);
					startActivity(it);
					AccountManageActivity.activity.finish();
					finish();
				} else {
					ToastCustom.showMessage(ReviseBankInfoActivity.this,
							"fail", Toast.LENGTH_SHORT);
				}
			}
			super.onPostExecute(result);
		}

	}

	class GetBankProvinceTask extends
			AsyncTask<String, Integer, ProvinceEntity> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ProvinceEntity doInBackground(String... params) {
			String[] values = { params[0], params[1] };
			return NetCommunicate.getQueryProvince(HttpUrls.QUERY_BANK_INFO,
					values);
		}

		@Override
		protected void onPostExecute(ProvinceEntity result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (Entity.STATE_OK.equals(result.getRspcod())) {
					// 获取id标识
					issno = result.getIssno();
					bankProvinceid = result.getProvid();
					bankCityid = result.getCityid();
					bankBranchid = result.getBkno();
					// 获取中文名
					bankname = result.getIssnam();
					tvBankName.setText(result.getIssnam());
					// 获取省中文名
					tvBankProvince.setText(result.getPronam());
					bankProvincename = result.getPronam();
					// 获取城市中文名
					tvBankCity.setText(result.getCitynam());
					bankCityname = result.getCitynam();
					// 获取支行中文名
					tvBankBranch.setText(result.getBenelx());
					bankBranchName = result.getBenelx();
					list = result.list;
				}
			}
			super.onPostExecute(result);
		}

	}

	class BankCityTask extends AsyncTask<String, Integer, CityEntity> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog("正在注册中...");
			super.onPreExecute();
		}

		@Override
		protected CityEntity doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate
					.getQueryCity(HttpUrls.QUERY_BANK_CITY, values);
		}

		@Override
		protected void onPostExecute(CityEntity result) {
			loadingDialogWhole.dismiss();
			if (result != null) {

				if (Entity.STATE_OK.equals(result.getRspcod())) {
					if (result.list.size() == 0) {
						ToastCustom.showMessage(ReviseBankInfoActivity.this,
								"没有该城市支行信息", Toast.LENGTH_SHORT);
						return;
					}
					// ProvinceDialog dialog = new ProvinceDialog(2,
					// ReviseBankInfoActivity.this, result.list,
					// new OnProvinceDialogListener() {
					//
					// @Override
					// public void back(HashMap<String, Object> map) {
					// bankCityid = map.get("CITYID").toString();
					// bankCityname = map.get("CITYNAM")
					// .toString();
					// tvBankCity.setText(map.get("CITYNAM")
					// .toString());
					//
					// }
					// });
					// dialog.setTitle("银行卡开户城市");
					// dialog.show();

					Intent intent = new Intent(ReviseBankInfoActivity.this,
							SelectListNameActivity.class);
					Bundle bundle = new Bundle();
					@SuppressWarnings("rawtypes")
					ArrayList carrier = new ArrayList();
					carrier.add(result.list);
					bundle.putString("titleContent", "银行卡开户城市");
					bundle.putParcelableArrayList("carrier", carrier);
					intent.putExtras(bundle);
					startActivityForResult(intent, 8);
					overridePendingTransition(0, 0);
				}else{
					Toast.makeText(getApplicationContext(),result.getRspmsg(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(ReviseBankInfoActivity.this,
//							result.getRspmsg());
				}
			}
			super.onPostExecute(result);
		}

	}

	class BankBranchTask extends AsyncTask<String, Integer, CityEntity> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog("正在查询中...");
			super.onPreExecute();
		}

		@Override
		protected CityEntity doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3],
					params[4], params[5] };
			return NetCommunicate.getQueryCity(HttpUrls.QUERY_BANK_BRANCH,
					values);
		}

		@Override
		protected void onPostExecute(CityEntity result) {
			loadingDialogWhole.dismiss();
			if (result != null) {

				if (Entity.STATE_OK.equals(result.getRspcod())) {
					if (result.list.size() == 0) {
						ToastCustom.showMessage(ReviseBankInfoActivity.this,
								"没有该城市支行信息", Toast.LENGTH_SHORT);
						return;
					}
					Intent intent = new Intent(ReviseBankInfoActivity.this,
							SelectListNameActivity.class);
					Bundle bundle = new Bundle();
					@SuppressWarnings("rawtypes")
					ArrayList carrier = new ArrayList();
					carrier.add(result.list);
					bundle.putString("titleContent", "银行卡开户支行");
					bundle.putParcelableArrayList("carrier", carrier);
					intent.putExtras(bundle);
					startActivityForResult(intent, 7);
					overridePendingTransition(0, 0);

				} else {
					Toast.makeText(getApplicationContext(),result.getRspmsg(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(ReviseBankInfoActivity.this,
//							result.getRspmsg());
				}
			}
			super.onPostExecute(result);
		}
	}

	/**
	 * 查看商户资料Task
	 * 
	 * @author Administrator
	 * 
	 */
	class BussinessInfoTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog("正在加载数据...");
			super.onPreExecute();
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate.get(HttpUrls.BUSSINESS_INFO, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					dealerCardNo.setText(result.get("CARDID").toString());
					dealerName.setText(result.get("ACTNAM").toString());
					tvName = result.get("ACTNAM").toString();
					tvCardNo = result.get("CARDID").toString();
					cardNo = result.get("ACTNO").toString();
					bankCardNo.setText(cardNo);
				} else {

					showSingleWarnDialog(result.get(Entity.RSPMSG).toString());
				}
			}
			super.onPostExecute(result);
		}

	}

	// class BrashRunable implements Runnable {
	//
	// @Override
	// public void run() {
	// while (flag) {
	// swiper = new XssSwiper();
	// // String psamId = swiper.getPsamId();
	// String card = swiper.trackData(50000);
	//
	// Message msg = new Message();
	// if (card != null) {
	// // String[] cards=card.split("d");
	// // card =cards[0];
	// msg.what = 2;
	// msg.obj = card;
	// }/*
	// * else{ msg.what = 3; msg.obj = card; }
	// */
	// mHandler.sendMessage(msg);
	// }
	// }
	//
	// }

	// @SuppressLint("HandlerLeak")
	// private Handler mHandler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// switch (msg.what) {
	// case 2:
	// // 刷卡获取银行卡
	// cardData = (String) msg.obj;
	// // etCardNo.setText(getCardNo(cardData.split("d")[0]));
	// bankCardNo.setText(cardData.split("d")[0]);
	// bankCardNo.setEnabled(false);
	// tvBankName.setEnabled(false);
	// track2 = cardData.substring(0, 48).replace("f", "")
	// .replace("d", "D");
	// track3 = cardData.substring(48, cardData.length())
	// .replace("f", "").replace("d", "D");
	// BankProvinceTask task = new BankProvinceTask();
	// task.execute(HttpUrls.QUERY_BANK_NAME + "", psamId, track2,
	// track3);
	// break;
	// case 3:
	// // 刷卡失败
	// ToastCustom.showMessage(getApplicationContext(), "刷卡未成功，请重新刷卡",
	// Toast.LENGTH_SHORT);
	// break;
	// case 13:
	// // // pause状态重新启动刷卡器
	// // Thread thread = new Thread(new BrashRunable());
	// // thread.start();
	// break;
	// default:
	// break;
	// }
	// };
	// };

	/**
	 * 银行卡开户行查询
	 * 
	 * @author Administrator
	 * 
	 */
	class BankProvinceTask extends AsyncTask<String, Integer, ProvinceEntity> {
		
		@Override
		protected void onPreExecute() {
			showLoadingCloseDialog("正在查询中。。。");
			super.onPreExecute();
		}

		@Override
		protected ProvinceEntity doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate.getQueryProvince(HttpUrls.QUERY_BANK_NAME,
					values);
		}

		@Override
		protected void onPostExecute(ProvinceEntity result) {
			loadingDialogClose.dismiss();
			if (result != null) {
				if (Entity.STATE_OK.equals(result.getRspcod())) {
					issno = result.getIssno();
					dcflag = result.getDcflag().toString();
					System.out.println("dcflag" + dcflag);
					if (dcflag != null && dcflag != "") {
						if (dcflag.equals("01")) {
							cardType = "(借记卡)";
						} else if (dcflag.equals("02")) {
							cardType = "(信用卡)";
							bankCardNo.setText("");
							tvBankName.setText("");
							bankCardNo.setEnabled(false);
							list = null;
							bankProvinceid = null;
							bankCityid = null;
							bankBranchName = null;
							showSingleWarnDialog("暂不支持信用卡注册！");
						} else {
							cardType = "";
						}
					}
					bankname = result.getIssnam();
					tvBankName.setText(result.getIssnam() + cardType);
					bankBranchName = result.getIssnam().toString();
					tvBankProvince.setText("选择省份");
					tvBankCity.setText("选择城市");
					tvBankBranch.setText("选择支行");
					Log.v("result", "成功获取银行信息");
					Log.v("result", "bankBranchName:" + bankBranchName);
					if (bankBranchName == null || bankBranchName == "") {
						list = null;
						bankProvinceid = null;
						bankCityid = null;
						Log.v("result", "list==null");
					}
					// else if (result.getIssno().equals("01000000")) {
					// bankCardNo.setText("");
					// tvBankName.setText("");
					// bankCardNo.setEnabled(false);
					// list = null;
					// bankProvinceid = null;
					// bankCityid = null;
					// bankBranchName = null;
					// isIsson(result.getIssno());
					// }
					else {
						list = result.list;
						tvBankName.setText(result.getIssnam() + cardType);
					}
				} else {
					ToastCustom.showMessage(ReviseBankInfoActivity.this, result
							.getRspmsg().toString(), Toast.LENGTH_SHORT);
				}
			}
			super.onPostExecute(result);
		}
	}

	// private void isIsson(String isson) {
	// // 判断邮政不可用
	// AlertDialog.Builder builder = new Builder(ReviseBankInfoActivity.this);
	// builder.setMessage("暂不支持邮政卡！");
	// builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
	//
	// public void onClick(DialogInterface dialog, int which) {
	// dialog.dismiss();
	// }
	// });
	// builder.create().show();
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_revise_info_bank_province:
			// 省份dialog选择
			if (DateUtil.isFastDoubleClick()) {
				return;
			} else {
				if (list != null) {
					tvBankCity.setText("选择城市");
					tvBankBranch.setText("选择支行");
					bankBranchid = null;
					bankCityid = null;
//					ProvinceDialog dialog = new ProvinceDialog(1,
//							ReviseBankInfoActivity.this, list,
//							new OnProvinceDialogListener() {
//
//								@Override
//								public void back(HashMap<String, Object> map) {
//									bankProvinceid = map.get("PROVID")
//											.toString();
//									tvBankProvince.setText(map.get("PROVNAM")
//											.toString());
//
//								}
//							});
//					dialog.setTitle("银行卡开户省份");
//					dialog.show();
					
					// 查询银行卡名称
					// getBankInfo(list);
					Intent intent = new Intent(ReviseBankInfoActivity.this,
							SelectListNameActivity.class);
					Bundle bundle = new Bundle();
					ArrayList carrier = new ArrayList();
					carrier.add(list);
					bundle.putString("titleContent", "银行卡开户省份");
					bundle.putParcelableArrayList("carrier", carrier);
					intent.putExtras(bundle);
					startActivityForResult(intent, 9);
					overridePendingTransition(0, 0);
				} else {
					ToastCustom.showMessage(ReviseBankInfoActivity.this,
							"请刷入银行卡号", Toast.LENGTH_SHORT);
				}
			}
			break;

		case R.id.bt_title_left:
			// 返回上一个Activity
			AppManager.getAppManager().finishActivity();
			break;
		case R.id.tv_revise_info_bank_city:
			// 选择城市dialog
			if (DateUtil.isFastDoubleClick()) {
				return;
			} else {
				if (bankProvinceid == null || "".equals(bankProvinceid)) {
					ToastCustom.showMessage(ReviseBankInfoActivity.this,
							"请先选择银行省份", Toast.LENGTH_SHORT);
					return;
				}
				tvBankBranch.setText("选择支行");
				bankBranchid = null;
				BankCityTask task = new BankCityTask();
				task.execute(HttpUrls.QUERY_BANK_CITY + "", "1", "200",
						bankProvinceid);

			}
			break;
		case R.id.tv_revise_info_bank_branch:
			// 支行信息选择dialog
			if (DateUtil.isFastDoubleClick()) {
				return;
			} else {
				if (bankCityid == null
						|| (bankCityid != null && bankCityid.equals(""))) {
					ToastCustom.showMessage(ReviseBankInfoActivity.this,
							"请先选择银行城市", Toast.LENGTH_SHORT);
					return;
				}
				BankBranchTask task = new BankBranchTask();
				task.execute(HttpUrls.QUERY_BANK_BRANCH + "", "1", "200",
						issno, bankProvinceid, bankCityid);
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			Bundle bundle = data.getExtras();
			if (null != bundle) {
				if (resultCode == 9) {
					bankProvinceid = bundle.getString("companyId");
					tvBankProvince.setText(bundle.getString("companyName"));
				} else if (resultCode == 8) {
					bankCityid = bundle.getString("companyId");
					tvBankCity.setText(bundle.getString("companyName"));
				} else if (resultCode == 7) {
					bankBranchid = bundle.getString("companyId");
					tvBankBranch.setText(bundle.getString("companyName"));
				}
			}
		}
	}
}
