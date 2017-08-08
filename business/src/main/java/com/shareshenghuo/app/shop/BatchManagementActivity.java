package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.entity.StringEntity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.adapter.CommodityManageAdapter1;
import com.shareshenghuo.app.shop.adapter.EtcAdapter;
import com.shareshenghuo.app.shop.adapter.PopTypeAdapter;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.CommodityManageBean;
import com.shareshenghuo.app.shop.network.bean.ShopCategoryBean;
import com.shareshenghuo.app.shop.network.request.CommodityManageRequest;
import com.shareshenghuo.app.shop.network.request.ShopCategoryRequest;
import com.shareshenghuo.app.shop.network.response.AutResponse;
import com.shareshenghuo.app.shop.network.response.CommodityManageResponse;
import com.shareshenghuo.app.shop.network.response.ShopCategoryResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.widget.dialog.MyEditDialog1;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.TwoButtonDialog;
import com.shareshenghuo.app.shop.widget.dialog.onMyaddTextListener;

public class BatchManagementActivity extends BaseTopActivity implements OnRefreshListener2<ScrollView>{
	
	
	private int pageNo = 1;
	private int pageSize = 15;
	private CommodityManageAdapter1 adapter;
//	private PullToRefreshListView lvData;
	
	private ListView lvData;
	private EditText edKeyword;
	private ImageView ivSearch;
	private TextView tv1,tv2,tv_type;
	private RelativeLayout search;
	private RelativeLayout re_check;
	private CheckBox check_dels;
	private TwoButtonDialog downloadDialog;
	private MyEditDialog1 doubleWarnDialog1;
	private LinearLayout lin_layout;
	private List<CommodityManageBean> data;
	private List<ShopCategoryBean> datas;
	private EtcAdapter typeadapter;
	private ListView listivew;
	private  StringBuilder strshopsype =new StringBuilder(); 
	private PullToRefreshScrollView refreshscrollview;
	private   PopupWindow popupWindow;
	private RelativeLayout re1,re2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commodity_manag_activity);
		
		initpopwind();
		initView();
		initd();
		loadData();
	}
	
	private void initd() {
		 data = new ArrayList<CommodityManageBean>();
		adapter = new CommodityManageAdapter1(this, data);
		lvData.setAdapter(adapter);
	}

	public void initView() {
		initTopBar("批量管理");
		tv_type = getView(R.id.tv_type);
		lvData = getView(R.id.lvShop);
		re1 = getView(R.id.re1);
		re2 = getView(R.id.re2);
		 getView(R.id.img1).setVisibility(View.GONE);
		 getView(R.id.img2).setVisibility(View.GONE);
		tv1 = getView(R.id.tv1);
		tv1.setText("删除");
		tv2 = getView(R.id.tv2);
		tv2.setText("分类至");
		edKeyword = getView(R.id.edSearch);
		edKeyword.setHint("查询");
		ivSearch = getView(R.id.ivSearch);
		lin_layout = getView(R.id.lin_layout);
		search = getView(R.id.search);
		search.setVisibility(View.GONE);
		re_check = getView(R.id.re_check);
		re_check.setVisibility(View.VISIBLE);
		check_dels= getView(R.id.check_dels);
		refreshscrollview = getView(R.id.refreshscrollview);
		refreshscrollview.setOnRefreshListener(this);
		refreshscrollview.setMode(Mode.BOTH);
		
		check_dels.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
//				if(arg1){
					adapter.setcheck(arg1);
//				}
			}
		});
		
		lvData.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				adapter.setchoose((int)arg3);
			}
		});
		
		re1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(adapter.ischeck()){
					initDialog();
				}else{
					T.showShort(getApplicationContext(), "未选中");
				}
				
			}
		});
		re2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(adapter.ischeck()){
					initd(false);
					showPopupWindow(tv_type);
				}else{
					T.showShort(getApplicationContext(), "未选中");
				}

			}
		});
		
	}
	
	
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		CommodityManageRequest req = new CommodityManageRequest();
		req.shopId = UserInfoManager.getUserInfo(this).shop_id+"";
		req.page_no = pageNo;
		req.page_size = pageSize;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.SHOPMANAGE, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
				refreshscrollview.onRefreshComplete();
					T.showNetworkError(getApplicationContext());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				refreshscrollview.onRefreshComplete();
					CommodityManageResponse bean = new Gson().fromJson(resp.result, CommodityManageResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						updateView(bean.data);
					} else {
						T.showShort(getApplicationContext(), bean.result_desc);
					}
			}
		});
	}
	
	public void updateView(List<CommodityManageBean> data) {
		if(pageNo==1 || adapter==null) {
			this.data.clear();
			this.data.addAll(data);
			adapter.notifyDataSetChanged();
//			adapter = new CommodityManageAdapter(this, data);
//			lvData.setAdapter(adapter);
		}
		if(pageNo > 1) {
			adapter.getmData().addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}

	
	private void initDialog() {
		// TODO Auto-generated method stub
		downloadDialog = new TwoButtonDialog(BatchManagementActivity.this, R.style.CustomDialog,
				"提示", "确定删除?", "否", "是",true,new OnMyDialogClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						switch (v.getId()) {
						case R.id.Button_OK:
							downloadDialog.dismiss();
							break;
						case R.id.Button_cancel:
						StringBuilder budd = new StringBuilder();
							List<CommodityManageBean> data = adapter.getchoose();
							List<Integer> list = new ArrayList<Integer>();
							for (int i = 0; i < data.size(); i++) {
								if(data.get(i).ischeck){
									list.add(i);
									budd.append(data.get(i).id+",");
//									}
								}
							}
							deletegoodstype(budd,list);
//							Log.e("", "budd = = =   "+budd.toString());
							downloadDialog.dismiss();
						default:
							break;
						}
					}
				});
			downloadDialog.show();
	}
	
	
	public void deletegoodstype(StringBuilder budd, final List<Integer> list) {
		ShopCategoryRequest req = new ShopCategoryRequest();
		req.ids = budd.toString();
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.BATCHDELETEGOODSBYIDS, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
					T.showNetworkError(getApplicationContext());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
				AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						
						if(bean.data!=null&&bean.data.RSPCOD.equals("000000")){
							T.showShort(getApplicationContext(), bean.data.RSPMSG);
							pageNo = 1;
							loadData();
						}else{
							T.showShort(getApplicationContext(), bean.data.RSPMSG);
						}
						
					} else {
						T.showShort(getApplicationContext(), bean.result_desc);
					}
			}
		});
	}

	
	 private void initpopwind() {
			// TODO Auto-generated method stub
		        // 一个自定义的布局，作为显示的内容
			 datas = new ArrayList<ShopCategoryBean>();
		        View contentView = LayoutInflater.from(this).inflate(
		                R.layout.comm_pop, null);
		        // 设置按钮的点击事件
		        TextView button = (TextView) contentView.findViewById(R.id.tv_conmmit);
		        TextView newtype = (TextView) contentView.findViewById(R.id.newtype);
		        TextView tv_dimss = (TextView) contentView.findViewById(R.id.tv_dimss);
		        final TextView content = (TextView) contentView.findViewById(R.id.content);
		        listivew = (ListView) contentView.findViewById(R.id.listview);
		      
		        popupWindow = new PopupWindow(contentView,
			                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		       	popupWindow.setTouchable(true);
			       
			   	popupWindow.setTouchInterceptor(new OnTouchListener() {
				@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						// TODO Auto-generated method stub
							return false;
						}
			     });
			      
			   	ColorDrawable dw = new ColorDrawable(0xb0000000);  
			     //设置SelectPicPopupWindow弹出窗体的背景  
			    popupWindow.setBackgroundDrawable(dw);  
			    popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		        
		        typeadapter = new EtcAdapter( BatchManagementActivity.this,datas);
		        listivew.setAdapter(typeadapter);
		        content.setText("分类至(按商分类展示商品,方便买家筛选)");
//		        button.setText("");
		        
		        button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						strshopsype = null;
						strshopsype =new StringBuilder();
						 List<ShopCategoryBean> dt = typeadapter.getchoose();
						 for (int i = 0; i < dt.size(); i++) {
							if(dt.get(i).ischeck){
								Log.e("", "选中了"+i);
								strshopsype.append(datas.get(i).id+",");
							}
						}
						 
						StringBuilder budd = new StringBuilder();
							List<CommodityManageBean> data = adapter.getchoose();
							for (int i = 0; i < data.size(); i++) {
								if(data.get(i).ischeck){
									budd.append(data.get(i).id+",");
								}
							}
							
							if(strshopsype==null||strshopsype.length()<=0){
								T.showShort(BatchManagementActivity.this, "未选择分类");
								return;
							}
							
							addshoptype(strshopsype.toString(),budd.toString());
						popupWindow.dismiss();
					}
				});
		        tv_dimss.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						popupWindow.dismiss();
					}
				});
		        
		        listivew.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						typeadapter.setcheck((int)arg3);
					}
				});
		        
		        newtype.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						showeditdailog();
					}
				});
		        popupWindow.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss() {
						WindowManager.LayoutParams lp = getWindow().getAttributes();
						lp.alpha = 1f;
						getWindow().setAttributes(lp);
					}
				});
			
		}
	
	 public void showeditdailog(){
			doubleWarnDialog1 = new MyEditDialog1(BatchManagementActivity.this,
					R.style.CustomDialog, "新建分类名称", "", "确认", "取消", "",
					new OnMyDialogClickListener() {

						@Override
						public void onClick(View v) {

							switch (v.getId()) {
							case R.id.btn_right:
								doubleWarnDialog1.dismiss();
//								InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//								m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
								break;
							case R.id.btn_left:
								String paypwd = doubleWarnDialog1.getpaypwd();

								if (paypwd == null || paypwd.equals("")) {
									Toast.makeText(BatchManagementActivity.this,"不能为空",
											Toast.LENGTH_SHORT).show();
									return;
								}
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
								Toast.makeText(BatchManagementActivity.this.getApplicationContext(),"不能为空",
										Toast.LENGTH_SHORT).show();
//								ToastCustom.showMessage(
//										TransferAccountsActivity.this,
//										"请输入支付密码！");
								return;
							}
							doubleWarnDialog1.dismiss();
							Toast.makeText(BatchManagementActivity.this.getApplicationContext(), paypwd, Toast.LENGTH_SHORT).show();
						}
					});

			doubleWarnDialog1.setCancelable(false);
			doubleWarnDialog1.setCanceledOnTouchOutside(false);
			doubleWarnDialog1.show();
			
		}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		pageNo = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// TODO Auto-generated method stub
		loadData();
	}
	
	private void showPopupWindow(final TextView  view) {

		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.7f;
		getWindow().setAttributes(lp);
		popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
//	        popupWindow.showAsDropDown(view);
	       
	    }
	
	
	 private void initd(final boolean tag) {
			
			ShopCategoryRequest req = new ShopCategoryRequest();
			req.shopId = UserInfoManager.getUserInfo(this).shop_id+"";
			RequestParams params = new RequestParams();
			try {
				params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			new HttpUtils().send(HttpMethod.POST, Api.FINDALLGOODSTYPELIST, params, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					ProgressDialogUtil.dismissProgressDlg();
						T.showNetworkError(getApplicationContext());
				}

				@Override
				public void onSuccess(ResponseInfo<String> resp) {
					ProgressDialogUtil.dismissProgressDlg();
					ShopCategoryResponse bean = new Gson().fromJson(resp.result, ShopCategoryResponse.class);
						if(Api.SUCCEED == bean.result_code) {
							datas.clear();
							datas.addAll(bean.data);
							if(typeadapter!=null){
								typeadapter.notifyDataSetChanged();
//								listivew.setAdapter(typeadapter);
							}
							if(tag){
								listivew.setSelection(typeadapter.getCount()-1);  
							}
							
						} else {
							T.showShort(getApplicationContext(), bean.result_desc);
						}
				}
			});
		}
	 
	 private void addshoptype(String typeId,String ids) {
			// TODO Auto-generated method stub
			ShopCategoryRequest req = new ShopCategoryRequest();
			req.typeIds = typeId;
			req.ids = ids;
			RequestParams params = new RequestParams();
			try {
				params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			new HttpUtils().send(HttpMethod.POST, Api.BATCHTYPEGOODS, params, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					ProgressDialogUtil.dismissProgressDlg();
						T.showNetworkError(getApplicationContext());
				}

				@Override
				public void onSuccess(ResponseInfo<String> resp) {
					ProgressDialogUtil.dismissProgressDlg();
					AutResponse bean = new Gson().fromJson(resp.result, AutResponse.class);
						if(Api.SUCCEED == bean.result_code) {
							if(bean.data!=null&&bean.data.RSPCOD.equals("000000")){
								T.showShort(getApplicationContext(), bean.data.RSPMSG);
//								finish();
							}else{
								T.showShort(getApplicationContext(), bean.data.RSPMSG);
							}
						} else {
							T.showShort(getApplicationContext(), bean.result_desc);
						}
				}
			});
		}
}