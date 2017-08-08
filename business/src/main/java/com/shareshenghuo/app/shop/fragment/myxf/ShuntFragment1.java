package com.shareshenghuo.app.shop.fragment.myxf;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.ConsumptionXFActivity;
import com.shareshenghuo.app.shop.IncentivePointsActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.TipsActivity;
import com.shareshenghuo.app.shop.fragment.BaseFragment;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.request.NumRequest;
import com.shareshenghuo.app.shop.network.response.NumResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.PreferenceUtils;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.Util;

public class ShuntFragment1 extends  BaseFragment implements OnClickListener{
	int statusBarHeight1;
	public TextView tv_num, num1, num2,tv_title;
	private String num;
	private String k_num;//产业链秀点
	
	
	@Override
	protected int getLayoutId() {
		return R.layout.shunt_main_activity;
	}

	@Override
	protected void init(View rootView) {
		
		initview();
		initdata();
	}
	private void initdata() {
		// TODO Auto-generated method stub
		
		
		NumRequest req = new NumRequest();
		req.userId = UserInfoManager.getUserInfo(getActivity()).shop_id+"";
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
				T.showNetworkError(getActivity());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				NumResponse bean = new Gson().fromJson(resp.result, NumResponse.class);
				Log.e("", ""+resp.result);
				if(Api.SUCCEED == bean.result_code) {
					if(bean.data.totalFilialMoneyNew!=null){
						tv_num.setText(Util.getnum(bean.data.totalFilialMoneyNew,false)+"");
						num1.setText(Util.getnum(bean.data.filialMoneyNew,false)+"");
						num2.setText(Util.getnum(bean.data.stayFilialMoneyNew,false)+"");
						num = bean.data.stayFilialMoneyNew;
						k_num=bean.data.filialMoneyNew;//可兑换秀点
					}

//					if(Double.parseDouble(bean.data.filialMoney)/100>=100){
//						tv_withdrawals_num.setText((int)((Double.parseDouble(bean.data.filialMoney)/10000))+"00");
//					}else{
//						tv_withdrawals_num.setText("0");
//					}
//					tv_withdrawals_num.setText(Util.getIntnum(bean.data.filialMoney,false)+"");
				}
			}
		});
	}

	private void initview() {
		// TODO Auto-generated method stub
		getView(R.id.btn1).setOnClickListener(this);
		getView(R.id.btn2).setOnClickListener(this);
		tv_num = getView(R.id.tv_num);
		num1 = getView(R.id.num1);
		num2 = getView(R.id.num2);
		tv_title = getView(R.id.tv_title);
		tv_title.setText("产业链秀点总额");
		
 		
 		
// 		int  statusBarHeight1 = 0;
//		//获取status_bar_height资源的ID  
//		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");  
//		if (resourceId > 0) {  
//		    //根据资源ID获取响应的尺寸值  
//		    statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);  
//		}  
// 		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
// 			statusBarHeight1 = 0;
//		}
// 		if(!PreferenceUtils.getPrefBoolean(getActivity(), "mfirst4", false)){
//		ViewUtil.showtip(getActivity(), getView(R.id.ll), 0, statusBarHeight1, "mfirst4", R.drawable.xiaofenpro,null);
// 		}
 		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn1:
			Intent it = new Intent(getActivity(), IncentivePointsActivity.class);
			it.putExtra("tag", "1");
			it.putExtra("k_num", k_num);
			startActivity(it);
//			startActivity(new Intent(getActivity(), IncentivePointsActivity.class));
			break;
		case R.id.btn2:
			Intent its = new Intent(getActivity(), ConsumptionXFActivity.class);
			its.putExtra("tag", "1");
			its.putExtra("num", num);
			startActivity(its);
			break;
		default:
			break;
		}
		
	}
	
	
	 private void showMask(final int statusBarHeight1 ,final int tip, final String string,final View view) {
//			view = (LinearLayout) rootView.findViewById(R.id.llMinebankcard);
			view.postDelayed(new Runnable() {
	            @Override
	            public void run() {
//	              runOnUiThread(new Runnable() {
//	                  @Override
//	                  public void run() {
	                     
	                      int right = view.getWidth()/2;
	                      int left = view.getLeft();
	                      int top = view.getTop()+statusBarHeight1;
	                      int bottom = view.getBottom()+statusBarHeight1;
	                      int hight = top;
	                      int loc[] = {left,top,right,bottom,hight,tip,1};
	                      Intent intent = new Intent(getActivity(),TipsActivity.class);
	                      intent.putExtra("loc",loc);
	                      startActivityForResult(intent, 101);
	                      PreferenceUtils.setPrefBoolean(getActivity(), string, true);
//	                  }
//	              });
	            }
	        },500);
	    }
}
