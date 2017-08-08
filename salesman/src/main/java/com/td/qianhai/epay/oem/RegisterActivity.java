package com.td.qianhai.epay.oem;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.share.app.LicenseDialog;
import com.share.app.network.CallbackString;
import com.share.app.network.Request;
import com.share.app.utils.ProgressDialogUtil;
import com.share.app.utils.PwdUtils;
import com.share.app.utils.WaringDialogUtils;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.CityEntity;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.ProvinceEntity;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.CountDownButton;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.epay.utils.AppManager;
import com.td.qianhai.epay.utils.Checkingroutine;
import com.td.qianhai.epay.utils.ChineseUtil;
import com.td.qianhai.epay.utils.Common;
import com.td.qianhai.epay.utils.DateUtil;

/**
 * 注册
 * 
 * @author liangge
 * 
 */
public class RegisterActivity extends BaseActivity implements OnClickListener {
	/** 姓名、身份证号码、手机号码、密码、再次输入密码、卡号、验证码 */
	private EditText etName, zetCertNo, etMobile, etLoginPasswd, etCertNo,
			tv_register_email,etCardNo, etVerifCode,et_ratenums,
			etTerminalNo;
//	etLoginPasswdAgain
	/** 协议查看、验证码获取、提交注册 */
	private Button  btnSubmit;
    private CountDownButton btnGetVerifCode;
	/** 同意协议 */
	private CheckBox ckAgreeAgreement;
	/** 手机号码、银行卡信息、登录密码 */
	private String mobile, cardData, loginPasswd, email;
	/** 终端类型 */
	private final String terminalType = "2";

	/** 终端类型 */
	private final String trmtyp = "2";
	/** 刷卡器 */
	boolean isOpen;
	/** 二磁、三磁 */
	private String track2, track3;
	/** 开户行名称ID、开户行省份ID、开户行城市ID、开户行支行ID、开户行支行名称、卡类型STR、卡类型 */
	private String issno, bankProvinceid, bankCityid, bankBranchid,
			bankBranchName, dcflag, cardType;
	/** 银行名称、银行开户省份、银行开户城市、银行开户支行 */
	private TextView tvBankName, tvBankProvince, tvBankCity, tvBankBranch,btnQueryAgreement,contacts;
	// tvTerminalError;
	/** 后台list数据集合 */
	private ArrayList<HashMap<String, Object>> list;
	/** 页面滚动条 */
//	private ScrollView scrollView;
	/** 返回、更多按钮、title中间内容 */
	private TextView bt_Back, tv_title_contre;
	/** 要操作的Handler */
	private int what;
	/** 完整的卡磁 */
	private String cardDatas;

	private String termina = "";
	/** 商户id */
	private String custId;

	private ProgressBar load;

	private ImageView load_img;

	private TextView tv_nameError, tv_cardError, tv_mobileError, tv_passError,
			tv_emailError, tv_passErrorAgain,tv_rate_help;

	private RelativeLayout relalay;
	
	private   int REQUEST_CONTACT = 1;
	
	private CheckBox e_pwd;

	private boolean setEM = false;
	
	private Editor editor;

    private OneButtonDialogWarn mWarnDialog;

