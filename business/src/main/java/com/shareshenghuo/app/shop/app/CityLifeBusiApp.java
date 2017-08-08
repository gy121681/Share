package com.shareshenghuo.app.shop.app;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import cn.jpush.android.api.JPushInterface;

import com.shareshenghuo.app.shop.manager.CrashHandler;
import com.shareshenghuo.app.shop.manager.IMManager;

public class CityLifeBusiApp extends Application {
	
	public static Context applicationContext;
	private static CityLifeBusiApp instance;
	
	public static CityLifeBusiApp getInstance() {
		return instance;
	}
@Override
protected void attachBaseContext(Context base) {
	super.attachBaseContext(base);
	MultiDex.install(this);
}
	@Override
	public void onCreate() {
		super.onCreate();
        applicationContext = this;
        instance = this;
//        CrashHandler.getInstance().init(this);  
		initJPush();
		initIM();
	}
	
	private void initJPush() {
		JPushInterface.setDebugMode(false);
		JPushInterface.init(this);
	}
	
	private void initIM() {
		String processAppName = getAppName(android.os.Process.myPid());
		// 如果app启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
		// 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
		if (processAppName != null && processAppName.equalsIgnoreCase("com.shareshenghuo.app.shop")) {
			IMManager.getInstance().init(this);
		}
	}
	
	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
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
