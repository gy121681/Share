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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

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
import com.shareshenghuo.app.shop.adapter.PointLogAdapter;
import com.shareshenghuo.app.shop.adapter.VipManageAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.PointLogInfo;
import com.shareshenghuo.app.shop.network.bean.UserInfo;
import com.shareshenghuo.app.shop.network.bean.VipInfo;
import com.shareshenghuo.app.shop.network.request.CardUseLogRequest;
import com.shareshenghuo.app.shop.network.request.RechargeConsumeRequest;
import com.shareshenghuo.app.shop.network.response.BaseResponse;
import com.shareshenghuo.app.shop.network.response.CalcDiscountResponse;
import com.shareshenghuo.app.shop.network.response.CardUseLogResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.Arith;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;

/**
 * @author hang
 * 会员详情
 */
public class VipDetailActivity extends BaseTopActivity implements OnCheckedChangeListener, OnRefreshListener2<ListView>, OnClickListener {
	
	private static final int REQ_PICK_PROD_CATEGORY = 0x101;
	private static final int REQ_PICK_PROD = 0x102;
	
	private PullToRefreshListView lvData;
	private LinearLayout llRecharge, llSpinner, llConsume1, llConsume2, llExchange;
	private EditText edRecharge, edConsume, edPointExchange;
	private EditText edSearch;
	private TextView tvPayType;
	private EditText edRemark;
	private Button btnCalc;
	private TextView tvDiscount;
	private Button btnConsume;
	
	private VipInfo vipInfo;
	
	private PointLogAdapter adapter;
	private int logType = 3;
	private int pageNo = 1;
	private int pageSize = 30;
	
//	private int prodTypeId = -1;
//	private String prodName;
	
