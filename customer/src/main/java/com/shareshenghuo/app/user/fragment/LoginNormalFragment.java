package com.shareshenghuo.app.user.fragment;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.shareshenghuo.app.user.fragment.BaseFragment;
import com.shareshenghuo.app.user.LoginActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.RetrievePwdActivity;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.request.LoginNormalRequest;
import com.shareshenghuo.app.user.network.request.LoginOtherRequest;
import com.shareshenghuo.app.user.network.response.LoginResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.MD5Utils;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.ViewUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
/**
 * 
 * @author Lenovo
 *普通登陆
 */
public class LoginNormalFragment extends BaseFragment implements OnClickListener, PlatformActionListener {

	private EditText edAccount;
	private EditText edPwd;
	private Button btnLogin;
	private ImageView del_img;
	private int loginType;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_login_normal;
	}

	@Override
	protected void init(View rootView) {
		edAccount = (EditText) rootView.findViewById(R.id.edAccount);
		edPwd = (EditText) rootView.findViewById(R.id.edPassword);
		btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
		
		rootView.findViewById(R.id.btnLogin).setOnClickListener(this);
		rootView.findViewById(R.id.tvForgetPwd).setOnClickListener(this);
		rootView.findViewById(R.id.btnLoginQQ).setOnClickListener(this);
		rootView.findViewById(R.id.btnLoginWX).setOnClickListener(this);
		edAccount.setText(UserInfoManager.getUserName(activity));	
		del_img = (ImageView) rootView.findViewById(R.id.del_img);
		edAccount.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					del_img.setVisibility(View.VISIBLE);
				}else{
					del_img.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		del_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				edAccount.setText("");
				del_img.setVisibility(View.GONE);
			}
		});
		
		edAccount.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(edAccount.getText().length()>0){
					del_img.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnLogin:
			login();
			break;
			
		case R.id.tvForgetPwd:
			startActivity(new Intent(activity, RetrievePwdActivity.class));
			break;
			
		case R.id.btnLoginQQ:
			loginType = 1;
			auth(ShareSDK.getPlatform(activity, QQ.NAME));
			break;
			
		case R.id.btnLoginWX:
			loginType = 2;
			auth(ShareSDK.getPlatform(activity, Wechat.NAME));
			break;
		}
	}
	
	public void login() {
		if(ViewUtil.checkEditEmpty(edAccount, "请填写手机号"))
			return;
		if(ViewUtil.checkEditEmpty(edPwd, "请填写密码"))
			return;
		
		ProgressDialogUtil.showProgressDlg(activity, "登录中");
		btnLogin.setEnabled(false);
		
		final LoginNormalRequest req = new LoginNormalRequest();
		req.account = edAccount.getText().toString();
		req.registration_id = JPushInterface.getRegistrationID(activity);
		String pwd = edPwd.getText().toString();
		for(int i=0; i<3; i++)
			pwd = MD5Utils.getMD5String(pwd);
		req.password = pwd;//546e153a5520b5f72344830325ea03d1
		Log.e("", ""+req.toString());
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_LOGIN_NORMAL, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				btnLogin.setEnabled(true);
			
				if(resp.statusCode==200 && resp.result!=null) {
					Log.e("", " - - - - -  "+resp.result);
					LoginResponse bean = new Gson().fromJson(resp.result, LoginResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						UserInfoManager.setLoginType(activity, 0);
						UserInfoManager.setUserPwd(activity, req.password);
						((LoginActivity)activity).login(bean.data);
					} else {
						T.showShort(activity, bean.result_desc);
					}
				}
			}
			
			@Override
			public void onFailure(HttpException e, String msg) {
				ProgressDialogUtil.dismissProgressDlg();
				btnLogin.setEnabled(true);
				if(activity != null)
					T.showNetworkError(activity);
			}
		});
	}
	
	public void auth(Platform platform) {
		ProgressDialogUtil.showProgressDlg(activity, "请稍后");
		platform.setPlatformActionListener(this);
		platform.showUser(null);
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			login(b.getString("id"), b.getString("name"), b.getString("avatar"), b.getString("unionid"));
		}
	};

	@Override
	public void onCancel(Platform platform, int arg1) {
		T.showShort(activity, "授权失败");
		ProgressDialogUtil.dismissProgressDlg();
		platform.removeAccount();
	}

	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
//		Toast.makeText(getActivity().getApplicationContext()," - - - - -"+ platform.getDb().toString(), Toast.LENGTH_LONG).show();
		if(action == Platform.ACTION_AUTHORIZING || action == Platform.ACTION_USER_INFOR) {
			if(platform.isValid()) {
				
//				Toast.makeText(getActivity().getApplicationContext(), platform.getDb().toString(), Toast.LENGTH_LONG).show();
				PlatformDb db = platform.getDb();
				String id = db.getUserId();
				String name = db.getUserName();
				String avatar = db.getUserIcon();
				String unionid  = db.get("unionid");
//				String banid = db.();
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putString("id", id);
				b.putString("name", name);
				b.putString("avatar", avatar);
				b.putString("unionid", unionid);
				msg.setData(b);
				handler.sendMessage(msg);
				platform.removeAccount();
			}
		}
		ProgressDialogUtil.dismissProgressDlg();
	}

	@Override
	public void onError(Platform platform, int arg1, Throwable arg2) {
		T.showShort(activity, "授权失败");
		platform.removeAccount();
		ProgressDialogUtil.dismissProgressDlg();
	}
	
	/**
	 * 第三方登录
	 */
	public void login(String id, String name, String avatar,String unionid) {
		ProgressDialogUtil.showProgressDlg(activity, "登录中");
		LoginOtherRequest req = new LoginOtherRequest();
		req.registration_id = JPushInterface.getRegistrationID(activity);
		req.band_id = id;
		req.band_type = loginType+"";
		req.nick_name = name;
		req.user_photo = avatar;
		req.band_unionid = unionid;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(req.toJson());
		new HttpUtils().send(HttpMethod.POST, Api.URL_LOGIN_OTHER, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				LoginResponse bean = new Gson().fromJson(resp.result, LoginResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					UserInfoManager.setLoginType(activity, loginType);
					((LoginActivity)activity).login(bean.data);
				} else {
					T.showShort(activity, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(activity);
			}
		});
	}
}
