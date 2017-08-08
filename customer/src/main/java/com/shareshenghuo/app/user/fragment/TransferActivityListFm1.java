//package com.shareshenghuo.app.user.fragment;
//
//import java.io.UnsupportedEncodingException;
//import java.util.List;
//
//import org.apache.http.entity.StringEntity;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.shareshenghuo.app.user.R;
//import com.shareshenghuo.app.user.adapter.IntegralAdapter;
//import com.shareshenghuo.app.user.adapter.TransferActivityListAdapter;
//import com.shareshenghuo.app.user.fragment.BaseFragment;
//import UserInfoManager;
//import com.shareshenghuo.app.user.network.bean.IntegralBean;
//import IntegralRquest;
//import com.shareshenghuo.app.user.network.request.NumRequest;
//import IntegralResponse;
//import com.shareshenghuo.app.user.network.response.NumResponse;
//import Api;
//import com.shareshenghuo.app.user.util.ProgressDialogUtil;
//import com.shareshenghuo.app.user.util.T;
//import com.shareshenghuo.app.user.util.Util;
//import com.google.gson.Gson;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
//
//public class TransferActivityListFm1 extends BaseFragment implements OnRefreshListener2<ListView>{
//	
//	private PullToRefreshListView lvData;
//	private TransferActivityListAdapter adapter;
//	private TextView tv_num,tv_title,tv_title1,tv_num1;
//	private int pageNo = 1;
//	private int pageSize = 10;
//	
//	@Override
//	protected int getLayoutId() {
//		// TODO Auto-generated method stub
//		return R.layout.transferlistfm_activity;
//	}
//
//	@Override
//	protected void init(View rootView) {
//		// TODO Auto-generated method stubxl
//        initView();
//        loadData();
//	}
//	
////	private PullToRefreshListView lvData;
////	private IntegralAdapter adapter;
////	private TextView tv_num,tv_title,tv_title1,tv_num1;
////	private int pageNo = 1;
////	private int pageSize = 10;
////	private String integral,totalIntegral;
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.integral_activity);
////        
////        integral = getIntent().getStringExtra("integral");
////        totalIntegral = getIntent().getStringExtra("totalIntegral");
////
////    }
//
//    
//    public void initView() {
////    	initTopBar("公益积分", "产业链积分");
//    	tv_title = getView(R.id.tv_title);
//    	tv_title1 = getView(R.id.tv_title1);
//    	tv_title.setText("当前转让秀心  ");
////    	tv_title1.setText("累计积分  ");
//		tv_num = getView(R.id.tv_num);
//		tv_num1 = getView(R.id.tv_num1);
//		lvData = getView(R.id.lvShop);
//		lvData.setMode(Mode.BOTH);
//		lvData.setOnRefreshListener(this);
//		getStatisticsData();
//    }
//    
//    public void getStatisticsData() {
//		
//		
////		ProgressDialogUtil.showProgressDlg(getActivity(), "");
//		NumRequest req = new NumRequest();
//		try {
//			req.userId = UserInfoManager.getUserInfo(getActivity()).id+"";
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		req.userType = "1";
//		
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.GETBUNBER, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
////				T.showNetworkError(IntegralDivisionActivity.this);
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				NumResponse bean = new Gson().fromJson(resp.result, NumResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					if(bean.data.integralNew!=null){
//						
//					}
//				}
//			}
//		});
//	}
//    
//    
//	public void loadData() {
////		List<ExcitationBean> bean = new ArrayList<ExcitationBean>();
////		for (int i = 0; i < 10; i++) {
////			ExcitationBean data = new ExcitationBean();
////			data.amount = "交易金额: 200元";
////			data.time = "结算时间: 2016-8-15";
////			data.num = "+30";
////			bean.add(data);
////		}
////		updateView(bean);
//		IntegralRquest req = new IntegralRquest();
//		req.userId = UserInfoManager.getUserInfo(getActivity()).id+"";
//		req.userType = "1";
//		req.startDate = "";
//		req.endDate = "";
//		req.channelType = "1";
//		req.pageNo = pageNo+"";
//		req.pageSize = pageSize+"";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.INVESTMENTCONTROLLERLIST, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				lvData.onRefreshComplete();
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				lvData.onRefreshComplete();
//				IntegralResponse bean = new Gson().fromJson(resp.result, IntegralResponse.class);
//				if(Api.SUCCEED == bean.result_code)
//				
//				if(bean.data!=null){
//					updateView(bean.data);
//				}
//			}
//		});
//		
//		
//	}
//    
//	public void updateView(List<IntegralBean> data) {
//		if(pageNo==1 || adapter==null) {
//			adapter = new TransferActivityListAdapter(activity, data);
//			lvData.setAdapter(adapter);
//		}
//		if(pageNo > 1) {
//			adapter.getmData().addAll(data);
//			adapter.notifyDataSetChanged();
//		}
//		pageNo++;
//	}
//	
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
//}
