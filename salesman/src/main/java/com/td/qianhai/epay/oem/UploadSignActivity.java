package com.td.qianhai.epay.oem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.oem.views.UploadWriteDialog;
import com.td.qianhai.epay.oem.views.dialog.interfaces.DialogListener;
import com.td.qianhai.epay.utils.BitmapUtil;

public class UploadSignActivity extends BaseActivity implements OnClickListener {

	private ImageView ivShow;
	private Button btnUpload, btnSign;
	private int height, width;
	private Bitmap mSignBitmap;
	private String signPath, signStr;
	private String custId, psamId, pcsimId;
	private String balance, bankCode, name, password, track2, track3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.upload_sign);
		AppContext.getInstance().addActivity(this);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		width = metrics.widthPixels; // 得到屏幕的宽度
		height = metrics.heightPixels; // 得到屏幕的长度

		initView();
	}

	private void initView() {
		Bundle bundle = this.getIntent().getExtras();
		balance = bundle.getString("balance");
		bankCode = bundle.getString("bankCode");
		name = bundle.getString("name");
		password = bundle.getString("password");
		track2 = bundle.getString("track2");
		track3 = bundle.getString("track3");

//		custId = ((AppContext) this.getApplication()).getCustId();
		custId = MyCacheUtil.getshared(this).getString("Mobile", "");
		psamId = ((AppContext) this.getApplication()).getPsamId();
		pcsimId = "11111111";

		ivShow = (ImageView) findViewById(R.id.iv_upload_sign_show);
		findViewById(R.id.left_menu).setOnClickListener(this);
		btnSign = (Button) findViewById(R.id.btn_upload_sign_sign);
		btnUpload = (Button) findViewById(R.id.btn_upload_sign_upload);
		btnSign.setOnClickListener(this);
		btnUpload.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_upload_sign_sign:
			// if(btnSign.getText().equals("签名")){
			createWriteDialog();
			// }else {

			// }
			break;
		case R.id.btn_upload_sign_upload:
			receive();
			// receives();
			break;
		case R.id.left_menu:
			backLast();
			break;
		default:
			break;
		}

	}

	private void backLast() {
		finish();
	}

	private void createWriteDialog() {

		UploadWriteDialog dialog = new UploadWriteDialog(
				UploadSignActivity.this, height, width, new DialogListener() {

					@Override
					public void refreshActivity(Object object) {
						mSignBitmap = (Bitmap) object;
						signStr = BitmapUtil.Bitmap2String(mSignBitmap);
						/*
						 * signPath = createFile(); BitmapFactory.Options
						 * options = new BitmapFactory.Options();
						 * options.inSampleSize = 15; options.inTempStorage =
						 * new byte[5 * 1024]; Bitmap zoombm =
						 * BitmapFactory.decodeFile(signPath, options);
						 */
						ivShow.setImageBitmap(mSignBitmap);

					}
				});
		dialog.show();

	}

	/**
	 * 创建手写签名文件
	 * 
	 * @return
	 */
	private String createFile() {
		ByteArrayOutputStream baos = null;
		String _path = null;
		try {
			String sign_dir = Environment.getExternalStorageDirectory()
					+ File.separator;
			_path = sign_dir + System.currentTimeMillis() + ".jpg";
			baos = new ByteArrayOutputStream();
			mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			byte[] photoBytes = baos.toByteArray();
			if (photoBytes != null) {
				new FileOutputStream(new File(_path)).write(photoBytes);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return _path;
	}

	private void receives() {
		if (signStr == null) {
			ToastCustom.showMessage(UploadSignActivity.this, "请您先手写电子签名",
					Toast.LENGTH_SHORT);
			return;
		}
		BossReceiveTask task = new BossReceiveTask();
		task.execute(HttpUrls.BOSS_RECEIVE + "", custId, psamId, pcsimId,
				bankCode, track2, track3, password, signStr, "jpg", balance,
				psamId);

	}

	class BossReceiveTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		private AlertDialog dialog;

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			AlertDialog.Builder builder = new Builder(UploadSignActivity.this);
			dialog = builder.create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			Window mWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = mWindow.getAttributes();
			lp.dimAmount = 0f;
			dialog.setContentView(R.layout.load);
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3],
					params[4], params[5], params[6], params[7], params[8],
					params[9], params[10], params[11] };
			return NetCommunicate.getPosp(HttpUrls.BOSS_RECEIVE, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			dialog.dismiss();
			if (result != null) {
				if (Entity.STATE_OK.equals(result.get(Entity.RSPCOD))) {
					ToastCustom.showMessage(UploadSignActivity.this, result
							.get(Entity.RSPMSG).toString(), Toast.LENGTH_SHORT);

				} else {
					ToastCustom.showMessage(UploadSignActivity.this, result
							.get(Entity.RSPMSG).toString(), Toast.LENGTH_SHORT);
				}
				confirm(result.get(Entity.RSPMSG).toString());
			}
			super.onPostExecute(result);
		}

	}

	private void confirm(String msg) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage(msg);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				Intent it = new Intent(UploadSignActivity.this,
						MenuActivity.class);
				startActivity(it);
			}
		});

		builder.create().show();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			backLast();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void receive() {
		if (signStr == null) {
			ToastCustom.showMessage(UploadSignActivity.this, "请您先手写电子签名",
					Toast.LENGTH_SHORT);
			return;
		}
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("您确定要收款?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				BossReceiveTask task = new BossReceiveTask();
				task.execute(HttpUrls.BOSS_RECEIVE + "", custId, psamId,
						pcsimId, bankCode, track2, track3, password, signStr,
						"jpg", balance, psamId);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();

			}
		});
		builder.create().show();
	}

}
