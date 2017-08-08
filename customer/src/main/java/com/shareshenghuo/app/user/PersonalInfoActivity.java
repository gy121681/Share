package com.shareshenghuo.app.user;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

import com.easemob.chat.EMChatManager;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.activity.realname.RealnameStepIDCardActivity;
import com.shareshenghuo.app.user.manager.ImageLoadManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.network.request.BindBean;
import com.shareshenghuo.app.user.network.request.UpdateUserInfoRequest;
import com.shareshenghuo.app.user.network.response.BaseResponse;
import com.shareshenghuo.app.user.network.response.FileUploadResponse;
import com.shareshenghuo.app.user.network.response.LoginResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.BitmapTool;
import com.shareshenghuo.app.user.util.FileUtil;
import com.shareshenghuo.app.user.util.PictureUtil;
import com.shareshenghuo.app.user.util.PreferenceUtils;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.user.widget.dialog.PayPwdWindow;
import com.shareshenghuo.app.user.widget.dialog.PickPhotoWindow;
import com.shareshenghuo.app.user.widget.dialog.TwoButtonDialog;
import com.shareshenghuo.app.user.widget.dialog.PayPwdWindow.PayPwdCallback;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * @author hang
 *	个人信息
 */
public class PersonalInfoActivity extends BaseTopActivity implements OnClickListener,PlatformActionListener {
	
	private ImageView ivAvatar;
	private ImageView ivBindLogo;
	private TextView tv_paypwd,realnametv;
	private TwoButtonDialog downloadDialog;
	private View rootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		rootView = LayoutInflater.from(this).inflate(R.layout.activity_personal_info, null);

		
		setContentView(rootView);
//		rootView = LayoutInflater.inflate(R.layout.activity_personal_info), null, false);
		
		initView();
		initListener();
		initdata();
		
