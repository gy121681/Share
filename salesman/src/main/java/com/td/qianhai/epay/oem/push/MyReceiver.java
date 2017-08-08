package com.td.qianhai.epay.oem.push;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.td.qianhai.epay.oem.BalanceDetailsAcitvity1;
import com.td.qianhai.epay.oem.IntroductionActivity;
import com.td.qianhai.epay.oem.MainActivity;
import com.td.qianhai.epay.oem.OrderInfoAcitvity;
import com.td.qianhai.epay.oem.PurchaseProductsActivity;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;


import cn.jpush.android.api.JPushInterface;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.text.TextUtils;
import android.util.Log;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
		
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

			JPushInterface.reportNotificationOpened(context,
					bundle.getString(JPushInterface.EXTRA_MSG_ID));

	        	String customContentString = bundle.getString(JPushInterface.EXTRA_EXTRA);
	        	String title = bundle.getString(JPushInterface.EXTRA_TITLE);
	        	String description = bundle.getString(JPushInterface.EXTRA_ALERT);
	        	
	            if (!TextUtils.isEmpty(customContentString)) {
	                JSONObject customJson = null;
	                try {
	                    customJson = new JSONObject(customContentString);
	                    String business_type = null;
	                    String push_type = null;
	                    String mark_status = null;
	                    	business_type = customJson.getString("BusinessType");
	                    	push_type = customJson.getString("PushType");
	                    	mark_status = customJson.getString("MarkStatus");
	                    
	                    isrun(context,business_type,push_type,mark_status,title,description);
	                    Log.e("mymessage", "mymessage = = = "+business_type +"++++"+push_type+"++++"+mark_status+"++++++"+title+"+++++"+description);
	                } catch (JSONException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                    isrun(context,"","","",title,description);
	                }
	            }

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));

		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	private boolean isRunning(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		boolean isAppRunning = false;
		String MY_PKG_NAME = "com.td.qianhai.epay.oem";
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
					|| info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
				isAppRunning = true;
				break;
			}
		}
		return isAppRunning;
	}
	
	
	 public void isrun(Context context, String business_type, String push_type, String mark_status, String title, String description){
	    	
	    	Intent it = new Intent();
	    	
	    	// 打开自定义的Activity
	    	if(isRunning(context)){
	    		
	    		if(push_type!=null&&push_type.equals("2")){
	    			it.setClass(context, OrderInfoAcitvity.class);
	    			it.putExtra("id", mark_status);
	    			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
	    			context.startActivity(it);
	    		}else if(push_type!=null&&push_type.equals("4")){
	    			
//	    			it.setClass(context, NewDistributorActivity.class);
//	    			it.putExtra("mobile", mark_status);
//	    			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//	    			context.startActivity(it);
	    		}else if(push_type!=null&&push_type.equals("3")){
	    			String mobile = MyCacheUtil.getshared(context).getString("Mobile", "");
	    			if(business_type.equals(mobile)){
	    				Intent its = new Intent(context,BalanceDetailsAcitvity1.class);
	    				context.startActivity(its);
	    			}
	    			
	    		}else if(push_type!=null&&push_type.equals("3")){
	    			String mobile = MyCacheUtil.getshared(context).getString("Mobile", "");
	    			if(business_type.equals(mobile)){
	    				it.setClass(context,PurchaseProductsActivity.class);
	    				it.putExtra("teg", "4");
	    				it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
	    				context.startActivity(it);
	    			}
    		}else{
    			it.setClass(context, IntroductionActivity.class);
    			it.putExtra("title", title);
    			it.putExtra("description", description);
    			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
    			context.startActivity(it);
    		}
    	}else{
    		if(push_type!=null&&push_type.equals("3")){
    			String mobile = MyCacheUtil.getshared(context).getString("Mobile", "");
    			if(business_type.equals(mobile)){
    				it.setClass(context,PurchaseProductsActivity.class);
    				it.putExtra("teg", "4");
    				it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
    				context.startActivity(it);
    		}else{
	    		AppContext.getInstance().setMsgtitle(title);
	    		AppContext.getInstance().setMsgcontent(description);
	    		AppContext.isscreenstatus = true;
	    		Intent i = new Intent(context, MainActivity.class);
//	    		i.putExtras(bundle);
	    		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    		context.startActivity(i);
				}
			}
		}
	}
}

