package com.shareshenghuo.app.shop;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.adapter.ExcitationGridAdapter;
import com.shareshenghuo.app.shop.adapter.PayListAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.ExcitationTypeBean;
import com.shareshenghuo.app.shop.network.bean.PayChannels;
import com.shareshenghuo.app.shop.network.bean.PayChannelsBean;
import com.shareshenghuo.app.shop.network.request.PayChannelsResquest;
import com.shareshenghuo.app.shop.network.response.ExcitationgridRespnose;
import com.shareshenghuo.app.shop.network.response.PayChannelsResponse;
import com.shareshenghuo.app.shop.network.response.QrUrlResultResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;

public class EkOrderPay extends  BaseTopActivity implements OnClickListener {
	
	private Button btnCancel,saom_pay,bankc_pay,apliy;
	
	private EditText edtNumber;
	
	private LayoutInflater inflater;
	
	private ListView listview;
	
	private int haight = 0;
	
	private   PopupWindow popupWindow;
	
	private  PayChannels  gRP;
	
	private ExcitationGridAdapter excitationgridadapter;
	
	private List<ExcitationTypeBean> datas;
	
	private PayListAdapter apdater;
	
	private   GridView gridview;
	
	private TextView tv_ctag;
	
	private String pointsid = "",custermtel = "",paytype = "";
	
	private String balances;
	
