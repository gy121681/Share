package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.adapter.CommodityManageAdapter;
import com.shareshenghuo.app.shop.adapter.CommodityManageAdapter1;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.CommodityManageBean;
import com.shareshenghuo.app.shop.network.request.CommodityManageRequest;
import com.shareshenghuo.app.shop.network.response.CommodityManageResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CommodityManageActivity extends BaseTopActivity implements  OnRefreshListener2<ScrollView>,OnKeyListener{
	
	
	private int pageNo = 1;
	private int pageSize = 15;
	private CommodityManageAdapter adapter;
	private ListView lvData;
	private EditText edKeyword;
	private ImageView ivSearch;
	private RelativeLayout re1,re2;
	private TextView tv1,tv2;
	private PullToRefreshScrollView refreshscrollview;
	private List<CommodityManageBean> data;
	private String name = "";
	private TextView tv_del;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commodity_manag_activity);
		
		initView();
		initd();
		loadData();
		
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
			pageNo = 1;
			loadData();	
	}
	
	private void initd() {
		// TODO Auto-generated method stub
//		List<CommodityManageBean> data = new ArrayList<CommodityManageBean>();
//		for (int i = 0; i < 10; i++) {
//			CommodityManageBean	bean = new CommodityManageBean();
//			data.add(bean);
//		}
		data= new ArrayList<CommodityManageBean>();
		adapter = new CommodityManageAdapter(this, data);
		lvData.setAdapter(adapter);
	}
	
	public void initView() {
		initTopBar("商品管理");
		refreshscrollview = getView(R.id.refreshscrollview);
		refreshscrollview.setOnRefreshListener(this);
		refreshscrollview.setMode(Mode.BOTH);
		lvData = getView(R.id.lvShop);
		re1 = getView(R.id.re1);
		re2 = getView(R.id.re2);
		tv1 = getView(R.id.tv1);
		tv2 = getView(R.id.tv2);
		tv_del = getView(R.id.tv_del);
		edKeyword = getView(R.id.edSearch);
//		edKeyword.setOnKeyListener(this);
		edKeyword.setHint("仅可搜索商品名称");
		ivSearch = getView(R.id.ivSearch);
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("添加商品");
		edKeyword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(tv_del.getVisibility()==View.GONE){
					data.clear();
					adapter.notifyDataSetChanged();
					tv_del.setVisibility(View.VISIBLE);
				}
			}
		});
		edKeyword.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1&&tv_del.getVisibility()==View.GONE){
					data.clear();
					adapter.notifyDataSetChanged();
					tv_del.setVisibility(View.VISIBLE);
				}else{
				}
			}
		});
		
		edKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() { 
			public boolean onEditorAction(TextView v, int actionId,                   KeyEvent event)  {                          
			if (actionId==EditorInfo.IME_ACTION_GO ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)) 
			{                
			//do something;   
				if(!TextUtils.isEmpty(edKeyword.getText().toString().trim())){
					pageNo = 1;
					name = edKeyword.getText().toString();
					loadData();
				}
			return true;             
			}               
			return false;           
			}       
			});
		
		ivSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pageNo = 1;
				name = edKeyword.getText().toString();
				loadData();
			}
		});
		
		edKeyword.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					tv_del.setVisibility(View.VISIBLE);
				}else{
//					tv_del.setVisibility(View.GONE);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		tv_del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				name = "";
				edKeyword.setText("");
				pageNo = 1;
				tv_del.setVisibility(View.GONE);
				loadData();
			}
		});
		
		btnTopRight1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CommodityManageActivity.this,CommodityInfoActivity.class));
			}
		});
		
		re1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CommodityManageActivity.this,ShopCategoryListActivity.class));
			}
		});
		re2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CommodityManageActivity.this,BatchManagementActivity.class));
			}
		});
		
		lvData.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent it = new Intent(CommodityManageActivity.this,CommodityInfoActivity.class);
				it.putExtra("id", data.get((int)arg3).id);
				startActivity(it);
			}
		});
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		CommodityManageRequest req = new CommodityManageRequest();
		req.shopId = UserInfoManager.getUserInfo(this).shop_id+"";
		Log.e("", ""+req.shopId);
		req.name = name;
		req.page_no = pageNo;
		req.page_size = pageSize;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.SHOPMANAGE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				refreshscrollview.onRefreshComplete();
					T.showNetworkError(getApplicationContext());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				refreshscrollview.onRefreshComplete();
					CommodityManageResponse bean = new Gson().fromJson(resp.result, CommodityManageResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						updateView(bean.data);
					} else {
						T.showShort(getApplicationContext(), bean.result_desc);
					}
			}
		});
	}
	
	public void updateView(List<CommodityManageBean> data) {
		if(pageNo==1 || adapter==null) {
			this.data.clear();
			this.data.addAll(data);
			adapter.notifyDataSetChanged();
//			adapter = new CommodityManageAdapter(this, data);
//			lvData.setAdapter(adapter);
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}

//	@Override
//	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//		// TODO Auto-generated method stub
//	
//	}
//
//	@Override
//	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//		// TODO Auto-generated method stub
//	
//		
//	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		name = "";
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		loadData();
	}

	@Override
	public boolean onKey(View arg0, int keyCode, KeyEvent arg2) {
		// TODO Auto-generated method stub
		 if (keyCode == KeyEvent.KEYCODE_ENTER) {
		       // 先隐藏键盘
//		       ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
//		       .hideSoftInputFromWindow(CommodityManageActivity.this.getCurrentFocus()
//		       .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		      
		      //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
		       loadData();
			 	return true;
		   }
		
		return false;
	}
	
}
