//package com.shareshenghuo.app.user;
//
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.http.entity.StringEntity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.TextView;
//
//import ShowFilialFragment;
//import com.shareshenghuo.app.user.fragment.ShowFilialFragment1;
//import UserInfoManager;
//import NumberBean;
//import com.shareshenghuo.app.user.network.bean.UserInfo;
//import com.shareshenghuo.app.user.network.request.NumRequest;
//import UpdateUserInfoRequest;
//import com.shareshenghuo.app.user.network.response.LoginResponse;
//import com.shareshenghuo.app.user.network.response.NumResponse;
//import Api;
//import com.shareshenghuo.app.user.util.ProgressDialogUtil;
//import com.shareshenghuo.app.user.util.T;
//import com.shareshenghuo.app.user.util.Util;
//import MyTabView;
//import MyTabView.PageChangeListener;
//import com.google.gson.Gson;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
//
//public class ShowFilialActivity extends BaseTopActivity {
//
//	private MyTabView tabView;
//	private TextView tv_title,tv_num;
//	private String totalFilialPiety = "";
//
//	@Override
//	protected void onCreate(Bundle arg0) {
//		// TODO Auto-generated method stub
//		super.onCreate(arg0);
//		setContentView(R.layout.mynew_integral_activirty);
//		tv_num = getView(R.id.tv_num);
//		tv_num.setText("秀心转让");
//		tv_title = getView(R.id.tv_title);
//		getStatisticsData(false);
//		tv_num.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent it = new Intent(ShowFilialActivity.this,FilialTransferActivity.class);
//				it.putExtra("num", totalFilialPiety);
//				startActivity(it);
//			}
//		});
////		initview();
//	}
//	
////	@Override
////	protected void onStart() {
////		// TODO Auto-generated method stub
////		super.onStart();
////		getStatisticsData();
////	}
//	@Override
//	protected void onRestart() {
//		// TODO Auto-generated method stub
//		super.onRestart();
//		getStatisticsData(true);
//	}
//	
//	
//	public void getStatisticsData(final boolean tag) {
//		
//		
//		ProgressDialogUtil.showProgressDlg(this, "");
//		NumRequest req = new NumRequest();
//		try {
//			req.userId = UserInfoManager.getUserInfo(this).id+"";
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
//				T.showNetworkError(ShowFilialActivity.this);
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				NumResponse bean = new Gson().fromJson(resp.result, NumResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					if(!tag){
//						initview(bean.data);
//					}else{
//						totalFilialPiety = bean.data.shareFilialPiety;
//						tabView.settitle("共享秀心\n"+bean.data.shareFilialPiety);
//					}
////					lvData.setAdapter(new DataReportAdapter(activity, bean.data));
//				}
//			}
//		});
//	}
//	
//	
//	
//
//	private void initview(final NumberBean data) {
//		// TODO Auto-generated method stub
//		totalFilialPiety = data.shareFilialPiety;
//		initTopBar("我的秀心");
//		
//		tv_title.setText("累计激励秀心\n"+data.totalFilialPiety);
//		tabView = (MyTabView) findViewById(R.id.tabFavorites);
//
//		List<Map<String, Integer>> titles = new ArrayList<Map<String, Integer>>();
//		Map<String, Integer> map = new HashMap<String, Integer>();
//		map.put("激励秀心\n"+data.filialPiety, null);
//		titles.add(map);
//		map = new HashMap<String, Integer>();
//		map.put("共享秀心\n"+data.shareFilialPiety, null);
//		titles.add(map);
//
//		List<Fragment> fragments = new ArrayList<Fragment>();
//		fragments.add(new ShowFilialFragment());
//		fragments.add(new ShowFilialFragment1());
//		
//		// fragments.add(new IncentivePoints2Fragment());
//
//		tabView.createView(titles, fragments, getSupportFragmentManager());
//		tabView.setPageChangeListener(new PageChangeListener() {
//			
//			@Override
//			public void onPageChanged(int index, String tabTitle) {
//				// TODO Auto-generated method stub
//				if(index==0){
//					tv_num.setVisibility(View.INVISIBLE);
//					tv_title.setText("累计激励秀心\n"+data.totalFilialPiety);
//				}else{
//					tv_num.setVisibility(View.VISIBLE);
//					tv_title.setText("累计共享秀心\n"+data.shareTotalFilialPiety);
//				}
//			}
//		});
//	}
//}
