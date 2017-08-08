package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shareshenghuo.app.shop.EkOrderPay;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.PayChannelsBean;


public class PayListAdapter extends CommonAdapter<PayChannelsBean>{
	
	private List<PayChannelsBean> data;
	private int haight;
	public Context context;
	
	public PayListAdapter(Context context, List<PayChannelsBean> data,int haight) {
		super(context, data, R.layout.paylist_layout);
		this.haight = haight;
		this.data = data;
		this.context = context;
	}

	@Override
	public void conver(ViewHolder holder, final PayChannelsBean item, int position) {
		
		final int positoins = position;
		 RelativeLayout re = (RelativeLayout)holder.getView(R.id.re);
		
        LayoutParams linearParams = (LayoutParams) re
                .getLayoutParams();
        linearParams.height = ((EkOrderPay) context).gethaight()/5;
//        linearParams.gravity = Gravity.CENTER_VERTICAL;
        re.setLayoutParams(linearParams);
        holder.getConvertView().setTag(holder);
		
		
		holder.setText(R.id.type_name, item.PAYWAYDES);
		if(item.PAYWAYID.equals("wechat")){
			holder.setImageResource(R.id.type_img, R.drawable.wechat_img);
		}else if(item.PAYWAYID.equals("alipay")){
			holder.setImageResource(R.id.type_img, R.drawable.alipay_img);
		}else if(item.PAYWAYID.equals("balance")){
			holder.setImageResource(R.id.type_img, R.drawable.balance_img);
		}else if(item.PAYWAYID.equals("quick")){
			holder.setImageResource(R.id.type_img, R.drawable.qrcode_img);
		}else if(item.PAYWAYID.equals("posp")){
			holder.setImageResource(R.id.type_img, R.drawable.pos_img);
		}
		
	}
}