	private String TERMNO = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = (LayoutInflater)this.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		setContentView(R.layout.activity_ekpay);
		initview();
	
	}

	private void init() {
		// TODO Auto-generated method stub
		
		PayChannelsResquest req = new PayChannelsResquest();
		req.ACCOUNT = UserInfoManager.getUserInfo(this).shop_id+"";
		req.TERMTYPE = "1";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.PAYCHANNELS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(EkOrderPay.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				PayChannelsResponse bean = new Gson().fromJson(resp.result, PayChannelsResponse.class);
				if(Api.SUCCEED == bean.result_code) {
					if(bean.data.RSPCOD.equals("000000")){
						if(bean.data.TERMNO!=null){
							TERMNO = bean.data.TERMNO;
						}
						initpaylist(bean.data.GRP);
						
						initpopwind(true,bean.data.CTAG);
					}else{
						T.showShort(getApplicationContext(), bean.data.RSPMSG);
					}
				}else{
					T.showShort(getApplicationContext(), "请求失败");
				}
			}
		});
	}
	
	private void inittype() {
		// TODO Auto-generated method stub
		PayChannelsResquest req = new PayChannelsResquest();
		req.ACCOUNT = UserInfoManager.getUserInfo(this).shop_id+"";
		req.TERMTYPE = "1";
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.POINTSLST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(EkOrderPay.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				ExcitationgridRespnose bean = new Gson().fromJson(resp.result, ExcitationgridRespnose.class);
				if(Api.SUCCEED == bean.result_code) {
					if(bean.data.RSPCOD.equals("000000")){
						if(datas!=null&&datas.size()>0){
							datas.clear();
						}
						datas.addAll(bean.data.GRP);
						excitationgridadapter.notifyDataSetChanged();
						
						for (int i = 0; i < bean.data.GRP.size(); i++) {
							if(bean.data.GRP.get(i).ISDEFAULT.equals("0")){
								pointsid = bean.data.GRP.get(i).POINTSID;
							}
						}
						
					}else{
						T.showShort(getApplicationContext(), bean.data.RSPMSG);
					}
				}else{
					T.showShort(getApplicationContext(), "请求失败");
				}
			}
		});
	}
	
	
	
	

	private void initpaylist(List<PayChannelsBean> gRP) {
		// TODO Auto-generated method stub
		this.gRP.GRP.addAll(gRP);
		apdater .notifyDataSetChanged();
		
//	    	 tv_ctag.setText(this.gRP.CTAG);
	}
	
	

	
	
	public int  gethaight(){
		return haight;
	}

	private void initview() {
		// TODO Auto-generated method stub
		
		
		initTopBar("收银台");
		btnTopRight2.setVisibility(View.VISIBLE);
		btnTopRight2.setBackgroundResource(R.drawable.shoppay_simg);
//		apliy = (Button) findViewById(R.id.btn1);
//		apliy.setOnClickListener(this);
		edtNumber = (EditText)findViewById(R.id.edtNumber);
//		saom_pay = (Button)findViewById(R.id.btn2);
//		saom_pay.setOnClickListener(this);
//		bankc_pay = (Button)findViewById(R.id.btn3);
//		bankc_pay.setOnClickListener(this);
		edtNumber.setInputType(InputType.TYPE_NULL);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);
		findViewById(R.id.btn_0).setOnClickListener(this);
		findViewById(R.id.btn_1).setOnClickListener(this);
		findViewById(R.id.btn_2).setOnClickListener(this);
		findViewById(R.id.btn_3).setOnClickListener(this);
		findViewById(R.id.btn_4).setOnClickListener(this);
		findViewById(R.id.btn_5).setOnClickListener(this);
		findViewById(R.id.btn_6).setOnClickListener(this);
		findViewById(R.id.btn_7).setOnClickListener(this);
		findViewById(R.id.btn_8).setOnClickListener(this);
		findViewById(R.id.btn_9).setOnClickListener(this);
		findViewById(R.id.btn_10).setOnClickListener(this);
		
		
		gRP  = new PayChannels();
		gRP.GRP = new ArrayList<PayChannelsBean>();
		listview  = getView(R.id.llistview);
		apdater = new PayListAdapter(this, gRP.GRP,haight);
		listview.setAdapter(apdater);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				paytype = gRP.GRP.get((int)arg3).PAYWAYID;
				 balances = edtNumber.getText().toString();
					if(balances==null&&balances.equals("")){
						Toast.makeText(getApplicationContext(), "请输入金额", Toast.LENGTH_SHORT).show();
						return;
					}
						try {
							Double.parseDouble(balances);
						} catch (Exception e) {
							// TODO: handle exception
							Toast.makeText(getApplicationContext(), "金额有误", Toast.LENGTH_SHORT).show();
							return;
						}
						if(balances.equals(".")){
							Toast.makeText(getApplicationContext(), "金额有误", Toast.LENGTH_SHORT).show();
							return;
						}
						if(!balances.equals(".")&&Double.parseDouble(balances)<=0){
							Toast.makeText(getApplicationContext(), "金额有误", Toast.LENGTH_SHORT).show();
							return;
						}
				
				if(gRP!=null&&gRP.GRP.size()>0){
					if(gRP.GRP.get((int)arg3).ISCNULL.equals("0")){
						settype();
					}else{
						strqrPay(2);
//						initpopwind(false);
					}
				}
			}
		});
		
		init();
		
	
		
	}
	
	public void settype(){
		final View v = getView(R.id.re);
		inittype();
		showPopupWindow(v);
	}
	
	private void Judgmentamount(){
		 balances = edtNumber.getText().toString();
		if(balances==null&&balances.equals("")){
			Toast.makeText(getApplicationContext(), "请输入金额", Toast.LENGTH_SHORT).show();
			return;
		}
			try {
				Double.parseDouble(balances);
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getApplicationContext(), "金额有误", Toast.LENGTH_SHORT).show();
				return;
			}
			if(balances.equals(".")){
				Toast.makeText(getApplicationContext(), "金额有误", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!balances.equals(".")&&Double.parseDouble(balances)<=0){
				Toast.makeText(getApplicationContext(), "金额有误", Toast.LENGTH_SHORT).show();
				return;
			}
			
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		String balances = edtNumber.getText().toString();
		switch (id) {
		case R.id.btnCancel:
			setEditValue();
			break;
		default :
			Button btn = (Button)v;
			String text = btn.getText().toString();
			if (null == text || text.equals("")) return;
			Editable edit = edtNumber.getText();
			if (edit.length() > 0) {
				edit.insert(edit.length(), text);
			} else {
				edit.insert(0, text);
			}
			break;
		}
	}
	
	private void setEditValue() {
		int start = edtNumber.getSelectionStart();
		if (start > 0) {
			edtNumber.getText().delete(start - 1, start);
		}
	}
	
	private void showPopupWindow(final View  view) {

		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.7f;
		getWindow().setAttributes(lp);
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
//	        popupWindow.showAsDropDown(view);

	    }
	
	private void initpopwind(boolean b, String cTAG) {
		// TODO Auto-generated method stub
	        // 一个自定义的布局，作为显示的内容
		 	datas = new ArrayList<ExcitationTypeBean>();
	        View contentView = LayoutInflater.from(this).inflate(
	                R.layout.excitationchoosetype_pop, null);
	        // 设置按钮的点击事件
	        TextView button = (TextView) contentView.findViewById(R.id.tv_conmmit);
	         tv_ctag = (TextView) contentView.findViewById(R.id.tv_ctag);
	         tv_ctag.setText(cTAG);
	        TextView tv_dimss = (TextView) contentView.findViewById(R.id.tv_dimss);
	        final EditText tv_phonenum = (EditText) contentView.findViewById(R.id.tv_phonenum);
	        final TextView content = (TextView) contentView.findViewById(R.id.content);
	        final LinearLayout lin = (LinearLayout) contentView.findViewById(R.id.ll_getphonse);
	        gridview = (GridView) contentView.findViewById(R.id.gridview);
	        gridview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					excitationgridadapter.setcheck(arg2);
					pointsid = excitationgridadapter.getdata().get(arg2).POINTSID;
					
					if(excitationgridadapter.getdata().get(arg2).ISCNULL.equals("0")){
					
						lin.setVisibility(View.VISIBLE);
					}else{
						lin.setVisibility(View.GONE);
					}
				}
			});
	        
	        excitationgridadapter  =   new ExcitationGridAdapter(EkOrderPay.this, datas);
	        
	        gridview.setAdapter(excitationgridadapter);
	      
	        popupWindow = new PopupWindow(contentView,
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
		    popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
	        
	        content.setText("请选择激励模式");
//	        button.setText("");
	        
	        button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub、
					custermtel = tv_phonenum.getText().toString();
					if(pointsid==null||pointsid.equals("")){
						T.showShort(getApplicationContext(), "请填选择激励模式");
						return;
					}
					if(custermtel==null||custermtel.equals("")){
						T.showShort(getApplicationContext(), "请填写消费者手机号");
						return;
					}
					
					popupWindow.dismiss();
					
					if(paytype.equals("wechat")){
						strqrPay(0);
					}else if(paytype.equals("alipay")){
						strqrPay(1);
					}else if(paytype.equals("balance")){
						strqrPay(2);
					}else if(paytype.equals("quick")){
						
					}else if(paytype.equals("posp")){
//						Intent it = new Intent(EkOrderPay.this,HandBrushJHLActivity.class);
//						it.putExtra("POINTSID", pointsid);
//						it.putExtra("CUSTERMTEL", custermtel);
//						it.putExtra("balance", balances);
//						it.putExtra("TERMNO", TERMNO);
//						startActivity(it);
					}
				}

			});
	        tv_dimss.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					popupWindow.dismiss();
