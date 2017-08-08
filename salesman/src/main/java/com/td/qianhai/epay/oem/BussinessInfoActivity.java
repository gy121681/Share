package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.CityEntity;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.ProvinceEntity;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.oem.views.dialog.EidtDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnEditDialogChlicListener;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.td.qianhai.epay.utils.AppManager;
import com.td.qianhai.epay.utils.DateUtil;

/***
 * 商户信息显示界面
 * 
 * @author liangge
 * 
 */
public class BussinessInfoActivity extends BaseActivity implements OnClickListener{
	/** 开户姓名、银行卡号、银行名称、身份证号、手机号码、实名认证状态、商户ID */
	private TextView tvName, tvCardId, tvBankName, tvId, tvMobile, tvStatus,tv_region_agt,
			tvCustonId;
	/** 商户ID、终端号 */
	private String custId, isvip,psamId,bankProvinceid = "", bankCityid = "",bankareid = "",mercnum,cardData,track2,track3,isretailers,issaleagt,isgeneralagent,tagsts;
	/** 关闭当前Activity */
	private TextView btnClose, tvContent,tv_issuing_com,my_frend,tv_bank_name_address,tv_issuing_bank,tv_issuing_bank_com,tv_feerat,tv_feerate;
	
	private  EidtDialog doubleWarnDialogs;
	
	private OneButtonDialogWarn warnDialog;
	
	private ArrayList<HashMap<String, Object>> mList;
	
