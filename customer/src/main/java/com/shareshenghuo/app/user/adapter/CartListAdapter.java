package com.shareshenghuo.app.user.adapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.ShopDetailActivity;
import com.shareshenghuo.app.user.adapter.CartProdListAdapter.OnProdCheckCallback;
import com.shareshenghuo.app.user.network.bean.CartInfo;
import com.shareshenghuo.app.user.network.bean.ProdInfo;
import com.shareshenghuo.app.user.network.request.UpdateCartRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.Arith;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.Utility;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class CartListAdapter extends CommonAdapter<CartInfo> {
	
	private int mode = Mode.MODE_NORMAL;
	
	private OnProdChangeCallback callback;
	
	private TextView tvAmount;
	
	public int checkedShopId = -1;

	public CartListAdapter(Context context, List<CartInfo> data, TextView tvAmount) {
		super(context, data, R.layout.item_cart);
		this.tvAmount = tvAmount;
	}

	@Override
	public void conver(ViewHolder holder, final CartInfo item, int position) {
		holder.setText(R.id.tvShopName, item.shop_name);
		holder.getView(R.id.llShopDetail).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent it = new Intent(mContext, ShopDetailActivity.class);
				it.putExtra("shopId", item.shop_id);
				mContext.startActivity(it);
			}
		});
		
		ListView lvProd = holder.getView(R.id.lvCartProd);
		lvProd.setAdapter(new CartProdListAdapter(mContext, item.product_info_list, mode, tvAmount, new OnProdCheckCallback() {
			@Override
			public void onProdChecked(ProdInfo prod) {
				if(mode == Mode.MODE_NORMAL) {
					// 判断是否选中同一商家物品
					if(checkedShopId!=-1 && checkedShopId!=prod.shop_id) {
						for(CartInfo cart : mData) {
							if(cart.shop_id == checkedShopId)
								for(ProdInfo info : cart.product_info_list)
									info.isChecked = false;
						}
						notifyDataSetChanged();
						tvAmount.setText(Arith.mul(prod.default_new_price, prod.shop_car_count)+"");
					}
					checkedShopId = prod.shop_id;
				}
			}
		}));
		Utility.setListViewHeightBasedOnChildren(lvProd);
	}
	
	public void delete() {
		UpdateCartRequest req = new UpdateCartRequest();
		for(CartInfo item : mData)
			for(ProdInfo prod : item.product_info_list)
				if(prod.isChecked)
					req.add(prod.id, 0);
		
		if(req.car_list.size() <= 0) {
			T.showShort(mContext, "请选择商品哦~");
			return;
		}
		
		updateCart(req, 0);
	}
	
	public void commitModify() {
		UpdateCartRequest req = new UpdateCartRequest();
		for(CartInfo item : mData)
			for(ProdInfo prod : item.product_info_list) {
				req.add(prod.id, prod.shop_car_count);
				prod.isChecked = false;
			}
		
		updateCart(req, 1);
	}
	
	public void updateCart(UpdateCartRequest req, final int operate) {
		ProgressDialogUtil.showProgressDlg(mContext, "");
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPDATE_CART, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					if(callback != null) {
						callback.onChanged(operate);
						notifyDataSetChanged();
					} else
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
	
	public List<ProdInfo> getCheckedProds() {
		List<ProdInfo> list = new ArrayList<ProdInfo>();
		for(CartInfo cart : mData) {
			if(cart.shop_id == checkedShopId) {
				for(ProdInfo item : cart.product_info_list)
					if(item.isChecked)
						list.add(item);
				break;
			}
		}
		return list;
	}
	
	public void setMode(int mode) {
		this.mode = mode;
		notifyDataSetChanged();
	}
	
	public int getMode() {
		return mode;
	}
	
	public static class Mode {
		public static final int MODE_NORMAL = 0;
		public static final int MODE_EDIT = 1;
	}
	
	public void setOnProdChangeCallback(OnProdChangeCallback callback) {
		this.callback = callback;
	}
	
	public interface OnProdChangeCallback {
		/**
		 * @param operate 0 删除	1 编辑数量
		 */
		public void onChanged(int operate);
	}
}
