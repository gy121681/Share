package com.shareshenghuo.app.user;

import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.shareshenghuo.app.user.senceidcard.MyDrawView;
import com.sensetime.idcard.IDCardActivity;

public class MyIDCardActivity extends IDCardActivity {

	private static final String TAG = IDCardActivity.class.getSimpleName();

	// 重写这个方法来实现自定义的扫描界面
	@Override
	protected View createOverlayView() {
		FrameLayout overlay = new FrameLayout(this);
		overlay.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));

		WindowManager windowManager = this.getWindowManager();
		
		MyDrawView mView = new MyDrawView(this);
		mView.guideRect = getGuideFrame();
		mView.screenWidth = windowManager.getDefaultDisplay().getWidth();
		mView.screenHeight = windowManager.getDefaultDisplay().getHeight();
        overlay.addView(mView);
        
		return overlay;
	}
}
