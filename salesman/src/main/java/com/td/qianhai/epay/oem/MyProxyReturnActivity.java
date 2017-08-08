package com.td.qianhai.epay.oem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONException;
import org.json.JSONObject;

import com.td.qianhai.epay.oem.adapter.MyProxyAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.MyCircleBean;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


public class MyProxyReturnActivity extends BaseActivity implements
		OnScrollListener{
	
	private int page = 1; // 页数
	private int allPageNum = 0; // 总页数
	private int PAGE_SIZE = 5;
	private ArrayList<HashMap<String, Object>> mList;
	private ListView listView;
	private String mobile;
	private View moreView;
	private View view;  
	private View emptyView;
	private boolean isThreadRun = false; // 加载数据线程运行状态
	private TextView null_data,query_tv;
	private MyCircleBean entitys;
	private MyProxyAdapter adapter;
	private LayoutInflater inflater;
	private String querypbone = "";
	private String querypname = "";
	private EditText query_ed;
	private PopupWindow pop;  
	 private TextView pro_pop_1,pro_pop_2,pro_pop_3,pro_pop,bt_title_right1,pro_pop_4,pro_pop_5,pro_pop_6,pro_pop_7,pro_pop_8,pro_pop_9,pro_pop_10;
	 private Drawable drawable,drawable1;
	 private CheckBox cb_title_contre;
	 private String agtid,shrtype = "",seltype = "";

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.activity_proxyreturn);
//		mobile = ((AppContext)getApplication()).getMobile();
		Intent it = getIntent();
		shrtype = it.getStringExtra("tag");
		seltype = it.getStringExtra("seltype");
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		agtid = MyCacheUtil.getshared(this).getString("Mobile", "");
		inintview();
		initPopupWindow();
		
	}
	
	private void inintview() {
		
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		

		
		inflater = LayoutInflater.from(this);
		mList = new ArrayList<HashMap<String, Object>>();
		null_data = (TextView) findViewById(R.id.null_data);
		bt_title_right1 = (TextView) findViewById(R.id.bt_title_right1);
		query_tv = (TextView) findViewById(R.id.query_tv);
		query_ed = (EditText) findViewById(R.id.query_ed);
		listView = (ListView) findViewById(R.id.mylist);
		cb_title_contre = (CheckBox) findViewById(R.id.cb_title_contre);
		cb_title_contre.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	            if (pop.isShowing()) {  
	                pop.dismiss();  
	                cb_title_contre.setChecked(false);
	                
	            } else {  
	            	 cb_title_contre.setChecked(true);
	                pop.showAsDropDown(v);  
	            }
			}
		});
		if(shrtype!=null){
			if(shrtype.equals("4")){
				cb_title_contre.setText("零售商收益");
			}
			if(shrtype.equals("5")){
				cb_title_contre.setText("分销商收益");
			}
			if(shrtype.equals("6")){
				cb_title_contre.setText("代理商收益");
				
			}
		}
		
		bt_title_right1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
	            if (pop.isShowing()) {  
	                pop.dismiss();  
	                cb_title_contre.setChecked(false);
	                
	            } else {  
	            	 cb_title_contre.setChecked(true);
	                pop.showAsDropDown(v);  
	            }
			}
		});
		
//		im_mycircle.setOnClickListener(new OnClickListener() {
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent it = new Intent(MyCircleActivity.this,PromotionEarningActivity.class);
//				startActivity(it);
//			}
//		});		
		
		moreView = inflater.inflate(R.layout.load, null);
		listView.addFooterView(moreView);
		moreView.setVisibility(View.GONE);
		listView.setOnScrollListener(this);
		

		
	if (mList.size() == 0) {
		emptyView = inflater.inflate(R.layout.progress_view, null);
		emptyView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		((ViewGroup) listView.getParent()).addView(emptyView);
		listView.setEmptyView(emptyView);
		// 加载数据
		loadMore();
	    }
	
		adapter = new MyProxyAdapter(this, mList, 0);
		listView.setAdapter(adapter);
		
		query_ed.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					query_tv.setEnabled(true);
				}else{
					query_tv.setEnabled(false);
					querypname = "";
					querypbone = "";
					page = 1;
					mList.clear();
					loadMore();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		query_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String s = query_ed.getText().toString();
				page = 1;
				mList.clear();
				if(gettext(s)){
					querypbone = "";
					querypname = s;
				}else if(!gettext(s)){
					querypbone = s;
					querypname = "";
				}
				loadMore();
				
			}
		});
	}
	
	
	
	private boolean gettext(String text){
		   Pattern p = Pattern.compile("[0-9]*");   
		     Matcher m = p.matcher(text);   
		     if(m.matches() ){  
		    	 return false;
		      }   
//		     p=Pattern.compile("[a-zA-Z]");  
//		     m=p.matcher(text);  
//		     if(m.matches()){  
//		    	 return false;
//		     }  
		     p=Pattern.compile("[\u4e00-\u9fa5]");  
		     m=p.matcher(text.substring(0,1));  
		     if(m.matches()){  
		    	 return true;
		     }
			return false;  
	}
	  

	
	
