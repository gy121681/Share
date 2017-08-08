package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.WithdrawLogInfo;
import com.shareshenghuo.app.user.util.DateUtil;

public class WithdrawLogAdapter extends CommonAdapter<WithdrawLogInfo> {
	
	private String[] status = {"未处理", "已处理", "处理失败"};

	public WithdrawLogAdapter(Context context, List<WithdrawLogInfo> data) {
		super(context, data, R.layout.item_wallet_log);
	}

	@Override
	public void conver(ViewHolder holder, WithdrawLogInfo item, int position) {
		holder.setText(R.id.tvWalletLogDesc, "提现");
		holder.setText(R.id.tvWalletLogDate, DateUtil.getTime(item.update_time, 0));
		holder.setText(R.id.tvWalletLogFee, item.fee+"");
		holder.setText(R.id.tvWalletLogStatus, status[item.status]);
	}
}
