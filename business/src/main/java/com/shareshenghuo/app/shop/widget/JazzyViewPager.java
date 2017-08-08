package com.shareshenghuo.app.shop.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.response.BannerListResp;
import com.shareshenghuo.app.shop.pager.OutlineContainer;
import com.shareshenghuo.app.shop.pager.ViewPagerScroller;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.util.Util;

public class JazzyViewPager extends ViewPager {

	public static final String TAG = "JazzyViewPager";

	private boolean mEnabled = true;
	private boolean mFadeEnabled = false;
	private boolean mOutlineEnabled = false;
	private int tag = 0;
	public static int sOutlineColor = Color.WHITE;
	private TransitionEffect mEffect = TransitionEffect.Standard;
	
	private HashMap<Integer, Object> mObjs = new LinkedHashMap<Integer, Object>();

	private static final float SCALE_MAX = 0.5f;
	private static final float ZOOM_MAX = 0.5f;
	private static final float ROT_MAX = 15.0f;
	
	

	private int time = 5000;
	
	public enum TransitionEffect {
		Standard,
		Tablet,
		CubeIn,
		CubeOut,
		FlipVertical,
		FlipHorizontal,
		Stack,
		ZoomIn,
		ZoomOut,
		RotateUp,
		RotateDown,
		Accordion
	}
	private MainAdapter adapter;
	private JazzyViewPager jazzy_pager;
	private ArrayList<View> views;
	private LayoutInflater inflater;
	private BannerListResp bean;
	public  void setupJazziness(TransitionEffect effect,final JazzyViewPager jazzy_pager,final Context context,final ArrayList<View> views, LayoutInflater inflater, BannerListResp bean,int tag) {
		
		this.bean = bean;
		this.inflater = inflater;
		if(handlers!=null){
			stop();
		}
		this.views = views;
		this.jazzy_pager = jazzy_pager;
		jazzy_pager.setTransitionEffect(effect);
		new ViewPagerScroller(context,1000).initViewPagerScroll(jazzy_pager);
		this.tag = tag;
		adapter = new MainAdapter(views);
		jazzy_pager.setAdapter(adapter);
		
//         adapter = new SimpleCarouselAdapter(jazzy_pager,
//                new int[]{R.layout.banner_layout1, R.layout.banner_layout2, R.layout.banner_layout3});
		 jazzy_pager.setCurrentItem(300); 
		 jazzy_pager.setCurrentItem(1);
		jazzy_pager.setPageMargin(0);
		
		
		handlers.postDelayed(runnable, 2000);
		
		jazzy_pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@SuppressLint("NewApi") @Override
			public void onPageSelected(int position) {
				
			}
			
			@SuppressLint("NewApi") @Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				//arg1 显示的view前一个view所占屏幕的比例
				//arg2 viewpager的总宽度
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		
		jazzy_pager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				stop();
				// TODO Auto-generated method stub
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				case MotionEvent.ACTION_UP:
					
					handlers.postDelayed(runnable, time);
				}
				
