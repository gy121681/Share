package com.shareshenghuo.app.user.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class ExitActivityWatcher {
	
	public static String ACT_EXIT = "com.cdh.user.nxmanage.receiver.ExitActivityReceiver";
	
	private Context context;
	private ExitActivityReceiver receiver;
	private IntentFilter filter;
	
	public ExitActivityWatcher(Context context) {
		this.context = context;
		receiver = new ExitActivityReceiver();
		filter = new IntentFilter();
		filter.addAction(ACT_EXIT);
	}
	
	public void startWatch() {
		if(receiver != null)
			context.registerReceiver(receiver, filter);
	}
	
	public void stopWatch() {
		if(receiver != null)
			context.unregisterReceiver(receiver);
	}
	
	public void sendExit() {
		context.sendBroadcast(new Intent(ACT_EXIT));
	}

	private class ExitActivityReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent data) {
			if(context != null)
				((Activity)context).finish();
		}
	}
}
