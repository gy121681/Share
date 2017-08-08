package com.shareshenghuo.app.shop.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.ChatActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.app.Constant;
import com.shareshenghuo.app.shop.network.bean.ContactInfo;
import com.shareshenghuo.app.shop.network.request.DelContactRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;

public class ContactListAdapter extends CommonAdapter<ContactInfo> {

	public ContactListAdapter(Context context, List<ContactInfo> data) {
		super(context, data, R.layout.item_contact);
	}

	@Override
	public void conver(ViewHolder holder, final ContactInfo item, int position) {
		holder.setImageByURL(R.id.ivContactAvatar, item.shop_photo);
		holder.setText(R.id.tvContactName, item.shop_name);
		
		holder.getView(R.id.llContactDetail).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 进入聊天页面
	            Intent intent = new Intent(mContext, ChatActivity.class);
	            intent.putExtra("title", "用户咨询");
	            intent.putExtra(Constant.EXTRA_USER_ID, "c"+item.user_id);
	            mContext.startActivity(intent);
			}
		});
		
		holder.getView(R.id.btnContactItemDel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(mContext).setMessage("确定删除该联系人？")
					.setPositiveButton("取消", null)
					.setNegativeButton("删除", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							delete(item);
						}
					}).show();
			}
		});
	}
	
	public void delete(final ContactInfo item) {
		ProgressDialogUtil.showProgressDlg(mContext, "删除中");
		DelContactRequest req = new DelContactRequest();
		req.im_id = item.id+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_DEL_CONTACT, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(mContext);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(mContext, "删除成功");
					remove(item);
					notifyDataSetChanged();
				} else {
					T.showShort(mContext, bean.result_desc);
				}
			}
		});
	}
}
