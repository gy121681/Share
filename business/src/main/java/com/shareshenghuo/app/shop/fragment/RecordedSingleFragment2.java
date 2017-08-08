package com.shareshenghuo.app.shop.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.OrderDispositionActivity;
import com.shareshenghuo.app.shop.OrderDispositionFmActivity;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.RecordedSingleActivity;
import com.shareshenghuo.app.shop.RehistoryListActivity;
import com.shareshenghuo.app.shop.RehistoryListFmActivity;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.ShopOrderDetailBean;
import com.shareshenghuo.app.shop.network.bean.UserInfobyAccBean;
import com.shareshenghuo.app.shop.network.request.NumRequest;
import com.shareshenghuo.app.shop.network.response.AutResponse;
import com.shareshenghuo.app.shop.network.response.ShopOrderDetailResponse;
import com.shareshenghuo.app.shop.network.response.UserInfobyAccResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.AnimationUtil;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;

public class RecordedSingleFragment2 extends BaseFragment implements OnClickListener{
	
	private TextView type,typename,tv1,tv2;
	public String[] typelist;
	public String union_id;
	public EditText ed_phone,ed_balance;
	private TwoButtonDialog downloadDialog;
	public String typenum;
	private ImageView img_arr;
	private int statusBarHeight1;
	private LinearLayout llayout;
	private Button btnTopRight1;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.recorded_single_activity;
	}

	@Override
	protected void init(View rootView) {
		// TODO Auto-generated method stub
		
		int resourceId = getResources().getIdentifier("status_bar_height","dimen", "android");
		if (resourceId > 0) {// 根据资源ID获取响应的尺寸值
			statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
		}else{
			statusBarHeight1 = 20;
		}
		initview();
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		getStatisticsData();
	}

	private void initview() {
		// TODO Auto-generated method stub
//		initTopBar("商家录单");
		getView(R.id.llTopBack).setVisibility(View.GONE);
		TextView title = getView(R.id.tvTopTitle);
		title.setText("录单");
		type = getView(R.id.type);
		typename = getView(R.id.typename);
		tv1 = getView(R.id.tv1);
		tv1.setOnClickListener(this);
		tv2 = getView(R.id.tv2);
		img_arr = getView(R.id.img_arr);
		ed_balance = getView(R.id.ed_balance);
		llayout = getView(R.id.llayout);
		ed_phone = getView(R.id.ed_phone);
		getView(R.id.llWalletRecharge).setOnClickListener(this);
		tv2.setOnClickListener(this);
		btnTopRight1 = getView(R.id.btnTopRight1);
		btnTopRight1.setVisibility(View.VISIBLE);
		btnTopRight1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent it  = new Intent(activity,OrderDispositionFmActivity.class);
				it.putExtra("tag", "1");
				startActivity(it);
			}
		});
		
		
		type.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showPopupWindow(type);
			}
		});
		ed_phone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()>=11){
					getuserinfobyaccounts(ed_phone.getText().toString());
				}else{
					setText(R.id.username, "");
					setText(R.id.nickname, "");
					union_id = "";
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
		
		ed_balance.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(arg0.length()>0){
					if(!TextUtils.isEmpty(ed_balance.getText())&&!TextUtils.isEmpty(typenum)){
						try {
							String result = String.format("%.2f", Double.parseDouble(ed_balance.getText().toString())*Double.parseDouble(typenum));
							setText(R.id.rebalance, result+"元");
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
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
	}
	
	public void getStatisticsData() {
		NumRequest req = new NumRequest();
		req.shop_id = UserInfoManager.getUserInfo(activity).shop_id+"";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETSHOPORDERDETAILS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				Log.e("", " - - -- - - "+resp.result);
				ShopOrderDetailResponse bean = new Gson().fromJson(resp.result, ShopOrderDetailResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					init(bean.data);
				}
			}
		});
	}
	
	private void init(ShopOrderDetailBean data) {
		if(typelist!=null){
			typelist = null;
		}
		setText(R.id.shopname, data.shop_name);
		setText(R.id.shopaddr, data.shop_address);
		setText(R.id.person_name, data.legal_person_name);
		if(data.discount_types!=null){
			typelist = data.discount_types.split(",");
			btnTopRight1.setText("订单("+data.shop_order_num+") >");
			
			
		}else{
			btnTopRight1.setText("订单(0) >");
		}
		
		
		
	}
	
	public void getuserinfobyaccounts(String mobilse) {
		NumRequest req = new NumRequest();
		req.account = mobilse;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.GETUSERINFOBYACCOUNTS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				UserInfobyAccResponse bean = new Gson().fromJson(resp.result, UserInfobyAccResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					inituserinfo(bean.data);
				}
			}
		});
	}
	
	private void inituserinfo(UserInfobyAccBean data) {
		// TODO Auto-generated method stub
		if(!TextUtils.isEmpty(data.real_name)){
			setText(R.id.username, data.real_name);
		}else{
			setText(R.id.username, "未实名认证");
		}
		if(!TextUtils.isEmpty(data.nick_name)){
			setText(R.id.nickname, data.nick_name);
		}else{
			setText(R.id.nickname, "");
		}
		
		union_id = data.union_id;
	}
	
	
	
	 private void showPopupWindow(View view) {
		 	img_arr.setImageResource(R.drawable.ic_droptop_gray);
		 	if(typelist==null||typelist.length<=0){
		 		return;
		 	}
		 
			final List<String> list = new ArrayList<String>();
			for (int i = 0; i < typelist.length; i++) {
				String item  = 10-Double.parseDouble(typelist[i])*10+"折(让利"+(int)(Double.parseDouble(typelist[i])*100)+"%)";
				list.add(item);
			}
			
	        // 一个自定义的布局，作为显示的内容
	        View contentView = LayoutInflater.from(activity).inflate(
	                R.layout.currency_pop, null);
	        // 设置按钮的点击事件
	        
			ListView listview = (ListView) contentView.findViewById(R.id.poplist);
			ArrayAdapter popadapter = new ArrayAdapter<String>(activity, R.layout.pop_name,list);
			listview.setAdapter(popadapter);
			

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
	        
	        ColorDrawable dw = new ColorDrawable(00000000);  
	        //设置SelectPicPopupWindow弹出窗体的背景  
	        popupWindow.setBackgroundDrawable(dw);  
//			WindowManager.LayoutParams lp = getWindow().getAttributes();
//			lp.alpha = 0.7f;
//			getWindow().setAttributes(lp);
	        // 设置好参数之后再show
//	        popupWindow.showAtLocation(view,Gravity.NO_GRAVITY,0,0);
	        
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				 popupWindow.showAsDropDown(view,Gravity.NO_GRAVITY,0,0);
			}else{
				 popupWindow.showAtLocation(view,Gravity.BOTTOM,0,0);
			}
	       
