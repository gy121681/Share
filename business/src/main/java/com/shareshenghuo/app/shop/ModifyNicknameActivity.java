package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.UserInfo;
import com.shareshenghuo.app.shop.network.request.UpdateShopRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ModifyNicknameActivity extends BaseTopActivity {
	
	private EditText edNick;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_nickname);
		init();
	}
	
	public void init() {
		

		initTopBar("修改昵称");
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("保存");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				save();
			}
		});
		
		edNick = getView(R.id.edNickname);
		ViewUtil.setEditWithClearButton(edNick, R.drawable.share_b_public_button_delete);
		UserInfo user = UserInfoManager.getUserInfo(ModifyNicknameActivity.this);
		edNick.setText(user.nick_name);
	}
	
	public void save() {
		if(ViewUtil.checkEditEmpty(edNick, "请输入昵称"))
			return;
		
		ProgressDialogUtil.showProgressDlg(this, "保存中");
		UpdateShopRequest req = new UpdateShopRequest();
		req.admin_id = UserInfoManager.getUserId(this)+"";
		req.nick_name = edNick.getText().toString();
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPDATE_SHOP, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ModifyNicknameActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(ModifyNicknameActivity.this, "保存成功");
					finish();
				} else {
					T.showShort(ModifyNicknameActivity.this, bean.result_desc);
				}
			}
		});
	}
}