//	private void loadMore() {
//		if (page != 1 &&page > allPageNum) {
////			ToastCustom.showMessage(this, "没有更多记录了");
//			moreView.setVisibility(View.GONE);
//			return;
//		}
//		if (!isThreadRun) {
//			isThreadRun = true;
//			showLoadingDialog("正在查询中...");
//			new Thread(run).start();
//		}
//
//	}
	
	
//	private String setParameter(String PHONENUMBER,String PageNum,String NumPerPag,String MERPHONE,String MERCNAM){
//		
//		JSONObject jsonObj = new JSONObject();
//		try {
//			jsonObj.put("PHONENUMBER", PHONENUMBER);
//			jsonObj.put("PageNum", PageNum);
//			jsonObj.put("NumPerPag", NumPerPag);
//			jsonObj.put("MERPHONE", MERPHONE);
//			jsonObj.put("MERCNAM", MERCNAM);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String aa = jsonObj.toString();
//		
//		return aa;
//		
//		
//		
//	}
	
	private void loadMore() {
		if(!isThreadRun){
			isThreadRun = true;
			showLoadingDialog("正在查询中...");
			new Thread(run).start();
		}

	}

	Runnable run = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = null;
			try {
				
				String[] values = {mobile, page+"", PAGE_SIZE+"", querypbone, querypname,shrtype,seltype};
				list = NetCommunicate.executeHttpPostnull(HttpUrls.PROXYRETURN,
						HttpKeys.PROXYRETURN,HttpKeys.PROXYRETURNASK,values);
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

			if (list != null) {
				mList.addAll(list);
				if (mList.size() <= 0 || mList == null) {

					msg.what = 2;
				} else {
					msg.what = 1;
				}
				page ++;
			} else {
				Log.e("", "111111");
				msg.what = 3;
			}
			isThreadRun = false;
			loadingDialogWhole.dismiss();
			handler.sendMessage(msg);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				listView.setVisibility(View.VISIBLE);
				moreView.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
				break;
			case 2:
				null_data.setVisibility(View.VISIBLE);
				adapter.notifyDataSetChanged();
//				if (moreView != null) {
					emptyView.setVisibility(View.GONE);
					moreView.setVisibility(View.GONE);
//				}
				 Toast.makeText(getApplicationContext(),"加载完毕",
				 Toast.LENGTH_SHORT).show();
				break;
			case 3:
				emptyView.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(), "网络不给力,请检查网络设置",
						Toast.LENGTH_SHORT).show();
				listView.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		};
	};
	
	
	private void initPopupWindow() {  
        view = this.getLayoutInflater().inflate(R.layout.profite_popupwindow, null);  
        pro_pop = (TextView) view.findViewById(R.id.pro_pop);
        pro_pop_1 = (TextView) view.findViewById(R.id.pro_pop_1);
        pro_pop_2 = (TextView) view.findViewById(R.id.pro_pop_2);
        pro_pop_3 = (TextView) view.findViewById(R.id.pro_pop_3);
        pro_pop_4 = (TextView) view.findViewById(R.id.pro_pop_4);
        pro_pop_5= (TextView) view.findViewById(R.id.pro_pop_5);
        pro_pop_6 = (TextView) view.findViewById(R.id.pro_pop_6);
        pro_pop_7 = (TextView) view.findViewById(R.id.pro_pop_7);
        pro_pop_8 = (TextView) view.findViewById(R.id.pro_pop_8);
        pro_pop_9 = (TextView) view.findViewById(R.id.pro_pop_9);
        pro_pop_10 = (TextView) view.findViewById(R.id.pro_pop_10);
        
        if(seltype!=null&&seltype.equals("2")){
        	pro_pop_4.setVisibility(View.GONE);
        	pro_pop_5.setVisibility(View.GONE);
        	pro_pop_6.setVisibility(View.GONE);
        }else if(seltype!=null&&seltype.equals("")){

        }else{
        	cb_title_contre.setEnabled(false);
        	cb_title_contre.setButtonDrawable(null);
        	bt_title_right1.setVisibility(View.GONE);
        }
        pop = new PopupWindow(view, ViewGroup.LayoutParams.FILL_PARENT,  
                ViewGroup.LayoutParams.WRAP_CONTENT);  
        drawable = getResources().getDrawable(R.drawable.bluechecked);
        drawable1 = getResources().getDrawable(R.drawable.blueonchecked);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        drawable1.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        pop.setOutsideTouchable(true);  
        pop.setOutsideTouchable(true);
        pro_pop.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
                pro_pop.setCompoundDrawables(null, null, drawable, null);
                pro_pop_1.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_2.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_3.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_4.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_5.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_6.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_7.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_10.setCompoundDrawables(null, null, drawable1, null);
                cb_title_contre.setText("收益汇总");
                shrtype = "";
                page = 1;
                mList.clear();
                loadMore();
            }  
        });
        pro_pop_1.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
                pro_pop.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_1.setCompoundDrawables(null, null, drawable, null);
                pro_pop_2.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_3.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_4.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_5.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_6.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_7.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_8.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_9.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_10.setCompoundDrawables(null, null, drawable1, null);
                
                cb_title_contre.setText("信用卡还款收益");
                shrtype = "0";
                page = 1;
                mList.clear();
                loadMore();
            }  
        });  
        pro_pop_2.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
                pro_pop.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_1.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_2.setCompoundDrawables(null, null, drawable, null);
                pro_pop_3.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_4.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_5.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_6.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_7.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_8.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_9.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_10.setCompoundDrawables(null, null, drawable1, null);
                cb_title_contre.setText("闪提收益");
                shrtype = "1";
                page = 1;
                mList.clear();
                loadMore();
            }  
        }); 
        
        
        pro_pop_3.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
                pro_pop.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_1.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_2.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_3.setCompoundDrawables(null, null, drawable, null);
                pro_pop_4.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_5.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_6.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_7.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_8.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_9.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_10.setCompoundDrawables(null, null, drawable1, null);
                cb_title_contre.setText("提现收益");
                shrtype = "2";
                page = 1;
                mList.clear();
                loadMore();
            }  
        }); 
        
        pro_pop_4.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
                pro_pop.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_1.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_2.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_3.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_4.setCompoundDrawables(null, null, drawable, null);
                pro_pop_5.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_6.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_7.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_8.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_9.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_10.setCompoundDrawables(null, null, drawable1, null);
                cb_title_contre.setText("零售商收益");
                shrtype = "4";
                page = 1;
                mList.clear();
                loadMore();
            }  
        }); 
        pro_pop_5.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
                pro_pop.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_1.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_2.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_3.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_4.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_5.setCompoundDrawables(null, null, drawable, null);
                pro_pop_6.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_7.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_8.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_9.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_10.setCompoundDrawables(null, null, drawable1, null);
                cb_title_contre.setText("分销商收益");
                shrtype = "5";
                page = 1;
                mList.clear();
                loadMore();
            }  
        }); 
        pro_pop_6.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
                pro_pop.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_1.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_2.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_3.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_4.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_5.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_6.setCompoundDrawables(null, null, drawable, null);
                pro_pop_7.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_8.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_9.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_10.setCompoundDrawables(null, null, drawable1, null);
                cb_title_contre.setText("代理商收益");
                shrtype = "6";
                page = 1;
                mList.clear();
                loadMore();
            }  
        }); 
        pro_pop_7.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
                pro_pop.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_1.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_2.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_3.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_4.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_5.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_6.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_7.setCompoundDrawables(null, null, drawable, null);
                pro_pop_8.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_9.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_10.setCompoundDrawables(null, null, drawable1, null);
                cb_title_contre.setText("三级代理收益");
                shrtype = "3";
                page = 1;
                mList.clear();
                loadMore();
            }  
        }); 
        pro_pop_8.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
                pro_pop.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_1.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_2.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_3.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_4.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_5.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_6.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_7.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_8.setCompoundDrawables(null, null, drawable, null);
                pro_pop_9.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_10.setCompoundDrawables(null, null, drawable1, null);
                cb_title_contre.setText("区域收益");
                shrtype = "8";
                page = 1;
                mList.clear();
                loadMore();
            }  
        }); 
        pro_pop_9.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
                pro_pop.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_1.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_2.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_3.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_4.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_5.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_6.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_7.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_8.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_9.setCompoundDrawables(null, null, drawable, null);
                pro_pop_10.setCompoundDrawables(null, null, drawable1, null);
                cb_title_contre.setText("商家返佣");
                shrtype = "9";
                page = 1;
                mList.clear();
                loadMore();
            }  
        }); 
        pro_pop_10.setOnClickListener(new View.OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                pop.dismiss();  
                cb_title_contre.setChecked(false);
                pro_pop.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_1.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_2.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_3.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_4.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_5.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_6.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_7.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_8.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_9.setCompoundDrawables(null, null, drawable1, null);
                pro_pop_10.setCompoundDrawables(null, null, drawable, null);
                cb_title_contre.setText("商家返佣");
                shrtype = "10";
                page = 1;
                mList.clear();
                loadMore();
            }  
        }); 
    } 
	
	
	private int lastItem;// 当前显示的最后一项

	@Override
	public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int arg3) {
		// TODO Auto-generated method stub
		lastItem = firstVisibleItem + visibleItemCount - 1; // 减1是因为上面加了个addFooterView
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == SCROLL_STATE_IDLE) {
			if (lastItem == mList.size()) {
				moreView.setVisibility(View.VISIBLE);
				loadMore();
			}
		}
	}

}
