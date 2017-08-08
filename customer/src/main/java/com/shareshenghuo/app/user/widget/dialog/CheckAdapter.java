package com.shareshenghuo.app.user.widget.dialog;


import java.util.List;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.AreasBean;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * @author： duanyanrui.
 * @创建时间： 2013-3-1 09:03:46.
 * 
 * @类说明：A custom adapter inherits BaseAdapter.
 */
public class CheckAdapter extends BaseAdapter {

	private class ViewHolder {
		public TextView name;
		public CheckBox checkBox;
	}

	private LayoutInflater mInflater;
	private Context mContext;
	private List<AreasBean> areas;


	public CheckAdapter(Context context,List<AreasBean> areas) {
		this.areas = areas;
		this.mContext = context;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return areas.size();
	}

	@Override
	public Object getItem(int position) {
		return areas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getItemView(int position) {
		return lmap.get(position);
	}

	public List<AreasBean> getCheckedItem() {
		
		for (int i = 0; i < getCount(); i++) {
			View view = this.getView(i, null, null);
			CheckBox box = (CheckBox)view.findViewById(R.id.chk_selectone);
			areas.get(i).bos = box.isChecked();
		}
		
		return areas;
	}

	SparseArray<View> lmap = new SparseArray<View>();

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		View view;
		if (lmap.get(position) == null) {
			view = mInflater.inflate(R.layout.dialog_multichoice_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) view.findViewById(R.id.contact_name);
			holder.checkBox = (CheckBox) view.findViewById(R.id.chk_selectone);
			lmap.put(position, view);
			
			if (areas.get(position).name != null) {
				String name =areas.get(position).name;
				holder.name.setText(name);
				holder.checkBox.setChecked(areas.get(position).bos);
				holder.name.setTextColor(mContext.getResources().getColor(areas.get(position).bos ? R.color.text_orange : R.color.text_black));
			}
			
			view.setTag(holder);
		} else {
			view = lmap.get(position);
			holder = (ViewHolder) view.getTag();
		}
		return view;
	}

}