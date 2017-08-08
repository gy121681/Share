package com.shareshenghuo.app.user.widget.pager;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.widget.Scroller;

public class ViewPagerScroller  extends Scroller{
	private int mScrollDuration = 1000;             // 滑动速度
	  
    /**
     * 设置速度速度
     * @param duration
     */
    public void setScrollDuration(int duration){
        this.mScrollDuration = duration;
    }
      
    public ViewPagerScroller(Context context,int duration) {
        super(context);
        this.mScrollDuration = duration;
    }
  
//    public ViewPagerScroller(Context context, Interpolator interpolator) {
//        super(context, interpolator);
//    }
//  
//    public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
//        super(context, interpolator, flywheel);
//    }
  
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }
  
    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }
  
      
      
    public void initViewPagerScroll(ViewPager viewPager) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager, this);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