	private LinearLayout lin_adress,lin_feerate,lin_region_agt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.bussiness_info_new);
		AppContext.getInstance().addActivity(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//		custId = ((AppContext) this.getApplication()).getCustId();
		tagsts = MyCacheUtil.getshared(this).getString("STS", "");
		custId = MyCacheUtil.getshared(this).getString("CustId", "");
		isvip = MyCacheUtil.getshared(this).getString("ISSENIORMEMBER", "");
		mercnum = MyCacheUtil.getshared(this).getString("MercNum","");
		isretailers = MyCacheUtil.getshared(this).getString("ISRETAILERS", "");
		issaleagt = MyCacheUtil.getshared(this).getString("ISSALEAGT", "");
		lin_region_agt = (LinearLayout) findViewById(R.id.lin_region_agt);
		isgeneralagent = MyCacheUtil.getshared(this).getString("ISGENERALAGENT", "");
		tv_bank_name_address = (TextView) findViewById(R.id.tv_bank_name_address);
		lin_feerate = (LinearLayout) findViewById(R.id.lin_feerate);
		tv_feerat = (TextView) findViewById(R.id.tv_feerat);
		tv_region_agt = (TextView) findViewById(R.id.tv_region_agt);
		tv_feerate = (TextView) findViewById(R.id.tv_feerate);
		psamId = ((AppContext) getApplication()).getPsamId();
		tv_issuing_bank = (TextView) findViewById(R.id.tv_issuing_bank);
		tv_issuing_com = (TextView) findViewById(R.id.tv_issuing_com);
		tv_issuing_com.setOnClickListener(this);
		tv_issuing_bank.setOnClickListener(this);
		tv_issuing_bank_com = (TextView) findViewById(R.id.tv_issuing_bank_com);
		tv_issuing_bank_com.setOnClickListener(this);
		lin_adress = (LinearLayout) findViewById(R.id.lin_adress);
		tvName = (TextView) findViewById(R.id.tv_bussiness_info_name);
		tvCardId = (TextView) findViewById(R.id.tv_bussiness_info_card_id);
		tvBankName = (TextView) findViewById(R.id.tv_bussiness_info_bank_name);
		tvId = (TextView) findViewById(R.id.tv_bussiness_info_id);
		my_frend = (TextView) findViewById(R.id.my_frend);
		tvMobile = (TextView) findViewById(R.id.tv_bussiness_info_mobile);
		tvStatus = (TextView) findViewById(R.id.tv_bussiness_info_status);
		tvCustonId = (TextView) findViewById(R.id.tv_bussiness_info_custon);
		tvContent = (TextView) findViewById(R.id.tv_title_contre);
		tvContent.setText("用户信息");
		 mList = new ArrayList<HashMap<String,Object>>();
		btnClose = (TextView) findViewById(R.id.bt_title_left);
		my_frend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showdilog();
			}
		});
		tv_bank_name_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				if(tagsts.equals("0")){
//					showpop();
//				}
			}
		});
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (DateUtil.isFastDoubleClick()) {
					return;
				} else {
					backActivity();
				}
			}

		});
		final BussinessInfoTask task = new BussinessInfoTask();
		task.execute(HttpUrls.BUSSINESS_INFO + "", custId, "4","0");

		new Thread() {
			public void run() {
				try {
					/**
					 * 在这里你可以设置超时的时间 切记：这段代码必须放到线程中执行，因为不放单独的线程中执行的话该方法会冻结UI线程
					 * 直接导致onPreExecute()方法中的弹出框不会立即弹出。
					 */
					task.get(30000, TimeUnit.MILLISECONDS);
				} catch (TimeoutException e) {
					// 请求超时
					/**
					 * 如果在doInbackground中的代码执行的时间超出10000秒则会出现这个异常。
					 * 所以这里就成为你处理异常操作的唯一途径。
					 * 
					 * 备注：这里是不能够处理UI操作的，如果处理UI操作则会出现崩溃异常。
					 * 你可以写一个Handler，向handler发送消息然后再Handler中接收消息并处理UI更新操作。
					 */
					Toast.makeText(getApplicationContext(), "请求服务器超时,请重新操作",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(BussinessInfoActivity.this,
//							"请求服务器超时,请重新操作!");
					task.cancel(true);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "系统错误,请重新操作",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(BussinessInfoActivity.this,
//							"系统错误,请重新操作!");
					task.cancel(true);
				}
			};
		}.start();

	}
	
	private void initcarddata(){
//		cardData = tv_banknum.getText().toString();
//		bankCardNo.setText(cardData.split("d")[0]);
//
//		bankCardNo.setEnabled(true);
		// char[] charBin = cardData.toCharArray();
		// String bankName = BankInfo.getNameOfBank(charBin, 0);
		cardData = "6666666666666666"
				+ "d49121202369991430fffffffffff996222024000079840084d1561560000000000001"
				+ "003236999010000049120d000000000000d000000000000d00000000fffffffffffffff";

		track2 = cardData.substring(0, 48).replace("f", "")
				.replace("d", "D");
		track3 = cardData.substring(48, cardData.length())
				.replace("f", "").replace("d", "D");
	}
	
	
	private void showpop() {
		// TODO Auto-generated method stub
		lin_adress.setVisibility(View.VISIBLE);
		
	}
	
	
	private void showdilog() {
		// TODO Auto-generated method stub
		doubleWarnDialogs = new EidtDialog(BussinessInfoActivity.this, R.style.MyEditDialog, "绑定推荐关系", "请输入您的推荐人手机号", "取消", "绑定", "", new OnEditDialogChlicListener() {
			
			@Override
			public void onClick(View v, String a) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.btn_right:
					if(a.length()<=0){
						Toast.makeText(getApplicationContext(), "请输入正确手机号",
								Toast.LENGTH_SHORT).show();
//						ToastCustom.showMessage(getApplicationContext(), "请输入正确划拨数量");
					}else{
						BindAgintTask task = new BindAgintTask();
						task.execute(HttpUrls.BINDAGENT + "", custId,a);
						doubleWarnDialogs.dismiss();
					}
					break;
				case R.id.btn_left:

					doubleWarnDialogs.dismiss();
					InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				
					break;
				default:
					break;
				}
			}
		},0);
		doubleWarnDialogs.setCancelable(false);
		doubleWarnDialogs.setCanceledOnTouchOutside(false);
		doubleWarnDialogs.show();
	}

	/**
	 * 关闭当前的Activity
	 */
	private void backActivity() {
		AppManager.getAppManager().finishActivity();
	}

	/**
	 * 控件显示内容
	 * 
	 * @param map
	 */
	private void showView(HashMap<String, Object> map) {
		
		if(map.get("ACTNAM")!=null){
			String acname = map.get("ACTNAM").toString();
			switch (acname.length()) {
			case 2:
				String setnames = acname.substring(0,1);
				tvName.setText(setnames+"*");
				break;
			case 3:
				String setnames1 = acname.substring(0,1);
				String getnames1 = acname.substring(acname.length()-1);
				tvName.setText(setnames1+"*"+getnames1);
				break;
			case 4:
				String setnames2 = acname.substring(0,1);
				String getnames2 = acname.substring(acname.length()-1);
				tvName.setText(setnames2+"**"+getnames2);
				break;
			case 5:
				String setnames3 = acname.substring(0,1);
				String getnames3 = acname.substring(acname.length()-1);
				tvName.setText(setnames3+"***"+getnames3);
				break;
			case 6:
				String setnames4 = acname.substring(0,1);
				String getnames4 = acname.substring(acname.length()-1);
				tvName.setText(setnames4+"****"+getnames4);
				break;
			case 7:
				String setnames5 = acname.substring(0,1);
				String getnames5 = acname.substring(acname.length()-1);
				tvName.setText(setnames5+"*****"+getnames5);
				break;
			case 8:
				String setnames6 = acname.substring(0,1);
				String getnames6 = acname.substring(acname.length()-1);
				tvName.setText(setnames6+"******"+getnames6);
				break;
			case 9:
				String setnames7 = acname.substring(0,1);
				String getnames7 = acname.substring(acname.length()-1);
				tvName.setText(setnames7+"*******"+getnames7);
				break;
			case 10:
				String setnames8 = acname.substring(0,1);
				String getnames8 = acname.substring(acname.length()-1);
				tvName.setText(setnames8+"********"+getnames8);
				break;
			default:
				tvName.setText(acname);
				break;
			}
		}
		if(map.get("ACTNO")!=null){
			String card = map.get("ACTNO").toString();
			String setpcard = card.substring(0, 7);
			String incard = card.substring(card.length()-8, card.length()-4);
//			String getpcard = card.substring(card.length()-4);
			tvCardId.setText(setpcard+"****"+incard+"****");
		}
		if(map.get("OPNBNK")!=null){
			tvBankName.setText(map.get("OPNBNK").toString());
		}
		if(map.get("CARDID")!=null){
			String ids = map.get("CARDID").toString();
			String setids = ids.substring(0, 5);
			String getids = ids.substring(ids.length()-3);
			tvId.setText(setids+"**********"+getids);
		}
		if(map.get("PHONENUMBER")!=null){
			
			String phone = map.get("PHONENUMBER").toString();
			String setphone = phone.substring(0,3);
			String getphone = phone.substring(phone.length()-4);
			tvMobile.setText(setphone+"****"+getphone);
		}
		
			if(map.get("ISAREAAGENT")!=null&&map.get("ISAREAAGENT").toString().equals("1")){
				tvCustonId.setText("省级代理");
				if(map.get("AGTPROVNAM")!=null){
					tv_region_agt.setText(map.get("AGTPROVNAM").toString());
				}else{
					lin_region_agt.setVisibility(View.GONE);
				}
				
			}else if(map.get("ISAREAAGENT")!=null&&map.get("ISAREAAGENT").toString().equals("2")){
				tvCustonId.setText("市级代理");
				if(map.get("AGTPROVNAM")!=null&&map.get("AGTCITYNAM")!=null){
					tv_region_agt.setText(map.get("AGTPROVNAM").toString()+map.get("AGTCITYNAM").toString());
				}else{
					lin_region_agt.setVisibility(View.GONE);
				}
			}else if(map.get("ISAREAAGENT")!=null&&map.get("ISAREAAGENT").toString().equals("3")){
				tvCustonId.setText("区级代理");
				if(map.get("AGTPROVNAM")!=null&&map.get("AGTCITYNAM")!=null&&map.get("AGTAREANAM")!=null){
					tv_region_agt.setText(map.get("AGTPROVNAM").toString()+map.get("AGTCITYNAM").toString()+map.get("AGTAREANAM").toString());
				}else{
					lin_region_agt.setVisibility(View.GONE);
				}
				
			}else{
				lin_region_agt.setVisibility(View.GONE);
				if(isgeneralagent.equals("1")){
					tvCustonId.setText("服务商");
				}else{
					if(issaleagt.equals("1")){
						tvCustonId.setText("服务商");
					}else{
						if(isretailers.equals("1")){
							tvCustonId.setText("服务商");
						}else{
//							if(tagsts.equals("3")||tagsts.equals("0")||tagsts.equals("4")){
								if(isvip.equals("1")){
									tvCustonId.setText("高级会员");
								}else{
									tvCustonId.setText("普通会员");
								}
//							}else{
//								tvCustonId.setText("未实名认证");
//								lin_feerate.setVisibility(View.GONE);
//							}
						}
					}
				}
			}
		
		
		
		if(map.get("MERLEVEL")!=null){
			String a = map.get("MERLEVEL").toString();
			if(map.get("STRATE")!=null){
				tv_feerate.setText(map.get("STRATE").toString()+"%");
			}
		
			if(a.equals("1")){
//				tvCustonId.setText("普通会员");
				tv_feerat.setText(map.get("FEERAT").toString()+"%");
				
			}else if(a.equals("2")){
//				tvCustonId.setText("高级会员");
				tv_feerat.setText(map.get("FEERAT").toString()+"%");
				
			}else if(a.equals("3")){
				tv_feerat.setText(map.get("FEERAT").toString()+"%");
//				tv_feerate.setText(map.get("SALEFEERATE").toString()+"%");
//				tvCustonId.setText("分销商");
			}
		}else{
			tvCustonId.setText("123");
		}
		String status = null;
		if(map.get("MERSTATUS")!=null){
			status = map.get("MERSTATUS").toString();
		}

		if (status != null) {
			if ("0".equals(status)) {
				status = "认证通过";
			} else if ("1".equals(status)) {
				status = "待认证";
			} else if ("2".equals(status)) {
				status = "认证不通过"; 
			} else if ("3".equals(status)) {
				status = "仅银行通过";
			} else if ("4".equals(status)) {
				status = "仅身份证认证通过";
			} else if ("5".equals(status)) {
				status = "商户被禁用";
			} else if ("6".equals(status)) {
				status = "待认证";
			}
		}
		
		if(map.get("ISRECOMMENDED")!=null&&map.get("ISRECOMMENDED").toString().equals("1")){
			my_frend.setEnabled(false);
			my_frend.setText(map.get("RECOMMENDED").toString());
		}else if(map.get("ISRECOMMENDED")!=null&&map.get("ISRECOMMENDED").toString().equals("0")){
			my_frend.setEnabled(true);
			my_frend.setTextColor(getResources().getColor(R.color.apptitle));
			my_frend.setText(Html.fromHtml("<u>立即绑定我的推荐人</u>"));
		}
		tvStatus.setText(status);
		  

		if(map.get("CITYNAM")!=null&&map.get("PROVNAM")!=null&&map.get("AREANAM")!=null){
			tv_bank_name_address.setEnabled(false);
			tv_bank_name_address.setText(map.get("PROVNAM").toString()+map.get("CITYNAM").toString()+map.get("AREANAM").toString());
		}else if(map.get("CITYNAM")!=null&&map.get("PROVNAM")!=null){
			tv_bank_name_address.setEnabled(false);
			tv_bank_name_address.setText(map.get("PROVNAM").toString()+map.get("CITYNAM").toString());
		}else{
//			tv_bank_name_address.setEnabled(true);
//			tv_bank_name_address.setText(Html.fromHtml("<u>添加开户行所在地</u>"));
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
			showLoadingDialog("正在查询中...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2],params[3] };
			return NetCommunicate.getVerificationMidatc(HttpUrls.BUSSINESS_INFO, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					
					showView(result);
				} else {
					finish();
					Toast.makeText(getApplicationContext(), result
							.get(Entity.RSPMSG).toString(), Toast.LENGTH_SHORT);
				}
			}
			super.onPostExecute(result);
		}
	}
	
	class BindAgintTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog("正在查询中...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2]};
			return NetCommunicate.getVerificationMidatc(
					HttpUrls.BINDAGENT, values);
			
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					
					warnDialog = new OneButtonDialogWarn(BussinessInfoActivity.this,
							R.style.CustomDialog, "提示",
							result.get(Entity.RSPMSG).toString(), "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									finish();
									warnDialog.dismiss();
								}
							});
					warnDialog.setCancelable(false);
					warnDialog.setCanceledOnTouchOutside(false);
					warnDialog.show();

				} else {
					Toast.makeText(getApplicationContext(), result
							.get(Entity.RSPMSG).toString(), Toast.LENGTH_SHORT).show();
				}
			}
			super.onPostExecute(result);
		}
}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_issuing_bank:
//			initcarddata();
//			BankProvinceTask1 task2 = new BankProvinceTask1();
//			task2.execute(HttpUrls.QUERY_BANK_NAME + "", "",
//					track2, track3);ss
			 new Thread(run).start();	
			break;
			
		case R.id.tv_issuing_bank_com:
