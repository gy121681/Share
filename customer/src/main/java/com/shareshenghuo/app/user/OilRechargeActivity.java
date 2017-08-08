//package com.shareshenghuo.app.user;
//
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.entity.StringEntity;
//
//import com.shareshenghuo.app.user.adapter.OilIntegralAdapter;
//import com.shareshenghuo.app.user.adapter.OilRechargeAdapter;
//import UserInfoManager;
//import OilintegralBean;
//import OilintegralRequest;
//import com.shareshenghuo.app.user.network.response.OilintegralResponse;
//import Api;
//import com.shareshenghuo.app.user.util.ProgressDialogUtil;
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
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ListView;
//
//public class OilRechargeActivity extends BaseTopActivity implements OnRefreshListener2<ListView>{
//	
//	private int pageNo = 1;
//	private int pageSize = 10;
//	private PullToRefreshListView lvData;
//	private OilRechargeAdapter adapter;
//	private String tag = "0"; 
//	
//	@Override
//	protected void onCreate(Bundle arg0) {
//		// TODO Auto-generated method stub
//		super.onCreate(arg0);
//		setContentView(R.layout.oilintegral_activity);
//		tag = getIntent().getStringExtra("tag");
//		initview();
//		loadData();
//	}
//
//	private void initview() {
//		// TODO Auto-generated method stub
//		if(tag!=null&&tag.equals("1")){
//			initTopBar("销售流水");
//		}else{
//			initTopBar("充值记录");
//		}
//		lvData = getView(R.id.lvShop);
//		lvData.setMode(Mode.BOTH);
//		lvData.setOnRefreshListener(this);
//		
////		List<OilintegralBean> data = new ArrayList<OilintegralBean>();
////		for (int i = 0; i < 10; i++) {
////			
////			OilintegralBean bean = new OilintegralBean(); 
////			data.add(bean);
////			
////		}
////		adapter = new OilRechargeAdapter(OilRechargeActivity.this, data);
////		lvData.setAdapter(adapter);
//		
//	}
//	
//	public void loadData() {
//		ProgressDialogUtil.showProgressDlg(this, "");
//		
//		OilintegralRequest req = new OilintegralRequest();
//		if(tag!=null&&tag.equals("1")){
//			req.qrUserId = UserInfoManager.getUserInfo(this).id+"";
//		}else{
//			req.userId = UserInfoManager.getUserInfo(this).id+"";
//		}
//		
//		req.pageNo = pageNo;
//		req.pageSize = pageSize;
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		String path = "";
//		if(tag!=null&&tag.equals("1")){
//			 path = Api.OILCARDSALESPIPELINECONTROLLER;
//		}else{
//			 path = Api.OILRECHARGELIST;
//		}
//		
//		new HttpUtils().send(HttpMethod.POST, path, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				lvData.onRefreshComplete();
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				lvData.onRefreshComplete();
//				OilintegralResponse bean = new Gson().fromJson(resp.result, OilintegralResponse.class);
//				Log.e("", ""+resp.result);
//				if(Api.SUCCEED == bean.result_code)
//					updateView(bean.data);
//			}
//		});
//		
//		
//	}
//    
//	public void updateView(List<OilintegralBean> data) {
//		if(pageNo==1 || adapter==null) {
//			adapter = new OilRechargeAdapter(OilRechargeActivity.this, data);
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
//		// TODO Auto-generated method stub
//			pageNo = 1;
//			loadData();
//	}
//
//	@Override
//	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//		// TODO Auto-generated method stub
//		loadData();
//	}
//}
