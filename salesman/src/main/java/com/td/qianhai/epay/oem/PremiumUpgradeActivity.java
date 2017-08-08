package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.adapter.ViewPagerAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.RateListBean;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.ToastCustom;
import com.td.qianhai.epay.oem.views.dialog.ChooseDialog1;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class PremiumUpgradeActivity extends BaseActivity implements OnPageChangeListener,OnClickListener{
	
	private View view1, view2;// 各个页卡
	private ViewPager viewPager;// 页卡内容
	private ViewPagerAdapter pageradapter;
	private ArrayList<View> views;// Tab页面列表
	private int currIndex = 0;// 当前页卡编号
	private DisplayMetrics display;
	private LayoutInflater inflater;
	private RadioButton card_3,card_4;
	private LinearLayout lin1,lin2;
	private TextView rate_price,rate_ratio,btn_rateblance_update,btn_ratenum_update,now_rate_ratio;
	private LinearLayout lin_choice_rate;
	private EditText et_rate_num;
	private ArrayList<HashMap<String, Object>> mList;
	private String mobile;
	private boolean isThreadRun = false; // 加载数据线程运行状态
	private int page = 1; // 页数
	private int allPageNum = 0; // 总页数
	private int PAGE_SIZE = 10;
	private ChooseDialog1 chooseDialog;
	private String ratenoein;
	private OneButtonDialogWarn warnDialog;
	private Editor editor;
	private SharedPreferences share;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_premium_upgrade);
		editor = MyCacheUtil.setshared(PremiumUpgradeActivity.this);
		share = MyCacheUtil.getshared(PremiumUpgradeActivity.this);
		AppContext.getInstance().addActivity(this);
		display = this.getResources().getDisplayMetrics();
		inflater = getLayoutInflater();
		initview();
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		ratenoein = share.getString("nocardfeerate","");

		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		now_rate_ratio.setText(ratenoein+"%");

		
	}

	private void initview() {
		((TextView) findViewById(R.id.tv_title_contre)).setText("费率升级");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		
		view1 = inflater.inflate(R.layout.premiumup_page1, null);
		view2 = inflater.inflate(R.layout.premiumuo_page2, null);
		
		rate_price = (TextView) view1.findViewById(R.id.rate_price);
		rate_ratio = (TextView) view1.findViewById(R.id.rate_ratio);
		btn_rateblance_update = (TextView)view1.findViewById(R.id.btn_rateblance_update);
//		btn_rateblance_update.setOnClickListener(this);
		lin_choice_rate = (LinearLayout) view1.findViewById(R.id.lin_choice_rate);
		lin_choice_rate.setOnClickListener(this);
		
		btn_ratenum_update = (TextView) view2.findViewById(R.id.btn_ratenum_update);
		btn_ratenum_update.setOnClickListener(this);
		btn_ratenum_update.setEnabled(false);
		et_rate_num = (EditText) view2.findViewById(R.id.et_rate_num);
		
		now_rate_ratio = (TextView) view1.findViewById(R.id.now_rate_ratio);
		now_rate_ratio.setText(ratenoein+"%");
		
		lin1  =(LinearLayout) view1.findViewById(R.id.lin1);
		lin2 = (LinearLayout) view2.findViewById(R.id.lin2);
		
		views = new ArrayList<View>();
		
		viewPager = (ViewPager) findViewById(R.id.rate_viewpagers);
		
		viewPager.setOnPageChangeListener(this);
		
		card_3 = (RadioButton) findViewById(R.id.card_3);
		card_3.setChecked(true);
		card_4 = (RadioButton) findViewById(R.id.card_4);
		
		card_3.setOnClickListener(new MyOnClickListener(0));
//
		card_4.setOnClickListener(new MyOnClickListener(1));
//		views.add(lin1);
		views.add(lin2);
		mobile =share.getString("Mobile", "");
//		mobile = ((AppContext) getApplication()).getMobile();
		mList = new ArrayList<HashMap<String, Object>>();
//		if (mList.size() == 0) {
//			emptyView = inflater.inflate(R.layout.progress_view, null);
//			emptyView.setLayoutParams(new LayoutParams(
//					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//			((ViewGroup) listView.getParent()).addView(emptyView);
//			listView.setEmptyView(emptyView);
			// 加载数据
//			loadMore();
//		}
		
		initadapter();
		
		
		et_rate_num.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				if(s.length()>=8){
					btn_ratenum_update.setEnabled(true);
				}else{
					btn_ratenum_update.setEnabled(false);
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
		

	}
	
	
	/**
	 * 头标点击监听
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}

	}
	
	private void initadapter() {
		pageradapter = new ViewPagerAdapter(views);

		viewPager.setAdapter(pageradapter);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lin_choice_rate:
			Intent intent = new Intent(PremiumUpgradeActivity.this,
					RateListActivity.class);
//			RateListActivity
			startActivity(intent);
//			chooseDialog = new ChooseDialog1(
//					PremiumUpgradeActivity.this,
//					R.style.CustomDialog,
//					new OnBackDialogClickListener() {
//
//						@Override
//						public void OnBackClick(View v, String str,
//								int position) {
//							// TODO Auto-generated method stub
////							tv_denomination.setText(str);
////							tv_price.setText((Double.parseDouble(mList.get(position).get("PRDAMT").toString()))/100+"");
////							prdid = mlist.get(position).get("PRDID").toString();
////							Log.e("", "prdid=+=++++1111 "+prdid);
////							prdtypes = mlist.get(position).get("PRDTYPE").toString();
////							avaamttype = mlist.get(position).get("PRDAMT").toString();
//							chooseDialog.dismiss();
//						}
//					}, "请选择费率", mList);
//			chooseDialog.show();
			
			break;

		case R.id.btn_ratenum_update:
			
			ratenumupdate();
			break;
		default:
			break;
		}
		
	}

	private void ratenumupdate() {
		
		String ratenum  = et_rate_num.getText().toString(); 
		GoRateTask  Task =  new GoRateTask();
		Task.execute(HttpUrls.GO_RATE+"",mobile,"1","","",ratenum);
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {

		currIndex = arg0;
		
		if (arg0 == 0) {
			card_3.setChecked(true);
		} else {
			card_4.setChecked(true);
		}
		
	}
	
	private void loadMore() {
		if (page != 1 && page > allPageNum) {
			Toast.makeText(getApplicationContext(),"没有更多记录了",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(this, "没有更多记录了");
			return;
		}
		if (!isThreadRun) {
			isThreadRun = true;
			showLoadingDialog("正在查询中...");
			new Thread(run).start();
		}

	}
	
	Runnable run = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			String[] values = { String.valueOf(HttpUrls.RATE_QUERY),mobile};
			RateListBean entitys = NetCommunicate.getRateListBean(
					HttpUrls.RATE_QUERY, values);

			Message msg = new Message();
			if (entitys != null) {
				((AppContext)getApplication()).setNocr(entitys.getNocr());
				list = entitys.list;
						mList.addAll(list);
					if(mList.size()<=0||mList==null){
						
						msg.what = 2;
					}else{
						msg.what = 1;
					}

					page++;
			} else {
				loadingDialogWhole.dismiss();
				msg.what = 3;
			}
			isThreadRun = false;
			loadingDialogWhole.dismiss();
			handler.sendMessage(msg);
		}
	};
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				
				break;
			case 2:
				finish();
				Toast.makeText(getApplicationContext(),"没有获取到您的订单信息",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(PremiumUpgradeActivity.this,
//						"没有获取到您的订单信息");
				break;
			case 3:
				Toast.makeText(getApplicationContext(),"订单信息获取失败",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(PremiumUpgradeActivity.this,
//						"订单信息获取失败");
				finish();
				break;
				
			default:
				break;
			}
		};
	};
	
	class GoRateTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog("正在加载中...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3] , params[4], params[5]};
			return NetCommunicate.getMidatc(HttpUrls.GO_RATE, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {

			loadingDialogWhole.dismiss();
			if (result != null) {

				if (result.get("RSPCOD").equals(Entity.STATE_OK)) {

					// doubleWarnDialog.dismiss();
					// ((AppContext)getApplication()).setNocein();
					// Intent it = new Intent(RateListActivity.this,
					// OnlineWeb.class);
					// it.putExtra("urlStr", result.get("REURL").toString());
					// startActivity(it);
					
					String rate = result.get("NOCARDFEERATE").toString();
					((AppContext) getApplication()).setNocein(rate);
					
					editor.putString("nocardfeerate", rate);
					
					editor.commit();
					warnDialog = new OneButtonDialogWarn(
							PremiumUpgradeActivity.this, R.style.CustomDialog,
							"提示", result.get(Entity.RSPMSG).toString()+"请重新登录", "确定",
							new OnMyDialogClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									warnDialog.dismiss();
									finish();
									AppContext.getInstance().exit();
									Intent it = new Intent(PremiumUpgradeActivity.this,UserActivity.class);
									
									startActivity(it);
								}
							});

					warnDialog.show();

				} else {
					ToastCustom.showMessage(PremiumUpgradeActivity.this, result
							.get(Entity.RSPMSG).toString(), Toast.LENGTH_SHORT);
				}

			} else {
				ToastCustom.showMessage(PremiumUpgradeActivity.this,
						"获取数据失败,请检查网络", Toast.LENGTH_SHORT);
			}
			super.onPostExecute(result);
		}
	}
	

}
