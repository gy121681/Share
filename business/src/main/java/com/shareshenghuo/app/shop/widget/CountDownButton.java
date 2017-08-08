package com.shareshenghuo.app.shop.widget;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.Button;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.network.request.VCodeRequest;
import com.shareshenghuo.app.shop.network.response.VCodeResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.T;

public class CountDownButton extends Button {
	
	private static final int HANDLER_COUNTDOWN = 0x100;
	
	private Context mContext;
	
	private boolean isPaying;
	private int waitTime;
	private String vcodeId;
	
	private CountDownListener listener;

	public CountDownButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CountDownButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CountDownButton(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.mContext = context;
		onStart();
	}
	
	public void onStart() {
		waitTime = 60;
		isPaying = true;
		this.setText("获取验证码");
	}
	
	public void onStop() {
		waitTime = 0;
		isPaying = false;
		this.setText("获取验证码");
	}
	
	/**
	 * 获取验证码
	 */
	public void getVCode(String mobile, final CountDownListener listener) {
		this.listener = listener;
		startCountDown();
		
		VCodeRequest req = new VCodeRequest();
		req.phone = mobile;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_GET_VCODE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				T.showNetworkError(mContext);
				onStop();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				if(resp.statusCode==200 && resp.result!=null) {
					VCodeResponse bean = new Gson().fromJson(resp.result, VCodeResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						vcodeId = bean.data.id;
					} else {
						onStop();
					}
				}
			}
		});
	}
	
	public String getVCodeId() {
		return vcodeId;
	}
	
	/**
	 * 开始倒计时
	 */
	private void startCountDown() {
		this.setEnabled(false);
		waitTime = 60;
		isPaying = true;
		new Thread(new CountDownRunnable()).start();
		this.setText(waitTime+"s");
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case HANDLER_COUNTDOWN:
				if(waitTime > 0) {
					CountDownButton.this.setText(waitTime+"s");
				} else {
					CountDownButton.this.setText("获取验证码");
					CountDownButton.this.setEnabled(true);
				}
				break;
			}
		}
	};
	
	private class CountDownRunnable implements Runnable {
		@Override
		public void run() {
			while(waitTime > 0) {
				if(!isPaying)
					break;
				
				try {
					Thread.sleep(1000);
					waitTime--;
					mHandler.sendEmptyMessage(HANDLER_COUNTDOWN);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public interface CountDownListener {
		
	}
}
