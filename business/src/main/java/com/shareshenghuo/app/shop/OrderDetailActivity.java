package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.OrderProdAdapter;
import com.shareshenghuo.app.shop.app.Constant;
import com.shareshenghuo.app.shop.network.bean.OrderInfo;
import com.shareshenghuo.app.shop.network.bean.OrderProdInfo;
import com.shareshenghuo.app.shop.network.request.UpdateOrderStatusRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.receiver.DataChangeWatcher;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.TransferTempDataUtil;
import com.shareshenghuo.app.shop.util.Utility;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class OrderDetailActivity extends BaseTopActivity implements OnClickListener {
	
	private ListView lvProd;
	Button btnReceive;
	Button btnRefuse;
	Button btnDeliver;
	Button btnRefundOK;
	Button btnRefundNO;
	Button btnSettle;
	
	private OrderInfo orderInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		initView();
	}
	
	public void initView() {
		initTopBar("订单详情");
		lvProd = (ListView) findViewById(R.id.lvOrderProd);
		btnReceive = getView(R.id.btnReceive);
		btnRefuse = getView(R.id.btnRefuse);
		btnDeliver = getView(R.id.btnDeliver);
		btnRefundOK = getView(R.id.btnRefundOK);
		btnRefundNO = getView(R.id.btnRefundNO);
		btnSettle = getView(R.id.btnSettle);
		
		orderInfo = (OrderInfo) TransferTempDataUtil.getInstance().getData();
		TransferTempDataUtil.getInstance().recycle();
		
		setText(R.id.tvOrderNo, orderInfo.order_no);
		setText(R.id.tvOrderDate, DateUtil.getTime(orderInfo.create_time, 0));
		String[] payType = {"", "余额支付", "微信支付", "支付宝支付", "现金支付"};
		setText(R.id.tvOrderPayType, payType[orderInfo.pay_type]);
		setText(R.id.tvOrderMobile, orderInfo.mobile);
		if(orderInfo.order_type == 1) {
			setText(R.id.tvOrderLabel, "地       址：");
			setText(R.id.tvOrderAddr, orderInfo.city_name+orderInfo.area_name+orderInfo.address);
		} else {
			setText(R.id.tvOrderLabel, "桌       号：");
			setText(R.id.tvOrderAddr, orderInfo.desk_no);
		}
		setText(R.id.tvOrderTotalFee, "¥"+orderInfo.order_all_fee);
		setText(R.id.tvOrderCouponFee, "¥"+orderInfo.coupon_fee);
		setText(R.id.tvOrderRealFee, "¥"+orderInfo.order_real_fee);
		
		lvProd.setAdapter(new OrderProdAdapter(this, orderInfo.order_details_list));
		Utility.setListViewHeightBasedOnChildren(lvProd);
		
		btnReceive.setOnClickListener(this);
		btnRefuse.setOnClickListener(this);
		btnDeliver.setOnClickListener(this);
		btnRefundOK.setOnClickListener(this);
		btnRefundNO.setOnClickListener(this);
		btnSettle.setOnClickListener(this);
		
		updateView();
	}
	
	public void updateView() {
		setText(R.id.tvOrderStatus, Constant.ORDER_STATUS[orderInfo.status]);
		
		switch(orderInfo.status) {
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnReceive:
			updateOrderStatus(4);
			break;

		case R.id.btnRefuse:
			updateOrderStatus(11);
			break;

		case R.id.btnDeliver:
			if(orderInfo.order_type == 1)
				updateOrderStatus(3);
			else
				updateOrderStatus(5);
			break;

		case R.id.btnRefundOK:
			updateOrderStatus(9);
			break;

		case R.id.btnRefundNO:
			updateOrderStatus(10);
			break;

		case R.id.btnSettle:
			updateOrderStatus(6);
			break;
		}
	}
	
	public void updateOrderStatus(final int status) {
		new AlertDialog.Builder(this).setMessage("是否确认操作？")
			.setPositiveButton("取消", null)
			.setNegativeButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					ProgressDialogUtil.showProgressDlg(OrderDetailActivity.this, "");
					UpdateOrderStatusRequest req = new UpdateOrderStatusRequest();
					req.order_id = orderInfo.id+"";
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
							T.showNetworkError(OrderDetailActivity.this);
						}

						@Override
						public void onSuccess(ResponseInfo<String> resp) {
							ProgressDialogUtil.dismissProgressDlg();
							BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
							if(Api.SUCCEED == bean.result_code) {
								T.showShort(OrderDetailActivity.this, "操作成功");
								orderInfo.status = status;
								updateView();
								DataChangeWatcher.sendDataChangeBroadcast(OrderDetailActivity.this);
							} else {
								T.showShort(OrderDetailActivity.this, bean.result_desc);
							}
						}
					});
				}
			})
			.show();
	}
}
