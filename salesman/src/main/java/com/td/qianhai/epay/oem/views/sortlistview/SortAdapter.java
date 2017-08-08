package com.td.qianhai.epay.oem.views.sortlistview;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
public class SortAdapter extends BaseAdapter implements SectionIndexer {
	private List<SortModel> list = null;
	private Context mContext;
	private ViewHolder viewHolder = null;

	public SortAdapter(Context mContext, List<SortModel> list) {
		this.mContext = mContext;
		this.list = list;
	}

	/**
	 * 锟斤拷ListView锟斤拷莘锟斤拷锟戒化时,锟斤拷锟矫此凤拷锟斤拷4锟斤拷锟斤拷ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<SortModel> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {

		final SortModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.listview_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		// viewHolder.tvTitle.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// if(event.getAction()==MotionEvent.ACTION_DOWN){
		// viewHolder.tvTitle.setTextColor(Color.WHITE) ;
		// }else if(event.getAction()==MotionEvent.ACTION_UP){
		// viewHolder.tvTitle.setTextColor(Color.BLACK) ;
		// }
		// return false;
		// }
		// }) ;
		// 锟斤拷锟絧osition锟斤拷取锟斤拷锟斤拷锟斤拷锟斤拷锟侥革拷锟紺har ascii值
		int section = getSectionForPosition(position);

		// 锟斤拷锟角拔伙拷玫锟斤拷诟梅锟斤拷锟斤拷锟斤拷锟侥革拷锟紺har锟斤拷位锟斤拷 锟斤拷锟斤拷锟斤拷为锟角碉拷一锟轿筹拷锟斤拷
		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		} else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}

		viewHolder.tvTitle.setText(this.list.get(position).getName());

		return view;

	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
	}

	/**
	 * 锟斤拷锟絃istView锟侥碉拷前位锟矫伙拷取锟斤拷锟斤拷锟斤拷锟斤拷锟侥革拷锟紺har ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 锟斤拷莘锟斤拷锟斤拷锟斤拷锟斤拷母锟斤拷Char ascii值锟斤拷取锟斤拷锟揭伙拷纬锟斤拷指锟斤拷锟斤拷锟侥革拷锟轿伙拷锟�
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 锟斤拷取英锟侥碉拷锟斤拷锟斤拷母锟斤拷锟斤拷英锟斤拷锟斤拷母锟斤拷#锟斤拷锟芥。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 锟斤拷锟斤拷锟斤拷式锟斤拷锟叫讹拷锟斤拷锟斤拷母锟角凤拷锟斤拷英锟斤拷锟斤拷母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}