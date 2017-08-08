package com.td.qianhai.epay.oem;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.views.SystemBarTintManager;
import com.td.qianhai.epay.oem.views.dialog.LoadingDialog;
import com.td.qianhai.epay.oem.views.dialog.LoadingDialogWhole;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.TwoButtonDialogStyle2;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.epay.utils.AppManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 应用程序Activity的基类
 * 
 */
public class BaseActivity extends Activity {
	/** 记录日志的标记. */
//	private String TAG = BaseActivity.class.getSimpleName();
	/** 保存主密钥的名字 */
	protected final String BCTPAY_KEYS = "bctpaykeys";
	/** 主密钥 */
	protected final String MIAN_KEY = "miankey";
	/** 网络监听IntentFilter对象 */
	protected IntentFilter filter;
	/** 当前的Context */
	protected Context nowContext;
	/** 当前Activity */
	protected Activity activity = this;
	/** 无按钮的进度dialog */
	protected LoadingDialogWhole loadingDialogWhole;
	/** 单个按钮提示dialog */
	protected OneButtonDialogWarn singlewWarnDialog;
	/** 双按钮提示dialog */
	protected TwoButtonDialogStyle2 doubleWarnDialog;
	/** 旋转按钮带关闭按钮dialog */
	protected LoadingDialog loadingDialogClose;
	/** 计数器 */
	protected int timeCount;

	protected boolean timeFlag;
	
//	protected ScreenBroadcastReceiver mScreenBroadcastReceiver;

	/**
	 * 基Handler
	 */
	@SuppressLint("HandlerLeak")
	protected Handler baseHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				// 初始化一个自定义的Dialog
				initNetWorkDialog();
				break;
			case 2:
				System.out.println("超时");
				((AppContext) getApplication()).setCustId(null); // 商户ID赋为空
				((AppContext) getApplication()).setPsamId(null);
				((AppContext) getApplication()).setMacKey(null);
				((AppContext) getApplication()).setPinKey(null);
				((AppContext) getApplication()).setMerSts(null);
				((AppContext) getApplication()).setMobile(null);
				((AppContext) getApplication()).setEncryPtkey(null);
				((AppContext) getApplication()).setStatus(null);
				((AppContext) getApplication()).setCustPass(null);
				((AppContext) getApplication()).setVersionSerial(null);
				AppManager.getAppManager().AppExit(activity);
				break;

			default:
				break;
			}
		}
	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// 获取当前的Context
		nowContext = this;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.bg_white);
 
		// 网络监听
//		filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
//		
//		this.registerReceiver(broadcastReceiver, filter);
//		// wifi信号监听
//		this.registerReceiver(rssiReceiver, new IntentFilter(
//				WifiManager.RSSI_CHANGED_ACTION));
		
//		startScreenBroadcastReceiver();
		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
	}
	
	protected  void settitle() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(false);
		tintManager.setStatusBarTintResource(R.color.bg_white);
//		SystemBarTintManager tintManager = new SystemBarTintManager(this);
//		tintManager.setStatusBarTintEnabled(true);
//		tintManager.setStatusBarTintResource(R.color.apptitle);
		
	}
	
	@TargetApi(19) 
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		// 关闭没有结束的广播
//		if (filter != null) {
//			nowContext.unregisterReceiver(broadcastReceiver);
//		}
//		nowContext.unregisterReceiver(rssiReceiver);
		timeFlag = false;
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

//	public class ScreenBroadcastReceiver extends BroadcastReceiver{
//		 private String action = null;
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// TODO Auto-generated method stub
//			
//	        action = intent.getAction();
//	        if (Intent.ACTION_SCREEN_ON.equals(action)) {  
//	        	
//	        	Log.e("", "1");
//	            // 开屏
//	        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { 
//	        	Log.e("", "2");
//	            // 锁屏
//	        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
//	        	Log.e("", "3");
//	            // 解锁
//	        }
//			
//		}
//	}
	
	
//	/**
//	 * 网络广播监听器
//	 */
//	protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			ConnectivityManager connectivityManager = (ConnectivityManager) context
//					.getSystemService(Context.CONNECTIVITY_SERVICE);
//			NetworkInfo activeNetInfo = connectivityManager
//					.getActiveNetworkInfo();
//			NetworkInfo mobNetInfo = connectivityManager
//					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//			if ((activeNetInfo != null && activeNetInfo.isAvailable())
//					|| (mobNetInfo != null && mobNetInfo.isAvailable())) {
//				((AppContext) getApplication()).setIsNetworkConn(1);
//			} else {
//				System.out.println("进入到了无网络");
//				((AppContext) getApplication()).setIsNetworkConn(-1);
//				Message message = Message.obtain(baseHandler);
//				message.what = 1;
//				message.obj = "网络异常";
//				message.sendToTarget();
//			}
//		}
//	};
	
	
	
	
	
