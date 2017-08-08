package com.shareshenghuo.app.user;

import android.os.Bundle;
import android.widget.LinearLayout;
import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.network.bean.ShopInfo;
import com.shareshenghuo.app.user.util.ViewUtil;

/**
 * @author hang
 * 商家信息
 */
public class ShopInfoActivity extends BaseTopActivity {
	
	private ShopInfo shopInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_info);
		init();
	}
	
	public void init() {
		initTopBar("商家信息");
		
		shopInfo = (ShopInfo) getIntent().getSerializableExtra("shopInfo");
		setText(R.id.tvShopName, shopInfo.shop_name);
		setText(R.id.tvShopTel, shopInfo.mobile);
		setText(R.id.tvShopType, shopInfo.shop_type_name);
		setText(R.id.tvShopSnippet, shopInfo.address);
		setText(R.id.tvShopDesc, shopInfo.introduction);
		
		ViewUtil.setPhotoList(this, (LinearLayout) getView(R.id.llShopPhoto), shopInfo.shop_photo);
	}
}
