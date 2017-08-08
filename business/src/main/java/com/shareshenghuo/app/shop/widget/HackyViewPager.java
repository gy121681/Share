package com.shareshenghuo.app.shop.widget;


import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class HackyViewPager extends ViewPager {
	
	private Context context;

	public HackyViewPager(Context context) {
		super(context);
		this.context = context;
	}
	public HackyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}
	
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		switch (event.getAction() & MotionEvent.ACTION_MASK) {  
//		case MotionEvent.ACTION_DOWN:  
//			Log.e("", "aa   - - - - - ");
//		    break;  
//		case MotionEvent.ACTION_UP:  
//			Log.e("", "bb   - - - - - ");
//		    break;  
//		case MotionEvent.ACTION_POINTER_UP:  
//			Log.e("", "cc   - - - - - ");
//		    break;  
//		case MotionEvent.ACTION_POINTER_DOWN:  
//			Log.e("", "dd   - - - - - ");
//		    break;        
//		case MotionEvent.ACTION_MOVE:  
//			Log.e("", "ee   - - - - - ");
//		        break;  
//		    }  
//		
//		return super.onTouchEvent(event);
//	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		boolean a = false;
		boolean b = false;
		boolean c = false;
		int  mode;
		try {
			
//			switch (event.getAction() & MotionEvent.ACTION_MASK) {  
//			case MotionEvent.ACTION_DOWN:  
//				Log.e("", "aa   - - - - - ");
//			    break;  
//			case MotionEvent.ACTION_UP:  
//				Log.e("", "bb   - - - - - ");
//			    break;  
//			case MotionEvent.ACTION_POINTER_UP:  
//				Log.e("", "cc   - - - - - ");
//			    break;  
//			case MotionEvent.ACTION_POINTER_DOWN:  
//				Log.e("", "dd   - - - - - ");
//			    break;        
//			case MotionEvent.ACTION_MOVE:  
//				Log.e("", "ee   - - - - - ");
//			        break;  
//			    }  
//				((Activity) context).finish();
			return super.onInterceptTouchEvent(event);
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
