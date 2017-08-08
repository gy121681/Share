package com.shareshenghuo.app.user.util;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.shareshenghuo.app.user.R;

/**
 * Created by hang on 2015/10/22.
 */
public class ShareHelper {

    public static void shareWechat(Context context, String title, String content, String url, String thumbnail) {
        ShareSDK.initSDK(context);
        Wechat.ShareParams wx = new Wechat.ShareParams();
        wx.setShareType(Platform.SHARE_WEBPAGE);
        wx.setTitle(title);
        wx.setText(content);
        if(TextUtils.isEmpty(thumbnail))
        	wx.setImageData(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        else
        	wx.setImageUrl(thumbnail);
        wx.setUrl(url);
        ShareSDK.getPlatform(Wechat.NAME).share(wx);
    }

    public static void shareWechatMoment(Context context, String title, String content, String url, String thumbnail) {
        ShareSDK.initSDK(context);
        WechatMoments.ShareParams moments = new WechatMoments.ShareParams();
        moments.setShareType(Platform.SHARE_WEBPAGE);
        moments.setTitle(title);
        moments.setText(content);
        if(TextUtils.isEmpty(thumbnail))
        	moments.setImageData(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        else
        	moments.setImageUrl(thumbnail);
        moments.setUrl(url);
        ShareSDK.getPlatform(WechatMoments.NAME).share(moments);
    }
    
    public static void shareQQ(Context context, String title, String content, String url, String thumbnail) {
    	ShareSDK.initSDK(context);
    	QQ.ShareParams qq = new QQ.ShareParams();
		qq.setText(content);
		qq.setTitle(title);
		qq.setTitleUrl(url);
		if(TextUtils.isEmpty(thumbnail))
			qq.setImageData(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
		else
			qq.setImageUrl(thumbnail);
		ShareSDK.getPlatform(QQ.NAME).share(qq);
    }
    
    public static void shareQZone(final Context context, String title, String content, String url, String thumbnail) {
    	ShareSDK.initSDK(context);
    	QZone.ShareParams qzone = new QZone.ShareParams();
		qzone.setTitle(title);
		qzone.setTitleUrl(url);
		qzone.setText(content);
		qzone.setSite(url);
		qzone.setSiteUrl(url);
		if(TextUtils.isEmpty(thumbnail))
			qzone.setImageData(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
		else
			qzone.setImageUrl(thumbnail);
//		ShareSDK.getPlatform(QZone.NAME).setPlatformActionListener(new PlatformActionListener() {
//			@Override
//			public void onNetError(Platform arg0, int arg1, Throwable arg2) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showShort(context, "分享失败");
//			}
//			
//			@Override
//			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showShort(context, "分享完成");
//			}
//			
//			@Override
//			public void onCancel(Platform arg0, int arg1) {
//				ProgressDialogUtil.dismissProgressDlg();
//				T.showShort(context, "分享取消");
//			}
//		});
//		ProgressDialogUtil.showProgressDlg(context, "分享中...");
		ShareSDK.getPlatform(QZone.NAME).share(qzone);
    }
    
//    public static void shareSinaWeibo(final Context context, String url, PlatformActionListener paListener) {
//    	ShareSDK.initSDK(context);
//    	SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
//    	sp.setText(url);
//    	Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
//    	weibo.setPlatformActionListener(paListener); // 设置分享事件回调
//    	// 执行图文分享
//    	weibo.share(sp);
//    }
}
