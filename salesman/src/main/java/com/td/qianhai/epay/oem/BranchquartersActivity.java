package com.td.qianhai.epay.oem;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.td.qianhai.epay.oem.adapter.ReceivablesListAdapter;
import com.td.qianhai.epay.oem.adapter.ReceivablesListAdapter1;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.WechatListBean;
import com.td.qianhai.epay.oem.dateutil.ScreenInfo;
import com.td.qianhai.epay.oem.dateutil.WheelMain;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;

public class BranchquartersActivity extends BaseActivity{
	private ArrayList<HashMap<String, Object>> mList;
	private List<WechatListBean>  WechatListBeanlist= null;
	private ListView listView;
	private String mobile;
//	private WechatListAdapter adapter;
	private ReceivablesListAdapter1 adapter;
	private TextView cb_title_contre,tv_pros;
	private RelativeLayout lin_1;
	private View view;
	
	private WheelMain wheelMain;
	
	private LayoutInflater inflater;
	
	private PopupWindow mPopupWindowDialog;
	
	private Button determine,cacel;
	
	private String setdate,subMchId,shopCodeMobile,shopName,flag,isSubbranch;
	
	private TextView tv_pro;
	
	private String more = "0",totalFee = "",orderCount = "",payDate = "";
	
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wechat_list);
		AppContext.getInstance().addActivity(this);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		
		Intent it = getIntent();
		subMchId = it.getStringExtra("flag");
		shopCodeMobile = it.getStringExtra("shopCodeMobile");
		isSubbranch  = it.getStringExtra("isSubbranch");
		shopName = it.getStringExtra("shopName");
		totalFee = it.getStringExtra("totalFee");
		orderCount = it.getStringExtra("orderCount");
		payDate = it.getStringExtra("payDate");
		setdate = payDate;//initdate(1);
		initview();
	}

	private String initdate(int a){
		SimpleDateFormat formatter = null; 
		if(a==1){
			formatter = new SimpleDateFormat ("yyyy-MM");
		}else{
			formatter = new SimpleDateFormat ("yyyy年MM月"); 
		}
		
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间 
		String str = formatter.format(curDate); 
		return str;
	}
	
	private void initview() {
//		if(subMchId!=null&&subMchId!=null&&subMchId.equals("01")){
//			((TextView) findViewById(R.id.tv_title_contre)).setText("总店记录");
//		}else{
		cb_title_contre = (TextView) findViewById(R.id.bt_title_right);
		if(shopName!=null){
			if(shopName.equals("null")){
				((TextView) findViewById(R.id.tv_title_contre)).setText("");
			}else{
				((TextView) findViewById(R.id.tv_title_contre)).setText(shopName);
			}
		}else{
			((TextView) findViewById(R.id.tv_title_contre)).setText("");
		}
			
//		}
				// TODO Auto-generated method stub
//		((TextView) findViewById(R.id.tv_title_contre)).setText("二维码收款记录");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		listView = (ListView) findViewById(R.id.mylist);
	
		mList = new ArrayList<HashMap<String,Object>>();
		tv_pro = (TextView) findViewById(R.id.tv_pro);
		WechatListBeanlist = new ArrayList<WechatListBean>();
		lin_1 = (RelativeLayout) findViewById(R.id.lin_1);
//		adapter = new WechatListAdapter(this, mList);
		tv_pros = (TextView) findViewById(R.id.tv_pros);
		adapter = new ReceivablesListAdapter1(this, mList, subMchId);
		listView.setAdapter(adapter);
//		listView.setGroupIndicator(null);
//		cb_title_contre.setText(initdate(2));
		
		if(subMchId!=null&&subMchId.equals("01")){ 
			String month = "";
     	   if(initData().equals(payDate.substring(payDate.length()-2))){
     				month = "本月";
     	   }else{
     		  month = payDate.substring(payDate.length()-2)+"月";
     	   }
			
			
 		  tv_pros.setText(month+"总金额: "+Double.parseDouble(totalFee)/100+"元 ("+orderCount+"笔)");
	   }else{
		   tv_pros.setText(payDate+"总金额: "+totalFee+"元 ("+orderCount+"笔)");
		   cb_title_contre.setVisibility(View.GONE);
		}

		if (mList.size() <= 0) {
			// 加载数据
			loadMore();
		    }
		cb_title_contre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showdate();
			}
		});
		
