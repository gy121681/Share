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
import com.shareshenghuo.app.user.adapter.AddrListAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.request.UpdateUserInfoRequest;
import com.shareshenghuo.app.user.network.response.AddrListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

/**
 * @author hang
 * 收货地址管理
 */
public class AddrManageActivity extends BaseTopActivity {
	
	public static final int REQ_EDIT_ADDR = 0x101;
	
	private ListView lvData;
	
	private boolean click;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addr_manage);
		init();
		loadData();
	}
	
	public void init() {
		initTopBar("地址管理");
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("新增");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(AddrManageActivity.this, AddrEditActivity.class), REQ_EDIT_ADDR);
			}
		});
		
		lvData = getView(R.id.lvAddr);
		
		click = getIntent().getBooleanExtra("click", false);
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "");
		UpdateUserInfoRequest req = new UpdateUserInfoRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_ADDR_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(AddrManageActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				AddrListResponse bean = new Gson().fromJson(resp.result, AddrListResponse.class);
				if(Api.SUCCEED == bean.result_code)
					lvData.setAdapter(new AddrListAdapter(AddrManageActivity.this, bean.data, click));
				else
					T.showShort(AddrManageActivity.this, bean.result_desc);
			}
		});
	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		if(reqCode==REQ_EDIT_ADDR && resCode==RESULT_OK)
			loadData();
	}
}
