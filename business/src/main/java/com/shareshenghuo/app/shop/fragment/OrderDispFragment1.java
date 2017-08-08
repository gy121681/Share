package com.shareshenghuo.app.shop.fragment;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.OrderDispositionActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.OrderdetailsListAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.RehistoryBean;
import com.shareshenghuo.app.shop.network.bean.ShopOrderDetailBean;
import com.shareshenghuo.app.shop.network.bean.WebLoadActivity;
import com.shareshenghuo.app.shop.network.request.NumRequest;
import com.shareshenghuo.app.shop.network.request.OrderDispositionRequest;
import com.shareshenghuo.app.shop.network.response.AutResponse;
import com.shareshenghuo.app.shop.network.response.RehistoryListResponse;
import com.shareshenghuo.app.shop.network.response.ShopOrderDetailResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.Utility;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;
import com.shareshenghuo.app.shop.widget.swipelistview.SwipeListView;

public class OrderDispFragment1 extends BaseFragment implements OnClickListener,OnRefreshListener2<ScrollView>{
	
	private SwipeListView lvMsg;
	public static OrderdetailsListAdapter adapter;
	public  int pageNo = 1;
	public static TextView tv_num;
	public TextView tvs_pro;
	private CheckBox check_dels;
	private int pageSize = 100;
	public List<RehistoryBean> data;
	private TwoButtonDialog downloadDialog;
//	public List<RehistoryBean> datas;
	public PullToRefreshScrollView svContent;
	public static String balance = "";
	public static StringBuffer buff = new StringBuffer();
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.orderdispositio_activity;
	}

	@Override
	protected void init(View rootView) {
		initview();
	}
	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		pageNo = 1;
		loadData();
	}

	private void initview() {
		// TODO Auto-generated method stub
//		initTopBar("订单详情");
		lvMsg = getView(R.id.lvMsg);
		check_dels= getView(R.id.check_dels);
		tv_num = getView(R.id.tv_num);
		getView(R.id.llWalletRecharge).setOnClickListener(this);
		lvMsg.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		svContent = getView(R.id.svHomeContent);
		svContent.setOnRefreshListener(this);
		svContent.setMode(Mode.BOTH);
		tvs_pro = getView(R.id.tvs_pro);
		data = new ArrayList<RehistoryBean>();
		adapter = new OrderdetailsListAdapter(activity, data,0);
		lvMsg.setAdapter(adapter);
		
		getStatisticsData();
		
		check_dels.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
					buff = null;
					buff = new StringBuffer();
					adapter.setcheck(arg1);
					Double balances = 0.0;
					List<RehistoryBean>  data = adapter.getchoose();
					for (int i = 0; i < data.size(); i++) {
						if(data.get(i).ischeck){
							if(data.get(i).fee_amt!=null){
								 balances += Double.parseDouble(data.get(i).fee_amt);
								 buff.append(data.get(i).id+",");
							}
						}
					}
						balance = String.valueOf(balances/100);
						tv_num.setText(balance+"元");
			}
		});
		
		lvMsg.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.e("", "lvMsg");
				adapter.setchoose((int)arg3);
				List<RehistoryBean>  data = adapter.getchoose();
				Double balances =0.0;
				for (int i = 0; i < data.size(); i++) {
					if(data.get(i).ischeck){
						 balances += Double.parseDouble(data.get(i).fee_amt)/100;
						 buff.append(data.get(i).id+",");
					}
				}
					balance = String.valueOf(balances);
					tv_num.setText(balance+"元");
			}
		});
		
	}
	
	public void getStatisticsData() {
		NumRequest req = new NumRequest();
		req.shop_id = UserInfoManager.getUserInfo(activity).shop_id+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETSHOPORDERDETAILS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ShopOrderDetailResponse bean = new Gson().fromJson(resp.result, ShopOrderDetailResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					init(bean.data);
				}
			}
		});
	}
	
	
	public void getStata() {
		final String moneys = String.valueOf((int)(round((Double.parseDouble(balance)*100),2)));
		ProgressDialogUtil.showProgressDlg(activity, "请稍候");
		ProgressDialogUtil.setCancelable(true);
		NumRequest req = new NumRequest();
		req.shopId = UserInfoManager.getUserInfo(activity).shop_id+"";
		req.ids = buff.toString();
		req.money = moneys;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.RISKCHECKNEW, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					if(bean.data.RSPCOD.equals("000000")){
						String url = Api.wxHOST+"oneCity/ytPayNew/qrCodeUnifiedPayForShop?"+"shopId="+UserInfoManager.getUserInfo(activity).shop_id+
						"&ids="+buff.toString()+"&money="+moneys;

						Intent about = new Intent(activity, WebLoadActivity.class);
						about.putExtra("title", "收银台");
						about.putExtra("url", url);
						startActivity(about);
					}else{
//						if(downloadDialog!=null&&!downloadDialog.isShowing()){
							initDialog(bean.data.RSPMSG, "", "确定");
//						}
					}
				}else{
					Log.e("", " 444444");
					T.showShort(activity, "请求失败");
				}
			}
		});
	}
	
	
	public  double round(double value, int scale) {  
        
        if (scale < 0) {  
            throw new IllegalArgumentException(  
                    "The scale must be a positive integer or zero");  
        }  
          
        BigDecimal b = new BigDecimal(Double.toString(value));  
        BigDecimal one = new BigDecimal("1");  
          
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();  
    }  
	
	
	private void init(ShopOrderDetailBean data) {
		if(!TextUtils.isEmpty(data.risk_money)){
			tvs_pro.setText("当前可用额度: "+Double.parseDouble(data.risk_money)/100+"元");
		}
	}
	
	public static void setnum(){
		buff = null;
		buff = new StringBuffer();
		Double balances = 0.0;
		List<RehistoryBean>  data = adapter.getchoose();
		for (int i = 0; i < data.size(); i++) {
			if(data.get(i).ischeck){
				 balances += Double.parseDouble(data.get(i).fee_amt);
				 buff.append(data.get(i).id+",");
			}
		}
		balance = String.valueOf(balances/100);
		tv_num.setText(balance+"元");
	}
	
	public void loadData() {
//		ProgressDialogUtil.showProgressDlg(activity, "请稍候");
		OrderDispositionRequest req = new OrderDispositionRequest();
		req.shop_id = UserInfoManager.getUserInfo(activity).shop_id+"";
		req.pay_status = "0";
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.SHOPPAYCLSINFLISTSNEW, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				svContent.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				svContent.onRefreshComplete();
				RehistoryListResponse bean = new Gson().fromJson(resp.result, RehistoryListResponse.class);
				if(Api.SUCCEED == bean.result_code)
//					data = bean.data;
					updateView(bean.data);
					Utility.setListViewHeightBasedOnChildren(lvMsg);
					
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.llWalletRecharge:
			if(!adapter.ischeck()){
				T.showShort(activity, "请选择");
				return;
			}
			getStata();
//			Intent it = new Intent(OrderDispositionActivity.this,PaymentnewActivity.class);
//			it.putExtra("balance", balance);
//			it.putExtra("ids", buff.toString());
//			startActivity(it);
			
//			try {
//				float bala1 = Float.parseFloat(balance);
////				double bala = Double.parseDouble(balance.getText().toString());
//				if(bala1<=0){
//					T.showShort(getApplicationContext(), "请选择订单");
//					return;
//				}else if(bala1<0.1){
//					T.showShort(getApplicationContext(), "金额不能小于0.1");
//					return;
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//				T.showShort(getApplicationContext(), "金额有误");
//				return;
//			}
			

//			finish();
			
			

//			startActivity(new Intent(OrderDispositionActivity.this,PaymentnewActivity.class));
			break;

		default:
			break;
		}
		
	}
    
	public void updateView(List<RehistoryBean> datas ) {
		
		if(pageNo==1 || adapter==null) {
			data.clear();
			data.addAll(datas);
			adapter.notifyDataSetChanged();
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(datas);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
		
//		if(pageNo==1 || adapter==null) {
//			adapter = new OrderdetailsListAdapter(OrderDispositionActivity.this, data);
//			lvMsg.setAdapter(adapter);
//		}
//		if(pageNo > 1) {
//			adapter.getmData().addAll(data);
//			adapter.notifyDataSetChanged();
//		}
//		pageNo++;
		setnum();
		check_dels.setChecked(false);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		loadData();
	}
	
	private void initDialog(String content,String left,String right) {
		// TODO Auto-generated method stub
		Log.e("", " 111111");
		downloadDialog = new TwoButtonDialog(getActivity(), R.style.CustomDialog,
				"提示", content, left, right,true,new OnMyDialogClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
		}
	
}