//		listView.setOnGroupClickListener(new OnGroupClickListener() {
//			
//			@Override
//			public boolean onGroupClick(ExpandableListView parent, View v,
//					int groupPosition, long id) {
//				// TODO Auto-generated method stub
//				return true;
//			}
//		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				Intent it = new Intent(BranchquartersActivity.this,BranchquartersDayActivity.class);
				it.putExtra("shopCodeMobile",shopCodeMobile);
				it.putExtra("payDate", mList.get((int)arg3).get("payDay").toString().replaceAll("-", ""));
				it.putExtra("flag", "02");
				startActivity(it);
			}
		});
		

		
	}
	
	

	private void loadMore() {
//		WechatListBeanlist.clear();
		new Thread(run).start();
		
		
	}
	
//	Runnable run = new Runnable() {
//
//		@Override
//		public void run() {
//			ArrayList<HashMap<String, Object>> list = null;
//			String a = "0";
//			if(subMchId.equals("01")){
//				a= "1";
//			}else{
//				a = "0";
//			}
//				String[] values = { HttpUrls.WECHATLISTDTL+"",shopCodeMobile,setdate,isSubbranch,more,a};
//				list = NetCommunicate.getList(HttpUrls.ETCLIST, values,HttpKeys.WECHATLISTDTL_BACK);
//
//			Message msg = new Message();
//
//			if (list != null) {
//				mList.addAll(list);
//				if (list.size() <= 0 || list == null) {
//
//					msg.what = 2;
//				} else {
//					msg.what = 1;
//				}
//				
////				int allNum = 0;
////				if(list.size()>0){
////				if(list.get(0).get("TOLCNT")!=null&&!list.get(0).get("TOLCNT").equals("null")){
////					 allNum = Integer.parseInt(list.get(0).get("TOLCNT").toString());
////				}
////				}
//
////				if (allNum % PAGE_SIZE != 0) {
////					allPageNum = allNum / PAGE_SIZE + 1;
////				} else {
////					allPageNum = allNum / PAGE_SIZE;
////				}
////				page++;
//				
//			} else {
//
//				msg.what = 3;
//			}
//			loadingDialogWhole.dismiss();
//			handler.sendMessage(msg);
//		}
//	};

	
	Runnable run = new Runnable() {

		@Override
		public void run() {
			String a = "0";
			if(subMchId!=null&&subMchId.equals("01")){
				a= "1";
			}else{
				shopCodeMobile = mobile;
				a = "0";
			}
			
			Log.e("", "payDate = = = "+payDate);
			try {
				String[] values = {shopCodeMobile,payDate,""};
				ArrayList<HashMap<String, Object>> List = NetCommunicate.executeHttpPostgetjsonlist(HttpUrls.ALIPAYBRANCHLIST,
						HttpKeys.ALIPAYBRANCHLIST_BACK,HttpKeys.ALIPAYBRANCHLIST_ASK,values,1);
				mList.addAll(List);
//				WechatListBeanlist.addAll(list);
			} catch (HttpHostConnectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Message msg = new Message();
//			if (list != null) {
//				mList.addAll(list);
				if (mList.size() <= 0 || mList == null) {

					msg.what = 2;
				} else {
					msg.what = 1;
				}
//			} else {
//				
//				msg.what = 3;
//			}
//			isThreadRun = false;
			
			handler.sendMessage(msg);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				tv_pro.setVisibility(View.GONE);
				
				if(mList.get(0).get("monthTotalFee")!=null){
					
					String month = "";
			     	   if(initData().equals(setdate.substring(setdate.length()-2))){
			     				month = "本月";
			     	   }else{
			     		  month = setdate.substring(setdate.length()-2)+"月";
			     	   }
					
					tv_pros.setText(month+"总金额: "+Double.parseDouble(mList.get(0).get("monthTotalFee").toString())/100+"元 ("+mList.get(0).get("monthTotalCount").toString()+"笔)");
				}else{
					tv_pros.setText("");
				}
				
//			     int groupCount = listView.getCount();
//
//			     for (int i=0; i<groupCount; i++) {
//
//			    	 listView.expandGroup(i);
//
//			         };
				break;
			case 2:
				tv_pros.setText("");
				adapter.notifyDataSetChanged();
				tv_pro.setVisibility(View.VISIBLE);
				break;
			case 3:
				break;
			default:
				break;
			}
		};
	};
	
    class MyAdapter extends BaseExpandableListAdapter {
        
        //得到子item需要关联的数据
        @Override
        public Object getChild(int groupPosition, int childPosition) {
//            String key = WechatListBeanlist.get(groupPosition).getPayTime();
           
            return ( WechatListBeanlist.get(groupPosition).list.get(childPosition));
        }
 
        //得到子item的ID
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }
 
        //设置子item的组件
        @Override
        public View getChildView(int groupPosition, int childPosition,
                boolean isLastChild, View convertView, ViewGroup parent) {
//        	Log.e("", "     asda   "+(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("totalFee").toString()));
            String info = WechatListBeanlist.get(groupPosition).list.get(childPosition).get("totalFee").toString();
//            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) BranchquartersActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.wechat_list_item, null);
//            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.tv_money);
            TextView tv_money = (TextView) convertView
                    .findViewById(R.id.tv_money);
            TextView tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            TextView tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            TextView tv_type = (TextView) convertView
                    .findViewById(R.id.tv_type);
            ImageView m_img = (ImageView) convertView.findViewById(R.id.m_img);
            m_img.setVisibility(View.GONE);
            
    		if(subMchId!=null&&subMchId.equals("01")){
                if(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("payDate")!=null){
                	String aa = WechatListBeanlist.get(groupPosition).list.get(childPosition).get("payDate").toString();
//                	tv_time.setText(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("payDate").toString());
//                	  tv_money.setText(getWeek(aa+"00000000"));
                    if(aa!=null&&aa.length()>=1){
                    	aa = aa.replace("-", "");
                    	tv_type.setText(getWeek(aa+"00000000"));
                    }
                    
                    tv_time.setText(aa);
                }
  	          
    		}else{
              if(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("payDate")!=null){
            		String aa = WechatListBeanlist.get(groupPosition).list.get(childPosition).get("payDate").toString();
            		tv_type.setText(aa);
              }
    		}
//          if(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("shopName")!=null){
//        	  tv_type.setText(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("shopName").toString());
//          }
//          if(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("orderCount")!=null){
//        	  tv_name.setText("共"+WechatListBeanlist.get(groupPosition).list.get(childPosition).get("orderCount").toString()+"笔");
//          }
           
//            if(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("payDate")!=null){
//            	String aa = WechatListBeanlist.get(groupPosition).list.get(childPosition).get("payDate").toString();
//            }
            tv.setText("+"+info);
            return convertView;
        }
 
        //获取当前父item下的子item的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            int size= WechatListBeanlist.get(groupPosition).list.size();
            return size;
        }
      //获取当前父item的数据
        @Override
        public Object getGroup(int groupPosition) {
            return WechatListBeanlist.get(groupPosition);
        }
 
        @Override
        public int getGroupCount() {
            return WechatListBeanlist.size();
        }
 
        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }
       //设置父item组件
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                View convertView, ViewGroup parent) {
//            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) BranchquartersActivity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.expandable_item, null);
//            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.tv_date);
            TextView tv_money= (TextView) convertView
                    .findViewById(R.id.tv_money);
            TextView tv_num= (TextView) convertView
                    .findViewById(R.id.tv_num);
            TextView tv_week= (TextView) convertView
                    .findViewById(R.id.tv_week);
         String aa = null;
            if(WechatListBeanlist.get(groupPosition).getPayDate()!=null){
            	 
            	   aa = WechatListBeanlist.get(groupPosition).getPayDate();
            	   
            	   if(subMchId!=null&&subMchId.equals("01")){
//                	   if(initData().equals(aa.substring(aa.length()-2))){
                		   tv.setText("本月当前总金额");
//                	   }else{
//                		   tv.setText(aa.substring(aa.length()-2)+"月总店总金额");
//                	   }
            	   }else{
            		   tv.setText(WechatListBeanlist.get(groupPosition).getPayDate()+"总金额");
            	   }

            	   
            	   
            }
            
            if(WechatListBeanlist.get(groupPosition).getTotalFee()!=null){
            	tv_week.setText(WechatListBeanlist.get(groupPosition).getTotalFee()+"元");//"￥"
            }
            
            
//            if(WechatListBeanlist.get(groupPosition).getOrderCount()!=null){
//            	tv_num.setText("共"+WechatListBeanlist.get(groupPosition).getOrderCount()+"笔");
//            }
            
            
            if(WechatListBeanlist.get(groupPosition).getOrderCount()!=null){
            	tv_num.setText(""+WechatListBeanlist.get(groupPosition).getOrderCount()+"笔");
            }
//            if(aa!=null&&aa.length()>=1){
//            	aa = aa.replace("-", "");
//            	 tv_week.setText(getWeek(aa+"00000000"));
//            }
           
            return convertView;
        }
 
        @Override
        public boolean hasStableIds() {
            return true;
        }
 
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
 
    }
    
    public String initData(){
		Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); //获取当前年份
        int mMonth = c.get(Calendar.MONTH)+1;//获取当前月份;
        String strMonth ="";
        if(mMonth<10){
        	strMonth = "0"+String.valueOf(mMonth);
        }else
        	strMonth = String.valueOf(mMonth);
        return strMonth;
    }
    
    private String getWeek(String date) {
        String Week = "周";   
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//也可将此值当参数传进来
//        try {
//			format.parse(date);
//		} catch (ParseException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//        Date  curDate = new Date(System.currentTimeMillis());
        String pTime = date;//format.format(curDate);  
        Calendar c = Calendar.getInstance();
        try {
         c.setTime(format.parse(date));
        } catch (ParseException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
        }
        switch(c.get(Calendar.DAY_OF_WEEK)){
        case 1:
            Week += "日";
            break;
        case 2:
            Week += "一";
            break;
        case 3:
            Week += "二";
            break;
        case 4:
            Week += "三";
            break;
        case 5:
            Week += "四";
            break;
        case 6:
            Week += "五";
            break;
        case 7:
            Week += "六";
            break;
        default:
            break;          
        }           
        Log.e("", ""+Week);
        return Week;
       }
    
    
	private void showdate() {
//		setLayoutY(lin_1, 0);
		
//		Calendar calendar = Calendar.getInstance();
//
//		int year = calendar.get(Calendar.YEAR);
//		int month = calendar.get(Calendar.MONTH);
//		int day = 1;
//		int hour = calendar.get(Calendar.HOUR_OF_DAY);
//		int min = calendar.get(Calendar.MINUTE);
		
		
		Calendar calendar = Calendar.getInstance();

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		
		view = inflater.inflate(R.layout.choose_dialog, null);
		setPopupWindowDialog();

		ScreenInfo screenInfo = new ScreenInfo(BranchquartersActivity.this);
		wheelMain = new WheelMain(view, 10);
		wheelMain.screenheight = screenInfo.getHeight();
		wheelMain.initDateTimePicker(year, month, day, hour, min);

		if (mPopupWindowDialog != null) {
			mPopupWindowDialog.showAtLocation(
					findViewById(R.id.lin_1), Gravity.BOTTOM
							| Gravity.CENTER_HORIZONTAL, 0, 0);
		}

		bottomBtn();
		
	}
	
	protected void setPopupWindowDialog() {
		// TODO Auto-generated method stub
		determine = (Button) view.findViewById(R.id.textview_dialog_album);
		cacel = (Button) view.findViewById(R.id.textview_dialog_cancel);

		mPopupWindowDialog = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindowDialog.setAnimationStyle(R.style.popwin_anim_style);  
		mPopupWindowDialog.setFocusable(false);
		mPopupWindowDialog.update();
		mPopupWindowDialog.setBackgroundDrawable(new BitmapDrawable(
				getResources(), (Bitmap) null));
//		mPopupWindowDialog.setBackgroundDrawable(getResources().getDrawable(R.drawable.air_city_button));
		mPopupWindowDialog.setOutsideTouchable(true);
//		mPopupWindowDialog.setClippingEnabled(false);
	}
	
	
	protected void bottomBtn() {
		// TODO Auto-generated method stub
		determine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if (mPopupWindowDialog != null
						&& mPopupWindowDialog.isShowing()) {
					mPopupWindowDialog.dismiss();
//					setLayoutY(lin_1, 0);
				}
				
			}
		});

		cacel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String dates = wheelMain.getTime();
				if(wheelMain.getTime().length()>=8){
					String a = dates.substring(0, 4);
					String b = dates.substring(4, 6);
					String c = dates.substring(dates.length()-2);
//					cb_title_contre.setText(a+"年"+b+"月");
					payDate = a+b;
					String d = b;
					mList.clear();
					adapter.notifyDataSetChanged();
					if(subMchId!=null&&subMchId.equals("01")){ 
						if(d.substring(0, 1).equals("0")){
							d = d.substring(1, 2);
						}
//						if(initData().equals(b)){
//							tv_pros.setText("本月当前总金额: "+Double.parseDouble(totalFee)/100+"元 ("+orderCount+"笔)");
//						}else{
//							tv_pros.setText(d+"月总金额: "+Double.parseDouble(totalFee)/100+"元 ("+orderCount+"笔)");
//						}
				 		  
					   }else{
//						   tv_pros.setText(payDate+"总金额: "+totalFee+"元 ("+orderCount+")笔");
						}
					
					loadMore();
				}
				
				if (mPopupWindowDialog != null
						&& mPopupWindowDialog.isShowing()) {
					mPopupWindowDialog.dismiss();
//					setLayoutY(lin_1, 0);
				}
			}
		});
	}
	
	public static void setLayoutY(View view,int y) 
	{ 
	MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams()); 
	margin.setMargins(margin.leftMargin,y, margin.rightMargin, y+margin.height); 
	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(margin); 
	view.setLayoutParams(layoutParams); 
	}

}
