package com.shareshenghuo.app.user;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.PhotoPagerAdapter;
import com.shareshenghuo.app.user.network.bean.PhotoInfo;
import com.shareshenghuo.app.user.util.TransferTempDataUtil;
import com.shareshenghuo.app.user.widget.HackyViewPager;

/**
 * @author hang
 * 图片浏览
 */
public class ImagePagerActivity extends BaseTopActivity {
	
	private TextView tvCount;
	private HackyViewPager viewPager;
	
	private PhotoPagerAdapter adapter;
	
	private String title;
	private List<PhotoInfo> photos;
	private ArrayList<String> urls;
	private int photoCount;
	
	private int curPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_pager);
		urls = new ArrayList<String>();
		initData();
		initView();
	}
	
	@SuppressWarnings("unchecked")
	public void initData() {
		title = getIntent().getStringExtra("title");
		curPosition = getIntent().getIntExtra("position", 0);
		urls = getIntent().getStringArrayListExtra("urls");
		photos = (List<PhotoInfo>) TransferTempDataUtil.getInstance().getData();
		TransferTempDataUtil.getInstance().recycle();
		
		if(photos!=null) {
			if(urls==null){
				urls = new ArrayList<String>();
			}
			for(PhotoInfo item : photos)
				urls.add(item.shop_photo);
		}
	}
	
	public void initView() {
		initTopBar(title);
		tvCount = getView(R.id.tvImageCount);
		viewPager = getView(R.id.pagerImage);
		
		photoCount = urls.size();
		tvCount.setText((curPosition+1)+"/"+photoCount);
		
		adapter = new PhotoPagerAdapter(urls);
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
}
