package com.shareshenghuo.app.shop;


import com.shareshenghuo.app.shop.widget.SystemBarTintManager;
import com.shareshenghuo.app.shop.widget.TipsView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

public class TipsActivity extends  Activity{

    private static final String TAG = "TipsActivity";
    private int[] mLocs;

//    @BindView(R.id.tips_rootview)
    private RelativeLayout tips_rootview;
//    RelativeLayout mRlRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//取消title
        setContentView(R.layout.activity_tips);
        
 		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(getResources().getColor(R.color.transparent));
        Intent intent = getIntent();
        mLocs = intent.getIntArrayExtra("loc");//获取坐标
//      ButterKnife.bind(this);
        initView();
    }

    @TargetApi(19) 
  	private void setTranslucentStatus(boolean on) {
  		Window win = getWindow();
  		WindowManager.LayoutParams winParams = win.getAttributes();
  		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
  		if (on) {
  			winParams.flags |= bits;
  		} else {
  			winParams.flags &= ~bits;
  		}
  		win.setAttributes(winParams);
  	}
    
    private void initView() {
//        PrintLog.d(TAG,"initView");
        TipsView tipsView = new TipsView(this);//将坐标传给自定义view
        tipsView.setCircleLocation(mLocs);
        tips_rootview = (RelativeLayout) findViewById(R.id.tips_rootview);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tips_rootview.addView(tipsView, layoutParams);
        
        tips_rootview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setResult(RESULT_OK);
		        finish();
		        overridePendingTransition(0, 0); //取消动画效果
			}
		});
    }
//    @OnClick(R.id.tips_rootview)
//    public void clickClose() {
//
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(0, 0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}