package com.shareshenghuo.app.user.manager;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.content.Intent;

import com.shareshenghuo.app.user.manager.DBManager;
import com.shareshenghuo.app.user.LoginActivity;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.network.request.UpdateUserInfoRequest;
import com.shareshenghuo.app.user.network.response.LoginResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.Arith;
import com.shareshenghuo.app.user.util.PreferenceUtils;
import com.shareshenghuo.app.user.util.T;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class UserInfoManager {

	private static final String KEY_USER_ID = "user_id";
	private static final String KEY_USER_NAME = "user_name";
	private static final String KEY_ISER_PWD = "user_pwd";
	private static final String KEY_LOGIN_TYPE = "login_type";
	private static final String KEY_WX_TYPE = "wx_type";
	
	private static final String HISTORY = "History";
	
	public static void setHISTORY(Context context, String loginType) {
		PreferenceUtils.setPrefString(context, HISTORY, loginType);
	}
	public static String getHISTORY(Context context) {
		return PreferenceUtils.getPrefString(context, HISTORY, "");
	}
	
	
	public static boolean isLogin(Context context) {
		return getUserId(context)>0;
	}
	
	public static void setUserId(Context context, int userId) {
		PreferenceUtils.setPrefInt(context, KEY_USER_ID, userId);
	}
	
	public static int getUserId(Context context) {
		return PreferenceUtils.getPrefInt(context, KEY_USER_ID, 0);
	}
	
	public static void setUserName(Context context, String userName) {
		PreferenceUtils.setPrefString(context, KEY_USER_NAME, userName);
	}
	
	public static String getUserName(Context context) {
		return PreferenceUtils.getPrefString(context, KEY_USER_NAME, "");
	}
	
	public static void setUserPwd(Context context, String pwd) {
		PreferenceUtils.setPrefString(context, KEY_ISER_PWD, pwd);
	}
	
	public static String getUserPwd(Context context) {
		return PreferenceUtils.getPrefString(context, KEY_ISER_PWD, "");
	}
	
	public static void setLoginType(Context context, int loginType) {
		PreferenceUtils.setPrefInt(context, KEY_LOGIN_TYPE, loginType);
	}
	
	public static int getLoginType(Context context) {
		return PreferenceUtils.getPrefInt(context, KEY_LOGIN_TYPE, 0);
	}
	
	public static void setWxType(Context context, String loginType) {
		PreferenceUtils.setPrefString(context, KEY_WX_TYPE, loginType);
	}
	
	public static String getWxType(Context context) {
		return PreferenceUtils.getPrefString(context, KEY_WX_TYPE, "");
	}
	
	public static UserInfo getUserInfo(Context context) {
		try {
			return DBManager.getInstance(context).getDB().findFirst(UserInfo.class);
		} catch (DbException e) {
			e.printStackTrace();
			return new UserInfo();
		}
	}
	
	public static void saveUserInfo(Context context, UserInfo userInfo) {
		try {
			DBManager.getInstance(context).getDB().deleteAll(UserInfo.class);
			DBManager.getInstance(context).getDB().save(userInfo);
			setUserId(context, userInfo.id);
			setUserName(context, userInfo.account);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	public static void savebindtype(){
		
	}
	
	public static void clearUserInfo(Context context) {
		try {
			DBManager.getInstance(context).getDB().deleteAll(UserInfo.class);
			setUserId(context, 0);
			setUserPwd(context, "");
			setLoginType(context, 0);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateMoney(Context context, double money) {
		UserInfo userInfo = getUserInfo(context);
		userInfo.money = Arith.add(userInfo.money, money);
		saveUserInfo(context, userInfo);
	}
	
	public static synchronized void updateUserInfo(final Context context) {
		if(isLogin(context)) {
			UpdateUserInfoRequest req = new UpdateUserInfoRequest();
			req.user_id = UserInfoManager.getUserId(context)+"";
			RequestParams params = new RequestParams();
			try {
				params.setBodyEntity(new StringEntity(req.toJson()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			new HttpUtils().send(HttpMethod.POST, Api.URL_GET_USERINFO, params, new RequestCallBack<String>() {
				@Override
				public void onSuccess(ResponseInfo<String> resp) {
					if(resp.statusCode==200 && resp.result!=null) {
						LoginResponse bean = new Gson().fromJson(resp.result, LoginResponse.class);
						if(0 == bean.result_code) {
							if(bean.data.status == 1)
								saveUserInfo(context, bean.data);
							else
								clearUserInfo(context);
						} else {
							clearUserInfo(context);
						}
					}
				}
				
				@Override
				public void onFailure(HttpException e, String msg) {
				}
			});
		}
	}
	
	public static boolean toLogin(Context context) {
		if(!UserInfoManager.isLogin(context)) {
			T.showShort(context, "您当前未登录，请先登录");
			context.startActivity(new Intent(context, LoginActivity.class));
			return true;
		}
		return false;
	}
}
