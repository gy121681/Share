package com.td.qianhai.fragmentmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.td.qianhai.epay.oem.FeedbackActivity;
import com.td.qianhai.epay.oem.HelpVersionActivity;
import com.td.qianhai.epay.oem.MyPromotionActivity;
import com.td.qianhai.epay.oem.OnlineWeb;
import com.td.qianhai.epay.oem.R;
import com.td.qianhai.epay.oem.ScreeningActivity;
import com.td.qianhai.epay.oem.UserActivity;
import com.td.qianhai.epay.oem.beans.AppContext;
import com.td.qianhai.epay.oem.beans.HttpUrls;
import com.td.qianhai.epay.oem.mail.utils.MyCacheUtil;
import com.td.qianhai.epay.oem.views.dialog.TwoButtonDialogStyle2;
import com.td.qianhai.epay.oem.views.dialog.interfaces.OnMyDialogClickListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

/**
 * tab页4（更多）
 * 帮助界面
 * @author Administrator
 * 
 */
public class TabCFm extends Fragment implements OnClickListener{
	/** 头部标题 */
	private TextView title_name ,tv_message,tv_dow,tv_og,tv_customer,tv_vqf,tv_feed,tv_qh,tv_qrcode,tv_seer;
	/** 视图 */
	private View view;
	
	private  UMSocialService mController;
	
	private String mobile;
	
	private Button go_out;
	
	private TwoButtonDialogStyle2 doubleWarnDialog;
	
	private Activity context;
	
	private Editor editor;
	
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.tab_c, null, false);
		context = this.getActivity();
		editor = MyCacheUtil.setshared(context);
//		mobile = AppContext.getInstance().getMobile();
		mobile = MyCacheUtil.getshared(getActivity()).getString("Mobile", "");
		  mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		  mController.getConfig().setPlatforms(SHARE_MEDIA.RENREN,SHARE_MEDIA.TENCENT,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA,SHARE_MEDIA.SMS);
		  
		initview();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// 相当于Fragment的onResume
			
		} else {
			// 相当于Fragment的onPause
		}
	}
	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//	    super.onActivityResult(requestCode, resultCode, data);
