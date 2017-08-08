package com.shareshenghuo.app.shop.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.OrderDetailActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.app.Constant;
import com.shareshenghuo.app.shop.network.bean.OrderInfo;
import com.shareshenghuo.app.shop.network.request.UpdateOrderStatusRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.receiver.DataChangeWatcher;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.TransferTempDataUtil;
import com.shareshenghuo.app.shop.util.Utility;

public class OrderListAdapter extends CommonAdapter<OrderInfo> {

	public OrderListAdapter(Context context, List<OrderInfo> data) {
		super(context, data, R.layout.item_order);
	}

	@Override
	public void conver(ViewHolder holder, final OrderInfo item, int position) {
		holder.setImageResource(R.id.ivOrderTypeIcon, item.order_type==1? R.drawable.tag_wai : R.drawable.tag_tang);
		holder.setText(R.id.tvOrderNo, item.order_no);
		holder.setText(R.id.tvOrderDate, DateUtil.getTime(item.create_time, 0));
		if(item.order_type == 1) {
			holder.setText(R.id.tvOrderLabel, "地址：");
			holder.setText(R.id.tvOrderAddr, item.city_name+item.area_name+item.address);
		} else {
			holder.setText(R.id.tvOrderLabel, "桌号：");
			holder.setText(R.id.tvOrderAddr, item.desk_no);
		}
		holder.setText(R.id.tvOrderProdCount, item.all_count+"");
		holder.setText(R.id.tvOrderRealFee, item.order_real_fee+"");
		holder.setText(R.id.tvOrderStatus, Constant.ORDER_STATUS[item.status]);
		
		ListView lvProd = holder.getView(R.id.lvItemOrderProd);
		lvProd.setAdapter(new OrderProdAdapter(mContext, item.order_details_list));
		Utility.setListViewHeightBasedOnChildren(lvProd);
		
		holder.getConvertView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				TransferTempDataUtil.getInstance().setData(item);
				mContext.startActivity(new Intent(mContext, OrderDetailActivity.class));
			}
		});
		
		Button btnReceive = holder.getView(R.id.btnReceive);
		Button btnRefuse = holder.getView(R.id.btnRefuse);
		Button btnDeliver = holder.getView(R.id.btnDeliver);
		Button btnRefundOK = holder.getView(R.id.btnRefundOK);
		Button btnRefundNO = holder.getView(R.id.btnRefundNO);
		Button btnSettle = holder.getView(R.id.btnSettle);
		
		btnReceive.setVisibility(View.GONE);
		btnRefuse.setVisibility(View.GONE);
		btnDeliver.setVisibility(View.GONE);
		btnRefundOK.setVisibility(View.GONE);
		btnRefundNO.setVisibility(View.GONE);
		btnSettle.setVisibility(View.GONE);
		
		switch(item.status) {
		case 2:
			btnReceive.setVisibility(View.VISIBLE);
			btnRefuse.setVisibility(View.GONE);
			break;
			
		case 4:
			btnDeliver.setVisibility(View.VISIBLE);
			break;
			
		case 8:
			btnRefundOK.setVisibility(View.VISIBLE);
			btnRefundNO.setVisibility(View.VISIBLE);
			break;
			
		case 12:
		case 13:
			btnSettle.setVisibility(View.VISIBLE);
			break;
		}
		
		//接单
		btnReceive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				updateOrderStatus(item.id, 4);
			}
		});
		//拒绝接单
		btnRefuse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				updateOrderStatus(item.id, 11);
			}
		});
		//派送/上菜
		btnDeliver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(item.order_type == 1)
					updateOrderStatus(item.id, 3);
				else
					updateOrderStatus(item.id, 5);
			}
		});
		//退款同意
		btnRefundOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				updateOrderStatus(item.id, 9);
			}
		});
		//退款拒绝
		btnRefundNO.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				updateOrderStatus(item.id, 10);
			}
		});
		//收钱
		btnSettle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				updateOrderStatus(item.id, 6);
			}
		});
	}
	
	public void updateOrderStatus(final int orderId, final int status) {
		new AlertDialog.Builder(mContext).setMessage("是否确认操作？")
			.setPositiveButton("取消", null)
			.setNegativeButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					ProgressDialogUtil.showProgressDlg(mContext, "");
					UpdateOrderStatusRequest req = new UpdateOrderStatusRequest();
					req.order_id = orderId+"";
					req.status = status+"";
					RequestParams params = new RequestParams();
					try {
						params.setBodyEntity(new StringEntity(req.toJson()));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					new HttpUtils().send(HttpMethod.POST, Api.URL_UPDATE_ORDER_STATUS, params, new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							ProgressDialogUtil.dismissProgressDlg();
							T.showNetworkError(mContext);
						}

						@Override
						public void onSuccess(ResponseInfo<String> resp) {
							ProgressDialogUtil.dismissProgressDlg();
							BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
							if(Api.SUCCEED == bean.result_code) {
								T.showShort(mContext, "操作成功");
								DataChangeWatcher.sendDataChangeBroadcast(mContext);
							} else {
								T.showShort(mContext, bean.result_desc);
							}
						}
					});
				}
			})
			.show();
	}
}
