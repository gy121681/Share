package com.shareshenghuo.app.user.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.ConsumptionAdapter;
import com.shareshenghuo.app.user.fragment.BaseFragment;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.ConsumptionBean;
import com.shareshenghuo.app.user.network.bean.ExcitationBean;
import com.shareshenghuo.app.user.network.request.ConsumptionRequest;
import com.shareshenghuo.app.user.network.request.NumRequest;
import com.shareshenghuo.app.user.network.response.ConsumptionResponse;
import com.shareshenghuo.app.user.network.response.NumResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.DateUtil;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.Util;
import com.example.widget.SelectBirthday;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.view.View;
import android.widget.TextView;

public class ShowDateFm2 extends BaseFragment {
	
	private PullToRefreshListView lvData;
	private ConsumptionAdapter adapter;
	private TextView tv_num,tv_num1,tv_num2,tv_sun,times,tv_date;
	private int pageNo = 1;
	private int pageSize = 10;
	private SelectBirthday birth;
	private String initstartdate;
	private String sumTotalConsumption = "";
	private View stopdata;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.consumption_series_fm;
	}

	@Override
	protected void init(View rootView) {
		// TODO Auto-generated method stub
		stopdata = getView(R.id.stopdata);
		tv_date = getView(R.id.tv_date);
    	times = getView(R.id.times);
    	tv_sun = getView(R.id.tv_sun);
    	tv_num = getView(R.id.tv_num);
		tv_num1 = getView(R.id.tv_num1);
		tv_num2 = getView(R.id.tv_num2);
//		tv_num.setText("全秀儿消费总额: "+sumTotalConsumption);
		tv_num1.setText(DateUtil.Yesterday());
//		tv_sun.setText("全平台消费统计: "+sumTotalConsumption);
//		times.setText(DateUtil.getCurrDate());
//		tv_num2.setText("秀心总数: 1000");
		lvData = getView(R.id.lvShop);
		lvData.setMode(Mode.DISABLED);
//		lvData.setMode(Mode.PULL_FROM_END);
//		lvData.setMode(Mode.PULL_FROM_START);
	    initstartdate =  DateUtil.Yesterday();
		getStatisticsData();
		loadData(initstartdate);
	}
	
	 public void getStatisticsData() {
			NumRequest req = new NumRequest();
			req.userId = UserInfoManager.getUserInfo(getActivity()).id+"";
			req.userType = "1";
			
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
//					T.showNetworkError(getActivity());
				}

				@Override
				public void onSuccess(ResponseInfo<String> resp) {
					ProgressDialogUtil.dismissProgressDlg();
					NumResponse bean = new Gson().fromJson(resp.result, NumResponse.class);
					if(Api.SUCCEED == bean.result_code) {
//						tv_num.setText("全秀儿消费总额: "+bean.data.totalFilialPiety);
//						lvData.setAdapter(new DataReportAdapter(activity, bean.data));
					}
				}
			});
	    }
		public void loadData(String initstartdates) {
			pageNo = 1;
			tv_num1.setText(initstartdates);
			ConsumptionRequest req = new ConsumptionRequest();
			req.shop_id = UserInfoManager.getUserInfo(getActivity())._id+"";
			req.payDate = initstartdates.replace("-", "");
			RequestParams params = new RequestParams("utf-8");
			try {
				params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			new HttpUtils().send(HttpMethod.POST, Api.CONSUMPTIONLIST, params, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					lvData.onRefreshComplete();
					T.showNetworkError(getActivity().getApplicationContext());
				}

				@Override
				public void onSuccess(ResponseInfo<String> resp) {
					lvData.onRefreshComplete();
					
					ConsumptionResponse bean = new Gson().fromJson(resp.result, ConsumptionResponse.class);
					if(Api.SUCCEED == bean.result_code){
						if(bean.data!=null&&bean.data.is_stop!=null&&bean.data.is_stop.equals("1")){
							stopdata.setVisibility(View.VISIBLE);
							tv_date.setText(initstartdate);
						}else{
							
							updateView(bean.data);
							stopdata.setVisibility(View.GONE);
						}
					};
				}
			});
		}
	    
		public void updateView(ConsumptionBean data) {
			if(data==null){
				tv_num.setText("全秀儿消费总额  0");
				List<ExcitationBean> bean = new ArrayList<ExcitationBean>();
				for (int i = 0; i < 3; i++) {
					ExcitationBean b = new ExcitationBean();
					b.amount = "0";
					bean.add(b);
				}
				
				
				if(pageNo==1 || adapter==null) {
					adapter = new ConsumptionAdapter(activity, bean);
					lvData.setAdapter(adapter);
				}
				if(pageNo > 1) {
					adapter.getmData().addAll(bean);
					adapter.notifyDataSetChanged();
				}
				pageNo++;
				lvData.onRefreshComplete();
				return;
				
			}
			
			
//			if(data.userSumFilial!=null){
////					String amount = Util.getnum(data.oneSunTotalFee, true);
//					
//			}
//			if(data.shopSumFilial!=null){
//					
////					String amount = Util.getnum(data.shopSumFilial, true);
//					tv_num1.setText("商家秀心总数: "+data.shopSumFilial);
//			}
			tv_num.setText("全秀儿消费总额"+Util.getnum(data.total_consumption,true));
			List<ExcitationBean> bean = new ArrayList<ExcitationBean>();
			if(data.three_total_consumption!=null){
				ExcitationBean b = new ExcitationBean();
				b.amount = Util.getnum(data.three_total_consumption, true);
				bean.add(b);
			}
			if(data.two_total_consumption!=null){
				ExcitationBean b = new ExcitationBean();
				b.amount = Util.getnum(data.two_total_consumption, true);
				bean.add(b);
			}

			if(data.one_total_consumption!=null){
				ExcitationBean b = new ExcitationBean();
				b.amount = Util.getnum(data.one_total_consumption, true);
				bean.add(b);
			}
			
			if(pageNo==1 || adapter==null) {
				adapter = new ConsumptionAdapter(getActivity(), bean);
				lvData.setAdapter(adapter);
			}
			if(pageNo > 1) {
				adapter.getmData().addAll(bean);
				adapter.notifyDataSetChanged();
			}
			pageNo++;
			lvData.onRefreshComplete();
		}

}
