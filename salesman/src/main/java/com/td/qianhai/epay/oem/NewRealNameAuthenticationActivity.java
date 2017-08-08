package com.td.qianhai.epay.oem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.CityEntity;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.ProvinceEntity;
import com.td.qianhai.epay.oem.mail.utils.DESKey;
import com.td.qianhai.epay.oem.mail.utils.GetImageUtil;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.AnimationUtil;
import com.td.qianhai.epay.oem.views.MyTextWatcher;
import com.td.qianhai.epay.oem.views.SelectPicPopupWindow;
import com.td.qianhai.epay.utils.DateUtil;

/**
 * 实名认证界面
 * 
 * @author liangge
 * 
 */
public class NewRealNameAuthenticationActivity extends BaseActivity implements
		OnClickListener {
	// 文件名
		private String localTempImgFileName = "";
		// 身份证照片Code
		private final int FRONT_CODE = 100;
		// 情景照片Code
		private final int SCENS_CODE = 101;
		// 银行卡照片code
		private final int BANK_CODE = 102;
		
		private String banks = "";

		// 显示的照片 身份证照片、情景照片
		private ImageView ivFront, ivScene,im_bank,iv_idcard_scene;
		// 标题
		private TextView realTitle;
		// 身份证照片、情景照片
		private String idFrontPic, scenePic, bancardPic, txnsts;
		private String mobile, cardData, psamId, stateaudit, issno, custId,
				bankCityname, bankProvincename, dcflag, cardType, bankname,
				bankBranchName, bankBranchid, bankProvinceid = "", bankCityid = "",bankareid = "";

		private String termina = "";
		// intent当前要操作的事件、要操作的key
		private String intentObj, realNameChangeKey,mobileAgain,pwd;
		/** 返回、title中间内容 */
		private TextView bt_Back;
		private String custpic, idcardpic, mercnum, bankpic;
		private File custPicFile, idcardPicFile, bankFile;

		private boolean tags = false;
		/** 认证操作(如果是修改则隐藏掉 如果是认证就显示) */
		private FrameLayout authentication_operation, lin_1, lin_2,lin_3;

		private RelativeLayout iv_name_auth_scene1,iv_name_auth_scene2;

		/** 商户编号 ，商户名称 ，商户身份证证号 */
		private EditText et_terminal_no, et_register_name, et_register_cert_no,
				et_mobile_no, et_register_card_no, bankCardNo, tvBankBranch;

		/** 银行名称、银行开户省、银行开户城市、银行开户支行 、返回 */
		private TextView tvBankName, tvBankProvince, tvBankCity, bt_back,
				tv_contre, tv_pro,take_scene1,tv_issuing_bank_area;

		/** 二磁道、三磁道 */
		private String track2, track3;

		boolean isOpen;

		private Editor editor;

		private TextView take_front, take_scene, take_bank, tv_propty1, tv_propty2,tv_propty3,
				real_pointimg1, real_pointimg2, real_pointimg,tv_issuing_bank,tv_issuing_bank_com,tv_pros,tv_pro1,tv_pro2,tv_pro3,real_pointimg3;

		public static boolean iscameras = false;

		private SelectPicPopupWindow menuWindow;

		private ImageView pro_1, pro_2, pro_3,pro_4;

		private boolean isshowpro = false;

		private EditText tv_banknum;
		
		private TextView tv_bank;

		private String banknum = "";
		
		private ArrayList<HashMap<String, Object>> list;
		
		private ArrayList<HashMap<String, Object>> mList;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.newreal_name_authentication);
			AppContext.getInstance().addActivity(this);
			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			editor = MyCacheUtil.setshared(this);
			mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
			mercnum = MyCacheUtil.getshared(this).getString("MercNum", "");
			txnsts = MyCacheUtil.getshared(this).getString("Txnsts", "");
			// mobile = ((AppContext) NewRealNameAuthenticationActivity.this
			// .getApplication()).getMobile();
			// mercnum = ((AppContext) NewRealNameAuthenticationActivity.this
			// .getApplication()).getMercNum();
			// custId = ((AppContext) getApplication()).getCustId();
			custId = MyCacheUtil.getshared(this).getString("Mobile", "");
			psamId = ((AppContext) getApplication()).getPsamId();
			pwd = MyCacheUtil.getshared(this).getString("pwd", "");
			stateaudit = ((AppContext) getApplication()).getStateaudit();

			initview();

			Intent intent = getIntent();
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				intentObj = bundle.getString("intentObj");
				if (intentObj!=null&&intentObj.equals("RequestCardInfoChangeActivity")) {
					tags = false;
					authentication_operation.setVisibility(View.GONE);
					realTitle.setText("银行卡变更认证");
					realNameChangeKey = "198102";

				} else if (intentObj!=null&&intentObj.equals("RequestPhonenumberChenges")) {
					mobileAgain = bundle.getString("phone");
					tags = false;
					authentication_operation.setVisibility(View.GONE);
					realTitle.setText("手机号码变更认证");
					realNameChangeKey = "198110";
				} else if (intentObj!=null&&intentObj.equals("MenuActivity")) {
					realTitle.setText("资料补全");
					realNameChangeKey = "199101";
					tags = true;
					tv_pro.setVisibility(View.VISIBLE);
					if (authentication_operation.getVisibility() == View.GONE) {
						authentication_operation.setVisibility(View.VISIBLE);
					}

				}else if(intentObj!=null&&intentObj.equals("Skactivity")){
					ivFront.setImageResource(R.drawable.newrealcard_img);
					ivScene.setImageResource(R.drawable.newrealq_img);
					iv_idcard_scene.setImageResource(R.drawable.idcardback_img);
					tv_pros.setVisibility(View.VISIBLE);
					lin_3.setVisibility(View.VISIBLE);
					tv_pro1.setVisibility(View.VISIBLE);
					tv_pro2.setVisibility(View.VISIBLE);
					tv_pro3.setVisibility(View.VISIBLE);
					lin_3.setVisibility(View.VISIBLE);
					realTitle.setText("收款宝资料补全");
					authentication_operation.setVisibility(View.GONE);
					realNameChangeKey = "702008";
				}
			} else {
				realTitle.setText("资料补全");
				realNameChangeKey = "199101";
				tags = true;
				tv_pro.setVisibility(View.VISIBLE);
				if (authentication_operation.getVisibility() == View.GONE) {
					authentication_operation.setVisibility(View.VISIBLE);
				}
			}

			if (txnsts.equals("0")) {
//				ivBank.setVisibility(View.VISIBLE);
				et_register_cert_no.setVisibility(View.GONE);
				et_register_name.setVisibility(View.GONE);
			}else{
				BussinessInfoTask task = new BussinessInfoTask();
				task.execute(HttpUrls.BUSSINESS_INFO + "", custId, "4","0");
			}
			if (!intentObj.equals("Skactivity")&&stateaudit != null) {

				if (stateaudit.equals("011")) {
					lin_1.setVisibility(View.GONE);
					tv_propty1.setVisibility(View.GONE);

				} else if (stateaudit.equals("001")) {
					lin_1.setVisibility(View.GONE);
					lin_2.setVisibility(View.GONE);
					tv_propty1.setVisibility(View.GONE);
					tv_propty2.setVisibility(View.GONE);

				} else if (stateaudit.equals("101")) {
					lin_2.setVisibility(View.GONE);
					tv_propty2.setVisibility(View.GONE);

				} else if (stateaudit.equals("100")) {
					tv_propty2.setVisibility(View.GONE);
					lin_2.setVisibility(View.GONE);
//					authentication_operation.setVisibility(View.GONE);
					tv_pro.setVisibility(View.GONE);

				} else if (stateaudit.equals("110")) {
//					authentication_operation.setVisibility(View.GONE);
					tv_pro.setVisibility(View.GONE);

				} else if (stateaudit.equals("010")) {
					lin_1.setVisibility(View.GONE);
//					authentication_operation.setVisibility(View.GONE);
					tv_pro.setVisibility(View.GONE);
					tv_propty1.setVisibility(View.GONE);
				}
			}

			custpic = realNameChangeKey + "_" + getStringDateMerge() + "_"
					+ "C.jpg";
			idcardpic = realNameChangeKey + "_" + getStringDateMerge() + "_"
					+ "I.jpg";

			bankpic = realNameChangeKey + "_" + getStringDateMerge() + "_"
					+ "B.jpg";

			// if(MainActivity.cache.getString("isone")!=null&&MainActivity.cache.getString("isone").equals("0")){
			pro_1.setVisibility(View.VISIBLE);

			AnimationUtil.JAnima(this, pro_1);
			isshowpro = true;
			// }
			bt_Back = (TextView) findViewById(R.id.bt_title_left);
			bt_Back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		}
		
		private void initcarddata(){
			cardData = tv_banknum.getText().toString();
//			bankCardNo.setText(cardData.split("d")[0]);
	//
//			bankCardNo.setEnabled(true);
			// char[] charBin = cardData.toCharArray();
			// String bankName = BankInfo.getNameOfBank(charBin, 0);
			cardData = cardData
					+ "d49121202369991430fffffffffff996222024000079840084d1561560000000000001"
					+ "003236999010000049120d000000000000d000000000000d00000000fffffffffffffff";

			track2 = cardData.substring(0, 48).replace("f", "")
					.replace("d", "D");
			track3 = cardData.substring(48, cardData.length())
					.replace("f", "").replace("d", "D");
		}

		private void initview() {
			tv_issuing_bank_area = (TextView) findViewById(R.id.tv_issuing_bank_area);
			tv_issuing_bank_area.setOnClickListener(this);
			take_front = (TextView) findViewById(R.id.btn_name_auth_take_front);
			take_front.setOnClickListener(this);
			take_scene = (TextView) findViewById(R.id.btn_name_auth_take_scene);
			take_scene.setOnClickListener(this);
			lin_3 = (FrameLayout) findViewById(R.id.lin_3);
			take_scene1 = (TextView) findViewById(R.id.btn_name_auth_take_scene1);
			take_scene1.setOnClickListener(this);
			tv_banknum = (EditText) findViewById(R.id.tv_banknum);
			tv_bank = (TextView) findViewById(R.id.tv_bank);
			tv_pro = (TextView) findViewById(R.id.tv_pro);
			tv_pros =  (TextView) findViewById(R.id.tv_pros);
			tv_pro1 =  (TextView) findViewById(R.id.tv_pro1);
			tv_pro2 =  (TextView) findViewById(R.id.tv_pro2);
			tv_pro3 =  (TextView) findViewById(R.id.tv_pro3);
			tv_issuing_bank = (TextView) findViewById(R.id.tv_issuing_bank);
			tv_issuing_bank.setOnClickListener(this);
			tv_issuing_bank_com = (TextView) findViewById(R.id.tv_issuing_bank_com);
			tv_issuing_bank_com.setOnClickListener(this);
			im_bank = (ImageView) findViewById(R.id.im_bank);
			et_register_name = (EditText) findViewById(R.id.tv_username);
			et_register_name.requestFocus();
			et_register_cert_no = (EditText) findViewById(R.id.tv_userid);
			// take_bank = (TextView) findViewById(R.id.btn_name_auth_take_bank);
			// take_bank.setOnClickListener(this);
			lin_1 = (FrameLayout) findViewById(R.id.lin_1);
			lin_2 = (FrameLayout) findViewById(R.id.lin_2);

			real_pointimg = (TextView) findViewById(R.id.real_pointimg);
			real_pointimg1 = (TextView) findViewById(R.id.real_pointimg1);
			real_pointimg2 = (TextView) findViewById(R.id.real_pointimg2);
			real_pointimg3 = (TextView) findViewById(R.id.real_pointimg3);

			iv_name_auth_scene1 = (RelativeLayout) findViewById(R.id.iv_name_auth_scene1);
			iv_name_auth_scene1.setOnClickListener(this);
			
			iv_name_auth_scene2 = (RelativeLayout) findViewById(R.id.iv_name_auth_scene2);
			iv_name_auth_scene2.setOnClickListener(this);

			tv_propty1 = (TextView) findViewById(R.id.tv_propty1);
			tv_propty2 = (TextView) findViewById(R.id.tv_propty2);
			tv_propty3 = (TextView) findViewById(R.id.tv_propty3);

			findViewById(R.id.btn_name_auth_upload).setOnClickListener(this);

			ivFront = (ImageView) findViewById(R.id.iv_name_auth_front);
			ivFront.setOnClickListener(this);
			
			iv_idcard_scene = (ImageView) findViewById(R.id.iv_idcard_scene);
			iv_idcard_scene.setOnClickListener(this);

			ivScene = (ImageView) findViewById(R.id.iv_name_auth_scene);
			// ivScene.setOnClickListener(this);

//			ivBank = (ImageView) findViewById(R.id.iv_name_auth_bank);
			// ivBank.setOnClickListener(this);

			realTitle = (TextView) findViewById(R.id.tv_title_contre);
			authentication_operation = (FrameLayout) findViewById(R.id.authentication_operation);

			pro_1 = (ImageView) findViewById(R.id.pro_1);
			pro_2 = (ImageView) findViewById(R.id.pro_2);
			pro_3 = (ImageView) findViewById(R.id.pro_3);
			pro_4 = (ImageView) findViewById(R.id.pro_4);
			tv_banknum.addTextChangedListener(new MyTextWatcher(tv_banknum));
			 mList = new ArrayList<HashMap<String,Object>>();
			tv_bank.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent it = new Intent(NewRealNameAuthenticationActivity.this,SelectBankActivity.class);
					it.putExtra("tag", "3");
					startActivityForResult(it, 2);
				}
			});

		}

		public String getStringDateMerge() {
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String dateString = formatter.format(currentTime);
			return dateString;
		}

		@Override
		public void onClick(View v) {
			if (DateUtil.isFastDoubleClick()) {
				return;
			} else {

				switch (v.getId()) {
				case R.id.btn_name_auth_take_front:
					iscameras = false;
					takePicture(FRONT_CODE);
					break;
				case R.id.btn_name_auth_take_scene:
					if(intentObj!=null&&intentObj.equals("Skactivity")){
						iscameras = false;
					}else{
						iscameras = true;
					}
					takePicture(SCENS_CODE);
					break;
				case R.id.btn_name_auth_take_scene1:
					iscameras = false;
					takePicture(BANK_CODE);
					break;
				// case R.id.btn_name_auth_take_bank:
				// iscameras = false;
				// takePicture(BANK_CODE);
				// break;
				case R.id.btn_name_auth_upload:
					if(intentObj!=null&&intentObj.equals("Skactivity")){
						uploadAuthentication1();
					}else{
						if (tags) {
							completedata(stateaudit);
						} else {
							uploadAuthentication();
						}
					}

					break;
				case R.id.iv_name_auth_front:
					editpic();
					break;
				case R.id.iv_name_auth_scene1:
					editpic1();
					// case R.id.iv_name_auth_scene:
					// editpic1();
					break;
				case R.id.iv_idcard_scene:
					editpic2();
					break;
				 case R.id.tv_issuing_bank:
					
//					 initcarddata();
//						BankProvinceTask1 task2 = new BankProvinceTask1();
//						task2.execute(HttpUrls.QUERY_BANK_NAME + "", termina,
//								track2, track3);
					 
					 new Thread(run).start();	
				 break;
				 case R.id.tv_issuing_bank_com:
					 if(tv_issuing_bank.getText().toString()==null||tv_issuing_bank.getText().toString().equals("")){
							AnimationUtil.BtnSpecialAnmations1(this, tv_issuing_bank, 500);
							return;
					 }
					 
					 new Thread(run1).start();	
//					 BankCityTask task = new BankCityTask();
//						task.execute(HttpUrls.QUERY_BANK_CITY + "", "1", "200",
//						bankProvinceid);..
				 break;
				 case R.id.tv_issuing_bank_area:
					 if(tv_issuing_bank_com.getText()==null||tv_issuing_bank_com.getText().toString().equals("")){
							AnimationUtil.BtnSpecialAnmations1(this, tv_issuing_bank_com, 500);
							return;
					 }
					 new Thread(run2).start();	
				 break;
				default:

				}
			}
		}

		private void editpic() {

			menuWindow = new SelectPicPopupWindow(
					NewRealNameAuthenticationActivity.this, itemsOnClick);
			menuWindow.showAtLocation(
					NewRealNameAuthenticationActivity.this.findViewById(R.id.main),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			if (idFrontPic == null) {
				menuWindow.setbutton();
			} else {
				menuWindow.setbutton1("重拍");
			}
		}

		private void editpic1() {
			menuWindow = new SelectPicPopupWindow(
					NewRealNameAuthenticationActivity.this, itemsOnClick1);
			menuWindow.showAtLocation(
					NewRealNameAuthenticationActivity.this.findViewById(R.id.main),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			if (scenePic == null) {
				menuWindow.setbutton();
			} else {
				menuWindow.setbutton1("重拍");
			}

		}

		private void editpic2() {
			menuWindow = new SelectPicPopupWindow(
					NewRealNameAuthenticationActivity.this, itemsOnClick2);
			menuWindow.showAtLocation(
					NewRealNameAuthenticationActivity.this.findViewById(R.id.main),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			if (bancardPic == null) {
				menuWindow.setbutton();
			} else {
				menuWindow.setbutton1("重拍");
			}
		}

		// 为弹出窗口实现监听类
		private OnClickListener itemsOnClick = new OnClickListener() {

			public void onClick(View v) {
				menuWindow.dismiss();
				switch (v.getId()) {
				case R.id.btn_take_photo:
					iscameras = false;
					takePicture(FRONT_CODE);
					break;
				case R.id.btn_pick_photo:
					if (idFrontPic != null) {
						real_pointimg
								.setBackgroundResource(R.drawable.newreal_point1);
						take_front
								.setBackgroundResource(R.drawable.newreal_camera_btn);
						take_front.setEnabled(true);
						ivFront.setBackgroundColor(getResources().getColor(
								R.color.transparent));
						ivFront.setImageResource(R.drawable.newreal_idcard);
						idFrontPic = null;
					}
					break;
				default:
					break;
				}
			}
		};

		// 为弹出窗口实现监听类
		private OnClickListener itemsOnClick1 = new OnClickListener() {

			public void onClick(View v) {
				menuWindow.dismiss();
				switch (v.getId()) {
				case R.id.btn_take_photo:
					iscameras = true;
					takePicture(SCENS_CODE);
					break;
				case R.id.btn_pick_photo:
					if (scenePic != null) {
						take_scene.setEnabled(true);
						real_pointimg1
								.setBackgroundResource(R.drawable.newreal_point1);
						take_scene
								.setBackgroundResource(R.drawable.newreal_camera_btn);
						// iv_name_auth_scene1
						// .setBackgroundResource(R.drawable.null_bg);
						ivScene.setBackgroundColor(getResources().getColor(
								R.color.transparent));
						ivScene.setImageResource(R.drawable.newreal_scene);
						scenePic = null;
					}

					break;
				default:
					break;
				}
			}
		};

		// 为弹出窗口实现监听类
		private OnClickListener itemsOnClick2 = new OnClickListener() {

			public void onClick(View v) {
				menuWindow.dismiss();
				switch (v.getId()) {
				case R.id.btn_take_photo:
					iscameras = false;
					takePicture(BANK_CODE);
					break;
				case R.id.btn_pick_photo:
					if (bancardPic != null) {
						take_bank.setEnabled(true);
						real_pointimg
								.setBackgroundResource(R.drawable.newreal_point1);
						take_bank
								.setBackgroundResource(R.drawable.newreal_camera_btn);
//						ivBank.setImageResource(R.drawable.newreal_bankcard);
						bancardPic = null;
					}

					break;
				default:
					break;
				}
			}
		};

		private void cleanPic() {
			ivFront.setImageResource(R.drawable.newreal_idcard);
			ivScene.setImageResource(R.drawable.newreal_scene);
		}

		/*
		 * 上传图片
		 */
		private void uploadAuthentication() {
			if (idFrontPic == null) {
				AnimationUtil.BtnSpecialAnmations1(this, ivFront, 500);
				Toast.makeText(getApplicationContext(), "请拍摄身份证正面照片",
						Toast.LENGTH_SHORT).show();
				// ToastCustom.showMessage(this, "请拍摄身份证正面照片");
				return;
			}

			if (scenePic == null) {
				AnimationUtil.BtnSpecialAnmations1(this, ivScene, 500);
				Toast.makeText(getApplicationContext(), "请拍摄情景照片",
						Toast.LENGTH_SHORT).show();
				// ToastCustom.showMessage(this, "请拍摄情景照片");
				return;
			}
			// String banknum = tv_banknum.getText().toString();
			// if(banknum==null||banknum.length()==0){
			//
			// }

			RealNameAuthTask task = new RealNameAuthTask();

			task.execute(realNameChangeKey, mercnum, idcardpic, custpic, "2");
		}
		
		/*
		 * 上传图片
		 */
		private void uploadAuthentication1() {
			if (idFrontPic == null) {
				AnimationUtil.BtnSpecialAnmations1(this, ivFront, 500);
				Toast.makeText(getApplicationContext(), "银行卡正面照片",
						Toast.LENGTH_SHORT).show();
				// ToastCustom.showMessage(this, "请拍摄身份证正面照片");
				return;
			}

			if (scenePic == null) {
				AnimationUtil.BtnSpecialAnmations1(this, ivScene, 500);
				Toast.makeText(getApplicationContext(), "请拍摄身份证银行卡情景照",
						Toast.LENGTH_SHORT).show();
				// ToastCustom.showMessage(this, "请拍摄情景照片");
				return;
			}
			if (bankpic == null) {
				AnimationUtil.BtnSpecialAnmations1(this, iv_idcard_scene, 500);
				Toast.makeText(getApplicationContext(), "请拍摄身份证背面照",
						Toast.LENGTH_SHORT).show();
				// ToastCustom.showMessage(this, "请拍摄情景照片");
				return;
			}
			// String banknum = tv_banknum.getText().toString();
			// if(banknum==null||banknum.length()==0){
			//
			// }

			RealNameAuthTask1 task = new RealNameAuthTask1();

			task.execute(realNameChangeKey, mercnum, idcardpic, custpic,bankpic, "2");
		}

		/**
		 * 补全资料(全部信息上传)
		 */
		private void completedata(String a) {
			banknum = tv_banknum.getText().toString().replace(" ", "");
			String username = et_register_name.getText().toString();
			String userid = et_register_cert_no.getText().toString();
			if (a != null && !a.equals("")) {
				if (a.equals("111")) {
					if (idFrontPic == null) {
						AnimationUtil.BtnSpecialAnmations1(this, ivFront, 500);
						Toast.makeText(getApplicationContext(), "请拍摄身份证正面照片",
								Toast.LENGTH_SHORT).show();
						// ToastCustom.showMessage(this, "请拍摄身份证正面照片");
						return;
					}

					if (scenePic == null) {
						AnimationUtil.BtnSpecialAnmations1(this, ivScene, 500);
						Toast.makeText(getApplicationContext(), "请拍摄情景照片",
								Toast.LENGTH_SHORT).show();
						// ToastCustom.showMessage(this, "请拍摄情景照片");
						return;
					}

					// if (bancardPic == null) {
					// AnimationUtil.BtnSpecialAnmations1(this, ivBank, 500);
					// ToastCustom.showMessage(this, "请拍摄银行卡图片照片");
					// return;
					// }
				} else if (a.equals("011")) {
					idcardpic = "";
					if (scenePic == null) {
						AnimationUtil.BtnSpecialAnmations1(this, ivScene, 500);
						Toast.makeText(getApplicationContext(), "请拍摄情景照片",
								Toast.LENGTH_SHORT).show();
						// ToastCustom.showMessage(this, "请拍摄情景照片");
						return;
					}

					// if (bancardPic == null) {
					// AnimationUtil.BtnSpecialAnmations1(this, ivBank, 500);
					// ToastCustom.showMessage(this, "请拍摄银行卡图片照片");
					// return;
					// }
				} else if (a.equals("001")) {
					idcardpic = "";
					custpic = "";
					// if (bancardPic == null) {
					// AnimationUtil.BtnSpecialAnmations1(this, ivBank, 500);
					// ToastCustom.showMessage(this, "请拍摄银行卡图片照片");
					// return;
					// }
				} else if (a.equals("110")) {
					bankpic = "";
					if (idFrontPic == null) {
						AnimationUtil.BtnSpecialAnmations1(this, ivFront, 500);
						Toast.makeText(getApplicationContext(), "请拍摄身份证正面照片",
								Toast.LENGTH_SHORT).show();
						// ToastCustom.showMessage(this, "请拍摄身份证正面照片");
						return;
					}

					if (scenePic == null) {
						AnimationUtil.BtnSpecialAnmations1(this, ivScene, 500);
						Toast.makeText(getApplicationContext(), "请拍摄情景照片",
								Toast.LENGTH_SHORT).show();
						// ToastCustom.showMessage(this, "请拍摄情景照片");
						return;
					}
				} else if (a.equals("100")) {
					custpic = "";
					bankpic = "";
					if (idFrontPic == null) {
						AnimationUtil.BtnSpecialAnmations1(this, ivFront, 500);
						Toast.makeText(getApplicationContext(), "请拍摄身份证正面照片",
								Toast.LENGTH_SHORT).show();
						// ToastCustom.showMessage(this, "请拍摄身份证正面照片");
						return;
					}
				} else if (a.equals("010")) {
					idcardpic = "";
					bankpic = "";
					if (scenePic == null) {
						AnimationUtil.BtnSpecialAnmations1(this, ivScene, 500);
						Toast.makeText(getApplicationContext(), "请拍摄情景照片",
								Toast.LENGTH_SHORT).show();
						// ToastCustom.showMessage(this, "请拍摄情景照片");
						return;
					}
				} else if (a.equals("101")) {
					custpic = "";
					if (idFrontPic == null) {
						AnimationUtil.BtnSpecialAnmations1(this, ivFront, 500);
						Toast.makeText(getApplicationContext(), "请拍摄身份证正面照片",
								Toast.LENGTH_SHORT).show();
						// ToastCustom.showMessage(this, "请拍摄身份证正面照片");
						return;
					}

					// if (banknum == null) {
					// AnimationUtil.BtnSpecialAnmations1(this, ivBank, 500);
					// ToastCustom.showMessage(this, "请拍摄银行卡图片照片");
					// return;
					// }

				}

				if (banknum == null || banknum.equals("")) {
					banknum = "";
				}
				if (username == null || username.equals("")) {
					username = "";
				}
				if (userid == null || userid.equals("")) {
					userid = "";
				}
			} else {
				if (idFrontPic == null) {
					AnimationUtil.BtnSpecialAnmations1(this, ivFront, 500);
					Toast.makeText(getApplicationContext(), "请拍摄身份证正面照片",
							Toast.LENGTH_SHORT).show();
					// ToastCustom.showMessage(this, "请拍摄身份证正面照片");
					return;
				}

				if (scenePic == null) {
					AnimationUtil.BtnSpecialAnmations1(this, ivScene, 500);
					Toast.makeText(getApplicationContext(), "请拍摄情景照片",
							Toast.LENGTH_SHORT).show();
					// ToastCustom.showMessage(this, "请拍摄情景照片");
					return;
				}
				
				if(txnsts.equals("1")){
					if (username == null || username.equals("")) {
						AnimationUtil.BtnSpecialAnmations1(this, et_register_name, 500);
						Toast.makeText(getApplicationContext(), "请输姓名",
								Toast.LENGTH_SHORT).show();
						// ToastCustom.showMessage(this, "请输入银行卡号");
						return;
					}
					if (userid == null || userid.equals("")) {
						AnimationUtil.BtnSpecialAnmations1(this, et_register_cert_no,
								500);
						Toast.makeText(getApplicationContext(), "请输入身份证",
								Toast.LENGTH_SHORT).show();
						// ToastCustom.showMessage(this, "请输入银行卡号");
						return;
					}
				}

				if (banknum == null || banknum.equals("")) {
					AnimationUtil.BtnSpecialAnmations1(this, tv_banknum, 500);
					Toast.makeText(getApplicationContext(), "请输入银行卡号",
							Toast.LENGTH_SHORT).show();
					// ToastCustom.showMessage(this, "请输入银行卡号");
					return;
				}
				if(banks.equals("")){
					AnimationUtil.BtnSpecialAnmations1(this, tv_bank, 500);
					Toast.makeText(getApplicationContext(), "请选择银行",
							Toast.LENGTH_SHORT).show();
					return;
				}

				// if (bancardPic == null) {
				// AnimationUtil.BtnSpecialAnmations1(this, ivBank, 500);
				// ToastCustom.showMessage(this, "请拍摄银行卡图片照片");
				// return;
				// }
			}
			RealNameAuthTaskAll realtask = new RealNameAuthTaskAll();
			realtask.execute(realNameChangeKey, mercnum, idcardpic, custpic,
					banknum, username, userid,bankProvinceid,bankCityid,bankareid);

		}

		/*
		 * 拍照返回图片
		 */
		private void takePicture(int code) {

			File dir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/DCIM/");// 设置临时文件的存放目录
			if (!dir.exists()) {
				dir.mkdir();
			}
			// 系统相机
			 Intent intent = new Intent(
			 android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			if (code == FRONT_CODE) {
				localTempImgFileName = idcardpic;

				Log.e("", "localTempImgFileName1 = = =" + localTempImgFileName);
			} else if (code == SCENS_CODE) {

				localTempImgFileName = custpic;
				Log.e("", "localTempImgFileName2 = = =" + localTempImgFileName);
			} else {
				localTempImgFileName = bankpic;
				Log.e("", "localTempImgFileName2 = = =" + localTempImgFileName);
			}
			File f = new File(dir, localTempImgFileName);
			Log.e("fileNam", dir.getAbsolutePath() + localTempImgFileName);
			Uri u = Uri.fromFile(f);
			 intent.putExtra(MediaStore.Images.Media.ORIENTATION, 10);
//			 intent.setDataAndType(u, "image/*"); // 格式
			 try {
//				 intent.putExtra("crop", "true"); // 发送裁剪信号

			} catch (Exception e) {
				// TODO: handle exception
			}
			 intent.putExtra("aspectX", 4); // X方向上的比例
			 intent.putExtra("aspectY", 3); // Y方向上的比例
			 intent.putExtra("outputX", 800f); // 裁剪区的宽
			 intent.putExtra("outputY", 400f); // 裁剪区的高
			 intent.putExtra("scale", false); // 是否保留比例
//			
//		        intent.putExtra("aspectX", 1);
//		        //同上Y 方向的比例. (注意： aspectX, aspectY ，两个值都需要为 整数，如果有一个为浮点数，就会导致比例失效。)
//		        intent.putExtra("aspectY", 1);
//		        //返回数据的时候的 X 像素大小。
//		        intent.putExtra("outputX", 80);
//		        //返回的时候 Y 的像素大小。
//		        intent.putExtra("outputY", 80);

			 intent.putExtra(MediaStore.EXTRA_OUTPUT, u); // 将URI指向相应的file:///
			 intent.putExtra("return-data", false); // 是否将数据保留在Bitmap中返回
//			 intent.putExtra("outputFormat",
//			 Bitmap.CompressFormat.JPEG.toString());
//			 intent.putExtra("noFaceDetection", true); // no face detection
//			 intent.putExtra("circleCrop", false); // 圆形裁剪区域
//			if(intentObj!=null&&intentObj.equals("Skactivity")){
//					code = 111;
//			}
//			 try {
//				 startActivityForResult(intent, code);
//			} catch (Exception e) {
				// TODO: handle exception
				Intent intents = new Intent(this, CameraActivity2.class);
				Bundle bundle = new Bundle();
				bundle.putString("fileName", localTempImgFileName);
				bundle.putString("tag", code + "");
	
				if(intentObj!=null&&intentObj.equals("Skactivity")){
					bundle.putString("tag1", "111");
				}
				intents.putExtras(bundle);
				startActivityForResult(intents, code);
//			}
			


		}

		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {

				case FRONT_CODE:
					// CameraActivity2.isdown = false;
					if(lin_3.getVisibility()==View.VISIBLE){
						
						pro_1.setImageBitmap(null);
						if (isshowpro) {
							pro_4.setVisibility(View.VISIBLE);
							AnimationUtil.JAnima(
									NewRealNameAuthenticationActivity.this, pro_4);
						}
						// CameraActivity2.isdown = false;
						take_front.setBackgroundResource(R.drawable.newreal_ok);
						AnimationUtil.ScaleAnimations(take_front);
						Drawable drawable = new BitmapDrawable(getBitmap(FRONT_CODE));
						ivFront.setBackgroundDrawable(drawable);
						ivFront.setImageBitmap(null);
						real_pointimg.setBackgroundResource(R.drawable.newreal_point);
						take_front.setEnabled(false);
						tv_propty1.setText("点击重拍");
					}else{
						pro_1.setImageBitmap(null);
						if (isshowpro) {
							pro_2.setVisibility(View.VISIBLE);
							AnimationUtil.JAnima(
									NewRealNameAuthenticationActivity.this, pro_2);
						}
						take_front.setBackgroundResource(R.drawable.newreal_ok);
						AnimationUtil.ScaleAnimations(take_front);
						Drawable drawable = new BitmapDrawable(getBitmap(FRONT_CODE));
						ivFront.setBackgroundDrawable(drawable);
						ivFront.setImageBitmap(null);
						real_pointimg.setBackgroundResource(R.drawable.newreal_point);
						take_front.setEnabled(false);
						tv_propty1.setText("点击重拍");
					}

					break;

				case SCENS_CODE:
					pro_2.setImageBitmap(null);
					// CameraActivity2.isdown = false;
					if (isshowpro) {
						pro_3.setVisibility(View.VISIBLE);
						AnimationUtil.JAnima(
								NewRealNameAuthenticationActivity.this, pro_3);
					}
					take_scene.setBackgroundResource(R.drawable.newreal_ok);
					AnimationUtil.ScaleAnimations(take_scene);
					ivScene.setImageBitmap(null);
					Drawable drawables = new BitmapDrawable(getBitmap(SCENS_CODE));
					ivScene.setBackgroundDrawable(drawables);
//					 iv_name_auth_scene1.setBackgroundDrawable(drawables);
//					 ivScene.setImageBitmap(getBitmap(SCENS_CODE));
					real_pointimg1.setBackgroundResource(R.drawable.newreal_point);
					take_scene.setEnabled(false);
					tv_propty2.setText("点击重拍");

					break;

				case BANK_CODE:
					pro_3.setImageBitmap(null);
					if (isshowpro) {
						pro_2.setVisibility(View.VISIBLE);
						AnimationUtil.JAnima(
								NewRealNameAuthenticationActivity.this, pro_2);
					}
					take_scene1.setBackgroundResource(R.drawable.newreal_ok);
					AnimationUtil.ScaleAnimations(take_scene1);
					Drawable drawable = new BitmapDrawable(getBitmap(BANK_CODE));
					iv_idcard_scene.setBackgroundDrawable(drawable);
					iv_idcard_scene.setImageBitmap(null);
					real_pointimg3.setBackgroundResource(R.drawable.newreal_point);
					take_scene1.setEnabled(false);
					tv_propty1.setText("点击重拍");
					break;

				default:
					break;
				}
			}
		};

		@Override
		public void onConfigurationChanged(Configuration config) {
			super.onConfigurationChanged(config);
		}

		/*
		 * 得到拍摄的图片
		 */
		private Bitmap getBitmap(int code) {
			File dir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/DCIM/" + localTempImgFileName);// 设置存放目录
			Log.e("", "      = = = = = = =dir = = = = =" + dir);
			Log.e("", "      = = = = = = =filenames = = = = ="
					+ localTempImgFileName);
			// File f = new File(dir.getAbsoluteFile(), localTempImgFileName);
			if (dir.isFile()) {
				Log.e("", "有文件");
				
//				Bitmap bitmap = getimage(Environment.getExternalStorageDirectory()
//						.getAbsolutePath() + "/DCIM/" + localTempImgFileName);
				Bitmap bitmap = getSmallBitmap(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/DCIM/" + localTempImgFileName);
				saveBitmap2file(bitmap, localTempImgFileName);
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
			if (code == FRONT_CODE) {
				custPicFile = dir;
				idFrontPic = "ok";
			} else if (code == SCENS_CODE) {
				idcardPicFile = dir;
				scenePic = "ok";
			} else {
				bankFile = dir;
				bancardPic = "ok";
			}
			return cbitmap;
		}

		@Override
		protected void onActivityResult(int arg0, int arg1, Intent data) {
			super.onActivityResult(arg0, arg1, data);
			if(arg1==2){
			  banks = data.getExtras().getString("result");
			  tv_bank.setText(banks);
			  String imgurl =data.getExtras().getString("img");
				Bitmap bit = null;
				try {
					 bit = GetImageUtil.iscace( im_bank,HttpUrls.HOST_POSM+imgurl);
				} catch (Exception e) {
					// TODO: handle exception\
					Log.e("", ""+e.toString());
				}
				if(bit!=null){
					im_bank.setImageBitmap(bit);
				}else{
				}
			}
			
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (null != bundle) {
					if (arg1 == 9) {
						bankProvinceid = bundle.getString("companyId");
						tv_issuing_bank.setText(bundle.getString("companyName"));
						tv_issuing_bank_com.setText("");
					} else if (arg1 == 8) {
						//bankCityid
						bankCityid = bundle.getString("companyId");
						tv_issuing_bank_com.setText(bundle.getString("companyName"));
					} else if (arg1 == 7) {
						bankareid = bundle.getString("companyId");
						tv_issuing_bank_area.setText(bundle.getString("companyName"));
					}
				}
			}

			if (arg1 != RESULT_OK) {
				return;
			} else if (arg0 == FRONT_CODE && arg1 == RESULT_OK) {
				handler.sendEmptyMessage(FRONT_CODE);
			} else if (arg0 == SCENS_CODE && arg1 == RESULT_OK) {
				handler.sendEmptyMessage(SCENS_CODE);
			} else {
				handler.sendEmptyMessage(BANK_CODE);
			}
			
//			if (data != null) {
//			Bundle bundle = data.getExtras();
//			if (null != bundle) {
//				if (arg1 == 9) {
//					bankProvinceid = bundle.getString("companyId");
//					tvBankProvince.setText(bundle.getString("companyName"));
//				} else if (arg1 == 8) {
//					bankCityid = bundle.getString("companyId");
//					tvBankCity.setText(bundle.getString("companyName"));
//				} else if (arg1 == 7) {
//					bankBranchid = bundle.getString("companyId");
////					tvBankBranch.setText(bundle.getString("companyName"));
//				}

		}
		
		/**
		 * 根据路径获得突破并压缩返回bitmap用于显示
		 * 
		 * @param imagesrc
		 * @return
		 */
		public static Bitmap getSmallBitmap(String filePath) {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);

			// Calculate inSampleSize
			options.inSampleSize = calculateInSampleSize(options, 480, 800);

			// Decode bitmap with inSampleSize set
			options.inJustDecodeBounds = false;

			return BitmapFactory.decodeFile(filePath, options);
		}
		
		/**
		 * 计算图片的缩放值
		 * 
		 * @param options
		 * @param reqWidth
		 * @param reqHeight
		 * @return
		 */
		public static int calculateInSampleSize(BitmapFactory.Options options,
				int reqWidth, int reqHeight) {
			// Raw height and width of image
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > reqHeight || width > reqWidth) {

				// Calculate ratios of height and width to requested height and
				// width
				final int heightRatio = Math.round((float) height
						/ (float) reqHeight);
				final int widthRatio = Math.round((float) width / (float) reqWidth);

				// Choose the smallest ratio as inSampleSize value, this will
				// guarantee
				// a final image with both dimensions larger than or equal to the
				// requested height and width.
				inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			}

			return inSampleSize;
		}

		/**
		 * 实名认证上传照片
		 * 
		 * @author liangge
		 * 
		 */
		class RealNameAuthTaskAll extends
				AsyncTask<String, Integer, HashMap<String, Object>> {

			@Override
			protected void onPreExecute() {
				showLoadingDialog("正在上传中。。。");
				super.onPreExecute();
			}

			@Override
			protected HashMap<String, Object> doInBackground(String... params) {
				String[] values = { params[0], params[1], params[2], params[3],
						params[4], params[5], params[6],params[7],params[8],params[9] };
				File[] fils = null;

				if (stateaudit == null) {
					fils = new File[] { custPicFile, idcardPicFile };
					// fils = files;
				} else {
					if (stateaudit.equals("100")) {
						File[] files = { custPicFile };
						fils = files;

					} else if (stateaudit.equals("110")) {
						File[] files = { custPicFile, idcardPicFile };
						fils = files;

					} else if (stateaudit.equals("101")) {
						File[] files = { custPicFile };
						fils = files;

					} else if (stateaudit.equals("011")) {
						File[] files = { idcardPicFile };
						fils = files;

					} else if (stateaudit.equals("001")) {
						File[] files = { bankFile };
						fils = files;

					} else if (stateaudit.equals("010")) {
						File[] files = { idcardPicFile };
						fils = files;

					} else if (stateaudit.equals("111")) {
						File[] files = { idcardPicFile, custPicFile };
						fils = files;
					} else {
						fils = new File[] { custPicFile, idcardPicFile };
						// fils = files;
					}
				}

				return NetCommunicate.getUpload(
						Integer.parseInt(realNameChangeKey),
						// 198110,
						values, fils);
			}

			@Override
			protected void onPostExecute(HashMap<String, Object> result) {
				loadingDialogWhole.dismiss();
				if (result != null) {
					if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
						Toast.makeText(getApplicationContext(),
								result.get(Entity.RSPMSG).toString(),
								Toast.LENGTH_SHORT).show();
						// ToastCustom.showMessage(
						// NewRealNameAuthenticationActivity.this,
						// result.get(Entity.RSPMSG).toString());
						// ((AppContext) getApplicationContext()).setSts("1");
						editor.putString("STS", "1");
						editor.commit();
						// Intent it = new
						// Intent(RealNameAuthenticationActivity.this,
						// MenuActivity.class);
						// startActivity(it);
						finish();
					} else {
						showSingleWarnDialog(result.get(Entity.RSPMSG).toString());
					}
				}
				super.onPostExecute(result);
			}
		}

		/**
		 * 实名认证上传照片
		 * 
		 * @author liangge
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
				String[] values = { params[0], params[1], params[2], params[3],
						params[4] };
				File[] files = { custPicFile, idcardPicFile };
				Log.e("", "realNameChangeKey====" + realNameChangeKey);
				return NetCommunicate.getUpload(
						Integer.parseInt(realNameChangeKey),
						// 198110,
						values, files);
			}

			@Override
			protected void onPostExecute(HashMap<String, Object> result) {
				loadingDialogWhole.dismiss();
				if (result != null) {
					if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
						Toast.makeText(getApplicationContext(),
								result.get(Entity.RSPMSG).toString(),
								Toast.LENGTH_SHORT).show();
						// ToastCustom.showMessage(
						// NewRealNameAuthenticationActivity.this,
						// result.get(Entity.RSPMSG).toString());
						if (intentObj!=null&&intentObj.equals("RequestCardInfoChangeActivity")) {
							MenuActivity.bankcardtag = true;
							// ((AppContext) getApplicationContext()).setSts("3");
//							editor.putString("STS", "3");
//							editor.commit();
						} else if (intentObj!=null&&intentObj.equals("RequestPhonenumberChenges")) {
//							cardtype();
							MenuActivity.phonetag = true;
							editor.putString("STS", "4");
							editor.commit();
							// ((AppContext) getApplicationContext()).setSts("4");
						} else {

						}
						finish();
						// if(intentObj.equals("RequestCardInfoChangeActivity")||intentObj.equals("RequestPhonenumberChenges")){
						// Intent it = new
						// Intent(RealNameAuthenticationActivity.this,UserActivity.class);
						// MenuActivity.context.finish();
						// startActivity(it);
						// }
						// Intent it = new
						// Intent(RealNameAuthenticationActivity.this,
						// MenuActivity.class);
						// startActivity(it);

					} else {
						showSingleWarnDialog(result.get(Entity.RSPMSG).toString());
					}
				}
				super.onPostExecute(result);
			}
		}
		
		/**
		 * 实名认证上传照片
		 * 
		 * @author liangge
		 * 
		 */
		class RealNameAuthTask1 extends
				AsyncTask<String, Integer, HashMap<String, Object>> {

			@Override
			protected void onPreExecute() {
				showLoadingDialog("正在上传照片中。。。");
				super.onPreExecute();
			}

			@Override
			protected HashMap<String, Object> doInBackground(String... params) {
				String[] values = { params[0], params[1], params[2], params[3],
						params[4],params[5] };
				File[] files = { custPicFile, idcardPicFile ,bankFile};
				Log.e("", "realNameChangeKey====" + realNameChangeKey);
				return NetCommunicate.getUpload(
						Integer.parseInt(realNameChangeKey),
						// 198110,
						values, files);
			}

			@Override
			protected void onPostExecute(HashMap<String, Object> result) {
				loadingDialogWhole.dismiss();
				if (result != null) {
					if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
						Toast.makeText(getApplicationContext(),
								result.get(Entity.RSPMSG).toString(),
								Toast.LENGTH_SHORT).show();
						finish();

					} else {
						showSingleWarnDialog(result.get(Entity.RSPMSG).toString());
					}
				}
				super.onPostExecute(result);
			}
		}


		/**
		 * 图片按比例大小压缩方法（根据路径获取图片并压缩）
		 * 
		 * @param srcPath
		 * @return
		 */
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
			newOpts.inPurgeable = true;
			try {
				bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
			} catch (Exception e) {
				// TODO: handle exception
				newOpts.inSampleSize = 1;
				bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
			}
			 return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
