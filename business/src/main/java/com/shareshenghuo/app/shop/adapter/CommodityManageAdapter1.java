package com.shareshenghuo.app.shop.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.CommodityManageBean;
import com.shareshenghuo.app.shop.network.bean.ExcitationBean;
import com.shareshenghuo.app.shop.network.bean.FilialobeBean;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.util.Util;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;


public class CommodityManageAdapter1 extends CommonAdapter<CommodityManageBean>{
	private boolean  ischeck = false;
	public List<CommodityManageBean> data;
	private TwoButtonDialog downloadDialog;
	private Context context;
//	public View view;
	
	public CommodityManageAdapter1(Context context, List<CommodityManageBean> data) {
		super(context, data, R.layout.commodity_manage_item);
		this.context = context;
		this.data = data;
	}
		

	@Override
	public void conver(ViewHolder holder, final CommodityManageBean item, final int position) {
		holder.getView(R.id.check_del).setVisibility(View.VISIBLE);
		
		
		DecimalFormat df = new DecimalFormat("###.00");  
		String price = "";
		try {
			price = df.format(Double.parseDouble(item.price));
		} catch (Exception e) {
			// TODO: handle exception
			price = "0.00";
		}
		if(price.substring(0, 1).equals(".")){
			price = "0"+price;
		}
		CheckBox box = holder.getView(R.id.check_del);
		holder.setText(R.id.shop_name, item.name);
		holder.setText(R.id.shop_imfo, item.description);
		holder.setText(R.id.shop_balance, "价格: "+price+"元");
		holder.setText(R.id.tv_time, item.create_time_string);
		holder.setImageByURL(R.id.shop_img, item.photo);
		holder.getView(R.id.img_arr).setVisibility(View.GONE);
		
		box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				data.get(position).ischeck= arg1;
				for (int i = 0; i < data.size(); i++) {
				}
			}
		});
		if(item.ischeck){
			box.setChecked(true);
		}else{
			box.setChecked(false);
		}
//		view =  holder.getConvertView();
	}
	
	public  void setcheck(boolean ischeck) {
		if(ischeck){
			for (int i = 0; i <data.size() ; i++) {
				data.get(i).ischeck = true;
				
			}
		}else{
			for (int i = 0; i <data.size() ; i++) {
				data.get(i).ischeck = false;
				
			}
		}
		this.notifyDataSetChanged();
	}
	
	
	public List<CommodityManageBean> getchoose(){
		for (int i = 0; i < data.size(); i++) {
		}
		return data;
	}
	
	public boolean ischeck(){
		boolean chexck = false;
		for (int i = 0; i < data.size(); i++) {
			if(data.get(i).ischeck){
				chexck = true;
			}
		}
		return chexck;
	}
	
	
	public void setchoose(int position){
		if(data.get(position).ischeck){
			data.get(position).ischeck= false;
		}else{
			data.get(position).ischeck= true;
		}
		notifyDataSetChanged();
	}
	
	public  void del(List<Integer> list) {
	
//		for (int i = 0; i < list.size(); i++) {
//			Log.e("", " = = = = "+list.get(i));
//			data.remove(i);
//		}
//		setcheck(false);
//		notifyDataSetChanged();

	}
	
	public  void deleteCell(final View v, final int index) {  
	    AnimationListener al = new AnimationListener() {  
	        @Override  
	        public void onAnimationEnd(Animation arg0) {  
	        	data.remove(index);  
	            notifyDataSetChanged();  
	        }  
	        @Override public void onAnimationRepeat(Animation animation) {}  
	        @Override public void onAnimationStart(Animation animation) {}  
	    };  
	  
	    collapse(v, al);  
	}  
	  
	private void collapse(final View v, AnimationListener al) {  
	    final int initialHeight = v.getMeasuredHeight();  
	  
	    Animation anim = new Animation() {  
	        @Override  
	        protected void applyTransformation(float interpolatedTime, Transformation t) {  
//	            if (interpolatedTime == 1) {  
//	                v.setVisibility(View.GONE);  
//	            }  
//	            else {  
	                v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);  
	                v.requestLayout();  
//	            }  
	        }  
	  
	        @Override  
	        public boolean willChangeBounds() {  
	            return true;  
	        }  
	    };  
	  
	    if (al!=null) {  
	        anim.setAnimationListener(al);  
	    }  
	    anim.setDuration(300);  
	    v.startAnimation(anim);  
	}


}