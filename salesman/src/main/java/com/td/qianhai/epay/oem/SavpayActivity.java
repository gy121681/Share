package com.td.qianhai.epay.oem;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import com.td.qianhai.epay.oem.EpayActivity.BussinessInfoTask;
import com.td.qianhai.epay.oem.EpayActivity.GetWalletInfo;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.dateutil.ScreenInfo;
import com.td.qianhai.epay.oem.dateutil.WheelMain;
import com.td.qianhai.epay.oem.mail.utils.IdCard;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.MyTextWatcher;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.oem.views.dialog.ImageDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.ImagedialogListener;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class SavpayActivity extends BaseActivity{
	
	private EditText e_pay2,e_pay4,e_pay5,e_pay6;
	
	private TextView tv_go,tv_card_propt,tv_card_propty1;
	
	private TextView balace_tv,bank_tv;
	
	private ImageView bank_img;
	
	private String mobile,mercnum;
	
	private String clslogno,ischeck,verifycode,bankcarddate;
	
	int a = 120,imgid,iscardtype;
	
	private String balace,bakname,url,idcards,idnames,cardtype;
	
	private String banknum =  "null";
	
	private boolean isok = false,isok1 = false,isok2 = false,isok3 = false,isok4 = false,isok5 = false,isok6 = false;
	
	private TextView bank_sp;
	
	private String[] c;
	
	private int[] d;
	
	private String[] b;
	
	private String isbind = "0";
	
	private CheckBox checs;
	
	private LayoutInflater inflater;
	
	private View view;
	
	private WheelMain wheelMain;
	
	private PopupWindow mPopupWindowDialog;
	
	private Button determine,cacel;
	
	private LinearLayout linerlayout;
	
	private ScrollView lin_1;
	
	private ImageDialog imgdialog;
	
	private TranslateAnimation taLeft, taRight, taTop, taBlow;
	
	public static Activity epayacticity;
	
	private OneButtonDialogWarn warnDialog;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.savpay_activity);
		AppContext.getInstance().addActivity(this);
		epayacticity = this;
//		mobile = ((AppContext)getApplication()).getMobile();
//		mercnum = ((AppContext)getApplication()).getMercNum();
		mercnum = MyCacheUtil.getshared(this).getString("MercNum", "");
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		Intent it = getIntent();
		 balace = it.getStringExtra("balance");
		 cardtype = it.getStringExtra("cardtype");
		 url = it.getStringExtra("url");
		 if(url==null||url.equals("")){
			 url = "";
		 }
		 idcards = it.getStringExtra("idcards");
		 idnames = it.getStringExtra("idnames");
