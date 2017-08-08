package com.shareshenghuo.app.shop;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PayIsCompleteAvtivty extends BaseTopActivity{
	
	private String tag ="",msg ="",colsno = "",balac = "",name = "";
	private TextView tvs,shopname,shopbalance,shopno;
	private Button llWalletRecharge;
	private ImageView img;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.payis_complete_avtivty);
		
		tag = getIntent().getStringExtra("tag");
		msg = getIntent().getStringExtra("msg");
		colsno = getIntent().getStringExtra("colsno");
		balac = getIntent().getStringExtra("balac");
		name = getIntent().getStringExtra("name");
		
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		
		initTopBar("支付结果");
		tvs = getView(R.id.tvs);
		shopname = getView(R.id.shopname);
		shopbalance = getView(R.id.shopbalance);
		shopno = getView(R.id.shopno);
		img = getView(R.id.img);
		llWalletRecharge = getView(R.id.llWalletRecharge);
		llWalletRecharge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		if(tag.equals("0")){
			img.setImageResource(R.drawable.refail);
			tvs.setText("支付失败");
			llWalletRecharge.setText("返回");
			tvs.setTextColor(getResources().getColor(R.color.red));
		}else{
			img.setImageResource(R.drawable.resuccess);
			tvs.setText("支付成功");
			llWalletRecharge.setText("确定");
			tvs.setTextColor(getResources().getColor(R.color.green_h));
		}
		tvs.setText(msg);
		shopname.setText(name);
		shopbalance.setText(balac+"元");
		shopno.setText(colsno);
	}
}
