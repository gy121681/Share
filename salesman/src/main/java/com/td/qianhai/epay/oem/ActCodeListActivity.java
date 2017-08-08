package com.td.qianhai.epay.oem;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.td.qianhai.epay.oem.adapter.ActCodeAdapter;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class ActCodeListActivity extends BaseActivity implements OnScrollListener {

	private String phone,agtid;
	
	private View moreView;
	private int page = 1; // 页数
	private int allPageNum = 0; // 总页数
	private int PAGE_SIZE = 10;
	private boolean isThreadRun = false; // 加载数据线程运行状态
	private ListView listView;
	private LayoutInflater inflater;
	private ArrayList<HashMap<String, Object>> mList;
	private TextView null_datas;
	private int lastItem;
	private ActCodeAdapter adapter;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_actcode_list);
		
//		phone = ((AppContext)getApplication()).getMobile();
		phone = MyCacheUtil.getshared(this).getString("Mobile", "");
		agtid = MyCacheUtil.getshared(this).getString("AGENTID", "");
		
		
		initview();

//		showLoadingDialog("正在查询中...");
//		new Thread(run).start();

	}
	
	private void initview() {
		((TextView) findViewById(R.id.tv_title_contre)).setText("已生成的激活码");
		findViewById(R.id.bt_title_left).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
		
		inflater = LayoutInflater.from(this);
		moreView = inflater.inflate(R.layout.load, null);
		mList = new ArrayList<HashMap<String,Object>>();
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
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				// TODO Auto-generated method stub
				String num = mList.get((int)id).get("ACTCOD").toString();
				UMSocialService  mController = UMServiceFactory.getUMSocialService("com.umeng.share");
				mController.getConfig().setPlatforms(SHARE_MEDIA.QQ,
						SHARE_MEDIA.WEIXIN,SHARE_MEDIA.SMS);

				mController = UMServiceFactory.getUMSocialService("com.umeng.share");
				mController
				.setShareContent("秀儿支付激活码"+ "  "+num);
				String appID = "wx67b3a23ca6de80f8";
				String appSecret = "1a35ae5ac3d57848c743a5655d11cb4b";
				
				SmsHandler smsHandler = new SmsHandler();
				smsHandler.addToSocialSDK();
				
				UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(ActCodeListActivity.this,
						"1104744982", "MCXWLlnpU2S4ULld");
				qqSsoHandler.addToSocialSDK();
				
				// 添加微信平台
				UMWXHandler wxHandler = new UMWXHandler(ActCodeListActivity.this, appID, appSecret);
				// WeiXinShareContent weixinContent = new WeiXinShareContent();
				// weixinContent.setTargetUrl("http://180.166.124.95:8092/posm/upload/QH_W_V1.2.apk");
				wxHandler.addToSocialSDK();
				
				UMImage aa = new UMImage(ActCodeListActivity.this, R.drawable.ico);

				WeiXinShareContent weixinContent = new WeiXinShareContent();
				// 设置分享文字
				weixinContent.setShareContent("费率激活码分享");
				// 设置title
				weixinContent.setTitle("秀儿支付激活码:  "+num);
				// 设置分享内容跳转URL
				weixinContent
						.setTargetUrl("http://mp.weixin.qq.com/s?__biz=MzAwNTI1NjgzMg==&mid=207759636&idx=1&sn=4c9374f6f65f1e4a41ea7614fce895aa#rd");
				// 设置分享图片m
				weixinContent.setShareImage(aa);
				
				QQShareContent qqShareContent = new QQShareContent();
				// 设置分享文字
				qqShareContent.setShareContent("费率激活码分享");
				// 设置分享title
				qqShareContent.setTitle("秀儿支付激活码:  "+num);
				// 设置分享图片
				qqShareContent.setShareImage(aa);
				// 设置点击分享内容的跳转链接
				qqShareContent
						.setTargetUrl("http://mp.weixin.qq.com/s?__biz=MzAwNTI1NjgzMg==&mid=207759636&idx=1&sn=4c9374f6f65f1e4a41ea7614fce895aa#rd");
				mController.setShareMedia(qqShareContent);

				mController.setShareMedia(weixinContent);

				mController.openShare(ActCodeListActivity.this, false);
				
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
//			new Thread(run).start();
		}

	}



	Runnable run = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			// ArrayList<HashMap<String, Object>> list2 = new
			// ArrayList<HashMap<String, Object>>();
			String[] values = { HttpUrls.ACTCODELIST + "", phone, agtid, page+"", PAGE_SIZE+"" };
			ActCodeBean entitys = NetCommunicate.ActcodelistBill(
					HttpUrls.ACTCODELIST, values);
			Message msg = new Message();
			if (entitys != null) {
				loadingDialogWhole.dismiss();
				list = entitys.list;
				if (list != null && list.size() != 0) {
					mList.addAll(list);
					msg.what = 1;
				}else{
					msg.what = 2;
//					ToastCustom.showMessage(AgentListActivity.this, "没有更多记录了");
				}
				
				if(entitys.getTolcnt()!=null&&!entitys.getTolcnt().equals("")&&!entitys.getTolcnt().equals("null")){
					
					int allNum = Integer.parseInt(entitys.getTolcnt());

					if (allNum % PAGE_SIZE != 0) {
						allPageNum = allNum / PAGE_SIZE + 1;
					} else {
						allPageNum = allNum / PAGE_SIZE;
					}
				}else{
					int allNum = 0;

					if (allNum % PAGE_SIZE != 0) {
						allPageNum = allNum / PAGE_SIZE + 1;
					} else {
						allPageNum = allNum / PAGE_SIZE;
					}
				}

				
//				if(mList.size()<=0||mList==null){
//					
//					msg.what = 2;
//					ToastCustom.showMessage(AgentListActivity.this, "没有更多记录了");
//				}else{
//					msg.what = 1;
//				}

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
				if(adapter==null){
					adapter = new ActCodeAdapter(ActCodeListActivity.this, mList);
					listView.setAdapter(adapter);
				}

				null_datas.setVisibility(View.GONE);
				
				moreView.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
				break;
			case 2:
				if (moreView != null) {
//					listView.setVisibility(View.GONE);
					moreView.setVisibility(View.GONE);
				}
				Toast.makeText(getApplicationContext(), "没有更多记录了",
						Toast.LENGTH_SHORT).show();
//				ToastCustom.showMessage(ActCodeListActivity.this, "没有更多记录了");
//				null_datas.setVisibility(View.VISIBLE);
				break;
			case 3:
				null_datas.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
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
	
	

}
