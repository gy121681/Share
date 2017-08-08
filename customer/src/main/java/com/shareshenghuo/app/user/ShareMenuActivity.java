package com.shareshenghuo.app.user;

import java.net.URLEncoder;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.DESKey;
import com.shareshenghuo.app.user.util.ShareHelper;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ShareMenuActivity extends BaseTopActivity implements OnClickListener{
	
	
	private String  url;
	private String title ;
	private String content;
	private String thumbnail,mobile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharemenu);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		initTopBar("分享");
		getView(R.id.llShareWx).setOnClickListener(this);
		getView(R.id.llShareWxMoment).setOnClickListener(this);
		getView(R.id.llShareQQ).setOnClickListener(this);
		getView(R.id.llShareQZone).setOnClickListener(this);
		  mobile = UserInfoManager.getUserInfo(ShareMenuActivity.this).mobile;
		 url =  Api.URL_SHARE_APP + "&sign=" +inithtml();
		 title = getResources().getString(R.string.app_name);
		 content = getResources().getString(R.string.app_name);
		 
	}
	
	private String  inithtml() {
		// TODO Auto-generated method stub

//		JSONObject jsonObj = new JSONObject();
//		try {
//			jsonObj.put("PHONENUMBER", mobile);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String aa = null;
		try {
			aa = DESKey.AES_Encode(mobile,
					"f15f1ede25a2471998ee06edba7d2e29");
			aa = URLEncoder.encode(aa);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aa;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.llShareWx:
			ShareHelper.shareWechat(this, title, content, url, thumbnail);
			break;
			
		case R.id.llShareWxMoment:
			ShareHelper.shareWechatMoment(this, title, content, url, thumbnail);
			break;
			
		case R.id.llShareQQ:
			ShareHelper.shareQQ(this, title, content, url, "");//分享图片
			break;
			
		case R.id.llShareQZone:
			ShareHelper.shareQZone(this, title, content, url, "");//分享图片
			break;
		}
		
	}
}