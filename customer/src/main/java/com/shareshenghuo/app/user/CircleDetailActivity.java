package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.MemberGridAdapter;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.CircleInfo;
import com.shareshenghuo.app.user.network.request.CircleDetailRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.network.response.CircleDetailResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.MapLayout;
import com.shareshenghuo.app.user.widget.MyGridView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;

public class CircleDetailActivity extends BaseTopActivity implements OnClickListener {
	
	private static final int REQ_EDIT_CIRCLE = 0x100;
	
	private MyGridView gvMember;
	private Button btnJoin;
	private MapLayout llmap;
	private int groupId;
	private CircleInfo circleInfo;
	private ScrollView scroll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_circle_detail);
		init();
		initView();
		loadData();
	}
	
	public void init() {
		groupId = getIntent().getIntExtra("id", 0);
	}
	
	public void initView() {
		initTopBar("圈子详情");
		gvMember = getView(R.id.gvGroupMember);
		btnJoin = getView(R.id.btnGroupJoin);
		scroll = getView(R.id.scroll);
		llmap = getView(R.id.llmap);
		llmap.setScrollView(scroll);
		btnJoin.setOnClickListener(this);
	}
	
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		CircleDetailRequest req = new CircleDetailRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";;
		req.group_id = groupId+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_CIRCLE_DETAIL, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(CircleDetailActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				CircleDetailResponse bean = new Gson().fromJson(resp.result, CircleDetailResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					updateView(bean.data);
				} else {
					T.showShort(CircleDetailActivity.this, bean.result_desc);
				}
			}
		});
	}

	protected void updateView(CircleInfo data) {
		circleInfo = data;
		setImageByURL(R.id.ivGroupPic, data.im_gourp_photo);
		setText(R.id.tvGroupName, data.group_name);
		setText(R.id.tvGroupID, data.id+"");
		setText(R.id.tvGroupDesc, data.introduction);
		if(data.group_user_list!=null && data.group_user_list.size()>0) {
			setText(R.id.tvGroupMemberCount, data.group_user_list.size()+"人");
			gvMember.setAdapter(new MemberGridAdapter(this, data.group_user_list));
		}
		
		btnJoin.setEnabled(true);
		btnJoin.setText(data.is_join==1? "退出圈子" : "加入圈子");
		
		if(data.owner_id == UserInfoManager.getUserId(this)) {
			// 我创建的
			btnTopRight2.setBackgroundResource(R.drawable.icon_91);
			btnTopRight2.setVisibility(View.VISIBLE);
			btnTopRight2.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnTopRight2:
			Intent it = new Intent(this, CircleEditActivity.class);
			it.putExtra("data", circleInfo);
			startActivityForResult(it, REQ_EDIT_CIRCLE);
			break;
		
		case R.id.btnGroupJoin:
			if(circleInfo.is_join == 1) {
				//已加入
				new AlertDialog.Builder(this).setMessage("确定退出圈子吗？")
				.setNegativeButton("取消", null)
				.setPositiveButton("退出", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						joinOrQuit(false);
					}
				}).show();
			} else {
				joinOrQuit(true);
			}
			break;
		}
	}
	
	/**
	 * 加入或退出圈子
	 */
	public void joinOrQuit(final boolean join) {
		ProgressDialogUtil.showProgressDlg(this, "");
		CircleDetailRequest req = new CircleDetailRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.group_id = groupId+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String api = join? Api.URL_CIRCLE_JOIN : Api.URL_CIRCLE_QUIT;
		new HttpUtils().send(HttpMethod.POST, api, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				T.showShort(CircleDetailActivity.this, bean.result_desc);
				if(Api.SUCCEED == bean.result_code) {
					loadData();
					
					if(!join) {
						setResult(RESULT_OK);
					} else {
						setResult(RESULT_CANCELED);
					}
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(CircleDetailActivity.this);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		if(reqCode==REQ_EDIT_CIRCLE && resCode==RESULT_OK)
			loadData();
	}
}
