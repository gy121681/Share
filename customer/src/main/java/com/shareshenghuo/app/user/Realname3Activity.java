package com.shareshenghuo.app.user;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.app.CityLifeApp;
import com.shareshenghuo.app.user.manager.CityManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.request.SubmitRealRequset;
import com.shareshenghuo.app.user.network.response.AutResponse;
import com.shareshenghuo.app.user.network.response.FileUploadResponse;
import com.shareshenghuo.app.user.network.response.SenseImageResponse;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.senceidcard.IDCardBothActivity;
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
import com.sensetime.card.CardActivity;
import com.sensetime.idcard.IDCard;
import com.sensetime.idcard.IDCardActivity;
import com.sensetime.idcard.IDCardRecognizer;
import com.sensetime.liveness.util.Utils;

public class Realname3Activity  extends BaseTopActivity implements OnClickListener{
	private TextView real_pointimg,real_pointimg1,real_pointimg2,tvpro,tv_mechanism;
	private Button btn1,btn2,btn;
	private int EXAMPLE_REQUEST_CODE = 1;
	private boolean only_name_id = false;
	private boolean doubleSide = false;
	private int scanRectOffset;
	private Bitmap imageFront = null;
	private Bitmap imageBack = null;
	private IDCard idcard;
	private LinearLayout id_info;
	private RelativeLayout id_info1,mResult;
	private ImageView idcardImageView, idcardImageView1;//idcardImageName, idcardImageNumber, iv_face, tvBack;
	private TextView  tvNumber,tvAddress;//tvSex, tvRace, tvBirth, tvAddress, tvNumber;	
	private EditText tvName;
	public static String storageFolder;
	private static final int START_DETECT_REQUEST_CODE = 8;
	private TwoButtonDialog downloadDialog;
	private String sex = "",national = "",address = "",idcardimg = "",idcardbackimg = "",validity = "";;
	private CityManager cityManager;
	private ScrollView scroll;
	private ImageView pro_img,re_camera_btn;
	private String ImageId;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.realname3_layout);
		CityLifeApp.getInstance().addActivity(this);
		cityManager = CityManager.getInstance(this);
		initview();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
