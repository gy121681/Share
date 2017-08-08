package com.shareshenghuo.app.user.fragment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.MyConsumptionAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.MyConsumBean;
import com.shareshenghuo.app.user.network.request.MyConsumptionRequest;
import com.shareshenghuo.app.user.network.response.MyConsumptionResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.Util;
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

public class MyConsumtiFragment2 extends BaseFragment implements OnRefreshListener2<ListView>{
	
	private PullToRefreshListView lvData;

	private int pageNo = 1;
	private int pageSize = 10;
	private MyConsumptionAdapter adapter;
	private TextView tv_title, tv_num;
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.excitation_fragment;
	}

	@Override
	protected void init(View rootView) {
		// TODO Auto-generated method stubxl
        init();
        
        loadData();
	}
	protected void init( ) {
//		initTopBar("我的消费");
		tv_title = getView(R.id.tv_title);
		tv_num = getView(R.id.tv_num);
		lvData = getView(R.id.lvShop);
		getView(R.id.llin1).setVisibility(View.GONE);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		tv_title.setText("累计消费");
	}

	public void loadData() {
		MyConsumptionRequest req = new MyConsumptionRequest();
		req.userId = UserInfoManager.getUserInfo(getActivity()).id+"";
		req.startDate = "";
		req.endDate = "";
		req.pageNo = pageNo+"";
		req.pageSize = pageSize+"";
		
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.e("", ""+Api.MYCONSUMPTION);
		new HttpUtils().send(HttpMethod.POST, Api.MYCONSUMPTION, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				MyConsumptionResponse bean = new Gson().fromJson(resp.result, MyConsumptionResponse.class);
				if(Api.SUCCEED == bean.result_code)
					Log.e("", " - - - -  "+resp.result.toString());
				if(bean.data!=null&&!TextUtils.isEmpty(bean.data.sumUserMoney)){
					tv_num.setText(Util.getfotmatnum(bean.data.sumUserMoney, false,1));
					if(bean.data.list!=null){
						updateView(bean.data.list);
					}
				}
			}
		});
		

	}

	public void updateView(List<MyConsumBean> data) {
		if(data!=null&&data.size()>0){
			
		if (pageNo == 1 || adapter == null) {
			adapter = new MyConsumptionAdapter(getActivity(), data);
			lvData.setAdapter(adapter);
		}
		if (pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
		}
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