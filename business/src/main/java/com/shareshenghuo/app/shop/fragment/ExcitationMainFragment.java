package com.shareshenghuo.app.shop.fragment;

import java.util.ArrayList;
import java.util.List;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.TransferActivityList;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;


public class ExcitationMainFragment extends BaseFragment implements   OnClickListener{

	private ViewPager mPaper;
	private FragmentPagerAdapter mAdapter;
	private RadioButton btn_1,btn_2;
	private List<Fragment> mFragments = new ArrayList<Fragment>();
	private ExcitationFragment1 fm1;
	private ExcitationFragment2 fm2 ;
	private Button tv_choosedate;
	private OnArticleSelectedListener Listener;
	private Button btnTopRight1;
	private int tag = 0;
	@Override
	public void onClick(View arg0) {
		
	}
	
	   public interface OnArticleSelectedListener {
	        public void onArticleSelected();
	    }

	@Override
	protected int getLayoutId() {
		return R.layout.integral_main_fragment;
	}

	@Override
	protected void init(View rootView) {
		
		initTopBar("产业链秀心", "秀儿秀心");
		tv_choosedate = getView(R.id.tv_choosedate);
		mPaper = getView(R.id.view_pager);
		btn_1 = getView(R.id.btn_1);
		btn_1.setChecked(true);
		btn_1.setTextColor(getResources().getColor(R.color.black));
		btn_2 = getView(R.id.btn_2);
		tv_choosedate.setVisibility(View.GONE);
		btnTopRight1 = getView(R.id.btnTopRight1);
		btnTopRight1.setVisibility(View.GONE);
		btnTopRight1.setText("");
		btnTopRight1.setVisibility(View.VISIBLE);
//		btnTopRight1.setOnClickListener(new OnClickListener() {//转让列表
//		@Override
//		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//			if(tag==0){
//				Intent it = new Intent(activity,TransferActivityList.class);
//				it.putExtra("tag", "0");
//				startActivity(it);
//			}else{
//				Intent it = new Intent(activity,TransferActivityList.class);
//				it.putExtra("tag", "1");
//				startActivity(it);
//			}
//
//		}
//	});//秀心接收屏蔽掉了.20170531 wyp
		
		
		btn_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				btn_1.setTextColor(getResources().getColor(R.color.black));
				btn_2.setTextColor(getResources().getColor(R.color.black));
				mPaper.setCurrentItem(0);
			}
		});
		
		btn_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				btn_1.setTextColor(getResources().getColor(R.color.black));
				btn_2.setTextColor(getResources().getColor(R.color.black));
				mPaper.setCurrentItem(1);
			}
		});
		
		tv_choosedate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				fm2.setdate();
//				HistoryDataFragment fragmentObject = (Fragment).getFragmentManager.findFragmentByTag("xx")
			}
		});
		
		
		initlayout();
		mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {

			@Override
			public int getCount() {
				return mFragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return mFragments.get(arg0);
			}
		};
		mPaper.setAdapter(mAdapter);
		mPaper.setOnPageChangeListener(new OnPageChangeListener() {

			private int currentIndex;

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					tag = 0;
//					tv_choosedate.setVisibility(View.GONE);
					btn_1.setChecked(true);
					btn_1.setTextColor(getResources().getColor(R.color.black));
					btn_2.setTextColor(getResources().getColor(R.color.black));
					break;
				case 1:
					tag = 1;
//					tv_choosedate.setVisibility(View.VISIBLE);
					btn_2.setChecked(true);
					btn_1.setTextColor(getResources().getColor(R.color.black));
					btn_2.setTextColor(getResources().getColor(R.color.black));
					break;
				default:
					break;
				}
				currentIndex = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		
	}
//	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	private void initlayout() {
		// TODO Auto-generated method stub
		
		FragmentTransaction ft = obtainFragmentTransaction(0);
		
        //如果tabFragment1为空，说明还没创建Tab1
        if(fm1==null){
        	fm1 = new ExcitationFragment1();
        }
        //如果isAdded == true 表示 tab1 已加入布局中
        if(!fm1.isAdded()){
//        	ft.add(0, fm1);
        	mFragments.add(fm1);
        }
        else{
        	
            //如果tab2不为空，把tab2隐藏就是、
            if(fm2!=null){
            	 ft.hide(fm2);
            }
            //Log.v("rush_yu", "hh");
            //显示tab1
            ft.show(fm1);
        }
        
        
        //如果tabFragment2为空，说明还没创建Tab2
        if(fm2==null){
        	 fm2 = new ExcitationFragment2();
        }
        //如果isAdded == true 表示 tab2 已加入布局中
        if(!fm2.isAdded()){
//        	ft.add(1, fm2);
        	mFragments.add(fm2);
        }
        else{
            //如果tab2不为空，把tab1隐藏就是、
            if(fm1!=null){
            	ft.hide(fm1);
            }
            //显示tab2
            ft.show(fm2);
            //Log.v("rush_yu", "hh1");
        }
//		mFragments.add(fm2);
	}

	@Override
	public void onStart() {
		super.onStart();
		mPaper.setCurrentItem(1);
	}

	private FragmentTransaction obtainFragmentTransaction(int i) {
		// TODO Auto-generated method stub
		
		
		FragmentTransaction ft = this.getActivity().getSupportFragmentManager()
				.beginTransaction();
		return ft;
	}

	/**
     * ViewPager适配器
    */ 
    public class MyPagerAdapter extends PagerAdapter { 
        public List<Activity> mListViews; 
 
        public MyPagerAdapter(List<Activity> mListViews) { 
            this.mListViews = mListViews; 
        } 
 
       
        @Override 
        public void finishUpdate(View arg0) { 
        } 
 
        @Override 
        public int getCount() { 
            return mListViews.size(); 
        } 
 
      
 
        @Override 
        public boolean isViewFromObject(View arg0, Object arg1) { 
            return arg0 == (arg1); 
        } 
 
        @Override 
        public void restoreState(Parcelable arg0, ClassLoader arg1) { 
        } 
 
        @Override 
        public Parcelable saveState() { 
            return null; 
        } 
 
        @Override 
        public void startUpdate(View arg0) { 
        } 
    } 
}

