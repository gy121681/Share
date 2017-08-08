package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.easemob.easeui.EaseConstant;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.CircleGridAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.CircleInfo;
import com.shareshenghuo.app.user.network.request.ArticleListRequest;
import com.shareshenghuo.app.user.network.request.CircleDetailRequest;
import com.shareshenghuo.app.user.network.response.CircleListResponse;
import com.shareshenghuo.app.user.network.response.MyCircleResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.MyGridView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author hang
 * 圈子
 */
public class CircleMyActivity extends BaseTopActivity implements OnClickListener, OnItemClickListener {
	
	private static final int REQ_CREATE_CIRCLE = 0x100;
	
	private MyGridView gvCreatedGroup;
	private MyGridView gvJoinedGroup;
	private MyGridView gvHobbyGroup;
	
	private int circleIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_my);
		init();
		getMyCircleList();
		getHobbyCircleList();
	}
	
	public void init() {
		initTopBar("圈子");
		gvCreatedGroup = getView(R.id.gvCreatedGroup);
		gvJoinedGroup = getView(R.id.gvJoinedGroup);
		gvHobbyGroup = getView(R.id.gvHobbyGroup);
		
		gvCreatedGroup.setOnItemClickListener(this);
		gvJoinedGroup.setOnItemClickListener(this);
		gvHobbyGroup.setOnItemClickListener(this);
		
		getView(R.id.ivCreateGroup).setOnClickListener(this);
		getView(R.id.tvSwitchCircle).setOnClickListener(this);
		
		circleIndex = 1;
	}
	
	/**
	 * 获取我的圈子
	 */
	public void getMyCircleList() {
		CircleDetailRequest req = new CircleDetailRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_CIRCLR_MY, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				MyCircleResponse bean = new Gson().fromJson(resp.result, MyCircleResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					gvJoinedGroup.setAdapter(new CircleGridAdapter(CircleMyActivity.this, bean.data.join_list));
					gvCreatedGroup.setAdapter(new CircleGridAdapter(CircleMyActivity.this, bean.data.create_list));
				} else {
					T.showShort(CircleMyActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				T.showNetworkError(CircleMyActivity.this);
			}
		});
	}
	
	/**
	 * 获取兴趣圈
	 */
	public void getHobbyCircleList() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		ArticleListRequest req = new ArticleListRequest();
		req.page_no = circleIndex+"";
		req.page_size = "4";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_CIRCLE_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				CircleListResponse bean = new Gson().fromJson(resp.result, CircleListResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					gvHobbyGroup.setAdapter(new CircleGridAdapter(CircleMyActivity.this, bean.data));
				} else {
					T.showShort(CircleMyActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(CircleMyActivity.this);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.ivCreateGroup:
			startActivityForResult(new Intent(this, CircleEditActivity.class), REQ_CREATE_CIRCLE);
			break;
			
		case R.id.tvSwitchCircle:
			circleIndex++;
			getHobbyCircleList();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
		if(adapterView==gvCreatedGroup || adapterView==gvJoinedGroup) {
			//进入群聊
			CircleInfo item = (CircleInfo) adapterView.getItemAtPosition(position);
			Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(EaseConstant.EXTRA_USER_ID, item.im_gourp_id);
            startActivity(intent);
		} else if(adapterView==gvHobbyGroup) {
			CircleInfo item = (CircleInfo) adapterView.getItemAtPosition(position);
			Intent it = new Intent(this, CircleDetailActivity.class);
			it.putExtra("id", item.id);
			startActivity(it);
		}
	}
	
	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		if(reqCode==REQ_CREATE_CIRCLE && resCode==RESULT_OK)
			getMyCircleList();
	}
}
