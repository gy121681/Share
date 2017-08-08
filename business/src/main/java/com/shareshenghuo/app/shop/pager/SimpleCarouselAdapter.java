package com.shareshenghuo.app.shop.pager;

import com.shareshenghuo.app.shop.widget.JazzyViewPager;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//public class SimpleCarouselAdapter extends CarouselPagerAdapter<JazzyViewPager>{
//	
//        private int[] viewResIds;
//        private JazzyViewPager viewPagers;
//
//        public SimpleCarouselAdapter(JazzyViewPager viewPager, int[] viewResIds) {
//            super(viewPager);
//            this.viewPagers = viewPager;
//            this.viewResIds = viewResIds;
//        }
//
//        @Override
//        public int getRealDataCount() {
//            return viewResIds != null ? viewResIds.length : 0;
//        }
//
//        @Override
//        public Object instantiateRealItem(ViewGroup container, int position) {
//            int resId = viewResIds[position];
//            View bannerView = LayoutInflater.from(container.getContext()).inflate(resId, null);
//            container.addView(bannerView, ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT);
//            viewPagers.setObjectForPosition(bannerView, position);
//            return bannerView;
//    }
//
//}

public class SimpleCarouselAdapter extends PagerAdapter{
	
    private int[] viewResIds;
    private JazzyViewPager viewPagers;
    private static final int COEFFICIENT = 10;

    public SimpleCarouselAdapter(JazzyViewPager viewPager, int[] viewResIds) {
//        super(viewPager);
        this.viewPagers = viewPager;
        this.viewResIds = viewResIds;
    }

    public int getRealDataCount() {
        return viewResIds != null ? viewResIds.length : 0;
    }
    
    @Override
    public final int getCount() {
//    	return viewResIds.length;
//    	return viewResIds != null ? viewResIds.length : 0;
        long realDataCount =  viewResIds.length;
        if (realDataCount > 1) {
            realDataCount =  viewResIds.length * COEFFICIENT;
            realDataCount = realDataCount > Integer.MAX_VALUE ? Integer.MAX_VALUE : realDataCount;
        }
        Log.e("", "realDataCount - - - - - "+realDataCount);
        return (int) realDataCount;
        
    }
    
    @Override
    public final boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    
//    @Override
//    public final Object instantiateItem(ViewGroup container, int position) {
//        position = position % getRealDataCount();
//        viewPagers.setObjectForPosition(container.getChildAt(position), position);
//        return this.instantiateRealItem(container, position);
//    }
    
    @Override
    public final void destroyItem(ViewGroup container, int position, Object object) {
    	
    	container.removeView(viewPagers.findViewFromObject(position));
//        container.removeView((View) object);
    }

    
    @Override
    public final void finishUpdate(ViewGroup container) {
        // 数量为1，不做position替换
        if (getCount() <= 1) {
            return;
        }

        int position = viewPagers.getCurrentItem();
        // ViewPager的更新即将完成，替换position，以达到无限循环的效果
        if (position == 0) {
            position = getRealDataCount();
            viewPagers.setCurrentItem(position, false);
        } else if (position == getCount() - 1) {
            position = getRealDataCount() - 1;
            viewPagers.setCurrentItem(position, false);
        }
    }
    
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

    	position = position % getRealDataCount();
    	
    	Log.e("", " position - - - - - "+position);
    	int  resId = viewResIds[position];
        View bannerView = LayoutInflater.from(container.getContext()).inflate(resId, null);
        viewPagers.setObjectForPosition(bannerView, position);
        container.addView(bannerView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
      
        return bannerView;
}

}
