package com.shareshenghuo.app.shop.manager;

import android.content.Context;

import com.lidroid.xutils.exception.DbException;
import com.shareshenghuo.app.shop.network.bean.UserInfo;
import com.shareshenghuo.app.shop.util.Arith;
import com.shareshenghuo.app.shop.util.PreferenceUtils;

public class UserInfoManager {

	private static final String KEY_USER_ID = "user_id";
	private static final String KEY_USER_NAME = "user_name";
	private static final String KEY_ISER_PWD = "user_pwd";

	private static String mall_login_name = "mall_login_name";// 商家登录账号
	private static String mall_login_password = "mall_login_password";// 商家登录密码
	private static String mall_url = "mall_url";// 商家地址
	private static String app_id = "app_id";// 应用id
	private static String mall_shop_id = "mall_shop_id";// 商家店铺id
	private static String account = "account";// 账户

	public static String getAccount(Context context) {
		return PreferenceUtils.getPrefString(context, account, "");
	}

	public static String getMallLoginName(Context context) {
		return PreferenceUtils.getPrefString(context, mall_login_name, "");
	}

	public static String getMallLoginPassword(Context context) {
		return PreferenceUtils.getPrefString(context, mall_login_password, "");
	}

	public static String getMallUrl(Context context) {
		return PreferenceUtils.getPrefString(context, mall_url, "");
	}

	public static String getAppId(Context context) {
		return PreferenceUtils.getPrefString(context, app_id, "");
	}

	public static String getMallShopId(Context context) {
		return PreferenceUtils.getPrefString(context, mall_shop_id, "");
	}

	public static void setAccount(Context context, String s) {
		PreferenceUtils.setPrefString(context, account, s);
	}

	public static void setMallLoginName(Context context, String s) {
		PreferenceUtils.setPrefString(context, mall_login_name, s);
	}

	public static void setMallLoginPassword(Context context, String s) {
		PreferenceUtils.setPrefString(context, mall_login_password, s);
	}

	public static void setMallUrl(Context context, String s) {
		PreferenceUtils.setPrefString(context, mall_url, s);
	}

	public static void setAppId(Context context, String s) {
		PreferenceUtils.setPrefString(context, app_id, s);
	}

	public static void setMallShopId(Context context, String s) {
		PreferenceUtils.setPrefString(context, mall_shop_id, s);
	}

	public static boolean isLogin(Context context) {
		return getUserId(context) > 0;
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

	public static UserInfo getUserInfo(Context context) {
		try {
			return DBManager.getInstance(context).getDB()
					.findFirst(UserInfo.class);
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

	public static void clearUserInfo(Context context) {
		try {
			DBManager.getInstance(context).getDB().deleteAll(UserInfo.class);
			setUserId(context, 0);
			setUserPwd(context, "");
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public static void refreshUserMoney(Context context, double money,
			boolean isAdd) {
		UserInfo userInfo = getUserInfo(context);
		if (isAdd)
			userInfo.money = Arith.add(userInfo.money, money);
		else
			userInfo.money = Arith.sub(userInfo.money, money);
		saveUserInfo(context, userInfo);
	}

	// public static synchronized void updateUserInfo(final Context context) {
	// if(isLogin(context)) {
	// LoginRequest req = new LoginRequest();
	// req.user_name = getUserName(context);
	// req.user_password = getUserPwd(context);
	// RequestParams params = new RequestParams();
	// try {
	// params.setBodyEntity(new StringEntity(req.toJson()));
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// new HttpUtils().send(HttpMethod.POST, NetConstants.URL_LOGIN, params, new
	// RequestCallBack<String>() {
	// @Override
	// public void onSuccess(ResponseInfo<String> resp) {
	// if(resp.statusCode==200 && resp.result!=null) {
	// LoginResponse bean = new Gson().fromJson(resp.result,
	// LoginResponse.class);
	// if("0".equals(bean.result_code)) {
	// saveUserInfo(context, bean.data);
	// } else {
	// clearUserInfo(context);
	// }
	// }
	// }
	//
	// @Override
	// public void onFailure(HttpException e, String msg) {
	// }
	// });
	// }
	// }
}
