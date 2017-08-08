//package com.shareshenghuo.app.user.fragment;
//
//import java.io.UnsupportedEncodingException;
//import java.util.List;
//
//import org.apache.http.entity.StringEntity;
//
//import android.content.Intent;
//import android.view.View;
//import android.widget.ListView;
//
//import com.shareshenghuo.app.user.R;
//import com.shareshenghuo.app.user.adapter.OrderListAdapter;
//import UserInfoManager;
//import com.shareshenghuo.app.user.network.bean.OrderDetailInfo;
//import com.shareshenghuo.app.user.network.request.OrderListRequest;
//import com.shareshenghuo.app.user.network.response.OrderListResponse;
//import Api;
//import com.shareshenghuo.app.user.util.T;
//import com.google.gson.Gson;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
//
//public class OrderListFragment extends BaseFragment implements OnRefreshListener2<ListView> {
//	
//	private PullToRefreshListView lvData;
//	
//	private OrderListAdapter adapter;
//	private int pageNo = 1;
//	private int pageSize = 10;
//	public int status;	//0全部 1待付款 2待收货 3待评价 4已完成
//
//	@Override
//	protected int getLayoutId() {
//		return R.layout.fragment_order_list;
//	}
//
//	@Override
//	protected void init(View rootView) {
//		lvData = getView(R.id.lvData);
//		lvData.setMode(Mode.BOTH);
//		lvData.setOnRefreshListener(this);
//		
//		onPullDownToRefresh(lvData);
//	}
//
//	public void loadData() {
//		OrderListRequest req = new OrderListRequest();
//		req.user_id = UserInfoManager.getUserId(activity)+"";
//		req.page_no = pageNo+"";
//		req.page_size = pageSize+"";
//		req.status = status+"";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_ORDER_LIST, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				lvData.onRefreshComplete();
//				T.showNetworkError(activity);
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				lvData.onRefreshComplete();
//				OrderListResponse bean = new Gson().fromJson(resp.result, OrderListResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					updateView(bean.data);
//				} else {
//					T.showShort(activity, bean.result_desc);
//				}
//			}
//		});
//	}
//	
//	public void updateView(List<OrderDetailInfo> data) {
//		if(pageNo==1 || adapter==null) {
//			adapter = new OrderListAdapter(activity, data);
//			adapter.fragment = this;
//			lvData.setAdapter(adapter);
//		}
//		if(pageNo > 1) {
//			adapter.getmData().addAll(data);
//			adapter.notifyDataSetChanged();
//		}
//		pageNo++;
//	}
//
//	@Override
//	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//		pageNo = 1;
//		loadData();
//	}
//
//	@Override
//	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//		loadData();
//	}
//	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if(resultCode==activity.RESULT_OK && requestCode==OrderListAdapter.REQ_ORDER_OPERATE)
//			onPullDownToRefresh(lvData);
//	}
//}
