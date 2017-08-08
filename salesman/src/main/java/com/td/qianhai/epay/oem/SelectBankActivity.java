package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.tsz.afinal.FinalBitmap;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.GetImageUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelectBankActivity extends BaseActivity{
	
	private GridView gv;
	private ArrayList<HashMap<String, Object>> mList;
	private MyAdapter mAdapter;
	private LayoutInflater mInflater;
	private TranslateAnimation taLeft, taRight, taTop, taBlow;
	private String tag = "";
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_bank_activity);
		AppContext.getInstance().addActivity(this);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Intent it = getIntent();
		tag = it.getStringExtra("tag");
		initview();
		InitAnima();
		if(tag.equals("4")){
			new Thread(run1).start();
		}else{
			new Thread(run).start();
		}
		
	}
	private void initview() {
		// TODO Auto-generated method stub
		gv = (GridView) findViewById(R.id.gridView1);
		mList = new ArrayList<HashMap<String,Object>>();
		mAdapter = new MyAdapter(mList);
		gv.setAdapter(mAdapter);
		
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				// TODO Auto-generated method stub
				
                Intent intent = new Intent();
                //把返回数据存入Intent
               
                if(tag.equals("3")){
               	 intent.putExtra("result", mList.get((int)id).get("BANKNAME").toString());
               	intent.putExtra("img", mList.get((int)id).get("IMG_URL").toString());
               	 setResult(2, intent);
                }else if(tag.equals("4")){
                 intent.putExtra("result", mList.get((int)id).get("BANKNAME").toString());
                 intent.putExtra("num", mList.get((int)id).get("FRPID").toString());
                 intent.putExtra("img", mList.get((int)id).get("IMG_URL").toString());
                 setResult(1, intent);
//                	 intent.putExtra("result", mList.get((int)id).get("BANKNAME").toString());
//                	 setResult(1, intent);
                }
               
                finish();
			}
		});
		
	}
	
	private void InitAnima() {
		// TODO Auto-generated method stub
		
//        push_left_in=AnimationUtils.loadAnimation(this, R.anim.push_left_in);  
//        push_right_in=AnimationUtils.loadAnimation(this, R.anim.push_right_in);  
//        slide_top_to_bottom=AnimationUtils.loadAnimation(this, R.anim.slide_top_to_bottom);  
//        slide_bottom_to_top=AnimationUtils.loadAnimation(this, R.anim.slide_bottom_to_top); 
		
		taLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		taRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		taTop = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		taBlow = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		taLeft.setDuration(1000);
		taRight.setDuration(1000);
		taTop.setDuration(1000);
		taBlow.setDuration(1000);
	}
	
	
	Runnable run = new Runnable() {

		@Override
		public void run() {
			String[] values = { HttpUrls.CHOOSEBANK+""};
			ArrayList<HashMap<String, Object>> list = NetCommunicate.getList(HttpUrls.CHOOSEBANK, values,HttpKeys.CHOOSEBANK_BACK);
			Message msg = new Message();
			if (list != null) {
						mList.addAll(list);
					if(mList.size()<=0||mList==null){
						
						msg.what = 2;
					}else{
						msg.what = 1;
					}
					} else {
				msg.what = 3;
			}
			handler.sendMessage(msg);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mAdapter.notifyDataSetChanged();
				break;
			case 2:
				
				Toast.makeText(getApplicationContext(),"",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"没有获取到您的订单信息");
				break;
			case 3:
				Toast.makeText(getApplicationContext(),"",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"订单信息获取失败");
				break;
			default:
				break;
			}
		};
	};
	
	
	Runnable run1 = new Runnable() {

		@Override
		public void run() {
			String[] values = { HttpUrls.BANK+""};
			ArrayList<HashMap<String, Object>> list = NetCommunicate.getList(HttpUrls.BANK, values,HttpKeys.BANK_BACK);
			Message msg = new Message();
			if (list != null) {
						mList.addAll(list);
					if(mList.size()<=0||mList==null){
						
						msg.what = 2;
					}else{
						msg.what = 1;
					}
					} else {
				msg.what = 3;
			}
			handler.sendMessage(msg);
		}
	};

	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mAdapter.notifyDataSetChanged();
				break;
			case 2:
				
				Toast.makeText(getApplicationContext(),"",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"没有获取到您的订单信息");
				break;
			case 3:
				Toast.makeText(getApplicationContext(),"",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"订单信息获取失败");
				break;
			default:
				break;
			}
		};
	};
	
	private class MyAdapter extends BaseAdapter {
		
		private ArrayList<HashMap<String, Object>> mList;
		public MyAdapter(ArrayList<HashMap<String, Object>> mList) {
			this.mList = mList;
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
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
				convertView = mInflater.inflate(R.layout.item, null);
				holder = new ViewHolder();
				holder.textview1 = (TextView) convertView.findViewById(R.id.textview1);
				holder.image = (ImageView) convertView.findViewById(R.id.imageView1);
				convertView.setTag(holder);
				
				
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			HashMap<String, Object> bankinfo = mList.get(position);
			if(bankinfo.get("BANKNAME")!=null){
				holder.textview1.setText(bankinfo.get("BANKNAME").toString());
			}
			
			
			if(bankinfo.get("IMG_URL")!=null){
				String imgurl = bankinfo.get("IMG_URL").toString();
				
//				new GetImageUtil(SelectBankActivity.this, holder.image,HttpUrls.HOST_POSM+imgurl);
				FinalBitmap.create(SelectBankActivity.this).display(holder.image,
						HttpUrls.HOST_POSM + imgurl,
						holder.image.getWidth(),
						holder.image.getHeight(), null, null);
				
//				Bitmap bit = null;
//				try {
//					 bit = GetImageUtil.iscace(holder.image,HttpUrls.HOST_POSM+imgurl);
//				} catch (Exception e) {
//					// TODO: handle exception\
//					Log.e("", ""+e.toString());
//				}
//				
//				if(bit!=null){
//					holder.image.setImageBitmap(bit);
//				}else{
//					new GetImageUtil(SelectBankActivity.this, holder.image,HttpUrls.HOST_POSM+imgurl);
//				}
			}
			
//			holder.image.setImageResource(imgID);
		
			
			
//			Random ran = new Random();
//			int rand = ran.nextInt(4);
//			switch (rand) {
//			case 0:
//				convertView.startAnimation(taLeft);
//				break;
//			case 1:
//				convertView.startAnimation(taRight);
//				break;
//			case 2:
//				convertView.startAnimation(taTop);
//				break;
//			case 3:
//				convertView.startAnimation(taBlow);
//				break;
//			}

			return convertView;
		}

		class ViewHolder {
			public TextView textview1;
			public ImageView image;
		}
	}
	
}
