package com.td.qianhai.fragmentmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.share.app.entity.response.Constans;
import com.td.qianhai.epay.oem.AddRegionActivity;
import com.td.qianhai.epay.oem.MenuActivity;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.UserActivity;
import com.td.qianhai.epay.oem.advertising.AdGallery;
import com.td.qianhai.epay.oem.advertising.AdGalleryHelper;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.GetImageUtil;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.unlock.StringUtil;
import com.td.qianhai.epay.oem.unlock.UnlockLoginActivity;
import com.td.qianhai.epay.oem.views.SelectPicPopupWindow;
import com.td.qianhai.epay.oem.views.SystemBarTintManager;
import com.td.qianhai.epay.oem.views.dialog.ButtonDialogStyleNotice;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.TwoButtonDialogStyle2;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.epay.utils.AppManager;

public class FmMainActivity extends FragmentActivity {
	/**
	 * Called when the activity is first created.
	 */
	private RadioGroup rgs;
	public List<Fragment> fragments = new ArrayList<Fragment>();

//	private  TextView title_name;
	
	public String hello = "hello ";
	
	public static Activity context;
	
	private ScreenBroadcastReceiver mScreenReceiver;
	
	private TwoButtonDialogStyle2 doubleWarnDialog;
	
	public static  LinearLayout  my_remind;
	
	private Editor editor;
	
	private OneButtonDialogWarn warnDialog;
	
	//测试图片

	private RelativeLayout adContainer;
	private AdGalleryHelper adGalleryHelper;
	private AdGallery adGallery;
	private TextView bt_main_title_right,bt_main_title_right1,tv_level;
	private FragmentTabAdapter tabAdapter;
	private RadioButton tab_host_work,tab_host_production,tab_host_semi,tab_host_order;
	private List<RadioButton> radioview;
	private String tagsts,isretailers,isareaagent,issaleagt,isgeneralagent,isvip,personpic,phone;
	private SharedPreferences share;
	private ImageView headimg;
	private int mBorderWidth = 2;  
	private int mBorderColor = Color.parseColor("#f2f2f2"); 
	private SelectPicPopupWindow menuWindow;
	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	private static final String PHOTO_FILE_NAME = "photo.jpg";
	private String idcardpic = "";
	private File tempFile;
	private File cardPicFile ;
	private String content = "",time = "";
	private ButtonDialogStyleNotice noticedialog;
	private String PROVID ,CITYID ,AREAID ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);
		context = this;
		editor = MyCacheUtil.setshared(context);
		share = MyCacheUtil.getshared(context);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.bg_white);
		
		personpic = MyCacheUtil.getshared(this).getString("PERSONPIC", "");
		isretailers = MyCacheUtil.getshared(this).getString("ISRETAILERS", "");
		issaleagt = MyCacheUtil.getshared(this).getString("ISSALEAGT", "");
		isareaagent = MyCacheUtil.getshared(this).getString("ISAREAAGENT", "");
		isgeneralagent = MyCacheUtil.getshared(this).getString("ISGENERALAGENT", "");
		phone = MyCacheUtil.getshared(this).getString("Mobile", "");
		isvip = MyCacheUtil.getshared(this).getString("ISSENIORMEMBER", "");
		content = MyCacheUtil.getshared(this).getString("NCONTENT", "");
		time = MyCacheUtil.getshared(this).getString("NCREATETIM", "");
		tagsts = MyCacheUtil.getshared(this).getString("STS", "");
		PROVID = MyCacheUtil.getshared(this).getString("PROVIDs", "");
		CITYID = MyCacheUtil.getshared(this).getString("CITYIDs", "");
		AREAID = MyCacheUtil.getshared(this).getString("AREAIDs", "");
		if(tagsts.equals("0")){
			if(PROVID==null||PROVID.equals("")||CITYID==null||CITYID.equals("")||AREAID==null||AREAID.equals("")){
				Intent it = new Intent(FmMainActivity.this,AddRegionActivity.class);
				startActivity(it);
			}	
		}
		AppContext.getInstance().addActivity(context);
		
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if(content==null||content.equals("")){

				}else{
					SpannableString msps = new SpannableString("");
					showDoubleWarnDialogs(msps,content,time);
				}
			}
		}, 2000);
