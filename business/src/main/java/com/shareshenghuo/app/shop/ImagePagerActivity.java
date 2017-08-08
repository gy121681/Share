package com.shareshenghuo.app.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle.Control;

import com.shareshenghuo.app.shop.adapter.PhotoPagerAdapter;
import com.shareshenghuo.app.shop.network.bean.PhotoInfo;
import com.shareshenghuo.app.shop.util.TransferTempDataUtil;
import com.shareshenghuo.app.shop.widget.HackyViewPager;

import android.app.Notification.Action;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * @author hang
 * 图片浏览
 */
public class ImagePagerActivity extends BaseTopActivity implements OnTouchListener{
	
	private TextView tvCount,tvs;
	private HackyViewPager viewPager;
	
	private PhotoPagerAdapter adapter;
	
	private String title;
	private List<PhotoInfo> photos;
	private ArrayList<String> urls;
	private int photoCount;
	private LinearLayout lls;
	private int curPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_pager);
		initData();
		initView();
	}
	
	public void initData() {
		title = getIntent().getStringExtra("title");
		curPosition = getIntent().getIntExtra("position", 0);
		urls = getIntent().getStringArrayListExtra("urls");
		photos = (List<PhotoInfo>) TransferTempDataUtil.getInstance().getData();
		TransferTempDataUtil.getInstance().recycle();
		
		if(urls==null && photos!=null) {
			urls = new ArrayList<String>();
			for(PhotoInfo item : photos)
				urls.add(item.shop_photo);
		}
	}
	
	public void initView() {
//		initTopBar(title);
		tvCount = getView(R.id.tvImageCount);
		viewPager = getView(R.id.pagerImage);
		tvs = getView(R.id.tvs);
		lls= getView(R.id.lls);
		
		lls.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tvs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		viewPager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		photoCount = urls.size();
		tvCount.setText((curPosition+1)+"/"+photoCount);
		
		adapter = new PhotoPagerAdapter(urls,ImagePagerActivity.this);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				tvCount.setText((position+1)+"/"+photoCount);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		viewPager.setCurrentItem(curPosition);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		int a = arg1.getAction();
		Log.e("", " = = = ==   a    = =     "+a);
		return false;
	}
	
	
}
