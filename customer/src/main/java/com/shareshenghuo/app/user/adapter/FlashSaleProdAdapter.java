package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.shareshenghuo.app.user.ProdDetailActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.ProdInfo;

public class FlashSaleProdAdapter extends CommonAdapter<ProdInfo> {

	public FlashSaleProdAdapter(Context context, List<ProdInfo> data) {
		super(context, data, R.layout.item_flash_sale);
	}

	@Override
	public void conver(ViewHolder holder, final ProdInfo item, int position) {
		holder.setImageByURL(R.id.ivProdPic, item.product_photo);
		holder.setText(R.id.tvProdName, item.product_name);
		holder.setText(R.id.tvProdNowPrice, "¥"+item.default_new_price);
		holder.setText(R.id.tvProdSaleCount, "已抢"+item.sale_count+"件/剩"+(item.all_product_repertory-item.sale_count)+"件");
		
		TextView tvOldPrice = holder.getView(R.id.tvProdOldPrice);
		tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
		tvOldPrice.setText("原价：¥"+item.default_old_price);
		
		holder.getConvertView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				toProdDetail(item);
			}
		});
		
		holder.getView(R.id.btnProdBuy).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				toProdDetail(item);
			}
		});
	}
	
	public void toProdDetail(ProdInfo item) {
		Intent it = new Intent(mContext, ProdDetailActivity.class);
		it.putExtra("prodInfo", item);
		it.putExtra("noCart", true);
		mContext.startActivity(it);
	}
}