    @SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 10:
				ToastCustom.showMessage(getApplicationContext(), "获取验证码失败",
						Toast.LENGTH_SHORT);
				btnGetVerifCode.setEnabled(true);
				break;
			case 11:
				@SuppressWarnings("unchecked")
				// HashMap<String, Object> map = (HashMap<String, Object>)
				// msg.obj;
				// ToastCustom.showMessage(getApplicationContext(),
				// map.get("RSPMSG").toString(), Toast.LENGTH_SHORT);
				HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
				Toast.makeText(getApplicationContext(),map.get("RSPMSG").toString(),
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(getApplicationContext(),
//						map.get("RSPMSG").toString());
				if (map.get("RSPCOD").toString().equals("000000")) {
					Common.timing(btnGetVerifCode);
				} else if (map.get("RSPCOD").toString().equals("400002")) {
					btnGetVerifCode.setEnabled(true);
				} else if (map.get("RSPCOD").toString().equals("099999")) {
					btnGetVerifCode.setEnabled(true);
				}else{
					btnGetVerifCode.setEnabled(true);
				}


				break;
			case 6:
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				isOpen = imm.isActive();
				if (isOpen) {
					imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
				etCardNo.clearFocus();
				list = null;
				cardData = (String) msg.obj;
				etCardNo.setText(cardData.split("d")[0]);
				etCardNo.setEnabled(true);
				cardData = cardData
						+ "d49121202369991430fffffffffff996222024000079840084d1561560000000000001003236999010000049120d000000000000d000000000000d00000000fffffffffffffff";

				track2 = cardData.substring(0, 48).replace("f", "")
						.replace("d", "D");
				track3 = cardData.substring(48, cardData.length())
						.replace("f", "").replace("d", "D");
				System.out.println("cardData:" + cardData);
				System.out.println("track2:" + track2);
				System.out.println("track3:" + track3);
				if ((String) msg.obj != null) {
					BankProvinceTask task2 = new BankProvinceTask();
					task2.execute(HttpUrls.QUERY_BANK_NAME + "", termina,
							track2, track3);
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		editor = MyCacheUtil.setshared(RegisterActivity.this);
//		custId = ((AppContext) this.getApplication()).getCustId();
		custId = MyCacheUtil.getshared(this).getString("Mobile", "");
		AppContext.getInstance().addActivity(this);
		
		initView();
	}

	@Override
	protected void doubleWarnOnClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_left:
			doubleWarnDialog.dismiss();
			break;
		case R.id.btn_right:
			isSelfCard();
			
			break;

		default:
			break;
		}
	}

	/**
	 * 提示是否自己的卡号
	 */
	private void isSelfCard() {
		final Message msg = new Message();
		msg.what = what;
		msg.obj = cardDatas;
		mHandler.sendMessage(msg);
		doubleWarnDialog.dismiss();
	}

	/*
	 * 初始化控件
	 */
	private void initView() {
		// psamId = ((AppContext) RegisterActivity.this.getApplication())
		// .getPsamId();
		relalay = (RelativeLayout) findViewById(R.id.re_lay);
		etName = (EditText) findViewById(R.id.et_register_name);
		load = (ProgressBar) findViewById(R.id.load_phonr_pro);
		load_img = (ImageView) findViewById(R.id.load_phonr_img);
		tv_register_email = (EditText) findViewById(R.id.tv_register_email);
		etCertNo = (EditText) findViewById(R.id.et_register_cert_no);
		etMobile = (EditText) findViewById(R.id.et_register_mobile);
		etLoginPasswd = (EditText) findViewById(R.id.et_register_login_passwd);
//		etLoginPasswdAgain = (EditText) findViewById(R.id.et_register_login_passwd_again);
		etCardNo = (EditText) findViewById(R.id.et_register_card_no);
		tvBankName = (TextView) findViewById(R.id.tv_register_bank_name);
		tvBankProvince = (TextView) findViewById(R.id.tv_register_bank_province);
		e_pwd = (CheckBox) findViewById(R.id.e_pwd);
		contacts = (TextView) findViewById(R.id.contacts);
		e_pwd.setOnClickListener(this);
//		scrollView = (ScrollView) findViewById(R.id.btn_register_scrollview);
		etVerifCode = (EditText) findViewById(R.id.et_register_verif_code);
		ckAgreeAgreement = (CheckBox) findViewById(R.id.register_agree_agreement);
		btnQueryAgreement = (TextView) findViewById(R.id.btn_register_query_agreement);
		btnQueryAgreement.setText(Html.fromHtml("<u>查看协议</u>"));
		btnQueryAgreement.setOnClickListener(this);
		btnGetVerifCode = (CountDownButton) findViewById(R.id.btn_register_get_verif_code);
		btnGetVerifCode.setOnClickListener(this);
		btnSubmit = (Button) findViewById(R.id.btn_register_submit);
		btnSubmit.setOnClickListener(this);
		tv_title_contre = (TextView) findViewById(R.id.tv_title_contre);
		tv_title_contre.setText("注　册");
		bt_Back = (TextView) findViewById(R.id.bt_title_left);
        bt_Back.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//		tv_register_email1 = (EditText) findViewById(R.id.tv_register_email1);
		// etTerminalNo = (EditText) findViewById(R.id.et_terminal_no);

		/* 错误提示初始化 */
		tv_nameError = (TextView) findViewById(R.id.et_register_name_error);
		tv_cardError = (TextView) findViewById(R.id.et_register_cert_no_error);
		tv_mobileError = (TextView) findViewById(R.id.et_register_mobile_error);
		tv_passError = (TextView) findViewById(R.id.et_register_login_passwd_error);
		et_ratenums = (EditText) findViewById(R.id.et_ratenums);
		tv_rate_help = (TextView) findViewById(R.id.tv_rate_help);
		tv_rate_help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(RegisterActivity.this,IntroductionActivity.class);
    			it.putExtra("title", "关于费率介绍");
    			it.putExtra("description", getResources().getString(R.string.rateintroduction));
    			startActivity(it);
				
			}
		});
//		tv_passErrorAgain = (TextView) findViewById(R.id.et_register_login_passwd_again_error);
//		tv_emailError = (TextView) findViewById(R.id.et_email_error);
		// tvTerminalError = (TextView) findViewById(R.id.et_terminal_n_error);

		// etTerminalNo.setOnFocusChangeListener(new OnFocusChangeListener() {
		//
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// // TODO Auto-generated method stub
		// if (!hasFocus) {
		// String termina = etTerminalNo.getText().toString();
		// if (termina == null || termina.equals("")) {
		// etTerminalNo.setVisibility(View.GONE);
		// tvTerminalError.setVisibility(View.VISIBLE);
		// tvTerminalError.setError("请输入终端号");
		// } else {
		// ValidateTerminTask task = new ValidateTerminTask();
		// task.execute(HttpUrls.LOCALYZZD + "", termina, "", "2");
		// }
		// }
		// }
		// });
		
		
		contacts.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();

				intent.setAction(Intent.ACTION_PICK);

				intent.setData(ContactsContract.Contacts.CONTENT_URI);

				startActivityForResult(intent, REQUEST_CONTACT);
				
			}
		});
		
		etMobile.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()>=11){
					etLoginPasswd.requestFocus();
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

		bt_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AppManager.getAppManager().finishActivity();
			}
		});

		tv_nameError.setOnClickListener(this);

		/**
		 * 姓名判断
		 */
		etName.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub

				if (!hasFocus) {
					String name = etName.getText().toString();
					if (name == null || (name != null && name.equals(""))) {
						tv_nameError.setVisibility(View.VISIBLE);
						tv_nameError.setError("请输入用户姓名");
						etName.setVisibility(View.GONE);
					} else if (!ChineseUtil.checkNameChese(name)) {
						tv_nameError.setVisibility(View.VISIBLE);
						tv_nameError.setError("用户姓名必须全为中文");
						etName.setVisibility(View.GONE);
					}
				} else {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					isOpen = imm.isActive();
					if (isOpen) {
						Log.v("resule", "进入到了姓名判断的焦点");
						imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
					}
				}
			}
		});

		tv_cardError.setOnClickListener(this);

		/**
		 * 身份证判断
		 */
		etCertNo.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					String certNo = etCertNo.getText().toString();
					if (certNo == null || (certNo != null && certNo.equals(""))) {
						tv_cardError.setVisibility(View.VISIBLE);
						tv_cardError.setError("请输入身份证号");
						etCertNo.setVisibility(View.GONE);
					}
					String identifyID = null;
					try {
						identifyID = Checkingroutine.IDCard
								.IDCardValidate(certNo);
					} catch (ParseException e) {

						e.printStackTrace();
					}
					if (!"".equals(identifyID)) {
						tv_cardError.setVisibility(View.VISIBLE);
						tv_cardError.setError(identifyID);
						etCertNo.setVisibility(View.GONE);
					}
				} else {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					isOpen = imm.isActive();
					if (isOpen) {
						imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
					}
				}
			}
		});

