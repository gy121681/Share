package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.shareshenghuo.app.user.ActivDetailActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.ExGratiaInfo;

/**
 * 全城特惠
 * @author hang
 */
public class ExGratiaAdapter extends CommonAdapter<ExGratiaInfo> {

	public ExGratiaAdapter(Context context, List<ExGratiaInfo> data) {
		super(context, data, R.layout.item_ex_gratia);
	}

	@Override
	public void conver(ViewHolder holder, ExGratiaInfo item, int position) {
		holder.getConvertView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mContext.startActivity(new Intent(mContext, ActivDetailActivity.class));
			}
		});
	}

}
