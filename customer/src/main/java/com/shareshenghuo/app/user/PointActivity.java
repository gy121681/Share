package com.shareshenghuo.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.fragment.ExchangeLogFragment;
import com.shareshenghuo.app.user.fragment.PointLogFragment;
import com.shareshenghuo.app.user.fragment.PointRuleFragment;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.network.bean.WebLoadActivity;
import com.shareshenghuo.app.user.networkapi.Api;

/**
 * @author hang
 * 我的生活币
 */
public class PointActivity extends BaseTopActivity implements OnCheckedChangeListener, OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_point);
		initView();
	}
	
	public void initView() {
		initTopBar("我的生活币");
		((RadioButton)findViewById(R.id.rbPoint1)).setChecked(true);
		
		UserInfo userInfo = UserInfoManager.getUserInfo(this);
		setText(R.id.tvUserPoint, userInfo.point+"生活币");
		
		getView(R.id.btnToShop).setOnClickListener(this);
		
		((RadioButton) findViewById(R.id.rbPoint1)).setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.rbPoint2)).setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.rbPoint3)).setOnCheckedChangeListener(this);
		
		showContent(new ExchangeLogFragment());
	}

	@Override
	public void onCheckedChanged(CompoundButton v, boolean b) {
		if(b) {
			switch(v.getId()) {
			case R.id.rbPoint1:
				showContent(new ExchangeLogFragment());
				break;
				
			case R.id.rbPoint2:
				showContent(new PointLogFragment());
				break;
				
			case R.id.rbPoint3:
				showContent(new PointRuleFragment());
				break;
			}
		}
	}
	
	public void showContent(Fragment fragment) {
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.rlContent, fragment);
		t.commit();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnToShop:
			StringBuilder sb = new StringBuilder(Api.URL_POINT_SHOP);
			sb.append("?city_id=").append(CityManager.getInstance(this).getCityId())
				.append("&user_id=").append(UserInfoManager.getUserId(this));
			Intent it = new Intent(this, WebLoadActivity.class);
			it.putExtra("title", "积分商城");
			it.putExtra("url", sb.toString());
			startActivity(it);
			break;
		}
	}
}
