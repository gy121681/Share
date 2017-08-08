package com.shareshenghuo.app.user.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class PayWatcher {
	
	public static String ACT_PAY = "com.cdh.user.nxmanage.receiver.PayReceiver";
	
	private Context context;
	private PayReceiver receiver;
	private IntentFilter filter;
	
	private OnPayResultCallback callback;
	
	public PayWatcher(Context context) {
		this.context = context;
		receiver = new PayReceiver();
		filter = new IntentFilter();
		filter.addAction(ACT_PAY);
	}
	
	public void startWatch() {
		if(receiver != null)
			context.registerReceiver(receiver, filter);
	}
	
	public void stopWatch() {
		if(receiver != null)
			context.unregisterReceiver(receiver);
	}
	
	private class PayReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context arg0, Intent data) {
			if(callback != null) {
				callback.onPayResult(data.getIntExtra("code", -1), data.getStringExtra("str"));
			}
		}
	}
	
	public void setOnPayResultCallback(OnPayResultCallback callback) {
		this.callback = callback;
	}
	
	public interface OnPayResultCallback {
		public void onPayResult(int errCode, String errStr);
	}
}
