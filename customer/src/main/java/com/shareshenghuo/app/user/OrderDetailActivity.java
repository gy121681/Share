package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.OrderProdAdapter;
import com.shareshenghuo.app.user.app.Constant;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.OrderDetailInfo;
import com.shareshenghuo.app.user.network.request.UpdateOrderRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.network.response.OrderDetailResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.DateUtil;
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

public class OrderDetailActivity extends BaseTopActivity implements OnClickListener {
	
	private static final int REQ_COMMENT_ORDER = 0x101;
	
	private ListView lvProd;
	
	private int orderId;
	private OrderDetailInfo orderDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		init();
		loadData();
	}
	
	public void init() {
		orderId = getIntent().getIntExtra("orderId", 0);
		
		initTopBar("订单详情");
		lvProd = getView(R.id.lvOrderProd);
		
	}
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		UpdateOrderRequest req = new UpdateOrderRequest();
		req.order_id = orderId+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_ORDER_DETAIL, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				OrderDetailResponse bean = new Gson().fromJson(resp.result, OrderDetailResponse.class);
				if(Api.SUCCEED == bean.result_code && bean.data != null) {
					orderDetail = bean.data;
					updateView(bean.data);
				} else {
					T.showShort(OrderDetailActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(OrderDetailActivity.this);
			}
		});
	}
	
	private String[] pay_type = {"余额支付", "微信支付", "支付宝支付", "现金支付", "银联支付"};
	
	public void updateView(OrderDetailInfo data) {
		setText(R.id.tvOrderShopName, data.shop_name);
		getView(R.id.llOrderShopDetail).setOnClickListener(this);
		lvProd.setAdapter(new OrderProdAdapter(this, data.order_details_list));
		Utility.setListViewHeightBasedOnChildren(lvProd);
		setText(R.id.tvOrderTotalFee, "¥"+data.order_all_fee);
		setText(R.id.tvOrderCouponFee, "¥"+data.coupon_fee);
		setText(R.id.tvOrderRealFee, "¥"+data.order_real_fee);
		setText(R.id.tvOrderNo, data.order_no);
		setText(R.id.tvOrderDate, DateUtil.getTime(data.create_time, 0));
		setText(R.id.tvOrderRemark, data.remarks);
		setText(R.id.tvOrderPayType, pay_type[data.pay_type-1]);
		setText(R.id.tvOrderContact, data.real_name);
		setText(R.id.tvOrderMobile, data.mobile);
		setText(R.id.tvOrderType, data.order_type==1? "外卖订单":"到店消费");
		if(data.order_type == 1) {
			// 外卖
			setText(R.id.tvOrderLabel, "地       址：");
			setText(R.id.tvOrderAddr, data.city_name+data.area_name+data.address);
		} else {
			// 到店
			setText(R.id.tvOrderLabel, "桌       号：");
			setText(R.id.tvOrderAddr, data.desk_no);
		}
		TextView tvStatus = getView(R.id.tvOrderStatus);
		tvStatus.setText(Constant.ORDER_STATUS[data.status]);
		getView(R.id.btnViewOrderStatus).setOnClickListener(this);
		
		Button btnCancel = getView(R.id.btnOrderCancel);
		btnCancel.setVisibility(View.GONE);
		
		Button btn = getView(R.id.btnOrderCommit);
		btn.setVisibility(View.INVISIBLE);
		btn.setOnClickListener(this);
		switch (data.status) {
		case 2:
			if(data.order_type == 2)
				tvStatus.setText("等待上菜中");
			else
				btn.setVisibility(View.VISIBLE);
			
			if(data.pay_type != 4)
				btn.setText("退款");
			else
				btn.setText("取消订单");
			break;

		case 3:
			btn.setVisibility(View.VISIBLE);
			btn.setText("确认收货");
			break;

		case 4:
			btn.setVisibility(View.VISIBLE);
			btn.setText("取消订单");
			break;

		case 5:
			btn.setVisibility(View.VISIBLE);
			btn.setText("确认收货");
			break;

		case 6:
			btn.setVisibility(View.VISIBLE);
			btn.setText("评价");
			break;
			
		case 0:
		case 7:
			btn.setVisibility(View.VISIBLE);
			btn.setText("删除");
			break;
			
		case 15:
			if(data.order_type == 2) {
				btn.setVisibility(View.VISIBLE);
				btn.setText("确认下单");
				
				//退款
				getView(R.id.btnOrderCancel).setVisibility(View.VISIBLE);
				getView(R.id.btnOrderCancel).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						refund();
					}
				});
			}
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.llOrderShopDetail:
			if(orderDetail.shop_id > 0) {
				Intent shop = new Intent(this, ShopDetailActivity.class);
				shop.putExtra("shopId", orderDetail.shop_id);
				startActivity(shop);
			}
			break;
			
		case R.id.btnViewOrderStatus:
			Intent status = new Intent(this, OrderStatusActivity.class);
			status.putExtra("orderId", orderId);
			startActivity(status);
			break;
			
		case R.id.btnOrderCommit:
			orderOperate();
			break;
		}
	}
	
	public void orderOperate() {
		switch(orderDetail.status) {
		case 2:
			refund();
			break;

		case 3:
			receive();
			break;

		case 4:
			cancelOrder();
			break;

		case 5:
			receive();
			break;

		case 6:
			TransferTempDataUtil.getInstance().setData(orderDetail.order_details_list);
			Intent it = new Intent(this, CommentOrderActivity.class);
			it.putExtra("orderId", orderId);
			it.putExtra("shopId", orderDetail.shop_id);
			startActivityForResult(it, REQ_COMMENT_ORDER);
			break;
			
		case 0:
		case 7:
			delete();
			break;
			
		case 15:
			if(orderDetail.order_type == 2) {
				confirm();
			}
			break;
		}
	}
	
	public void cancelOrder() {
		new AlertDialog.Builder(this).setMessage("是否确定取消该订单?")
			.setNegativeButton("否", null)
			.setPositiveButton("是", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					updateOrder(0);
				}
			})
			.show();
	}
	
	public void refund() {
		new AlertDialog.Builder(this).setMessage("确定该操作吗?")
		.setNegativeButton("否", null)
		.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				updateOrder(8);
			}
		})
		.show();
	}
	
	public void receive() {
		new AlertDialog.Builder(this).setMessage("是否确认收货?")
		.setNegativeButton("否", null)
		.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				updateOrder(6);
			}
		})
		.show();
	}
	
	public void delete() {
		new AlertDialog.Builder(this).setMessage("是否确定删除该订单?")
		.setNegativeButton("否", null)
		.setPositiveButton("是", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				updateOrder(-1);
			}
		})
		.show();
	}
	
	private EditText edDestNo;
	
	public void confirm() {
		edDestNo = new EditText(this);
		edDestNo.setBackgroundResource(R.drawable.bg_arc_white);
		edDestNo.setHint("请填写桌号");
		edDestNo.setText(orderDetail.desk_no);
		
		new AlertDialog.Builder(this).setMessage("用户一旦点击确认下单,商家就会开始上菜,局时需要退款需要自行和商家沟通")
		.setView(edDestNo)
		.setNegativeButton("取消", null)
		.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(TextUtils.isEmpty(edDestNo.getText().toString())) {
					T.showShort(OrderDetailActivity.this, "确认失败，桌号为空");
					return;
				}
				updateOrder(2);
			}
		}).show();
	}
	
	public void updateOrder(final int status) {
		ProgressDialogUtil.showProgressDlg(OrderDetailActivity.this, "");
		UpdateOrderRequest req = new UpdateOrderRequest();
		req.order_id = orderId+"";
		req.user_id = UserInfoManager.getUserId(OrderDetailActivity.this)+"";
		req.status = status+"";
		if(orderDetail.order_type == 2 && edDestNo != null)
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
				T.showNetworkError(OrderDetailActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					T.showShort(OrderDetailActivity.this, "成功");
					setResult(RESULT_OK);
					if(status == -1) //删除
						finish();
					else
						loadData();
				} else {
					T.showShort(OrderDetailActivity.this, bean.result_desc);
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent arg2) {
		if(resCode==RESULT_OK && reqCode==REQ_COMMENT_ORDER) {
			loadData();
			setResult(RESULT_OK);
		}
	}
}
