package com.shareshenghuo.app.user.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.FilialobeBean;
import com.shareshenghuo.app.user.util.DateUtil;
import com.shareshenghuo.app.user.util.Util;

public class IncentivePoints1Adapter extends CommonAdapter<FilialobeBean> {

	public IncentivePoints1Adapter(Context context, List<FilialobeBean> data) {
		super(context, data, R.layout.incentivepoints1_item);
	}

	@Override
	public void conver(ViewHolder holder, final FilialobeBean item, int position) {
		System.out.println("==" + item.toString());
		// holder.setImageByURL(R.id.ivCommentAvatar, item.user_photo);
		// holder.setText(R.id.tv_amount, item.amount);
		// holder.setText(R.id.tv_time, DateUtil.getTime(item.create_time, 0));
		String time = DateUtil.getTime(item.create_time, 0);
		if (time.length() > 10) {
			holder.setText(
					R.id.tv_time,
					time.substring(0, 10) + ""
							+ time.substring(10, time.length()));
		}
		holder.getView(R.id.img).setVisibility(View.VISIBLE);
		holder.getView(R.id.tv_name).setVisibility(View.GONE);
		holder.setText(R.id.tv_name1, "" + item.filial_piety);
		if (item.operb_type.equals("0")) {// 0+
			holder.setText(R.id.tv_num,
					" + " + Util.getnum(item.money_num, false));
			if (item.opers_type.equals("13")) {
				holder.setText(R.id.tv_time, time);
				holder.getView(R.id.tv_name).setVisibility(View.VISIBLE);
				String body = "";
				if (null != item.body && !item.body.equals("null")) {
					body = item.body;
				}
				holder.setText(R.id.tv_name, "" + body);
				holder.setText(R.id.tv_name1, "");
				holder.getView(R.id.img).setVisibility(View.GONE);
				if (time.length() > 10) {
					holder.setText(R.id.tv_time, time.substring(0, 10) + ""
							+ time.substring(10, time.length()));
				}
			}
			if (item.opers_type.equals("15")) {// 消费
				holder.setText(R.id.tv_time, time);
				// holder.getView(R.id.tv_name).setVisibility(View.VISIBLE);
				// holder.setText(R.id.tv_name, "" + "秀点入股");
				// String num = "";
				// try {
				// double d = Double.valueOf(item.fee_rate) * 100;
				// num = String.valueOf(d);
				// } catch (Exception e) {
				// }
				// if (num.contains(".")) {// 如果包含小数点
				// // 如果等于.0结束的话
				// num = String.valueOf(num).substring(0,
				// String.valueOf(num).lastIndexOf("."));
				// }////换算法
				String fee_rate = "";
				if (item.fee_rate.equals("1")) {
					fee_rate = "20";
				} else if (item.fee_rate.equals("2")) {
					fee_rate = "40";
				} else if (item.fee_rate.equals("3")) {
					fee_rate = "80";
				}
				// Util.getnum(item.total_fee, false)
				holder.setText(R.id.tv_name1,
						"消费" + Util.getnum(item.total_fee, false) + "\n  "
								+ fee_rate + " %");

				holder.getView(R.id.img).setVisibility(View.GONE);
				if (time.length() > 10) {
					holder.setText(R.id.tv_time, time.substring(0, 10) + "\n"
							+ time.substring(10, time.length()));
				}
			}
			if (item.opers_type.equals("16")) {// 16商城消费
				holder.setText(R.id.tv_time, time);
				// holder.getView(R.id.tv_name).setVisibility(View.VISIBLE);
				// holder.setText(R.id.tv_name, "" + "秀点入股");
				String num = "";
				try {
					double d = Double.valueOf(item.fee_rate) * 100;
					num = String.valueOf(d);
				} catch (Exception e) {
				}
				if (num.contains(".")) {// 如果包含小数点
					// 如果等于.0结束的话
					num = String.valueOf(num).substring(0,
							String.valueOf(num).lastIndexOf("."));
				}
				holder.setText(R.id.tv_name1, "商城消费" + "\n  " + num + "秀点");

				holder.getView(R.id.img).setVisibility(View.GONE);
				if (time.length() > 10) {
					holder.setText(R.id.tv_time, time.substring(0, 10) + "\n"
							+ time.substring(10, time.length()));
				}
			}
		} else if (item.operb_type.equals("1")) {// 1-
			holder.setText(R.id.tv_num,
					" - " + Util.getnum(item.money_num, false));
			if (item.opers_type.equals("13")) {
				holder.setText(R.id.tv_time, time);
				holder.getView(R.id.tv_name).setVisibility(View.VISIBLE);
				String body = "";
				if (null != item.body && !item.body.equals("null")) {
					body = item.body;
				}
				holder.setText(R.id.tv_name, "" + body);
				holder.setText(R.id.tv_name1, "");
				holder.getView(R.id.img).setVisibility(View.GONE);
				if (time.length() > 10) {
					holder.setText(R.id.tv_time, time.substring(0, 10) + ""
							+ time.substring(10, time.length()));
				}
			}
			if (item.opers_type.equals("14")) {
				holder.setText(R.id.tv_time, time);
				holder.getView(R.id.tv_name).setVisibility(View.VISIBLE);
				holder.setText(R.id.tv_name, "" + "秀点入股");
				holder.setText(R.id.tv_name1, "");
				holder.getView(R.id.img).setVisibility(View.GONE);
				if (time.length() > 10) {
					holder.setText(R.id.tv_time, time.substring(0, 10) + ""
							+ time.substring(10, time.length()));
				}
			}
			if (item.opers_type.equals("15")) {// 消费
				holder.setText(R.id.tv_time, time);
				// holder.getView(R.id.tv_name).setVisibility(View.VISIBLE);
				// holder.setText(R.id.tv_name, "" + "秀点入股");
				// String num = "";
				// try {
				// double d = Double.valueOf(item.fee_rate) * 100;
				// num = String.valueOf(d);
				// } catch (Exception e) {
				// }
				// if (num.contains(".")) {// 如果包含小数点
				// // 如果等于.0结束的话
				// num = String.valueOf(num).substring(0,
				// String.valueOf(num).lastIndexOf("."));
				// }
				String fee_rate = "";
				if (item.fee_rate.equals("1")) {
					fee_rate = "20";
				} else if (item.fee_rate.equals("2")) {
					fee_rate = "40";
				} else if (item.fee_rate.equals("3")) {
					fee_rate = "80";
				}
				// Util.getnum(item.total_fee, false)
				holder.setText(R.id.tv_name1,
						"消费" + Util.getnum(item.total_fee, false) + "\n  "
								+ fee_rate + " %");

				holder.getView(R.id.img).setVisibility(View.GONE);
				if (time.length() > 10) {
					holder.setText(R.id.tv_time, time.substring(0, 10) + "\n"
							+ time.substring(10, time.length()));
				}
			}
			if (item.opers_type.equals("16")) {// 16商城消费
				holder.setText(R.id.tv_time, time);
				// holder.getView(R.id.tv_name).setVisibility(View.VISIBLE);
				// holder.setText(R.id.tv_name, "" + "秀点入股");
				String num = "";
				try {
					double d = Double.valueOf(item.fee_rate) * 100;
					num = String.valueOf(d);
				} catch (Exception e) {
				}
				if (num.contains(".")) {// 如果包含小数点
					// 如果等于.0结束的话
					num = String.valueOf(num).substring(0,
							String.valueOf(num).lastIndexOf("."));
				}
				holder.setText(R.id.tv_name1, "商城消费" + "\n  " + num + "秀点");

				holder.getView(R.id.img).setVisibility(View.GONE);
				if (time.length() > 10) {
					holder.setText(R.id.tv_time, time.substring(0, 10) + "\n"
							+ time.substring(10, time.length()));
				}
			}
		}

		// holder.setText(R.id.tv_name, item.name);
		// holder.getView(R.id.ivCommentNice).setVisibility(item.ranking<=5?
		// View.VISIBLE:View.GONE);

	}
}