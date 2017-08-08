package com.td.qianhai.epay.oem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.utils.AppManager;
import com.td.qianhai.epay.utils.Common;

public class NetworkRemindDialogActivity extends Activity implements
		android.view.View.OnClickListener {

	private ImageButton wifiBtn, networkBtn;
	private ImageView wifiImages, networkImages;
	private Button btClose;
	private boolean flag;
	private String activityName;
	private TelephonyManager mTelephonyManager;
	// Frame动画
	private AnimationDrawable animWifi, animNetwork;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.network_dialog);
		wifiBtn = (ImageButton) findViewById(R.id.btn_wifi_on_off);
		networkBtn = (ImageButton) findViewById(R.id.btn_network_on_off);
		wifiImages = (ImageView) findViewById(R.id.btn_wifi_anim_iamge);
		networkImages = (ImageView) findViewById(R.id.btn_network_anim_iamge);
		btClose = (Button) findViewById(R.id.net_close);
		this.animWifi = (AnimationDrawable) this.wifiImages.getBackground();
		this.animNetwork = (AnimationDrawable) this.networkImages
				.getBackground();
		wifiBtn.setOnClickListener(this);
		networkBtn.setOnClickListener(this);
		btClose.setOnClickListener(this);
		this.setFinishOnTouchOutside(false);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			activityName = bundle.getString("name");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_wifi_on_off:
			this.animWifi.start();
			Common.toggleWiFi(this, true);
			wifiBtn.setBackgroundResource(R.drawable.combox_choose);
			flag = true;
			Thread thread = new Thread(new NetConnectRunable());
			thread.start();
			break;
		case R.id.btn_network_on_off:
			isSimExist();
			if (getSimState() == TelephonyManager.SIM_STATE_READY) {
				this.animNetwork.start();
				Common.toggleMobileData(this, true);
				networkBtn.setBackgroundResource(R.drawable.combox_choose);
				flag = true;
				Thread thread2 = new Thread(new NetConnectRunable());
				thread2.start();
			} else {
				Toast.makeText(getApplicationContext(), isSimExist(),
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.net_close:
			if (activityName != null
					&& activityName.equals("com.td.qianhai.epay.oem.MainActivity")) {
				((AppContext) getApplicationContext()).setCustId(null); // 商户ID赋为空
				((AppContext) getApplicationContext()).setPsamId(null);
				((AppContext) getApplicationContext()).setMacKey(null);
				((AppContext) getApplicationContext()).setPinKey(null);
				((AppContext) getApplicationContext()).setMobile(null);
				((AppContext) getApplicationContext()).setOpenNetwork(null);
				finish();
				AppManager.getAppManager().AppExit(getApplicationContext());
			} else {
				((AppContext) getApplicationContext()).setOpenNetwork(null);
				finish();
			}
			break;
		default:
			break;
		}
	}

	private Handler netHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
			case 6:
				try {
					Thread.sleep(1000);
					Toast.makeText(getApplicationContext(), "网络连接成功!",
							Toast.LENGTH_SHORT).show();
					((AppContext) getApplicationContext()).setOpenNetwork(null);
					finish();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			case 7:
				if (activityName != null
						&& activityName
								.equals("com.td.qianhai.pay.MainActivity")) {
					((AppContext) getApplicationContext()).setCustId(null); // 商户ID赋为空
					((AppContext) getApplicationContext()).setPsamId(null);
					((AppContext) getApplicationContext()).setMacKey(null);
					((AppContext) getApplicationContext()).setPinKey(null);
					((AppContext) getApplicationContext()).setMobile(null);
					((AppContext) getApplicationContext()).setOpenNetwork(null);
					finish();
					AppManager.getAppManager().AppExit(getApplicationContext());
				} else {
					((AppContext) getApplicationContext()).setOpenNetwork(null);
					finish();
				}
				Toast.makeText(getApplicationContext(), "连接网络超时!",
						Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}

		}
	};

	class NetConnectRunable implements Runnable {
		int countTime = 0;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (flag) {
				if (countTime >= 39) {
					flag = false;
					netHandler.sendEmptyMessage(7);
				}
				if (Common.isNetworkConnected(getApplicationContext())) {
					flag = false;
					netHandler.sendEmptyMessage(6);
				}
				try {
					Thread.sleep(1000);
					countTime++;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private int getSimState() {
		return mTelephonyManager.getSimState();
	}

	private String isSimExist() {
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		int simState = mTelephonyManager.getSimState();

		switch (simState) {

		case TelephonyManager.SIM_STATE_ABSENT:
			return "无sim卡,请插入手机sim卡";
			// do something

		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			return "需要NetworkPIN解锁";
			// do something

		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			return "需要PIN解锁";
			// do something

		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			return "需要PUN解锁";
			// do something
		case TelephonyManager.SIM_STATE_READY:
			return "良好";
			// do something

		case TelephonyManager.SIM_STATE_UNKNOWN:
			return "未知状态";
			// do something
		}

		return null;
	}
}
