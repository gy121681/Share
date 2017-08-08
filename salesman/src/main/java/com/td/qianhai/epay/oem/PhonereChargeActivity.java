package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.adapter.PhoneListAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.PhoneChargeBean;
import com.td.qianhai.epay.oem.interfaces.onMyaddTextListener;
import com.td.qianhai.epay.oem.mail.utils.ConnUtil;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.AnimationUtil;
import com.td.qianhai.epay.oem.views.dialog.ChooseDialog;
import com.td.qianhai.epay.oem.views.dialog.MyEditDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnBackDialogClickListener;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class PhonereChargeActivity extends BaseActivity {

	private TextView title, back, tv_denomination, tv_price, contacts,
			tv_operators,transfer_confirm,balance_tv;
	private RelativeLayout money_typt_content,phone_relativw;
	private ChooseDialog chooseDialog;
	private EditText charge_phone; 
	private static final int REQUEST_CONTACT = 1;
	private LinearLayout phone_lin_list; //电话list布局
	private ListView phonenuber_list;	//历史充值手机list
	private TextView clear_phonenumber; // 清除历史充值
	private PhoneListAdapter adapter;
	private String userphone;
	private ArrayList<HashMap<String, String>> phonelist;
	private ArrayList<HashMap<String, Object>> mlist;//
	private String[] type;
	private String prdid;
	private MyEditDialog doubleWarnDialog;
	private String mobile,mercnum,Operators,prdtypes,mobiles;
	private int a ;
	private boolean tags = false;
	private boolean isPreservation;
	private OneButtonDialogWarn warnDialog;
	String avaamt ="0";
	private SharedPreferences share;
	String avaamttype = "0";
	private Editor editor;
	private GridView gridView2;
	private LayoutInflater mInflater;
	private MyAdapter myadapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.phone_charge_activity);
//		mobiles = ((AppContext)getApplication()).getMobile();
//		mercnum = ((AppContext)getApplication()).getMercNum();
		share = MyCacheUtil.getshared(PhonereChargeActivity.this);
		mobiles = share.getString("Mobile", "");
		mercnum = share.getString("MercNum", "");
		editor = MyCacheUtil.setshared(PhonereChargeActivity.this);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		a = share.getInt("size",0);
		initview();
		initlist() ;
