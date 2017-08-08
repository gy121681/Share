package com.shareshenghuo.app.shop.function;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shareshenghuo.app.shop.BaseTopActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.city.CityPicker;
import com.shareshenghuo.app.shop.city.onChoiceCytyChilListener;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker.NewMessageCallback;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;

/**
 * 
 * 商家入驻
 */
public class MallSettledActivity extends BaseTopActivity implements
		NewMessageCallback, OnClickListener {

	public static MallSettledActivity instance = null;

	private NewChatMsgWorker newMsgWatcher;// 监听聊天

	private EditText jingyingfenlei_text;// 经营分类---文本框
	private TextView suozaidiqu_text;// 所在地区---文本框
	private EditText yingyezhizhao_text;// 营业执照---文本框
	private EditText shehuitongyima_text;// 社会统一码---文本框
	private EditText farenxingming_text;// 法人姓名---文本框
	private EditText farenshenfenzhenghao_text;// 法人身份证号---文本框
	private EditText lianxifangshi_text;// 联系方式---文本框

	private Button btn_mall_settled_jbxx_next;// 商城入驻，下一步

	private TextView shangjiaruzhu_jbxx_text;// 基本信息，标题
	private TextView real_pointimg;// 基本信息图标
	private TextView tvs1;// 基本信息后线

	private LinearLayout lin_cascade;// 时间控件的布局
	private CityPicker citypicker;
	public static String bankProvinceid = "广东省", bankCityid = "深圳市",
			bankareid = "罗湖区";
	private TextView tv_comit;// 地区选择，完成
	private TextView tv_finishs;// 地区选择，返回

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_settled);
		instance = this;
		initView();// 加载本界面.
	}

	public void initView() {
		initTopBar("商家入驻");
		jingyingfenlei_text = getView(R.id.jingyingfenlei_text);// 经营分类
		suozaidiqu_text = getView(R.id.suozaidiqu_text);// 所在地区
		yingyezhizhao_text = getView(R.id.yingyezhizhao_text);// 营业执照
		shehuitongyima_text = getView(R.id.shehuitongyima_text);// 社会统一码
		farenxingming_text = getView(R.id.farenxingming_text);// 法人姓名
		farenshenfenzhenghao_text = getView(R.id.farenshenfenzhenghao_text);// 法人身份证号
		lianxifangshi_text = getView(R.id.lianxifangshi_text);// 联系方式

		btn_mall_settled_jbxx_next = getView(R.id.btn_mall_settled_jbxx_next);// 商城入住-下一步

		getView(R.id.jingyingfelei_layout).setOnClickListener(this);
		getView(R.id.suozaidiqu_layout).setOnClickListener(this);
		getView(R.id.yingyezhizhaomingcheng_layout).setOnClickListener(this);
		getView(R.id.shehuitongyima_layout).setOnClickListener(this);
		getView(R.id.farenxingming_layout).setOnClickListener(this);
		getView(R.id.farenshenfenzhenghao_layout).setOnClickListener(this);
		getView(R.id.lianxifangshi_layout).setOnClickListener(this);
		getView(R.id.btn_mall_settled_jbxx_next).setOnClickListener(this);

		shangjiaruzhu_jbxx_text = (TextView) findViewById(R.id.shangjiaruzhu_jbxx_text);// 基本信息

		real_pointimg = (TextView) findViewById(R.id.real_pointimg);// 基本信息图标
		real_pointimg.setBackgroundResource(R.drawable.bg_circle_orange);
		real_pointimg.setTextColor(getResources().getColor(R.color.text_white));

		tvs1 = (TextView) findViewById(R.id.tvs1);// 基本信息后竖条
		// tvs1.setBackgroundResource(R.color.black);

		citypicker = getView(R.id.citypicker);
		lin_cascade = getView(R.id.lin_cascade);

		tv_comit = getView(R.id.tv_comit);
		tv_comit.setOnClickListener(this);

		tv_finishs = getView(R.id.tv_finishs);
		tv_finishs.setOnClickListener(this);

		newMsgWatcher = new NewChatMsgWorker(this, this);
		newMsgWatcher.startWork();
		initdata();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.jingyingfelei_layout:
			break;
		case R.id.suozaidiqu_layout:// 所在地区点击事件
			btn_mall_settled_jbxx_next.setEnabled(false);
			initsity();
			break;
		case R.id.yingyezhizhaomingcheng_layout:
			break;
		case R.id.shehuitongyima_layout:
			break;
		case R.id.farenxingming_layout:
			break;
		case R.id.farenshenfenzhenghao_layout:
			break;
		case R.id.lianxifangshi_layout:
			break;
		case R.id.btn_mall_settled_jbxx_next:// 商城入驻——基本信息——下一步
			if (isTextNotNull()) {
				Intent i = new Intent();
				i.setClass(MallSettledActivity.this,
						MallSettled_sczl_Activity.class);
				Bundle bundle = new Bundle();
				bundle.putString("yyzzmc", yingyezhizhao_text.getText()
						.toString().trim());// 营业执照名称
				bundle.putString("shtym", shehuitongyima_text.getText()
						.toString().trim());// 社会统一码
				bundle.putString("frxm", farenxingming_text.getText()
						.toString().trim());// 法人姓名
				bundle.putString("frsfzh", farenshenfenzhenghao_text.getText()
						.toString().trim());// 法人身份证号
				bundle.putString("frlxfs", lianxifangshi_text.getText()
						.toString().trim());// 法人联系方式

				bundle.putString("jyfl", jingyingfenlei_text.getText()
						.toString().trim());
				System.out.println("===appid"
						+ UserInfoManager.getAppId(MallSettledActivity.this)
						+ ",acc"
						+ UserInfoManager
								.getMallShopId(MallSettledActivity.this));// 用户标识);
				i.putExtras(bundle);
				startActivity(i);
			}
			break;
		case R.id.tv_comit:
			btn_mall_settled_jbxx_next.setEnabled(true);
			tv_comit.setEnabled(true);
			lin_cascade.setVisibility(View.INVISIBLE);
			citypicker.setfirstdata();
			break;
		case R.id.tv_finishs:
			btn_mall_settled_jbxx_next.setEnabled(true);
			lin_cascade.setVisibility(View.INVISIBLE);
			break;
		}
	}

	/**
	 * 
	 * @return判断输入框是否数据完整
	 */
	private boolean isTextNotNull() {
		int num = 0;
		if (yingyezhizhao_text.getText().toString().trim().length() == 0) {
			num = num + 1;
		}
		if (shehuitongyima_text.getText().toString().trim().length() == 0) {
			num = num + 1;
		}
		if (farenxingming_text.getText().toString().trim().length() == 0) {
			num = num + 1;
		}
		if (farenshenfenzhenghao_text.getText().toString().trim().length() == 0) {
			num = num + 1;
		}
		if (lianxifangshi_text.getText().toString().trim().length() == 0) {
			num = num + 1;
		}
		if (num > 0) {
			T.showShort(MallSettledActivity.this, "请确认提交的资料是否齐全!");
			return false;
		} else {
			return true;
		}

	}

	private void initdata() {
		citypicker.getcity(new onChoiceCytyChilListener() {
			@Override
			public void onClick(String i, String v, String a, String ni,
					String vi, String ai) {
				bankProvinceid = i;
				bankCityid = v;
				bankareid = a;
				try {
					suozaidiqu_text.setText(ni + "/" + vi + "/" + ai);
				} catch (Exception e) {
				}
			}
		});
	}

	public void initsity() {

		if (lin_cascade.getVisibility() == View.VISIBLE) {
			lin_cascade.setVisibility(View.INVISIBLE);
		} else {
			lin_cascade.setVisibility(View.VISIBLE);
		}
	}

	/*
	 * 获取商家申请状态,根据不同状态,进入不同的界面
	 */
	class getSelectMallStatus extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			ProgressDialogUtil.showProgressDlg(MallSettledActivity.this,
					"正在加载中,请稍后...");
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			ProgressDialogUtil.dismissProgressDlg();
		}
	}

	@Override
	public void newMessage(int which) {

	}

}
