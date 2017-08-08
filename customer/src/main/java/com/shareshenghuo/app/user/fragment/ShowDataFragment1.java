package com.shareshenghuo.app.user.fragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.shareshenghuo.app.user.ConsumptionSeriesActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.request.BannerRequest;
import com.shareshenghuo.app.user.network.response.BannerListResp;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.DateUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.Util;
import com.shareshenghuo.app.user.widget.JazzyViewPager;
import com.shareshenghuo.app.user.widget.JazzyViewPager.TransitionEffect;
import com.shareshenghuo.app.user.widget.pager.OutlineContainer;
import com.shareshenghuo.app.user.widget.pager.ViewPagerScroller;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;


public class ShowDataFragment1 extends BaseFragment {
	
	 private final Handler mHandler = new Handler();  
	private View view;
	private ArrayList<View> views;
	
	private JazzyViewPager jazzy_pager;
	private MainAdapter adapter;
	private ImageView img_bg;
	private HorizontalScrollView scroll;
	private int scrollwidth = 0;
	private int scrollmove = 0;
	private int page = 0;
	private int currentPage = 0;
	private  BannerListResp bean = null;
	private LayoutInflater inflater;
	private RadioButton btn_1,btn_2;
	private TextView tvTopTitle,tv_date;
	MyAdapter adapter1;
	private View stopdata;
	
	@Override
	protected int getLayoutId() {
		return R.layout.show_data_frag;
	}

	@Override
	protected void init(View rootView) {
		// TODO Auto-generated method stub
		inflater = getActivity().getLayoutInflater();
		img_bg = getView(R.id.img_bg);
		scroll = getView(R.id.scroll);
		btn_1 = getView(R.id.btn_1);
		btn_2 = getView(R.id.btn_2);
		stopdata = getView(R.id.stopdata);
		tv_date = getView(R.id.tv_date);
		tvTopTitle = getView(R.id.tvTopTitle);
		tvTopTitle.setText("数据展示");
		
		
//		if(bean==null){
			getBannerList();
//			}else{
//				if(bean.data.isStop.equals("1")){
//					stopdata.setVisibility(View.VISIBLE);
//					if(bean.data.openDate!=null){
//						tv_date.setText(bean.data.openDate);
//					}
//				}else{
//					stopdata.setVisibility(View.GONE);
//					initpager(bean);
//				}
////			initpager(bean);
//		}
		
		btn_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				T.showShort(activity, "暂未开通");
			}
		});
		btn_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!UserInfoManager.isLogin(activity)) {
					T.showShort(activity, "您当前未登录，请先登录");
//					startActivityForResult(new Intent(activity, LoginActivity.class), HomeFragment.0x102);
					return;
				}
				
				Intent intent = new Intent();
				intent.setClass(activity, ConsumptionSeriesActivity.class);
