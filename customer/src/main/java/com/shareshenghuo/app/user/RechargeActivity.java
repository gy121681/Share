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
//import android.widget.EditText;
//import android.widget.RadioButton;
//
//import com.shareshenghuo.app.user.R;
//import com.shareshenghuo.app.user.alipay.AliPayHelper;
//import com.shareshenghuo.app.user.alipay.AliPayHelper.PayCompleteCallback;
//import UserInfoManager;
//import com.shareshenghuo.app.user.network.bean.UserInfo;
//import com.shareshenghuo.app.user.network.bean.WebLoadActivity;
//import AddPayLogRequest;
//import RechargeResponse;
//import Api;
//import Arith;
//import com.shareshenghuo.app.user.util.ProgressDialogUtil;
//import com.shareshenghuo.app.user.util.T;
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
// * 钱包充值
// */
//public class RechargeActivity extends BaseTopActivity implements PayCompleteCallback {
//	
//	private static final int REQ_UNION_PAY = 0x100;
//	
//	private EditText edMoney;
//	private RadioButton rbAliPay;
//	private RadioButton rbWxPay;
//	private RadioButton rbBank;
//	
//	private AliPayHelper aliPay;
//	private WxPayHelper wxPay;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_recharge);
//		init();
//	}
//	
//	public void init() {
//		initTopBar("充值");
//		edMoney = getView(R.id.edRechargeMoney);
//		rbAliPay = getView(R.id.rbRechargeAlipay);
//		rbWxPay = getView(R.id.rbRechargeWxPay);
//		rbBank = getView(R.id.rbRechargeBank);
//		
//		edMoney.setText(getIntent().getStringExtra("fee"));
//		
//		findViewById(R.id.btnRecharge).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				if(ViewUtil.checkEditEmpty(edMoney, "请输入充值金额"))
//					return;
//				addPayLog();
//			}
//		});
//		
//		aliPay = new AliPayHelper(this);
//		aliPay.setPayCallback(this);
//		wxPay = new WxPayHelper(this);
//	}
//	
//	private double rechargeMoney = 0;
//	
//	public void addPayLog() {
//		ProgressDialogUtil.showProgressDlg(this, "");
//		final AddPayLogRequest req = new AddPayLogRequest();
//		req.user_id = UserInfoManager.getUserId(this)+"";
//		req.total_fee = edMoney.getText().toString();
//		req.free_fee = "0";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_ADD_PAY_LOG, params, new RequestCallBack<String>() {
//
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(RechargeActivity.this);
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				RechargeResponse bean = new Gson().fromJson(resp.result, RechargeResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					rechargeMoney = Double.parseDouble(req.total_fee);
//					if(rbAliPay.isChecked())
//						aliPay.pay(bean.data.order_no, "充值", bean.data.order_no, rechargeMoney);
//					else if(rbWxPay.isChecked())
//						wxPay.payFromUrl("充值", bean.data.order_no, rechargeMoney, UserInfoManager.getUserId(RechargeActivity.this), 2);
//					else if(rbBank.isChecked()) {
//						Intent it = new Intent(RechargeActivity.this, WebLoadActivity.class);
//						it.putExtra("title", "充值");
//						StringBuilder sb = new StringBuilder(Api.URL_UNION_PAY);
//						sb.append("?order_id=").append(bean.data.order_no)
//							.append("&order_fee=").append(rechargeMoney);
//						it.putExtra("url", sb.toString());
//						startActivityForResult(it, REQ_UNION_PAY);
//					}
//				} else {
//					T.showShort(RechargeActivity.this, bean.result_desc);
//				}
//			}
//		});
//	}
//
//	@Override
//	public void paySuccess() {
//		T.showShort(this, "充值成功");
//		UserInfoManager.updateMoney(this, rechargeMoney);
//		finish();
//	}
//
//	@Override
//	public void payFailure() {
//		
//	}
//	
//	@Override
//	protected void onActivityResult(int reqCode, int resCode, Intent data) {
//		if(resCode == RESULT_OK) {
//			switch(reqCode) {
//			case REQ_UNION_PAY:
//				UserInfoManager.updateMoney(this, rechargeMoney);
//				finish();
//				break;
//			}
//		}
//	}
//}
