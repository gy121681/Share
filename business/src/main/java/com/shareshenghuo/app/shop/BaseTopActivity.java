package com.shareshenghuo.app.shop;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.ImageLoadManager;
import com.shareshenghuo.app.shop.widget.SystemBarTintManager;

public class BaseTopActivity extends FragmentActivity {
	
	protected LinearLayout llTopBack;
	protected TextView tvTopTitle;
	protected Button btnTopRight1;
	protected Button btnTopRight2;
	protected Button btnTopRight3;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				setTranslucentStatus(true);
			}
			
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.bg_white);
	}
	
	protected void initTopBar(String title) {
		
        

		
		llTopBack = (LinearLayout) findViewById(R.id.llTopBack);
		tvTopTitle = (TextView) findViewById(R.id.tvTopTitle);
		btnTopRight1 = (Button) findViewById(R.id.btnTopRight1);
		btnTopRight2 = (Button) findViewById(R.id.btnTopRight2);
		btnTopRight3 = (Button) findViewById(R.id.btnTopRight3);

		tvTopTitle.setText(title);
		
		llTopBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(BaseTopActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				} catch (Exception e) {
					// TODO: handle exception
				}
			
//		    	InputMethodManager m=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//		    	  if (m.isActive()) {
//		    		  m.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
//		    	  }

				finish();
			}
		});
		
	}
	
	protected void initTop(){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.bg_white);
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
	
	public void setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
	}

	public void setText(int viewId, int textId) {
		TextView tv = getView(viewId);
		tv.setText(textId);
	}

	public void setImageResource(int viewId, int drawableId) {
		ImageView iv = getView(viewId);
		iv.setImageResource(drawableId);
	}

	public void setImageBitmap(int viewId, Bitmap bm) {
		ImageView iv = getView(viewId);
		iv.setImageBitmap(bm);
	}

	public void setImageByURL(int viewId, final String url) {
		ImageView iv = getView(viewId);
		ImageLoadManager.getInstance(this).displayImage(url, iv);
	}

	public <T extends View>T getView(int viewId) {
		View view  = findViewById(viewId);
		return (T)view;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//		
//		closeKeybord(this);
	}
	
    /**
     * 关闭软键盘
     * 
     * @param mEditText输入框
     * @param mContext上下文
     */
    public void closeKeybord( Context mContext)
    {
    	
//    	InputMethodManager m=(InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//    	
//    	if(getWindow().getAttributes().softInputMode==WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED){
//    		 Log.e("", " - - - - - - dalkai  ");
//    	}
//    	
//    	  if (m.isActive()) {
//    		  Log.e("", " - - - - - -  "+m.isActive());
//    	m.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
//    	  }
    }

    public void toast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
