package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.adapter.SubAgentAdapter1;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.beans.MyAgtBean;
import com.td.qianhai.epay.oem.interfaces.onMyaddTextListener;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.ChooseDialog;
import com.td.qianhai.epay.oem.views.dialog.EidtDialog;
import com.td.qianhai.epay.oem.views.dialog.MyEditDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnBackDialogClickListener;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnEditDialogChlicListener;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;

public class AgentListActivity1 extends BaseActivity implements OnScrollListener {

	private String phone,agtid;
	
	private View moreView;
	private int page = 1; // 页数
	private int allPageNum = 0; // 总页数
	private int PAGE_SIZE = 20;
	private boolean isThreadRun = false; // 加载数据线程运行状态
	private ListView listView;
	private LayoutInflater inflater;
	private ArrayList<HashMap<String, Object>> mList;
	private TextView null_datas;
	private int lastItem;
	private SubAgentAdapter1 adapter;
	private  EidtDialog doubleWarnDialogs;
	private String tag ;
	private MyEditDialog WarnDialog;
	private String agentids = "";
	private  ChooseDialog chooseDialog;
	private String[] rlist,wlist;
	private OneButtonDialogWarn warnDialog;
	private String ratestr = "",setnum;
	private boolean ischoose = false;
	private String min;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_list);
		AppContext.getInstance().addActivity(this);
//		phone = ((AppContext)getApplication()).getMobile();
		phone = MyCacheUtil.getshared(this).getString("Mobile", "");
		agtid = MyCacheUtil.getshared(this).getString("AGENTID", "");;
		rlist = ((AppContext)getApplication()).getRatelist();
		wlist = ((AppContext)getApplication()).getFlashlist();
		Intent it = getIntent();
		tag = it.getStringExtra("tag");
		setnum = it.getStringExtra("num");
		min = it.getStringExtra("min");
		
		initview();

//		showLoadingDialog("正在查询中...");
//		new Thread(run).start();

	}
	
	private void initview() {
	
		if(tag.equals("0")){
			((TextView) findViewById(R.id.tv_title_contre)).setText("下属代理商列表");
		}else{
			((TextView) findViewById(R.id.tv_title_contre)).setText("下属代理商");
		}
		
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		mList = new ArrayList<HashMap<String,Object>>();
		inflater = LayoutInflater.from(this);
		adapter = new SubAgentAdapter1(AgentListActivity1.this, mList);
		moreView = inflater.inflate(R.layout.load, null);
		null_datas = (TextView) findViewById(R.id.null_datas);
		listView = (ListView) findViewById(R.id.sub_agt_list);
		listView.addFooterView(moreView);
		moreView.setVisibility(View.GONE);
		listView.setOnScrollListener(this);
		
		
		if (mList.size() == 0) {
			// 加载数据
			loadMore();
		}
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long id) {
				// TODO Auto-generated method stub
				if(tag.equals("0")){
					
					agentids = mList.get((int)id).get("AGENTID").toString();
					showeditdialog();
					
				}else{
					agentids = mList.get((int)id).get("AGENTID").toString();
					String aa = mList.get((int)id).get("MINFEERATE").toString();
					String bb = mList.get((int)id).get("MAXRATE").toString();
					String min = mList.get((int)id).get("MINURGENTFEE").toString();
					String max= mList.get((int)id).get("MAXURGENTFEE").toString();
					final String[] b = setdialog(aa, bb); 
					final String[] cc = setdialog(min,max); 
					
					
					
					adapter.getid((int)id);
					adapter.notifyDataSetChanged();
//					agentids = mList.get((int)id).get("AGENTID").toString();
//					v.findViewById(R.id.tv_rate1).setOnClickListener(new OnClickListener() {
//						
//						@Override
//						public void onClick(View arg0) {
//							// TODO Auto-generated method stub
//							PopUpBox(b);
//						}
//					});
//					v.findViewById(R.id.tv_rate2).setOnClickListener(new OnClickListener() {
//						
//						@Override
//						public void onClick(View arg0) {
//							// TODO Auto-generated method stub
//							PopUpBox1(cc);
//						}
//
//					});
					
					
				}
			}


		});
		
		

		
