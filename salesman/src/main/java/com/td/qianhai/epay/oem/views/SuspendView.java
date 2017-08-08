package com.td.qianhai.epay.oem.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;

public class SuspendView extends TextView {
	
	private final String TAG = SuspendView.class.getSimpleName();

	public static int TOOL_BAR_HIGH = 0;
	public static WindowManager.LayoutParams params = new WindowManager.LayoutParams();
	private float startX;
	private float startY;
	private float x;
	private float y;

	private String text;

	WindowManager wm = (WindowManager) getContext().getApplicationContext()
			.getSystemService(getContext().WINDOW_SERVICE);

	public SuspendView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SuspendView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SuspendView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		text = "世上只有妈妈好,有妈的孩子像块宝";
		this.setWidth(200);
		this.setHeight(100);
		// this.setBackgroundColor(Color.YELLOW);
		// this.setBackgroundColor(Color.argb(90, 150, 150, 150));
		 this.setBackgroundColor(Color.argb(0, 0, 255, 0)); //背景透明度
		 this.setBackgroundColor(0x80000000); //背景透明度
//		 this.setBackgroundColor(#000); //背景透明度
		// this.setTypeface(tf, style);
//		this.setBackgroundColor(R.color.transparent);
		// this.getBackground().setAlpha(180);
		// this.setBackgroundColor(color).setAlpha(180);

		// 下面这句话在此并不是控制歌词大小，仅仅是为了控制背景大小，如果不设置的话，Paint字体大时会被遮挡
		this.setTextSize(20f);
//		handler = new Handler();
		// handler.post(update);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 触摸点相对于屏幕左上角坐标
		x = event.getRawX();
		y = event.getRawY() - TOOL_BAR_HIGH;
		Log.d(TAG, "------X: " + x + "------Y:" + y);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = event.getX();
			startY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			updatePosition();
			break;
		case MotionEvent.ACTION_UP:
			updatePosition();
			startX = startY = 0;
			break;
		}

		return true;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Paint p = new Paint();
		p.setTextSize(26f);
		canvas.drawText(text, 10, 40, p);
	}

	// 更新浮动窗口位置参数
	private void updatePosition() {
		// View的当前位置
		params.x = (int) (x - startX);
		params.y = (int) (y - startY);
		wm.updateViewLayout(this, params);
	}
	
}
