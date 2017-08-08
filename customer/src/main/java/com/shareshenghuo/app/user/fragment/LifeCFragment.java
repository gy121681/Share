package com.shareshenghuo.app.user.fragment;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.UserInfo;
import com.shareshenghuo.app.user.networkapi.Api;

/**
 * @author hang 生活圈
 */
public class LifeCFragment extends Fragment {

	private static final int REQ_ADD_ARTICLE = 0x100;

	private View rootView;
	private Activity activity;
	private WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.activity = getActivity();
		rootView = LayoutInflater.from(getActivity()).inflate(
				R.layout.fragment_life_c, null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = LayoutInflater.from(getActivity()).inflate(
					R.layout.fragment_life_c, null);
		}

		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		initView();
		return rootView;
	}

	public void initView() {
		webView = (WebView) rootView.findViewById(R.id.c_webView);
		webView.getSettings().setJavaScriptEnabled(true);
		UserInfo userInfo = UserInfoManager.getUserInfo(getActivity());

		// http://119.23.146.39/mobile/user.php?appid=32241c04df075159c316fee74dced7a0&account=%@&nickname=%@&face=%@
		// 需要访问的网址
//		String url = "http://119.23.146.39/mobile/user.php";
		String url = ""; // TODO: 2017/7/22 商城地址
		// post访问需要提交的参数
		// String postDate = "appid=" + userInfo.app_id + "&account="
		// + userInfo.mall_user_id + "&nickname=" + userInfo.nick_name
		// + "&face=" + Api.IMG_HOST + userInfo.user_photo;
		String temp_userphone = userInfo.user_photo;

		String userphone = temp_userphone;
		try {
			String a = userphone.substring(0, 6);
			if (!a.contains("http")||!a.contains("HTTP")) {
				userphone = Api.IMG_HOST + temp_userphone;
			}
		} catch (Exception e) {
		}

		String postDate = "appid=" + userInfo.app_id + "&account="
				+ UserInfoManager.getUserId(activity) + "&nickname="
				+ userInfo.nick_name + "&face=" + userphone;
		System.out.println("====商城访问提交的参数:" + postDate);
		// 由于webView.postUrl(url, postData)中 postData类型为byte[] ，
		// 通过EncodingUtils.getBytes(data, charset)方法进行转换
		webView.postUrl(url, EncodingUtils.getBytes(postDate, "BASE64"));

		// webView.loadUrl("http://119.23.146.39/mobile/user.php?appid="
		// + userInfo.app_id + "&account=" + userInfo.mall_user_id
		// + "&nickname=" + userInfo.nick_name + "&face=" + Api.IMG_HOST
		// + userInfo.user_photo);
		// System.out.println("====商城访问---"
		// + "http://119.23.146.39/mobile/user.php?appid="
		// + userInfo.app_id + "&account=" + userInfo.mall_user_id
		// + "&nickname=" + userInfo.nick_name + "&face=" + Api.IMG_HOST
		// + userInfo.user_photo);
		webView.setWebViewClient(new HelloWebViewClient());
	}

	// Web视图
	private class HelloWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}
}
