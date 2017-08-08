package com.shareshenghuo.app.user.widget.dialog;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ShareHelper;
import com.shareshenghuo.app.user.widget.dialog.CommonDialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class ShareMenuWindow extends CommonDialog implements OnClickListener {
	
	public String title;
	public String content;
	public String url;
	public String thumbnail;
	public  String ico;

	public ShareMenuWindow(Context context) {
		super(context, R.layout.dlg_share_menu, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	public void initDlgView() {
		getView(R.id.llShareWx).setOnClickListener(this);
		getView(R.id.llShareWxMoment).setOnClickListener(this);
		getView(R.id.llShareQQ).setOnClickListener(this);
		getView(R.id.llShareQZone).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.llShareWx:
			ShareHelper.shareWechat(context, title, content, url, "");
			break;
			
		case R.id.llShareWxMoment:
			ShareHelper.shareWechatMoment(context, title, content, url, "");
			break;
			
		case R.id.llShareQQ:
			ShareHelper.shareQQ(context, title, content, url,  "");
			break;
			
		case R.id.llShareQZone:
			ShareHelper.shareQZone(context, title, content, url,  "");
			break;
		}
	}


	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		odismiss();
	}
}
