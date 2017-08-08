package com.td.qianhai.epay.oem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.adapter.CardListAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.BankListBean;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.oem.views.dialog.ChooseDialog;

/**
 * 未使用
 */
public class MyBankCardAvtivity extends BaseActivity {
	
	private ListView listview;
	
	private TextView delete_propty;
	
	private LinearLayout add_creditcard;
	
	private ArrayList<HashMap<String, Object>> mlist;//
	
	private String mobile;

	private CardListAdapter adapter;
	
	private ChooseDialog chooseDialog;
	
	private String balace;
	
	private String clslogno,names,mobiles,cardid;
	
	private String attStr,sts,idcards="",idnames="";
	
	private String idcardpic;
	
	private File cardPicFile;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_bank_card);
		AppContext.getInstance().addActivity(this);
//		mobile = ((AppContext)getApplication()).getMobile();
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		attStr =  MyCacheUtil.getshared(this).getString("MERSTS", "");
		sts = MyCacheUtil.getshared(this).getString("STS", "");
		Intent it = getIntent();
		balace = it.getStringExtra("balance");
		
		initview();
		
		initchargelist();
		
		mlist = new ArrayList<HashMap<String,Object>>();
		adapter = new CardListAdapter(MyBankCardAvtivity.this, mlist, 0);
		listview.setAdapter(adapter);

	}

	private void initview() {
		
		idcardpic = "701734" + "_" + getStringDateMerge() + "_"
				+ "B.jpg";
		
		((TextView) findViewById(R.id.tv_title_contre)).setText("我的信用卡");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		
		listview = (ListView) findViewById(R.id.mycardlist);
		delete_propty = (TextView) findViewById(R.id.delete_proptys);
		add_creditcard = (LinearLayout) findViewById(R.id.add_bankcard);
		delete_propty.setVisibility(View.GONE);
		
		
		add_creditcard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(MyCacheUtil.getshared(MyBankCardAvtivity.this).getString("Txnsts", "").equals("1")){
					takePicture(200);
				}else{
					Intent it = new Intent(MyBankCardAvtivity.this,EpayActivity.class);
					it.putExtra("balance", balace);
					it.putExtra("idcards", idcards);
					it.putExtra("idnames", idnames);
					startActivity(it);
					finish();
				}
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				// TODO Auto-generated method stub
				
				String cardtel = null;
				String c_num = null;
				String c_name = null;
				String c_card = null;
				String banknum = null;
				String b_year = null;
				String b_month = null;
				String cvv = null;
				String bakname = null;
				
				if(mlist.get((int)id).get("CARDTEL")!=null){
					 cardtel = mlist.get((int)id).get("CARDTEL").toString();
					mobiles = cardtel;
				}else{
					return;
				}
				if(mlist.get((int)id).get("CREDCODE")!=null){
					  c_num= mlist.get((int)id).get("CREDCODE").toString();
				}else{
					return;
				}
				if(mlist.get((int)id).get("CARDNAME")!=null){
					 c_name = mlist.get((int)id).get("CARDNAME").toString();
					names = c_name;
				}else{
					return;
				}
				if(mlist.get((int)id).get("CARDCODE")!=null){
					 c_card = mlist.get((int)id).get("CARDCODE").toString();
				}else{
					return;
				}
				if(mlist.get((int)id).get("FRPID")!=null){
					 banknum = mlist.get((int)id).get("FRPID").toString();
					cardid = banknum;
				}else{
					return;
				}
				if(mlist.get((int)id).get("EXPIREYEAR")!=null){
					 b_year = mlist.get((int)id).get("EXPIREYEAR").toString();
				}else{
					return;
				}
				if(mlist.get((int)id).get("EXPIREMONTH")!=null){
					 b_month = mlist.get((int)id).get("EXPIREMONTH").toString();
				}else{
					return;
				}
				if(mlist.get((int)id).get("CVV")!=null){
					 cvv = mlist.get((int)id).get("CVV").toString();
				}else{
					return;
				}
				if(mlist.get((int)id).get("ISSUER")!=null){
					 bakname = mlist.get((int)id).get("ISSUER").toString();
				}else{
					return;
				}

				OrderTask1 otask = new OrderTask1();
				
				otask.execute(HttpUrls.EPAY + "", mobile ,"02",String.valueOf((int)(Double.parseDouble(balace) *100)),cardtel,"IDCARD",c_num,c_name,c_card,banknum
						,b_year,b_month,cvv,bakname,"3","0","");
				
			}
		});
	}
	
	
	
	
	
	private void initchargelist() {	
		
		mlist = new ArrayList<HashMap<String,Object>>();

		showLoadingDialog("正在查询中...");
		new Thread(run).start();
		
	}
	Runnable run = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			String[] values = { String.valueOf(HttpUrls.BANKCARDLIST),mobile};
			BankListBean entitys = NetCommunicate.getBankList(
					HttpUrls.BANKCARDLIST, values);

			Message msg = new Message();
			if (entitys != null) {
				list = entitys.list;
						mlist.addAll(list);
					if(mlist.size()<=0||mlist==null){
						
						msg.what = 2;
					}else{
						msg.what = 1;
						idcards = mlist.get(0).get("CREDCODE").toString();
						idnames = mlist.get(0).get("CARDNAME").toString();
					}

			} else {
				loadingDialogWhole.dismiss();
				msg.what = 3;
			}
			loadingDialogWhole.dismiss();
			handler.sendMessage(msg);
		}
	};
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				
				adapter.notifyDataSetChanged();