//	    /**使用SSO授权必须添加如下代码 */
//	    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
//	    if(ssoHandler != null){
//	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//	    }
//	}

	private void initview() {
		
//		tv_share = (TextView) view.findViewById(R.id.tv_fx);
		
		tv_message = (TextView) view.findViewById(R.id.tv_message);
		
		tv_seer = (TextView) view.findViewById(R.id.tv_seer);
		
		tv_qrcode = (TextView) view.findViewById(R.id.tv_qrcodes);
		
		tv_dow = (TextView) view.findViewById(R.id.tv_dow);
		
		tv_customer = (TextView) view.findViewById(R.id.tv_customer);
		
		tv_og = (TextView) view.findViewById(R.id.tv_og);
		
		tv_vqf = (TextView) view.findViewById(R.id.tv_vqf);
		
		tv_feed = (TextView) view.findViewById(R.id.tv_feed);
		
		tv_qh = (TextView) view.findViewById(R.id.tv_qh);
		
		go_out = (Button) view.findViewById(R.id.go_out);
		
		tv_seer.setOnClickListener(this);
		
		tv_og.setOnClickListener(this);
		
		go_out.setOnClickListener(this);
		
		tv_qh.setOnClickListener(this);
		
		tv_dow.setOnClickListener(this);
		
		tv_customer.setOnClickListener(this);
		
		tv_vqf.setOnClickListener(this);
		
		tv_feed.setOnClickListener(this);
		
		tv_qrcode.setOnClickListener(this);
		
		tv_message.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		Intent it = new Intent();
		switch (v.getId()) {
		
//		case R.id.tv_fx:
//			
//		 mController = UMServiceFactory.getUMSocialService("com.umeng.share");
//		 mController.setShareContent("来自  "+ mobile +" 赶快下载属于你的移动钱包！！"+"http://a.app.qq.com/o/simple.jsp?pkgname=com.td.qianhai.epay");
//			String appID = "wx504be3bb18c9dee4";
//			String appSecret = "289893ba8532e2968a9694ad45f601a1";
//			
//			
//			// 添加微信平台
//			UMWXHandler wxHandler = new UMWXHandler(getActivity(),appID,appSecret);
////			WeiXinShareContent weixinContent = new WeiXinShareContent();
////			weixinContent.setTargetUrl("http://180.166.124.95:8092/posm/upload/QH_W_V1.2.apk");
//			wxHandler.addToSocialSDK();
//			
//			// 添加微信朋友圈
//			UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(),appID,appSecret);
//			wxCircleHandler.setToCircle(true);
//			wxCircleHandler.addToSocialSDK();
//			//添加短信
//			SmsHandler smsHandler = new SmsHandler();
//			smsHandler.addToSocialSDK();
//
//			 UMImage aa = new UMImage(getActivity(), R.drawable.logo);
//			
//			WeiXinShareContent weixinContent = new WeiXinShareContent();
//			//设置分享文字
//			weixinContent.setShareContent("来自  "+ mobile +"  的分享");
//			//设置title
//			weixinContent.setTitle("钱海钱包");
//			//设置分享内容跳转URL
//			weixinContent.setTargetUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.td.qianhai.epay");
//			//设置分享图片m
//			weixinContent.setShareImage(aa);
//			
//			UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), "1104231525",
//	                "dX6nQwFwj93nC0t9");
//			qqSsoHandler.addToSocialSDK(); 
//			
//			//参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
//			QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(), "1104231525",
//			                "dX6nQwFwj93nC0t9");
//			qZoneSsoHandler.addToSocialSDK();
//			
//			QQShareContent qqShareContent = new QQShareContent();
//			//设置分享文字
//			qqShareContent.setShareContent("来自  "+ mobile +"  的分享");
//			//设置分享title
//			qqShareContent.setTitle("钱海钱包");
//			//设置分享图片
//			qqShareContent.setShareImage(aa);
//			//设置点击分享内容的跳转链接
//			qqShareContent.setTargetUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.td.qianhai.epay");
//			
//			
//			QZoneShareContent qzone = new QZoneShareContent();
//			//设置分享文字
//			qzone.setShareContent("来自  "+ mobile +"  的分享");
//			//设置点击消息的跳转URL
//			qzone.setTargetUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.td.qianhai.epay");
//			//设置分享内容的标题
//			qzone.setTitle("钱海钱包");
//			//设置分享图片
//			qzone.setShareImage(aa);
//			
//			mController.setShareMedia(qzone);
//			
//			mController.setShareMedia(qqShareContent);
//			
//			mController.setShareMedia(weixinContent);
//			
//			mController.openShare(getActivity(), false);
//			
//			break;
		case R.id.tv_message:
			Intent intent = new Intent();
			intent.setClass(getActivity(), OnlineWeb.class);
			intent.putExtra("urlStr",HttpUrls.COMMONPROBLEM);
			intent.putExtra("titleStr", "常见问题");
			startActivity(intent);
			
//			ToastCustom.showMessage(getActivity(), "亲 您遇到的问题还在收集中");
			break;
			
		case R.id.tv_dow:
			
			it.setClass(getActivity(), HelpVersionActivity.class);
			it.putExtra("mark", 7);
			startActivity(it);
			
			break;
			
		case R.id.tv_seer:
			it.setClass(getActivity(), ScreeningActivity.class);
			startActivity(it);
			break;
			
		case R.id.tv_qrcodes:
			it.setClass(getActivity(), MyPromotionActivity.class);
			startActivity(it);
			
			break;
			
			
		case R.id.tv_customer:
			it.setClass(getActivity(), HelpVersionActivity.class);
			it.putExtra("mark", 1);
			startActivity(it);
			break;
			
		case R.id.tv_vqf:
//			PictureActivitys
			it.setClass(getActivity(), OnlineWeb.class);
			it.putExtra("titleStr", "资质证明");
			it.putExtra("urlStr", HttpUrls.CERTIFICATE);
			startActivity(it);
			break;
			
		case R.id.tv_feed:
			it.setClass(getActivity(), FeedbackActivity.class);
			startActivity(it);
			break;
			
		case R.id.tv_qh:
			it.setClass(getActivity(), OnlineWeb.class);
			it.putExtra("titleStr", "简介");
			it.putExtra("urlStr", HttpUrls.CFINFO);
			startActivity(it);
//			it.setClass(getActivity(),IntroductionActivity.class);
//			it.putExtra("tag", "0");
//			startActivity(it);
			break;
			
		case R.id.go_out:
			
			SpannableString msps = new SpannableString("确定退出?");
			showDoubleWarnDialog(msps);
			break;
			
		case R.id.tv_og:
			it.setClass(getActivity(), OnlineWeb.class);
			it.putExtra("titleStr", "操作手册");
			it.putExtra("urlStr", HttpUrls.HELP);
			startActivity(it);
			break;
		default:
			break;
		}
		
	}
	
	/***
	 * 双按钮提示dialog
	 * 
	 * @param msg
	 */
	protected void showDoubleWarnDialog(SpannableString msg) {
		 doubleWarnDialog = new TwoButtonDialogStyle2(context,
				R.style.CustomDialog, "提示", msg, "退出帐号", "退出应用",
				new OnMyDialogClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						doubleWarnOnClick(v);
					}
				});
		doubleWarnDialog.setCancelable(true);