//		if(MainActivity.cache.getString("isone")==null||MainActivity.cache.getString("isone").equals("")){
//			MainActivity.cache.putString("isone","1");
//			MainActivity.cache.commit();
//		}else{
//		if(share.getBoolean("isfirst",false)==false){
//			warnDialog = new OneButtonDialogWarn(FmMainActivity.this,
//					R.style.CustomDialog, "提示", "理财需主动购买才会产生收益",
//					"确定", new OnMyDialogClickListener() {
//						@Override
//						public void onClick(View v) {
//							warnDialog.dismiss();
//						}
//					});
//			warnDialog.setCancelable(false);
//			warnDialog.setCanceledOnTouchOutside(false);
//			warnDialog.show();
//		}
		editor.putBoolean("isfirst", true);
		editor.commit();
//		}
		
//		Intent i = new Intent("android.bluetooth.device.action.STATE_CHANGED");
//		sendBroadcast(i);

//		startScreenBroadcastReceiver();
		headimg = (ImageView) findViewById(R.id.head_por);
        new GetImageUtil(this, headimg,HttpUrls.HOST_POSM+MyCacheUtil.getshared(this).getString(Constans.Login.PHOTO, ""));
		bt_main_title_right = (TextView) findViewById(R.id.bt_main_title_right);
		bt_main_title_right1 = (TextView) findViewById(R.id.bt_main_title_right1);
		tab_host_semi = (RadioButton) findViewById(R.id.tab_host_semi);
		tab_host_work = (RadioButton) findViewById(R.id.tab_host_work);
		tab_host_order = (RadioButton) findViewById(R.id.tab_host_order);
		tv_level = (TextView) findViewById(R.id.tv_level);
		tab_host_production = (RadioButton) findViewById(R.id.tab_host_production);
		radioview = new ArrayList<RadioButton>();
		radioview.add(tab_host_work);
		radioview.add(tab_host_order);
		radioview.add(tab_host_production);
		radioview.add(tab_host_semi);
	
		
		my_remind = (LinearLayout) findViewById(R.id.my_remind);
//		if(tagsts.equals("2")){
//			my_remind.setVisibility(View.VISIBLE);
//			new Handler().postDelayed(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					AnimationUtil.BtnSpecialAnmations1(FmMainActivity.this, my_remind,700);
//				}
//			}, 2000);
//			
//		}else{
//			my_remind.setVisibility(View.GONE);
//		}
		setfirstimg();
        SharedPreferences sharedPreferences = MyCacheUtil.getshared(this);
        String type = sharedPreferences.getString(Constans.Login.TYPE, "");
        if (TextUtils.equals(Constans.Role.NORMAl, type)) {
            if (TextUtils.equals(sharedPreferences.getString(Constans.Login.LEVEL, ""), Constans.MemberLevel.LEVEL_VIP)){
                tv_level.setText("高级会员");
            } else {
                tv_level.setText("普通会员");
            }
        } else if (TextUtils.equals(Constans.Role.AREA_MANAGER, type)) {
                tv_level.setText("区域经理");
        } else if (TextUtils.equals(Constans.Role.AGENT, type)) {
            String agentType = sharedPreferences.getString(Constans.Login.AGENTTYPE, "");
            if (TextUtils.equals(Constans.AgentType.TYPE_PROVINCE, agentType)) {
                tv_level.setText("省级代理");
            } else if (TextUtils.equals(Constans.AgentType.TYPE_CITY, agentType)) {
                tv_level.setText("市级代理");
            } else if (TextUtils.equals(Constans.AgentType.TYPE_AREA, agentType)) {
                tv_level.setText("区级代理");
            } else {
                tv_level.setText("");
            }
        }

