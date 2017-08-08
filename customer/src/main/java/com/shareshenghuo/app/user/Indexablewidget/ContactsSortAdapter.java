package com.shareshenghuo.app.user.Indexablewidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.shareshenghuo.app.user.Indexablewidget.SortModel;
import com.shareshenghuo.app.user.R;


public class ContactsSortAdapter extends BaseAdapter implements SectionIndexer {
	private List<SortModel> mList;
	private List<SortModel> mSelectedList;
	private Context mContext;
	LayoutInflater mInflater;
	private boolean istag = false;
	private boolean ischeck = false;
	private boolean lock = true;

	public ContactsSortAdapter(Context mContext, List<SortModel> list) {
		this.mContext = mContext;
		mSelectedList = new ArrayList<SortModel>();
		if (list == null) {
			this.mList = new ArrayList<SortModel>();
		} else {
			this.mList = list;
		}
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<SortModel> list) {
//		if (list == null) {
//			this.mList = new ArrayList<SortModel>();
//		} else {
			this.mList = list;
//		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return this.mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SortModel mContent = mList.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.fragment_phone_constacts_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
//			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvNumber = (TextView) view.findViewById(R.id.phone);
//			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.sortLetters);
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		if(this.mList.get(position).name==null||this.mList.get(position).name.equals("null")||this.mList.get(position).name.equals("")){
			viewHolder.tvTitle.setText("未知");
		}else{
			viewHolder.tvTitle.setText(this.mList.get(position).name);
		}
		
		if(this.mList.get(position).number==null||this.mList.get(position).number.equals("null")||this.mList.get(position).number.equals("")){
			viewHolder.tvNumber.setText("");
		}else{
			viewHolder.tvNumber.setText(this.mList.get(position).number);
		}
		
		return view;

	}

	public static class ViewHolder {
		public TextView tvLetter;
		public TextView tvTitle;
		public TextView tvNumber;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	@Override
	public int getSectionForPosition(int position) {
		return mList.get(position).sortLetters.charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mList.get(i).sortLetters;
			char firstChar = sortStr.toUpperCase(Locale.CHINESE).charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	private boolean isSelected(SortModel model) {
		return mSelectedList.contains(model);
		//return true;
	}
	
	public  void visib(boolean istags) {
		this.istag = istags;
		this.notifyDataSetChanged();
		//return true;
	}
	
	public  void setcheck(boolean ischeck) {
		this.ischeck = ischeck;
//		this.istag = istags;
		this.notifyDataSetChanged();
		//return true;
	}

	public void toggleChecked(int position) {
		if (isSelected(mList.get(position))) {
			removeSelected(position);
		} else {
			setSelected(position);
		}
		
	}

	private void setSelected(int position) {
		if (!mSelectedList.contains(mList.get(position))) {
			mSelectedList.add(mList.get(position));
		}
	}

	private void removeSelected(int position) {
		if (mSelectedList.contains(mList.get(position))) {
			mSelectedList.remove(mList.get(position));
		}
	}

	public List<SortModel> getSelectedList() {
		return mSelectedList;
	}
}