//		listView.setOnScrollListener(new OnScrollListener() {
//		    @Override
//		    public void onScrollStateChanged(AbsListView view, int scrollState) {
//		        switch (scrollState) {
//		            // 当不滚动时
//		            case OnScrollListener.SCROLL_STATE_IDLE:
//		                // 判断滚动到底部
//		                if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
//		                	loadMore();
//		                	
//		              }
//		              break;
//		        }
//		    }
//		 
//		    @Override
//		    public void onScroll(AbsListView view, int firstVisibleItem,
//		           int visibleItemCount, int totalItemCount) {
//		    }
//		});
		
	}
	
	public String[] setdialog(String aa ,String bb){
		int min1 = (int) (Double.parseDouble(aa) * 100);
		int max1 = (int) (Double.parseDouble(bb) * 100);
		List<String> list1 = new ArrayList<String>();
		for (int i = min1; i <= max1; i++) {
			list1.add(String.valueOf((double) i / 100));
		}
		final String[] b = new String[list1.size()];
		for (int i = 0; i < list1.size(); i++) {
			if (list1.get(i).length()==3) {
				b[i] = list1.get(i)+"0%";
			} else {
				b[i] = list1.get(i)+"%";
			}

		}
		return b;
	}
	
	private void PopUpBox1(String[] cc) {
		// TODO Auto-generated method stub
		chooseDialog = new ChooseDialog(
				AgentListActivity1.this,
				R.style.CustomDialog,
				new OnBackDialogClickListener() {

					@Override
					public void OnBackClick(View v, String str,
							int position) {
//						positions = position+1;
						ratestr = str.substring(0,str.length()-1);
						SpannableString msp = new SpannableString("您确定修改此代理商的闪提费率吗?");
						showDoubleWarnDialog(msp);
						ischoose = false;
//						// TODO Auto-generated method stub
						chooseDialog.dismiss();
					}
				}, "修改闪提费率", cc);
		chooseDialog.show();
	}
	private void PopUpBox(String[] b) {
		chooseDialog = new ChooseDialog(
				AgentListActivity1.this,
				R.style.CustomDialog,
				new OnBackDialogClickListener() {

					@Override
					public void OnBackClick(View v, String str,
							int position) {
//						positions = position+1;
						ratestr = str.substring(0,str.length()-1);
						SpannableString msp = new SpannableString("您确定修改此代理商的成本费率吗?");
						showDoubleWarnDialog(msp);
						ischoose = true;
//						// TODO Auto-generated method stub
						chooseDialog.dismiss();
					}
				}, "修改成本费率", b);
		chooseDialog.show();
		
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
			doubleWarnDialog.dismiss();
			if(ischoose){
				rateedit(ratestr);
			}else{
				rateWithdrawalsedit(ratestr);
			}
			
			
			break;

		default:
			break;
		}
	}
	
	
	private void showeditdialog() {
		new OnEditDialogChlicListener() {
			
			@Override
			public void onClick(View v, String a) {
				// TODO Auto-generated method stub
				
			}
		};
		doubleWarnDialogs = new EidtDialog(AgentListActivity1.this, R.style.MyEditDialog, "激活码划拨", "可用数量"+setnum, "确认", "取消", "", new OnEditDialogChlicListener() {
			
			@Override
			public void onClick(View v, String a) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.btn_right:
					doubleWarnDialogs.dismiss();
					InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					break;
				case R.id.btn_left:
					if(a.equals("0")||a.length()<=0){
						Toast.makeText(getApplicationContext(), "请输入正确划拨数量",
								Toast.LENGTH_SHORT).show();
//						ToastCustom.showMessage(getApplicationContext(), "请输入正确划拨数量");
					}else{
						doubleWarnDialogs.dismiss();
						TransferTask  ttask = new TransferTask();
						ttask.execute(HttpUrls.ACTCODETRANSFER + "", phone,agtid,agentids,a,"");
//						shwpaypwd(a);
						
					}
					
					break;
				default:
					break;
				}
			}
		}, 0);
//		doubleWarnDialogs = new EidtDialog(AgentListActivity.this,
//				R.style.MyEditDialog, "充值", "请输入支付密码", "确认", "取消", "",
//				new OnMyDialogClickListener() {
//
//					@Override
//					public void onClick(View v) {
//
//						switch (v.getId()) {
//						case R.id.btn_right:
//							doubleWarnDialog.dismiss();
//							InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//							break;
//						case R.id.btn_left:
//							
//							break;
//						default:
//							break;
//						}
//						
//					}
//				},

