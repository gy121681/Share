package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.ImageLoadManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.CircleInfo;
import com.shareshenghuo.app.user.network.request.EditCircleRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.ViewUtil;
import com.shareshenghuo.app.user.widget.CircleImageView;
import com.shareshenghuo.app.user.widget.dialog.PickPhotoWindow;
import com.shareshenghuo.app.user.widget.dialog.PickPhotoWindow.PhotoUploadCallback;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class CircleEditActivity extends BaseTopActivity implements OnClickListener, PhotoUploadCallback {
	
	private CircleImageView civAvatar;
	private EditText edName;
	private EditText edDesc;
	
	private CircleInfo circleInfo;
	
	private PickPhotoWindow photoWindow;
	
	private String avatarUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_edit);
		initData();
		initView();
	}
	
	public void initData() {
		circleInfo = (CircleInfo) getIntent().getSerializableExtra("data");
	}
	
	public void initView() {
		if(circleInfo == null) {
			initTopBar("新建圈子");
		} else {
			initTopBar("编辑圈子");
		}
		civAvatar = getView(R.id.civGroupPic);
		edName = getView(R.id.edGroupName);
		edDesc = getView(R.id.edGroupDesc);
		
		civAvatar.setOnClickListener(this);
		getView(R.id.btnSubmit).setOnClickListener(this);
		
		photoWindow = new PickPhotoWindow(this);
		photoWindow.setPhotoUploadCallback(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.civGroupPic:
			photoWindow.showAtBottom();
			break;
		
		case R.id.btnSubmit:
			if(TextUtils.isEmpty(avatarUrl)) {
				T.showShort(this, "请选择头像");
				return;
			}
			if(ViewUtil.checkEditEmpty(edName, "请填写名称"))
				return;
			if(ViewUtil.checkEditEmpty(edDesc, "请填写介绍"))
				return;
			submit();
			break;
		}
	}

	@Override
	public void uploadSucceed(String fileUrl) {
		avatarUrl = fileUrl;
		ImageLoadManager.getInstance(this).displayImage(fileUrl, civAvatar);
	}
	
	public void submit() {
		ProgressDialogUtil.showProgressDlg(this, "");
		String api = "";
		EditCircleRequest req = new EditCircleRequest();
		if(circleInfo == null) {
			//新建
			req.user_id = UserInfoManager.getUserId(this)+"";
			api = Api.URL_CIRCLE_CREATE;
		} else {
			//修改
			req.group_id = circleInfo.id+"";
			api = Api.URL_CIRCLE_EDIT;
		}
		req.im_gourp_photo = avatarUrl;
		req.group_name = edName.getText().toString();
		req.introduction = edDesc.getText().toString();
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
				T.showShort(CircleEditActivity.this, bean.result_desc);
				if(Api.SUCCEED == bean.result_code) {
					setResult(RESULT_OK);
					finish();
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(CircleEditActivity.this);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		if(resCode == RESULT_OK)
			photoWindow.onActivityResult(reqCode, resCode, data);
	}
}
