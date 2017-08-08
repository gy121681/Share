package com.shareshenghuo.app.user;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.fragment.CardListFragment;
import com.shareshenghuo.app.user.util.ViewUtil;

public class SearchCardActivity extends BaseTopActivity {
	
	private EditText edKeyword;
	private CardListFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_card);
		initView();
	}
	
	public void initView() {
		initTopBar("搜索");
		edKeyword = getView(R.id.edSearchKeyWord);
		fragment = ((CardListFragment)getSupportFragmentManager().findFragmentById(R.id.fCardList));
		
		findViewById(R.id.btnSearch).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(ViewUtil.checkEditEmpty(edKeyword, "请输入关键字"))
					return;
				fragment.keyword = edKeyword.getText().toString();
				fragment.loadData();
			}
		});
	}
}
