package com.shareshenghuo.app.user.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.ImageLoadManager;
import com.shareshenghuo.app.user.network.bean.Banner;
import com.shareshenghuo.app.user.network.bean.WebLoadActivity;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.Arith;
import com.shareshenghuo.app.user.util.BitmapTool;

public class BannerViewPager<T extends Banner> extends RelativeLayout {

	private Context mContext;
	
	private View view;
	private ViewPager pagerGuide;
	private LinearLayout llSlideDot;
	
	private List<T> banners;
	private List<ImageView> dots;
	private int pagerCount;
	private int currentIndex;
	
	private Handler mHandler;
	private boolean isRolling;
	
	public boolean hasDownBG = false;
	
	public BannerViewPager(Context context)
	{
		super(context);
		init(context);
	}
	
	public BannerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context)
	
	
	{
		this.mContext = context;
		view = LayoutInflater.from(mContext).inflate(R.layout.view_guide_pager, this, true);
	}
	
	public void createView(List<T> banners)
	{
	
		if(banners == null || banners.size()<=0)
			return;
		
		pagerGuide = (ViewPager)view.findViewById(R.id.pagerGuide);
		llSlideDot = (LinearLayout)view.findViewById(R.id.llSlideDot);
		if(hasDownBG)
			llSlideDot.setBackgroundResource(R.drawable.banner_down);
		this.banners = banners;
		pagerCount = banners.size();
		dots = new ArrayList<ImageView>();
		isRolling = false;
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				int i = (currentIndex+1) % pagerCount;
				pagerGuide.setCurrentItem(i);
				if(isRolling)
					mHandler.sendEmptyMessageDelayed(1, 3000);
			}
		};
		
		initDot();
		initPager();
	}
	
	private void initDot()
	{
		llSlideDot.removeAllViews();
		for(int i=0; i<pagerCount; i++)
		{
			ImageView dot = new ImageView(mContext);
			dot.setBackgroundResource(R.drawable.gray_circle);
			int side = BitmapTool.dp2px(mContext, 5);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(side, side);
			params.setMargins(5, 5, 5, 5);
			dot.setLayoutParams(params);
			dots.add(dot);
			llSlideDot.addView(dot);
		}
		dots.get(0).setBackgroundResource(R.drawable.gree_circle);
	}
	
	private void initPager()
	{
		pagerGuide.setAdapter(new GuidePagerAdapter(mContext, banners));
		pagerGuide.setCurrentItem(0);
		pagerGuide.setOnPageChangeListener(new OnGuidePagerChangeListener());
	}
	
	public void startRoll() {
		if(pagerCount>1 && !isRolling) {
			isRolling = true;
			mHandler.sendEmptyMessageDelayed(1, 3000);
		}
	}
	
	public void stopRoll() {
		isRolling = false;
	}
	
	class GuidePagerAdapter extends PagerAdapter {
		
		private List<T> data;
		private List<ImageView> imgList;
		
		public GuidePagerAdapter(Context context, List<T> data) {
			super();
			this.data = data;
			imgList = new ArrayList<ImageView>();
			for(Banner banner : data) {
				ImageView img = new ImageView(context);
				img.setClickable(true);
				img.setScaleType(ScaleType.CENTER_CROP);
				ImageLoadManager.getInstance(mContext).displayImage(banner.banner_imgurl, img);
				img.setOnClickListener(new OnBannerClickListener(banner));
				imgList.add(img);
			}
		}
		
		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(imgList.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(imgList.get(position), 0);
			return imgList.get(position);
		}
	}
	
	class OnBannerClickListener implements OnClickListener {
		Banner banner;
		
		public OnBannerClickListener(Banner banner) {
			this.banner = banner;
		}

		@Override
		public void onClick(View v) {
			if(banner.is_go == 1) {
				String url = Api.URL_BANNER_DETAIL + "?banner_id=" + banner.id;
				Intent it = new Intent(mContext, WebLoadActivity.class);
				it.putExtra("title", "详情");
				it.putExtra("url", url);
				mContext.startActivity(it);
			}
		}
	}
	
	class OnGuidePagerChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int selectedIndex) {
			// TODO Auto-generated method stub
			dots.get(selectedIndex).setBackgroundResource(R.drawable.gree_circle);
			dots.get(currentIndex).setBackgroundResource(R.drawable.gray_circle);
			currentIndex = selectedIndex;
		}
	}
	
	public void setLayoutWidthHeight(double ratio) {
		setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, (int) Arith.mul(BitmapTool.getScreenWidthPX(mContext), ratio)));
	}
	
	public void setRelativeLayoutWidthHeight(double ratio) {
		setLayoutParams(new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, (int) Arith.mul(BitmapTool.getScreenWidthPX(mContext), ratio)));
	}
}
