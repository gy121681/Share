package com.td.qianhai.epay.oem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.share.app.entity.response.ActivationdeListResponse;
import com.share.app.entity.response.Constans;
import com.share.app.network.CallbackList;
import com.share.app.network.Request;
import com.td.qianhai.epay.oem.adapter.ActivatedCodeAdapter;
import com.td.qianhai.epay.oem.adapter.SubAgentAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.Entity;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.td.qianhai.epay.oem.views.dialog.ChooseDialog;
import com.td.qianhai.epay.oem.views.dialog.EidtDialog;
import com.td.qianhai.epay.oem.views.dialog.MyEditDialog;
import com.td.qianhai.epay.oem.views.dialog.OneButtonDialogWarn;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnEditDialogChlicListener;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;


public class AgentListActivity extends BaseActivity implements OnScrollListener {

	private String phone,agtid;
	
	private View moreView;
	private int page = 1; // 页数
	private int allPageNum = 0; // 总页数
	private int PAGE_SIZE = 20;
	private boolean isThreadRun = false; // 加载数据线程运行状态
	private ListView listView;
	private LayoutInflater inflater;
	private ArrayList<HashMap<String, Object>> mList;
	private TextView null_datas,query_tv;
	private int lastItem;
//	private SubAgentAdapter adapter;
	private ActivatedCodeAdapter mAdapter;
	private  EidtDialog doubleWarnDialogs;
	private String tag ;
	
	private MyEditDialog WarnDialog;
	private String agentids = "";
	private  ChooseDialog chooseDialog;
	private String[] rlist;
	private String minfeerate,isretailers,issaleagt,isgeneralagent;
	private OneButtonDialogWarn warnDialog;
	private String ratestr = "",setnum;
	private String querypname = "",querypbone = "";
	private EditText query_ed;
	private String min;
	
	private String mLevel;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppContext.getInstance().addActivity(this);
		setContentView(R.layout.activity_agent_list1);
//		phone = ((AppContext)getApplication()).getMobile();
		phone = MyCacheUtil.getshared(this).getString("Mobile", "");
//		agtid = ((AppContext)getApplication()).getAgentid();
		agtid = MyCacheUtil.getshared(this).getString("AGENTID", "");
		isretailers = MyCacheUtil.getshared(this).getString("ISRETAILERS", "");
		issaleagt = MyCacheUtil.getshared(this).getString("ISSALEAGT", "");
		isgeneralagent = MyCacheUtil.getshared(this).getString("ISGENERALAGENT", "");
        mLevel = getIntent().getStringExtra("level");
		rlist = ((AppContext)getApplication()).getRatelist();
		Intent it = getIntent();
		tag = it.getStringExtra("tag");
		setnum = it.getStringExtra("num");
		min = it.getStringExtra("min");
		
