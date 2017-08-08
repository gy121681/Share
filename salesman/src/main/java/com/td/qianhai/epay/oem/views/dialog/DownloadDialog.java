package com.td.qianhai.epay.oem.views.dialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnIsDownloadClickListener;

public class DownloadDialog extends Dialog {

	private TextView tv_title;
	private String title;
	private Context mContext;
	private ProgressBar mProgress;
	private Thread downLoadThread;
	/** 下载过程中不能点击 */
	private boolean isClick = false;
	private boolean downloadOk = false;
	private OnIsDownloadClickListener isDownloadClickListener;
	private TextView tv;
	/**
	 * 下载的url
	 */
	private String apkUrl = null;

	/* 进度值 */
	private int progress;

	/* 下载更新开始 */
	private static final int DOWN_UPDATE = 1;
	/* 更新结束 */
	private static final int DOWN_OVER = 2;

	private String fileName;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

			case DOWN_UPDATE:// 下载进度条更新
				mProgress.setProgress(progress);
				tv.setText(progress + "%");
				break;
			case DOWN_OVER:// 更新完毕
//				cancel();
				isDownloadClickListener.isDownload(fileName, DOWN_OVER);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_dialog);
		tv_title = (TextView) findViewById(R.id.dow_title);
		tv_title.setText(title);

		Thread getFileName = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				URL url;
				HttpURLConnection conn = null;
				try {
					url = new URL(apkUrl);
					conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET");

					if (conn.getResponseCode() != 200) {
						throw new RuntimeException("server no response!");
					}
					int fileSize = conn.getContentLength();
					if (fileSize <= 0) {
						throw new RuntimeException("file is incorrect!");
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fileName = getFileName(conn);
			}
		});
		getFileName.start();
		try {
			getFileName.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		init();
		// if (FileUtil.isFileExists(fileName)) {
		// mHandler.sendEmptyMessage(DOWN_OVER);
		// } else {
		downloadApk();
		// }
	}

	public DownloadDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public DownloadDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public DownloadDialog(Context context, String url, String title,
			OnIsDownloadClickListener isDownloadClickListener) {
		super(context, R.style.CustomDialog);
		mContext = context;
		this.title = title;
		this.apkUrl = url;
		this.isDownloadClickListener = isDownloadClickListener;
	}

	@Override
	public void cancel() {
		super.cancel();
	}

	private Runnable mdownApk = new Runnable() {

		@Override
		public void run() {
			HttpClient client = new DefaultHttpClient();
			// params[0]代表连接的url
			HttpGet get = new HttpGet(apkUrl);
			HttpResponse response;
			try {
				response = client.execute(get);
				HttpEntity entity = response.getEntity();
				long length = entity.getContentLength();
				InputStream is = entity.getContent();
				FileOutputStream fileOutputStream = null;
				if (is != null) {
					Log.e("log", fileName);
					File file = new File(
							Environment.getExternalStorageDirectory(), fileName);
					fileOutputStream = new FileOutputStream(file);

					byte[] buf = new byte[1024];
					int ch = -1;
					int count = 0;
					while ((ch = is.read(buf)) != -1) {

						fileOutputStream.write(buf, 0, ch);
						count += ch;
						progress = (int) (((float) count / length) * 100);
						// 更新进展
						if (mProgress.getProgress() < progress) {
							mHandler.sendEmptyMessage(DOWN_UPDATE);
						}
					}
				}
				fileOutputStream.flush();
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
				mHandler.sendEmptyMessage(DOWN_OVER);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	private void downloadApk() {
		downLoadThread = new Thread(mdownApk);
		downLoadThread.start();

	}

	private void init() {
		tv = (TextView) this.findViewById(R.id.textView1);
		mProgress = (ProgressBar) this.findViewById(R.id.progressBar1);
	}

	@Override
	public void show() {
		isClick = true;
		downloadOk = false;
		super.show();
	}

	/**
	 * 安装APK
	 */
	@SuppressLint("SdCardPath")
	private void installApk() {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.fromFile(new File("/sdcard/" + fileName)),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}

	/**
	 * 获取服务端文件名
	 * 
	 * @param conn
	 * @return
	 */
	private String getFileName(HttpURLConnection conn) {
		String fileName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1,
				apkUrl.length());
		if (fileName == null || "".equals(fileName.trim())) {
			String content_disposition = null;
			for (Entry<String, List<String>> entry : conn.getHeaderFields()
					.entrySet()) {
				if ("content-disposition".equalsIgnoreCase(entry.getKey())) {
					content_disposition = entry.getValue().toString();
				}
			}
			try {
				Matcher matcher = Pattern.compile(".*filename=(.*)").matcher(
						content_disposition);
				if (matcher.find())
					fileName = matcher.group(1);
			} catch (Exception e) {
				fileName = UUID.randomUUID().toString() + ".tmp"; // 默认名
			}
		}
		return fileName;
	}
}
