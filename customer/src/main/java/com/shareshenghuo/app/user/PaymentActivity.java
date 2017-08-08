//package com.shareshenghuo.app.user;
//
//import java.io.UnsupportedEncodingException;
//
//import net.sourceforge.WxPayHelper;
//
//import org.apache.http.entity.StringEntity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RadioGroup.OnCheckedChangeListener;
//
//import com.shareshenghuo.app.user.R;
//import com.shareshenghuo.app.user.alipay.AliPayHelper;
//import com.shareshenghuo.app.user.alipay.AliPayHelper.PayCompleteCallback;
//import UserInfoManager;
//import com.shareshenghuo.app.user.network.bean.UserInfo;
//import com.shareshenghuo.app.user.network.bean.WebLoadActivity;
//import com.shareshenghuo.app.user.network.request.UpdateOrderRequest;
//import com.shareshenghuo.app.user.network.request.ValidatePayPwdRequest;
//import com.shareshenghuo.app.user.network.response.BaseResponse;
//import Api;
//import com.shareshenghuo.app.user.receiver.PayWatcher;
//import com.shareshenghuo.app.user.receiver.PayWatcher.OnPayResultCallback;
//import com.shareshenghuo.app.user.util.MD5Utils;
//import com.shareshenghuo.app.user.util.ProgressDialogUtil;
//import com.shareshenghuo.app.user.util.T;
//import com.shareshenghuo.app.user.widget.dialog.PayPwdWindow;
//import com.shareshenghuo.app.user.widget.dialog.PayPwdWindow.PayPwdCallback;
//import com.google.gson.Gson;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
//
//public class PaymentActivity extends BaseTopActivity
//	implements OnCheckedChangeListener, OnClickListener, PayCompleteCallback, OnPayResultCallback {
//	
//	private static final int REQ_UNION_PAY = 0x100;
//	
//	private int orderId;
//	private String orderNo;
//	private double realFee;
//	
//	private int payType = 1;	// 1余额 2微信支付 3支付宝支付 4线下支付（现金支付）5银联
//	
//	private AliPayHelper aliPay;
//	private WxPayHelper wxPay;
//	
//	private PayWatcher payWatcher;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_payment);
//		init();
//	}
//	
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		payWatcher.stopWatch();
//	}
//
//	public void init() {
//		initTopBar("订单支付");
//		
//		UserInfo user = UserInfoManager.getUserInfo(this);
//		((RadioButton) findViewById(R.id.rbPayBalance)).setText("账户余额："+user.money);
//		
//		((RadioGroup) findViewById(R.id.rgPayType)).setOnCheckedChangeListener(this);
//		getView(R.id.btnPay).setOnClickListener(this);
//		
//		orderId = getIntent().getIntExtra("orderId", 0);
//		orderNo = getIntent().getStringExtra("orderNo");
//		realFee = getIntent().getDoubleExtra("realFee", 0);
//		
//		aliPay = new AliPayHelper(this);
//		aliPay.setPayCallback(this);
//		wxPay = new WxPayHelper(this);
//		
//		payWatcher = new PayWatcher(this);
//		payWatcher.setOnPayResultCallback(this);
//		payWatcher.startWatch();
//	}
//
//	@Override
//	public void onCheckedChanged(RadioGroup arg0, int id) {
//		switch(id) {
//		case R.id.rbPayBalance:
//			payType = 1;
//			break;
//			
//		case R.id.rbPayWx:
//			payType = 2;
//			break;
//			
//		case R.id.rbPayAli:
//			payType = 3;
//			break;
//			
//		case R.id.rbPayCash:
//			payType = 4;
//			break;
//			
//		case R.id.rbPayBank:
//			payType = 5;
//			break;
//		}
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch(v.getId()) {
//		case R.id.btnPay:
////			if(payType == 1) {
////				//余额支付
////				PayPwdWindow window = new PayPwdWindow(this);
////				window.tips = "请输入6位数字支付密码";
////				window.setPayPwdCallback(new PayPwdCallback() {
////					@Override
////					public void inputPayPwd(String payPwd) {
////						validatePayPwd(payPwd);
////					}
////				});
////				window.showAtBottom();
////				return;
////			}
//			pay();
//			break;
//		}
//	}
//	
//	public void validatePayPwd(String payPwd) {
//		ProgressDialogUtil.showProgressDlg(this, "");
//		ProgressDialogUtil.setCancelable(false);
//		ValidatePayPwdRequest req = new ValidatePayPwdRequest();
//		req.user_id = UserInfoManager.getUserId(this)+"";
//		for(int i=0; i<3; i++)
//			payPwd = MD5Utils.getMD5String(payPwd);
//		req.pay_password = payPwd;
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_VALIDATE_PAY_PWD, params, new RequestCallBack<String>() {
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					pay();
//				} else {
//					ProgressDialogUtil.dismissProgressDlg();
//					T.showShort(PaymentActivity.this, bean.result_desc);
//				}
//			}
//			
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(PaymentActivity.this);
//			}
//		});
//	}
//	
//	public void pay() {
//		ProgressDialogUtil.showProgressDlg(this, "");
//		UpdateOrderRequest req = new UpdateOrderRequest();
//		req.order_id = orderId+"";
//		req.user_id = UserInfoManager.getUserId(this)+"";
//		req.status = "1";
//		req.pay_type = payType+"";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_UPDATE_ORDER, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(PaymentActivity.this);
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					if(payType==1 || payType==4) {
//						T.showLong(PaymentActivity.this, "成功");
//						toOrderListActivity();
//					} else if(payType == 2) {
//						wxPay.payFromUrl(orderNo, orderNo, realFee, UserInfoManager.getUserId(PaymentActivity.this), 1);
//					} else if(payType == 3) {
//						aliPay.pay(orderNo, orderNo, "订单支付", realFee);
//					} else if(payType == 5) {
//						Intent it = new Intent(PaymentActivity.this, WebLoadActivity.class);
//						it.putExtra("title", "支付");
//						StringBuilder sb = new StringBuilder(Api.URL_UNION_PAY);
//						sb.append("?order_id=").append(orderNo)
//							.append("&order_fee=").append(realFee);
//						it.putExtra("url", sb.toString());
//						startActivityForResult(it, REQ_UNION_PAY);
//					}
//				} else {
//					T.showShort(PaymentActivity.this, bean.result_desc);
//				}
//			}
//		});
//	}
//
//	@Override
//	public void paySuccess() {
//		T.showShort(this, "支付成功");
//		toOrderListActivity();
//	}
//
//	@Override
//	public void payFailure() {
//		T.showShort(this, "支付失败");
//	}
//
//	@Override
//	public void onPayResult(int errCode, String errStr) {
//		if(errCode == 0)
//			toOrderListActivity();
//	}
//	
//	@Override
//	protected void onActivityResult(int reqCode, int resCode, Intent data) {
//		if(resCode == RESULT_OK) {
//			switch(reqCode) {
//			case REQ_UNION_PAY:
//				toOrderListActivity();
//				break;
//			}
//		}
//	}
//	
//	/**
//	 * 跳转订单列表界面
//	 */
//	public void toOrderListActivity() {
//		Intent it = new Intent(this, OrderDetailActivity.class);
//		it.putExtra("orderId", orderId);
//		startActivity(it);
//		finish();
//	}
//}
