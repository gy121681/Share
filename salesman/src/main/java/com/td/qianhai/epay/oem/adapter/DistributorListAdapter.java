package com.td.qianhai.epay.oem.adapter;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.td.qianhai.epay.oem.DistributorActivity;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.RateMianActivity;
import com.td.qianhai.epay.oem.WebViewActivity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.DESKey;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class DistributorListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> list;
	private SharedPreferences share;
	private String isretailers,issaleagt,isgeneralagent,sts,isvip;

	public DistributorListAdapter(Context context,
			ArrayList<HashMap<String, Object>> list, SharedPreferences share, String isretailers, String issaleagt, String isgeneralagent, String sts, String isvip) {
		this.list = list;
		this.mContext = context;
		this.share = share;
		this.isretailers = isretailers;
		this.issaleagt = issaleagt;
		this.isgeneralagent = isgeneralagent;
		this.sts = sts;
		this.isvip = isvip;

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
	
	private String initaes(String mobile,String pwd,String oemid) {
		// TODO Auto-generated method stub

		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("PHONENUMBER", mobile);
			jsonObj.put("PASSWORD", pwd);
			jsonObj.put("OEMID", oemid);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String aa = null;
		try {
			aa = DESKey.AES_Encode(jsonObj.toString(),
					"f15f1ede25a2471998ee06edba7d2e29");
			aa = URLEncoder.encode(aa);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return aa;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final int positions = position;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.dis_list_item, null);
			holder.money_rate = (TextView) convertView
					.findViewById(R.id.money_rate);
			holder.product_name = (TextView) convertView
					.findViewById(R.id.product_name);
			holder.money_state = (TextView) convertView
					.findViewById(R.id.money_state);
			holder.balance = (TextView) convertView
					.findViewById(R.id.balance);
			holder.tv_buy = (TextView) convertView
					.findViewById(R.id.tv_buy);
			holder.tv_state = (TextView) convertView
					.findViewById(R.id.tv_state);
			holder.money_withdrawal_fee = (TextView) convertView
					.findViewById(R.id.money_withdrawal_fee);
			holder.tv_apply = (TextView) convertView
					.findViewById(R.id.tv_apply);
			holder.product_image = (ImageView) convertView.findViewById(R.id.product_image);
			convertView.setTag(holder);
//
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
				 final HashMap<String, Object> maps = list.get(position);
				final String rodid = maps.get("roleId").toString();
				if(rodid.equals("0")){
					holder.product_image.setImageResource(R.drawable.ordinary_img);
				}else if(rodid.equals("1")){
					holder.product_image.setImageResource(R.drawable.retail_img);
				}else if(rodid.equals("2")){
					holder.product_image.setImageResource(R.drawable.distribution_img);
				}else if(rodid.equals("3")){
					holder.product_image.setImageResource(R.drawable.agent_img);
				}else{
					holder.tv_state.setVisibility(View.GONE);
					holder.tv_apply.setVisibility(View.GONE);
					holder.product_image.setImageResource(R.drawable.region_agt_img);
				}
				holder.product_name.setText(maps.get("roleName").toString());
				holder.balance.setText(maps.get("rechargeRate").toString()+"%");
				holder.money_rate.setText(maps.get("urgentWithdrawRate").toString()+"%");
				
				;
				holder.money_withdrawal_fee.setText( String .format("%.2f",Double.parseDouble(maps.get("commWithdrawAmt").toString())/100)+"元");
				String actNum =  maps.get("upgradeAmt").toString();
				
				
				if(actNum!=null&&!actNum.equals("null")){
					String a = String .format("%.2f",Double.parseDouble(actNum)/100);
					NumberFormat nf = new DecimalFormat("###,###.##");
					 holder.tv_apply .setText(nf.format(Double.parseDouble(a))+"元");
//					holder.tv_apply .setText(String .format("%.2f",Double.parseDouble(actNum)/100)+"元");
				}else{
					holder.tv_state.setText("免费申请");
					holder.tv_apply.setVisibility(View.GONE);
				}
				if (maps.get("alias")!=null&&maps.get("alias").toString().equals("AREAAGENT")) {
					holder.tv_buy.setEnabled(true);
				}
				if (rodid.equals("0")) {

					if (isvip.equals("1")) {
						holder.tv_state.setVisibility(View.GONE);
						holder.tv_apply.setVisibility(View.GONE);
						holder.tv_buy.setEnabled(false);
						holder.tv_buy.setText("管理");
						
					} else {
						holder.tv_state.setVisibility(View.GONE);
						holder.tv_apply.setVisibility(View.GONE);
						holder.tv_buy.setEnabled(true);
						holder.tv_buy.setText("申请");
//						holder.tv_buy.setText("已申请");
					}
				}
//
				if(rodid.equals("2")){

					if(issaleagt.equals("0")){
//						holder.tv_buy.setBackgroundResource(R.drawable.button03_bg);
						holder.tv_buy.setText("申请");
//						holder.tv_buy.setEnabled(true);
						if (isvip.equals("0")) {
							holder.tv_buy.setEnabled(false);
						} 
					}else{
						holder.tv_buy.setText("管理");
//						holder.tv_buy.setEnabled(false);
//						holder.tv_buy.setText("已申请");
						holder.tv_buy.setBackgroundResource(R.drawable.button03_bg);
						if (isvip.equals("0")) {
							holder.tv_buy.setEnabled(false);
						} 
					}

				}
				if(rodid.equals("1")){

					if(isretailers.equals("0")){
//						holder.tv_buy.setBackgroundResource(R.drawable.button03_bg);
						holder.tv_buy.setText("申请");
//						holder.tv_buy.setEnabled(true);
						if (isvip.equals("0")) {
							holder.tv_buy.setEnabled(false);
						} 
					}else{
						holder.tv_buy.setBackgroundResource(R.drawable.button03_bg);
//						holder.tv_buy.setEnabled(false);
//						holder.tv_buy.setText("已申请");
						holder.tv_buy.setText("管理");
						if (isvip.equals("0")) {
							holder.tv_buy.setEnabled(false);
						} 
					}

				}
				if(rodid.equals("3")){

					if(isgeneralagent.equals("0")){
//						holder.tv_buy.setBackgroundResource(R.drawable.button03_bg);
						holder.tv_buy.setText("申请");
//						holder.tv_buy.setEnabled(true);
						if (isvip.equals("0")) {
							holder.tv_buy.setEnabled(false);
						} 
					}else{
//						holder.tv_buy.setEnabled(false);
						holder.tv_buy.setBackgroundResource(R.drawable.button03_bg);
						holder.tv_buy.setText("管理");
//						holder.tv_buy.setText("已申请");
						if (isvip.equals("0")) {
							holder.tv_buy.setEnabled(false);
						} 
					}

				}

				
				
				
				holder.tv_buy .setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent it = new Intent();
						if(rodid.equals("0")){
							it.setClass(mContext, RateMianActivity.class);
							mContext.startActivity(it);
						}else{
							it.setClass(mContext, WebViewActivity.class);
							String alias = maps.get("alias").toString();
							String oemid = MyCacheUtil.getshared(mContext).getString("OEMID", "");
							String mobile = MyCacheUtil.getshared(mContext).getString("Mobile", "");
							String pwd = MyCacheUtil.getshared(mContext).getString("pwd", "");
							it.setClass(mContext, WebViewActivity.class);
							String URLs = HttpUrls.MEMBERUPGRADE+alias+HttpUrls.MEMBERUPGRADESUFFIX;
							String aesdata = initaes(mobile, pwd, oemid);
							Log.e("", "=- =- "+mobile+" = = = "+pwd+ "  - - "+oemid);
							it.putExtra("URLs",URLs+aesdata);
							Log.e("", ""+URLs+aesdata);
							mContext.startActivity(it);
						}
					}
				});
				
		return convertView;
	}

	class ViewHolder {
		TextView money_rate;
		TextView product_name;
		TextView balance;
		TextView money_state;
		TextView tv_buy;
		TextView tv_state;
		ImageView product_image;
		TextView money_withdrawal_fee;
		TextView tv_apply;
		
	}
}