//		RechargeTask ta = new RechargeTask();
//		ta.execute(HttpUrls.RECHARGE+"");
		mlist = new ArrayList<HashMap<String,Object>>();
		
		
		initchargelist();
		

	}

	private void initchargelist() {		
		showLoadingDialog("正在加载中...");
		new Thread(run).start();
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		intdata();

	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		transfer_confirm.setEnabled(true);
	}
	
	public void intdata(){
		a = share.getInt("size",0);
		initlist();
		adapter.notifyDataSetChanged();
	}
	

	private void initview() {
//		userphone = ((AppContext)getApplication()).getMobile();
		userphone = share.getString("Mobile", "");
		title = (TextView) findViewById(R.id.tv_title_contre);
		transfer_confirm = (TextView) findViewById(R.id.transfer_confirm);
		title.setText("手机充值");
		
		back = (TextView) findViewById(R.id.bt_title_left);
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		transfer_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
//				if(){
//					
//				}
//				
				charge();
				
			}

		});
		phone_relativw = (RelativeLayout) findViewById(R.id.phone_relativw);
		money_typt_content = (RelativeLayout) findViewById(R.id.money_typt_content);
		tv_denomination = (TextView) findViewById(R.id.tv_denomination);
		phone_lin_list = (LinearLayout) findViewById(R.id.phone_lin_list);
		phonenuber_list = (ListView) findViewById(R.id.phonenuber_list);
		clear_phonenumber = (TextView) findViewById(R.id.clear_phonenumber);
		balance_tv = (TextView) findViewById(R.id.balance_tv);
		gridView2 = (GridView) findViewById(R.id.gridView2);
		tv_price = (TextView) findViewById(R.id.tv_price);
		charge_phone = (EditText) findViewById(R.id.charge_phone);
		contacts = (TextView) findViewById(R.id.contacts);
		tv_operators = (TextView) findViewById(R.id.tv_operators);
		charge_phone.setText(userphone);
		
		
		
		phonenuber_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				String  phone =  phonelist.get((int)id).get("phonenumbers").toString();
				charge_phone.setText(phone);
				
				AnimationUtil.ButTranslateAnimation(PhonereChargeActivity.this, phone_lin_list);
				phone_lin_list.setVisibility(View.GONE);
				tags = false;
			}
		});
		
		
		charge_phone.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				
				if(arg1){
					charge_phone.setFocusableInTouchMode(true);
					charge_phone.setFocusable(true);
					
				}
			}
		});
		
		clear_phonenumber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				for (int i = 0; i < a; i++) {	
					intdata();
					editor.remove("size"+i);
					editor.remove("size");
					editor.commit();
					phone_lin_list.setVisibility(View.GONE);
				}
				
			}
		});
		
		charge_phone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(tags){
					phone_lin_list.setVisibility(View.GONE);
					tags = false;
				}else{
					AnimationUtil.TopTranslateAnimation(PhonereChargeActivity.this, phone_lin_list);
					phone_lin_list.setVisibility(View.VISIBLE);
					tags = true;
				}

			}
		});
		/**
		 * 跳转通讯录
		 */
		contacts.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent();

				intent.setAction(Intent.ACTION_PICK);

				intent.setData(ContactsContract.Contacts.CONTENT_URI);

				startActivityForResult(intent, REQUEST_CONTACT);
				
			}
		});
		
		/**
		 * 判断手机运营商
		 */
		String p = charge_phone.getText().toString();
		if(p.length()>=11){
			if(validateMobile(p).equals("1")){
				tv_operators.setVisibility(View.VISIBLE);
				tv_operators.setText("中国移动");
				Operators = "10";
			}else if(validateMobile(p).equals("2")){
				tv_operators.setVisibility(View.VISIBLE);
				tv_operators.setText("中国联通");
				Operators = "20";
			}else if(validateMobile(p).equals("3")){
				tv_operators.setVisibility(View.VISIBLE);
				tv_operators.setText("中国电信");
				Operators = "30";
				
			}else{
				tv_operators.setVisibility(View.VISIBLE);
				tv_operators.setText("未知运营商");
				Operators = "0";
			}
		}else{
			tv_operators.setVisibility(View.GONE);
		}
		
		
		/**
		 * 判断手机运营商
		 */
		charge_phone.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if (s.length()>=11){
					transfer_confirm.setEnabled(true);
					String ss = charge_phone.getText().toString();
					if(validateMobile(ss).equals("1")){
						tv_operators.setVisibility(View.VISIBLE);
						Operators = "10";
						tv_operators.setText("中国移动");
					}else if(validateMobile(ss).equals("2")){
						tv_operators.setVisibility(View.VISIBLE);
						Operators = "20";
						tv_operators.setText("中国联通");
					}else if(validateMobile(ss).equals("3")){
						tv_operators.setVisibility(View.VISIBLE);
						tv_operators.setText("中国电信");
						Operators = "30";
					}else{
						Operators = "0";
						tv_operators.setVisibility(View.VISIBLE);
						tv_operators.setText("未知运营商");
					}
				}else{
					transfer_confirm.setEnabled(false);
					tv_operators.setVisibility(View.GONE);
//					AnimationUtil.ButTranslateAnimation(PhonereChargeActivity.this, phone_lin_list);
					phone_lin_list.setVisibility(View.GONE);
					tags = false;
					
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
		
		/**
		 * 面值选择
		 */
		money_typt_content.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				chooseDialog = new ChooseDialog(
						PhonereChargeActivity.this,
						R.style.CustomDialog,
						new OnBackDialogClickListener() {

							@Override
							public void OnBackClick(View v, String str,
									int position) {
								// TODO Auto-generated method stub
								tv_denomination.setText(str);
								tv_price.setText((Double.parseDouble(mlist.get(position).get("PRDAMT").toString()))/100+"");
								prdid = mlist.get(position).get("PRDID").toString();
								prdtypes = mlist.get(position).get("PRDTYPE").toString();
								avaamttype = mlist.get(position).get("PRDAMT").toString();
								chooseDialog.dismiss();
							}
						}, "请选择充值面额", type);
				chooseDialog.show();
				
			}
		});
		
			gridView2.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long position) {
					// TODO Auto-generated method stub
					prdid = mlist.get((int)position).get("PRDID").toString();
					prdtypes = mlist.get((int)position).get("PRDTYPE").toString();
					avaamttype = mlist.get((int)position).get("PRDAMT").toString();
					charge();
				}
			});
		
	}

	/**
	 * 设置手机list数据
	 */
	private void initlist() {
		 phonelist = new ArrayList<HashMap<String, String>>();

			HashMap<String, String> h4 = new HashMap<String, String>();
			h4.put("phonenumbers", userphone);
			h4.put("phonetags", "用户绑定号码");
			phonelist.add(h4);

			if(a==0){
				a=1;
			}
			for (int i = 1; i < a; i++) {
				String num = share.getString("size"+i,"");
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("phonenumbers", num);
				phonelist.add(map);

		}
		
			adapter = new PhoneListAdapter(this, phonelist);
			phonenuber_list.setAdapter(adapter);
		
	}

	/**
	 * 获取联系人手机号
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == REQUEST_CONTACT) {
			if (resultCode == RESULT_OK) {
				if (data == null) {
					return;
				}

				Uri result = data.getData();

				ContentResolver reContentResolverol = getContentResolver();

				Cursor c = managedQuery(result, null, null, null, null);
				c.moveToFirst();

				String contactId = c.getString(c
						.getColumnIndex(ContactsContract.Contacts._ID));
				Cursor phone = reContentResolverol.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId, null, null);
				while (phone.moveToNext()) {
					String usernumber = phone
							.getString(phone
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

					charge_phone.setText(ConnUtil.format(usernumber));
				}
			}
		}
	}
	


	/**
	 * 判断传入的参数号码为哪家运营商
	 * 
	 * @param mobile
	 * @return 运营商名称
	 */
	public String validateMobile(String mobile) {
		String returnString = "";
		if (mobile == null || mobile.trim().length() != 11) {
			return "-1"; // mobile参数为空或者手机号码长度不为11，错误！
		}
		if (mobile.trim().substring(0, 3).equals("134")
				|| mobile.trim().substring(0, 3).equals("135")
				|| mobile.trim().substring(0, 3).equals("136")
				|| mobile.trim().substring(0, 3).equals("137")
				|| mobile.trim().substring(0, 3).equals("138")
				|| mobile.trim().substring(0, 3).equals("139")
				|| mobile.trim().substring(0, 3).equals("150")
				|| mobile.trim().substring(0, 3).equals("151")
				|| mobile.trim().substring(0, 3).equals("152")
				|| mobile.trim().substring(0, 3).equals("157")
				|| mobile.trim().substring(0, 3).equals("158")
				|| mobile.trim().substring(0, 3).equals("159")
				|| mobile.trim().substring(0, 3).equals("187")
				|| mobile.trim().substring(0, 3).equals("147")
				|| mobile.trim().substring(0, 3).equals("188")) {
			returnString = "1"; // 中国移动
		}
		if (mobile.trim().substring(0, 3).equals("130")
				|| mobile.trim().substring(0, 3).equals("131")
				|| mobile.trim().substring(0, 3).equals("132")
				|| mobile.trim().substring(0, 3).equals("156")
				|| mobile.trim().substring(0, 3).equals("185")
				|| mobile.trim().substring(0, 3).equals("186")) {
			returnString = "2"; // 中国联通
		}
		if (mobile.trim().substring(0, 3).equals("133")
				|| mobile.trim().substring(0, 3).equals("153")
				|| mobile.trim().substring(0, 3).equals("180")
				|| mobile.trim().substring(0, 3).equals("177")
				|| mobile.trim().substring(0, 3).equals("189")) {
			returnString = "3"; // 中国电信
		}
		if (returnString.trim().equals("")) {
			returnString = "0"; // 未知运营商
		}
		return returnString;
	}
	
	
	 class MyAdapter extends BaseAdapter {
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mlist.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return mlist.get(position);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				ViewHolder holder = null;
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.tran_type_item, null);
					holder = new ViewHolder();
					holder.textview1 = (TextView) convertView.findViewById(R.id.textviewss);
					convertView.setTag(holder);
					
					
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				HashMap<String, Object>  text = mlist.get(position);
//				mlist.get(i).get("PRDNAME").toString();
				
				holder.textview1.setText(Integer.parseInt(text.get("PRDAMT").toString())/100+"元");
				
				return convertView;
			}

			class ViewHolder {
				public TextView textview1;
			}
		}

	
	Runnable run = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			String[] values = { String.valueOf(HttpUrls.RECHARGE)};
			PhoneChargeBean entitys = NetCommunicate.getPay1(
					HttpUrls.RECHARGE, values);

			Message msg = new Message();
			if (entitys != null) {
				list = entitys.list;
						mlist.addAll(list);
					if(mlist.size()<=0||mlist==null){
						
						msg.what = 2;
//						ToastCustom.showMessage(PhonereChargeActivity.this, "手机充值订单获取失败");
					}else{
						msg.what = 1;
					}

			} else {
//				loadingDialogWhole.dismiss();
				msg.what = 3;
			}
