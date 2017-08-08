package com.shareshenghuo.app.shop;
//package com.shareshenghuo.app.shop;
//
//
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.entity.StringEntity;
//
//import com.example.widget.MyOnclicListener;
//import com.example.widget.SelectBirthday;
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
//import com.shareshenghuo.app.shop.adapter.ConsumptionAdapter;
//import com.shareshenghuo.app.shop.adapter.IntegralAdapter;
//import com.shareshenghuo.app.shop.manager.UserInfoManager;
//import com.shareshenghuo.app.shop.network.bean.ConsumptionBean;
//import com.shareshenghuo.app.shop.network.bean.ExcitationBean;
//import com.shareshenghuo.app.shop.network.request.BannerRequest;
//import com.shareshenghuo.app.shop.network.request.ConsumptionRequest;
//import com.shareshenghuo.app.shop.network.request.VipListRequest;
//import com.shareshenghuo.app.shop.network.response.ConsumptionResponse;
//import com.shareshenghuo.app.shop.network.response.ShopShowDataResponse;
//import com.shareshenghuo.app.shop.network.response.VipListResponse;
//import com.shareshenghuo.app.shop.networkapi.Api;
//import com.shareshenghuo.app.shop.util.DateUtil;
//import com.shareshenghuo.app.shop.util.T;
//import com.shareshenghuo.app.shop.util.Util;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ListView;
//import android.widget.TextView;
//
//public class ConsumptionSeriesActivity extends BaseTopActivity implements OnRefreshListener2<ListView>{
//	
//	private PullToRefreshListView lvData;
//	private ConsumptionAdapter adapter;
//	private TextView tv_num,tv_num1,tv_num2,tv_sun,times;
//	private int pageNo = 1;
//	private int pageSize = 10;
//	private SelectBirthday birth;
//	private String initstartdate;
//	private String sumTotalConsumption = "";
//	private View stopdata;
//	private TextView tv_date;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.consumption_series_activity);
//        
//        sumTotalConsumption = getIntent().getStringExtra("sumTotalConsumption");
//        initstartdate =  DateUtil.Yesterday();
//        initView();
//        loadData();
//    }
//
//    
//    public void initView() {
//    	initTopBar("消费系列");
//    	btnTopRight2 =  getView(R.id.btnTopRight2);
//    	stopdata = getView(R.id.stopdata);
//    	tv_date = getView(R.id.tv_date);
//    	btnTopRight2.setVisibility(View.VISIBLE);
//    	btnTopRight2.setBackgroundResource(R.drawable.btn_search);
//    	times = getView(R.id.times);
//    	tv_sun = getView(R.id.tv_sun);
//    	tv_num = getView(R.id.tv_num);
//		tv_num1 = getView(R.id.tv_num1);
//		tv_num2 = getView(R.id.tv_num2);
////		tv_num.setText("全秀儿消费总额: "+sumTotalConsumption);
//		tv_num1.setText(DateUtil.Yesterday());
////		tv_sun.setText("全平台消费统计: "+sumTotalConsumption);
////		times.setText(DateUtil.getCurrDate());
////		tv_num2.setText("秀心总数: 1000");
//		lvData = getView(R.id.lvShop);
//		lvData.setMode(Mode.DISABLED);
////		lvData.setMode(Mode.PULL_FROM_END);
////		lvData.setMode(Mode.PULL_FROM_START);
//		lvData.setOnRefreshListener(this);
//		
//		btnTopRight2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				setdate();
//			}
//		});
//    }
//    
//    
////    public void getstop() {
////		BannerRequest req = new BannerRequest();
////		RequestParams params = new RequestParams();
////		new HttpUtils().send(HttpMethod.GET, Api.GETBANNERSTOP, params,
////				new RequestCallBack<String>() {
////					@Override
////					public void onSuccess(ResponseInfo<String> resp) {
////						ShopShowDataResponse bean = new Gson().fromJson(resp.result, ShopShowDataResponse.class);
////						if(Api.SUCCEED == bean.result_code) {
////							if(bean.data.isStop.equals("1")){
////								stopdata.setVisibility(View.VISIBLE);
////							}else{
////								stopdata.setVisibility(View.GONE);
////								loadData();
////							}
////						}
////					}
////					@Override
////					public void onFailure(HttpException arg0, String arg1) {
////					}
////				});
////	}
//    
//    public void setdate(){
//		if(initstartdate.equals("")){
//			initstartdate = DateUtil.getCurrDate();
//		}
//		birth = new SelectBirthday(this,initstartdate,new MyOnclicListener() {
//
//			public void MyOnclicListener(View v) {
//				// TODO Auto-generated method stub
//				switch (v.getId()) {
//				case R.id.submit:
//					initstartdate = birth.getdata();
//					tv_num1.setText(initstartdate);
////					times.setText(initstartdate);
////					Toast.makeText(getActivity().getApplicationContext(), initstartdate, Toast.LENGTH_SHORT).show();
//					birth.dismiss();
//					onPullDownToRefresh(lvData);
////					pageNo = 1;
////					loadData();
//					break;
//				case R.id.cancel:
//					birth.dismiss();
//					break;
//				default:
//					break;
//				}
//			}
//		});
//		birth.showAtLocation(this.findViewById(R.id.root),
//				Gravity.BOTTOM, 0, 0);
//	}
//    
//    
//    
//	public void loadData() {
//		
//		ConsumptionRequest req = new ConsumptionRequest();
//		req.shop_id = UserInfoManager.getUserInfo(this).shop_id+"";
//		req.payDate = initstartdate.replace("-", "");
//		Log.e("", " - - - -  "+req.toString());
//		RequestParams params = new RequestParams("utf-8");
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		
//		new HttpUtils().send(HttpMethod.POST, Api.CONSUMPTIONLIST, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				lvData.onRefreshComplete();
//				T.showNetworkError(getApplicationContext());
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				lvData.onRefreshComplete();
//				ConsumptionResponse bean = new Gson().fromJson(resp.result, ConsumptionResponse.class);
//				if(Api.SUCCEED == bean.result_code){
//					
//					if(bean.data!=null&&bean.data.is_stop!=null&&bean.data.is_stop.equals("1")){
//						stopdata.setVisibility(View.VISIBLE);
//						tv_date.setText(initstartdate);
//						
//					}else{
//						updateView(bean.data);
//						stopdata.setVisibility(View.GONE);
//					}
//				};
//			}
//		});
//	}
//    
//	public void updateView(ConsumptionBean data) {
//		if(data==null){
//			tv_num.setText("全秀儿消费总额  0");
//			List<ExcitationBean> bean = new ArrayList<ExcitationBean>();
//			for (int i = 0; i < 3; i++) {
//				ExcitationBean b = new ExcitationBean();
//				b.amount = "0";
//				bean.add(b);
//			}
//			
//			
//			if(pageNo==1 || adapter==null) {
//				adapter = new ConsumptionAdapter(ConsumptionSeriesActivity.this, bean);
//				lvData.setAdapter(adapter);
//			}
//			if(pageNo > 1) {
//				adapter.getmData().addAll(bean);
//				adapter.notifyDataSetChanged();
//			}
//			pageNo++;
//			lvData.onRefreshComplete();
//			return;
//			
//		}
//		
//		
////		if(data.userSumFilial!=null){
//////			String amount = Util.getnum(data.oneSunTotalFee, true);
////			tv_num.setText("用户秀心总数: "+data.userSumFilial);
////	}
////		if(data.shopSumFilial!=null){
////				
//////				String amount = Util.getnum(data.shopSumFilial, true);
////				tv_num1.setText("商家秀心总数: "+data.shopSumFilial);
////		}
//		tv_num.setText("全秀儿消费总额"+Util.getnum(data.total_consumption,true));
//		List<ExcitationBean> bean = new ArrayList<ExcitationBean>();
//		if(data.three_total_consumption!=null){
//			ExcitationBean b = new ExcitationBean();
//			b.amount = Util.getnum(data.three_total_consumption, true);
//			bean.add(b);
//		}
//		if(data.two_total_consumption!=null){
//			ExcitationBean b = new ExcitationBean();
//			b.amount = Util.getnum(data.two_total_consumption, true);
//			bean.add(b);
//		}
//
//		if(data.one_total_consumption!=null){
//			ExcitationBean b = new ExcitationBean();
//			b.amount = Util.getnum(data.one_total_consumption, true);
//			bean.add(b);
//		}
//		
//		if(pageNo==1 || adapter==null) {
//			adapter = new ConsumptionAdapter(ConsumptionSeriesActivity.this, bean);
//			lvData.setAdapter(adapter);
//		}
//		if(pageNo > 1) {
//			adapter.getmData().addAll(bean);
//			adapter.notifyDataSetChanged();
//		}
//		pageNo++;
//		lvData.onRefreshComplete();
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
