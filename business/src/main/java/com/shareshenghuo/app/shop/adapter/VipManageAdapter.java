package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.NewMemberbean;
import com.shareshenghuo.app.shop.network.bean.VipInfo;
import com.shareshenghuo.app.shop.util.DateUtil;

public class VipManageAdapter extends CommonAdapter<NewMemberbean> {

	public VipManageAdapter(Context context, List<NewMemberbean> data) {
		super(context, data, R.layout.item_vip_manage);
	}

	@Override
	public void conver(ViewHolder holder, NewMemberbean item, int position) {
//		holder.setText(R.id.tvVipAccount, item.account);
		
		if(!TextUtils.isEmpty(item.account)){
			holder.setText(R.id.tvVipAccount, item.account);
		}else{
			holder.setText(R.id.tvVipAccount, "未知");
		}
		
		if(!TextUtils.isEmpty(item.real_name)){
			holder.setText(R.id.tvVipName, item.real_name);
		}else{
			if(!TextUtils.isEmpty(item.nick_name)){
				holder.setText(R.id.tvVipName, item.nick_name);
			}else{
				holder.setText(R.id.tvVipName, "未知");
			}
			
		}
		
		try {
			holder.setText(R.id.tvVipMobile,DateUtil.getTime(item.create_time, 0));
		} catch (Exception e) {
			// TODO: handle exception
		}

//		holder.setText(R.id.tvVipMobile, item.mobile);
	}
}
