package com.td.qianhai.epay.oem;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.broadcast.DownloadService;
import com.td.qianhai.epay.oem.interfaces.DownloadListener;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.push.Utils;
import com.td.qianhai.epay.oem.thread.VersionUpdateVerifyThread;
import com.td.qianhai.epay.oem.unlock.StringUtil;
import com.td.qianhai.epay.oem.unlock.UnlockLoginActivity;
import com.td.qianhai.epay.oem.views.SystemBarTintManager;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.oem.views.dialog.DownloadDialog;
import com.td.qianhai.epay.oem.views.dialog.DownloanDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonImgDialog;
import com.td.qianhai.epay.oem.views.dialog.TwoButtonDialog;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnIsDownloadClickListener;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.epay.utils.AppManager;
import com.td.qianhai.epay.utils.Common;
import com.td.qianhai.epay.utils.CustomUtil;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * 主程序第一个界面
 * 
 * @author liangge
 * 
 */
public class MainActivity extends BaseActivity1 {

	public static String psamIdqd;
	/* 终端号码、pcsim */
	private String psamId, pcsim;
	/* 下载的url */
	private String apkUrl;
	/* 进度条 */
	private ProgressBar mProgress;
	/* 进度值 */
	private int progress;
	/* 下载线程 */
	private Thread downLoadThread;
	public static int flag;
	/* 下载更新开始 */
	private static final int DOWN_UPDATE = 1;
	/* 更新结束 */
	private static final int DOWN_OVER = 2;
	private String isForce = null;

	private String localPsam;

	private int isDownload = 0;

	/* 工具类CustomUtil对象 */
	private CustomUtil customUtil;
	/* 主密钥解密key */
	private String decryptionKey;

	/** 体验帐号、登录、注册 */
	private Button bt_accountLogin, bt_login, bt_register;
	/* 是否继续连接、双按钮更新提示 */
	private TwoButtonDialog twoButtonDialog, downloadDialog;

	private Intent intent;
	private Bundle bundle;
	/* 错误dialog */
	private OneButtonImgDialog erorDialog;

	private VersionUpdateVerifyThread taskVersionUpdateThread,
			taskVersionUpdateThreadOnRestart;

	private String versionSerial;
	private DownloadService servcie;
	private DownloanDialog downloanDialog;
	
	private DownloadDialog downloanDialog1;

	private String fileName;
	
	private Editor editor;
	
	private SharedPreferences share;
	
	public static String idd;
	
//	public static MyCacheUtil cache;