//		tv_emailError.setOnClickListener(this);
		/**
		 * 邮箱判断焦点
		 */

//		tv_register_email1.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus) {
//					scrollView.scrollTo(200, 200);
//					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//					isOpen = imm.isActive();
//					if (isOpen) {
//						imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
//					}
//
//				} else {
//					email = tv_register_email1.getText().toString();
//
//					if (email == null || (email != null && email.equals(""))) {
//						tv_emailError.setVisibility(View.VISIBLE);
//						tv_emailError.setText("请输入邮箱");
//						tv_register_email1.setVisibility(View.GONE);
//					} else if (!isEmail(email)) {
//						tv_emailError.setVisibility(View.VISIBLE);
//						tv_emailError.setText("邮箱格式不正确");
//						tv_register_email1.setVisibility(View.GONE);
//					}
//				}
//
//			}
//		});

		tv_mobileError.setOnClickListener(this);

		/**
		 * 焦点在电话输入框的时候上跳200
		 */
		etMobile.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
//					scrollView.scrollTo(200, 200);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					isOpen = imm.isActive();
					if (isOpen) {
						Log.v("resule", "进入到了姓名判断的焦点");
						imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
					}
				} else {
					mobile = etMobile.getText().toString();
					if (mobile == null || (mobile != null && mobile.equals(""))) {
						tv_mobileError.setVisibility(View.VISIBLE);
						tv_mobileError.setText("请输入手机号码");
						etMobile.setVisibility(View.GONE);
						relalay.setVisibility(View.GONE);
						// load_img.setVisibility(View.GONE);
						// load.setVisibility(View.GONE);
					} else if (!Checkingroutine.isNumeric(mobile)) {
						tv_mobileError.setVisibility(View.VISIBLE);
						tv_mobileError.setText("只能输入数字");
						relalay.setVisibility(View.GONE);
						etMobile.setVisibility(View.GONE);
					} else if (mobile.length() != 11) {
						tv_mobileError.setVisibility(View.VISIBLE);
						tv_mobileError.setText("手机号码必须为11位数字");
						etMobile.setVisibility(View.GONE);
						relalay.setVisibility(View.GONE);
					} else if (!isMobileNO(mobile)) {
						tv_mobileError.setVisibility(View.VISIBLE);
						tv_mobileError.setText("手机号码格式有误");
						etMobile.setVisibility(View.GONE);
						relalay.setVisibility(View.GONE);
					} else {
//						oldCheckMobile();
						checkMobile();
					}
				}
			}
		});

		tv_passError.setOnClickListener(this);

		etLoginPasswd.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					loginPasswd = etLoginPasswd.getText().toString();
					if (loginPasswd == null
							|| (loginPasswd != null && loginPasswd.equals(""))) {
						tv_passError.setVisibility(View.VISIBLE);
						tv_passError.setText("请输入登录密码");
						etLoginPasswd.setVisibility(View.GONE);
					} else if (loginPasswd.length() < 6) {
						tv_passError.setVisibility(View.VISIBLE);
						tv_passError.setText("密码长度为6-15位");
						etLoginPasswd.setVisibility(View.GONE);
					}
				} else {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					isOpen = imm.isActive();
					if (isOpen) {
						Log.v("resule", "进入到了姓名判断的焦点");
						imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
					}
				}
			}
		});

//		tv_passErrorAgain.setOnClickListener(this);
		/**
		 * 焦点在密码输入框的时候上跳200
		 */
