package com.td.qianhai.epay.oem;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.td.qianhai.epay.oem.beans.AppContext;

public class HelpVersionActivity extends BaseActivity {

	private int mark;
	private TextView tvShow, tv_title_content, tv_back;
	private LinearLayout tv_help_timework,qrcard_lin;
	private ImageView myqrcode_img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		Bundle bundle = getIntent().getExtras();
		mark = bundle.getInt("mark");
		setContentView(R.layout.help_version_new);
		tvShow = ((TextView) findViewById(R.id.tv_help_version));
		tv_back = (TextView) findViewById(R.id.bt_title_left);
		tv_help_timework = (LinearLayout) findViewById(R.id.tv_help_timework);
		myqrcode_img = (ImageView) findViewById(R.id.myqrcode_img);
		qrcard_lin = (LinearLayout) findViewById(R.id.qrcard_lin);
		// 返回
		tv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		tv_title_content = (TextView) findViewById(R.id.tv_title_contre);
		viewShow(mark);
	}

	private void viewShow(int mark) {
		switch (mark) {
		case 1:
			qrcard_lin.setVisibility(View.GONE);
			tv_title_content.setText("客服电话");
			tvShow.setText("客服电话");
			tv_help_timework.setVisibility(View.VISIBLE);
			TextView tvContent = (TextView) findViewById(R.id.tv_help_contents);
			tvContent.setVisibility(View.VISIBLE);
			tvContent.setText("02888265063");
			break;
		case 2:
			tvShow.setText("常见问题：" + getQuesion());
			break;
		case 3:
			tv_title_content.setText("终端号");
			tvShow.setText("终端号：" + getTerminalId());
			break;
		case 4:
			tvShow.setText("微信客服：");
			break;
		case 5:
			tvShow.setText(getInstructions());
			break;
		case 6:
			tvShow.setText(getFee());
			break;
		case 7:
//			qrcard_lin.setVisibility(View.VISIBLE);
//			try {
//				Bitmap qrCodeBitmap = EncodingHandler.createQRCode(HttpUrls.APPURL, 500);
//				myqrcode_img.setImageBitmap(qrCodeBitmap);
//			} catch (WriterException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			tv_title_content.setText("版本信息");
			tvShow.setText("当前版本：" + getVersion());
			break;
		case 8:
			tvShow.setText(getComplaintsAndSuggestions());
			break;
		default:
			break;
		}
	}

	private CharSequence getComplaintsAndSuggestions() {
		// TODO Auto-generated method stub
		return null;
	}

	private CharSequence getFee() {
		// TODO Auto-generated method stub
		return null;
	}

	private CharSequence getInstructions() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getQuesion() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getVersion() {
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = "秀儿支付" + info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "找不到版本号";
		}
	}

	private String getTerminalId() {
		String terminalId = ((AppContext) getApplication()).getPsamId();
		return terminalId;
	}
}
