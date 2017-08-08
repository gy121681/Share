package com.shareshenghuo.app.shop;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.manager.ImageLoadManager;
import com.shareshenghuo.app.shop.manager.MessageManager;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.UserInfo;
import com.shareshenghuo.app.shop.network.bean.WebLoadActivity;
import com.shareshenghuo.app.shop.network.request.AdminRequest;
import com.shareshenghuo.app.shop.network.request.UpdateShopRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.network.response.FileUploadResponse;
import com.shareshenghuo.app.shop.network.response.LoginResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker.NewMessageCallback;
import com.shareshenghuo.app.shop.util.BitmapTool;
import com.shareshenghuo.app.shop.util.FileUtil;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.widget.CircleImageView;
import com.shareshenghuo.app.shop.widget.dialog.PickPhotoWindow;

/**
 * 
 * @author Lenovo 设置界面，退出
 */
public class MineActivity extends BaseTopActivity implements
		NewMessageCallback, OnClickListener {

	private ImageView ivIM;
	private CircleImageView civAvatar;
	private TextView tvNickname, tv_paypwd;
	private TextView tvMobile, bankcard;

	private NewChatMsgWorker newMsgWatcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_mine);
		initView();
	}

	public void initView() {
		initTopBar("设置");
		ivIM = getView(R.id.ivHomeIM);
		civAvatar = getView(R.id.civAvtar);
		tvNickname = getView(R.id.tvname);
		tvMobile = getView(R.id.tv_mobile);
		tv_paypwd = getView(R.id.tv_paypwd);
		bankcard = getView(R.id.bankcard);
		if (UserInfoManager.getUserInfo(MineActivity.this).is_set_pay_passwrod == 0) {
			tv_paypwd.setText("设置支付密码");
		} else {
			tv_paypwd.setText("找回支付密码");
		}
		if (TextUtils.isEmpty(UserInfoManager.getUserInfo(this).card_no)) {
			bankcard.setText("添加银行卡");
		} else {
			bankcard.setText("修改银行卡");
		}
		getView(R.id.llModifyAvatar).setOnClickListener(this);
		getView(R.id.btnLogout).setOnClickListener(this);
		getView(R.id.llModifyNick).setOnClickListener(this);
		getView(R.id.llBindMobile).setOnClickListener(this);
		getView(R.id.llModifyPwd).setOnClickListener(this);
		getView(R.id.llSetting).setOnClickListener(this);
		getView(R.id.llAboutUs).setOnClickListener(this);
		getView(R.id.llContactUs).setOnClickListener(this);
		getView(R.id.llpaypass).setOnClickListener(this);
		getView(R.id.llfindpaypass).setOnClickListener(this);
		getView(R.id.llBank).setOnClickListener(this);

		ivIM.setOnClickListener(this);

		newMsgWatcher = new NewChatMsgWorker(this, this);
		newMsgWatcher.startWork();

		updateView();
	}

	// protected void initTopBar(String title) {
	// llTopBack = (LinearLayout) findViewById(R.id.llTopBack);
	// tvTopTitle = (TextView) findViewById(R.id.tvTopTitle);
	// btnTopRight1 = (Button) findViewById(R.id.btnTopRight1);
	// btnTopRight2 = (Button) findViewById(R.id.btnTopRight2);
	// btnTopRight3 = (Button) findViewById(R.id.btnTopRight3);
	//
	// tvTopTitle.setText(title);
	//
	// llTopBack.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// finish();
	// }
	// });
	// }

	// protected void init() {
	//
	// }

	@Override
	public void onStart() {
		super.onStart();
		loadData();
	}

	@Override
	public void onResume() {
		super.onResume();
		ivIM.setBackgroundResource(MessageManager.getUnreadCount() > 0 ? R.drawable.ic_im_notice
				: R.drawable.ic_im);
	}

	public void loadData() {
		AdminRequest req = new AdminRequest();
		req.admin_id = UserInfoManager.getUserId(this) + "";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_SHOP_DETAIL, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						LoginResponse bean = new Gson().fromJson(resp.result,
								LoginResponse.class);
						if (Api.SUCCEED == bean.result_code) {
							UserInfoManager.saveUserInfo(MineActivity.this,
									bean.data);
							updateView();
						}
					}
				});
	}

	public void updateView() {
		UserInfo user = UserInfoManager.getUserInfo(MineActivity.this);
		tvNickname.setText(user.nick_name);
		tvMobile.setText(user.band_mobile);
		// ImageLoadManager.getInstance(MineActivity.this).displayImage(user.user_photo,
		// civAvatar);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llModifyAvatar:
			new PickPhotoWindow(MineActivity.this).showAtBottom();
			break;

		case R.id.btnLogout:
			logout();
			break;

		case R.id.llBank:
			if (TextUtils.isEmpty(UserInfoManager.getUserInfo(this).card_no)) {
				Intent it = new Intent(MineActivity.this,
						RealnameActivity.class);

				startActivity(it);
			} else {
				Intent it = new Intent(MineActivity.this,
						RealnameActivity.class);
				it.putExtra("tag", "1");
				startActivity(it);
			}

			// startActivity(new Intent(MineActivity.this,
			// RealnameActivity.class));
			// logout();
			break;

		case R.id.llModifyNick:
			startActivity(new Intent(MineActivity.this,
					ModifyNicknameActivity.class));
			break;

		case R.id.llBindMobile:

			startActivity(new Intent(this, MobileChangeActivity.class));
			// Intent bind = new Intent(MineActivity.this,
			// BindMobileActivity.class);
			// bind.putExtra("userInfo",
			// UserInfoManager.getUserInfo(MineActivity.this));
			// bind.putExtra("back", true);
			// startActivity(bind);
			break;

		case R.id.llModifyPwd:
			startActivity(new Intent(MineActivity.this,
					RetrievePwdActivity.class));
			break;

		case R.id.llSetting:
			startActivity(new Intent(MineActivity.this, SettingActivity.class));
			break;

		case R.id.llAboutUs:
			Intent about = new Intent(MineActivity.this, WebLoadActivity.class);
			about.putExtra("title", "关于我们");
			about.putExtra("url", Api.URL_ABOUT);
			startActivity(about);
			break;

		case R.id.llContactUs:
			startActivity(new Intent(MineActivity.this, ContactUsActivity.class));
			break;

		case R.id.ivHomeIM:
			ivIM.setBackgroundResource(R.drawable.ic_im);
			startActivity(new Intent(MineActivity.this, MessageActivity.class));
			break;

		case R.id.llpaypass:
			if (UserInfoManager.getUserInfo(MineActivity.this).is_set_pay_passwrod == 0) {
				startActivity(new Intent(MineActivity.this,
						FindPaypassActivity.class));

			} else {
				Intent intent = new Intent(MineActivity.this,
						FindPaypassActivity.class);
				intent.putExtra("tag", "1");
				startActivity(intent);
				// startActivity(new Intent(MineActivity.this,
				// FindPaypassActivity.class));
			}

			break;

		case R.id.llfindpaypass:
			startActivity(new Intent(MineActivity.this,
					SetPayPassActivity.class));
			break;

		}
	}

	@Override
	public void newMessage(int which) {
		ivIM.setBackgroundResource(R.drawable.ic_im_notice);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PickPhotoWindow.REQUEST_PICK_LOCAL
					|| requestCode == PickPhotoWindow.REQUEST_TAKE_CAMERA) {
				// 修改头像
				String path = FileUtil.getPath(MineActivity.this,
						data.getData());
				if (path == null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap bmp = extras.getParcelable("data");
						if (bmp != null) {
							upPhoto(BitmapTool.Bitmap2File(MineActivity.this,
									bmp));
						}
					}
				} else {
					upPhoto(new File(path));
				}
			}
		}
	}

	public void upPhoto(File f) {
		ProgressDialogUtil.showProgressDlg(MineActivity.this, "图片上传中");
		RequestParams params = new RequestParams();
		params.addBodyParameter("business_type",
				UserInfoManager.getUserId(MineActivity.this) + "");
		params.addBodyParameter("file", f);
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPLOAD_FILE, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ProgressDialogUtil.dismissProgressDlg();
						T.showNetworkError(MineActivity.this);
					}

					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						ProgressDialogUtil.dismissProgressDlg();
						if (resp.statusCode == 200 && resp.result != null) {
							FileUploadResponse bean = new Gson().fromJson(
									resp.result, FileUploadResponse.class);
							T.showShort(MineActivity.this, bean.result_desc);
							if (Api.SUCCEED == bean.result_code)
								updateAvatar(bean.data.get(0));
						}
					}
				});
	}

	public void updateAvatar(final String value) {
		ProgressDialogUtil.showProgressDlg(MineActivity.this, "修改头像");
		UpdateShopRequest req = new UpdateShopRequest();
		req.admin_id = UserInfoManager.getUserId(MineActivity.this) + "";
		req.user_photo = value;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPDATE_SHOP, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> resp) {
						ProgressDialogUtil.dismissProgressDlg();
						if (resp.statusCode == 200 && resp.result != null) {
							BaseResponse bean = new Gson().fromJson(
									resp.result, BaseResponse.class);
							T.showShort(MineActivity.this, bean.result_desc);
							if (Api.SUCCEED == bean.result_code) {
								UserInfo user = UserInfoManager
										.getUserInfo(MineActivity.this);
								user.user_photo = value;
								UserInfoManager.saveUserInfo(MineActivity.this,
										user);
								ImageLoadManager.getInstance(MineActivity.this)
										.displayImage(value, civAvatar);
							}
						}
					}

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ProgressDialogUtil.dismissProgressDlg();
						T.showNetworkError(MineActivity.this);
					}
				});
	}

	public void logout() {
		EMChatManager.getInstance().logout();
		UserInfoManager.clearUserInfo(MineActivity.this);
		Intent login = new Intent(MineActivity.this, LoginActivity.class);
		login.putExtra("logout", true);
		startActivity(login);
		MineActivity.this.finish();
		MainActivity.context.finish();
	}
}
