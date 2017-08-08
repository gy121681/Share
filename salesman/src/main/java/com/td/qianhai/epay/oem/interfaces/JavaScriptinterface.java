package com.td.qianhai.epay.oem.interfaces;

import android.app.Activity;
import android.content.Context;

public class JavaScriptinterface {

	private Context mContext;
	//这个一定要定义，要不在showToast()方法里没办法启动intent
	Activity activity;

	/** Instantiate the interface and set the context */
	public JavaScriptinterface(Context c) {
		mContext = c;
		activity = (Activity) c;
	}

	/** 与js交互时用到的方法，在js里直接调用的 */
	public void showToast() {
		
		activity.finish();
	}
}