	/* 判断是否第一次进入onPause */
	private int isOnPause = 0;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case DOWN_UPDATE:// 下载进度条更新
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:// 更新完毕
				ToastCustom.showMessage(MainActivity.this, "下载完毕", 2000);
				isDownload = 2;
				installApk();
				break;
			case 8:
				initResume();
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void loadingCloseDialogOnClick(View v) {
		// TODO Auto-generated method stub
		super.loadingCloseDialogOnClick(v);
		if (v.getId() == R.id.close) {
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
			// loadingDialogClose.dismiss();
			AppManager.getAppManager().AppExit(MainActivity.this);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//	     getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,  
//                   WindowManager.LayoutParams. FLAG_FULLSCREEN);  
		setContentView(R.layout.main);
		editor = MyCacheUtil.setshared(MainActivity.this);
		share = MyCacheUtil.getshared(MainActivity.this);
//			cache = new MyCacheUtil(this);
		
//        MyGifView2 gif1 = (MyGifView2) findViewById(R.id.mygif);  
        // 设置背景gif图片资源  
//        gif1.setMovieResource(R.drawable.first_interface);  
		// initView();
		// showLoadingCloseDialog("正在加载中。。。");
		
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			setTranslucentStatus(true);
//		}

//		SystemBarTintManager tintManager = new SystemBarTintManager(this);
//		tintManager.setStatusBarTintEnabled(false);ss
		
		init();
//        PushManager.startWork(getApplicationContext(),
//                PushConstants.LOGIN_TYPE_API_KEY,
//                Utils.getMetaValue(MainActivity.this, "api_key"));
        
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		StringBuilder sb1 = new StringBuilder();
		sb1.append(tm.getDeviceId());
		 idd = sb1.toString();
		Set<String> Products = new HashSet<String>();
		Products.add(idd);
		JPushInterface.setAliasAndTags(this, idd, Products);
		editor.putString("IDD",idd);
		editor.commit();
        
        AnalyticsConfig.setChannel("erma");
        settitle();
        

//        PushManager.startWork(getApplicationContext(),
//                PushConstants.LOGIN_TYPE_API_KEY,
//                Utils.getMetaValue(MainActivity.this, "api_key"));
		// Test

//		File dir = new File(Environment.getExternalStorageDirectory()
//				.getAbsolutePath() + "/DCIM/");// 设置存放目录
//		File f = new File(dir.getAbsoluteFile(), "test.jpg");
//		BitmapFactory.Options opts = new BitmapFactory.Options();// 获取缩略图显示到屏幕
//		opts.inSampleSize = 15;
//		Bitmap cbitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
//		String idFrontPic = BitmapUtil.Bitmap2String(cbitmap);
//		outPutStreamTest("imgStr3.txt", idFrontPic);
	}
	
//	private void outPutStreamTest(String fileName,String str) {
//
//		try {
//			FileOutputStream os = new FileOutputStream(
//					Environment.getExternalStorageDirectory().getAbsolutePath()
//							+ "/DCIM/"+fileName, true);
//			os.write(str.getBytes());
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

	
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
	/**
	 * 初始化
	 */
	private void init() {
		
//		TextView tt = (TextView) findViewById(R.id.mian_text);
//		AnimationUtil.ScaleAnimations(tt);
		versionSerial = ((AppContext) getApplication()).getVersionSerial();
		System.out.println(isDownload);
		// if (isDownload == 0)
		// System.out.println(versionSerial == null);
		// 判断是否有网络
		if (Common.isNetworkConnected(this)) {

			// 版本更新检测
			if (taskVersionUpdateThread == null) {
				taskVersionUpdateThread = new VersionUpdateVerifyThread(
						HttpUrls.VERSION_UPDATE + "", HttpUrls.APPNAME);
			}
			taskVersionUpdateThread.start();
			try {
				// 主线程等待子线程执行完毕后执行
				System.out.println("正在等待中。。。");
				taskVersionUpdateThread.join();
				System.out.println("线程执行完毕。。。");
				VersionUpdateDetectionWin(taskVersionUpdateThread.result);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			taskVersionUpdateThread = null;
			// 动画播放结束后跳转首页
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					if (flag == 1) {
						// 检测成功
						// loadingDialogClose.dismiss();
						if(!StringUtil.isEmpty(share.getString("usermobile",""))){
//							if(share.getBoolean("isfirst",false)){
							if(MyCacheUtil.getshared(MainActivity.this).getString("isgesture","").equals("0")){
								Intent intent = new Intent(MainActivity.this,
										UserActivity.class);
								startActivity(intent);
	
							}else{
								Intent intent = new Intent(MainActivity.this,
										UnlockLoginActivity.class);
								startActivity(intent);
							}
	
//								}else{
//									Intent intent = new Intent(MainActivity.this,
//											WelcomeActivity.class);
//									intent.putExtra("type", "1");
//									startActivity(intent);

//								}

//							UnlockLoginActivity
//							if(cache.getString("refresh")!=null&&cache.getString("refresh").equals("refresh")){
//								intent.putExtra("refresh", "refresh");
//							}

							finish();
						}else{
//							if(share.getBoolean("isfirst",false)){
								Intent intent = new Intent(MainActivity.this,
										UserActivity.class);
								startActivity(intent);
//							}else{
//								Intent intent = new Intent(MainActivity.this,
//										WelcomeActivity.class);
//								intent.putExtra("type", "0");
//								startActivity(intent);
//							}

							finish();
						}
					}
				}
			};
			// 延迟2秒进入
			timer.schedule(task, 2000);
		}else{
			initNetWorkDialog();
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 JPushInterface.onPause(this);
		MobclickAgent.onPause(this);
		if (servcie != null)
			servcie.isPause = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		 JPushInterface.onResume(this);
		MobclickAgent.onResume(this);
		if (isOnPause == 1)
			mHandler.sendEmptyMessage(8);
		isOnPause = 1;
	}

	private void initResume() {
//		versionSerial = ((AppContext) getApplication()).getVersionSerial();
//		if (isDownload == 0) {
//			if (versionSerial == null) {
//				System.out.println("onRestart");
//				/* onRestart状态下检测 */
//				taskVersionUpdateThreadOnRestart = new VersionUpdateVerifyThread(
//						HttpUrls.VERSION_UPDATE + "", HttpUrls.APPNAME);
//				taskVersionUpdateThreadOnRestart.start();
//				try {
//					taskVersionUpdateThreadOnRestart.join();
//					VersionUpdateDatectionWinOnRestart(taskVersionUpdateThreadOnRestart.result);
//				} catch (InterruptedException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				taskVersionUpdateThreadOnRestart = null;
//				if (isForce == null) {
//					// null重新获取
//					// 版本更新检测
//					if (taskVersionUpdateThread == null) {
//						taskVersionUpdateThread = new VersionUpdateVerifyThread(
//								HttpUrls.VERSION_UPDATE + "", HttpUrls.APPNAME);
//					}
//					taskVersionUpdateThread.start();
//					try {
//						// 主线程等待子线程执行完毕后执行
//						taskVersionUpdateThread.join();
//						VersionUpdateDetectionWin(taskVersionUpdateThread.result);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					taskVersionUpdateThread = null;
//				}
//				// 动画播放结束后跳转首页
//				Timer timer = new Timer();
//				TimerTask task = new TimerTask() {
//
//					@Override
//					public void run() {
//						System.out.println("flag:" + flag);
//						if (flag == 1) {
////							Intent intent = new Intent(MainActivity.this,
////									UnlockLoginActivity.class);
////							startActivity(intent);
//							finish();
//						}
//					}
//				};
//				timer.schedule(task, 2000);
//
//			}
//		} else if (isDownload == 2) {
//			((AppContext) getApplication()).setCustId(null); // 商户ID赋为空
//			((AppContext) getApplication()).setPsamId(null);
//			((AppContext) getApplication()).setMacKey(null);
//			((AppContext) getApplication()).setPinKey(null);
//			((AppContext) getApplication()).setMerSts(null);
//			((AppContext) getApplication()).setMobile(null);
//			((AppContext) getApplication()).setEncryPtkey(null);
//			((AppContext) getApplication()).setStatus(null);
//			((AppContext) getApplication()).setCustPass(null);
//			((AppContext) getApplication()).setVersionSerial(null);
//			// loadingDialogClose.dismiss();
//			AppManager.getAppManager().AppExit(MainActivity.this);
//		} else {
//			startDowApk();
//		}
	}

	private void startDowApk() {
		Log.e("", "更新线程");
		downloanDialog = new DownloanDialog(MainActivity.this,
				R.style.CustomDialog, "程序更新", mHandler);
		downloanDialog.setCancelable(false);
		downloanDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
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
		downloanDialog.setCanceledOnTouchOutside(false);
		if (downloadDialog == null || !downloadDialog.isShowing()) {
			downloanDialog.show();
		}
		isDownload = 1;
		DownloadTask task;
		try {
			System.out.println(apkUrl);
			task = new DownloadTask(apkUrl);
			servcie.isPause = false;
			new Thread(task).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final class DownloadTask implements Runnable {

		public DownloadTask(String target) throws Exception {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File destination = Environment.getExternalStorageDirectory();
				servcie = new DownloadService(target, destination, 3,
						getApplicationContext());
				servcie.thread.join();
				fileName = servcie.fileName;
				Log.e("Main", servcie.fileSize + "");
				downloanDialog.progressBar.setMax(servcie.fileSize);
			} else {
				Toast.makeText(getApplicationContext(), "SD卡不存在或写保护!",
						Toast.LENGTH_LONG).show();
			}
		}

		public void run() {
			try {
				servcie.download(new DownloadListener() {

					public void onDownload(int downloaded_size) {
						Message message = new Message();
						message.what = 1;
						message.getData().putInt("size", downloaded_size);
						downloanDialog.handler.sendMessage(message);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 软件下载dialog
	 */
	private void showDownloadDialog() {
//		AlertDialog.Builder builder = new Builder(MainActivity.this);
//		builder.setTitle("软件版本更新");
//		builder.setMessage("正在下载请稍后");
//		final LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
//		View v = inflater.inflate(R.layout.progress_version_update, null);
//		mProgress = (ProgressBar) v
//				.findViewById(R.id.progress_version_update_pb);
//		builder.setView(v);
//		// downloadDialog = builder.create();
//		Dialog dialog = builder.create();
//		dialog.setCancelable(false);
//		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode,
//					KeyEvent event) {
//				// TODO Auto-generated method stub
//				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
//					return true;
//				} else {
//					return true; // 默认返回 false
//				}
//			}
//		});
//		dialog.setCanceledOnTouchOutside(false);
//		dialog.show();
		// 下载apk安装包
		// downloadApk();
		
		downloanDialog1 = new DownloadDialog(MainActivity.this,
				apkUrl, "程序更新", new OnIsDownloadClickListener() {

					@Override
					public void isDownload(String fileString, int DOWN_OVER) {
						// TODO Auto-generated method stub
						mHandler.sendEmptyMessage(DOWN_OVER);
						fileName = fileString;
					}
				});

		downloanDialog1.setCancelable(false);
		downloanDialog1.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				} else {
					return true; // 默认返回 false
				}
			}
		});
		downloanDialog1.setCanceledOnTouchOutside(false);
		downloanDialog1.show();
		// 下载apk安装包
		isDownload = 1;
		// downloadApk();
	}

	/**
	 * 安装APK
	 */
	@SuppressLint("SdCardPath")
	private void installApk() {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.fromFile(new File("/sdcard/" + fileName)),
				"application/vnd.android.package-archive");
		finish();
		MainActivity.this.startActivity(i);
	}

	private void VersionUpdateDatectionWinOnRestart(
			HashMap<String, Object> result) {
		if (result != null) {
			if (Entity.STATE_OK.equals(result.get(Entity.RSPCOD))) {
				int versionCode = Integer.parseInt(result.get("VERCODE")
						.toString());
				System.out.println("getVersion()" + getVersion());
				if (getVersion() < versionCode) {
					if (android.os.Environment.MEDIA_MOUNTED
							.equals(android.os.Environment
									.getExternalStorageState())) {
						apkUrl = result.get("APKRUL").toString();
					}
					flag = -1;
				} else {
					((AppContext) getApplication())
							.setVersionSerial(getVersion() + "");
					flag = 1;
				}
			} else {
				errordialog();
			}
		} else {
			initNetWorkDialog();
		}
	}

	/**
	 * 版本更新检测成功判断
	 */
	private void VersionUpdateDetectionWin(HashMap<String, Object> result) {
		if (result != null) {
			if (Entity.STATE_OK.equals(result.get(Entity.RSPCOD))) {
				
				int versionCode = Integer.parseInt(result.get("VERCODE")
						.toString());
				if (getVersion() < versionCode) {
					if (android.os.Environment.MEDIA_MOUNTED
							.equals(android.os.Environment
									.getExternalStorageState())) {
						if (result.get("ISFORCE") == null) {
							isForce = "0";
						} else {
							isForce = result.get("ISFORCE").toString();
						}
						if(result.get("APKRUL")!=null){
							apkUrl = result.get("APKRUL").toString();
						}
						
						if (isForce.equals("0")) {
							// 非强制更新
							showUpdateNoticeDialog();
							return;
						} else if (isForce.equals("1")) {
							// 强制更新
							 showDownloadDialog();
//							startDowApk();
							 return;
						}
					}
				} else {
					((AppContext) getApplication())
							.setVersionSerial(getVersion() + "");
					// 版本一致不更新
					flag = 1;
				}
			} else {
				// 连接服务器失败
				if(result.get(Entity.RSPCOD)!=null&&result.get(Entity.RSPCOD).equals("000002")){
					flag = 1;
				}else{
					if(flag == 1){
						
					}else{
						Intent it  =  new Intent(MainActivity.this,UserActivity.class);
						startActivity(it);
					}

//					errordialog();
				}
				
				
			}
		} else {
			// 没有获取到值显示网络连接设置
//			initNetWorkDialog();
//			Intent it  =  new Intent(MainActivity.this,UserActivity.class);
//			startActivity(it);
			errordialog();
		}
	}

	/**
	 * 获取版本号
	 * 
	 * @return
	 */
	private int getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			int versionCode = info.versionCode;
			return versionCode;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 显示非强制更新的dialog
	 */
	private void showUpdateNoticeDialog() {
		downloadDialog = new TwoButtonDialog(this, R.style.CustomDialog,
				"软件版本更新", "有最新的软件包哦，亲快下载吧~", "确认更新", "下次再说",
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							showDownloadDialog();
							break;
						case R.id.Button_cancel:
							downloadDialog.dismiss();
							break;
						default:
							break;
						}
					}
				});

		downloadDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
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
		downloadDialog.setCanceledOnTouchOutside(false);
		downloadDialog.show();
	}