//	        Log.e("", "view.getBottom = =  "+view.getBottom());
//	        popupWindow.showAtLocation(view,Gravity.NO_GRAVITY,0,view.getBottom()+statusBarHeight1);
	        
	        listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					type.setText(list.get((int)arg3));
					typenum = typelist[(int)arg3];
					
					if(!TextUtils.isEmpty(ed_balance.getText())){
						String result = String.format("%.2f", Double.parseDouble(ed_balance.getText().toString())*Double.parseDouble(typenum));
						setText(R.id.rebalance, result+"元");
					}
					
					img_arr.setImageResource(R.drawable.ic_dropdown_gray);
					popupWindow.dismiss();
				}
			});
	        
	        popupWindow.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					// TODO Auto-generated method stub
					img_arr.setImageResource(R.drawable.ic_dropdown_gray);
				}
			});
	    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv1:
//			Intent it  = new Intent(activity,RehistoryListFmActivity.class);
//			it.putExtra("tag", "1");
//			startActivity(it);
//			startActivity(new Intent(activity,RehistoryListActivity.class));
			break;
		case R.id.tv2:
			Intent it1  = new Intent(activity,RehistoryListFmActivity.class);
			it1.putExtra("tag", "1");
			startActivity(it1);
//			startActivity(new Intent(activity,RehistoryListActivity.class));
			break;
		case R.id.llWalletRecharge:
			Submit();
			break;
			
		default:
			break;
		}
		
	}

	private void Submit() {
		// TODO Auto-generated method stub
		
//		if(TextUtils.isEmpty(union_id)){
//			initDialog("抱歉，当前消费者帐号暂不可进行录单", "确定", "");
//			return;
//		}
		
		if(TextUtils.isEmpty(ed_phone.getText())){
			T.showShort(activity, "请填写消费者手机号");
			return;
		}
		
		if(TextUtils.isEmpty(ed_balance.getText())){
			T.showShort(activity, "请输入金额");
			return;
		}
		
		if(Double.parseDouble(ed_balance.getText().toString())<1){
			T.showShort(activity, "金额不能小于一元");
			return;
		}
		
		if(TextUtils.isEmpty(typenum)){
			T.showShort(activity, "请选择让利率");
			return;
		}
		
		addshoppayclsinfs();
		
		
	}
	
	
	public void addshoppayclsinfs() {
		ProgressDialogUtil.showProgressDlg(activity, "");
		NumRequest req = new NumRequest();
		req.shop_id = UserInfoManager.getUserInfo(activity).shop_id+"";
		req.account = ed_phone.getText().toString();
		req.total_fee = (int)(Float.parseFloat(ed_balance.getText().toString())*100)+"";
		req.fee_rate =	typenum;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.ADDSHOPPAYCLSINFS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(activity);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					if(bean.data.RSPCOD.equals("000000")){
						Refresh();
						initDialog(bean.data.RSPMSG, "确定", "");
					}else{
						initDialog(bean.data.RSPMSG, "确定", "");
					}
				}
			}
		});
	}
	private void Refresh() {
		// TODO Auto-generated method stub
		AnimationUtil.BtnSpecialAnmations1(activity, btnTopRight1, 500,2000);
		typenum = "";
		setText(R.id.ed_balance, "");
		setText(R.id.type, "");
		setText(R.id.rebalance, "");
		setText(R.id.ed_phone, "");
		setText(R.id.username, "");
		setText(R.id.nickname, "");
		getStatisticsData();
		
	}
	
	private void initDialog(String content,String left,String right) {
	// TODO Auto-generated method stub
	downloadDialog = new TwoButtonDialog(activity, R.style.CustomDialog,
			"", content, left, right,true,new OnMyDialogClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					switch (v.getId()) {
					case R.id.Button_OK:
						downloadDialog.dismiss();
						break;
					case R.id.Button_cancel:
						downloadDialog.dismiss();
					default:
						break;
					}
				}
			});
		downloadDialog.show();
	}
}
