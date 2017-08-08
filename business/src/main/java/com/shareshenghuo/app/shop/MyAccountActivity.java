package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.IncomeLogAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.IncomeInfo;
import com.shareshenghuo.app.shop.network.bean.UserInfo;
import com.shareshenghuo.app.shop.network.request.IncomeLogRequest;
import com.shareshenghuo.app.shop.network.response.IncomeLogResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.T;

public class MyAccountActivity extends BaseTopActivity implements OnRefreshListener2<ListView> {
	
	private PullToRefreshListView lvData;
	
	private int pageNo = 1;
	private int pageSize = 20;
	private IncomeLogAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);
		init();
		loadData();
	}
	
	public void init() {
		initTopBar("我的账户");
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("提现");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(TextUtils.isEmpty(UserInfoManager.getUserInfo(MyAccountActivity.this).alipay_account)) {
					T.showShort(MyAccountActivity.this, "未设置体现帐号，请联系管理员");
					return;
				}
				withdraw();
			}
		});
		
		lvData = getView(R.id.lvData);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		UserInfo user = UserInfoManager.getUserInfo(this);
		setText(R.id.tvUnconfirmMoney, "¥"+user.with_drawals_ing_fee);
		setText(R.id.tvCanWithdrawMoney, "¥"+user.money);
		setText(R.id.tvHasWithdrawMoney, "¥"+user.with_drawals_ed_fee);
	}

	public void loadData() {
		IncomeLogRequest req = new IncomeLogRequest();
		req.shop_id = UserInfoManager.getUserInfo(this).shop_id+"";
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_INCOME_LOG, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
				T.showNetworkError(MyAccountActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				IncomeLogResponse bean = new Gson().fromJson(resp.result, IncomeLogResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					updateView(bean.data);
				}
			}
		});
	}
	
	public void updateView(List<IncomeInfo> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new IncomeLogAdapter(this, data);
			lvData.setAdapter(adapter);
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData();
	}
	
	private int index = 0;
	public void withdraw() {
//		String[] items = {"提现到支付宝", "提现到微信钱包", "提现到银行卡"};
//		new AlertDialog.Builder(this).setTitle("请选择提现方式")
//		.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int which) {
//				index = which;
//			}
//		})
//		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface arg0, int arg1) {
//				Intent it = new Intent(MyAccountActivity.this, WithdrawActivity.class);
//				it.putExtra("source", index+1);
//				startActivity(it);
//			}
//		})
//		.setNegativeButton("取消", null)
//		.show();
		
		Intent it = new Intent(MyAccountActivity.this, WithdrawActivity.class);
		it.putExtra("source", UserInfoManager.getUserInfo(this).source);
		startActivity(it);
	}
}
