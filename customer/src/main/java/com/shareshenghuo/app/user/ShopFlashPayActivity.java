//package com.shareshenghuo.app.user;
//
//import java.io.UnsupportedEncodingException;
//
//import net.sourceforge.WxPayHelper;
//
//import org.apache.http.entity.StringEntity;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.shareshenghuo.app.user.R;
//import com.shareshenghuo.app.user.alipay.AliPayHelper;
//import com.shareshenghuo.app.user.alipay.AliPayHelper.PayCompleteCallback;
//import UserInfoManager;
//import com.shareshenghuo.app.user.network.bean.ActivInfo;
//import com.shareshenghuo.app.user.network.bean.ShopInfo;
//import com.shareshenghuo.app.user.network.bean.WebLoadActivity;
//import com.shareshenghuo.app.user.network.request.ShopQuickPayRequest;
//import com.shareshenghuo.app.user.network.request.ValidatePayPwdRequest;
//import com.shareshenghuo.app.user.network.response.BaseResponse;
//import StringResponse;
//import Api;
//import Arith;
//import com.shareshenghuo.app.user.util.DateUtil;
//import com.shareshenghuo.app.user.util.MD5Utils;
//import com.shareshenghuo.app.user.util.ProgressDialogUtil;
//import com.shareshenghuo.app.user.util.T;
//import com.shareshenghuo.app.user.util.ViewUtil;
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
///**
// * @author hang
// * 店铺闪付
// */
//public class ShopFlashPayActivity extends BaseTopActivity implements PayCompleteCallback {
//	
//	private static final int REQ_UNION_PAY = 0x100;
//	
//	private EditText edTotalFee;
//	private TextView tvRealFee;
//	
//	private ShopInfo shopInfo;
//	private ActivInfo activInfo;
//	
//	private AliPayHelper aliPay;
//	private WxPayHelper wxPay;
//	
//	private String realFee;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_shop_flash_pay);
//		init();
//	}
//	
//	public void init() {
//		shopInfo = (ShopInfo) getIntent().getSerializableExtra("shopInfo");
//		activInfo = (ActivInfo) getIntent().getSerializableExtra("activInfo");
//		
//		initTopBar(shopInfo.shop_name);
//		
//		setText(R.id.tvFlashPayCoupon, "满"+shopInfo.lowest_fee+"减"+shopInfo.coupon_fee);
//		if(activInfo != null && activInfo.id!=0) {
//			findViewById(R.id.llActivContainer).setVisibility(View.VISIBLE);
//			setText(R.id.tvFlashPayActivContent, activInfo.active_title);
//			setText(R.id.tvFlashPayActivDate, DateUtil.getTime(activInfo.effective_start_time,2)
//					+"至"+ DateUtil.getTime(activInfo.effective_end_time,2));
//		}
//		
//		edTotalFee = getView(R.id.edFlashPayTotalFee);
//		tvRealFee = getView(R.id.tvFlashPayRealFee);
//		
//		edTotalFee.addTextChangedListener(new TextWatcher() {
//			@Override
//			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//				realFee = arg0.toString();
//				try  {
//					double fee = Double.parseDouble(arg0.toString());
//					if(fee >= shopInfo.lowest_fee)
//						realFee = Arith.sub(fee, shopInfo.coupon_fee)+"";
//				}catch(Exception e) {
//					e.printStackTrace();
//				}
//				tvRealFee.setText("¥"+realFee);
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//			}
//			
//			@Override
//			public void afterTextChanged(Editable arg0) {
//			}
//		});
//		
//		getView(R.id.btnFlashPay).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				if(ViewUtil.checkEditEmpty(edTotalFee, "请输入金额"))
//					return;
//				showPayTypeDlg();
//			}
//		});
//		
//		aliPay = new AliPayHelper(this);
//		aliPay.setPayCallback(this);
//		wxPay = new WxPayHelper(this);
//	}
//	
//	private String[] items = {"余额支付", "微信支付", "支付宝支付"};
//	
//	public void showPayTypeDlg() {
//		new AlertDialog.Builder(this).setTitle("选择支付方式")
//			.setItems(items, new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface arg0, final int which) {
////					if(which == 0) {
////						//余额支付
////						PayPwdWindow window = new PayPwdWindow(ShopFlashPayActivity.this);
////						window.tips = "请输入6位数字支付密码";
////						window.setPayPwdCallback(new PayPwdCallback() {
////							@Override
////							public void inputPayPwd(String payPwd) {
////								validatePayPwd(payPwd, Double.parseDouble(edTotalFee.getText().toString()), which+1);
////							}
////						});
////						window.showAtBottom();
////					} else {
//						pay(Double.parseDouble(edTotalFee.getText().toString()), which+1);
////					}
//				}
//			})
//			.show();
//	}
//	
//	public void validatePayPwd(String payPwd, final double fee, final int payType) {
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
//					pay(fee, payType);
//				} else {
//					ProgressDialogUtil.dismissProgressDlg();
//					T.showShort(ShopFlashPayActivity.this, bean.result_desc);
//				}
//			}
//			
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(ShopFlashPayActivity.this);
//			}
//		});
//	}
//	
//	public void pay(final double fee, final int payType) {
//		ProgressDialogUtil.showProgressDlg(this, "");
//		ShopQuickPayRequest req = new ShopQuickPayRequest();
//		req.user_id = UserInfoManager.getUserId(this)+"";
//		req.shop_id = shopInfo.id+"";
//		req.pay_all_fee = fee+"";
//		req.pay_real_fee = realFee;
//		req.pay_type = payType+"";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_SHOP_QUICK_PAY, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(ShopFlashPayActivity.this);
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				StringResponse bean = new Gson().fromJson(resp.result, StringResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					if(payType == 1) {
//						T.showShort(ShopFlashPayActivity.this, "支付成功");
//						UserInfoManager.updateMoney(ShopFlashPayActivity.this, -fee);
//						finish();
//					} else if(payType == 2) {
//						wxPay.payFromUrl(shopInfo.shop_name+"闪付", bean.data, fee, UserInfoManager.getUserId(ShopFlashPayActivity.this), 3);
//					} else if(payType == 3) {
//						aliPay.pay(bean.data, shopInfo.shop_name, "闪付", fee);
//					} else if(payType == 4) {
//						//银联支付
//						Intent it = new Intent(ShopFlashPayActivity.this, WebLoadActivity.class);
//						it.putExtra("title", "闪付");
//						StringBuilder sb = new StringBuilder(Api.URL_UNION_PAY);
//						sb.append("?order_id=").append(bean.data)
//							.append("&order_fee=").append(fee);
//						it.putExtra("url", sb.toString());
//						startActivityForResult(it, REQ_UNION_PAY);
//					}
//				} else {
//					T.showShort(ShopFlashPayActivity.this, bean.result_desc);
//				}
//			}
//		});
//	}
//
//	@Override
//	public void paySuccess() {
//		T.showShort(this, "支付成功");
//		finish();
//	}
//
//	@Override
//	public void payFailure() {
//	}
//	
//	@Override
//	protected void onActivityResult(int reqCode, int resCode, Intent data) {
//		if(resCode == RESULT_OK) {
//			switch(reqCode) {
//			case REQ_UNION_PAY:
//				finish();
//				break;
//			}
//		}
//	}
//}
