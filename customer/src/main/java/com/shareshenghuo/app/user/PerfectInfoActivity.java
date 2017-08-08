package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.city.CityPicker;
import com.shareshenghuo.app.user.city.onChoiceCytyChilListener;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.request.CityRequest;
import com.shareshenghuo.app.user.network.response.AutResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class PerfectInfoActivity extends BaseTopActivity implements
		OnClickListener {

	private TextView tv_1, tv_2, tv_3, commit;
	private CityPicker citypicker;
	private String bankProvinceid, bankCityid, bankareid;
	private LinearLayout lin_cascade;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.perfect_info_activity);
		initview();
		initdata();
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("常住地址填写");
		citypicker = getView(R.id.citypicker);
		tv_1 = getView(R.id.tv_1);
		tv_2 = getView(R.id.tv_2);
		tv_3 = getView(R.id.tv_3);
		tv_1.setOnClickListener(this);
		tv_2.setOnClickListener(this);
		tv_3.setOnClickListener(this);

		commit = (TextView) findViewById(R.id.commit);
		commit.setOnClickListener(this);
		getView(R.id.llTopBack).setVisibility(View.GONE);
		getView(R.id.tv_finishs).setOnClickListener(this);
		lin_cascade = getView(R.id.lin_cascade);
		getView(R.id.tv_comit).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_1:
			initsity();
			break;
		case R.id.tv_2:
			initsity();
			break;
		case R.id.tv_3:
			initsity();
			break;
		case R.id.commit:
			updata();
			break;
		case R.id.tv_finishs:
			lin_cascade.setVisibility(View.INVISIBLE);
			// updata();
			break;
		case R.id.tv_comit:
			commit.setEnabled(true);
			lin_cascade.setVisibility(View.INVISIBLE);
			citypicker.setfirstdata();
			break;

		default:
			break;
		}

	}

	private void initdata() {
		citypicker.getcity(new onChoiceCytyChilListener() {
			@Override
			public void onClick(String i, String v, String a, String ni,
					String vi, String ai) {
				bankProvinceid = i;
				bankCityid = v;
				bankareid = a;
				try {
					tv_1.setText(ni);
					tv_2.setText(vi);
					tv_3.setText(ai);
				} catch (Exception e) {
				}
			}
		});
	}

	public void initsity() {

		if (lin_cascade.getVisibility() == View.VISIBLE) {
			lin_cascade.setVisibility(View.INVISIBLE);
		} else {
			lin_cascade.setVisibility(View.VISIBLE);
		}
	}

	public void updata() {
		ProgressDialogUtil.showProgressDlg(this, "请稍后...");
		CityRequest req = new CityRequest();
		req.user_id = UserInfoManager.getUserInfo(this).id + "";
		req.province_code = bankProvinceid;
		req.city_code = bankCityid;
		req.area_code = bankareid;
		Log.e("", "" + req.toString());
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.UPDATEUSERPOSITION, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ProgressDialogUtil.dismissProgressDlg();
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						ProgressDialogUtil.dismissProgressDlg();
						Log.e("", "" + resp.result);
						AutResponse bean = new Gson().fromJson(resp.result,
								AutResponse.class);
						if (Api.SUCCEED == bean.result_code) {
							if (bean.data.RSPCOD.equals("000000")) {

								T.showShort(getApplicationContext(),
										bean.data.RSPMSG);
								finish();
							} else {
								T.showShort(getApplicationContext(),
										bean.data.RSPMSG);
							}
						} else {
							T.showShort(getApplicationContext(),
									bean.result_desc);
						}
					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// T.showShort(getApplicationContext(), bean.data.RSPMSG);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
