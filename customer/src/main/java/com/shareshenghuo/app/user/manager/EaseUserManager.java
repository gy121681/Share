package com.shareshenghuo.app.user.manager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Context;

import com.easemob.easeui.domain.EaseUser;
import com.shareshenghuo.app.user.network.bean.ContactInfo;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.request.ShopDetailRequest;
import com.shareshenghuo.app.user.network.request.UserIMListRequest;
import com.shareshenghuo.app.user.network.response.ContactListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class EaseUserManager {
	
	public static List<EaseUser> cacheContactList = new ArrayList<EaseUser>();

	public static EaseUser getEaseUser(final String username) {
		EaseUser user = getEaseUserFromCache(username);
		if(user == null) {
			user = new EaseUser(username);
			getEaseUserFromNet(user);
		}
		return user;
	}
	
	public static EaseUser getEaseUserFromCache(String username) {
		for(EaseUser item : cacheContactList) {
			if(item.getUsername().equals(username))
				return item;
		}
		return null;
	}
	
	public static void getEaseUserFromNet(final EaseUser user) {
		UserIMListRequest req = new UserIMListRequest();
		req.search_name = user.getUsername();
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_USER_IM_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ContactListResponse bean = new Gson().fromJson(resp.result, ContactListResponse.class);
				if(Api.SUCCEED == bean.result_code && bean.data.size()>0) {
					ContactInfo item = bean.data.get(0);
					if(user.getUsername().startsWith("s")) {
						//商家
						user.setAvatar(Api.HOST+item.shop_photo);
						user.setNick(item.shop_name);
					} else {
						//用户
						user.setAvatar(Api.HOST+item.user_photo);
						user.setNick(item.user_name);
					}
					cacheContactList.add(user);
				}
			}
		});
	}
	
	public static void addContact(Context context, int shopId) {
		ShopDetailRequest req = new ShopDetailRequest();
		req.user_id = UserInfoManager.getUserId(context)+"";
		req.shop_id = shopId+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_ADD_CONTACT, params, null);
	}
}
