package com.td.qianhai.epay.oem.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;

public class DownloanDialog extends Dialog {

	private TextView tv_title, progress;
	public ProgressBar progressBar;
	private Context context;
	private String title;

	private Handler mainHandler;

	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				int downloaded_size = msg.getData().getInt("size");
				progressBar.setProgress(downloaded_size);
				int result = (int) ((float) downloaded_size
						/ progressBar.getMax() * 100);
				progress.setText(result + "%");
				if (progressBar.getMax() == progressBar.getProgress()) {
					dismiss();
					mainHandler.sendEmptyMessage(2);
				}
			}
		}
	};

	public DownloanDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public DownloanDialog(Context context, int theme, String title,
			Handler handler) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.title = title;
		this.mainHandler = handler;
	}

	public DownloanDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_dialog);
		tv_title = (TextView) findViewById(R.id.dow_title);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progress = (TextView) findViewById(R.id.textView1);
		tv_title.setText(title);
	}
}