//			 BankCityTask task = new BankCityTask();
//				task.execute(HttpUrls.QUERY_BANK_CITY + "", "1", "200",
//				bankProvinceid);
			 new Thread(run1).start();	
			break;
		case R.id.tv_issuing_com:
//			initcarddata();
//			BankProvinceTask1 task2 = new BankProvinceTask1();
//			task2.execute(HttpUrls.QUERY_BANK_NAME + "", "",
//					track2, track3);ss
			 new Thread(run2).start();	
			break;
		default:
			break;
		}
		
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent data) {
		super.onActivityResult(arg0, arg1, data);
		
//		if (data != null) {
//			Bundle bundle = data.getExtras();
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (null != bundle) {
					if (arg1 == 9) {
						bankProvinceid = bundle.getString("companyId");
						tv_issuing_bank.setText(bundle.getString("companyName"));
						tv_issuing_bank_com.setText("");
					} else if (arg1 == 8) {
					
						bankCityid = bundle.getString("companyId");
						tv_issuing_bank_com.setText(bundle.getString("companyName"));
					} else if (arg1 == 7) {
						
						bankareid = bundle.getString("companyId");
						tv_issuing_com.setText(bundle.getString("companyName"));
						
						SpannableString msp = new SpannableString(tv_issuing_bank.getText().toString()+tv_issuing_bank_com.getText().toString()+tv_issuing_com.getText().toString());
						showDoubleWarnDialog(msp);

//					}
				}
			}
//			if (null != bundle) {
//				if (arg1 == 9) {
//					bankProvinceid = bundle.getString("companyId");
//					tv_issuing_bank.setText(bundle.getString("companyName"));
//					tv_issuing_bank_com.setText("");
//				} else if (arg1 == 8) {
//					bankCityid = bundle.getString("companyId");
//					tv_issuing_bank_com.setText(bundle.getString("companyName"));
//					ComitAdressTask task = new ComitAdressTask();
//					task.execute(HttpUrls.COMITADRESS + "", mercnum,
//							bankProvinceid, bankCityid);
//					
//				}
//			}
		}
	}
	
	@Override
	protected void doubleWarnOnClick(View v) {
		// TODO Auto-generated method stub
		super.doubleWarnOnClick(v);
		switch (v.getId()) {
		case R.id.btn_left:

			doubleWarnDialog.dismiss();
			
			break;
		case R.id.btn_right:
			ComitAdressTask task = new ComitAdressTask();
			task.execute(HttpUrls.COMITADRESS + "", mercnum,
					bankProvinceid, bankCityid,bankareid);
			doubleWarnDialog.dismiss();
			break;

		default:
			break;
		}
	}
	
	
	class ComitAdressTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在加载...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {

			String[] values = { params[0], params[1], params[2], params[3] ,params[4]};
			return NetCommunicate.get(HttpUrls.COMITADRESS, values);

		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD) != null
						&& result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					ToastCustom.showMessage(BussinessInfoActivity.this,
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT);
					lin_adress.setVisibility(View.GONE);
					BussinessInfoTask task = new BussinessInfoTask();
					task.execute(HttpUrls.BUSSINESS_INFO + "", custId, "4","0");
				}else{
					Toast.makeText(getApplicationContext(), result
							.get(Entity.RSPMSG).toString(), Toast.LENGTH_SHORT);
					lin_adress.setVisibility(View.GONE);
				}
			}
			super.onPostExecute(result);
		}
	}
	
	
	/**
	 * 银行卡开户行查询
	 * 
	 * @author Administrator
	 * 
	 */
