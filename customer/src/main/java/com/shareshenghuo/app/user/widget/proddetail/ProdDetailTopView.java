package com.shareshenghuo.app.user.widget.proddetail;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shareshenghuo.app.user.CommentListActivity;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.ShopDetailActivity;
import com.shareshenghuo.app.user.adapter.CommentListAdapter;
import com.shareshenghuo.app.user.manager.ImageLoadManager;
import com.shareshenghuo.app.user.manager.UserInfoManager;
import com.shareshenghuo.app.user.network.bean.Banner;
import com.shareshenghuo.app.user.network.bean.ProdInfo;
import com.shareshenghuo.app.user.networkapi.Api;
import com.shareshenghuo.app.user.util.Utility;
import com.shareshenghuo.app.user.widget.BannerViewPager;
import com.shareshenghuo.app.user.widget.dialog.ShareMenuWindow;
import com.shareshenghuo.app.user.widget.snapscrollview.McoyScrollView;
import com.shareshenghuo.app.user.widget.snapscrollview.McoySnapPageLayout;

public class ProdDetailTopView implements McoySnapPageLayout.McoySnapPage, OnClickListener {

	private Context context;
	
	private View rootView = null;
	private McoyScrollView mcoyScrollView = null;
	private BannerViewPager banner;
	private ListView lvComment;
	
	private ProdInfo prodInfo;
	
	public ProdDetailTopView (Context context, View rootView, ProdInfo prodInfo) {
		this.context = context;
		this.rootView = rootView;
		this.prodInfo = prodInfo;
		initView();
		initListener();
		updateView();
	}
	
	@Override
	public View getRootView() {
		return rootView;
	}

	@Override
	public boolean isAtTop() {
		return true;
	}

	@Override
	public boolean isAtBottom() {
        int scrollY = mcoyScrollView.getScrollY();
        int height = mcoyScrollView.getHeight();
        int scrollViewMeasuredHeight = mcoyScrollView.getChildAt(0).getMeasuredHeight();

        if ((scrollY + height) >= scrollViewMeasuredHeight) {
            return true;
        }
        return false;
	}
	
	public void initView() {
		mcoyScrollView = (McoyScrollView) this.rootView.findViewById(R.id.product_scrollview);
		banner = (BannerViewPager)rootView.findViewById(R.id.bannerProd);
		lvComment = (ListView)rootView.findViewById(R.id.lvProdComment);
	}
	
	public void initListener() {
		getView(R.id.btnProdShop).setOnClickListener(this);
		rootView.findViewById(R.id.btnBack).setOnClickListener(this);
		rootView.findViewById(R.id.btnProdMoreComment).setOnClickListener(this);
		rootView.findViewById(R.id.ivShare).setOnClickListener(this);
	}
	
	public void updateView() {
		if(!TextUtils.isEmpty(prodInfo.product_photo)) {
			List<Banner> list = new ArrayList<Banner>();
			String[] urls = prodInfo.product_photo.split(",");
			for(String url : urls) {
				Banner item = new Banner();
				item.banner_imgurl = url;
				list.add(item);
			}
			banner.createView(list);
		}
		
		setText(R.id.tvProdDetailName, prodInfo.product_name);
		setText(R.id.tvProdDetailNowPrice, "¥"+prodInfo.default_new_price);
		setText(R.id.tvProdDetailOldPrice, "原价：¥"+prodInfo.default_old_price);
		setText(R.id.tvProdDetailSaleCount, "已售"+prodInfo.order_count+"件/剩"+prodInfo.all_product_repertory+"件");
		setText(R.id.tvShopName, prodInfo.shop_name);
		ImageView ivLogo = getView(R.id.ivShopLogo);
		ImageLoadManager.getInstance(context).displayImage(prodInfo.shop_logo, ivLogo);
		
		lvComment.setAdapter(new CommentListAdapter(context, prodInfo.product_comment_list));
		Utility.setListViewHeightBasedOnChildren(lvComment);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnProdMoreComment:
			Intent comment = new Intent(context, CommentListActivity.class);
			comment.putExtra("prodId", prodInfo.id);
			context.startActivity(comment);
			break;

		case R.id.btnBack:
			((Activity) context).finish();
			break;
			
		case R.id.ivShare:
			ShareMenuWindow window = new ShareMenuWindow(context);
			window.url = Api.URL_SHARE_PROD + "?product_id=" + prodInfo.id + "&user_id=" + UserInfoManager.getUserId(context);
			window.title = prodInfo.product_name;
			window.content = prodInfo.produce_desc;
			window.thumbnail = Api.HOST+prodInfo.thum_photo;
			window.showAtBottom();
			break;
			
		case R.id.btnProdShop:
			Intent it = new Intent(context, ShopDetailActivity.class);
			it.putExtra("shopId", prodInfo.shop_id);
			context.startActivity(it);
			break;
		}
	}
	
	public void startScroll() {
		banner.startRoll();
	}
	
	public void stopScroll() {
		banner.stopRoll();
	}
	
	public void setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
	}
	
	public <T extends View>T getView(int viewId) {
		View view  = rootView.findViewById(viewId);
		return (T)view;
	}
}
