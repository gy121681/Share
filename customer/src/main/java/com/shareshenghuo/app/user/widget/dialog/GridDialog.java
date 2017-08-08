package com.shareshenghuo.app.user.widget.dialog;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.adapter.ShAdapter;
import com.shareshenghuo.app.user.network.bean.ScreenBean;


public class GridDialog extends Dialog implements DialogInterface.OnDismissListener{
	private Context context;
	private GridView dlg_grid = null;
	private String str;
	private int position;
	public static  List<ScreenBean> data;
	private android.view.View.OnClickListener Listener;
	private LinearLayout llayout;
	private TextView tv_config,tv_cech; 
//	private List<AreasBean> areas1;
//	  = multiChoiceDialogBuilder.getCheckedItems();

	public GridDialog(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public GridDialog(Context context, int theme, String str,List<ScreenBean> data,android.view.View.OnClickListener listener2) {
		super(context, theme);
		GridDialog.data = data;
		this.context = context;
		this.Listener = listener2;
		this.str = str;
	}
	
	public void setBackgroundAlpha(Context context, float alpha) {
		WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
		lp.alpha = alpha;
		((Activity)context).getWindow().setAttributes(lp);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置对话框使用的布局文件
		this.setContentView(R.layout.grid_dialog);
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(true);
		setOnDismissListener(this);
		initview();

//		dlg_grid = (GridView) findViewById(R.id.gridview);

//		// 设置GridView的数据源
//		SimpleAdapter adapter = new SimpleAdapter(context,//当前View
//				getPriorityList(str), //数据源
//				R.layout.grid_item,//item的布局文件
//				new String[] { "list_value" },
//				new int[] { R.id.itemText });//布局文件里面对应的view的id
//		dlg_grid.setAdapter(adapter);
//		dlg_grid.setOnItemClickListener(Listener);
		// 为GridView设置监听器
//		dlg_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				Toast.makeText(context, "" + arg2, Toast.LENGTH_SHORT).show();// 显示信息;
//				position = arg2;
//				//点击item后Dialog消失
//				PriorityDlg.this.dismiss();
//
//			}
//		});
	}
	private void initview() {
		// TODO Auto-generated method stub
		tv_config = (TextView) findViewById(R.id.tv_config);
		llayout= (LinearLayout) findViewById(R.id.llayout);
		tv_config.setOnClickListener(Listener);
		tv_cech = (TextView) findViewById(R.id.tv_cech);
		tv_cech.setOnClickListener(Listener);
		initdata(data,false);
//		tv_cech.setOnClickListener(new android.view.View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				for (int i = 0; i < data.size(); i++) {
//					for (int j = 0; j < data.get(i).child_filter_configures_list.size(); j++) {
//						 data.get(i).child_filter_configures_list.get(j).ischex = false;					
//						 }
//				}
//				
//				initdata(data);
//			}
//		});
//		initdata(data);
	}
	
	public void initdata(List<ScreenBean> data,boolean tag){
		if(tag){
			llayout.removeAllViews();
		}
		if(data==null||data.size()<=0){
			return;
		}
		
		for (int i = 0; i < data.size(); i++) {
	        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,   LayoutParams.WRAP_CONTENT);  
	        params.setMargins(10, 10, 10, 10); 
	        
	      
	        LinearLayout.LayoutParams  params1= new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,   LayoutParams.WRAP_CONTENT);
	        params1.gravity = Gravity.CENTER_VERTICAL; 
	       
//	        LinearLayout lineLayout = new LinearLayout(this);  
	        
	        TextView tv = new TextView(context);
	        tv.setBackgroundResource(R.color.bg_color);
	        tv.setPadding(10, 10, 0, 10);
	        tv.setTextSize(14);
	        tv.setText(data.get(i).configure_name);
	        
	        tv.setLayoutParams(params1);  
			final GridView gv1  = new GridView(context);
			gv1.setHorizontalSpacing(15);
			gv1.setSelector(R.color.transparent);
			gv1.setVerticalSpacing(15);
			gv1.setLayoutParams(params); 
			gv1.setNumColumns(3);
			gv1.setId(i);
			ShAdapter adapter = new ShAdapter(context, data.get(i).child_filter_configures_list,i);
			gv1.setAdapter(adapter);
	
			gv1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
//					for (int i = 0; i < count; i++) {
					Log.e("", " - - - - - "+arg3);
//						RadioButton radiobutton = (RadioButton) gv1.getAdapter()
//								.getView((int)arg3, null, null).findViewById(R.id.tvHotCity);
//						if(radiobutton.isChecked()){
//							radiobutton.setChecked(false);
//							
//						}else{
//							radiobutton.setChecked(true);
//						}
					}
						
				});
			
			llayout.addView(tv);
			llayout.addView(gv1);
		}
	}
//    android:layout_width="match_parent"
//    android:layout_height="wrap_content"
//    android:layout_marginRight="@dimen/margin_normal"
//    android:background="@color/bg_white"
//    android:horizontalSpacing="6dp"
//    android:numColumns="4"
//    android:padding="6dp"
//    android:verticalSpacing="6dp" >
	//返回点击的位置
	public List<ScreenBean> getPosition() {
		return data;
	}
	
//	public List<AreasBean> getCheckedItem() {
//		List<AreasBean> areas = new ArrayList<AreasBean>();
//		for (int i = 0; i < getCount(); i++) {
//			View view = this.getView(i, null, null);
//			CheckBox box = (CheckBox)view.findViewById(R.id.tvHotCity);
//			areas.get(i).bos = box.isChecked();
//		}
//		return areas;
//	}
	
	//得到数据，这是要显示在GridView上的数据
//	private List<HashMap<String, Object>> getPriorityList(String str) {
//		List<HashMap<String, Object>> priorityList = new ArrayList<HashMap<String, Object>>();
//		for (int i = 0; i < 4; i++) {
//			HashMap<String, Object> a = new HashMap<String, Object>();
//			a.put("list_value", str);
//			priorityList.add(a);
//		}
//		return priorityList;
//	}

@Override
public void onDismiss(DialogInterface arg0) {
	// TODO Auto-generated method stub
	this.dismiss();
	setBackgroundAlpha(context, 1f);
	
}

	public void ondess(){
		this.dismiss();
	setBackgroundAlpha(context, 1f);
	}

//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		// TODO Auto-generated method stub
//		switch (arg0.getId()) {
//		case 0:
//			Log.e("", " - - - - - "+arg2);
//			break;
//		case 1:
//			Log.e("", " - - - - - "+arg2);
//			break;
//		case 2:
//			Log.e("", " - - - - - "+arg2);
//			break;
//
//		default:
//			break;
//		}
//	}
}
