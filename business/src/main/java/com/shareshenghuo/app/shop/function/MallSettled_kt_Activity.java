package com.shareshenghuo.app.shop.function;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.shareshenghuo.app.shop.BaseTopActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker.NewMessageCallback;

/**
 * 
 * 商家入驻-开通
 */
public class MallSettled_kt_Activity extends BaseTopActivity implements
		NewMessageCallback, OnClickListener {

	private NewChatMsgWorker newMsgWatcher;// 监听聊天

	private TextView shangjiaruzhu_jbxx_text;// 基本信息，标题
	private TextView real_pointimg;// 基本信息图标
	private TextView tvs1;// 基本信息后线

	private TextView shangjiaruzhu_sczl_text;// 上传资料，标题
	private TextView real_pointimg1;// 上传资料图标

	private TextView tvs2;// 审核前线
	private TextView shangjiaruzhu_sh_text;// 审核，标题
	private TextView real_pointimg2;// 审核图标

	private TextView tvs3;// 开通前线
	private TextView shangjiaruzhu_kt_text;// 开通，标题
	private TextView real_pointimg3;// 开通图标

	private TextView ktok_htdl_text;// 后台登录,显示文本
	private TextView ktok_dlzh_text;// 后台登录的账号
	private String url = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_settled_kt);
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		url = bundle.getString("url");
		initView();
	}

	public void initView() {
		initTopBar("商城入驻");

		shangjiaruzhu_jbxx_text = getView(R.id.shangjiaruzhu_jbxx_text);// 基本信息，下部文字

		real_pointimg = getView(R.id.real_pointimg);// 基本信息图标
		real_pointimg.setBackgroundResource(R.drawable.bg_circle_orange);// 基本信息圆圈，红
		real_pointimg.setTextColor(getResources().getColor(R.color.text_white));// 文字颜色设置白

		tvs1 = getView(R.id.tvs1);// 基本信息后竖条
		tvs1.setBackgroundResource(R.color.text_orange);

		tvs2 = getView(R.id.tvs2);// 上传资料后竖条
		tvs2.setBackgroundResource(R.color.text_orange);

		tvs3 = getView(R.id.tvs3);// 开通前的竖条
		tvs3.setBackgroundResource(R.color.text_orange);

		shangjiaruzhu_sczl_text = getView(R.id.shangjiaruzhu_sczl_text);// 上传资料，下部文字
		shangjiaruzhu_sczl_text.setTextColor(getResources().getColor(
				R.color.text_orange));
		real_pointimg1 = getView(R.id.real_pointimg1);// 上传资料，图标
		real_pointimg1.setBackgroundResource(R.drawable.bg_circle_orange);// 上传资料圆圈，红
		real_pointimg1.setTextColor(getResources().getColor(R.color.text_white));// 上传资料文字颜色设置白

		shangjiaruzhu_sh_text = getView(R.id.shangjiaruzhu_sh_text);// 审核文字
		shangjiaruzhu_sh_text.setTextColor(getResources().getColor(
				R.color.text_orange));

		real_pointimg2 = getView(R.id.real_pointimg2);// 审核，图标
		real_pointimg2.setBackgroundResource(R.drawable.bg_circle_orange);// 审核圆圈，红
		real_pointimg2.setTextColor(getResources().getColor(R.color.text_white));// 审核文字颜色设置白

		shangjiaruzhu_kt_text = getView(R.id.shangjiaruzhu_kt_text);// 开通文字
		shangjiaruzhu_kt_text.setTextColor(getResources().getColor(
				R.color.text_orange));

		real_pointimg3 = getView(R.id.real_pointimg3);// 开通，图标
		real_pointimg3.setBackgroundResource(R.drawable.bg_circle_orange);// 开通圆圈，红
		real_pointimg3.setTextColor(getResources().getColor(R.color.text_white));// 开通文字颜色设置白

		ktok_dlzh_text = getView(R.id.ktok_dlzh_text);// 登录账号的显示
		ktok_dlzh_text.setTag("登录账号：");
		ktok_dlzh_text.setText(ktok_dlzh_text.getTag().toString()
				+"\n"+ UserInfoManager.getAccount(MallSettled_kt_Activity.this));

		ktok_htdl_text = getView(R.id.ktok_htdl_text);// 登录地址的显示
		ktok_htdl_text.setTag("后台登录：");
		ktok_htdl_text.setText(ktok_htdl_text.getTag().toString() +"\n"+ url);

		newMsgWatcher = new NewChatMsgWorker(this, this);
		newMsgWatcher.startWork();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		}
	}

	@Override
	public void newMessage(int which) {

	}

}
