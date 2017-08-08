package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import com.amap.api.maps2d.model.Text;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.ExcitationBean;
import com.shareshenghuo.app.shop.network.bean.FractionBean;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.util.Util;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
/**
 * 
* @author wyp
* @version 创建时间：2017-6-7 下午9:10:50
* 类说明：秀儿秀心
 */
public class ExcitationAdapter extends CommonAdapter<FractionBean> {

	public ExcitationAdapter(Context context, List<FractionBean> data) {
		super(context, data, R.layout.excitation_list_item);
	}

	@Override
	public void conver(ViewHolder holder, final FractionBean item, int position) {
		holder.getView(R.id.imgs).setBackgroundResource(R.drawable.share_b_mine_icon1_xiuxin);
		System.out.println("===秀儿秀心:"+item.toString());
		if (item.type.equals("0")) {
			if (item.trade_type != null && item.trade_type.equals("2")) {
				if (item.is_special_investment != null
						&& item.is_special_investment.equals("0")) {
					holder.setText(R.id.tv_amount, "秀心接收: " + (TextUtils.isEmpty(item.project_name) ? "" : item.project_name));
				} else {
					holder.setText(R.id.tv_amount, "秀心接收: "
							+ (TextUtils.isEmpty(item.user_shop_name) ? "" : item.user_shop_name));
				}
				holder.getView(R.id.img1).setVisibility(View.GONE);
				holder.getView(R.id.img2).setVisibility(View.GONE);
				holder.setText(R.id.tv_num, "+" + item.filial_num);
			} else {
				if (item.integral != null) {
					holder.setText(
							R.id.tv_amount,
							"增加积分: "
									+ Util.getfotmatnum(item.integral, true, 1));
					holder.getView(R.id.img1).setVisibility(View.VISIBLE);
					holder.getView(R.id.img2).setVisibility(View.GONE);
				}
				holder.setText(R.id.tv_num, "+" + item.filial_num);
			}

		} else if (item.type.equals("1")) {

			if (item.trade_type != null && item.trade_type.equals("03")) {

				holder.setText(R.id.tv_amount, "秀心投资");
				holder.setText(R.id.tv_num, "-" + item.filial_num);
			} else if (item.trade_type != null && item.trade_type.equals("3")) {

				holder.setText(R.id.tv_amount, "代理服务费: " + item.project_name);
				holder.setText(R.id.tv_num, "-" + item.filial_num);
			} else if (item.trade_type != null && item.trade_type.equals("2")) {

				if (item.is_special_investment != null
						&& item.is_special_investment.equals("0")) {
					holder.setText(R.id.tv_amount, "秀心转让: " + item.project_name);
				} else {
					holder.setText(R.id.tv_amount, "秀心转让: "
							+ item.user_shop_name);
				}
				holder.setText(R.id.tv_num, "-" + item.filial_num);
				holder.getView(R.id.img1).setVisibility(View.GONE);
				holder.getView(R.id.img2).setVisibility(View.GONE);
			}else if (item.trade_type != null && item.trade_type.equals("07")) {
				holder.setText(R.id.tv_amount, "秀心入股");
				holder.setText(R.id.tv_num, "-" + item.filial_num);
				holder.getView(R.id.img1).setVisibility(View.GONE);
				holder.getView(R.id.img2).setVisibility(View.GONE);
			}  else {
				holder.setText(R.id.tv_num, "-" + item.filial_num);
				if (item.integral != null) {
					holder.setText(
							R.id.tv_amount,
							"激励到账: "
									+ Util.getfotmatnum(item.integral, true, 1));
					holder.getView(R.id.img1).setVisibility(View.GONE);
					holder.getView(R.id.img2).setVisibility(View.VISIBLE);
				}
			}

		} else {
			holder.setText(R.id.tv_num, "");
			holder.getView(R.id.img1).setVisibility(View.GONE);
			holder.getView(R.id.img2).setVisibility(View.GONE);
		}
		holder.setText(R.id.tv_time, DateUtil.getTime(item.create_time, 0));

		// holder.getView(R.id.ivCommentNice).setVisibility(item.ranking<=5?
		// View.VISIBLE:View.GONE);

	}
}