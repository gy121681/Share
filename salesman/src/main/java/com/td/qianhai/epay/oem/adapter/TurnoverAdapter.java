package com.td.qianhai.epay.oem.adapter;

import java.util.List;

import android.content.Context;

import com.amap.api.services.core.PoiItem;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.beans.TurnoverBean;
import com.td.qianhai.epay.oem.mail.utils.NumUtil;

public class TurnoverAdapter extends CommonAdapter<TurnoverBean> {

	public TurnoverAdapter(Context context, List<TurnoverBean> data) {
		super(context, data, R.layout.turnover_item);
	}

	@Override
	public void conver(ViewHolder holder, TurnoverBean item, int position) {
//		holder.setImageResource(R.id.ivPoiIcon, position==0? R.drawable.icon_82:R.drawable.icon_152);
		holder.setText(R.id.total_amount, "¥"+NumUtil.getfotmatnum(item.total_trade_amount, true, 1));
		holder.setText(R.id.trade_amount_type3, "¥"+NumUtil.getfotmatnum(item.trade_amount_type3, true, 1));
		holder.setText(R.id.trade_amount_type2, "¥"+NumUtil.getfotmatnum(item.trade_amount_type2, true, 1));
		holder.setText(R.id.trade_amount_type1, "¥"+NumUtil.getfotmatnum(item.trade_amount_type1, true, 1));
		
		holder.setText(R.id.tv_date, NumUtil.getStrTime(item.trade_date) );
	}
}