//			return bitmap;
		}

		/**
		 * 图片质量压缩
		 * 
		 * @param image
		 * @return
		 */
		
		private Bitmap compressImage(Bitmap image) {  
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中  
	        int options = 100;  
	        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩         
				  Log.e("", ""+"asdasdasdasdasdasd");

	            baos.reset();//重置baos即清空baos  
	            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中  
	            options -= 10;//每次都减少10  
	        }  
	        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中  
	        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片  
	        return bitmap;  
	    }

		/**
		 * 图片按比例大小压缩方法（根据Bitmap图片压缩）
		 * 
		 * @param image
		 * @return
		 */
		private Bitmap comp(Bitmap image) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
			if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
				baos.reset();// 重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
			}
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
			newOpts.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
			float hh = 600f;// 这里设置高度为800f
			float ww = 480f;// 这里设置宽度为480f
			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			int be = 1;// be=1表示不缩放
			if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
				be = (int) (newOpts.outWidth / ww);
			} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
				be = (int) (newOpts.outHeight / hh);
			}
			if (be <= 0)
				be = 1;
			newOpts.inSampleSize = be;// 设置缩放比例
			// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			isBm = new ByteArrayInputStream(baos.toByteArray());
			bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
			return bitmap;
			// return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
		}

		private static boolean saveBitmap2file(Bitmap bmp, String filename) {
			CompressFormat format = Bitmap.CompressFormat.JPEG;
			int quality = 40;
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
		 * 校验银行卡卡号
		 * 
		 * @param cardId
		 * @return
		 */
		private boolean checkBankCard(String cardId) {
			char bit = getBankCardCheckCode(cardId
					.substring(0, cardId.length() - 1));
			if (bit == 'N') {
				return false;
			}
			return cardId.charAt(cardId.length() - 1) == bit;
		}

		/**
		 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
		 * 
		 * @param nonCheckCodeCardId
		 * @return
		 */
		private char getBankCardCheckCode(String nonCheckCodeCardId) {
			if (nonCheckCodeCardId == null
					|| nonCheckCodeCardId.trim().length() == 0
					|| !nonCheckCodeCardId.matches("\\d+")) {
				// 如果传的不是数据返回N
				return 'N';
			}
			char[] chs = nonCheckCodeCardId.trim().toCharArray();
			int luhmSum = 0;
			for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
				int k = chs[i] - '0';
				if (j % 2 == 0) {
					k *= 2;
					k = k / 10 + k % 10;
				}
				luhmSum += k;
			}
			return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
		}

		
		private void cardtype(){
			new Thread(new Runnable() {

				@Override
				public void run() {
					
			        JSONObject jsonObj = new JSONObject();
			        try {
						jsonObj.put("PHONENUMBER_OLD", mobile);
						jsonObj.put("PHONENUMBER_NEW", mobileAgain);
						jsonObj.put("PASSWORD", pwd);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        String aa = null;
					try {
						aa = DESKey.AES_Encode(jsonObj.toString(),"f15f1ede25a2471998ee06edba7d2e29");
						aa = URLEncoder.encode(aa);
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String url = "http://wap.qianhaihg.com/?/mobile/user/qhepayChangeMobileNum/";
//					NetCommunicate.dopost(url+aa);
					
				}
			}).start();
		}
		
		class BussinessInfoTask extends
				AsyncTask<String, Integer, HashMap<String, Object>> {

			@Override
			protected void onPreExecute() {
			}

			@Override
			protected HashMap<String, Object> doInBackground(String... params) {
				String[] values = { params[0], params[1], params[2], params[3] };
				return NetCommunicate.getVerificationMidatc(
						HttpUrls.BUSSINESS_INFO, values);
			}

			@Override
			protected void onPostExecute(HashMap<String, Object> result) {
				if (result != null) {
					if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
						if(result.get("CARDID")!=null){
							et_register_cert_no.setText(result.get("CARDID").toString());
						}
						if(result.get("ACTNAM")!=null){
							et_register_name.setText(result.get("ACTNAM").toString());
						}
					} else {
//						Toast.makeText(getApplicationContext(),
//								result.get(Entity.RSPMSG).toString(),
//								Toast.LENGTH_SHORT);
					}
				}
				super.onPostExecute(result);
			}
		}
		
		
		Runnable run = new Runnable() {

			@Override
			public void run() {
				String[] values = { HttpUrls.INTHEQUERY+"","1"};
				ArrayList<HashMap<String, Object>> list = NetCommunicate.getList(HttpUrls.INTHEQUERY, values,HttpKeys.INTHEQUERY_BACK);
				Message msg = new Message();
				if (list != null) {
							mList.addAll(list);
						if(mList.size()<=0||mList==null){
							
							msg.what = 2;
						}else{
							msg.what = 1;
						}
						} else {
					msg.what = 3;
				}
				handlers.sendMessage(msg);
			}
		};

		private Handler handlers = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					
					Intent intent = new Intent(NewRealNameAuthenticationActivity.this,
							SelectListNameActivity.class);
					Bundle bundle = new Bundle();
					ArrayList carrier = new ArrayList();
					carrier.add(mList);
					bundle.putString("titleContent", "银行卡开户省份");
					bundle.putParcelableArrayList("carrier", carrier);
					intent.putExtras(bundle);
					startActivityForResult(intent, 9);
					overridePendingTransition(0, 0);
					mList.clear();
					break;
				case 2:
					
					Toast.makeText(getApplicationContext(),"获取列表失败",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(RateListActivity.this,
//							"没有获取到您的订单信息");
					break;
				case 3:
					Toast.makeText(getApplicationContext(),"网络异常,请检查您的网络",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(RateListActivity.this,
//							"订单信息获取失败");
					break;
				default:
					break;
				}
			};
		};
		
		
		Runnable run1 = new Runnable() {

			@Override
			public void run() {
				String[] values = { HttpUrls.INTHEQUERY+"",bankProvinceid};
				ArrayList<HashMap<String, Object>> list = NetCommunicate.getList(HttpUrls.INTHEQUERY, values,HttpKeys.INTHEQUERY_BACK);
				Message msg = new Message();
				if (list != null) {
							mList.addAll(list);
						if(mList.size()<=0||mList==null){
							
							msg.what = 2;
						}else{
							msg.what = 1;
						}
						} else {
					msg.what = 3;
				}
				handlers1.sendMessage(msg);
			}
		};

		private Handler handlers1 = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					
					Intent intent = new Intent(NewRealNameAuthenticationActivity.this,
							SelectListNameActivity.class);
					Bundle bundle = new Bundle();
					@SuppressWarnings("rawtypes")
					ArrayList carrier = new ArrayList();
					carrier.add(mList);
					bundle.putString("titleContent", "银行卡开户城市");
					bundle.putParcelableArrayList("carrier", carrier);
					intent.putExtras(bundle);
					startActivityForResult(intent, 8);
					overridePendingTransition(0, 0);
					mList.clear();
					break;
				case 2:
					
					Toast.makeText(getApplicationContext(),"获取列表失败",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(RateListActivity.this,
//							"没有获取到您的订单信息");
					break;
				case 3:
					Toast.makeText(getApplicationContext(),"网络异常,请检查您的网络",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(RateListActivity.this,
//							"订单信息获取失败");
					break;
				default:
					break;
				}
			};
		};
		
		
		Runnable run2 = new Runnable() {

			@Override
			public void run() {
				String[] values = { HttpUrls.INTHEQUERY+"",bankCityid};
				ArrayList<HashMap<String, Object>> list = NetCommunicate.getList(HttpUrls.INTHEQUERY, values,HttpKeys.INTHEQUERY_BACK);
				Message msg = new Message();
				if (list != null) {
							mList.addAll(list);
						if(mList.size()<=0||mList==null){
							
							msg.what = 2;
						}else{
							msg.what = 1;
						}
						} else {
					msg.what = 3;
				}
				handlers2.sendMessage(msg);
			}
		};

		private Handler handlers2 = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					
					Intent intent = new Intent(NewRealNameAuthenticationActivity.this,
							SelectListNameActivity.class);
					Bundle bundle = new Bundle();
					@SuppressWarnings("rawtypes")
					ArrayList carrier = new ArrayList();
					carrier.add(mList);
					bundle.putString("titleContent", "开户所在区/县");
					bundle.putParcelableArrayList("carrier", carrier);
					intent.putExtras(bundle);
					startActivityForResult(intent, 7);
					overridePendingTransition(0, 0);
					mList.clear();
					break;
				case 2:
					
					Toast.makeText(getApplicationContext(),"无区/县级,可不填",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(RateListActivity.this,
//							"没有获取到您的订单信息");
					break;
				case 3:
					Toast.makeText(getApplicationContext(),"网络异常,请检查您的网络",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(RateListActivity.this,
//							"订单信息获取失败");
					break;
				default:
					break;
				}
			};
		};
		
//		/**
//		 * 银行卡开户行查询
//		 * 
//		 * @author Administrator
//		 * 
//		 */
//		class BankProvinceTask1 extends AsyncTask<String, Integer, ProvinceEntity> {
//			
//			@Override
//			protected void onPreExecute() {
//				showLoadingCloseDialog("正在查询中。。。");
//				super.onPreExecute();
//			}
//
//			@Override
//			protected ProvinceEntity doInBackground(String... params) {
//				String[] values = { params[0], params[1], params[2], params[3] };
//				return NetCommunicate.getQueryProvince(HttpUrls.QUERY_BANK_NAME,
//						values);
//			}
//
//			@Override
//			protected void onPostExecute(ProvinceEntity result) {
//				loadingDialogClose.dismiss();
//				if (result != null) {
//					if (Entity.STATE_OK.equals(result.getRspcod())) {
//						list = result.list;
//						if (list != null) {
//							bankBranchid = null;
//							bankCityid = null;
//							Intent intent = new Intent(NewRealNameAuthenticationActivity.this,
//									SelectListNameActivity.class);
//							Bundle bundle = new Bundle();
//							ArrayList carrier = new ArrayList();
//							carrier.add(list);
//							bundle.putString("titleContent", "银行卡开户省份");
//							bundle.putParcelableArrayList("carrier", carrier);
//							intent.putExtras(bundle);
//							startActivityForResult(intent, 9);
//							overridePendingTransition(0, 0);
//						}else{
//							Toast.makeText(NewRealNameAuthenticationActivity.this,
//											"城市列表获取失败", Toast.LENGTH_SHORT);
//						}
//					} else {
//						Toast.makeText(NewRealNameAuthenticationActivity.this, result
//								.getRspmsg().toString(), Toast.LENGTH_SHORT);
//					}
//				}
//				super.onPostExecute(result);
//			}
//		}
		
		class BankCityTask extends AsyncTask<String, Integer, CityEntity> {

			@Override
			protected void onPreExecute() {
				showLoadingDialog("正在查询中...");
				super.onPreExecute();
			}

			@Override
			protected CityEntity doInBackground(String... params) {
				String[] values = { params[0], params[1], params[2], params[3] };
				return NetCommunicate
						.getQueryCity(HttpUrls.QUERY_BANK_CITY, values);
			}

			@Override
			protected void onPostExecute(CityEntity result) {
				loadingDialogWhole.dismiss();
				if (result != null) {

					if (Entity.STATE_OK.equals(result.getRspcod())) {
						if (result.list.size() == 0) {
							Toast.makeText(NewRealNameAuthenticationActivity.this,
									"没有该城市支行信息", Toast.LENGTH_SHORT);
							return;
						}

						Intent intent = new Intent(NewRealNameAuthenticationActivity.this,
								SelectListNameActivity.class);
						Bundle bundle = new Bundle();
						@SuppressWarnings("rawtypes")
						ArrayList carrier = new ArrayList();
						carrier.add(result.list);
						bundle.putString("titleContent", "银行卡开户城市");
						bundle.putParcelableArrayList("carrier", carrier);
						intent.putExtras(bundle);
						startActivityForResult(intent, 8);
						overridePendingTransition(0, 0);
					}else{
						Toast.makeText(NewRealNameAuthenticationActivity.this,
								result.getRspmsg(),Toast.LENGTH_SHORT);
					}
				}
				super.onPostExecute(result);
			}

		}
		
	}