		initview();
		
//		showLoadingDialog("正在查询中...");
//		new Thread(run).start();

	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mList.clear();
//		adapter = null;
	}
	
	private void initview() {
	
		if(Constans.MemberLevel.LEVEL_VIP.equals(mLevel)){
			((TextView) findViewById(R.id.tv_title_contre)).setText("高级会员");
		}else{
			((TextView) findViewById(R.id.tv_title_contre)).setText("普通会员");
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
		mAdapter = new ActivatedCodeAdapter();
//		adapter = new SubAgentAdapter(AgentListActivity.this, mList);
		moreView = inflater.inflate(R.layout.load, null);
		null_datas = (TextView) findViewById(R.id.null_datas);
		listView = (ListView) findViewById(R.id.sub_agt_list);
		query_ed = (EditText) findViewById(R.id.query_ed);
		query_tv = (TextView) findViewById(R.id.query_tv);
		listView.addFooterView(moreView);
		moreView.setVisibility(View.GONE);
		listView.setOnScrollListener(this);
		listView.setAdapter(mAdapter);
//		listView.setAdapter(adapter);
		if (mList.size() == 0) {
			// 加载数据
			loadMore();
		}
		query_ed.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					query_tv.setEnabled(true);
				} else {
					query_tv.setEnabled(false);
					querypname = "";
					querypbone = "";
					page = 1;
					mList.clear();
					loadMore();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
		
		query_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String s = query_ed.getText().toString();
				page = 1;
				mList.clear();
				if (gettext(s)) {
					querypbone = "";
					querypname = s;
				} else {
					querypbone = s;
					querypname = "";
				}
				loadMore();

			}
		});
		
			     
		
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
//					long id) {
//				// TODO Auto-generated method stub
//				if(isgeneralagent.equals("1")||issaleagt.equals("1")||isretailers.equals("1")){
//					String phone = mList.get((int)id).get("merphonenumber").toString();
//					if(tag.equals("0")){
////						agentids = mList.get((int)id).get("AGENTID").toString();
//						showeditdialog(phone);
//				}else{
//					warnDialog = new OneButtonDialogWarn(AgentListActivity.this,
//							R.style.CustomDialog, "提示",
//							"请先升级会员等级再操作", "确定",
//							new OnMyDialogClickListener() {
//								@Override
//								public void onClick(View v) {
//
//									warnDialog.dismiss();
//								}
//							});
//					warnDialog.setCancelable(false);
//					warnDialog.setCanceledOnTouchOutside(false);
//					warnDialog.show();
//				}
//
//				}
//			}
//		});
		

		

		
		
		
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
	
	private boolean gettext(String text){
		   Pattern p = Pattern.compile("[0-9]*");   
		     Matcher m = p.matcher(text);   
		     if(m.matches() ){  
		    	 return false;
		    	 
		      }   
		     
		     p=Pattern.compile("[\u4e00-\u9fa5]");  
		     m=p.matcher(text.substring(0, 1));  
		     if(m.matches()){  
		    	 return true;
		     }
			return false;  
	}
	
        
//	
//	private void PopUpBox(String[] b) {
//		chooseDialog = new ChooseDialog(
//				AgentListActivity.this,
//				R.style.CustomDialog,
//				new OnBackDialogClickListener() {
//
//					@Override
//					public void OnBackClick(View v, String str,
//							int position) {
////						positions = position+1;
//						ratestr = str.substring(0,str.length()-1);
//						SpannableString msp = new SpannableString("您确定修改此代理商的费率吗?");
//						showDoubleWarnDialog(msp);
////						// TODO Auto-generated method stub
//						chooseDialog.dismiss();
//					}
//				}, "修改费率", b);
//		chooseDialog.show();
//		
//	}
	
//	@Override
//	protected void doubleWarnOnClick(View v) {
//		// TODO Auto-generated method stub
//		super.doubleWarnOnClick(v);
//		switch (v.getId()) {
//		case R.id.btn_left:
//			doubleWarnDialog.dismiss();
//			
//			break;
//		case R.id.btn_right:
//			doubleWarnDialog.dismiss();
//			rateedit(ratestr);
//			
//			break;
//
//		default:
//			break;
//		}
//	}
	
	
	private void showeditdialog(final String tophone) {
//		doubleWarnDialogs = new EidtDialog(AgentListActivity.this, R.style.MyEditDialog, "" ,"可用数量"+setnum,"确认", "取消", "", new OnEditDialogChlicListener() {
//			
//			@Override
//			public void onClick(View v, String a) {
//				// TODO Auto-generated method stub
//				
//			}
//		}, 0)
		//可用数量"+setnum
		
		doubleWarnDialogs = new EidtDialog(AgentListActivity.this, R.style.MyEditDialog, "激活码划拨", "建议零售价198/个", "确认", "取消", "", new OnEditDialogChlicListener() {
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
						return;
					}else if(Integer.parseInt(a)>5){
						Toast.makeText(getApplicationContext(), "最多可划拨5个激活码",
								Toast.LENGTH_SHORT).show();
//						doubleWarnDialogs.dismiss();
						return;
					}else {
						TransferTask  ttask = new TransferTask();
						ttask.execute(HttpUrls.CODETRANSFER + "", phone,tophone,a);
					}
					
					break;
				default:
					break;
				}
			}
		},0);
		doubleWarnDialogs.setCancelable(false);
		doubleWarnDialogs.setCanceledOnTouchOutside(false);
		doubleWarnDialogs.show();
