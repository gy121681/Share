package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.BalanceDetailInfo;

/**
 * @author hang
 * 余额明细
 */
public class BalanceDetailAdapter extends CommonAdapter<BalanceDetailInfo> {

	public BalanceDetailAdapter(Context context, List<BalanceDetailInfo> data) {
		super(context, data, R.layout.item_balance_detail);
	}

	@Override
	public void conver(ViewHolder holder, BalanceDetailInfo item, int position) {
		
	}
}
