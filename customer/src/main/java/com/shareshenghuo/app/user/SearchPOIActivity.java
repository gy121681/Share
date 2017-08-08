package com.shareshenghuo.app.user;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.PoiListAdapter;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.ViewUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class SearchPOIActivity extends BaseTopActivity
	implements OnClickListener, OnPoiSearchListener, OnItemClickListener {
	
	private EditText edKeyWord;
	private ListView lvPOI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_poi);
		initView();
	}
	
	public void initView() {
		edKeyWord = getView(R.id.edSearchKeyWord);
		lvPOI = getView(R.id.lvPOIS);
		
		lvPOI.setOnItemClickListener(this);
		findViewById(R.id.llSearchBack).setOnClickListener(this);
		findViewById(R.id.btnSearch).setOnClickListener(this);
	}
	
	public void queryPOIs(String keyword) {
		PoiSearch.Query query = new PoiSearch.Query(keyword, "", "");
		query.setPageSize(30);
		query.setPageNum(0);
		PoiSearch poiSearch = new PoiSearch(this, query);
		poiSearch.setBound(new SearchBound(
				new LatLonPoint(CityManager.getInstance(this).latitude, CityManager.getInstance(this).longitude)
				, 10000));
		poiSearch.setOnPoiSearchListener(this);
		ProgressDialogUtil.showProgressDlg(this, "");
		poiSearch.searchPOIAsyn();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
		PoiItem item = (PoiItem) parent.getItemAtPosition(position);
		Intent data = new Intent();
		data.putExtra("addr", item.getSnippet());
		data.putExtra("lat", item.getLatLonPoint().getLatitude());
		data.putExtra("lng", item.getLatLonPoint().getLongitude());
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.llSearchBack:
			finish();
			break;
			
		case R.id.btnSearch:
			if(ViewUtil.checkEditEmpty(edKeyWord, "请输入关键字"))
				return;
			queryPOIs(edKeyWord.getText().toString());
			break;
		}
	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		ProgressDialogUtil.dismissProgressDlg();
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				lvPOI.setAdapter(new PoiListAdapter(this, result.getPois()));
			}
		}
	}
}
