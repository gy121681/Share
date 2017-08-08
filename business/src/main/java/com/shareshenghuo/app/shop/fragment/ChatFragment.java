package com.shareshenghuo.app.shop.fragment;

import android.view.View;
import android.view.View.OnClickListener;

import com.easemob.easeui.ui.EaseChatFragment;
import com.shareshenghuo.app.shop.R;

public class ChatFragment extends EaseChatFragment {
	
	@Override
	protected void setUpView() {
		super.setUpView();
		titleBar.setTitle("顾客咨询");
		titleBar.setBackgroundColor(getResources().getColor(R.color.black));
		titleBar.setRightLayoutVisibility(View.GONE);
		titleBar.setRightLayoutClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(toChatUsername.startsWith("b")) {
					
				}
			}
		});
	}
	
	@Override
	protected void registerExtendMenuItem() {
		for(int i = 0; i < 2; i++){
            inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
        }
	}
}