			int statusBarHeight1 = 0;
			// 获取status_bar_height资源的ID
			int resourceId = getResources().getIdentifier("status_bar_height","dimen", "android");
			if (resourceId > 0) {// 根据资源ID获取响应的尺寸值
				statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
			}
			if(!PreferenceUtils.getPrefBoolean(PersonalInfoActivity.this, "mfirst3", false)&&!UserInfoManager.getUserInfo(this).certification_status.equals("2")){
//			     ViewUtil.showtip(PersonalInfoActivity.this,rootView.findViewById(R.id.llreaname), 2, statusBarHeight1, "mfirst3", R.drawable.tip3,rootView.findViewById(R.id.layout));
			    	  return;
			 }

	}
	
	public void initView() {
		initTopBar("个人信息");
		ivAvatar = getView(R.id.ivUserAvatar);
		ivBindLogo = getView(R.id.ivBindLogo);
		realnametv = getView(R.id.realnametv);
		
		UserInfo userInfo = UserInfoManager.getUserInfo(this);
		int loginType = UserInfoManager.getLoginType(this);
		if(userInfo.user_photo!=null){
			ImageLoadManager.getInstance(this).displayImage(userInfo.user_photo, ivAvatar);
		}
		
		if(loginType == 0) {
//			ImageLoadManager.getInstance(this).displayImage(userInfo.user_photo, ivAvatar);
		} else {
//			ImageLoadManager.getInstance(this).displayImageByNet(userInfo.user_photo, ivAvatar);
			if(loginType == 1)
				ivBindLogo.setImageResource(R.drawable.btn_qq);
			else if(loginType == 2)
				ivBindLogo.setImageResource(R.drawable.btn_wx);
		}
		getView(R.id.llBindPlatform).setVisibility(loginType==0? View.GONE : View.VISIBLE);
		
		tv_paypwd = getView(R.id.tv_paypwd);
		if(UserInfoManager.getUserInfo(PersonalInfoActivity.this).is_set_pay_passwrod==0){
			tv_paypwd.setText("设置支付密码");
		}else{
			tv_paypwd.setText("找回支付密码");
		}


		
	}
	
	public void initListener() {
		findViewById(R.id.llUserAvatar).setOnClickListener(this);
		findViewById(R.id.btnLogout).setOnClickListener(this);
		findViewById(R.id.llModifyNickname).setOnClickListener(this);
		findViewById(R.id.llModifyRealname).setOnClickListener(this);
		findViewById(R.id.llModifyCardNo).setOnClickListener(this);
		findViewById(R.id.llModifyMobile).setOnClickListener(this);
		findViewById(R.id.llModifyPwd).setOnClickListener(this);
//		findViewById(R.id.llAddrManage).setOnClickListener(this);
		findViewById(R.id.llreaname).setOnClickListener(this);
		findViewById(R.id.llbankcardinfo).setOnClickListener(this);
		findViewById(R.id.llwechat).setOnClickListener(this);
		findViewById(R.id.llpaypwd).setOnClickListener(this);
		findViewById(R.id.llfandpaypwd).setOnClickListener(this);
		
		
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		ProgressDialogUtil.dismissProgressDlg();
		
	}
	
	private void initdata(){
		UserInfo userInfo = UserInfoManager.getUserInfo(this);
//		UserInfoManager.getUserInfo(getActivity()).band_type==0
		
		String isold = userInfo.is_old_user;
		String real = userInfo.certification_status;
		
		if(userInfo.band_type==2){
			setText(R.id.tv_wxbind, "已绑定");
			findViewById(R.id.llwechat).setEnabled(false);
//			((HomeFragment)activity.).auth(ShareSDK.getPlatform(this, Wechat.NAME));
		}
		setText(R.id.tvNickName, userInfo.nick_name);
		if(isold!=null&&isold.equals("1")){
			if(userInfo.person_no!=null&&userInfo.person_no.length()>4){
				setText(R.id.tvCardNo, "后四位: " +userInfo.person_no.substring(userInfo.person_no.length()-4));
			}
			setText(R.id.tvRealName, userInfo.real_name);
		}else if(real.equals("2")){
			if(userInfo.person_no!=null&&userInfo.person_no.length()>4){
				setText(R.id.tvCardNo, "后四位: " +userInfo.person_no.substring(userInfo.person_no.length()-4));
			}
			setText(R.id.tvRealName, userInfo.real_name);
		}
		
		if(userInfo.real_name!=null&&userInfo.real_name.length()>1){
			String xx = "";
			int a = userInfo.real_name.length()-1;
			switch ( a) {
			case 1:
				xx = "*";
				break;
			case 2:
				xx = "**";
				break;
			case 3:
				xx = "***";
				break;
			case 4:
				xx = "****";
				break;
			case 5:
				xx = "*****";
				break;
			default:
				break;
			}
			setText(R.id.tvRealName, xx+userInfo.real_name.substring(userInfo.real_name.length()-1));
		}
		
		

		setText(R.id.tvMobile, userInfo.mobile);
		
		

		if(userInfo.certification_status!=null&&userInfo.certification_status.equals("2")){
			setText(R.id.tv_aut, "已认证");
			findViewById(R.id.llreaname).setEnabled(false);
//			setText(R.id.tv_card, "尾号: " +userInfo.card_no.substring(userInfo.card_no.length()-6));
		}else{
			
			if(userInfo.is_old_user!=null&&userInfo.is_old_user.equals("1")){
//				realnametv.setText("");
				setText(R.id.tv_aut, "完善资料");
			}else{
				findViewById(R.id.llpaypwd).setVisibility(View.GONE);
				findViewById(R.id.llfandpaypwd).setVisibility(View.GONE);
			}
			if(UserInfoManager.getUserInfo(this).is_can_certification.equals("0")){
				setText(R.id.tv_aut, "认证失败");
			}
		}
		if(TextUtils.isEmpty(userInfo.card_no)){
			setText(R.id.tv_card, "未添加");
			findViewById(R.id.llbankcardinfo).setEnabled(false);
		}else{
			findViewById(R.id.llbankcardinfo).setEnabled(true);
		}
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		

		
		loadUserInfo();
	}
	
	public void loadUserInfo() {
//		ProgressDialogUtil.showProgressDlg(activity, "");
		UpdateUserInfoRequest req = new UpdateUserInfoRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		new HttpUtils().send(HttpMethod.POST, Api.URL_GET_USERINFO, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				if(this != null)
					T.showNetworkError(PersonalInfoActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				LoginResponse bean = new Gson().fromJson(resp.result, LoginResponse.class);
				if(Api.SUCCEED==bean.result_code && this!=null) {
					UserInfoManager.saveUserInfo(PersonalInfoActivity.this, bean.data);
					initdata();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.llfandpaypwd:
			startActivity(new Intent(PersonalInfoActivity.this, SetPayPassActivity.class));
			break;
	
		case R.id.llpaypwd:
			if(UserInfoManager.getUserInfo(PersonalInfoActivity.this).is_set_pay_passwrod==0){
				startActivity(new Intent(PersonalInfoActivity.this, FindPaypassActivity.class));
				
			}else{
				Intent intent = new Intent(PersonalInfoActivity.this, FindPaypassActivity.class);
				intent.putExtra("tag", "1");
				startActivity(intent);
			}
			break;
		case R.id.llUserAvatar:
			new PickPhotoWindow(this).showAtBottom1();
			break;
		case R.id.llbankcardinfo:
			
			startActivity(new Intent(PersonalInfoActivity.this, BankCardInfoActivity.class));
			break;
		case R.id.btnLogout:
			logout();
			break;
		case R.id.llwechat:
			auth(ShareSDK.getPlatform(PersonalInfoActivity.this, Wechat.NAME));
			break;
		case R.id.llreaname:
			if(UserInfoManager.getUserInfo(this).is_can_certification.equals("0")){
				initDialog("实名认证失败,请联系客服", "", "确定");
				return;
			}
			startActivity(new Intent(PersonalInfoActivity.this, RealnameStepIDCardActivity.class));
			
			finish();
			break;
		case R.id.llModifyNickname:
			Intent nick = new Intent(this, ModifyUserInfoActivity.class);
			nick.putExtra("modifyType", ModifyUserInfoActivity.MODIFY_NICKNAME);
			startActivity(nick);
			break;
			
		case R.id.llModifyRealname:
			Intent realname = new Intent(this, ModifyUserInfoActivity.class);
//			realname.putExtra("modifyType", ModifyUserInfoActivity.MODIFY_REALNAME);
//			startActivity(realname);
			break;
			
		case R.id.llModifyCardNo:
//			Intent cardNo = new Intent(this, ModifyUserInfoActivity.class);
//			cardNo.putExtra("modifyType", ModifyUserInfoActivity.MODIFY_CARDNO);
//			startActivity(cardNo);
			break;
			
		case R.id.llModifyMobile:
			startActivity(new Intent(this, MobileChangeActivity.class));
//			startActivity(new Intent(this, BindMobileActivity.class));
			break;
			
		case R.id.llModifyPwd:
			startActivity(new Intent(this, RetrievePwdActivity.class));
			break;
			
//		case R.id.llAddrManage:
////			startActivity(new Intent(this, AddrManageActivity.class));
//			break;
		}
	}
	
	private String tempPayPwd;
	
	public void showPayPwdDlg() {
		PayPwdWindow window = new PayPwdWindow(this);
		window.tips = "设置6位数字支付密码";
		window.setPayPwdCallback(new PayPwdCallback() {
			@Override
			public void inputPayPwd(String payPwd) {
				tempPayPwd = payPwd;
				PayPwdWindow window = new PayPwdWindow(PersonalInfoActivity.this);
				window.tips = "再次输入6位数字支付密码";
				window.setPayPwdCallback(new PayPwdCallback() {
					@Override
					public void inputPayPwd(String payPwd) {
						if(tempPayPwd.equals(payPwd))
							modifyPayPwd(payPwd);
						else
							T.showShort(PersonalInfoActivity.this, "两次输入密码不一致");
					}
				});
				window.showAtBottom();
			}
		});
		window.showAtBottom();
	}
	
	public void auth(Platform platform) {
		ProgressDialogUtil.showProgressDlg(PersonalInfoActivity.this, "请稍后");
		platform.setPlatformActionListener(this);
		platform.showUser(null);
	}
	
	public void modifyPayPwd(String payPwd) {
		ProgressDialogUtil.showProgressDlg(this, "设置中");
		UpdateUserInfoRequest req = new UpdateUserInfoRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.pay_password = payPwd;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPDATE_USER, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					UserInfo u = UserInfoManager.getUserInfo(PersonalInfoActivity.this);
					u.is_set_pay_passwrod = 1;
					UserInfoManager.saveUserInfo(PersonalInfoActivity.this, u);
					T.showShort(PersonalInfoActivity.this, "设置成功");
				} else {
					T.showShort(PersonalInfoActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(PersonalInfoActivity.this);
			}
		});
	}
	
	public void logout() {
		EMChatManager.getInstance().logout();
		UserInfoManager.clearUserInfo(this);
		setResult(RESULT_OK);
		finish();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PickPhotoWindow.REQUEST_PICK_LOCAL) {
                String path = FileUtil.getPath(this, data.getData());
                if (path == null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap bmp = extras.getParcelable("data");
                        if (bmp != null) {
                            upPhoto(BitmapTool.Bitmap2File(this, bmp));
                        }
                    }
                } else {
                    upPhoto(new File(path));
                }
            } else if (requestCode == PickPhotoWindow.REQUEST_TAKE_CAMERA) {
                upPhoto(new File(PickPhotoWindow.mImagePaths));
            }
        }