				return false;
			}
		});

	}
	
	
	public  void startpager() {
		// TODO Auto-generated method stub
		handlers.postDelayed(runnable, 2000);
//   	 final Timer timer = new Timer();
//	      TimerTask task = new TimerTask(){
//	         public void run(){
//	
//	         }
//	      };
//	      timer.schedule(task, 0,2000);
	}
	
	final Handler handlers = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			scrollToNext();
			handlers.postDelayed(this, time);// 延时时长
		}
	};
	
	public void stop(){
		handlers.removeCallbacks(runnable);
	}
	
	private void scrollToNext() {
		// TODO Auto-generated method stub
 		
		
// 		int count = adapter.getCount();  
// 		if (count > 2) { // 实际上，多于1个，就多于3个  
// 		    int index = jazzy_pager.getCurrentItem();  
//// 		    Log.e("", "count = = = "+count);
// 		    index = index % (count - 2) + 1; //这里修改过  
//// 		   Log.e("", "  (count - 2) + 1 = = = "+  (count - 2) + 1);
// 		 
//// 		   Log.e("", "index = = = "+index);
// 		   jazzy_pager.setCurrentItem(index, true);  
// 		}
 		int count = adapter.getCount();  
		if (count > 1) { // 多于1个，才循环  
		    int index = jazzy_pager.getCurrentItem();  
		    index = (index + 1) % count;  
		    jazzy_pager.setCurrentItem(index, true);  
		}	
	}
	
	
	private class MainAdapter extends PagerAdapter {
		
		
		
		public MainAdapter(ArrayList<View> list) {
			// TODO Auto-generated constructor stub
			
		}
		
		@Override
		public Object instantiateItem(ViewGroup container,  int position) {
			container.requestFocus();
			


			
			
			if(bean != null&&bean.data!=null){
				
				if(tag==0){
					if(bean.data.payDateNew!=null){
					View view4 = inflater.inflate(R.layout.banner_layout_new1, null);
					View view6 = inflater.inflate(R.layout.banner_layout_new2, null);
					View view5 = inflater.inflate(R.layout.banner_layout_new3, null);
					TextView tv_todaypayDate1 = (TextView) view4.findViewById(R.id.tv_payDates);
					TextView tv_threeSunTotalFee1 = (TextView) view4.findViewById(R.id.tv_threeSunTotalFee);
					TextView tv_twoSunTotalFee1 = (TextView) view4.findViewById(R.id.tv_twoSunTotalFee);
					TextView tv_oneSunTotalFee1 = (TextView) view4.findViewById(R.id.tv_oneSunTotalFee);
					tv_todaypayDate1.setText(DateUtil.getStrTime(bean.data.payDateNew));
					tv_threeSunTotalFee1.setText(Util.getfotmatnum(bean.data.threeSunTotalFeeNew,true,1));
					tv_twoSunTotalFee1.setText(Util.getfotmatnum(bean.data.twoSunTotalFeeNew,true,1));
					tv_oneSunTotalFee1.setText(Util.getfotmatnum(bean.data.oneSunTotalFeeNew,true,1));	
					
					TextView tv_payDate1 = (TextView) view5.findViewById(R.id.tv_payDate);
					TextView tv_userFilialPric1 = (TextView) view5.findViewById(R.id.tv_userFilialPric);
					TextView tv_shopFilialPric1 = (TextView) view5.findViewById(R.id.tv_shopFilialPric);
					
					tv_payDate1.setText( DateUtil.getStrTime(bean.data.payDateNew));
//					tv_userFilialPric.setText(Util.getnum(bean.data.userFilialPric,false));
//					tv_shopFilialPric.setText(Util.getnum(bean.data.shopFilialPric,false));
					
					TextView tv_sumTotalConsumption = (TextView) view6.findViewById(R.id.tv_sumTotalConsumption);
					TextView tv_countUser = (TextView) view6.findViewById(R.id.tv_countUser);
					TextView tv_countShop = (TextView) view6.findViewById(R.id.tv_countShop);
					tv_sumTotalConsumption.setText(Util.getnum(bean.data.sumTotalConsumptionNew,true));
					tv_countUser.setText(bean.data.countUser);
					tv_countShop.setText(bean.data.countShop);
					
					
					if(bean.data.userFilialPricRateNew!=null){
						tv_userFilialPric1.setText(Util.getfotmatnum(bean.data.userFilialPricRateNew, true,1));
					}
					if(bean.data.shopFilialPricRateNew!=null){
						tv_shopFilialPric1.setText(Util.getfotmatnum(bean.data.shopFilialPricRateNew, true,1));
					}
					views.add(view4);
					views.add(view6);
					views.add(view5);
					}
				}else{
					View view1 = inflater.inflate(R.layout.banner_layout_1, null);
					View view2 = inflater.inflate(R.layout.banner_layout_2, null);
					View view3 = inflater.inflate(R.layout.banner_layout_3, null);
					TextView tv_todaypayDate = (TextView) view1.findViewById(R.id.tv_payDate);
					TextView tv_threeSunTotalFee = (TextView) view1.findViewById(R.id.tv_threeSunTotalFee);
					TextView tv_twoSunTotalFee = (TextView) view1.findViewById(R.id.tv_twoSunTotalFee);
					TextView tv_oneSunTotalFee = (TextView) view1.findViewById(R.id.tv_oneSunTotalFee);
					tv_todaypayDate.setText(DateUtil.getStrTime(bean.data.payDate));
					tv_threeSunTotalFee.setText(String.format("%s", Util.getfotmatnum(bean.data.threeSunTotalFee,true,1)));
					tv_twoSunTotalFee.setText(String.format("%s", Util.getfotmatnum(bean.data.twoSunTotalFee,true,1)));
					tv_oneSunTotalFee.setText(String.format("%s", Util.getfotmatnum(bean.data.oneSunTotalFee,true,1)));
					
					TextView tv_sumTotalConsumption = (TextView) view2.findViewById(R.id.tv_sumTotalConsumption);
					TextView tv_countUser = (TextView) view2.findViewById(R.id.tv_countUser);
					TextView tv_countShop = (TextView) view2.findViewById(R.id.tv_countShop);
					tv_sumTotalConsumption.setText(Util.getnum(bean.data.sumTotalConsumption,true));
					tv_countUser.setText(bean.data.countUser);
					tv_countShop.setText(bean.data.countShop);
					
					
					TextView tv_payDate = (TextView) view3.findViewById(R.id.tv_payDate);
					TextView tv_userFilialPric = (TextView) view3.findViewById(R.id.tv_userFilialPric);
					TextView tv_shopFilialPric = (TextView) view3.findViewById(R.id.tv_shopFilialPric);
					
					tv_payDate.setText( DateUtil.getStrTime(bean.data.payDate));
					
					if(bean.data.userFilialPricRate!=null){
						tv_userFilialPric.setText(Util.getfotmatnum(bean.data.userFilialPricRate, true,1));
					}
					if(bean.data.shopFilialPricRate!=null){
						tv_shopFilialPric.setText(Util.getfotmatnum(bean.data.shopFilialPricRate, true,1));
					}
					views.add(view1);
					views.add(view2);
					views.add(view3);
				}
			
				
				
//				TextView tv_todaypayDate1 = (TextView) view4.findViewById(R.id.tv_payDates);
//				TextView tv_threeSunTotalFee1 = (TextView) view4.findViewById(R.id.tv_threeSunTotalFee);
//				TextView tv_twoSunTotalFee1 = (TextView) view4.findViewById(R.id.tv_twoSunTotalFee);
//				TextView tv_oneSunTotalFee1 = (TextView) view4.findViewById(R.id.tv_oneSunTotalFee);
//				tv_todaypayDate1.setText(DateUtil.getStrTime(bean.data.payDateNew));
//				tv_threeSunTotalFee1.setText(Util.getnum(bean.data.threeSunTotalFeeNew,false));
//				tv_twoSunTotalFee1.setText(Util.getnum(bean.data.twoSunTotalFeeNew,false));
//				tv_oneSunTotalFee1.setText(Util.getnum(bean.data.oneSunTotalFeeNew,false));	
//				
//				TextView tv_payDate1 = (TextView) view5.findViewById(R.id.tv_payDate);
//				TextView tv_userFilialPric1 = (TextView) view5.findViewById(R.id.tv_userFilialPric);
//				TextView tv_shopFilialPric1 = (TextView) view5.findViewById(R.id.tv_shopFilialPric);
				
//				tv_payDate1.setText( DateUtil.getStrTime(bean.data.payDateNew));
//				tv_userFilialPric.setText(Util.getnum(bean.data.userFilialPric,false));
//				tv_shopFilialPric.setText(Util.getnum(bean.data.shopFilialPric,false));
				
				
//				if(bean.data.userFilialPricRateNew!=null){
//					tv_userFilialPric1.setText(Util.getfotmatnum(bean.data.userFilialPricRateNew, true,1));
//				}
//				if(bean.data.shopFilialPricRateNew!=null){
//					tv_shopFilialPric1.setText(Util.getfotmatnum(bean.data.shopFilialPricRateNew, true,1));
//				}

			
			
//			tv_countUser.setText(bean.data.userFilialPricRate);
//			tv_countShop.setText(bean.data.shopFilialPricRate);
			}


			try {
				container.addView(views.get(position%views.size()), LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				jazzy_pager.setObjectForPosition(views.get(position%views.size()), position);
			} catch (Exception e) {
//				// TODO: handle exception
			}
//			
			try {
				return views.get(position%views.size());
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object obj) {
			
//			((ViewPager) arg0).removeView(jazzy_pager.findViewFromObject(position%views.size()));
//			container.removeView(jazzy_pager.findViewFromObject(position%views.size()));
			try {
				container.removeView(jazzy_pager.findViewFromObject(position%views.size()));
//				container.removeView(views.get(position%views.size()));
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			if (view instanceof OutlineContainer) {
				return ((OutlineContainer) view).getChildAt(0) == obj;
			} else {
				return view == obj;
			}
		}		
	}
	

	private static final boolean API_11;
	static {
		API_11 = Build.VERSION.SDK_INT >= 11;
	}

	public JazzyViewPager(Context context) {
		this(context, null);
	}

	@SuppressWarnings("incomplete-switch")
	public JazzyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		setClipChildren(false);
		// now style everything!
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.JazzyViewPager);
		int effect = ta.getInt(R.styleable.JazzyViewPager_style, 0);
		String[] transitions = getResources().getStringArray(R.array.jazzy_effects);
		setTransitionEffect(TransitionEffect.valueOf(transitions[effect]));
		setFadeEnabled(ta.getBoolean(R.styleable.JazzyViewPager_fadeEnabled, false));
		setOutlineEnabled(ta.getBoolean(R.styleable.JazzyViewPager_outlineEnabled, false));
		setOutlineColor(ta.getColor(R.styleable.JazzyViewPager_outlineColor, Color.WHITE));
		switch (mEffect) {
		case Stack:
		case ZoomOut:
			setFadeEnabled(true);
		}
		ta.recycle();
	}

	public void setTransitionEffect(TransitionEffect effect) {
		mEffect = effect;
//		reset();
	}

	public void setPagingEnabled(boolean enabled) {
		mEnabled = enabled;
	}

	public void setFadeEnabled(boolean enabled) {
		mFadeEnabled = enabled;
	}
	
	public boolean getFadeEnabled() {
		return mFadeEnabled;
	}

	public void setOutlineEnabled(boolean enabled) {
		mOutlineEnabled = enabled;
		wrapWithOutlines();
	}

	public void setOutlineColor(int color) {
		sOutlineColor = color;
	}

	private void wrapWithOutlines() {
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			if (!(v instanceof OutlineContainer)) {
				removeView(v);
				super.addView(wrapChild(v), i);
			}
		}
	}

	private View wrapChild(View child) {
		if (!mOutlineEnabled || child instanceof OutlineContainer) return child;
		OutlineContainer out = new OutlineContainer(getContext());
		out.setLayoutParams(generateDefaultLayoutParams());
		child.setLayoutParams(new OutlineContainer.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		out.addView(child);
		return out;
	}

	public void addView(View child) {
		super.addView(wrapChild(child));
	}

	public void addView(View child, int index) {
		super.addView(wrapChild(child), index);
	}

	public void addView(View child, LayoutParams params) {
		super.addView(wrapChild(child), params);
	}

	public void addView(View child, int width, int height) {
		super.addView(wrapChild(child), width, height);
	}

	public void addView(View child, int index, LayoutParams params) {
		super.addView(wrapChild(child), index, params);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return mEnabled ? super.onInterceptTouchEvent(arg0) : false;
	}

	private State mState;
	private int oldPage;

	private View mLeft;
	private View mRight;
	private float mRot;
	private float mTrans;
	private float mScale;

	private enum State {
		IDLE,
		GOING_LEFT,
		GOING_RIGHT
	}
	
//	public void reset() {
//	resetPrivate();
//	int curr = getCurrentItem();
//	onPageScrolled(curr, 0.0f, 0);
//}
//
//private void resetPrivate() {
//	for (int i = 0; i < getChildCount(); i++) {
//		View v = getChildAt(i);
//		//			ViewHelper.setRotation(v, -ViewHelper.getRotation(v));
//		//			ViewHelper.setRotationX(v, -ViewHelper.getRotationX(v));
//		//			ViewHelper.setRotationY(v, -ViewHelper.getRotationY(v));
//		//
//		//			ViewHelper.setTranslationX(v, -ViewHelper.getTranslationX(v));
//		//			ViewHelper.setTranslationY(v, -ViewHelper.getTranslationY(v));
//
//		ViewHelper.setRotation(v, 0);
//		ViewHelper.setRotationX(v, 0);
//		ViewHelper.setRotationY(v, 0);
//
//		ViewHelper.setTranslationX(v, 0);
//		ViewHelper.setTranslationY(v, 0);
//
//		ViewHelper.setAlpha(v, 1.0f);
//
//		ViewHelper.setScaleX(v, 1.0f);
//		ViewHelper.setScaleY(v, 1.0f);
//
//		ViewHelper.setPivotX(v, 0);
//		ViewHelper.setPivotY(v, 0);
//
//		logState(v, "Child " + i);
//	}
//}

	private void logState(View v, String title) {
		Log.v(TAG, title + ": ROT (" + ViewHelper.getRotation(v) + ", " +
				ViewHelper.getRotationX(v) + ", " +
				ViewHelper.getRotationY(v) + "), TRANS (" +
				ViewHelper.getTranslationX(v) + ", " +
				ViewHelper.getTranslationY(v) + "), SCALE (" +
				ViewHelper.getScaleX(v) + ", " + 
				ViewHelper.getScaleY(v) + "), ALPHA " +
				ViewHelper.getAlpha(v));
	}

	protected void animateScroll(int position, float positionOffset) {
		if (mState != State.IDLE) {
			mRot = (float)(1-Math.cos(2*Math.PI*positionOffset))/2*30.0f;
			ViewHelper.setRotationY(this, mState == State.GOING_RIGHT ? mRot : -mRot);
			ViewHelper.setPivotX(this, getMeasuredWidth()*0.5f);
			ViewHelper.setPivotY(this, getMeasuredHeight()*0.5f);
		}
	}

	protected void animateTablet(View left, View right, float positionOffset) {		
		if (mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				mRot = 30.0f * positionOffset;
				mTrans = getOffsetXForRotation(mRot, left.getMeasuredWidth(),
						left.getMeasuredHeight());
				ViewHelper.setPivotX(left, left.getMeasuredWidth()/2);
				ViewHelper.setPivotY(left, left.getMeasuredHeight()/2);
				ViewHelper.setTranslationX(left, mTrans);
				ViewHelper.setRotationY(left, mRot);
				logState(left, "Left");
			}
			if (right != null) {
				manageLayer(right, true);
				mRot = -30.0f * (1-positionOffset);
				mTrans = getOffsetXForRotation(mRot, right.getMeasuredWidth(), 
						right.getMeasuredHeight());
				ViewHelper.setPivotX(right, right.getMeasuredWidth()*0.5f);
				ViewHelper.setPivotY(right, right.getMeasuredHeight()*0.5f);
				ViewHelper.setTranslationX(right, mTrans);
				ViewHelper.setRotationY(right, mRot);
				logState(right, "Right");
			}
		}
	}

	private void animateCube(View left, View right, float positionOffset, boolean in) {
		if (mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				mRot = (in ? 90.0f : -90.0f) * positionOffset;
				ViewHelper.setPivotX(left, left.getMeasuredWidth());
				ViewHelper.setPivotY(left, left.getMeasuredHeight()*0.5f);
				ViewHelper.setRotationY(left, mRot);
			}
			if (right != null) {
				manageLayer(right, true);
				mRot = -(in ? 90.0f : -90.0f) * (1-positionOffset);
				ViewHelper.setPivotX(right, 0);
				ViewHelper.setPivotY(right, right.getMeasuredHeight()*0.5f);
				ViewHelper.setRotationY(right, mRot);
			}
		}
	}

	private void animateAccordion(View left, View right, float positionOffset) {
		if (mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				ViewHelper.setPivotX(left, left.getMeasuredWidth());
				ViewHelper.setPivotY(left, 0);
				ViewHelper.setScaleX(left, 1-positionOffset);
			}
			if (right != null) {
				manageLayer(right, true);
				ViewHelper.setPivotX(right, 0);
				ViewHelper.setPivotY(right, 0);
				ViewHelper.setScaleX(right, positionOffset);
			}
		}
	}

	private void animateZoom(View left, View right, float positionOffset, boolean in) {
		if (mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				mScale = in ? ZOOM_MAX + (1-ZOOM_MAX)*(1-positionOffset) :
					1+ZOOM_MAX - ZOOM_MAX*(1-positionOffset);
				ViewHelper.setPivotX(left, left.getMeasuredWidth()*0.5f);
				ViewHelper.setPivotY(left, left.getMeasuredHeight()*0.5f);
				ViewHelper.setScaleX(left, mScale);
				ViewHelper.setScaleY(left, mScale);
			}
			if (right != null) {
				manageLayer(right, true);
				mScale = in ? ZOOM_MAX + (1-ZOOM_MAX)*positionOffset :
					1+ZOOM_MAX - ZOOM_MAX*positionOffset;
				ViewHelper.setPivotX(right, right.getMeasuredWidth()*0.5f);
				ViewHelper.setPivotY(right, right.getMeasuredHeight()*0.5f);
				ViewHelper.setScaleX(right, mScale);
				ViewHelper.setScaleY(right, mScale);
			}
		}
	}

	private void animateRotate(View left, View right, float positionOffset, boolean up) {
		if (mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				mRot = (up ? 1 : -1) * (ROT_MAX * positionOffset);
				mTrans = (up ? -1 : 1) * (float) (getMeasuredHeight() - getMeasuredHeight()*Math.cos(mRot*Math.PI/180.0f));
				ViewHelper.setPivotX(left, left.getMeasuredWidth()*0.5f);
				ViewHelper.setPivotY(left, up ? 0 : left.getMeasuredHeight());
				ViewHelper.setTranslationY(left, mTrans);
				ViewHelper.setRotation(left, mRot);
			}
			if (right != null) {
				manageLayer(right, true);
				mRot = (up ? 1 : -1) * (-ROT_MAX + ROT_MAX*positionOffset);
				mTrans = (up ? -1 : 1) * (float) (getMeasuredHeight() - getMeasuredHeight()*Math.cos(mRot*Math.PI/180.0f));
				ViewHelper.setPivotX(right, right.getMeasuredWidth()*0.5f);
				ViewHelper.setPivotY(right, up ? 0 : right.getMeasuredHeight());
				ViewHelper.setTranslationY(right, mTrans);
				ViewHelper.setRotation(right, mRot);
			}
		}
	}

	private void animateFlipHorizontal(View left, View right, float positionOffset, int positionOffsetPixels) {
		if (mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				mRot = 180.0f * positionOffset;
				if (mRot > 90.0f) {
					left.setVisibility(View.INVISIBLE);
				} else {
					if (left.getVisibility() == View.INVISIBLE)
						left.setVisibility(View.VISIBLE);
					mTrans = positionOffsetPixels;
					ViewHelper.setPivotX(left, left.getMeasuredWidth()*0.5f);
					ViewHelper.setPivotY(left, left.getMeasuredHeight()*0.5f);
					ViewHelper.setTranslationX(left, mTrans);
					ViewHelper.setRotationY(left, mRot);
				}
			}
			if (right != null) {
				manageLayer(right, true);
				mRot = -180.0f * (1-positionOffset);
				if (mRot < -90.0f) {
					right.setVisibility(View.INVISIBLE);
				} else {
					if (right.getVisibility() == View.INVISIBLE)
						right.setVisibility(View.VISIBLE);
					mTrans = -getWidth()-getPageMargin()+positionOffsetPixels;
					ViewHelper.setPivotX(right, right.getMeasuredWidth()*0.5f);
					ViewHelper.setPivotY(right, right.getMeasuredHeight()*0.5f);
					ViewHelper.setTranslationX(right, mTrans);
					ViewHelper.setRotationY(right, mRot);
				}
			}
		}
	}
	
	private void animateFlipVertical(View left, View right, float positionOffset, int positionOffsetPixels) {
		if(mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				mRot = 180.0f * positionOffset;
				if (mRot > 90.0f) {
					left.setVisibility(View.INVISIBLE);
				} else {
					if (left.getVisibility() == View.INVISIBLE)
						left.setVisibility(View.VISIBLE);
					mTrans = positionOffsetPixels;
					ViewHelper.setPivotX(left, left.getMeasuredWidth()*0.5f);
					ViewHelper.setPivotY(left, left.getMeasuredHeight()*0.5f);
					ViewHelper.setTranslationX(left, mTrans);
					ViewHelper.setRotationX(left, mRot);
				}
			}
			if (right != null) {
				manageLayer(right, true);
				mRot = -180.0f * (1-positionOffset);
				if (mRot < -90.0f) {
					right.setVisibility(View.INVISIBLE);
				} else {
					if (right.getVisibility() == View.INVISIBLE)
						right.setVisibility(View.VISIBLE);
					mTrans = -getWidth()-getPageMargin()+positionOffsetPixels;
					ViewHelper.setPivotX(right, right.getMeasuredWidth()*0.5f);
					ViewHelper.setPivotY(right, right.getMeasuredHeight()*0.5f);
					ViewHelper.setTranslationX(right, mTrans);
					ViewHelper.setRotationX(right, mRot);
				}
			}
		}
	}

	protected void animateStack(View left, View right, float positionOffset, int positionOffsetPixels) {		
		if (mState != State.IDLE) {
			if (right != null) {
				manageLayer(right, true);
				mScale = (1-SCALE_MAX) * positionOffset + SCALE_MAX;
				mTrans = -getWidth()-getPageMargin()+positionOffsetPixels;
				ViewHelper.setScaleX(right, mScale);
				ViewHelper.setScaleY(right, mScale);
				ViewHelper.setTranslationX(right, mTrans);
			}
			if (left != null) {
				left.bringToFront();
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void manageLayer(View v, boolean enableHardware) {
		if (!API_11) return;
		int layerType = enableHardware ? View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_NONE;
		if (layerType != v.getLayerType())
			v.setLayerType(layerType, null);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void disableHardwareLayer() {
		if (!API_11) return;
		View v;
		for (int i = 0; i < getChildCount(); i++) {
			v = getChildAt(i);
			if (v.getLayerType() != View.LAYER_TYPE_NONE)
				v.setLayerType(View.LAYER_TYPE_NONE, null);
		}
	}

	private Matrix mMatrix = new Matrix();
	private Camera mCamera = new Camera();
	private float[] mTempFloat2 = new float[2];

	protected float getOffsetXForRotation(float degrees, int width, int height) {
		mMatrix.reset();
		mCamera.save();
		mCamera.rotateY(Math.abs(degrees));
		mCamera.getMatrix(mMatrix);
		mCamera.restore();

		mMatrix.preTranslate(-width * 0.5f, -height * 0.5f);
		mMatrix.postTranslate(width * 0.5f, height * 0.5f);
		mTempFloat2[0] = width;
		mTempFloat2[1] = height;
		mMatrix.mapPoints(mTempFloat2);
		return (width - mTempFloat2[0]) * (degrees > 0.0f ? 1.0f : -1.0f);
	}

	protected void animateFade(View left, View right, float positionOffset) {
		if (left != null) {
			ViewHelper.setAlpha(left, 1-positionOffset);
		}
		if (right != null) {
			ViewHelper.setAlpha(right, positionOffset);
		}
	}

	protected void animateOutline(View left, View right) {
		if (!(left instanceof OutlineContainer))
			return;
		if (mState != State.IDLE) {
			if (left != null) {
				manageLayer(left, true);
				((OutlineContainer)left).setOutlineAlpha(1.0f);
			}
			if (right != null) {
				manageLayer(right, true);
				((OutlineContainer)right).setOutlineAlpha(1.0f);
			}
		} else {
			if (left != null)
				((OutlineContainer)left).start();
			if (right != null)
				((OutlineContainer)right).start();
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		if (mState == State.IDLE && positionOffset > 0) {
			oldPage = getCurrentItem();
			mState = position == oldPage ? State.GOING_RIGHT : State.GOING_LEFT;
		}
		boolean goingRight = position == oldPage;				
		if (mState == State.GOING_RIGHT && !goingRight)
			mState = State.GOING_LEFT;
		else if (mState == State.GOING_LEFT && goingRight)
			mState = State.GOING_RIGHT;

		float effectOffset = isSmall(positionOffset) ? 0 : positionOffset;
		
//		mLeft = getChildAt(position);
//		mRight = getChildAt(position+1);
		mLeft = findViewFromObject(position);
		mRight = findViewFromObject(position+1);
		
		if (mFadeEnabled)
			animateFade(mLeft, mRight, effectOffset);
		if (mOutlineEnabled)
			animateOutline(mLeft, mRight);
		
		switch (mEffect) {
		case Standard:
			break;
		case Tablet:
			animateTablet(mLeft, mRight, effectOffset);
			break;
		case CubeIn:
			animateCube(mLeft, mRight, effectOffset, true);
			break;
		case CubeOut:
			animateCube(mLeft, mRight, effectOffset, false);
			break;
		case FlipVertical:
			animateFlipVertical(mLeft, mRight, positionOffset, positionOffsetPixels);
			break;
		case FlipHorizontal:
			animateFlipHorizontal(mLeft, mRight, effectOffset, positionOffsetPixels);
		case Stack:
			animateStack(mLeft, mRight, effectOffset, positionOffsetPixels);
			break;
		case ZoomIn:
			animateZoom(mLeft, mRight, effectOffset, true);
			break;
		case ZoomOut:
			animateZoom(mLeft, mRight, effectOffset, false);
			break;
		case RotateUp:
			animateRotate(mLeft, mRight, effectOffset, true);
			break;
		case RotateDown:
			animateRotate(mLeft, mRight, effectOffset, false);
			break;
		case Accordion:
			animateAccordion(mLeft, mRight, effectOffset);
			break;
		}

		super.onPageScrolled(position, positionOffset, positionOffsetPixels);

		if (effectOffset == 0) {
			disableHardwareLayer();
			mState = State.IDLE;
		}

	}

	private boolean isSmall(float positionOffset) {
		return Math.abs(positionOffset) < 0.0001;
	}
	
	public void setObjectForPosition(Object obj, int position) {
		mObjs.put(Integer.valueOf(position), obj);
	}
	
	public View findViewFromObject(int position) {
		Object o = mObjs.get(Integer.valueOf(position));
		if (o == null) {
			return null;
		}
		PagerAdapter a = getAdapter();
		View v;
		for (int i = 0; i < getChildCount(); i++) {
			v = getChildAt(i);
			if (a.isViewFromObject(v, o))
				return v;
		}
		return null;
	}
	
	
	
	
}