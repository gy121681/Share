package com.td.qianhai.epay.oem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class MentionNowAcitvity extends Activity {


	private String money,crdflg,banknames,card;
	
	private int state;

	private TextView me_result_time,  me_result,card_name,bank_name,moneyview,bt_title_left,tv_title_contre;

	private Button btn_mention;
	
	private LinearLayout me_data;

	private Intent it;
	
	private ImageView result_img;
	
	private String tag ,sts,mersts;
	
	private int isreal = -1;
	
	private OneButtonDialogWarn warnDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.mention_now_activity);
		AppContext.getInstance().addActivity(this);
		mersts =  MyCacheUtil.getshared(this).getString("MERSTS", "");
		sts = MyCacheUtil.getshared(this).getString("STS", "");
		initview();

	}

	private void initview() {
		tv_title_contre = (TextView) findViewById(R.id.tv_title_contre);
		tv_title_contre.setText("");
		me_result_time = (TextView) findViewById(R.id.me_result_time);
		me_data = (LinearLayout) findViewById(R.id.me_data);
		btn_mention = (Button) findViewById(R.id.btn_mentions);
		me_result = (TextView) findViewById(R.id.me_result);
		card_name = (TextView) findViewById(R.id.card_name);
		bank_name =  (TextView) findViewById(R.id.bank_name);
		moneyview = (TextView) findViewById(R.id.money);
		result_img = (ImageView) findViewById(R.id.result_img);
		bt_title_left = (TextView) findViewById(R.id.bt_title_left);
		bt_title_left.setVisibility(View.GONE);
		
		it = getIntent();
		state = it.getIntExtra("State", -1);
		tag = it.getStringExtra("tag");
		
		if(tag!=null&&!tag.equals("")){
			
		if(tag.equals("0")){
			 Calendar calendar = Calendar.getInstance();

			 SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd"); 
			 calendar.setTime(new Date()); //当天
			 //下一天
			 calendar.add(Calendar.DAY_OF_YEAR, 1);
			 String nextDate_1 = sf.format(calendar.getTime());  
			 me_result_time.setText("预计"+nextDate_1+"   "+"23:59:00"+"之前到账");
		}else{
//			 Calendar calendar = Calendar.getInstance();
//			 int year = calendar.get(Calendar.YEAR);
//			 int moth = calendar.get(Calendar.MONTH)+1;
//			 int day = calendar.get(Calendar.DAY_OF_MONTH);
			 me_result_time.setText("预计5分钟内到账");
		}
		}
		 
		if (state == 0) {
			me_data.setVisibility(View.INVISIBLE);
			me_result_time.setVisibility(View.INVISIBLE);
			me_result.setText(it.getStringExtra("Err"));
			result_img.setBackgroundResource(R.drawable.resize_png_newerr);
			btn_mention.setText("返回");

			
		} else if (state == 1) {
			result_img.setBackgroundResource(R.drawable.resize_png_new);
			card = it.getStringExtra("Actcard");
			banknames = it.getStringExtra("Banknam");
			money = it.getStringExtra("Money");
			crdflg = it.getStringExtra("Crdflg");
			
			if(crdflg.equals("01")){
				card_name.setText("借记卡");
			}else{
				card_name.setText("贷记卡");
			}
			bank_name.setText(banknames+"  尾号 "+card.substring(card.length()-4));
			moneyview.setText("¥  "+money);

			
			
//			if (money.length() == 1) {
//				moneyview.setText("0.0" + money);
//			} else if (money.length() == 2) {
//				moneyview.setText("0." + money);
//			} else {
//				moneyview.setText(money.substring(0,
//						money.length() - 2)
//						+ "."
//						+ money.substring(
//								money.length() - 2));
//			}
		}else{
			if(state == 4){
				result_img.setBackgroundResource(R.drawable.resize_png_newerr);
				me_data.setVisibility(View.INVISIBLE);
				me_result_time.setVisibility(View.INVISIBLE);
				me_result.setText(it.getStringExtra("res"));
				btn_mention.setText("返回");
			}else if(state == 3){
				result_img.setBackgroundResource(R.drawable.resize_png_new);
				me_data.setVisibility(View.INVISIBLE);
				me_result_time.setVisibility(View.INVISIBLE);
				me_result.setText(it.getStringExtra("res"));
				btn_mention.setText("确定");
				
				if(sts.equals("0")||sts.equals("3")||sts.equals("4")){
					isreal = 0;
				}else if(sts==null||sts.equals("")){
					isreal = 2;
				}else{
					isreal = 1;
				}
			}
			
		}

		
		btn_mention.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(isreal==1){
					
					Intent it = new Intent(MentionNowAcitvity.this,AuthenticationActivity.class);
					startActivity(it);
					
//					warnDialog = new OneButtonDialogWarn(MentionNowAcitvity.this,
//							R.style.CustomDialog, "提示",
//							"为了您账户安全！请补全资料进行实名认证再进行操作", "确定",
//							new OnMyDialogClickListener() {
//								@Override
//								public void onClick(View v) {
//									Intent it = new Intent(
//											MentionNowAcitvity.this,
//											NewRealNameAuthenticationActivity.class);
//									startActivity(it);
//									finish();
//									warnDialog.dismiss();
//								}
//							});
//					warnDialog.setCancelable(false);
//					warnDialog.setCanceledOnTouchOutside(false);
//					warnDialog.show();
//					finish();
//					Intent it = new Intent(MentionNowAcitvity.this,NewRealNameAuthenticationActivity.class);
//					startActivity(it);
				}else{
					finish();
				}
			}
		});
	}

}
