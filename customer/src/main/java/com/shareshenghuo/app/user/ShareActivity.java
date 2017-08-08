package com.shareshenghuo.app.user;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.WebLoadFragment;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.widget.dialog.ShareMenuWindow;

/**
 * @author hang
 * 推荐有礼
 */
public class ShareActivity extends BaseTopActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		initView();
	}
	
	public void initView() {
		initTopBar("推荐有礼");
		btnTopRight2.setVisibility(View.VISIBLE);
		btnTopRight2.setBackgroundResource(R.drawable.icon_89);
		btnTopRight2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ShareMenuWindow window = new ShareMenuWindow(ShareActivity.this);
				window.url = Api.URL_SHARE_APP + "?user_id=" + UserInfoManager.getUserId(ShareActivity.this);
				window.title = getResources().getString(R.string.app_name);
				window.content = getResources().getString(R.string.app_name);
				window.showAtBottom();
			}
		});
		
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.rlContent, WebLoadFragment.getInstance(Api.URL_RECOMMEND));
		t.commit();
	}
}
