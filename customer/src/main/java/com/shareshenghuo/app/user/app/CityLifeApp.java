package com.shareshenghuo.app.user.app;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

import com.shareshenghuo.app.user.BuildConfig;
import com.shareshenghuo.app.user.manager.IMManager;
import com.sensetime.service.STService;
import com.tencent.bugly.crashreport.CrashReport;

public class  CityLifeApp extends MultiDexApplication {

	public static Context applicationContext;
	private static CityLifeApp instance;
	private static final String APP_ID = "b329ed69d52b4ff396622f11f1dfd4b8";
	private static final String APP_SECRET = "ca54559bd0b741d99b8e831c5a7ae277";
	private List<Activity> activityList = new LinkedList<Activity>();

	public static CityLifeApp getInstance() {
		return instance;
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
//		MultiDex.install(this);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		applicationContext = this;
		instance = this;
		ShareSDK.initSDK(this);
		CrashReport.initCrashReport(getApplicationContext(), "3f23cfd059", BuildConfig.DEBUG);
		// CrashHandler.getInstance().init(this);
		initJPush();
		initIM();
		STService.getInstance(instance).activateInBackground(APP_ID, APP_SECRET);
	}

	private void initJPush() {
		JPushInterface.setDebugMode(false);
		JPushInterface.init(this);
	}

	// private static AppContext myApplication;
	//
	// public synchronized static AppContext getInstance() {
	//
	// return myApplication;
	// }

	/**
	 * 添加Activity到列表中
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	/**
	 * 退出应用程序
	 */
	public void exit() {

		for (Activity activity : activityList) {
			if (activity != null) {
				activity.finish();
			}
		}
		activityList.clear();
	}

	private void initIM() {
		String processAppName = getAppName(android.os.Process.myPid());
		// 如果app启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
		// 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process
		// name就立即返回
		if (processAppName != null
				&& processAppName.equalsIgnoreCase("com.shareshenghuo.app.user")) {
			IMManager.getInstance().init(this);
		}
	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm
							.getApplicationInfo(info.processName,
									PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}
}
