package com.shareshenghuo.app.user.sh;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shareshenghuo.app.user.AddBankCardActivity;
import com.shareshenghuo.app.user.BaseTopActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.BankcardSlideAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.MyBankCardBean;
import com.shareshenghuo.app.user.network.response.BankCardListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.roamer.slidelistview.SlideListView;
import com.roamer.slidelistview.SlideListView.SlideAction;
import com.roamer.slidelistview.SlideListView.SlideMode;

public class MallActivity extends BaseTopActivity implements OnRefreshListener2<ScrollView>{

	private List<MyBankCardBean> datas;
	private SlideListView mSlideListView;
	private BankcardSlideAdapter mAdapter;
	private TextView tv_pro;
	private int pageNo = 1;
	private int pageSize = 10;
//	private PullToRefreshScrollView refreshscrollview;
	private LinearLayout ll_add;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.mall_activity);
		initview();
		loadData();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		loadData();
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("我的银行卡");

//		refreshscrollview = getView(R.id.refreshscrollview);
//		refreshscrollview.setOnRefreshListener(this);
//		refreshscrollview.setMode(Mode.BOTH);
		
		mSlideListView = ((SlideListView) findViewById(R.id.list_view));
		mSlideListView.setSlideMode(SlideMode.RIGHT);
		mSlideListView.setSlideRightAction(SlideAction.REVEAL);
		ll_add = getView(R.id.ll_add);
		tv_pro = getView(R.id.tv_pro);
		datas = new ArrayList<MyBankCardBean>();
		MyBankCardBean b= new MyBankCardBean();
		mAdapter = new BankcardSlideAdapter(MallActivity.this, datas);
		mSlideListView.setAdapter(mAdapter);
		ll_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MallActivity.this,AddBankCardActivity.class));
			}
		});
	}

	
	public void loadData() {
		MyBankCardBean req = new MyBankCardBean();
		req.user_id = UserInfoManager.getUserInfo(this).id+"";
		req.user_type = "1";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.FINDBINDCARDS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
//				refreshscrollview.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
//				refreshscrollview.onRefreshComplete();
				BankCardListResponse bean = new Gson().fromJson(resp.result, BankCardListResponse.class);
				if(Api.SUCCEED == bean.result_code)
					updateView(bean.data);
			}
		});
		
		
	}
    
	public void updateView(List<MyBankCardBean> data) {
		if(data.size()>0){
			tv_pro.setVisibility(View.VISIBLE);
		}
		
		
//		if(pageNo==1 || mAdapter==null) {
			datas.clear();
			datas.addAll(data);
//			for (int i = 0; i <10; i++) {
//				MyBankCardBean b= new MyBankCardBean();
//				datas.add(b);
//			}
			
			mAdapter.notifyDataSetChanged();
//		}else
//		if(pageNo > 1) {
//			mAdapter.getmData().addAll(data);
//			mAdapter.notifyDataSetChanged();
//		}
//		if(pageNo==1 || mAdapter==null) {
//			mAdapter = new BankcardSlideAdapter(BankCardListActivity.this, data);
//			mSlideListView.setAdapter(mAdapter);
//		}
//		if(pageNo > 1) {
//			mAdapter.getmData().addAll(data);
//		
//		}
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
