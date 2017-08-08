package com.shareshenghuo.app.shop;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.entity.StringEntity;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.umeng.analytics.MobclickAgent;
import com.shareshenghuo.app.shop.fragment.DobusinessFragment;
import com.shareshenghuo.app.shop.fragment.HomeFragment;
import com.shareshenghuo.app.shop.fragment.HomeMainFragment;
import com.shareshenghuo.app.shop.fragment.MemberManageFragment;
import com.shareshenghuo.app.shop.fragment.MineFragment;
import com.shareshenghuo.app.shop.fragment.OrderFragment;
import com.shareshenghuo.app.shop.network.bean.NoticeBean;
import com.shareshenghuo.app.shop.network.request.NumRequest;
import com.shareshenghuo.app.shop.network.response.NoticeResponse;
import com.shareshenghuo.app.shop.networkapi.Api;
import com.shareshenghuo.app.shop.util.ProgressDialogUtil;
import com.shareshenghuo.app.shop.util.T;
import com.shareshenghuo.app.shop.widget.SystemBarTintManager;
import com.shareshenghuo.app.shop.widget.dialog.ButtonDialogStyleNotice;
import com.shareshenghuo.app.shop.widget.dialog.OnMyDialogClickListener;


public class MainActivity extends FragmentActivity {
	
	public FragmentTabHost fTabHost;
//	private String[] tabTags = {"tab_home", "tab_order", "tab_member", "tab_mine"};
	private String[] tabTags = {"tab_home", "tab_order", "tab_mine"};
//	private Class[] tabFragments = {HomeFragment.class, OrderFragment.class, MemberManageFragment.class, MineFragment.class};
	private Class[] tabFragments = {HomeMainFragment.class, DobusinessFragment.class, MineFragment.class};
//	private String[] tabTitles = {"首页", "订单管理", "会员管理", "我的"};
	private String[] tabTitles = {"首页", "营业额", "我的"};
//	private int[] tabIcons = {R.drawable.tab_home, R.drawable.tab_order, R.drawable.tab_member, R.drawable.tab_mine};
	private int[] tabIcons = {R.drawable.tab_home, R.drawable.tab_order, R.drawable.tab_mine};
	private ButtonDialogStyleNotice noticedialog;
	public static MainActivity context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initTabHost();
        context = this;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.bg_white);
		
//		getnotice();
        
    }
    
	@TargetApi(19) 
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		  MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		MobclickAgent.onResume(this);
	}
    
    public void initTabHost() {
    	fTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
    	fTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
    	
    	for(int i=0; i<tabFragments.length; i++) {
			TabSpec spec = fTabHost.newTabSpec(tabTags[i]);
			spec.setIndicator(getTabView(i));
			fTabHost.addTab(spec, tabFragments[i], null);
		}
    	
    	fTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }
    
    public View getTabView(int i) {
		View view = LayoutInflater.from(this).inflate(R.layout.view_tab_main, null);
		ImageView ivIcon = (ImageView) view.findViewById(R.id.ivMainTabIcon);
		TextView tvTitle = (TextView) view.findViewById(R.id.tvMainTabTitle);
		ivIcon.setImageResource(tabIcons[i]);
		tvTitle.setText(tabTitles[i]);
		return view;
	}
    
    @Override
	public void onBackPressed() {
		exitBy2Click();
	}
	
	private static boolean isExit = false;
	
	private void exitBy2Click() {
		if(!isExit) {
			isExit = true;
			T.showShort(this, "再按一次退出程序");
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 1500);
		} else {
			finish();
		}
	}
	
	
	
	
}
