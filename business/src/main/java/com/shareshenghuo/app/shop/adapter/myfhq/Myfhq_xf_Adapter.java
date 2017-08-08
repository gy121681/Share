package com.shareshenghuo.app.shop.adapter.myfhq;

import java.util.List;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.CommonAdapter;
import com.shareshenghuo.app.shop.adapter.ViewHolder;
import com.shareshenghuo.app.shop.network.bean.myfhq.FilialInvestmentBean;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.util.Util;

import android.content.Context;
import android.view.View;

/**
 * 
 * @author wyp
 * @version 创建时间：2017-5-26 下午11:38:39 类说明：我的分红权-秀点
 */
public class Myfhq_xf_Adapter extends CommonAdapter<FilialInvestmentBean> {

	public Myfhq_xf_Adapter(Context context, List<FilialInvestmentBean> data) {
		super(context, data, R.layout.excitation_list_item);
	}

	@Override
	public void conver(ViewHolder holder, final FilialInvestmentBean item,
			int position) {

		System.out.println("=====秀点Adapter:" + item.project_name);
		holder.setText(R.id.tv_amount, item.project_name);
		holder.getView(R.id.img1).setVisibility(View.GONE);
		holder.getView(R.id.img2).setVisibility(View.GONE);

		holder.getView(R.id.imgs).setBackgroundResource(R.drawable.share_b_mine_icon1_xiudian);// 秀点图标
		String money_num = Util.getfotmatnum(item.money_num, true, 1);
		holder.setText(R.id.tv_num, "-" + money_num);
		holder.setText(R.id.tv_time, DateUtil.getTime(item.create_time, 0));

		String str = item.channel_type;
		if (str.equals("0")) {// 产业链
			str = "产业链";
		} else {
			str = "消费";
		}
		holder.setText(R.id.fenhongquan_text_qufen, str);
		// if (item.type.equals("0")) {
		// if (item.trade_type != null && item.trade_type.equals("1")) {
		//
		// if (item.is_special_investment != null
		// && item.is_special_investment.equals("0")) {
		// holder.setText(R.id.tv_amount, "秀心接收: " + item.project_name);
		// } else {
		// holder.setText(R.id.tv_amount, "秀心接收: "
		// + item.user_shop_name);
		// }
		//
		// holder.setText(R.id.tv_num, "+" + item.filial_num);
		// } else {
		// if (item.integral != null) {
		// holder.setText(
		// R.id.tv_amount,
		// "增加积分: "
		// + Util.getfotmatnum(item.integral, true, 1));
		// holder.getView(R.id.img1).setVisibility(View.VISIBLE);
		// holder.getView(R.id.img2).setVisibility(View.GONE);
		// }
		// holder.setText(R.id.tv_num, "+" + item.filial_num);
		// }
		//
		// } else if (item.type.equals("1")) {
		//
		// if (item.trade_type != null && item.trade_type.equals("1")) {
		//
		// holder.setText(R.id.tv_amount, "秀心投资");
		// holder.setText(R.id.tv_num, "-" + item.filial_num);
		// } else if (item.trade_type != null && item.trade_type.equals("3")) {
		//
		// holder.setText(R.id.tv_amount, "代理服务费: " + item.project_name);
		// holder.setText(R.id.tv_num, "-" + item.filial_num);
		// } else if (item.trade_type != null && item.trade_type.equals("2")) {
		//
		// if (item.is_special_investment != null
		// && item.is_special_investment.equals("0")) {
		// holder.setText(R.id.tv_amount, "秀心转让: " + item.project_name);
		// } else {
		// holder.setText(R.id.tv_amount, "秀心转让: "
		// + item.user_shop_name);
		// }
		// holder.setText(R.id.tv_num, "-" + item.filial_num);
		// } else {
		// holder.setText(R.id.tv_num, "-" + item.filial_num);
		// if (item.integral != null) {
		// holder.setText(
		// R.id.tv_amount,
		// "激励到账: "
		// + Util.getfotmatnum(item.integral, true, 1));
		// holder.getView(R.id.img1).setVisibility(View.GONE);
		// holder.getView(R.id.img2).setVisibility(View.VISIBLE);
		// }
		// }
		//
		// } else {
		// holder.setText(R.id.tv_num, "");
		// holder.getView(R.id.img1).setVisibility(View.GONE);
		// holder.getView(R.id.img2).setVisibility(View.GONE);
		// }
		// holder.setText(R.id.tv_time, DateUtil.getTime(item.create_time, 0));
		//
		// // holder.getView(R.id.ivCommentNice).setVisibility(item.ranking<=5?
		// // View.VISIBLE:View.GONE);

	}
}