package com.shareshenghuo.app.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.network.bean.WebLoadFragment;

/**
 * @author hang
 * 地图导航
 */
public class ShopNavActivity extends BaseTopActivity {
	
	private String API_KEY = "b1efb6ea0fd0a157a312ba3fec79109f";
	
	private WebLoadFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_nav);
		initView();
	}

	public void initView() {
		String title = getIntent().getStringExtra("title");
		String addr = getIntent().getStringExtra("addr");
		double lat = getIntent().getDoubleExtra("lat", 0);
		double lng = getIntent().getDoubleExtra("lng", 0);
		
		initTopBar(title);
		
//		String url = "http://m.amap.com/navi/?dest="+lng+","+lat+"&destName="+title+"&key="+API_KEY;
		CityManager cm = CityManager.getInstance(this);
		String url = "http://m.amap.com/?from="+cm.latitude+","+cm.longitude+"(我)&to="+lat+","+lng+
				"("+addr+")&type=0&opt=0";
		
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		fragment = WebLoadFragment.getInstance(url);
		t.replace(R.id.rlContent, fragment);
		t.commit();
		
		llTopBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				fragment.goBack();
			}
		});
	}
}
