package com.shareshenghuo.app.user;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.ImageLoadManager;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.SwipeBackLayout;
import com.shareshenghuo.app.user.widget.SystemBarTintManager;

public class BaseTopActivity extends FragmentActivity {
	protected SwipeBackLayout layout;
	protected LinearLayout llTopBack;
	protected TextView tvTopTitle;
	protected Button btnTopRight1;
	protected Button btnTopRight4;
	protected Button btnTopRight2;
	protected CheckBox btnTopRight3;
    final int RIGHT = 0;  
    final int LEFT = 1;  
    private GestureDetector gestureDetector; 
    
    @Override
    protected void onCreate(Bundle arg0) {
    	// TODO Auto-generated method stub
    	super.onCreate(arg0);
    	 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
 		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(false);
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
	
	protected void initTopBar(String title) {
		
        if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
        	getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        gestureDetector = new GestureDetector(this,onGestureListener); 
        
//		layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
//				R.layout.base, null);
//		layout.attachToActivity(this);
        
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.bg_white);
		llTopBack = (LinearLayout) findViewById(R.id.llTopBack);
		tvTopTitle = (TextView) findViewById(R.id.tvTopTitle);
		btnTopRight4 = (Button) findViewById(R.id.btnTopRight4);
		btnTopRight1 = (Button) findViewById(R.id.btnTopRight1);
		btnTopRight2 = (Button) findViewById(R.id.btnTopRight2);
		btnTopRight3 = (CheckBox) findViewById(R.id.btnTopRight3);

		tvTopTitle.setText(title);
		
		llTopBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(BaseTopActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				finish();
			}
		});
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
	
//	@Override
//	protected void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		
//		closeKeybord(this);
//	}
//	
//    /**
//     * 关闭软键盘
//     * 
//     * @param mEditText输入框
//     * @param mContext上下文
//     */
//    public void closeKeybord( Context mContext)
//    {
//    	getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
////    	InputMethodManager m=(InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
////    	m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
//    }
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub	
		try {
			this.gestureDetector.onTouchEvent(ev);
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		return super.dispatchTouchEvent(ev);
	}
	
	private GestureDetector.OnGestureListener onGestureListener =   
	        new GestureDetector.SimpleOnGestureListener() {  
	        @Override  
	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
	                float velocityY) {  
	        	
	        	if(e1.getX()<50){
	        		if(e2.getX()>52){
	        			finish();
	        		}
	        	}
//	        	Log.e("", "e1 = = =  "+e1.getX());
//	        	Log.e("", "e2 = = =  "+e2.getX());
	            return true;  
	        }  
	    };  
	  
//	    public boolean onTouchEvent(MotionEvent event) {  
//	        return gestureDetector.onTouchEvent(event);  
//	    }  
	  
	    public void doResult(int action) {  
	  
	        switch (action) {  
	        case RIGHT:  
	        	Log.e("", "go right");
	            break;  
	  
	        case LEFT:  
	        	Log.e("", "go left");
	            break;  
	  
	        }  
	    }

	    protected void toast(String msg){
			T.show(this, msg, Toast.LENGTH_SHORT);
		}
	
}
