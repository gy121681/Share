package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.network.request.UpdateUserInfoRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.ViewUtil;

public class ModifyUserInfoActivity extends BaseTopActivity {
	
	public static final int MODIFY_NICKNAME = 0;
	public static final int MODIFY_REALNAME = 1;
	public static final int MODIFY_CARDNO = 2;
	
	private EditText edContent;
	
	private int modifyType;	// 0 昵称  1 真名  2 身份证
	
	private UserInfo userInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_user_info);
		init();
	}
	
	public void init() {
		modifyType = getIntent().getIntExtra("modifyType", 0);
		userInfo = UserInfoManager.getUserInfo(this);
		
		edContent = getView(R.id.edContent);
		
		switch(modifyType) {
		case MODIFY_NICKNAME:
			initTopBar("修改昵称");
			edContent.setText(userInfo.nick_name);
			break;
			
		case MODIFY_REALNAME:
			initTopBar("修改真实姓名");
			edContent.setText(userInfo.real_name);
			break;
			
		case MODIFY_CARDNO:
			initTopBar("修改身份证");
			edContent.setText(userInfo.person_no);
			break;
		}
		
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("保存");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(ViewUtil.checkEditEmpty(edContent, "请输入内容"))
					return;
				updateUserInfo(edContent.getText().toString());
			}
		});
	}
	
	public void updateUserInfo(final String value) {
		ProgressDialogUtil.showProgressDlg(this, "修改中");
		UpdateUserInfoRequest req = new UpdateUserInfoRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		switch(modifyType) {
		case MODIFY_NICKNAME:
			req.nick_name = value;
			break;
			
		case MODIFY_REALNAME:
			req.real_name = value;
			break;
			
		case MODIFY_CARDNO:
			req.person_no = value;
			break;
		}
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPDATE_USER, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				if(resp.statusCode==200 && resp.result!=null) {
					BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
					T.showShort(ModifyUserInfoActivity.this, bean.result_desc);
					if(Api.SUCCEED == bean.result_code) {
						T.showShort(ModifyUserInfoActivity.this, "修改成功");
						switch(modifyType) {
						case MODIFY_NICKNAME:
							userInfo.nick_name = value;
							break;
							
						case MODIFY_REALNAME:
							userInfo.real_name = value;
							break;
							
						case MODIFY_CARDNO:
							userInfo.person_no = value;
							break;
						}
						UserInfoManager.saveUserInfo(ModifyUserInfoActivity.this, userInfo);
						finish();
					} else {
						T.showShort(ModifyUserInfoActivity.this, bean.result_desc);
					}
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ModifyUserInfoActivity.this);
			}
		});
	}
}
