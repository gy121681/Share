package com.shareshenghuo.app.user.widget;


import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.util.DateUtil;

public class CountDownTextView extends LinearLayout {
	
	private Context mContext;
	private CountDownTimer mTimer;
	
	private TextView tvHour;
	private TextView tvMin;
	private TextView tvSec;

	private OnCountDownListener listener;

	public CountDownTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	public CountDownTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public CountDownTextView(Context context) {
		super(context);
		init(context, null, 0);
	}

	public void init(Context context, AttributeSet attrs, int defStyleAttr) {
		this.mContext = context;
		LayoutInflater.from(context).inflate(R.layout.text_count_down, this);
		tvHour = (TextView) findViewById(R.id.tvCountDownHour);
		tvMin = (TextView) findViewById(R.id.tvCountDownMin);
		tvSec = (TextView) findViewById(R.id.tvCountDownSec);
	}

	public void setText(String timeStr) {
		tvHour.setText(timeStr+"");
	}

	public void setText(String text, int i) {
		tvHour.setText(text);
		stopCountDown();
	}
	
	public void startCountDown(final String endTimeStr) {
		startCountDown(endTimeStr, 0);
	}

	public void startCountDown(final String endTimeStr, final long difTime) {
		if(mTimer != null)
			return;

		long endTime = DateUtil.StringToLong(endTimeStr, DateUtil.DATETIME_YMDHMS);
		long curTime = System.currentTimeMillis() + difTime;
		long time = endTime-curTime;

		startCountDown(time);
	}
	
	public void startCountDown(long endTime) {
		if(mTimer != null)
			return;
		
		long time = endTime * 1000;
		if(time <= 0) {
			if(listener != null)
				listener.onFinish();
			return;
		}

		mTimer = new CountDownTimer(time, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				millisUntilFinished = millisUntilFinished / 1000;
				int hour = (int) (millisUntilFinished / (60*60));
				int minute = (int) ((millisUntilFinished - hour*60*60) / 60);
				int second = (int) (millisUntilFinished % 60);
//				tvHour.setText(hour+":"+minute+":"+second);
				tvHour.setText(hour<10? "0"+hour : ""+hour);
				tvMin.setText(minute<10? "0"+minute : ""+minute);
				tvSec.setText(second<10? "0"+second : ""+second);
			}

			@Override
			public void onFinish() {
				tvHour.setText("00");
				tvMin.setText("00");
				tvSec.setText("00");
				if(listener != null)
					listener.onFinish();
			}
		};
		mTimer.start();
	}
	
	public void stopCountDown() {
		if(mTimer != null) {
			mTimer.cancel();
			mTimer = null;
			tvHour.setText("00");
			tvMin.setText("00");
			tvSec.setText("00");
		}
	}

	public void setTextColor(int color) {
//		tvHour.setTextColor(getResources().getColor(color));
	}

	public void setTextSize(int size) {
//		tvHour.setTextSize(size);
	}

	public void setOnCountDownListener(OnCountDownListener listener) {
		this.listener = listener;
	}

	public interface OnCountDownListener {
		public void onFinish();
	}
}
