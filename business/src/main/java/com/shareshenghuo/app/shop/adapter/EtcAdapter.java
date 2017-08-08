package com.shareshenghuo.app.shop.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.bean.ShopCategoryBean;


/**
 * 
 */
public class EtcAdapter extends BaseAdapter {

	private Context context;
	private List<ShopCategoryBean> list;

	public EtcAdapter(Context context,
			 List<ShopCategoryBean> list) {
		this.context = context;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		RadioViewHolder holder = null;
		if (convertView != null) {
			holder = (RadioViewHolder) convertView.getTag();
		} else {
			holder = new RadioViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.bastextview, null);
			holder.box = (CheckBox) convertView
					.findViewById(R.id.check_del);
			holder.tvs = (TextView) convertView
					.findViewById(R.id.tvs);
			holder.tv2 = (TextView) convertView
					.findViewById(R.id.tv2);
			
			convertView.setTag(holder);
		}

		
		holder.tvs.setText(list.get(position).name);
		holder.tv2.setText("共"+list.get(position).goods_num+"件商品");
		if(list.get(position).ischeck){
			holder.box.setChecked(true);
		}else{
			holder.box.setChecked(false);
		}
		
		holder.box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				list.get(position).ischeck= arg1;
			}
		});
		
		return convertView;
	}
	

	public  void setcheck(int position) {
		
		if(list.get(position).ischeck){
			list.get(position).ischeck = false;
		}else{
			list.get(position).ischeck = true;
		}
		this.notifyDataSetChanged();
	}
	
	public List<ShopCategoryBean> getchoose(){
		
		return list;
	}

	class RadioViewHolder {
		TextView tvs,tv2;
		CheckBox box;
	}

}
