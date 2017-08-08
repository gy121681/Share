package com.td.qianhai.epay.oem.adapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.qrcode.FinishListener;
import com.td.qianhai.mpay.utils.DateUtil;

/**
 * 结算方式的dialog
 * 
 * @author liangge
 * 
 */
public class ReceivablesListAdapter2 extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, Object>> list;
	private String subMchId;

	public ReceivablesListAdapter2(Context context,
			ArrayList<HashMap<String, Object>> list,String subMchId) {
		this.context = context;
		this.subMchId = subMchId;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		RadioViewHolder holder = null;
		if (convertView != null) {
			holder = (RadioViewHolder) convertView.getTag();
		} else {
			holder = new RadioViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.receivableslist_item, null);
			holder.tv_time = (TextView) convertView
					.findViewById(R.id.tv_time);
			holder.tv_name = (TextView) convertView
					.findViewById(R.id.tv_name);
			holder.tv_type = (TextView) convertView
					.findViewById(R.id.tv_type);
			holder.tv_money = (TextView) convertView
					.findViewById(R.id.tv_money);
			holder.lin_1 = (LinearLayout) convertView
					.findViewById(R.id.lin_1);
			
			holder.imgs = (ImageView) convertView
			.findViewById(R.id.imgs);
			convertView.setTag(holder);
			
		}
		holder.imgs.setImageResource(R.drawable.zhi);
		
		HashMap<String, Object> maps = list.get(position);
		if(subMchId!=null&&subMchId.equals("01")){
	         if(maps.get("payDay")!=null){
	        		String aa = maps.get("payDay").toString();
	        		 holder.tv_time.setText(aa);
	          }
			 
            if(maps.get("payDay")!=null){
            	String aa = maps.get("payDay").toString();
//            	tv_time.setText(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("payDate").toString());
//            	  tv_money.setText(getWeek(aa+"00000000"));
                if(aa!=null&&aa.length()>=1){
                	aa = aa.replace("-", "");
                	holder.tv_type.setText(getWeek(aa+"00000000"));
                }
                
              
            }
	          
		}else{
			holder.lin_1 .setVisibility(View.GONE);
          if(maps.get("gmtPayment")!=null){
        		String aa = maps.get("gmtPayment").toString();
        		holder.tv_type.setText(aa.substring(aa.length()-8));
          }
		}
		
	
		
		if(maps.get("totalFee")!=null){
			
			String a = String .format("%.2f",Double.parseDouble(maps.get("totalFee").toString())/100);
			holder.tv_money.setText("+"+a);
//			NumberFormat nf = new DecimalFormat("###,###.##");
//			holder.tv_money.setText("+"+nf.format(Double.parseDouble(a)));
//			holder.tv_money.setText("+"+maps.get("totalFee").toString());
		}
			
		return convertView;
	}

	  private String getWeek(String date) {
	        String Week = "周";   
	        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//也可将此值当参数传进来
//	        try {
//				format.parse(date);
//			} catch (ParseException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//	        Date  curDate = new Date(System.currentTimeMillis());
	        String pTime = date;//format.format(curDate);  
	        Calendar c = Calendar.getInstance();
	        try {
	         c.setTime(format.parse(date));
	        } catch (ParseException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	        }
	        switch(c.get(Calendar.DAY_OF_WEEK)){
	        case 1:
	            Week += "日";
	            break;
	        case 2:
	            Week += "一";
	            break;
	        case 3:
	            Week += "二";
	            break;
	        case 4:
	            Week += "三";
	            break;
	        case 5:
	            Week += "四";
	            break;
	        case 6:
	            Week += "五";
	            break;
	        case 7:
	            Week += "六";
	            break;
	        default:
	            break;          
	        }           
	        Log.e("", ""+Week);
	        return Week;
	       }
	    
	
	class RadioViewHolder {
		TextView tv_time,tv_name,tv_type,tv_money;
		LinearLayout  lin_1 ;
		ImageView imgs;
	}

}
