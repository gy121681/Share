package com.td.qianhai.epay.oem.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenBroadcastReceivers extends BroadcastReceiver{
	 private String action = null;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
        action = intent.getAction();
        if (Intent.ACTION_SCREEN_ON.equals(action)) {  
        	
        	Log.e("", "1");
            // 开屏
        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { 
        	Log.e("", "2");
            // 锁屏
        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
        	Log.e("", "3");
            // 解锁
        }
		
	}

}