//	/**
//	 * 监听黑屏
//	 * @author Administrator
//	 *
//	 */
//	private void startScreenBroadcastReceiver() {
//		 mScreenBroadcastReceiver= new ScreenBroadcastReceiver();
//	    IntentFilter filter = new IntentFilter();
//	    filter.addAction(Intent.ACTION_SCREEN_ON);
//	    filter.addAction(Intent.ACTION_SCREEN_OFF);
//	    filter.addAction(Intent.ACTION_USER_PRESENT);
//	    this.registerReceiver(mScreenBroadcastReceiver, filter);
//	}

	/**
	 * 无网络弹出的dialog
	 */
	public void initNetWorkDialog() {
		if (((AppContext) getApplicationContext()).getOpenNetwork() == null) {
			System.out.println("进入到无网络跳转");
			((AppContext) getApplicationContext()).setOpenNetwork("open");
			Intent intent = new Intent(this, NetworkRemindDialogActivity.class);
			Bundle bundle = new Bundle();
			System.out.println(this.getClass().getName());
			bundle.putString("name", this.getClass().getName());
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}
	
    public  boolean checkEditEmpty(EditText v, String msg) {
        if(TextUtils.isEmpty(v.getText())) {
        	Texterro(v, msg);
            return true;
        } else {
            return false;
        }
    }
	
	public void Texterro(TextView v ,String srt){
		v.requestFocus();
		v.setText("");
		v.setHint(Html.fromHtml("<font color=#B2001F>" + srt + "</font>"));
		
		
	}
	public void Editerro(EditText v ,String srt){
		v.setText("");
		v.setHint(Html.fromHtml("<font color=#B2001F>" + srt + "</font>"));
		
	}
	
	
//
//	/**
//	 * wifi信号监听器
//	 */
//	public BroadcastReceiver rssiReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			// Wifi的连接速度及信号强度：
//			WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
//			// WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//			WifiInfo info = wifiManager.getConnectionInfo();
//			if (info.getBSSID() != null) {
//				// 链接信号强度
//				int strength = WifiManager.calculateSignalLevel(info.getRssi(),
//						5);
//				// 链接速度
//				int speed = info.getLinkSpeed();
//				// 链接速度单位
//				// String units = WifiInfo.LINK_SPEED_UNITS;
//				// Wifi源名称
//				// String ssid = info.getSSID();
//				if (strength < 2) {
//					ToastCustom.showMessage(getApplicationContext(), speed
//							+ "当前网络信号强度不太好" + strength);
//				}
//			}
//		}
//	};

	/**
	 * 先加载数据显示dialog
	 * 
	 * @param msg
	 */
	protected void showLoadingDialog(String msg) {
		loadingDialogWhole = new LoadingDialogWhole(this, R.style.CustomDialog,
				msg);
		loadingDialogWhole.setCancelable(false);
		loadingDialogWhole
				.setOnKeyListener(new DialogInterface.OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_SEARCH) {
							return true;
						} else {
							return true; // 默认返回 false
						}
					}
				});
		loadingDialogWhole.setCanceledOnTouchOutside(false);
		loadingDialogWhole.show();
	}

	protected void dismissLoadingDialog(){
		loadingDialogWhole.dismiss();
	}

	/***
	 * 单个提示dialog
	 * 
	 * @param msg
	 *            内容
	 */
	protected void showSingleWarnDialog(String msg) {
		singlewWarnDialog = new OneButtonDialogWarn(this, R.style.CustomDialog,
				"提示", msg, "确定", new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						singleWarnOnClick(v);
					}
				});
		singlewWarnDialog.setCancelable(false);
		singlewWarnDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else {
					return true; // 默认返回 false
				}
			}
		});
		singlewWarnDialog.setCanceledOnTouchOutside(false);
		singlewWarnDialog.show();
	}

	/**
	 * 
	 * @param prompt
	 *            提示
	 * @param msg
	 *            显示类容
	 * @param btStr
	 *            按钮名称
	 */
	protected void showSingleWarnDialog(String prompt, String msg, String btStr) {
		singlewWarnDialog = new OneButtonDialogWarn(this, R.style.CustomDialog,
				prompt, msg, btStr, new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						singleWarnOnClick(v);
					}
				});
		singlewWarnDialog.setCancelable(false);
		singlewWarnDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else {
					return true; // 默认返回 false
				}
			}
		});
		singlewWarnDialog.setCanceledOnTouchOutside(false);
		singlewWarnDialog.show();
	}

	/***
	 * 单个dialog操作
	 * 
	 * @param v
	 *            要操作的View
	 */
	protected void singleWarnOnClick(View v) {
		singlewWarnDialog.dismiss();
	}

	/**
	 * 双按钮dialog显示
	 * 
	 * @param prompt
	 *            提示
	 * @param msg
	 *            显示的内容
	 * @param affirmStr
	 *            按钮一名称
	 * @param cancelStr
	 *            按钮二名称
	 */
	protected void showDoubleWarnDialog(String prompt, SpannableString msg,
			String affirmStr, String cancelStr) {
		doubleWarnDialog = new TwoButtonDialogStyle2(this,
				R.style.CustomDialog, prompt, msg, affirmStr, cancelStr,
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doubleWarnOnClick(v);
					}
				});
		doubleWarnDialog.setCancelable(false);
		doubleWarnDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else {
					return true; // 默认返回 false
				}
			}
		});
		doubleWarnDialog.setCanceledOnTouchOutside(false);
		doubleWarnDialog.show();
	}

	/***
	 * 双按钮提示dialog
	 * 
	 * @param msg
	 */
	protected void showDoubleWarnDialog(SpannableString msg) {
		doubleWarnDialog = new TwoButtonDialogStyle2(this,
				R.style.CustomDialog, "提示", msg, "确认", "取消",
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doubleWarnOnClick(v);
					}
				});
		doubleWarnDialog.setCancelable(false);
		doubleWarnDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else {
					return true; // 默认返回 false
				}
			}
		});
		doubleWarnDialog.setCanceledOnTouchOutside(false);
		doubleWarnDialog.show();
	}

	/***
	 * 双按钮提示dialog
	 * 
	 * @param v
	 */
	protected void doubleWarnOnClick(View v) {
		switch (v.getId()) {
		case R.id.btn_right:
			doubleWarnDialog.dismiss();
			break;
		default:
			break;
		}
	}
	
	
	protected Editor getedit(Context context) {
		
		return MyCacheUtil.setshared(context);
		
	}
	
	protected SharedPreferences getshare(Context context) {
		
		return MyCacheUtil.getshared(context);
		
	}

	/**
	 * 加载式dialog带关闭按钮
	 * 
	 * @param msg
	 */
	protected void showLoadingCloseDialog(String msg) {
		loadingDialogClose = new LoadingDialog(this, R.style.CustomDialog, msg,
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						loadingCloseDialogOnClick(v);
					}
				});
		loadingDialogClose.setCancelable(false);
		loadingDialogClose
				.setOnKeyListener(new DialogInterface.OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_SEARCH) {
							return true;
						} else {
							return true; // 默认返回 false
						}
					}
				});
		loadingDialogClose.setCanceledOnTouchOutside(false);
		loadingDialogClose.show();
	}

	/**
	 * 加载式dialog带关闭按钮操作方法
	 * 
	 * @param v
	 *            要操作的View
	 */
	protected void loadingCloseDialogOnClick(View v) {
		loadingDialogClose.dismiss();
	}

	protected <V extends View> V getView(int id) {
        return (V) findViewById(id);
    }

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected Activity getActivity(){
		return this;
	}

	/**
	 * 验证手机合法性
	 *
	 * @param mobiles
	 * @return
	 */
	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(17[0-9])|(14[0-9])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
}
