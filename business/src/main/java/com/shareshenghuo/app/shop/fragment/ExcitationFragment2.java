package com.shareshenghuo.app.shop.fragment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

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
import com.shareshenghuo.app.shop.TransferActivity;
import com.shareshenghuo.app.shop.adapter.ExcitationAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.FractionBean;
import com.shareshenghuo.app.shop.network.request.FractionRequest;
import com.shareshenghuo.app.shop.network.request.NumRequest;
import com.shareshenghuo.app.shop.network.response.FractionResponse;
import com.shareshenghuo.app.shop.network.response.NumResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;

public class ExcitationFragment2  extends BaseFragment implements OnRefreshListener2<ListView>{
	
	private PullToRefreshListView lvData;
	
	private int pageNo = 1;
	private int pageSize = 10;
	private ExcitationAdapter adapter;
	private TextView tv_title,tv_num,tv_title1,tv_num1;
	private TextView trans_btn;
	
//	private String filialPiety,totalFilialPiety;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.excitation_fragment;
	}

	@Override
	protected void init(View rootView) {
		// TODO Auto-generated method stubxl
//        filialPiety = getIntent().getStringExtra("filialPiety");
//        totalFilialPiety = getIntent().getStringExtra("totalFilialPiety");
        initView();
        
        loadData();
	}
	
	protected void initView() {
//		initTopBar("我的秀心");
    	tv_title = getView(R.id.tv_title);
    	tv_title1 = getView(R.id.tv_title1);
    	
    	trans_btn = getView(R.id.trans_btn);
    
		tv_num = getView(R.id.tv_num);
		tv_num1 = getView(R.id.tv_num1);
//		tv_num.setText(filialPiety);
//		tv_num1.setText(totalFilialPiety);
		lvData = getView(R.id.lvShop);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		getStatisticsData();
		
		trans_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent it = new Intent(activity, TransferActivity.class);
				it.putExtra("tag", "0");
				startActivity(it);
			}
		});
//		tv_num.setText("累计秀心数  "+totalFilialPiety);
//		tv_title.setText("当前秀心数  "+filialPiety);
	}
	
	
	  public void getStatisticsData() {
			
			
//			ProgressDialogUtil.showProgressDlg(getActivity(), "");
			NumRequest req = new NumRequest();
			try {
				req.userId = UserInfoManager.getUserInfo(getActivity()).shop_id+"";
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			req.userType = "2";
			
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
//					T.showNetworkError(IntegralDivisionActivity.this);
				}

				@Override
				public void onSuccess(ResponseInfo<String> resp) {
					ProgressDialogUtil.dismissProgressDlg();
					NumResponse bean = new Gson().fromJson(resp.result, NumResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						tv_num1.setText("累计秀心数  "+bean.data.totalFilialPiety+"");
						tv_title.setText("当前秀心数  "+bean.data.filialPiety+"");
//						lvData.setAdapter(new DataReportAdapter(activity, bean.data));
					}
				}
			});
		}
	
	
	public void loadData() {
		
		FractionRequest req = new FractionRequest();
		req.userId = UserInfoManager.getUserInfo(getActivity()).shop_id+"";
		req.type = "";
		req.userType = "2";
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
		new HttpUtils().send(HttpMethod.POST, Api.FRACTIONLIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				FractionResponse bean = new Gson().fromJson(resp.result, FractionResponse.class);
				if(Api.SUCCEED == bean.result_code)
					Log.e("", " - - - -  "+resp.result.toString());
				if(bean.data!=null){
					updateView(bean.data);
				}
			}
		});
		
	}
	
	public void updateView(List<FractionBean> data) {
		if(pageNo==1 || adapter==null ) {
			adapter = new ExcitationAdapter(activity, data);
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
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData();
	}


}