//		 iscardtype = it.getIntExtra("iscardtype",0);
//		 imgid = it.getIntExtra("img", 0);
			GetWalletInfo  walletinfo = new GetWalletInfo();
			
			walletinfo.execute(HttpUrls.RICH_TREASURE_INFO + "",
					mobile);
		 
		 InitAnima();
		 
		 initview();
		
		 InitData();

	}

	private void InitData() {
		// TODO Auto-generated method stub

		b = new String[]{"农业银行","北京银行","中国银行","建设银行","光大银行","兴业银行","中信银行","广东发展银行","华夏银行",
				"工商银行","邮政储蓄银行","平安银行","浦东发展银行","上海银行"};
		c = new String[]{"ABCCREDIT","BCCBCREDIT","BOCCREDIT","CCBCREDIT","EVERBRIGHTCREDIT","CIBCREDIT","ECITICCREDIT",
				"GDBCREDIT","HXBCREDIT","ICBCCREDIT","PSBCCREDIT",
				"PINGANCREDIT","SPDBCREDIT","BOSHCREDIT"};
		d = new int[]{R.drawable.ps_abc,R.drawable.ps_bjb,R.drawable.ps_boc,R.drawable.ps_ccb,R.drawable.ps_cebb,
				R.drawable.ps_cib,R.drawable.ps_citic,
				R.drawable.ps_gdb,R.drawable.ps_hxb,R.drawable.ps_icbc,R.drawable.ps_psbc,
				R.drawable.ps_spa,R.drawable.ps_spdb,R.drawable.ps_sh};
	
	}

	private void inittime() {
		// TODO Auto-generated method stub
		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 在此处添加执行的代码
					// TODO Auto-generated method stub
				if(a!=0){
					Toast.makeText(getApplicationContext(),"",
							Toast.LENGTH_SHORT).show();
				}else{
//					inittime();
				}
				handler.postDelayed(this, 1000);// 20是延时时长
			}

		};
		handler.postDelayed(runnable, 1000);// 打开定时器，执行操作
	}

	private void initview() {
		
		((TextView) findViewById(R.id.tv_title_contre)).setText("支付中心");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		
		balace_tv = (TextView) findViewById(R.id.balaces_tv);
		checs = (CheckBox) findViewById(R.id.checs);
		checs.setChecked(true);
//		bank_tv = (TextView) findViewById(R.id.bank_tv);
//		cardtypr_tv = (TextView) findViewById(R.id.cardtypr_tv);
		bank_img = (ImageView) findViewById(R.id.bank_img);
		bank_sp = (TextView) findViewById(R.id.bank_sp);
		
		balace_tv.setText(balace);
//		((int) (Double.parseDouble(balace) /100)+"");
		
//		cardtypr_tv.setText("信用卡");
//		bank_layout = (LinearLayout) findViewById(R.id.bank_layout);
		
		e_pay2 = (EditText) findViewById(R.id.e_pay2);
//		e_pay2.setEnabled(false);
//		e_pay2.setText("13510086425");
		e_pay4 = (EditText) findViewById(R.id.e_pay4);
//		e_pay4.setEnabled(false);
//		e_pay4.setText("413026198902133910");
		e_pay5 = (EditText) findViewById(R.id.e_pay5);
//		e_pay5.setText("胡安强");
//		e_pay5.setEnabled(false);
		e_pay6 = (EditText) findViewById(R.id.e_pay6);
//		e_pay6.setText("6226230010917043");
//		e_pay6.setEnabled(false);
//		e_pay8.setText("2016");
//		e_pay8.setEnabled(false);
//		e_pay10.setText("002");
//		e_pay10.setEnabled(false);
//		e_pay12 = (EditText) findViewById(R.id.e_pay12);
		linerlayout = (LinearLayout) findViewById(R.id.linerlayout);
		lin_1 = (ScrollView) findViewById(R.id.lin_1);
		tv_card_propt = (TextView) findViewById(R.id.tv_card_propty);
		tv_card_propty1 = (TextView) findViewById(R.id.tv_card_propty1);
		e_pay6.addTextChangedListener(new MyTextWatcher(e_pay6));
		
		 
//		 if(idnames!=null&&!idnames.equals("")){
//			 e_pay5.setText(idnames);
//			 e_pay5.setEnabled(true);
//			 isok2 = true;
////			 e_pay6.setEnabled(true);
//		 }
//		 if(idcards!=null&&!idcards.equals("")){
//			 e_pay4.setText(idcards);
//			 e_pay4.setEnabled(true);
//			 isok1 = true;
//		 }
		
		tv_card_propty1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 imgdialog = new ImageDialog(SavpayActivity.this, R.style.CustomDialog,"卡背面后三位数字,即CVN2码",R.drawable.card_cvv_img,new ImagedialogListener() {
						
						@Override
						public void ImagedialogListener(View v) {
							// TODO Auto-generated method stub
							
							imgdialog.dismiss();
						}
					});
					 imgdialog.show();
			}
		});
		
		tv_card_propt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				 imgdialog = new ImageDialog(SavpayActivity.this, R.style.CustomDialog,"输入日期格式: 1215",R.drawable.card_propty_img,new ImagedialogListener() {
					
					@Override
					public void ImagedialogListener(View v) {
						// TODO Auto-generated method stub
						
						imgdialog.dismiss();
					}
				});
				 imgdialog.show();
				
			}
		});
		
		bank_sp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent it = new Intent(SavpayActivity.this,ChooseBankActivity.class);
				it.putExtra("tag", "1");
				startActivityForResult(it, 1);
//				bank_layout.setVisibility(View.VISIBLE);
//				mAdapter = null;
//				mAdapter = new MyAdapter();
//				gv.setAdapter(mAdapter);
//				mAdapter.notifyDataSetChanged();
			}
		});
		
