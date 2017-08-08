//package com.shareshenghuo.app.user;
//
//import java.io.UnsupportedEncodingException;
//
//import org.apache.http.entity.StringEntity;
//
//import UserInfoManager;
//import com.shareshenghuo.app.user.network.request.NumRequest;
//import com.shareshenghuo.app.user.network.response.NumResponse;
//import Api;
//import com.shareshenghuo.app.user.util.T;
//import com.shareshenghuo.app.user.util.Util;
//import com.shareshenghuo.app.user.util.ViewUtil;
//import com.shareshenghuo.app.user.widget.dialog.MyEditDialog;
//import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
//import TwoButtonDialog;
//import com.shareshenghuo.app.user.widget.dialog.onMyaddTextListener;
//import com.google.gson.Gson;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
//
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class TransferTextActivity extends BaseTopActivity {
//	private TextView tv_ok, edname;
//	private EditText eidcard, eidnum;
//	private MyEditDialog doubleWarnDialog1;
//	private TwoButtonDialog downloadDialog;
//	private int num = 0;
//
//	@Override
//	protected void onCreate(Bundle arg0) {
//		// TODO Auto-generated method stub
//		super.onCreate(arg0);
//		setContentView(R.layout.transfer_text_activity);
//		initview();
//		getStatisticsData();
//	}
//
//	private void initview() {
//		// TODO Auto-generated method stub
//		initTopBar("转账");
//		tv_ok = getView(R.id.tv_ok);
//		edname = getView(R.id.edname);
//		eidcard = getView(R.id.eidcard);
//		eidnum = getView(R.id.eidnum);
//	
//	
//		
//		tv_ok.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if(TextUtils.isEmpty(eidcard.getText())){
//					T.showShort(TransferTextActivity.this, "请输入手机号");
//					return;
//				}
//				if(TextUtils.isEmpty(eidnum.getText())){
//					T.showShort(TransferTextActivity.this, "请输入需转账的秀点");
//					return;
//				}
//				
//				
//				shwpaypwd();
//				
//			}
//
//		});
//	}
//	
//	public void getStatisticsData() {
//		NumRequest req = new NumRequest();
//		req.userId = UserInfoManager.getUserInfo(this).id+"";
//		req.userType = "1";
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.GETBUNBER, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				T.showNetworkError(TransferTextActivity.this);
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				NumResponse bean = new Gson().fromJson(resp.result, NumResponse.class);
//				if(Api.SUCCEED == bean.result_code) {
////					num = bean.data.filialMoney;
////					edname.setText( (double)bean.data.filialMoney/100+"");//Util.getIntnum(bean.data.filialMoney,false)+"");
////					tv_withdrawals_num.setText(Util.getIntnum(bean.data.filialMoney,false)+"");
//				}
//			}
//		});
//	}
//	
//	private void showresult() {
//		// TODO Auto-generated method stub
//		
//		downloadDialog = new TwoButtonDialog(this, R.style.CustomDialog,
//				"", "转账成功", "确定", "",true,new OnMyDialogClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						switch (v.getId()) {
//						case R.id.Button_OK:
//							
//							downloadDialog.dismiss();
//							break;
//						case R.id.Button_cancel:
//							downloadDialog.dismiss();
//						default:
//							break;
//						}
//					}
//				});
//		downloadDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode,
//					KeyEvent event) {
//				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
//					return true;
//				} else {
//					return true; // 默认返回 false
//				}
//			}
//		});
//		downloadDialog.setCanceledOnTouchOutside(false);
//		
//			downloadDialog.show();
//		
//	}
//	
//	protected void shwpaypwd() {
//		doubleWarnDialog1 = new MyEditDialog(TransferTextActivity.this,
//				R.style.loading_dialog, "兑换", "请输入支付密码", "确认", "取消", eidnum.getText().toString(),
//				new OnMyDialogClickListener() {
//
//					@Override
//					public void onClick(View v) {
//
//						switch (v.getId()) {
//						case R.id.btn_right:
//							doubleWarnDialog1.dismiss();
//							InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//							break;
//						case R.id.btn_left:
//							String paypwd = doubleWarnDialog1.getpaypwd();
//
//							if (paypwd == null || paypwd.equals("")) {
//								Toast.makeText(getApplicationContext(),"请输入支付密码",
//										Toast.LENGTH_SHORT).show();
//								return;
//							}
//							if (paypwd.length() < 6 || paypwd.length() > 15) {
//								return;
//							}
//							break;
//						default:
//							break;
//						}
//					}
//
//				},
//		new onMyaddTextListener() {
//			
//			@Override
//			public void refreshActivity(String paypwd) {
//				
//				if (paypwd == null || paypwd.equals("")) {
//					Toast.makeText(getApplicationContext(),"请输入支付密码",
//							Toast.LENGTH_SHORT).show();
//					return;
//				}
//				if (paypwd.length() < 6 || paypwd.length() > 15) {
//					return;
//				}
//				doubleWarnDialog1.dismiss();
//				String a = (double)num/100 -Double.parseDouble(eidnum.getText().toString())+"";
//				edname.setText(a);
//				eidnum.setText("");
//				showresult();
//				
//			}
//		});
//		doubleWarnDialog1.setCancelable(false);
//		doubleWarnDialog1.setCanceledOnTouchOutside(false);
//		doubleWarnDialog1.show();
//		
//	}
//}
