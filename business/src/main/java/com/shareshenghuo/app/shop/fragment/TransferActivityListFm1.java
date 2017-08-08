package com.shareshenghuo.app.shop.fragment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.TransferActivityListAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.IntegralBean;
import com.shareshenghuo.app.shop.network.request.IntegralRquest;
import com.shareshenghuo.app.shop.network.request.NumRequest;
import com.shareshenghuo.app.shop.network.response.IntegralResponse;
import com.shareshenghuo.app.shop.network.response.NumResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;

public class TransferActivityListFm1 extends BaseFragment implements OnRefreshListener2<ListView>{
	
	private PullToRefreshListView lvData;
	private TransferActivityListAdapter adapter;
	private TextView tv_num,tv_title,tv_title1,tv_num1;
	private int pageNo = 1;
	private int pageSize = 10;
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.transferlistfm_activity;
	}

	@Override
	protected void init(View rootView) {
		// TODO Auto-generated method stubxl
        initView();
        loadData();
	}
	
//	private PullToRefreshListView lvData;
//	private IntegralAdapter adapter;
//	private TextView tv_num,tv_title,tv_title1,tv_num1;
//	private int pageNo = 1;
//	private int pageSize = 10;
//	private String integral,totalIntegral;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.integral_activity);
//        
//        integral = getIntent().getStringExtra("integral");
//        totalIntegral = getIntent().getStringExtra("totalIntegral");
//
//    }

    
    public void initView() {
//    	initTopBar("公益积分", "秀儿积分");
    	tv_title = getView(R.id.tv_title);
    	tv_title1 = getView(R.id.tv_title1);
    	tv_title.setText("当前接收秀心  ");
//    	tv_title1.setText("累计积分  ");
		tv_num = getView(R.id.tv_num);
		tv_num1 = getView(R.id.tv_num1);
		lvData = getView(R.id.lvShop);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		getStatisticsData();
    }
    
    public void getStatisticsData() {
		
		
//		ProgressDialogUtil.showProgressDlg(getActivity(), "");
		NumRequest req = new NumRequest();
		try {
			req.userId = UserInfoManager.getUserInfo(getActivity()).shop_id+"";
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		req.userType = "2";
		
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETBUNBER, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(IntegralDivisionActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				Log.e("", "1111111  "+resp.result);
				ProgressDialogUtil.dismissProgressDlg();
				NumResponse bean = new Gson().fromJson(resp.result, NumResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					if(bean.data.receiveFilialNew!=null){
						setText(R.id.tv_num1, bean.data.receiveFilialNew);
						
					}
				}
			}
		});
	}
    
    
	public void loadData() {
//		List<ExcitationBean> bean = new ArrayList<ExcitationBean>();
//		for (int i = 0; i < 10; i++) {
//			ExcitationBean data = new ExcitationBean();
//			data.amount = "交易金额: 200元";
//			data.time = "结算时间: 2016-8-15";
//			data.num = "+30";
//			bean.add(data);
//		}
//		updateView(bean);
		IntegralRquest req = new IntegralRquest();
		req.userId = UserInfoManager.getUserInfo(getActivity()).shop_id+"";
		req.userType = "2";
		req.startDate = "";
		req.endDate = "";
		req.channelType = "1";
		req.pageNo = pageNo+"";
		req.pageSize = pageSize+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.INVESTMENTCONTROLLERLIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				Log.e("", "222222  "+resp.result);
				lvData.onRefreshComplete();
				IntegralResponse bean = new Gson().fromJson(resp.result, IntegralResponse.class);
				if(Api.SUCCEED == bean.result_code)
				
				if(bean.data!=null){
					updateView(bean.data);
				}
			}
		});
		
		
	}
    
	public void updateView(List<IntegralBean> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new TransferActivityListAdapter(activity, data);
			lvData.setAdapter(adapter);
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}
	

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData();
	}

}
