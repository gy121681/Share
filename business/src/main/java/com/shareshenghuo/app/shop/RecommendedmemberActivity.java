package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.widget.MyOnclicListener;
import com.example.widget.SelectBirthday;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.adapter.RecommendedMemberAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.Recommendedmember;
import com.shareshenghuo.app.shop.network.request.RecommendedmemberRequest;
import com.shareshenghuo.app.shop.network.response.RecommendedmemberResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.DateUtil;

public class RecommendedmemberActivity extends BaseTopActivity  implements OnClickListener, OnRefreshListener2<ListView>{
	
	private int pageNo = 1;
	private int pageSize = 10;
	private RecommendedMemberAdapter adapter;
	private PullToRefreshListView lvData;
	private View title;
	private TextView tv,tv1;
	private SelectBirthday birth;
	private String startdate;
	private String enddate,sdate = "",edate = "";
	private String condition = "";
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.recommendedmember_layout);
		initview();
		startdate = getthismoth();
		enddate = DateUtil.Today();
		loadData();
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("推荐记录");
		lvData = getView(R.id.lvShop);
		title = findViewById(R.id.title);
		lvData.setMode(Mode.BOTH);
		lvData.setOnRefreshListener(this);
		btnTopRight2.setVisibility(View.VISIBLE);
		btnTopRight2.setBackgroundResource(R.drawable.share_b_public__icon_search1);
		btnTopRight2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				showPopupWindow(title);
				
			}
		});
		
	}
	
	
	 private void showPopupWindow(View view) {

	        // 一个自定义的布局，作为显示的内容
	        View contentView = LayoutInflater.from(this).inflate(
	                R.layout.remember_pop, null);
	        // 设置按钮的点击事件
	        Button button = (Button) contentView.findViewById(R.id.btnOK);
	        Button button1 = (Button) contentView.findViewById(R.id.btnNo);
	        final EditText edquery = (EditText) contentView.findViewById(R.id.edquery);
	        tv = (TextView) contentView.findViewById(R.id.tvDateStart);
//	        tv.setText(startdate); 
	        tv1 = (TextView) contentView.findViewById(R.id.tvDateEnd);
//	        tv1.setText(enddate);
	        
	        tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					setdate(tv,startdate);
				}
			});
	        tv1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					setdate(tv1,enddate);
				}
			});

	        final PopupWindow popupWindow = new PopupWindow(contentView,
	                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
	        popupWindow.setTouchable(true);
	        
	        popupWindow.setTouchInterceptor(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
	        });
	        ColorDrawable dw = new ColorDrawable(0xb0000000);  
	        //设置SelectPicPopupWindow弹出窗体的背景  
	        popupWindow.setBackgroundDrawable(dw);  
	        
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			lp.alpha = 0.7f;
			getWindow().setAttributes(lp);
	        // 设置好参数之后再show
	        popupWindow.showAsDropDown(view);
	        
	        button.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	            	popupWindow.dismiss();
	            	condition = edquery.getText().toString();
	            	onPullDownToRefresh(lvData);
	            }
	        });
	        button1.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	            	popupWindow.dismiss();
	            }
	        });
	        
	        popupWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					WindowManager.LayoutParams lp = getWindow().getAttributes();
					lp.alpha = 1f;
					getWindow().setAttributes(lp);
				}
			});
	    }
	 

	
	public void loadData() {

		
		if(!TextUtils.isEmpty(sdate)&&TextUtils.isEmpty(edate)){
			Toast.makeText(RecommendedmemberActivity.this, "选择结束时间", Toast.LENGTH_SHORT).show();
			sdate = "";
			edate = "";
			return;
		}
		if(TextUtils.isEmpty(sdate)&&!TextUtils.isEmpty(edate)){
			Toast.makeText(RecommendedmemberActivity.this, "选择开始时间", Toast.LENGTH_SHORT).show();
			sdate = "";
			edate = "";
			return;
		}
		
		RecommendedmemberRequest req = new RecommendedmemberRequest();
		req.userId = UserInfoManager.getUserInfo(this).shop_id;
		req.userType = "2";
		req.qrytype = "";//shopPay
		req.searchName= condition;
		req.startDate = sdate;
		req.endDate = edate;
		req.pageNo = pageNo+"";
		req.pageSize = pageSize+"";
		
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.NEWMEMBERELIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
			}
			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				RecommendedmemberResponse bean = new Gson().fromJson(resp.result, RecommendedmemberResponse.class);
				sdate = "";
				edate = "";
				if(Api.SUCCEED == bean.result_code){
					updateView(bean.data);

				}else{
					
				}
			}
		});
	}
	
	public void updateView(List<Recommendedmember> data) {
//		if(data!=null){
			if(pageNo==1 || adapter==null) {
				adapter = new RecommendedMemberAdapter(this, data);
				lvData.setAdapter(adapter);
			}
			if(pageNo > 1) {
				adapter.getmData().addAll(data);
				adapter.notifyDataSetChanged();
			}
			pageNo++;
//		}

	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		pageNo = 1;
		
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		loadData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.tv:
//			
//			break;
//			
//		case R.id.tv1:
//			setdate(tv1);
//			break;
//
//		default:
//			break;
//		}
		
	}
	
	public void setdate(final TextView tv2, String startdate2){
		
		birth = new SelectBirthday(this,startdate2,new MyOnclicListener() {

			public void MyOnclicListener(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.submit:

					String initstartdate = birth.getdata();
					if(tv2==tv){
						startdate = initstartdate;
						sdate = initstartdate;
					}else {
						enddate = initstartdate;
						edate = initstartdate;
					}
					tv2.setText(initstartdate);
					
					birth.dismiss();
//					onPullDownToRefresh(lvData);
					break;
				case R.id.cancel:
					birth.dismiss();
					break;
				default:
					break;
				}
			}
		});
		birth.showAtLocation(this.findViewById(R.id.root),
				Gravity.BOTTOM, 0, 0);
	}
	
	private String getthismoth(){
		
		SimpleDateFormat dateFormater = new SimpleDateFormat(  
                "yyyy-MM-dd");  
        Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.DAY_OF_MONTH, 1);  
        cal.getTime();  
//        start_time.setText(dateFormater.format(cal.getTime()) + "");  

//        cal.set(Calendar.DAY_OF_MONTH,  
//                cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
//        end_time.setText(dateFormater.format(cal.getTime())); 
		return dateFormater.format(cal.getTime()) + "";
		
	}

}
