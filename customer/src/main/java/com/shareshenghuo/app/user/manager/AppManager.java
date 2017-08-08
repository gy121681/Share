package com.shareshenghuo.app.user.manager;

import com.shareshenghuo.app.user.app.CityLifeApp;
import com.shareshenghuo.app.user.util.PreferenceUtils;

public class AppManager {
	
	public static final String GUIDE_SHOP_CLASSIFY = "shop_classify";
	public static final String GUIDE_CHOOSE_PROD = "choose_prod";
	public static final String GUIDE_TO_CART = "to_cart";
	public static final String GUIDE_ADD_CART = "add_cart";
	
	public static boolean getShopClassify() {
		return PreferenceUtils.getPrefBoolean(CityLifeApp.applicationContext, GUIDE_SHOP_CLASSIFY, false);
	}
	
	public static void setShopClassify() {
		PreferenceUtils.setPrefBoolean(CityLifeApp.applicationContext, GUIDE_SHOP_CLASSIFY, true);
	}
	
	public static boolean getChooseProd() {
		return PreferenceUtils.getPrefBoolean(CityLifeApp.applicationContext, GUIDE_CHOOSE_PROD, false);
	}

	public static void setChooseProd() {
		PreferenceUtils.setPrefBoolean(CityLifeApp.applicationContext, GUIDE_CHOOSE_PROD, true);
	}
	
	public static boolean getToCart() {
		return PreferenceUtils.getPrefBoolean(CityLifeApp.applicationContext, GUIDE_TO_CART, false);
	}

	public static void setToCart() {
		PreferenceUtils.setPrefBoolean(CityLifeApp.applicationContext, GUIDE_TO_CART, true);
	}
	
	public static boolean getAddCart() {
		return PreferenceUtils.getPrefBoolean(CityLifeApp.applicationContext, GUIDE_ADD_CART, false);
	}

	public static void setAddCart() {
		PreferenceUtils.setPrefBoolean(CityLifeApp.applicationContext, GUIDE_ADD_CART, true);
	}
}
