package com.td.qianhai.epay.oem;

import java.text.ParseException;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.CountDownButton;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.mpay.utils.Checkingroutine;
import com.td.qianhai.mpay.utils.Common;

/**
 * 找回支付密码
 * 
 * @author liangge
 * 
 */
public class RegetPayPwActivity extends BaseActivity {
	/** 手机号码、身份证号码、验证码 */
	private EditText etphone, etidcard, etverif;
	/** 获取验证码、提交找回密码 */
	private Button btnsubmit;
	private CountDownButton btngetverif;
	/** 用户手机、终端号 */
	private String userMobile;
	/** 提示 */
	private OneButtonDialogWarn warnDialog;

	/** 返回、title中间内容 */
	private TextView bt_Back, tv_title_contre;
	private String phone;

	private void showDialog(String msg) {
		warnDialog = new OneButtonDialogWarn(RegetPayPwActivity.this,
				R.style.CustomDialog, "提示", msg, "确定",new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						warnDialog.dismiss();
					}
				});
				
			
		warnDialog.show();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 10:
				showDialog("获取验证码失败");
				btngetverif.setEnabled(true);
				break;
			case 11:
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
				Toast.makeText(getApplicationContext(),map.get("RSPMSG").toString(),
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(getApplicationContext(),
//						map.get("RSPMSG").toString());
				if (map.get("RSPCOD").toString().equals("000000")) {
					Common.timing(btngetverif);
				} else if (map.get("RSPCOD").toString().equals("400002")) {
					btngetverif.setEnabled(true);
					showDialog("商户被禁用，请联系工作人员");
				} else if(map.get("RSPCOD").toString().equals("099999")) {
					btngetverif.setEnabled(true);
				}else{
					btngetverif.setEnabled(true);
				}

				break;
			case 1:
				showDialog("提交失败");
				break;
			case 2:
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map2 = (HashMap<String, Object>) msg.obj;
				Toast.makeText(getApplicationContext(),map2.get("RSPMSG").toString(),
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(getApplicationContext(),
//						map2.get("RSPMSG").toString());
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reget_pw_new1);
		AppContext.getInstance().addActivity(this);
		init();
	}

	public void init() {
//		phone  = ((AppContext)getApplication()).getMobile();
		phone = MyCacheUtil.getshared(this).getString("Mobile", "");
		etphone = (EditText) findViewById(R.id.et_reget_mobiles);
		etidcard = (EditText) findViewById(R.id.et_reget_cert_no);
		etverif = (EditText) findViewById(R.id.et_reget_verif_code);
		btngetverif = (CountDownButton) findViewById(R.id.btn_reget_get_verif_code);
		btnsubmit = (Button) findViewById(R.id.btn_reget_submit);
		tv_title_contre = (TextView) findViewById(R.id.tv_title_contre);
		bt_Back = (TextView) findViewById(R.id.bt_title_left);
		tv_title_contre.setText("找回支付密码");
		bt_Back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btngetverif.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 String mobile = etphone.getText().toString();
				 
				if(!mobile.equals(phone)){
					Toast.makeText(getApplicationContext(),"请输此用户手机号",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(getApplicationContext(), "请输此用户手机号");
					return;
				}

				if (mobile == null || (mobile != null && mobile.equals(""))) {
					Toast.makeText(getApplicationContext(),"请输入手机号码",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(getApplicationContext(), "请输入手机号码");
					return;
				} else if (mobile.length() != 11) {
					Toast.makeText(getApplicationContext(),"手机号码位数错误",
							Toast.LENGTH_SHORT).show();
//					ToastCustom
//							.showMessage(getApplicationContext(), "手机号码位数错误");
					return;
				}
				final String idcard = etidcard.getText().toString();

				if (idcard == null || (idcard != null && idcard.equals(""))) {
					Toast.makeText(getApplicationContext(),"请输入身份证号",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(RegetPayPwActivity.this, "请输入身份证号");
					return;
				}
				
//				String identifyID = null;
//				try {
//					identifyID = Checkingroutine.IDCard.IDCardValidate(idcard);
//					Log.v("test", "id:" + idcard);
//				} catch (ParseException e) {
//
//					e.printStackTrace();
//				}
//				if (!"".equals(identifyID)) {
//					Toast.makeText(getApplicationContext(),identifyID,
//							Toast.LENGTH_SHORT).show();
////					ToastCustom
////							.showMessage(RegetPayPwActivity.this, identifyID);
//					return;
//				}
                btngetverif.getVCode(mobile, null);
//                oldGetSmsCode(mobile, idcard);
			}
		});
		btnsubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				verifCommit();
			}
		});
		OnKeyListener keyListener = new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					verifCommit();
					return true;
				}
				return false;
			}
		};
		etverif.setOnKeyListener(keyListener);
	}

	private void oldGetSmsCode(String mobile, String idcard){
        btngetverif.setEnabled(false);
        // 验证码倒计时
        final String[] values = { HttpUrls.REG_PAY_PW_VCODE + "",
                mobile ,idcard};

        new Thread(new Runnable() {

            @Override
            public void run() {

                HashMap<String, Object> result = NetCommunicate
                        .getMidatc(
                                HttpUrls.REG_PAY_PW_VCODE, values);
                Message msg = new Message();
                if (result == null) {
                    msg.what = 10;
                    msg.obj = result;
                } else {
                    msg.what = 11;
                    msg.obj = result;
                }
                mHandler.sendMessage(msg);
            }
        }).start();
    }

	public void verifCommit() {
		userMobile = etphone.getText().toString();
		
		if(!userMobile.equals(phone)){
			Toast.makeText(getApplicationContext(),"请输此用户手机号",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(getApplicationContext(), "请输此用户手机号");
			return;
		}
		if (userMobile == null || (userMobile != null && userMobile.equals(""))) {
			Toast.makeText(getApplicationContext(),"请输入手机号",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(this, "请输入手机号");
			return;
		} else if (userMobile.length() != 11) {
			Toast.makeText(getApplicationContext(),"手机号码位数错误",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(this, "手机号码位数错误");
			return;
		}

		final String idcard = etidcard.getText().toString();
		if (idcard == null || (idcard != null && idcard.equals(""))) {
			Toast.makeText(getApplicationContext(),"请输入身份证号",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(RegetPayPwActivity.this, "请输入身份证号");
			return;
		}
//		String identifyID = null;
//		try {
//			identifyID = Checkingroutine.IDCard.IDCardValidate(idcard);
//			Log.v("test", "id:" + idcard);
//		} catch (ParseException e) {
//
//			e.printStackTrace();
//		}
//		if (!"".equals(identifyID)) {
//			Toast.makeText(getApplicationContext(),identifyID,
//					Toast.LENGTH_SHORT).show();
////			ToastCustom.showMessage(RegetPayPwActivity.this, identifyID);
//			return;
//		}

		String verif = etverif.getText().toString();
		if (verif == null || (verif != null && verif.equals(""))) {
			Toast.makeText(getApplicationContext(),"请输入验证码",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(this, "请输入验证码");
			return;
		}
		if (TextUtils.isEmpty(btngetverif.getVCodeId())) {
            toast("请获取验证码");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("mobile", userMobile);
        bundle.putString("msgId", btngetverif.getVCodeId());
        bundle.putString("msgCode", verif);
        Intent it = new Intent(RegetPayPwActivity.this,
                RegSetPayPassActivity.class);
        it.putExtras(bundle);
        startActivity(it);
        finish();
//        oldVerifyUser();
	}

    /**
     * 验证用户
     * @param verif
     * @param idcard
     */
	private void oldVerifyUser(String verif, String idcard){
        // 请求找回密码
        RegetPayPwTask task = new RegetPayPwTask();
        task.execute(HttpUrls.REG_PAY_PW_VCODE_VD + "", userMobile, verif,
                idcard);
    }


	/**
	 * 修改密码申请
	 * 
	 * @author liangge
	 * 
	 */
	class RegetPayPwTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			showLoadingDialog("正在努力加载...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			// TODO Auto-generated method stub
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate.getMidatc(HttpUrls.REG_PAY_PW_VCODE_VD,
					values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				System.out.println(Entity.RSPCOD);
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					Toast.makeText(getApplicationContext(),"身份验证成功",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(RegetPayPwActivity.this, "身份验证成功");
					Bundle bundle = new Bundle();
					bundle.putString("mobile", userMobile);
					Intent it = new Intent(RegetPayPwActivity.this,
							RegSetPayPassActivity.class);
					it.putExtras(bundle);
					startActivity(it);
					finish();
				} else {
					showDialog(result.get(Entity.RSPMSG).toString());
				}
			}
			super.onPostExecute(result);
		}
	}
}