//		if(isareaagent!=null&&isareaagent.equals("1")){
//			tv_level.setText("省级代理");
//		}else if(isareaagent!=null&&isareaagent.equals("2")){
//			tv_level.setText("市级代理");
//		}else if(isareaagent!=null&&isareaagent.equals("3")){
//			tv_level.setText("区级代理");
//		}else{
//			if(isgeneralagent.equals("1")){
//				tv_level.setText("服务商");
//			}else{
//				if(issaleagt.equals("1")){
//					tv_level.setText("服务商");
//				}else{
//					if(isretailers.equals("1")){
//						tv_level.setText("服务商");
//					}else{
////						if(tagsts.equals("3")||tagsts.equals("0")||tagsts.equals("4")){
//							if(isvip.equals("1")){
//								tv_level.setText("高级会员");
//							}else{
//								tv_level.setText("普通会员");
//							}
//
////						}else{
////							tv_level.setText("未实名认证");
////						}
//					}
//				}
//			}
//		}
		
		headimg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				setheadimg();
			}
		});

		bt_main_title_right.setVisibility(View.GONE);
//		bt_main_title_right1.setVisibility(View.VISIBLE);
		bt_main_title_right1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				SpannableString msps = new SpannableString("确定退出?");
				showDoubleWarnDialog(msps);
			}
		});
//		title_name= (TextView) findViewById(R.id.title_names);xl
		fragments.add(new MenuActivity());
		fragments.add(new TabDFm());
		fragments.add(new TabBFm());
		fragments.add(new TabCFm());
		// fragments.add(new TabEFm());
		rgs = (RadioGroup) findViewById(R.id.tabs_rg);

		 tabAdapter = new FragmentTabAdapter(this, fragments,
				R.id.tab_content, rgs,radioview);
		tabAdapter
				.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
					@Override
					public void OnRgsExtraCheckedChanged(RadioGroup radioGroup,
							int checkedId, int index) {
						System.out.println("Extra---- " + index
								+ " checked!!! ");
					}
				});
	}
	
	
	@TargetApi(19) 
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		AppContext.isunlock = false;
		
	}

	private boolean isExit;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			if(tabAdapter.getCurrentTab()==0){
				if (!isExit) {
					isExit = true;
					Toast.makeText(getApplicationContext(), "再按一次退出程序",
							Toast.LENGTH_SHORT).show();
					new Handler().postDelayed(new Runnable() {
						public void run() {
							isExit = false;
						}
					}, 2000);
					;
					return false;
				} else {
					((AppContext) getApplication()).setCustId(null); // 商户ID赋为空
					((AppContext) getApplication()).setPsamId(null);
					((AppContext) getApplication()).setMacKey(null);
					((AppContext) getApplication()).setPinKey(null);
					((AppContext) getApplication()).setMerSts(null);
					((AppContext) getApplication()).setMobile(null);
					((AppContext) getApplication()).setEncryPtkey(null);
					((AppContext) getApplication()).setStatus(null);
					((AppContext) getApplication()).setCustPass(null);
					((AppContext) getApplication()).setVersionSerial(null);
					AppContext.getInstance().setStateaudit(null);
					editor.putString("isopem", "-1");
					editor.putString("MERSTS","");
					editor.putString("AGENTID","");
					editor.putString("PERSONPIC","");
					editor.commit();
					AppManager.getAppManager().AppExit(FmMainActivity.this);
					AppContext.getInstance().exit();
					finish();
			}
			
			}else{
				tabAdapter.showTab(0);
				tab_host_work.setChecked(true);
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
//	/**
//	 * 监听黑屏
//	 *
//	 */
	public class ScreenBroadcastReceiver extends BroadcastReceiver{
		 private String action = null;
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
	        action = intent.getAction();
	        if (Intent.ACTION_SCREEN_ON.equals(action)) {
	            // 开屏
	        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) { 
	        	if(StringUtil.isEmpty(share.getString("usermobile",""))){
	        		
	        	}else{
	        		
	        		
	        		Log.e("", "isunlock = = "+AppContext.isunlock);
	        		if(!AppContext.isunlock){
			        	Intent it = new Intent(FmMainActivity.this,UnlockLoginActivity.class);
			        	it.putExtra("islogin", "1");
			        	AppContext.isunlock = true;
			        	startActivity(it);
	        		}

	        	}
	            // 锁屏
	        } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
	            // 解锁
	        }
			
		}
	}
	
	
	private void startScreenBroadcastReceiver() {
//		 mScreenReceiver = new ScreenBroadcastReceiver();
//	    IntentFilter filter = new IntentFilter();
//	    filter.addAction(Intent.ACTION_SCREEN_ON);
//	    filter.addAction(Intent.ACTION_SCREEN_OFF);
//	    filter.addAction(Intent.ACTION_USER_PRESENT);
//	    getApplication().registerReceiver(mScreenReceiver, filter);
	}
