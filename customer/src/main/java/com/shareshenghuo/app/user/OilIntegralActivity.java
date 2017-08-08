//package com.shareshenghuo.app.user;
//
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.apache.http.entity.StringEntity;
//
//import com.shareshenghuo.app.user.adapter.OilIntegralAdapter;
//import UserInfoManager;
//import OilintegralBean;
//import OilintegralRequest;
//import com.shareshenghuo.app.user.network.response.OilintegralResponse;
//import Api;
//import com.shareshenghuo.app.user.util.ProgressDialogUtil;
//import com.shareshenghuo.app.user.widget.SpinerPopWindow;
//import com.example.widget.MyOnclicListener;
//import com.example.widget.SelectBirthday;
//import com.google.gson.Gson;
//import com.handmark.pulltorefresh.library.PullToRefreshBase;
//import com.handmark.pulltorefresh.library.PullToRefreshListView;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
//import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
//
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.support.v4.widget.DrawerLayout.LayoutParams;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.PopupWindow.OnDismissListener;
//
//public class OilIntegralActivity extends BaseTopActivity implements OnRefreshListener2<ListView>{
//	
//	private int pageNo = 1;
//	private int pageSize = 10;
//	private PullToRefreshListView lvData;
//	private OilIntegralAdapter adapter;
//	private RelativeLayout title;
//	private PopupWindow popupWindow;
//	private TextView tv,tv1,tv3;
//	private String condition = "";
//	private String enddate,sdate = "",edate = "",startdate;
//	private SelectBirthday birth;
//	private SpinerPopWindow mSpinerPopWindow;
//	private List<String> list;
//	private View parent;
//	
//	@Override
//	protected void onCreate(Bundle arg0) {
//		// TODO Auto-generated method stub
//		super.onCreate(arg0);
//		View parentView = getLayoutInflater().inflate(R.layout.oilintegral_activity, null);
//		setContentView(parentView);
//		initview();
//		loadData();
//		
//		showPopupWindow(true);
//
//		initsppop();
//	}
//
//	private void initsppop() {
//		// TODO Auto-generated method stub
//		list = new ArrayList<String>();
////		for (int i = 0; i < 20; i++) {
//			list.add("支付积分");
//			list.add("奖励积分");
//			list.add("代理积分");
//			list.add("销售积分");
////		}
//        mSpinerPopWindow = new SpinerPopWindow<String>(OilIntegralActivity.this, list,itemClickListener);  
//        mSpinerPopWindow.setOnDismissListener(new OnDismissListener() {
//			@Override
//			public void onDismiss() {
//				// TODO Auto-generated method stub
//				   setTextImage(tv3,R.drawable.ic_dropdown_gray);  
//			}
//		});
//        
//
//	}
//	
//    /** 
//     * popupwindow显示的ListView的item点击事件 
//     */  
//    private OnItemClickListener itemClickListener = new OnItemClickListener() {  
//        @Override  
//        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {  
//            mSpinerPopWindow.dismiss();  
//            tv3.setText(list.get(position));  
//        }  
//    };  
//
//	private void initview() {
//		// TODO Auto-generated method stub
//		initTopBar("积分流水");
//		lvData = getView(R.id.lvShop);
//		lvData.setMode(Mode.BOTH);
//		lvData.setOnRefreshListener(this);
//		title = (RelativeLayout) findViewById(R.id.title);
//		parent = findViewById(R.id.parent);
//		btnTopRight2.setVisibility(View.VISIBLE);
//		btnTopRight2.setBackgroundResource(R.drawable.btn_search);
//		btnTopRight2.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
////                mSpinerPopWindow.setWidth(title.getWidth());  
////                mSpinerPopWindow.showAsDropDown(parent, Gravity.BOTTOM, 0, 0);  
////                setTextImage(tv3,R.drawable.ic_droptop_gray);  
//				showpop(title);
//				  
//			}
//		});
//
//		
//		
////		List<OilintegralBean> data = new ArrayList<OilintegralBean>();
////		for (int i = 0; i < 10; i++) {
////			
////			OilintegralBean bean = new OilintegralBean(); 
////			data.add(bean);
////			
////		}
////		adapter = new OilIntegralAdapter(OilIntegralActivity.this, data);
////		lvData.setAdapter(adapter);
//		
//	}
//	
//	private void showpop(View title) {
//		// TODO Auto-generated method stub
//		WindowManager.LayoutParams lp = getWindow().getAttributes();
//		lp.alpha = 0.7f;
//		getWindow().setAttributes(lp);
//        // 设置好参数之后再show
//        popupWindow.showAsDropDown(title);
//	}
//	
//	public void loadData() {
//		ProgressDialogUtil.showProgressDlg(this, "");
//		OilintegralRequest req = new OilintegralRequest();
//		req.userId = UserInfoManager.getUserInfo(this).id+"";
//		req.userType = "1";
//		req.pageNo = pageNo;
//		req.pageSize = pageSize;
//		RequestParams params = new RequestParams();
//		try {
//			params.setBodyEntity(new StringEntity(req.toJson()));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		new HttpUtils().send(HttpMethod.POST, Api.OILINTEGRALLIST, params, new RequestCallBack<String>() {
//			@Override
//			public void onFailure(HttpException arg0, String arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				lvData.onRefreshComplete();
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> resp) {
//				ProgressDialogUtil.dismissProgressDlg();
//				lvData.onRefreshComplete();
//				OilintegralResponse bean = new Gson().fromJson(resp.result, OilintegralResponse.class);
//				Log.e("", ""+resp.result);
//				if(Api.SUCCEED == bean.result_code)
//					updateView(bean.data);
//			}
//		});
//		
//		
//	}
//    
//	public void updateView(List<OilintegralBean> data) {
//		if(pageNo==1 || adapter==null) {
//			adapter = new OilIntegralAdapter(OilIntegralActivity.this, data);
//			lvData.setAdapter(adapter);
//		}
//		if(pageNo > 1) {
//			adapter.getmData().addAll(data);
//			adapter.notifyDataSetChanged();
//		}
//		pageNo++;
//	}
//
//	@Override
//	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//		// TODO Auto-generated method stub
//			pageNo = 1;
//			loadData();
//	}
//
//	@Override
//	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//		// TODO Auto-generated method stub
//		loadData();
//	}
//	
//	 private void showPopupWindow(boolean b) {
////	        mSpinerPopWindow.showAtLocation(parent,
////	        		Gravity.BOTTOM, 0, 0);
//	        // 一个自定义的布局，作为显示的内容
//	        View contentView = LayoutInflater.from(this).inflate(
//	                R.layout.remember_pop1, null);
//	        // 设置按钮的点击事件
//	        Button button = (Button) contentView.findViewById(R.id.btnOK);
//	        Button button1 = (Button) contentView.findViewById(R.id.btnNo);
//	        final EditText edquery = (EditText) contentView.findViewById(R.id.edquery);
//	        if(b){
//	        	edquery.setVisibility(View.GONE);
//	        	contentView.findViewById(R.id.tv_type).setVisibility(View.VISIBLE);
//	        }
//	        tv3 =  (TextView) contentView.findViewById(R.id.tv_type);
//	        tv3.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
////					// TODO Auto-generated method stub
//	                mSpinerPopWindow.setWidth(title.getWidth());  
//	                mSpinerPopWindow.setHeight(500);  
////	                mSpinerPopWindow.showAsDropDown(parent, 0, 0,Gravity.BOTTOM);
//	                mSpinerPopWindow.showAtLocation(parent,
//	                		Gravity.BOTTOM, 0, 0);
////	                mSpinerPopWindow.showAsDropDown(title, Gravity.BOTTOM, 0, 0);  
//	                setTextImage(tv3,R.drawable.ic_droptop_gray);  
//				}
//			});
//	          tv = (TextView) contentView.findViewById(R.id.tvDateStart);
////	        tv.setText(startdate); 
//	          tv1 = (TextView) contentView.findViewById(R.id.tvDateEnd);
////	        tv1.setText(enddate);
//	        
//	        tv.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					setdate(tv,startdate);
//				}
//			});
//	        tv1.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					setdate(tv1,enddate);
//				}
//			});
//
//	         popupWindow = new PopupWindow(contentView,
//	                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
//	        popupWindow.setTouchable(true);
//	        
//	        popupWindow.setTouchInterceptor(new OnTouchListener() {
//
//				@Override
//				public boolean onTouch(View arg0, MotionEvent arg1) {
//					// TODO Auto-generated method stub
//					return false;
//				}
//	        });
//	        
//	        ColorDrawable dw = new ColorDrawable(0xb0000000);  
//	        //设置SelectPicPopupWindow弹出窗体的背景  
//	        popupWindow.setBackgroundDrawable(dw);  
//
//	        
//	        button.setOnClickListener(new OnClickListener() {
//
//	            @Override
//	            public void onClick(View v) {
//	            	popupWindow.dismiss();
////	            	condition = edquery.getText().toString();
//	            	onPullDownToRefresh(lvData);
//	            }
//	        });
//	        button1.setOnClickListener(new OnClickListener() {
//
//	            @Override
//	            public void onClick(View v) {
//	            	popupWindow.dismiss();
//	            }
//	        });
//	        
//	        popupWindow.setOnDismissListener(new OnDismissListener() {
//
//				@Override
//				public void onDismiss() {
//					WindowManager.LayoutParams lp = getWindow().getAttributes();
//					lp.alpha = 1f;
//					getWindow().setAttributes(lp);
//				}
//			});
//	    }
//	 
//
//		public void setdate(final TextView tv2, String startdate2){
//			
//			birth = new SelectBirthday(this,startdate2,new MyOnclicListener() {
//
//				public void MyOnclicListener(View v) {
//					// TODO Auto-generated method stub
//					switch (v.getId()) {
//					case R.id.submit:
//
//						String initstartdate = birth.getdata();
//						if(tv2==tv){
//							startdate = initstartdate;
//							sdate = initstartdate;
//						}else {
//							enddate = initstartdate;
//							edate = initstartdate;
//						}
//						tv2.setText(initstartdate);
//						
//						birth.dismiss();
////						onPullDownToRefresh(lvData);
//						break;
//					case R.id.cancel:
//						birth.dismiss();
//						break;
//					default:
//						break;
//					}
//				}
//			});
//			birth.showAtLocation(this.findViewById(R.id.parent),
//					Gravity.BOTTOM, 0, 0);
//		}
//		
//		private void setTextImage(TextView tv,int id ) {
//			// TODO Auto-generated method stub
//			Drawable drawable_n = getResources().getDrawable(id);
//			drawable_n.setBounds(0, 0, drawable_n.getMinimumWidth(),drawable_n.getMinimumHeight());  //此为必须写的
//			tv.setCompoundDrawables(null, null, drawable_n, null);
//		}
//}
