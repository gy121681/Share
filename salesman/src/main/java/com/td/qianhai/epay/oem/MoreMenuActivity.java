package com.td.qianhai.epay.oem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import com.share.app.entity.response.Constans;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpKeys;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.interfaces.JavaScriptinterface;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.net.NetCommunicate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.td.qianhai.epay.oem.beans.HttpUrls.MOREMENUACTIVITY_HELP2;

public class MoreMenuActivity extends BaseActivity{
	
	private LinearLayout lin;
	private GridView gridview;
	private String tag,tagsts,curl,mobile;
	private MyAdapter adapter;
	
	private WebView wb_epos;
	
	private HashMap<String, Object> result = null; 
	private HashMap<String, Object> result1 = null; 
	
	
	private String URLs;
	String[] URLs1;

	String[] name = {"店主代付","微信支付", "固定二维码","我的账户"};
	String[] name1 = {"商户","营业额"};
//	String[] name2 = {"会员升级","费率收益", "总收益管理","我的推广","下属查询","激活码管理","广告发布"};
	String[] name2 = {"下属查询","激活码管理"};

	int[] img = {R.drawable.dzdf_img,R.drawable.wxzf_img,R.drawable.gdewm_img,R.drawable.wdzh_img};
	int[] img1 = {R.drawable.share_s_homepage_my_seller_icon_shop,R.drawable.share_s_homepage_my_seller_icon_business};
	int[] img2 = {R.drawable.share_s_homepage_member_subordinate,R.drawable.share_s_homepage_member_activation_code};
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_menu_activity);
		AppContext.getInstance().addActivity(this);
		tagsts = MyCacheUtil.getshared(this).getString("STS", "");
		curl = MyCacheUtil.getshared(this).getString("CURROL", "");
		tag = getIntent().getStringExtra("tag");
		mobile = MyCacheUtil.getshared(this).getString("Mobile", "");
		initview();
		
		load();
	}
	
	private void load() {
		// TODO Auto-generated method stub
		showLoadingDialog("请稍候...");
		new Thread(run).start();
		
	}

	@SuppressLint("NewApi")
	private void initview() {

		TextView mTitle = (TextView) findViewById(R.id.tv_title_contre);
		findViewById(R.id.bt_title_left).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		gridview = (GridView) findViewById(R.id.gridview);
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		if(tag.equals("0")){
			initlayout();
		}else if(tag.equals("1")){
			mTitle.setText("我的商户");
			initlayout1();
		}else if(tag.equals("2")){
			mTitle.setText("会员管理");
			initlayout2();
		}
//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		
//		lp.weight = 1;
//		LinearLayout layout = new LinearLayout(this);
//		layout.setLayoutParams(lp);
//		layout.setOrientation(LinearLayout.VERTICAL);
//		layout.setBackground(getResources().getDrawable(R.drawable.layout_btn));
//		
//		
//		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		lp1.setMargins(10, 10, 10, 0);
//		lp1.weight = 1;
//		ImageView img = new ImageView(this);
//		img.setLayoutParams(lp1);
//		img.setImageResource(R.drawable.etc_img);
//		
//		TextView tv = new TextView(this);
//		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		lp2.weight = 2;
//		tv.setLayoutParams(lp2);
//		tv.setGravity(Gravity.CENTER);
//		tv.setTextSize(14);
//		tv.setText("收款");
//		layout.addView(img);
//		layout.addView(tv);
//		
//		LinearLayout layout2 = new LinearLayout(this);
//		layout2.setLayoutParams(lp);
//		layout2.setBackground(getResources().getDrawable(R.drawable.layout_btn));
//		
//		LinearLayout layout3 = new LinearLayout(this);
//		layout3.setLayoutParams(lp);
//		layout3.setBackground(getResources().getDrawable(R.drawable.layout_btn));
//		
//		LinearLayout layout4 = new LinearLayout(this);
//		layout4.setLayoutParams(lp);
//		layout4.setBackground(getResources().getDrawable(R.drawable.layout_btn));
//		
//		lin.addView(layout);
//		lin.addView(layout2);
//		lin.addView(layout3);
//		lin.addView(layout4);
	}

	private void initlayout2() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		adapter = new MyAdapter(this, name2,img2);
		gridview.setAdapter(adapter);
		
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				switch ((int)id) {
				case 0:
					//下属查询
					intent.setClass(MoreMenuActivity.this, MyCircleActivity1.class);
					intent.putExtra("tag", "1");
					startActivity(intent);

//					if(tagsts.equals("3")||tagsts.equals("0")||tagsts.equals("4")){
//							intent.setClass(MoreMenuActivity.this, HaiGouAvtivity.class);
//							intent.putExtra("url",HttpUrls.NEWVIP);
//							intent.putExtra("title","会员升级");
//							intent.putExtra("tag","0");
//							startActivity(intent);
//					}else{
//							Intent it = new Intent(MoreMenuActivity.this,AuthenticationActivity.class);
//							startActivity(it);
//							Toast.makeText(getApplicationContext(), "请先实名认证", Toast.LENGTH_SHORT).show();
//							return;
//
//					}

//					intent.setClass(MoreMenuActivity.this, DistributorActivity.class);
//					startActivity(intent);
					break;
				case 1:
					//激活码管理
					intent.setClass(MoreMenuActivity.this, ActivationCodeManageActivity.class);
					startActivity(intent);

//					if(tagsts.equals("3")||tagsts.equals("0")||tagsts.equals("4")){
//						if(curl.equals("1")||curl.equals("2")){
//							intent.setClass(MoreMenuActivity.this, AgentManageActivity.class);
//							startActivity(intent);
//						}else{
//							Toast.makeText(getApplicationContext(), "请先升级会员", Toast.LENGTH_SHORT).show();
//							return;
//						}
//					}else{
//							Intent it = new Intent(MoreMenuActivity.this,AuthenticationActivity.class);
//							startActivity(it);
//							Toast.makeText(getApplicationContext(), "请先实名认证", Toast.LENGTH_SHORT).show();
//							return;
//						
//					}
					
					break;
				case 2:
					intent.setClass(MoreMenuActivity.this, MyProfitActivitys.class);
					startActivity(intent);
					break;
				case 3:
					intent.setClass(MoreMenuActivity.this, MyCircleActivity.class);
					startActivity(intent);
					break;
//				case 4:
//					intent.setClass(MoreMenuActivity.this, OnlineWeb.class);
//					intent.putExtra("titleStr", "资质证明");
//					intent.putExtra("urlStr",help3);
//					startActivity(intent);
//					break;
				case 4:
					intent.setClass(MoreMenuActivity.this, MyCircleActivity1.class);
					intent.putExtra("tag", "1");
					startActivity(intent);

					break;
				case 5:
					intent.setClass(MoreMenuActivity.this, ActivationCodeManageActivity.class);
					startActivity(intent);
					break;
				case 6:
					intent.setClass(MoreMenuActivity.this, HaiGouAvtivity.class);
					intent.putExtra("url",HttpUrls.TOAVERTISING);
					intent.putExtra("title","广告发布");
					intent.putExtra("tag","0");
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		});
	}

	private void initlayout1() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		adapter = new MyAdapter(this, name1,img1);
		gridview.setAdapter(adapter);
		
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				switch ((int)id) {
				case 0:
					intent.setClass(MoreMenuActivity.this, OnlineWeb.class);
					intent.putExtra("titleStr", "商户");
					intent.putExtra("urlStr", HttpUrls.SHARE_WEB_TENANT + MyCacheUtil.getshared(MoreMenuActivity.this).getString(Constans.Login.USERID, ""));
					startActivity(intent);

//					intent.setClass(MoreMenuActivity.this, ScreeningActivity.class);
//					startActivity(intent);
					break;
				case 1:
					intent.setClass(MoreMenuActivity.this, OnlineWeb.class);
					intent.putExtra("titleStr", "营业额");
					intent.putExtra("urlStr", HttpUrls.SHARE_WEB_COMMERICIAL + MyCacheUtil.getshared(MoreMenuActivity.this).getString(Constans.Login.USERID, ""));
					startActivity(intent);

//					String subMchId = "";
////					if(result.get("subMchId")!=null){
////						 subMchId = result.get("subMchId").toString();
////					}
//					if(result.get("is_subbranch")!=null){
//						 subMchId = result.get("is_subbranch").toString();
//					}
//					intent.putExtra("subMchId", subMchId);
//					Log.e("", " - - - "+subMchId);
//					intent.setClass(MoreMenuActivity.this, WechatCollectionRecordActivity.class);//
//					startActivity(intent);
//					Toast.makeText(getApplicationContext(), "暂未开通", Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(getApplicationContext(), "暂未开通", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					intent.setClass(MoreMenuActivity.this, OnlineWeb.class);
					intent.putExtra("titleStr", "资质证明");
					intent.putExtra("urlStr", MOREMENUACTIVITY_HELP2);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		});
	}

	private void initlayout() {
		// TODO Auto-generated method stub
		adapter = new MyAdapter(this, name,img);
		gridview.setAdapter(adapter);
		
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				switch ((int)id) {
				case 0:
					intent.setClass(MoreMenuActivity.this,ShouKuanBAvtivity1.class);
					intent.putExtra("tag", "0");
					intent.putExtra("value", "1");
					startActivity(intent);
					break;
				case 1:
					intent.setClass(MoreMenuActivity.this,ShouKuanBAvtivity1.class);
					intent.putExtra("tag", "0");
					intent.putExtra("value", "2");
					startActivity(intent);
//					intent.setClass(MoreMenuActivity.this, ShouKuanBAvtivity.class);
//					intent.putExtra("tag", "0");
//					startActivity(intent);
					break;
				case 2:
					intent.setClass(MoreMenuActivity.this, PersonalCollectionActivity.class);
					startActivity(intent);
					break;
				case 3:
					intent.setClass(MoreMenuActivity.this,ShouKuanBAvtivity1.class);
					intent.putExtra("tag", "0");
					intent.putExtra("value", "3");
					startActivity(intent);
//					Toast.makeText(getApplicationContext(), "暂未开通", Toast.LENGTH_SHORT).show();
					break;
				case 4:
					intent.setClass(MoreMenuActivity.this, OnlineWeb.class);
					intent.putExtra("titleStr", "资质证明");
					intent.putExtra("urlStr", HttpUrls.MOREMENUACTIVITY_HELP1);
					startActivity(intent);
					break;
				default:
					break;
				}
			}
		});
		
	}
	
	public class MyAdapter extends BaseAdapter {

		private Context mContext;
		private LayoutInflater inflater;
		private String[] dataList;
		private int[] imgs;

		// private class GirdTemp{
		// ImageView phone_function_pic;
		// TextView phone_function_name;
		// }
		public MyAdapter(Context c,String[] dataList,int[] img) {
			this.dataList = dataList;
			mContext = c;
			this.imgs = img;
			inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return dataList.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.moremenu_item,
						null);
				viewHolder = new ViewHolder();

//				viewHolder.tv_wallet = (TextView) convertView
//						.findViewById(R.id.tv_wallet);

				viewHolder.title = (TextView) convertView
						.findViewById(R.id.tv_wallet_p);
				viewHolder.img_m = (ImageView) convertView
						.findViewById(R.id.img_m);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			String maps = dataList[position];
			viewHolder.title.setText(maps);
			viewHolder.img_m.setImageResource(imgs[position]);
			return convertView;
		}

		class ViewHolder {
			public TextView title;
//			public TextView tv_wallet;
			private ImageView img_m;
		}

	}
	
	Runnable run = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = null;
			
			try {
				
				String[] values = {mobile};
				result = NetCommunicate.executeHttpPostnulls(HttpUrls.SCREENINGURL,
						HttpKeys.SCREENINGURL_BACK,HttpKeys.SCREENINGURL_ASK,values);
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
			if(result!=null){
				if(result.get("flag")!=null&&result.get("flag").toString().equals("00")){
					msg.what = 1;
				}else if(result.get("flag")!=null&&result.get("flag").toString().equals("03")){
					msg.what = 2;
				}else{
					msg.what = 5;
				}
			}else{
				msg.what = 5;
			}
			loadingDialogWhole.dismiss();
			handler.sendMessage(msg);
			
		}
	};
	
	private Handler handler = new Handler() {
	public void handleMessage(Message msg) {
		new Thread(run1).start();
		switch (msg.what) {
		case 1:
				
			
			break;
		default:
			break;
		}
	};
	};
	
	Runnable run1 = new Runnable() {

		@Override
		public void run() {
			ArrayList<HashMap<String, Object>> list = null;
			 
			try {
				
				String[] values = {"4",mobile};
				 result1 = NetCommunicate.executeHttpPostnulls(HttpUrls.SKBDIRECTLOGIN,
						HttpKeys.WECHATRECEIVE1_BACK,HttpKeys.WECHATRECEIVE_ASK,values);
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
			if(result1!=null){
				if(result1.get("RSPCOD")!=null&&result1.get("RSPCOD").toString().equals("000000")){
					msg.what = 0;
				}else if(result1.get("RSPCOD")!=null&&result1.get("RSPCOD").toString().equals("100001")){
					msg.what = 1;
				}else if(result1.get("RSPCOD")!=null&&result1.get("RSPCOD").toString().equals("100002")){
					msg.what = 2;
				}else if(result1.get("RSPCOD")!=null&&result1.get("RSPCOD").toString().equals("100003")){
					msg.what = 3;
				}else if(result1.get("RSPCOD")!=null&&result1.get("RSPCOD").toString().equals("100004")){
					msg.what = 4;
				}else if(result1.get("RSPCOD")!=null&&result1.get("RSPCOD").toString().equals("100005")){
					msg.what = 5;
				}else if(result1.get("RSPCOD")!=null&&result1.get("RSPCOD").toString().equals("100006")){
					msg.what = 6;
				}
			}else{
				msg.what = -1;
			}
			Log.e("", " - - - "+	msg.what);
			handler1.sendMessage(msg);
			
		}
	};
	
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			Log.e("", " - - - "+	msg.what);
			switch (msg.what) {
			case 0:
				if(result1.get("login_url")!=null){
				URLs = result1.get("login_url").toString();
				  URLs1 =  URLs.split("\\|", 3);
//				  for (int i = 0; i < URLs1.length; i++) {
//				}
//				  Log.e("", "URLs1 = = = "+URLs1[0]);
//				  URLs2 =  URLs.split("|", 1);
//				  Log.e("", "URLs2 = = = "+URLs2[1]);
//				  URLs3 =  URLs.split("|", 2);
//				  Log.e("", "URLs3 = = = "+URLs3[2]);
				}
				
				if(URLs1!=null&&URLs1[0]!=null&&!URLs1[0].equals("")){
					webview(URLs1[0]);
				}
				
				
				break;
			default:
				break;
			}
		};
	};
	
	@SuppressLint("JavascriptInterface") private void webview(String a) {
		wb_epos = (WebView) findViewById(R.id.wv_epos);
		wb_epos.getSettings().setSupportMultipleWindows(true);
		wb_epos.getSettings().setJavaScriptEnabled(true);
		wb_epos.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		wb_epos.loadUrl(a);
		
//		wb_epos.getSettings().setSupportZoom(true);
//		wb_epos.getSettings().setJavaScriptEnabled(true);
//		wb_epos.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//		wb_epos.getSettings().setBuiltInZoomControls(true);//support zoom
//		wb_epos.getSettings().setUseWideViewPort(true);// 这个很关键
//		wb_epos.getSettings().setLoadWithOverviewMode(true);
		wb_epos.addJavascriptInterface(new JavaScriptinterface(this),
				"android");
		// 监听有需要再修改
		OnlineWebViewClient viewClient = new OnlineWebViewClient();
		wb_epos.setWebViewClient(viewClient);
		
	}
	
	public class OnlineWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			// TODO Auto-generated method stub
			handler.proceed();
		}
		
	}
	
	private void initdata(){
		
	}

}