//	
	
	public void setheadimg(){
		menuWindow = new SelectPicPopupWindow(context, itemsOnClick);
		LinearLayout lin = (LinearLayout)context.findViewById(R.id.fm_main);
		menuWindow.showAtLocation(lin,
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		menuWindow.setbutton2("图库");
		menuWindow.settitile();
//	    WindowManager.LayoutParams params=this.getWindow().getAttributes();  
//	    params.alpha=0.7f;  
//	    this.getWindow().setAttributes(params);
	}
	
	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:
				camera();
				break;
			case R.id.btn_pick_photo:
				gallery();
				break;
			default:
				break;
			}
		}
	};
	
	
	/*
	 * 从相册获取
	 */
	public void gallery() {
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}
	/*
	 * 从相机获取
	 */
	public void camera() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		// 判断存储卡是否可以用，可用进行存储
//		if (hasSdcard()) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
//		}
		startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}
	
	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
	
	private void setfirstimg(){
		Bitmap bitmap = null;
//		
		if (personpic==null||personpic.equals("")) {
//			Resources res = getResources();
//			BitmapFactory.Options opts=new BitmapFactory.Options();
//			opts.inTempStorage = new byte[100 * 1024];
//			//3.设置位图颜色显示优化方式
//			//ALPHA_8：每个像素占用1byte内存（8位）
//			//ARGB_4444:每个像素占用2byte内存（16位）
//			//ARGB_8888:每个像素占用4byte内存（32位）
//			//RGB_565:每个像素占用2byte内存（16位）
//			//Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。但同样的，占用的内存//也最大。也就意味着一个像素点占用4个字节的内存。我们来做一个简单的计算题：3200*2400*4 bytes //=30M。如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。
//			opts.inPreferredConfig = Bitmap.Config.RGB_565;
//			//4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
//			opts.inPurgeable = true;
//			//5.设置位图缩放比例
//			//width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
//			opts.inSampleSize = 4;
//			//6.设置解码位图的尺寸信息
//			opts.inInputShareable = true; 
//			bitmap = BitmapFactory.decodeResource(res, R.drawable.share_s_public_head_small_big,opts);
//			
//			headimg.setImageBitmap(getRoundedCornerBitmap(bitmap));
		}else{
			new GetImageUtil(this, headimg,HttpUrls.HOST_POSM+personpic);
//			new ImageLoadTask(this).execute(HttpUrls.HOST_POSM+personpic);
		}

		}
	
	private Bitmap getbitmap(String url){
		 InputStream is = null;
			try {
				is = new FileInputStream(url);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		BitmapFactory.Options opts=new BitmapFactory.Options();
		opts.inTempStorage = new byte[100 * 1024];
		//3.设置位图颜色显示优化方式
		//ALPHA_8：每个像素占用1byte内存（8位）
		//ARGB_4444:每个像素占用2byte内存（16位）
		//ARGB_8888:每个像素占用4byte内存（32位）
		//RGB_565:每个像素占用2byte内存（16位）
		//Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。但同样的，占用的内存//也最大。也就意味着一个像素点占用4个字节的内存。我们来做一个简单的计算题：3200*2400*4 bytes //=30M。如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		//4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
		opts.inPurgeable = true;
		//5.设置位图缩放比例
		//width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
		opts.inSampleSize = 4;
		//6.设置解码位图的尺寸信息
		opts.inInputShareable = true; 
		//7.解码位图
		Bitmap btp =BitmapFactory.decodeStream(is,null, opts);    
		Drawable drawable = new BitmapDrawable(btp);
		try {
			if(is!=null){
				is.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return btp;
	} 
		
		/**
		 * 圆形图片裁剪
		 * @param bitmap
		 * @return
		 */
		private  Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
	        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
	        Canvas canvas = new Canvas(output);

	        final Paint paint = new Paint();
	        //保证是方形，并且从中心画
	        int width = bitmap.getWidth();
	        int height = bitmap.getHeight();
	        int w;
	        int deltaX = 0;
	        int deltaY = 0;
	        if (width <= height) {
	            w = width;
	            deltaY = height - w;
	        } else {
	            w = height;
	            deltaX = width - w;
	        }
	        final Rect rect = new Rect(deltaX, deltaY, w, w);
	        final RectF rectF = new RectF(rect);

	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        //圆形，所有只用一个
	        
	        int radius = (int) (Math.sqrt(w * w * 2.0d) / 2);
	        canvas.drawRoundRect(rectF, radius, radius, paint);

	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        canvas.drawBitmap(bitmap, rect, rect, paint);
	        drawBorder(canvas, width, height); 
	        return output;
	    }
		
		private void drawBorder(Canvas canvas, final int width, final int height) {  
	        if (mBorderWidth == 0) {  
	            return;  
	        }  
	        final Paint mBorderPaint = new Paint();  
	        mBorderPaint.setStyle(Paint.Style.STROKE);  
	        mBorderPaint.setAntiAlias(true);  
	        mBorderPaint.setColor(mBorderColor);  
	        mBorderPaint.setStrokeWidth(mBorderWidth);  
	        /** 
	         * 坐标x：view宽度的一般 坐标y：view高度的一般 半径r：因为是view的宽度-border的一半 
	         */  
	        canvas.drawCircle(width >> 1, height >> 1, (width - mBorderWidth) >> 1, mBorderPaint);  
	        canvas = null;  
	    }  
		
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		getApplication().unregisterReceiver(mScreenReceiver);  
	}
	/***
	 * 双按钮提示dialog
	 * 
	 * @param msg
	 */
	protected void showDoubleWarnDialog(SpannableString msg) {
		 doubleWarnDialog = new TwoButtonDialogStyle2(context,
				R.style.CustomDialog, "提示", msg, "退出帐号", "退出应用",
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doubleWarnOnClick(v);
					}
				});
		doubleWarnDialog.setCancelable(true);
