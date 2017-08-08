package com.shareshenghuo.app.user.adapter;

import java.util.List;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.IntegralBean;
import com.shareshenghuo.app.user.util.DateUtil;
import com.shareshenghuo.app.user.util.Util;

import android.content.Context;
import android.view.View;

public class IntegralAdapter extends CommonAdapter<IntegralBean> {

	public IntegralAdapter(Context context, List<IntegralBean> data) {
		super(context, data, R.layout.integral_list_item);
	}

	@Override
	public void conver(ViewHolder holder, final IntegralBean item, int position) {

		// Util.getfotmatnum(item.total_fee, true,1)
		if (item.operb_type.equals("0")) {
			holder.setImageResource(R.id.xiaofen, R.drawable.point_2);
			holder.setText(R.id.tv_toamounts,
					"+" + Util.getfotmatnum(item.integral_num, true, 1));
			if (item.total_fee != null) {
				if (item.opers_type.equals("02")) {
					holder.getView(R.id.oiltypes).setVisibility(View.VISIBLE);
					holder.getView(R.id.rate_img).setVisibility(View.INVISIBLE);
					holder.setText(
							R.id.tv_amount,
							"油卡消费 ￥: "
									+ Util.getfotmatnum(item.total_fee, true, 1));
					holder.setText(R.id.oiltypes, "支付积分");

				} else if (item.opers_type.equals("03")) {
					holder.getView(R.id.oiltypes).setVisibility(View.VISIBLE);
					holder.getView(R.id.rate_img).setVisibility(View.INVISIBLE);
					holder.setText(
							R.id.tv_amount,
							"油卡消费 ￥: "
									+ Util.getfotmatnum(item.total_fee, true, 1));
					holder.setText(R.id.oiltypes, "奖励积分");
				} else if (item.opers_type.equals("04")) {
					holder.getView(R.id.oiltypes).setVisibility(View.VISIBLE);
					holder.getView(R.id.rate_img).setVisibility(View.INVISIBLE);
					holder.setText(
							R.id.tv_amount,
							"油卡消费 ￥: "
									+ Util.getfotmatnum(item.total_fee, true, 1));
					holder.setText(R.id.oiltypes, "销售积分");
				} else if (item.opers_type.equals("05")) {
					holder.getView(R.id.oiltypes).setVisibility(View.VISIBLE);
					holder.getView(R.id.rate_img).setVisibility(View.INVISIBLE);
					holder.setText(
							R.id.tv_amount,
							"油卡消费 ￥: "
									+ Util.getfotmatnum(item.total_fee, true, 1));
					holder.setText(R.id.oiltypes, "代理积分");
				} else {
					holder.getView(R.id.rate_img).setVisibility(View.VISIBLE);
					holder.getView(R.id.oiltypes).setVisibility(View.GONE);
					holder.setText(R.id.oiltypes, "");
					holder.setText(
							R.id.tv_amount,
							"交易金额: "
									+ Util.getfotmatnum(item.total_fee, true, 1));

				}
			} else {
				holder.getView(R.id.rate_img).setVisibility(View.VISIBLE);
				holder.getView(R.id.oiltypes).setVisibility(View.GONE);
				holder.setText(R.id.oiltypes, "");
				holder.setText(R.id.tv_amount,
						"交易金额: " + Util.getfotmatnum(item.total_fee, true, 1));
			}

			if (item.discount_type != null && item.discount_type.equals("1")) {
				holder.getView(R.id.rate_img).setTag("1");

				// if(holder.getView(R.id.rate_img).getTag().equals("1")){
				holder.setImageResource(R.id.rate_img, R.drawable.share_c_consume_series_25);
				// }else{
				// holder.setImageResource(R.id.rate_img, R.drawable.point_2);
				// }

			} else
			// {
			// holder.setImageResource(R.id.rate_img, R.drawable.point_2);
			// }
			if (item.discount_type != null && item.discount_type.equals("2")) {
				holder.getView(R.id.rate_img).setTag("2");
				// if(holder.getView(R.id.rate_img).getTag().equals("2")){
				holder.setImageResource(R.id.rate_img, R.drawable.share_c_consume_series_50);
				// }else{
				// holder.setImageResource(R.id.rate_img, R.drawable.point_2);
				// }

			} else
			// {
			// holder.setImageResource(R.id.rate_img, R.drawable.point_2);
			// }
			if (item.discount_type != null && item.discount_type.equals("3")) {
				holder.getView(R.id.rate_img).setTag("3");
				// if(holder.getView(R.id.rate_img).getTag().equals("3")){
				holder.setImageResource(R.id.rate_img, R.drawable.share_c_consume_series_100);
				// }else{
				// holder.setImageResource(R.id.rate_img, R.drawable.point_2);
				// }
			} else {
				holder.setImageResource(R.id.rate_img, R.drawable.point_2);
			}
			if (item.opers_type.equals("13")) {
				holder.setImageResource(R.id.xiaofen,
						R.drawable.cb_collect_true);
				holder.getView(R.id.rate_img).setVisibility(View.GONE);
				holder.getView(R.id.oiltypes).setVisibility(View.GONE);
				holder.setText(R.id.oiltypes, "");
				holder.setText(R.id.tv_amount, "秀心接收: " + item.filial_num);
				holder.setText(R.id.tv_toamounts,
						"+" + Util.getfotmatnum(item.integral_num, true, 1));
			}
//			if (item.opers_type.equals("13")) {
//				holder.setImageResource(R.id.xiaofen,
//						R.drawable.cb_collect_true);
//				holder.getView(R.id.rate_img).setVisibility(View.GONE);
//				holder.getView(R.id.oiltypes).setVisibility(View.GONE);
//				holder.setText(R.id.oiltypes, "");
//				holder.setText(R.id.tv_amount, "秀心接收: " + item.filial_num);
//				holder.setText(R.id.tv_toamounts,
//						"+" + Util.getfotmatnum(item.integral_num, true, 1));
//			}

			else if (item.opers_type.equals("12")) {
				holder.getView(R.id.rate_img).setVisibility(View.GONE);
				holder.getView(R.id.oiltypes).setVisibility(View.GONE);
				holder.setText(R.id.oiltypes, "");
				holder.setText(R.id.tv_amount, "秀心投资");
				holder.setText(R.id.tv_toamounts,
						"+" + Util.getfotmatnum(item.integral_num, true, 1));
			} else if (item.opers_type.equals("14")) {
				holder.setImageResource(R.id.xiaofen,
						R.drawable.cb_collect_true);
				holder.getView(R.id.rate_img).setVisibility(View.GONE);
				holder.getView(R.id.oiltypes).setVisibility(View.GONE);
				holder.setText(R.id.oiltypes, "");
				holder.setText(R.id.tv_amount, "代理服务费: " + item.filial_num);
				holder.setText(R.id.tv_toamounts,
						"+" + Util.getfotmatnum(item.integral_num, true, 1));
			}else if (item.opers_type.equals("16")) {
				holder.getView(R.id.xiaofen).setVisibility(View.GONE);//隐藏小图标
				holder.getView(R.id.rate_img).setVisibility(View.GONE);
				holder.getView(R.id.oiltypes).setVisibility(View.GONE);
				holder.setText(R.id.tv_amount, "激活积分");
				holder.setText(R.id.tv_toamounts,
						"+" + Util.getfotmatnum(item.integral_num, true, 1));
			}

		} else if (item.operb_type.equals("1")) {// operb_type = 0 是正 1 是负
		// holder.setImageResource(R.id.rate_img, R.drawable.point_2);
		// holder.setImageResource(R.id.xiaofen, R.drawable.share_c_mine_icon1_xiudian);
			if (item.opers_type.equals("13")) {
				holder.setImageResource(R.id.xiaofen,
						R.drawable.cb_collect_true);
				holder.getView(R.id.rate_img).setVisibility(View.GONE);
				holder.getView(R.id.oiltypes).setVisibility(View.GONE);
				holder.setText(R.id.tv_amount, "秀心转让: " + item.filial_num);
				holder.setText(R.id.tv_toamounts,
						"-" + Util.getfotmatnum(item.integral_num, true, 1));
			}
			
			else if (item.opers_type.equals("12")) {
				holder.getView(R.id.rate_img).setVisibility(View.GONE);
				holder.getView(R.id.oiltypes).setVisibility(View.GONE);
				holder.setText(R.id.tv_amount, "秀心投资");
				String str = "-";
				if (item.operb_type.equals("0")) {
					str = "+";
				}
				holder.setText(R.id.tv_toamounts,
						str + Util.getfotmatnum(item.integral_num, true, 1));
			} else if (item.opers_type.equals("14")) {
				holder.setImageResource(R.id.xiaofen,
						R.drawable.cb_collect_true);
				holder.getView(R.id.rate_img).setVisibility(View.GONE);
				holder.getView(R.id.oiltypes).setVisibility(View.GONE);
				holder.setText(R.id.tv_amount, "代理服务费: " + item.filial_num);
				holder.setText(R.id.tv_toamounts,
						"-" + Util.getfotmatnum(item.integral_num, true, 1));
			} else if (item.opers_type.equals("15")) {
				holder.getView(R.id.xiaofen).setVisibility(View.GONE);//隐藏小图标
				holder.getView(R.id.rate_img).setVisibility(View.GONE);
				holder.getView(R.id.oiltypes).setVisibility(View.GONE);
				holder.setText(R.id.tv_amount, "积分平移");
				String str = "-";
				if (item.operb_type.equals("0")) {
					str = "+";
				}
				holder.setText(R.id.tv_toamounts,
						str + Util.getfotmatnum(item.integral_num, true, 1));
			} else if (item.opers_type.equals("16")) {
				holder.getView(R.id.xiaofen).setVisibility(View.GONE);//隐藏小图标
				holder.getView(R.id.rate_img).setVisibility(View.GONE);
				holder.getView(R.id.oiltypes).setVisibility(View.GONE);
				holder.setText(R.id.tv_amount, "激活积分");
				holder.setText(R.id.tv_toamounts,
						"-" + Util.getfotmatnum(item.integral_num, true, 1));
			} else if (item.opers_type.equals("17")) {
				holder.getView(R.id.xiaofen).setVisibility(View.GONE);//隐藏小图标
				holder.getView(R.id.rate_img).setVisibility(View.GONE);
				holder.getView(R.id.oiltypes).setVisibility(View.GONE);
				holder.setText(R.id.tv_amount, "转让手续费 ");
				String str = "-";
				if (item.operb_type.equals("0")) {
					str = "+";
				}
				holder.setText(R.id.tv_toamounts,
						str + Util.getfotmatnum(item.integral_num, true, 1));
			} else if (item.opers_type.equals("18")) {
				holder.getView(R.id.xiaofen).setVisibility(View.GONE);//隐藏小图标
				holder.getView(R.id.rate_img).setVisibility(View.GONE);
				holder.getView(R.id.oiltypes).setVisibility(View.GONE);
				holder.setText(R.id.tv_amount, "秀心入股 ");
				String str = "-";
				if (item.operb_type.equals("0")) {
					str = "+";
				}
				holder.setText(R.id.tv_toamounts,
						str + Util.getfotmatnum(item.integral_num, true, 1));
			}

			else {
				holder.setImageResource(R.id.rate_img, R.drawable.point_2);
				holder.setImageResource(R.id.xiaofen, R.drawable.share_c_mine_icon1_xiudian);
				if (item.money_num != null) {
					holder.getView(R.id.oiltypes).setVisibility(View.GONE);
					holder.setText(
							R.id.tv_amount,
							"激励到账: "
									+ Util.getfotmatnum(item.money_num, true, 1));
					holder.setText(R.id.tv_toamounts,
							"-" + Util.getfotmatnum(item.integral_num, true, 1));
				}
			}
		} else {
			//holder.getView(R.id.rate_img).setVisibility(View.INVISIBLE);
			holder.setImageResource(R.id.rate_img, R.drawable.point_2);
			holder.setImageResource(R.id.xiaofen, R.drawable.share_c_mine_icon1_xiudian);
			if (item.money_num != null) {
				holder.getView(R.id.oiltypes).setVisibility(View.GONE);
				holder.setText(R.id.oiltypes, "");
				holder.setText(R.id.tv_amount,
						"激励到账: " + Util.getfotmatnum(item.money_num, true, 1));
				holder.setText(R.id.tv_toamounts,
						"-" + Util.getfotmatnum(item.integral_num, true, 1));
			}

		}
		holder.setText(R.id.tv_time, DateUtil.getTime(item.create_time, 0));

		// // holder.setImageByURL(R.id.ivCommentAvatar, item.user_photo);
		// holder.setText(R.id.tv_amount, item.amount);
		// holder.setText(R.id.tv_time, DateUtil.getTime(item.createTime, 0));
		// holder.setText(R.id.tv_time, item.time);

		// holder.getView(R.id.ivCommentNice).setVisibility(item.ranking<=5?
		// View.VISIBLE:View.GONE);

	}
}