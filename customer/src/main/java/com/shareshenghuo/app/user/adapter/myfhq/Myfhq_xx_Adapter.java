package com.shareshenghuo.app.user.adapter.myfhq;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.CommonAdapter;
import com.shareshenghuo.app.user.adapter.ViewHolder;
import com.shareshenghuo.app.user.network.bean.myfhq.FilialInvestmentBean;
import com.shareshenghuo.app.user.util.DateUtil;

/**
 * 
 * @author wyp
 * @version 创建时间：2017-5-26 下午11:39:15 类说明：我的分红权-秀点
 */
public class Myfhq_xx_Adapter extends CommonAdapter<FilialInvestmentBean> {

	public Myfhq_xx_Adapter(Context context, List<FilialInvestmentBean> data) {
		super(context, data, R.layout.excitation_list_item);
	}

	@Override
	public void conver(ViewHolder holder, final FilialInvestmentBean item,
			int position) {

		holder.setText(R.id.tv_amount, item.project_name);
		holder.getView(R.id.img1).setVisibility(View.GONE);
		holder.getView(R.id.img2).setVisibility(View.GONE);

		holder.getView(R.id.imgs).setBackgroundResource(R.drawable.icon_35);// 秀心图标
		holder.setText(R.id.tv_num, "-" + item.filial_num);
		holder.setText(R.id.tv_time, DateUtil.getTime(item.create_time, 0));
		
		String str=item.channel_type;
		if (str.equals("0")) {//产业链
			str="产业链";
		}else{
			str="消费";
		}
		holder.setText(R.id.fenhongquan_text_qufen,str);
		
		

		// holder.getView(R.id.ivCommentNice).setVisibility(item.ranking<=5?
		// View.VISIBLE:View.GONE);

	}
}