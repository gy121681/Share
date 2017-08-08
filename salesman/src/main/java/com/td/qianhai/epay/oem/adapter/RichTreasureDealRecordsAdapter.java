package com.td.qianhai.epay.oem.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.mpay.utils.DateUtil;

public class RichTreasureDealRecordsAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<HashMap<String, Object>> list;
	private int tag;

	public RichTreasureDealRecordsAdapter(Context context,
			ArrayList<HashMap<String, Object>> list, int tag) {
		this.list = list;
		this.mContext = context;
		this.tag = tag;
		
	}

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
					R.layout.rich_treasure_detail_item, null);
			// holder.tvTradName = (TextView) convertView
			// .findViewById(R.id.tv_operation);
			holder.tvTradMoney = (TextView) convertView
					.findViewById(R.id.tv_trading_amt);
			holder.tvTradDate = (TextView) convertView
					.findViewById(R.id.tv_operation_time);
			holder.tvTradStatus = (TextView) convertView
					.findViewById(R.id.tv_trading_state);
			holder.tvStlType = (TextView) convertView
					.findViewById(R.id.tv_type);
			convertView.setTag(holder);
//
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		HashMap<String, Object> maps = list.get(position);

		String operbtyp = maps.get("OPERBTYP").toString();
		String operstyp = maps.get("OPERSTYP").toString();
		


//		Double money = Double.parseDouble(maps.get("SUMAMT").toString());
//		holder.tvTradMoney.setText(money / 100 + "元");
		
		String date = DateUtil.strToDateToLong(maps.get("OPERTIM").toString());
		holder.tvTradDate.setText(date);
		String sts = maps.get("OPERSTS").toString();
		if ("1".equals(sts)) {
			holder.tvTradStatus.setText("成功");
		} else if ("0".equals(sts)) {
			holder.tvTradStatus.setText("失败");
		} else if ("2".equals(sts)) {
			holder.tvTradStatus.setText("处理中");
		}
		
		double d = Double.parseDouble(maps.get("SUMAMT").toString());
		String r = String .format("%.2f",d/100);
//		holder.tvTradMoney.setText(r);
		if(operbtyp.substring(0, 1).equals("0")){

			holder.tvTradMoney.setText("+ ￥"+r);
			
		}else if(operbtyp.substring(0, 1).equals("2")){
			holder.tvTradMoney.setText("- ￥"+r);
		}
		

		String typename = "";
		if (maps.get("OPERSTYPNAM") != null) {
			typename = maps.get("OPERSTYPNAM").toString();
			holder.tvStlType.setText(typename);
		}

		if (operstyp.equals("02")) {
			if (maps.get("OUTACTNAM") != null) {
				holder.tvStlType.setText(typename + " ↼ "
						+ maps.get("OUTACTNAM").toString());
			} else {
				holder.tvStlType.setText(typename);
			}
		}

		if (operstyp.equals("22")) {
			if (maps.get("PACTNAM") != null) {
				holder.tvStlType.setText(typename + " ⇀ "
						+ maps.get("PACTNAM").toString());
			} else {
				holder.tvStlType.setText(typename);
			}
		}
		if (operstyp.equals("24")) {
			if (maps.get("RECNUMBER") != null) {
				String a = maps.get("RECNUMBER").toString();
				holder.tvStlType.setText(typename + " - 尾号"
						+ a.substring(a.length() - 4));
			} else {
				holder.tvStlType.setText(typename);
			}
		}
		if (operstyp.equals("27")) {
			if (maps.get("CARDNO") != null) {
				String b = maps.get("CARDNO").toString();
				holder.tvStlType.setText(typename + " - 尾号"
						+ b.substring(b.length() - 4));
			} else {
				holder.tvStlType.setText(typename);
			}
		}
		
		if(operstyp.equals("35")){
			holder.tvStlType.setText("宝币兑换");
		}

//		}else if(operstyp.equals("2")){
//			if(tag==2){
//				Double money = Double.parseDouble(maps.get("TXNAMT").toString());
//				holder.tvTradMoney.setText(money / 100 + "元");
//				String date = DateUtil.strToDateToLong(maps.get("OPERTIM")
//						.toString());
//				holder.tvTradDate.setText(date);
//				String sts = maps.get("OPERSTS").toString();
//				if ("1".equals(sts)) {
//					holder.tvTradStatus.setText("成功");
//				} else {
//					holder.tvTradStatus.setText("失败");
//				}
//				if (operstyp.equals("0")) {
//					holder.tvStlType.setText("充值");
//				} else if (operstyp.equals("2")) {
//					holder.tvStlType.setText("提现");
//				} else if (operstyp.equals("4")) {
//					holder.tvStlType.setText("管理");
//				}
//			}
//		}else{
//			Double money = Double.parseDouble(maps.get("TXNAMT").toString());
//			holder.tvTradMoney.setText(money / 100 + "元");
//			String date = DateUtil.strToDateToLong(maps.get("OPERTIM")
//					.toString());
//			holder.tvTradDate.setText(date);
//			String sts = maps.get("OPERSTS").toString();
//			if ("1".equals(sts)) {
//				holder.tvTradStatus.setText("成功");
//			} else {
//				holder.tvTradStatus.setText("失败");
//			}
//			if (operstyp.equals("0")) {
//				holder.tvStlType.setText("充值");
//			} else if (operstyp.equals("2")) {
//				holder.tvStlType.setText("提现");
//			} else if (operstyp.equals("4")) {
//				holder.tvStlType.setText("管理");
//			}
//		}

		//
		return convertView;
	}

	class ViewHolder {
		TextView tvTradName;
		TextView tvTradMoney;
		TextView tvTradDate;
		TextView tvTradStatus;
		TextView tvStlType;
	}
}
