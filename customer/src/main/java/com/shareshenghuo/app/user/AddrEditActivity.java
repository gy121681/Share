package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.AddrInfo;
import com.shareshenghuo.app.user.network.request.AddrEditRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.ViewUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author hang
 * 新增/编辑地址
 */
public class AddrEditActivity extends BaseTopActivity implements OnClickListener {
	
	private static final int REQ_PICK_CITY = 0x101;
	private static final int REQ_PICK_AREA = 0x102;
	
	private EditText edName;
	private EditText edMobile;
	private TextView tvCity;
	private TextView tvArea;
	private EditText edSnippet;
	private CheckBox cbDefault;
	
	private AddrInfo addrInfo;
	
	private boolean isNewAddr = true;	// true 新增  false 修改

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addr_edit);
		init();
	}
	
	public void init() {
		addrInfo = (AddrInfo) getIntent().getSerializableExtra("addrInfo");
		initTopBar("地址管理");
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("保存");
		btnTopRight1.setOnClickListener(this);
		
		edName = getView(R.id.edAddrName);
		edMobile = getView(R.id.edAddrMobile);
		tvCity = getView(R.id.tvAddrCity);
		tvArea = getView(R.id.tvAddrArea);
		edSnippet = getView(R.id.edAddrSnippet);
		cbDefault = getView(R.id.cbAddrDefault);
		
		if(addrInfo != null) {
			isNewAddr = false;
			edName.setText(addrInfo.real_name);
			edMobile.setText(addrInfo.mobile);
			tvCity.setText(addrInfo.city_name);
			tvArea.setText(addrInfo.area_name);
			edSnippet.setText(addrInfo.address);
			cbDefault.setChecked(addrInfo.status == 1);
		} else {
			isNewAddr = true;
			addrInfo = new AddrInfo();
		}
		
		findViewById(R.id.llPickCity).setOnClickListener(this);
		findViewById(R.id.llPickArea).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnTopRight1:
			submit();
			break;
			
		case R.id.llPickCity:
			Intent city = new Intent(this, CityListActivity.class);
			city.putExtra("from", CityListActivity.FROM_ADDR_MANAGE);
			startActivityForResult(city, REQ_PICK_CITY);
			break;
			
		case R.id.llPickArea:
			if(addrInfo.city_id == 0) {
				T.showShort(this, "请先选择城市");
				return;
			}
			Intent area = new Intent(this, AreaListActivity.class);
			area.putExtra("cityId", addrInfo.city_id);
			startActivityForResult(area, REQ_PICK_AREA);
			break;
		}
	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		if(resCode == RESULT_OK) {
			switch(reqCode) {
			case REQ_PICK_CITY:
				addrInfo.city_id = data.getIntExtra("cityId", 0);
				addrInfo.city_name = data.getStringExtra("cityName");
				tvCity.setText(addrInfo.city_name);
				break;
				
			case REQ_PICK_AREA:
				addrInfo.area_id = data.getIntExtra("areaId", 0);
				addrInfo.area_name = data.getStringExtra("areaName");
				tvArea.setText(addrInfo.area_name);
				break;
			}
		}
	}
	
	public void submit() {
		if(ViewUtil.checkEditEmpty(edName, "输入姓名"))
			return;
		if(ViewUtil.checkEditEmpty(edMobile, "输入手机号"))
			return;
		if(ViewUtil.checkEditEmpty(edSnippet, "输入详细地址"))
			return;
		if(addrInfo.city_id==0 || addrInfo.area_id==0) {
			T.showShort(this, "请选择城市和区域");
			return;
		}
		
		ProgressDialogUtil.showProgressDlg(this, "保存中");
		String api = "";
		AddrEditRequest req = new AddrEditRequest();
		if(isNewAddr) {
			api = Api.URL_ADDR_NEW;
			req.user_id = UserInfoManager.getUserId(this)+"";
		} else {
			api = Api.URL_ADDR_EDIT;
			req.address_id = addrInfo.id+"";
		}
		req.real_name = edName.getText().toString();
		req.mobile = edMobile.getText().toString();
		req.city_id = addrInfo.city_id+"";
		req.city_name = addrInfo.city_name;
		req.area_id = addrInfo.area_id+"";
		req.area_name = addrInfo.area_name;
		req.address = edSnippet.getText().toString();
		req.status = cbDefault.isChecked()? "1" : "0";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, api, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(AddrEditActivity.this, "保存成功");
					setResult(RESULT_OK);
					finish();
				} else {
					T.showShort(AddrEditActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(AddrEditActivity.this);
			}
		});
	}
}