//		doubleWarnDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode,
//					KeyEvent event) {
//				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
//					return true;
//				} else {
//					return true; // 默认返回 false
//				}
//			}
//		});
		doubleWarnDialog.setCanceledOnTouchOutside(true);
		doubleWarnDialog.show();
	}
	
	protected void doubleWarnOnClick(View v) {

		switch (v.getId()) {
		case R.id.btn_left:
			doubleWarnDialog.dismiss();
			AppContext.getInstance().setCustId(null);
			AppContext.getInstance().setPsamId(null);
			AppContext.getInstance().setMacKey(null);
			AppContext.getInstance().setPinKey(null);
			AppContext.getInstance().setMerSts(null);
			AppContext.getInstance().setMobile(null);
			AppContext.getInstance().setEncryPtkey(null);
			AppContext.getInstance().setStatus(null);
			AppContext.getInstance().setCustPass(null);
			AppContext.getInstance().setVersionSerial(null);
			AppContext.getInstance().setStateaudit(null);
			editor.putString("MERSTS","");
			editor.putString("AGENTID","");
			editor.putString("PERSONPIC","");
			editor.commit();
			AppContext.getInstance().exit();
		break;
		case R.id.btn_right:
			AppContext.getInstance().setCustId(null);
			AppContext.getInstance().setPsamId(null);
			AppContext.getInstance().setMacKey(null);
			AppContext.getInstance().setPinKey(null);
			AppContext.getInstance().setMerSts(null);
			AppContext.getInstance().setMobile(null);
			AppContext.getInstance().setUsername(null);
			AppContext.getInstance().setEncryPtkey(null);
			AppContext.getInstance().setStatus(null);
			AppContext.getInstance().setCustPass(null);
			AppContext.getInstance().setVersionSerial(null);
			AppContext.getInstance().setStateaudit(null);
			editor.putString("MERSTS","");
			editor.putString("AGENTID","");
			editor.putString("PERSONPIC","");
			editor.putString("isopem", "-1");
			editor.commit();
			doubleWarnDialog.dismiss();
			AppContext.getInstance().exit();
			
			Intent it = new Intent(context,UserActivity.class);
			
			startActivity(it);
			break;
		default:
			break;
		}
	}
	

}
