package com.shareshenghuo.app.user;


import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.IntegralAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.IntegralBean;
import com.shareshenghuo.app.user.network.request.IntegralRquest;
import com.shareshenghuo.app.user.network.response.IntegralResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

public class IntegralActivity extends BaseTopActivity implements OnRefreshListener2<ListView>{
	
	private PullToRefreshListView lvData;
	private IntegralAdapter adapter;
	private TextView tv_num,tv_title,tv_title1,tv_num1;
	private int pageNo = 1;
	private int pageSize = 10;
	private String integral,totalIntegral;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integral_activity);
        
        integral = getIntent().getStringExtra("integral");
        totalIntegral = getIntent().getStringExtra("totalIntegral");
        initView();
        
        loadData();
    }

    
    public void initView() {
    	initTopBar("我的积分");
    	tv_title = getView(R.id.tv_title);
    	tv_title1 = getView(R.id.tv_title1);
    	tv_title.setText("当前积分  ");
    	tv_title1.setText("累计积分  ");
		tv_num = getView(R.id.tv_num);
		tv_num1 = getView(R.id.tv_num1);
		tv_num.setText(integral);
		tv_num1.setText(totalIntegral);
		lvData = getView(R.id.lvShop);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
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
		req.userId = UserInfoManager.getUserInfo(this).id+"";
		req.userType = "1";
		req.operbType = "";
		req.opersType = "";
		req.startDate = "";
		req.endDate = "";
		req.pageNo = pageNo+"";
		req.pageSize = pageSize+"";
		
		Log.e("", " - - - - "+req.toString());
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.INTEGRALLIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				IntegralResponse bean = new Gson().fromJson(resp.result, IntegralResponse.class);
				if(Api.SUCCEED == bean.result_code)
					Log.e("", " - - - -  "+resp.result.toString());
					updateView(bean.data);
			}
		});
		
		
	}
    
	public void updateView(List<IntegralBean> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new IntegralAdapter(IntegralActivity.this, data);
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
