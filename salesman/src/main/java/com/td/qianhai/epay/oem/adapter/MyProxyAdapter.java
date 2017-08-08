package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import net.tsz.afinal.FinalBitmap;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.GetImageUtil;
import com.td.qianhai.mpay.utils.DateUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyProxyAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> list;

	public MyProxyAdapter(Context context,
			ArrayList<HashMap<String, Object>> list, int tag) {
		this.list = list;
		this.mContext = context;
		

	}
	
//	@Override    
//	public boolean isEnabled(int position) {     
//	   return false;     
//	}  

	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public Object getItem(int arg0) {

		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
//		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.myproxy_list_item, null);
			// holder.tvTradName = (TextView) convertView
			// .findViewById(R.id.tv_operation);
			holder.date_tv = (TextView) convertView
					.findViewById(R.id.date_tv);
			holder.name_tv = (TextView) convertView
					.findViewById(R.id.name_tv);
			holder.phonenum_tv = (TextView) convertView
					.findViewById(R.id.phonenum_tv);
			holder.imgs = (ImageView) convertView
					.findViewById(R.id.imgs);
			
			holder.tottxnamt = (TextView) convertView
					.findViewById(R.id.tottxnamt);
			
			holder.agtshramt = (TextView) convertView
					.findViewById(R.id.agtshramt);
			
			holder.type_tv = (TextView) convertView
					.findViewById(R.id.type_tv);
			holder.user_head_img = (ImageView) convertView.findViewById(R.id.user_head_img);
			
			convertView.setTag(holder);
//
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
		
		
				HashMap<String, Object> maps = list.get(position);
				
				if(maps.get("mercnam")!=null){
					if(maps.get("mercnam").toString().equals("null")){
						holder.name_tv.setText("未知");
					}else{
						holder.name_tv.setText(maps.get("mercnam").toString());
					}
				}else{
					holder.name_tv.setText("未知");
				}
				
				if(maps.get("merphonenumber")!=null){
//					if(maps.get("merphonenumber").toString().length()>11){
//						String phone = maps.get("merphonenumber").toString();
//						String setphone = phone.substring(0,3);
//						String getphone = phone.substring(phone.length()-4);
//						holder.phonenum_tv.setText(setphone+"****"+getphone);
//					}else{
					if(maps.get("merphonenumber").toString().equals("null")){
						holder.phonenum_tv.setText("未知");
					}else{
						holder.phonenum_tv.setText(maps.get("merphonenumber").toString());
					}
					
//					}
					
				}
				String bb = "";
				if(maps.get("res")!=null){
					if(maps.get("res").toString().equals("null")){
						bb = "";
					}else{
						bb = maps.get("res").toString();
					}
				}
				
				String type = maps.get("shrtype").toString();
				if(type!=null){
					if(type.equals("0")){
						holder.type_tv.setText(bb+"  收款收益");
					}else if(type.equals("1")){
						holder.type_tv.setText(bb+"  闪提收益");
					}else if(type.equals("2")){
						holder.type_tv.setText(bb+"  提现收益");
					}else if(type.equals("3")){
						holder.type_tv.setText(bb+"  三级代理收益");
					}else if(type.equals("4")){
						holder.type_tv.setText(bb+"  零售商收益");
					}else if(type.equals("5")){
						holder.type_tv.setText(bb+"  分销商收益");
					}else if(type.equals("6")){
						holder.type_tv.setText(bb+"  代理商收益");
					}else if(type.equals("8")){
						holder.type_tv.setText(bb+"  区域收益");
					}else if(type.equals("9")){
						holder.type_tv.setText(bb+"  商家返佣");
					}else if(type.equals("10")){
						holder.type_tv.setText(bb+"  信用卡还款");
					}
				}
				
				
				String imgUrl = maps.get("personpic").toString();
				holder.user_head_img.setTag(imgUrl);
				// 预设一个图片
				holder.user_head_img.setImageResource(R.drawable.share_s_public_head_small_big);

				// 通过 tag 来防止图片错位
				if (holder.user_head_img.getTag() != null &&!holder.user_head_img.getTag().equals("null")&& holder.user_head_img.getTag().equals(imgUrl)) {
					Bitmap bit = null;
					try {
						 bit = GetImageUtil.iscace(holder.user_head_img,HttpUrls.HOST_POSM+maps.get("personpic").toString());
					} catch (Exception e) {
						// TODO: handle exception\
						Log.e("", ""+e.toString());
					}
					if(bit!=null){
						holder.user_head_img.setImageBitmap(bit);
					}else{
						new GetImageUtil(mContext, holder.user_head_img,HttpUrls.HOST_POSM+maps.get("personpic").toString());
					}
				}else{
					holder.user_head_img.setImageResource(R.drawable.share_s_public_head_small_big);

				}
				
//				if(!imgUrl.equals("null")){
////					FinalBitmap.create(mContext).display(holder.user_head_img,
////							HttpUrls.HOST_POSM+maps.get("personpic").toString());
//					new GetImageUtil(mContext, holder.user_head_img,HttpUrls.HOST_POSM+maps.get("personpic").toString());
//				}else{
//					holder.user_head_img.setImageResource(R.drawable.head_user_img);
//				}
				
//					holder.user_head_img.setTag(maps.get("personpic").toString());
//				// 通过 tag 来防止图片错位
//				
//				Log.e("", " = = = = "+holder.user_head_img.getTag());
////				if(maps.get("personpic")!=null&&!maps.get("personpic").equals("null")){
//					
//					if((holder.user_head_img.getTag().equals("null"))){
//						holder.user_head_img.setBackgroundResource(R.drawable.head_user_img);
//					}else{
//						if(holder.user_head_img.getTag().equals(maps.get("personpic").toString())){
//							new GetImageUtil(mContext, holder.user_head_img,HttpUrls.HOST_POSM+maps.get("personpic").toString());
//						}
//						
//					}
					
				
				if(maps.get("systim")!=null){
					try {
						String date = DateUtil.strToDateToLong(maps.get("systim")
								.toString());
						holder.date_tv.setText(date);
						
					} catch (Exception e) {
						// TODO: handle exception
						holder.date_tv.setText(maps.get("systim").toString());
					}
					
				}
				if(maps.get("isgeneralagent")!=null){
					String agt = maps.get("isgeneralagent").toString();
					if(agt.equals("1")){
						holder.imgs.setImageResource(R.drawable.share_s_homepage_member_label_label_agent);
					}else{
						if(maps.get("issaleagt")!=null){
							String a = maps.get("issaleagt").toString();
							if(a.equals("1")){
								holder.imgs.setImageResource(R.drawable.share_s_homepage_member_label_label_distributor);
							}else{
								
								if(maps.get("isretailers")!=null&&maps.get("isretailers").toString().equals("1")){
									holder.imgs.setImageResource(R.drawable.share_s_homepage_member_retail);
								}else{
									if(maps.get("isseniormember")!=null){
										String b = maps.get("isseniormember").toString();
										if(b.equals("1")){
											holder.imgs.setImageResource(R.drawable.share_s_homepage_member_senior);
										}else{
											holder.imgs.setImageResource(R.drawable.share_s_homepage_member_senior1);
										}
									}
								}

							}
						}
					}
				}
				
				
				
				if(maps.get("tottxnamt")!=null){
					String aa = maps.get("tottxnamt").toString();
					holder.tottxnamt.setText(Double.parseDouble(aa)/100+"");
					
				}
				if(maps.get("agtshramt")!=null){
					String aa = maps.get("agtshramt").toString();
					holder.agtshramt.setText(Double.parseDouble(aa)/100+"");
				}
				
				
				
				
				

		return convertView;
	}

	class ViewHolder {
		TextView date_tv;
		ImageView imgs,user_head_img;
		TextView name_tv;
		TextView phonenum_tv;
		TextView tottxnamt;
		TextView agtshramt;
		TextView type_tv;
		
	}
}
