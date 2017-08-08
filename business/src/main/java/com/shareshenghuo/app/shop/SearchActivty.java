package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.network.bean.SearchBankBean;
import com.shareshenghuo.app.shop.network.request.AutRequset;
import com.shareshenghuo.app.shop.network.response.SearchBankResponse;
import com.shareshenghuo.app.shop.networkapi.Api;

public class SearchActivty  extends Activity implements OnKeyListener{
	public String bankname= "";
	public EditText ed_search;
	public TextView tv_cancel;
	public String bank_key,code;
	private RelativeLayout relayout;
	private ListView listview;
	public List<SearchBankBean> datas;
	private ProvinceAdapter pAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serach_activty);
		initview();
		bankname = getIntent().getStringExtra("bankname");
		
		
	}

	private void initanima() {
		// TODO Auto-generated method stub
		relayout.setVisibility(View.VISIBLE);
	}
	
	

	private void seartch(String bankkey) {
		// TODO Auto-generated method stub
		AutRequset req = new AutRequset();
		req.bankKey = bankkey;
		req.bankName =  bankname;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.SEARCHBANK, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				SearchBankResponse bean = new Gson().fromJson(resp.result, SearchBankResponse.class);
				if(0 == bean.result_code){
					if(bean.data!=null&&bean.data.size()>0){
						datas.clear();
						datas .addAll(bean.data);
						pAdapter.notifyDataSetChanged();
						listview.setVisibility(View.VISIBLE);
//						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
//						imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
					}else{
						datas.clear();
						pAdapter.notifyDataSetChanged();
						listview.setVisibility(View.GONE);
					}
				}
			}
		});
	}

	private void initview() {
		// TODO Auto-generated method stub
		ed_search = (EditText) findViewById(R.id.ed_search);
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		relayout = (RelativeLayout) findViewById(R.id.relayout);
		listview = (ListView) findViewById(R.id.listview);
		datas = new ArrayList<SearchBankBean>();
		pAdapter = new ProvinceAdapter(datas);
		listview.setAdapter(pAdapter);
		ed_search.requestFocus();
		initanima();
		tv_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				relayout.setVisibility(View.GONE);
				listview.setVisibility(View.GONE);
				finish();
			}
		});
		ed_search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					seartch(ed_search.getText().toString());
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
		        Intent mIntent = new Intent();  
		        mIntent.putExtra("bank_key", datas.get(arg2).bank_key);  
		        mIntent.putExtra("code", datas.get(arg2).code);  
		      	setResult(100, mIntent);  
		      	listview.setVisibility(View.GONE);
		      	finish();
			}
		});
		
		listview.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				try {
					((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchActivty.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				} catch (Exception e) {
					// TODO: handle exception
				}
				return false;
			}
		});
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}, 500);

	}
	
	

//	private void initdialog(final List<SearchBankBean> data) {
//	// TODO Auto-generated method stub
//
//		final Dialog dialog = new Dialog(this,R.style.MyEditDialog1);
//		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		//	dialog.setTitle("选择");
//		ListView lv = new ListView(this); 
//		ProvinceAdapter pAdapter = new ProvinceAdapter(data);
//		lv.setAdapter(pAdapter);
//		lv.setOnItemClickListener(new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View initbank, int arg2,
//				long arg3) {
//			// TODO Auto-generated method stub
//
//			dialog.dismiss();
//		}
//	});
//	
//	dialog.setContentView(lv);
//	dialog.show();
//	}
	
	class ProvinceAdapter extends BaseAdapter{
		public List<SearchBankBean>  adapter_list;
		public ProvinceAdapter(List<SearchBankBean> b){
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
			
			TextView tv = new TextView(SearchActivty.this);
			tv.setWidth(800);
			tv.setTextColor(getResources().getColor(R.color.black));
			tv.setPadding(20, 30, 20, 30);
			tv.setTextSize(14);
			tv.setText(adapter_list.get(position).bank_key);
			return tv;
		}
		
	}
	
	@Override
	public boolean onKey(View arg0, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if (keyCode == KeyEvent.KEYCODE_ENTER) {
		       // 先隐藏键盘
//		       ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
//		       .hideSoftInputFromWindow(CommodityManageActivity.this.getCurrentFocus()
//		       .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			 if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
			      //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
				 	seartch(ed_search.getText().toString());
				 	return true;
			 }
		   }
		return false;
	}

}
