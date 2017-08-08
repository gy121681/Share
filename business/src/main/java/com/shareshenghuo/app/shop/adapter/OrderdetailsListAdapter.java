package com.shareshenghuo.app.shop.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.Animation.AnimationListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.OrderDispositionActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.fragment.OrderDispFragment1;
import com.shareshenghuo.app.shop.fragment.OrderDispFragment2;
import com.shareshenghuo.app.shop.network.bean.CommodityManageBean;
import com.shareshenghuo.app.shop.network.bean.RehistoryBean;
import com.shareshenghuo.app.shop.network.request.ShopCategoryRequest;
import com.shareshenghuo.app.shop.network.response.AutResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.Util;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;

public class OrderdetailsListAdapter extends CommonAdapter<RehistoryBean> {
	public List<RehistoryBean> data;
	private TwoButtonDialog downloadDialog;
	private Context context;
	private int tag = 0;

	public OrderdetailsListAdapter(Context context, List<RehistoryBean> data, int i) {
//		super(context, data, R.layout.item_message);
		super(context, data, R.layout.rehistory_list_item1);
		this.context = context;
		this.data = data;
		this.tag = i;
		
	}

	@Override
	public void conver(ViewHolder holder, final RehistoryBean item, final int position) {
		
		holder.setText(R.id.tv_name, item.real_name);
		holder.setText(R.id.tv_phone, item.account);
		holder.setText(R.id.tv_time, item.create_time_string);
		
		if(!TextUtils.isEmpty(item.fee_rate)){
			holder.setText(R.id.num, (int)(Double.parseDouble(item.fee_rate)*100)+"%");
		}
//		if(!TextUtils.isEmpty(item.fee_rate)){
//			holder.setText(R.id.num, (int)(Double.parseDouble(item.fee_rate)*100)+"%");
//		}
		if(!TextUtils.isEmpty(item.fee_amt)){
			holder.setText(R.id.tv_balance,"¥"+ Util.getnum(item.fee_amt, false));
		}
		
		if(!TextUtils.isEmpty(item.total_fee)){
			holder.setText(R.id.tv_consbalacne,"¥"+Util.getnum(item.total_fee, false));
		}
		
		TextView status = holder.getView(R.id.tv_status);
		status.setTextColor(mContext.getResources().getColor(R.color.green_h));
		if(item.status.equals("0")){
			holder.setText(R.id.tv_status, "待支付");
			status.setTextColor(mContext.getResources().getColor(R.color.red));
		}else if(item.status.equals("1")){
			holder.setText(R.id.tv_status, "支付成功");
		}else if(item.status.equals("2")){
			
			status.setTextColor(mContext.getResources().getColor(R.color.red));
			holder.setText(R.id.tv_status, "支付失败");
		}else{
			status.setTextColor(mContext.getResources().getColor(R.color.red));
		}
		
		CheckBox box = holder.getView(R.id.check_del);
		if(item.ischeck){
			box.setChecked(true);
		}else{
			box.setChecked(false);
		}
		
		box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				data.get(position).ischeck= arg1;
				if(tag==0){
					OrderDispFragment1.setnum();
				}else if(tag==1){
					OrderDispFragment2.setnum();
				}else{
					((OrderDispositionActivity) context).setnum();
				}
				
				
			}
		});
		final View view = holder.getConvertView();
		holder.getView(R.id.btnConversationItemDel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				downloadDialog = new TwoButtonDialog(context, R.style.CustomDialog,
						"", "是否确定删除?", "取消", "确定",true,new OnMyDialogClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								switch (v.getId()) {
								case R.id.Button_OK:
									downloadDialog.dismiss();
									break;
								case R.id.Button_cancel:
									if(data.get(position).id!=null){
										deletegoodstype(data.get(position).id,view,position);
									}else{
										T.showShort(context, "删除失败");
									}
									downloadDialog.dismiss();
								default:
									break;
								}
							}
						});
					downloadDialog.show();
				
//				new AlertDialog.Builder(mContext)
//						.setMessage("是否确定删除该条会话")
//						.setNegativeButton("取消", null)
//						.setPositiveButton("删除", new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface arg0, int arg1) {
//								remove(item);
//								notifyDataSetChanged();
//							}
//						})
//						.show();
			}
		});
	}
	
	public List<RehistoryBean> getchoose(){
		
		
		return data;
	}
	
	public boolean ischeck(){
		
		for (int i = 0; i < data.size(); i++) {
			
			if(data.get(i).ischeck){
				return true;
			}
		}
		return false;
	}
	
	public void setchoose(int position){
		if(data.get(position).ischeck){
			data.get(position).ischeck= false;
		}else{
			data.get(position).ischeck= true;
		}
		notifyDataSetChanged();
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
	
	public void deletegoodstype(final String id,final View v,final int index) {
		ShopCategoryRequest req = new ShopCategoryRequest();
		req.id = id;
		RequestParams params = new RequestParams();
		String url = Api.DELETESHOPPAYCLSINFBYIDS;
		if(tag==0){
			url = Api.DELETESHOPPAYCLSINFBYIDSNEW;
		}else{
			url = Api.DELETESHOPPAYCLSINFBYIDS;
		}
		
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
					T.showNetworkError(context);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						if(bean.data!=null&&bean.data.RSPCOD.equals("000000")){
//							T.showShort(context, bean.data.RSPMSG);
//							((OrderDispositionActivity)context).loadData();
							deleteCell(v, index);
						}else{
							T.showShort(context, bean.data.RSPMSG);
						}
						
					} else {
//						T.showShort(context, bean.result_desc);
					}
			}
		});
	}
	
	private void deleteCell(final View v, final int index) {  
    	data.remove(index);  
  	  
        notifyDataSetChanged(); 
//	    AnimationListener al = new AnimationListener() {  
//	        @Override  
//	        public void onAnimationEnd(Animation arg0) {  
// 
//	        }  
//	        @Override public void onAnimationRepeat(Animation animation) {}  
//	        @Override public void onAnimationStart(Animation animation) {}  
//	    };  
	  
//	    collapse(v, al);  
	}  
	  
	private void collapse(final View v, AnimationListener al) {  
	    final int initialHeight = v.getMeasuredHeight();  
	  
	    Animation anim = new Animation() {  
	        @Override  
	        protected void applyTransformation(float interpolatedTime, Transformation t) {  
	            if (interpolatedTime == 1) {  
	                v.setVisibility(View.GONE);  
	            }  
	            else {  
	                v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);  
	                v.requestLayout();  
	            }  
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
