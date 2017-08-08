package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.ExchangeListInfoBean;
import com.shareshenghuo.app.shop.network.request.ExchangeListInfoRequset;
import com.shareshenghuo.app.shop.network.response.ExchangeListInfoResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.DateUtil;
import com.shareshenghuo.app.shop.util.Util;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExchangeListInfoAcyivity extends BaseTopActivity {

	private String id = "";
	private TextView tv_1,tv_2,tv_3,tv_4,tv_5,tv_6,tv_7,tv_8,tv_9,tv_,tv_11;
	private LinearLayout llre;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exchange_list_info);
		initView();
		loadData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		initTopBar("兑换详情");
		id = getIntent().getStringExtra("id");
		tv_1 = getView(R.id.tv_1);
		tv_2 = getView(R.id.tv_2);
		tv_3 = getView(R.id.tv_3);
		tv_4 = getView(R.id.tv_4);
		tv_ = getView(R.id.tv_);
		tv_5 = getView(R.id.tv_5);
		tv_6 = getView(R.id.tv_6);
		tv_7 = getView(R.id.tv_7);
		tv_8 = getView(R.id.tv_8);
		tv_9 = getView(R.id.tv_9);
//		tv_9 = getView(R.id.tv_10);
//		tv_9 = getView(R.id.tv_11);
//		tv_9 = getView(R.id.tv_12);
		tv_11 = getView(R.id.tv_11);
		llre = getView(R.id.llre);
		
		
	}
	
	public void loadData() {
		ExchangeListInfoRequset req = new ExchangeListInfoRequset();
		req.wdId = id;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.WITHDRAWSINGLEQUERY, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ExchangeListInfoResponse bean = new Gson().fromJson(resp.result, ExchangeListInfoResponse.class);
				if(Api.SUCCEED == bean.result_code)
					Log.e("", " - - - -  "+resp.result.toString());
					updateView(bean.data);
			}

		});
	}
	
	private void updateView(ExchangeListInfoBean data) {
		// TODO Auto-generated method stub
		if(data.act_nam!=null){
			tv_1.setText(data.act_nam);
		}
//		if(data.create_time!=null){
			tv_2.setText(DateUtil.getTime(data.create_time,0));
//		}
		if(data.bank_nam!=null){
			tv_3.setText(data.bank_nam);
		}
		if(data.card_no!=null){
			if(!TextUtils.isEmpty(data.card_no)){
				String card = data.card_no;
				tv_4.setText(card.substring(0, 6)+" **** **** "+card.substring(card.length()-4));
			}
			
//			tv_4.setText(data.card_no);
		}
		if(data.wd_amt!=null){
			tv_5.setText(Util.getnum(data.wd_amt,false));
		}
		if(data.fee_amt!=null){
			tv_6.setText(Util.getnum(data.fee_amt,false));
		}
		if(data.income_tax!=null){
			tv_7.setText(Util.getnum(data.income_tax,false));
		}
		if(data.tf_amt!=null){
			tv_8.setText(Util.getnum(data.tf_amt,false));
		}
		if(data.wd_sts!=null){
			if(data.wd_sts.equals("0")){
				setText(R.id.tv_9, "待打款");
			}else if(data.wd_sts.equals("1")){
				setText(R.id.tv_9, "银行处理中");
			}else if(data.wd_sts.equals("2")){
				setText(R.id.tv_9, "打款失败");
			}else if(data.wd_sts.equals("3")){
				setText(R.id.tv_9, "已退款");
				llre.setVisibility(View.VISIBLE);
				if(data.refund_amt!=null){
					setText(R.id.tv_10, Util.getnum(data.refund_amt,false));
				}
				if(data.refund_reason!=null){
					setText(R.id.tv_11, data.refund_reason);
				}
				setText(R.id.tv_12, DateUtil.getTime(data.refund_create_time,0));
			}else if(data.wd_sts.equals("4")){
				setText(R.id.tv_9, "打款成功");
			}
		}
		
		if(data.wd_type.equals("1")){
//			tv_.setText("(T+1)预计"+getSpecWorkDate(DateUtil.getTime(data.create_time,2),0)+"到账");
			tv_.setText("预计1个工作日到账");
		}else if(data.wd_type.equals("2")){
//			tv_.setText("(T+3)预计"+getSpecWorkDate(DateUtil.getTime(data.create_time,2),2)+"到账");
			tv_.setText("预计3个工作日到账");
		}else if(data.wd_type.equals("3")){
//			tv_.setText("(T+7)预计"+getSpecWorkDate(DateUtil.getTime(data.create_time,2),7)+"到账");
			tv_.setText("预计7个工作日到账");
		}
		
		if(data.wdcls_inf_type!=null&&data.wdcls_inf_type.equals("1")){
			tv_11.setText("产业链秀点");
		}else{
			tv_11.setText("秀儿秀点");
		}
		
	}
	
	public  String  getSpecWorkDate(String strDate, int j){
		String workDay = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
		d = sdf.parse(strDate);
		} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		//传进来的日期 往后加一天
		cal.add(Calendar.DAY_OF_YEAR, j);
		int workDayFlag = 0;
		for (int i = 0; i < 15; i++) {
		if (isWorkDay(cal) && workDayFlag<2) {
		Date time = cal.getTime();
		if (workDayFlag == 1) {
		workDay = sdf.format(time);
		//System.out.println("输出第二个工作日：" + workDay);
		break;
		}
		workDayFlag++;
		}
		cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		return workDay;
		}
	
	 public boolean isWorkDay(Calendar calendar){  
	       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	       if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY  
	               && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){  
	           //平时  
	    	   return true;
//	           return !getWeekdayIsHolidayList().contains(sdf.format(calendar.getTime()));  
	       }else{  
	           //周末  
	    	   return false;
//	           return getWeekendIsWorkDateList().contains(sdf.format(calendar.getTime()));  
	       }  
	   }  

}
