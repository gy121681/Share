package com.shareshenghuo.app.user.widget.dialog;

import java.util.List;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.AreasBean;
import com.shareshenghuo.app.user.widget.dialog.CheckAdapter;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener1;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
public class CustomMultiChoiceDialog extends Dialog  {
	public CustomMultiChoiceDialog(Context context) {
		super(context);
	}

	public CustomMultiChoiceDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder  {
		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private ListView listView;
		private LinearLayout lllayout;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;
		private OnItemClickListener onItemClickListener;
		private  CustomMultiChoiceDialog dialog;
		private boolean showSelectAll;
		private boolean isMultiChoice = false;
		public OnMyDialogClickListener1 lis1;
		public  OnMyDialogClickListener1 lis;
		private boolean tag = false,isshowbutton = false;
		private CheckAdapter checkAdapter;
		private int positions = 0;
		private List<AreasBean> areas;
		
		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public ListView getListView() {
			return listView;
		}

		public List<AreasBean> getCheckedItems() {
			List<AreasBean> areas1 = null;
			if(listView != null){
				CheckAdapter adapter = (CheckAdapter)listView.getAdapter();
				areas1 = adapter.getCheckedItem();
			}
			return areas1;
		}
		
		public int getCheckednum() {
			List<AreasBean> areas1 = null;
			if(tag){
				return positions;
			}
			int num = -1;
			if(listView != null){
				CheckAdapter adapter = (CheckAdapter)listView.getAdapter();
				areas1 = adapter.getCheckedItem();
			}
			for (int i = 0; i < areas1.size(); i++) {
				if(areas1.get(i).bos){
					num++;
				}
			}
			return num;
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

		public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener,OnMyDialogClickListener1 onclic) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			this.lis1 = onclic;
			return this;
		}
		

		public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener,OnMyDialogClickListener1 onclic) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			this.lis = onclic;
			return this;
		}
		
//		//点击确定取消监听
//		public Builder setPositive(String positiveButtonText, OnMyDialogClickListener onclic) {
//			this.negativeButtonText = positiveButtonText;
//			this.lis = onclic;
//			return this;
//		}
//		public Builder setPositiveb(String positiveButtonText, OnMyDialogClickListener onclic) {
//			this.positiveButtonText = positiveButtonText;
//			this.lis = onclic;
//			return this;
//		}

		/**
		 * 
		 * 
		 * @param list 
		 * @param items
		 * @param checkedItems
		 * @param onItemClickListener
		 *            Listening the item click event.
		 * @param showSelectAll
		 *            Whether to display the full checkbox.
		 * @param b 
		 * @return
		 */
		public Builder setMultiChoiceItems(List<AreasBean> list,boolean tag,
				OnItemClickListener onItemClickListener, boolean showSelectAll) {
			this.tag = tag;
			this.isMultiChoice = true;
			this.areas = list;
			this.onItemClickListener = onItemClickListener;
			this.showSelectAll = showSelectAll;
			return this;
		}

		public CustomMultiChoiceDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			  dialog = new CustomMultiChoiceDialog(context, R.style.Transparentdialog);
			View layout = inflater.inflate(R.layout.dialog_multichoice_layout, null);
			
			// set the dialog title
			TextView multichoicTitle = (TextView) layout.findViewById(R.id.multichoic_title);
			multichoicTitle.setText(title);
			CheckBox checkAll = (CheckBox) layout.findViewById(R.id.chk_selectall);

			Button positiveButton = (Button) layout.findViewById(R.id.positiveButton);
			Button negativeButton = (Button) layout.findViewById(R.id.negativeButton);


			listView = (ListView) layout.findViewById(R.id.multichoiceList);
			lllayout = (LinearLayout) layout.findViewById(R.id.lllayout);
			lllayout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			dialog.addContentView(layout, new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			
			// set the confirm button
			if (positiveButtonText != null) {
				positiveButton.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					positiveButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
							lis.onClick(v,dialog,DialogInterface.BUTTON_POSITIVE);
							dialog.dismiss();
						}
					});
				} else {
					positiveButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
//							lis1.onClick(v,dialog,DialogInterface.BUTTON_POSITIVE);
//							dialog.dismiss();
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
						@Override
						public void onClick(View v) {
						
							negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
							lis.onClick(v,dialog,DialogInterface.BUTTON_NEUTRAL);
							dialog.dismiss();
						}
					});
				} else {
					negativeButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
//							lis.onClick(v,dialog,DialogInterface.BUTTON_NEUTRAL);
//							dialog.dismiss();
						}
					});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				negativeButton.setVisibility(View.GONE);
			}
//
//			positiveButton.setOnClickListener((android.view.View.OnClickListener) this);
//			negativeButton.setOnClickListener((android.view.View.OnClickListener) this);
			
			// is the Multichoice(多选框)
			if (isMultiChoice == true) {

				 checkAdapter = new CheckAdapter(context, areas);

				listView.setAdapter(checkAdapter);
				listView.setItemsCanFocus(true);
				
				if (onItemClickListener != null) {
					listView.setOnItemClickListener(onItemClickListener);
				} else {
					listView.setOnItemClickListener(new OnMultiItemClick());
					
				}
				layout.findViewById(R.id.lltitle).setVisibility(View.GONE);
				if(tag){
					((LinearLayout) layout.findViewById(R.id.buttons)).setVisibility(View.GONE);
					checkAll.setEnabled(false);
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
				
					int count = listView.getAdapter().getCount();
					CheckBox box = (CheckBox) view.findViewById(R.id.chk_selectone);
					if (box.isChecked()) {
						box.setChecked(false);
					} else {
						box.setChecked(true);
						positions = positon;
						if(tag){
							for (int i = 0; i < count; i++) {
								CheckBox itemCheckBox = (CheckBox) listView.getAdapter()
										.getView(i, null, null).findViewById(R.id.chk_selectone);
								if(i!=positon){
									itemCheckBox.setChecked(false);
								}
								
							}
						}
				}
			}
		}

//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			lis.onClick(v);
//		}
	}

}
