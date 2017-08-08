//package com.shareshenghuo.app.user;
//
//import java.io.UnsupportedEncodingException;
//import java.util.List;
//
//import org.apache.http.entity.StringEntity;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.shareshenghuo.app.user.R;
//import com.shareshenghuo.app.user.adapter.OrderProdAdapter;
//import CityManager;
//import UserInfoManager;
//import com.shareshenghuo.app.user.network.bean.AddrInfo;
//import com.shareshenghuo.app.user.network.bean.CouponInfo;
//import com.shareshenghuo.app.user.network.bean.OrderDetailInfo;
//import com.shareshenghuo.app.user.network.bean.OrderProd;
//import com.shareshenghuo.app.user.network.bean.OrderProdInfo;
//import com.shareshenghuo.app.user.network.bean.UserInfo;
//import com.shareshenghuo.app.user.network.request.CheckProdZhekouRequest;
//import com.shareshenghuo.app.user.network.request.UpdateOrderRequest;
//import UpdateUserInfoRequest;
//import com.shareshenghuo.app.user.network.response.BaseResponse;
//import com.shareshenghuo.app.user.network.response.DefaultAddrResponse;
//import com.shareshenghuo.app.user.network.response.OrderDetailResponse;
//import com.shareshenghuo.app.user.network.response.OrderProdResponse;
//import Api;
//import Arith;
//import com.shareshenghuo.app.user.util.ProgressDialogUtil;
//import com.shareshenghuo.app.user.util.T;
//import TransferTempDataUtil;
//import com.shareshenghuo.app.user.util.Utility;
//import com.shareshenghuo.app.user.util.ViewUtil;
//import com.google.gson.Gson;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
//
///**
// * @author hang
// * 订单确认
// */
//public class SubmitOrderActivity extends BaseTopActivity implements OnClickListener {
//	
//	private static final int REQ_PICK_ADDR = 0x101;
//	private static final int REQ_PICK_COUPON = 0x102;
//	
//	private LinearLayout llDeskNo;
//	private LinearLayout llSnippet;
//	private TextView tvRealName;
//	private TextView tvMobile;
//	private EditText edDeskNo;
//	private EditText edRemarks;
//	private ListView lvProd;
//	private Button btnCancel;
//	private Button btnCommit;
//	
//	private int orderId;	//0 待创建订单     非0 待支付订单
//	private int orderType;	//1外卖   2到店消费
//	
//	private int shopId;
//	private List<OrderProdInfo> prodList;
//	private OrderDetailInfo orderDetailInfo;
//	
//	private AddrInfo addrInfo = new AddrInfo();
//	private CouponInfo couponInfo = new CouponInfo();
//	
//	private double totalFee;
//	private double realFee;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_submit_order);
//		initData();
//		initView();
//		loadData();
//	}
//	
//	public void initData() {
//		orderId = getIntent().getIntExtra("orderId", 0);
//		orderType = getIntent().getIntExtra("orderType", 1);
//		shopId = getIntent().getIntExtra("shopId", 0);
//		prodList = (List<OrderProdInfo>) TransferTempDataUtil.getInstance().getData();
//		TransferTempDataUtil.getInstance().recycle();
//	}
//	
//	public void initView() {
//		initTopBar("订单确认");
//		llDeskNo = getView(R.id.llDeskNo);
//		llSnippet = getView(R.id.llSnippet);
//		tvRealName = getView(R.id.tvRealName);
//		tvMobile = getView(R.id.tvMobile);
//		edDeskNo = getView(R.id.edDeskNo);
//		edRemarks = getView(R.id.edOrderRemarks);
//		lvProd = getView(R.id.lvProd);
//		btnCancel = getView(R.id.btnOrderCancel);
//		btnCommit = getView(R.id.btnOrderCommit);
//		
//		getView(R.id.llOrderCoupon).setOnClickListener(this);
//		btnCancel.setOnClickListener(this);
//		btnCommit.setOnClickListener(this);
//		
//		btnCommit.setEnabled(false);
//	}
//	
//	public void loadData() {
//		if(orderId == 0) {
//			edRemarks.setEnabled(true);
//			btnCancel.setVisibility(View.INVISIBLE);
//			if(orderType == 1) {
//				llDeskNo.setVisibility(View.GONE);
//				getView(R.id.llAddr).setOnClickListener(this);
//				loadDefaultAddr();
//			} else {
//				llSnippet.setVisibility(View.GONE);
//				UserInfo userInfo = UserInfoManager.getUserInfo(this);
//				tvRealName.setText(TextUtils.isEmpty(userInfo.real_name)? userInfo.account:userInfo.real_name);
//				tvMobile.setText(userInfo.mobile);
//			}
//			checkProdZhekou();
//		} else {
//			edDeskNo.setEnabled(false);
//			loadOrderDetail();
//		}
//	}
//	
//	public void loadDefaultAddr() {
//		ProgressDialogUtil.dismissProgressDlg();
//		UpdateUserInfoRequest req = new UpdateUserInfoRequest();
//		req.user_id = UserInfoManager.getUserId(this)+"";
//		req.city_id = CityManager.getInstance(this).getCityId()+"";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_ADDR_DEFAULT, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(SubmitOrderActivity.this);
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				DefaultAddrResponse bean = new Gson().fromJson(resp.result, DefaultAddrResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					updateAddrInfo(bean.data);
//				}
//			}
//		});
//	}
//	
//	public void loadOrderDetail() {
//		ProgressDialogUtil.showProgressDlg(this, "加载数据");
//		UpdateOrderRequest req = new UpdateOrderRequest();
//		req.order_id = orderId+"";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_ORDER_DETAIL, params, new RequestCallBack<String>() {
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				OrderDetailResponse bean = new Gson().fromJson(resp.result, OrderDetailResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					orderDetailInfo = bean.data;
//					if(orderDetailInfo.is_timeout == 1)
//						checkProdZhekou();
//					else {
//						btnCommit.setEnabled(true);
//						
//						tvRealName.setText(orderDetailInfo.real_name);
//						tvMobile.setText(orderDetailInfo.mobile);
//						
//						if(orderType == 1) {
//							llDeskNo.setVisibility(View.GONE);
//							setText(R.id.tvSnippet, orderDetailInfo.city_name+orderDetailInfo.area_name+orderDetailInfo.address);
//						} else {
//							llSnippet.setVisibility(View.GONE);
//							edDeskNo.setText(orderDetailInfo.desk_no);
//						}
//						
//						if(orderDetailInfo.coupon_id != 0) {
//							couponInfo.id = orderDetailInfo.coupon_id;
//							couponInfo.coupon_name = orderDetailInfo.coupon_name;
//							couponInfo.coupon_fee = orderDetailInfo.coupon_fee;
//							setText(R.id.tvOrderCoupon, orderDetailInfo.coupon_name+" "+orderDetailInfo.coupon_fee+"元");
//							setText(R.id.tvOrderCouponFee, "¥"+orderDetailInfo.coupon_fee);
//						}
//						
//						updateProdList(orderDetailInfo.order_details_list);
//					}
//				} else {
//					T.showShort(SubmitOrderActivity.this, bean.result_desc);
//				}
//			}
//			
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(SubmitOrderActivity.this);
//			}
//		});
//	}
//	
//	public void updateAddrInfo(AddrInfo data) {
//		if(data == null)
//			return;
//		
//		this.addrInfo = data;
//		tvRealName.setText(addrInfo.real_name);
//		tvMobile.setText(addrInfo.mobile);
//		setText(R.id.tvSnippet, addrInfo.city_name+addrInfo.area_name+addrInfo.address);
//	}
//	
//	public void updateCouponInfo(CouponInfo data) {
//		this.couponInfo = data;
//		setText(R.id.tvOrderCoupon, couponInfo.coupon_name+" "+couponInfo.coupon_fee+"元");
//		setText(R.id.tvOrderCouponFee, "¥"+couponInfo.coupon_fee);
//		checkProdZhekou();
//	}
//	
//	public void updateProdList(List<OrderProdInfo> list) {
//		this.prodList = list;
//		lvProd.setAdapter(new OrderProdAdapter(this, list));
//		Utility.setListViewHeightBasedOnChildren(lvProd);
//		
//		totalFee = 0;
//		for(OrderProdInfo item : list) {
//			totalFee = Arith.add(totalFee, item.zhe_kou_price);
//		}
//		realFee = Arith.sub(totalFee, couponInfo.coupon_fee);
//		if(realFee < 0)
//			realFee = 0.01;
//		
//		setText(R.id.tvOrderTotalFee, "¥"+totalFee);
//		setText(R.id.tvOrderRealFee, "¥"+realFee);
//	}
//	
//	public void checkProdZhekou() {
//		ProgressDialogUtil.showProgressDlg(this, "");
//		CheckProdZhekouRequest req = new CheckProdZhekouRequest();
//		req.user_id = UserInfoManager.getUserId(this)+"";
//		req.shop_id = shopId+"";
//		req.coupon_id = couponInfo.id+"";
//		if(orderId!=0 && orderDetailInfo!=null && orderDetailInfo.is_timeout==1) {
//			req.order_id = orderId+"";
//		} else {
//			for(OrderProdInfo item : prodList) {
//				req.addProd(item.product_id, 
//						item.product_format_id, 
//						item.child_format_id, 
//						item.product_count, 
//						item.product_name, 
//						item.thum_photo, 
//						item.per_price);
//			}
//		}
//		RequestParams params = new RequestParams("utf-8");
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_CHECK_PROD_ZHEKOU, params, new RequestCallBack<String>() {
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				btnCommit.setEnabled(true);
//				OrderProdResponse bean = new Gson().fromJson(resp.result, OrderProdResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					updateProdList(bean.data);
//				} else {
//					T.showShort(SubmitOrderActivity.this, bean.result_desc);
//				}
//			}
//			
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(SubmitOrderActivity.this);
//			}
//		});
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch(v.getId()) {
//		case R.id.llAddr:
//			Intent addr = new Intent(this, AddrManageActivity.class);
//			addr.putExtra("click", true);
//			startActivityForResult(addr, REQ_PICK_ADDR);
//			break;
//			
//		case R.id.llOrderCoupon:
//			Intent coupon = new Intent(this, CouponListActivity.class);
//			coupon.putExtra("shopId", shopId);
//			coupon.putExtra("orderPrice", totalFee);
//			startActivityForResult(coupon, REQ_PICK_COUPON);
//			break;
//			
//		case R.id.btnOrderCancel:
//			if(orderId > 0)
//				cancelOrder();
//			break;
//			
//		case R.id.btnOrderCommit:
//			if(orderId == 0) {
//				commitOrder();
//			} else {
//				if(orderDetailInfo == null) {
//					T.showShort(this, "订单数据加载失败，请重新加载");
//					return;
//				}
//				toPayment(orderDetailInfo);
//			}
//			break;
//		}
//	}
//	
//	public void cancelOrder() {
//		new AlertDialog.Builder(this).setMessage("是否确定取消该订单?")
//		.setNegativeButton("否", null)
//		.setPositiveButton("是", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				ProgressDialogUtil.showProgressDlg(SubmitOrderActivity.this, "");
//				UpdateOrderRequest req = new UpdateOrderRequest();
//				req.order_id = orderId+"";
//				req.user_id = UserInfoManager.getUserId(SubmitOrderActivity.this)+"";
//				req.status = "0";
//				RequestParams params = new RequestParams();
//				try {
//					params.setBodyEntity(new StringEntity(req.toJson()));
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
//				new HttpUtils().send(HttpMethod.POST, Api.URL_UPDATE_ORDER, params, new RequestCallBack<String>() {
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						ProgressDialogUtil.dismissProgressDlg();
//						T.showNetworkError(SubmitOrderActivity.this);
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> resp) {
//						ProgressDialogUtil.dismissProgressDlg();
//						BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
//						if(Api.SUCCEED == bean.result_code) {
//							T.showShort(SubmitOrderActivity.this, "成功");
//							setResult(RESULT_OK);
//							finish();
//						} else {
//							T.showShort(SubmitOrderActivity.this, bean.result_desc);
//						}
//					}
//				});
//			}
//		})
//		.show();
//	}
//	
//	public void commitOrder() {
//		if(orderType == 1 && addrInfo.id == 0) {
//			T.showShort(this, "请选择地址");
//			return;
//		}
//		
//		CheckProdZhekouRequest req = new CheckProdZhekouRequest();
//		req.order_type = orderType+"";
//		req.user_id = UserInfoManager.getUserId(this)+"";
//		req.shop_id = shopId+"";
//		if(orderType == 1) {
//			req.city_id = addrInfo.city_id+"";
//			req.city_name = addrInfo.city_name+"";
//			req.area_id = addrInfo.area_id+"";
//			req.area_name = addrInfo.area_name;
//			req.address = addrInfo.address;
//		} else {
//			req.city_id = CityManager.getInstance(this).getCityId()+"";
//			req.desk_no = edDeskNo.getText().toString();
//		}
//		req.real_name = tvRealName.getText().toString();
//		req.mobile = tvMobile.getText().toString();
//		req.coupon_id = couponInfo.id+"";
//		req.coupon_fee = couponInfo.coupon_fee+"";
//		req.order_all_fee = totalFee+"";
//		req.order_real_fee = realFee+"";
//		req.remarks = edRemarks.getText().toString();
//		int prodAllCount = 0;
//		for(OrderProdInfo item : prodList) {
//			OrderProd prod = new OrderProd();
//			prod.product_id = item.product_id+"";
//			prod.product_count = item.product_count+"";
//			prod.product_name = item.product_name;
//			prod.product_photo = item.thum_photo;
//			prod.format_id = item.product_format_id+"";
//			prod.child_format_id = item.child_format_id+"";
//			prod.per_price = item.per_price+"";
//			prod.level_desc = item.level_desc;
//			prod.zhe_kou = item.zhe_kou+"";
//			prod.zhe_kou_price = item.zhe_kou_price+"";
//			req.product_list.add(prod);
//			
//			prodAllCount += item.product_count;
//		}
//		req.all_count = prodAllCount+"";
//		
//		ProgressDialogUtil.showProgressDlg(this, "提交订单");
//		RequestParams params = new RequestParams("utf-8");
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_ADD_ORDER, params, new RequestCallBack<String>() {
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				OrderDetailResponse bean = new Gson().fromJson(resp.result, OrderDetailResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					T.showShort(SubmitOrderActivity.this, "订单确认成功,可在我的订单中查看");
//					toPayment(bean.data);
//				} else {
//					T.showShort(SubmitOrderActivity.this, bean.result_desc);
//				}
//			}
//			
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(SubmitOrderActivity.this);
//			}
//		});
//	}
//	
//	public void toPayment(OrderDetailInfo data) {
//		Intent it = new Intent(SubmitOrderActivity.this, PaymentActivity.class);
//		it.putExtra("orderId", data.id);
//		it.putExtra("orderNo", data.order_no);
//		it.putExtra("realFee", realFee);
//		startActivity(it);
//		setResult(RESULT_OK);
//		finish();
//	}
//
//	@Override
//	protected void onActivityResult(int reqCode, int resCode, Intent data) {
//		if(resCode == RESULT_OK) {
//			switch(reqCode) {
//			case REQ_PICK_ADDR:
//				updateAddrInfo((AddrInfo) data.getSerializableExtra("addrInfo"));
//				break;
//				
//			case REQ_PICK_COUPON:
//				updateCouponInfo((CouponInfo) data.getSerializableExtra("couponInfo"));
//				break;
//			}
//		}
//	}
//}
