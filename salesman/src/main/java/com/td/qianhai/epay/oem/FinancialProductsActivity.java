package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.td.qianhai.epay.oem.adapter.FinanInfoListAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.MyEditDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class FinancialProductsActivity extends BaseActivity implements
OnScrollListener {

	private String instatus,outstatus,mininamt,maxinamt,minoutamt,odernam,
	maxoutamt,balace,orderid,ordertyp,tot,pop,inday,outday,savedamt,phone,
	dayoutnum,dayinnum,sts;
	private TextView me_result_money,me_profit_money,me_tot_money,tv_1,tv_2,tv_3,tv_4,capital_pro,tv_reward;
	private RelativeLayout btn_recharge,btn_accounts;
	private OneButtonDialogWarn warnDialog;
	private MyEditDialog doubleWarnDialog;
	private LinearLayout lin_1,lin_2;
	private int page = 1; // 页数
	private int allPageNum = 0; // 总页数
	private int PAGE_SIZE = 10;
	private ListView mlistview;
	private FinanInfoListAdapter adapter;
	private ArrayList<HashMap<String, Object>> mList;
	private boolean isThreadRun = false; // 加载数据线程运行状态
	
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.financial_products_activity);
		phone = MyCacheUtil.getshared(this).getString("Mobile", "");
		sts = MyCacheUtil.getshared(this).getString("STS", "");
		Intent it = getIntent();
		orderid = it.getStringExtra("oderid");
		odernam = it.getStringExtra("odernam");
		ordertyp = it.getStringExtra("odertyp");
		balace = it.getStringExtra("balace");
		

//		instatus = it.getStringExtra("instatus");
//		outstatus = it.getStringExtra("outstatus");
//		mininamt = it.getStringExtra("mininamt");
//		maxinamt = it.getStringExtra("maxinamt");
//		minoutamt = it.getStringExtra("minoutamt");
//		maxoutamt = it.getStringExtra("maxoutamt");
//
		