//		btn.setVisibility(View.VISIBLE);
//		btn1.setVisibility(View.VISIBLE);
	}

	private void initview() {
		initTopBar("实名认证");
		real_pointimg = getView(R.id.real_pointimg);
		real_pointimg1 = getView(R.id.real_pointimg1);
		real_pointimg2 = getView(R.id.real_pointimg2);
		scroll = getView(R.id.scroll);
		real_pointimg.setBackgroundResource(R.drawable.bg_arc_blue);
		real_pointimg.setTextColor(getResources().getColor(R.color.white));
//		real_pointimg1.setBackgroundResource(R.drawable.newreal_point);
//		real_pointimg2.setBackgroundResource(R.drawable.newreal_point);
//		findViewById(R.id.tvs2).setBackgroundResource(R.color.blue);
		tvpro = getView(R.id.tvpro);
//		mResult = getView(R.id.rl_result_facade);
		idcardImageView = (ImageView) findViewById(R.id.iv_facade);
		pro_img =  getView(R.id.pro_img);
		
//		AnimationUtil.JAnima(
//				Realname3Activity.this, pro_img);
		idcardImageView.setOnClickListener(this);
		idcardImageView1 = (ImageView) findViewById(R.id.iv_facade1);
		idcardImageView1.setOnClickListener(this);
//		idcardImageName = (ImageView) findViewById(R.id.img_name);
//		idcardImageNumber = (ImageView) findViewById(R.id.img_number);
		tvName = getView(R.id.tv_name);
		tvAddress = (TextView) findViewById(R.id.tv_address);
		tvNumber = (TextView) findViewById(R.id.tv_number);
		id_info = getView(R.id.id_info);
		id_info1 = getView(R.id.id_info1);
//		mBtnSet = getView(R.id.layout_switch);
//		iv_face = (ImageView) findViewById(R.id.face);
//		ll_next = getView(R.id.ll_next);
		btn1 = getView(R.id.llWalletRecharge2);
		btn = getView(R.id.llWalletRecharge1);
		btn2 = getView(R.id.btn2);
		tv_mechanism = getView(R.id.tv_mechanism);
		btn1.setOnClickListener(this);
		btn.setOnClickListener(this);
		btn2.setOnClickListener(this);
		storageFolder = Utils.storageFolder;
		scanRectOffset = 0;	
		
		llTopBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
					initDialog("确定退出身份认证?", "取消", "确定");
			}
		});
		re_camera_btn = getView(R.id.re_camera_btn);
		re_camera_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initcamera(true);
			}
		});
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		deleteFiles(storageFolder);
	}
	
	private void initcamera(boolean tag) {
		// TODO Auto-generated method stub
		Intent scanIntent = new Intent();
		if(!tag){
			
			scanIntent.setClass(Realname3Activity.this, IDCardBothActivity.class);
			only_name_id = false;
			scanIntent.putExtra(IDCardActivity.EXTRA_RECOGNIZE_MODE, IDCardRecognizer.Mode.BACK);
			scanIntent.putExtra(CardActivity.EXTRA_SCAN_TIPS, "请将身份证反面放入扫描框内");
			only_name_id = false;
		}else{
			scanIntent.setClass(Realname3Activity.this,MyIDCardActivity.class);
			doubleSide = false;
//			mBtnSet.setVisibility(View.GONE);
			scanIntent.putExtra(CardActivity.EXTRA_BACK_DRAWABLE_ID, R.drawable.scan_back);//scan_back_user
			scanIntent.putExtra(IDCardActivity.EXTRA_RECOGNIZE_MODE, IDCardRecognizer.Mode.FRONT);
//			tvBack.setImageResource(R.drawable.scan_back_user);
			only_name_id = false;
		}
		
		File livenessFolder = new File(storageFolder);
		
		if (!livenessFolder.exists()) {
			livenessFolder.mkdirs();
		}
		// 开始检测之前请删除文件夹下保留的文件
		deleteFiles(storageFolder);
		
		// 设置扫描界面方向为竖直，设备头部朝上
		scanIntent.putExtra(CardActivity.EXTRA_SCAN_ORIENTATION, CardActivity.ORIENTATION_PORTRAIT);
		// 设置需要返回裁剪后的身份证图像
		scanIntent.putExtra(CardActivity.EXTRA_CARD_IMAGE_RECTIFIED, true);
		// 设置需要返回原图。若为正反面连续扫描时，使用EXTRA_CARD_IMAGE返回的是反面图像
		//scanIntent.putExtra(IDCardActivity.EXTRA_CARD_IMAGE, true);
		
		scanIntent.putExtra(CardActivity.EXTRA_SCAN_RECT_OFFSET, scanRectOffset);
		startActivityForResult(scanIntent, EXAMPLE_REQUEST_CODE);
//		startActivity(scanIntent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==8){
			switch (resultCode) {
			case RESULT_OK:
				
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
				tvpro.setText(getResources().getString(R.string.pass_liveness));
				Intent it  = new Intent(Realname3Activity.this,Realname4Activity.class);
				startActivity(it);
				break;
			case RESULT_CANCELED:
				tvpro.setText(getResources().getString(R.string.cancel_liveness));
				break;
			case RESULT_FIRST_USER:
				tvpro.setText(getResources().getString(R.string.save_liveness_result_fail));
				break;
			default:
				tvpro.setText(getResources().getString(R.string.failed_init_sdk));
				break;
			}
			return;
		}
		switch (resultCode) {
		case CardActivity.RESULT_CARD_INFO: {
			new Handler().post(new Runnable() {  
			    @Override  
			    public void run() {  
			    	scroll.fullScroll(View.FOCUS_DOWN);
			    }  
			}); 
			imageFront = null;
			imageBack = null;
			byte[] imageBytesFront = null;
			byte[] imageBytesBack = null;
			try {
				idcard = data.getParcelableExtra(CardActivity.EXTRA_SCAN_RESULT);
				//连续扫描正反面模式下
				if(doubleSide){
					//分别获取裁剪后的正面和反面图像
					imageBytesFront = data.getByteArrayExtra(CardActivity.EXTRA_CARD_IMAGE_FRONT_RECTIFIED);
					imageBytesBack = data.getByteArrayExtra(CardActivity.EXTRA_CARD_IMAGE_RECTIFIED);	
					//分别获取正面和反面原图
					//imageBytesFront = data.getByteArrayExtra(IDCardActivity.EXTRA_CARD_IMAGE_FRONT);	
					//imageBytesBack = data.getByteArrayExtra(IDCardActivity.EXTRA_CARD_IMAGE);
				}else if(idcard.getSide() == IDCard.Side.FRONT){
					//扫描正面模式和快速扫描姓名和号码时，返回裁剪图
					imageBytesFront = data.getByteArrayExtra(CardActivity.EXTRA_CARD_IMAGE_RECTIFIED);
					//扫描正面模式和快速扫描姓名和号码时，返回原图
					//imageBytesFront = data.getByteArrayExtra(IDCardActivity.EXTRA_CARD_IMAGE);
				}else if(idcard.getSide() == IDCard.Side.BACK){
					//扫描反面时，返回裁剪图
					imageBytesBack = data.getByteArrayExtra(CardActivity.EXTRA_CARD_IMAGE_RECTIFIED);
					//扫描反面时，返回原图
					//imageBytesBack = data.getByteArrayExtra(IDCardActivity.EXTRA_CARD_IMAGE);
				}
			} catch (Exception e) {
			}
			if(imageBytesFront != null){
				
				upPhoto(byte2File(imageBytesFront,storageFolder,"imageBytesFront.png"),0);
				
				upimage(byte2File(imageBytesFront,storageFolder,"imageBytesFront.png"));
				imageFront = BitmapFactory.decodeByteArray(imageBytesFront, 0, imageBytesFront.length);}
			if(imageBytesBack != null){
				
				upPhoto(byte2File(imageBytesBack,storageFolder,"imageBytesBack.png"),1);
				imageBack = BitmapFactory.decodeByteArray(imageBytesBack, 0, imageBytesBack.length);}
			if(idcard == null){
				tvpro.setText("身份证识别结果出现异常");
			}else if(doubleSide){
				getResultIDCard(idcard, doubleSide, IDCard.Side.FRONT, imageFront, imageBack);
//				mTextShowFront.setTextColor(getResources().getColor(R.color.darkblue));
//				mTextShowBack.setTextColor(Color.BLACK);
//				mLineShowFront.setVisibility(View.VISIBLE);
//				lllayout.setVisibility(View.GONE);
			}else{
				if(idcard.getSide() == IDCard.Side.FRONT){
					getResultIDCard(idcard, false, IDCard.Side.FRONT, imageFront, imageBack);
				}else if(idcard.getSide() == IDCard.Side.BACK){
					getResultIDCard(idcard, false, IDCard.Side.BACK, imageFront, imageBack);
				}
			}
		}
			break;			
		case CardActivity.RESULT_CAMERA_NOT_AVAILABLE:{
			tvpro.setText("摄像头不可用，或用户拒绝授权使用");
		}
			break;
		case RESULT_CANCELED:
			tvpro.setText("扫描被取消");
			break;
		case CardActivity.RESULT_RECOGNIZER_INIT_FAILED:
			tvpro.setText("算法SDK初始化失败：可能是模型路径错误，SDK权限过期，包名绑定错误");
			break;
		case CardActivity.RESULT_AUTHENTICATION_FAILED:
			tvpro.setText("API账户验证错误：请检查网络以及您的API ID和API Secret信息");
			break;
			
		default:
			tvpro.setText("未知结果");
			break;
			
		}
	}
	


	public void getResultIDCard(IDCard idCard, boolean isDouble, IDCard.Side side, Bitmap front, Bitmap back){
		if(!isDouble){
//			mBtnSet.setVisibility(View.GONE);
		}
		Bitmap picRes = null;
		if(side == IDCard.Side.FRONT){
			picRes = front;
			idcardImageView.setImageBitmap(picRes);
		}else if(side == IDCard.Side.BACK){
			picRes = back;
			idcardImageView1.setImageBitmap(picRes);
		}

		if (idcard == null) {
			tvpro.setText("身份证识别结果出现异常");
		} else if (side == IDCard.Side.FRONT) {
//			mResult.setVisibility(View.VISIBLE);

			if (only_name_id) {	
//				iv_face.setVisibility(View.GONE);
			
//				tvNameLabel.setText("姓名");
//				tvSexLabel.setText("号码");
				
				//返回姓名和身份证号截图区域，要使用该功能请设置参数为裁剪后的身份证图片，即设置IDCardActivity.EXTRA_CARD_IMAGE_RECTIFIED
				Rect imgNameRect = idcard.getImgName();
				Rect imgNumRect = idcard.getImgID();
				if(imgNameRect != null && imgNameRect.right < picRes.getWidth() && imgNameRect.bottom < picRes.getHeight()){
					//picRes必须为SDK返回的裁剪后的图片
					Bitmap imgName = Bitmap.createBitmap(picRes, imgNameRect.left, imgNameRect.top, imgNameRect.right-imgNameRect.left, imgNameRect.bottom-imgNameRect.top);
//					idcardImageName.setImageBitmap(imgName);
				}
				if(imgNumRect != null && imgNumRect.right<picRes.getWidth() && imgNumRect.bottom < picRes.getHeight()){
					Bitmap imgNum = Bitmap.createBitmap(picRes, imgNumRect.left, imgNumRect.top, imgNumRect.right-imgNumRect.left, imgNumRect.bottom-imgNumRect.top);
//					idcardImageNumber.setImageBitmap(imgNum);
				}				
				
//				llRace.setVisibility(View.INVISIBLE);
//				llBirth.setVisibility(View.INVISIBLE);
//				llAddress.setVisibility(View.INVISIBLE);
//				llNumber.setVisibility(View.INVISIBLE);
//				
//				mLineFour.setVisibility(View.INVISIBLE);
//				mLineFive.setVisibility(View.INVISIBLE);
//				mLineSix.setVisibility(View.INVISIBLE);
//
//				tvName.setText(idcard.getStrName());
//				tvSex.setText(idcard.getStrID());
			} else {
				
				btn.setVisibility(View.GONE);
				
				
				
				id_info.setVisibility(View.VISIBLE);
				int height = picRes.getHeight();
				int width = picRes.getWidth();
				
				
//				iv_face.setVisibility(View.VISIBLE);
				Bitmap avatar = Bitmap.createBitmap(picRes, width/8*5, height/7, width/3, height/5*3);
//				iv_face.setImageBitmap(avatar);
				
				tvName.setText(idcard.getStrName());
				sex = idcard.getStrSex();
				national = idcard.getStrNation();
				address = idcard.getStrAddress();
//				tvSex.setText(idcard.getStrSex());
//				tvRace.setText(idcard.getStrNation());
//				tvBirth.setText(idcard.getStrDate());
//				tvAddress.setText(idcard.getStrAddress());
			
				tvNumber.setText(idcard.getStrID());

				
			}
		} else if (side == IDCard.Side.BACK) {
			btn1.setVisibility(View.GONE);
//			mResult.setVisibility(View.VISIBLE);
//			mDefault.setVisibility(View.GONE);
			
//			tvNameLabel.setText("签发机关");
//			tvSexLabel.setText("有效期限");
			
//			llRace.setVisibility(View.INVISIBLE);
//			llBirth.setVisibility(View.INVISIBLE);
//			llAddress.setVisibility(View.INVISIBLE);
//			llNumber.setVisibility(View.INVISIBLE);
//			
//			mLineFour.setVisibility(View.INVISIBLE);
//			mLineFive.setVisibility(View.INVISIBLE);
//			mLineSix.setVisibility(View.INVISIBLE);
			id_info1.setVisibility(View.VISIBLE);
			tv_mechanism.setText(idcard.getStrAuthority());
			validity = idcard.getStrValidity();
//			tvSex.setText(idcard.getStrValidity());
		} else {
			tvpro.setText("身份证识别结果出现异常");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.llWalletRecharge1:
//			Intent scanIntent= new Intent(Realname3Activity.this,SenseMainActivity.class);
//			startActivity(scanIntent);
			initcamera(true);
			break;
		case R.id.llWalletRecharge2:
			initcamera(false);
			break;
		case R.id.btn2:
//			Intent scanIntent= new Intent(Realname3Activity.this,Realname2Activity.class);
//			startActivity(scanIntent);
			if(TextUtils.isEmpty(ImageId)){
				T.showShort(Realname3Activity.this,  "上传失败 ，请重新扫描身份证正面");
				idcardImageView.setImageResource(R.drawable.idcard_pro_back);
				return;
			}
			if(TextUtils.isEmpty(idcardimg)){
				T.showShort(Realname3Activity.this,  "请重新扫描身份证");
				idcardImageView.setImageResource(R.drawable.idcard_pro_back);
				
				return;
			}
			

			
			if(TextUtils.isEmpty(tvName.getText().toString())){
				T.showShort(Realname3Activity.this,  "姓名不能为空,请重新扫描身份证正面");
				return;
			}
			
			if(TextUtils.isEmpty(idcardbackimg)){
				T.showShort(Realname3Activity.this,  "请重新扫描身份证背面");
				idcardImageView1.setImageResource(R.drawable.idcard_pro);
				
				return;
			}
//			if(TextUtils.isEmpty(tvNumber.getText().toString())){
//				T.showShort(Realname3Activity.this,  "身份证扫描出错请重新扫描");
//				return;
//			}
//			

			
			if(TextUtils.isEmpty(tv_mechanism.getText().toString())){
				T.showShort(Realname3Activity.this,  "签发机关不能为空,请重新扫描身份证背面");
				return;
			}
			
			downloadDialog = new TwoButtonDialog(Realname3Activity.this, R.style.CustomDialog,
					"身份证有误将影响资金交易,请确认", tvName.getText().toString()+"\n\n"+tvNumber.getText().toString(), "返回修改", "确认无误",true,new OnMyDialogClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							switch (v.getId()) {
							case R.id.Button_OK:
								downloadDialog.dismiss();
//								idcardImageView.setImageResource(R.drawable.idcard_pro_back);
//								tvName.setText("");
//								tvNumber.setText("");
								break;
							case R.id.Button_cancel:
								loadreal();
								downloadDialog.dismiss();
							default:
								break;
							}
						}
					});
				downloadDialog.show();
				downloadDialog.setCancelable(false);
			
			
			break;
		case R.id.iv_facade:
			initcamera(true);
			break;
		case R.id.iv_facade1:
			initcamera(false);
			break;
		default:
			break;
		}
		
	}
	
	

