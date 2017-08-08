package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.BaseTopActivity;
import com.shareshenghuo.app.shop.CommodityInfoActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.AllDragListAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.CommodityManageBean;
import com.shareshenghuo.app.shop.network.bean.ShopCategoryBean;
import com.shareshenghuo.app.shop.network.request.CommodityManageRequest;
import com.shareshenghuo.app.shop.network.response.CommodityManageResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.widget.AllDragListView;
import com.shareshenghuo.app.shop.widget.DragListView;

public class IfiedManageActivity extends BaseTopActivity implements OnRefreshListener2<ScrollView>{
	
	
	private int pageNo = 1;
	private int pageSize = 15;
	private AllDragListAdapter adapter;
//	private PullToRefreshListView lvData;
	private boolean tag = false;
	private AllDragListView lvData;
	private TextView tv1, tv2;
	private LinearLayout lin_layout;
	private String id,title;
	private List<CommodityManageBean> datas;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ified_manag_activity);
		id = getIntent().getStringExtra("id");
		title = getIntent().getStringExtra("title");
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
		datas = new ArrayList<CommodityManageBean>();
		adapter = new AllDragListAdapter(this, datas,id);
		lvData.setAdapter(adapter);
	}

	public void initView() {
		if(title!=null){
			initTopBar(title);
		}
		lvData = getView(R.id.lvShop);
		tv1 = getView(R.id.tv1);
		tv1.setText("添加新商品");
		tv2 = getView(R.id.tv2);
		tv2.setText("商品排序");
	  
		
		tv1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it = new Intent(IfiedManageActivity.this,CommodityInfoActivity.class);
				it.putExtra("typeid", id);
				startActivity(it);
			}
		});
		tv2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(tag){
					tv1.setVisibility(View.VISIBLE);
					tv2.setText("商品排序");
					tag = false;
				}else{
					tv1.setVisibility(View.GONE);
					tv2.setText("完成");
					tag = true;
				}
				adapter.showEdit(tag);
			}
		});
		
		lvData.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(id.equals("0")){
					Intent it = new Intent(IfiedManageActivity.this,CommodityInfoActivity.class);
					it.putExtra("id", datas.get((int)arg3).goods_id);
					it.putExtra("typeid", id);
					startActivity(it);
				}
			}
		});
	}
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		CommodityManageRequest req = new CommodityManageRequest();
		req.shopId = UserInfoManager.getUserInfo(this).shop_id+"";
		req.typeId = id;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.FINDGOODSTYPEGOODSLIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
					T.showNetworkError(getApplicationContext());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
					CommodityManageResponse bean = new Gson().fromJson(resp.result, CommodityManageResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						Log.e("", ""+resp.result);
						updateView(bean.data);
					} else {
						T.showShort(getApplicationContext(), bean.result_desc);
					}
			}
		});
	}
	
	public void updateView(List<CommodityManageBean> data) {

		
		if(pageNo==1 || adapter==null) {
			this.datas.clear();
			this.datas.addAll(data);
			adapter.notifyDataSetChanged();
		}
		if(pageNo > 1) {
			this.datas.addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}

	

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		loadData();
	}
	
}
	
