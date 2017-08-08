package com.shareshenghuo.app.user.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.shareshenghuo.app.user.CommentOrderActivity;
import com.shareshenghuo.app.user.OrderDetailActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.ShopDetailActivity;
import com.shareshenghuo.app.user.app.Constant;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.OrderDetailInfo;
import com.shareshenghuo.app.user.network.request.UpdateOrderRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.TransferTempDataUtil;
import com.shareshenghuo.app.user.util.Utility;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class OrderListAdapter extends CommonAdapter<OrderDetailInfo> {
	
	public static final int REQ_ORDER_OPERATE = 1;
	
	public Fragment fragment;
	
	public OrderListAdapter(Context context, List<OrderDetailInfo> data) {
		super(context, data, R.layout.item_order);
	}

	@Override
	public void conver(ViewHolder holder, final OrderDetailInfo item, int position) {
		holder.setText(R.id.tvOrderShopName, item.shop_name);
		holder.setText(R.id.tvOrderStatus, Constant.ORDER_STATUS[item.status]);
		holder.setText(R.id.tvOrderProdCount, "共"+item.all_count+"件商品");
		holder.setText(R.id.tvOrderTotal, item.order_real_fee+"");
		
		ListView lvProd = holder.getView(R.id.lvOrderProd);
		lvProd.setAdapter(new OrderProdDetailAdapter(mContext, item.order_details_list));
		Utility.setListViewHeightBasedOnChildren(lvProd);
		lvProd.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				toOrderDetail(item);
			}
		});
		
		Button btnRefund = holder.getView(R.id.btnOrderRefund);
		Button btnCancel = holder.getView(R.id.btnOrderCancel);
		Button btnReceived = holder.getView(R.id.btnOrderReceived);
		Button btnPay = holder.getView(R.id.btnOrderPay);
		Button btnComment = holder.getView(R.id.btnOrderComment);
		Button btnDelete = holder.getView(R.id.btnOrderDelete);
		Button btnConfirm = holder.getView(R.id.btnOrderConfirm);
		
		btnRefund.setVisibility(View.GONE);
		btnCancel.setVisibility(View.GONE);
		btnReceived.setVisibility(View.GONE);
		btnPay.setVisibility(View.GONE);
		btnComment.setVisibility(View.GONE);
		btnDelete.setVisibility(View.GONE);
		btnConfirm.setVisibility(View.GONE);
		
		if(item.pay_type == 4)
			// 现金支付
			btnRefund.setText("取消订单");
		else
			btnRefund.setText("退款");
		
		switch(item.status) {
		case 0:
			btnDelete.setVisibility(View.VISIBLE);
			break;
		
		case 1:
			btnCancel.setVisibility(View.VISIBLE);
			btnPay.setVisibility(View.VISIBLE);
			break;
			
		case 2:
			if(item.order_type == 2)
				holder.setText(R.id.tvOrderStatus, "等待上菜中");
			else
				btnRefund.setVisibility(View.VISIBLE);
			break;
			
		case 3:
			btnReceived.setVisibility(View.VISIBLE);
			break;
			
		case 5:
			btnReceived.setVisibility(View.VISIBLE);
			break;
			
		case 6:
			btnComment.setVisibility(View.VISIBLE);
			break;
			
		case 7:
			btnDelete.setVisibility(View.VISIBLE);
			break;
			
		case 15:
			if(item.order_type == 2) {
				btnRefund.setVisibility(View.VISIBLE);
				btnConfirm.setVisibility(View.VISIBLE);
			}
			break;
		}
		
		holder.getConvertView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				toOrderDetail(item);
			}
		});
		
		holder.getView(R.id.llOrderShop).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(item.shop_id > 0) {
					Intent it = new Intent(mContext, ShopDetailActivity.class);
					it.putExtra("shopId", item.shop_id);
					mContext.startActivity(it);
				}
			}
		});
		
		btnPay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				toPay(item);
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				cancelOrder(item);
			}
		});
		
		btnRefund.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				refund(item);
			}
		});
		
		btnReceived.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				receive(item);
			}
		});
		
		btnComment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				TransferTempDataUtil.getInstance().setData(item.order_details_list);
				Intent it = new Intent(mContext, CommentOrderActivity.class);
				it.putExtra("orderId", item.id);
				it.putExtra("shopId", item.shop_id);
				fragment.startActivityForResult(it, REQ_ORDER_OPERATE);
			}
		});
		
		btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				delete(item);
			}
		});
		
		btnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				confirm(item);
			}
		});
	}
	
	public void toOrderDetail(OrderDetailInfo item) {
		if(item.status == 1)
			toPay(item);
		else {
			Intent it = new Intent(mContext, OrderDetailActivity.class);
			it.putExtra("orderId", item.id);
			fragment.startActivityForResult(it, REQ_ORDER_OPERATE);
		}
	}
	
	/**
	 * 去支付
	 */
	public void toPay(OrderDetailInfo item) {
//		Intent it = new Intent(mContext, SubmitOrderActivity.class);
//		it.putExtra("orderId", item.id);
//		it.putExtra("orderType", item.order_type);
//		it.putExtra("shopId", item.shop_id);
//		TransferTempDataUtil.getInstance().setData(item.order_details_list);
//		fragment.startActivityForResult(it, REQ_ORDER_OPERATE);
	}
	
	public void cancelOrder(final OrderDetailInfo item) {
		new AlertDialog.Builder(mContext).setMessage("是否确定取消该订单?")
			.setNegativeButton("否", null)
			.setPositiveButton("是", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					updateOrder(item, 0);
				}
			})
			.show();
	}
	
	public void refund(final OrderDetailInfo item) {
		new AlertDialog.Builder(mContext).setMessage("是否确定申请退款?")
		.setNegativeButton("否", null)
		.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				updateOrder(item, 8);
			}
		})
		.show();
	}
	
	public void receive(final OrderDetailInfo item) {
		new AlertDialog.Builder(mContext).setMessage("是否确认收货?")
		.setNegativeButton("否", null)
		.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				updateOrder(item, 6);
			}
		})
		.show();
	}
	
	public void delete(final OrderDetailInfo item) {
		new AlertDialog.Builder(mContext).setMessage("是否确定删除该订单?")
		.setNegativeButton("否", null)
		.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				updateOrder(item, -1);
			}
		})
		.show();
	}
	
	private EditText edDestNo;
	
	public void confirm(final OrderDetailInfo item) {
		edDestNo = new EditText(mContext);
		edDestNo.setBackgroundResource(R.drawable.bg_arc_white);
		edDestNo.setHint("请填写桌号");
		edDestNo.setText(item.desk_no);
		
		new AlertDialog.Builder(mContext).setMessage("用户一旦点击确认下单,商家就会开始上菜,局时需要退款需要自行和商家沟通")
		.setView(edDestNo)
		.setNegativeButton("取消", null)
		.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(TextUtils.isEmpty(edDestNo.getText().toString())) {
					T.showShort(mContext, "确认失败，桌号为空");
					return;
				}
				updateOrder(item, 2);
			}
		}).show();
	}
	
	public void updateOrder(final OrderDetailInfo item, final int status) {
		ProgressDialogUtil.showProgressDlg(mContext, "");
		UpdateOrderRequest req = new UpdateOrderRequest();
		req.order_id = item.id+"";
		req.user_id = UserInfoManager.getUserId(mContext)+"";
		req.status = status+"";
		if(item.order_type == 2 && edDestNo != null)
			req.desk_no = edDestNo.getText().toString();
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPDATE_ORDER, params, new RequestCallBack<String>() {
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
					T.showShort(mContext, "成功");
					if(status == -1) {
						remove(item);
					} else {
						item.status = status;
					}
					notifyDataSetChanged();
				} else {
					T.showShort(mContext, bean.result_desc);
				}
			}
		});
	}
}
