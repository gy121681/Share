package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.fragment.IncentivePoints1Fragment;
import com.shareshenghuo.app.shop.fragment.IncentivePoints2Fragment;
import com.shareshenghuo.app.shop.fragment.IncentivePointsFragment;
import com.shareshenghuo.app.shop.fragment.Qrcode1Fm;
import com.shareshenghuo.app.shop.fragment.Qrcode2Fm;
import com.shareshenghuo.app.shop.fragment.QrcodeFm;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.QRcodeBean;
import com.shareshenghuo.app.shop.network.request.BusinesstodayRequest;
import com.shareshenghuo.app.shop.network.request.QrcodeRequest;
import com.shareshenghuo.app.shop.network.response.BusinesstodayResponse;
import com.shareshenghuo.app.shop.network.response.QrcodeResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.widget.MyTabView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * @author hang 秀点
 */
public class QrCodeActivity extends BaseTopActivity {

	private MyTabView tabView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_incentivepoints1);
		initView();

	}

	private void initdata() {
		// TODO Auto-generated method stub
		QrcodeRequest req = new QrcodeRequest();
		req.shopId = UserInfoManager.getUserInfo(this).shop_id + "";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GENERCODELIST, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// T.showNetworkError(getApplicationContext());
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						QrcodeResponse bean = new Gson().fromJson(resp.result,
								QrcodeResponse.class);
						Log.e("", "" + resp.result);
						if (Api.SUCCEED == bean.result_code) {
							if(bean.data.size()>0){
								updateView(bean.data);
							}
						}
					}

				});
	}

	private void updateView(List<QRcodeBean> data) {
		// TODO Auto-generated method stub
		List<Map<String, Integer>> titles = new ArrayList<Map<String, Integer>>();
		List<Fragment> fragments = new ArrayList<Fragment>();
		for (int i = 0; i < data.size(); i++) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			if (data.get(i).discountType.equals("3")) {
				Bundle bundle1 = new Bundle();
				bundle1.putString("url", data.get(i).returnUrl);
				QrcodeFm fm = new QrcodeFm();
				fm.setArguments(bundle1);
				fragments.add(fm);
				map.put("100%积分", null);
				titles.add(map);
			} else if (data.get(i).discountType.equals("2")) {

				Bundle bundle1 = new Bundle();
				bundle1.putString("url", data.get(i).returnUrl);
				QrcodeFm fm = new QrcodeFm();
				fm.setArguments(bundle1);
				fragments.add(fm);
				map.put("50%积分", null);
				titles.add(map);
			} else if (data.get(i).discountType.equals("1")) {
				Bundle bundle1 = new Bundle();
				bundle1.putString("url", data.get(i).returnUrl);
				QrcodeFm fm = new QrcodeFm();
				fm.setArguments(bundle1);
				fragments.add(fm);
				map.put("25%积分", null);
				titles.add(map);
			}

		}
		tabView.createView(titles, fragments, getSupportFragmentManager());

	}

	public void initView() {
		initTopBar("我的收款码");
		tabView = (MyTabView) findViewById(R.id.tabFavorites);

		initdata();
		// Map<String,Integer> map = new HashMap<String, Integer>();
		// map.put("100%积分", null);
		// titles.add(map);
		// map = new HashMap<String, Integer>();
		// map.put("25%积分", null);
		// titles.add(map);
		// map = new HashMap<String, Integer>();
		// map.put("5%积分", null);
		// titles.add(map);

		// fragments.add(new Qrcode1Fm());
		// fragments.add(new Qrcode2Fm());

	}

}