//				delete_propty.setVisibility(View.VISIBLE);
				
				break;
			case 2:
//				delete_propty.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
				break;
			case 3:
				delete_propty.setVisibility(View.GONE);
				Toast.makeText(getApplicationContext(),"网络异常",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(MyBankCardAvtivity.this,
//						"网络异常");
				break;
			default:
				break;
			}
		};
	};
	
	class OrderTask1 extends AsyncTask<String, Integer, HashMap<String, Object>> {
		private AlertDialog dialog;

		@Override
		protected void onPreExecute() {
			AlertDialog.Builder builder = new Builder(MyBankCardAvtivity.this);
			builder.setCancelable(false);
			dialog = builder.create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			Window mWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = mWindow.getAttributes();
			lp.dimAmount = 0f;
			dialog.setContentView(R.layout.load);
			super.onPreExecute();
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3],
					params[4], params[5], params[6], params[7],params[8],params[9],params[10],params[11],params[12],params[13],params[14],params[15],params[16]};
			return NetCommunicate.getPay(HttpUrls.EPAY, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			dialog.dismiss();
			if (result != null) {
				if (Entity.STATE_OK.equals(result.get(Entity.RSPCOD))) {
					
					clslogno = result.get("CLSLOGNO").toString();
//					verifycode = result.get("VERIFYCODE").toString();
//					Intent intent = new Intent(EpayActivity.this,
//							OnlineWeb.class);
//					intent.putExtra("urlStr", result.get("REURL").toString());
//					intent.putExtra("titleStr", "充值");
//					startActivity(intent);
//					if(result.get("ISCHECK").equals("0")){
//						ToastCustom.showMessage(EpayActivity.this,
//								result.get(Entity.RSPMSG).toString(),
//								Toast.LENGTH_SHORT);
//					}else{
//						
////						inittime();
//						
//					}
					Intent it = new Intent(MyBankCardAvtivity.this,LastOderAvtivity.class);
					int imgid = 0;
					if (cardid.equals("CMBCHINACREDIT")) {
						imgid = R.drawable.ps_cmb;
					}else if(cardid.equals("ABCCREDIT")) {
						imgid = R.drawable.ps_abc;
					}else if(cardid.equals("BCCBCREDIT")){
						imgid =R.drawable.ps_bjb;
					}else if(cardid.equals("BOCCREDIT")){
						imgid =R.drawable.ps_boc;
					}else if(cardid.equals("CCBCREDIT")){
						imgid =R.drawable.ps_ccb;
					}else if(cardid.equals("EVERBRIGHTCREDIT")){
						imgid =R.drawable.ps_cebb;
					}else if(cardid.equals("CIBCREDIT")){
						imgid =R.drawable.ps_cib;
					}else if(cardid.equals("ECITICCREDIT")){
						imgid =R.drawable.ps_citic;
					}else if(cardid.equals("CMBCCREDIT")){
						imgid =R.drawable.ps_cmbc;
					}else if(cardid.equals("BOCOCREDIT")){
						imgid =R.drawable.ps_comm;
					}else if(cardid.equals("HXBCREDIT")){
						imgid =R.drawable.ps_hxb;
					}else if(cardid.equals("GDBCREDIT")){
						imgid =R.drawable.ps_gdb;
					}else if(cardid.equals("PSBCCREDIT")){
						imgid =R.drawable.ps_psbc;
					}else if(cardid.equals("ICBCCREDIT")){
						imgid =R.drawable.ps_icbc;
					}else if(cardid.equals("PINGANCREDIT")){
						imgid =R.drawable.ps_spa;
					}else if(cardid.equals("SPDBCREDIT")){
						imgid =R.drawable.ps_spdb;
					}else if(cardid.equals("BSBCREDIT")){
						imgid =R.drawable.ps_bsb;
					}else if(cardid.equals("BOSHCREDIT")){
						imgid =R.drawable.ps_sh;
					}else{
						imgid =R.drawable.ps_unionpay;
					}
					
//					String mobiles = e_pay2.getText().toString();;
//					String names = e_pay5.getText().toString();
					it.putExtra("balance", balace);
					it.putExtra("mobile", mobiles);
					it.putExtra("name", names);
					it.putExtra("imgid", imgid);
					it.putExtra("clslogno", clslogno);
					startActivity(it);
					finish();
				} else {
					ToastCustom.showMessage(MyBankCardAvtivity.this,
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT);
				}
			}
			super.onPostExecute(result);
		}
	}
	
	
	
	public String getStringDateMerge() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	private void takePicture(int code) {

		File dir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/DCIM/");// 设置临时文件的存放目录
		if (!dir.exists()) {
			dir.mkdir();
		}
		// 系统相机
		// Intent intent = new Intent(
		// android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//		if (code == FRONT_CODE) {
//			localTempImgFileName = idcardpic;
//
//			Log.e("", "localTempImgFileName1 = = =" + localTempImgFileName);
//		} else if (code == SCENS_CODE) {
//
//			localTempImgFileName = custpic;
//			Log.e("", "localTempImgFileName2 = = =" + localTempImgFileName);
//		} else {
//			localTempImgFileName = bankpic;
//			Log.e("", "localTempImgFileName2 = = =" + localTempImgFileName);
//		}
		File f = new File(dir, idcardpic);
		Log.e("fileNam", dir.getAbsolutePath() + idcardpic);
		Uri u = Uri.fromFile(f);
		// intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
		// intent.setDataAndType(u, "image/*"); // 格式
		// intent.putExtra("crop", "true"); // 发送裁剪信号
		// intent.putExtra("aspectX", 4); // X方向上的比例
		// intent.putExtra("aspectY", 3); // Y方向上的比例
		// intent.putExtra("outputX", 800f); // 裁剪区的宽
		// intent.putExtra("outputY", 400f); // 裁剪区的高
		// intent.putExtra("scale", false); // 是否保留比例
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, u); // 将URI指向相应的file:///
		// intent.putExtra("return-data", false); // 是否将数据保留在Bitmap中返回
		// intent.putExtra("outputFormat",
		// Bitmap.CompressFormat.JPEG.toString());
		// intent.putExtra("noFaceDetection", true); // no face detection
		// intent.putExtra("circleCrop", false); // 圆形裁剪区域
		// startActivityForResult(intent, code);

		Intent intent = new Intent(this, CameraActivity2.class);
		Bundle bundle = new Bundle();
		bundle.putString("fileName", idcardpic);
		bundle.putString("tag", code + "");
		intent.putExtras(bundle);
		NewRealNameAuthenticationActivity.iscameras = false;
		startActivityForResult(intent, code);
		
	}
	
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent data) {
		super.onActivityResult(arg0, arg1, data);

		if (arg1 != RESULT_OK) {
			return;
		} else if (arg0 == 200 && arg1 == RESULT_OK) {
			
//			CameraActivity2.isdown = false;
			
			File dir = getBitmap();
			
			
			RealNameAuthTask task = new RealNameAuthTask();

			task.execute(HttpUrls.UPLODPIC+"", idcardpic);
		}
	}
	
	private File getBitmap() {
		File dir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/DCIM/" + idcardpic);// 设置存放目录
		Log.e("", "      = = = = = = =dir = = = = =" + dir);
		Log.e("", "      = = = = = = =filenames = = = = ="
				+ idcardpic);
		// File f = new File(dir.getAbsoluteFile(), localTempImgFileName);
		if (dir.isFile()) {
			Log.e("", "有文件");
			Bitmap bitmap = getimage(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/DCIM/" + idcardpic);
			saveBitmap2file(bitmap, idcardpic);
		} else {
			Log.e("", "没有文件");
		}
		BitmapFactory.Options opts = new BitmapFactory.Options();// 获取缩略图显示到屏幕
		opts.inSampleSize = 4;
		/* 下面两个字段需要组合使用 */
		// opts.inJustDecodeBounds = false;
		// opts.inPurgeable = true;
		// opts.inInputShareable = true;
		Bitmap cbitmap = BitmapFactory.decodeFile(dir.getAbsolutePath());
		
		cardPicFile = dir;
		return dir;
	}
	
	private Bitmap getimage(String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 600f;// 这里设置高度为800f
		float ww = 400f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = 4;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		// return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
		return bitmap;
	}
	
	private static boolean saveBitmap2file(Bitmap bmp, String filename) {
		CompressFormat format = Bitmap.CompressFormat.JPEG;
		int quality = 100;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/DCIM/"
					+ filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bmp.compress(format, quality, stream);
	}
	
	
	/**
	 * 充值传银行卡
	 * 
	 * 
	 */
	class RealNameAuthTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog("正在上传照片中。。。");
			super.onPreExecute();
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1]};
			File[] files = { cardPicFile };
			return NetCommunicate.getUpload(HttpUrls.UPLODPIC,
					// 198110,
					values, files);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					Toast.makeText(getApplicationContext(),result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(
//							MyBankCardAvtivity.this,
//							result.get(Entity.RSPMSG).toString());
					Intent it = new Intent(MyBankCardAvtivity.this,EpayActivity.class);
					it.putExtra("balance", balace);
					if(result.get("IMGPATH")!=null){
						it.putExtra("url", result.get("IMGPATH").toString());
					}
					startActivity(it);
					finish();

				} else {
					Intent it = new Intent(MyBankCardAvtivity.this,EpayActivity.class);
					it.putExtra("balance", balace);
					if(result.get("IMGPATH")!=null){
						it.putExtra("url", result.get("IMGPATH").toString());
					}
					startActivity(it);
					finish();
				}
			}
			super.onPostExecute(result);
		}
	}
}
