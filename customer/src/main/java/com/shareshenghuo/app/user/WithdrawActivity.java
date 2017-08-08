//package com.shareshenghuo.app.user;
//
//import java.io.UnsupportedEncodingException;
//
//import org.apache.http.entity.StringEntity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.EditText;
//
//import com.google.gson.Gson;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
//import com.shareshenghuo.app.user.R;
//import UserInfoManager;
//import com.shareshenghuo.app.user.network.bean.UserInfo;
//import com.shareshenghuo.app.user.network.request.ValidatePayPwdRequest;
//import WithdrawRequest;
//import com.shareshenghuo.app.user.network.response.BaseResponse;
//import Api;
//import com.shareshenghuo.app.user.util.MD5Utils;
//import com.shareshenghuo.app.user.util.ProgressDialogUtil;
//import com.shareshenghuo.app.user.util.T;
//import com.shareshenghuo.app.user.util.ViewUtil;
//import com.shareshenghuo.app.user.widget.dialog.PayPwdWindow;
//import com.shareshenghuo.app.user.widget.dialog.PayPwdWindow.PayPwdCallback;
//
//public class WithdrawActivity extends BaseTopActivity {
//	
//	private EditText edAccount;
//	private EditText edMoney;
//	
//	private int source;	  // 1支付宝 2微信
//	
//	private UserInfo userInfo;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_withdraw);
//		init();
//	}
//	
//	public void init() {
//		source = getIntent().getIntExtra("source", 1);
//		userInfo = UserInfoManager.getUserInfo(this);
//		
//		initTopBar("零钱提现");
//		edAccount = getView(R.id.edWithdrawAccount);
//		edMoney = getView(R.id.edWithdrawMoney);
//		
//		setText(R.id.tvWithdrawSource, source==1? "支付宝账号：":"微信账号");
//		setText(R.id.tvWithdrawMoney, userInfo.money+"");
//		
//		findViewById(R.id.btnWithdraw).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				withdraw();
//			}
//		});
//	}
//	
//	public void withdraw() {
//		if(ViewUtil.checkEditEmpty(edMoney, "请输入提现金额") || ViewUtil.checkEditEmpty(edAccount, "请输入账号"))
//			return;
//		
//		double money = Double.parseDouble(edMoney.getText().toString());
//		if(money > userInfo.money) {
//			T.showShort(this, "提现金额大于账户余额");
//			return;
//		}
//		
//		PayPwdWindow window = new PayPwdWindow(this);
//		window.tips = "请输入6位数字支付密码";
//		window.setPayPwdCallback(new PayPwdCallback() {
//			@Override
//			public void inputPayPwd(String payPwd) {
//				validatePayPwd(payPwd);
//			}
//		});
//		window.showAtBottom();
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
//				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					submit();
//				} else {
//					ProgressDialogUtil.dismissProgressDlg();
//					T.showShort(WithdrawActivity.this, bean.result_desc);
//				}
//			}
//			
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(WithdrawActivity.this);
//			}
//		});
//	}
//	
//	public void submit() {
//		final WithdrawRequest req = new WithdrawRequest();
//		req.user_id = UserInfoManager.getUserId(this)+"";
//		req.fee = edMoney.getText().toString();
//		req.alipay_account = edAccount.getText().toString();
//		req.user_type = "1";
//		req.source = source+"";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.URL_WITHDRAW, params, new RequestCallBack<String>() {
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
//					userInfo.money -= Double.parseDouble(req.fee);
//					UserInfoManager.saveUserInfo(WithdrawActivity.this, userInfo);
//					
//					Intent it = new Intent(WithdrawActivity.this, WithdrawResultActivity.class);
//					it.putExtra("source", source);
//					it.putExtra("account", req.alipay_account);
//					it.putExtra("money", req.fee);
//					startActivity(it);
//					finish();
//				} else {
//					T.showShort(WithdrawActivity.this, bean.result_desc);
//				}
//			}
//			
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(WithdrawActivity.this);
//			}
//		});
//	}
//}
