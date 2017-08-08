package com.shareshenghuo.app.user.fragment;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.easeui.EaseConstant;
import com.easemob.easeui.ui.EaseChatFragment;
import com.easemob.exceptions.EaseMobException;
import com.shareshenghuo.app.user.CircleDetailActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.CircleInfo;
import com.shareshenghuo.app.user.network.request.CircleFromImRequest;
import com.shareshenghuo.app.user.network.response.CircleListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ChatFragment extends EaseChatFragment {
	
	private static final int REQ_CIRCLE_DETAIL = 0x100;
	
	private Activity activity;
	
	@Override
	protected void setUpView() {
		activity = getActivity();
		
		super.setUpView();
		if(toChatUsername.startsWith("s") || toChatUsername.startsWith("c")) {
			titleBar.setTitle("商家咨询");
			chatType = EaseConstant.CHATTYPE_SINGLE;
		} else {
			titleBar.setTitle("圈子");
			chatType = EaseConstant.CHATTYPE_CHATROOM;
			getCircleDetail();
			
		}
		titleBar.rightImage.setVisibility(View.GONE);
		titleBar.setBackgroundColor(getResources().getColor(R.color.bg_top_bar));
	}
	
	@Override
	protected void registerExtendMenuItem() {
		for(int i = 0; i < 2; i++){
            inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
        }
	}
	
	/**
	 * 通过groupId获取圈子详情
	 */
	public void getCircleDetail() {
		ProgressDialogUtil.showProgressDlg(getActivity(), "");
		CircleFromImRequest req = new CircleFromImRequest();
		req.im_id_list = toChatUsername;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_CIRCLE_FROM_IM, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				CircleListResponse bean = new Gson().fromJson(resp.result, CircleListResponse.class);
				if(Api.SUCCEED == bean.result_code && bean.data.size()>0) {
					final CircleInfo item = bean.data.get(0);
					EMChatManager.getInstance().getConversation(toChatUsername).setExtField(new Gson().toJson(item));
					titleBar.rightText.setText("圈子详情");
					titleBar.setTitle(item.group_name);
					titleBar.setRightLayoutClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent it = new Intent(activity, CircleDetailActivity.class);
							it.putExtra("id", item.id);
							startActivityForResult(it, REQ_CIRCLE_DETAIL);
						}
					});
				}
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK && requestCode==REQ_CIRCLE_DETAIL) {
			try {
				EMGroupManager.getInstance().exitFromGroup(toChatUsername);
			} catch (EaseMobException e) {
				e.printStackTrace();
			}
			activity.finish();
		}
	}
}
