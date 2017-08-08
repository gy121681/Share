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

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.ShopDetailActivity;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.FavorityShopInfo;
import com.shareshenghuo.app.user.network.request.CollectRequest;
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

/**
 * @author hang
 * 收藏的商家
 */
public class FavorityShopAdapter extends CommonAdapter<FavorityShopInfo> {

	public FavorityShopAdapter(Context context, List<FavorityShopInfo> data) {
		super(context, data, R.layout.item_favority_shop);
	}

	@Override
	public void conver(ViewHolder holder, final FavorityShopInfo item, int position) {
		holder.setImageByURL(R.id.ivShopLogo, item.logo);
		holder.setText(R.id.tvShopName, item.shop_name);
		holder.setText(R.id.tvShopCollectCount, item.collect_count+"人收藏");
		
		holder.getConvertView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent it = new Intent(mContext, ShopDetailActivity.class);
				it.putExtra("shopId", item.shop_id);
				mContext.startActivity(new Intent(it));
			}
		});
		
		holder.getView(R.id.btnShopCollect).setOnClickListener(new OnClickListener() {
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
	
	public void cancelCollect(final FavorityShopInfo item) {
		ProgressDialogUtil.showProgressDlg(mContext, "");
		CollectRequest req = new CollectRequest();
		req.collect_id = item.shop_id+"";
		req.user_id = UserInfoManager.getUserId(mContext)+"";
		req.collect_type = "1";
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