//		e_pay8.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				
//				 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);			 
//				 if(imm.isActive()&&getCurrentFocus()!=null){
//					if (getCurrentFocus().getWindowToken()!=null) {
//					imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//					}			  
//				 }
//				
//				showdate();
//				
//			}
//
//		});
//		e_pay8.setOnFocusChangeListener(new OnFocusChangeListener() {
//			
//			@Override
//			public void onFocusChange(View arg0, boolean focus) {
//				// TODO Auto-generated method stub
//				if(focus){
//					 imgdialog = new ImageDialog(EpayActivity.this, R.style.CustomDialog,"输入日期格式: 201512",R.drawable.card_propty_img,new ImagedialogListener() {
//							
//							@Override
//							public void ImagedialogListener(View v) {
//								// TODO Auto-generated method stub
//								
//								imgdialog.dismiss();
//							}
//						});
//						 imgdialog.show();
//				}
//			}
//		});
//		
//		
//				e_pay10.setOnFocusChangeListener(new OnFocusChangeListener() {
//			
//			@Override
//			public void onFocusChange(View arg0, boolean focus) {
//				// TODO Auto-generated method stub
//				if(focus){
//					 imgdialog = new ImageDialog(EpayActivity.this, R.style.CustomDialog,"卡背面后三位数字,即CVN2码",R.drawable.card_cvv_img,new ImagedialogListener() {
//							
//							@Override
//							public void ImagedialogListener(View v) {
//								// TODO Auto-generated method stub
//								
//								imgdialog.dismiss();
//							}
//						});
//						 imgdialog.show();
//				}
//			}
//		});
		initinput();
		
		tv_go  = (TextView) findViewById(R.id.tv_go);
//		tv_go.setEnabled(false);
		
		tv_go.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(banknum.equals("null")){
					Toast.makeText(getApplicationContext(), "请先选择银行", Toast.LENGTH_SHORT).show();
					return;
				}else if(e_pay2.getText().toString()==null||e_pay2.getText().length()!=11){
					Toast.makeText(getApplicationContext(), "请输入11位手机号", Toast.LENGTH_SHORT).show();
					return;
				}else if(e_pay4.getText().toString()==null||e_pay4.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "请输入您本人身份证号码", Toast.LENGTH_SHORT).show();
					return;
				}else if(e_pay5.getText().toString()==null||e_pay5.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "请输入您姓名", Toast.LENGTH_SHORT).show();
					return;
				}else if(e_pay6.getText().toString()==null||e_pay6.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "请输入您本人信用卡号", Toast.LENGTH_SHORT).show();
					return;
				}
				if(new IdCard().isValidatedAllIdcard(e_pay4.getText().toString())){
				}else{
					if(e_pay4.getText().toString().length()<15){
						if(new IdCard().iscard(e_pay4.getText().toString())){

						}else{
							Toast.makeText(getApplicationContext(), "身份证有误", Toast.LENGTH_SHORT).show();
							return;
						}
						
					}else{
						Toast.makeText(getApplicationContext(), "身份证有误", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				
				
				
				String c_mobile = e_pay2.getText().toString();
				String c_num = e_pay4.getText().toString();
				String c_name = e_pay5.getText().toString();
				String c_card = e_pay6.getText().toString().replace(" ","");
				String b_year = null;
				String b_month = null;
				
//				

					
					OrderTask2 otask = new OrderTask2();
					
					otask.execute(HttpUrls.EPAYSAV + "", mobile ,"02",String.valueOf((int)(Double.parseDouble(balace) *100)),c_mobile,"IDCARD",c_num,c_name,c_card,bakname,banknum,"2",isbind,url);
					
			}
		});
		
		checs.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean ischeck) {
				// TODO Auto-generated method stub
				if(ischeck){
					isbind = "0";
				}else{
					isbind = "1";
				}
			}
		});
	}