//		doubleWarnDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode,
//					KeyEvent event) {
//				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
//					return true;
//				} else {
//					return true; // 默认返回 false
//				}
//			}
//		});
		doubleWarnDialog.setCanceledOnTouchOutside(true);
		doubleWarnDialog.show();
	}
	
	protected void doubleWarnOnClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
			doubleWarnDialog.dismiss();
			AppContext.getInstance().setCustId(null);
			AppContext.getInstance().setPsamId(null);
			AppContext.getInstance().setMacKey(null);
			AppContext.getInstance().setPinKey(null);
			AppContext.getInstance().setMerSts(null);
			AppContext.getInstance().setMobile(null);
			AppContext.getInstance().setEncryPtkey(null);
			AppContext.getInstance().setStatus(null);
			AppContext.getInstance().setCustPass(null);
			AppContext.getInstance().setVersionSerial(null);
			AppContext.getInstance().setStateaudit(null);
			AppContext.getInstance().exit();
		break;
		case R.id.btn_right:
			AppContext.getInstance().setCustId(null);
			AppContext.getInstance().setPsamId(null);
			AppContext.getInstance().setMacKey(null);
			AppContext.getInstance().setPinKey(null);
			AppContext.getInstance().setMerSts(null);
			AppContext.getInstance().setMobile(null);
			AppContext.getInstance().setUsername(null);
			AppContext.getInstance().setEncryPtkey(null);
			AppContext.getInstance().setStatus(null);
			AppContext.getInstance().setCustPass(null);
			AppContext.getInstance().setVersionSerial(null);
			AppContext.getInstance().setStateaudit(null);
			doubleWarnDialog.dismiss();
			AppContext.getInstance().exit();
			
			Intent it = new Intent(context,UserActivity.class);
			
			startActivity(it);
			break;
		default:
			break;
		}
	}
	


//	@Override
//	protected void onRestart() {
//		// TODO Auto-generated method stub
//		super.onRestart();
//		if(tabAdapter.getCurrentTab()!=0){
//			
//		}
//
//	}

