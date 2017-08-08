package com.shareshenghuo.app.user;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.app.CityLifeApp;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.request.SubmitRealSenseRequset;
import com.shareshenghuo.app.user.network.response.AutResponse;
import com.shareshenghuo.app.user.network.response.FileUploadResponse;
import com.shareshenghuo.app.user.network.response.SenseImageResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.ProgressDialogUtil;
import com.shareshenghuo.app.user.util.T;
import com.shareshenghuo.app.user.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.user.widget.dialog.TwoButtonDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.sensetime.liveness.util.Constants;
import com.sensetime.liveness.util.Utils;
import com.sensetime.stlivenesslibrary.ui.LivenessActivity;

public class RealnameSenseActivity extends BaseTopActivity{
	
	private TextView real_pointimg,real_pointimg1,real_pointimg2,tv_pro;
	private TwoButtonDialog downloadDialog;
	private Button llWalletRecharge;
	public static String storageFolder;
	private static final int START_DETECT_REQUEST_CODE = 8;
	private StringBuilder bff = new StringBuilder();
	private List<String> imaglist = new ArrayList<String>();
	private int tagnum = 0;
	private String idcard = "" ,idname = "";
	private String ImageId;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.realname_sense_activity);
		
		Intent it = getIntent();
		idcard = it.getStringExtra("idcard");
		idname = it.getStringExtra("idname");
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		initTopBar("实名认证");
		tv_pro = getView(R.id.tv_pro);
		tv_pro.setText("离实名认证成功还差一步");
		real_pointimg = getView(R.id.real_pointimg);
		real_pointimg1 = getView(R.id.real_pointimg1);
		real_pointimg2 = getView(R.id.real_pointimg2);
		real_pointimg.setBackgroundResource(R.drawable.bg_arc_blue);
		real_pointimg.setTextColor(getResources().getColor(R.color.white));
		real_pointimg1.setTextColor(getResources().getColor(R.color.white));
//		real_pointimg2.setTextColor(getResources().getColor(R.color.white));
		real_pointimg1.setBackgroundResource(R.drawable.bg_arc_blue);
//		real_pointimg2.setBackgroundResource(R.drawable.bg_arc_blue);
		findViewById(R.id.tvs1).setBackgroundResource(R.color.bg_top_bar);
//		findViewById(R.id.tvs2).setBackgroundResource(R.color.bg_top_bar);
		llWalletRecharge = getView(R.id.llWalletRecharge);
		storageFolder = Utils.storageFolder;
		llWalletRecharge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				if(tagnum>=3){
//					initDialog("肖像认证次数超限", "确定", "");
//					return;
//				}
				startLiveness();
//				tagnum++;
			}
		});
		
		llTopBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initDialog("确定退出肖像认证?", "取消", "确定");
			}
		});
		
	}
	
	
	public void upPhoto(File f, final int i) {
//		try {
            // 压缩图片
//            String compressPath = PictureUtil.compressImage(this, f.getPath(), f.getName(), 65);
//            f = new File(f);
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//		if(i==0){
			ProgressDialogUtil.showProgressDlg(this, "请稍候");
//		}
		
		RequestParams params =  new RequestParams();
		params.addBodyParameter("business_type", UserInfoManager.getUserId(this)+"");
		params.addBodyParameter("file", f);
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPLOAD_FILE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				T.showNetworkError(RealnameSenseActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
					ProgressDialogUtil.dismissProgressDlg();
				
				if(resp.statusCode==200 && resp.result!=null) {
//					ReturnImgResponse bean = new Gson().fromJson(resp.result, ReturnImgResponse.class);
					FileUploadResponse bean = new Gson().fromJson(resp.result, FileUploadResponse.class);
//					T.showShort(RealnameSenseActivity.this, bean.result_desc);
					if(Api.SUCCEED == bean.result_code){
//						imaglist.add(bean.data.get(0));
//						if(imaglist.size()==3){
//							ProgressDialogUtil.dismissProgressDlg();
//							for (int j = 0; j < imaglist.size(); j++) {
//								bff.append(imaglist.get(j)+",");
//							}
//							loadSense(bean.data.get(0));
//						}
					}else{
//						ProgressDialogUtil.dismissProgressDlg();
					}
				}
			}
		});
	}
	
	private void loadSense(String string) {
		// TODO Auto-generated method stub
		
		ProgressDialogUtil.showProgressDlg(this, "请稍后");
		ProgressDialogUtil.setCancelable(false);
		SubmitRealSenseRequset req = new SubmitRealSenseRequset();
		req.user_id = UserInfoManager.getUserInfo(this).id+"";
		req.action_urls =string;
		req.person_no = idcard;
		req.real_name = idname;
//		Log.e("", ""+req.toString());
//		EncryptionUtil.getEncryptionstring(req.user_id);
//		req.isEncrypt = "1";
//		req.info = EncryptionUtil.getEncryptionstring(req.user_id);
		
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.FACEVERIFICATION, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
					T.showNetworkError(RealnameSenseActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
//				Log.e("", " = = = = = = = "+resp.result);
			
				AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
				if(Api.SUCCEED==bean.result_code) {
					if(bean.data.RSPCOD!=null&&bean.data.RSPCOD.equals("000000")){
//						T.showShort(RealnameSenseActivity.this,  bean.data.RSPMSG);
						CityLifeApp.getInstance().exit();
						Intent it  = new Intent(RealnameSenseActivity.this,Realname4Activity.class);
						it.putExtra("tag", "1");
						it.putExtra("msg", bean.data.RSPMSG);
						startActivity(it);
						finish();
					}else{
						CityLifeApp.getInstance().exit();
						Intent it  = new Intent(RealnameSenseActivity.this,Realname4Activity.class);
						it.putExtra("tag", "0");
						it.putExtra("msg", bean.data.RSPMSG);
						startActivity(it);
						finish();
//						T.showShort(RealnameSenseActivity.this,  bean.data.RSPMSG);
					}
				}
			}
		});
	}
	
	/**
	 * 监听返回按钮
	 */
