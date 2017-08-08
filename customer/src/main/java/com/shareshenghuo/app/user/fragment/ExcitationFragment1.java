package com.shareshenghuo.app.user.fragment;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baozi.Zxing.CaptureActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.TransferActivity;
import com.shareshenghuo.app.user.adapter.ExcitationAdapter1;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.FractionBean;
import com.shareshenghuo.app.user.network.request.FractionRequest;
import com.shareshenghuo.app.user.network.request.NumRequest;
import com.shareshenghuo.app.user.network.response.FractionResponse;
import com.shareshenghuo.app.user.network.response.NumResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.PreferenceUtils;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
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

public class ExcitationFragment1  extends BaseFragment implements OnRefreshListener2<ListView>{
	
	private PullToRefreshListView lvData;
	
	private int pageNo = 1;
	private int pageSize = 10;
	private ExcitationAdapter1 adapter;
	private TextView tv_title,tv_num,tv_title1,tv_num1;
	private LinearLayout ll_marke;
	private TextView trans_btn;
//	private String filialPiety,totalFilialPiety;
	
	int statusBarHeight1;
	
	
	@Override
	protected int getLayoutId() {
		return R.layout.excitation_fragment;
	}

	@Override
	protected void init(View rootView) {
//        filialPiety = getIntent().getStringExtra("filialPiety");
//        totalFilialPiety = getIntent().getStringExtra("totalFilialPiety");
        initView();
        
        loadData();
        
        
	}
  
    
	
	@Override
	public void onActivityResult(int reqCode, int resCode, Intent data) {
		super.onActivityResult(reqCode, resCode, data);
		if(resCode == Activity.RESULT_OK) {
			switch(reqCode) {
			case CaptureActivity.REQ_SCAN_QR:
				
			case 101:
			      if(!PreferenceUtils.getPrefBoolean(getActivity(), "mfirst22", false)){
//			    	  ViewUtil.showtip(getActivity(), rootView.findViewById(R.id.llin1), 1, statusBarHeight1, "mfirst22", R.drawable.tip4,null);
			    	  return;
			      }
				break;
			}
			
		
		}
	}
	
	
	protected void initView() {
//		initTopBar("我的秀心");
    	tv_title = getView(R.id.tv_title);
    	ll_marke = getView(R.id.ll_marke);
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
//		tv_num.setText("累计秀心数  "+totalFilialPiety);
//		tv_title.setText("当前秀心数  "+filialPiety);
		
		trans_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(UserInfoManager.getUserInfo(activity).band_type!=2){
				T.showShort(activity, "请先绑定微信");
				return;
				}
				if(!UserInfoManager.getUserInfo(activity).certification_status.equals("2")){
					T.showShort(activity, "请先实名认证");
					return;
				}
				Intent it  = new Intent(activity,TransferActivity.class);
				it.putExtra("tag", "0");
				startActivity(it);
			}
		});
	}
	
	
	
	
	  public void getStatisticsData() {
			
//			ProgressDialogUtil.showProgressDlg(getActivity(), "");
			NumRequest req = new NumRequest();
			try {
				req.userId = UserInfoManager.getUserInfo(getActivity()).id+"";
				System.out.println("====用户id:"+req.userId);
			} catch (Exception e) {
				// TODO: handle exception
			}
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
//					T.showNetworkError(IntegralDivisionActivity.this);
				}

				@Override
				public void onSuccess(ResponseInfo<String> resp) {
					ProgressDialogUtil.dismissProgressDlg();
					NumResponse bean = new Gson().fromJson(resp.result, NumResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						if(bean.data.totalFilialPietyNew!=null){
							tv_num.setText("累计秀心数  "+bean.data.totalFilialPietyNew+"");
							tv_title.setText("当前秀心数  "+bean.data.filialPietyNew+"");
						}

//						lvData.setAdapter(new DataReportAdapter(activity, bean.data));
					}
				}
			});
		}
	
	
	public void loadData() {
		
		FractionRequest req = new FractionRequest();
		req.userId = UserInfoManager.getUserInfo(getActivity()).id+"";
		req.type = "";
		req.userType = "1";
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
		new HttpUtils().send(HttpMethod.POST, Api.FRACTIONLISTNEW, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				FractionResponse bean = new Gson().fromJson(resp.result, FractionResponse.class);
				if(Api.SUCCEED == bean.result_code)
				
				if(bean.data!=null){
					updateView(bean.data);
				}
			}
		});
		
	}
	
	public void updateView(List<FractionBean> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new ExcitationAdapter1(getActivity(), data);
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
