package com.shareshenghuo.app.shop.function;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shareshenghuo.app.shop.BaseTopActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker.NewMessageCallback;
import com.shareshenghuo.app.shop.util.MallStringUtils;

/**
 * 
 * 商家入驻-审核
 */
public class MallSettled_sh_Activity extends BaseTopActivity implements
		NewMessageCallback, OnClickListener {

	private NewChatMsgWorker newMsgWatcher;// 监听聊天

	private TextView shangjiaruzhu_jbxx_text;// 基本信息，标题
	private TextView real_pointimg;// 基本信息图标
	private TextView tvs1;// 基本信息后线

	private TextView shangjiaruzhu_sczl_text;// 上传资料，标题
	private TextView real_pointimg1;// 上传资料图标

	private TextView tvs2;//
	private TextView shangjiaruzhu_sh_text;// 审核，标题
	private TextView real_pointimg2;// 审核图标

	private LinearLayout shenme_ok_layout, shenme_error_layout;
	private Button chongxin_tijiao_btn;// 重新提交的

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_settled_sh);
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

		newMsgWatcher = new NewChatMsgWorker(this, this);
		newMsgWatcher.startWork();

		shenme_ok_layout = getView(R.id.shenme_ok_layout);// 等待的审核界面
		shenme_error_layout = getView(R.id.shenme_error_layout);// 审核失败的界面
		if (MallStringUtils.mallState == -1) {// 未通过审核
			shenme_ok_layout.setVisibility(View.GONE);
			shenme_error_layout.setVisibility(View.VISIBLE);
		} else {
			shenme_ok_layout.setVisibility(View.VISIBLE);
			shenme_error_layout.setVisibility(View.GONE);
		}
		chongxin_tijiao_btn = getView(R.id.chongxin_tijiao_btn);
		getView(R.id.chongxin_tijiao_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chongxin_tijiao_btn:
			finish();
			Intent i = new Intent();
			i.setClass(MallSettled_sh_Activity.this, MallSettledActivity.class);
			startActivity(i);
			break;
		}
	}

	@Override
	public void newMessage(int which) {

	}

}