	private int payType = 1;  //1 卡内余额   2 现金消费

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vip_detail);
		initView();
		loadData();
	}
	
	public void initView() {
		vipInfo = (VipInfo) getIntent().getSerializableExtra("vipInfo");
		
		initTopBar("会员详情");
		llRecharge = (LinearLayout) findViewById(R.id.llHomeRecharge);
		llSpinner = (LinearLayout) findViewById(R.id.llHomeSpinner);
		llConsume1 = (LinearLayout) findViewById(R.id.llHomeConsume1);
		llConsume2 = (LinearLayout) findViewById(R.id.llHomeConsume2);
		llExchange = (LinearLayout) findViewById(R.id.llHomeExchange);
		edRecharge = (EditText) findViewById(R.id.edHomeRecharge);
		edConsume = (EditText) findViewById(R.id.edHomeConsume);
		edPointExchange = (EditText) findViewById(R.id.edHomePointExchange);
		edSearch = (EditText) findViewById(R.id.edSearch);
		tvPayType = (TextView) findViewById(R.id.tvHomePayType);
		edRemark = (EditText) findViewById(R.id.edHomeRemark);
		btnCalc = (Button) findViewById(R.id.btnHomeCalc);
		tvDiscount = (TextView) findViewById(R.id.tvHomeDiscount);
		btnConsume = (Button) findViewById(R.id.btnHomeConsume);
		
		lvData = getView(R.id.lvData);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		
		setText(R.id.tvCardDesc, vipInfo.user_level_desc);
		setText(R.id.tvCardName, vipInfo.real_name+" "+vipInfo.mobile);
		setText(R.id.tvCardNo, "NO."+vipInfo.card_no);
		setText(R.id.tvCardPoint, "当前积分："+vipInfo.point);
		setText(R.id.tvCardMoney, "余额："+vipInfo.money);
		
		llRecharge.setVisibility(View.VISIBLE);
		llSpinner.setVisibility(View.GONE);
		llConsume1.setVisibility(View.GONE);
		llConsume2.setVisibility(View.GONE);
		llExchange.setVisibility(View.GONE);
		
		((RadioGroup)findViewById(R.id.rgTab)).setOnCheckedChangeListener(this);
		findViewById(R.id.btnHomeRecharge).setOnClickListener(this);
		tvPayType.setOnClickListener(this);
		btnCalc.setOnClickListener(this);
		btnConsume.setOnClickListener(this);
    	findViewById(R.id.btnHomePointExchange).setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int id) {
		switch(id) {
		case R.id.rbTab1:
			logType = 3;
			onPullDownToRefresh(lvData);
			
			llRecharge.setVisibility(View.VISIBLE);
			llSpinner.setVisibility(View.GONE);
			llConsume1.setVisibility(View.GONE);
			llConsume2.setVisibility(View.GONE);
			llExchange.setVisibility(View.GONE);
			break;
			
		case R.id.rbTab2:
			logType = 2;
			onPullDownToRefresh(lvData);
			
			llRecharge.setVisibility(View.GONE);
			llSpinner.setVisibility(View.VISIBLE);
			llConsume1.setVisibility(View.VISIBLE);
			llConsume2.setVisibility(View.VISIBLE);
			tvPayType.setVisibility(View.VISIBLE);
			llExchange.setVisibility(View.GONE);
			break;
			
		case R.id.rbTab3:
			logType = 1;
			onPullDownToRefresh(lvData);
			
			llRecharge.setVisibility(View.GONE);
			llSpinner.setVisibility(View.VISIBLE);
			llConsume1.setVisibility(View.GONE);
			llConsume2.setVisibility(View.GONE);
			tvPayType.setVisibility(View.GONE);
			llExchange.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	public void loadData() {
		CardUseLogRequest req = new CardUseLogRequest();
		req.user_id = vipInfo.user_id+"";
		req.shop_id = UserInfoManager.getUserInfo(this).shop_id+"";
		req.type = logType+"";
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_CARD_USE_LOG, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
				T.showNetworkError(VipDetailActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				CardUseLogResponse bean = new Gson().fromJson(resp.result, CardUseLogResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					updateView(bean.data);
				} else {
					T.showShort(VipDetailActivity.this, bean.result_desc);
				}
			}
		});
	}
	
	public void updateView(List<PointLogInfo> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new PointLogAdapter(this, data);
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

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnHomeRecharge:
			showConfirmDlg(3, vipInfo.account, edRecharge.getText().toString());
			break;
			
		case R.id.btnHomeConsume:
			showConfirmDlg(2, vipInfo.account, tvDiscount.getText().toString());
			break;
			
		case R.id.btnHomePointExchange:
			showConfirmDlg(1, vipInfo.account, edPointExchange.getText().toString());
			break;
			
//		case R.id.tvHomeProdCategory:
//			startActivityForResult(new Intent(this, ProdCategoryActivity.class), REQ_PICK_PROD_CATEGORY);
//			break;
//			
//		case R.id.tvHomeProdName:
//			if(prodTypeId == -1) {
//				T.showShort(this, "请先选择商品分类");
//				return;
//			}
//			Intent prod = new Intent(this, PickProdActivity.class);
//			prod.putExtra("typeId", prodTypeId);
//			startActivityForResult(prod, REQ_PICK_PROD);
//			break;
			
		case R.id.tvHomePayType:
			showPayTypeDlg();
			break;
			
		case R.id.btnHomeCalc:
			if(ViewUtil.checkEditEmpty(edConsume, "请输入金额"))
				return;
			calcDiscount(edConsume.getText().toString());
			break;
		}
	}
	
	String[] payTypeValues = new String[]{"卡内余额", "现金消费"};
	/**
	 * 显示消费方式选择框
	 */
	public void showPayTypeDlg() {
		new AlertDialog.Builder(this).setItems(payTypeValues, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int which) {
				payType = which+1;
				tvPayType.setText(payTypeValues[which]);
			}
		}).show();
	}
	
	/**
	 * 计算折扣
	 */
	public void calcDiscount(String fee) {
		ProgressDialogUtil.showProgressDlg(VipDetailActivity.this, "");
		RechargeConsumeRequest req = new RechargeConsumeRequest();
		UserInfo user = UserInfoManager.getUserInfo(this);
		req.shop_id = user.shop_id+"";
		req.user_id = vipInfo.user_id+"";
		req.fee = fee;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_CALC_DISCOUNT, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				CalcDiscountResponse bean = new Gson().fromJson(resp.result, CalcDiscountResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					btnConsume.setEnabled(true);
					tvDiscount.setText(bean.data.zhe_kou_price+"");
				} else {
					T.showShort(VipDetailActivity.this, bean.result_desc);
				}
			}
			
			@Override
			public void onFailure(HttpException arg0, String msg) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(VipDetailActivity.this);
			}
		});
	}
	
	public void showConfirmDlg(final int type, final String account, final String fee) {
		if(TextUtils.isEmpty(fee)) {
			T.showShort(this, "请输入金额");
			return;
		}
		
		StringBuilder msg = new StringBuilder();
		switch(type) {
		case 1:
			//积分
//			if(TextUtils.isEmpty(prodName)) {
//				T.showShort(this, "请选择商品");
//				return;
//			}
			msg.append("确定要为").append(account).append("扣减").append(fee).append("积分？");
			break;
			
		case 2:
			//消费
//			if(TextUtils.isEmpty(prodName)) {
//				T.showShort(this, "请选择商品");
//				return;
//			}
			
			if(TextUtils.isEmpty(edConsume.getText()) || TextUtils.isEmpty(tvDiscount.getText())) {
				T.showShort(VipDetailActivity.this, "请先计算折后价格");
				return;
			}
			
			msg.append("确定要为").append(account).append("扣减").append(fee).append("元？");
			break;
			
		case 3:
			//充值
			msg.append("确定要为").append(account).append("充值").append(fee).append("元？");
			break;
		}
		
		new AlertDialog.Builder(this).setTitle("温馨提示")
			.setMessage(msg.toString())
			.setNegativeButton("取消", null)
			.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					ProgressDialogUtil.showProgressDlg(VipDetailActivity.this, "");
					RechargeConsumeRequest req = new RechargeConsumeRequest();
					req.admin_id = UserInfoManager.getUserId(VipDetailActivity.this)+"";
					req.search_name = account;
					req.shop_id = UserInfoManager.getUserInfo(VipDetailActivity.this).shop_id+"";
					req.type = type+"";
					req.fee = fee;
					if (type != 3) {
						if(type == 2)
							req.pay_type = payType+"";
						else
							req.pay_type = "1";
						req.product_name = edRemark.getText().toString();
					}
					RequestParams params = new RequestParams("utf-8");
					try {
						params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					new HttpUtils().send(HttpMethod.POST, Api.URL_RECHAR_CONSUME, params, new RequestCallBack<String>() {
						@Override
						public void onSuccess(ResponseInfo<String> resp) {
							ProgressDialogUtil.dismissProgressDlg();
							BaseResponse bean = new Gson().fromJson(resp.result, BaseResponse.class);
							if(Api.SUCCEED == bean.result_code) {
								T.showShort(VipDetailActivity.this, "成功");
								onPullDownToRefresh(lvData);
								setResult(RESULT_OK);
								
								try {
									double dFee = Double.parseDouble(fee);
									switch(type) {
									case 3:
										//充值
										vipInfo.money = Arith.add(vipInfo.money, dFee);
										setText(R.id.tvCardMoney, "余额："+vipInfo.money);
										break;
										
									case 2:
										//消费
										vipInfo.money = Arith.sub(vipInfo.money, dFee);
										setText(R.id.tvCardMoney, "余额："+vipInfo.money);
										break;
										
									case 1:
										//积分
										vipInfo.point = (int) (vipInfo.point - dFee);
										setText(R.id.tvCardPoint, "当前积分："+vipInfo.point);
										break;
									}
								} catch(Exception e) {
									e.printStackTrace();
								}
							} else {
								T.showShort(VipDetailActivity.this, bean.result_desc);
							}
						}
						
						@Override
						public void onFailure(HttpException arg0, String arg1) {
							ProgressDialogUtil.dismissProgressDlg();
							T.showNetworkError(VipDetailActivity.this);
						}
					});
				}
			})
			.show();
	}
	
	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		if(resCode == RESULT_OK) {
//			switch(reqCode) {
//			case REQ_PICK_PROD_CATEGORY:
//				prodTypeId = data.getIntExtra("prodTypeId", 0);
//				tvProdCategory.setText(data.getStringExtra("prodTypeName"));
//				break;
//				
//			case REQ_PICK_PROD:
//				prodName = data.getStringExtra("prodName");
//				tvProdName.setText(prodName);
//				break;
//			}
		}
	}
}
