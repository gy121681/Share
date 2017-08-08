package com.td.qianhai.epay.oem.unlock;

import java.util.HashMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.BaseActivity;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.SetPayPassActivity;
import com.td.qianhai.epay.oem.UserActivity;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.GetImageUtil;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.unlock.LocusPassWordView.OnCompleteListener;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.fragmentmanager.FmMainActivity;

public class UnlockLoginActivity extends BaseActivity {
	private LocusPassWordView lpwv;
	private Toast toast;
	private String lognum;
	private OneButtonDialogWarn warnDialog;
	private int tagpwd = 0;
	private TextView tv_pro,tv_userlogin,toastTv,tv_phone;
	private int TAGPWD = 4;
	private boolean istagpwd = true;
	private String isrefresh;
	private boolean isjup = false;
	private String appid,userid;
	private ImageView img_head;
	public static boolean isjp = false;
	private String personpic;
	private Editor editor;
	private SharedPreferences share;
	private String islogin ;

	private void showToast(CharSequence message) {
		if (null == toast) {
			toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}

		toast.show();
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.logins_activity);
		editor = MyCacheUtil.setshared(UnlockLoginActivity.this);
		share = MyCacheUtil.getshared(UnlockLoginActivity.this);
		settitle();
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		personpic = MyCacheUtil.getshared(this).getString("PERSONPIC", "");
		String userName = share.getString("usermobile","");
		if(userName!=null&&!userName.equals("")){
//			userName
			
			String setphone = userName.substring(0,3);
			String getphone = userName.substring(userName.length()-4);
			tv_phone.setText(setphone+"****"+getphone);
		}
		img_head = (ImageView) findViewById(R.id.tv_head);
		setfirstimg();
		tv_pro = (TextView) findViewById(R.id.tv_prompt);
		lpwv = (LocusPassWordView) this.findViewById(R.id.mLocusPassWordView);
		lpwv.setOnCompleteListener(new OnCompleteListener() {
			@Override
			public void onComplete(String mPassword) {
				// 如果密码正确,则进入主页面。
				if (lpwv.verifyPassword(mPassword)) {
					isjp = true;
					
					if(istagpwd){
						if(islogin!=null&&islogin.equals("1")){
							AppContext.isunlock = false;
							finish();
						}else{
							LoginTask task = new LoginTask();
							// task.execute("199002", "13917830795","111111",
							// "18917114556");
							String pcsim = "11111111";
							String userName = share.getString("usermobile","");
							String passWord = share.getString("userpwd","");
							task.execute("199002", userName, passWord, pcsim, "", "2",appid,userid,"1",HttpUrls.APPNUM,"","","");
						}
					}
					lpwv.clearPassword();
				} else {
					tagpwd++;
					if(TAGPWD-tagpwd<=0){
						tv_pro.setText("密码次数超限!请用帐号密码登录后重设手势密码");
						lpwv.resetPassWord("");
						editor.putString("usermobile", "");
						editor.putString("userpwd", "");
						editor.commit();
						istagpwd = false;
					}else{
						toastTv.setText("");
						tv_pro.setText("密码输入错误,您还有"+(TAGPWD-tagpwd)+"次输入机会");
					}
					
//					showToast("密码输入错误,请重新输入");
					lpwv.clearPassword();
				}
			}
		});
		


	}

	@Override
	protected void onStart() {
		super.onStart();
		Intent it = getIntent();
	    islogin = it.getStringExtra("islogin");
		// 如果密码为空,则进入设置密码的界面
		appid = ((AppContext)getApplication()).getAppid();
		userid = ((AppContext)getApplication()).getUserid();
		View noSetPassword = (View) this.findViewById(R.id.tvNoSetPassword);
		 toastTv = (TextView) findViewById(R.id.login_toast);
		tv_userlogin = (TextView) findViewById(R.id.tv_user_login);
		
		tv_userlogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		toastTv.setText("");
		
		isrefresh = it.getStringExtra("refresh");
		if (isrefresh != null && isrefresh.equals("refresh")) {
			isjup = true;
			
			tv_userlogin.setText("跳 过");
			
			lpwv.resetPassWord("");
		}else{
			isjup = false;
			tv_userlogin.setVisibility(View.VISIBLE);
		}
		tv_userlogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
//				MainActivity.cache.putString("usermobile", "");
//				MainActivity.cache.putString("userpwd", "");
//				MainActivity.cache.commit();
				if(isjup){
					
					editor.putString("usermobile",
							"");
					editor.commit();
					Intent it = new Intent(UnlockLoginActivity.this,FmMainActivity.class);
					startActivity(it);
					finish();
					isjp = false;
				}else{
					if(islogin!=null&&islogin.equals("1")){
						AppContext.getInstance().exit();
						Intent it = new Intent(UnlockLoginActivity.this,UserActivity.class);
						startActivity(it);
						finish();
						
					}else{
						Intent it = new Intent(UnlockLoginActivity.this,UserActivity.class);
						startActivity(it);
						finish();
					}

				}

				
			}
		});
		if (lpwv.isPasswordEmpty()) {
			lpwv.setVisibility(View.GONE);
			noSetPassword.setVisibility(View.VISIBLE);
			toastTv.setText("快捷密码");
			noSetPassword.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(UnlockLoginActivity.this,
							SetUnlockPasswordActivity.class);
					// 打开新的Activity
					startActivity(intent);
					finish();
				}

			});
		} else {
			toastTv.setText("请输入手势密码");
			lpwv.setVisibility(View.VISIBLE);
			noSetPassword.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	/**
	 * 登录异步类
	 * 
	 * @author liangge
	 * 
	 */
	class LoginTask extends AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			showLoadingDialog("正在努力加载...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3],
					params[4], params[5],params[6], params[7],params[8],params[9],params[10],params[11],params[12]};
			return NetCommunicate.get(HttpUrls.LOGIN, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			final HashMap<String, Object> results = result;
			final Intent intent = new Intent(UnlockLoginActivity.this,
					FmMainActivity.class);
			if (result != null) {
				Log.e("", "登录返回值 = = =" + result);
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					
					if(result.get("PERSONPIC")!=null){
						editor.putString("PERSONPIC", result.get("PERSONPIC").toString());
					}
					if(result.get("PROVID")!=null){
						editor.putString("PROVIDs", result.get("PROVID").toString());
					}
					if(result.get("CITYID")!=null){
						editor.putString("CITYIDs", result.get("CITYID").toString());
					}
					if(result.get("AREAID")!=null){
						editor.putString("AREAIDs", result.get("AREAID").toString());
					}
						
						if(result.get("NOCARDFEERATE")!=null){
							((AppContext) getApplication())
							.setNocein(result.get("NOCARDFEERATE").toString());
							editor.putString("nocardfeerate", result.get("NOCARDFEERATE").toString());
							editor.putString("oemfeerate", result.get("OEMFEERATE").toString());
							editor.commit();
						}else{
							editor.putString("nocardfeerate", "0.69");
							
							editor.commit();
						}
						if(result.get("ISAREAAGENT")!=null){
							editor.putString("ISAREAAGENT", result.get("ISAREAAGENT").toString());
						}
						if(result.get("NOTICEMESSAGE")!=null){
							editor.putString("NOTICEMESSAGE", result.get("NOTICEMESSAGE").toString());
						}else{
							
						}
						if(result.get("NCONTENT")!=null){
							editor.putString("NCONTENT", result.get("NCONTENT").toString());
							editor.putString("NCREATETIM", result.get("NCREATETIM").toString());
						}else{
							editor.putString("NCONTENT", "");
							editor.putString("NCREATETIM", "");
						}
						editor.putString("OEMID", result.get("LOEMID").toString());
						editor.putString("ISRETAILERS", result.get("ISRETAILERS").toString());
						editor.putString("ISSALEAGT", result.get("ISSALEAGT").toString());
						editor.putString("ISGENERALAGENT", result.get("ISGENERALAGENT").toString());
						editor.putString("ISSENIORMEMBER", result.get("ISSENIORMEMBER").toString());
						((AppContext) getApplicationContext()).setCurrol(results.get("CURROL").toString());
						editor.putString("CURROL",results.get("CURROL").toString());
						
						editor.commit();
						if(results.get("CURROL").toString().equals("0")){

						}else if(results.get("AGENTID")!=null){
							editor.putString("AGENTID",results.get("AGENTID").toString());
							
							editor.commit();
							((AppContext) getApplicationContext()).setAgentid(results.get("AGENTID").toString());
						}
					((AppContext) getApplicationContext()).setTxnsts(result
							.get("TXNSTS").toString());
					((AppContext) getApplicationContext()).setCustId(result
							.get("PHONENUMBER").toString());
					((AppContext) getApplicationContext()).setMobile(result
							.get("PHONENUMBER").toString());
					((AppContext) getApplicationContext()).setMerSts(result
							.get("MERSTS").toString());
					((AppContext) getApplicationContext()).setSts(result
							.get("STS").toString());
					((AppContext) getApplicationContext())
							.setCustPass(share
									.getString("userpwd",""));

					((AppContext) UnlockLoginActivity.this.getApplication())
							.setMercNum(result.get("MERCNUM").toString());
					editor.putString("Txnsts", result.get("TXNSTS").toString());
					editor.putString("CustId", result.get("PHONENUMBER").toString());
					editor.putString("Mobile", result.get("PHONENUMBER").toString());
					editor.putString("MercNum",result.get("MERCNUM").toString());
					editor.putString("STS",result.get("STS").toString());
					editor.putString("MERSTS",result.get("MERSTS").toString());
					editor.commit();


					lognum = result.get("LOGNUM").toString();

//					if (lognum != null && lognum.equals("0")) {
//						Intent it = new Intent(UnlockLoginActivity.this,
//								SetPayPassActivity.class);
//						startActivity(it);
//
//
//						Toast.makeText(getApplicationContext(), "请设置支付密码!",
//								Toast.LENGTH_SHORT).show();
//						ToastCustom.showMessage(UnlockLoginActivity.this,
//								"请设置支付密码!");
//						finish();
//
//					} else {
						// MainActivity.cache.putString("usermobile", result
						// .get("PHONENUMBER").toString());
						// MainActivity.cache.putString("usepwd", passWord);

						startActivity(intent);
						finish();

//						ToastCustom.showMessage(UnlockLoginActivity.this,
//								"欢迎登录钱海钱包");
//					}
//
				} else {
					if (results != null) {
						
						if(result.get("NOCARDFEERATE")!=null){
							((AppContext) getApplication())
							.setNocein(result.get("NOCARDFEERATE").toString());
							editor.putString("nocardfeerate", result.get("NOCARDFEERATE").toString());
							editor.putString("oemfeerate", result.get("OEMFEERATE").toString());
							editor.commit();
						}

						if (results.get("PHONENUMBER") != null) {
							editor.putString("CustId", result.get("PHONENUMBER").toString());
							editor.putString("Mobile", result.get("PHONENUMBER").toString());
							editor.commit();
							((AppContext) getApplicationContext())
									.setCustId(results.get("PHONENUMBER")
											.toString());
							((AppContext) getApplicationContext())
									.setMobile(results.get("PHONENUMBER")
											.toString());
						}
						((AppContext) getApplicationContext())
								.setCustPass(share.getString("userpwd",""));
						if (results.get("MERCNUM") != null) {
							editor.putString("MercNum",result.get("MERCNUM").toString());
							editor.commit();
							((AppContext) UnlockLoginActivity.this
									.getApplication()).setMercNum(results.get(
									"MERCNUM").toString());
						}

						if (results.get("MERSTS") != null) {
							((AppContext) getApplicationContext())
									.setMerSts(results.get("MERSTS").toString());
//							ToastCustom.showMessage(UnlockLoginActivity.this,
//									"欢迎登录q");
							editor.putString("MERSTS",result.get("MERSTS").toString());
							editor.putString("STS",result.get("STS").toString());
							editor.commit();
							startActivity(intent);
							finish();
						} else {
							
							editor.putString("usermobile", "");
							editor.putString("userpwd", "");
							editor.commit();

							warnDialog = new OneButtonDialogWarn(
									UnlockLoginActivity.this,
									R.style.CustomDialog, "提示", result.get(
											Entity.RSPMSG).toString(), "确定",
									new OnMyDialogClickListener() {
										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											
											finish();

											warnDialog.dismiss();
										}
									});

							warnDialog.show();
						}
					}
				}

			} else {
				warnDialog = new OneButtonDialogWarn(UnlockLoginActivity.this,
						R.style.CustomDialog, "提示", "请求服务器失败！！！", "确定",
						new OnMyDialogClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								warnDialog.dismiss();
							}
						});
				warnDialog.show();
			}
			// Intent intent = new Intent(UserActivity.this,
			// MenuActivity.class);
			// startActivity(intent);
			// finish();
			super.onPostExecute(result);

		}
	}
	
	private boolean isExit;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (!isExit) {
				isExit = true;
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable() {
					public void run() {
						isExit = false;
					}
				}, 2000);
				;
				return false;
			} else {
				
				if (isrefresh != null && isrefresh.equals("refresh")) {
					editor.putString("usermobile", "");
					editor.putString("userpwd", "");
					editor.commit();
				}else{

				}

				((AppContext) getApplication()).setCustId(null); // 商户ID赋为空
				((AppContext) getApplication()).setPsamId(null);
				((AppContext) getApplication()).setMacKey(null);
				((AppContext) getApplication()).setPinKey(null);
				((AppContext) getApplication()).setMerSts(null);
				((AppContext) getApplication()).setMobile(null);
				((AppContext) getApplication()).setEncryPtkey(null);
				((AppContext) getApplication()).setStatus(null);
				((AppContext) getApplication()).setCustPass(null);
				((AppContext) getApplication()).setVersionSerial(null);
				AppContext.getInstance().exit();
//				AppManager.;
//				if(FmMainActivity.context!=null){
//					FmMainActivity.context.finish();
//				}
				finish();
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	

	private void setfirstimg(){
		
//		Resources res = getResources();
//		Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.head_portrait);
//		headimg.setImageBitmap(getRoundedCornerBitmap(bmp));
		Bitmap bitmap = null;
//		
		if (personpic==null||personpic.equals("")) {
//			Resources res = getResources();
//			bitmap = BitmapFactory.decodeResource(res, R.drawable.head_portrait);
//			img_head.setImageBitmap(getRoundedCornerBitmap(bitmap));
		}else{
			Bitmap bit = null;
			try {
				 bit = GetImageUtil.iscace(img_head,HttpUrls.HOST_POSM+personpic);
			} catch (Exception e) {
				// TODO: handle exception\
				Log.e("", ""+e.toString());
			}
			if(bit!=null){
				img_head.setImageBitmap(bit);
			}else{
			
			new GetImageUtil(this, img_head,HttpUrls.HOST_POSM+personpic);
			}
//			new ImageLoadTask(this).execute(HttpUrls.HOST_POSM+personpic);
		}
		
	}

}
