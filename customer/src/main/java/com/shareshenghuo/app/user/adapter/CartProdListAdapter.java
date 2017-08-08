package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.CartListAdapter.Mode;
import com.shareshenghuo.app.user.network.bean.ProdInfo;
import com.shareshenghuo.app.user.util.Arith;
import com.shareshenghuo.app.user.widget.NumberPickLayout;
import com.shareshenghuo.app.user.widget.NumberPickLayout.OnNumberChangeListener;

public class CartProdListAdapter extends CommonAdapter<ProdInfo> {
	
	private int mode;
	
	private TextView tvAmount;
	
	private OnProdCheckCallback callback;

	public CartProdListAdapter(Context context, List<ProdInfo> data, int mode, TextView tvAmount, OnProdCheckCallback callback) {
		super(context, data, R.layout.item_cart_prod);
		this.mode = mode;
		this.tvAmount = tvAmount;
		this.callback = callback;
	}

	@Override
	public void conver(ViewHolder holder, final ProdInfo item, int position) {
		holder.setImageByURL(R.id.ivProdPhoto, item.thum_photo);
		holder.setText(R.id.tvProdName, item.product_name);
		holder.setText(R.id.tvProdNewPrice, "¥"+item.default_new_price);
		TextView tvOldPrice = holder.getView(R.id.tvProdOldPrice);
		tvOldPrice.setText("原价：¥"+item.default_old_price);
		tvOldPrice.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG|Paint.STRIKE_THRU_TEXT_FLAG);
		holder.setText(R.id.tvProdFormat, item.format_name);
		
		NumberPickLayout npCount = holder.getView(R.id.npProdCount);
		npCount.setMinNumber(1);
		npCount.setCurNumber(item.shop_car_count);
		npCount.setEnabled(mode == Mode.MODE_EDIT);
		npCount.setOnNumberChangeListener(new OnNumberChangeListener() {
			@Override
			public void onChanged(int curNum, boolean isAdd) {
				item.shop_car_count = curNum;
			}
		});
		
		final CheckBox cb = holder.getView(R.id.cbProdChoose);
		cb.setChecked(item.isChecked);
		cb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				item.isChecked = cb.isChecked();
				
				if(mode == Mode.MODE_NORMAL) {
					double amount = Double.parseDouble(tvAmount.getText().toString());
					if(item.isChecked)
						amount = Arith.add(amount, Arith.mul(item.default_new_price, item.shop_car_count));
					else
						amount = Arith.sub(amount, Arith.mul(item.default_new_price, item.shop_car_count));
					tvAmount.setText(amount+"");
				}
				
				if(item.isChecked && callback!=null)
					callback.onProdChecked(item);
			}
		});
	}
	
	public void setMode(int mode) {
		this.mode = mode;
		notifyDataSetChanged();
	}
	
	public interface OnProdCheckCallback {
		public void onProdChecked(ProdInfo prod);
	}
}
