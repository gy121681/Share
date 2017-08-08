package com.shareshenghuo.app.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.fragment.RechargeLogFragment;
import com.shareshenghuo.app.user.network.bean.CardInfo;
import com.shareshenghuo.app.user.network.bean.WebLoadActivity;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.widget.MyTabView;

/**
 * @author hang
 * 我的会员卡详细
 */
public class CardDetailActivity extends BaseTopActivity implements OnClickListener {
	
	private MyTabView tabView;
	
	private CardInfo cardInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_detail);
		init();
	}
	
	public void init() {
		cardInfo = (CardInfo) getIntent().getSerializableExtra("cardInfo");
		
		initTopBar("我的会员卡");
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("进店");
		btnTopRight1.setOnClickListener(this);
		
		setText(R.id.tvCardShopName, cardInfo.shop_name+"会员卡");
		setText(R.id.tvCardNo, "NO."+cardInfo.card_no);
		setText(R.id.tvCardLevelDesc, cardInfo.level_desc);
		setText(R.id.tvCardMoney, "余额："+cardInfo.money);
		setText(R.id.tvCardDetailPoint, cardInfo.point+"");
		setText(R.id.tvCardDetailLevelDesc, cardInfo.level_desc);
		
		findViewById(R.id.llSeePointDetail).setOnClickListener(this);
		findViewById(R.id.llSeeCardDetail).setOnClickListener(this);
		
		tabView = getView(R.id.tabCardUseInfo);
		List<Map<String,Integer>> titles = new ArrayList<Map<String,Integer>>();
		Map<String,Integer> map = new HashMap<String, Integer>();
		map.put("充值记录", null);
		titles.add(map);
		map = new HashMap<String, Integer>();
		map.put("消费记录", null);
		titles.add(map);
		
		List<Fragment> fragments = new ArrayList<Fragment>();
		RechargeLogFragment f1 = new RechargeLogFragment();
		f1.type = 3;
		f1.shopId = cardInfo.shop_id;
		fragments.add(f1);
		RechargeLogFragment f2 = new RechargeLogFragment();
		f2.type = 2;
		f2.shopId = cardInfo.shop_id;
		fragments.add(f2);
		tabView.createView(titles, fragments, getSupportFragmentManager());
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnTopRight1:
			Intent it = new Intent(CardDetailActivity.this, ShopDetailActivity.class);
			it.putExtra("shopId", cardInfo.shop_id);
			startActivity(it);
			break;
			
		case R.id.llSeePointDetail:
			Intent point = new Intent(this, PointDetailActivity.class);
			point.putExtra("cardInfo", cardInfo);
			startActivity(point);
			break;
			
		case R.id.llSeeCardDetail:
			Intent card = new Intent(this, WebLoadActivity.class);
			card.putExtra("title", "会员卡详情");
			card.putExtra("url", Api.URL_CARD_DETAIL+"?card_id="+cardInfo.id);
			startActivity(card);
			break;
		}
	}
}
