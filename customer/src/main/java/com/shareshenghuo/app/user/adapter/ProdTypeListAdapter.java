package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;
import android.widget.CheckBox;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.ProdTypeInfo;

public class ProdTypeListAdapter extends CommonAdapter<ProdTypeInfo> {
	
	public int selectedIndex = -1;

	public ProdTypeListAdapter(Context context, List<ProdTypeInfo> data) {
		super(context, data, R.layout.item_textview);
	}

	@Override
	public void conver(ViewHolder holder, ProdTypeInfo item, int position) {
		CheckBox cb = holder.getView(R.id.textView);
		cb.setText(item.name);
		cb.setChecked(selectedIndex == position);
	}
}