//			loadingDialogWhole.dismiss();
			handler.sendMessage(msg);
		}
	};
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				 myadapter = new MyAdapter();
				 gridView2.setAdapter(myadapter);
//				tv_denomination.setText(mlist.get(0).get("PRDNAME").toString());
//				tv_price.setText((Double.parseDouble(mlist.get(0).get("PRDAMT").toString()))/100+"");
//				prdid = mlist.get(0).get("PRDID").toString();
//				Log.e("", "prdid=+=++++ "+prdid);
//				prdtypes = mlist.get(0).get("PRDTYPE").toString();
//				avaamttype = mlist.get(0).get("PRDAMT").toString();
//				type = new String[mlist.size()];
//				for (int i = 0; i < mlist.size(); i++) {
//					
//					 type[i] = mlist.get(i).get("PRDNAME").toString();
//					
//				}
				
				GetWalletInfo  walletinfo = new GetWalletInfo();
				
				walletinfo.execute(HttpUrls.RICH_TREASURE_INFO + "",
						mobiles);
				
//				adapter = new PhoneListAdapter(PhonereChargeActivity.this, phonelist);
//				phonenuber_list.setAdapter(adapter);
				
				break;
			case 2:
				loadingDialogWhole.dismiss();
				Toast.makeText(getApplicationContext(),"手机充值订单获取失败",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(PhonereChargeActivity.this,
//						"手机充值订单获取失败");
				break;
			case 3:
				loadingDialogWhole.dismiss();
				Toast.makeText(getApplicationContext(),"手机充值订单获取失败",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(PhonereChargeActivity.this,
//						"手机充值订单获取失败");
				break;
			default:
				break;
			}
		};
	};
	
	
	private void charge() {
		
		
		if(Double.parseDouble(avaamt) > Double.parseDouble(avaamttype)){
			transfer_confirm.setEnabled(false);
			doubleWarnDialog = new MyEditDialog(PhonereChargeActivity.this,
					R.style.MyEditDialog, "充值", "请输入支付密码", "确认", "取消", (Double.parseDouble(avaamttype))+"",
					new OnMyDialogClickListener() {

						@Override
						public void onClick(View v) {

							switch (v.getId()) {
							case R.id.btn_right:
								doubleWarnDialog.dismiss();
								InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
								break;
							case R.id.btn_left:
								break;
							default:
								break;
							}
							transfer_confirm.setEnabled(true);
						}
						
					},
			new onMyaddTextListener() {
				
				@Override
				public void refreshActivity(String paypwd) {
					
					if (paypwd == null || paypwd.equals("")) {
						Toast.makeText(getApplicationContext(),"请输入支付密码",
								Toast.LENGTH_SHORT).show();
//						ToastCustom.showMessage(
//								PhonereChargeActivity.this,
//								"请输入支付密码！");
						return;
					}
					if (paypwd.length() < 6 || paypwd.length() > 15) {
						Toast.makeText(getApplicationContext(),"输入的密码长度有误,请输入6个数字、字母或特殊符号",
								Toast.LENGTH_SHORT).show();
//						ToastCustom.showMessage(
//								PhonereChargeActivity.this,
//								"输入的密码长度有误,请输入6个数字、字母或特殊符号！");
						return;
					}
					mobile = charge_phone.getText().toString().trim();
					PhoneChargeTask cardTask = new PhoneChargeTask();
					cardTask.execute(HttpUrls.PHONE_CHARGE + "", mobile,paypwd,Operators,prdtypes,prdid,mercnum);
					
				}
			});
			doubleWarnDialog.setCancelable(false);
			doubleWarnDialog.setCanceledOnTouchOutside(false);
			doubleWarnDialog.show();
			
		}else{
			Toast.makeText(getApplicationContext(),"余额不足",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(PhonereChargeActivity.this, "余额不足");
		}
	}
	
	/**
	 * 
	 * 
	 */
	class PhoneChargeTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			showLoadingDialog("正在操作中...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3],params[4] ,params[5],params[6]};
			return NetCommunicate.getPay(HttpUrls.PHONE_CHARGE,
					values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					doubleWarnDialog.dismiss();
					if(mobile.equals(mobiles)){
						
					}else{
						if(a==0){
							a=1;
						}
						for (int i = 0; i < a ;i++) {

							if(share.getString("size"+i,"").equals(mobile)){
								isPreservation = true;
							}else{


							}
						}
						if(!isPreservation){
							editor.putString("size"+(a),mobile );
							editor.putInt("size", a+1);
							editor.commit();
						}
						

					}
										
					Intent it = new Intent(PhonereChargeActivity.this,MentionNowAcitvity.class);
					
					it.putExtra("State", 3);
					it.putExtra("res", result.get(Entity.RSPMSG).toString());
					startActivity(it);
					doubleWarnDialog.dismiss();
					finish();

				}else if(result.get(Entity.RSPCOD).equals("000088")){
					doubleWarnDialog.dismiss();
				} else if (result.get(Entity.RSPCOD).equals("000001")) {
					Intent it = new Intent(PhonereChargeActivity.this,MentionNowAcitvity.class);
					doubleWarnDialog.dismiss();
					it.putExtra("State", 0);
					it.putExtra("Err", "您当日密码输错次数已超限,请明日再试");
					startActivity(it);
					finish();

				} else {

					Intent it = new Intent(PhonereChargeActivity.this,MentionNowAcitvity.class);
					doubleWarnDialog.dismiss();
					it.putExtra("State", 4);
					it.putExtra("res", result.get(Entity.RSPMSG).toString());
					startActivity(it);
				}
			} else {
				Toast.makeText(getApplicationContext(),"fail",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(PhonereChargeActivity.this, "fail");
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
		
		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD)!=null&&result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					
					if(result.get("AVAAMT").toString()!=null){
						avaamt = result.get("AVAAMT").toString();
						
						
						double d = Double.parseDouble(avaamt);
						
						
						String r = String .format("%.2f",d/100);
//						me_result_money.setText(d/100+"");
						balance_tv.setText(r);
						
//						double d = Double.parseDouble(avaamt);
//						
//						balance_tv.setText(d/100+"");
						
//						if (avaamt.length() == 1) {
//							balance_tv.setText("0.0" + avaamt);
//						} else if (avaamt.length() == 2) {
//							balance_tv.setText("0." + avaamt);
//						} else {
//							balance_tv.setText(avaamt.substring(0,
//									avaamt.length() - 2)
//									+ "."
//									+ avaamt.substring(
//											avaamt.length() - 2));
//						}
						
						}
					

					
				} else {
					
					if(result.get(Entity.RSPMSG)!=null){
						warnDialog = new OneButtonDialogWarn(PhonereChargeActivity.this,
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
					}else{
						warnDialog = new OneButtonDialogWarn(PhonereChargeActivity.this,
								R.style.CustomDialog, "提示", "网络异常请重试", "确定",
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
					

				}
			} else {
				Toast.makeText(getApplicationContext(),"数据获取失败,请检查网络连接",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(PhonereChargeActivity.this, "数据获取失败,请检查网络连接");
			}
			super.onPostExecute(result);
			
			}
				
		}

}