//	public static int getWidth(View view) 
//	{ 
//	int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
//	int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
//	view.measure(w, h); 
//	return (view.getMeasuredWidth()); 
//	} 
//	
//	public static int getHeight(View view) 
//	{ 
//	int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
//	int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
//	view.measure(w, h); 
//	return (view.getMeasuredHeight()); 
//	} 
	
	public static void setLayoutY(View view,int y) 
	{ 
	MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams()); 
	margin.setMargins(margin.leftMargin,y, margin.rightMargin, y+margin.height); 
	LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(margin); 
	view.setLayoutParams(layoutParams); 
	} 


	
	private void showdate() {
		setLayoutY(lin_1, -100);
		
		Calendar calendar = Calendar.getInstance();

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = 1;
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		
		view = inflater.inflate(R.layout.choose_dialog, null);
		setPopupWindowDialog();

		ScreenInfo screenInfo = new ScreenInfo(SavpayActivity.this);
		wheelMain = new WheelMain(view, 1);
		wheelMain.screenheight = screenInfo.getHeight();
		wheelMain.initDateTimePicker(year, month, day, hour, min);

		if (mPopupWindowDialog != null) {
			mPopupWindowDialog.showAtLocation(
					findViewById(R.id.e_pay8), Gravity.BOTTOM
							| Gravity.CENTER_HORIZONTAL, 0, 0);
		}

		bottomBtn();
		
		
	}
	
	protected void setPopupWindowDialog() {
		// TODO Auto-generated method stub
		determine = (Button) view.findViewById(R.id.textview_dialog_album);
		cacel = (Button) view.findViewById(R.id.textview_dialog_cancel);

		mPopupWindowDialog = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindowDialog.setAnimationStyle(R.style.popwin_anim_style);  
		mPopupWindowDialog.setFocusable(true);
		mPopupWindowDialog.update();
		mPopupWindowDialog.setBackgroundDrawable(new BitmapDrawable(
				getResources(), (Bitmap) null));
//		mPopupWindowDialog.setBackgroundDrawable(getResources().getDrawable(R.drawable.air_city_button));
		mPopupWindowDialog.setOutsideTouchable(true);
	}
	
	
	protected void bottomBtn() {
		// TODO Auto-generated method stub
		determine.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				e_pay8.setText(wheelMain.getTime());
				if (mPopupWindowDialog != null
						&& mPopupWindowDialog.isShowing()) {
					mPopupWindowDialog.dismiss();
					setLayoutY(lin_1, 0);
				}
			}
		});

		cacel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mPopupWindowDialog != null
						&& mPopupWindowDialog.isShowing()) {
					mPopupWindowDialog.dismiss();
					setLayoutY(lin_1, 0);
				}
			}
		});
	}
	
	
	
