package com.td.qianhai.epay.oem;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.RichTreasureBean;
import com.td.qianhai.epay.oem.interfaces.onMyaddTextListener;
import com.td.qianhai.epay.oem.mail.utils.ConnUtil;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.mail.utils.SetPricePoint;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.MyEditDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.epay.utils.Checkingroutine;
import com.td.qianhai.epay.utils.ChineseUtil;
import com.td.qianhai.epay.utils.InputMethodUtils;
import com.td.qianhai.mpay.utils.XhgUtil;

public class TransferAccountsActivity extends BaseActivity implements
		OnClickListener {

	/** 错误提示 */
	private TextView transfer_money_error, transfer_phone_error,transfer_username_error,tv_pro,//transfer_usersaypwd_error
			transfer_capital_tvs,tv_payee,tv_payee_p;
	/** 输入框 */
	private EditText transfer_money, transfer_userphone,transfer_username,paypwd;//,transfer_usersaypwd;
	/** 提交按钮 退出按钮 标题 */
	private TextView transfer_confirm, bt_title_left, tv_title_contre,transfer_allmoney,contact;
	/** 金额显示 */
	private LinearLayout transfer_capital_layouts,tarns_user,userphone,username;
	/** 收款手机号  姓名  支付密码*/
	private String tousermobile,name,payPassword;
	/** 当前用户手机号*/
	private String mymobile;
	
	private OneButtonDialogWarn warnDialog;
	
	private RichTreasureBean treasureBean;
	
	private ImageView load_img;
	
	private ProgressBar load;
	
	private RelativeLayout relalay;
	
	private MyEditDialog doubleWarnDialog1;
	
	
	private   int REQUEST_CONTACT = 1;
	
	private String balance ;
	
	private String tag = "";
	
	private String truserphone;
	
	private String trusername;
	
	private String contactId;
	
	private String contactName;
	
	private LayoutInflater inflater;
	
	private String etm,etu;
	
	private double Upperlimit;
	
	/** 界面视图*/
	private View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transfer_accounts_avtivity);

		AppContext.getInstance().addActivity(this);
		
		Intent it  = getIntent();
		
		if(it.getStringExtra("tag")!=null){
			
			tag = it.getStringExtra("tag");
			
			truserphone = tag.substring(0,11);
					
			trusername = tag.substring(11,tag.length());
		}
		inflater = LayoutInflater.from(this); 
//		inflater = con.getLayoutInflater();
		view = inflater.inflate(R.layout.edit_dialog, null);
		initview();
