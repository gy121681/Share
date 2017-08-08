package com.shareshenghuo.app.user.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.CouponInfo;
import com.shareshenghuo.app.user.network.request.ReceiveCouponRequest;
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

public class CouponListAdapter extends CommonAdapter<CouponInfo> {
	
	private String[] status;
	
	public boolean click;
	
	public CouponListAdapter(Context context, List<CouponInfo> data) {
		super(context, data, R.layout.item_coupon);
		status = context.getResources().getStringArray(R.array.coupon_statuss);
	}

	@Override
	public void conver(ViewHolder holder, final CouponInfo item, int position) {
		holder.setText(R.id.tvCouponName, item.coupon_name);
		holder.setText(R.id.tvCouponEffectiveTime, DateUtil.getTime(item.effective_start_time,2)
				+"至"+ DateUtil.getTime(item.effective_end_time,2));
		holder.setText(R.id.tvCouponDesc, "满"+item.lowest_limit+"元抵用"+item.coupon_fee+"元");
		
		holder.getConvertView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(click) {
					Intent data = new Intent();
					data.putExtra("couponInfo", item);
					((Activity)mContext).setResult(Activity.RESULT_OK, data);
					((Activity)mContext).finish();
				}
			}
		});
		
		LinearLayout llBg = holder.getView(R.id.llCouponBg);
		if(item.coupon_status==3 || item.coupon_status==4) {
			//已使用、已过期
			llBg.setBackgroundResource(R.drawable.bg_coupon_unenable);
		} else {
			llBg.setBackgroundResource(R.drawable.bg_coupon);
		}
		
		final TextView btnGet = holder.getView(R.id.btnCouponGet);
		if(item.is_have == 0) {
			btnGet.setText("立即领取");
		} else {
			btnGet.setText(status[item.coupon_status]);
		}
		btnGet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if("立即领取".equals(btnGet.getText().toString())) {
					receiveCoupon(item);
				}
			}
		});
	}
	
	public void receiveCoupon(final CouponInfo item) {
		ProgressDialogUtil.showProgressDlg(mContext, "领取优惠券");
		ReceiveCouponRequest req = new ReceiveCouponRequest();
		req.user_id = UserInfoManager.getUserId(mContext)+"";
		req.coupon_id = item.id+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_RECEIVE_COUPON, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(mContext, "领取成功");
					item.is_have = 1;
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
