package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.ExchangeListBean;
import com.shareshenghuo.app.shop.network.bean.IntegralBean;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.util.Util;

public class ExchangeListAdapter extends CommonAdapter<ExchangeListBean> {

	public ExchangeListAdapter(Context context, List<ExchangeListBean> data) {
		super(context, data, R.layout.exchange_list_item);
	}

	@Override
	public void conver(ViewHolder holder, final ExchangeListBean item, int position) {

		holder.setText(R.id.tv_amount, " +"+Util.getnum(item.wd_amt,false));
		holder.setText(R.id.time, DateUtil.getTime(item.create_time,0));
		if(item.wd_sts.equals("0")){
			holder.setText(R.id.type, "待打款");
		}else if(item.wd_sts.equals("1")){
			holder.setText(R.id.type, "银行处理中");
		}else if(item.wd_sts.equals("2")){
			holder.setText(R.id.type, "打款失败");
		}else if(item.wd_sts.equals("3")){
			holder.setText(R.id.type, "已退款");
		}else if(item.wd_sts.equals("4")){
			holder.setText(R.id.type, "打款成功");
		}
	}
}