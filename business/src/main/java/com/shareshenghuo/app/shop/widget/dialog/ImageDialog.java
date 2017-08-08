package com.shareshenghuo.app.shop.widget.dialog;


import com.shareshenghuo.app.shop.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ImageDialog extends Dialog implements android.view.View.OnClickListener{
	
	private ImageView img_clear,img_card;
	private Context context;
	private ImagedialogListener listener;
	private String content;
	private int ids;
	private TextView tv_cvv;
	public ImageDialog(Context context,int theme,String content,int id,ImagedialogListener listeners) {
		super(context,theme);
		this.context = context;
		this.listener = listeners;
		this.content = content;
		this.ids = id;
	
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagedialog_layout);
		img_clear=(ImageView) findViewById(R.id.img_clear);
		tv_cvv = (TextView) findViewById(R.id.tv_cvv);
		img_card = (ImageView) findViewById(R.id.img_card);
		img_clear.setOnClickListener(this);
		tv_cvv.setText(content);
		img_card.setImageDrawable(context.getResources().getDrawable(ids));
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		listener.ImagedialogListener(v);
	}

}
