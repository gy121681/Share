package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.CardInfo;

public class ClubCardAdapter extends CommonAdapter<CardInfo> {

	public ClubCardAdapter(Context context, List<CardInfo> data) {
		super(context, data, R.layout.item_club_card);
	}

	@Override
	public void conver(ViewHolder holder, CardInfo item, int position) {
		holder.setText(R.id.tvCardLevelDesc, item.level_desc);
		holder.setText(R.id.tvCardShopName, item.shop_name+"会员卡");
		holder.setText(R.id.tvCardNo, "NO."+item.card_no);
		holder.setText(R.id.tvCardMoney, "余额："+item.money);
	}
}