//		if (resultCode == Activity.RESULT_OK && data != null) {
//			if (requestCode == PickPhotoWindow.REQUEST_PICK_LOCAL || requestCode == PickPhotoWindow.REQUEST_TAKE_CAMERA) {
//				// 修改头像
//				String path = FileUtil.getPath(this, data.getData());
//				if (path == null) {
//					Bundle extras = data.getExtras();
//					if (extras != null) {
//						Bitmap bmp = extras.getParcelable("data");
//						if (bmp != null) {
//							upPhoto(BitmapTool.Bitmap2File(this, bmp));
//						}
//					}
//				} else {
//					upPhoto(new File(path));
//				}
//			}
//		}
	}
	
	public void upPhoto(File f) {
		try {
            // 压缩图片
            String compressPath = PictureUtil.compressImage(this, f.getPath(), f.getName(), 65);
            f = new File(compressPath);
        } catch(Exception e) {
            e.printStackTrace();
        }
		
		ProgressDialogUtil.showProgressDlg(this, "图片上传中");
		RequestParams params =  new RequestParams();
		params.addBodyParameter("business_type", UserInfoManager.getUserId(this)+"");
		params.addBodyParameter("file", f);
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPLOAD_FILE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(PersonalInfoActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
				if(resp.statusCode==200 && resp.result!=null) {
//					ReturnImgResponse bean = new Gson().fromJson(resp.result, ReturnImgResponse.class);
					FileUploadResponse bean = new Gson().fromJson(resp.result, FileUploadResponse.class);
					T.showShort(PersonalInfoActivity.this, bean.result_desc);
					if(Api.SUCCEED == bean.result_code)
						updateAvatar(bean.data.get(0));
				}
			}
		});
	}
	
	public void updateAvatar(final String value) {
		ProgressDialogUtil.showProgressDlg(this, "修改头像");
		UpdateUserInfoRequest req = new UpdateUserInfoRequest();
		req.user_id = UserInfoManager.getUserId(this)+"";
		req.user_photo = value;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPDATE_USER, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				if(resp.statusCode==200 && resp.result!=null) {
					BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
					T.showShort(PersonalInfoActivity.this, bean.result_desc);
					if(Api.SUCCEED == bean.result_code) {
						UserInfo user = UserInfoManager.getUserInfo(PersonalInfoActivity.this);
						user.user_photo = value;
						UserInfoManager.saveUserInfo(PersonalInfoActivity.this, user);
						ImageLoadManager.getInstance(PersonalInfoActivity.this).displayImage(value, ivAvatar);
					}
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(PersonalInfoActivity.this);
			}
		});
	}
	
	
	/**
	 * 下面是授权登录
	 */
	@Override
	public void onCancel(Platform platform, int arg1) {
		// TODO Auto-generated method stub
		T.showShort(PersonalInfoActivity.this, "授权失败");
		ProgressDialogUtil.dismissProgressDlg();
		platform.removeAccount();
	}

	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		if(action == Platform.ACTION_AUTHORIZING || action == Platform.ACTION_USER_INFOR) {
			if(platform.isValid()) {
				PlatformDb db = platform.getDb();
				String name = "";
				String id = db.getUserId();
				 name = db.getUserName();
				String avatar = db.getUserIcon();
				String unionid  = db.get("unionid");
				if(name==null||name.equals("")){
					name = db.get("nickname");
				}
				Message msg = new Message();
				Bundle b = new Bundle();

				b.putString("id", id);
				b.putString("name", name);
				b.putString("avatar", avatar);
				b.putString("unionid", unionid);
				msg.setData(b);
				handler.sendMessage(msg);
			}
		}
		
		ProgressDialogUtil.dismissProgressDlg();
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			
			BInd(b.getString("id"), b.getString("name"), b.getString("avatar"),b.getString("unionid"));
		}
	};

	
	/**
	 * 第三方登录
	 */
	public void BInd(String id, String name, String avatar,String unionid) {
		ProgressDialogUtil.showProgressDlg(PersonalInfoActivity.this, "登录中");
		BindBean req = new BindBean();
		req.band_type = "2";
		req.band_id = id;
		req.band_unionid = unionid;
		req.nick_name = name;
		req.user_photo = avatar;
		req.user_id = UserInfoManager.getUserInfo(PersonalInfoActivity.this).id;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(req.toJson());
		new HttpUtils().send(HttpMethod.POST, Api.USERBAND, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
				if(Api.SUCCEED == bean.result_code) {
//					UserInfoManager.setLoginType(activity, loginType);
//					((LoginActivity)activity).login(bean.data);
					UserInfo u = UserInfoManager.getUserInfo(PersonalInfoActivity.this);
					u.band_type = 2;
					T.showShort(PersonalInfoActivity.this, "绑定成功");
					ProgressDialogUtil.dismissProgressDlg();
				} else {
					T.showShort(PersonalInfoActivity.this, bean.result_desc);
					ProgressDialogUtil.dismissProgressDlg();
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(PersonalInfoActivity.this);
			}
		});
	}
	
	@Override
	public void onError(Platform platform, int arg1, Throwable arg2) {
		// TODO Auto-generated method stub
		T.showShort(PersonalInfoActivity.this, "授权失败");
		platform.removeAccount();
	}
	
	private void initDialog(String content,String left,String right) {
		// TODO Auto-generated method stub
		downloadDialog = new TwoButtonDialog(this, R.style.CustomDialog,
				"", content, left, right,true,new OnMyDialogClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
		}
		
}