//	class BankProvinceTask1 extends AsyncTask<String, Integer, ProvinceEntity> {
//		
//		@Override
//		protected void onPreExecute() {
//			showLoadingCloseDialog("正在查询中。。。");
//			super.onPreExecute();
//		}
//
//		@Override
//		protected ProvinceEntity doInBackground(String... params) {
//			String[] values = { params[0], params[1], params[2], params[3] };
//			return NetCommunicate.getQueryProvince(HttpUrls.QUERY_BANK_NAME,
//					values);
//		}
//
//		@Override
//		protected void onPostExecute(ProvinceEntity result) {
//			loadingDialogClose.dismiss();
//			if (result != null) {
//				if (Entity.STATE_OK.equals(result.getRspcod())) {
//					list = result.list;
//					if (list != null) {
//						bankCityid = null;
//						Intent intent = new Intent(BussinessInfoActivity.this,
//								SelectListNameActivity.class);
//						Bundle bundle = new Bundle();
//						ArrayList carrier = new ArrayList();
//						carrier.add(list);
//						bundle.putString("titleContent", "银行卡开户省份");
//						bundle.putParcelableArrayList("carrier", carrier);
//						intent.putExtras(bundle);
//						startActivityForResult(intent, 9);
//						overridePendingTransition(0, 0);
//					}else{
//						Toast.makeText(BussinessInfoActivity.this,
//										"城市列表获取失败", Toast.LENGTH_SHORT);
//						lin_adress.setVisibility(View.GONE);
//					}
//				} else {
//					Toast.makeText(BussinessInfoActivity.this, result
//							.getRspmsg().toString(), Toast.LENGTH_SHORT);
//					lin_adress.setVisibility(View.GONE);
//				}
//			}
//			super.onPostExecute(result);
//		}
//	}
	