//		
	}
	
	class TransferTask extends
			AsyncTask<String, Integer, HashMap<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog("正在处理中。。。");
		}

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			String[] values = { params[0], params[1], params[2], params[3] };
			return NetCommunicate.getAgentMidatc(HttpUrls.CODETRANSFER, values);
		}

		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			loadingDialogWhole.dismiss();
			if (result != null) {
				if (Entity.STATE_OK
						.equals(result.get(Entity.RSPCOD).toString())) {

					warnDialog = new OneButtonDialogWarn(
							AgentListActivity.this, R.style.CustomDialog, "提示",
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
	
	private void loadMore() {
		if (page != 1 && page > allPageNum) {
			Toast.makeText(getApplicationContext(), "没有更多记录了",
					Toast.LENGTH_SHORT).show();
//			ToastCustom.showMessage(this, "没有更多记录了");
//			moreView.setVisibility(View.GONE);
			return;
		}
		if (!isThreadRun) {
			isThreadRun = true;
			showLoadingDialog("正在查询中...");
//			new Thread(run).start();
			requestActivatedCodeList();
		}

	}

	/**
	 * 获取已激活列表
	 */
	private void requestActivatedCodeList() {
		String userId = MyCacheUtil.getshared(this).getString(Constans.Login.USERID, "");
		Request.getMemberManagerQueryActivationideList(userId, querypname, querypbone,
				String.valueOf(page), String.valueOf(PAGE_SIZE),
				new CallbackList<List<ActivationdeListResponse>>() {
					@Override
					public void onFailure(String msg) {
						loadingDialogWhole.dismiss();
						toast(msg);
                        isThreadRun = false;
					}

					@Override
					public void onSuccess(List<ActivationdeListResponse> data) {
						loadingDialogWhole.dismiss();
						if (page == 1) {
							mAdapter.setDatas(data);
						} else {
							mAdapter.addDatas(data);
						}
						page ++;
                        isThreadRun = false;
					}

					@Override
					public void onNetError(int code, String msg) {
						loadingDialogWhole.dismiss();
                        isThreadRun = false;
					}
				});
	}


	Runnable run = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//			String[] values = { HttpUrls.CHIRDACT + "", phone, agtid, page+"", PAGE_SIZE+"" };
//			MyAgtBean entitys = NetCommunicate.getSubAgentBill(
//					HttpUrls.CHIRDACT, values);
			String[] values = {agtid, page+"", PAGE_SIZE+"",querypbone,querypname,"0"};
			try {
				list = NetCommunicate.executeHttpPostnull(HttpUrls.UNDERLINGURL,
						HttpKeys.SUBORDINATEAGENTSBACK,HttpKeys.SUBORDINATEAGENTSASK,values);
			} catch (HttpHostConnectException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Message msg = new Message();
			if (list != null) {
				mList.addAll(list);
				if (list.size() <= 0 || list == null) {

					msg.what = 2;
				} else {
					msg.what = 1;
				}
				page ++;
			} else {
				
				msg.what = 3;
			}
			isThreadRun = false;
			handler.sendMessage(msg);
		}
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				loadingDialogWhole.dismiss();
				null_datas.setVisibility(View.GONE);
				moreView.setVisibility(View.GONE);
//				adapter.notifyDataSetChanged();
				break;
			case 2:
				loadingDialogWhole.dismiss();
				Toast.makeText(getApplicationContext(), "没有更多记录了",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(AgentListActivity.this, "没有更多记录了");
//				null_datas.setVisibility(View.VISIBLE);
				break;
			case 3:
				loadingDialogWhole.dismiss();
				null_datas.setVisibility(View.VISIBLE);
				break;
			default:
				loadingDialogWhole.dismiss();
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

//public class AgentListActivity extends BaseActivity implements OnScrollListener {
//
//	private String phone,agtid;
//	
//	private View moreView;
//	private int page = 1; // 页数
//	private int allPageNum = 0; // 总页数
//	private int PAGE_SIZE = 20;
//	private boolean isThreadRun = false; // 加载数据线程运行状态
//	private ListView listView;
//	private LayoutInflater inflater;
//	private ArrayList<HashMap<String, Object>> mList;
//	private TextView null_datas;
//	private int lastItem;
//	private SubAgentAdapter adapter;
//	private  EidtDialog doubleWarnDialogs;
//	private String tag ;
//	private MyEditDialog WarnDialog;
//	private String agentids = "";
//	private  ChooseDialog chooseDialog;
//	private String[] rlist;
//	private OneButtonDialogWarn warnDialog;
//	private String ratestr = "",setnum;
//	private String min;
//	
//	@Override
//	@SuppressLint("NewApi")
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		AppContext.getInstance().addActivity(this);
//		setContentView(R.layout.activity_agent_list);
////		phone = ((AppContext)getApplication()).getMobile();
//		phone = MyCacheUtil.getshared(this).getString("Mobile", "");
////		agtid = ((AppContext)getApplication()).getAgentid();
//		agtid = MyCacheUtil.getshared(this).getString("AGENTID", "");
//		rlist = ((AppContext)getApplication()).getRatelist();
//		Intent it = getIntent();
//		tag = it.getStringExtra("tag");
//		setnum = it.getStringExtra("num");
//		min = it.getStringExtra("min");
//		
//		initview();
//
////		showLoadingDialog("正在查询中...");
////		new Thread(run).start();
//
//	}
//	
//	private void initview() {
//	
//		if(tag.equals("0")){
//			((TextView) findViewById(R.id.tv_title_contre)).setText("激活码划拨");
//		}else{
//			((TextView) findViewById(R.id.tv_title_contre)).setText("下级商户查询");
//		}
//		
//		findViewById(R.id.bt_title_left).setOnClickListener(
//				new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						finish();
//					}
//				});
//		mList = new ArrayList<HashMap<String,Object>>();
//		inflater = LayoutInflater.from(this);
//		adapter = new SubAgentAdapter(AgentListActivity.this, mList);
//		moreView = inflater.inflate(R.layout.load, null);
//		null_datas = (TextView) findViewById(R.id.null_datas);
//		listView = (ListView) findViewById(R.id.sub_agt_list);
//		listView.addFooterView(moreView);
//		moreView.setVisibility(View.GONE);
//		listView.setOnScrollListener(this);
//		listView.setAdapter(adapter);
//		
//		
//		if (mList.size() == 0) {
//			// 加载数据
//			loadMore();
//		}
//		
//		
//		
////		listView.setOnScrollListener(new OnScrollListener() {
////		    @Override
////		    public void onScrollStateChanged(AbsListView view, int scrollState) {
////		        switch (scrollState) {
////		            // 当不滚动时
////		            case OnScrollListener.SCROLL_STATE_IDLE:
////		                // 判断滚动到底部
////		                if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
////		                	loadMore();
////		                	
////		              }
////		              break;
////		        }
////		    }
////		 
////		    @Override
////		    public void onScroll(AbsListView view, int firstVisibleItem,
////		           int visibleItemCount, int totalItemCount) {
////		    }
////		});
//		
//	}
//	
////	
////	private void PopUpBox(String[] b) {
////		chooseDialog = new ChooseDialog(
////				AgentListActivity.this,
////				R.style.CustomDialog,
////				new OnBackDialogClickListener() {
////
////					@Override
////					public void OnBackClick(View v, String str,
////							int position) {
//////						positions = position+1;
////						ratestr = str.substring(0,str.length()-1);
////						SpannableString msp = new SpannableString("您确定修改此代理商的费率吗?");
////						showDoubleWarnDialog(msp);
//////						// TODO Auto-generated method stub
////						chooseDialog.dismiss();
////					}
////				}, "修改费率", b);
////		chooseDialog.show();
////		
////	}
//	
////	@Override
////	protected void doubleWarnOnClick(View v) {
////		// TODO Auto-generated method stub
////		super.doubleWarnOnClick(v);
////		switch (v.getId()) {
////		case R.id.btn_left:
////			doubleWarnDialog.dismiss();
////			
////			break;
////		case R.id.btn_right:
////			doubleWarnDialog.dismiss();
////			rateedit(ratestr);
////			
////			break;
////
////		default:
////			break;
////		}
////	}
//	
//	
////	private void showeditdialog() {
////		doubleWarnDialogs = new EidtDialog(AgentListActivity.this, R.style.MyEditDialog, "", "可用数量"+setnum, "确认", "取消", "", new OnEditDialogChlicListener() {
////			
////			@Override
////			public void onClick(View v, String a) {
////				// TODO Auto-generated method stub
////				switch (v.getId()) {
////				case R.id.btn_right:
////					doubleWarnDialogs.dismiss();
////					InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////					m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
////					break;
////				case R.id.btn_left:
////					if(a.equals("0")||a.length()<=0){
////						Toast.makeText(getApplicationContext(), "请输入正确划拨数量",
////								Toast.LENGTH_SHORT).show();
//////						ToastCustom.showMessage(getApplicationContext(), "请输入正确划拨数量");
////					}else{
////						shwpaypwd(a);
////						doubleWarnDialogs.dismiss();
////					}
////					
////					break;
////				default:
////					break;
////				}
////			}
////		},0);
////		doubleWarnDialogs.setCancelable(false);
////		doubleWarnDialogs.setCanceledOnTouchOutside(false);
////		doubleWarnDialogs.show();
////		
////	}
//	
//	private void loadMore() {
////		if (page != 1 && page > allPageNum) {
////			Toast.makeText(getApplicationContext(), "没有更多记录了",
////					Toast.LENGTH_SHORT).show();
//////			ToastCustom.showMessage(this, "没有更多记录了");
////			moreView.setVisibility(View.GONE);
////			return;
////		}
//		if (!isThreadRun) {
//			isThreadRun = true;
//			showLoadingDialog("正在查询中...");
//			new Thread(run).start();
//		}
//
//	}
//
//	Runnable run = new Runnable() {
//
//		@Override
//		public void run() {
//			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
////			String[] values = { HttpUrls.CHIRDACT + "", phone, agtid, page+"", PAGE_SIZE+"" };
////			MyAgtBean entitys = NetCommunicate.getSubAgentBill(
////					HttpUrls.CHIRDACT, values);
//			String[] values = {agtid, page+"", PAGE_SIZE+""};
//			try {
//				list = NetCommunicate.executeHttpPostnull(HttpUrls.UNDERLINGURL,
//						HttpKeys.SUBORDINATEAGENTSBACK,HttpKeys.SUBORDINATEAGENTSASK,values);
//			} catch (HttpHostConnectException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ClientProtocolException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalStateException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			Message msg = new Message();
//			if (list != null) {
//				mList.addAll(list);
//				if (list.size() <= 0 || list == null) {
//
//					msg.what = 2;
//				} else {
//					msg.what = 1;
//				}
//				page ++;
//			} else {
//				
//				msg.what = 3;
//			}
//			isThreadRun = false;
//			loadingDialogWhole.dismiss();
//			handler.sendMessage(msg);
//		}
//	};
//
//	private Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 1:
//				null_datas.setVisibility(View.GONE);
//				moreView.setVisibility(View.GONE);
//				adapter.notifyDataSetChanged();
//				break;
//			case 2:
//				moreView.setVisibility(View.GONE);
//				Toast.makeText(getApplicationContext(), "没有更多记录了",
//						Toast.LENGTH_SHORT).show();
////				ToastCustom.showMessage(AgentListActivity.this, "没有更多记录了");
////				null_datas.setVisibility(View.VISIBLE);
//				break;
//			case 3:
//				null_datas.setVisibility(View.VISIBLE);
//				break;
//			default:
//				
//				break;
//			}
//		};
//	};
//
//	@Override
//	public void onScroll(AbsListView arg0, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//		// TODO Auto-generated method stub
//		lastItem = firstVisibleItem + visibleItemCount-1;
//	}
//
//	@Override
//	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
//		// TODO Auto-generated method stub
//		if (scrollState == SCROLL_STATE_IDLE) {
//			if (lastItem == mList.size()) {
//			moreView.setVisibility(View.VISIBLE);
//			loadMore();
//			}
//		}
//	}
	
//	protected void shwpaypwd(String num) {
//		final String number = num;
//		
//		WarnDialog = new MyEditDialog(AgentListActivity.this,
//				R.style.MyEditDialog, "充值", "请输入支付密码", "确认", "取消", "",
//				new OnMyDialogClickListener() {
//
//					@Override
//					public void onClick(View v) {
//
//						switch (v.getId()) {
//						case R.id.btn_right:
//							WarnDialog.dismiss();
//							InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//							break;
//						case R.id.btn_left:
//							break;
//						default:
//							break;
//						}
//						
//					}
//				},
//		new onMyaddTextListener() {
//			
//			@Override
//			public void refreshActivity(String paypwd) {
//				
//				if (paypwd == null || paypwd.equals("")) {
//					Toast.makeText(getApplicationContext(), "请输入支付密码",
//							Toast.LENGTH_SHORT).show();
////					ToastCustom.showMessage(
////							AgentListActivity.this,
////							"请输入支付密码！");
//					return;
//				}
//				if (paypwd.length() < 6 || paypwd.length() > 15) {
//					Toast.makeText(getApplicationContext(), "输入的密码长度有误,请输入6个数字、字母或特殊符号",
//							Toast.LENGTH_SHORT).show();
////					ToastCustom.showMessage(
////							AgentListActivity.this,
////							"输入的密码长度有误,请输入6个数字、字母或特殊符号！");
//					return;
//				}
//
//					TransferTask  ttask = new TransferTask();
//					ttask.execute(HttpUrls.ACTCODETRANSFER + "", phone,agtid,agentids,number,paypwd);
//
//
//				
//			}
//		});
////		agents_new_actcode.setEnabled(true);
//		WarnDialog.setCancelable(false);
//		WarnDialog.setCanceledOnTouchOutside(false);
//		WarnDialog.show();
//		
//
//		
//	}
//	
//	private void rateedit(String number){
//		RateEditTask  rtask = new RateEditTask();
//		rtask.execute(HttpUrls.RATEEDIT + "", phone,agentids,number);
//	}
//	
//	class TransferTask extends
//			AsyncTask<String, Integer, HashMap<String, Object>> {
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			WarnDialog.dismiss();
//			showLoadingDialog("正在处理中。。。");
//		}
//
//		@Override
//		protected HashMap<String, Object> doInBackground(String... params) {
//			String[] values = { params[0], params[1], params[2], params[3], params[4], params[5] };
//			return NetCommunicate
//					.getAgentMidatc(HttpUrls.ACTCODETRANSFER, values);
//		}
//
//		@Override
//		protected void onPostExecute(HashMap<String, Object> result) {
//			loadingDialogWhole.dismiss();
//			if (result != null) {
//				if (Entity.STATE_OK.equals(result.get(Entity.RSPCOD).toString())) {
//					
//					warnDialog = new OneButtonDialogWarn(AgentListActivity.this,
//							R.style.CustomDialog, "提示",
//							result.get(Entity.RSPMSG).toString(), "确定",
//							new OnMyDialogClickListener() {
//								@Override
//								public void onClick(View v) {
//									finish();
//									warnDialog.dismiss();
//								}
//							});
//					warnDialog.setCancelable(false);
//					warnDialog.setCanceledOnTouchOutside(false);
//					warnDialog.show();
//				} else {
//					Toast.makeText(getApplicationContext(), result.get(Entity.RSPMSG).toString(),
//							Toast.LENGTH_SHORT).show();
////					ToastCustom.showMessage(AgentListActivity.this,
////							result.get(Entity.RSPMSG).toString());
//				}
//			}
//			super.onPostExecute(result);
//		}
//	}
//	
//	class RateEditTask extends
//			AsyncTask<String, Integer, HashMap<String, Object>> {
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			showLoadingDialog("正在处理中。。。");
//		}
//
//		@Override
//		protected HashMap<String, Object> doInBackground(String... params) {
//			String[] values = { params[0], params[1], params[2], params[3]};
//			return NetCommunicate.getAgentMidatc(HttpUrls.RATEEDIT,
//					values);
//		}
//
//		@Override
//		protected void onPostExecute(HashMap<String, Object> result) {
//			loadingDialogWhole.dismiss();
//			if (result != null) {
//				if (Entity.STATE_OK
//						.equals(result.get(Entity.RSPCOD).toString())) {
//					warnDialog = new OneButtonDialogWarn(AgentListActivity.this,
//							R.style.CustomDialog, "提示",
//							result.get(Entity.RSPMSG).toString(), "确定",
//							new OnMyDialogClickListener() {
//								@Override
//								public void onClick(View v) {
//									finish();
//									warnDialog.dismiss();
//								}
//							});
//					warnDialog.setCancelable(false);
//					warnDialog.setCanceledOnTouchOutside(false);
//					warnDialog.show();
//					
//				} else {
//					Toast.makeText(getApplicationContext(), result.get(Entity.RSPMSG).toString(),
//							Toast.LENGTH_SHORT).show();
////					ToastCustom.showMessage(AgentListActivity.this,
////							result.get(Entity.RSPMSG).toString());
//				}
//			}
//			super.onPostExecute(result);
//		}
//	}
	

}
