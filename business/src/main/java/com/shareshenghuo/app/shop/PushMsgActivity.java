package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
import com.shareshenghuo.app.shop.network.bean.UserLevelInfo;
import com.shareshenghuo.app.shop.network.request.PushMsgRequest;
import com.shareshenghuo.app.shop.network.request.ShopRequest;
import com.shareshenghuo.app.shop.network.request.UpdateShopRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.network.response.UserLevelResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;
import com.shareshenghuo.app.shop.widget.dialog.DateTimeWindow;
import com.shareshenghuo.app.shop.widget.dialog.UserLevelWindow;
import com.shareshenghuo.app.shop.widget.dialog.DateTimeWindow.PickTimeCallback;
import com.shareshenghuo.app.shop.widget.dialog.UserLevelWindow.SelectLevelCallback;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class PushMsgActivity extends BaseTopActivity
	implements OnClickListener, PickTimeCallback, SelectLevelCallback {
	
	private TextView tvTime;
	private TextView tvLevel;
	private EditText edContent;
	
	private DateTimeWindow window;
	
	private UserLevelWindow levelWindow;
	private List<UserLevelInfo> levelList;
	private int levelOrder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_push_msg);
		init();
	}
	
	public void init() {
		initTopBar("发布推送消息");
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("提交");
		btnTopRight1.setOnClickListener(this);
		
		tvTime = getView(R.id.tvPushTime);
		tvLevel = getView(R.id.tvPushLevel);
		edContent = getView(R.id.edPushContent);
		
		getView(R.id.llPushTime).setOnClickListener(this);
		getView(R.id.llPushLevel).setOnClickListener(this);
		
		window = new DateTimeWindow(this);
		window.setPickTimeCallback(this);
		
		levelList = new ArrayList<UserLevelInfo>();
		UserLevelInfo info = new UserLevelInfo();
		info.level_order = 0;
		info.level_name = "全部";
		levelList.add(0, info);
		
		levelWindow = new UserLevelWindow(this, levelList);
		levelWindow.setSelectLevelCallback(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnTopRight1:
			push();
			break;
			
		case R.id.llPushTime:
			window.show();
			break;
			
		case R.id.llPushLevel:
			if(levelList.size() > 1)
				showLevelDlg();
			else
				loadLevelList();
			break;
		}
	}
	
	public void loadLevelList() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		ShopRequest req = new ShopRequest();
		req.shop_id = UserInfoManager.getUserInfo(this).shop_id+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_USER_LEVEL, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				UserLevelResponse bean = new Gson().fromJson(resp.result, UserLevelResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					levelList.addAll(bean.data);
					showLevelDlg();
				} else {
					T.showShort(PushMsgActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(PushMsgActivity.this);
			}
		});
	}
	
	public void showLevelDlg() {
		levelWindow.showAsDropDown(tvLevel);
	}
	
	public void push() {
		if(ViewUtil.checkEditEmpty(edContent, "请输入推送内容"))
			return;
		String time = tvTime.getText().toString();
		if(TextUtils.isEmpty(time)) {
			T.showShort(this, "请设置推送时间");
			return;
		}
		
		ProgressDialogUtil.showProgressDlg(this, "提交中");
		PushMsgRequest req = new PushMsgRequest();
		req.city_id = UserInfoManager.getUserInfo(this).city_id+"";
		req.shop_id = UserInfoManager.getUserInfo(this).shop_id+"";
		req.push_content = edContent.getText().toString();
		req.start_time = time;
		req.level = levelOrder+"";
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_PUSH_MSG, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(PushMsgActivity.this, "提交成功");
					finish();
				} else {
					T.showShort(PushMsgActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(PushMsgActivity.this);
			}
		});
	}

	@Override
	public void timePicked(String dateTime) {
		tvTime.setText(dateTime);
	}

	@Override
	public void selectLevel(int postion) {
		levelOrder = postion;
		tvLevel.setText(levelList.get(postion).level_name);
	}
}