//	private void initspinner() {
//		
////		ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_spinner_item,b);
////	      adapter.setDropDownViewResource(R.layout.sp_item);  
//	      //	</span>//只需在这里设置一句即可setDropDownViewResource  
////	      bank_sp.setAdapter(adapter);  
//		
//
//	      
//	      bank_sp.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long id) {
//				// TODO Auto-generated method stub
//				
//				banknum = c[(int)id];
//				bakname = b[(int)id];
//				imgid = d[(int)id];
//				bank_img.setImageResource(d[(int)id]);
//				if(!banknum.endsWith("null")){
//					e_pay2.setEnabled(true);
//				}
//				bank_tv.setText(b[(int)id]);
//				
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//	      bank_sp.performClick();
//	}

	private void initinput() {
		
		e_pay2.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()==11){
//					e_pay4.setEnabled(true);
					e_pay6.setEnabled(true);
					isok = true;
					if(isok3&&isok1&&isok2&&isok5&&isok4){
//						tv_go.setEnabled(true);
					}
				}else{
					isok = false;
//					tv_go.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		e_pay4.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					isok1 = true;
					e_pay5.setEnabled(true);
					if(isok&&isok3&&isok2&&isok5&&isok4){
//						tv_go.setEnabled(true);
					}
				}else{
					isok1 = false;
//					tv_go.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		e_pay5.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					e_pay6.setEnabled(true);
					isok2 = true;
					if(isok&&isok1&&isok3&&isok5&&isok4){
//						tv_go.setEnabled(true);
					}
				}else{
					isok2 = false;
//					tv_go.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		e_pay6.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					isok3 = true;
					if(isok&&isok1&&isok2&&isok5&&isok4){
//						tv_go.setEnabled(true);
					}
				}else{
					
//					tv_go.setEnabled(false);
					isok3 = false;
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
//		e_pay12.addTextChangedListener(new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
//				// TODO Auto-generated method stub
//				if(s.length()==6){
//					isok = true;
//				}else{
//					isok = false;
//				}
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
//					int arg3) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void afterTextChanged(Editable arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});

		
	}
	
	private void InitAnima() {
		// TODO Auto-generated method stub
		
//        push_left_in=AnimationUtils.loadAnimation(this, R.anim.push_left_in);  
//        push_right_in=AnimationUtils.loadAnimation(this, R.anim.push_right_in);  
//        slide_top_to_bottom=AnimationUtils.loadAnimation(this, R.anim.slide_top_to_bottom);  
//        slide_bottom_to_top=AnimationUtils.loadAnimation(this, R.anim.slide_bottom_to_top); 
		
		taLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		taRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		taTop = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		taBlow = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		taLeft.setDuration(1000);
		taRight.setDuration(1000);
		taTop.setDuration(1000);
		taBlow.setDuration(1000);
	}
	
	
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == 1) {
    		int id = data.getExtras().getInt("result");//得到新Activity 关闭后返回的数据
			banknum = c[(int)id];
			bakname = b[(int)id];
			imgid = d[(int)id];
			bank_img.setImageResource(d[(int)id]);
//			if(!banknum.endsWith("null")){
//				e_pay2.setEnabled(true);
//			}
			e_pay2.requestFocus();
			bank_sp.setText(b[(int)id]);
    	}
    }

	class OrderTask1 extends AsyncTask<String, Integer, HashMap<String, Object>> {
		private AlertDialog dialog;

		@Override
		protected void onPreExecute() {
			AlertDialog.Builder builder = new Builder(SavpayActivity.this);
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
					Intent it = new Intent(SavpayActivity.this,LastOderAvtivity.class);
					String idcard = e_pay4.getText().toString();
					String mobile = e_pay2.getText().toString();
					String name = e_pay5.getText().toString();
					it.putExtra("balance", balace);
					it.putExtra("mobile", mobile);
					it.putExtra("name", name);
					it.putExtra("imgid", imgid);
					it.putExtra("clslogno", clslogno);
					it.putExtra("idcard", idcard);
					startActivity(it);
				} else {
					ToastCustom.showMessage(SavpayActivity.this,
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT);
				}
			}
			super.onPostExecute(result);
		}
	}
	
	class OrderTask2 extends AsyncTask<String, Integer, HashMap<String, Object>> {
		private AlertDialog dialog;

		@Override
		protected void onPreExecute() {
			AlertDialog.Builder builder = new Builder(SavpayActivity.this);
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
					params[4], params[5], params[6], params[7],params[8],params[9],params[10],params[11],params[12],params[13]};
			return NetCommunicate.getPay(HttpUrls.EPAYSAV, values);
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
					Intent it = new Intent(SavpayActivity.this,LastOderAvtivity.class);
					String idcard = e_pay4.getText().toString();
					String mobile = e_pay2.getText().toString();
					String name = e_pay5.getText().toString();
					it.putExtra("balance", balace);
					it.putExtra("mobile", mobile);
					it.putExtra("name", name);
					it.putExtra("imgid", imgid);
					it.putExtra("clslogno", clslogno);
					it.putExtra("idcard", idcard);
					startActivity(it);
				} else {
					ToastCustom.showMessage(SavpayActivity.this,
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT);
				}
			}
			super.onPostExecute(result);
		}
	}
	
	/**
	 * 查看商户资料Task
	 * 
	 * @author Administrator
	 * 
	 */
	class BussinessInfoTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2],params[3] };
			return NetCommunicate.get(HttpUrls.BUSSINESS_INFO, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
		
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					if(result.get("CARDID")!=null){
						String ids = result.get("CARDID").toString();
						e_pay4.setText(ids);
						e_pay4.setEnabled(false);
					}
					if(result.get("ACTNAM")!=null){
						String acname = result.get("ACTNAM").toString();
						e_pay5.setText(acname);
						e_pay5.setEnabled(false);
					}
				} else {
				}
			}
			super.onPostExecute(result);
		}

	}
	
class GetWalletInfo extends AsyncTask<String , Integer, HashMap<String, Object>>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			
			String[] values = { params[0], params[1] };
			return NetCommunicate
					.getMidatc(HttpUrls.RICH_TREASURE_INFO, values);
			
		}
		String avaamt ="0";
		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			if (result != null) {
				if (result.get(Entity.RSPCOD)!=null&&result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					if (result.get("ISBRUSHOTHERSCARD") != null) {
						if (result.get("ISBRUSHOTHERSCARD").toString().equals("1")) {
							e_pay4.setEnabled(true);
							e_pay5.setEnabled(true);
						} else {
							BussinessInfoTask task = new BussinessInfoTask();
							task.execute(HttpUrls.BUSSINESS_INFO + "", mobile,
									"4", "0");
						}
					}
					
				} else {
					warnDialog = new OneButtonDialogWarn(SavpayActivity.this,
							R.style.CustomDialog, "提示", result.get(Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									finish();
									warnDialog.dismiss();
								}
							});
					if(warnDialog!=null){
						warnDialog.show();
					}
				}
			} else {
				Toast.makeText(getApplicationContext(), "fail",
						Toast.LENGTH_SHORT).show();
				finish();
			}
			super.onPostExecute(result);

		}
	}
}