//		etLoginPasswdAgain
//				.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//					@Override
//					public void onFocusChange(View v, boolean hasFocus) {
//						// TODO Auto-generated method stub
//						if (hasFocus) {
//							scrollView.scrollTo(400, 400);
//							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//							isOpen = imm.isActive();
//							if (isOpen) {
//								Log.v("resule", "进入到了姓名判断的焦点");
//								imm.showSoftInput(v,
//										InputMethodManager.SHOW_FORCED);
//							}
//						} else {
//							String loginPasswdAgain = etLoginPasswdAgain
//									.getText().toString();
//							if (loginPasswdAgain == null
//									|| (loginPasswdAgain != null && loginPasswdAgain
//											.equals(""))) {
//								tv_passErrorAgain.setVisibility(View.VISIBLE);
//								tv_passErrorAgain.setText("请确认登录密码");
//								etLoginPasswdAgain.setVisibility(View.GONE);
//							} else if (!loginPasswd.equals(loginPasswdAgain)) {
//								tv_passErrorAgain.setVisibility(View.VISIBLE);
//								tv_passErrorAgain.setText("登录密码和确认密码不一致");
//								etLoginPasswdAgain.setVisibility(View.GONE);
//							}
//						}
//					}
//				});

		/**
		 * 点击银行卡把其他的信息先清除掉
		 */
		etCardNo.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub

				if (hasFocus == false) {
					String cardNo = etCardNo.getText().toString();
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
						message.obj = etCardNo.getText().toString();
						message.sendToTarget();
					}
				} else {
//					scrollView.scrollTo(600, 600);
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
				etCardNo.clearFocus();
			}
		});

		tvBankProvince.setOnClickListener(new OnClickListener() {

			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void onClick(View v) {
				if (DateUtil.isFastDoubleClick()) {
					return;
				} else {
					if (list == null) {
						ToastCustom.showMessage(RegisterActivity.this,
								"请先输入银行卡号", Toast.LENGTH_SHORT);
						return;
					}
					if (tvBankName.getText().equals("")) {
						BankProvinceTask task = new BankProvinceTask();
						task.execute(HttpUrls.QUERY_BANK_NAME + "", termina,
								track2, track3);
					}
					bankCityid = null;
					tvBankCity.setText("选择城市");
					tvBankBranch.setText("选择支行");
					// 查询银行卡名称
					// getBankInfo(list);
					Intent intent = new Intent(RegisterActivity.this,
							SelectListNameActivity.class);
					Bundle bundle = new Bundle();
					ArrayList carrier = new ArrayList();
					carrier.add(list);
					bundle.putString("titleContent", "银行卡开户省份");
					bundle.putParcelableArrayList("carrier", carrier);
					intent.putExtras(bundle);
					startActivityForResult(intent, 9);
					overridePendingTransition(0, 0);
				}
			}
		});
		tvBankCity = (TextView) findViewById(R.id.tv_register_bank_city);
		tvBankCity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DateUtil.isFastDoubleClick()) {
					return;
				} else {
					if (bankProvinceid == null
							|| (bankProvinceid != null && bankProvinceid
									.equals(""))) {
						ToastCustom.showMessage(RegisterActivity.this,
								"请先选择开户行省份", Toast.LENGTH_SHORT);
						return;
					}
					tvBankBranch.setText("选择支行");
					BankCityTask task = new BankCityTask();
					task.execute(HttpUrls.QUERY_BANK_CITY + "", "1", "200",
							bankProvinceid);
				}
			}
		});
		tvBankBranch = (TextView) findViewById(R.id.tv_register_bank_branch);
		tvBankBranch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DateUtil.isFastDoubleClick()) {
					return;
				} else {
					if (bankCityid == null
							|| (bankCityid != null && bankCityid.equals(""))) {
						ToastCustom.showMessage(RegisterActivity.this,
								"请先选择开户行城市", Toast.LENGTH_SHORT);
						return;
					}
					BankBranchTask task = new BankBranchTask();
					task.execute(HttpUrls.QUERY_BANK_BRANCH + "", "1", "200",
							issno, bankProvinceid, bankCityid);
				}
			}
		});
		
		
		
		etLoginPasswd.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					e_pwd.setVisibility(View.VISIBLE);
				}else{
					e_pwd.setVisibility(View.GONE);
				}
			}
		});
		
		e_pwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				
				CharSequence text = e_pwd.getText();
				 //Debug.asserts(text instanceof Spannable);

				// TODO Auto-generated method stub
				if(arg1){
					etLoginPasswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}else{
					etLoginPasswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
		OnKeyListener keyListener = new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					// register();
					View view = getWindow().peekDecorView();
					InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputmanger.hideSoftInputFromWindow(view.getWindowToken(),
							0);
					return true;
				}
				return false;
			}
		};
		// etVerifCode.setOnKeyListener(keyListener);
	}

	private void checkMobile() {
		String mobile = etMobile.getText().toString();
		Request.getSalesmanCheckMobile(mobile, new CallbackString() {
			@Override
			public void onFailure(String msg) {
				load.setVisibility(View.GONE);
				relalay.setVisibility(View.GONE);
				load_img.setVisibility(View.GONE);
				tv_mobileError.setVisibility(View.VISIBLE);
				tv_mobileError.setText("该手机号码已注册");
				etMobile.setVisibility(View.GONE);
			}

			@Override
			public void onSuccess(String data) {
				load.setVisibility(View.GONE);
				load_img.setVisibility(View.VISIBLE);
			}

			@Override
			public void onNetError(int code, String msg) {
				WaringDialogUtils.showWaringDialog(getActivity(), "网络异常", null);
			}
		});
	}

	/**
	 * 原检查手机号是否注册
	 */
	private void oldCheckMobile(){
								String custId = etMobile.getText().toString();
						final BussinessInfoTask task = new BussinessInfoTask();

						task.execute(HttpUrls.BUSSINESS_INFO + "", custId, "4",
								"1");
						new Thread() {
							public void run() {
								try {
									/**
									 * 在这里你可以设置超时的时间 切记：这段代码必须放到线程中执行，
									 * 因为不放单独的线程中执行的话该方法会冻结UI线程
									 * 直接导致onPreExecute()方法中的弹出框不会立即弹出。
									 */
									task.get(30000, TimeUnit.MILLISECONDS);
								} catch (TimeoutException e) {
									// 请求超时
									/**
									 * 如果在doInbackground中的代码执行的时间超出10000秒则会出现这个异常
									 * 。 所以这里就成为你处理异常操作的唯一途径。
									 *
									 * 备注：这里是不能够处理UI操作的，如果处理UI操作则会出现崩溃异常。
									 * 你可以写一个Handler
									 * ，向handler发送消息然后再Handler中接收消息并处理UI更新操作。
									 */
									Toast.makeText(getApplicationContext(),"请求服务器超时,请重新操作",
											Toast.LENGTH_SHORT).show();
//									ToastCustom.showMessage(
//											RegisterActivity.this,
//											"请求服务器超时,请重新操作!");
									task.cancel(true);
								} catch (Exception e) {
									Toast.makeText(getApplicationContext(),"系统错误,请重新操作",
											Toast.LENGTH_SHORT).show();
//									ToastCustom.showMessage(
//											RegisterActivity.this,
//											"系统错误,请重新操作!");
									task.cancel(true);
								}
							};
						}.start();
	}

	/**
	 * 注册
	 */
	private void register() {
		// if (tvBankBranch.getText().toString() != ""
		// && tvBankBranch.getText().toString() != null
		// && tvBankBranch.getText().toString().equals("选择支行")) {
		// bankBranchid = null;
		// }

		// termina = etTerminalNo.getText().toString();
		//
		// if (termina == null || termina.equals("")) {
		// ToastCustom.showMessage(this, "请输入终端号", Toast.LENGTH_SHORT);
		// return;
		// }

		// String name = etName.getText().toString();
		// if (name == null || (name != null && name.equals(""))) {
		// ToastCustom.showMessage(this, "请输入用户姓名", Toast.LENGTH_SHORT);
		// return;
		// } else if (!ChineseUtil.checkNameChese(name)) {
		// ToastCustom.showMessage(this, "用户姓名必须全为中文", Toast.LENGTH_SHORT);
		// return;
		// }

		// String certNo = etCertNo.getText().toString();
		// if (certNo == null || (certNo != null && certNo.equals(""))) {
		// ToastCustom.showMessage(this, "请输入身份证号", Toast.LENGTH_SHORT);
		// return;
		// }
		// String identifyID = null;
		// try {
		// identifyID = Checkingroutine.IDCard.IDCardValidate(certNo);
		// } catch (ParseException e) {
		//
		// e.printStackTrace();
		// }
		// if (!"".equals(identifyID)) {
		// ToastCustom.showMessage(this, identifyID, Toast.LENGTH_SHORT);
		// return;
		// }

		mobile = etMobile.getText().toString();

		if (!isMobileNO(mobile)) {
			ToastCustom.showMessage(this, "手机号码格式有误", Toast.LENGTH_SHORT);
			return;
		}
		if (mobile == null || (mobile != null && mobile.equals(""))) {
			ToastCustom.showMessage(this, "请输入手机号码", Toast.LENGTH_SHORT);
			return;
		} else if (!Checkingroutine.isNumeric(mobile)) {
			ToastCustom.showMessage(this, "手机号码只能输入数字", Toast.LENGTH_SHORT);
			return;
		} else if (mobile.length() != 11) {
			ToastCustom.showMessage(this, "手机号码必须为11位数字", Toast.LENGTH_SHORT);
			return;
		}

		String loginPasswd = etLoginPasswd.getText().toString();
		if (loginPasswd == null
				|| (loginPasswd != null && loginPasswd.equals(""))) {
			ToastCustom.showMessage(this, "请输入登录密码", Toast.LENGTH_SHORT);
			return;
		} else if (loginPasswd.length() < 6) {
			ToastCustom.showMessage(this, "密码长度必须为6-15位", Toast.LENGTH_SHORT);
			return;
		}

//		String loginPasswdAgain = etLoginPasswdAgain.getText().toString();
//		if (loginPasswdAgain == null
//				|| (loginPasswdAgain != null && loginPasswdAgain.equals(""))) {
//			ToastCustom.showMessage(this, "请确认登录密码", Toast.LENGTH_SHORT);
//			return;
//		} else if (!loginPasswd.equals(loginPasswdAgain)) {
//			ToastCustom.showMessage(this, "登录密码和确认密码不一致");
//			return;
//		}

		// String cardNo = etCardNo.getText().toString();
		// if (cardNo == null || (cardNo != null && cardNo.equals(""))) {
		// ToastCustom.showMessage(this, "请输入银行卡号", Toast.LENGTH_SHORT);
		// return;
		// } else if (!CardUtil.checkBankCard(cardNo)) {
		// ToastCustom.showMessage(this, "银行卡号不正确", Toast.LENGTH_SHORT);
		// return;
		// }
		//
		// if (bankBranchid == null
		// || (bankBranchid != null && bankBranchid.equals(""))) {
		// ToastCustom.showMessage(this, "请选择开户银行支行", Toast.LENGTH_SHORT);
		// return;
		// }

//		String useremail = tv_register_email.getText().toString();
		
		String actcode = et_ratenums.getText().toString();
        if (TextUtils.isEmpty(actcode.trim())) {
            toast("请输入激活码");
            return;
        }
//		String useremail1 = tv_register_email1.getText().toString();
////
//		if (useremail1 == null || useremail1.equals("")) {
//			ToastCustom.showMessage(this, "请输入邮箱", Toast.LENGTH_SHORT);
//			return;
//		} else if (!isEmail(useremail1)) {
//			ToastCustom.showMessage(this, "邮箱格式不正确.", Toast.LENGTH_SHORT);
//			return;
//		}

//		String refereeMobile = "";

		boolean agreeAgreement = ckAgreeAgreement.isChecked();
		if (!agreeAgreement) {
			ToastCustom.showMessage(this, "请先同意用户协议.", Toast.LENGTH_SHORT);
			return;
		}

		String verifCode = etVerifCode.getText().toString();

		if (verifCode == null || (verifCode != null && verifCode.equals(""))) {
			ToastCustom.showMessage(this, "请输入手机验证码", Toast.LENGTH_SHORT);
			return;
		}
//		if(useremail == null || (useremail != null && useremail.equals(""))){
//			ToastCustom.showMessage(this, "请输推荐人手机号", Toast.LENGTH_SHORT);
//			return;
//		}

        regist(etMobile.getText().toString(), etLoginPasswd.getText().toString(),
                btnGetVerifCode.getVCodeId(), verifCode, actcode);

//		oldRegist(verifCode, useremail, actcode);
	}

	private void regist(final String mobile, String pwd, String msgId, String msgCode, String actcode){
		String password = PwdUtils.getEncripyPwd(pwd, 3);
		ProgressDialogUtil.showProgressDlg(this, "");
		Request.getSalesmanSalesmanRegist(mobile, password, msgId, msgCode, actcode, new CallbackString() {
			@Override
			public void onFailure(String msg) {
				ProgressDialogUtil.dismissProgressDlg();
                showWringDialog (msg);
			}

			@Override
			public void onSuccess(String data) {
				ProgressDialogUtil.dismissProgressDlg();
                toast("注册成功！");
                ((AppContext) getApplication()).setMobile(mobile);
                editor.putString("userp", mobile);
                editor.commit();
                finish();
			}

			@Override
			public void onNetError(int code, String msg) {
				ProgressDialogUtil.dismissProgressDlg();
                showWringDialog("网络异常");
			}
		});
	}

	private void showWringDialog(String msg) {
        mWarnDialog = new OneButtonDialogWarn(this,
                R.style.CustomDialog, "提示", msg, "确定",
                new OnMyDialogClickListener() {
                    @Override
                    public void onClick(View v) {
                        mWarnDialog.dismiss();
                    }
                });
        mWarnDialog.show();
    }

	/**
	 * 原注册功能。
	 * @param verifCode
	 * @param useremail
	 * @param actcode
	 */
	private void oldRegist(String verifCode, String useremail, String actcode){
		String mermod = "3";
		String trmtyp = "2";
		RegisterTask task = new RegisterTask();
		task.execute(mobile, loginPasswd, "", mermod, verifCode, trmtyp,HttpUrls.APPNUM,useremail,actcode);
	}

	/**
	 * 验证邮箱合法性
	 * 
	 * @param email
	 * @return
	 */
	public boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 打开用户协议页面
	 */
	@SuppressLint("NewApi")
	private void openReadAgreement() {
		
		Intent it = new Intent(RegisterActivity.this,WebViewActivity.class);
		it.putExtra("URLs", HttpUrls.REGISTRATIONAGREEMENT);
		startActivity(it);
//		Builder builder = new AlertDialog.Builder(this,
//				android.R.style.Theme_DeviceDefault_DialogWhenLarge);
//		builder.setTitle("秀儿支付服务协议");
//		builder.setMessage(R.string.register_treaty);
//		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface mWarnDialog, int which) {
//				mWarnDialog.dismiss();
//			}
//		});
//		AlertDialog mWarnDialog = builder.create();
//		mWarnDialog.show();

	}

	@Override
	public void onClick(View v) {
		if (DateUtil.isFastDoubleClick()) {
			return;
		} else {
			switch (v.getId()) {
			case R.id.btn_register_query_agreement:// 打开协议
				LicenseDialog dialog = new LicenseDialog();
				dialog.setListener(new LicenseDialog.LicenseAgreeListener() {
					@Override
					public void agreeLicense() {
						ckAgreeAgreement.setChecked(true);
					}
				});
				dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
//				openReadAgreement();
				break;
			case R.id.btn_register_get_verif_code:// 获取验证码
				getVerifCode();
				break;
			case R.id.btn_register_submit:// 注册
				register();
				break;
			case R.id.e_pwd:// 注册
				register();
				break;
				
			case R.id.et_register_name_error:// 姓名点击
				etName.setVisibility(View.VISIBLE);
				tv_nameError.setVisibility(View.GONE);
				etName.requestFocus();
				break;
			case R.id.et_register_cert_no_error:// 身份证号码错误点击
				etCertNo.setVisibility(View.VISIBLE);
				tv_cardError.setVisibility(View.GONE);
				etCertNo.requestFocus();
				break;
			case R.id.et_register_mobile_error:// 手机号码错误点击
				relalay.setVisibility(View.VISIBLE);
				load_img.setVisibility(View.GONE);
				load.setVisibility(View.GONE);
				etMobile.setVisibility(View.VISIBLE);
				tv_mobileError.setVisibility(View.GONE);
				etMobile.requestFocus();
				break;
			case R.id.et_register_login_passwd_error:// 身份证号码错误点击
				etLoginPasswd.setVisibility(View.VISIBLE);
				tv_passError.setVisibility(View.GONE);
				etLoginPasswd.requestFocus();
				break;
//			case R.id.et_register_login_passwd_again_error:// 身份证号码错误点击
//				etLoginPasswdAgain.setVisibility(View.VISIBLE);
//				tv_passErrorAgain.setVisibility(View.GONE);
//				etLoginPasswdAgain.requestFocus();
//				break;
//			case R.id.et_email_error:// 邮箱
//				tv_emailError.setVisibility(View.GONE);
//				tv_register_email1.setVisibility(View.VISIBLE);
//				tv_register_email1.requestFocus();
//				break;
			default:
				break;
			}
		}

	}

	/**
	 * 获取验证码
	 */
	private void getVerifCode() {

		// String termina = etTerminalNo.getText().toString();
		//
		// if (termina == null || termina.equals("")) {
		// ToastCustom.showMessage(this, "请输入终端号", Toast.LENGTH_SHORT);
		// return;
		// }

		// String name = etName.getText().toString();
		// if (name == null || (name != null && name.equals(""))) {
		// ToastCustom.showMessage(this, "请输入用户姓名", Toast.LENGTH_SHORT);
		// return;
		// } else if (!ChineseUtil.checkNameChese(name)) {
		// ToastCustom.showMessage(this, "用户姓名必须全为中文", Toast.LENGTH_SHORT);
		// return;
		// }

		// String certNo = etCertNo.getText().toString();
		// if (certNo == null || (certNo != null && certNo.equals(""))) {
		// ToastCustom.showMessage(this, "请输入身份证号", Toast.LENGTH_SHORT);
		// return;
		// }
		// String identifyID = null;
		// try {
		// identifyID = Checkingroutine.IDCard.IDCardValidate(certNo);
		// } catch (ParseException e) {
		//
		// e.printStackTrace();
		// }
		// if (!"".equals(identifyID)) {
		// ToastCustom.showMessage(this, identifyID, Toast.LENGTH_SHORT);
		// return;
		// }

		mobile = etMobile.getText().toString();

		if (tv_mobileError.getVisibility() == View.VISIBLE) {

			ToastCustom.showMessage(this, "手机号码有误", Toast.LENGTH_SHORT);
			return;
		}

		if (!isMobileNO(mobile)) {
			ToastCustom.showMessage(this, "手机号码格式有误", Toast.LENGTH_SHORT);
			return;
		}
		if (mobile == null || (mobile != null && mobile.equals(""))) {
			ToastCustom.showMessage(this, "请输入手机号码", Toast.LENGTH_SHORT);
			return;
		} else if (!Checkingroutine.isNumeric(mobile)) {
			ToastCustom.showMessage(this, "手机号码只能输入数字", Toast.LENGTH_SHORT);
			return;
		} else if (mobile.length() != 11) {
			ToastCustom.showMessage(this, "手机号码必须为11位数字", Toast.LENGTH_SHORT);
			return;
		}

//		String loginPasswd = etLoginPasswd.getText().toString();
//		if (loginPasswd == null
//				|| (loginPasswd != null && loginPasswd.equals(""))) {
//			ToastCustom.showMessage(this, "请输入登录密码", Toast.LENGTH_SHORT);
//			return;
//		} else if (loginPasswd.length() != 6) {
			// ToastCustom.showMessage(this, "密码长度必须为6位", Toast.LENGTH_SHORT);
			// return;
//		}

//		String loginPasswdAgain = etLoginPasswdAgain.getText().toString();
//		if (loginPasswdAgain == null
//				|| (loginPasswdAgain != null && loginPasswdAgain.equals(""))) {
//			ToastCustom.showMessage(this, "请确认登录密码", Toast.LENGTH_SHORT);
//			return;
//		} else if (!loginPasswd.equals(loginPasswdAgain)) {
//			ToastCustom.showMessage(this, "登录密码和确认密码不一致", Toast.LENGTH_SHORT);
//			return;
//		}

//		String useremails1 = tv_register_email1.getText().toString();
//
//		if (useremails1 == null || useremails1.equals("")) {
//			ToastCustom.showMessage(this, "请输入邮箱", Toast.LENGTH_SHORT);
//			return;
//		} else if (!isEmail(useremails1)) {
//			ToastCustom.showMessage(this, "邮箱格式不正确.", Toast.LENGTH_SHORT);
//			return;
//		}

		// String cardNo = etCardNo.getText().toString();
		// if (cardNo == null || (cardNo != null && cardNo.equals(""))) {
		// ToastCustom.showMessage(this, "请输入银行卡号", Toast.LENGTH_SHORT);
		// return;
		// } else if (!CardUtil.checkBankCard(cardNo)) {
		// ToastCustom.showMessage(this, "银行卡号不正确", Toast.LENGTH_SHORT);
		// return;
		// }

		// if (bankBranchid == null
		// || (bankBranchid != null && bankBranchid.equals(""))) {
		// ToastCustom.showMessage(this, "请选择开户银行支行", Toast.LENGTH_SHORT);
		// return;
		// }

		boolean agreeAgreement = ckAgreeAgreement.isChecked();
		if (!agreeAgreement) {
			ToastCustom.showMessage(this, "请先同意用户协议.", Toast.LENGTH_SHORT);
			return;
		}
//		String useremail = tv_register_email.getText().toString();
//		if(useremail == null || (useremail != null && useremail.equals(""))){
//			ToastCustom.showMessage(this, "请输推荐人手机号", Toast.LENGTH_SHORT);
//			return;
//		}
//		String useremail = tv_register_email.getText().toString();
//		if (useremail == null || (useremail != null && useremail.equals(""))) {
//			ToastCustom.showMessage(this, "请输推荐人手机号", Toast.LENGTH_SHORT);
//			return;
//		}

		// 验证终端是否为null,没有找到终端为null,Log记录
		if (termina == null || termina.trim().equals("")) {
			termina = "psamIdIsNull";
		}
//		btnGetVerifCode.setEnabled(false);

        btnGetVerifCode.getVCode(mobile, null);

//		final String[] values = { "199018", mobile, "100001", termina, trmtyp,HttpUrls.APPNUM};
//
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//
//				HashMap<String, Object> result = NetCommunicate.get(
//						HttpUrls.REGISTER_VERIFCODE, values);
//				Message msg = new Message();
//				if (result == null) {
//					msg.what = 10;
//					msg.obj = result;
//				} else {
//					msg.what = 11;
//					msg.obj = result;
//				}
//				mHandler.sendMessage(msg);
//			}
//		}).start();
		// Common.
	}

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
			String[] values = { params[0], params[1], params[2], params[3],params[4] };
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
							etCardNo.setText("");
							tvBankName.setText("");
							etCardNo.setEnabled(false);
							list = null;
							bankProvinceid = null;
							bankCityid = null;
							bankBranchName = null;
							showSingleWarnDialog("暂不支持信用卡注册！");
						} else {
							cardType = "";
						}
					}

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
					// etCardNo.setText("");
					// tvBankName.setText("");
					// etCardNo.setEnabled(false);
					// list = null;
					// bankProvinceid = null;
					// bankCityid = null;
					// bankBranchName = null;
					// showSingleWarnDialog("暂不支持邮政卡！");
					// }
					else {
						list = result.list;
						tvBankName.setText(result.getIssnam() + cardType);
					}
				} else {
					ToastCustom.showMessage(RegisterActivity.this, result
							.getRspmsg().toString(), Toast.LENGTH_SHORT);
				}
			}
			super.onPostExecute(result);
		}

	}

	class BankCityTask extends AsyncTask<String, Integer, CityEntity> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在查询中...");
		}

		@Override
		protected CityEntity doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate
					.getQueryCity(HttpUrls.QUERY_BANK_CITY, values);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(CityEntity result) {
			loadingDialogWhole.dismiss();
			if (result != null) {

				if (Entity.STATE_OK.equals(result.getRspcod())) {
					// ProvinceDialog mWarnDialog = new ProvinceDialog(2,
					// RegisterActivity.this, result.list,
					// new OnProvinceDialogListener() {
					//
					// @Override
					// public void back(HashMap<String, Object> map) {
					// bankCityid = map.get("CITYID").toString();
					// tvBankCity.setText(map.get("CITYNAM")
					// .toString());
					//
					// }
					// });
					// mWarnDialog.setTitle("银行卡开户城市");
					// mWarnDialog.show();

					Intent intent = new Intent(RegisterActivity.this,
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

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(CityEntity result) {
			loadingDialogWhole.dismiss();
			if (result != null) {

				if (Entity.STATE_OK.equals(result.getRspcod())) {
					if (result.list.size() == 0) {
						ToastCustom.showMessage(RegisterActivity.this,
								"没有该城市支行信息", Toast.LENGTH_SHORT);
						return;
					}
					// ProvinceDialog mWarnDialog = new ProvinceDialog(3,
					// RegisterActivity.this, result.list,
					// new OnProvinceDialogListener() {
					//
					// @Override
					// public void back(HashMap<String, Object> map) {
					// bankBranchid = map.get("BKNO").toString();
					// tvBankBranch.setText(map.get("BENELX")
					// .toString());
					// // bankBranchName =
					// // map.get("BENELX").toString();
					//
					// }
					// });
					// mWarnDialog.setTitle("银行卡开户支行");
					// mWarnDialog.show();
					Intent intent = new Intent(RegisterActivity.this,
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
				}
			}
			super.onPostExecute(result);
		}

	}

	class MyOnClickListener implements OnClickListener {
		ArrayList<HashMap<String, Object>> list;

		public MyOnClickListener(ArrayList<HashMap<String, Object>> list) {
			this.list = list;
		}

		@Override
		public void onClick(View v) {
		}

	}

	// @SuppressLint("InlinedApi")
	// private void getBankInfo(ArrayList<HashMap<String, Object>> list) {
	//
	// ProvinceDialog mWarnDialog = new ProvinceDialog(
	// android.R.style.Theme_DeviceDefault_Light_Dialog, 1,
	// RegisterActivity.this, list, new OnProvinceDialogListener() {
	//
	// @Override
	// public void back(HashMap<String, Object> map) {
	// bankProvinceid = map.get("PROVID").toString();
	// tvBankProvince.setText(map.get("PROVNAM").toString());
	// }
	// });
	// mWarnDialog.setTitle("银行卡开户省份");
	// mWarnDialog.show();
	//
	// }

	class RegisterTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在注册中...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3],
					params[4], params[5] ,params[6],params[7],params[8]};
			return NetCommunicate.get(HttpUrls.REGISTER, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				ToastCustom.showMessage(RegisterActivity.this,
						result.get(Entity.RSPMSG).toString(),
						Toast.LENGTH_SHORT);
				// 注册成功
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					((AppContext) getApplication()).setMobile(mobile);
					
					editor.putString("userp", mobile);
					editor.commit();
					finish();
				}
			} else {
				ToastCustom.showMessage(RegisterActivity.this, "fail",
						Toast.LENGTH_SHORT);
			}
			super.onPostExecute(result);
		}
	}

	/**
	 * 终端号验证
	 * 
	 * @author liangge
	 * 
	 */
	class ValidateTerminTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate.get(HttpUrls.LOCALYZZD, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					// flag2 = true;
				} else {
					// flag2 = false;
					// tvTerminError.setVisibility(View.VISIBLE);
					// tvTerminError.setText(result.get(Entity.RSPMSG).toString());
					// tvTerminError
					// .setError(result.get(Entity.RSPMSG).toString());
					// et_register_termin.setVisibility(View.GONE);
					Toast.makeText(getApplicationContext(),
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();
				}

			} else {
				// flag2 = false;
				// tvTerminError.setVisibility(View.VISIBLE);
				// tvTerminError.setText("fail");
				// tvTerminError.setError("fail");
				// et_register_termin.setVisibility(View.GONE);
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
			load.setVisibility(View.VISIBLE);
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate.get(HttpUrls.BUSSINESS_INFO, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			if (result != null) {

				if (result.get("RSPCOD").toString().equals("000000")) {

					load.setVisibility(View.GONE);
					load_img.setVisibility(View.VISIBLE);
				} else {
					load.setVisibility(View.GONE);
					relalay.setVisibility(View.GONE);
					load_img.setVisibility(View.GONE);
					tv_mobileError.setVisibility(View.VISIBLE);
					tv_mobileError.setText("该手机号码已注册");
					etMobile.setVisibility(View.GONE);
				}
				super.onPostExecute(result);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == REQUEST_CONTACT) {
			if (resultCode == RESULT_OK) {
				if (data == null) {
					return;
				}

				Uri result = data.getData();
				
				 ContentResolver reContentResolverol = getContentResolver();
				
                Cursor c = managedQuery(result, null, null, null, null);
                c.moveToFirst();
                
                String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
                        null, 
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, 
                        null, 
                        null);
                while (phone.moveToNext()) {
                    String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    
                    
                    Pattern p = Pattern.compile("\\s*|\t|\r|\n|-");  
                    Matcher m = p.matcher(usernumber);  
                    String dest = m.replaceAll("");  
                    usernumber = usernumber.replace("-","");  
                    tv_register_email.setText(dest);
                }

                
                
//              int  contactName = result.getPhoneContacts(contactId);
//				contactId = result.getLastPathSegment();
//				contactId = result.getQueryParameters(key)
//				
//				contactName = getPhoneContacts(contactId);

			}
		}else{
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
}