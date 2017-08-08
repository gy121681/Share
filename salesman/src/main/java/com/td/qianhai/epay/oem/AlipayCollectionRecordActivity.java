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
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.WechatListBean;
import com.td.qianhai.epay.oem.dateutil.ScreenInfo;
import com.td.qianhai.epay.oem.dateutil.WheelMain;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;

public class AlipayCollectionRecordActivity extends BaseActivity{
	
//	private ArrayList<HashMap<String, Object>> mList;
	private List<WechatListBean>  WechatListBeanlist;
	private ArrayList<HashMap<String, Object>> mList;
	private HashMap<String, Object> result = null; 
	private ExpandableListView listView;
	private String mobile;
//	private WechatListAdapter adapter;
	private MyAdapter adapter;
	private TextView cb_title_contre;
	private RelativeLayout lin_1;
	private View view;
	
	private WheelMain wheelMain;
	
	private LayoutInflater inflater;
	
	private PopupWindow mPopupWindowDialog;
	
	private Button determine,cacel;
	
	private String setdate = "",frontdate = "",subMchId;
	
	private TextView tv_pro;
	
	private int stat = 0;
	
	private RelativeLayout re_title;
	
	private RelativeLayout re_title1;
	
	private TextView tv3,tv4;
	
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wechat_collection_record_activity);
		AppContext.getInstance().addActivity(this);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		setdate = initdate(1);
		frontdate = initfrontdate();
		Intent it = getIntent();
		subMchId = it.getStringExtra("subMchId");
		if(subMchId!=null&&subMchId.equals("01")){
			
		}else{
			
		}
		initview();
	}

	private String initdate(int a){
		SimpleDateFormat formatter = null; 
		if(a==1){
			formatter = new SimpleDateFormat ("yyyyMM");
		}else{
			formatter = new SimpleDateFormat ("yyyy年MM月"); 
		}
		
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间 
		String str = formatter.format(curDate); 
		return str;
	}
	
	private String initfrontdate(){
		Calendar ca = Calendar.getInstance();//得到一个Calendar的实例 
		ca.setTime(new Date()); //设置时间为当前时间 
		ca.add(Calendar.MONTH, -1); //减1 
		Date lastMonth = ca.getTime(); //结果
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMM"); 
		return  sf.format(lastMonth);
	}
	
	
	private void initview() {
		tv3 = (TextView) findViewById(R.id.tv1);
		tv4 = (TextView) findViewById(R.id.tv2);
		re_title = (RelativeLayout) findViewById(R.id.re_title);
		re_title1 = (RelativeLayout) findViewById(R.id.re_title1);
		re_title.setVisibility(View.VISIBLE);
		re_title1.setVisibility(View.VISIBLE);
		WechatListBeanlist = new ArrayList<WechatListBean>();
		mList = new ArrayList<HashMap<String,Object>>();
				// TODO Auto-generated method stub
		if(subMchId!=null&&subMchId!=null&&subMchId.equals("01")){
			((TextView) findViewById(R.id.tv_title_contre)).setText("总店记录");
		}else{
			((TextView) findViewById(R.id.tv_title_contre)).setText("分店记录");
			re_title1.setVisibility(View.GONE);
		}
		
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		listView = (ExpandableListView) findViewById(R.id.mylist);
		cb_title_contre = (TextView) findViewById(R.id.bt_title_right);
//		mList = new ArrayList<HashMap<String,Object>>();
		tv_pro = (TextView) findViewById(R.id.tv_pro);
		lin_1 = (RelativeLayout) findViewById(R.id.lin_1);
//		adapter = new WechatListAdapter(this, mList);
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		listView.setGroupIndicator(null);
//		cb_title_contre.setText(initdate(2));

//		if (mList.size() == 0) {
			// 加载数据
			loadMore();
//		    }
		cb_title_contre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showdate();
			}
		});
		
		listView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		
		listView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
					int arg3, long position) {
				// TODO Auto-generated method stub
				Intent its = new Intent();
				if(subMchId.equals("01")){
					
					 its.setClass(AlipayCollectionRecordActivity.this,BranchquartersActivity.class);
					String shopName = WechatListBeanlist.get(arg2).list.get(arg3).get("shopName").toString();
					String shopCodeMobile = WechatListBeanlist.get(arg2).list.get(arg3).get("mobile").toString();
//					String isSubbranch = WechatListBeanlist.get(arg2).list.get(arg3).get("isSubbranch").toString();
//					String isSubbranch = WechatListBeanlist.get(arg2).list.get(arg3).get("isSubbranch").toString();
					its.putExtra("shopCodeMobile", shopCodeMobile);
					its.putExtra("shopName", shopName);
					its.putExtra("flag", subMchId);
//					its.putExtra("isSubbranch", isSubbranch);
//					its.putExtra("isSubbranch", isSubbranch);
					its.putExtra("totalFee",  WechatListBeanlist.get(arg2).list.get(arg3).get("monthTotalFee").toString());
					its.putExtra("orderCount", WechatListBeanlist.get(arg2).list.get(arg3).get("monthTotalCount").toString());
					if(WechatListBeanlist.get(arg2).list.get(arg3).get("payDate")!=null){
						its.putExtra("payDate",  WechatListBeanlist.get(arg2).list.get(arg3).get("payDate").toString());

						}

				}else{
					its.setClass(AlipayCollectionRecordActivity.this,BranchquartersDayActivity.class);
					its.putExtra("shopCodeMobile",mobile);
					its.putExtra("payDate",  WechatListBeanlist.get(arg2).list.get(arg3).get("payDay").toString().replaceAll("-", ""));
					its.putExtra("flag", subMchId);
				}
				
				startActivity(its);
				return false;
			}
		});

		
	}

	private void loadMore() {
		
		new Thread(run).start();
		
		
	}
	
	Runnable run = new Runnable() {

		@Override
		public void run() {
			Log.e("", "setdate  == "+setdate);
			Log.e("", "frontdate  == "+frontdate);
			try {
				String[] values = {mobile,setdate,frontdate};
				if(subMchId.equals("01")){
					ArrayList<HashMap<String, Object>> List = NetCommunicate.executeHttpPostgetjsonlist(HttpUrls.ALIPAYLIST,
							HttpKeys.ALIPAYLIST_BACK,HttpKeys.ALIPAYLIST_ASK,values,0);
					mList.addAll(List);
				}else{
					List<WechatListBean>  list
					 = NetCommunicate.executeHttpPostgetjson(HttpUrls.ALIPAYBRANCHLIST,
							 HttpKeys.ALIPAYBRANCHLIST_BACK,HttpKeys.ALIPAYBRANCHLIST_ASK,values,1);
					WechatListBeanlist.addAll(list);
				}

				
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
			if(subMchId.equals("01")){
				if (mList.size() <= 0 || mList == null) {
					msg.what = 2;
				} else {
					msg.what = 1;
				}
			}else{
				if (WechatListBeanlist.size() <= 0 || WechatListBeanlist == null) {
					msg.what = 2;
				} else {
					msg.what = 1;
				}
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
				
				tv_pro.setVisibility(View.GONE);
				if(subMchId.equals("01")){
					if(stat==0){
						WechatListBean bean = new WechatListBean();
						
						WechatListBean bean1 = new WechatListBean();
						
						double balace  = 0;
						int num = 0;
						double balace1  = 0;
						int num1 = 0;
						
						for (int i = 0; i < mList.size()/2; i++) {
							balace+=Double.parseDouble(mList.get(i).get("monthTotalFee").toString());
							num +=Integer.parseInt(mList.get(i).get("monthTotalCount").toString());
							mList.get(i).put("payDate", setdate);
							bean.list.add(mList.get(i));
						}
						for (int j = mList.size()/2; j <mList.size(); j++) {
							balace1+=Double.parseDouble(mList.get(j).get("monthTotalFee").toString());
							num1 +=Integer.parseInt(mList.get(j).get("monthTotalCount").toString());
							mList.get(j).put("payDate", frontdate);
							bean1.list.add(mList.get(j));
						}
						
						bean.setTotalFee(balace+"");
						bean.setOrderCount(String.valueOf(num));
						bean.setPayDate(setdate);
						WechatListBeanlist.add(bean);
						bean1.setTotalFee(balace1+"");
						bean1.setOrderCount(String.valueOf(num1));
						bean1.setPayDate(frontdate);
						WechatListBeanlist.add(bean1);
					}else{
						WechatListBean bean = new WechatListBean();
						
						double balace  = 0;
						int num = 0;
						
						for (int i = 0; i < mList.size(); i++) {
							balace+=Double.parseDouble(mList.get(i).get("monthTotalFee").toString());
							num +=Integer.parseInt(mList.get(i).get("monthTotalCount").toString());
							mList.get(i).put("payDate", setdate);
							bean.list.add(mList.get(i));
						}
						
						bean.setTotalFee(String.valueOf(balace));
						bean.setOrderCount(String.valueOf(num));
						bean.setPayDate(setdate);
						WechatListBeanlist.add(bean);
					}
					
				}else{
					for (int i = 0; i < WechatListBeanlist.size(); i++) {
						if(i==0){
							WechatListBeanlist.get(i).setPayDate(setdate);
						}else{
							WechatListBeanlist.get(i).setPayDate(frontdate);
						}
					
					}
					
				}
				
				adapter.notifyDataSetChanged();
				
				
			     int groupCount = listView.getCount();

			     for (int i=0; i<groupCount; i++) {
			    	 listView.expandGroup(i);
			         };
			         
			         
			         new Thread(run1).start();
			         
				break;
			case 2:
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
        	Log.e("", "asdas    "+childPosition);
//        	Log.e("", "     asda   "+(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("totalFee").toString()));
        	 String info = "";
        	if(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("monthTotalFee")!=null){
        		info = WechatListBeanlist.get(groupPosition).list.get(childPosition).get("monthTotalFee").toString();
        	}
        	if(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("dayTotalFee")!=null){
        		info = WechatListBeanlist.get(groupPosition).list.get(childPosition).get("dayTotalFee").toString();
        		
        	}
//            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) AlipayCollectionRecordActivity.this
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
            ImageView imgs = (ImageView) convertView
                    .findViewById(R.id.imgs);
            
            imgs.setImageResource(R.drawable.zhi);

          if(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("monthTotalCount")!=null){
        	  tv_name.setText("共"+WechatListBeanlist.get(groupPosition).list.get(childPosition).get("monthTotalCount").toString()+"笔");
          }
          if(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("dayTotalCount")!=null){
        	  tv_name.setText("共"+WechatListBeanlist.get(groupPosition).list.get(childPosition).get("dayTotalCount").toString()+"笔");
          }
           

            tv.setText("+"+Double.parseDouble(info)/100);
    		if(subMchId!=null&&subMchId.equals("01")){
    	          if(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("shopName")!=null){
    	        	  tv_type.setText(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("shopName").toString());
    	          }
    		}else{
                if(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("payDay")!=null){
                	 tv_time.setText(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("payDay").toString());
                	String aa = WechatListBeanlist.get(groupPosition).list.get(childPosition).get("payDay").toString();
//                	tv_time.setText(WechatListBeanlist.get(groupPosition).list.get(childPosition).get("payDate").toString());
//                	  tv_money.setText(getWeek(aa+"00000000"));
                    if(aa!=null&&aa.length()>=1){
                    	aa = aa.replace("-", "");
                    	tv_type.setText(getWeek(aa+"00000000"));
                    }
                }

    		}
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
                LayoutInflater inflater = (LayoutInflater) AlipayCollectionRecordActivity.this
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
            	   tv.setText(WechatListBeanlist.get(groupPosition).getPayDate());
            	   aa = WechatListBeanlist.get(groupPosition).getPayDate();
            	   if(initData().equals(aa.substring(aa.length()-2))){
            			if(subMchId!=null&&subMchId.equals("01")){
            				
            				 tv.setText("本月当前总店总金额");
            			}else{
            				 tv.setText("本月当前分店店总金额");
            			}
            		  
            	   }else{
					if (subMchId!=null&&subMchId.equals("01")) {
						 tv.setText(aa.substring(aa.length()-2)+"月总店总金额");
					} else {
						 tv.setText(aa.substring(aa.length()-2)+"月分店总金额");
					}
            		  
            	   }
            	   
            	   
            }
            if(WechatListBeanlist.get(groupPosition).getTotalFee()!=null){
            	tv_week.setText(Double.parseDouble(WechatListBeanlist.get(groupPosition).getTotalFee())/100+"元");//"￥"
            }
            
            
//            if(WechatListBeanlist.get(groupPosition).getOrderCount()!=null){
//            	tv_num.setText("共"+WechatListBeanlist.get(groupPosition).getOrderCount()+"笔");
//            }
            
            
            if(WechatListBeanlist.get(groupPosition).getOrderCount()!=null){
            	tv_num.setText("共"+WechatListBeanlist.get(groupPosition).getOrderCount()+"笔");
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

		ScreenInfo screenInfo = new ScreenInfo(AlipayCollectionRecordActivity.this);
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
					stat = 1;
					setdate = a+b;
					frontdate = "";
					WechatListBeanlist.clear();
					mList.clear();
					adapter.notifyDataSetChanged();
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
	
	Runnable run1 = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = null;
			
			try {
				
				String[] values = {mobile};
				result = NetCommunicate.executeHttpPostnulls(HttpUrls.SHOPSUM,
						HttpKeys.SHOPSUM_BACK,HttpKeys.SHOPSUM_ASK,values);
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
			if(result!=null){
					msg.what = 1;
			}else{
				msg.what = 5;
			}
			handler1.sendMessage(msg);
			
		}
	};
	
	private Handler handler1 = new Handler() {
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case 1:
			if(result.get("totalPayFee")!=null){
				tv3.setText(Double.parseDouble(result.get("totalPayFee").toString())/100+"");
				if(Double.parseDouble(result.get("totalPayFee").toString())/100>0){
					re_title.setBackgroundColor(getResources().getColor(R.color.red));
					re_title1.setBackgroundColor(getResources().getColor(R.color.red));
				}
			}
			if(result.get("withDrawFee")!=null){
				tv4.setText(Double.parseDouble(result.get("withDrawFee").toString())/100+"");
			}
			break;
		case 2:
			break;
		case 5:
			break;
		default:
			break;
		}
	};
};

}
