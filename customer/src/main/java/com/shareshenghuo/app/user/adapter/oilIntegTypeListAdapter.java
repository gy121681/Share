package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.OilintegralBean;
import com.shareshenghuo.app.user.util.DateUtil;
import com.shareshenghuo.app.user.util.Util;

public class oilIntegTypeListAdapter extends CommonAdapter<OilintegralBean> {
	
	public int selectedIndex = -1;

	public oilIntegTypeListAdapter(Context context, List<OilintegralBean> data) {
		super(context, data, R.layout.oilinteg_item);
	}

	@Override
	public void conver(ViewHolder holder, OilintegralBean item, int position) {
		
		if(item.opers_type.equals("02")){
			holder.setText(R.id.tv2,"支付积分");
		}else if(item.opers_type.equals("03")){
			holder.setText(R.id.tv2,"奖励积分");
		}else if(item.opers_type.equals("04")){
			holder.setText(R.id.tv2,"销售积分");
		}else if(item.opers_type.equals("05")){
			holder.setText(R.id.tv2,"代理积分");
		}
		
		holder.setText(R.id.tv3,  "+"+Util.getnum(item.integral_num,false));
		holder.setText(R.id.tv1,DateUtil.getStrTime(item.collection_date)+"到账");
	}
}