//	private void startLiveness() {
//		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//		String outputType = sp.getString(Constants.OUTPUTTYPE, Constants.MULTIIMAGE);
//		String complexity = sp.getString(Constants.COMPLEXITY, Constants.NORMAL);
//		boolean soundNotice = sp.getBoolean(Constants.NOTICE, true);
//		SharedPreferences detectSp = getApplicationContext().getSharedPreferences(Constants.DETECTLIST, MODE_PRIVATE);
//		String detectList = detectSp.getString(Constants.DETECTLIST, Constants.DEFAULTDETECTLIST);
//		Intent intent = new Intent();
//		intent.setClass(Realname3Activity.this, LivenessActivity.class);
//		/**
//		 * EXTRA_MOTION_SEQUENCE 动作检测序列配置，支持四种检测动作， BLINK(眨眼), MOUTH（张嘴）, NOD（点头）, YAW（摇头）, 各个动作以空格隔开。 推荐第一个动作为BLINK.
//		 * 默认配置为"BLINK MOUTH NOD YAW"
//		 */
//		intent.putExtra(LivenessActivity.EXTRA_MOTION_SEQUENCE, detectList);
//		/**
//		 * SOUND_NOTICE 配置, 传入的soundNotice为boolean值，true为打开, false为关闭, 默认为true.
//		 */
//		intent.putExtra(LivenessActivity.SOUND_NOTICE, soundNotice);
//		/**
//		 * OUTPUT_TYPE 配置, 传入的outputType类型为singleImg （单图）或 multiImg （多图）, 默认为multiImg.
//		 */
//		intent.putExtra(LivenessActivity.OUTPUT_TYPE, outputType);
//		/**
//		 * COMPLEXITY 配置, 传入的complexity类型, 支持四种难度:easy, normal, hard, hell.默认为normal.
//		 */
//		intent.putExtra(LivenessActivity.COMPLEXITY, complexity);
//		File livenessFolder = new File(storageFolder);
//	
//		if (!livenessFolder.exists()) {
//			livenessFolder.mkdirs();
//		}
//		// 开始检测之前请删除文件夹下保留的文件
//		deleteFiles(storageFolder);
//		/**
//		 * EXTRA_RESULT_PATH 配置， 传入的storageFolder为sdcard下目录, 为了保存检测结果文件, 传入之前请确保该文件夹存在。
//		 */
//		intent.putExtra(LivenessActivity.EXTRA_RESULT_PATH, storageFolder);
//		startActivityForResult(intent, START_DETECT_REQUEST_CODE);
//	}
	
	public static void deleteFiles(String folderPath) {
		File dir = new File(folderPath);
		if (dir == null || !dir.exists() || !dir.isDirectory() || dir.listFiles() == null)
			return;
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete();
		}
	}
	
	
	
	private void initDialog(String content,String left,String right) {
	// TODO Auto-generated method stub
	downloadDialog = new TwoButtonDialog(Realname3Activity.this, R.style.CustomDialog,
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
	
	/**
	 * 监听返回按钮
	 */
//	@Override
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			initDialog("确定退出实名认证?", "取消", "确定");
		}
		return true;
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
				T.showNetworkError(Realname3Activity.this);
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
					}
