package com.shareshenghuo.app.user.receiver;

import com.shareshenghuo.app.user.MessageActivity;

import cn.jpush.android.api.JPushInterface;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class JPushCustomReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			//用户点击通知栏
			Intent it = new Intent(context, MessageActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(it);
		}
	}
}
