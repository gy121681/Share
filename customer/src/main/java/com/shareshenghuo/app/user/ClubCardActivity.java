package com.shareshenghuo.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.fragment.CardListFragment;

/**
 * @author hang 
 * 会员卡
 */
public class ClubCardActivity extends BaseTopActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_club_card);
		initView();
	}
	
	public void initView() {
		initTopBar("我的会员卡");
		btnTopRight2.setVisibility(View.VISIBLE);
		btnTopRight2.setBackgroundResource(R.drawable.btn_search);
		btnTopRight2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(ClubCardActivity.this, SearchCardActivity.class));
			}
		});
		
		((CardListFragment) getSupportFragmentManager().findFragmentById(R.id.fCardList)).loadData();
	}
}