//					lin.setVisibility(View.GONE);
					
				}
			});
	        

	        
	        popupWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					WindowManager.LayoutParams lp = getWindow().getAttributes();
					lp.alpha = 1f;
					getWindow().setAttributes(lp);
//					lin.setVisibility(View.GONE);
				}
			});
		
	}
	
	
	private void strqrPay(final int i) {
		// TODO Auto-generated method stub
		String host = "";
		if( i==0){
			host = Api.WECHATPAYD;
		}else if( i==1){
			host = Api.ALIPAYD;
		}else if( i==2){
			host = Api.BALANCEPAY;
		}else if(i==3){
			host = Api.QUICKPAY;
		}
		
		ProgressDialogUtil.showProgressDlg(EkOrderPay.this, "请稍候");
		ProgressDialogUtil.setCancelable(false);
		PayChannelsResquest req = new PayChannelsResquest();
		req.ACCOUNT = UserInfoManager.getUserInfo(this).shop_id+"";
		req.TERMTYPE = "1";
		req.CUSTERMTEL = custermtel;
		req.ORDAMT = String.valueOf((int)(Double.parseDouble(balances)*100));
		req.POINTSID = 	pointsid;
				Log.e("", ""+req.toString());
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, host, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(EkOrderPay.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				QrUrlResultResponse bean = new Gson().fromJson(resp.result, QrUrlResultResponse.class);
				
				if(Api.SUCCEED == bean.result_code) {
					if(bean.data.RSPCOD.equals("000000")){
						

						if(i==3){
							Intent it = new Intent(EkOrderPay.this,QrcodeAct.class);
							it.putExtra("title","收款二维码");
							it.putExtra("url",bean.data.QRCODE);
							startActivity(it);
							return;
						}
						
						Intent it = new Intent(EkOrderPay.this,QrcodeAct.class);
						it.putExtra("title","收款二维码");
						it.putExtra("url",bean.data.QRCODE);
						startActivity(it);
						
					}else{
						T.showShort(getApplicationContext(), bean.data.RSPMSG);
					}
				}else{
					T.showShort(getApplicationContext(), "请求失败");
				}
			}
		});
	}

	
	@Override  
	  public void onWindowFocusChanged(boolean hasFocus) {  
		haight = getView(R.id.re_number).getHeight();
	    super.onWindowFocusChanged(hasFocus);  
	  }  
	
}
