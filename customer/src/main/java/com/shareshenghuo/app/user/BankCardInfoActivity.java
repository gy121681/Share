package com.shareshenghuo.app.user;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.BankListBean;
import com.shareshenghuo.app.user.network.request.CityRequest;
import com.shareshenghuo.app.user.network.request.ReBankCardRequest;
import com.shareshenghuo.app.user.network.response.AutResponse;
import com.shareshenghuo.app.user.network.response.BanklListResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.util.ViewUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BankCardInfoActivity extends BaseTopActivity{
	
private EditText eibankcardnum,edname,eidcard;
	
	private TextView eibankcard,recardname,recard;
	
	private String banknum = "";
	
	private BanklListResponse bean;
	
	
	
	
	private ImageView card_img;
	
	private LinearLayout llname,llcard;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_cardinfo);
		
      
		initview1();
		if(bean==null){
			initbank();
		}

    }

	
	
	private void initview1() {
		// TODO Auto-generated method stub
		initTopBar("银行卡修改");
		eibankcard = getView(R.id.eibankcard);
		edname = getView(R.id.edname);
		eidcard = getView(R.id.eidcard);
		llname = getView(R.id.llname);
		llname.setVisibility(View.GONE);
		eidcard.setEnabled(true);
		
		recardname = getView(R.id.recardname);
		recard =getView(R.id.recard);
		llcard = getView(R.id.llcard);
		llcard.setVisibility(View.VISIBLE);
		card_img = getView(R.id.card_img);
		
		if(!TextUtils.isEmpty(UserInfoManager.getUserInfo(this).card_no)&&UserInfoManager.getUserInfo(this).card_no.length()>4){
			String card = UserInfoManager.getUserInfo(this).card_no;
			recard.setText(card.substring(0, 4)+" **** **** "+card.substring(card.length()-4));
		}
		
		if(!TextUtils.isEmpty(UserInfoManager.getUserInfo(this).bank_name)){
			String bank = UserInfoManager.getUserInfo(this).bank_name;
			setbank(card_img,bank);
		}
		if(!TextUtils.isEmpty(UserInfoManager.getUserInfo(this).real_name)){
			String bank = UserInfoManager.getUserInfo(this).real_name;
			recardname.setText(bank);
		}
		
		eibankcardnum = getView(R.id.eibankcardnum);
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setText("提交");
		btnTopRight1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				rebank();
			}

		});
		eibankcard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initdialog();
			}
		});
		
	}
	
	
	private void initdialog() {
		// TODO Auto-generated method stub

		final Dialog dialog = new Dialog(this);
		ListView lv = new ListView(this); 
		ProvinceAdapter pAdapter = new ProvinceAdapter(bean.data);
		lv.setAdapter(pAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View initbank, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				banknum = bean.data.get(arg2).bank_name;
				eibankcard.setText(bean.data.get(arg2).bank_name);
				dialog.dismiss();
			}
		});
		
		dialog.setContentView(lv);
		dialog.show();
	}

	private void initbank(){
		
		CityRequest req = new CityRequest();
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.BANKLIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				 bean = new Gson().fromJson(resp.result, BanklListResponse.class);
				if(Api.SUCCEED == bean.result_code){
				}
			}
		});
	}
	
	
	private void rebank() {
		
		if(ViewUtil.checkEditEmpty(eidcard, "请填身份证号")){
			return;
		}
		
		if(banknum.equals("")){
			eibankcard.setError("");
			return;
		}
		if(ViewUtil.checkEditEmpty(eibankcardnum, "请填写银行卡号"))
			return;
		
		if(eibankcardnum.getText().length()<16){
			T.showShort(BankCardInfoActivity.this, "银行卡至少16位");
			return;
		}
		
		// TODO Auto-generated method stub
		ReBankCardRequest req = new ReBankCardRequest();
		req.userShopId = UserInfoManager.getUserInfo(this).id+"";
		req.userType = "1";
		req.personNo = eidcard.getText().toString();
		req.bankName = banknum;
		req.cardNo =	eibankcardnum.getText().toString();
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.USERCARDNOCHANGE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
				if(Api.SUCCEED == bean.result_code){
					if(bean.data.RSPCOD.equals("000000")){
						T.showShort(getApplicationContext(), bean.data.RSPMSG);
						finish();
					}else{
						T.showShort(getApplicationContext(), bean.data.RSPMSG);
					}
				}
			}
		});
	}

	class ProvinceAdapter extends BaseAdapter{
		public List<BankListBean>  adapter_list;
		public ProvinceAdapter(List<BankListBean> b){
			adapter_list = b;
		}
		
		@Override
		public int getCount() {
			return adapter_list.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			TextView tv = new TextView(BankCardInfoActivity.this);
			tv.setTextColor(getResources().getColor(R.color.white));
			tv.setPadding(20, 20, 20, 20);
			tv.setTextSize(18);
			tv.setText(adapter_list.get(position).bank_name);
			return tv;
		}
		
	}
	
private void setbank(ImageView v,String bank){
		
		if (bank!= null&&bank.length()>=2) {
			if (bank.contains("招商")) {
				v.setImageResource(R.drawable.ps_cmb);
			}else if(bank.contains("农业")) {
				v.setImageResource(R.drawable.ps_abc);
			}else if(bank.contains("农行")){
				v.setImageResource(R.drawable.ps_abc);
			}else if(bank.contains("北京")){
				v.setImageResource(R.drawable.ps_bjb);
			}else if(bank.equals("中国银行")){
//				if(bank.substring(0,4).equals("中国银行")){
//					v.setImageResource(R.drawable.ps_ccb);
//				}else{
					v.setImageResource(R.drawable.ps_boc);
//				}
				
			}else if(bank.contains("建设")){
				v.setImageResource(R.drawable.ps_ccb);
			}else if(bank.contains("光大")){
				v.setImageResource(R.drawable.ps_cebb);
			}else if(bank.contains("兴业")){
				v.setImageResource(R.drawable.ps_cib);
			}else if(bank.contains("中信")){
				v.setImageResource(R.drawable.ps_citic);
			}else if(bank.contains("民生")){
				v.setImageResource(R.drawable.ps_cmbc);
			}else if(bank.contains("交通")){
				v.setImageResource(R.drawable.ps_comm);
			}else if(bank.contains("华夏")){
				v.setImageResource(R.drawable.ps_hxb);
			}else if(bank.contains("广东发展")){
				v.setImageResource(R.drawable.ps_gdb);
			}else if(bank.contains("广发")){
				v.setImageResource(R.drawable.ps_gdb);
			}else if(bank.contains("邮政")){
				v.setImageResource(R.drawable.ps_psbc);
			}else if(bank.contains("邮储")){
				v.setImageResource(R.drawable.ps_psbc);
			}else if(bank.contains("工商")){
				v.setImageResource(R.drawable.ps_icbc);
			}else if(bank.contains("平安")){
				v.setImageResource(R.drawable.ps_spa);
			}else if(bank.contains("浦东")){
				v.setImageResource(R.drawable.ps_spdb);
			}else if(bank.contains("工商")){
				v.setImageResource(R.drawable.ps_icbc);
			}else if(bank.contains("上海")){
				v.setImageResource(R.drawable.ps_sh);
			}else{
				v.setImageResource(R.drawable.ps_unionpay);
			}
			
		}else{
			v.setImageResource(R.drawable.ps_unionpay);
		}
		
		
	}
	
}
