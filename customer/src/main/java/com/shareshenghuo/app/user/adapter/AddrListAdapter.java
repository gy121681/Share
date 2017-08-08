package com.shareshenghuo.app.user.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import com.shareshenghuo.app.user.AddrEditActivity;
import com.shareshenghuo.app.user.AddrManageActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.AddrInfo;
import com.shareshenghuo.app.user.network.request.AddrDelRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class AddrListAdapter extends CommonAdapter<AddrInfo> {
	
	private boolean click;

	public AddrListAdapter(Context context, List<AddrInfo> data, boolean click) {
		super(context, data, R.layout.item_addr);
		this.click = click;
	}

	@Override
	public void conver(ViewHolder holder, final AddrInfo item, int position) {
		holder.setText(R.id.tvAddrUserName, item.real_name);
		holder.setText(R.id.tvAddrMobile, item.mobile);
		holder.setText(R.id.tvAddrSnippet, item.city_name+item.area_name+item.address);
		((CheckBox) holder.getView(R.id.cbAddrDefault)).setChecked(item.status==1);
		
		holder.getConvertView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(click) {
					Intent data = new Intent();
					data.putExtra("addrInfo", item);
					((Activity)mContext).setResult(Activity.RESULT_OK, data);
					((Activity)mContext).finish();
				}
			}
		});
		
		holder.getView(R.id.ivAddrDelete).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(mContext).setMessage("确定要删除该地址吗？")
					.setNegativeButton("取消", null)
					.setPositiveButton("删除", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							delete(item);
						}
					})
					.show();
			}
		});
		
		holder.getView(R.id.ivAddrEdit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent it = new Intent(mContext, AddrEditActivity.class);
				it.putExtra("addrInfo", item);
				((Activity)mContext).startActivityForResult(it, AddrManageActivity.REQ_EDIT_ADDR);
			}
		});
	}
	
	public void delete(final AddrInfo item) {
		ProgressDialogUtil.showProgressDlg(mContext, "删除中");
		AddrDelRequest req = new AddrDelRequest();
		req.address_id = item.id+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_ADDR_DEL, params, new RequestCallBack<String>() {
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
