package com.shareshenghuo.app.user.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.shareshenghuo.app.user.ActivDetailActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.ActivInfo;
import com.shareshenghuo.app.user.network.request.CollectRequest;
import com.shareshenghuo.app.user.network.request.JoinActivRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.DateUtil;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ActivListAdapter extends CommonAdapter<ActivInfo> {
	
	public boolean canCollect;	// true 显示收藏按钮   false 隐藏收藏按钮(default)

	public ActivListAdapter(Context context, List<ActivInfo> data) {
		super(context, data, R.layout.item_activ);
	}

	@Override
	public void conver(ViewHolder holder, final ActivInfo item, int position) {
		holder.setText(R.id.tvActivName, item.active_title);
		holder.setText(R.id.tvActivDate, DateUtil.getTime(item.effective_start_time,2)
				+"至"+ DateUtil.getTime(item.effective_end_time,2));
		holder.setText(R.id.tvActivAddr, item.address);
		holder.setText(R.id.tvActivRange, item.range+"km");
		holder.setImageByURL(R.id.ivActivPic, item.thum_photo);
		
		if(canCollect) {
			holder.getView(R.id.ivActivLocIcon).setVisibility(View.GONE);
			holder.getView(R.id.tvActivRange).setVisibility(View.GONE);
		} else {
			holder.getView(R.id.ivActivLocIcon).setVisibility(View.VISIBLE);
			holder.getView(R.id.tvActivRange).setVisibility(View.VISIBLE);
		}
		
		holder.getConvertView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent it = new Intent(mContext, ActivDetailActivity.class);
				it.putExtra("activInfo", item);
				mContext.startActivity(it);
			}
		});
		
		Button btnJoin = holder.getView(R.id.btnActivJoin);
		btnJoin.setText(item.is_join==0? "我要参加":"已报名");
		btnJoin.setEnabled(item.is_join == 0);
		btnJoin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(item.is_join == 0)
					joinActiv(item);
			}
		});
		
		Button btnCollect = holder.getView(R.id.btnActivCollect);
		btnCollect.setVisibility(canCollect? View.VISIBLE : View.GONE);
		btnCollect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new AlertDialog.Builder(mContext).setMessage("是否取消关注？")
				.setNegativeButton("否", null)
				.setPositiveButton("取消关注", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						cancelCollect(item);
					}
				})
				.show();
			}
		});
	}
	
	public void joinActiv(final ActivInfo item) {
		ProgressDialogUtil.showProgressDlg(mContext, "报名中");
		JoinActivRequest req = new JoinActivRequest();
		req.active_id = item.id+"";
		req.user_id = UserInfoManager.getUserId(mContext)+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_JOIN_ACTIV, params, new RequestCallBack<String>() {
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
					T.showShort(mContext, "报名成功");
					item.is_join = 1;
					notifyDataSetChanged();
				} else {
					T.showShort(mContext, bean.result_desc);
				}
			}
		});
	}
	
	public void cancelCollect(final ActivInfo item) {
		ProgressDialogUtil.showProgressDlg(mContext, "");
		CollectRequest req = new CollectRequest();
		req.collect_id = item.active_id+"";
		req.user_id = UserInfoManager.getUserId(mContext)+"";
		req.collect_type = "2";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_COLLECT, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(mContext, "成功");
					remove(item);
					notifyDataSetChanged();
				} else {
					T.showShort(mContext, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(mContext);
			}
		});
	}
}