	/**
	 * 错误的dialog
	 */
	public void errordialog() {
		erorDialog = new OneButtonImgDialog(this, R.style.CustomDialog, "错误",
				"服务器繁忙,请稍后重试!", "确认",
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						switch (v.getId()) {
						case R.id.Button_OK:
							((AppContext) getApplicationContext())
									.setCustId(null); // 商户ID赋为空
							((AppContext) getApplicationContext())
									.setPsamId(null);
							((AppContext) getApplicationContext())
									.setMacKey(null);
							((AppContext) getApplicationContext())
									.setPinKey(null);
							((AppContext) getApplicationContext())
									.setMobile(null);
							((AppContext) getApplicationContext())
									.setOpenNetwork(null);
							erorDialog.dismiss();
							finish();
							AppManager.getAppManager().AppExit(
									getApplicationContext());
							break;

						default:
							break;
						}
					}
				}, R.drawable.redalert);
		erorDialog.setCancelable(false);
		erorDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
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
		erorDialog.setCanceledOnTouchOutside(false);
		erorDialog.show();
	}
	
	
	/**
	 * 无网络弹出的dialog
	 */
	public void initNetWorkDialog() {
		
//		if(){
//			
//		}else{
//			
//		}
//		if (((AppContext) getApplicationContext()).getOpenNetwork() == null) {
//			((AppContext) getApplicationContext()).setOpenNetwork("open");
			Intent intent = new Intent(this, NetworkRemindDialogActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("name", this.getClass().getName());
			intent.putExtras(bundle);
			startActivity(intent);
//		}
	}
	
	
}