//	@Override
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			initDialog("确定退出肖像认证?", "取消", "确定");
		}
		return true;
	}
	
	private void initDialog(String content,String left,String right) {
	// TODO Auto-generated method stub
	downloadDialog = new TwoButtonDialog(RealnameSenseActivity.this, R.style.CustomDialog,
			"尊敬的会员", content, left, right,true,new OnMyDialogClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					switch (v.getId()) {
					case R.id.Button_OK:
						downloadDialog.dismiss();
						break;
					case R.id.Button_cancel:
						finish();
						downloadDialog.dismiss();
					default:
						break;
					}
				}
			});
		downloadDialog.show();
	}
	
	private void startLiveness() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String outputType = sp.getString(Constants.OUTPUTTYPE, Constants.MULTIIMAGE);
		String complexity = sp.getString(Constants.COMPLEXITY, Constants.NORMAL);
		boolean soundNotice = sp.getBoolean(Constants.NOTICE, true);
		SharedPreferences detectSp = getApplicationContext().getSharedPreferences(Constants.DETECTLIST, MODE_PRIVATE);
		String detectList = detectSp.getString(Constants.DETECTLIST, Constants.DEFAULTDETECTLIST);
		Intent intent = new Intent();
		intent.setClass(RealnameSenseActivity.this, LivenessActivity.class);
		/**
		 * EXTRA_MOTION_SEQUENCE 动作检测序列配置，支持四种检测动作， BLINK(眨眼), MOUTH（张嘴）, NOD（点头）, YAW（摇头）, 各个动作以空格隔开。 推荐第一个动作为BLINK.
		 * 默认配置为"BLINK MOUTH NOD YAW"
		 */
		intent.putExtra(LivenessActivity.EXTRA_MOTION_SEQUENCE, detectList);
		/**
		 * SOUND_NOTICE 配置, 传入的soundNotice为boolean值，true为打开, false为关闭, 默认为true.
		 */
		intent.putExtra(LivenessActivity.SOUND_NOTICE, soundNotice);
		/**
		 * OUTPUT_TYPE 配置, 传入的outputType类型为singleImg （单图）或 multiImg （多图）, 默认为multiImg.
		 */
		intent.putExtra(LivenessActivity.OUTPUT_TYPE, outputType);
		/**
		 * COMPLEXITY 配置, 传入的complexity类型, 支持四种难度:easy, normal, hard, hell.默认为normal.
		 */
		intent.putExtra(LivenessActivity.COMPLEXITY, complexity);
		File livenessFolder = new File(storageFolder);
		if (!livenessFolder.exists()) {
			livenessFolder.mkdirs();
		}
		// 开始检测之前请删除文件夹下保留的文件
		deleteFiles(storageFolder);
		/**
		 * EXTRA_RESULT_PATH 配置， 传入的storageFolder为sdcard下目录, 为了保存检测结果文件, 传入之前请确保该文件夹存在。
		 */
		intent.putExtra(LivenessActivity.EXTRA_RESULT_PATH, storageFolder);
		startActivityForResult(intent, START_DETECT_REQUEST_CODE);
	}
	
	public static void deleteFiles(String folderPath) {
		File dir = new File(folderPath);
		if (dir == null || !dir.exists() || !dir.isDirectory() || dir.listFiles() == null)
			return;
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==8){
			switch (resultCode) {
			case RESULT_OK:
				
				List<String> mDatas = Utils.getImageListName(storageFolder);
				if(mDatas!=null&&mDatas.size()>0){
//					for (int i = 0; i < mDatas.size(); i++) {
//						upPhoto(new File(storageFolder+mDatas.get(0)), 0);
						upimage(new File(storageFolder+mDatas.get(0)));
//					}
				}
				

				
				
				Bundle b = data.getExtras();
				if (Utils.getImageListName(storageFolder) != null && Utils.getImageListName(storageFolder).size() > 0) {
					if (Utils.getLoacalBitmap(storageFolder + Utils.getImageListName(storageFolder).get(0)) != null) {
						Drawable drawable = new BitmapDrawable(Utils.getLoacalBitmap(storageFolder
										+ Utils.getImageListName(storageFolder).get(0)));
//						resultLayout.setBackgroundDrawable(drawable);
//						moreImageView.setBackgroundDrawable(new BitmapDrawable(Utils.getLoacalBitmap(storageFolder
//										+ Utils.getImageListName(storageFolder).get(0))));
					}
				} else {
//					moreImageView.setVisibility(View.INVISIBLE);
				}
//				moreImageView.setText(Utils.getImageCountInPath(storageFolder)
//								+ getResources().getString(R.string.zhang));
//				tvpro.setText(getResources().getString(R.string.pass_liveness));

				break;
			case RESULT_CANCELED:
//				tvpro.setText(getResources().getString(R.string.cancel_liveness));
				break;
			case RESULT_FIRST_USER:
//				tvpro.setText(getResources().getString(R.string.save_liveness_result_fail));
				break;
			default:
//				tvpro.setText(getResources().getString(R.string.failed_init_sdk));
				break;
			}
			return;
		}
	}
	
	private void upimage(File f) {
		// TODO Auto-generated method stub
		ProgressDialogUtil.showProgressDlg(this, "图片上传中");
		RequestParams params =  new RequestParams();
		params.addBodyParameter("api_id", "b329ed69d52b4ff396622f11f1dfd4b8");
		params.addBodyParameter("api_secret", "ca54559bd0b741d99b8e831c5a7ae277");
		params.addBodyParameter("file", f);
		new HttpUtils().send(HttpMethod.POST, Api.STUPLOAD_IMAGE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
//				T.showNetworkError(RealnameSenseActivity.this);
//				startLiveness();
				T.showShort(RealnameSenseActivity.this, "上传失败,请重试");
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				if(resp.statusCode==200 && resp.result!=null) {
					Log.e("", ""+resp.result);
//					ReturnImgResponse bean = new Gson().fromJson(resp.result, ReturnImgResponse.class);
					SenseImageResponse bean = new Gson().fromJson(resp.result, SenseImageResponse.class);
					if(bean.image_id!=null){
						ImageId = bean.image_id;
						loadSense();
					}else{
//						startLiveness();
						T.showShort(RealnameSenseActivity.this, "上传失败,请重试");
					}
//					T.showShort(Realname3Activity.this, bean.result_desc);
//					if(Api.SUCCEED == bean.result_code){
//						
//					}
				}
			}
		});
	}
	
	private void loadSense() {
		// TODO Auto-generated method stub
		
		if(TextUtils.isEmpty(ImageId)){
			startLiveness();
			T.showShort(RealnameSenseActivity.this, "上传失败,请重试");
			return;
		}
		
		
		ProgressDialogUtil.showProgressDlg(this, "请稍后");
		ProgressDialogUtil.setCancelable(false);
		SubmitRealSenseRequset req = new SubmitRealSenseRequset();
		req.user_id = UserInfoManager.getUserInfo(this).id+"";
		req.action_urls ="";
		req.self_image_id = ImageId;
		req.person_no = idcard;
		req.real_name = idname;
		Log.e("", ""+req.toString());
//		EncryptionUtil.getEncryptionstring(req.user_id);
//		req.isEncrypt = "1";
//		req.info = EncryptionUtil.getEncryptionstring(req.user_id);
		
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.FACEVERIFICATION1, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
					T.showNetworkError(RealnameSenseActivity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
//				Log.e("", " = = = = = = = "+resp.result);
			
				AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
				if(Api.SUCCEED==bean.result_code) {
					if(bean.data.RSPCOD!=null&&bean.data.RSPCOD.equals("000000")){
//						T.showShort(RealnameSenseActivity.this,  bean.data.RSPMSG);
						CityLifeApp.getInstance().exit();
						Intent it  = new Intent(RealnameSenseActivity.this,Realname4Activity.class);
						it.putExtra("tag", "1");
						it.putExtra("msg", bean.data.RSPMSG);
						startActivity(it);
						finish();
					}else{
						CityLifeApp.getInstance().exit();
						Intent it  = new Intent(RealnameSenseActivity.this,Realname4Activity.class);
						it.putExtra("tag", "0");
						it.putExtra("msg", bean.data.RSPMSG);
						startActivity(it);
						finish();
//						T.showShort(RealnameSenseActivity.this,  bean.data.RSPMSG);
					}
				}
			}
		});
	}
}