//					T.showShort(Realname3Activity.this, bean.result_desc);
//					if(Api.SUCCEED == bean.result_code){
//						
//					}
				}
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
		
		ProgressDialogUtil.showProgressDlg1(this, "图片上传中");
		RequestParams params =  new RequestParams();
		params.addBodyParameter("business_type", UserInfoManager.getUserId(this)+"");
		params.addBodyParameter("file", f);
		new HttpUtils().send(HttpMethod.POST, Api.URL_UPLOAD_FILE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg1();
				T.showNetworkError(Realname3Activity.this);
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg1();
				if(resp.statusCode==200 && resp.result!=null) {
//					ReturnImgResponse bean = new Gson().fromJson(resp.result, ReturnImgResponse.class);
					FileUploadResponse bean = new Gson().fromJson(resp.result, FileUploadResponse.class);
					Log.e("", ""+resp.result);
//					T.showShort(Realname3Activity.this, bean.result_desc);
					if(Api.SUCCEED == bean.result_code)
						updateAvatar(bean.data.get(0),i);
				}
			}
		});
	}
	
	private void updateAvatar(String string,int tag) {
		// TODO Auto-generated method stub
		if(tag==0){
			idcardimg = string;
		}else{
			idcardbackimg = string;
		}
		
	}
	
	  public static File byte2File(byte[] buf, String filePath, String fileName)  
	    {  
	        BufferedOutputStream bos = null;  
	        FileOutputStream fos = null;  
	        File file = null;  
	        try  
	        {  
	            File dir = new File(filePath);  
	            if (!dir.exists() && dir.isDirectory())  
	            {  
	                dir.mkdirs();  
	            }  
	            file = new File(filePath + File.separator + fileName);  
	            fos = new FileOutputStream(file);  
	            bos = new BufferedOutputStream(fos);  
	            bos.write(buf);  
	        }  
	        catch (Exception e)  
	        {  
	            e.printStackTrace();  
	        }  
	        finally  
	        {  
	            if (bos != null)  
	            {  
	                try  
	                {  
	                    bos.close();  
	                }  
	                catch (IOException e)  
	                {  
	                    e.printStackTrace();  
	                }  
	            }  
	            if (fos != null)  
	            {  
	                try  
	                {  
	                    fos.close();  
	                }  
	                catch (IOException e)  
	                {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }
			return file;  
	    }  
	  
	  
		public void loadreal() {
//			
			

			
			
			
			ProgressDialogUtil.showProgressDlg(this, "请稍后");
			String tosex = "";
			if(sex.equals("男")){
				tosex = "1";
			}else{
				tosex = "2";
			}
			
			
			
			SubmitRealRequset req = new SubmitRealRequset();
			req.image_id = ImageId;
			req.user_id = UserInfoManager.getUserInfo(this).id+"";
			req.real_name = tvName.getText().toString();
			req.person_no = tvNumber.getText().toString();
			req.id_card_positive_photo = idcardimg;
			req.id_card_nagetive_photo = idcardbackimg;
			req.latitude =  cityManager.latitude;
			req.longitude = cityManager.longitude;
			req.sex = tosex;
			req.validity = validity;
			req.national = national;
			req.address = address;
			req.signOrgan = tv_mechanism.getText().toString();
			Log.e("", " = = =  "+req.image_id);
//			EncryptionUtil.getEncryptionstring(req.user_id);
//			req.isEncrypt = "1";
//			req.info = EncryptionUtil.getEncryptionstring(req.user_id);
			
			RequestParams params = new RequestParams();
			try {
				params.setBodyEntity(new StringEntity(req.toJson(),"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			new HttpUtils().send(HttpMethod.POST, Api.SUBMITIDCARDINFO, params, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					ProgressDialogUtil.dismissProgressDlg();
						T.showNetworkError(Realname3Activity.this);
				}

				@Override
				public void onSuccess(ResponseInfo<String> resp) {
					ProgressDialogUtil.dismissProgressDlg();
					AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
					
					if(Api.SUCCEED==bean.result_code) {
						if(bean.data.RSPCOD.equals("000000")){
							Intent scanIntent= new Intent(Realname3Activity.this,Realname2Activity.class);
							scanIntent.putExtra("idcard", tvNumber.getText().toString());
							scanIntent.putExtra("idname", tvName.getText().toString());
							startActivity(scanIntent);
							T.showShort(Realname3Activity.this,  bean.data.RSPMSG);
						}else{
							T.showShort(Realname3Activity.this,  bean.data.RSPMSG);
						}
					}
				}
			});
		}
}
