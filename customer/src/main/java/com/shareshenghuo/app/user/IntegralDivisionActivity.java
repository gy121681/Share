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
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.TextView;
//
//import UserInfoManager;
//import NumberBean;
//import com.shareshenghuo.app.user.network.bean.WebLoadActivity;
//import com.shareshenghuo.app.user.network.request.NumRequest;
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
//public class IntegralDivisionActivity extends BaseTopActivity{
//	
//	private MyTabView tabView;
//	private TextView tv_title;
//	
//	@Override
//	protected void onCreate(Bundle arg0) {
//		// TODO Auto-generated method stub
//		super.onCreate(arg0);
//		setContentView(R.layout.mynew_integral_activirty);
//		
//		initTopBar("我的积分");
//		btnTopRight1.setVisibility(View.VISIBLE);
//		btnTopRight1.setText("积分说明");
//		btnTopRight1.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent about = new Intent(IntegralDivisionActivity.this, WebLoadActivity.class);
//				about.putExtra("title", "积分说明");
//				about.putExtra("url", Api.INTEGRALPRO);
//				startActivity(about);
//			}
//		});
////		initview();
//		getStatisticsData();
//	}
//	
//	
//	
//	
//public void getStatisticsData() {
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
//				T.showNetworkError(IntegralDivisionActivity.this);
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				NumResponse bean = new Gson().fromJson(resp.result, NumResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					initview(bean.data);
////					lvData.setAdapter(new DataReportAdapter(activity, bean.data));
//				}
//			}
//		});
//	}
//
//	private void initview(final NumberBean data) {
//		// TODO Auto-generated method stub
//		
//		
//		tabView = (MyTabView) findViewById(R.id.tabFavorites);
//		
//		tv_title = getView(R.id.tv_title);
//		tv_title.setText("累计激励积分\n"+Util.getfotmatnum(data.totalIntegral, true,1));
//		
//		List<Map<String,Integer>> titles = new ArrayList<Map<String,Integer>>();
//		Map<String,Integer> map = new HashMap<String, Integer>();
//		map.put("激励积分\n"+Util.getfotmatnum(data.integral, true,1), null);
////		map.put("激励积分", null);
//		titles.add(map);
//		map = new HashMap<String, Integer>();
//		map.put("共享积分\n"+Util.getfotmatnum(data.shareIntegrel, true,1), null);
////		map.put("共享积分", null);
//		titles.add(map);
//		
//		List<Fragment> fragments = new ArrayList<Fragment>();
//		fragments.add(new IntegralFragment());
//		fragments.add(new IntegralFragment1());
////		fragments.add(new IncentivePoints2Fragment());
//		
//		tabView.createView(titles, fragments, getSupportFragmentManager());
//		tabView.setPageChangeListener(new PageChangeListener() {
//			
//			@Override
//			public void onPageChanged(int index, String tabTitle) {
//				// TODO Auto-generated method stub
//				if(index==0){
//					tv_title.setText("累计激励积分\n"+Util.getfotmatnum(data.totalIntegral, true,1));
//				}else{
//					btnTopRight1.setVisibility(View.VISIBLE);
//					tv_title.setText("累计共享积分\n"+Util.getfotmatnum(data.shareTotalIntegrel, true,1));
//				}
//			}
//		});
//	}
//
//}
