package com.shareshenghuo.app.user;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.fragment.RechargeLogFragment;
import com.shareshenghuo.app.user.network.bean.CardInfo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

/**
 * @author hang
 * 积分详情
 */
public class PointDetailActivity extends BaseTopActivity {
	
	private CardInfo cardInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_point_detail);
		init();
	}
	
	public void init() {
		cardInfo = (CardInfo) getIntent().getSerializableExtra("cardInfo");
		
		initTopBar("积分详情");
		
		setText(R.id.tvAllPoint, "积分累计："+cardInfo.all_point);
		setText(R.id.tvPoint, cardInfo.point+"积分");
		
		RechargeLogFragment f = new RechargeLogFragment();
		f.type = 1;
		f.shopId = cardInfo.shop_id;
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.rlContent, f);
		t.commit();
	}
}
