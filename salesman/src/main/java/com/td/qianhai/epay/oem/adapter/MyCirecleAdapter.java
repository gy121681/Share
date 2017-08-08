package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.GetImageUtil;
import com.td.qianhai.mpay.utils.DateUtil;

public class MyCirecleAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> list;

	public MyCirecleAdapter(Context context,
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
					R.layout.mycircle_list_item, null);
			// holder.tvTradName = (TextView) convertView
			// .findViewById(R.id.tv_operation);
			holder.date_tv = (TextView) convertView
					.findViewById(R.id.date_tv);
			holder.name_tv = (TextView) convertView
					.findViewById(R.id.name_tv);
			holder.call_phone = (ImageView) convertView
					.findViewById(R.id.call_phone);
			holder.phonenum_tv = (TextView) convertView
					.findViewById(R.id.phonenum_tv);
			holder.user_head_img = (ImageView) convertView
					.findViewById(R.id.user_head_img);
			convertView.setTag(holder);
			holder.imgs = (ImageView) convertView
					.findViewById(R.id.imgs);
			holder.tv_leve = (TextView) convertView
					.findViewById(R.id.tv_leve);
			
			convertView.setTag(holder);
//
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
		
		
		
		
				final HashMap<String, Object> maps = list.get(position);
				
				String imgUrl  = "";
				if(maps.get("PERSONPIC")!=null){
				    imgUrl = maps.get("PERSONPIC").toString();
					holder.user_head_img.setTag(imgUrl);
				}
				// 预设一个图片
				holder.user_head_img.setImageResource(R.drawable.share_s_public_head_small_big);

//				// 通过 tag 来防止图片错位
				if (holder.user_head_img.getTag() != null &&!holder.user_head_img.getTag().equals("null")&& holder.user_head_img.getTag().equals(imgUrl)) {
					Bitmap bit = null;
					try {
						 bit = GetImageUtil.iscace(holder.user_head_img,HttpUrls.HOST_POSM+maps.get("PERSONPIC").toString());
					} catch (Exception e) {
						// TODO: handle exception\
						Log.e("", ""+e.toString());
					}
					if(bit!=null){
						holder.user_head_img.setImageBitmap(bit);
					}else{
						new GetImageUtil(mContext, holder.user_head_img,HttpUrls.HOST_POSM+maps.get("PERSONPIC").toString());
					}
					
				}else{
					holder.user_head_img.setImageResource(R.drawable.share_s_public_head_small_big);

				}
				
				
				if(maps.get("MERCNAM")!=null){
					holder.name_tv.setText(maps.get("MERCNAM").toString());
				}else{
					holder.name_tv.setText("未知");
				}
				
				if(maps.get("MERPHONENUMBER")!=null){
					if(maps.get("MERPHONENUMBER").toString().length()>11){
						String phone = maps.get("MERPHONENUMBER").toString();
						String setphone = phone.substring(0,3);
						String getphone = phone.substring(phone.length()-4);
						holder.phonenum_tv.setText(setphone+"****"+getphone);
					}else{
						holder.phonenum_tv.setText(maps.get("MERPHONENUMBER").toString());
					}
					
				}
				
				holder.call_phone.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						String phone = maps.get("MERPHONENUMBER").toString();
						
						Uri uri = Uri.parse("tel:"+phone);   
						Intent it = new Intent(Intent.ACTION_DIAL, uri);  
						mContext.startActivity(it);
						
//						 Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
//								mContext.startActivity(intent);
					}
				});
				
				if(maps.get("ISAREAAGENT")!=null&&maps.get("ISAREAAGENT").toString().equals("1")){
					holder.tv_leve.setText("省级代理");
				}else if(maps.get("ISAREAAGENT")!=null&&maps.get("ISAREAAGENT").toString().equals("2")){
					holder.tv_leve.setText("市级代理");
				}else if(maps.get("ISAREAAGENT")!=null&&maps.get("ISAREAAGENT").toString().equals("3")){
					holder.tv_leve.setText("区级代理");
				}else{
					if(maps.get("ISGENERALAGENT")!=null&&maps.get("ISGENERALAGENT").toString().equals("1")){
						holder.tv_leve.setText("服务商");
					}else if(maps.get("ISSALEAGT")!=null&&maps.get("ISSALEAGT").toString().equals("1")){
						holder.tv_leve.setText("服务商");
					}else if(maps.get("ISRETAILERS")!=null&&maps.get("ISRETAILERS").toString().equals("1")){
						holder.tv_leve.setText("服务商");
					}else if(maps.get("ISSENIORMEMBER")!=null&&maps.get("ISSENIORMEMBER").toString().equals("1")){
						holder.tv_leve.setText("高级会员");
					}else{
						holder.tv_leve.setText("普通会员");
					}
				}
				
//				if(maps.get("ISGENERALAGENT")!=null){
//					String a = maps.get("ISGENERALAGENT").toString();
//					if(a.equals("1")){
//						holder.imgs.setImageResource(R.drawable.agent_imgs);
//					}else{
//						if(maps.get("ISSALEAGT")!=null){
//							String b = maps.get("ISSALEAGT").toString();
//							if(a.equals("1")){
//								holder.imgs.setImageResource(R.drawable.distribution_img);
//							}else{
//								if(maps.get("ISRETAILERS")!=null){
//									String c = maps.get("ISRETAILERS").toString();
//									if(c.equals("1")){
//										holder.imgs.setImageResource(R.drawable.share_s_homepage_member_retail);
//									}else{
//										if(maps.get("ISSENIORMEMBER")!=null){
//											String d = maps.get("ISSENIORMEMBER").toString();
//											if(d.equals("1")){
//												holder.imgs.setImageResource(R.drawable.share_s_homepage_member_senior);
//											}else{
//												holder.imgs.setImageResource(R.drawable.share_s_homepage_member_senior1);
//											}
//										}
//									}
//								}
//							}
//						}
//					}
//				}
				
				if(maps.get("APPLYDAT")!=null){
					try {
						String date = DateUtil.strToDateToLong(maps.get("APPLYDAT")
								.toString());
						holder.date_tv.setText(date);
						
					} catch (Exception e) {
						// TODO: handle exception
						holder.date_tv.setText(maps.get("APPLYDAT").toString());
					}

					
				}

		return convertView;
	}

	class ViewHolder {
		TextView date_tv,tv_leve;
		TextView name_tv;
		TextView phonenum_tv;
		ImageView user_head_img,imgs,call_phone;
			
		
	}
}
