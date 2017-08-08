package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.ProdInfo;

public class HomeSpreeAdapter extends CommonAdapter<ProdInfo> {

	public HomeSpreeAdapter(Context context, List<ProdInfo> data) {
		super(context, data, R.layout.item_home_prod_spree);
	}

	@Override
	public void conver(ViewHolder holder, ProdInfo item, int position) {
		TextView tvOldPrice = holder.getView(R.id.tvHomeSpreeItemOldPrice);
		tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
		tvOldPrice.setText("原价："+item.default_old_price);
		holder.setText(R.id.tvHomeSpreeItemPrice, "¥"+item.default_new_price);
		holder.setImageByURL(R.id.ivHomeSpreeItemPic, item.thum_photo);
	}
}
