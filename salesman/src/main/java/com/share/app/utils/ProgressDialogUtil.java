package com.share.app.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.views.dialog.LoadingDialogWhole;


public class ProgressDialogUtil {

	private static   Dialog loadingDialogWhole ;

	private static int count = 0;

	public static void showProgressDlg(Context context, String message) {
		if (loadingDialogWhole != null && loadingDialogWhole.isShowing()) {
		} else {
			loadingDialogWhole = new LoadingDialogWhole(context, R.style.CustomDialog,
					message);
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
		count++;
	}

	public static void dismissProgressDlg() {
		if (loadingDialogWhole != null) {
			if (count > 0) {
				count--;
			}
			if (count == 0) {
				loadingDialogWhole.dismiss();
			}
		}
	}

}
