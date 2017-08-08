package com.td.qianhai.epay.oem;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.mpay.utils.DateUtil;

public class OrderInfoAcitvity extends BaseActivity {

	private String getid,odernames;

	private Double getmoney;
	
	private TextView o_hand_tv, o_type_tv, o_money_tv, o_name_tv, o_id_tv,
			o_time_tv, o_num_tv,o_account1,o_account,title, back,o_money1_tv,o_money2_tv,tocredit_tv;

	private LinearLayout o_user_rlayout,money_type;
	
	private ImageView o_img;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_order_info);
		AppContext.getInstance().addActivity(this);
		Intent it = getIntent();

		getid = it.getStringExtra("id");
		odernames = it.getStringExtra("odernames");
		
		
		String money = it.getStringExtra("money");
		if(money!=null&&!money.equals("")){
			 getmoney = Double.parseDouble(money);
		}

		initview();

		initdata();
	}

	private void initview() {
		o_account1 = (TextView) findViewById(R.id.o_account1);
		o_account = (TextView) findViewById(R.id.o_account);
		o_hand_tv = (TextView) findViewById(R.id.o_hand_tv);
		o_type_tv = (TextView) findViewById(R.id.o_type_tv);
		o_money_tv = (TextView) findViewById(R.id.o_money_tv);
		o_name_tv = (TextView) findViewById(R.id.o_name_tv);
		o_id_tv = (TextView) findViewById(R.id.o_id_tv);
		o_time_tv = (TextView) findViewById(R.id.o_time_tv);
		o_num_tv = (TextView) findViewById(R.id.o_num_tv);
		o_user_rlayout = (LinearLayout) findViewById(R.id.o_user_rlayout);
		money_type = (LinearLayout) findViewById(R.id.money_type);
		o_money1_tv = (TextView) findViewById(R.id.o_money1_tv);
		o_money2_tv = (TextView) findViewById(R.id.o_money2_tv);
		tocredit_tv = (TextView) findViewById(R.id.tocredit_tv);
		o_img = (ImageView) findViewById(R.id.o_img);

		title = (TextView) findViewById(R.id.tv_title_contre);
		title.setText("详情");
		back = (TextView) findViewById(R.id.bt_title_left);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	private void initdata() {

		BussinessInfoTask task = new BussinessInfoTask();
		task.execute(HttpUrls.ORDERINFO + "", getid);

	}

	private void showView(HashMap<String, Object> result) {
		if(result!=null){
			String operbtyp = result.get("OPERBTYP").toString();
			String operstyp = result.get("OPERSTYP").toString();
			o_num_tv.setText(getid);
			

			
			Double money = Double.parseDouble(result.get("TXNAMT").toString());
			
			Double money1 = 0.0;
			if(result.get("FEEAMT")!=null){
				 money1 = Double.parseDouble(result.get("FEEAMT").toString());
			}
			
			if(getmoney!=null&&!getmoney.equals("")){
				o_money1_tv.setText((getmoney/100)+"元");
			}else{
				o_money1_tv.setText((money/100)+"元");
			}
			
			String date = DateUtil.strToDateToLong(result.get("OPERTIM")
					.toString());
			o_time_tv.setText(date);
			String sts = result.get("OPERSTS").toString();
			if ("1".equals(sts)) {
				o_hand_tv.setText("成功");
				o_img.setImageResource(R.drawable.resize_png_new);
			} else if("0".equals(sts)) {
				o_hand_tv.setText("失败");
				o_img.setImageResource(R.drawable.resize_png_newerr);
			}else if("2".equals(sts)) {
				o_img.setImageResource(R.drawable.resize_png_new);
				o_hand_tv.setText("处理中");
			}
			if (operbtyp.equals("0")) {
				if(operstyp.equals("04")){
					o_type_tv.setText("收            益");
					o_money_tv.setText("+"+money / 100 + "元");
					o_user_rlayout.setVisibility(View.GONE);
					o_account1.setVisibility(View.GONE);
					o_account.setVisibility(View.GONE);
				}else if (operstyp.equals("02")){
					o_money_tv.setText("+"+money / 100 + "元");
					o_type_tv.setText("转 入 金 额");
					
					if(result.get("OUTACTNAM")!=null){
						String optername = result.get("OUTACTNAM").toString();
						o_name_tv.setText(optername);
					}else{
						o_name_tv.setText("未知");
					}
					
					if(result.get("OUTACTID")!=null){
						String phone = result.get("OUTACTID").toString();
						String setphone = phone.substring(0,3);
						String getphone = phone.substring(phone.length()-4);
						o_id_tv.setText(setphone+"****"+getphone);
					}
				}else if(operstyp.equals("09")){
					o_money_tv.setText("+"+money / 100 + "元");
					o_type_tv.setText("高级会员收益");
					o_user_rlayout.setVisibility(View.GONE);
				}else if(operstyp.equals("01")){
					o_money_tv.setText("+"+money / 100 + "元");
					o_type_tv.setText("收            款");
					o_user_rlayout.setVisibility(View.GONE);
					
				}else{
					o_type_tv.setText(odernames);
					o_user_rlayout.setVisibility(View.GONE);
					o_account1.setVisibility(View.GONE);
					o_account.setVisibility(View.GONE);
				}
			} else if (operbtyp.equals("2")) {
				if(operstyp.equals("22")){
					o_money_tv.setText("-"+money / 100 + "元");
					o_type_tv.setText("转           账");
					
					if(result.get("PACTNAM")!=null){
						String optername = result.get("PACTNAM").toString();
						o_name_tv.setText(optername);
					}else{
						o_name_tv.setText("未知");
					}
					
					if(result.get("PACTID")!=null){
						String phone = result.get("PACTID").toString();
						String setphone = phone.substring(0,3);
						String getphone = phone.substring(phone.length()-4);
						o_id_tv.setText(setphone+"****"+getphone);
					}
				}else if(operstyp.equals("24")){
					o_money_tv.setText("-"+money / 100 + "元");
					o_type_tv.setText("手机充值");
					tocredit_tv.setText("充 值 号 码");
					if(result.get("RECNUMBER")!=null){
						String a = result.get("RECNUMBER").toString();
						String setphone = a.substring(0,3);
						String getphone = a.substring(a.length()-4);
						o_name_tv.setText(setphone+"****"+getphone);
						o_id_tv.setText("");
					}
					
				}else if(operstyp.equals("27")){
					o_type_tv.setText("信用卡还款");
					tocredit_tv.setText("账 户 信 息");
					if(result.get("CARDNO")!=null){
						String a = result.get("CARDNO").toString();
						o_name_tv.setText("尾号"+a.substring(a.length()-4));
						o_id_tv.setText(result.get("CARDNAM").toString());
					}
					if(result.get("BANKNAM")!=null){
						money_type.setVisibility(View.VISIBLE);
						o_money_tv.setText(result.get("BANKNAM").toString());
						o_money2_tv.setText("");
					}
					
					
				}else if(operstyp.equals("25")){
					o_money_tv.setText("-"+money / 100 + "元");
					o_type_tv.setText("费 率 购 买");
					o_user_rlayout.setVisibility(View.GONE);
					o_account1.setVisibility(View.GONE);
					o_account.setVisibility(View.GONE);
				}else if(operstyp.equals("21")){
					money_type.setVisibility(View.VISIBLE);
					o_money_tv.setText ("到账金额: "+Double.parseDouble(result.get("TXNAMT").toString()) / 100 + "元");
					o_money2_tv.setText("手  续  费: "+money1 / 100 + "元");
					o_type_tv.setText("提            现");
					o_account1.setVisibility(View.GONE);
					o_account.setVisibility(View.GONE);
					o_user_rlayout.setVisibility(View.GONE);
				}else if(operstyp.equals("304")){
					money_type.setVisibility(View.VISIBLE);
					o_type_tv.setText("信用卡还款");
					tocredit_tv.setText("账 户 信 息");
					if(result.get("TARGATCNAM")!=null){
						String a = result.get("TARGACTID").toString();
						o_name_tv.setText("尾号"+a.substring(a.length()-4));
						o_id_tv.setText(result.get("TARGATCNAM").toString());
					}
//					if(result.get("BANKNAM")!=null){
//						
//						o_money_tv.setText(result.get("BANKNAM").toString());
//						o_money2_tv.setText("");
//					}
					o_account1.setText(odernames);
					o_money_tv.setText ("到账金额: "+Double.parseDouble(result.get("TXNAMT").toString()) / 100 + "元");
					o_money2_tv.setText("手  续  费: "+money1 / 100 + "元");
				}else if(operstyp.equals("303")){
					money_type.setVisibility(View.VISIBLE);
					o_type_tv.setText("信用卡还款");
					tocredit_tv.setText("账 户 信 息");
					if(result.get("TARGATCNAM")!=null){
						String a = result.get("TARGACTID").toString();
						o_name_tv.setText("尾号"+a.substring(a.length()-4));
						o_id_tv.setText(result.get("TARGATCNAM").toString());
					}
					o_account1.setText(odernames);
					o_money_tv.setText ("到账金额: "+Double.parseDouble(result.get("TXNAMT").toString()) / 100 + "元");
					o_money2_tv.setText("手  续  费: "+money1 / 100 + "元");
				}else if(operstyp.equals("33")){
					o_money_tv.setText ("加盟费: "+Double.parseDouble(result.get("TXNAMT").toString()) / 100 + "元");
					o_money2_tv.setText("手  续  费: "+money1 / 100 + "元");
					o_type_tv.setText("高级会员加盟费");
					o_account1.setVisibility(View.GONE);
					o_account.setVisibility(View.GONE);
					o_user_rlayout.setVisibility(View.GONE);
				}else if(operstyp.equals("26")){
					money_type.setVisibility(View.VISIBLE);
					o_money_tv.setText ("到账金额: "+Double.parseDouble(result.get("TXNAMT").toString()) / 100 + "元");
					o_money2_tv.setText("手  续  费: "+money1 / 100 + "元");
					o_type_tv.setText("闪 电 提 现");
					o_account1.setVisibility(View.GONE);
					o_account.setVisibility(View.GONE);
					o_user_rlayout.setVisibility(View.GONE);
				}else if(operstyp.equals("35")){
					o_type_tv.setText("宝 币 兑 换");
					o_user_rlayout.setVisibility(View.GONE);
					o_account1.setVisibility(View.GONE);
					o_account.setVisibility(View.GONE);
				}else{
					o_type_tv.setText(odernames);
					o_user_rlayout.setVisibility(View.GONE);
					o_account1.setVisibility(View.GONE);
					o_account.setVisibility(View.GONE);
				}
			} else if (operbtyp.equals("4")) {
				o_type_tv.setText("");
			}
		}
	}

	class BussinessInfoTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog("正在查询中...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1] };
			return NetCommunicate.getMidatc(HttpUrls.ORDERINFO,
					values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					showView(result);
				} else {
					ToastCustom.showMessage(OrderInfoAcitvity.this,
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT);
					finish();
				}
			}
			super.onPostExecute(result);
		}

	}

}
