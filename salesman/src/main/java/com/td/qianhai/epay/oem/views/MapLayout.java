package com.td.qianhai.epay.oem.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
* 解决百度地图在主scrollview中滑动冲突的问题
* 由于MapView被定义成final class，所以只能在容器中操作了
*/
public class MapLayout extends LinearLayout {
    private ScrollView scrollView;
    public MapLayout(Context context) {
        super(context);
    }

    public MapLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollView(ScrollView scrollView) {
        this.scrollView = scrollView;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	if(scrollView!=null){
    	
    	
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            scrollView.requestDisallowInterceptTouchEvent(false);
        } else {
            scrollView.requestDisallowInterceptTouchEvent(true);
        }
    	}
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}

