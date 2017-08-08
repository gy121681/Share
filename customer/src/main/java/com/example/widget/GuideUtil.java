package com.example.widget;


import com.shareshenghuo.app.user.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

/**
 * @类名:GuideUtil
 * @类描述:引导工具界面
 * @作者:Administrator
 * @创建时间:2015年2月12日-下午4:18:52
 * @修改人:
 * @修改时间:
 * @修改备注:
 * @版本:
 */
public class GuideUtil {
	private Context context;
	private ImageView imgView;
	private WindowManager windowManager;
	private View re;
	private static GuideUtil instance = null;
	/** 是否第一次进入该程序 **/
	private boolean isFirst = true;

	private GuideUtil() {
	}

	public static GuideUtil getInstance() {
		synchronized (GuideUtil.class) {
			if (null == instance) {
				instance = new GuideUtil();
			}
		}
		return instance;
	}

	private Handler handler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				// 设置LayoutParams参数
				final LayoutParams params = new WindowManager.LayoutParams();
				// 设置显示的类型，TYPE_PHONE指的是来电话的时候会被覆盖，其他时候会在最前端，显示位置在stateBar下面，其他更多的值请查阅文档
				params.type = WindowManager.LayoutParams.TYPE_PHONE;
				// 设置显示格式
				params.format = PixelFormat.RGBA_8888;
				// 设置对齐方式
				params.gravity = Gravity.LEFT | Gravity.TOP;
				// 设置宽高
				params.width = ScreenUtils.getScreenWidth(context);
				params.height = ScreenUtils.getScreenHeight(context);
				// 设置动画
				params.windowAnimations = R.style.view_anim;

				// 添加到当前的窗口上
				windowManager.addView(re, params);
				break;
			}
		};
	};

	public void initGuide(Activity context, int drawableRourcesId) {
		if (!isFirst) {
			return;
		}
		this.context = context;
		windowManager = context.getWindowManager();
		
		 re = LayoutInflater.from(context).inflate(R.layout.tips1_layout, null);
//		re.setLayoutParams(new LayoutParams(
//				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
//				android.view.ViewGroup.LayoutParams.MATCH_PARENT));
//		re.setBackgroundColor(context.getResources().getColor(R.color.bg_color));
//		re.setAlpha(0.7f);
//		re.setBackgroundColor(context.getResources().getColor(""));
		// 动态初始化图层
		imgView = new ImageView(context);
		imgView.setLayoutParams(new LayoutParams(
				100,
				100));
//		imgView.setScaleType(ScaleType.FIT_XY);
//		imgView.setImageResource(drawableRourcesId);
		handler.sendEmptyMessageDelayed(1, 1000);
//		((ViewGroup) re).addView(imgView);
		// 点击图层之后，将图层移除
		re.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				windowManager.removeView(re);
			}
		});
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

}