//	@Override
//	protected void onResume() {
//		isForeground = true;
//		super.onResume();
//	}
//
//
//	@Override
//	protected void onPause() {
//		isForeground = false;
//		super.onPause();
//	}
//
//
//	@Override
//	protected void onDestroy() {
//		unregisterReceiver(mMessageReceiver);
//		super.onDestroy();
//	}
	
//	/**
//	 * 测试：使用AdGlleryHelper来实现对外提供自定义的布局视图
//	 */
//	public void realizeFunc2(){
//		adContainer = (RelativeLayout)findViewById(R.id.ad_container);
//		adGalleryHelper = new AdGalleryHelper(this, dataids, 3000,true);
//		adContainer.addView(adGalleryHelper.getLayout());
//		adGallery = adGalleryHelper.getAdGallery();
//		adGallery.setAdOnItemClickListener(this);
//	}
//	
//	/**
//	 * 实现AdGallery的OnAdItemOnClickListener
//	 */
//	@Override
//	public void setItemClick(int position) {
//		System.out.println("you had clicked position="+position);
		//调用系统浏览器访问对应广告图的链接
//		Intent intent= new Intent();        
//	    intent.setAction("android.intent.action.VIEW");    
//	    Uri content_url = Uri.parse(data[position].getLinkUrl());   
//	    intent.setData(content_url);  
//	    startActivity(intent);
//	}
//	@Override
//	public void onDestroy() {
//		if(adGalleryHelper!=null){
//			adGalleryHelper.stopAutoSwitch();
//		}
//		super.onDestroy();
//	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		idcardpic = phone +"_" + getStringDateMerge() + ".jpg";
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data != null) {
				
				
				// 得到图片的全路径
				Uri uri = data.getData();
				crop(uri);
				

				
				
			}

		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (hasSdcard()) {
			 tempFile = new File(Environment.getExternalStorageDirectory(),
						PHOTO_FILE_NAME);
				crop(Uri.fromFile(tempFile));
			} else {
				Toast.makeText(context, "未找到存储卡，无法存储照片！", 0).show();
			}

		} else if (requestCode == PHOTO_REQUEST_CUT) {
			if (data != null) {

			
			try {
				Bitmap bitmap = data.getParcelableExtra("data");
				headimg.setImageBitmap(getRoundedCornerBitmap(bitmap));
//				boolean delete = tempFile.delete();
				
				File file = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/myImage/");
				file.mkdirs();// 创建文件夹
				String fileName = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/myImage/"+idcardpic;
				FileOutputStream b = null;
				try {
					b = new FileOutputStream(fileName);
					Log.e("", ""+fileName);

					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						b.flush();
						b.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				
				cardPicFile = new File(Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/myImage/",idcardpic);
				
				RealNameAuthTask task = new RealNameAuthTask();

				task.execute(HttpUrls.AVATARUPLOAD + "",phone, idcardpic);
			}
		}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public String getStringDateMerge() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	private void crop(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 125);
		intent.putExtra("outputY", 125);
		// 图片格式
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}
	
	
	/***
	 * 双按钮提示dialog
	 * 
	 * @param msg
	 */
	protected void showDoubleWarnDialogs(SpannableString msg,String content,String info) {
		noticedialog = new ButtonDialogStyleNotice(context,
				R.style.CustomDialog, "提示", msg,content,info,"我知道了", "我知道了",
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doubleWarnOnClicks(v);
					}
				});
		noticedialog.setCancelable(false);
//		doubleWarnDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode,
//					KeyEvent event) {
//				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
//					return true;
//				} else {
//					return true; // 默认返回 false
//				}
//			}
//		});
		noticedialog.setCanceledOnTouchOutside(false);
		noticedialog.show();
	}
	
	
	protected void doubleWarnOnClicks(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
			noticedialog.dismiss();
		break;
		case R.id.btn_right:
			noticedialog.dismiss();
			break;
		default:
			break;
		}
	}
	
	
	class RealNameAuthTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2] };
			File[] files = { cardPicFile };
			return NetCommunicate.getUpload(HttpUrls.AVATARUPLOAD,
			// 198110,
					values, files);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					Toast.makeText(getApplicationContext(),
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(getApplicationContext(),
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();
				}
			}
			super.onPostExecute(result);
		}
	}
	
}