//		mymobile = ((AppContext)getApplication()).getMobile();
		mymobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		
		GetWalletInfo  walletinfo = new GetWalletInfo();
		
		walletinfo.execute(HttpUrls.RICH_TREASURE_INFO + "",
				mymobile);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		

		
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		transfer_confirm.setEnabled(true);
		if(loadingDialogWhole!=null){
			loadingDialogWhole.dismiss();
		}
	}

	private void initview() {
		paypwd = (EditText) view.findViewById(R.id.searchCf);
		contact = (TextView) findViewById(R.id.contact);
		tarns_user = (LinearLayout) findViewById(R.id.tarns_user);
		userphone = (LinearLayout) findViewById(R.id.userphone);
		username = (LinearLayout) findViewById(R.id.username);
		tv_pro = (TextView) findViewById(R.id.tv_pro);
		tv_payee = (TextView) findViewById(R.id.tv_payee);
		tv_payee_p = (TextView) findViewById(R.id.tv_payee_p);
		relalay = (RelativeLayout) findViewById(R.id.relalay);
		load_img = (ImageView) findViewById(R.id.load_phonr_img);
		load = (ProgressBar) findViewById(R.id.load_phonr_pro);
		bt_title_left = (TextView) findViewById(R.id.bt_title_left);
		tv_title_contre = (TextView) findViewById(R.id.tv_title_contre);
		tv_title_contre.setText("付款");
		transfer_username_error = (TextView) findViewById(R.id.transfer_username_error);
//		transfer_usersaypwd_error = (TextView) findViewById(R.id.transfer_usersaypwd_error);
		transfer_capital_tvs = (TextView) findViewById(R.id.transfer_capital_tvs);
		transfer_capital_layouts = (LinearLayout) findViewById(R.id.transfer_capital_layouts);
		transfer_money_error = (TextView) findViewById(R.id.transfer_money_error);
		transfer_phone_error = (TextView) findViewById(R.id.transfer_phone_error);
		transfer_money = (EditText) findViewById(R.id.transfer_money);
		transfer_userphone = (EditText) findViewById(R.id.transfer_userphone);
		transfer_confirm = (TextView) findViewById(R.id.transfer_confirm);
		transfer_username = (EditText) findViewById(R.id.transfer_username);
//		transfer_usersaypwd = (EditText) findViewById(R.id.transfer_usersaypwd);
		transfer_allmoney = (TextView) findViewById(R.id.transfer_allmoney);
		bt_title_left.setOnClickListener(this);
		transfer_money_error.setOnClickListener(this);
		transfer_phone_error.setOnClickListener(this);
		transfer_money.setOnClickListener(this);
		transfer_userphone.setOnClickListener(this);
		transfer_confirm.setOnClickListener(this);
		transfer_confirm.setOnClickListener(this);
		transfer_username_error.setOnClickListener(this);
//		transfer_usersaypwd_error.setOnClickListener(this);
		transfer_username.setOnClickListener(this);
//		transfer_usersaypwd.setOnClickListener(this);
		
		
		contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();

				intent.setAction(Intent.ACTION_PICK);

				intent.setData(ContactsContract.Contacts.CONTENT_URI);

				startActivityForResult(intent, REQUEST_CONTACT);
				
			}
		});
		
	
		
		
		if(!tag.equals("")){
			tarns_user.setVisibility(View.VISIBLE);
			username.setVisibility(View.GONE);
			userphone.setVisibility(View.GONE);
			switch (trusername.length()) {
			case 2:
				String setnames = trusername.substring(0,1);
				tv_payee.setText(setnames+"*");
				break;
			case 3:
				String setnames1 = trusername.substring(0,1);
				String getnames1 = trusername.substring(trusername.length()-1);
				tv_payee.setText(setnames1+"*"+getnames1);
				break;
			case 4:
				String setnames2 = trusername.substring(0,1);
				String getnames2 = trusername.substring(trusername.length()-1);
				tv_payee.setText(setnames2+"**"+getnames2);
				break;
			case 5:
				String setnames3 = trusername.substring(0,1);
				String getnames3 = trusername.substring(trusername.length()-1);
				tv_payee.setText(setnames3+"***"+getnames3);
				break;
			case 6:
				String setnames4 = trusername.substring(0,1);
				String getnames4 = trusername.substring(trusername.length()-1);
				tv_payee.setText(setnames4+"****"+getnames4);
				break;
			case 7:
				String setnames5 = trusername.substring(0,1);
				String getnames5 = trusername.substring(trusername.length()-1);
				tv_payee.setText(setnames5+"*****"+getnames5);
				break;
			case 8:
				String setnames6 = trusername.substring(0,1);
				String getnames6 = trusername.substring(trusername.length()-1);
				tv_payee.setText(setnames6+"******"+getnames6);
				break;
			case 9:
				String setnames7 = trusername.substring(0,1);
				String getnames7 = trusername.substring(trusername.length()-1);
				tv_payee.setText(setnames7+"*******"+getnames7);
				break;
			case 10:
				String setnames8 = trusername.substring(0,1);
				String getnames8 = trusername.substring(trusername.length()-1);
				tv_payee.setText(setnames8+"********"+getnames8);
				break;
			default:
				tv_payee.setText(trusername);
				break;
			}
			
//			tv_payee.setText(trusername);
			
			String setphone = truserphone.substring(0,3);
			String getphone = truserphone.substring(truserphone.length()-4);
			tv_payee_p.setText(setphone+"****"+getphone);
			
//			tv_payee_p.setText(truserphone);
			init2();
		}else{
			
			init1();
		}
		
		
		
