package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.ProdInfo;

public class PickProdAdapter extends CommonAdapter<ProdInfo> {

	public PickProdAdapter(Context context, List<ProdInfo> data) {
		super(context, data, R.layout.item_pick_prod);
	}

	@Override
	public void conver(ViewHolder holder, final ProdInfo item, int position) {
		CheckBox cb = holder.getView(R.id.checkBox);
		cb.setText(item.product_name);
		cb.setChecked(item.isChecked);
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean b) {
				item.isChecked = b;
			}
		});
	}

	public String getSelectedName() {
		StringBuilder sb = new StringBuilder();
		for(ProdInfo item : mData) {
			if(item.isChecked)
				sb.append(item.product_name).append(",");
		}
		return sb.toString();
	}
}
