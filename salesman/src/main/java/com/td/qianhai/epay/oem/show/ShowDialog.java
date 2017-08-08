package com.td.qianhai.epay.oem.show;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.views.dialog.LoadingDialogWhole;

/**
 * 公共dialog显示
 * @author liangge
 *
 */
public class ShowDialog {
	
	public Context context;
	public Activity activity;
	/** 无按钮的进度dialog */
	public LoadingDialogWhole loadingDialogWhole;
	
	public ShowDialog(Context context, Activity activity) {
		super();
		this.context = context;
		this.activity = activity;
	}
	
	/**
	 * 先加载数据显示dialog
	 * 
	 * @param msg
	 */
	public void showLoadingDialog(String msg) {
		loadingDialogWhole = new LoadingDialogWhole(context, R.style.CustomDialog,
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
}
