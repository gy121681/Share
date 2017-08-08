package com.shareshenghuo.app.shop.fragment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.OrderListAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.OrderInfo;
import com.shareshenghuo.app.shop.network.request.OrderListRequest;
import com.shareshenghuo.app.shop.network.response.OrderListResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.receiver.DataChangeWatcher;
import com.shareshenghuo.app.shop.receiver.DataChangeWatcher.DataChangeCallback;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;

public class OrderListFragment extends BaseFragment implements OnRefreshListener2<ListView>, DataChangeCallback {
	
	private View rootView;
	private PullToRefreshListView lvData;
	
	public int status = 0;	//0全部 1待接单 2进行中 3已结束
	public String order_no; //搜索订单号或者电话
	public int order_type;	//1外卖 2到店消费
	public String start_time, end_time;	//"2015-11-11"
	
	private int pageNo = 1;
	private int pageSize = 5;
	private OrderListAdapter adapter;
	
	private DataChangeWatcher watcher; 

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_order_list;
	}

	@Override
	protected void init(View rootView) {
		lvData = (PullToRefreshListView) rootView.findViewById(R.id.lvOrder);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		onPullDownToRefresh(lvData);
		
		watcher = new DataChangeWatcher(activity, this);
		watcher.startWork();
	}
	
	@Override
    public void onDestroy() {
        watcher.stopWork();
        super.onDestroy();
    }
	
	public void loadData() {
		OrderListRequest req = new OrderListRequest();
		req.shop_id = UserInfoManager.getUserInfo(activity).shop_id+"";
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		req.status = status+"";
		if(!TextUtils.isEmpty(order_no))
			req.order_no = order_no;
		if(order_type != 0)
			req.order_type = order_type+"";
		if(!TextUtils.isEmpty(start_time) && !TextUtils.isEmpty(end_time)) {
			req.start_time = start_time;
			req.end_time = end_time;
		}
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_ORDER_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				ProgressDialogUtil.dismissProgressDlg();
				OrderListResponse bean = new Gson().fromJson(resp.result, OrderListResponse.class);
				if(Api.SUCCEED == bean.result_code)
					updateView(bean.data);
				else
					T.showShort(activity, bean.result_desc);
			}
		});
	}
	
	public void updateView(List<OrderInfo> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new OrderListAdapter(activity, data);
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
		if(lvData == null)
			return;
		
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData();
	}

	@Override
	public void refreshData(int which) {
		onPullDownToRefresh(lvData);
	}
}
