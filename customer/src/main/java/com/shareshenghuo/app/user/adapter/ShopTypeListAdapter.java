package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;
import android.widget.CheckBox;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.CategoryInfo;

public class ShopTypeListAdapter extends CommonAdapter<CategoryInfo> {
	
	public int selectedIndex = -2;
	public int posotion;
	private boolean tag = false;

	public ShopTypeListAdapter(Context context, List<CategoryInfo> data) {
		super(context, data, R.layout.item_textview);
	}

	@Override
	public void conver(ViewHolder holder, CategoryInfo item, int position) {
		CheckBox cb = holder.getView(R.id.textView);
		cb.setText(item.type_name);
//		if(tag){
//			cb.setChecked(!(selectedIndex == position));
//		}else{
			cb.setChecked(selectedIndex == position);
//		}
		
//		if(posotion==position){
//			cb.setChecked(true);
//		}
	}
	
	public void setcheckd(int posotion){
		this.posotion = posotion;
		notifyDataSetChanged();
	}
}
