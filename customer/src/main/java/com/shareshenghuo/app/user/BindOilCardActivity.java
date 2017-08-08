package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.SlideAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.MyOilBean;
import com.shareshenghuo.app.user.network.request.MyOilRequest;
import com.shareshenghuo.app.user.network.response.MyOilResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
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

public class BindOilCardActivity extends BaseTopActivity implements OnRefreshListener2<ScrollView>{
	
	private SlideListView mSlideListView;
	private SlideAdapter mAdapter;
	private int pageNo = 1;
	private int pageSize = 10;
	private PullToRefreshScrollView refreshscrollview;
	private List<MyOilBean> datas;	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.myoilcardlist_activity);

		initview();
		loadData();
	}
	
	@Override
	protected void onRestart() {  
		// TODO Auto-generated method stub
		super.onRestart();
		pageNo = 1;
		loadData();
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("绑定油卡");
		refreshscrollview = getView(R.id.refreshscrollview);
		refreshscrollview.setOnRefreshListener(this);
		refreshscrollview.setMode(Mode.BOTH);
		mSlideListView = ((SlideListView) findViewById(R.id.list_view));
		mSlideListView.setSlideMode(SlideMode.RIGHT);
		mSlideListView.setSlideRightAction(SlideAction.REVEAL);
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("添加");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(BindOilCardActivity.this,AddOilCardActivity.class));
			}
		});
		
		datas = new ArrayList<MyOilBean>();
		MyOilBean b = new MyOilBean();
//		datas.add(b);
		mAdapter = new SlideAdapter(BindOilCardActivity.this, datas);
		mSlideListView.setAdapter(mAdapter);
		
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "");
		MyOilRequest req = new MyOilRequest();
		req.userId = UserInfoManager.getUserInfo(this).id+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.MYOILLIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				refreshscrollview.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
					refreshscrollview.onRefreshComplete();
				MyOilResponse bean = new Gson().fromJson(resp.result, MyOilResponse.class);
				if(Api.SUCCEED == bean.result_code)
					
					updateView(bean.data);
			}
		});
		
		
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
//		loadData();
		refreshscrollview.onRefreshComplete();
	}
    
	public void updateView(List<MyOilBean> data) {
	
		if(pageNo==1 || mAdapter==null) {
			datas.clear();
			datas.addAll(data);
//			mAdapter = new SlideAdapter(BindOilCardActivity.this, data);
//			mSlideListView.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();
		}
		if(pageNo > 1) {
			mAdapter.getmData().addAll(data);
			mAdapter.notifyDataSetChanged();
		}
//		Utility.setListViewHeightBasedOnChildren(mSlideListView);
		pageNo++;
	}

}
