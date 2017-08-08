package com.shareshenghuo.app.user;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.fragment.ProdListFragment;
import com.shareshenghuo.app.user.util.ViewUtil;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class SearchProdActivity extends BaseTopActivity implements OnClickListener {
	
	private EditText edKeyword;
	
	private ProdListFragment fragment;
	
	private int shopId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_prod);
		init();
	}
	
	public void init() {
		initTopBar("搜索");
		
		edKeyword = getView(R.id.edSearchKeyWord);
		findViewById(R.id.btnSearch).setOnClickListener(this);
		
		shopId = getIntent().getIntExtra("shopId", 0);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnSearch:
			if(ViewUtil.checkEditEmpty(edKeyword, "请输入关键字"))
				return;
			
			if(fragment == null) {
				fragment = new ProdListFragment();
				fragment.shop_id = shopId;
				fragment.keyword = edKeyword.getText().toString();
				
				FragmentTransaction t = getSupportFragmentManager().beginTransaction();
				t.replace(R.id.rlSearchResult, fragment);
				t.commit();
			} else {
				fragment.keyword = edKeyword.getText().toString();
				fragment.onPullDownToRefresh(null);
			}
			break;
		}
	}
}
