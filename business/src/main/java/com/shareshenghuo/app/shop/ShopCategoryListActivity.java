package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.ShopCategoryBean;
import com.shareshenghuo.app.shop.network.request.ShopCategoryRequest;
import com.shareshenghuo.app.shop.network.response.AutResponse;
import com.shareshenghuo.app.shop.network.response.ShopCategoryResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.widget.DragListAdapter;
import com.shareshenghuo.app.shop.widget.DragListView;
import com.shareshenghuo.app.shop.widget.dialog.MyEditDialog1;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;
import com.shareshenghuo.app.shop.widget.dialog.onMyaddTextListener;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ShopCategoryListActivity extends BaseTopActivity{
	
    private DragListAdapter adapter = null;
    private   DragListView dragListView;
    private TextView tv1,tv2;
    public int pageNo = 1;
    private boolean tag = false;
    private MyEditDialog1 doubleWarnDialog1;
    private List<ShopCategoryBean> datas;
    private boolean isopenedit = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_category_activity);
		initd(false);
		initView();
	}

	public void initView() {

		initTopBar("分类管理");
		tv1 = getView(R.id.tv1);
		tv2 = getView(R.id.tv2);
//		btnTopRight1.setText("完成");
		dragListView = (DragListView)findViewById(R.id.other_drag_list);
		 datas = new ArrayList<ShopCategoryBean>();
        adapter = new DragListAdapter(this, datas);
        dragListView.setAdapter(adapter);
        tv1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showeditdailog();
			}
		});
        tv2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(tag){
					T.showShort(getApplicationContext(), "编辑完成");
					tv1.setVisibility(View.VISIBLE);
					tv2.setText("编辑分类");
					tag = false;
				}else{
					tv1.setVisibility(View.GONE);
					tv2.setText("完成");
					tag = true;
				}
				adapter.showEdit(tag);
			}
		});
        dragListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(!tag){
					Intent it = new Intent(ShopCategoryListActivity.this,IfiedManageActivity.class);
					it.putExtra("title",datas.get((int)arg3).type_name);
					it.putExtra("id", datas.get((int)arg3).id);
					startActivity(it);
				}else {
					if((int)arg3==0){
						Intent it = new Intent(ShopCategoryListActivity.this,IfiedManageActivity.class);
						it.putExtra("title",datas.get((int)arg3).type_name);
						it.putExtra("id", datas.get((int)arg3).id);
						startActivity(it);
					}
				}
			}
		});
	}
	
//    public void initData(){
//        //数据结果
//    	data = new ArrayList<String>();        
//        for(int i=0; i<24; i++){
//        	data.add("A选项"+i);
//        }
//    }
    
	public void loadData() {
		ProgressDialogUtil.showProgressDlg(this, "加载数据");
		ShopCategoryRequest req = new ShopCategoryRequest();
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.SHOPCATEGORY, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ProgressDialogUtil.dismissProgressDlg();
//				lvData.onRefreshComplete();
					T.showNetworkError(getApplicationContext());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				ProgressDialogUtil.dismissProgressDlg();
//				lvData.onRefreshComplete();
				ShopCategoryResponse bean = new Gson().fromJson(resp.result, ShopCategoryResponse.class);
					if(Api.SUCCEED == bean.result_code) {
						updateView(bean.data,false);
					} else {
						T.showShort(getApplicationContext(), bean.result_desc);
					}
			}
		});
	}
	
	
	 public void initd(final boolean b) {
		 
		 
			
			ShopCategoryRequest req = new ShopCategoryRequest();
			req.shopId = UserInfoManager.getUserInfo(this).shop_id+"";
			RequestParams params = new RequestParams();
			try {
				params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			new HttpUtils().send(HttpMethod.POST, Api.SHOPCATEGORY, params, new RequestCallBack<String>() {
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
							updateView(bean.data,b);
							
							for (int i = 0; i < bean.data.size(); i++) {
								bean.data.get(i).tag = i+"";
								Log.e("", ""+bean.data.get(i).id);
							}
							
						} else {
							T.showShort(getApplicationContext(), bean.result_desc);
						}
				}
			});
		}
	
	public void updateView(List<ShopCategoryBean> data, boolean b) {
		
		if(pageNo==1 || adapter==null) {
			this.datas.clear();
			this.datas.addAll(data);
			adapter.notifyDataSetChanged();
			if(b){
				dragListView.setSelection(adapter.getCount());  
			}
		}
		if(pageNo > 1) {
			this.datas.addAll(data);
			adapter.notifyDataSetChanged();
		}
		pageNo++;
	}
	
	public void showeditdailog(){
		doubleWarnDialog1 = new MyEditDialog1(ShopCategoryListActivity.this,
				R.style.CustomDialog, "新建分类名称", "", "确认", "取消", "",
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {

						switch (v.getId()) {
						case R.id.btn_right:
							doubleWarnDialog1.dismiss();
//							InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//							m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
							break;
						case R.id.btn_left:
							String paypwd = doubleWarnDialog1.getpaypwd();

							if (paypwd == null || paypwd.equals("")) {
								Toast.makeText(ShopCategoryListActivity.this,"不能为空",
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
							Toast.makeText(ShopCategoryListActivity.this.getApplicationContext(),"不能为空",
									Toast.LENGTH_SHORT).show();
//							ToastCustom.showMessage(
//									TransferAccountsActivity.this,
//									"请输入支付密码！");
							return;
						}
						InputMethodManager m=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
						
						if(judgetext(paypwd)){
							T.showShort(getApplicationContext(), "非法字符");
							return;
						}
						doubleWarnDialog1.dismiss();
						addshoptype(paypwd);
//						Toast.makeText(ShopCategoryListActivity.this.getApplicationContext(), paypwd, Toast.LENGTH_SHORT).show();
					}
				});

		doubleWarnDialog1.setCancelable(false);
		doubleWarnDialog1.setCanceledOnTouchOutside(false);
		doubleWarnDialog1.show();
		
	}
	
	
	private boolean judgetext(String text) {
		// TODO Auto-generated method stub
		
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？，。、；‘’,./;']"; 
        Pattern p = Pattern.compile(regEx); 
        Matcher m = p.matcher(text);                 
        if( m.find()){
        	return true;
//            Toast.makeText(CommodityInfoActivity.this, "不允许输入特殊符号！", Toast.LENGTH_LONG).show();
        }
		return m.find();
	}
	
	private void addshoptype(String name) {
		// TODO Auto-generated method stub
		ShopCategoryRequest req = new ShopCategoryRequest();
		req.shopId = UserInfoManager.getUserInfo(this).shop_id+"";
		req.name = name;
		RequestParams params = new RequestParams();
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.ADDSHOPCATEGORY, params, new RequestCallBack<String>() {
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
							initd(true);
							
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
