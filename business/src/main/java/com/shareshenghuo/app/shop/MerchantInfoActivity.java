package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.manager.ImageLoadManager;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.ShopInfoBean;
import com.shareshenghuo.app.shop.network.bean.UserInfo;
import com.shareshenghuo.app.shop.network.request.ShopInfoRequest;
import com.shareshenghuo.app.shop.network.response.ShopInfoResponse;
import com.shareshenghuo.app.shop.network.response.UpdataBankInfoResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.Util;
import com.shareshenghuo.app.shop.util.ViewUtil;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;



public class MerchantInfoActivity extends BaseTopActivity{
	
	private ImageView head_img,bank_img,explain;
	private TextView tv_branch,card_no, bank_name,tvname1, tvname2,tvname3,tvname4,tvname5,tvname6,tvname7,tvname8,tvname9,tvname11,tvname12;
	private LinearLayout ll_bank;
	private Button llWalletRecharge;
	private String bankCode,bank_address;
	private TwoButtonDialog downloadDialog;
	private LinearLayout ll_balan;
	private RelativeLayout re_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchantinfo_activity);
		initView();
		loadData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		initTopBar("商户信息");
		head_img = getView(R.id.head_img);
		UserInfo user = UserInfoManager.getUserInfo(this);
		if(!TextUtils.isEmpty(user.user_photo)){
			ImageLoadManager.getInstance(this).displayHeadIconImage(user.user_photo, head_img);
		}
		tvname9 = getView(R.id.tvname9);
		tvname1 = getView(R.id.tvname1);
		tvname2 = getView(R.id.tvname2);
		tvname3 = getView(R.id.tvname3);
		tvname4 = getView(R.id.tvname4);
		tvname5 = getView(R.id.tvname5);
		tvname6 = getView(R.id.tvname6);
		tvname7 = getView(R.id.tvname7);
		tvname8 = getView(R.id.tvname8);
		card_no = getView(R.id.card_no);
		tvname11 = getView(R.id.tvname11);
		tvname12 = getView(R.id.tvname12);
		ll_balan= getView(R.id.ll_balan);
		bank_name = getView(R.id.bank_name);
		bank_img = getView(R.id.bank_img);
		ll_bank = getView(R.id.ll_bank);
		tv_branch = getView(R.id.tv_branch);
		re_title = getView(R.id.re_title);
		llWalletRecharge = getView(R.id.llWalletRecharge);
		tv_branch.setEnabled(false);
		re_title.setEnabled(false);
		explain = getView(R.id.explain);
		tv_branch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(!TextUtils.isEmpty(bank_name.getText())){
					// TODO Auto-generated method stub
					Intent it = new Intent(MerchantInfoActivity.this,SearchActivty.class);
					it.putExtra("bankname", bank_name.getText().toString());
					startActivityForResult(it, 100);
				}else{
					T.showShort(MerchantInfoActivity.this, "未获取到银行信息");
				}
			}
		});
		re_title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(bank_name.getText())){
					// TODO Auto-generated method stub
					Intent it = new Intent(MerchantInfoActivity.this,SearchActivty.class);
					it.putExtra("bankname", bank_name.getText().toString());
					startActivityForResult(it, 100);
				}else{
					T.showShort(MerchantInfoActivity.this, "未获取到银行信息");
				}
			}
		});
		llWalletRecharge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(bankCode)){
					T.showShort(MerchantInfoActivity.this, "请选择支行");
					return;
				}
				if(TextUtils.isEmpty(bank_address)){
					T.showShort(MerchantInfoActivity.this, "请选择支行");
					return;
				}
				savData();
			}
		});
		explain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initDialog1("所在支行填写说明","1.请正确填写结算储蓄卡开户的所在支行全程,填错会导致商家货款收取失败;\n2.若填写错误可拨打对应银行客服电话进行支行地址查询后填写;\n3.若输入的支行名称无法通过,请更换银行卡后重新填写支行地址.", "知道了","");
			}
		});
	}
	
	
	private void initDialog1(String title,String content,String left,String right) {
		// TODO Auto-generated method stub
		downloadDialog = new TwoButtonDialog(this, R.style.CustomDialog,
				title, content, left, right,true,new OnMyDialogClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
		}
	
	public void loadData() {
		ShopInfoRequest req = new ShopInfoRequest();
		req.shop_id = UserInfoManager.getUserInfo(this).shop_id;
		req.latitude = "0";
		req.longitude="0";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETSHOPDETAILS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ShopInfoResponse bean = new Gson().fromJson(resp.result, ShopInfoResponse.class);
				Log.e("", " = == = =  "+resp.result);
				if(Api.SUCCEED == bean.result_code) {
//					UserInfoManager.saveUserInfo(MerchantInfoActivity.this, bean.data.);
					updateView(bean.data.shop_info);
				}
			}

		});
	}
	
	public void savData() {
		ShopInfoRequest req = new ShopInfoRequest();
		req.shopId = UserInfoManager.getUserInfo(this).shop_id;
		req.bankCode = bankCode;
		req.bankAddress = bank_address;
		Log.e("", ""+req.toString());
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Log.e("", ""+Api.UPDATEBANK);
		new HttpUtils().send(HttpMethod.POST, Api.UPDATEBANK, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				Log.e("", ""+resp.result);
				UpdataBankInfoResponse bean = new Gson().fromJson(resp.result, UpdataBankInfoResponse.class);
				if(Api.SUCCEED == bean.result_code) {
						initDialog1("提示",bean.data, "确定","");
						if(bean.data.equals("操作成功")){
							ll_bank.setVisibility(View.GONE);
							tv_branch.setEnabled(false);
							re_title.setEnabled(false);
						}else{
							ll_bank.setVisibility(View.VISIBLE);
							tv_branch.setEnabled(true);
							re_title.setEnabled(true);
						}
				}else{
					initDialog1("提示","操作失败", "确定","");
					ll_bank.setVisibility(View.VISIBLE);
					tv_branch.setEnabled(true);
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==100&&data!=null){
			bankCode= data.getStringExtra("code");
			bank_address = data.getStringExtra("bank_key");
			tv_branch.setText(bank_address);
//			Log.e("", data.getStringExtra("bank_key"));
//			Log.e("", data.getStringExtra("code"));

		}
	}

	private void updateView(ShopInfoBean data) {
		
		// TODO Auto-generated method stub
		tvname1.setText(data.shop_name);
		tvname2.setText(data.id);
		tvname3.setText(data.address);
		tvname4.setText(data.shop_type_name);
		tvname5.setText(data.shop_child_type_name);
		if(!TextUtils.isEmpty(data.risk_money)){
			tvname8.setText(Util.getfotmatnum(data.risk_money, false,1));
			
//			if(!TextUtils.isEmpty(data.expiration_date)){
//				tvname9.setText("("+data.expiration_date+"到期)");
//			}
		}
		
		if(!TextUtils.isEmpty(data.expiration_date)){
			tvname11.setText(Util.getfotmatnum(data.temp_risk_money, false,1)+"\n"+"("+data.expiration_date+"到期)");
//			tvname12.setText();
		}else{
			ll_balan.setVisibility(View.GONE);
		}
//		if(TextUtils.isEmpty(data.bank_code)){
//			ll_bank.setVisibility(View.VISIBLE);
//			re_title.setEnabled(true);
//			tv_branch.setEnabled(true);
//		}else{
//			ll_bank.setVisibility(View.GONE);
//			re_title.setEnabled(false);
//			tv_branch.setEnabled(false);
//		}
//		
//		
//		if(!TextUtils.isEmpty(data.alipay_account)){
//			String card = data.alipay_account;
//			card_no.setText(card.substring(0, 4)+" **** **** "+card.substring(card.length()-4));
//		}
//		
//		if(!TextUtils.isEmpty(data.bank_name)){
//			String bank = data.bank_name;
//			bank_name.setText(bank);
//			ViewUtil.setbank(bank_img,bank);
//		}
//		if(!TextUtils.isEmpty(data.bank_address)){
//			tv_branch.setText(data.bank_address);
//		}
	}
}
