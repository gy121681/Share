package com.shareshenghuo.app.shop;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.network.bean.BankBean;
import com.shareshenghuo.app.shop.network.bean.MyBankCardBean;
import com.shareshenghuo.app.shop.network.request.MyBankCardRequest;
import com.shareshenghuo.app.shop.network.response.GetPaySupportCardResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class ChooseCardTypeActivity extends BaseTopActivity {

	private RadioButton card_1, card_2;
	private LayoutInflater mInflater;
	private List<BankBean> banklist;
	private List<BankBean> banklist1;
	private List<BankBean> banklist2;
	private MyAdapter mAdapter;
	private GridView gv;
	private String type = "1";
	private final int RESULT_CODE=101;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.choose_cardtype_activity);
		initview();
		initcardlist(type);
	}
	


	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("选择银行类型");
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		card_1 = (RadioButton) findViewById(R.id.card_1);
		card_1.setChecked(true);
		card_1.setTextColor(getResources().getColor(R.color.black));
		card_2 = (RadioButton) findViewById(R.id.card_2);
		card_1.setOnClickListener(new MyOnClickListener(0));
		card_2.setOnClickListener(new MyOnClickListener(1));
		gv = (GridView) findViewById(R.id.gridviews1);
		
		banklist = new ArrayList<BankBean>();
		banklist1 = new ArrayList<BankBean>();
		banklist2 = new ArrayList<BankBean>();
		
		mAdapter = new MyAdapter(banklist);
		gv.setAdapter(mAdapter);
		
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
			    Intent intent=new Intent();
			    intent.putExtra("bankname", banklist.get(arg2).bank_name);
			    intent.putExtra("type", type);
			    setResult(RESULT_CODE, intent);
			    finish();
			}
		});
	}
	
	private void initcardlist(final String type2) {
		// TODO Auto-generated method stub

		ProgressDialogUtil.showProgressDlg(this, "请稍候");
		MyBankCardRequest req = new MyBankCardRequest();
		req.card_type = type2;
		
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		new HttpUtils().send(HttpMethod.POST, Api.GETPAYSUPPORTCARDS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(ChooseCardTypeActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				GetPaySupportCardResponse bean = new Gson().fromJson(resp.result, GetPaySupportCardResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					initlist(bean.data);
					if(type2.equals("1")){
						banklist1.addAll( bean.data);
						
					}else{
						banklist2 .addAll( bean.data);
					}
					
				}else{
					T.showShort(getApplicationContext(), "请求失败");
				}
			}

		});
		
	}

	
	private void initlist(List<BankBean> data) {
		// TODO Auto-generated method stub
		banklist.clear();
		banklist.addAll(data);
		mAdapter.notifyDataSetChanged();
//		mAdapter = null;
//		Log.e("", "mAdapter");
//		MyAdapter mAdapter = new MyAdapter(data);
//		gv.setAdapter(mAdapter);
	}
	
	/**
	 * 头标点击监听
	 */
	private class MyOnClickListener implements OnClickListener {

		public MyOnClickListener(int i) {
		}

		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.card_1:
				type = "1";
				card_1.setTextColor(getResources().getColor(R.color.black));
				card_2.setTextColor(getResources().getColor(R.color.gray_dark));
				
				if(banklist1.size()>0){
					banklist.clear();
					banklist.addAll(banklist1);
					mAdapter.notifyDataSetChanged();
				}else{
					initcardlist(type);
				}
				
				break;
			case R.id.card_2:
				type = "2";
				card_2.setTextColor(getResources().getColor(R.color.black));
				card_1.setTextColor(getResources().getColor(R.color.gray_dark));
				if(banklist2.size()>0){
					banklist.clear();
					banklist.addAll(banklist2);
					mAdapter.notifyDataSetChanged();
				}else{
					initcardlist(type);
				}
				break;
			default:
				break;
			}
		}
	}
	
	
	private class MyAdapter extends BaseAdapter {
		private List<BankBean> banklists;
		
		public MyAdapter(List<BankBean> banklist) {
			// TODO Auto-generated constructor stub
			this.banklists = banklist;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return banklists.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return banklists.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.itemfobanklist, null);
				holder = new ViewHolder();
				holder.textview1 = (TextView) convertView.findViewById(R.id.textview1);
				holder.textview2 = (TextView) convertView.findViewById(R.id.textview2);
				holder.image = (ImageView) convertView.findViewById(R.id.imageView1);
				convertView.setTag(holder);
				
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String  text = banklist.get(position).bank_name;
			if(text!=null){
				ViewUtil.setbank(holder.image, text);
			}
//			if(banklist.get(position).card_type!=null){
//				String type  = banklist.get(position).card_type;
//				if(type.equals("1")){
//					holder.textview1.setText(text+"储蓄卡");
//				}else if(type.equals("2")){
//					holder.textview2.setVisibility(View.VISIBLE);
//					holder.textview2.setText(text+"信用卡");
//				}else if(type.equals("3")){
//					holder.textview2.setVisibility(View.VISIBLE);
//					holder.textview1.setText(text+"储蓄卡");
//					holder.textview2.setText(text+"信用卡");
//				}
//			}else{
				holder.textview2.setVisibility(View.GONE);
				holder.textview1.setText(text);
//			}
			
			return convertView;
		}

		class ViewHolder {
			public TextView textview1,textview2;
			public ImageView image;
		}
	}

}
