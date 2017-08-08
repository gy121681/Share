package com.shareshenghuo.app.shop;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.DataCleanManager;
import com.shareshenghuo.app.shop.network.request.VersionRequest;
import com.shareshenghuo.app.shop.network.response.VersionRespone;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ApkUtil;
import com.shareshenghuo.app.shop.util.PreferenceUtils;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;

public class SettingActivity extends BaseTopActivity implements OnClickListener {
	
	private static String KEY_NOTIFY_SOUND = "notify_sound";
	
	private TextView tvCacheTotal;
	private CheckBox cbPush;
	private CheckBox cbSound;
	private TextView tvVersion;
	private TwoButtonDialog downloadDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		init();
	}
	
	public void init() {
		initTopBar("设置");
		tvCacheTotal = getView(R.id.tvCacheTotal);
		cbPush = getView(R.id.cbPush);
		cbSound = getView(R.id.cbNotifySound);
		tvVersion = getView(R.id.tvVersionName);
		
		cbPush.setChecked(!JPushInterface.isPushStopped(this));
		cbSound.setChecked(PreferenceUtils.getPrefBoolean(this, KEY_NOTIFY_SOUND, true));
		
		getView(R.id.llCleanCache).setOnClickListener(this);
		getView(R.id.llCheckVersion).setOnClickListener(this);
		cbPush.setOnClickListener(this);
		cbSound.setOnClickListener(this);
		
		try {
			tvVersion.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			tvCacheTotal.setText(DataCleanManager.getTotalCacheSize(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.llCleanCache:
			cleanCache();
			break;
			
		case R.id.cbPush:
			if(JPushInterface.isPushStopped(this))
				JPushInterface.resumePush(this);
			else
				JPushInterface.stopPush(this);
			break;
			
		case R.id.cbNotifySound:
			BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getApplicationContext());
			EMChatOptions options = EMChatManager.getInstance().getChatOptions();
			if(PreferenceUtils.getPrefBoolean(this, KEY_NOTIFY_SOUND, true)) {
				//关闭声音提醒
				builder.notificationDefaults = Notification.DEFAULT_VIBRATE;
				options.setNoticeBySound(false);
				options.setNoticedByVibrate(true);
				PreferenceUtils.setPrefBoolean(this, KEY_NOTIFY_SOUND, false);
			} else {
				//开启声音提醒
				builder.notificationDefaults = Notification.DEFAULT_SOUND;
				options.setNoticeBySound(true);
				options.setNoticedByVibrate(false);
				PreferenceUtils.setPrefBoolean(this, KEY_NOTIFY_SOUND, true);
			}
			JPushInterface.setDefaultPushNotificationBuilder(builder);
			break;
			
		case R.id.llCheckVersion:
			break;
		}
	}
	
	public void cleanCache() {
		
		downloadDialog = new TwoButtonDialog(this, R.style.CustomDialog,
				"温馨提示", "少量的缓存可能不会拖慢手机的运行速度，清除缓存后下次使用时需重新加载图片等信息，您确定要清除缓存吗?", "取消", "确定",false,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							DataCleanManager.clearAllCache(SettingActivity.this);
							T.showShort(SettingActivity.this, "清除缓存成功");
							tvCacheTotal.setText("");
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
		downloadDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
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
		downloadDialog.setCanceledOnTouchOutside(false);
		
			downloadDialog.show();
		}
//		new AlertDialog.Builder(this).setTitle("温馨提示")
//			.setMessage("少量的缓存可能不会拖慢手机的运行速度，清除缓存后下次使用时需重新加载图片等信息，您确定要清除缓存吗？")
//			.setNegativeButton("取消", null)
//			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface arg0, int arg1) {
//					DataCleanManager.clearAllCache(SettingActivity.this);
//					T.showShort(SettingActivity.this, "清除缓存成功");
//					tvCacheTotal.setText("");
//				}
//			}).show();
}
