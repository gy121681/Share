package com.shareshenghuo.app.shop.widget.dialog;

import java.util.List;

import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.network.request.MyBankCardRequest;
import com.shareshenghuo.app.shop.util.ViewUtil;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Custom MultiChoice Dialog
 * 
 * @author duanyanrui
 * @date 2013-11-18
 * 
 */
public class BankListChoiceDialog extends Dialog {

	public BankListChoiceDialog(Context context) {
		super(context);
	}

	public BankListChoiceDialog(Context context, int theme) {
		super(context, theme);
	}
	
	

	public static class Builder {
		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private ListView listView;
		private String tags = "";
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;
		private OnItemClickListener onItemClickListener;

		private boolean showSelectAll;
		private boolean isMultiChoice = false;
		private List<MyBankCardRequest> bean;

		public Builder(Context context,String tags) {
			this.tags = tags;
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public ListView getListView() {
			return listView;
		}

		public List<MyBankCardRequest> getCheckedItems() {
			if(listView != null){
				CheckAdapter adapter = (CheckAdapter)listView.getAdapter();
				bean = adapter.getCheckedItem();
			}
			return bean;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		/**
		 * 
		 * 
		 * @param items
		 * @param checkedItems
		 * @param onItemClickListener
		 *            Listening the item click event.
		 * @param showSelectAll
		 *            Whether to display the full checkbox.
		 * @return
		 */
		public Builder setMultiChoiceItems(List<MyBankCardRequest> bean,
				OnItemClickListener onItemClickListener, boolean showSelectAll) {
			this.isMultiChoice = true;
			this.bean = bean;
			this.onItemClickListener = onItemClickListener;
			this.showSelectAll = showSelectAll;
			return this;
		}

		public BankListChoiceDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final BankListChoiceDialog dialog = new BankListChoiceDialog(context, R.style.dialog);
			View layout = inflater.inflate(R.layout.dialog_banklist_layout, null);

			// set the dialog title
			TextView multichoicTitle = (TextView) layout.findViewById(R.id.multichoic_title);
			multichoicTitle.setText(title);
			CheckBox checkAll = (CheckBox) layout.findViewById(R.id.chk_selectall);

			Button positiveButton = (Button) layout.findViewById(R.id.positiveButton);
			Button negativeButton = (Button) layout.findViewById(R.id.negativeButton);

			listView = (ListView) layout.findViewById(R.id.multichoiceList);
			
			dialog.addContentView(layout, new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

			// set the confirm button
			if (positiveButtonText != null) {
				positiveButton.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					positiveButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
							dialog.dismiss();
						}
					});
				} else {
					positiveButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				positiveButton.setVisibility(View.GONE);
			}
			// set the cancel button
			if (negativeButtonText != null) {
				negativeButton.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					negativeButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
							dialog.dismiss();
						}
					});
				} else {
					negativeButton.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				negativeButton.setVisibility(View.GONE);
			}

			// is the Multichoice(多选框)
			if (isMultiChoice == true) {

				final CheckAdapter checkAdapter = new CheckAdapter(context, bean,tags);

				listView.setAdapter(checkAdapter);
				listView.setItemsCanFocus(true);
				
				if (onItemClickListener != null) {
					listView.setOnItemClickListener(onItemClickListener);
				} else {
					listView.setOnItemClickListener(new OnMultiItemClick());
				}
				
				// show the all selectButton or not
				if (showSelectAll) {
					// checkAll.setVisibility( View.VISIBLE);
					checkAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							int count = listView.getAdapter().getCount();
							if (isChecked) {
								// update the status of all checkbox to checked
								if (count > 0) {
									for (int i = 0; i < count; i++) {
										CheckBox itemCheckBox = (CheckBox) listView.getAdapter()
												.getView(i, null, null).findViewById(R.id.chk_selectone);
										itemCheckBox.setChecked(true);
									}
								}
							} else {
								// update the status of checkbox to unchecked
								if (count > 0) {
									for (int i = 0; i < count; i++) {
										CheckBox itemCheckBox = (CheckBox) listView.getAdapter()
												.getView(i, null, null).findViewById(R.id.chk_selectone);
										itemCheckBox.setChecked(false);
									}
								}
							}
						}
					});

				} else {
					checkAll.setVisibility(View.GONE);
				}
			} else {
				checkAll.setVisibility(View.GONE);
			}

			// set the content message
			if (message != null) {
				// ((TextView)layout.findViewById(R.id.message)).setText(message);
			} else if (contentView != null) {
				// if no message set add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
				((LinearLayout) layout.findViewById(R.id.content)).addView(contentView, new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}

		class OnMultiItemClick implements OnItemClickListener {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int positon, long arg3) {
				// TODO Auto-generated method stub
				List<MyBankCardRequest> aa = ((CheckAdapter)listView.getAdapter()).getCheckedItem();
				
				CheckBox box = (CheckBox) view.findViewById(R.id.chk_selectone);
				if (box.isChecked()) {
					box.setChecked(false);
				} else {
					if(aa.get((int)arg3).is_quickpay.equals("1")){
						box.setChecked(true);
					}else{
						
					}
				}
			}
		}
		
		public class CheckAdapter extends BaseAdapter {

			private class ViewHolder {
				public TextView name;
				public ImageView img;
				public CheckBox checkBox;
			}
			private String tag = "";
			private LayoutInflater mInflater;
			private Context mContext;
			private List<MyBankCardRequest> areas;


			public CheckAdapter(Context context,List<MyBankCardRequest> areas,String tag) {
				this.areas = areas;
				this.tag =tag;
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

			public List<MyBankCardRequest> getCheckedItem() {
				
				for (int i = 0; i < getCount(); i++) {
					View view = this.getView(i, null, null);
					CheckBox box = (CheckBox)view.findViewById(R.id.chk_selectone);
					areas.get(i).bos = box.isChecked();
				}
				
				return areas;
			}

			SparseArray<View> lmap = new SparseArray<View>();

			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder holder = null;
				View view;
				if (lmap.get(position) == null) {
					view = mInflater.inflate(R.layout.dialog_banklist_item, null);
					holder = new ViewHolder();
					holder.img = (ImageView) view.findViewById(R.id.img);
					holder.name = (TextView) view.findViewById(R.id.contact_name);
					holder.checkBox = (CheckBox) view.findViewById(R.id.chk_selectone);
					lmap.put(position, view);
					
					if(areas.get(position).bank_name!=null){
						ViewUtil.setbank(holder.img , areas.get(position).bank_name);
					}
					holder.name.setTextColor(mContext.getResources().getColor(R.color.black));
					if(tags.equals("1")&&areas.get(position).is_quickpay!=null&&areas.get(position).is_quickpay.equals("0")){
						holder.name.setTextColor(mContext.getResources().getColor(R.color.gray_light));
						}else{
//							holder.name.setTextColor(mContext.getResources().getColor(R.color.black));
						}
					
					if(tags.equals("2")&&areas.get(position).is_support_withdraw!=null&&areas.get(position).is_support_withdraw.equals("0")){
						holder.name.setTextColor(mContext.getResources().getColor(R.color.gray_light));
						}else{
//							holder.name.setTextColor(mContext.getResources().getColor(R.color.black));
						}
					
					String type = "";
					if(areas.get(position).card_type!=null&&areas.get(position).card_type.equals("1")){
						type = "储蓄卡";
					}else{
						type = "信用卡";
					}
					String cardno = "";
					if(areas.get(position).card_no!=null&&areas.get(position).card_no.length()>4){
						cardno = areas.get(position).card_no.substring(areas.get(position).card_no.length()-4);
					}
					if (areas.get(position).bank_name != null) {
						String name =areas.get(position).bank_name;
						holder.name.setText(name+type+"尾号 ("+cardno+")");
					}
					holder.checkBox.setChecked(areas.get(position).bos);
					notifyDataSetChanged();
//					if(areas.get(position).bos){
//						holder.checkBox.setClickable(true);
//					}else{
//						holder.checkBox.setClickable(false);
//					}
					
					
					
					view.setTag(holder);
				} else {
					view = lmap.get(position);
					holder = (ViewHolder) view.getTag();
				}
				return view;
			}
		}
	}
	
	
}
