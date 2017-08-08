package com.shareshenghuo.app.user.widget.dialog;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.ShopListActivity;
import com.shareshenghuo.app.user.adapter.CategoryGridAdapter;
import com.shareshenghuo.app.user.network.bean.CategoryInfo;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class CategoryWindow extends CommonDialog implements OnItemClickListener {
	
	private CategoryInfo categoryInfo;

	public CategoryWindow(Context context, CategoryInfo categoryInfo) {
		super(context, R.layout.dlg_category, 300, 220);
		this.categoryInfo = categoryInfo;
	}

	@Override
	public void initDlgView() {
		((TextView)getView(R.id.tvCategoryTitle)).setText(categoryInfo.type_name);
		GridView gvCategory = getView(R.id.gvCategory);
		gvCategory.setAdapter(new CategoryGridAdapter(context, categoryInfo.child_shop_type_list));
		gvCategory.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
		CategoryInfo item = ((CategoryGridAdapter)parent.getAdapter()).getItem(position);
		Intent it = new Intent(context, ShopListActivity.class);
		it.putExtra("shop_type_id", item.id);
		context.startActivity(it);
		odismiss();
	}

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		
	}
}
