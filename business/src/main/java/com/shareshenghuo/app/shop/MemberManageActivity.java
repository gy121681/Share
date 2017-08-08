package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.entity.StringEntity;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.shareshenghuo.app.shop.R;
import com.shareshenghuo.app.shop.adapter.VipManageAdapter;
import com.shareshenghuo.app.shop.fragment.BaseFragment;
import com.shareshenghuo.app.shop.manager.MessageManager;
import com.shareshenghuo.app.shop.manager.UserInfoManager;
import com.shareshenghuo.app.shop.network.bean.NewMemberbean;
import com.shareshenghuo.app.shop.network.bean.VipInfo;
import com.shareshenghuo.app.shop.network.request.IntegralRquest;
import com.shareshenghuo.app.shop.network.request.NewMemberResqust;
import com.shareshenghuo.app.shop.network.request.VipListRequest;
import com.shareshenghuo.app.shop.network.response.IntegralResponse;
import com.shareshenghuo.app.shop.network.response.NewMemberResponse;
import com.shareshenghuo.app.shop.network.response.VipListResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker;
import com.shareshenghuo.app.shop.receiver.NewChatMsgWorker.NewMessageCallback;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.util.ViewUtil;
import com.shareshenghuo.app.shop.widget.dialog.QRCodeWindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.AdapterView.OnItemClickListener;

public class MemberManageActivity extends BaseTopActivity
	implements OnItemClickListener, OnRefreshListener2<ListView>, OnClickListener, NewMessageCallback{
	
	private static final int REQ_VIP_DETAIL = 0x100;
	
//	private ImageView ivIM;
	private EditText edKeyword;
	private ImageView ivSearch;
	private PullToRefreshListView lvData;
	
	private VipManageAdapter adapter;
	private int pageNo = 1;
	private int pageSize = 30;
	private RadioButton btn_1,btn_2;
	private NewChatMsgWorker newMsgWatcher;
	private String tag = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_member_manage);
		init();
	}
	

	protected void init() {
		
		initTopBar("会员管理");
		btn_1 = getView(R.id.btn_1);
		btn_1.setChecked(true);
		btn_2 = getView(R.id.btn_2);
		
		
		pageNo = 1;
		
		initView();
		loadData(tag);
		
		newMsgWatcher = new NewChatMsgWorker(this, this);
		newMsgWatcher.startWork();
		
		btn_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tag = "";
				pageNo =1;
				loadData(tag);
			}
		});
		btn_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				tag = "shopPay";
				
				pageNo =1;
				loadData(tag);
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
//		ivIM.setBackgroundResource(MessageManager.getUnreadCount() > 0 ? 
//				R.drawable.ic_im_notice : R.drawable.ic_im);
	}
	
	public void initView() {
//		ivIM = getView(R.id.ivHomeIM);
		edKeyword = getView(R.id.edSearch);
		edKeyword.setHint("请输入会员手机号/姓名/卡号");
		ivSearch = getView(R.id.ivSearch);
		lvData = getView(R.id.lvData);
		
		ViewUtil.setEditWithClearButton(edKeyword, R.drawable.img_cancel);
		lvData.setMode(Mode.BOTH);

//		ivIM.setOnClickListener(this);
		ivSearch.setOnClickListener(this);
		lvData.setOnRefreshListener(this);
		lvData.setOnItemClickListener(this);
	}
	
	public void loadData() {
		VipListRequest req = new VipListRequest();
		req.shop_id = UserInfoManager.getUserInfo(this).shop_id+"";
		req.page_no = pageNo+"";
		req.page_size = pageSize+"";
		String keyword = edKeyword.getText().toString();
		if(!TextUtils.isEmpty(keyword))
			req.search_name = keyword;
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.URL_VIP_LIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
				T.showNetworkError(getApplicationContext());
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				
				VipListResponse bean = new Gson().fromJson(resp.result, VipListResponse.class);
				if(Api.SUCCEED == bean.result_code){};
//					updateView(bean.data);
			}
		});
	}
	
	public void loadData(String tag) {
		
		NewMemberResqust req = new NewMemberResqust();
		req.userId = UserInfoManager.getUserInfo(this).shop_id+"";
		req.userType = "2";
		req.qrytype = tag;
		String keyword = edKeyword.getText().toString();
		if(!TextUtils.isEmpty(keyword))
			req.searchName = keyword;
		else
		req.searchName = "";
		req.pageNo = pageNo+"";
		req.pageSize = pageSize+"";
		
		RequestParams params = new RequestParams("utf-8");
		try {
			params.setBodyEntity(new StringEntity(req.toJson(),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		new HttpUtils().send(HttpMethod.POST, Api.NEWMEMBERELIST, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				lvData.onRefreshComplete();
			}

			@Override
			public void onSuccess(ResponseInfo<String> resp) {
				lvData.onRefreshComplete();
				NewMemberResponse bean = new Gson().fromJson(resp.result, NewMemberResponse.class);
				if(Api.SUCCEED == bean.result_code)
					updateView(bean.data);
			}
		});
		
		
	}
	
	public void updateView(List<NewMemberbean> data) {
		if(pageNo==1 || adapter==null) {
			adapter = new VipManageAdapter(this, data);
			lvData.setAdapter(adapter);
		}
		if(pageNo > 1) {
			if(adapter!=null&&data!=null){
				adapter.getmData().addAll(data);
				adapter.notifyDataSetChanged();
			}
		}
		pageNo++;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long id) {
//		VipInfo item = (VipInfo) adapterView.getItemAtPosition(position);
//		Intent it = new Intent(this, VipDetailActivity.class);
//		it.putExtra("vipInfo", item);
//		startActivityForResult(it, REQ_VIP_DETAIL);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageNo = 1;
		loadData(tag);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData(tag);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
//		case R.id.ivHomeIM:
//			ivIM.setBackgroundResource(R.drawable.ic_im);
//			startActivity(new Intent(this, MessageActivity.class));
//			break;
			
		case R.id.ivSearch:
			if(ViewUtil.checkEditEmpty(edKeyword, "请输入关键字"))
				return;
			onPullDownToRefresh(lvData);
			break;
		}
	}

	@Override
	public void onActivityResult(int reqCode, int resCode, Intent data) {
		if(resCode==Activity.RESULT_OK && reqCode==REQ_VIP_DETAIL) {
			onPullDownToRefresh(lvData);
		}
	}
	
	@Override
	public void newMessage(int which) {
//		ivIM.setBackgroundResource(R.drawable.ic_im_notice);		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//		closeKeybord(this);
	}
//	
//    /**
//     * 关闭软键盘
//     * 
//     * @param mEditText输入框
//     * @param mContext上下文
//     */
//    public void closeKeybord( Context mContext)
//    {
//    	InputMethodManager m=(InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
//    	m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//    }
}