//		agents_new_actcode.setEnabled(true);
		doubleWarnDialogs.setCancelable(false);
		doubleWarnDialogs.setCanceledOnTouchOutside(false);
		doubleWarnDialogs.show();
		
	}
	
	private void loadMore() {
		if (page != 1 && page > allPageNum) {
			Toast.makeText(getApplicationContext(), "没有更多记录了",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(this, "没有更多记录了");
			moreView.setVisibility(View.GONE);
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
			// ArrayList<HashMap<String, Object>> list2 = new
			// ArrayList<HashMap<String, Object>>();
			String[] values = { HttpUrls.CHIRDACT + "", phone, agtid, page+"", PAGE_SIZE+"" };
			MyAgtBean entitys = NetCommunicate.getSubAgentBill(
					HttpUrls.CHIRDACT, values);
			Message msg = new Message();
			if (entitys != null) {
//				loadingDialogWhole.dismiss();
				list = entitys.list;
				if (list != null && list.size() != 0) {
					mList.addAll(list);
//					msg.what = 1;
					Log.e("", "111111");
					int allNum = Integer.parseInt(entitys.getTolcnt());

					if (allNum % PAGE_SIZE != 0) {
						allPageNum = allNum / PAGE_SIZE + 1;
					} else {
						allPageNum = allNum / PAGE_SIZE;
					}
					
					if(mList.size()<=0||mList==null){
						Log.e("", "22222");
						msg.what = 2;
					}else{
						msg.what = 1;
						Log.e("", "33333");
					}

					page++;
				}else{
					loadingDialogWhole.dismiss();
					msg.what = 2;
//					ToastCustom.showMessage(AgentListActivity.this, "没有更多记录了");
				}
				
				

				
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
				null_datas.setVisibility(View.GONE);
				
				moreView.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
				listView.setAdapter(adapter);
				break;
			case 2:
				if (moreView != null) {
					listView.setVisibility(View.GONE);
					moreView.setVisibility(View.GONE);
				}
				Toast.makeText(getApplicationContext(), "没有更多记录了",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(AgentListActivity.this, "没有更多记录了");
//				null_datas.setVisibility(View.VISIBLE);
				break;
			case 3:
				null_datas.setVisibility(View.VISIBLE);
				break;
			default:
				
				break;
			}
		};
	};

	@Override
	public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		lastItem = firstVisibleItem + visibleItemCount-1;
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == SCROLL_STATE_IDLE) {
			if (lastItem == mList.size()) {
			moreView.setVisibility(View.VISIBLE);
			loadMore();
			}
		}
	}
	
	protected void shwpaypwd(String num) {
		final String number = num;
		
		WarnDialog = new MyEditDialog(AgentListActivity1.this,
				R.style.MyEditDialog, "充值", "请输入支付密码", "确认", "取消", "",
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {

						switch (v.getId()) {
						case R.id.btn_right:
							WarnDialog.dismiss();
							InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
							break;
						case R.id.btn_left:
							break;
						default:
							break;
						}
						
					}
				},
		new onMyaddTextListener() {
			
			@Override
			public void refreshActivity(String paypwd) {
				
				if (paypwd == null || paypwd.equals("")) {
					Toast.makeText(getApplicationContext(), "请输入支付密码",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(
//							AgentListActivity.this,
//							"请输入支付密码！");
					return;
				}
				if (paypwd.length() < 6 || paypwd.length() > 15) {
					Toast.makeText(getApplicationContext(), "输入的密码长度有误,请输入6个数字、字母或特殊符号",
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(
//							AgentListActivity.this,
//							"输入的密码长度有误,请输入6个数字、字母或特殊符号！");
					return;
				}




				
			}
		});
//		agents_new_actcode.setEnabled(true);
		WarnDialog.setCancelable(false);
		WarnDialog.setCanceledOnTouchOutside(false);
		WarnDialog.show();
		

		
	}
	
	private void rateedit(String number){
		RateEditTask  rtask = new RateEditTask();
		rtask.execute(HttpUrls.RATEEDIT + "", phone,agentids,number);
	}
	
	private void rateWithdrawalsedit(String number){
		RateWithdrawalsEditTask  rtasks = new RateWithdrawalsEditTask();
		rtasks.execute(HttpUrls.RATEWITHDRAWALSEDIT + "", phone,agentids,number);
	}
	
	
	
	class RateWithdrawalsEditTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在处理中。。。");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate.getAgentMidatc(HttpUrls.RATEWITHDRAWALSEDIT, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (Entity.STATE_OK
						.equals(result.get(Entity.RSPCOD).toString())) {
					warnDialog = new OneButtonDialogWarn(
							AgentListActivity1.this, R.style.CustomDialog, "提示",
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
					Toast.makeText(getApplicationContext(),
							result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();
					// ToastCustom.showMessage(AgentListActivity.this,
					// result.get(Entity.RSPMSG).toString());
				}
			}
			super.onPostExecute(result);
		}
	}
	
	class TransferTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			WarnDialog.dismiss();
			showLoadingDialog("正在处理中。。。");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3], params[4], params[5] };
			return NetCommunicate
					.getAgentMidatc(HttpUrls.ACTCODETRANSFER, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (Entity.STATE_OK.equals(result.get(Entity.RSPCOD).toString())) {
					
					warnDialog = new OneButtonDialogWarn(AgentListActivity1.this,
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
					Toast.makeText(getApplicationContext(), result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(AgentListActivity.this,
//							result.get(Entity.RSPMSG).toString());
				}
			}
			super.onPostExecute(result);
		}
	}
	
	class RateEditTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在处理中。。。");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3]};
			return NetCommunicate.getAgentMidatc(HttpUrls.RATEEDIT,
					values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (Entity.STATE_OK
						.equals(result.get(Entity.RSPCOD).toString())) {
					warnDialog = new OneButtonDialogWarn(AgentListActivity1.this,
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
					Toast.makeText(getApplicationContext(), result.get(Entity.RSPMSG).toString(),
							Toast.LENGTH_SHORT).show();
//					ToastCustom.showMessage(AgentListActivity.this,
//							result.get(Entity.RSPMSG).toString());
				}
			}
			super.onPostExecute(result);
		}
	}
	

}