//	class BankCityTask extends AsyncTask<String, Integer, CityEntity> {
//
//		@Override
//		protected void onPreExecute() {
//			showLoadingDialog("正在查询中...");
//			super.onPreExecute();
//		}
//
//		@Override
//		protected CityEntity doInBackground(String... params) {
//			String[] values = { params[0], params[1], params[2], params[3] };
//			return NetCommunicate
//					.getQueryCity(HttpUrls.QUERY_BANK_CITY, values);
//		}
//
//		@Override
//		protected void onPostExecute(CityEntity result) {
//			loadingDialogWhole.dismiss();
//			if (result != null) {
//
//				if (Entity.STATE_OK.equals(result.getRspcod())) {
//					if (result.list.size() == 0) {
//						Toast.makeText(BussinessInfoActivity.this,
//								"没有该城市支行信息", Toast.LENGTH_SHORT);
//						return;
//					}
//
//					Intent intent = new Intent(BussinessInfoActivity.this,
//							SelectListNameActivity.class);
//					Bundle bundle = new Bundle();
//					@SuppressWarnings("rawtypes")
//					ArrayList carrier = new ArrayList();
//					carrier.add(result.list);
//					bundle.putString("titleContent", "银行卡开户城市");
//					bundle.putParcelableArrayList("carrier", carrier);
//					intent.putExtras(bundle);
//					startActivityForResult(intent, 8);
//					overridePendingTransition(0, 0);
//				}else{
//					Toast.makeText(BussinessInfoActivity.this,
//							result.getRspmsg(),Toast.LENGTH_SHORT);
//					lin_adress.setVisibility(View.GONE);
//				}
//			}else{
//				lin_adress.setVisibility(View.GONE);
//			}
//			super.onPostExecute(result);
//		}
//	}
	
	
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
				
				Intent intent = new Intent(BussinessInfoActivity.this,
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
//				ToastCustom.showMessage(RateListActivity.this,
//						"没有获取到您的订单信息");
				break;
			case 3:
				Toast.makeText(getApplicationContext(),"网络异常,请检查您的网络",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"订单信息获取失败");
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
				
				Intent intent = new Intent(BussinessInfoActivity.this,
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
//				ToastCustom.showMessage(RateListActivity.this,
//						"没有获取到您的订单信息");
				break;
			case 3:
				Toast.makeText(getApplicationContext(),"网络异常,请检查您的网络",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"订单信息获取失败");
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
				
				Intent intent = new Intent(BussinessInfoActivity.this,
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
//				ToastCustom.showMessage(RateListActivity.this,
//						"没有获取到您的订单信息");
				break;
			case 3:
				Toast.makeText(getApplicationContext(),"网络异常,请检查您的网络",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"订单信息获取失败");
				break;
			default:
				break;
			}
		};
	};
}
