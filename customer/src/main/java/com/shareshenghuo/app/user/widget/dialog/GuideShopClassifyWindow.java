package com.shareshenghuo.app.user.widget.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

import com.shareshenghuo.app.user.R;
import com.shareshenghuo.app.user.manager.AppManager;
import com.shareshenghuo.app.user.widget.dialog.CommonDialog;

public class GuideShopClassifyWindow extends CommonDialog {
	
	private String guideType;

	public GuideShopClassifyWindow(Context context, String guideType) {
		super(context, R.layout.dlg_guide, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.guideType = guideType;
	}

	@Override
	public void initDlgView() {
		if(AppManager.GUIDE_SHOP_CLASSIFY.equals(guideType))
			setImageResource(R.id.ivGuideBg, R.drawable.guide_shop_classify);
		else if(AppManager.GUIDE_CHOOSE_PROD.equals(guideType))
			setImageResource(R.id.ivGuideBg, R.drawable.guide_choose_prod);
		else if(AppManager.GUIDE_ADD_CART.equals(guideType))
			setImageResource(R.id.ivGuideBg, R.drawable.guide_add_cart);
		else if(AppManager.GUIDE_TO_CART.equals(guideType))
			setImageResource(R.id.ivGuideBg, R.drawable.guide_to_cart);
		
		getView(R.id.ivGuideBg).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				odismiss();
			}
		});
	}


	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		
	}
}
