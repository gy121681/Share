package com.shareshenghuo.app.shop;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.ImageLoadManager;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.UserInfo;
import com.shareshenghuo.app.shop.network.bean.WebLoadActivity;
import com.shareshenghuo.app.shop.network.request.AdminRequest;
import com.shareshenghuo.app.shop.network.request.UpdateShopRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.network.response.FileUploadResponse;
import com.shareshenghuo.app.shop.network.response.LoginResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.BitmapTool;
import com.shareshenghuo.app.shop.util.FileUtil;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.widget.CircleImageView;
import com.shareshenghuo.app.shop.widget.dialog.PickPhotoWindow;

public class PersonalCenterActivity extends BaseTopActivity implements OnClickListener {
	
	private CircleImageView civAvatar;
	private TextView tvNickname;
	private TextView tvMobile;
	private TextView tvIncomeToday;
	private TextView tvIncomeTotal;
	private TextView tvUnconfirmMoney;
	private TextView tvCanWithdrawMoney;
	private TextView tvHasWithdrawMoney;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_center);
		init();
		updateView();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		loadData();
	}

	public void init() {
		civAvatar = getView(R.id.civAvtar);
		tvNickname = getView(R.id.tvNickname);
		tvMobile = getView(R.id.tvMobile);
		tvIncomeToday = getView(R.id.tvIncomeToday);
		tvIncomeTotal = getView(R.id.tvIncomeTotal);
		tvUnconfirmMoney = getView(R.id.tvUnconfirmMoney);
		tvCanWithdrawMoney = getView(R.id.tvCanWithdrawMoney);
		tvHasWithdrawMoney = getView(R.id.tvHasWithdrawMoney);
		
		getView(R.id.ivBack).setOnClickListener(this);
		getView(R.id.llModifyAvatar).setOnClickListener(this);
		getView(R.id.btnLogout).setOnClickListener(this);
		getView(R.id.llMyAccount).setOnClickListener(this);
		getView(R.id.llModifyNick).setOnClickListener(this);
		getView(R.id.llBindMobile).setOnClickListener(this);
		getView(R.id.llModifyPwd).setOnClickListener(this);
		getView(R.id.llSetting).setOnClickListener(this);
		getView(R.id.llAboutUs).setOnClickListener(this);
		getView(R.id.llContactUs).setOnClickListener(this);
	}
	
	public void loadData() {
		AdminRequest req = new AdminRequest();
		req.admin_id = UserInfoManager.getUserId(this)+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_SHOP_DETAIL, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				LoginResponse bean = new Gson().fromJson(resp.result, LoginResponse.class);
				Log.e("", ""+resp.result.toString());
				if(Api.SUCCEED == bean.result_code) {
					UserInfoManager.saveUserInfo(PersonalCenterActivity.this, bean.data);
					updateView();
				}
			}
		});
	}
	
	public void updateView() {
		UserInfo user = UserInfoManager.getUserInfo(this);
		ImageLoadManager.getInstance(this).displayImage(user.user_photo, civAvatar);
		tvNickname.setText(user.nick_name);
		tvMobile.setText(user.band_mobile);
		tvIncomeToday.setText("¥"+user.today_income);
		tvIncomeTotal.setText("¥"+user.all_income);
		tvUnconfirmMoney.setText("¥"+user.with_drawals_ing_fee);
		tvCanWithdrawMoney.setText("¥"+user.money);
		tvHasWithdrawMoney.setText("¥"+user.with_drawals_ed_fee);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.ivBack:
			finish();
			break;
			
		case R.id.llModifyAvatar:
			new PickPhotoWindow(this).showAtBottom();
			break;
			
		case R.id.btnLogout:
			logout();
			break;
			
		case R.id.llMyAccount:
			startActivity(new Intent(this, MyAccountActivity.class));
			break;
			
		case R.id.llModifyNick:
			startActivity(new Intent(this, ModifyNicknameActivity.class));
			break;
			
		case R.id.llBindMobile:
			Intent bind = new Intent(this, BindMobileActivity.class);
			bind.putExtra("userInfo", UserInfoManager.getUserInfo(this));
			bind.putExtra("back", true);
			startActivity(bind);
			break;
			
		case R.id.llModifyPwd:
			startActivity(new Intent(this, RetrievePwdActivity.class));
			break;
			
		case R.id.llSetting:
			startActivity(new Intent(this, SettingActivity.class));
			break;
			
		case R.id.llAboutUs:
			Intent about = new Intent(this, WebLoadActivity.class);
			about.putExtra("title", "关于我们");
			about.putExtra("url", Api.URL_ABOUT);
			startActivity(about);
			break;
			
		case R.id.llContactUs:
			startActivity(new Intent(this, ContactUsActivity.class));
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == PickPhotoWindow.REQUEST_PICK_LOCAL || requestCode == PickPhotoWindow.REQUEST_TAKE_CAMERA) {
				// 修改头像
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
			}
		}
	}
	
	public void upPhoto(File f) {
		ProgressDialogUtil.showProgressDlg(this, "图片上传中");
		RequestParams params =  new RequestParams();
		params.addBodyParameter("business_type", UserInfoManager.getUserId(this)+"");
		params.addBodyParameter("file", f);
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPLOAD_FILE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(PersonalCenterActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				if(resp.statusCode==200 && resp.result!=null) {
					FileUploadResponse bean = new Gson().fromJson(resp.result, FileUploadResponse.class);
					T.showShort(PersonalCenterActivity.this, bean.result_desc);
					if(Api.SUCCEED == bean.result_code)
						updateAvatar(bean.data.get(0));
				}
			}
		});
	}
	
	public void updateAvatar(final String value) {
		ProgressDialogUtil.showProgressDlg(this, "修改头像");
		UpdateShopRequest req = new UpdateShopRequest();
		req.admin_id = UserInfoManager.getUserId(this)+"";
		req.user_photo = value;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPDATE_SHOP, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				if(resp.statusCode==200 && resp.result!=null) {
					BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
					T.showShort(PersonalCenterActivity.this, bean.result_desc);
					if(Api.SUCCEED == bean.result_code) {
						UserInfo user = UserInfoManager.getUserInfo(PersonalCenterActivity.this);
						user.user_photo = value;
						UserInfoManager.saveUserInfo(PersonalCenterActivity.this, user);
						ImageLoadManager.getInstance(PersonalCenterActivity.this).displayImage(value, civAvatar);
					}
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(PersonalCenterActivity.this);
			}
		});
	}
	
	public void logout() {
		EMChatManager.getInstance().logout();
		UserInfoManager.clearUserInfo(this);
		setResult(RESULT_OK);
		finish();
	}
}
