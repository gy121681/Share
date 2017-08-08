package com.td.qianhai.epay.oem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.utils.DateUtil;

/**
 * 帮助界面
 * @author liangge
 *
 */
public class HelpActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_help_new);
		AppContext.getInstance().addActivity(this);
		((TextView) findViewById(R.id.tv_title_contre)).setText("帮助");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		findViewById(R.id.qualifications).setOnClickListener(this);
		findViewById(R.id.btn_menu_help_customer_tel).setOnClickListener(this);
		findViewById(R.id.btn_menu_help_version).setOnClickListener(this);
		findViewById(R.id.btn_menu_help_qrcode).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		if (DateUtil.isFastDoubleClick()) {
			return;
		} else {
			switch (v.getId()) {
//			case R.id.btn_menu_help_terminal_num:
//				gotoActivity(HelpVersionActivity.class, 3);
//				break;
			case R.id.btn_menu_help_customer_tel:
//				gotoActivity(HelpVersionActivity.class, 1);
				Intent it1 = new Intent(this, HelpVersionActivity.class);
				it1.putExtra("mark", 1);
				startActivity(it1);
				break;
			case R.id.btn_menu_help_version:
//				gotoActivity(HelpVersionActivity.class, 7);
				Intent it2 = new Intent(this, HelpVersionActivity.class);
				it2.putExtra("mark", 7);
				startActivity(it2);
				break;
			case R.id.btn_menu_help_qrcode:
				Intent it3 = new Intent(HelpActivity.this,IntroductionActivity.class);
				startActivity(it3);
				break;
			case R.id.qualifications:
				Intent it4 = new Intent(HelpActivity.this,PictureActivitys.class);
				startActivity(it4);
				break;
			default:
				break;
			}
		}
	}

//	@SuppressWarnings("rawtypes")
//	private void gotoActivity(Class c, int mark) {
//		Intent it = new Intent(this, c);
//		it.putExtra("mark", mark);
//		startActivity(it);
//	}
}
