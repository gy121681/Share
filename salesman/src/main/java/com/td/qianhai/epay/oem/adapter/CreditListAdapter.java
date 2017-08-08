package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;
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

public class CreditListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> list;

	public CreditListAdapter(Context context,
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
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.credit_list_item, null);
			// holder.tvTradName = (TextView) convertView
			// .findViewById(R.id.tv_operation);
			holder.bankname_tv = (TextView) convertView
					.findViewById(R.id.bankname_tv);
			holder.cardnum_tv = (TextView) convertView
					.findViewById(R.id.cardnum_tv);
			holder.cardname_tv = (TextView) convertView
					.findViewById(R.id.cardname_tv);
			holder.creditcard_img = (ImageView) convertView
					.findViewById(R.id.creditcard_img);
			holder.mian_bg = (LinearLayout) convertView.findViewById(R.id.mian_bg);
			convertView.setTag(holder);
//
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
//		if(position%2==0){
////			holder.mian_bg.setb
//			
//		}
				if(list.size()>0){
					HashMap<String, Object> maps = list.get(position);
					
					if(maps.get("FRPID")!=null){
					
					String cardname = maps.get("FRPID").toString();
					if (cardname.equals("CMBCHINACREDIT")) {
						holder.mian_bg.setBackgroundResource(R.drawable.cmbchinacredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_cmb);
					}else if(cardname.equals("ABCCREDIT")) {
						holder.mian_bg.setBackgroundResource(R.drawable.abccredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_abc);
						
					}else if(cardname.equals("BCCBCREDIT")){
						holder.mian_bg.setBackgroundResource(R.drawable.cmbchinacredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_bjb);
					}else if(cardname.equals("BOCCREDIT")){
						holder.mian_bg.setBackgroundResource(R.drawable.cmbchinacredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_boc);
					}else if(cardname.equals("CCBCREDIT")){
						holder.mian_bg.setBackgroundResource(R.drawable.ccbcredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_ccb);
					}else if(cardname.equals("EVERBRIGHTCREDIT")){
						holder.mian_bg.setBackgroundResource(R.drawable.everbrightcredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_cebb);
					}else if(cardname.equals("CIBCREDIT")){
						holder.mian_bg.setBackgroundResource(R.drawable.ccbcredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_cib);
					}else if(cardname.equals("ECITICCREDIT")){
						holder.mian_bg.setBackgroundResource(R.drawable.cmbchinacredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_citic);
					}else if(cardname.equals("CMBCCREDIT")){
						holder.mian_bg.setBackgroundResource(R.drawable.cmbccredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_cmbc);
					}else if(cardname.equals("BOCOCREDIT")){
						holder.mian_bg.setBackgroundResource(R.drawable.ccbcredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_comm);
					}else if(cardname.equals("HXBCREDIT")){
						holder.mian_bg.setBackgroundResource(R.drawable.cmbchinacredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_hxb);
					}else if(cardname.equals("GDBCREDIT")){
						holder.mian_bg.setBackgroundResource(R.drawable.gdbcredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_gdb);
					}else if(cardname.equals("PSBCCREDIT")){
						holder.mian_bg.setBackgroundResource(R.drawable.abccredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_psbc);
					}else if(cardname.equals("ICBCCREDIT")){
						holder.mian_bg.setBackgroundResource(R.drawable.cmbchinacredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_icbc);
					}else if(cardname.equals("PINGANCREDIT")){
						holder.mian_bg.setBackgroundResource(R.drawable.pingancredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_spa);
					}else if(cardname.equals("SPDBCREDIT")){  
						holder.mian_bg.setBackgroundResource(R.drawable.ccbcredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_spdb);
					}else if(cardname.equals("SDBCREDIT")){  
						holder.mian_bg.setBackgroundResource(R.drawable.ccbcredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_spdb);
					}else if(cardname.equals("BSBCREDIT")){
						holder.mian_bg.setBackgroundResource(R.drawable.bsbcredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_bsb);
					}else if(cardname.equals("BOSHCREDIT")){
						holder.mian_bg.setBackgroundResource(R.drawable.ccbcredit);
						holder.creditcard_img.setImageResource(R.drawable.ps_sh);
					}else{
						holder.mian_bg.setBackgroundResource(R.drawable.unionpay);
						holder.creditcard_img.setImageResource(R.drawable.ps_unionpay);
					}
					
				}else{
					holder.mian_bg.setBackgroundResource(R.drawable.unionpay);
					holder.creditcard_img.setImageResource(R.drawable.ps_unionpay);
				}

				if(maps.get("ISSUER")!=null){
					holder.bankname_tv.setText(maps.get("ISSUER").toString());
				}else{
					holder.bankname_tv.setText("");
				}
				
				if(maps.get("CARDCODE")!=null){
					String cardno = maps.get("CARDCODE").toString();
					holder.cardnum_tv.setText("**** **** **** "+cardno.substring(cardno.length()-4));
				}
				if(maps.get("CARDNAME")!=null){
					
					holder.cardname_tv.setText(maps.get("CARDNAME").toString());
				}else{
					holder.cardname_tv.setText("未知");
				}
				}

				
				
				
				

		return convertView;
	}

	class ViewHolder {
		TextView bankname_tv;
		TextView cardnum_tv;
		TextView cardname_tv;
		ImageView creditcard_img;
		LinearLayout mian_bg;
		
	}
}