//		tot = it.getStringExtra("tot");
//		pop = it.getStringExtra("pop");
//		savedamt = it.getStringExtra("savedamt");
//		
//		inday = it.getStringExtra("inday");
//		outday = it.getStringExtra("outday");
//		dayinnum = it.getStringExtra("dayinnum");
//		dayoutnum = it.getStringExtra("dayoutnum");
		
		mlistview = (ListView) findViewById(R.id.mlistview);
		mlistview.setOnScrollListener(this);
		mList = new ArrayList<HashMap<String, Object>>();
		adapter = new FinanInfoListAdapter(this, mList);
		mlistview.setAdapter(adapter);
		initview();
		
	}

	private void initview() {
		// TODO Auto-generated method stub
		((TextView) findViewById(R.id.tv_title_contre)).setText(odernam);
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		
		me_result_money = (TextView) findViewById(R.id.me_result_money);
		me_profit_money = (TextView) findViewById(R.id.me_profit_money);
		capital_pro = (TextView) findViewById(R.id.capital_pro);
		tv_reward = (TextView) findViewById(R.id.tv_reward);
		me_tot_money = (TextView) findViewById(R.id.me_tot_money);
		tv_1 = (TextView) findViewById(R.id.tv_1);
		lin_1 = (LinearLayout) findViewById(R.id.lin_1);
		lin_2 = (LinearLayout) findViewById(R.id.lin_2);
		
		btn_recharge = (RelativeLayout) findViewById(R.id.btn_recharge);
		btn_accounts = (RelativeLayout) findViewById(R.id.btn_accounts);
		tv_3 = (TextView) findViewById(R.id.tv_3);
		tv_4 = (TextView) findViewById(R.id.tv_4);
		
		if(ordertyp.equals("1")){
			tv_4.setText("不可转");
			tv_4.setTextColor(this.getResources().getColor(R.color.gray));
			btn_accounts.setEnabled(false);
		}
		if(ordertyp.equals("0")){
			lin_1.setVisibility(View.VISIBLE);
//			lin_2.setVisibility(View.VISIBLE);
			capital_pro.setVisibility(View.VISIBLE);
			mlistview.setVisibility(View.GONE);
		}else{
			tv_reward.setText("累计收益");
		}
			ChangeIntoInfoTask walletinfo = new ChangeIntoInfoTask();
			
			walletinfo.execute(HttpUrls.EXCHANGEINFO + "", phone,orderid,"","","","");


		btn_recharge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(sts.equals("0")||sts.equals("3")||sts.equals("4")){
					Intent it = new Intent(FinancialProductsActivity.this,PurchaseProductsActivity.class);
					it.putExtra("tag", "0");
					it.putExtra("orderid", orderid);
					it.putExtra("ordertyp", ordertyp);
					it.putExtra("savedamt", savedamt);
					it.putExtra("balace", balace);
					it.putExtra("inday", inday);
					it.putExtra("outday", outday);
					it.putExtra("dayinnum", dayinnum);
					it.putExtra("dayoutnum", dayoutnum);
					finish();
					startActivity(it);
				}else{
					Toast.makeText(getApplicationContext(), "请先实名认证,待实名认证通过重试", Toast.LENGTH_SHORT).show();
				}
			}
		});
		btn_accounts.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(sts.equals("0")||sts.equals("3")||sts.equals("4")){
				Intent it = new Intent(FinancialProductsActivity.this,PurchaseProductsActivity.class);
				it.putExtra("orderid", orderid);
				it.putExtra("ordertyp", ordertyp);
				it.putExtra("savedamt", savedamt);
				it.putExtra("outday", outday);
				it.putExtra("inday", inday);
				it.putExtra("dayinnum", dayinnum);
				it.putExtra("dayoutnum", dayoutnum);
				it.putExtra("balace", balace);
				it.putExtra("tag", "1");
				finish();
				startActivity(it);
				}else {
					Toast.makeText(getApplicationContext(), "请先实名认证,待实名认证通过重试", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	private void loadMore() {
		if (page != 1 && page > allPageNum) {
			Toast.makeText(getApplicationContext(), "没有更多记录了", Toast.LENGTH_SHORT).show();
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
			String[] values = { HttpUrls.EXCHANGEINFO + "", phone,orderid,"","",page+"",PAGE_SIZE+""};
			ArrayList<HashMap<String, Object>> list = NetCommunicate.getList(HttpUrls.EXCHANGEINFO, values,HttpKeys.EXCHANGEINFO1_BACK);
			Message msg = new Message();
			if (list != null) {
						mList.addAll(list);
					if(mList.size()<=0||mList==null){
						
						msg.what = 2;
					}else{
						msg.what = 1;
					}
					int allNum = 0;
					if(list.size()>0){
					if(list.get(0).get("TOLCNT")!=null&&!list.get(0).get("TOLCNT").equals("null")){
						 allNum = Integer.parseInt(list.get(0).get("TOLCNT").toString());
					}
					}

					if (allNum % PAGE_SIZE != 0) {
						allPageNum = allNum / PAGE_SIZE + 1;
					} else {
						allPageNum = allNum / PAGE_SIZE;
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
				for (int i = 0; i < mList.size(); i++) {
					if(!mList.get(i).get("MODSTS").toString().equals("1")){
						mList.remove(i);
						i=i-1;
					}
				}
				adapter.notifyDataSetChanged();
				break;
			case 2:
				
				Toast.makeText(getApplicationContext(),"理财产品暂无记录",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"没有获取到您的订单信息");
				break;
			case 3:
				Toast.makeText(getApplicationContext(),"网络异常，请检查网络设置",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(RateListActivity.this,
//						"订单信息获取失败");
				break;
			default:
				break;
			}
		};
	};
	
	
	class ChangeIntoInfoTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在加载...");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {

			String[] values = { params[0], params[1], params[2], params[3], params[4], params[5], params[6] };
			return NetCommunicate.getMidatc(HttpUrls.EXCHANGEINFO, values);

		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (result.get(Entity.RSPCOD) != null
						&& result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {

					instatus = result.get("INSTATUS").toString(); // 转入
					outstatus = result.get("OUTSTATUS").toString(); // 转出

					mininamt = String.format("%.2f", Double.parseDouble(result
							.get("MININAMT").toString()) / 100); // 转入限额
					maxinamt = String.format("%.2f", Double.parseDouble(result
							.get("MAXINAMT").toString()) / 100);

					minoutamt = String.format("%.2f", Double.parseDouble(result
							.get("MINOUTAMT").toString()) / 100); // 转出限额
					maxoutamt = String.format("%.2f", Double.parseDouble(result
							.get("MAXOUTAMT").toString()) / 100);

					tot = String.format("%.2f", Double.parseDouble(result.get(
							"TOTPOINTS").toString()) / 100);
					pop = String.format("%.2f", Double.parseDouble(result.get(
							"OPOINTS").toString()) / 100);

					inday = String.format("%.2f", Double.parseDouble(result
							.get("DAYSUMAMT").toString()) / 100);
					outday = String.format("%.2f", Double.parseDouble(result
							.get("DAYOUTSUMAMT").toString()) / 100);

					dayinnum = result.get("DAYINNUM").toString(); // 转入次数
					dayoutnum = result.get("DAYOUTNUM").toString(); // 转出次数
					
					 savedamt  = String .format("%.2f",Double.parseDouble(result.get("SAVEDAMT").toString())/100); 

					if (instatus.equals("0")) {
						tv_3.setTextColor(FinancialProductsActivity.this.getResources().getColor(R.color.gray));
						tv_3.setText("不可转");
						btn_recharge.setEnabled(false);
					}
					if (outstatus.equals("0")) {
						tv_4.setTextColor(FinancialProductsActivity.this.getResources().getColor(R.color.gray));
						tv_4.setText("不可转");
						btn_accounts.setEnabled(false);
					}
					
					 if(ordertyp.equals("1")){
						 if(result.get("DPTCYCLETYP").toString().equals("1")){
							 capital_pro.setText("定存期限"+result.get("DPTCYCLE").toString()+"天,未到期不可转出");
						 }else if(result.get("DPTCYCLETYP").toString().equals("2")){
							 capital_pro.setText("定存期限"+result.get("DPTCYCLE").toString()+"个月,未到期不可转出");
						 }
					 }
					
					 if(ordertyp.equals("1")){
						 me_profit_money.setText(tot);
					 }else{
						 me_profit_money.setText(pop);
					 }
					
					me_tot_money.setText(tot);

					me_result_money.setText(savedamt);
					
					if(!ordertyp.equals("0")){
						loadMore();
					}
					
				} else {

					if (result.get(Entity.RSPMSG) != null) {
						// loadingDialogWhole.dismiss();

						warnDialog = new OneButtonDialogWarn(
								FinancialProductsActivity.this,
								R.style.CustomDialog, "提示", result.get(
										Entity.RSPMSG).toString(), "确定",
								new OnMyDialogClickListener() {
									@Override
									public void onClick(View v) {
										warnDialog.dismiss();
									}
								});
						warnDialog.show();
					} else {
						// loadingDialogWhole.dismiss();

						warnDialog = new OneButtonDialogWarn(
								FinancialProductsActivity.this,
								R.style.CustomDialog, "提示", "网络异常请重试", "确定",
								new OnMyDialogClickListener() {
									@Override
									public void onClick(View v) {
										warnDialog.dismiss();
									}
								});
						warnDialog.show();
					}
				}
			} else {
				Toast.makeText(getApplicationContext(), "数据获取失败,请检查网络连接",
						Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
	}

	private int lastItem;// 当前显示的最后一项
	@Override
	public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int arg3) {
		// TODO Auto-generated method stub
		lastItem = firstVisibleItem + visibleItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == SCROLL_STATE_IDLE) {
			if (lastItem == mList.size()) {
				loadMore();
			}
		}
		
	}
}
