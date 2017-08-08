package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.WalletRechargeInfo;
import com.shareshenghuo.app.user.util.DateUtil;

/**
 * @author hang
 * 钱包充值
 */
public class WalletRechargeLogAdapter extends CommonAdapter<WalletRechargeInfo> {
	
	public int type; // 1消费 2充值 

	public WalletRechargeLogAdapter(Context context, List<WalletRechargeInfo> data, int type) {
		super(context, data, R.layout.item_wallet_log);
		this.type = type;
	}

	@Override
	public void conver(ViewHolder holder, WalletRechargeInfo item, int position) {
		holder.setText(R.id.tvWalletLogDesc, type==1? "消费":"充值");
		holder.setText(R.id.tvWalletLogDate, DateUtil.getTime(item.update_time, 0));
		holder.setText(R.id.tvWalletLogFee, item.total_fee+"");
		holder.setText(R.id.tvWalletLogStatus, item.status==1? "成功":"审核中");
	}
}
