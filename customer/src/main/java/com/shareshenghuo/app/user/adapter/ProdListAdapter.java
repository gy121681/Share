package com.shareshenghuo.app.user.adapter;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.shareshenghuo.app.user.CommodityInfosActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.ProdInfo;
import com.shareshenghuo.app.user.network.request.AddToCartRequest;
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

public class ProdListAdapter extends CommonAdapter<ProdInfo> {

	public ProdListAdapter(Context context, List<ProdInfo> data) {
		super(context, data, R.layout.item_prod);
	}

	@Override
	public void conver(ViewHolder holder, final ProdInfo item, int position) {
		holder.setImageByURL(R.id.ivHomeHotItemPic, item.photo);
		holder.setText(R.id.tvHomeHotItemTitle, item.name);
		holder.setText(R.id.tvHomeHotItemDesc, item.description);
		
		DecimalFormat df = new DecimalFormat("###.00");  
		String price = "";
		try {
			price = df.format(Double.parseDouble(item.price)/100);
		} catch (Exception e) {
			// TODO: handle exception
			price = "0.00";
		}
		holder.setText(R.id.tvHomeHotItemPrice, price+"元");//+item.unit);
		
		holder.getConvertView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				Intent it = new Intent(mContext, ProdDetailActivity.class);
//				it.putExtra("prodInfo", item);
//				mContext.startActivity(it);
				Intent it = new Intent(mContext, CommodityInfosActivity.class);
				it.putExtra("prodInfo", item);
				mContext.startActivity(it);
			}
		});
		
		holder.getView(R.id.ivProdAdd).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addToCart(item);
			}
		});
	}
	
	public void addToCart(ProdInfo item) {
		ProgressDialogUtil.showProgressDlg(mContext, "");
		AddToCartRequest req = new AddToCartRequest();
		req.user_id = UserInfoManager.getUserId(mContext)+"";
		req.product_id = item.id+"";
		req.shop_id = item.shop_id+"";
		req.product_count = "1";
		req.product_format_id = item.default_format_id+"";
		req.child_format_id = item.default_child_format_id+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_ADD_TO_CART, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(mContext, "添加购物车成功");
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
