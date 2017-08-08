package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.request.NumRequest;
import com.shareshenghuo.app.shop.network.response.NumResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.PreferenceUtils;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.Util;

public class ShuntMainActivity extends BaseTopActivity implements OnClickListener{
	
	 int statusBarHeight1;
	 public TextView tv_num,num1,num2;
	 private String num;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.shunt_main_activity);
		initview();
		initdata();
	}

	private void initdata() {
		// TODO Auto-generated method stub
		
		
		NumRequest req = new NumRequest();
		req.userId = UserInfoManager.getUserInfo(this).id+"";
		req.userType = "1";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.e("", ""+Api.GETBUNBER);
		new HttpUtils().send(HttpMethod.POST, Api.GETBUNBER, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				T.showNetworkError(ShuntMainActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				NumResponse bean = new Gson().fromJson(resp.result, NumResponse.class);
				Log.e("", ""+resp.result);
				if(Api.SUCCEED == bean.result_code) {
					tv_num.setText(Util.getnum(bean.data.totalFilialMoney,false)+"");
					num1.setText(Util.getnum(bean.data.filialMoney,false)+"");
					num2.setText(Util.getnum(bean.data.consumeFilialMoney,false)+"");
					num = bean.data.consumeFilialMoney;
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
		initTopBar("我的秀点");
		findViewById(R.id.btn1).setOnClickListener(this);
		findViewById(R.id.btn2).setOnClickListener(this);
		tv_num = getView(R.id.tv_num);
		num1 = getView(R.id.num1);
		num2 = getView(R.id.num2);
		btnTopRight1 = (Button) findViewById(R.id.btnTopRight1);
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("秀点说明");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		int resourceId = getResources().getIdentifier("status_bar_height","dimen", "android");
		if (resourceId > 0) {// 根据资源ID获取响应的尺寸值
			statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
		}
 		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
 			statusBarHeight1 = 0;
		}
		
// 		ViewUtil.showtip(ShuntMainActivity.this, getView(R.id.btn1), 0, statusBarHeight1, "mfirst3", R.drawable.shoppay_simg,null);
 		
 		
// 		ViewUtil.showtip(ShuntMainActivity.this, getView(R.id.btn1), 2, statusBarHeight1, "mfirst3", R.drawable.shoppay_simg,null);
 		
// 		ViewUtil.showtip(ShuntMainActivity.this, getView(R.id.btn1), 3, statusBarHeight1, "mfirst3", R.drawable.shoppay_simg,null);
//	      if(!PreferenceUtils.getPrefBoolean(this, "mfirst1", false)){
//	    	  showMask(statusBarHeight1, R.drawable.shoppay_simg, "mfirst1", getView(R.id.btn1));
	    	  
//	    	  ViewUtil.showtip(getActivity(), rootView.findViewById(R.id.llMinebankcard), 1, statusBarHeight1, "mfirst1", R.drawable.tip4);
//	    	  return;
//	      }
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn1:
			startActivity(new Intent(ShuntMainActivity.this, IncentivePointsActivity.class));
			break;
		case R.id.btn2:
			Intent it = new Intent(ShuntMainActivity.this, ConsumptionXFActivity.class);
			it.putExtra("num", num);
			startActivity(it);
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
	                      Intent intent = new Intent(ShuntMainActivity.this,TipsActivity.class);
	                      intent.putExtra("loc",loc);
	                      startActivityForResult(intent, 101);
	                      PreferenceUtils.setPrefBoolean(ShuntMainActivity.this, string, true);
//	                  }
//	              });
	            }
	        },500);
	    }
	
	
	
}
