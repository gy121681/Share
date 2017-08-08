package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.adapter.CreditListAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.ChooseDialog;

public class CreditToActivity extends BaseActivity {
	
	private LinearLayout add_creditcard;
	
	private ArrayList<HashMap<String, Object>> mlist;//
	
	private String mercnum;

	private ListView listview;
	
	private int allPageNum = 0;
	
	private int PAGE_SIZE = 10;
	
	private CreditListAdapter adapter;
	
	private ChooseDialog chooseDialog;
	
	private TextView delete_propty;
	
	private String carcatno,mobile;
	
	private boolean isThreadRun = false; 
	
	private int page = 1; // 总页数
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.activity_credit_card);
		
//		mercnum = ((AppContext)getApplication()).getMercNum();
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		mercnum = MyCacheUtil.getshared(this).getString("MercNum", "");

		initview();
		mlist = new ArrayList<HashMap<String,Object>>();
		adapter = new CreditListAdapter(CreditToActivity.this, mlist, 0);
		listview.setAdapter(adapter);
		
		initchargelist();
		
		initadapter();

	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		if(AppContext.iscreditcardlist){

//			AppContext.iscreditcardlist = false;
//		}
		
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		page = 1;
		allPageNum = 0;
		PAGE_SIZE = 10;
			mlist.clear();
			initchargelist();
	}

	private void initadapter() {
		

		
	}

	private void initview() {
		((TextView) findViewById(R.id.tv_title_contre)).setText("信用卡还款");

		
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		add_creditcard = (LinearLayout) findViewById(R.id.add_creditcard);
		listview = (ListView) findViewById(R.id.mycirclist);
		delete_propty = (TextView) findViewById(R.id.delete_propty);
		add_creditcard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				Intent it = new Intent(CreditToActivity.this,CreditToDetileActivity.class);
				
				startActivity(it);
				
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				String ids = "";
				String nam = "";
				String car = "";
				String carname = "未知";
				if( mlist.get((int)id).get("CARDNAME")!=null){
					nam = mlist.get((int)id).get("CARDNAME").toString();
					
				}else{
					nam = "未知";
				}
				if( mlist.get((int)id).get("CARDCODE")!=null){
					car = mlist.get((int)id).get("CARDCODE").toString();
				}
				if( mlist.get((int)id).get("FRPID")!=null){
					ids = mlist.get((int)id).get("FRPID").toString();
				}
				if( mlist.get((int)id).get("ISSUER")!=null){
					carname = mlist.get((int)id).get("ISSUER").toString();
				}

				Intent it = new Intent(CreditToActivity.this,CreditToDetileActivity1.class);
				it.putExtra("car", car);
				it.putExtra("nam", nam);
				it.putExtra("ids",ids);
				it.putExtra("carname",carname);
				startActivity(it);
			}
		});
		
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View v,
					int arg2, long id) {
				carcatno = mlist.get((int)id).get("CARDCODE").toString();
				// TODO Auto-generated method stub
//				TextView tv =  (TextView) v.findViewById(R.id.remove_img);
//				tv.setVisibility(View.VISIBLE);
//				chooseDialog = new ChooseDialog(
//						CreditToActivity.this,
//						R.style.CustomDialog,
//						new OnBackDialogClickListener() {
//
//							@Override
//							public void OnBackClick(View v, String str,
//									int position) {
//								// TODO Auto-generated method stub
//
//								if (position == 0) {
									
									SpannableString msp = new SpannableString("您要删除该信用卡?");
									showDoubleWarnDialog(msp);
									
//								} else {
//									
//								}
//								chooseDialog.dismiss();
//							}
//						}, "功能选择", CreditToActivity.this
//								.getResources().getStringArray(
//										R.array.credititem));
//				chooseDialog.show();
				
				return true;
			}
		});
	}
	
	
	
	private void initchargelist() {		
		if (page != 1 && page >= allPageNum) {
			Toast.makeText(getApplicationContext(),"没有更多记录了",
					Toast.LENGTH_SHORT).show();
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
			ArrayList<HashMap<String, Object>> list = null;
			// ArrayList<HashMap<String, Object>> list2 = new
			// ArrayList<HashMap<String, Object>>();
			String[] values = { HttpUrls.CREDITQUERY + "", mobile,  page+"", PAGE_SIZE+"" };
//			MyAgtBean entitys = NetCommunicate.getSubAgentBill(
//					HttpUrls.CREDITQUERY, values);
			list = NetCommunicate.getList(HttpUrls.CREDITQUERY, values,HttpKeys.CREDITQUERY_BACK);

			Message msg = new Message();

			if (list != null) {
				mlist.addAll(list);
				if (list.size() <= 0 || list == null) {

					msg.what = 2;
				} else {
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
				
				adapter.notifyDataSetChanged();
				break;
			case 2:
				adapter.notifyDataSetChanged();
				Toast.makeText(getApplicationContext(), "没有更多记录了",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(AgentListActivity.this, "没有更多记录了");
//				null_datas.setVisibility(View.VISIBLE);
				break;
			case 3:
				break;
			default:
				
				break;
			}
		};
	};
	
	
//	Runnable run = new Runnable() {
//
//		@Override
//		public void run() {
//			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//			String[] values = { String.valueOf(HttpUrls.CREDITQUERY),mercnum};
//			CreditListBean entitys = NetCommunicate.getPay2(
//					HttpUrls.CREDITQUERY, values);
//
//			Message msg = new Message();
//			if (entitys != null) {
//				list = entitys.list;
//						mlist.addAll(list);
//					if(mlist.size()<=0||mlist==null){
//						
//						msg.what = 2;
//					}else{
//						msg.what = 1;
//					}
//
//			} else {
//				loadingDialogWhole.dismiss();
//				msg.what = 3;
//			}
//			loadingDialogWhole.dismiss();
//			handler.sendMessage(msg);
//		}
//	};
//	
//	
//	private Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 1:
//				
//				adapter.notifyDataSetChanged();
//				delete_propty.setVisibility(View.VISIBLE);
//				
//				break;
//			case 2:
//				delete_propty.setVisibility(View.GONE);
//				adapter.notifyDataSetChanged();
//				break;
//			case 3:
//				delete_propty.setVisibility(View.GONE);
//				Toast.makeText(getApplicationContext(),"网络异常",
//						Toast.LENGTH_SHORT).show();
////				ToastCustom.showMessage(CreditToActivity.this,
////						"网络异常");
//				break;
//			default:
//				break;
//			}
//		};
//	};
	
	
	@Override
	protected void doubleWarnOnClick(View v) {
		// TODO Auto-generated method stub
		super.doubleWarnOnClick(v);
		switch (v.getId()) {
		case R.id.btn_left:
			doubleWarnDialog.dismiss();

			break;
		case R.id.btn_right:
			DeleteCreditToPay payTask = new DeleteCreditToPay();
			payTask.execute(HttpUrls.DELETECREDITCARD+ "",mobile, carcatno);
			doubleWarnDialog.dismiss();
			break;

		default:
			break;
		}
	}
	
	
	/**
	 * 解绑
	 * 
	 * 
	 */
	class DeleteCreditToPay extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1],params[2]};
			return NetCommunicate.getMidatc(HttpUrls.DELETECREDITCARD,
					values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			if (result != null) {
				if (result.get(Entity.RSPCOD).equals(Entity.STATE_OK)) {
					allPageNum = 0;
					PAGE_SIZE = 10;
					allPageNum = 0;
					page = 1;
					mlist.clear();
					initchargelist();
					Toast.makeText(getApplicationContext(),result.get("RSPMSG").toString(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(CreditToActivity.this,result.get("RSPMSG").toString());
					
				} else {
					Toast.makeText(getApplicationContext(),result.get("RSPMSG").toString(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(CreditToActivity.this,result.get("RSPMSG").toString());

				}
			} else {
				Toast.makeText(getApplicationContext(),"fail",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(CreditToActivity.this, "fail");
			}
			super.onPostExecute(result);
		}
	}

}