//				if(bean!=null&&bean.data!=null){
//					String sumTotalConsumption =bean.data.sumTotalConsumptionNew+"";
//					if(sumTotalConsumption!=null){
//						intent.putExtra("sumTotalConsumption", Util.getnum(sumTotalConsumption,true));
//					}
//				}
				startActivity(intent);
				
			}
		});
	}
	
	
	private void initpager(BannerListResp bean) {
		// TODO Auto-generated method stub

		
		views = new ArrayList<View>();
//		View view1 = inflater.inflate(R.layout.banner_layout_1, null);
//		View view2 = inflater.inflate(R.layout.banner_layout_2, null);
//		View view3 = inflater.inflate(R.layout.banner_layout_3, null);
//		if(bean != null&&bean.data!=null){
//		
//			
//		TextView tv_todaypayDate = (TextView) view1.findViewById(R.id.tv_payDate);
//		TextView tv_threeSunTotalFee = (TextView) view1.findViewById(R.id.tv_threeSunTotalFee);
//		TextView tv_twoSunTotalFee = (TextView) view1.findViewById(R.id.tv_twoSunTotalFee);
//		TextView tv_oneSunTotalFee = (TextView) view1.findViewById(R.id.tv_oneSunTotalFee);
//		tv_todaypayDate.setText( DateUtil.Yesterday());
//		tv_threeSunTotalFee.setText(Util.getnum(bean.data.threeSunTotalFee,true));
//		tv_twoSunTotalFee.setText(Util.getnum(bean.data.twoSunTotalFee,true));
//		tv_oneSunTotalFee.setText(Util.getnum(bean.data.oneSunTotalFee,true));
//		
//		
//		TextView tv_sumTotalConsumption = (TextView) view2.findViewById(R.id.tv_sumTotalConsumption);
//		TextView tv_countUser = (TextView) view2.findViewById(R.id.tv_countUser);
//		TextView tv_countShop = (TextView) view2.findViewById(R.id.tv_countShop);
//		tv_sumTotalConsumption.setText(Util.getnum(bean.data.sumTotalConsumption,true));
//		tv_countUser.setText(bean.data.countUser);
//		tv_countShop.setText(bean.data.countShop);
//		
//		
//		TextView tv_payDate = (TextView) view3.findViewById(R.id.tv_payDate);
//		TextView tv_userFilialPric = (TextView) view3.findViewById(R.id.tv_userFilialPric);
//		TextView tv_shopFilialPric = (TextView) view3.findViewById(R.id.tv_shopFilialPric);
//		
//		tv_payDate.setText( DateUtil.getStrTime(bean.data.payDate));
//		tv_userFilialPric.setText(Util.getnum(bean.data.userFilialPric,false));
//		tv_shopFilialPric.setText(Util.getnum(bean.data.shopFilialPric,false));
//		}
//
//		
//		
//		views.add(view1);
//		views.add(view2);
//		views.add(view3);
		
//		views.add(inflater.inflate(R.layout.textimagelayout, null));
//		views.add(inflater.inflate(R.layout.textimagelayout, null));
//		views.add(inflater.inflate(R.layout.textimagelayout, null));
//		views.add(inflater.inflate(R.layout.textimagelayout, null));
//		views.add(inflater.inflate(R.layout.textimagelayout, null));
		
//		initscroll();
		jazzy_pager = getView(R.id.jazzy_pager);
		try {
			setupJazziness(TransitionEffect.CubeOut);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
//		jazzy_pager.setupJazziness(TransitionEffect.CubeOut, jazzy_pager, activity, views);
//		jazzy_pager.startpager();
		
		
	}
	
	private void initscroll() {
		// TODO Auto-generated method stub
		new Handler().post(new Runnable() {
		    @Override
		    public void run() {
		    	scroll.fullScroll(View.FOCUS_RIGHT);
		    }
		});
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		img_bg.measure(w, h);
		scrollwidth = img_bg.getMeasuredWidth();
		scrollmove = scrollwidth/views.size();
	}
	
	public void getBannerList() {
		BannerRequest req = new BannerRequest();
		try {
			req.city_id = UserInfoManager.getUserInfo(activity).city_id + "";
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		req.banner_place = "1";
//		req.city_id = "";
//		req.banner_place = "";
		RequestParams params = new RequestParams();
//		try {
////			params.setBodyEntity(new StringEntity());
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		new HttpUtils().send(HttpMethod.GET, Api.GETBANNER, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						 bean = new Gson().fromJson(resp.result, BannerListResp.class);
						 Log.e("", ""+resp.result);
						if(Api.SUCCEED == bean.result_code) {
							if(bean.data.isStop!=null&&bean.data.isStop.equals("1")){
								stopdata.setVisibility(View.VISIBLE);
								if(bean.data.openDate!=null){
								}
								tv_date.setText(bean.data.openDate+"开市");
							}else{
								stopdata.setVisibility(View.GONE);
								initpager(bean);
							}
						}else{
//							initpager(bean);
						}
					}
					@Override
					public void onFailure(HttpException arg0, String arg1) {
//						initpager(bean);
					}
				});
	}
	

	private void setupJazziness(TransitionEffect effect) {
		jazzy_pager = getView(R.id.jazzy_pager);
		jazzy_pager.setTransitionEffect(effect);
		new ViewPagerScroller(getActivity(),1000).initViewPagerScroll(jazzy_pager);
//		adapter = new MainAdapter();
//		adapter1 = new AdapterCycle(activity, jazzy_pager);
		 adapter1 = new MyAdapter();
		jazzy_pager.setAdapter(adapter1);
//		jazzy_pager.setAdapter(adapter1);
//		jazzy_pager.setPageMargin(0);
		
	    jazzy_pager.setCurrentItem(300); 
	    jazzy_pager.setCurrentItem(1);
//		jazzy_pager.setCurrentItem(300);
//		jazzy_pager.setCurrentItem(adapter.getCount()/2); // 默认选中中间位置为起始位置
		handlers.postDelayed(runnable,2000);
		jazzy_pager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@SuppressLint("NewApi") @Override
			public void onPageSelected(int arg0) {
				page = arg0;
				
			}
			
			@SuppressLint("NewApi") @Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
					if(arg1!=0){
//						scroll.setScrollX(a);
//						scroll.scrollTo(scrollmove*page+(int)(100/arg1), 0);
//						scroll.scrollTo((int)(scrollmove*(5-page)*arg1),0); 
//						scroll.scrollTo((scrollmove*(3-page))+(int)(scrollmove*-arg1),0); 
					}
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		initrunpager();
		
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
					
					handlers.postDelayed(runnable, 2000);
				}
				
				return false;
			}
		});

	}
	
	
	
	
	
	private void initrunpager() {
		// TODO Auto-generated method stub
		
   	 final Timer timer = new Timer();
	      TimerTask task = new TimerTask(){
	         @Override
			public void run(){
	
	         }
	      };
	      timer.schedule(task, 0,2000);
	}

	
	final Handler handlers = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			scrollToNext();
			handlers.postDelayed(this, 5000);// 延时时长
		}
	};
	
	public void stop(){
		handlers.removeCallbacks(runnable);
	}
	
	private void scrollToNext() {
		// TODO Auto-generated method stub
		
//		int count = adapter1.getCount(); 
//		if (count > 2) { // 实际上，多于1个，就多于3个  
//		    int index = jazzy_pager.getCurrentItem();  
//		    index = index % (count - 2) + 1; //这里修改过  
//		    jazzy_pager.setCurrentItem(index, true);  
 		int count = adapter1.getCount();  
		if (count > 1) { // 多于1个，才循环  
		    int index = jazzy_pager.getCurrentItem();  
		    index = (index + 1) % count;  
		    jazzy_pager.setCurrentItem(index, true);  
		}	
	}


	private class MainAdapter extends PagerAdapter {
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
//			 if (container != null) {
//				 container.removeView(views.get(position % views.size()));
//			 } 
//			((ViewPager) container).addView(views.get(position % views.size()));
			try {
				container.addView(views.get(position), LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				return views.get(position);
			} catch (Exception e) {
				// TODO: handle exception
			}
//			jazzy_pager.setObjectForPosition(views.get(position % views.size()), position);
			
//			return views.get(position % views.size()); // 返回ImageView
			return null;
			
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object obj) {
			
			
			try {
				container.removeView((View) obj);
//				container.removeView(views.get(position%views.size()));
			} catch (Exception e) {
				// TODO: handle exception
			}
			
//			container.removeView(jazzy_pager.findViewFromObject(position));
		}
		@Override
		public int getCount() {
			
//			if (views.size() < 2)// 如果只有一张图时不滚动
//				return views.size();
//			return Integer.MAX_VALUE;
			return views.size();
		}
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
//			if (view instanceof OutlineContainer) {
//				return ((OutlineContainer) view).getChildAt(0) == obj;
//			} else {
//				return view == obj;
//			}
		}		
	}
	
	
	
	class MyAdapter extends PagerAdapter {


		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View view, Object arg1) {
//			return view == arg1;
			if (view instanceof OutlineContainer) {
				return ((OutlineContainer) view).getChildAt(0) == arg1;
			} else {
				return view == arg1;
			}
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
//			views.remove(arg1%views.size());
			try {
				((ViewPager) arg0).removeView(jazzy_pager.findViewFromObject(arg1%views.size()));
			} catch (Exception e) {
				// TODO: handle exception
			}
			
//			((ViewPager) arg0).removeView(views.get(arg1%views.size()));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			View view1 = inflater.inflate(R.layout.banner_layout_new1, null);
			View view2 = inflater.inflate(R.layout.banner_layout_new2, null);
			View view3 = inflater.inflate(R.layout.banner_layout_new3, null);
			if(bean != null&&bean.data!=null){
				
				if(bean.data.payDateNew!=null){

			
			
				
			TextView tv_todaypayDate = (TextView) view1.findViewById(R.id.tv_payDates);
			TextView tv_threeSunTotalFee = (TextView) view1.findViewById(R.id.tv_threeSunTotalFee);
			TextView tv_twoSunTotalFee = (TextView) view1.findViewById(R.id.tv_twoSunTotalFee);
			TextView tv_oneSunTotalFee = (TextView) view1.findViewById(R.id.tv_oneSunTotalFee);
			tv_todaypayDate.setText(DateUtil.getStrTime(bean.data.payDateNew));
			tv_threeSunTotalFee.setText(Util.getfotmatnum(bean.data.threeSunTotalFeeNew,true,1));
			tv_twoSunTotalFee.setText(Util.getfotmatnum(bean.data.twoSunTotalFeeNew,true,1));
			tv_oneSunTotalFee.setText(Util.getfotmatnum(bean.data.oneSunTotalFeeNew,true,1));
			
			
			TextView tv_sumTotalConsumption = (TextView) view2.findViewById(R.id.tv_sumTotalConsumption);
			TextView tv_countUser = (TextView) view2.findViewById(R.id.tv_countUser);
			TextView tv_countShop = (TextView) view2.findViewById(R.id.tv_countShop);
			tv_sumTotalConsumption.setText(Util.getnum(bean.data.sumTotalConsumptionNew,true));
			tv_countUser.setText(bean.data.countUser);
			tv_countShop.setText(bean.data.countShop);
			
			
			TextView tv_payDate = (TextView) view3.findViewById(R.id.tv_payDate);
			TextView tv_userFilialPric = (TextView) view3.findViewById(R.id.tv_userFilialPric);
			TextView tv_shopFilialPric = (TextView) view3.findViewById(R.id.tv_shopFilialPric);
			
			tv_payDate.setText( DateUtil.getStrTime(bean.data.payDateNew));
//			tv_userFilialPric.setText(Util.getnum(bean.data.userFilialPric,false));
//			tv_shopFilialPric.setText(Util.getnum(bean.data.shopFilialPric,false));
			
			
			if(bean.data.userFilialPricRateNew!=null){
				tv_userFilialPric.setText(Util.getfotmatnum(bean.data.userFilialPricRateNew, true,1));
			}
			if(bean.data.shopFilialPricRateNew!=null){
				tv_shopFilialPric.setText(Util.getfotmatnum(bean.data.shopFilialPricRateNew, true,1));
			}
		
			
				}
			}
			views.add(view1);
			views.add(view2);
			views.add(view3);
			try{
				((ViewPager) arg0).addView(views.get(arg1%views.size()));
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			jazzy_pager.setObjectForPosition(views.get(arg1 % views.size()), arg1);

			return views.get(arg1%views.size());

		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}
	

	
	
	//背景滚动
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		stop();
	}
	
	private Runnable mScrollToBottom = new Runnable() 
    {   
        @Override  
        public void run()
        {   
            int off = scrollmove;   
            if (off > 0) 
            {   
            	scroll.scrollTo(off, 0);   
            }                          
        }   
    }; 
}