//		//判断支付密码
//		transfer_usersaypwd.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				// TODO Auto-generated method stub
//				if (!hasFocus) {
//					
//					payPassword = transfer_usersaypwd.getText().toString();
//					if (payPassword == null || payPassword.equals("")) {
//						transfer_usersaypwd_error.setVisibility(View.VISIBLE);
//						transfer_usersaypwd_error.setText("请输入支付密码！");
//						transfer_usersaypwd.setVisibility(View.GONE);
//						return;
//					}
//					if (payPassword.length() < 6||payPassword.length() > 15) {
//						transfer_usersaypwd_error.setVisibility(View.VISIBLE);
//						transfer_usersaypwd_error.setText("输入的密码长度有误,请输入6～15个数字、字母或特殊符号！");
//						transfer_usersaypwd.setVisibility(View.GONE);
//						return;
//					}
//				}
//			}
//		});
	}

	private void init2() {
		// TODO Auto-generated method stub
		//判断金额
//		transfer_money.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				// TODO Auto-generated method stub
//				if (!hasFocus) {
//					balanceFlag();
//				}
//			}
//		});
		
//		transfer_money.setSelection(transfer_money.length());
		
		//金额10万上限
		transfer_money.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					transfer_capital_layouts.setVisibility(View.VISIBLE);
					try {
						transfer_capital_tvs.setText(XhgUtil.change(Double
								.parseDouble(transfer_money.getText().toString())));
						Float money = Float.parseFloat(transfer_money.getText()
								.toString());
						
						if(money>Upperlimit){
							transfer_money_error.setVisibility(View.VISIBLE);
							transfer_money_error.setText("转账金额不能大于"+Upperlimit+"元");
							transfer_money_error.setError("转账金额不能大于"+Upperlimit+"元");
							transfer_money.setVisibility(View.GONE);
							return;
						}else
						if (money >= Float.parseFloat(treasureBean.getAvaamt())/100) {
							transfer_money_error.setVisibility(View.VISIBLE);
							transfer_money_error.setText("余额不足");
							transfer_money_error.setError("余额不足");
							transfer_money.setVisibility(View.GONE);
//							transfer_money.requestFocus();
							return;
						}
					} catch (Exception e) {
						transfer_capital_tvs.setText("输入有误。。。");
					}
				} else {
					transfer_capital_layouts.setVisibility(View.GONE);
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
		
		SetPricePoint.setPricePoint(transfer_money);
		

		
	}

	private void init1() {
		// TODO Auto-generated method stub
		
		InputMethodUtils.show(TransferAccountsActivity.this, transfer_money);

//		transfer_money.setSelection(transfer_money.length());
		//判断金额
//		transfer_money.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				// TODO Auto-generated method stub
//				if (!hasFocus) {
//					
//					balanceFlag();
//				}
//			}
//		});
		//金额10万上限
		transfer_money.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					transfer_capital_layouts.setVisibility(View.VISIBLE);
					try {
						transfer_capital_tvs.setText(XhgUtil.change(Double
								.parseDouble(transfer_money.getText().toString())));
						
						Float money = Float.parseFloat(transfer_money.getText()
								.toString());
						
						if(money>Upperlimit){
							transfer_money_error.setVisibility(View.VISIBLE);
							transfer_money_error.setText("转账金额不能大于"+Upperlimit+"元");
							transfer_money_error.setError("转账金额不能大于"+Upperlimit+"元");
							transfer_money.setVisibility(View.GONE);
							return;
						}else
						if (money > Float.parseFloat(treasureBean.getAvaamt())/100) {
							transfer_money_error.setVisibility(View.VISIBLE);
							transfer_money_error.setText("余额不足");
							transfer_money_error.setError("余额不足");
							transfer_money.setVisibility(View.GONE);
							transfer_money.requestFocus();
							return;
						}
					} catch (Exception e) {
						transfer_capital_tvs.setText("输入有误。。。");
					}
				} else {
					transfer_capital_layouts.setVisibility(View.GONE);
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
		
		SetPricePoint.setPricePoint(transfer_money);
		

		
		transfer_money.setOnEditorActionListener(new EditText.OnEditorActionListener() {  
            
            @Override  
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
                /*判断是否是“GO”键*/  
                if(actionId == EditorInfo.IME_ACTION_GO){  
                	
                	transfer_userphone.requestFocus();
                      
                    return true;  
                }  
                return false;  
            }  
        });  
		
		transfer_userphone.setOnEditorActionListener(new TextView.OnEditorActionListener() {  
            
            @Override  
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
                /*判断是否是“GO”键*/  
                if(actionId == EditorInfo.IME_ACTION_GO){  
                	
                	transfer_username.requestFocus();
                      
                    return true;  
                }  
                return false;  
            }  
        }); 
		transfer_username.setOnEditorActionListener(new TextView.OnEditorActionListener() {  
            
            @Override  
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
                /*判断是否是“GO”键*/  
                if(actionId == EditorInfo.IME_ACTION_GO){  
                	
                	closeinput();
                    return true;  
                }  
                return false;  
            }  
        }); 
		
		//判断手机
		transfer_userphone.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					tousermobile = transfer_userphone.getText().toString();
					if (tousermobile == null || (tousermobile != null && tousermobile.equals(""))) {
						transfer_phone_error.setVisibility(View.VISIBLE);
						transfer_phone_error.setText("请输入手机号码");
						transfer_userphone.setVisibility(View.GONE);
						relalay.setVisibility(View.GONE);
						// load_img.setVisibility(View.GONE);
						// load.setVisibility(View.GONE);
					} else if (!Checkingroutine.isNumeric(tousermobile)) {
						transfer_phone_error.setVisibility(View.VISIBLE);
						transfer_phone_error.setText("只能输入数字");
						transfer_userphone.setVisibility(View.GONE);
						relalay.setVisibility(View.GONE);
					} else if (tousermobile.length() != 11) {
						transfer_phone_error.setVisibility(View.VISIBLE);
						transfer_phone_error.setText("手机号码必须为11位数字");
						transfer_userphone.setVisibility(View.GONE);
						relalay.setVisibility(View.GONE);
					} else if (!isMobileNO(tousermobile)) {
						transfer_phone_error.setVisibility(View.VISIBLE);
						transfer_phone_error.setText("手机号码格式有误");
						transfer_userphone.setVisibility(View.GONE);
						relalay.setVisibility(View.GONE);
					}
					
				}
			}
		});
		
		transfer_userphone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()==11){
					
					String custId = transfer_userphone.getText().toString();
					
					final BussinessInfoTask task = new BussinessInfoTask();

					task.execute(HttpUrls.BUSSINESS_INFO + "", custId, "4",
							"0");
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
//								ToastCustom.showMessage(
//										TransferAccountsActivity.this,
//										"请求服务器超时,请重新操作!");
								task.cancel(true);
							} catch (Exception e) {
								Toast.makeText(getApplicationContext(),"系统错误,请重新操作",
										Toast.LENGTH_SHORT).show();
//								ToastCustom.showMessage(
//										TransferAccountsActivity.this,
//										"系统错误,请重新操作!");
								task.cancel(true);
							}
						};
					}.start();
				}else{
					transfer_username.setText("");
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
		//判断姓名
		transfer_username.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					name = transfer_username.getText().toString();
					
					if (name == null || (name != null && name.equals(""))) {
						transfer_username.setVisibility(View.GONE);
						transfer_username_error.setText("请输入用户姓名");
						transfer_username_error.setVisibility(View.VISIBLE);
					} else if (!ChineseUtil.checkNameChese(name)) {
						transfer_username.setVisibility(View.GONE);
						transfer_username_error.setText("用户名只能是中文");
						transfer_username_error.setVisibility(View.VISIBLE);
					}

					
				}
			}
		});
		
	}

	private void balanceFlag() {
		String balance = transfer_money.getText().toString();

		if (balance != null && !balance.equals("")) {
			float intBalance = Float.parseFloat(balance);
			
			if (Upperlimit < intBalance) {

//				ToastCustom.showMessage(this, "充值金额不能小于1");
				transfer_money_error.setVisibility(View.VISIBLE);
				transfer_money_error.setText("转账金额不能大于"+Upperlimit+"元");
				transfer_money_error.setError("转账金额不能大于"+Upperlimit+"元");
				transfer_money.setText("");
				transfer_money.setVisibility(View.GONE);
				return;
			}
		}

		if (balance == null || (balance != null && balance.equals(""))) {
			transfer_money_error.setVisibility(View.VISIBLE);
			transfer_money_error.setText("请输入收款金额");
			transfer_money_error.setError("请输入收款金额");
			transfer_money.setVisibility(View.GONE);
			return;
		}
		if (!isNumber(balance)) {
			transfer_money_error.setVisibility(View.VISIBLE);
			transfer_money_error.setText("格式错误");
			transfer_money_error.setError("格式错误");
			transfer_money.setVisibility(View.GONE);
			return;
		} else {
			if (balance.substring(balance.length() - 1, balance.length())
					.equals(".")) {
				transfer_money_error.setVisibility(View.VISIBLE);
				transfer_money_error.setText("格式错误");
				transfer_money_error.setError("格式错误");
				transfer_money.setVisibility(View.GONE);
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
	
	/**
	 * 管理键盘
	 */
	public void closeinput(){
		InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.transfer_confirm:
			String a = transfer_money.getText().toString();
			if(a!=null&&!a.equals("")){
				if (0>= Float.parseFloat(a)) {
					transfer_money_error.setVisibility(View.VISIBLE);
					transfer_money_error.setText("请输入正确金额");
					transfer_money_error.setError("请输入正确金额");
					transfer_money.setVisibility(View.GONE);
					transfer_money.setText("");
					return;
				}
			}

			if(!tag.equals("")){
				transferaccount1();
			}else{
				transferaccount();
			}
			
			
			break;
		case R.id.bt_title_left:

			if(!tag.equals("")){
				SpannableString msp = new SpannableString("您确取消本次交易?");
				showDoubleWarnDialog(msp);
			}else{
				finish();
			}


			break;
		case R.id.transfer_money_error:
			transfer_money.setVisibility(View.VISIBLE);
			transfer_money.setText("");
			transfer_money_error.setVisibility(View.GONE);
			transfer_money.requestFocus();
			InputMethodUtils.show(TransferAccountsActivity.this, transfer_money);
			break;
		case R.id.transfer_phone_error:
			relalay.setVisibility(View.VISIBLE);
			load_img.setVisibility(View.GONE);
			load.setVisibility(View.GONE);
			transfer_userphone.setVisibility(View.VISIBLE);
			transfer_phone_error.setVisibility(View.GONE);
			transfer_userphone.requestFocus();
			break;
		case R.id.transfer_username_error:
			transfer_username.setVisibility(View.VISIBLE);
			transfer_username_error.setVisibility(View.GONE);
			transfer_username.requestFocus();
			break;
//		case R.id.transfer_usersaypwd_error:
//			transfer_usersaypwd_error.setVisibility(View.GONE);
//			transfer_usersaypwd.setVisibility(View.VISIBLE);
//			transfer_usersaypwd.requestFocus();
//			break;
			
		default:
			break;
		}
	}

	private void transferaccount1() {
		// TODO Auto-generated method stub
		
//		transfer_money.clearFocus();
		// etCardName.clearFocus();
		 balance = transfer_money.getText().toString();

		if (balance != null && !balance.equals("")) {
			float intBalance = Float.parseFloat(balance);
//			if (intBalance <= 1) {
//
//				ToastCustom.showMessage(this, "转账金额必须大于1.00元");
//				return;
//				
//			} else
				if (intBalance > 20000) {
				transfer_money.setText("");
				Toast.makeText(getApplicationContext(),"转账金额不能大于20000元,请重新输入",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(TransferAccountsActivity.this,
//						"转账金额不能大于20000元,请重新输入");
				transfer_money.setFocusable(true);
				transfer_money.setFocusableInTouchMode(true);
				return;
			}
			
		}
		
//		String balance = transfer_money.getText().toString();

		if (balance != null && !balance.equals("")) {
			float intBalance = Float.parseFloat(balance);
			if (Upperlimit < intBalance) {

//				ToastCustom.showMessage(this, "充值金额不能小于1");
				transfer_money_error.setVisibility(View.VISIBLE);
				transfer_money_error.setText("转账金额不能大于"+Upperlimit+"元");
				transfer_money_error.setError("转账金额不能大于"+Upperlimit+"元");
				transfer_money.setText("");
				transfer_money.setVisibility(View.GONE);

				return;
			}
		}

		if (balance == null || (balance != null && balance.equals(""))) {
			transfer_money_error.setVisibility(View.VISIBLE);
			transfer_money_error.setText("请输入收款金额");
			transfer_money_error.setError("请输入收款金额");
			transfer_money.setVisibility(View.GONE);
			return;
		}
		if (!isNumber(balance)) {
			transfer_money_error.setVisibility(View.VISIBLE);
			transfer_money_error.setText("格式错误");
			transfer_money_error.setError("格式错误");
			transfer_money.setVisibility(View.GONE);
			return;
		} else {
			if (balance.substring(balance.length() - 1, balance.length())
					.equals(".")) {
				transfer_money_error.setVisibility(View.VISIBLE);
				transfer_money_error.setText("格式错误");
				transfer_money_error.setError("格式错误");
				transfer_money.setVisibility(View.GONE);
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
				}
			}
		}
		int a = (int) (Double.parseDouble(balance) * 100);
		balance = String.valueOf(a);
		
		transfer_confirm.setEnabled(false);
		doubleWarnDialog1 = new MyEditDialog(TransferAccountsActivity.this,
				R.style.MyEditDialog, "转账", "请输入支付密码", "确认", "取消", balance,
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {

						switch (v.getId()) {
						case R.id.btn_right:
							doubleWarnDialog1.dismiss();
							closeinput();
//							InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
							break;
						case R.id.btn_left:
							String paypwd = doubleWarnDialog1.getpaypwd();

							if (paypwd == null || paypwd.equals("")) {
								Toast.makeText(getApplicationContext(),"请输入支付密码",
										Toast.LENGTH_SHORT).show();
//								ToastCustom.showMessage(
//										TransferAccountsActivity.this,
//										"请输入支付密码！");
								return;
							}
							if (paypwd.length() < 6 || paypwd.length() > 15) {
								Toast.makeText(getApplicationContext(),"输入的密码长度有误,请输入6～15个数字、字母或特殊符号",
										Toast.LENGTH_SHORT).show();
//								ToastCustom.showMessage(
//										TransferAccountsActivity.this,
//										"输入的密码长度有误,请输入6～15个数字、字母或特殊符号！");
								return;
							}
							


							TransferAccountTask task = new TransferAccountTask();

							task.execute(HttpUrls.TANSFER_ACCO + "", mymobile,
									truserphone, trusername, paypwd, "02",
									balance);
							break;
						default:
							break;
						}
						transfer_confirm.setEnabled(false);
					}
					
				},
		
				new onMyaddTextListener() {
					
					@Override
					public void refreshActivity(String paypwd) {
						if (paypwd == null || paypwd.equals("")) {
							Toast.makeText(getApplicationContext(),"请输入支付密码",
									Toast.LENGTH_SHORT).show();
//							ToastCustom.showMessage(
//									TransferAccountsActivity.this,
//									"请输入支付密码！");
							return;
						}
						if (paypwd.length() < 6 || paypwd.length() > 15) {
							Toast.makeText(getApplicationContext(),"输入的密码长度有误,请输入6个数字、字母或特殊符号",
									Toast.LENGTH_SHORT).show();
//							ToastCustom.showMessage(
//									TransferAccountsActivity.this,
//									"输入的密码长度有误,请输入6个数字、字母或特殊符号！");
							return;
						}
						


						TransferAccountTask task = new TransferAccountTask();

						task.execute(HttpUrls.TANSFER_ACCO + "", mymobile,
								truserphone, trusername, paypwd, "02",
								balance);
					}
				});

		
		doubleWarnDialog1.setCancelable(false);
		doubleWarnDialog1.setCanceledOnTouchOutside(false);
		doubleWarnDialog1.show();
//		transfer_confirm.setEnabled(false);
		
	}

	private void transferaccount() {
		
		
//		transfer_money.clearFocus();
		// etCardName.clearFocus();
		 balance = transfer_money.getText().toString();

		if (balance != null && !balance.equals("")) {
			float intBalance = Float.parseFloat(balance);
//			if (intBalance <= 1) {
//
//				ToastCustom.showMessage(this, "转账金额必须大于1.00元");
//				return;
//				
//			} else
				if (intBalance > 20000) {
				transfer_money.setText("");
				Toast.makeText(getApplicationContext(),"转账金额不能大于20000元,请重新输入",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(TransferAccountsActivity.this,
//						"转账金额不能大于20000元,请重新输入");
				transfer_money.setFocusable(true);
				transfer_money.setFocusableInTouchMode(true);
				return;
			}
			
		}
		
		if (balance != null && !balance.equals("")) {
			float intBalance = Float.parseFloat(balance);
			if (Float.parseFloat(treasureBean.getTotamt())/100 < intBalance) {

//				ToastCustom.showMessage(this, "充值金额不能小于1");
				transfer_money_error.setVisibility(View.VISIBLE);
				transfer_money_error.setText("余额不足");
				transfer_money_error.setError("余额不足");
				transfer_money.setVisibility(View.GONE);

				return;
			}
		}

		if (balance == null || (balance != null && balance.equals(""))) {
			transfer_money_error.setVisibility(View.VISIBLE);
			transfer_money_error.setText("请输入收款金额");
			transfer_money_error.setError("请输入收款金额");
			transfer_money.setVisibility(View.GONE);
			return;
		}
		if (!isNumber(balance)) {
			transfer_money_error.setVisibility(View.VISIBLE);
			transfer_money_error.setText("格式错误");
			transfer_money_error.setError("格式错误");
			transfer_money.setVisibility(View.GONE);
			return;
		} else {
			if (balance.substring(balance.length() - 1, balance.length())
					.equals(".")) {
				transfer_money_error.setVisibility(View.VISIBLE);
				transfer_money_error.setText("格式错误");
				transfer_money_error.setError("格式错误");
				transfer_money.setVisibility(View.GONE);
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
				}
			}
		}
		int a = (int) (Double.parseDouble(balance) * 100);
		balance = String.valueOf(a);
		
		//判断收款手机号
		final String touserphone = transfer_userphone.getText().toString();
		if (touserphone == null || (touserphone != null && touserphone.equals(""))) {
			transfer_phone_error.setVisibility(View.VISIBLE);
			transfer_phone_error.setText("请输入手机号码");
			transfer_userphone.setVisibility(View.GONE);
			relalay.setVisibility(View.GONE);
			return;
		} else if (!Checkingroutine.isNumeric(touserphone)) {
			transfer_phone_error.setVisibility(View.VISIBLE);
			transfer_phone_error.setText("只能输入数字");
			transfer_userphone.setVisibility(View.GONE);
			relalay.setVisibility(View.GONE);
			return;
		} else if (touserphone.length() != 11) {
			transfer_phone_error.setVisibility(View.VISIBLE);
			transfer_phone_error.setText("手机号码必须为11位数字");
			transfer_userphone.setVisibility(View.GONE);
			relalay.setVisibility(View.GONE);
			return;
		} else if (!isMobileNO(touserphone)) {
			transfer_phone_error.setVisibility(View.VISIBLE);
			transfer_phone_error.setText("手机号码格式有误");
			transfer_userphone.setVisibility(View.GONE);
			relalay.setVisibility(View.GONE);
			return;
		}
		
		//判断收款用户名
		final String tousername = transfer_username.getText().toString();
		if (tousername == null || (tousername != null && tousername.equals(""))) {
			transfer_username.setVisibility(View.GONE);
			transfer_username_error.setText("请输入用户姓名");
			transfer_username_error.setVisibility(View.VISIBLE);
			return;
		} else if (!ChineseUtil.checkNameChese(tousername)) {
			transfer_username.setVisibility(View.GONE);
			transfer_username_error.setText("用户名只能是中文");
			transfer_username_error.setVisibility(View.VISIBLE);
			return;
		}
		
//		//判断支付密码
//		String topayowd = transfer_usersaypwd.getText().toString();
//		if (topayowd == null || topayowd.equals("")) {
//			transfer_usersaypwd_error.setVisibility(View.VISIBLE);
//			transfer_usersaypwd_error.setText("请输入支付密码！");
//			transfer_usersaypwd.setVisibility(View.GONE);
//			return;
//		}else if (topayowd.length() < 6||topayowd.length() > 15) {
//			transfer_usersaypwd_error.setVisibility(View.VISIBLE);
//			transfer_usersaypwd_error.setText("输入的密码长度有误,请输入6～15个数字、字母或特殊符号！");
//			transfer_usersaypwd.setVisibility(View.GONE);
//			return;
//		}
		transfer_confirm.setEnabled(false);
		doubleWarnDialog1 = new MyEditDialog(TransferAccountsActivity.this,
				R.style.MyEditDialog, "转账", "请输入支付密码", "确认", "取消", balance,
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {

						switch (v.getId()) {
						case R.id.btn_right:
							doubleWarnDialog1.dismiss();
							closeinput();
//							InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
							break;
						case R.id.btn_left:
							String paypwd = doubleWarnDialog1.getpaypwd();

							if (paypwd == null || paypwd.equals("")) {
								Toast.makeText(getApplicationContext(),"请输入支付密码",
										Toast.LENGTH_SHORT).show();
//								ToastCustom.showMessage(
//										TransferAccountsActivity.this,
//										"请输入支付密码！");
								return;
							}
							if (paypwd.length() < 6 || paypwd.length() > 15) {
								Toast.makeText(getApplicationContext(),"输入的密码长度有误,请输入6～15个数字、字母或特殊符号",
										Toast.LENGTH_SHORT).show();
//								ToastCustom.showMessage(
//										TransferAccountsActivity.this,
//										"输入的密码长度有误,请输入6～15个数字、字母或特殊符号！");
								return;
							}

							TransferAccountTask task = new TransferAccountTask();

							task.execute(HttpUrls.TANSFER_ACCO + "", mymobile,
									touserphone, tousername, paypwd, "02",
									balance);
							break;
						default:
							break;
						}
						
						transfer_confirm.setEnabled(true);
					}
					
				},
				new onMyaddTextListener() {
					
					@Override
					public void refreshActivity(String paypwd) {
						// TODO Auto-generated method stub
						if (paypwd == null || paypwd.equals("")) {
							Toast.makeText(getApplicationContext(),"请输入支付密码",
									Toast.LENGTH_SHORT).show();
//							ToastCustom.showMessage(
//									TransferAccountsActivity.this,
//									"请输入支付密码！");
							return;
						}
						if (paypwd.length() < 6 || paypwd.length() > 15) {
							Toast.makeText(getApplicationContext(),"输入的密码长度有误,请输入6个数字、字母或特殊符号",
									Toast.LENGTH_SHORT).show();
//							ToastCustom.showMessage(
//									TransferAccountsActivity.this,
//									"输入的密码长度有误,请输入6个数字、字母或特殊符号！");
							return;
						}

						TransferAccountTask task = new TransferAccountTask();

						task.execute(HttpUrls.TANSFER_ACCO + "", mymobile,
								touserphone, tousername, paypwd, "02",
								balance);
					}
				});

		
		doubleWarnDialog1.setCancelable(false);
		doubleWarnDialog1.setCanceledOnTouchOutside(false);
		doubleWarnDialog1.show();
//		transfer_confirm.setEnabled(false);
		//进行交易

		
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
					
					double d = Double.parseDouble(treasureBean.getAvaamt());
					String r = String .format("%.2f",d/100);
					transfer_allmoney.setText(r);
					
					if(result.get("EPURSTFCOUNT")!=null){
						etu = result.get("EPURSTFCOUNT").toString();
					}
					if(result.get("EPURSTFSUMAMT")!=null){
						String etms = result.get("EPURSTFSUMAMT").toString();
						Upperlimit = Double.parseDouble(etms)/100;
						
					}
					tv_pro.setText("日转账金额累计不超过"+Upperlimit+"元，日转账上限"+etu+"次");
					
//					if (treasureBean.getLogsts().equals("1")) {
//						
//					} else if (treasureBean.getLogsts().equals("0")) {
//						loadingDialogWhole.dismiss();
//						ToastCustom
//								.showMessage(TransferAccountsActivity.this, "账户暂未开通该功能!");
//					} else {
//						loadingDialogWhole.dismiss();
//						ToastCustom.showMessage(TransferAccountsActivity.this, "账户暂未开通该功能!"
//								+ treasureBean.getLogsts());
//					}
				} else {
					loadingDialogWhole.dismiss();
					
					warnDialog = new OneButtonDialogWarn(TransferAccountsActivity.this,
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
//					ToastCustom.showMessage(RichTreasureActivity.this,
//							result.get(Entity.RSPMSG).toString());
				}
			} else {
				Toast.makeText(getApplicationContext(),"数据获取失败,请检查网络连接",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(TransferAccountsActivity.this, "数据获取失败,请检查网络连接");
				loadingDialogWhole.dismiss();
			}
			super.onPostExecute(result);
			
				
		}
		
	}
	
	

	/**
	 * 转账task
	 * 
	 * @author
	 * 
	 */
	class TransferAccountTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog("请稍等...");
		
			super.onPreExecute();
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3],
					params[4] ,params[5], params[6]};
			// 198110,
			return NetCommunicate.getMidatc(HttpUrls.TANSFER_ACCO, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
//					ToastCustom.showMessage(TransferAccountsActivity.this,result.get(Entity.RSPMSG).toString());
					Intent it= new Intent(TransferAccountsActivity.this,MentionNowAcitvity.class);
					
					it.putExtra("State", 3);
					it.putExtra("res", result.get(Entity.RSPMSG).toString());
					startActivity(it);
					finish();
				}else if(result.get(Entity.RSPCOD).equals("000088")){
					doubleWarnDialog1.dismiss();
					Toast.makeText(getApplicationContext(),result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(TransferAccountsActivity.this,result.get(Entity.RSPMSG).toString());
				} else {
					doubleWarnDialog1.dismiss();
//					showSingleWarnDialog(result.get(Entity.RSPMSG).toString());
					doubleWarnDialog1.setpaypwd();
					Intent it= new Intent(TransferAccountsActivity.this,MentionNowAcitvity.class);
					it.putExtra("State", 4);
					it.putExtra("res", result.get(Entity.RSPMSG).toString());
					startActivity(it);
//					transfer_confirm.setEnabled(true);
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
			load.setVisibility(View.VISIBLE);
			load_img.setVisibility(View.GONE);
			
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
					if(result.get("ACTNAM")!=null){
						transfer_username.setText(result.get("ACTNAM").toString());
						if(transfer_username_error.getVisibility()==View.VISIBLE){
							transfer_username.setVisibility(View.VISIBLE);
							transfer_username_error.setVisibility(View.GONE);
						}
					}else{
						transfer_username.setText("");
					}
				} else {
					load.setVisibility(View.GONE);
					load_img.setVisibility(View.GONE);
					transfer_phone_error.setVisibility(View.VISIBLE);
					transfer_phone_error.setText("号码有误,对方账户不存在!");
					transfer_userphone.setVisibility(View.GONE);
					transfer_userphone.setText("");
					relalay.setVisibility(View.GONE);
					transfer_username.setText("");

				}
				super.onPostExecute(result);
			}else{
				Toast.makeText(getApplicationContext(),"获取收款人信息失败",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(TransferAccountsActivity.this, "获取收款人信息失败");
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if(!tag.equals("")){
				SpannableString msp = new SpannableString("取消本次交易?");
				showDoubleWarnDialog(msp);
			}else{
				finish();
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
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
			finish();
			
			break;

		default:
			break;
		}
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
                    
                    
//                    Pattern p = Pattern.compile("\\s*|\t|\r|\n|-");  
//                    Matcher m = p.matcher(usernumber);  
//                    String dest = m.replaceAll("");  
//                    usernumber = usernumber.replace("-","");  
                    transfer_userphone.setText(ConnUtil.format(usernumber));
                }

                
                
//              int  contactName = result.getPhoneContacts(contactId);
//				contactId = result.getLastPathSegment();
//				contactId = result.getQueryParameters(key)
//				
//				contactName = getPhoneContacts(contactId);

			}
		}
	}
}
